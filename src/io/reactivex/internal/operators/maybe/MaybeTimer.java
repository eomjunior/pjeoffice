/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
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
/*    */ 
/*    */ 
/*    */ public final class MaybeTimer
/*    */   extends Maybe<Long>
/*    */ {
/*    */   final long delay;
/*    */   final TimeUnit unit;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public MaybeTimer(long delay, TimeUnit unit, Scheduler scheduler) {
/* 35 */     this.delay = delay;
/* 36 */     this.unit = unit;
/* 37 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super Long> observer) {
/* 42 */     TimerDisposable parent = new TimerDisposable(observer);
/* 43 */     observer.onSubscribe(parent);
/* 44 */     parent.setFuture(this.scheduler.scheduleDirect(parent, this.delay, this.unit));
/*    */   }
/*    */   
/*    */   static final class TimerDisposable
/*    */     extends AtomicReference<Disposable> implements Disposable, Runnable {
/*    */     private static final long serialVersionUID = 2875964065294031672L;
/*    */     final MaybeObserver<? super Long> downstream;
/*    */     
/*    */     TimerDisposable(MaybeObserver<? super Long> downstream) {
/* 53 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 58 */       this.downstream.onSuccess(Long.valueOf(0L));
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 63 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 68 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */     
/*    */     void setFuture(Disposable d) {
/* 72 */       DisposableHelper.replace(this, d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */