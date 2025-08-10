/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ public final class SingleUnsubscribeOn<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public SingleUnsubscribeOn(SingleSource<T> source, Scheduler scheduler) {
/* 34 */     this.source = source;
/* 35 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 40 */     this.source.subscribe(new UnsubscribeOnSingleObserver<T>(observer, this.scheduler));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class UnsubscribeOnSingleObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements SingleObserver<T>, Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = 3256698449646456986L;
/*    */     
/*    */     final SingleObserver<? super T> downstream;
/*    */     final Scheduler scheduler;
/*    */     Disposable ds;
/*    */     
/*    */     UnsubscribeOnSingleObserver(SingleObserver<? super T> actual, Scheduler scheduler) {
/* 55 */       this.downstream = actual;
/* 56 */       this.scheduler = scheduler;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 61 */       Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/* 62 */       if (d != DisposableHelper.DISPOSED) {
/* 63 */         this.ds = d;
/* 64 */         this.scheduler.scheduleDirect(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 70 */       this.ds.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 75 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 80 */       if (DisposableHelper.setOnce(this, d)) {
/* 81 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 87 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 92 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleUnsubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */