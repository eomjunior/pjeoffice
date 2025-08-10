/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
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
/*    */ 
/*    */ public final class CompletableEmpty
/*    */   extends Completable
/*    */ {
/* 20 */   public static final Completable INSTANCE = new CompletableEmpty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void subscribeActual(CompletableObserver observer) {
/* 27 */     EmptyDisposable.complete(observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */