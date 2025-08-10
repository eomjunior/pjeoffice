/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class InnerQueuedSubscriber<T>
/*     */   extends AtomicReference<Subscription>
/*     */   implements FlowableSubscriber<T>, Subscription
/*     */ {
/*     */   private static final long serialVersionUID = 22876611072430776L;
/*     */   final InnerQueuedSubscriberSupport<T> parent;
/*     */   final int prefetch;
/*     */   final int limit;
/*     */   volatile SimpleQueue<T> queue;
/*     */   volatile boolean done;
/*     */   long produced;
/*     */   int fusionMode;
/*     */   
/*     */   public InnerQueuedSubscriber(InnerQueuedSubscriberSupport<T> parent, int prefetch) {
/*  52 */     this.parent = parent;
/*  53 */     this.prefetch = prefetch;
/*  54 */     this.limit = prefetch - (prefetch >> 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  59 */     if (SubscriptionHelper.setOnce(this, s)) {
/*  60 */       if (s instanceof QueueSubscription) {
/*     */         
/*  62 */         QueueSubscription<T> qs = (QueueSubscription<T>)s;
/*     */         
/*  64 */         int m = qs.requestFusion(3);
/*  65 */         if (m == 1) {
/*  66 */           this.fusionMode = m;
/*  67 */           this.queue = (SimpleQueue<T>)qs;
/*  68 */           this.done = true;
/*  69 */           this.parent.innerComplete(this);
/*     */           return;
/*     */         } 
/*  72 */         if (m == 2) {
/*  73 */           this.fusionMode = m;
/*  74 */           this.queue = (SimpleQueue<T>)qs;
/*  75 */           QueueDrainHelper.request(s, this.prefetch);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*  80 */       this.queue = QueueDrainHelper.createQueue(this.prefetch);
/*     */       
/*  82 */       QueueDrainHelper.request(s, this.prefetch);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  88 */     if (this.fusionMode == 0) {
/*  89 */       this.parent.innerNext(this, t);
/*     */     } else {
/*  91 */       this.parent.drain();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  97 */     this.parent.innerError(this, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 102 */     this.parent.innerComplete(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void request(long n) {
/* 107 */     if (this.fusionMode != 1) {
/* 108 */       long p = this.produced + n;
/* 109 */       if (p >= this.limit) {
/* 110 */         this.produced = 0L;
/* 111 */         get().request(p);
/*     */       } else {
/* 113 */         this.produced = p;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void requestOne() {
/* 119 */     if (this.fusionMode != 1) {
/* 120 */       long p = this.produced + 1L;
/* 121 */       if (p == this.limit) {
/* 122 */         this.produced = 0L;
/* 123 */         get().request(p);
/*     */       } else {
/* 125 */         this.produced = p;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 132 */     SubscriptionHelper.cancel(this);
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 136 */     return this.done;
/*     */   }
/*     */   
/*     */   public void setDone() {
/* 140 */     this.done = true;
/*     */   }
/*     */   
/*     */   public SimpleQueue<T> queue() {
/* 144 */     return this.queue;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/InnerQueuedSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */