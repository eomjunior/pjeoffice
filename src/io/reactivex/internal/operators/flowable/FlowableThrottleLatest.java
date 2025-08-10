/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class FlowableThrottleLatest<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final boolean emitLast;
/*     */   
/*     */   public FlowableThrottleLatest(Flowable<T> source, long timeout, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/*  50 */     super(source);
/*  51 */     this.timeout = timeout;
/*  52 */     this.unit = unit;
/*  53 */     this.scheduler = scheduler;
/*  54 */     this.emitLast = emitLast;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  59 */     this.source.subscribe(new ThrottleLatestSubscriber<T>(s, this.timeout, this.unit, this.scheduler.createWorker(), this.emitLast));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ThrottleLatestSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -8296689127439125014L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final boolean emitLast;
/*     */     
/*     */     final AtomicReference<T> latest;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Throwable error;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean timerFired;
/*     */     
/*     */     long emitted;
/*     */     
/*     */     boolean timerRunning;
/*     */     
/*     */     ThrottleLatestSubscriber(Subscriber<? super T> downstream, long timeout, TimeUnit unit, Scheduler.Worker worker, boolean emitLast) {
/*  98 */       this.downstream = downstream;
/*  99 */       this.timeout = timeout;
/* 100 */       this.unit = unit;
/* 101 */       this.worker = worker;
/* 102 */       this.emitLast = emitLast;
/* 103 */       this.latest = new AtomicReference<T>();
/* 104 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 109 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 110 */         this.upstream = s;
/* 111 */         this.downstream.onSubscribe(this);
/* 112 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 118 */       this.latest.set(t);
/* 119 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 124 */       this.error = t;
/* 125 */       this.done = true;
/* 126 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 131 */       this.done = true;
/* 132 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 137 */       if (SubscriptionHelper.validate(n)) {
/* 138 */         BackpressureHelper.add(this.requested, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 144 */       this.cancelled = true;
/* 145 */       this.upstream.cancel();
/* 146 */       this.worker.dispose();
/* 147 */       if (getAndIncrement() == 0) {
/* 148 */         this.latest.lazySet(null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 154 */       this.timerFired = true;
/* 155 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 159 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 163 */       int missed = 1;
/*     */       
/* 165 */       AtomicReference<T> latest = this.latest;
/* 166 */       AtomicLong requested = this.requested;
/* 167 */       Subscriber<? super T> downstream = this.downstream;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 172 */         if (this.cancelled) {
/* 173 */           latest.lazySet(null);
/*     */           
/*     */           return;
/*     */         } 
/* 177 */         boolean d = this.done;
/*     */         
/* 179 */         if (d && this.error != null) {
/* 180 */           latest.lazySet(null);
/* 181 */           downstream.onError(this.error);
/* 182 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 186 */         T v = latest.get();
/* 187 */         boolean empty = (v == null);
/*     */         
/* 189 */         if (d) {
/* 190 */           if (!empty && this.emitLast) {
/* 191 */             v = latest.getAndSet(null);
/* 192 */             long e = this.emitted;
/* 193 */             if (e != requested.get()) {
/* 194 */               this.emitted = e + 1L;
/* 195 */               downstream.onNext(v);
/* 196 */               downstream.onComplete();
/*     */             } else {
/* 198 */               downstream.onError((Throwable)new MissingBackpressureException("Could not emit final value due to lack of requests"));
/*     */             } 
/*     */           } else {
/*     */             
/* 202 */             latest.lazySet(null);
/* 203 */             downstream.onComplete();
/*     */           } 
/* 205 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 209 */         if (empty) {
/* 210 */           if (this.timerFired) {
/* 211 */             this.timerRunning = false;
/* 212 */             this.timerFired = false;
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 217 */         else if (!this.timerRunning || this.timerFired) {
/* 218 */           v = latest.getAndSet(null);
/* 219 */           long e = this.emitted;
/* 220 */           if (e != requested.get()) {
/* 221 */             downstream.onNext(v);
/* 222 */             this.emitted = e + 1L;
/*     */           } else {
/* 224 */             this.upstream.cancel();
/* 225 */             downstream.onError((Throwable)new MissingBackpressureException("Could not emit value due to lack of requests"));
/*     */             
/* 227 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 231 */           this.timerFired = false;
/* 232 */           this.timerRunning = true;
/* 233 */           this.worker.schedule(this, this.timeout, this.unit);
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 239 */         missed = addAndGet(-missed);
/* 240 */         if (missed == 0)
/*     */           break; 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableThrottleLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */