/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ public abstract class BasicFuseableSubscriber<T, R>
/*     */   implements FlowableSubscriber<T>, QueueSubscription<R>
/*     */ {
/*     */   protected final Subscriber<? super R> downstream;
/*     */   protected Subscription upstream;
/*     */   protected QueueSubscription<T> qs;
/*     */   protected boolean done;
/*     */   protected int sourceMode;
/*     */   
/*     */   public BasicFuseableSubscriber(Subscriber<? super R> downstream) {
/*  51 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onSubscribe(Subscription s) {
/*  58 */     if (SubscriptionHelper.validate(this.upstream, s)) {
/*     */       
/*  60 */       this.upstream = s;
/*  61 */       if (s instanceof QueueSubscription) {
/*  62 */         this.qs = (QueueSubscription<T>)s;
/*     */       }
/*     */       
/*  65 */       if (beforeDownstream()) {
/*     */         
/*  67 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/*  69 */         afterDownstream();
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
/*  80 */     return true;
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
/*  96 */     if (this.done) {
/*  97 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 100 */     this.done = true;
/* 101 */     this.downstream.onError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fail(Throwable t) {
/* 109 */     Exceptions.throwIfFatal(t);
/* 110 */     this.upstream.cancel();
/* 111 */     onError(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 116 */     if (this.done) {
/*     */       return;
/*     */     }
/* 119 */     this.done = true;
/* 120 */     this.downstream.onComplete();
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
/* 134 */     QueueSubscription<T> qs = this.qs;
/* 135 */     if (qs != null && (
/* 136 */       mode & 0x4) == 0) {
/* 137 */       int m = qs.requestFusion(mode);
/* 138 */       if (m != 0) {
/* 139 */         this.sourceMode = m;
/*     */       }
/* 141 */       return m;
/*     */     } 
/*     */     
/* 144 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void request(long n) {
/* 153 */     this.upstream.request(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 158 */     this.upstream.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 163 */     return this.qs.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 168 */     this.qs.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean offer(R e) {
/* 177 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean offer(R v1, R v2) {
/* 182 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/BasicFuseableSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */