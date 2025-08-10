/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
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
/*     */ public final class FlowableWithLatestFrom<T, U, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final BiFunction<? super T, ? super U, ? extends R> combiner;
/*     */   final Publisher<? extends U> other;
/*     */   
/*     */   public FlowableWithLatestFrom(Flowable<T> source, BiFunction<? super T, ? super U, ? extends R> combiner, Publisher<? extends U> other) {
/*  32 */     super(source);
/*  33 */     this.combiner = combiner;
/*  34 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  39 */     SerializedSubscriber<R> serial = new SerializedSubscriber(s);
/*  40 */     WithLatestFromSubscriber<T, U, R> wlf = new WithLatestFromSubscriber<T, U, R>((Subscriber<? super R>)serial, this.combiner);
/*     */     
/*  42 */     serial.onSubscribe(wlf);
/*     */     
/*  44 */     this.other.subscribe((Subscriber)new FlowableWithLatestSubscriber(wlf));
/*     */     
/*  46 */     this.source.subscribe((FlowableSubscriber)wlf);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WithLatestFromSubscriber<T, U, R>
/*     */     extends AtomicReference<U>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -312246233408980075L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     final BiFunction<? super T, ? super U, ? extends R> combiner;
/*  58 */     final AtomicReference<Subscription> upstream = new AtomicReference<Subscription>();
/*     */     
/*  60 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*  62 */     final AtomicReference<Subscription> other = new AtomicReference<Subscription>();
/*     */     
/*     */     WithLatestFromSubscriber(Subscriber<? super R> actual, BiFunction<? super T, ? super U, ? extends R> combiner) {
/*  65 */       this.downstream = actual;
/*  66 */       this.combiner = combiner;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  71 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  76 */       if (!tryOnNext(t)) {
/*  77 */         ((Subscription)this.upstream.get()).request(1L);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*  83 */       U u = get();
/*  84 */       if (u != null) {
/*     */         R r;
/*     */         try {
/*  87 */           r = (R)ObjectHelper.requireNonNull(this.combiner.apply(t, u), "The combiner returned a null value");
/*  88 */         } catch (Throwable e) {
/*  89 */           Exceptions.throwIfFatal(e);
/*  90 */           cancel();
/*  91 */           this.downstream.onError(e);
/*  92 */           return false;
/*     */         } 
/*  94 */         this.downstream.onNext(r);
/*  95 */         return true;
/*     */       } 
/*  97 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 103 */       SubscriptionHelper.cancel(this.other);
/* 104 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 109 */       SubscriptionHelper.cancel(this.other);
/* 110 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 115 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 120 */       SubscriptionHelper.cancel(this.upstream);
/* 121 */       SubscriptionHelper.cancel(this.other);
/*     */     }
/*     */     
/*     */     public boolean setOther(Subscription o) {
/* 125 */       return SubscriptionHelper.setOnce(this.other, o);
/*     */     }
/*     */     
/*     */     public void otherError(Throwable e) {
/* 129 */       SubscriptionHelper.cancel(this.upstream);
/* 130 */       this.downstream.onError(e);
/*     */     }
/*     */   }
/*     */   
/*     */   final class FlowableWithLatestSubscriber implements FlowableSubscriber<U> {
/*     */     private final FlowableWithLatestFrom.WithLatestFromSubscriber<T, U, R> wlf;
/*     */     
/*     */     FlowableWithLatestSubscriber(FlowableWithLatestFrom.WithLatestFromSubscriber<T, U, R> wlf) {
/* 138 */       this.wlf = wlf;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 143 */       if (this.wlf.setOther(s)) {
/* 144 */         s.request(Long.MAX_VALUE);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(U t) {
/* 150 */       this.wlf.lazySet(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 155 */       this.wlf.otherError(t);
/*     */     }
/*     */     
/*     */     public void onComplete() {}
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableWithLatestFrom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */