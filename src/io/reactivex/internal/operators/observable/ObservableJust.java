/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.fuseable.ScalarCallable;
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
/*    */ public final class ObservableJust<T>
/*    */   extends Observable<T>
/*    */   implements ScalarCallable<T>
/*    */ {
/*    */   private final T value;
/*    */   
/*    */   public ObservableJust(T value) {
/* 28 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 33 */     ObservableScalarXMap.ScalarDisposable<T> sd = new ObservableScalarXMap.ScalarDisposable<T>(observer, this.value);
/* 34 */     observer.onSubscribe((Disposable)sd);
/* 35 */     sd.run();
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() {
/* 40 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableJust.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */