/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import io.reactivex.schedulers.Timed;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public final class FlowableTimeInterval<T>
/*    */   extends AbstractFlowableWithUpstream<T, Timed<T>>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public FlowableTimeInterval(Flowable<T> source, TimeUnit unit, Scheduler scheduler) {
/* 29 */     super(source);
/* 30 */     this.scheduler = scheduler;
/* 31 */     this.unit = unit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super Timed<T>> s) {
/* 36 */     this.source.subscribe(new TimeIntervalSubscriber<T>(s, this.unit, this.scheduler));
/*    */   }
/*    */   
/*    */   static final class TimeIntervalSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Subscription
/*    */   {
/*    */     final Subscriber<? super Timed<T>> downstream;
/*    */     final TimeUnit unit;
/*    */     final Scheduler scheduler;
/*    */     Subscription upstream;
/*    */     long lastTime;
/*    */     
/*    */     TimeIntervalSubscriber(Subscriber<? super Timed<T>> actual, TimeUnit unit, Scheduler scheduler) {
/* 49 */       this.downstream = actual;
/* 50 */       this.scheduler = scheduler;
/* 51 */       this.unit = unit;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 56 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 57 */         this.lastTime = this.scheduler.now(this.unit);
/* 58 */         this.upstream = s;
/* 59 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 65 */       long now = this.scheduler.now(this.unit);
/* 66 */       long last = this.lastTime;
/* 67 */       this.lastTime = now;
/* 68 */       long delta = now - last;
/* 69 */       this.downstream.onNext(new Timed(t, delta, this.unit));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 74 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 79 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 84 */       this.upstream.request(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 89 */       this.upstream.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTimeInterval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */