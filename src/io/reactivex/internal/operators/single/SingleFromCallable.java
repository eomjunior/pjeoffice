/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.disposables.Disposables;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ public final class SingleFromCallable<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final Callable<? extends T> callable;
/*    */   
/*    */   public SingleFromCallable(Callable<? extends T> callable) {
/* 30 */     this.callable = callable;
/*    */   }
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*    */     T value;
/* 35 */     Disposable d = Disposables.empty();
/* 36 */     observer.onSubscribe(d);
/*    */     
/* 38 */     if (d.isDisposed()) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 44 */       value = (T)ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value");
/* 45 */     } catch (Throwable ex) {
/* 46 */       Exceptions.throwIfFatal(ex);
/* 47 */       if (!d.isDisposed()) {
/* 48 */         observer.onError(ex);
/*    */       } else {
/* 50 */         RxJavaPlugins.onError(ex);
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 55 */     if (!d.isDisposed())
/* 56 */       observer.onSuccess(value); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFromCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */