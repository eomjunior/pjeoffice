/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.disposables.Disposables;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Action;
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
/*    */ public final class CompletableFromAction
/*    */   extends Completable
/*    */ {
/*    */   final Action run;
/*    */   
/*    */   public CompletableFromAction(Action run) {
/* 27 */     this.run = run;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 32 */     Disposable d = Disposables.empty();
/* 33 */     observer.onSubscribe(d);
/*    */     try {
/* 35 */       this.run.run();
/* 36 */     } catch (Throwable e) {
/* 37 */       Exceptions.throwIfFatal(e);
/* 38 */       if (!d.isDisposed()) {
/* 39 */         observer.onError(e);
/*    */       } else {
/* 41 */         RxJavaPlugins.onError(e);
/*    */       } 
/*    */       return;
/*    */     } 
/* 45 */     if (!d.isDisposed())
/* 46 */       observer.onComplete(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableFromAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */