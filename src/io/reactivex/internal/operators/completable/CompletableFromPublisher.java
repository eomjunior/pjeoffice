/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import org.reactivestreams.Publisher;
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
/*    */ public final class CompletableFromPublisher<T>
/*    */   extends Completable
/*    */ {
/*    */   final Publisher<T> flowable;
/*    */   
/*    */   public CompletableFromPublisher(Publisher<T> flowable) {
/* 27 */     this.flowable = flowable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver downstream) {
/* 32 */     this.flowable.subscribe((Subscriber)new FromPublisherSubscriber(downstream));
/*    */   }
/*    */   
/*    */   static final class FromPublisherSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Disposable
/*    */   {
/*    */     final CompletableObserver downstream;
/*    */     Subscription upstream;
/*    */     
/*    */     FromPublisherSubscriber(CompletableObserver downstream) {
/* 42 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 47 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 48 */         this.upstream = s;
/*    */         
/* 50 */         this.downstream.onSubscribe(this);
/*    */         
/* 52 */         s.request(Long.MAX_VALUE);
/*    */       } 
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onNext(T t) {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 63 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 68 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 73 */       this.upstream.cancel();
/* 74 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 79 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableFromPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */