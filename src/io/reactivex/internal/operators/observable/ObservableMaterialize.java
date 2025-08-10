/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Notification;
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
/*    */ public final class ObservableMaterialize<T>
/*    */   extends AbstractObservableWithUpstream<T, Notification<T>>
/*    */ {
/*    */   public ObservableMaterialize(ObservableSource<T> source) {
/* 23 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super Notification<T>> t) {
/* 28 */     this.source.subscribe(new MaterializeObserver<T>(t));
/*    */   }
/*    */   
/*    */   static final class MaterializeObserver<T>
/*    */     implements Observer<T>, Disposable {
/*    */     final Observer<? super Notification<T>> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     MaterializeObserver(Observer<? super Notification<T>> downstream) {
/* 37 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 42 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 43 */         this.upstream = d;
/* 44 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 50 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 55 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 60 */       this.downstream.onNext(Notification.createOnNext(t));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 65 */       Notification<T> v = Notification.createOnError(t);
/* 66 */       this.downstream.onNext(v);
/* 67 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 72 */       Notification<T> v = Notification.createOnComplete();
/*    */       
/* 74 */       this.downstream.onNext(v);
/* 75 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */