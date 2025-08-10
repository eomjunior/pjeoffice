/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.FuseToObservable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableCountSingle<T>
/*    */   extends Single<Long>
/*    */   implements FuseToObservable<Long>
/*    */ {
/*    */   final ObservableSource<T> source;
/*    */   
/*    */   public ObservableCountSingle(ObservableSource<T> source) {
/* 25 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(SingleObserver<? super Long> t) {
/* 30 */     this.source.subscribe(new CountObserver(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public Observable<Long> fuseToObservable() {
/* 35 */     return RxJavaPlugins.onAssembly(new ObservableCount<T>(this.source));
/*    */   }
/*    */   
/*    */   static final class CountObserver
/*    */     implements Observer<Object>, Disposable
/*    */   {
/*    */     final SingleObserver<? super Long> downstream;
/*    */     Disposable upstream;
/*    */     long count;
/*    */     
/*    */     CountObserver(SingleObserver<? super Long> downstream) {
/* 46 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 51 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 52 */         this.upstream = d;
/* 53 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 59 */       this.upstream.dispose();
/* 60 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 65 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(Object t) {
/* 70 */       this.count++;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 75 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 76 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 81 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 82 */       this.downstream.onSuccess(Long.valueOf(this.count));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableCountSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */