/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import org.reactivestreams.Subscriber;
/*    */ import org.reactivestreams.Subscription;
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
/*    */ public final class SubscriberCompletableObserver<T>
/*    */   implements CompletableObserver, Subscription
/*    */ {
/*    */   final Subscriber<? super T> subscriber;
/*    */   Disposable upstream;
/*    */   
/*    */   public SubscriberCompletableObserver(Subscriber<? super T> subscriber) {
/* 28 */     this.subscriber = subscriber;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 33 */     this.subscriber.onComplete();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable e) {
/* 38 */     this.subscriber.onError(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 43 */     if (DisposableHelper.validate(this.upstream, d)) {
/* 44 */       this.upstream = d;
/*    */       
/* 46 */       this.subscriber.onSubscribe(this);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void request(long n) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 57 */     this.upstream.dispose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/SubscriberCompletableObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */