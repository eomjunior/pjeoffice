/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.HalfSerializer;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StrictSubscriber<T>
/*     */   extends AtomicInteger
/*     */   implements FlowableSubscriber<T>, Subscription
/*     */ {
/*     */   private static final long serialVersionUID = -4945028590049415624L;
/*     */   final Subscriber<? super T> downstream;
/*     */   final AtomicThrowable error;
/*     */   final AtomicLong requested;
/*     */   final AtomicReference<Subscription> upstream;
/*     */   final AtomicBoolean once;
/*     */   volatile boolean done;
/*     */   
/*     */   public StrictSubscriber(Subscriber<? super T> downstream) {
/*  57 */     this.downstream = downstream;
/*  58 */     this.error = new AtomicThrowable();
/*  59 */     this.requested = new AtomicLong();
/*  60 */     this.upstream = new AtomicReference<Subscription>();
/*  61 */     this.once = new AtomicBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public void request(long n) {
/*  66 */     if (n <= 0L) {
/*  67 */       cancel();
/*  68 */       onError(new IllegalArgumentException("§3.9 violated: positive request amount required but it was " + n));
/*     */     } else {
/*  70 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/*  76 */     if (!this.done) {
/*  77 */       SubscriptionHelper.cancel(this.upstream);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  83 */     if (this.once.compareAndSet(false, true)) {
/*     */       
/*  85 */       this.downstream.onSubscribe(this);
/*     */       
/*  87 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     } else {
/*  89 */       s.cancel();
/*  90 */       cancel();
/*  91 */       onError(new IllegalStateException("§2.12 violated: onSubscribe must be called at most once"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  97 */     HalfSerializer.onNext(this.downstream, t, this, this.error);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 102 */     this.done = true;
/* 103 */     HalfSerializer.onError(this.downstream, t, this, this.error);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 108 */     this.done = true;
/* 109 */     HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/StrictSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */