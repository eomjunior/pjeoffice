/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class ObserverResourceWrapper<T>
/*    */   extends AtomicReference<Disposable>
/*    */   implements Observer<T>, Disposable
/*    */ {
/*    */   private static final long serialVersionUID = -8612022020200669122L;
/*    */   final Observer<? super T> downstream;
/* 28 */   final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
/*    */   
/*    */   public ObserverResourceWrapper(Observer<? super T> downstream) {
/* 31 */     this.downstream = downstream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 36 */     if (DisposableHelper.setOnce(this.upstream, d)) {
/* 37 */       this.downstream.onSubscribe(this);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onNext(T t) {
/* 43 */     this.downstream.onNext(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 48 */     dispose();
/* 49 */     this.downstream.onError(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 54 */     dispose();
/* 55 */     this.downstream.onComplete();
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 60 */     DisposableHelper.dispose(this.upstream);
/*    */     
/* 62 */     DisposableHelper.dispose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 67 */     return (this.upstream.get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */   
/*    */   public void setResource(Disposable resource) {
/* 71 */     DisposableHelper.set(this, resource);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObserverResourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */