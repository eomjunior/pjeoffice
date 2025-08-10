/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeFromRunnable<T>
/*    */   extends Maybe<T>
/*    */   implements Callable<T>
/*    */ {
/*    */   final Runnable runnable;
/*    */   
/*    */   public MaybeFromRunnable(Runnable runnable) {
/* 33 */     this.runnable = runnable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 38 */     Disposable d = Disposables.empty();
/* 39 */     observer.onSubscribe(d);
/*    */     
/* 41 */     if (!d.isDisposed()) {
/*    */       
/*    */       try {
/* 44 */         this.runnable.run();
/* 45 */       } catch (Throwable ex) {
/* 46 */         Exceptions.throwIfFatal(ex);
/* 47 */         if (!d.isDisposed()) {
/* 48 */           observer.onError(ex);
/*    */         } else {
/* 50 */           RxJavaPlugins.onError(ex);
/*    */         } 
/*    */         
/*    */         return;
/*    */       } 
/* 55 */       if (!d.isDisposed()) {
/* 56 */         observer.onComplete();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() throws Exception {
/* 63 */     this.runnable.run();
/* 64 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFromRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */