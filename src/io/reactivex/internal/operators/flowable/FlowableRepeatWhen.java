/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.processors.FlowableProcessor;
/*     */ import io.reactivex.processors.UnicastProcessor;
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
/*     */ public final class FlowableRepeatWhen<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super Flowable<Object>, ? extends Publisher<?>> handler;
/*     */   
/*     */   public FlowableRepeatWhen(Flowable<T> source, Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
/*  33 */     super(source);
/*  34 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*     */     Publisher<?> when;
/*  40 */     SerializedSubscriber<T> z = new SerializedSubscriber(s);
/*     */     
/*  42 */     FlowableProcessor<Object> processor = UnicastProcessor.create(8).toSerialized();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  47 */       when = (Publisher)ObjectHelper.requireNonNull(this.handler.apply(processor), "handler returned a null Publisher");
/*  48 */     } catch (Throwable ex) {
/*  49 */       Exceptions.throwIfFatal(ex);
/*  50 */       EmptySubscription.error(ex, s);
/*     */       
/*     */       return;
/*     */     } 
/*  54 */     WhenReceiver<T, Object> receiver = new WhenReceiver<T, Object>((Publisher<T>)this.source);
/*     */     
/*  56 */     RepeatWhenSubscriber<T> subscriber = new RepeatWhenSubscriber<T>((Subscriber<? super T>)z, processor, receiver);
/*     */     
/*  58 */     receiver.subscriber = subscriber;
/*     */     
/*  60 */     s.onSubscribe((Subscription)subscriber);
/*     */     
/*  62 */     when.subscribe((Subscriber)receiver);
/*     */     
/*  64 */     receiver.onNext(Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WhenReceiver<T, U>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<Object>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 2827772011130406689L;
/*     */     
/*     */     final Publisher<T> source;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicLong requested;
/*     */     FlowableRepeatWhen.WhenSourceSubscriber<T, U> subscriber;
/*     */     
/*     */     WhenReceiver(Publisher<T> source) {
/*  82 */       this.source = source;
/*  83 */       this.upstream = new AtomicReference<Subscription>();
/*  84 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  89 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/*  94 */       if (getAndIncrement() == 0) {
/*     */         do {
/*  96 */           if (this.upstream.get() == SubscriptionHelper.CANCELLED) {
/*     */             return;
/*     */           }
/*     */           
/* 100 */           this.source.subscribe((Subscriber)this.subscriber);
/*     */         }
/* 102 */         while (decrementAndGet() != 0);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 111 */       this.subscriber.cancel();
/* 112 */       this.subscriber.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 117 */       this.subscriber.cancel();
/* 118 */       this.subscriber.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 123 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 128 */       SubscriptionHelper.cancel(this.upstream);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class WhenSourceSubscriber<T, U>
/*     */     extends SubscriptionArbiter
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -5604623027276966720L;
/*     */     
/*     */     protected final Subscriber<? super T> downstream;
/*     */     
/*     */     protected final FlowableProcessor<U> processor;
/*     */     protected final Subscription receiver;
/*     */     private long produced;
/*     */     
/*     */     WhenSourceSubscriber(Subscriber<? super T> actual, FlowableProcessor<U> processor, Subscription receiver) {
/* 146 */       super(false);
/* 147 */       this.downstream = actual;
/* 148 */       this.processor = processor;
/* 149 */       this.receiver = receiver;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onSubscribe(Subscription s) {
/* 154 */       setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(T t) {
/* 159 */       this.produced++;
/* 160 */       this.downstream.onNext(t);
/*     */     }
/*     */     
/*     */     protected final void again(U signal) {
/* 164 */       setSubscription((Subscription)EmptySubscription.INSTANCE);
/* 165 */       long p = this.produced;
/* 166 */       if (p != 0L) {
/* 167 */         this.produced = 0L;
/* 168 */         produced(p);
/*     */       } 
/* 170 */       this.receiver.request(1L);
/* 171 */       this.processor.onNext(signal);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 176 */       super.cancel();
/* 177 */       this.receiver.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class RepeatWhenSubscriber<T>
/*     */     extends WhenSourceSubscriber<T, Object>
/*     */   {
/*     */     private static final long serialVersionUID = -2680129890138081029L;
/*     */     
/*     */     RepeatWhenSubscriber(Subscriber<? super T> actual, FlowableProcessor<Object> processor, Subscription receiver) {
/* 187 */       super(actual, processor, receiver);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 192 */       this.receiver.cancel();
/* 193 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 198 */       again(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRepeatWhen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */