/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
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
/*     */ public final class FlowableTakeUntil<T, U>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Publisher<? extends U> other;
/*     */   
/*     */   public FlowableTakeUntil(Flowable<T> source, Publisher<? extends U> other) {
/*  27 */     super(source);
/*  28 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> child) {
/*  33 */     TakeUntilMainSubscriber<T> parent = new TakeUntilMainSubscriber<T>(child);
/*  34 */     child.onSubscribe(parent);
/*     */     
/*  36 */     this.other.subscribe((Subscriber)parent.other);
/*     */     
/*  38 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeUntilMainSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -4945480365982832967L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final AtomicLong requested;
/*     */     final AtomicReference<Subscription> upstream;
/*     */     final AtomicThrowable error;
/*     */     final OtherSubscriber other;
/*     */     
/*     */     TakeUntilMainSubscriber(Subscriber<? super T> downstream) {
/*  56 */       this.downstream = downstream;
/*  57 */       this.requested = new AtomicLong();
/*  58 */       this.upstream = new AtomicReference<Subscription>();
/*  59 */       this.other = new OtherSubscriber();
/*  60 */       this.error = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  65 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  70 */       HalfSerializer.onNext(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  75 */       SubscriptionHelper.cancel(this.other);
/*  76 */       HalfSerializer.onError(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  81 */       SubscriptionHelper.cancel(this.other);
/*  82 */       HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  87 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  92 */       SubscriptionHelper.cancel(this.upstream);
/*  93 */       SubscriptionHelper.cancel(this.other);
/*     */     }
/*     */     
/*     */     final class OtherSubscriber
/*     */       extends AtomicReference<Subscription>
/*     */       implements FlowableSubscriber<Object> {
/*     */       private static final long serialVersionUID = -3592821756711087922L;
/*     */       
/*     */       public void onSubscribe(Subscription s) {
/* 102 */         SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(Object t) {
/* 107 */         SubscriptionHelper.cancel(this);
/* 108 */         onComplete();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 113 */         SubscriptionHelper.cancel(FlowableTakeUntil.TakeUntilMainSubscriber.this.upstream);
/* 114 */         HalfSerializer.onError(FlowableTakeUntil.TakeUntilMainSubscriber.this.downstream, t, FlowableTakeUntil.TakeUntilMainSubscriber.this, FlowableTakeUntil.TakeUntilMainSubscriber.this.error);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 119 */         SubscriptionHelper.cancel(FlowableTakeUntil.TakeUntilMainSubscriber.this.upstream);
/* 120 */         HalfSerializer.onComplete(FlowableTakeUntil.TakeUntilMainSubscriber.this.downstream, FlowableTakeUntil.TakeUntilMainSubscriber.this, FlowableTakeUntil.TakeUntilMainSubscriber.this.error); } } } final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> { public void onComplete() { SubscriptionHelper.cancel(FlowableTakeUntil.TakeUntilMainSubscriber.this.upstream); HalfSerializer.onComplete(FlowableTakeUntil.TakeUntilMainSubscriber.this.downstream, FlowableTakeUntil.TakeUntilMainSubscriber.this, FlowableTakeUntil.TakeUntilMainSubscriber.this.error); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -3592821756711087922L;
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     public void onNext(Object t) {
/*     */       SubscriptionHelper.cancel(this);
/*     */       onComplete();
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       SubscriptionHelper.cancel(FlowableTakeUntil.TakeUntilMainSubscriber.this.upstream);
/*     */       HalfSerializer.onError(FlowableTakeUntil.TakeUntilMainSubscriber.this.downstream, t, FlowableTakeUntil.TakeUntilMainSubscriber.this, FlowableTakeUntil.TakeUntilMainSubscriber.this.error);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTakeUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */