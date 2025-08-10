/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
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
/*    */ public final class SingleError<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final Callable<? extends Throwable> errorSupplier;
/*    */   
/*    */   public SingleError(Callable<? extends Throwable> errorSupplier) {
/* 28 */     this.errorSupplier = errorSupplier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*    */     Throwable error;
/*    */     try {
/* 36 */       error = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
/* 37 */     } catch (Throwable e) {
/* 38 */       Exceptions.throwIfFatal(e);
/* 39 */       error = e;
/*    */     } 
/*    */     
/* 42 */     EmptyDisposable.error(error, observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */