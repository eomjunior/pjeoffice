/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.disposables.Disposables;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Action;
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
/*    */ public final class MaybeFromAction<T>
/*    */   extends Maybe<T>
/*    */   implements Callable<T>
/*    */ {
/*    */   final Action action;
/*    */   
/*    */   public MaybeFromAction(Action action) {
/* 34 */     this.action = action;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 39 */     Disposable d = Disposables.empty();
/* 40 */     observer.onSubscribe(d);
/*    */     
/* 42 */     if (!d.isDisposed()) {
/*    */       
/*    */       try {
/* 45 */         this.action.run();
/* 46 */       } catch (Throwable ex) {
/* 47 */         Exceptions.throwIfFatal(ex);
/* 48 */         if (!d.isDisposed()) {
/* 49 */           observer.onError(ex);
/*    */         } else {
/* 51 */           RxJavaPlugins.onError(ex);
/*    */         } 
/*    */         
/*    */         return;
/*    */       } 
/* 56 */       if (!d.isDisposed()) {
/* 57 */         observer.onComplete();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() throws Exception {
/* 64 */     this.action.run();
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFromAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */