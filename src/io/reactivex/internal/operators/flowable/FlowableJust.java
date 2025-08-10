/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.internal.fuseable.ScalarCallable;
/*    */ import io.reactivex.internal.subscriptions.ScalarSubscription;
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
/*    */ public final class FlowableJust<T>
/*    */   extends Flowable<T>
/*    */   implements ScalarCallable<T>
/*    */ {
/*    */   private final T value;
/*    */   
/*    */   public FlowableJust(T value) {
/* 29 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 34 */     s.onSubscribe((Subscription)new ScalarSubscription(s, this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() {
/* 39 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableJust.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */