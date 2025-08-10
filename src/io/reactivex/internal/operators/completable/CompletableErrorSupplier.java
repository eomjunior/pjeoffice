/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import java.util.concurrent.Callable;
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
/*    */ public final class CompletableErrorSupplier
/*    */   extends Completable
/*    */ {
/*    */   final Callable<? extends Throwable> errorSupplier;
/*    */   
/*    */   public CompletableErrorSupplier(Callable<? extends Throwable> errorSupplier) {
/* 28 */     this.errorSupplier = errorSupplier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/*    */     Throwable error;
/*    */     try {
/* 36 */       error = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "The error returned is null");
/* 37 */     } catch (Throwable e) {
/* 38 */       Exceptions.throwIfFatal(e);
/* 39 */       error = e;
/*    */     } 
/*    */     
/* 42 */     EmptyDisposable.error(error, observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableErrorSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */