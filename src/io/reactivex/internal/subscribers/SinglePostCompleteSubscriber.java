/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public abstract class SinglePostCompleteSubscriber<T, R>
/*     */   extends AtomicLong
/*     */   implements FlowableSubscriber<T>, Subscription
/*     */ {
/*     */   private static final long serialVersionUID = 7917814472626990048L;
/*     */   protected final Subscriber<? super R> downstream;
/*     */   protected Subscription upstream;
/*     */   protected R value;
/*     */   protected long produced;
/*     */   static final long COMPLETE_MASK = -9223372036854775808L;
/*     */   static final long REQUEST_MASK = 9223372036854775807L;
/*     */   
/*     */   public SinglePostCompleteSubscriber(Subscriber<? super R> downstream) {
/*  52 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  57 */     if (SubscriptionHelper.validate(this.upstream, s)) {
/*  58 */       this.upstream = s;
/*  59 */       this.downstream.onSubscribe(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void complete(R n) {
/*  68 */     long p = this.produced;
/*  69 */     if (p != 0L) {
/*  70 */       BackpressureHelper.produced(this, p);
/*     */     }
/*     */     
/*     */     while (true) {
/*  74 */       long r = get();
/*  75 */       if ((r & Long.MIN_VALUE) != 0L) {
/*  76 */         onDrop(n);
/*     */         return;
/*     */       } 
/*  79 */       if ((r & Long.MAX_VALUE) != 0L) {
/*  80 */         lazySet(-9223372036854775807L);
/*  81 */         this.downstream.onNext(n);
/*  82 */         this.downstream.onComplete();
/*     */         return;
/*     */       } 
/*  85 */       this.value = n;
/*  86 */       if (compareAndSet(0L, Long.MIN_VALUE)) {
/*     */         return;
/*     */       }
/*  89 */       this.value = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onDrop(R n) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void request(long n) {
/* 103 */     if (SubscriptionHelper.validate(n)) {
/*     */       while (true) {
/* 105 */         long r = get();
/* 106 */         if ((r & Long.MIN_VALUE) != 0L) {
/* 107 */           if (compareAndSet(Long.MIN_VALUE, -9223372036854775807L)) {
/* 108 */             this.downstream.onNext(this.value);
/* 109 */             this.downstream.onComplete();
/*     */           } 
/*     */           break;
/*     */         } 
/* 113 */         long u = BackpressureHelper.addCap(r, n);
/* 114 */         if (compareAndSet(r, u)) {
/* 115 */           this.upstream.request(n);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 124 */     this.upstream.cancel();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/SinglePostCompleteSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */