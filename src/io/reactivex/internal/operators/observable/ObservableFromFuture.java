/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.observers.DeferredScalarDisposable;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public final class ObservableFromFuture<T>
/*    */   extends Observable<T>
/*    */ {
/*    */   final Future<? extends T> future;
/*    */   final long timeout;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public ObservableFromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
/* 29 */     this.future = future;
/* 30 */     this.timeout = timeout;
/* 31 */     this.unit = unit;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 36 */     DeferredScalarDisposable<T> d = new DeferredScalarDisposable(observer);
/* 37 */     observer.onSubscribe((Disposable)d);
/* 38 */     if (!d.isDisposed()) {
/*    */       T v;
/*    */       try {
/* 41 */         v = (T)ObjectHelper.requireNonNull((this.unit != null) ? this.future.get(this.timeout, this.unit) : this.future.get(), "Future returned null");
/* 42 */       } catch (Throwable ex) {
/* 43 */         Exceptions.throwIfFatal(ex);
/* 44 */         if (!d.isDisposed()) {
/* 45 */           observer.onError(ex);
/*    */         }
/*    */         return;
/*    */       } 
/* 49 */       d.complete(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFromFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */