/*    */ package io.reactivex.internal.subscribers;
/*    */ 
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import io.reactivex.internal.util.NotificationLite;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class BlockingSubscriber<T>
/*    */   extends AtomicReference<Subscription>
/*    */   implements FlowableSubscriber<T>, Subscription
/*    */ {
/*    */   private static final long serialVersionUID = -4875965440900746268L;
/* 29 */   public static final Object TERMINATED = new Object();
/*    */   
/*    */   final Queue<Object> queue;
/*    */   
/*    */   public BlockingSubscriber(Queue<Object> queue) {
/* 34 */     this.queue = queue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Subscription s) {
/* 39 */     if (SubscriptionHelper.setOnce(this, s)) {
/* 40 */       this.queue.offer(NotificationLite.subscription(this));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onNext(T t) {
/* 46 */     this.queue.offer(NotificationLite.next(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 51 */     this.queue.offer(NotificationLite.error(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 56 */     this.queue.offer(NotificationLite.complete());
/*    */   }
/*    */ 
/*    */   
/*    */   public void request(long n) {
/* 61 */     get().request(n);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 66 */     if (SubscriptionHelper.cancel(this)) {
/* 67 */       this.queue.offer(TERMINATED);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isCancelled() {
/* 72 */     return (get() == SubscriptionHelper.CANCELLED);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/BlockingSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */