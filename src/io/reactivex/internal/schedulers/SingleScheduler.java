/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableContainer;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class SingleScheduler
/*     */   extends Scheduler
/*     */ {
/*     */   final ThreadFactory threadFactory;
/*  31 */   final AtomicReference<ScheduledExecutorService> executor = new AtomicReference<ScheduledExecutorService>();
/*     */ 
/*     */   
/*     */   private static final String KEY_SINGLE_PRIORITY = "rx2.single-priority";
/*     */ 
/*     */   
/*     */   private static final String THREAD_NAME_PREFIX = "RxSingleScheduler";
/*     */ 
/*     */   
/*     */   static final RxThreadFactory SINGLE_THREAD_FACTORY;
/*     */   
/*  42 */   static final ScheduledExecutorService SHUTDOWN = Executors.newScheduledThreadPool(0); static {
/*  43 */     SHUTDOWN.shutdown();
/*     */     
/*  45 */     int priority = Math.max(1, Math.min(10, 
/*  46 */           Integer.getInteger("rx2.single-priority", 5).intValue()));
/*     */     
/*  48 */     SINGLE_THREAD_FACTORY = new RxThreadFactory("RxSingleScheduler", priority, true);
/*     */   }
/*     */   
/*     */   public SingleScheduler() {
/*  52 */     this(SINGLE_THREAD_FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleScheduler(ThreadFactory threadFactory) {
/*  62 */     this.threadFactory = threadFactory;
/*  63 */     this.executor.lazySet(createExecutor(threadFactory));
/*     */   }
/*     */   
/*     */   static ScheduledExecutorService createExecutor(ThreadFactory threadFactory) {
/*  67 */     return SchedulerPoolFactory.create(threadFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  72 */     ScheduledExecutorService current, next = null;
/*     */     do {
/*  74 */       current = this.executor.get();
/*  75 */       if (current != SHUTDOWN) {
/*  76 */         if (next != null) {
/*  77 */           next.shutdown();
/*     */         }
/*     */         return;
/*     */       } 
/*  81 */       if (next != null)
/*  82 */         continue;  next = createExecutor(this.threadFactory);
/*     */     }
/*  84 */     while (!this.executor.compareAndSet(current, next));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/*  93 */     ScheduledExecutorService current = this.executor.get();
/*  94 */     if (current != SHUTDOWN) {
/*  95 */       current = this.executor.getAndSet(SHUTDOWN);
/*  96 */       if (current != SHUTDOWN) {
/*  97 */         current.shutdownNow();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/* 105 */     return new ScheduledWorker(this.executor.get());
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run, long delay, TimeUnit unit) {
/* 111 */     ScheduledDirectTask task = new ScheduledDirectTask(RxJavaPlugins.onSchedule(run));
/*     */     try {
/*     */       Future<?> f;
/* 114 */       if (delay <= 0L) {
/* 115 */         f = ((ScheduledExecutorService)this.executor.get()).submit(task);
/*     */       } else {
/* 117 */         f = ((ScheduledExecutorService)this.executor.get()).schedule(task, delay, unit);
/*     */       } 
/* 119 */       task.setFuture(f);
/* 120 */       return task;
/* 121 */     } catch (RejectedExecutionException ex) {
/* 122 */       RxJavaPlugins.onError(ex);
/* 123 */       return (Disposable)EmptyDisposable.INSTANCE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, TimeUnit unit) {
/* 130 */     Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/* 131 */     if (period <= 0L) {
/*     */       
/* 133 */       ScheduledExecutorService exec = this.executor.get();
/*     */       
/* 135 */       InstantPeriodicTask periodicWrapper = new InstantPeriodicTask(decoratedRun, exec);
/*     */       try {
/*     */         Future<?> f;
/* 138 */         if (initialDelay <= 0L) {
/* 139 */           f = exec.submit(periodicWrapper);
/*     */         } else {
/* 141 */           f = exec.schedule(periodicWrapper, initialDelay, unit);
/*     */         } 
/* 143 */         periodicWrapper.setFirst(f);
/* 144 */       } catch (RejectedExecutionException ex) {
/* 145 */         RxJavaPlugins.onError(ex);
/* 146 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       } 
/*     */       
/* 149 */       return periodicWrapper;
/*     */     } 
/* 151 */     ScheduledDirectPeriodicTask task = new ScheduledDirectPeriodicTask(decoratedRun);
/*     */     try {
/* 153 */       Future<?> f = ((ScheduledExecutorService)this.executor.get()).scheduleAtFixedRate(task, initialDelay, period, unit);
/* 154 */       task.setFuture(f);
/* 155 */       return task;
/* 156 */     } catch (RejectedExecutionException ex) {
/* 157 */       RxJavaPlugins.onError(ex);
/* 158 */       return (Disposable)EmptyDisposable.INSTANCE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ScheduledWorker
/*     */     extends Scheduler.Worker
/*     */   {
/*     */     final ScheduledExecutorService executor;
/*     */     final CompositeDisposable tasks;
/*     */     volatile boolean disposed;
/*     */     
/*     */     ScheduledWorker(ScheduledExecutorService executor) {
/* 171 */       this.executor = executor;
/* 172 */       this.tasks = new CompositeDisposable();
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
/* 178 */       if (this.disposed) {
/* 179 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/*     */       
/* 182 */       Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */       
/* 184 */       ScheduledRunnable sr = new ScheduledRunnable(decoratedRun, (DisposableContainer)this.tasks);
/* 185 */       this.tasks.add(sr);
/*     */       
/*     */       try {
/*     */         Future<?> f;
/* 189 */         if (delay <= 0L) {
/* 190 */           f = this.executor.submit(sr);
/*     */         } else {
/* 192 */           f = this.executor.schedule(sr, delay, unit);
/*     */         } 
/*     */         
/* 195 */         sr.setFuture(f);
/* 196 */       } catch (RejectedExecutionException ex) {
/* 197 */         dispose();
/* 198 */         RxJavaPlugins.onError(ex);
/* 199 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       } 
/*     */       
/* 202 */       return sr;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 207 */       if (!this.disposed) {
/* 208 */         this.disposed = true;
/* 209 */         this.tasks.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 215 */       return this.disposed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/SingleScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */