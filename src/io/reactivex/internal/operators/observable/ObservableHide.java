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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableHide<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   public ObservableHide(ObservableSource<T> source) {
/* 29 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> o) {
/* 34 */     this.source.subscribe(new HideDisposable<T>(o));
/*    */   }
/*    */   
/*    */   static final class HideDisposable<T>
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     final Observer<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     HideDisposable(Observer<? super T> downstream) {
/* 44 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 49 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 54 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 59 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 60 */         this.upstream = d;
/* 61 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 67 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 72 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 77 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */