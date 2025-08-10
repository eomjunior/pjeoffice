/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableContainer;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class IoScheduler
/*     */   extends Scheduler
/*     */ {
/*     */   private static final String WORKER_THREAD_NAME_PREFIX = "RxCachedThreadScheduler";
/*     */   static final RxThreadFactory WORKER_THREAD_FACTORY;
/*     */   private static final String EVICTOR_THREAD_NAME_PREFIX = "RxCachedWorkerPoolEvictor";
/*     */   static final RxThreadFactory EVICTOR_THREAD_FACTORY;
/*     */   private static final String KEY_KEEP_ALIVE_TIME = "rx2.io-keep-alive-time";
/*     */   public static final long KEEP_ALIVE_TIME_DEFAULT = 60L;
/*     */   private static final long KEEP_ALIVE_TIME;
/*  42 */   private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;
/*     */   
/*     */   static final ThreadWorker SHUTDOWN_THREAD_WORKER;
/*     */   
/*     */   final ThreadFactory threadFactory;
/*     */   
/*     */   final AtomicReference<CachedWorkerPool> pool;
/*     */   
/*     */   private static final String KEY_IO_PRIORITY = "rx2.io-priority";
/*     */   static final CachedWorkerPool NONE;
/*     */   
/*     */   static {
/*  54 */     KEEP_ALIVE_TIME = Long.getLong("rx2.io-keep-alive-time", 60L).longValue();
/*     */     
/*  56 */     SHUTDOWN_THREAD_WORKER = new ThreadWorker(new RxThreadFactory("RxCachedThreadSchedulerShutdown"));
/*  57 */     SHUTDOWN_THREAD_WORKER.dispose();
/*     */     
/*  59 */     int priority = Math.max(1, Math.min(10, 
/*  60 */           Integer.getInteger("rx2.io-priority", 5).intValue()));
/*     */     
/*  62 */     WORKER_THREAD_FACTORY = new RxThreadFactory("RxCachedThreadScheduler", priority);
/*     */     
/*  64 */     EVICTOR_THREAD_FACTORY = new RxThreadFactory("RxCachedWorkerPoolEvictor", priority);
/*     */     
/*  66 */     NONE = new CachedWorkerPool(0L, null, WORKER_THREAD_FACTORY);
/*  67 */     NONE.shutdown();
/*     */   }
/*     */   
/*     */   static final class CachedWorkerPool implements Runnable {
/*     */     private final long keepAliveTime;
/*     */     private final ConcurrentLinkedQueue<IoScheduler.ThreadWorker> expiringWorkerQueue;
/*     */     final CompositeDisposable allWorkers;
/*     */     private final ScheduledExecutorService evictorService;
/*     */     private final Future<?> evictorTask;
/*     */     private final ThreadFactory threadFactory;
/*     */     
/*     */     CachedWorkerPool(long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
/*  79 */       this.keepAliveTime = (unit != null) ? unit.toNanos(keepAliveTime) : 0L;
/*  80 */       this.expiringWorkerQueue = new ConcurrentLinkedQueue<IoScheduler.ThreadWorker>();
/*  81 */       this.allWorkers = new CompositeDisposable();
/*  82 */       this.threadFactory = threadFactory;
/*     */       
/*  84 */       ScheduledExecutorService evictor = null;
/*  85 */       Future<?> task = null;
/*  86 */       if (unit != null) {
/*  87 */         evictor = Executors.newScheduledThreadPool(1, IoScheduler.EVICTOR_THREAD_FACTORY);
/*  88 */         task = evictor.scheduleWithFixedDelay(this, this.keepAliveTime, this.keepAliveTime, TimeUnit.NANOSECONDS);
/*     */       } 
/*  90 */       this.evictorService = evictor;
/*  91 */       this.evictorTask = task;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  96 */       evictExpiredWorkers();
/*     */     }
/*     */     
/*     */     IoScheduler.ThreadWorker get() {
/* 100 */       if (this.allWorkers.isDisposed()) {
/* 101 */         return IoScheduler.SHUTDOWN_THREAD_WORKER;
/*     */       }
/* 103 */       while (!this.expiringWorkerQueue.isEmpty()) {
/* 104 */         IoScheduler.ThreadWorker threadWorker = this.expiringWorkerQueue.poll();
/* 105 */         if (threadWorker != null) {
/* 106 */           return threadWorker;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 111 */       IoScheduler.ThreadWorker w = new IoScheduler.ThreadWorker(this.threadFactory);
/* 112 */       this.allWorkers.add(w);
/* 113 */       return w;
/*     */     }
/*     */ 
/*     */     
/*     */     void release(IoScheduler.ThreadWorker threadWorker) {
/* 118 */       threadWorker.setExpirationTime(now() + this.keepAliveTime);
/*     */       
/* 120 */       this.expiringWorkerQueue.offer(threadWorker);
/*     */     }
/*     */     
/*     */     void evictExpiredWorkers() {
/* 124 */       if (!this.expiringWorkerQueue.isEmpty()) {
/* 125 */         long currentTimestamp = now();
/*     */         
/* 127 */         for (IoScheduler.ThreadWorker threadWorker : this.expiringWorkerQueue) {
/* 128 */           if (threadWorker.getExpirationTime() <= currentTimestamp) {
/* 129 */             if (this.expiringWorkerQueue.remove(threadWorker)) {
/* 130 */               this.allWorkers.remove(threadWorker);
/*     */             }
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long now() {
/* 142 */       return System.nanoTime();
/*     */     }
/*     */     
/*     */     void shutdown() {
/* 146 */       this.allWorkers.dispose();
/* 147 */       if (this.evictorTask != null) {
/* 148 */         this.evictorTask.cancel(true);
/*     */       }
/* 150 */       if (this.evictorService != null) {
/* 151 */         this.evictorService.shutdownNow();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public IoScheduler() {
/* 157 */     this(WORKER_THREAD_FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoScheduler(ThreadFactory threadFactory) {
/* 166 */     this.threadFactory = threadFactory;
/* 167 */     this.pool = new AtomicReference<CachedWorkerPool>(NONE);
/* 168 */     start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 173 */     CachedWorkerPool update = new CachedWorkerPool(KEEP_ALIVE_TIME, KEEP_ALIVE_UNIT, this.threadFactory);
/* 174 */     if (!this.pool.compareAndSet(NONE, update)) {
/* 175 */       update.shutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/*     */     while (true) {
/* 182 */       CachedWorkerPool curr = this.pool.get();
/* 183 */       if (curr == NONE) {
/*     */         return;
/*     */       }
/* 186 */       if (this.pool.compareAndSet(curr, NONE)) {
/* 187 */         curr.shutdown();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/* 196 */     return new EventLoopWorker(this.pool.get());
/*     */   }
/*     */   
/*     */   public int size() {
/* 200 */     return ((CachedWorkerPool)this.pool.get()).allWorkers.size();
/*     */   }
/*     */   
/*     */   static final class EventLoopWorker
/*     */     extends Scheduler.Worker {
/*     */     private final CompositeDisposable tasks;
/*     */     private final IoScheduler.CachedWorkerPool pool;
/*     */     private final IoScheduler.ThreadWorker threadWorker;
/* 208 */     final AtomicBoolean once = new AtomicBoolean();
/*     */     
/*     */     EventLoopWorker(IoScheduler.CachedWorkerPool pool) {
/* 211 */       this.pool = pool;
/* 212 */       this.tasks = new CompositeDisposable();
/* 213 */       this.threadWorker = pool.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 218 */       if (this.once.compareAndSet(false, true)) {
/* 219 */         this.tasks.dispose();
/*     */ 
/*     */         
/* 222 */         this.pool.release(this.threadWorker);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 228 */       return this.once.get();
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
/* 234 */       if (this.tasks.isDisposed())
/*     */       {
/* 236 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/*     */       
/* 239 */       return this.threadWorker.scheduleActual(action, delayTime, unit, (DisposableContainer)this.tasks);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ThreadWorker extends NewThreadWorker {
/*     */     private long expirationTime;
/*     */     
/*     */     ThreadWorker(ThreadFactory threadFactory) {
/* 247 */       super(threadFactory);
/* 248 */       this.expirationTime = 0L;
/*     */     }
/*     */     
/*     */     public long getExpirationTime() {
/* 252 */       return this.expirationTime;
/*     */     }
/*     */     
/*     */     public void setExpirationTime(long expirationTime) {
/* 256 */       this.expirationTime = expirationTime;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/IoScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */