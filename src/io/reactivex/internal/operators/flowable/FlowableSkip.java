/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*    */ public final class FlowableSkip<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final long n;
/*    */   
/*    */   public FlowableSkip(Flowable<T> source, long n) {
/* 24 */     super(source);
/* 25 */     this.n = n;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 30 */     this.source.subscribe(new SkipSubscriber<T>(s, this.n));
/*    */   }
/*    */   
/*    */   static final class SkipSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Subscription {
/*    */     final Subscriber<? super T> downstream;
/*    */     long remaining;
/*    */     Subscription upstream;
/*    */     
/*    */     SkipSubscriber(Subscriber<? super T> actual, long n) {
/* 40 */       this.downstream = actual;
/* 41 */       this.remaining = n;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 46 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 47 */         long n = this.remaining;
/* 48 */         this.upstream = s;
/* 49 */         this.downstream.onSubscribe(this);
/* 50 */         s.request(n);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 56 */       if (this.remaining != 0L) {
/* 57 */         this.remaining--;
/*    */       } else {
/* 59 */         this.downstream.onNext(t);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 65 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 70 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 75 */       this.upstream.request(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 80 */       this.upstream.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSkip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */