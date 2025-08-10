/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
/*     */ import com.google.common.util.concurrent.internal.InternalFutures;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class AbstractCatchingFuture<V, X extends Throwable, F, T>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */   implements Runnable
/*     */ {
/*     */   @CheckForNull
/*     */   ListenableFuture<? extends V> inputFuture;
/*     */   @CheckForNull
/*     */   Class<X> exceptionType;
/*     */   @CheckForNull
/*     */   F fallback;
/*     */   
/*     */   static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
/*  46 */     CatchingFuture<V, X> future = new CatchingFuture<>(input, exceptionType, fallback);
/*  47 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  48 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
/*  56 */     AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture<>(input, exceptionType, fallback);
/*  57 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  58 */     return future;
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
/*     */   AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback) {
/*  71 */     this.inputFuture = (ListenableFuture<? extends V>)Preconditions.checkNotNull(inputFuture);
/*  72 */     this.exceptionType = (Class<X>)Preconditions.checkNotNull(exceptionType);
/*  73 */     this.fallback = (F)Preconditions.checkNotNull(fallback);
/*     */   }
/*     */   
/*     */   public final void run() {
/*     */     T fallbackResult;
/*  78 */     ListenableFuture<? extends V> localInputFuture = this.inputFuture;
/*  79 */     Class<X> localExceptionType = this.exceptionType;
/*  80 */     F localFallback = this.fallback;
/*  81 */     if ((((localInputFuture == null) ? 1 : 0) | ((localExceptionType == null) ? 1 : 0) | ((localFallback == null) ? 1 : 0)) != 0 || 
/*     */       
/*  83 */       isCancelled()) {
/*     */       return;
/*     */     }
/*  86 */     this.inputFuture = null;
/*     */ 
/*     */     
/*  89 */     V sourceResult = null;
/*  90 */     Throwable throwable = null;
/*     */     try {
/*  92 */       if (localInputFuture instanceof InternalFutureFailureAccess)
/*     */       {
/*  94 */         throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)localInputFuture);
/*     */       }
/*     */       
/*  97 */       if (throwable == null) {
/*  98 */         sourceResult = Futures.getDone((Future)localInputFuture);
/*     */       }
/* 100 */     } catch (ExecutionException e) {
/* 101 */       throwable = e.getCause();
/* 102 */       if (throwable == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 108 */         throwable = new NullPointerException("Future type " + localInputFuture.getClass() + " threw " + e.getClass() + " without a cause");
/*     */       }
/*     */     }
/* 111 */     catch (Throwable t) {
/* 112 */       throwable = t;
/*     */     } 
/*     */     
/* 115 */     if (throwable == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       set(NullnessCasts.uncheckedCastNullableTToT(sourceResult));
/*     */       
/*     */       return;
/*     */     } 
/* 124 */     if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
/* 125 */       setFuture(localInputFuture);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 131 */     Throwable throwable1 = throwable;
/*     */     
/*     */     try {
/* 134 */       fallbackResult = doFallback(localFallback, (X)throwable1);
/* 135 */     } catch (Throwable t) {
/* 136 */       Platform.restoreInterruptIfIsInterruptedException(t);
/* 137 */       setException(t);
/*     */       return;
/*     */     } finally {
/* 140 */       this.exceptionType = null;
/* 141 */       this.fallback = null;
/*     */     } 
/*     */     
/* 144 */     setResult(fallbackResult);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected String pendingToString() {
/* 150 */     ListenableFuture<? extends V> localInputFuture = this.inputFuture;
/* 151 */     Class<X> localExceptionType = this.exceptionType;
/* 152 */     F localFallback = this.fallback;
/* 153 */     String superString = super.pendingToString();
/* 154 */     String resultString = "";
/* 155 */     if (localInputFuture != null) {
/* 156 */       resultString = "inputFuture=[" + localInputFuture + "], ";
/*     */     }
/* 158 */     if (localExceptionType != null && localFallback != null) {
/* 159 */       return resultString + "exceptionType=[" + localExceptionType + "], fallback=[" + localFallback + "]";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     if (superString != null) {
/* 166 */       return resultString + superString;
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @ForOverride
/*     */   abstract T doFallback(F paramF, X paramX) throws Exception;
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   abstract void setResult(@ParametricNullness T paramT);
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/* 182 */     maybePropagateCancellationTo(this.inputFuture);
/* 183 */     this.inputFuture = null;
/* 184 */     this.exceptionType = null;
/* 185 */     this.fallback = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class AsyncCatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>>
/*     */   {
/*     */     AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
/* 199 */       super(input, exceptionType, fallback);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause) throws Exception {
/* 205 */       ListenableFuture<? extends V> replacement = fallback.apply(cause);
/* 206 */       Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", fallback);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 211 */       return replacement;
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(ListenableFuture<? extends V> result) {
/* 216 */       setFuture(result);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class CatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V>
/*     */   {
/*     */     CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
/* 230 */       super(input, exceptionType, fallback);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     V doFallback(Function<? super X, ? extends V> fallback, X cause) throws Exception {
/* 236 */       return (V)fallback.apply(cause);
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(@ParametricNullness V result) {
/* 241 */       set(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AbstractCatchingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */