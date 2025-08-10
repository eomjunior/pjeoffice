/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
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
/*    */ public final class SingleFromUnsafeSource<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   
/*    */   public SingleFromUnsafeSource(SingleSource<T> source) {
/* 22 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 27 */     this.source.subscribe(observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFromUnsafeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */