/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableInterval
/*    */   extends Observable<Long>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   final long initialDelay;
/*    */   final long period;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public ObservableInterval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/* 32 */     this.initialDelay = initialDelay;
/* 33 */     this.period = period;
/* 34 */     this.unit = unit;
/* 35 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super Long> observer) {
/* 40 */     IntervalObserver is = new IntervalObserver(observer);
/* 41 */     observer.onSubscribe(is);
/*    */     
/* 43 */     Scheduler sch = this.scheduler;
/*    */     
/* 45 */     if (sch instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
/* 46 */       Scheduler.Worker worker = sch.createWorker();
/* 47 */       is.setResource((Disposable)worker);
/* 48 */       worker.schedulePeriodically(is, this.initialDelay, this.period, this.unit);
/*    */     } else {
/* 50 */       Disposable d = sch.schedulePeriodicallyDirect(is, this.initialDelay, this.period, this.unit);
/* 51 */       is.setResource(d);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   static final class IntervalObserver
/*    */     extends AtomicReference<Disposable>
/*    */     implements Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = 346773832286157679L;
/*    */     
/*    */     final Observer<? super Long> downstream;
/*    */     long count;
/*    */     
/*    */     IntervalObserver(Observer<? super Long> downstream) {
/* 66 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 71 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 76 */       return (get() == DisposableHelper.DISPOSED);
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 81 */       if (get() != DisposableHelper.DISPOSED) {
/* 82 */         this.downstream.onNext(Long.valueOf(this.count++));
/*    */       }
/*    */     }
/*    */     
/*    */     public void setResource(Disposable d) {
/* 87 */       DisposableHelper.setOnce(this, d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableInterval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */