/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
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
/*    */ public final class CompletableDefer
/*    */   extends Completable
/*    */ {
/*    */   final Callable<? extends CompletableSource> completableSupplier;
/*    */   
/*    */   public CompletableDefer(Callable<? extends CompletableSource> completableSupplier) {
/* 28 */     this.completableSupplier = completableSupplier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/*    */     CompletableSource c;
/*    */     try {
/* 36 */       c = (CompletableSource)ObjectHelper.requireNonNull(this.completableSupplier.call(), "The completableSupplier returned a null CompletableSource");
/* 37 */     } catch (Throwable e) {
/* 38 */       Exceptions.throwIfFatal(e);
/* 39 */       EmptyDisposable.error(e, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 43 */     c.subscribe(observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */