/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ public abstract class BasicFuseableConditionalSubscriber<T, R>
/*     */   implements ConditionalSubscriber<T>, QueueSubscription<R>
/*     */ {
/*     */   protected final ConditionalSubscriber<? super R> downstream;
/*     */   protected Subscription upstream;
/*     */   protected QueueSubscription<T> qs;
/*     */   protected boolean done;
/*     */   protected int sourceMode;
/*     */   
/*     */   public BasicFuseableConditionalSubscriber(ConditionalSubscriber<? super R> downstream) {
/*  50 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onSubscribe(Subscription s) {
/*  57 */     if (SubscriptionHelper.validate(this.upstream, s)) {
/*     */       
/*  59 */       this.upstream = s;
/*  60 */       if (s instanceof QueueSubscription) {
/*  61 */         this.qs = (QueueSubscription<T>)s;
/*     */       }
/*     */       
/*  64 */       if (beforeDownstream()) {
/*     */         
/*  66 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/*  68 */         afterDownstream();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean beforeDownstream() {
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterDownstream() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  95 */     if (this.done) {
/*  96 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/*  99 */     this.done = true;
/* 100 */     this.downstream.onError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fail(Throwable t) {
/* 108 */     Exceptions.throwIfFatal(t);
/* 109 */     this.upstream.cancel();
/* 110 */     onError(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 115 */     if (this.done) {
/*     */       return;
/*     */     }
/* 118 */     this.done = true;
/* 119 */     this.downstream.onComplete();
/*     */   }
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
/*     */   protected final int transitiveBoundaryFusion(int mode) {
/* 133 */     QueueSubscription<T> qs = this.qs;
/* 134 */     if (qs != null && (
/* 135 */       mode & 0x4) == 0) {
/* 136 */       int m = qs.requestFusion(mode);
/* 137 */       if (m != 0) {
/* 138 */         this.sourceMode = m;
/*     */       }
/* 140 */       return m;
/*     */     } 
/*     */     
/* 143 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void request(long n) {
/* 152 */     this.upstream.request(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 157 */     this.upstream.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 162 */     return this.qs.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 167 */     this.qs.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean offer(R e) {
/* 176 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean offer(R v1, R v2) {
/* 181 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/BasicFuseableConditionalSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */