/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*    */ public final class FlowableUnsubscribeOn<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public FlowableUnsubscribeOn(Flowable<T> source, Scheduler scheduler) {
/* 27 */     super(source);
/* 28 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 33 */     this.source.subscribe(new UnsubscribeSubscriber<T>(s, this.scheduler));
/*    */   }
/*    */   
/*    */   static final class UnsubscribeSubscriber<T>
/*    */     extends AtomicBoolean
/*    */     implements FlowableSubscriber<T>, Subscription
/*    */   {
/*    */     private static final long serialVersionUID = 1015244841293359600L;
/*    */     final Subscriber<? super T> downstream;
/*    */     final Scheduler scheduler;
/*    */     Subscription upstream;
/*    */     
/*    */     UnsubscribeSubscriber(Subscriber<? super T> actual, Scheduler scheduler) {
/* 46 */       this.downstream = actual;
/* 47 */       this.scheduler = scheduler;
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
/* 60 */       if (!get()) {
/* 61 */         this.downstream.onNext(t);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 67 */       if (get()) {
/* 68 */         RxJavaPlugins.onError(t);
/*    */         return;
/*    */       } 
/* 71 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 76 */       if (!get()) {
/* 77 */         this.downstream.onComplete();
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 83 */       this.upstream.request(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 88 */       if (compareAndSet(false, true))
/* 89 */         this.scheduler.scheduleDirect(new Cancellation()); 
/*    */     }
/*    */     
/*    */     final class Cancellation
/*    */       implements Runnable
/*    */     {
/*    */       public void run() {
/* 96 */         FlowableUnsubscribeOn.UnsubscribeSubscriber.this.upstream.cancel();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableUnsubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */