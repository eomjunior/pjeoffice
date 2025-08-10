/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableLastMaybe<T>
/*    */   extends Maybe<T>
/*    */ {
/*    */   final ObservableSource<T> source;
/*    */   
/*    */   public ObservableLastMaybe(ObservableSource<T> source) {
/* 31 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 38 */     this.source.subscribe(new LastObserver<T>(observer));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class LastObserver<T>
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     T item;
/*    */     
/*    */     LastObserver(MaybeObserver<? super T> downstream) {
/* 50 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 55 */       this.upstream.dispose();
/* 56 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 61 */       return (this.upstream == DisposableHelper.DISPOSED);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 66 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 67 */         this.upstream = d;
/*    */         
/* 69 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 75 */       this.item = t;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 80 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 81 */       this.item = null;
/* 82 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 87 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 88 */       T v = this.item;
/* 89 */       if (v != null) {
/* 90 */         this.item = null;
/* 91 */         this.downstream.onSuccess(v);
/*    */       } else {
/* 93 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableLastMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */