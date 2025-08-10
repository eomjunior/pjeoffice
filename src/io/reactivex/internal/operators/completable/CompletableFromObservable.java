/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
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
/*    */ public final class CompletableFromObservable<T>
/*    */   extends Completable
/*    */ {
/*    */   final ObservableSource<T> observable;
/*    */   
/*    */   public CompletableFromObservable(ObservableSource<T> observable) {
/* 24 */     this.observable = observable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 29 */     this.observable.subscribe(new CompletableFromObservableObserver(observer));
/*    */   }
/*    */   
/*    */   static final class CompletableFromObservableObserver<T> implements Observer<T> {
/*    */     final CompletableObserver co;
/*    */     
/*    */     CompletableFromObservableObserver(CompletableObserver co) {
/* 36 */       this.co = co;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 41 */       this.co.onSubscribe(d);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onNext(T value) {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 51 */       this.co.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 56 */       this.co.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableFromObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */