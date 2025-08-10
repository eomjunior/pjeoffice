/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class FlowableOnBackpressureLatest<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   public FlowableOnBackpressureLatest(Flowable<T> source) {
/*  27 */     super(source);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  32 */     this.source.subscribe(new BackpressureLatestSubscriber<T>(s));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BackpressureLatestSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 163080509307634843L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     Subscription upstream;
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     volatile boolean cancelled;
/*  48 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*  50 */     final AtomicReference<T> current = new AtomicReference<T>();
/*     */     
/*     */     BackpressureLatestSubscriber(Subscriber<? super T> downstream) {
/*  53 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  58 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  59 */         this.upstream = s;
/*  60 */         this.downstream.onSubscribe(this);
/*  61 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  67 */       this.current.lazySet(t);
/*  68 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  73 */       this.error = t;
/*  74 */       this.done = true;
/*  75 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  80 */       this.done = true;
/*  81 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  86 */       if (SubscriptionHelper.validate(n)) {
/*  87 */         BackpressureHelper.add(this.requested, n);
/*  88 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  94 */       if (!this.cancelled) {
/*  95 */         this.cancelled = true;
/*  96 */         this.upstream.cancel();
/*     */         
/*  98 */         if (getAndIncrement() == 0) {
/*  99 */           this.current.lazySet(null);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 105 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/* 108 */       Subscriber<? super T> a = this.downstream;
/* 109 */       int missed = 1;
/* 110 */       AtomicLong r = this.requested;
/* 111 */       AtomicReference<T> q = this.current;
/*     */       
/*     */       do {
/* 114 */         long e = 0L;
/*     */         
/* 116 */         while (e != r.get()) {
/* 117 */           boolean d = this.done;
/* 118 */           T v = q.getAndSet(null);
/* 119 */           boolean empty = (v == null);
/*     */           
/* 121 */           if (checkTerminated(d, empty, a, q)) {
/*     */             return;
/*     */           }
/*     */           
/* 125 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 129 */           a.onNext(v);
/*     */           
/* 131 */           e++;
/*     */         } 
/*     */         
/* 134 */         if (e == r.get() && checkTerminated(this.done, (q.get() == null), a, q)) {
/*     */           return;
/*     */         }
/*     */         
/* 138 */         if (e != 0L) {
/* 139 */           BackpressureHelper.produced(r, e);
/*     */         }
/*     */         
/* 142 */         missed = addAndGet(-missed);
/* 143 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, AtomicReference<T> q) {
/* 150 */       if (this.cancelled) {
/* 151 */         q.lazySet(null);
/* 152 */         return true;
/*     */       } 
/*     */       
/* 155 */       if (d) {
/* 156 */         Throwable e = this.error;
/* 157 */         if (e != null) {
/* 158 */           q.lazySet(null);
/* 159 */           a.onError(e);
/* 160 */           return true;
/*     */         } 
/* 162 */         if (empty) {
/* 163 */           a.onComplete();
/* 164 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 168 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */