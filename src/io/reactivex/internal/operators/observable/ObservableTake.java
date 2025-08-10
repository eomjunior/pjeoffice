/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableTake<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final long limit;
/*    */   
/*    */   public ObservableTake(ObservableSource<T> source, long limit) {
/* 24 */     super(source);
/* 25 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 30 */     this.source.subscribe(new TakeObserver<T>(observer, this.limit));
/*    */   }
/*    */   
/*    */   static final class TakeObserver<T>
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     final Observer<? super T> downstream;
/*    */     boolean done;
/*    */     Disposable upstream;
/*    */     long remaining;
/*    */     
/*    */     TakeObserver(Observer<? super T> actual, long limit) {
/* 42 */       this.downstream = actual;
/* 43 */       this.remaining = limit;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 48 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 49 */         this.upstream = d;
/* 50 */         if (this.remaining == 0L) {
/* 51 */           this.done = true;
/* 52 */           d.dispose();
/* 53 */           EmptyDisposable.complete(this.downstream);
/*    */         } else {
/* 55 */           this.downstream.onSubscribe(this);
/*    */         } 
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 62 */       if (!this.done && this.remaining-- > 0L) {
/* 63 */         boolean stop = (this.remaining == 0L);
/* 64 */         this.downstream.onNext(t);
/* 65 */         if (stop) {
/* 66 */           onComplete();
/*    */         }
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 73 */       if (this.done) {
/* 74 */         RxJavaPlugins.onError(t);
/*    */         
/*    */         return;
/*    */       } 
/* 78 */       this.done = true;
/* 79 */       this.upstream.dispose();
/* 80 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 85 */       if (!this.done) {
/* 86 */         this.done = true;
/* 87 */         this.upstream.dispose();
/* 88 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 94 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 99 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTake.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */