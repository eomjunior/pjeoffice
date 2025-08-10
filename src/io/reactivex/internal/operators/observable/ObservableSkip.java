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
/*    */ public final class ObservableSkip<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final long n;
/*    */   
/*    */   public ObservableSkip(ObservableSource<T> source, long n) {
/* 23 */     super(source);
/* 24 */     this.n = n;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 29 */     this.source.subscribe(new SkipObserver<T>(observer, this.n));
/*    */   }
/*    */   
/*    */   static final class SkipObserver<T>
/*    */     implements Observer<T>, Disposable {
/*    */     final Observer<? super T> downstream;
/*    */     long remaining;
/*    */     Disposable upstream;
/*    */     
/*    */     SkipObserver(Observer<? super T> actual, long n) {
/* 39 */       this.downstream = actual;
/* 40 */       this.remaining = n;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 45 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 46 */         this.upstream = d;
/* 47 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 53 */       if (this.remaining != 0L) {
/* 54 */         this.remaining--;
/*    */       } else {
/* 56 */         this.downstream.onNext(t);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 62 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 67 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 72 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 77 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSkip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */