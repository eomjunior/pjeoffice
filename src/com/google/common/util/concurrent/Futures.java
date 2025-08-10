/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableCollection;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
/*      */ import com.google.common.util.concurrent.internal.InternalFutures;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.time.Duration;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Futures
/*      */   extends GwtFuturesCatchingSpecialization
/*      */ {
/*      */   public static <V> ListenableFuture<V> immediateFuture(@ParametricNullness V value) {
/*  135 */     if (value == null) {
/*      */ 
/*      */       
/*  138 */       ListenableFuture<V> typedNull = (ListenableFuture)ImmediateFuture.NULL;
/*  139 */       return typedNull;
/*      */     } 
/*  141 */     return new ImmediateFuture<>(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ListenableFuture<Void> immediateVoidFuture() {
/*  152 */     return (ListenableFuture)ImmediateFuture.NULL;
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
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
/*  164 */     Preconditions.checkNotNull(throwable);
/*  165 */     return new ImmediateFuture.ImmediateFailedFuture<>(throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateCancelledFuture() {
/*  175 */     ListenableFuture<Object> instance = ImmediateFuture.ImmediateCancelledFuture.INSTANCE;
/*  176 */     if (instance != null) {
/*  177 */       return (ListenableFuture)instance;
/*      */     }
/*  179 */     return new ImmediateFuture.ImmediateCancelledFuture<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <O> ListenableFuture<O> submit(Callable<O> callable, Executor executor) {
/*  190 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  191 */     executor.execute(task);
/*  192 */     return task;
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
/*      */   public static ListenableFuture<Void> submit(Runnable runnable, Executor executor) {
/*  204 */     TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(runnable, (Void)null);
/*  205 */     executor.execute(task);
/*  206 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <O> ListenableFuture<O> submitAsync(AsyncCallable<O> callable, Executor executor) {
/*  217 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  218 */     executor.execute(task);
/*  219 */     return task;
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, Duration delay, ScheduledExecutorService executorService) {
/*  233 */     return scheduleAsync(callable, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS, executorService);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, long delay, TimeUnit timeUnit, ScheduledExecutorService executorService) {
/*  251 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  252 */     Future<?> scheduled = executorService.schedule(task, delay, timeUnit);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  257 */     task.addListener(() -> scheduled.cancel(false), MoreExecutors.directExecutor());
/*  258 */     return task;
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
/*  304 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
/*  369 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, Duration time, ScheduledExecutorService scheduledExecutor) {
/*  387 */     return withTimeout(delegate, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS, scheduledExecutor);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  410 */     if (delegate.isDone()) {
/*  411 */       return delegate;
/*      */     }
/*  413 */     return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
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
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  453 */     return AbstractTransformFuture.create(input, function, executor);
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
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
/*  488 */     return AbstractTransformFuture.create(input, function, executor);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
/*  515 */     Preconditions.checkNotNull(input);
/*  516 */     Preconditions.checkNotNull(function);
/*  517 */     return new Future<O>()
/*      */       {
/*      */         public boolean cancel(boolean mayInterruptIfRunning)
/*      */         {
/*  521 */           return input.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isCancelled() {
/*  526 */           return input.isCancelled();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isDone() {
/*  531 */           return input.isDone();
/*      */         }
/*      */ 
/*      */         
/*      */         public O get() throws InterruptedException, ExecutionException {
/*  536 */           return applyTransformation(input.get());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  542 */           return applyTransformation(input.get(timeout, unit));
/*      */         }
/*      */         
/*      */         private O applyTransformation(I input) throws ExecutionException {
/*      */           try {
/*  547 */             return (O)function.apply(input);
/*  548 */           } catch (Throwable t) {
/*      */             
/*  550 */             throw new ExecutionException(t);
/*      */           } 
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
/*      */   @SafeVarargs
/*      */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
/*  576 */     ListenableFuture<List<V>> nullable = new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), true);
/*      */ 
/*      */     
/*  579 */     ListenableFuture<List<V>> nonNull = nullable;
/*  580 */     return nonNull;
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
/*      */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  602 */     ListenableFuture<List<V>> nullable = new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), true);
/*      */ 
/*      */     
/*  605 */     ListenableFuture<List<V>> nonNull = nullable;
/*  606 */     return nonNull;
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
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
/*  620 */     return new FutureCombiner<>(false, ImmutableList.copyOf((Object[])futures));
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
/*      */   public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  633 */     return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
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
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
/*  646 */     return new FutureCombiner<>(true, ImmutableList.copyOf((Object[])futures));
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
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  658 */     return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
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
/*      */   @GwtCompatible
/*      */   public static final class FutureCombiner<V>
/*      */   {
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
/*      */     private final ImmutableList<ListenableFuture<? extends V>> futures;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
/*  694 */       this.allMustSucceed = allMustSucceed;
/*  695 */       this.futures = futures;
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
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
/*  720 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
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
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
/*  745 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
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
/*      */     public ListenableFuture<?> run(final Runnable combiner, Executor executor) {
/*  765 */       return call(new Callable<Void>(this)
/*      */           {
/*      */             @CheckForNull
/*      */             public Void call() throws Exception
/*      */             {
/*  770 */               combiner.run();
/*  771 */               return null;
/*      */             }
/*      */           }executor);
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
/*      */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
/*  787 */     if (future.isDone()) {
/*  788 */       return future;
/*      */     }
/*  790 */     NonCancellationPropagatingFuture<V> output = new NonCancellationPropagatingFuture<>(future);
/*  791 */     future.addListener(output, MoreExecutors.directExecutor());
/*  792 */     return output;
/*      */   }
/*      */   
/*      */   private static final class NonCancellationPropagatingFuture<V>
/*      */     extends AbstractFuture.TrustedFuture<V> implements Runnable {
/*      */     @CheckForNull
/*      */     private ListenableFuture<V> delegate;
/*      */     
/*      */     NonCancellationPropagatingFuture(ListenableFuture<V> delegate) {
/*  801 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/*  808 */       ListenableFuture<V> localDelegate = this.delegate;
/*  809 */       if (localDelegate != null) {
/*  810 */         setFuture(localDelegate);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     protected String pendingToString() {
/*  817 */       ListenableFuture<V> localDelegate = this.delegate;
/*  818 */       if (localDelegate != null) {
/*  819 */         return "delegate=[" + localDelegate + "]";
/*      */       }
/*  821 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void afterDone() {
/*  826 */       this.delegate = null;
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
/*      */   @SafeVarargs
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
/*  863 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), false);
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
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  886 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), false);
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
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
/*  912 */     ListenableFuture[] arrayOfListenableFuture = (ListenableFuture[])gwtCompatibleToArray(futures);
/*  913 */     InCompletionOrderState<T> state = new InCompletionOrderState<>(arrayOfListenableFuture);
/*      */     
/*  915 */     ImmutableList.Builder<AbstractFuture<T>> delegatesBuilder = ImmutableList.builderWithExpectedSize(arrayOfListenableFuture.length);
/*  916 */     for (int i = 0; i < arrayOfListenableFuture.length; i++) {
/*  917 */       delegatesBuilder.add(new InCompletionOrderFuture(state));
/*      */     }
/*      */     
/*  920 */     ImmutableList<AbstractFuture<T>> delegates = delegatesBuilder.build();
/*  921 */     for (int j = 0; j < arrayOfListenableFuture.length; j++) {
/*  922 */       int localI = j;
/*  923 */       arrayOfListenableFuture[j].addListener(() -> state.recordInputCompletion(delegates, localI), MoreExecutors.directExecutor());
/*      */     } 
/*      */ 
/*      */     
/*  927 */     return (ImmutableList)delegates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> ListenableFuture<? extends T>[] gwtCompatibleToArray(Iterable<? extends ListenableFuture<? extends T>> futures) {
/*      */     ImmutableList immutableList;
/*  936 */     if (futures instanceof Collection) {
/*  937 */       Collection<ListenableFuture<? extends T>> collection = (Collection)futures;
/*      */     } else {
/*  939 */       immutableList = ImmutableList.copyOf(futures);
/*      */     } 
/*  941 */     return (ListenableFuture<? extends T>[])immutableList.toArray((Object[])new ListenableFuture[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class InCompletionOrderFuture<T>
/*      */     extends AbstractFuture<T>
/*      */   {
/*      */     @CheckForNull
/*      */     private Futures.InCompletionOrderState<T> state;
/*      */     
/*      */     private InCompletionOrderFuture(Futures.InCompletionOrderState<T> state) {
/*  952 */       this.state = state;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean interruptIfRunning) {
/*  957 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  958 */       if (super.cancel(interruptIfRunning)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  967 */         ((Futures.InCompletionOrderState)Objects.<Futures.InCompletionOrderState>requireNonNull(localState)).recordOutputCancellation(interruptIfRunning);
/*  968 */         return true;
/*      */       } 
/*  970 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void afterDone() {
/*  975 */       this.state = null;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     protected String pendingToString() {
/*  981 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  982 */       if (localState != null)
/*      */       {
/*      */         
/*  985 */         return "inputCount=[" + localState
/*  986 */           .inputFutures.length + "], remaining=[" + localState
/*      */           
/*  988 */           .incompleteOutputCount.get() + "]";
/*      */       }
/*      */       
/*  991 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class InCompletionOrderState<T>
/*      */   {
/*      */     private boolean wasCancelled = false;
/*      */     
/*      */     private boolean shouldInterrupt = true;
/*      */     
/*      */     private final AtomicInteger incompleteOutputCount;
/*      */     private final ListenableFuture<? extends T>[] inputFutures;
/* 1004 */     private volatile int delegateIndex = 0;
/*      */     
/*      */     private InCompletionOrderState(ListenableFuture<? extends T>[] inputFutures) {
/* 1007 */       this.inputFutures = inputFutures;
/* 1008 */       this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
/*      */     }
/*      */     
/*      */     private void recordOutputCancellation(boolean interruptIfRunning) {
/* 1012 */       this.wasCancelled = true;
/*      */ 
/*      */       
/* 1015 */       if (!interruptIfRunning) {
/* 1016 */         this.shouldInterrupt = false;
/*      */       }
/* 1018 */       recordCompletion();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void recordInputCompletion(ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex) {
/* 1027 */       ListenableFuture<? extends T> inputFuture = Objects.<ListenableFuture<? extends T>>requireNonNull(this.inputFutures[inputFutureIndex]);
/*      */       
/* 1029 */       this.inputFutures[inputFutureIndex] = null;
/* 1030 */       for (int i = this.delegateIndex; i < delegates.size(); i++) {
/* 1031 */         if (((AbstractFuture<T>)delegates.get(i)).setFuture(inputFuture)) {
/* 1032 */           recordCompletion();
/*      */           
/* 1034 */           this.delegateIndex = i + 1;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */       
/* 1041 */       this.delegateIndex = delegates.size();
/*      */     }
/*      */     
/*      */     private void recordCompletion() {
/* 1045 */       if (this.incompleteOutputCount.decrementAndGet() == 0 && this.wasCancelled) {
/* 1046 */         for (ListenableFuture<? extends T> toCancel : this.inputFutures) {
/* 1047 */           if (toCancel != null) {
/* 1048 */             toCancel.cancel(this.shouldInterrupt);
/*      */           }
/*      */         } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor) {
/* 1100 */     Preconditions.checkNotNull(callback);
/* 1101 */     future.addListener(new CallbackListener<>(future, callback), executor);
/*      */   }
/*      */   
/*      */   private static final class CallbackListener<V>
/*      */     implements Runnable {
/*      */     final Future<V> future;
/*      */     final FutureCallback<? super V> callback;
/*      */     
/*      */     CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
/* 1110 */       this.future = future;
/* 1111 */       this.callback = callback;
/*      */     }
/*      */     
/*      */     public void run() {
/*      */       V value;
/* 1116 */       if (this.future instanceof InternalFutureFailureAccess) {
/*      */         
/* 1118 */         Throwable failure = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)this.future);
/* 1119 */         if (failure != null) {
/* 1120 */           this.callback.onFailure(failure);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */       try {
/* 1126 */         value = Futures.getDone(this.future);
/* 1127 */       } catch (ExecutionException e) {
/* 1128 */         this.callback.onFailure(e.getCause());
/*      */         return;
/* 1130 */       } catch (Throwable e) {
/*      */         
/* 1132 */         this.callback.onFailure(e);
/*      */         return;
/*      */       } 
/* 1135 */       this.callback.onSuccess(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1140 */       return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
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
/*      */   @ParametricNullness
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getDone(Future<V> future) throws ExecutionException {
/* 1177 */     Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
/* 1178 */     return Uninterruptibles.getUninterruptibly(future);
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
/*      */   @ParametricNullness
/*      */   @CanIgnoreReturnValue
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
/* 1229 */     return FuturesGetChecked.getChecked(future, exceptionClass);
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
/*      */   @ParametricNullness
/*      */   @CanIgnoreReturnValue
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, Duration timeout) throws X {
/* 1281 */     return getChecked(future, exceptionClass, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*      */   @ParametricNullness
/*      */   @CanIgnoreReturnValue
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
/* 1334 */     return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
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
/*      */   @ParametricNullness
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getUnchecked(Future<V> future) {
/* 1374 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1376 */       return Uninterruptibles.getUninterruptibly(future);
/* 1377 */     } catch (ExecutionException e) {
/* 1378 */       wrapAndThrowUnchecked(e.getCause());
/* 1379 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1384 */     if (cause instanceof Error) {
/* 1385 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1392 */     throw new UncheckedExecutionException(cause);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/Futures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */