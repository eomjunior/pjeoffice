/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.disposables.Disposables;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public final class CompletableFromCallable
/*    */   extends Completable
/*    */ {
/*    */   final Callable<?> callable;
/*    */   
/*    */   public CompletableFromCallable(Callable<?> callable) {
/* 28 */     this.callable = callable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 33 */     Disposable d = Disposables.empty();
/* 34 */     observer.onSubscribe(d);
/*    */     try {
/* 36 */       this.callable.call();
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


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableFromCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */