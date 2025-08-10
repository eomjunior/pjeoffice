/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.time.Duration;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Delayed;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadPoolExecutor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class MoreExecutors
/*      */ {
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, Duration terminationTimeout) {
/*   88 */     return getExitingExecutorService(executor, 
/*   89 */         Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  110 */     return (new Application()).getExitingExecutorService(executor, terminationTimeout, timeUnit);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/*  129 */     return (new Application()).getExitingExecutorService(executor);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, Duration terminationTimeout) {
/*  149 */     return getExitingScheduledExecutorService(executor, 
/*  150 */         Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  171 */     return (new Application())
/*  172 */       .getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/*  192 */     return (new Application()).getExitingScheduledExecutorService(executor);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static void addDelayedShutdownHook(ExecutorService service, Duration terminationTimeout) {
/*  209 */     addDelayedShutdownHook(service, Internal.toNanosSaturated(terminationTimeout), TimeUnit.NANOSECONDS);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
/*  228 */     (new Application()).addDelayedShutdownHook(service, terminationTimeout, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static class Application
/*      */   {
/*      */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  239 */       MoreExecutors.useDaemonThreadFactory(executor);
/*  240 */       ExecutorService service = Executors.unconfigurableExecutorService(executor);
/*  241 */       addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
/*  242 */       return service;
/*      */     }
/*      */     
/*      */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/*  246 */       return getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
/*      */     }
/*      */ 
/*      */     
/*      */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  251 */       MoreExecutors.useDaemonThreadFactory(executor);
/*  252 */       ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
/*  253 */       addDelayedShutdownHook(executor, terminationTimeout, timeUnit);
/*  254 */       return service;
/*      */     }
/*      */ 
/*      */     
/*      */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/*  259 */       return getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
/*      */     }
/*      */ 
/*      */     
/*      */     final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
/*  264 */       Preconditions.checkNotNull(service);
/*  265 */       Preconditions.checkNotNull(timeUnit);
/*  266 */       addShutdownHook(
/*  267 */           MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable(this)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               public void run()
/*      */               {
/*      */                 try {
/*  278 */                   service.shutdown();
/*  279 */                   service.awaitTermination(terminationTimeout, timeUnit);
/*  280 */                 } catch (InterruptedException interruptedException) {}
/*      */               }
/*      */             }));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     void addShutdownHook(Thread hook) {
/*  289 */       Runtime.getRuntime().addShutdownHook(hook);
/*      */     }
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
/*  296 */     executor.setThreadFactory((new ThreadFactoryBuilder())
/*      */         
/*  298 */         .setDaemon(true)
/*  299 */         .setThreadFactory(executor.getThreadFactory())
/*  300 */         .build());
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static final class DirectExecutorService
/*      */     extends AbstractListeningExecutorService
/*      */   {
/*  308 */     private final Object lock = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("lock")
/*  317 */     private int runningTasks = 0;
/*      */ 
/*      */     
/*      */     @GuardedBy("lock")
/*      */     private boolean shutdown = false;
/*      */ 
/*      */     
/*      */     public void execute(Runnable command) {
/*  325 */       startTask();
/*      */       try {
/*  327 */         command.run();
/*      */       } finally {
/*  329 */         endTask();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isShutdown() {
/*  335 */       synchronized (this.lock) {
/*  336 */         return this.shutdown;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void shutdown() {
/*  342 */       synchronized (this.lock) {
/*  343 */         this.shutdown = true;
/*  344 */         if (this.runningTasks == 0) {
/*  345 */           this.lock.notifyAll();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public List<Runnable> shutdownNow() {
/*  353 */       shutdown();
/*  354 */       return Collections.emptyList();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isTerminated() {
/*  359 */       synchronized (this.lock) {
/*  360 */         return (this.shutdown && this.runningTasks == 0);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/*  366 */       long nanos = unit.toNanos(timeout);
/*  367 */       synchronized (this.lock) {
/*      */         while (true) {
/*  369 */           if (this.shutdown && this.runningTasks == 0)
/*  370 */             return true; 
/*  371 */           if (nanos <= 0L) {
/*  372 */             return false;
/*      */           }
/*  374 */           long now = System.nanoTime();
/*  375 */           TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
/*  376 */           nanos -= System.nanoTime() - now;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void startTask() {
/*  388 */       synchronized (this.lock) {
/*  389 */         if (this.shutdown) {
/*  390 */           throw new RejectedExecutionException("Executor already shutdown");
/*      */         }
/*  392 */         this.runningTasks++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void endTask() {
/*  398 */       synchronized (this.lock) {
/*  399 */         int numRunning = --this.runningTasks;
/*  400 */         if (numRunning == 0) {
/*  401 */           this.lock.notifyAll();
/*      */         }
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
/*      */     private DirectExecutorService() {}
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
/*      */   public static ListeningExecutorService newDirectExecutorService() {
/*  436 */     return new DirectExecutorService();
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
/*      */   public static Executor directExecutor() {
/*  501 */     return DirectExecutor.INSTANCE;
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static Executor newSequentialExecutor(Executor delegate) {
/*  551 */     return new SequentialExecutor(delegate);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
/*  572 */     return (delegate instanceof ListeningExecutorService) ? 
/*  573 */       (ListeningExecutorService)delegate : (
/*  574 */       (delegate instanceof ScheduledExecutorService) ? 
/*  575 */       new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : 
/*  576 */       new ListeningDecorator(delegate));
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
/*      */   public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
/*  599 */     return (delegate instanceof ListeningScheduledExecutorService) ? 
/*  600 */       (ListeningScheduledExecutorService)delegate : 
/*  601 */       new ScheduledListeningDecorator(delegate);
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static class ListeningDecorator extends AbstractListeningExecutorService {
/*      */     private final ExecutorService delegate;
/*      */     
/*      */     ListeningDecorator(ExecutorService delegate) {
/*  610 */       this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/*  615 */       return this.delegate.awaitTermination(timeout, unit);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isShutdown() {
/*  620 */       return this.delegate.isShutdown();
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isTerminated() {
/*  625 */       return this.delegate.isTerminated();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void shutdown() {
/*  630 */       this.delegate.shutdown();
/*      */     }
/*      */ 
/*      */     
/*      */     public final List<Runnable> shutdownNow() {
/*  635 */       return this.delegate.shutdownNow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void execute(Runnable command) {
/*  640 */       this.delegate.execute(command);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String toString() {
/*  645 */       return super.toString() + "[" + this.delegate + "]";
/*      */     }
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static final class ScheduledListeningDecorator
/*      */     extends ListeningDecorator
/*      */     implements ListeningScheduledExecutorService {
/*      */     final ScheduledExecutorService delegate;
/*      */     
/*      */     ScheduledListeningDecorator(ScheduledExecutorService delegate) {
/*  657 */       super(delegate);
/*  658 */       this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/*  664 */       TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, null);
/*  665 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/*  666 */       return new ListenableScheduledTask(task, scheduled);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/*  672 */       TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
/*  673 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/*  674 */       return new ListenableScheduledTask<>(task, scheduled);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/*  680 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*  681 */       ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
/*  682 */       return new ListenableScheduledTask(task, scheduled);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/*  688 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*      */       
/*  690 */       ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
/*  691 */       return new ListenableScheduledTask(task, scheduled);
/*      */     }
/*      */     
/*      */     private static final class ListenableScheduledTask<V>
/*      */       extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*      */       implements ListenableScheduledFuture<V>
/*      */     {
/*      */       private final ScheduledFuture<?> scheduledDelegate;
/*      */       
/*      */       public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
/*  701 */         super(listenableDelegate);
/*  702 */         this.scheduledDelegate = scheduledDelegate;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean cancel(boolean mayInterruptIfRunning) {
/*  707 */         boolean cancelled = super.cancel(mayInterruptIfRunning);
/*  708 */         if (cancelled)
/*      */         {
/*  710 */           this.scheduledDelegate.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */         
/*  714 */         return cancelled;
/*      */       }
/*      */ 
/*      */       
/*      */       public long getDelay(TimeUnit unit) {
/*  719 */         return this.scheduledDelegate.getDelay(unit);
/*      */       }
/*      */ 
/*      */       
/*      */       public int compareTo(Delayed other) {
/*  724 */         return this.scheduledDelegate.compareTo(other);
/*      */       }
/*      */     }
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     private static final class NeverSuccessfulListenableFutureTask
/*      */       extends AbstractFuture.TrustedFuture<Void> implements Runnable {
/*      */       private final Runnable delegate;
/*      */       
/*      */       public NeverSuccessfulListenableFutureTask(Runnable delegate) {
/*  735 */         this.delegate = (Runnable)Preconditions.checkNotNull(delegate);
/*      */       }
/*      */ 
/*      */       
/*      */       public void run() {
/*      */         try {
/*  741 */           this.delegate.run();
/*  742 */         } catch (Throwable t) {
/*      */           
/*  744 */           setException(t);
/*  745 */           throw t;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       protected String pendingToString() {
/*  751 */         return "task=[" + this.delegate + "]";
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
/*      */   @ParametricNullness
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, Duration timeout) throws InterruptedException, ExecutionException, TimeoutException {
/*  780 */     return invokeAnyImpl(executorService, tasks, timed, 
/*  781 */         Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*      */   @ParametricNullness
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  802 */     Preconditions.checkNotNull(executorService);
/*  803 */     Preconditions.checkNotNull(unit);
/*  804 */     int ntasks = tasks.size();
/*  805 */     Preconditions.checkArgument((ntasks > 0));
/*  806 */     List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
/*  807 */     BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
/*  808 */     long timeoutNanos = unit.toNanos(timeout);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
/*  882 */     final ListenableFuture<T> future = executorService.submit(task);
/*  883 */     future.addListener(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  887 */             queue.add(future);
/*      */           }
/*      */         }, 
/*  890 */         directExecutor());
/*  891 */     return future;
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static ThreadFactory platformThreadFactory() {
/*  907 */     if (!isAppEngineWithApiClasses()) {
/*  908 */       return Executors.defaultThreadFactory();
/*      */     }
/*      */     try {
/*  911 */       return 
/*  912 */         (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager")
/*  913 */         .getMethod("currentRequestThreadFactory", new Class[0])
/*  914 */         .invoke(null, new Object[0]);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  920 */     catch (IllegalAccessException e) {
/*  921 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/*  922 */     } catch (ClassNotFoundException e) {
/*  923 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/*  924 */     } catch (NoSuchMethodException e) {
/*  925 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/*  926 */     } catch (InvocationTargetException e) {
/*  927 */       throw Throwables.propagate(e.getCause());
/*      */     } 
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static boolean isAppEngineWithApiClasses() {
/*  934 */     if (System.getProperty("com.google.appengine.runtime.environment") == null) {
/*  935 */       return false;
/*      */     }
/*      */     try {
/*  938 */       Class.forName("com.google.appengine.api.utils.SystemProperty");
/*  939 */     } catch (ClassNotFoundException e) {
/*  940 */       return false;
/*      */     } 
/*      */     
/*      */     try {
/*  944 */       return 
/*      */         
/*  946 */         (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null);
/*      */     }
/*  948 */     catch (ClassNotFoundException e) {
/*      */       
/*  950 */       return false;
/*  951 */     } catch (InvocationTargetException e) {
/*      */       
/*  953 */       return false;
/*  954 */     } catch (IllegalAccessException e) {
/*      */       
/*  956 */       return false;
/*  957 */     } catch (NoSuchMethodException e) {
/*      */       
/*  959 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static Thread newThread(String name, Runnable runnable) {
/*  970 */     Preconditions.checkNotNull(name);
/*  971 */     Preconditions.checkNotNull(runnable);
/*      */     
/*  973 */     Thread result = Objects.<Thread>requireNonNull(platformThreadFactory().newThread(runnable));
/*      */     try {
/*  975 */       result.setName(name);
/*  976 */     } catch (SecurityException securityException) {}
/*      */ 
/*      */     
/*  979 */     return result;
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
/*  999 */     Preconditions.checkNotNull(executor);
/* 1000 */     Preconditions.checkNotNull(nameSupplier);
/* 1001 */     return new Executor()
/*      */       {
/*      */         public void execute(Runnable command) {
/* 1004 */           executor.execute(Callables.threadRenaming(command, nameSupplier));
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
/* 1024 */     Preconditions.checkNotNull(service);
/* 1025 */     Preconditions.checkNotNull(nameSupplier);
/* 1026 */     return new WrappingExecutorService(service)
/*      */       {
/*      */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 1029 */           return Callables.threadRenaming(callable, nameSupplier);
/*      */         }
/*      */ 
/*      */         
/*      */         protected Runnable wrapTask(Runnable command) {
/* 1034 */           return Callables.threadRenaming(command, nameSupplier);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
/* 1054 */     Preconditions.checkNotNull(service);
/* 1055 */     Preconditions.checkNotNull(nameSupplier);
/* 1056 */     return new WrappingScheduledExecutorService(service)
/*      */       {
/*      */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 1059 */           return Callables.threadRenaming(callable, nameSupplier);
/*      */         }
/*      */ 
/*      */         
/*      */         protected Runnable wrapTask(Runnable command) {
/* 1064 */           return Callables.threadRenaming(command, nameSupplier);
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
/*      */   @CanIgnoreReturnValue
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static boolean shutdownAndAwaitTermination(ExecutorService service, Duration timeout) {
/* 1096 */     return shutdownAndAwaitTermination(service, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*      */   @CanIgnoreReturnValue
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
/* 1129 */     long halfTimeoutNanos = unit.toNanos(timeout) / 2L;
/*      */     
/* 1131 */     service.shutdown();
/*      */     
/*      */     try {
/* 1134 */       if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
/*      */         
/* 1136 */         service.shutdownNow();
/*      */         
/* 1138 */         service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
/*      */       } 
/* 1140 */     } catch (InterruptedException ie) {
/*      */       
/* 1142 */       Thread.currentThread().interrupt();
/*      */       
/* 1144 */       service.shutdownNow();
/*      */     } 
/* 1146 */     return service.isTerminated();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Executor rejectionPropagatingExecutor(final Executor delegate, final AbstractFuture<?> future) {
/* 1157 */     Preconditions.checkNotNull(delegate);
/* 1158 */     Preconditions.checkNotNull(future);
/* 1159 */     if (delegate == directExecutor())
/*      */     {
/* 1161 */       return delegate;
/*      */     }
/* 1163 */     return new Executor()
/*      */       {
/*      */         public void execute(Runnable command) {
/*      */           try {
/* 1167 */             delegate.execute(command);
/* 1168 */           } catch (RejectedExecutionException e) {
/* 1169 */             future.setException(e);
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/MoreExecutors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */