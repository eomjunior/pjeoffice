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
/*     */ public final class FlowableSkipLastTimed<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long time;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public FlowableSkipLastTimed(Flowable<T> source, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  34 */     super(source);
/*  35 */     this.time = time;
/*  36 */     this.unit = unit;
/*  37 */     this.scheduler = scheduler;
/*  38 */     this.bufferSize = bufferSize;
/*  39 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  44 */     this.source.subscribe(new SkipLastTimedSubscriber<T>(s, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
/*     */   }
/*     */   
/*     */   static final class SkipLastTimedSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -5677354903406201275L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final long time;
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     final SpscLinkedArrayQueue<Object> queue;
/*     */     final boolean delayError;
/*     */     Subscription upstream;
/*  59 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     
/*     */     SkipLastTimedSubscriber(Subscriber<? super T> actual, long time, TimeUnit unit, Scheduler scheduler, int bufferSize, boolean delayError) {
/*  67 */       this.downstream = actual;
/*  68 */       this.time = time;
/*  69 */       this.unit = unit;
/*  70 */       this.scheduler = scheduler;
/*  71 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*  72 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  77 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  78 */         this.upstream = s;
/*  79 */         this.downstream.onSubscribe(this);
/*  80 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  86 */       long now = this.scheduler.now(this.unit);
/*     */       
/*  88 */       this.queue.offer(Long.valueOf(now), t);
/*     */       
/*  90 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  95 */       this.error = t;
/*  96 */       this.done = true;
/*  97 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       this.done = true;
/* 103 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 108 */       if (SubscriptionHelper.validate(n)) {
/* 109 */         BackpressureHelper.add(this.requested, n);
/* 110 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 116 */       if (!this.cancelled) {
/* 117 */         this.cancelled = true;
/* 118 */         this.upstream.cancel();
/*     */         
/* 120 */         if (getAndIncrement() == 0) {
/* 121 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 127 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 131 */       int missed = 1;
/*     */       
/* 133 */       Subscriber<? super T> a = this.downstream;
/* 134 */       SpscLinkedArrayQueue<Object> q = this.queue;
/* 135 */       boolean delayError = this.delayError;
/* 136 */       TimeUnit unit = this.unit;
/* 137 */       Scheduler scheduler = this.scheduler;
/* 138 */       long time = this.time;
/*     */ 
/*     */       
/*     */       do {
/* 142 */         long r = this.requested.get();
/* 143 */         long e = 0L;
/*     */         
/* 145 */         while (e != r) {
/* 146 */           boolean d = this.done;
/*     */           
/* 148 */           Long ts = (Long)q.peek();
/*     */           
/* 150 */           boolean empty = (ts == null);
/*     */           
/* 152 */           long now = scheduler.now(unit);
/*     */           
/* 154 */           if (!empty && ts.longValue() > now - time) {
/* 155 */             empty = true;
/*     */           }
/*     */           
/* 158 */           if (checkTerminated(d, empty, a, delayError)) {
/*     */             return;
/*     */           }
/*     */           
/* 162 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 166 */           q.poll();
/*     */           
/* 168 */           T v = (T)q.poll();
/*     */           
/* 170 */           a.onNext(v);
/*     */           
/* 172 */           e++;
/*     */         } 
/*     */         
/* 175 */         if (e != 0L) {
/* 176 */           BackpressureHelper.produced(this.requested, e);
/*     */         }
/*     */         
/* 179 */         missed = addAndGet(-missed);
/* 180 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<? super T> a, boolean delayError) {
/* 187 */       if (this.cancelled) {
/* 188 */         this.queue.clear();
/* 189 */         return true;
/*     */       } 
/* 191 */       if (d) {
/* 192 */         if (delayError) {
/* 193 */           if (empty) {
/* 194 */             Throwable e = this.error;
/* 195 */             if (e != null) {
/* 196 */               a.onError(e);
/*     */             } else {
/* 198 */               a.onComplete();
/*     */             } 
/* 200 */             return true;
/*     */           } 
/*     */         } else {
/* 203 */           Throwable e = this.error;
/* 204 */           if (e != null) {
/* 205 */             this.queue.clear();
/* 206 */             a.onError(e);
/* 207 */             return true;
/*     */           } 
/* 209 */           if (empty) {
/* 210 */             a.onComplete();
/* 211 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/* 215 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSkipLastTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */