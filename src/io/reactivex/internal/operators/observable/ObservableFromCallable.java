/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.observers.DeferredScalarDisposable;
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
/*    */ public final class ObservableFromCallable<T>
/*    */   extends Observable<T>
/*    */   implements Callable<T>
/*    */ {
/*    */   final Callable<? extends T> callable;
/*    */   
/*    */   public ObservableFromCallable(Callable<? extends T> callable) {
/* 31 */     this.callable = callable;
/*    */   }
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/*    */     T value;
/* 36 */     DeferredScalarDisposable<T> d = new DeferredScalarDisposable(observer);
/* 37 */     observer.onSubscribe((Disposable)d);
/* 38 */     if (d.isDisposed()) {
/*    */       return;
/*    */     }
/*    */     
/*    */     try {
/* 43 */       value = (T)ObjectHelper.requireNonNull(this.callable.call(), "Callable returned null");
/* 44 */     } catch (Throwable e) {
/* 45 */       Exceptions.throwIfFatal(e);
/* 46 */       if (!d.isDisposed()) {
/* 47 */         observer.onError(e);
/*    */       } else {
/* 49 */         RxJavaPlugins.onError(e);
/*    */       } 
/*    */       return;
/*    */     } 
/* 53 */     d.complete(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() throws Exception {
/* 58 */     return (T)ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFromCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */