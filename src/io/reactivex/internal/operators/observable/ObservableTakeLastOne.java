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
/*    */ public final class ObservableTakeLastOne<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   public ObservableTakeLastOne(ObservableSource<T> source) {
/* 22 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 27 */     this.source.subscribe(new TakeLastOneObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class TakeLastOneObserver<T>
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     final Observer<? super T> downstream;
/*    */     Disposable upstream;
/*    */     T value;
/*    */     
/*    */     TakeLastOneObserver(Observer<? super T> downstream) {
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
/*    */     public void onNext(T t) {
/* 51 */       this.value = t;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 56 */       this.value = null;
/* 57 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 62 */       emit();
/*    */     }
/*    */     
/*    */     void emit() {
/* 66 */       T v = this.value;
/* 67 */       if (v != null) {
/* 68 */         this.value = null;
/* 69 */         this.downstream.onNext(v);
/*    */       } 
/* 71 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 76 */       this.value = null;
/* 77 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 82 */       return this.upstream.isDisposed();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTakeLastOne.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */