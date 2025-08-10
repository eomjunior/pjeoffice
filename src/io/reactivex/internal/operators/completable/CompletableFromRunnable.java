/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.disposables.Disposables;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public final class CompletableFromRunnable
/*    */   extends Completable
/*    */ {
/*    */   final Runnable runnable;
/*    */   
/*    */   public CompletableFromRunnable(Runnable runnable) {
/* 28 */     this.runnable = runnable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 33 */     Disposable d = Disposables.empty();
/* 34 */     observer.onSubscribe(d);
/*    */     try {
/* 36 */       this.runnable.run();
/* 37 */     } catch (Throwable e) {
/* 38 */       Exceptions.throwIfFatal(e);
/* 39 */       if (!d.isDisposed()) {
/* 40 */         observer.onError(e);
/*    */       } else {
/* 42 */         RxJavaPlugins.onError(e);
/*    */       } 
/*    */       return;
/*    */     } 
/* 46 */     if (!d.isDisposed())
/* 47 */       observer.onComplete(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableFromRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */