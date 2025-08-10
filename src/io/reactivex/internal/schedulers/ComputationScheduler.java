/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableContainer;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.disposables.ListCompositeDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
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
/*     */ public final class ComputationScheduler
/*     */   extends Scheduler
/*     */   implements SchedulerMultiWorkerSupport
/*     */ {
/*     */   static final FixedSchedulerPool NONE;
/*     */   private static final String THREAD_NAME_PREFIX = "RxComputationThreadPool";
/*     */   static final RxThreadFactory THREAD_FACTORY;
/*     */   static final String KEY_MAX_THREADS = "rx2.computation-threads";
/*  53 */   static final int MAX_THREADS = cap(Runtime.getRuntime().availableProcessors(), Integer.getInteger("rx2.computation-threads", 0).intValue());
/*     */   
/*  55 */   static final PoolWorker SHUTDOWN_WORKER = new PoolWorker(new RxThreadFactory("RxComputationShutdown")); final ThreadFactory threadFactory; static {
/*  56 */     SHUTDOWN_WORKER.dispose();
/*     */     
/*  58 */     int priority = Math.max(1, Math.min(10, 
/*  59 */           Integer.getInteger("rx2.computation-priority", 5).intValue()));
/*     */     
/*  61 */     THREAD_FACTORY = new RxThreadFactory("RxComputationThreadPool", priority, true);
/*     */     
/*  63 */     NONE = new FixedSchedulerPool(0, THREAD_FACTORY);
/*  64 */     NONE.shutdown();
/*     */   }
/*     */   final AtomicReference<FixedSchedulerPool> pool; private static final String KEY_COMPUTATION_PRIORITY = "rx2.computation-priority";
/*     */   static int cap(int cpuCount, int paramThreads) {
/*  68 */     return (paramThreads <= 0 || paramThreads > cpuCount) ? cpuCount : paramThreads;
/*     */   }
/*     */   
/*     */   static final class FixedSchedulerPool
/*     */     implements SchedulerMultiWorkerSupport
/*     */   {
/*     */     final int cores;
/*     */     final ComputationScheduler.PoolWorker[] eventLoops;
/*     */     long n;
/*     */     
/*     */     FixedSchedulerPool(int maxThreads, ThreadFactory threadFactory) {
/*  79 */       this.cores = maxThreads;
/*  80 */       this.eventLoops = new ComputationScheduler.PoolWorker[maxThreads];
/*  81 */       for (int i = 0; i < maxThreads; i++) {
/*  82 */         this.eventLoops[i] = new ComputationScheduler.PoolWorker(threadFactory);
/*     */       }
/*     */     }
/*     */     
/*     */     public ComputationScheduler.PoolWorker getEventLoop() {
/*  87 */       int c = this.cores;
/*  88 */       if (c == 0) {
/*  89 */         return ComputationScheduler.SHUTDOWN_WORKER;
/*     */       }
/*     */       
/*  92 */       return this.eventLoops[(int)(this.n++ % c)];
/*     */     }
/*     */     
/*     */     public void shutdown() {
/*  96 */       for (ComputationScheduler.PoolWorker w : this.eventLoops) {
/*  97 */         w.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void createWorkers(int number, SchedulerMultiWorkerSupport.WorkerCallback callback) {
/* 103 */       int c = this.cores;
/* 104 */       if (c == 0) {
/* 105 */         for (int i = 0; i < number; i++) {
/* 106 */           callback.onWorker(i, ComputationScheduler.SHUTDOWN_WORKER);
/*     */         }
/*     */       } else {
/* 109 */         int index = (int)this.n % c;
/* 110 */         for (int i = 0; i < number; i++) {
/* 111 */           callback.onWorker(i, new ComputationScheduler.EventLoopWorker(this.eventLoops[index]));
/* 112 */           if (++index == c) {
/* 113 */             index = 0;
/*     */           }
/*     */         } 
/* 116 */         this.n = index;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComputationScheduler() {
/* 126 */     this(THREAD_FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComputationScheduler(ThreadFactory threadFactory) {
/* 137 */     this.threadFactory = threadFactory;
/* 138 */     this.pool = new AtomicReference<FixedSchedulerPool>(NONE);
/* 139 */     start();
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/* 145 */     return new EventLoopWorker(((FixedSchedulerPool)this.pool.get()).getEventLoop());
/*     */   }
/*     */ 
/*     */   
/*     */   public void createWorkers(int number, SchedulerMultiWorkerSupport.WorkerCallback callback) {
/* 150 */     ObjectHelper.verifyPositive(number, "number > 0 required");
/* 151 */     ((FixedSchedulerPool)this.pool.get()).createWorkers(number, callback);
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run, long delay, TimeUnit unit) {
/* 157 */     PoolWorker w = ((FixedSchedulerPool)this.pool.get()).getEventLoop();
/* 158 */     return w.scheduleDirect(run, delay, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, TimeUnit unit) {
/* 164 */     PoolWorker w = ((FixedSchedulerPool)this.pool.get()).getEventLoop();
/* 165 */     return w.schedulePeriodicallyDirect(run, initialDelay, period, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 170 */     FixedSchedulerPool update = new FixedSchedulerPool(MAX_THREADS, this.threadFactory);
/* 171 */     if (!this.pool.compareAndSet(NONE, update)) {
/* 172 */       update.shutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/*     */     while (true) {
/* 179 */       FixedSchedulerPool curr = this.pool.get();
/* 180 */       if (curr == NONE) {
/*     */         return;
/*     */       }
/* 183 */       if (this.pool.compareAndSet(curr, NONE)) {
/* 184 */         curr.shutdown();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class EventLoopWorker
/*     */     extends Scheduler.Worker {
/*     */     private final ListCompositeDisposable serial;
/*     */     private final CompositeDisposable timed;
/*     */     private final ListCompositeDisposable both;
/*     */     private final ComputationScheduler.PoolWorker poolWorker;
/*     */     volatile boolean disposed;
/*     */     
/*     */     EventLoopWorker(ComputationScheduler.PoolWorker poolWorker) {
/* 199 */       this.poolWorker = poolWorker;
/* 200 */       this.serial = new ListCompositeDisposable();
/* 201 */       this.timed = new CompositeDisposable();
/* 202 */       this.both = new ListCompositeDisposable();
/* 203 */       this.both.add((Disposable)this.serial);
/* 204 */       this.both.add((Disposable)this.timed);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 209 */       if (!this.disposed) {
/* 210 */         this.disposed = true;
/* 211 */         this.both.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 217 */       return this.disposed;
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable action) {
/* 223 */       if (this.disposed) {
/* 224 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/*     */       
/* 227 */       return this.poolWorker.scheduleActual(action, 0L, TimeUnit.MILLISECONDS, (DisposableContainer)this.serial);
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
/* 233 */       if (this.disposed) {
/* 234 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/*     */       
/* 237 */       return this.poolWorker.scheduleActual(action, delayTime, unit, (DisposableContainer)this.timed);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class PoolWorker extends NewThreadWorker {
/*     */     PoolWorker(ThreadFactory threadFactory) {
/* 243 */       super(threadFactory);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/ComputationScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */