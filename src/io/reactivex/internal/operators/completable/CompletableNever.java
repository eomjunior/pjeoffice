/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
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
/*    */ public final class CompletableNever
/*    */   extends Completable
/*    */ {
/* 20 */   public static final Completable INSTANCE = new CompletableNever();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 27 */     observer.onSubscribe((Disposable)EmptyDisposable.NEVER);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */