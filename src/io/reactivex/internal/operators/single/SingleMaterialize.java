/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Notification;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.annotations.Experimental;
/*    */ import io.reactivex.internal.operators.mixed.MaterializeSingleObserver;
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
/*    */ public final class SingleMaterialize<T>
/*    */   extends Single<Notification<T>>
/*    */ {
/*    */   final Single<T> source;
/*    */   
/*    */   public SingleMaterialize(Single<T> source) {
/* 33 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Notification<T>> observer) {
/* 38 */     this.source.subscribe((SingleObserver)new MaterializeSingleObserver(observer));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */