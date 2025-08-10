/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
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
/*    */ public final class FlowableFromObservable<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   private final Observable<T> upstream;
/*    */   
/*    */   public FlowableFromObservable(Observable<T> upstream) {
/* 24 */     this.upstream = upstream;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 29 */     this.upstream.subscribe(new SubscriberObserver<T>(s));
/*    */   }
/*    */   
/*    */   static final class SubscriberObserver<T>
/*    */     implements Observer<T>, Subscription
/*    */   {
/*    */     final Subscriber<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     SubscriberObserver(Subscriber<? super T> s) {
/* 39 */       this.downstream = s;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 44 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 49 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T value) {
/* 54 */       this.downstream.onNext(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 59 */       this.upstream = d;
/* 60 */       this.downstream.onSubscribe(this);
/*    */     }
/*    */     
/*    */     public void cancel() {
/* 64 */       this.upstream.dispose();
/*    */     }
/*    */     
/*    */     public void request(long n) {}
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFromObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */