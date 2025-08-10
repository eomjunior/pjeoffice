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
/*    */ 
/*    */ public final class CompletableError
/*    */   extends Completable
/*    */ {
/*    */   final Throwable error;
/*    */   
/*    */   public CompletableError(Throwable error) {
/* 24 */     this.error = error;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 29 */     EmptyDisposable.error(this.error, observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */