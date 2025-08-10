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
/*    */ public final class MaybeFromCallable<T>
/*    */   extends Maybe<T>
/*    */   implements Callable<T>
/*    */ {
/*    */   final Callable<? extends T> callable;
/*    */   
/*    */   public MaybeFromCallable(Callable<? extends T> callable) {
/* 33 */     this.callable = callable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 38 */     Disposable d = Disposables.empty();
/* 39 */     observer.onSubscribe(d);
/*    */     
/* 41 */     if (!d.isDisposed()) {
/*    */       T v;
/*    */ 
/*    */       
/*    */       try {
/* 46 */         v = this.callable.call();
/* 47 */       } catch (Throwable ex) {
/* 48 */         Exceptions.throwIfFatal(ex);
/* 49 */         if (!d.isDisposed()) {
/* 50 */           observer.onError(ex);
/*    */         } else {
/* 52 */           RxJavaPlugins.onError(ex);
/*    */         } 
/*    */         
/*    */         return;
/*    */       } 
/* 57 */       if (!d.isDisposed()) {
/* 58 */         if (v == null) {
/* 59 */           observer.onComplete();
/*    */         } else {
/* 61 */           observer.onSuccess(v);
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() throws Exception {
/* 69 */     return this.callable.call();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFromCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */