/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import io.reactivex.internal.util.EmptyComponent;
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
/*    */ public final class FlowableDetach<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   public FlowableDetach(Flowable<T> source) {
/* 25 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 30 */     this.source.subscribe(new DetachSubscriber<T>(s));
/*    */   }
/*    */   
/*    */   static final class DetachSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Subscription
/*    */   {
/*    */     Subscriber<? super T> downstream;
/*    */     Subscription upstream;
/*    */     
/*    */     DetachSubscriber(Subscriber<? super T> downstream) {
/* 40 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 45 */       this.upstream.request(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 50 */       Subscription s = this.upstream;
/* 51 */       this.upstream = (Subscription)EmptyComponent.INSTANCE;
/* 52 */       this.downstream = EmptyComponent.asSubscriber();
/* 53 */       s.cancel();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 58 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 59 */         this.upstream = s;
/*    */         
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
/* 72 */       Subscriber<? super T> a = this.downstream;
/* 73 */       this.upstream = (Subscription)EmptyComponent.INSTANCE;
/* 74 */       this.downstream = EmptyComponent.asSubscriber();
/* 75 */       a.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 80 */       Subscriber<? super T> a = this.downstream;
/* 81 */       this.upstream = (Subscription)EmptyComponent.INSTANCE;
/* 82 */       this.downstream = EmptyComponent.asSubscriber();
/* 83 */       a.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */