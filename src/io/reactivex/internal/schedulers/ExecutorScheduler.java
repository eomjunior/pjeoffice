/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableContainer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.schedulers.SchedulerRunnableIntrospection;
/*     */ import io.reactivex.schedulers.Schedulers;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExecutorScheduler
/*     */   extends Scheduler
/*     */ {
/*     */   final boolean interruptibleWorker;
/*     */   @NonNull
/*     */   final Executor executor;
/*  39 */   static final Scheduler HELPER = Schedulers.single();
/*     */   
/*     */   public ExecutorScheduler(@NonNull Executor executor, boolean interruptibleWorker) {
/*  42 */     this.executor = executor;
/*  43 */     this.interruptibleWorker = interruptibleWorker;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/*  49 */     return new ExecutorWorker(this.executor, this.interruptibleWorker);
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run) {
/*  55 */     Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */     try {
/*  57 */       if (this.executor instanceof ExecutorService) {
/*  58 */         ScheduledDirectTask task = new ScheduledDirectTask(decoratedRun);
/*  59 */         Future<?> f = ((ExecutorService)this.executor).submit(task);
/*  60 */         task.setFuture(f);
/*  61 */         return task;
/*     */       } 
/*     */       
/*  64 */       if (this.interruptibleWorker) {
/*  65 */         ExecutorWorker.InterruptibleRunnable interruptibleTask = new ExecutorWorker.InterruptibleRunnable(decoratedRun, null);
/*  66 */         this.executor.execute(interruptibleTask);
/*  67 */         return interruptibleTask;
/*     */       } 
/*  69 */       ExecutorWorker.BooleanRunnable br = new ExecutorWorker.BooleanRunnable(decoratedRun);
/*  70 */       this.executor.execute(br);
/*  71 */       return br;
/*     */     }
/*  73 */     catch (RejectedExecutionException ex) {
/*  74 */       RxJavaPlugins.onError(ex);
/*  75 */       return (Disposable)EmptyDisposable.INSTANCE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run, long delay, TimeUnit unit) {
/*  82 */     Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*  83 */     if (this.executor instanceof ScheduledExecutorService) {
/*     */       try {
/*  85 */         ScheduledDirectTask task = new ScheduledDirectTask(decoratedRun);
/*  86 */         Future<?> f = ((ScheduledExecutorService)this.executor).schedule(task, delay, unit);
/*  87 */         task.setFuture(f);
/*  88 */         return task;
/*  89 */       } catch (RejectedExecutionException ex) {
/*  90 */         RxJavaPlugins.onError(ex);
/*  91 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       } 
/*     */     }
/*     */     
/*  95 */     DelayedRunnable dr = new DelayedRunnable(decoratedRun);
/*     */     
/*  97 */     Disposable delayed = HELPER.scheduleDirect(new DelayedDispose(dr), delay, unit);
/*     */     
/*  99 */     dr.timed.replace(delayed);
/*     */     
/* 101 */     return dr;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, TimeUnit unit) {
/* 107 */     if (this.executor instanceof ScheduledExecutorService) {
/* 108 */       Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */       try {
/* 110 */         ScheduledDirectPeriodicTask task = new ScheduledDirectPeriodicTask(decoratedRun);
/* 111 */         Future<?> f = ((ScheduledExecutorService)this.executor).scheduleAtFixedRate(task, initialDelay, period, unit);
/* 112 */         task.setFuture(f);
/* 113 */         return task;
/* 114 */       } catch (RejectedExecutionException ex) {
/* 115 */         RxJavaPlugins.onError(ex);
/* 116 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       } 
/*     */     } 
/* 119 */     return super.schedulePeriodicallyDirect(run, initialDelay, period, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ExecutorWorker
/*     */     extends Scheduler.Worker
/*     */     implements Runnable
/*     */   {
/*     */     final boolean interruptibleWorker;
/*     */     
/*     */     final Executor executor;
/*     */     final MpscLinkedQueue<Runnable> queue;
/*     */     volatile boolean disposed;
/* 132 */     final AtomicInteger wip = new AtomicInteger();
/*     */     
/* 134 */     final CompositeDisposable tasks = new CompositeDisposable();
/*     */     
/*     */     public ExecutorWorker(Executor executor, boolean interruptibleWorker) {
/* 137 */       this.executor = executor;
/* 138 */       this.queue = new MpscLinkedQueue();
/* 139 */       this.interruptibleWorker = interruptibleWorker;
/*     */     }
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run) {
/*     */       Runnable task;
/*     */       Disposable disposable;
/* 145 */       if (this.disposed) {
/* 146 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/*     */       
/* 149 */       Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 154 */       if (this.interruptibleWorker) {
/* 155 */         InterruptibleRunnable interruptibleTask = new InterruptibleRunnable(decoratedRun, (DisposableContainer)this.tasks);
/* 156 */         this.tasks.add(interruptibleTask);
/*     */         
/* 158 */         task = interruptibleTask;
/* 159 */         disposable = interruptibleTask;
/*     */       } else {
/* 161 */         BooleanRunnable runnableTask = new BooleanRunnable(decoratedRun);
/*     */         
/* 163 */         task = runnableTask;
/* 164 */         disposable = runnableTask;
/*     */       } 
/*     */       
/* 167 */       this.queue.offer(task);
/*     */       
/* 169 */       if (this.wip.getAndIncrement() == 0) {
/*     */         try {
/* 171 */           this.executor.execute(this);
/* 172 */         } catch (RejectedExecutionException ex) {
/* 173 */           this.disposed = true;
/* 174 */           this.queue.clear();
/* 175 */           RxJavaPlugins.onError(ex);
/* 176 */           return (Disposable)EmptyDisposable.INSTANCE;
/*     */         } 
/*     */       }
/*     */       
/* 180 */       return disposable;
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
/* 186 */       if (delay <= 0L) {
/* 187 */         return schedule(run);
/*     */       }
/* 189 */       if (this.disposed) {
/* 190 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/*     */       
/* 193 */       SequentialDisposable first = new SequentialDisposable();
/*     */       
/* 195 */       SequentialDisposable mar = new SequentialDisposable((Disposable)first);
/*     */       
/* 197 */       Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */       
/* 199 */       ScheduledRunnable sr = new ScheduledRunnable(new SequentialDispose(mar, decoratedRun), (DisposableContainer)this.tasks);
/* 200 */       this.tasks.add(sr);
/*     */       
/* 202 */       if (this.executor instanceof ScheduledExecutorService) {
/*     */         try {
/* 204 */           Future<?> f = ((ScheduledExecutorService)this.executor).schedule(sr, delay, unit);
/* 205 */           sr.setFuture(f);
/* 206 */         } catch (RejectedExecutionException ex) {
/* 207 */           this.disposed = true;
/* 208 */           RxJavaPlugins.onError(ex);
/* 209 */           return (Disposable)EmptyDisposable.INSTANCE;
/*     */         } 
/*     */       } else {
/* 212 */         Disposable d = ExecutorScheduler.HELPER.scheduleDirect(sr, delay, unit);
/* 213 */         sr.setFuture(new DisposeOnCancel(d));
/*     */       } 
/*     */       
/* 216 */       first.replace(sr);
/*     */       
/* 218 */       return (Disposable)mar;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 223 */       if (!this.disposed) {
/* 224 */         this.disposed = true;
/* 225 */         this.tasks.dispose();
/* 226 */         if (this.wip.getAndIncrement() == 0) {
/* 227 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 234 */       return this.disposed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 239 */       int missed = 1;
/* 240 */       MpscLinkedQueue<Runnable> q = this.queue;
/*     */       
/*     */       do {
/* 243 */         if (this.disposed) {
/* 244 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/*     */         while (true) {
/* 249 */           Runnable run = (Runnable)q.poll();
/* 250 */           if (run == null) {
/*     */             break;
/*     */           }
/* 253 */           run.run();
/*     */           
/* 255 */           if (this.disposed) {
/* 256 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 261 */         if (this.disposed) {
/* 262 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 266 */         missed = this.wip.addAndGet(-missed);
/* 267 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     static final class BooleanRunnable
/*     */       extends AtomicBoolean
/*     */       implements Runnable, Disposable
/*     */     {
/*     */       private static final long serialVersionUID = -2421395018820541164L;
/*     */       final Runnable actual;
/*     */       
/*     */       BooleanRunnable(Runnable actual) {
/* 279 */         this.actual = actual;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 284 */         if (get()) {
/*     */           return;
/*     */         }
/*     */         try {
/* 288 */           this.actual.run();
/*     */         } finally {
/* 290 */           lazySet(true);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 296 */         lazySet(true);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 301 */         return get();
/*     */       }
/*     */     }
/*     */     
/*     */     final class SequentialDispose implements Runnable {
/*     */       private final SequentialDisposable mar;
/*     */       private final Runnable decoratedRun;
/*     */       
/*     */       SequentialDispose(SequentialDisposable mar, Runnable decoratedRun) {
/* 310 */         this.mar = mar;
/* 311 */         this.decoratedRun = decoratedRun;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 316 */         this.mar.replace(ExecutorScheduler.ExecutorWorker.this.schedule(this.decoratedRun));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     static final class InterruptibleRunnable
/*     */       extends AtomicInteger
/*     */       implements Runnable, Disposable
/*     */     {
/*     */       private static final long serialVersionUID = -3603436687413320876L;
/*     */       
/*     */       final Runnable run;
/*     */       
/*     */       final DisposableContainer tasks;
/*     */       
/*     */       volatile Thread thread;
/*     */       
/*     */       static final int READY = 0;
/*     */       
/*     */       static final int RUNNING = 1;
/*     */       
/*     */       static final int FINISHED = 2;
/*     */       
/*     */       static final int INTERRUPTING = 3;
/*     */       
/*     */       static final int INTERRUPTED = 4;
/*     */ 
/*     */       
/*     */       InterruptibleRunnable(Runnable run, DisposableContainer tasks) {
/* 345 */         this.run = run;
/* 346 */         this.tasks = tasks;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 351 */         if (get() == 0) {
/* 352 */           this.thread = Thread.currentThread();
/* 353 */           if (compareAndSet(0, 1)) {
/*     */             try {
/* 355 */               this.run.run();
/*     */             } finally {
/* 357 */               this.thread = null;
/* 358 */               if (compareAndSet(1, 2)) {
/* 359 */                 cleanup();
/*     */               } else {
/* 361 */                 while (get() == 3) {
/* 362 */                   Thread.yield();
/*     */                 }
/* 364 */                 Thread.interrupted();
/*     */               } 
/*     */             } 
/*     */           } else {
/* 368 */             this.thread = null;
/*     */           } 
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/*     */         while (true) {
/* 376 */           int state = get();
/* 377 */           if (state >= 2)
/*     */             break; 
/* 379 */           if (state == 0) {
/* 380 */             if (compareAndSet(0, 4)) {
/* 381 */               cleanup(); break;
/*     */             } 
/*     */             continue;
/*     */           } 
/* 385 */           if (compareAndSet(1, 3)) {
/* 386 */             Thread t = this.thread;
/* 387 */             if (t != null) {
/* 388 */               t.interrupt();
/* 389 */               this.thread = null;
/*     */             } 
/* 391 */             set(4);
/* 392 */             cleanup();
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       void cleanup() {
/* 400 */         if (this.tasks != null) {
/* 401 */           this.tasks.delete(this);
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean isDisposed()
/*     */       {
/* 407 */         return (get() >= 2); } } } static final class BooleanRunnable extends AtomicBoolean implements Runnable, Disposable { private static final long serialVersionUID = -2421395018820541164L; final Runnable actual; BooleanRunnable(Runnable actual) { this.actual = actual; } public void run() { if (get()) return;  try { this.actual.run(); } finally { lazySet(true); }  } public void dispose() { lazySet(true); } public boolean isDisposed() { return get(); } } static final class InterruptibleRunnable extends AtomicInteger implements Runnable, Disposable { private static final long serialVersionUID = -3603436687413320876L; final Runnable run; final DisposableContainer tasks; volatile Thread thread; static final int READY = 0; static final int RUNNING = 1; public boolean isDisposed() { return (get() >= 2); }
/*     */     static final int FINISHED = 2; static final int INTERRUPTING = 3; static final int INTERRUPTED = 4; InterruptibleRunnable(Runnable run, DisposableContainer tasks) { this.run = run; this.tasks = tasks; } public void run() { if (get() == 0) { this.thread = Thread.currentThread(); if (compareAndSet(0, 1)) { try { this.run.run(); } finally { this.thread = null; if (compareAndSet(1, 2)) { cleanup(); } else { while (get() == 3)
/*     */                 Thread.yield();  Thread.interrupted(); }  }  }
/*     */         else { this.thread = null; }
/*     */          }
/*     */        } public void dispose() { while (true) { int state = get(); if (state >= 2)
/*     */           break;  if (state == 0) { if (compareAndSet(0, 4)) { cleanup(); break; }
/*     */            continue; }
/*     */          if (compareAndSet(1, 3)) { Thread t = this.thread; if (t != null) { t.interrupt(); this.thread = null; }
/*     */            set(4); cleanup(); break; }
/*     */          }
/*     */        } void cleanup() { if (this.tasks != null)
/*     */         this.tasks.delete(this);  } }
/*     */    static final class DelayedRunnable extends AtomicReference<Runnable> implements Runnable, Disposable, SchedulerRunnableIntrospection
/*     */   {
/* 422 */     private static final long serialVersionUID = -4101336210206799084L; DelayedRunnable(Runnable run) { super(run);
/* 423 */       this.timed = new SequentialDisposable();
/* 424 */       this.direct = new SequentialDisposable(); }
/*     */     
/*     */     final SequentialDisposable timed; final SequentialDisposable direct;
/*     */     
/*     */     public void run() {
/* 429 */       Runnable r = get();
/* 430 */       if (r != null) {
/*     */         try {
/* 432 */           r.run();
/*     */         } finally {
/* 434 */           lazySet(null);
/* 435 */           this.timed.lazySet(DisposableHelper.DISPOSED);
/* 436 */           this.direct.lazySet(DisposableHelper.DISPOSED);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 443 */       return (get() == null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 448 */       if (getAndSet(null) != null) {
/* 449 */         this.timed.dispose();
/* 450 */         this.direct.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Runnable getWrappedRunnable() {
/* 456 */       Runnable r = get();
/* 457 */       return (r != null) ? r : Functions.EMPTY_RUNNABLE;
/*     */     }
/*     */   }
/*     */   
/*     */   final class DelayedDispose implements Runnable {
/*     */     private final ExecutorScheduler.DelayedRunnable dr;
/*     */     
/*     */     DelayedDispose(ExecutorScheduler.DelayedRunnable dr) {
/* 465 */       this.dr = dr;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 470 */       this.dr.direct.replace(ExecutorScheduler.this.scheduleDirect(this.dr));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/ExecutorScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */