/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableSamplePublisher<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Publisher<T> source;
/*     */   final Publisher<?> other;
/*     */   final boolean emitLast;
/*     */   
/*     */   public FlowableSamplePublisher(Publisher<T> source, Publisher<?> other, boolean emitLast) {
/*  33 */     this.source = source;
/*  34 */     this.other = other;
/*  35 */     this.emitLast = emitLast;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  40 */     SerializedSubscriber<T> serial = new SerializedSubscriber(s);
/*  41 */     if (this.emitLast) {
/*  42 */       this.source.subscribe((Subscriber)new SampleMainEmitLast<T>((Subscriber<? super T>)serial, this.other));
/*     */     } else {
/*  44 */       this.source.subscribe((Subscriber)new SampleMainNoLast<T>((Subscriber<? super T>)serial, this.other));
/*     */     } 
/*     */   }
/*     */   
/*     */   static abstract class SamplePublisherSubscriber<T>
/*     */     extends AtomicReference<T>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -3517602651313910099L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final Publisher<?> sampler;
/*  55 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*  57 */     final AtomicReference<Subscription> other = new AtomicReference<Subscription>();
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     SamplePublisherSubscriber(Subscriber<? super T> actual, Publisher<?> other) {
/*  62 */       this.downstream = actual;
/*  63 */       this.sampler = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  68 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  69 */         this.upstream = s;
/*  70 */         this.downstream.onSubscribe(this);
/*  71 */         if (this.other.get() == null) {
/*  72 */           this.sampler.subscribe((Subscriber)new FlowableSamplePublisher.SamplerSubscriber<T>(this));
/*  73 */           s.request(Long.MAX_VALUE);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  81 */       lazySet(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  86 */       SubscriptionHelper.cancel(this.other);
/*  87 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  92 */       SubscriptionHelper.cancel(this.other);
/*  93 */       completion();
/*     */     }
/*     */     
/*     */     void setOther(Subscription o) {
/*  97 */       SubscriptionHelper.setOnce(this.other, o, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 102 */       if (SubscriptionHelper.validate(n)) {
/* 103 */         BackpressureHelper.add(this.requested, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 109 */       SubscriptionHelper.cancel(this.other);
/* 110 */       this.upstream.cancel();
/*     */     }
/*     */     
/*     */     public void error(Throwable e) {
/* 114 */       this.upstream.cancel();
/* 115 */       this.downstream.onError(e);
/*     */     }
/*     */     
/*     */     public void complete() {
/* 119 */       this.upstream.cancel();
/* 120 */       completion();
/*     */     }
/*     */     
/*     */     void emit() {
/* 124 */       T value = getAndSet(null);
/* 125 */       if (value != null) {
/* 126 */         long r = this.requested.get();
/* 127 */         if (r != 0L) {
/* 128 */           this.downstream.onNext(value);
/* 129 */           BackpressureHelper.produced(this.requested, 1L);
/*     */         } else {
/* 131 */           cancel();
/* 132 */           this.downstream.onError((Throwable)new MissingBackpressureException("Couldn't emit value due to lack of requests!"));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void completion();
/*     */     
/*     */     abstract void run(); }
/*     */   
/*     */   static final class SamplerSubscriber<T> implements FlowableSubscriber<Object> {
/*     */     final FlowableSamplePublisher.SamplePublisherSubscriber<T> parent;
/*     */     
/*     */     SamplerSubscriber(FlowableSamplePublisher.SamplePublisherSubscriber<T> parent) {
/* 145 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 151 */       this.parent.setOther(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 156 */       this.parent.run();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 161 */       this.parent.error(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 166 */       this.parent.complete();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SampleMainNoLast<T>
/*     */     extends SamplePublisherSubscriber<T> {
/*     */     private static final long serialVersionUID = -3029755663834015785L;
/*     */     
/*     */     SampleMainNoLast(Subscriber<? super T> actual, Publisher<?> other) {
/* 175 */       super(actual, other);
/*     */     }
/*     */ 
/*     */     
/*     */     void completion() {
/* 180 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void run() {
/* 185 */       emit();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SampleMainEmitLast<T>
/*     */     extends SamplePublisherSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3029755663834015785L;
/*     */     final AtomicInteger wip;
/*     */     volatile boolean done;
/*     */     
/*     */     SampleMainEmitLast(Subscriber<? super T> actual, Publisher<?> other) {
/* 198 */       super(actual, other);
/* 199 */       this.wip = new AtomicInteger();
/*     */     }
/*     */ 
/*     */     
/*     */     void completion() {
/* 204 */       this.done = true;
/* 205 */       if (this.wip.getAndIncrement() == 0) {
/* 206 */         emit();
/* 207 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void run() {
/* 213 */       if (this.wip.getAndIncrement() == 0)
/*     */         do {
/* 215 */           boolean d = this.done;
/* 216 */           emit();
/* 217 */           if (d) {
/* 218 */             this.downstream.onComplete();
/*     */             return;
/*     */           } 
/* 221 */         } while (this.wip.decrementAndGet() != 0); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSamplePublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */