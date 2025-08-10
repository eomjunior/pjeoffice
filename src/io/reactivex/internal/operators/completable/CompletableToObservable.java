/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.observers.BasicQueueDisposable;
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
/*    */ public final class CompletableToObservable<T>
/*    */   extends Observable<T>
/*    */ {
/*    */   final CompletableSource source;
/*    */   
/*    */   public CompletableToObservable(CompletableSource source) {
/* 31 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 36 */     this.source.subscribe(new ObserverCompletableObserver(observer));
/*    */   }
/*    */   
/*    */   static final class ObserverCompletableObserver
/*    */     extends BasicQueueDisposable<Void>
/*    */     implements CompletableObserver
/*    */   {
/*    */     final Observer<?> observer;
/*    */     Disposable upstream;
/*    */     
/*    */     ObserverCompletableObserver(Observer<?> observer) {
/* 47 */       this.observer = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 52 */       this.observer.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 57 */       this.observer.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 62 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 63 */         this.upstream = d;
/* 64 */         this.observer.onSubscribe((Disposable)this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public int requestFusion(int mode) {
/* 70 */       return mode & 0x2;
/*    */     }
/*    */ 
/*    */     
/*    */     public Void poll() throws Exception {
/* 75 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isEmpty() {
/* 80 */       return true;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void clear() {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 90 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 95 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableToObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */