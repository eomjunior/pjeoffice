/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.QueueDrain;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public abstract class QueueDrainSubscriber<T, U, V>
/*     */   extends QueueDrainSubscriberPad4
/*     */   implements FlowableSubscriber<T>, QueueDrain<U, V>
/*     */ {
/*     */   protected final Subscriber<? super V> downstream;
/*     */   protected final SimplePlainQueue<U> queue;
/*     */   protected volatile boolean cancelled;
/*     */   protected volatile boolean done;
/*     */   protected Throwable error;
/*     */   
/*     */   public QueueDrainSubscriber(Subscriber<? super V> actual, SimplePlainQueue<U> queue) {
/*  47 */     this.downstream = actual;
/*  48 */     this.queue = queue;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean cancelled() {
/*  53 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean done() {
/*  58 */     return this.done;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean enter() {
/*  63 */     return (this.wip.getAndIncrement() == 0);
/*     */   }
/*     */   
/*     */   public final boolean fastEnter() {
/*  67 */     return (this.wip.get() == 0 && this.wip.compareAndSet(0, 1));
/*     */   }
/*     */   
/*     */   protected final void fastPathEmitMax(U value, boolean delayError, Disposable dispose) {
/*  71 */     Subscriber<? super V> s = this.downstream;
/*  72 */     SimplePlainQueue<U> q = this.queue;
/*     */     
/*  74 */     if (fastEnter()) {
/*  75 */       long r = this.requested.get();
/*  76 */       if (r != 0L) {
/*  77 */         if (accept(s, value) && 
/*  78 */           r != Long.MAX_VALUE) {
/*  79 */           produced(1L);
/*     */         }
/*     */         
/*  82 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/*  86 */         dispose.dispose();
/*  87 */         s.onError((Throwable)new MissingBackpressureException("Could not emit buffer due to lack of requests"));
/*     */         return;
/*     */       } 
/*     */     } else {
/*  91 */       q.offer(value);
/*  92 */       if (!enter()) {
/*     */         return;
/*     */       }
/*     */     } 
/*  96 */     QueueDrainHelper.drainMaxLoop(q, s, delayError, dispose, this);
/*     */   }
/*     */   
/*     */   protected final void fastPathOrderedEmitMax(U value, boolean delayError, Disposable dispose) {
/* 100 */     Subscriber<? super V> s = this.downstream;
/* 101 */     SimplePlainQueue<U> q = this.queue;
/*     */     
/* 103 */     if (fastEnter()) {
/* 104 */       long r = this.requested.get();
/* 105 */       if (r != 0L) {
/* 106 */         if (q.isEmpty()) {
/* 107 */           if (accept(s, value) && 
/* 108 */             r != Long.MAX_VALUE) {
/* 109 */             produced(1L);
/*     */           }
/*     */           
/* 112 */           if (leave(-1) == 0) {
/*     */             return;
/*     */           }
/*     */         } else {
/* 116 */           q.offer(value);
/*     */         } 
/*     */       } else {
/* 119 */         this.cancelled = true;
/* 120 */         dispose.dispose();
/* 121 */         s.onError((Throwable)new MissingBackpressureException("Could not emit buffer due to lack of requests"));
/*     */         return;
/*     */       } 
/*     */     } else {
/* 125 */       q.offer(value);
/* 126 */       if (!enter()) {
/*     */         return;
/*     */       }
/*     */     } 
/* 130 */     QueueDrainHelper.drainMaxLoop(q, s, delayError, dispose, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accept(Subscriber<? super V> a, U v) {
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Throwable error() {
/* 140 */     return this.error;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int leave(int m) {
/* 145 */     return this.wip.addAndGet(m);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long requested() {
/* 150 */     return this.requested.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public final long produced(long n) {
/* 155 */     return this.requested.addAndGet(-n);
/*     */   }
/*     */   
/*     */   public final void requested(long n) {
/* 159 */     if (SubscriptionHelper.validate(n))
/* 160 */       BackpressureHelper.add(this.requested, n); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/QueueDrainSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */