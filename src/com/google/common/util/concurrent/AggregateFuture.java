/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.logging.Level;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class AggregateFuture<InputT, OutputT>
/*     */   extends AggregateFutureState<OutputT>
/*     */ {
/*  46 */   private static final LazyLogger logger = new LazyLogger(AggregateFuture.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean allMustSucceed;
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean collectsValues;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AggregateFuture(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
/*  67 */     super(futures.size());
/*  68 */     this.futures = (ImmutableCollection<? extends ListenableFuture<? extends InputT>>)Preconditions.checkNotNull(futures);
/*  69 */     this.allMustSucceed = allMustSucceed;
/*  70 */     this.collectsValues = collectsValues;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/*  75 */     super.afterDone();
/*     */     
/*  77 */     ImmutableCollection<? extends Future<?>> localFutures = (ImmutableCollection)this.futures;
/*  78 */     releaseResources(ReleaseResourcesReason.OUTPUT_FUTURE_DONE);
/*     */     
/*  80 */     if ((isCancelled() & ((localFutures != null) ? 1 : 0)) != 0) {
/*  81 */       boolean wasInterrupted = wasInterrupted();
/*  82 */       for (UnmodifiableIterator<Future> unmodifiableIterator = localFutures.iterator(); unmodifiableIterator.hasNext(); ) { Future<?> future = unmodifiableIterator.next();
/*  83 */         future.cancel(wasInterrupted); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected final String pendingToString() {
/*  95 */     ImmutableCollection<? extends Future<?>> localFutures = (ImmutableCollection)this.futures;
/*  96 */     if (localFutures != null) {
/*  97 */       return "futures=" + localFutures;
/*     */     }
/*  99 */     return super.pendingToString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void init() {
/* 115 */     Objects.requireNonNull(this.futures);
/*     */ 
/*     */     
/* 118 */     if (this.futures.isEmpty()) {
/* 119 */       handleAllCompleted();
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 126 */     if (this.allMustSucceed) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 136 */       int i = 0;
/* 137 */       for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { ListenableFuture<? extends InputT> future = unmodifiableIterator.next();
/* 138 */         int index = i++;
/* 139 */         future.addListener(() -> {
/*     */               try {
/*     */                 if (future.isCancelled()) {
/*     */                   this.futures = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   cancel(false);
/*     */                 } else {
/*     */                   collectValueFromNonCancelledFuture(index, future);
/*     */                 } 
/*     */               } finally {
/*     */                 decrementCountAndMaybeComplete((ImmutableCollection<? extends Future<? extends InputT>>)null);
/*     */               } 
/* 159 */             }MoreExecutors.directExecutor());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */          }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 179 */       ImmutableCollection<? extends ListenableFuture<? extends InputT>> immutableCollection = this.collectsValues ? this.futures : null;
/* 180 */       Runnable listener = () -> decrementCountAndMaybeComplete(localFutures);
/* 181 */       for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { ListenableFuture<? extends InputT> future = unmodifiableIterator.next();
/* 182 */         future.addListener(listener, MoreExecutors.directExecutor()); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleException(Throwable throwable) {
/* 194 */     Preconditions.checkNotNull(throwable);
/*     */     
/* 196 */     if (this.allMustSucceed) {
/*     */ 
/*     */       
/* 199 */       boolean completedWithFailure = setException(throwable);
/* 200 */       if (!completedWithFailure) {
/*     */ 
/*     */         
/* 203 */         boolean firstTimeSeeingThisException = addCausalChain(getOrInitSeenExceptions(), throwable);
/* 204 */         if (firstTimeSeeingThisException) {
/* 205 */           log(throwable);
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 215 */     if (throwable instanceof Error)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 223 */       log(throwable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void log(Throwable throwable) {
/* 231 */     String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
/* 232 */     logger.get().log(Level.SEVERE, message, throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   final void addInitialException(Set<Throwable> seen) {
/* 237 */     Preconditions.checkNotNull(seen);
/* 238 */     if (!isCancelled())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 256 */       boolean bool = addCausalChain(seen, Objects.<Throwable>requireNonNull(tryInternalFastPathGetFailure()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void collectValueFromNonCancelledFuture(int index, Future<? extends InputT> future) {
/*     */     try {
/* 267 */       collectOneValue(index, Futures.getDone((Future)future));
/* 268 */     } catch (ExecutionException e) {
/* 269 */       handleException(e.getCause());
/* 270 */     } catch (Throwable t) {
/* 271 */       handleException(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decrementCountAndMaybeComplete(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
/* 279 */     int newRemaining = decrementRemainingAndGet();
/* 280 */     Preconditions.checkState((newRemaining >= 0), "Less than 0 remaining futures");
/* 281 */     if (newRemaining == 0) {
/* 282 */       processCompleted(futuresIfNeedToCollectAtCompletion);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processCompleted(@CheckForNull ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
/* 290 */     if (futuresIfNeedToCollectAtCompletion != null) {
/* 291 */       int i = 0;
/* 292 */       for (UnmodifiableIterator<Future<? extends InputT>> unmodifiableIterator = futuresIfNeedToCollectAtCompletion.iterator(); unmodifiableIterator.hasNext(); ) { Future<? extends InputT> future = unmodifiableIterator.next();
/* 293 */         if (!future.isCancelled()) {
/* 294 */           collectValueFromNonCancelledFuture(i, future);
/*     */         }
/* 296 */         i++; }
/*     */     
/*     */     } 
/* 299 */     clearSeenExceptions();
/* 300 */     handleAllCompleted();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     releaseResources(ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   @OverridingMethodsMustInvokeSuper
/*     */   void releaseResources(ReleaseResourcesReason reason) {
/* 324 */     Preconditions.checkNotNull(reason);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     this.futures = null;
/*     */   }
/*     */   
/*     */   enum ReleaseResourcesReason {
/* 335 */     OUTPUT_FUTURE_DONE,
/* 336 */     ALL_INPUT_FUTURES_PROCESSED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean addCausalChain(Set<Throwable> seen, Throwable param) {
/* 350 */     Throwable t = param;
/*     */     
/* 352 */     for (; t != null; t = t.getCause()) {
/* 353 */       boolean firstTimeSeen = seen.add(t);
/* 354 */       if (!firstTimeSeen)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 361 */         return false;
/*     */       }
/*     */     } 
/* 364 */     return true;
/*     */   }
/*     */   
/*     */   abstract void collectOneValue(int paramInt, @ParametricNullness InputT paramInputT);
/*     */   
/*     */   abstract void handleAllCompleted();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AggregateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */