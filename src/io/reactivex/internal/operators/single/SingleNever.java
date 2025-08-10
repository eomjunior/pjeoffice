/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
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
/*    */ public final class SingleNever
/*    */   extends Single<Object>
/*    */ {
/* 20 */   public static final Single<Object> INSTANCE = new SingleNever();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Object> observer) {
/* 27 */     observer.onSubscribe((Disposable)EmptyDisposable.NEVER);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */