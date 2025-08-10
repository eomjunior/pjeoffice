/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.disposables.Disposables;
/*    */ import io.reactivex.exceptions.Exceptions;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeErrorCallable<T>
/*    */   extends Maybe<T>
/*    */ {
/*    */   final Callable<? extends Throwable> errorSupplier;
/*    */   
/*    */   public MaybeErrorCallable(Callable<? extends Throwable> errorSupplier) {
/* 33 */     this.errorSupplier = errorSupplier;
/*    */   }
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*    */     Throwable ex;
/* 38 */     observer.onSubscribe(Disposables.disposed());
/*    */ 
/*    */     
/*    */     try {
/* 42 */       ex = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
/* 43 */     } catch (Throwable ex1) {
/* 44 */       Exceptions.throwIfFatal(ex1);
/* 45 */       ex = ex1;
/*    */     } 
/*    */     
/* 48 */     observer.onError(ex);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeErrorCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */