/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
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
/*    */ public final class ObservableTimer
/*    */   extends Observable<Long>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   final long delay;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public ObservableTimer(long delay, TimeUnit unit, Scheduler scheduler) {
/* 28 */     this.delay = delay;
/* 29 */     this.unit = unit;
/* 30 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super Long> observer) {
/* 35 */     TimerObserver ios = new TimerObserver(observer);
/* 36 */     observer.onSubscribe(ios);
/*    */     
/* 38 */     Disposable d = this.scheduler.scheduleDirect(ios, this.delay, this.unit);
/*    */     
/* 40 */     ios.setResource(d);
/*    */   }
/*    */   
/*    */   static final class TimerObserver
/*    */     extends AtomicReference<Disposable>
/*    */     implements Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = -2809475196591179431L;
/*    */     final Observer<? super Long> downstream;
/*    */     
/*    */     TimerObserver(Observer<? super Long> downstream) {
/* 51 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 56 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 61 */       return (get() == DisposableHelper.DISPOSED);
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 66 */       if (!isDisposed()) {
/* 67 */         this.downstream.onNext(Long.valueOf(0L));
/* 68 */         lazySet((Disposable)EmptyDisposable.INSTANCE);
/* 69 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */     
/*    */     public void setResource(Disposable d) {
/* 74 */       DisposableHelper.trySet(this, d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */