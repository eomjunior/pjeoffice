/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
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
/*    */ public final class SingleHide<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   
/*    */   public SingleHide(SingleSource<? extends T> source) {
/* 25 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 30 */     this.source.subscribe(new HideSingleObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class HideSingleObserver<T>
/*    */     implements SingleObserver<T>, Disposable
/*    */   {
/*    */     final SingleObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     HideSingleObserver(SingleObserver<? super T> downstream) {
/* 40 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 45 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 50 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 55 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 56 */         this.upstream = d;
/* 57 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 63 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 68 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */