/*    */ package io.reactivex.internal.operators.mixed;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.Notification;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.annotations.Experimental;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ 
/*    */ @Experimental
/*    */ public final class MaterializeSingleObserver<T>
/*    */   implements SingleObserver<T>, MaybeObserver<T>, CompletableObserver, Disposable
/*    */ {
/*    */   final SingleObserver<? super Notification<T>> downstream;
/*    */   Disposable upstream;
/*    */   
/*    */   public MaterializeSingleObserver(SingleObserver<? super Notification<T>> downstream) {
/* 36 */     this.downstream = downstream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 41 */     if (DisposableHelper.validate(this.upstream, d)) {
/* 42 */       this.upstream = d;
/* 43 */       this.downstream.onSubscribe(this);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 49 */     this.downstream.onSuccess(Notification.createOnComplete());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSuccess(T t) {
/* 54 */     this.downstream.onSuccess(Notification.createOnNext(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable e) {
/* 59 */     this.downstream.onSuccess(Notification.createOnError(e));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 64 */     return this.upstream.isDisposed();
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 69 */     this.upstream.dispose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/MaterializeSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */