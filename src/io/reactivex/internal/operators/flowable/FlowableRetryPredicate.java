/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
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
/*     */ public final class FlowableRetryPredicate<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super Throwable> predicate;
/*     */   final long count;
/*     */   
/*     */   public FlowableRetryPredicate(Flowable<T> source, long count, Predicate<? super Throwable> predicate) {
/*  31 */     super(source);
/*  32 */     this.predicate = predicate;
/*  33 */     this.count = count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*  38 */     SubscriptionArbiter sa = new SubscriptionArbiter(false);
/*  39 */     s.onSubscribe((Subscription)sa);
/*     */     
/*  41 */     RetrySubscriber<T> rs = new RetrySubscriber<T>(s, this.count, this.predicate, sa, (Publisher<? extends T>)this.source);
/*  42 */     rs.subscribeNext();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RetrySubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7098360935104053232L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final SubscriptionArbiter sa;
/*     */     final Publisher<? extends T> source;
/*     */     final Predicate<? super Throwable> predicate;
/*     */     long remaining;
/*     */     long produced;
/*     */     
/*     */     RetrySubscriber(Subscriber<? super T> actual, long count, Predicate<? super Throwable> predicate, SubscriptionArbiter sa, Publisher<? extends T> source) {
/*  59 */       this.downstream = actual;
/*  60 */       this.sa = sa;
/*  61 */       this.source = source;
/*  62 */       this.predicate = predicate;
/*  63 */       this.remaining = count;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  68 */       this.sa.setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  73 */       this.produced++;
/*  74 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  79 */       long r = this.remaining;
/*  80 */       if (r != Long.MAX_VALUE) {
/*  81 */         this.remaining = r - 1L;
/*     */       }
/*  83 */       if (r == 0L) {
/*  84 */         this.downstream.onError(t);
/*     */       } else {
/*     */         boolean b;
/*     */         try {
/*  88 */           b = this.predicate.test(t);
/*  89 */         } catch (Throwable e) {
/*  90 */           Exceptions.throwIfFatal(e);
/*  91 */           this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */           return;
/*     */         } 
/*  94 */         if (!b) {
/*  95 */           this.downstream.onError(t);
/*     */           return;
/*     */         } 
/*  98 */         subscribeNext();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 104 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void subscribeNext() {
/* 111 */       if (getAndIncrement() == 0) {
/* 112 */         int missed = 1;
/*     */         do {
/* 114 */           if (this.sa.isCancelled()) {
/*     */             return;
/*     */           }
/*     */           
/* 118 */           long p = this.produced;
/* 119 */           if (p != 0L) {
/* 120 */             this.produced = 0L;
/* 121 */             this.sa.produced(p);
/*     */           } 
/*     */           
/* 124 */           this.source.subscribe((Subscriber)this);
/*     */           
/* 126 */           missed = addAndGet(-missed);
/* 127 */         } while (missed != 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRetryPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */