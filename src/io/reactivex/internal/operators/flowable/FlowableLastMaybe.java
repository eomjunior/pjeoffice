/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableLastMaybe<T>
/*    */   extends Maybe<T>
/*    */ {
/*    */   final Publisher<T> source;
/*    */   
/*    */   public FlowableLastMaybe(Publisher<T> source) {
/* 32 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 39 */     this.source.subscribe((Subscriber)new LastSubscriber<T>(observer));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class LastSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     Subscription upstream;
/*    */     T item;
/*    */     
/*    */     LastSubscriber(MaybeObserver<? super T> downstream) {
/* 51 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 56 */       this.upstream.cancel();
/* 57 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 62 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 67 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 68 */         this.upstream = s;
/*    */         
/* 70 */         this.downstream.onSubscribe(this);
/*    */         
/* 72 */         s.request(Long.MAX_VALUE);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 78 */       this.item = t;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 83 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 84 */       this.item = null;
/* 85 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 90 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 91 */       T v = this.item;
/* 92 */       if (v != null) {
/* 93 */         this.item = null;
/* 94 */         this.downstream.onSuccess(v);
/*    */       } else {
/* 96 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableLastMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */