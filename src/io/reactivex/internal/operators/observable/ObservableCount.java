/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ public final class ObservableCount<T>
/*    */   extends AbstractObservableWithUpstream<T, Long>
/*    */ {
/*    */   public ObservableCount(ObservableSource<T> source) {
/* 22 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super Long> t) {
/* 27 */     this.source.subscribe(new CountObserver(t));
/*    */   }
/*    */   
/*    */   static final class CountObserver
/*    */     implements Observer<Object>, Disposable
/*    */   {
/*    */     final Observer<? super Long> downstream;
/*    */     Disposable upstream;
/*    */     long count;
/*    */     
/*    */     CountObserver(Observer<? super Long> downstream) {
/* 38 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 43 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 44 */         this.upstream = d;
/* 45 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 51 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 56 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(Object t) {
/* 61 */       this.count++;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 66 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 71 */       this.downstream.onNext(Long.valueOf(this.count));
/* 72 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */