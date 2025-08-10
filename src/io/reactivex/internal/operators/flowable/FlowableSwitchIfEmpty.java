/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
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
/*    */ public final class FlowableSwitchIfEmpty<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final Publisher<? extends T> other;
/*    */   
/*    */   public FlowableSwitchIfEmpty(Flowable<T> source, Publisher<? extends T> other) {
/* 24 */     super(source);
/* 25 */     this.other = other;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 30 */     SwitchIfEmptySubscriber<T> parent = new SwitchIfEmptySubscriber<T>(s, this.other);
/* 31 */     s.onSubscribe((Subscription)parent.arbiter);
/* 32 */     this.source.subscribe(parent);
/*    */   }
/*    */   
/*    */   static final class SwitchIfEmptySubscriber<T>
/*    */     implements FlowableSubscriber<T> {
/*    */     final Subscriber<? super T> downstream;
/*    */     final Publisher<? extends T> other;
/*    */     final SubscriptionArbiter arbiter;
/*    */     boolean empty;
/*    */     
/*    */     SwitchIfEmptySubscriber(Subscriber<? super T> actual, Publisher<? extends T> other) {
/* 43 */       this.downstream = actual;
/* 44 */       this.other = other;
/* 45 */       this.empty = true;
/* 46 */       this.arbiter = new SubscriptionArbiter(false);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 51 */       this.arbiter.setSubscription(s);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 56 */       if (this.empty) {
/* 57 */         this.empty = false;
/*    */       }
/* 59 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 64 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 69 */       if (this.empty) {
/* 70 */         this.empty = false;
/* 71 */         this.other.subscribe((Subscriber)this);
/*    */       } else {
/* 73 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSwitchIfEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */