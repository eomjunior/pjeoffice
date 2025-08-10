/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Functions;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.FluentIterable;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.DoNotMock;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.Closeable;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.logging.Level;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @DoNotMock("Use ClosingFuture.from(Futures.immediate*Future)")
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @J2ktIncompatible
/*      */ public final class ClosingFuture<V>
/*      */ {
/*  198 */   private static final LazyLogger logger = new LazyLogger(ClosingFuture.class);
/*      */ 
/*      */   
/*      */   public static final class DeferredCloser
/*      */   {
/*      */     @RetainedWith
/*      */     private final ClosingFuture.CloseableList list;
/*      */ 
/*      */     
/*      */     DeferredCloser(ClosingFuture.CloseableList list) {
/*  208 */       this.list = list;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     @CanIgnoreReturnValue
/*      */     public <C extends AutoCloseable> C eventuallyClose(@ParametricNullness C closeable, Executor closingExecutor) {
/*  238 */       Preconditions.checkNotNull(closingExecutor);
/*  239 */       if (closeable != null) {
/*  240 */         this.list.add((AutoCloseable)closeable, closingExecutor);
/*      */       }
/*  242 */       return closeable;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class ValueAndCloser<V>
/*      */   {
/*      */     private final ClosingFuture<? extends V> closingFuture;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ValueAndCloser(ClosingFuture<? extends V> closingFuture) {
/*  335 */       this.closingFuture = (ClosingFuture<? extends V>)Preconditions.checkNotNull(closingFuture);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V get() throws ExecutionException {
/*  350 */       return Futures.getDone(this.closingFuture.future);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void closeAsync() {
/*  364 */       this.closingFuture.close();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ClosingFuture<V> submit(ClosingCallable<V> callable, Executor executor) {
/*  390 */     return new ClosingFuture<>(callable, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ClosingFuture<V> submitAsync(AsyncClosingCallable<V> callable, Executor executor) {
/*  402 */     return new ClosingFuture<>(callable, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ClosingFuture<V> from(ListenableFuture<V> future) {
/*  413 */     return new ClosingFuture<>(future);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <C extends AutoCloseable> ClosingFuture<C> eventuallyClosing(ListenableFuture<C> future, final Executor closingExecutor) {
/*  441 */     Preconditions.checkNotNull(closingExecutor);
/*  442 */     final ClosingFuture<C> closingFuture = new ClosingFuture<>(Futures.nonCancellationPropagating(future));
/*  443 */     Futures.addCallback(future, (FutureCallback)new FutureCallback<AutoCloseable>()
/*      */         {
/*      */           
/*      */           public void onSuccess(@CheckForNull AutoCloseable result)
/*      */           {
/*  448 */             closingFuture.closeables.closer.eventuallyClose(result, closingExecutor);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public void onFailure(Throwable t) {}
/*  454 */         }MoreExecutors.directExecutor());
/*  455 */     return closingFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Combiner whenAllComplete(Iterable<? extends ClosingFuture<?>> futures) {
/*  465 */     return new Combiner(false, futures);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Combiner whenAllComplete(ClosingFuture<?> future1, ClosingFuture<?>... moreFutures) {
/*  476 */     return whenAllComplete(Lists.asList(future1, (Object[])moreFutures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Combiner whenAllSucceed(Iterable<? extends ClosingFuture<?>> futures) {
/*  487 */     return new Combiner(true, futures);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V1, V2> Combiner2<V1, V2> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2) {
/*  502 */     return new Combiner2<>(future1, future2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V1, V2, V3> Combiner3<V1, V2, V3> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3) {
/*  519 */     return new Combiner3<>(future1, future2, future3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V1, V2, V3, V4> Combiner4<V1, V2, V3, V4> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4) {
/*  542 */     return new Combiner4<>(future1, future2, future3, future4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V1, V2, V3, V4, V5> Combiner5<V1, V2, V3, V4, V5> whenAllSucceed(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4, ClosingFuture<V5> future5) {
/*  567 */     return new Combiner5<>(future1, future2, future3, future4, future5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Combiner whenAllSucceed(ClosingFuture<?> future1, ClosingFuture<?> future2, ClosingFuture<?> future3, ClosingFuture<?> future4, ClosingFuture<?> future5, ClosingFuture<?> future6, ClosingFuture<?>... moreFutures) {
/*  585 */     return whenAllSucceed(
/*  586 */         (Iterable<? extends ClosingFuture<?>>)FluentIterable.of(future1, (Object[])new ClosingFuture[] { future2, future3, future4, future5, future6
/*  587 */           }).append((Object[])moreFutures));
/*      */   }
/*      */   
/*  590 */   private final AtomicReference<State> state = new AtomicReference<>(State.OPEN);
/*  591 */   private final CloseableList closeables = new CloseableList();
/*      */   private final FluentFuture<V> future;
/*      */   
/*      */   private ClosingFuture(ListenableFuture<V> future) {
/*  595 */     this.future = FluentFuture.from(future);
/*      */   }
/*      */   
/*      */   private ClosingFuture(final ClosingCallable<V> callable, Executor executor) {
/*  599 */     Preconditions.checkNotNull(callable);
/*      */     
/*  601 */     TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(new Callable<V>()
/*      */         {
/*      */           @ParametricNullness
/*      */           public V call() throws Exception
/*      */           {
/*  606 */             return callable.call(ClosingFuture.this.closeables.closer);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/*  611 */             return callable.toString();
/*      */           }
/*      */         });
/*  614 */     executor.execute(task);
/*  615 */     this.future = task;
/*      */   }
/*      */   
/*      */   private ClosingFuture(final AsyncClosingCallable<V> callable, Executor executor) {
/*  619 */     Preconditions.checkNotNull(callable);
/*      */     
/*  621 */     TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(new AsyncCallable<V>()
/*      */         {
/*      */           public ListenableFuture<V> call() throws Exception
/*      */           {
/*  625 */             ClosingFuture.CloseableList newCloseables = new ClosingFuture.CloseableList();
/*      */             try {
/*  627 */               ClosingFuture<V> closingFuture = callable.call(newCloseables.closer);
/*  628 */               closingFuture.becomeSubsumedInto(ClosingFuture.this.closeables);
/*  629 */               return closingFuture.future;
/*      */             } finally {
/*  631 */               ClosingFuture.this.closeables.add(newCloseables, MoreExecutors.directExecutor());
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/*  637 */             return callable.toString();
/*      */           }
/*      */         });
/*  640 */     executor.execute(task);
/*  641 */     this.future = task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ListenableFuture<?> statusFuture() {
/*  656 */     return Futures.nonCancellationPropagating(this.future.transform(Functions.constant(null), MoreExecutors.directExecutor()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <U> ClosingFuture<U> transform(final ClosingFunction<? super V, U> function, Executor executor) {
/*  694 */     Preconditions.checkNotNull(function);
/*  695 */     AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>()
/*      */       {
/*      */         public ListenableFuture<U> apply(V input) throws Exception
/*      */         {
/*  699 */           return ClosingFuture.this.closeables.applyClosingFunction(function, input);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  704 */           return function.toString();
/*      */         }
/*      */       };
/*      */     
/*  708 */     return derive(this.future.transformAsync(applyFunction, executor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <U> ClosingFuture<U> transformAsync(final AsyncClosingFunction<? super V, U> function, Executor executor) {
/*  788 */     Preconditions.checkNotNull(function);
/*  789 */     AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>()
/*      */       {
/*      */         public ListenableFuture<U> apply(V input) throws Exception
/*      */         {
/*  793 */           return ClosingFuture.this.closeables.applyAsyncClosingFunction(function, input);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  798 */           return function.toString();
/*      */         }
/*      */       };
/*  801 */     return derive(this.future.transformAsync(applyFunction, executor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, U> AsyncClosingFunction<V, U> withoutCloser(final AsyncFunction<V, U> function) {
/*  832 */     Preconditions.checkNotNull(function);
/*  833 */     return new AsyncClosingFunction<V, U>()
/*      */       {
/*      */         public ClosingFuture<U> apply(ClosingFuture.DeferredCloser closer, V input) throws Exception {
/*  836 */           return ClosingFuture.from(function.apply(input));
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <X extends Throwable> ClosingFuture<V> catching(Class<X> exceptionType, ClosingFunction<? super X, ? extends V> fallback, Executor executor) {
/*  884 */     return catchingMoreGeneric(exceptionType, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private <X extends Throwable, W extends V> ClosingFuture<V> catchingMoreGeneric(Class<X> exceptionType, final ClosingFunction<? super X, W> fallback, Executor executor) {
/*  890 */     Preconditions.checkNotNull(fallback);
/*  891 */     AsyncFunction<X, W> applyFallback = new AsyncFunction<X, W>()
/*      */       {
/*      */         public ListenableFuture<W> apply(X exception) throws Exception
/*      */         {
/*  895 */           return ClosingFuture.this.closeables.applyClosingFunction(fallback, exception);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  900 */           return fallback.toString();
/*      */         }
/*      */       };
/*      */     
/*  904 */     return derive(this.future.catchingAsync(exceptionType, applyFallback, executor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <X extends Throwable> ClosingFuture<V> catchingAsync(Class<X> exceptionType, AsyncClosingFunction<? super X, ? extends V> fallback, Executor executor) {
/*  981 */     return catchingAsyncMoreGeneric(exceptionType, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <X extends Throwable, W extends V> ClosingFuture<V> catchingAsyncMoreGeneric(Class<X> exceptionType, final AsyncClosingFunction<? super X, W> fallback, Executor executor) {
/*  989 */     Preconditions.checkNotNull(fallback);
/*  990 */     AsyncFunction<X, W> asyncFunction = new AsyncFunction<X, W>()
/*      */       {
/*      */         public ListenableFuture<W> apply(X exception) throws Exception
/*      */         {
/*  994 */           return ClosingFuture.this.closeables.applyAsyncClosingFunction(fallback, exception);
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  999 */           return fallback.toString();
/*      */         }
/*      */       };
/* 1002 */     return derive(this.future.catchingAsync(exceptionType, asyncFunction, executor));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FluentFuture<V> finishToFuture() {
/* 1022 */     if (compareAndUpdateState(State.OPEN, State.WILL_CLOSE)) {
/* 1023 */       logger.get().log(Level.FINER, "will close {0}", this);
/* 1024 */       this.future.addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/* 1028 */               ClosingFuture.this.checkAndUpdateState(ClosingFuture.State.WILL_CLOSE, ClosingFuture.State.CLOSING);
/* 1029 */               ClosingFuture.this.close();
/* 1030 */               ClosingFuture.this.checkAndUpdateState(ClosingFuture.State.CLOSING, ClosingFuture.State.CLOSED);
/*      */             }
/* 1033 */           }MoreExecutors.directExecutor());
/*      */     } else {
/* 1035 */       switch ((State)this.state.get()) {
/*      */         case SUBSUMED:
/* 1037 */           throw new IllegalStateException("Cannot call finishToFuture() after deriving another step");
/*      */ 
/*      */         
/*      */         case WILL_CREATE_VALUE_AND_CLOSER:
/* 1041 */           throw new IllegalStateException("Cannot call finishToFuture() after calling finishToValueAndCloser()");
/*      */ 
/*      */         
/*      */         case WILL_CLOSE:
/*      */         case CLOSING:
/*      */         case CLOSED:
/* 1047 */           throw new IllegalStateException("Cannot call finishToFuture() twice");
/*      */         
/*      */         case OPEN:
/* 1050 */           throw new AssertionError();
/*      */       } 
/*      */     } 
/* 1053 */     return this.future;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void finishToValueAndCloser(final ValueAndCloserConsumer<? super V> consumer, Executor executor) {
/* 1069 */     Preconditions.checkNotNull(consumer);
/* 1070 */     if (!compareAndUpdateState(State.OPEN, State.WILL_CREATE_VALUE_AND_CLOSER)) {
/* 1071 */       switch ((State)this.state.get()) {
/*      */         case SUBSUMED:
/* 1073 */           throw new IllegalStateException("Cannot call finishToValueAndCloser() after deriving another step");
/*      */ 
/*      */         
/*      */         case WILL_CLOSE:
/*      */         case CLOSING:
/*      */         case CLOSED:
/* 1079 */           throw new IllegalStateException("Cannot call finishToValueAndCloser() after calling finishToFuture()");
/*      */ 
/*      */         
/*      */         case WILL_CREATE_VALUE_AND_CLOSER:
/* 1083 */           throw new IllegalStateException("Cannot call finishToValueAndCloser() twice");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1088 */       throw new AssertionError(this.state);
/*      */     } 
/* 1090 */     this.future.addListener(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/* 1094 */             ClosingFuture.provideValueAndCloser(consumer, ClosingFuture.this);
/*      */           }
/*      */         }executor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <C, V extends C> void provideValueAndCloser(ValueAndCloserConsumer<C> consumer, ClosingFuture<V> closingFuture) {
/* 1102 */     consumer.accept(new ValueAndCloser<>(closingFuture));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 1123 */     logger.get().log(Level.FINER, "cancelling {0}", this);
/* 1124 */     boolean cancelled = this.future.cancel(mayInterruptIfRunning);
/* 1125 */     if (cancelled) {
/* 1126 */       close();
/*      */     }
/* 1128 */     return cancelled;
/*      */   }
/*      */   
/*      */   private void close() {
/* 1132 */     logger.get().log(Level.FINER, "closing {0}", this);
/* 1133 */     this.closeables.close();
/*      */   }
/*      */   
/*      */   private <U> ClosingFuture<U> derive(FluentFuture<U> future) {
/* 1137 */     ClosingFuture<U> derived = new ClosingFuture(future);
/* 1138 */     becomeSubsumedInto(derived.closeables);
/* 1139 */     return derived;
/*      */   }
/*      */   
/*      */   private void becomeSubsumedInto(CloseableList otherCloseables) {
/* 1143 */     checkAndUpdateState(State.OPEN, State.SUBSUMED);
/* 1144 */     otherCloseables.add(this.closeables, MoreExecutors.directExecutor());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Peeker
/*      */   {
/*      */     private final ImmutableList<ClosingFuture<?>> futures;
/*      */ 
/*      */     
/*      */     private volatile boolean beingCalled;
/*      */ 
/*      */     
/*      */     private Peeker(ImmutableList<ClosingFuture<?>> futures) {
/* 1158 */       this.futures = (ImmutableList<ClosingFuture<?>>)Preconditions.checkNotNull(futures);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public final <D> D getDone(ClosingFuture<D> closingFuture) throws ExecutionException {
/* 1175 */       Preconditions.checkState(this.beingCalled);
/* 1176 */       Preconditions.checkArgument(this.futures.contains(closingFuture));
/* 1177 */       return Futures.getDone(closingFuture.future);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     private <V> V call(ClosingFuture.Combiner.CombiningCallable<V> combiner, ClosingFuture.CloseableList closeables) throws Exception {
/* 1183 */       this.beingCalled = true;
/* 1184 */       ClosingFuture.CloseableList newCloseables = new ClosingFuture.CloseableList();
/*      */       try {
/* 1186 */         return combiner.call(newCloseables.closer, this);
/*      */       } finally {
/* 1188 */         closeables.add(newCloseables, MoreExecutors.directExecutor());
/* 1189 */         this.beingCalled = false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private <V> FluentFuture<V> callAsync(ClosingFuture.Combiner.AsyncCombiningCallable<V> combiner, ClosingFuture.CloseableList closeables) throws Exception {
/* 1195 */       this.beingCalled = true;
/* 1196 */       ClosingFuture.CloseableList newCloseables = new ClosingFuture.CloseableList();
/*      */       try {
/* 1198 */         ClosingFuture<V> closingFuture = combiner.call(newCloseables.closer, this);
/* 1199 */         closingFuture.becomeSubsumedInto(closeables);
/* 1200 */         return closingFuture.future;
/*      */       } finally {
/* 1202 */         closeables.add(newCloseables, MoreExecutors.directExecutor());
/* 1203 */         this.beingCalled = false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @DoNotMock("Use ClosingFuture.whenAllSucceed() or .whenAllComplete() instead.")
/*      */   public static class Combiner
/*      */   {
/* 1236 */     private final ClosingFuture.CloseableList closeables = new ClosingFuture.CloseableList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean allMustSucceed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final ImmutableList<ClosingFuture<?>> inputs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner(boolean allMustSucceed, Iterable<? extends ClosingFuture<?>> inputs) {
/* 1283 */       this.allMustSucceed = allMustSucceed;
/* 1284 */       this.inputs = ImmutableList.copyOf(inputs);
/* 1285 */       for (ClosingFuture<?> input : inputs) {
/* 1286 */         input.becomeSubsumedInto(this.closeables);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <V> ClosingFuture<V> call(final CombiningCallable<V> combiningCallable, Executor executor) {
/* 1306 */       Callable<V> callable = new Callable<V>()
/*      */         {
/*      */           @ParametricNullness
/*      */           public V call() throws Exception
/*      */           {
/* 1311 */             return (new ClosingFuture.Peeker(ClosingFuture.Combiner.this.inputs)).call(combiningCallable, ClosingFuture.Combiner.this.closeables);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/* 1316 */             return combiningCallable.toString();
/*      */           }
/*      */         };
/* 1319 */       ClosingFuture<V> derived = new ClosingFuture<>(futureCombiner().call(callable, executor));
/* 1320 */       derived.closeables.add(this.closeables, MoreExecutors.directExecutor());
/* 1321 */       return derived;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <V> ClosingFuture<V> callAsync(final AsyncCombiningCallable<V> combiningCallable, Executor executor) {
/* 1362 */       AsyncCallable<V> asyncCallable = new AsyncCallable<V>()
/*      */         {
/*      */           public ListenableFuture<V> call() throws Exception
/*      */           {
/* 1366 */             return (new ClosingFuture.Peeker(ClosingFuture.Combiner.this.inputs)).callAsync(combiningCallable, ClosingFuture.Combiner.this.closeables);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/* 1371 */             return combiningCallable.toString();
/*      */           }
/*      */         };
/*      */       
/* 1375 */       ClosingFuture<V> derived = new ClosingFuture<>(futureCombiner().callAsync(asyncCallable, executor));
/* 1376 */       derived.closeables.add(this.closeables, MoreExecutors.directExecutor());
/* 1377 */       return derived;
/*      */     }
/*      */     
/*      */     private Futures.FutureCombiner<Object> futureCombiner() {
/* 1381 */       return this.allMustSucceed ? 
/* 1382 */         Futures.<Object>whenAllSucceed((Iterable)inputFutures()) : 
/* 1383 */         Futures.<Object>whenAllComplete((Iterable)inputFutures());
/*      */     }
/*      */ 
/*      */     
/*      */     private ImmutableList<FluentFuture<?>> inputFutures() {
/* 1388 */       return FluentIterable.from((Iterable)this.inputs)
/* 1389 */         .transform(future -> future.future)
/* 1390 */         .toList();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface CombiningCallable<V>
/*      */     {
/*      */       @ParametricNullness
/*      */       V call(ClosingFuture.DeferredCloser param2DeferredCloser, ClosingFuture.Peeker param2Peeker) throws Exception;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface AsyncCombiningCallable<V>
/*      */     {
/*      */       ClosingFuture<V> call(ClosingFuture.DeferredCloser param2DeferredCloser, ClosingFuture.Peeker param2Peeker) throws Exception;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Combiner2<V1, V2>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner2(ClosingFuture<V1> future1, ClosingFuture<V2> future2) {
/* 1459 */       super(true, (Iterable)ImmutableList.of(future1, future2));
/* 1460 */       this.future1 = future1;
/* 1461 */       this.future2 = future2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction2<V1, V2, U> function, Executor executor) {
/* 1479 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             @ParametricNullness
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1484 */               return (U)function.apply(closer, peeker.getDone(ClosingFuture.Combiner2.this.future1), peeker.getDone(ClosingFuture.Combiner2.this.future2));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1489 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction2<V1, V2, U> function, Executor executor) {
/* 1532 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1536 */               return function.apply(closer, peeker.getDone(ClosingFuture.Combiner2.this.future1), peeker.getDone(ClosingFuture.Combiner2.this.future2));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1541 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface ClosingFunction2<V1, V2, U>
/*      */     {
/*      */       @ParametricNullness
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2) throws Exception;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface AsyncClosingFunction2<V1, V2, U>
/*      */     {
/*      */       ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2) throws Exception;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Combiner3<V1, V2, V3>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V3> future3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner3(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3) {
/* 1629 */       super(true, (Iterable)ImmutableList.of(future1, future2, future3));
/* 1630 */       this.future1 = future1;
/* 1631 */       this.future2 = future2;
/* 1632 */       this.future3 = future3;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction3<V1, V2, V3, U> function, Executor executor) {
/* 1650 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             @ParametricNullness
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1655 */               return (U)function.apply(closer, peeker
/*      */                   
/* 1657 */                   .getDone(ClosingFuture.Combiner3.this.future1), peeker
/* 1658 */                   .getDone(ClosingFuture.Combiner3.this.future2), peeker
/* 1659 */                   .getDone(ClosingFuture.Combiner3.this.future3));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1664 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction3<V1, V2, V3, U> function, Executor executor) {
/* 1707 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1711 */               return function.apply(closer, peeker
/*      */                   
/* 1713 */                   .getDone(ClosingFuture.Combiner3.this.future1), peeker
/* 1714 */                   .getDone(ClosingFuture.Combiner3.this.future2), peeker
/* 1715 */                   .getDone(ClosingFuture.Combiner3.this.future3));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1720 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface ClosingFunction3<V1, V2, V3, U>
/*      */     {
/*      */       @ParametricNullness
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2, @ParametricNullness V3 param2V3) throws Exception;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface AsyncClosingFunction3<V1, V2, V3, U>
/*      */     {
/*      */       ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2, @ParametricNullness V3 param2V3) throws Exception;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Combiner4<V1, V2, V3, V4>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V3> future3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V4> future4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner4(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4) {
/* 1823 */       super(true, (Iterable)ImmutableList.of(future1, future2, future3, future4));
/* 1824 */       this.future1 = future1;
/* 1825 */       this.future2 = future2;
/* 1826 */       this.future3 = future3;
/* 1827 */       this.future4 = future4;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction4<V1, V2, V3, V4, U> function, Executor executor) {
/* 1845 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             @ParametricNullness
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1850 */               return (U)function.apply(closer, peeker
/*      */                   
/* 1852 */                   .getDone(ClosingFuture.Combiner4.this.future1), peeker
/* 1853 */                   .getDone(ClosingFuture.Combiner4.this.future2), peeker
/* 1854 */                   .getDone(ClosingFuture.Combiner4.this.future3), peeker
/* 1855 */                   .getDone(ClosingFuture.Combiner4.this.future4));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1860 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction4<V1, V2, V3, V4, U> function, Executor executor) {
/* 1903 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 1907 */               return function.apply(closer, peeker
/*      */                   
/* 1909 */                   .getDone(ClosingFuture.Combiner4.this.future1), peeker
/* 1910 */                   .getDone(ClosingFuture.Combiner4.this.future2), peeker
/* 1911 */                   .getDone(ClosingFuture.Combiner4.this.future3), peeker
/* 1912 */                   .getDone(ClosingFuture.Combiner4.this.future4));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 1917 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface ClosingFunction4<V1, V2, V3, V4, U>
/*      */     {
/*      */       @ParametricNullness
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2, @ParametricNullness V3 param2V3, @ParametricNullness V4 param2V4) throws Exception;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @FunctionalInterface
/*      */     public static interface AsyncClosingFunction4<V1, V2, V3, V4, U>
/*      */     {
/*      */       ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2, @ParametricNullness V3 param2V3, @ParametricNullness V4 param2V4) throws Exception;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Combiner5<V1, V2, V3, V4, V5>
/*      */     extends Combiner
/*      */   {
/*      */     private final ClosingFuture<V1> future1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V2> future2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V3> future3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V4> future4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ClosingFuture<V5> future5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Combiner5(ClosingFuture<V1> future1, ClosingFuture<V2> future2, ClosingFuture<V3> future3, ClosingFuture<V4> future4, ClosingFuture<V5> future5) {
/* 2031 */       super(true, (Iterable)ImmutableList.of(future1, future2, future3, future4, future5));
/* 2032 */       this.future1 = future1;
/* 2033 */       this.future2 = future2;
/* 2034 */       this.future3 = future3;
/* 2035 */       this.future4 = future4;
/* 2036 */       this.future5 = future5;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> call(final ClosingFunction5<V1, V2, V3, V4, V5, U> function, Executor executor) {
/* 2055 */       return call(new ClosingFuture.Combiner.CombiningCallable<U>()
/*      */           {
/*      */             @ParametricNullness
/*      */             public U call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 2060 */               return (U)function.apply(closer, peeker
/*      */                   
/* 2062 */                   .getDone(ClosingFuture.Combiner5.this.future1), peeker
/* 2063 */                   .getDone(ClosingFuture.Combiner5.this.future2), peeker
/* 2064 */                   .getDone(ClosingFuture.Combiner5.this.future3), peeker
/* 2065 */                   .getDone(ClosingFuture.Combiner5.this.future4), peeker
/* 2066 */                   .getDone(ClosingFuture.Combiner5.this.future5));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 2071 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction5<V1, V2, V3, V4, V5, U> function, Executor executor) {
/* 2115 */       return callAsync(new ClosingFuture.Combiner.AsyncCombiningCallable<U>()
/*      */           {
/*      */             public ClosingFuture<U> call(ClosingFuture.DeferredCloser closer, ClosingFuture.Peeker peeker) throws Exception
/*      */             {
/* 2119 */               return function.apply(closer, peeker
/*      */                   
/* 2121 */                   .getDone(ClosingFuture.Combiner5.this.future1), peeker
/* 2122 */                   .getDone(ClosingFuture.Combiner5.this.future2), peeker
/* 2123 */                   .getDone(ClosingFuture.Combiner5.this.future3), peeker
/* 2124 */                   .getDone(ClosingFuture.Combiner5.this.future4), peeker
/* 2125 */                   .getDone(ClosingFuture.Combiner5.this.future5));
/*      */             }
/*      */ 
/*      */             
/*      */             public String toString() {
/* 2130 */               return function.toString();
/*      */             }
/*      */           }executor);
/*      */     } @FunctionalInterface
/*      */     public static interface ClosingFunction5<V1, V2, V3, V4, V5, U> {
/*      */       @ParametricNullness
/*      */       U apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2, @ParametricNullness V3 param2V3, @ParametricNullness V4 param2V4, @ParametricNullness V5 param2V5) throws Exception; } @FunctionalInterface
/*      */     public static interface AsyncClosingFunction5<V1, V2, V3, V4, V5, U> { ClosingFuture<U> apply(ClosingFuture.DeferredCloser param2DeferredCloser, @ParametricNullness V1 param2V1, @ParametricNullness V2 param2V2, @ParametricNullness V3 param2V3, @ParametricNullness V4 param2V4, @ParametricNullness V5 param2V5) throws Exception; }
/*      */   }
/*      */   public String toString() {
/* 2140 */     return MoreObjects.toStringHelper(this).add("state", this.state.get()).addValue(this.future).toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void finalize() {
/* 2145 */     if (((State)this.state.get()).equals(State.OPEN)) {
/* 2146 */       logger.get().log(Level.SEVERE, "Uh oh! An open ClosingFuture has leaked and will close: {0}", this);
/* 2147 */       FluentFuture<V> fluentFuture = finishToFuture();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void closeQuietly(@CheckForNull AutoCloseable closeable, Executor executor) {
/* 2152 */     if (closeable == null) {
/*      */       return;
/*      */     }
/*      */     try {
/* 2156 */       executor.execute(() -> {
/*      */             
/*      */             try {
/*      */               closeable.close();
/* 2160 */             } catch (Exception e) {
/*      */               Platform.restoreInterruptIfIsInterruptedException(e);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               logger.get().log(Level.WARNING, "thrown by close()", e);
/*      */             } 
/*      */           });
/* 2173 */     } catch (RejectedExecutionException e) {
/* 2174 */       if (logger.get().isLoggable(Level.WARNING)) {
/* 2175 */         logger
/* 2176 */           .get()
/* 2177 */           .log(Level.WARNING, 
/*      */             
/* 2179 */             String.format("while submitting close to %s; will close inline", new Object[] { executor }), e);
/*      */       }
/*      */       
/* 2182 */       closeQuietly(closeable, MoreExecutors.directExecutor());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkAndUpdateState(State oldState, State newState) {
/* 2187 */     Preconditions.checkState(
/* 2188 */         compareAndUpdateState(oldState, newState), "Expected state to be %s, but it was %s", oldState, newState);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean compareAndUpdateState(State oldState, State newState) {
/* 2195 */     return this.state.compareAndSet(oldState, newState);
/*      */   }
/*      */   
/*      */   private static final class CloseableList
/*      */     extends IdentityHashMap<AutoCloseable, Executor>
/*      */     implements Closeable {
/* 2201 */     private final ClosingFuture.DeferredCloser closer = new ClosingFuture.DeferredCloser(this);
/*      */     
/*      */     private volatile boolean closed;
/*      */     
/*      */     @CheckForNull
/*      */     private volatile CountDownLatch whenClosed;
/*      */ 
/*      */     
/*      */     <V, U> ListenableFuture<U> applyClosingFunction(ClosingFuture.ClosingFunction<? super V, U> transformation, @ParametricNullness V input) throws Exception {
/* 2210 */       CloseableList newCloseables = new CloseableList();
/*      */       try {
/* 2212 */         return (ListenableFuture)Futures.immediateFuture(transformation.apply(newCloseables.closer, input));
/*      */       } finally {
/* 2214 */         add(newCloseables, MoreExecutors.directExecutor());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <V, U> FluentFuture<U> applyAsyncClosingFunction(ClosingFuture.AsyncClosingFunction<V, U> transformation, @ParametricNullness V input) throws Exception {
/* 2223 */       CloseableList newCloseables = new CloseableList();
/*      */       try {
/* 2225 */         ClosingFuture<U> closingFuture = transformation.apply(newCloseables.closer, input);
/* 2226 */         closingFuture.becomeSubsumedInto(newCloseables);
/* 2227 */         return closingFuture.future;
/*      */       } finally {
/* 2229 */         add(newCloseables, MoreExecutors.directExecutor());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void close() {
/* 2235 */       if (this.closed) {
/*      */         return;
/*      */       }
/* 2238 */       synchronized (this) {
/* 2239 */         if (this.closed) {
/*      */           return;
/*      */         }
/* 2242 */         this.closed = true;
/*      */       } 
/* 2244 */       for (Map.Entry<AutoCloseable, Executor> entry : entrySet()) {
/* 2245 */         ClosingFuture.closeQuietly(entry.getKey(), entry.getValue());
/*      */       }
/* 2247 */       clear();
/* 2248 */       if (this.whenClosed != null) {
/* 2249 */         this.whenClosed.countDown();
/*      */       }
/*      */     }
/*      */     
/*      */     void add(@CheckForNull AutoCloseable closeable, Executor executor) {
/* 2254 */       Preconditions.checkNotNull(executor);
/* 2255 */       if (closeable == null) {
/*      */         return;
/*      */       }
/* 2258 */       synchronized (this) {
/* 2259 */         if (!this.closed) {
/* 2260 */           put(closeable, executor);
/*      */           return;
/*      */         } 
/*      */       } 
/* 2264 */       ClosingFuture.closeQuietly(closeable, executor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CountDownLatch whenClosedCountDown() {
/* 2271 */       if (this.closed) {
/* 2272 */         return new CountDownLatch(0);
/*      */       }
/* 2274 */       synchronized (this) {
/* 2275 */         if (this.closed) {
/* 2276 */           return new CountDownLatch(0);
/*      */         }
/* 2278 */         Preconditions.checkState((this.whenClosed == null));
/* 2279 */         return this.whenClosed = new CountDownLatch(1);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private CloseableList() {}
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   CountDownLatch whenClosedCountDown() {
/* 2290 */     return this.closeables.whenClosedCountDown();
/*      */   }
/*      */ 
/*      */   
/*      */   enum State
/*      */   {
/* 2296 */     OPEN,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2302 */     SUBSUMED,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2308 */     WILL_CLOSE,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2314 */     CLOSING,
/*      */ 
/*      */     
/* 2317 */     CLOSED,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2323 */     WILL_CREATE_VALUE_AND_CLOSER;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ValueAndCloserConsumer<V> {
/*      */     void accept(ClosingFuture.ValueAndCloser<V> param1ValueAndCloser);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface AsyncClosingFunction<T, U> {
/*      */     ClosingFuture<U> apply(ClosingFuture.DeferredCloser param1DeferredCloser, @ParametricNullness T param1T) throws Exception;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ClosingFunction<T, U> {
/*      */     @ParametricNullness
/*      */     U apply(ClosingFuture.DeferredCloser param1DeferredCloser, @ParametricNullness T param1T) throws Exception;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface AsyncClosingCallable<V> {
/*      */     ClosingFuture<V> call(ClosingFuture.DeferredCloser param1DeferredCloser) throws Exception;
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface ClosingCallable<V> {
/*      */     @ParametricNullness
/*      */     V call(ClosingFuture.DeferredCloser param1DeferredCloser) throws Exception;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ClosingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */