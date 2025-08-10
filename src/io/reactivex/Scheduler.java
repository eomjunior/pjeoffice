/*     */ package io.reactivex;
/*     */ 
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.schedulers.NewThreadWorker;
/*     */ import io.reactivex.internal.schedulers.SchedulerWhen;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.schedulers.SchedulerRunnableIntrospection;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public abstract class Scheduler
/*     */ {
/*  99 */   static final long CLOCK_DRIFT_TOLERANCE_NANOSECONDS = TimeUnit.MINUTES.toNanos(
/* 100 */       Long.getLong("rx2.scheduler.drift-tolerance", 15L).longValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long clockDriftTolerance() {
/* 110 */     return CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
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
/*     */   @NonNull
/*     */   public abstract Worker createWorker();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long now(@NonNull TimeUnit unit) {
/* 134 */     return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
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
/*     */   public void start() {}
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
/*     */   public void shutdown() {}
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
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run) {
/* 179 */     return scheduleDirect(run, 0L, TimeUnit.NANOSECONDS);
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
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
/* 197 */     Worker w = createWorker();
/*     */     
/* 199 */     Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */     
/* 201 */     DisposeTask task = new DisposeTask(decoratedRun, w);
/*     */     
/* 203 */     w.schedule(task, delay, unit);
/*     */     
/* 205 */     return task;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, @NonNull TimeUnit unit) {
/* 229 */     Worker w = createWorker();
/*     */     
/* 231 */     Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */     
/* 233 */     PeriodicDirectTask periodicTask = new PeriodicDirectTask(decoratedRun, w);
/*     */     
/* 235 */     Disposable d = w.schedulePeriodically(periodicTask, initialDelay, period, unit);
/* 236 */     if (d == EmptyDisposable.INSTANCE) {
/* 237 */       return d;
/*     */     }
/*     */     
/* 240 */     return periodicTask;
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
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public <S extends Scheduler & Disposable> S when(@NonNull Function<Flowable<Flowable<Completable>>, Completable> combine) {
/* 321 */     return (S)new SchedulerWhen(combine, this);
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
/*     */   public static abstract class Worker
/*     */     implements Disposable
/*     */   {
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run) {
/* 371 */       return schedule(run, 0L, TimeUnit.NANOSECONDS);
/*     */     }
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
/*     */     @NonNull
/*     */     public abstract Disposable schedule(@NonNull Runnable param1Runnable, long param1Long, @NonNull TimeUnit param1TimeUnit);
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
/*     */     @NonNull
/*     */     public Disposable schedulePeriodically(@NonNull Runnable run, long initialDelay, long period, @NonNull TimeUnit unit) {
/* 423 */       SequentialDisposable first = new SequentialDisposable();
/*     */       
/* 425 */       SequentialDisposable sd = new SequentialDisposable((Disposable)first);
/*     */       
/* 427 */       Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */       
/* 429 */       long periodInNanoseconds = unit.toNanos(period);
/* 430 */       long firstNowNanoseconds = now(TimeUnit.NANOSECONDS);
/* 431 */       long firstStartInNanoseconds = firstNowNanoseconds + unit.toNanos(initialDelay);
/*     */       
/* 433 */       Disposable d = schedule(new PeriodicTask(firstStartInNanoseconds, decoratedRun, firstNowNanoseconds, sd, periodInNanoseconds), initialDelay, unit);
/*     */ 
/*     */       
/* 436 */       if (d == EmptyDisposable.INSTANCE) {
/* 437 */         return d;
/*     */       }
/* 439 */       first.replace(d);
/*     */       
/* 441 */       return (Disposable)sd;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long now(@NonNull TimeUnit unit) {
/* 451 */       return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
/*     */     }
/*     */ 
/*     */     
/*     */     final class PeriodicTask
/*     */       implements Runnable, SchedulerRunnableIntrospection
/*     */     {
/*     */       @NonNull
/*     */       final Runnable decoratedRun;
/*     */       
/*     */       @NonNull
/*     */       final SequentialDisposable sd;
/*     */       
/*     */       final long periodInNanoseconds;
/*     */       long count;
/*     */       long lastNowNanoseconds;
/*     */       long startInNanoseconds;
/*     */       
/*     */       PeriodicTask(@NonNull long firstStartInNanoseconds, Runnable decoratedRun, @NonNull long firstNowNanoseconds, SequentialDisposable sd, long periodInNanoseconds) {
/* 470 */         this.decoratedRun = decoratedRun;
/* 471 */         this.sd = sd;
/* 472 */         this.periodInNanoseconds = periodInNanoseconds;
/* 473 */         this.lastNowNanoseconds = firstNowNanoseconds;
/* 474 */         this.startInNanoseconds = firstStartInNanoseconds;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 479 */         this.decoratedRun.run();
/*     */         
/* 481 */         if (!this.sd.isDisposed()) {
/*     */ 
/*     */ 
/*     */           
/* 485 */           long nowNanoseconds = Scheduler.Worker.this.now(TimeUnit.NANOSECONDS);
/*     */ 
/*     */ 
/*     */           
/* 489 */           long nextTick = nowNanoseconds + this.periodInNanoseconds;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 494 */           this.startInNanoseconds = nextTick - this.periodInNanoseconds * ++this.count;
/*     */           
/* 496 */           nextTick = this.startInNanoseconds + ++this.count * this.periodInNanoseconds;
/*     */           
/* 498 */           this.lastNowNanoseconds = nowNanoseconds;
/*     */           
/* 500 */           long delay = nextTick - nowNanoseconds;
/* 501 */           this.sd.replace(Scheduler.Worker.this.schedule(this, delay, TimeUnit.NANOSECONDS));
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Runnable getWrappedRunnable() {
/* 507 */         return this.decoratedRun;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class PeriodicDirectTask
/*     */     implements Disposable, Runnable, SchedulerRunnableIntrospection
/*     */   {
/*     */     @NonNull
/*     */     final Runnable run;
/*     */     
/*     */     @NonNull
/*     */     final Scheduler.Worker worker;
/*     */     volatile boolean disposed;
/*     */     
/*     */     PeriodicDirectTask(@NonNull Runnable run, @NonNull Scheduler.Worker worker) {
/* 524 */       this.run = run;
/* 525 */       this.worker = worker;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 530 */       if (!this.disposed) {
/*     */         try {
/* 532 */           this.run.run();
/* 533 */         } catch (Throwable ex) {
/* 534 */           Exceptions.throwIfFatal(ex);
/* 535 */           this.worker.dispose();
/* 536 */           throw ExceptionHelper.wrapOrThrow(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 543 */       this.disposed = true;
/* 544 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 549 */       return this.disposed;
/*     */     }
/*     */ 
/*     */     
/*     */     public Runnable getWrappedRunnable() {
/* 554 */       return this.run;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DisposeTask
/*     */     implements Disposable, Runnable, SchedulerRunnableIntrospection
/*     */   {
/*     */     @NonNull
/*     */     final Runnable decoratedRun;
/*     */     @NonNull
/*     */     final Scheduler.Worker w;
/*     */     @Nullable
/*     */     Thread runner;
/*     */     
/*     */     DisposeTask(@NonNull Runnable decoratedRun, @NonNull Scheduler.Worker w) {
/* 570 */       this.decoratedRun = decoratedRun;
/* 571 */       this.w = w;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 576 */       this.runner = Thread.currentThread();
/*     */       try {
/* 578 */         this.decoratedRun.run();
/*     */       } finally {
/* 580 */         dispose();
/* 581 */         this.runner = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 587 */       if (this.runner == Thread.currentThread() && this.w instanceof NewThreadWorker) {
/* 588 */         ((NewThreadWorker)this.w).shutdown();
/*     */       } else {
/* 590 */         this.w.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 596 */       return this.w.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public Runnable getWrappedRunnable() {
/* 601 */       return this.decoratedRun;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Scheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */