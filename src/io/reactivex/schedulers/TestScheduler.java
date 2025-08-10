/*     */ package io.reactivex.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.disposables.Disposables;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
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
/*     */ public final class TestScheduler
/*     */   extends Scheduler
/*     */ {
/*  32 */   final Queue<TimedRunnable> queue = new PriorityBlockingQueue<TimedRunnable>(11);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long counter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile long time;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestScheduler() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestScheduler(long delayTime, TimeUnit unit) {
/*  54 */     this.time = unit.toNanos(delayTime);
/*     */   }
/*     */   
/*     */   static final class TimedRunnable
/*     */     implements Comparable<TimedRunnable> {
/*     */     final long time;
/*     */     final Runnable run;
/*     */     final TestScheduler.TestWorker scheduler;
/*     */     final long count;
/*     */     
/*     */     TimedRunnable(TestScheduler.TestWorker scheduler, long time, Runnable run, long count) {
/*  65 */       this.time = time;
/*  66 */       this.run = run;
/*  67 */       this.scheduler = scheduler;
/*  68 */       this.count = count;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  73 */       return String.format("TimedRunnable(time = %d, run = %s)", new Object[] { Long.valueOf(this.time), this.run.toString() });
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(TimedRunnable o) {
/*  78 */       if (this.time == o.time) {
/*  79 */         return ObjectHelper.compare(this.count, o.count);
/*     */       }
/*  81 */       return ObjectHelper.compare(this.time, o.time);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public long now(@NonNull TimeUnit unit) {
/*  87 */     return unit.convert(this.time, TimeUnit.NANOSECONDS);
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
/*     */   public void advanceTimeBy(long delayTime, TimeUnit unit) {
/*  99 */     advanceTimeTo(this.time + unit.toNanos(delayTime), TimeUnit.NANOSECONDS);
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
/*     */   public void advanceTimeTo(long delayTime, TimeUnit unit) {
/* 111 */     long targetTime = unit.toNanos(delayTime);
/* 112 */     triggerActions(targetTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void triggerActions() {
/* 120 */     triggerActions(this.time);
/*     */   }
/*     */   
/*     */   private void triggerActions(long targetTimeInNanoseconds) {
/*     */     while (true) {
/* 125 */       TimedRunnable current = this.queue.peek();
/* 126 */       if (current == null || current.time > targetTimeInNanoseconds) {
/*     */         break;
/*     */       }
/*     */       
/* 130 */       this.time = (current.time == 0L) ? this.time : current.time;
/* 131 */       this.queue.remove(current);
/*     */ 
/*     */       
/* 134 */       if (!current.scheduler.disposed) {
/* 135 */         current.run.run();
/*     */       }
/*     */     } 
/* 138 */     this.time = targetTimeInNanoseconds;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/* 144 */     return new TestWorker();
/*     */   }
/*     */   
/*     */   final class TestWorker
/*     */     extends Scheduler.Worker
/*     */   {
/*     */     volatile boolean disposed;
/*     */     
/*     */     public void dispose() {
/* 153 */       this.disposed = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 158 */       return this.disposed;
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run, long delayTime, @NonNull TimeUnit unit) {
/* 164 */       if (this.disposed) {
/* 165 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/* 167 */       TestScheduler.TimedRunnable timedAction = new TestScheduler.TimedRunnable(this, TestScheduler.this.time + unit.toNanos(delayTime), run, TestScheduler.this.counter++);
/* 168 */       TestScheduler.this.queue.add(timedAction);
/*     */       
/* 170 */       return Disposables.fromRunnable(new QueueRemove(timedAction));
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run) {
/* 176 */       if (this.disposed) {
/* 177 */         return (Disposable)EmptyDisposable.INSTANCE;
/*     */       }
/* 179 */       TestScheduler.TimedRunnable timedAction = new TestScheduler.TimedRunnable(this, 0L, run, TestScheduler.this.counter++);
/* 180 */       TestScheduler.this.queue.add(timedAction);
/* 181 */       return Disposables.fromRunnable(new QueueRemove(timedAction));
/*     */     }
/*     */ 
/*     */     
/*     */     public long now(@NonNull TimeUnit unit) {
/* 186 */       return TestScheduler.this.now(unit);
/*     */     }
/*     */     
/*     */     final class QueueRemove implements Runnable {
/*     */       final TestScheduler.TimedRunnable timedAction;
/*     */       
/*     */       QueueRemove(TestScheduler.TimedRunnable timedAction) {
/* 193 */         this.timedAction = timedAction;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 198 */         TestScheduler.this.queue.remove(this.timedAction);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/schedulers/TestScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */