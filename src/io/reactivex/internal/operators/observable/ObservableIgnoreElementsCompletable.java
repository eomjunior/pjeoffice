/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.fuseable.FuseToObservable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableIgnoreElementsCompletable<T>
/*    */   extends Completable
/*    */   implements FuseToObservable<T>
/*    */ {
/*    */   final ObservableSource<T> source;
/*    */   
/*    */   public ObservableIgnoreElementsCompletable(ObservableSource<T> source) {
/* 26 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(CompletableObserver t) {
/* 31 */     this.source.subscribe(new IgnoreObservable(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public Observable<T> fuseToObservable() {
/* 36 */     return RxJavaPlugins.onAssembly(new ObservableIgnoreElements<T>(this.source));
/*    */   }
/*    */   
/*    */   static final class IgnoreObservable<T>
/*    */     implements Observer<T>, Disposable {
/*    */     final CompletableObserver downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     IgnoreObservable(CompletableObserver t) {
/* 45 */       this.downstream = t;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 50 */       this.upstream = d;
/* 51 */       this.downstream.onSubscribe(this);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onNext(T v) {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 61 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 66 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 71 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 76 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableIgnoreElementsCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */