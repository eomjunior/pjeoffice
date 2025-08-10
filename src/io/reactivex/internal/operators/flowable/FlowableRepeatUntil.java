/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BooleanSupplier;
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
/*     */ public final class FlowableRepeatUntil<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final BooleanSupplier until;
/*     */   
/*     */   public FlowableRepeatUntil(Flowable<T> source, BooleanSupplier until) {
/*  28 */     super(source);
/*  29 */     this.until = until;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*  34 */     SubscriptionArbiter sa = new SubscriptionArbiter(false);
/*  35 */     s.onSubscribe((Subscription)sa);
/*     */     
/*  37 */     RepeatSubscriber<T> rs = new RepeatSubscriber<T>(s, this.until, sa, (Publisher<? extends T>)this.source);
/*  38 */     rs.subscribeNext();
/*     */   }
/*     */   
/*     */   static final class RepeatSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7098360935104053232L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final SubscriptionArbiter sa;
/*     */     final Publisher<? extends T> source;
/*     */     final BooleanSupplier stop;
/*     */     long produced;
/*     */     
/*     */     RepeatSubscriber(Subscriber<? super T> actual, BooleanSupplier until, SubscriptionArbiter sa, Publisher<? extends T> source) {
/*  53 */       this.downstream = actual;
/*  54 */       this.sa = sa;
/*  55 */       this.source = source;
/*  56 */       this.stop = until;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  61 */       this.sa.setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  66 */       this.produced++;
/*  67 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  72 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       boolean b;
/*     */       try {
/*  79 */         b = this.stop.getAsBoolean();
/*  80 */       } catch (Throwable e) {
/*  81 */         Exceptions.throwIfFatal(e);
/*  82 */         this.downstream.onError(e);
/*     */         return;
/*     */       } 
/*  85 */       if (b) {
/*  86 */         this.downstream.onComplete();
/*     */       } else {
/*  88 */         subscribeNext();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void subscribeNext() {
/*  96 */       if (getAndIncrement() == 0) {
/*  97 */         int missed = 1;
/*     */         do {
/*  99 */           if (this.sa.isCancelled()) {
/*     */             return;
/*     */           }
/*     */           
/* 103 */           long p = this.produced;
/* 104 */           if (p != 0L) {
/* 105 */             this.produced = 0L;
/* 106 */             this.sa.produced(p);
/*     */           } 
/*     */           
/* 109 */           this.source.subscribe((Subscriber)this);
/*     */           
/* 111 */           missed = addAndGet(-missed);
/* 112 */         } while (missed != 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRepeatUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */