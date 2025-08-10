/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
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
/*    */ public final class ObservableError<T>
/*    */   extends Observable<T>
/*    */ {
/*    */   final Callable<? extends Throwable> errorSupplier;
/*    */   
/*    */   public ObservableError(Callable<? extends Throwable> errorSupplier) {
/* 26 */     this.errorSupplier = errorSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/*    */     Throwable error;
/*    */     try {
/* 33 */       error = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
/* 34 */     } catch (Throwable t) {
/* 35 */       Exceptions.throwIfFatal(t);
/* 36 */       error = t;
/*    */     } 
/* 38 */     EmptyDisposable.error(error, observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */