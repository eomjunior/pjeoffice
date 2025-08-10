/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.HalfSerializer;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableSkipUntil<T, U>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Publisher<U> other;
/*     */   
/*     */   public FlowableSkipUntil(Flowable<T> source, Publisher<U> other) {
/*  28 */     super(source);
/*  29 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> child) {
/*  34 */     SkipUntilMainSubscriber<T> parent = new SkipUntilMainSubscriber<T>(child);
/*  35 */     child.onSubscribe(parent);
/*     */     
/*  37 */     this.other.subscribe((Subscriber)parent.other);
/*     */     
/*  39 */     this.source.subscribe((FlowableSubscriber)parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SkipUntilMainSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -6270983465606289181L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicLong requested;
/*     */     final OtherSubscriber other;
/*     */     final AtomicThrowable error;
/*     */     volatile boolean gate;
/*     */     
/*     */     SkipUntilMainSubscriber(Subscriber<? super T> downstream) {
/*  59 */       this.downstream = downstream;
/*  60 */       this.upstream = new AtomicReference<Subscription>();
/*  61 */       this.requested = new AtomicLong();
/*  62 */       this.other = new OtherSubscriber();
/*  63 */       this.error = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  68 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  73 */       if (!tryOnNext(t)) {
/*  74 */         ((Subscription)this.upstream.get()).request(1L);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*  80 */       if (this.gate) {
/*  81 */         HalfSerializer.onNext(this.downstream, t, this, this.error);
/*  82 */         return true;
/*     */       } 
/*  84 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  89 */       SubscriptionHelper.cancel(this.other);
/*  90 */       HalfSerializer.onError(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       SubscriptionHelper.cancel(this.other);
/*  96 */       HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 101 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 106 */       SubscriptionHelper.cancel(this.upstream);
/* 107 */       SubscriptionHelper.cancel(this.other);
/*     */     }
/*     */     
/*     */     final class OtherSubscriber
/*     */       extends AtomicReference<Subscription>
/*     */       implements FlowableSubscriber<Object>
/*     */     {
/*     */       private static final long serialVersionUID = -5592042965931999169L;
/*     */       
/*     */       public void onSubscribe(Subscription s) {
/* 117 */         SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(Object t) {
/* 122 */         FlowableSkipUntil.SkipUntilMainSubscriber.this.gate = true;
/* 123 */         get().cancel();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 128 */         SubscriptionHelper.cancel(FlowableSkipUntil.SkipUntilMainSubscriber.this.upstream);
/* 129 */         HalfSerializer.onError(FlowableSkipUntil.SkipUntilMainSubscriber.this.downstream, t, FlowableSkipUntil.SkipUntilMainSubscriber.this, FlowableSkipUntil.SkipUntilMainSubscriber.this.error);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 134 */         FlowableSkipUntil.SkipUntilMainSubscriber.this.gate = true; } } } final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> { public void onComplete() { FlowableSkipUntil.SkipUntilMainSubscriber.this.gate = true; }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -5592042965931999169L;
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     public void onNext(Object t) {
/*     */       FlowableSkipUntil.SkipUntilMainSubscriber.this.gate = true;
/*     */       get().cancel();
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       SubscriptionHelper.cancel(FlowableSkipUntil.SkipUntilMainSubscriber.this.upstream);
/*     */       HalfSerializer.onError(FlowableSkipUntil.SkipUntilMainSubscriber.this.downstream, t, FlowableSkipUntil.SkipUntilMainSubscriber.this, FlowableSkipUntil.SkipUntilMainSubscriber.this.error);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSkipUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */