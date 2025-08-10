/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
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
/*     */ public final class FlowableRepeat<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long count;
/*     */   
/*     */   public FlowableRepeat(Flowable<T> source, long count) {
/*  26 */     super(source);
/*  27 */     this.count = count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*  32 */     SubscriptionArbiter sa = new SubscriptionArbiter(false);
/*  33 */     s.onSubscribe((Subscription)sa);
/*     */     
/*  35 */     RepeatSubscriber<T> rs = new RepeatSubscriber<T>(s, (this.count != Long.MAX_VALUE) ? (this.count - 1L) : Long.MAX_VALUE, sa, (Publisher<? extends T>)this.source);
/*  36 */     rs.subscribeNext();
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
/*     */     long remaining;
/*     */     long produced;
/*     */     
/*     */     RepeatSubscriber(Subscriber<? super T> actual, long count, SubscriptionArbiter sa, Publisher<? extends T> source) {
/*  51 */       this.downstream = actual;
/*  52 */       this.sa = sa;
/*  53 */       this.source = source;
/*  54 */       this.remaining = count;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  59 */       this.sa.setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  64 */       this.produced++;
/*  65 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  70 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  75 */       long r = this.remaining;
/*  76 */       if (r != Long.MAX_VALUE) {
/*  77 */         this.remaining = r - 1L;
/*     */       }
/*  79 */       if (r != 0L) {
/*  80 */         subscribeNext();
/*     */       } else {
/*  82 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void subscribeNext() {
/*  90 */       if (getAndIncrement() == 0) {
/*  91 */         int missed = 1;
/*     */         do {
/*  93 */           if (this.sa.isCancelled()) {
/*     */             return;
/*     */           }
/*  96 */           long p = this.produced;
/*  97 */           if (p != 0L) {
/*  98 */             this.produced = 0L;
/*  99 */             this.sa.produced(p);
/*     */           } 
/* 101 */           this.source.subscribe((Subscriber)this);
/*     */           
/* 103 */           missed = addAndGet(-missed);
/* 104 */         } while (missed != 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRepeat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */