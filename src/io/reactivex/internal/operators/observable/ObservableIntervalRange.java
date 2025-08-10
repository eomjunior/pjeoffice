/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class ObservableIntervalRange
/*     */   extends Observable<Long>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   final long start;
/*     */   final long end;
/*     */   final long initialDelay;
/*     */   final long period;
/*     */   final TimeUnit unit;
/*     */   
/*     */   public ObservableIntervalRange(long start, long end, long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/*  34 */     this.initialDelay = initialDelay;
/*  35 */     this.period = period;
/*  36 */     this.unit = unit;
/*  37 */     this.scheduler = scheduler;
/*  38 */     this.start = start;
/*  39 */     this.end = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super Long> observer) {
/*  44 */     IntervalRangeObserver is = new IntervalRangeObserver(observer, this.start, this.end);
/*  45 */     observer.onSubscribe(is);
/*     */     
/*  47 */     Scheduler sch = this.scheduler;
/*     */     
/*  49 */     if (sch instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
/*  50 */       Scheduler.Worker worker = sch.createWorker();
/*  51 */       is.setResource((Disposable)worker);
/*  52 */       worker.schedulePeriodically(is, this.initialDelay, this.period, this.unit);
/*     */     } else {
/*  54 */       Disposable d = sch.schedulePeriodicallyDirect(is, this.initialDelay, this.period, this.unit);
/*  55 */       is.setResource(d);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class IntervalRangeObserver
/*     */     extends AtomicReference<Disposable>
/*     */     implements Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 1891866368734007884L;
/*     */     
/*     */     final Observer<? super Long> downstream;
/*     */     final long end;
/*     */     long count;
/*     */     
/*     */     IntervalRangeObserver(Observer<? super Long> actual, long start, long end) {
/*  71 */       this.downstream = actual;
/*  72 */       this.count = start;
/*  73 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  78 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  83 */       return (get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  88 */       if (!isDisposed()) {
/*  89 */         long c = this.count;
/*  90 */         this.downstream.onNext(Long.valueOf(c));
/*     */         
/*  92 */         if (c == this.end) {
/*  93 */           DisposableHelper.dispose(this);
/*  94 */           this.downstream.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/*  98 */         this.count = c + 1L;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setResource(Disposable d) {
/* 104 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableIntervalRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */