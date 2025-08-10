/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
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
/*    */ public final class SingleDefer<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final Callable<? extends SingleSource<? extends T>> singleSupplier;
/*    */   
/*    */   public SingleDefer(Callable<? extends SingleSource<? extends T>> singleSupplier) {
/* 28 */     this.singleSupplier = singleSupplier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*    */     SingleSource<? extends T> next;
/*    */     try {
/* 36 */       next = (SingleSource<? extends T>)ObjectHelper.requireNonNull(this.singleSupplier.call(), "The singleSupplier returned a null SingleSource");
/* 37 */     } catch (Throwable e) {
/* 38 */       Exceptions.throwIfFatal(e);
/* 39 */       EmptyDisposable.error(e, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 43 */     next.subscribe(observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */