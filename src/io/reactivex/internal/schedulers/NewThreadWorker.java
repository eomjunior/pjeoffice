/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableContainer;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
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
/*     */ public class NewThreadWorker
/*     */   extends Scheduler.Worker
/*     */   implements Disposable
/*     */ {
/*     */   private final ScheduledExecutorService executor;
/*     */   volatile boolean disposed;
/*     */   
/*     */   public NewThreadWorker(ThreadFactory threadFactory) {
/*  36 */     this.executor = SchedulerPoolFactory.create(threadFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable schedule(@NonNull Runnable run) {
/*  42 */     return schedule(run, 0L, null);
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
/*  48 */     if (this.disposed) {
/*  49 */       return (Disposable)EmptyDisposable.INSTANCE;
/*     */     }
/*  51 */     return scheduleActual(action, delayTime, unit, null);
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
/*     */   public Disposable scheduleDirect(Runnable run, long delayTime, TimeUnit unit) {
/*  63 */     ScheduledDirectTask task = new ScheduledDirectTask(RxJavaPlugins.onSchedule(run));
/*     */     try {
/*     */       Future<?> f;
/*  66 */       if (delayTime <= 0L) {
/*  67 */         f = this.executor.submit(task);
/*     */       } else {
/*  69 */         f = this.executor.schedule(task, delayTime, unit);
/*     */       } 
/*  71 */       task.setFuture(f);
/*  72 */       return task;
/*  73 */     } catch (RejectedExecutionException ex) {
/*  74 */       RxJavaPlugins.onError(ex);
/*  75 */       return (Disposable)EmptyDisposable.INSTANCE;
/*     */     } 
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
/*     */   public Disposable schedulePeriodicallyDirect(Runnable run, long initialDelay, long period, TimeUnit unit) {
/*  89 */     Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*  90 */     if (period <= 0L) {
/*     */       
/*  92 */       InstantPeriodicTask periodicWrapper = new InstantPeriodicTask(decoratedRun, this.executor);
/*     */       try {
/*     */         Future<?> f;
/*  95 */         if (initialDelay <= 0L) {
/*  96 */           f = this.executor.submit(periodicWrapper);
/*     */         } else {
/*  98 */           f = this.executor.schedule(periodicWrapper, initialDelay, unit);
/*     */         } 
/* 100 */         periodicWrapper.setFirst(f);
/* 101 */       } catch (RejectedExecutionException ex) {
/* 102 */         RxJavaPlugins.onError(ex);
/* 103 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       } 
/*     */       
/* 106 */       return periodicWrapper;
/*     */     } 
/* 108 */     ScheduledDirectPeriodicTask task = new ScheduledDirectPeriodicTask(decoratedRun);
/*     */     try {
/* 110 */       Future<?> f = this.executor.scheduleAtFixedRate(task, initialDelay, period, unit);
/* 111 */       task.setFuture(f);
/* 112 */       return task;
/* 113 */     } catch (RejectedExecutionException ex) {
/* 114 */       RxJavaPlugins.onError(ex);
/* 115 */       return (Disposable)EmptyDisposable.INSTANCE;
/*     */     } 
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
/*     */   @NonNull
/*     */   public ScheduledRunnable scheduleActual(Runnable run, long delayTime, @NonNull TimeUnit unit, @Nullable DisposableContainer parent) {
/* 132 */     Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
/*     */     
/* 134 */     ScheduledRunnable sr = new ScheduledRunnable(decoratedRun, parent);
/*     */     
/* 136 */     if (parent != null && 
/* 137 */       !parent.add(sr)) {
/* 138 */       return sr;
/*     */     }
/*     */     
/*     */     try {
/*     */       Future<?> f;
/*     */       
/* 144 */       if (delayTime <= 0L) {
/* 145 */         f = this.executor.submit(sr);
/*     */       } else {
/* 147 */         f = this.executor.schedule(sr, delayTime, unit);
/*     */       } 
/* 149 */       sr.setFuture(f);
/* 150 */     } catch (RejectedExecutionException ex) {
/* 151 */       if (parent != null) {
/* 152 */         parent.remove(sr);
/*     */       }
/* 154 */       RxJavaPlugins.onError(ex);
/*     */     } 
/*     */     
/* 157 */     return sr;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 162 */     if (!this.disposed) {
/* 163 */       this.disposed = true;
/* 164 */       this.executor.shutdownNow();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 172 */     if (!this.disposed) {
/* 173 */       this.disposed = true;
/* 174 */       this.executor.shutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 180 */     return this.disposed;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/NewThreadWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */