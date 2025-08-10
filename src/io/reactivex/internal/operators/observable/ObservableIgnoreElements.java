/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public final class ObservableIgnoreElements<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   public ObservableIgnoreElements(ObservableSource<T> source) {
/* 22 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> t) {
/* 27 */     this.source.subscribe(new IgnoreObservable<T>(t));
/*    */   }
/*    */   
/*    */   static final class IgnoreObservable<T>
/*    */     implements Observer<T>, Disposable {
/*    */     final Observer<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     IgnoreObservable(Observer<? super T> t) {
/* 36 */       this.downstream = t;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 41 */       this.upstream = d;
/* 42 */       this.downstream.onSubscribe(this);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onNext(T v) {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 52 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 57 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 62 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 67 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableIgnoreElements.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */