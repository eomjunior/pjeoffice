/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableScanSeed<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final BiFunction<R, ? super T, R> accumulator;
/*     */   final Callable<R> seedSupplier;
/*     */   
/*     */   public FlowableScanSeed(Flowable<T> source, Callable<R> seedSupplier, BiFunction<R, ? super T, R> accumulator) {
/*  35 */     super(source);
/*  36 */     this.accumulator = accumulator;
/*  37 */     this.seedSupplier = seedSupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*     */     R r;
/*     */     try {
/*  45 */       r = (R)ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seed supplied is null");
/*  46 */     } catch (Throwable e) {
/*  47 */       Exceptions.throwIfFatal(e);
/*  48 */       EmptySubscription.error(e, s);
/*     */       
/*     */       return;
/*     */     } 
/*  52 */     this.source.subscribe(new ScanSeedSubscriber<T, R>(s, this.accumulator, r, bufferSize()));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ScanSeedSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -1776795561228106469L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final BiFunction<R, ? super T, R> accumulator;
/*     */     
/*     */     final SimplePlainQueue<R> queue;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Throwable error;
/*     */     Subscription upstream;
/*     */     R value;
/*     */     int consumed;
/*     */     
/*     */     ScanSeedSubscriber(Subscriber<? super R> actual, BiFunction<R, ? super T, R> accumulator, R value, int prefetch) {
/*  84 */       this.downstream = actual;
/*  85 */       this.accumulator = accumulator;
/*  86 */       this.value = value;
/*  87 */       this.prefetch = prefetch;
/*  88 */       this.limit = prefetch - (prefetch >> 2);
/*  89 */       this.queue = (SimplePlainQueue<R>)new SpscArrayQueue(prefetch);
/*  90 */       this.queue.offer(value);
/*  91 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  96 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  97 */         this.upstream = s;
/*     */         
/*  99 */         this.downstream.onSubscribe(this);
/*     */         
/* 101 */         s.request((this.prefetch - 1));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 107 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 111 */       R v = this.value;
/*     */       try {
/* 113 */         v = (R)ObjectHelper.requireNonNull(this.accumulator.apply(v, t), "The accumulator returned a null value");
/* 114 */       } catch (Throwable ex) {
/* 115 */         Exceptions.throwIfFatal(ex);
/* 116 */         this.upstream.cancel();
/* 117 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 121 */       this.value = v;
/* 122 */       this.queue.offer(v);
/* 123 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 128 */       if (this.done) {
/* 129 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 132 */       this.error = t;
/* 133 */       this.done = true;
/* 134 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 139 */       if (this.done) {
/*     */         return;
/*     */       }
/* 142 */       this.done = true;
/* 143 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 148 */       this.cancelled = true;
/* 149 */       this.upstream.cancel();
/* 150 */       if (getAndIncrement() == 0) {
/* 151 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 157 */       if (SubscriptionHelper.validate(n)) {
/* 158 */         BackpressureHelper.add(this.requested, n);
/* 159 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 164 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 168 */       int missed = 1;
/* 169 */       Subscriber<? super R> a = this.downstream;
/* 170 */       SimplePlainQueue<R> q = this.queue;
/* 171 */       int lim = this.limit;
/* 172 */       int c = this.consumed;
/*     */ 
/*     */       
/*     */       do {
/* 176 */         long r = this.requested.get();
/* 177 */         long e = 0L;
/*     */         
/* 179 */         while (e != r) {
/* 180 */           if (this.cancelled) {
/* 181 */             q.clear();
/*     */             return;
/*     */           } 
/* 184 */           boolean d = this.done;
/*     */           
/* 186 */           if (d) {
/* 187 */             Throwable ex = this.error;
/* 188 */             if (ex != null) {
/* 189 */               q.clear();
/* 190 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 195 */           R v = (R)q.poll();
/* 196 */           boolean empty = (v == null);
/*     */           
/* 198 */           if (d && empty) {
/* 199 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 203 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 207 */           a.onNext(v);
/*     */           
/* 209 */           e++;
/* 210 */           if (++c == lim) {
/* 211 */             c = 0;
/* 212 */             this.upstream.request(lim);
/*     */           } 
/*     */         } 
/*     */         
/* 216 */         if (e == r && 
/* 217 */           this.done) {
/* 218 */           Throwable ex = this.error;
/* 219 */           if (ex != null) {
/* 220 */             q.clear();
/* 221 */             a.onError(ex);
/*     */             return;
/*     */           } 
/* 224 */           if (q.isEmpty()) {
/* 225 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 231 */         if (e != 0L) {
/* 232 */           BackpressureHelper.produced(this.requested, e);
/*     */         }
/*     */         
/* 235 */         this.consumed = c;
/* 236 */         missed = addAndGet(-missed);
/* 237 */       } while (missed != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableScanSeed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */