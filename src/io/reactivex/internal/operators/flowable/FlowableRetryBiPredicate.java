/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ public final class FlowableRetryBiPredicate<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final BiPredicate<? super Integer, ? super Throwable> predicate;
/*     */   
/*     */   public FlowableRetryBiPredicate(Flowable<T> source, BiPredicate<? super Integer, ? super Throwable> predicate) {
/*  30 */     super(source);
/*  31 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*  36 */     SubscriptionArbiter sa = new SubscriptionArbiter(false);
/*  37 */     s.onSubscribe((Subscription)sa);
/*     */     
/*  39 */     RetryBiSubscriber<T> rs = new RetryBiSubscriber<T>(s, this.predicate, sa, (Publisher<? extends T>)this.source);
/*  40 */     rs.subscribeNext();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RetryBiSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7098360935104053232L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final SubscriptionArbiter sa;
/*     */     final Publisher<? extends T> source;
/*     */     final BiPredicate<? super Integer, ? super Throwable> predicate;
/*     */     int retries;
/*     */     long produced;
/*     */     
/*     */     RetryBiSubscriber(Subscriber<? super T> actual, BiPredicate<? super Integer, ? super Throwable> predicate, SubscriptionArbiter sa, Publisher<? extends T> source) {
/*  57 */       this.downstream = actual;
/*  58 */       this.sa = sa;
/*  59 */       this.source = source;
/*  60 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  65 */       this.sa.setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  70 */       this.produced++;
/*  71 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       boolean b;
/*     */       try {
/*  78 */         b = this.predicate.test(Integer.valueOf(++this.retries), t);
/*  79 */       } catch (Throwable e) {
/*  80 */         Exceptions.throwIfFatal(e);
/*  81 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */         return;
/*     */       } 
/*  84 */       if (!b) {
/*  85 */         this.downstream.onError(t);
/*     */         return;
/*     */       } 
/*  88 */       subscribeNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  93 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void subscribeNext() {
/* 100 */       if (getAndIncrement() == 0) {
/* 101 */         int missed = 1;
/*     */         do {
/* 103 */           if (this.sa.isCancelled()) {
/*     */             return;
/*     */           }
/*     */           
/* 107 */           long p = this.produced;
/* 108 */           if (p != 0L) {
/* 109 */             this.produced = 0L;
/* 110 */             this.sa.produced(p);
/*     */           } 
/*     */           
/* 113 */           this.source.subscribe((Subscriber)this);
/*     */           
/* 115 */           missed = addAndGet(-missed);
/* 116 */         } while (missed != 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRetryBiPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */