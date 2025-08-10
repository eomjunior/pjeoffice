/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
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
/*    */ 
/*    */ 
/*    */ public final class CompletableTimer
/*    */   extends Completable
/*    */ {
/*    */   final long delay;
/*    */   final TimeUnit unit;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public CompletableTimer(long delay, TimeUnit unit, Scheduler scheduler) {
/* 33 */     this.delay = delay;
/* 34 */     this.unit = unit;
/* 35 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 40 */     TimerDisposable parent = new TimerDisposable(observer);
/* 41 */     observer.onSubscribe(parent);
/* 42 */     parent.setFuture(this.scheduler.scheduleDirect(parent, this.delay, this.unit));
/*    */   }
/*    */   
/*    */   static final class TimerDisposable
/*    */     extends AtomicReference<Disposable> implements Disposable, Runnable {
/*    */     private static final long serialVersionUID = 3167244060586201109L;
/*    */     final CompletableObserver downstream;
/*    */     
/*    */     TimerDisposable(CompletableObserver downstream) {
/* 51 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 56 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 61 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 66 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */     
/*    */     void setFuture(Disposable d) {
/* 70 */       DisposableHelper.replace(this, d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */