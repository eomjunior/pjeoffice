/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposables;
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
/*    */ public final class SingleJust<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final T value;
/*    */   
/*    */   public SingleJust(T value) {
/* 24 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 29 */     observer.onSubscribe(Disposables.disposed());
/* 30 */     observer.onSuccess(this.value);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleJust.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */