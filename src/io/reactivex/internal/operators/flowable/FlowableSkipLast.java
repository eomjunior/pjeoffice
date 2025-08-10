/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import java.util.ArrayDeque;
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
/*    */ public final class FlowableSkipLast<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final int skip;
/*    */   
/*    */   public FlowableSkipLast(Flowable<T> source, int skip) {
/* 27 */     super(source);
/* 28 */     this.skip = skip;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 33 */     this.source.subscribe(new SkipLastSubscriber<T>(s, this.skip));
/*    */   }
/*    */   
/*    */   static final class SkipLastSubscriber<T>
/*    */     extends ArrayDeque<T>
/*    */     implements FlowableSubscriber<T>, Subscription {
/*    */     private static final long serialVersionUID = -3807491841935125653L;
/*    */     final Subscriber<? super T> downstream;
/*    */     final int skip;
/*    */     Subscription upstream;
/*    */     
/*    */     SkipLastSubscriber(Subscriber<? super T> actual, int skip) {
/* 45 */       super(skip);
/* 46 */       this.downstream = actual;
/* 47 */       this.skip = skip;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 52 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 53 */         this.upstream = s;
/* 54 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 60 */       if (this.skip == size()) {
/* 61 */         this.downstream.onNext(poll());
/*    */       } else {
/* 63 */         this.upstream.request(1L);
/*    */       } 
/* 65 */       offer(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 70 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 75 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 80 */       this.upstream.request(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 85 */       this.upstream.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSkipLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */