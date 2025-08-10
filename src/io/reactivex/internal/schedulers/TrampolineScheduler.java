/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.disposables.Disposables;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TrampolineScheduler
/*     */   extends Scheduler
/*     */ {
/*  34 */   private static final TrampolineScheduler INSTANCE = new TrampolineScheduler();
/*     */   
/*     */   public static TrampolineScheduler instance() {
/*  37 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/*  43 */     return new TrampolineWorker();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run) {
/*  52 */     RxJavaPlugins.onSchedule(run).run();
/*  53 */     return (Disposable)EmptyDisposable.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run, long delay, TimeUnit unit) {
/*     */     try {
/*  60 */       unit.sleep(delay);
/*  61 */       RxJavaPlugins.onSchedule(run).run();
/*  62 */     } catch (InterruptedException ex) {
/*  63 */       Thread.currentThread().interrupt();
/*  64 */       RxJavaPlugins.onError(ex);
/*     */     } 
/*  66 */     return (Disposable)EmptyDisposable.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class TrampolineWorker extends Scheduler.Worker implements Disposable {
/*  70 */     final PriorityBlockingQueue<TrampolineScheduler.TimedRunnable> queue = new PriorityBlockingQueue<TrampolineScheduler.TimedRunnable>();
/*     */     
/*  72 */     private final AtomicInteger wip = new AtomicInteger();
/*     */     
/*  74 */     final AtomicInteger counter = new AtomicInteger();
/*     */     
/*     */     volatile boolean disposed;
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable action) {
/*  81 */       return enqueue(action, now(TimeUnit.MILLISECONDS));
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
/*  87 */       long execTime = now(TimeUnit.MILLISECONDS) + unit.toMillis(delayTime);
/*     */       
/*  89 */       return enqueue(new TrampolineScheduler.SleepingRunnable(action, this, execTime), execTime);
/*     */     }
/*     */     
/*     */     Disposable enqueue(Runnable action, long execTime) {
/*  93 */       if (this.disposed) {
/*  94 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/*  96 */       TrampolineScheduler.TimedRunnable timedRunnable = new TrampolineScheduler.TimedRunnable(action, Long.valueOf(execTime), this.counter.incrementAndGet());
/*  97 */       this.queue.add(timedRunnable);
/*     */       
/*  99 */       if (this.wip.getAndIncrement() == 0) {
/* 100 */         int missed = 1;
/*     */         
/*     */         while (true) {
/* 103 */           if (this.disposed) {
/* 104 */             this.queue.clear();
/* 105 */             return (Disposable)EmptyDisposable.INSTANCE;
/*     */           } 
/* 107 */           TrampolineScheduler.TimedRunnable polled = this.queue.poll();
/* 108 */           if (polled == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 115 */             missed = this.wip.addAndGet(-missed);
/* 116 */             if (missed == 0)
/*     */               break;  continue;
/*     */           }  if (!polled.disposed)
/*     */             polled.run.run(); 
/*     */         } 
/* 121 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       } 
/*     */       
/* 124 */       return Disposables.fromRunnable(new AppendToQueueTask(timedRunnable));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 130 */       this.disposed = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 135 */       return this.disposed;
/*     */     }
/*     */     
/*     */     final class AppendToQueueTask implements Runnable {
/*     */       final TrampolineScheduler.TimedRunnable timedRunnable;
/*     */       
/*     */       AppendToQueueTask(TrampolineScheduler.TimedRunnable timedRunnable) {
/* 142 */         this.timedRunnable = timedRunnable;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 147 */         this.timedRunnable.disposed = true;
/* 148 */         TrampolineScheduler.TrampolineWorker.this.queue.remove(this.timedRunnable);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TimedRunnable
/*     */     implements Comparable<TimedRunnable> {
/*     */     final Runnable run;
/*     */     final long execTime;
/*     */     final int count;
/*     */     volatile boolean disposed;
/*     */     
/*     */     TimedRunnable(Runnable run, Long execTime, int count) {
/* 161 */       this.run = run;
/* 162 */       this.execTime = execTime.longValue();
/* 163 */       this.count = count;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(TimedRunnable that) {
/* 168 */       int result = ObjectHelper.compare(this.execTime, that.execTime);
/* 169 */       if (result == 0) {
/* 170 */         return ObjectHelper.compare(this.count, that.count);
/*     */       }
/* 172 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SleepingRunnable implements Runnable {
/*     */     private final Runnable run;
/*     */     private final TrampolineScheduler.TrampolineWorker worker;
/*     */     private final long execTime;
/*     */     
/*     */     SleepingRunnable(Runnable run, TrampolineScheduler.TrampolineWorker worker, long execTime) {
/* 182 */       this.run = run;
/* 183 */       this.worker = worker;
/* 184 */       this.execTime = execTime;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 189 */       if (!this.worker.disposed) {
/* 190 */         long t = this.worker.now(TimeUnit.MILLISECONDS);
/* 191 */         if (this.execTime > t) {
/* 192 */           long delay = this.execTime - t;
/*     */           try {
/* 194 */             Thread.sleep(delay);
/* 195 */           } catch (InterruptedException e) {
/* 196 */             Thread.currentThread().interrupt();
/* 197 */             RxJavaPlugins.onError(e);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 202 */         if (!this.worker.disposed)
/* 203 */           this.run.run(); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/TrampolineScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */