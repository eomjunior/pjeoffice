/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class FlowableTakeLastTimed<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long count;
/*     */   final long time;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public FlowableTakeLastTimed(Flowable<T> source, long count, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  37 */     super(source);
/*  38 */     this.count = count;
/*  39 */     this.time = time;
/*  40 */     this.unit = unit;
/*  41 */     this.scheduler = scheduler;
/*  42 */     this.bufferSize = bufferSize;
/*  43 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  48 */     this.source.subscribe(new TakeLastTimedSubscriber<T>(s, this.count, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
/*     */   }
/*     */   
/*     */   static final class TakeLastTimedSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -5677354903406201275L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final long count;
/*     */     final long time;
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     final SpscLinkedArrayQueue<Object> queue;
/*     */     final boolean delayError;
/*     */     Subscription upstream;
/*  64 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     
/*     */     TakeLastTimedSubscriber(Subscriber<? super T> actual, long count, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  72 */       this.downstream = actual;
/*  73 */       this.count = count;
/*  74 */       this.time = time;
/*  75 */       this.unit = unit;
/*  76 */       this.scheduler = scheduler;
/*  77 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*  78 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  83 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  84 */         this.upstream = s;
/*  85 */         this.downstream.onSubscribe(this);
/*  86 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  92 */       SpscLinkedArrayQueue<Object> q = this.queue;
/*     */       
/*  94 */       long now = this.scheduler.now(this.unit);
/*     */       
/*  96 */       q.offer(Long.valueOf(now), t);
/*     */       
/*  98 */       trim(now, q);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 103 */       if (this.delayError) {
/* 104 */         trim(this.scheduler.now(this.unit), this.queue);
/*     */       }
/* 106 */       this.error = t;
/* 107 */       this.done = true;
/* 108 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 113 */       trim(this.scheduler.now(this.unit), this.queue);
/* 114 */       this.done = true;
/* 115 */       drain();
/*     */     }
/*     */     
/*     */     void trim(long now, SpscLinkedArrayQueue<Object> q) {
/* 119 */       long time = this.time;
/* 120 */       long c = this.count;
/* 121 */       boolean unbounded = (c == Long.MAX_VALUE);
/*     */       
/* 123 */       while (!q.isEmpty()) {
/* 124 */         long ts = ((Long)q.peek()).longValue();
/* 125 */         if (ts < now - time || (!unbounded && (q.size() >> 1) > c)) {
/* 126 */           q.poll();
/* 127 */           q.poll();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 136 */       if (SubscriptionHelper.validate(n)) {
/* 137 */         BackpressureHelper.add(this.requested, n);
/* 138 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 144 */       if (!this.cancelled) {
/* 145 */         this.cancelled = true;
/* 146 */         this.upstream.cancel();
/*     */         
/* 148 */         if (getAndIncrement() == 0) {
/* 149 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 155 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 159 */       int missed = 1;
/*     */       
/* 161 */       Subscriber<? super T> a = this.downstream;
/* 162 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 163 */       boolean delayError = this.delayError;
/*     */ 
/*     */       
/*     */       do {
/* 167 */         if (this.done) {
/* 168 */           boolean empty = q.isEmpty();
/*     */           
/* 170 */           if (checkTerminated(empty, a, delayError)) {
/*     */             return;
/*     */           }
/*     */           
/* 174 */           long r = this.requested.get();
/* 175 */           long e = 0L;
/*     */           
/*     */           while (true) {
/* 178 */             Object ts = q.peek();
/* 179 */             empty = (ts == null);
/*     */             
/* 181 */             if (checkTerminated(empty, a, delayError)) {
/*     */               return;
/*     */             }
/*     */             
/* 185 */             if (r == e) {
/*     */               break;
/*     */             }
/*     */             
/* 189 */             q.poll();
/*     */             
/* 191 */             T o = (T)q.poll();
/*     */             
/* 193 */             a.onNext(o);
/*     */             
/* 195 */             e++;
/*     */           } 
/*     */           
/* 198 */           if (e != 0L) {
/* 199 */             BackpressureHelper.produced(this.requested, e);
/*     */           }
/*     */         } 
/*     */         
/* 203 */         missed = addAndGet(-missed);
/* 204 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean checkTerminated(boolean empty, Subscriber<? super T> a, boolean delayError) {
/* 211 */       if (this.cancelled) {
/* 212 */         this.queue.clear();
/* 213 */         return true;
/*     */       } 
/* 215 */       if (delayError) {
/* 216 */         if (empty) {
/* 217 */           Throwable e = this.error;
/* 218 */           if (e != null) {
/* 219 */             a.onError(e);
/*     */           } else {
/* 221 */             a.onComplete();
/*     */           } 
/* 223 */           return true;
/*     */         } 
/*     */       } else {
/* 226 */         Throwable e = this.error;
/* 227 */         if (e != null) {
/* 228 */           this.queue.clear();
/* 229 */           a.onError(e);
/* 230 */           return true;
/*     */         } 
/* 232 */         if (empty) {
/* 233 */           a.onComplete();
/* 234 */           return true;
/*     */         } 
/*     */       } 
/* 237 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTakeLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */