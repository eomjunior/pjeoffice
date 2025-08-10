/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ public final class ParallelFromPublisher<T>
/*     */   extends ParallelFlowable<T>
/*     */ {
/*     */   final Publisher<? extends T> source;
/*     */   final int parallelism;
/*     */   final int prefetch;
/*     */   
/*     */   public ParallelFromPublisher(Publisher<? extends T> source, int parallelism, int prefetch) {
/*  42 */     this.source = source;
/*  43 */     this.parallelism = parallelism;
/*  44 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  49 */     return this.parallelism;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super T>[] subscribers) {
/*  54 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  58 */     this.source.subscribe((Subscriber)new ParallelDispatcher<T>(subscribers, this.prefetch));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ParallelDispatcher<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -4470634016609963609L;
/*     */ 
/*     */     
/*     */     final Subscriber<? super T>[] subscribers;
/*     */ 
/*     */     
/*     */     final AtomicLongArray requests;
/*     */     
/*     */     final long[] emissions;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     Throwable error;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     int index;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*  93 */     final AtomicInteger subscriberCount = new AtomicInteger();
/*     */     
/*     */     int produced;
/*     */     
/*     */     int sourceMode;
/*     */     
/*     */     ParallelDispatcher(Subscriber<? super T>[] subscribers, int prefetch) {
/* 100 */       this.subscribers = subscribers;
/* 101 */       this.prefetch = prefetch;
/* 102 */       this.limit = prefetch - (prefetch >> 2);
/* 103 */       int m = subscribers.length;
/* 104 */       this.requests = new AtomicLongArray(m + m + 1);
/* 105 */       this.requests.lazySet(m + m, m);
/* 106 */       this.emissions = new long[m];
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 111 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 112 */         this.upstream = s;
/*     */         
/* 114 */         if (s instanceof QueueSubscription) {
/*     */           
/* 116 */           QueueSubscription<T> qs = (QueueSubscription<T>)s;
/*     */           
/* 118 */           int m = qs.requestFusion(7);
/*     */           
/* 120 */           if (m == 1) {
/* 121 */             this.sourceMode = m;
/* 122 */             this.queue = (SimpleQueue<T>)qs;
/* 123 */             this.done = true;
/* 124 */             setupSubscribers();
/* 125 */             drain();
/*     */             return;
/*     */           } 
/* 128 */           if (m == 2) {
/* 129 */             this.sourceMode = m;
/* 130 */             this.queue = (SimpleQueue<T>)qs;
/*     */             
/* 132 */             setupSubscribers();
/*     */             
/* 134 */             s.request(this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 140 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
/*     */         
/* 142 */         setupSubscribers();
/*     */         
/* 144 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */     
/*     */     void setupSubscribers() {
/* 149 */       Subscriber<? super T>[] subs = this.subscribers;
/* 150 */       int m = subs.length;
/*     */       
/* 152 */       for (int i = 0; i < m; i++) {
/* 153 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 157 */         this.subscriberCount.lazySet(i + 1);
/*     */         
/* 159 */         subs[i].onSubscribe(new RailSubscription(i, m));
/*     */       } 
/*     */     }
/*     */     
/*     */     final class RailSubscription
/*     */       implements Subscription
/*     */     {
/*     */       final int j;
/*     */       final int m;
/*     */       
/*     */       RailSubscription(int j, int m) {
/* 170 */         this.j = j;
/* 171 */         this.m = m;
/*     */       }
/*     */ 
/*     */       
/*     */       public void request(long n) {
/* 176 */         if (SubscriptionHelper.validate(n)) {
/* 177 */           long r, u; AtomicLongArray ra = ParallelFromPublisher.ParallelDispatcher.this.requests;
/*     */           do {
/* 179 */             r = ra.get(this.j);
/* 180 */             if (r == Long.MAX_VALUE) {
/*     */               return;
/*     */             }
/* 183 */             u = BackpressureHelper.addCap(r, n);
/* 184 */           } while (!ra.compareAndSet(this.j, r, u));
/*     */ 
/*     */ 
/*     */           
/* 188 */           if (ParallelFromPublisher.ParallelDispatcher.this.subscriberCount.get() == this.m) {
/* 189 */             ParallelFromPublisher.ParallelDispatcher.this.drain();
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void cancel() {
/* 196 */         if (ParallelFromPublisher.ParallelDispatcher.this.requests.compareAndSet(this.m + this.j, 0L, 1L)) {
/* 197 */           ParallelFromPublisher.ParallelDispatcher.this.cancel(this.m + this.m);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 204 */       if (this.sourceMode == 0 && 
/* 205 */         !this.queue.offer(t)) {
/* 206 */         this.upstream.cancel();
/* 207 */         onError((Throwable)new MissingBackpressureException("Queue is full?"));
/*     */         
/*     */         return;
/*     */       } 
/* 211 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 216 */       this.error = t;
/* 217 */       this.done = true;
/* 218 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 223 */       this.done = true;
/* 224 */       drain();
/*     */     }
/*     */     
/*     */     void cancel(int m) {
/* 228 */       if (this.requests.decrementAndGet(m) == 0L) {
/* 229 */         this.cancelled = true;
/* 230 */         this.upstream.cancel();
/*     */         
/* 232 */         if (getAndIncrement() == 0) {
/* 233 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drainAsync() {
/* 239 */       int missed = 1;
/*     */       
/* 241 */       SimpleQueue<T> q = this.queue;
/* 242 */       Subscriber<? super T>[] a = this.subscribers;
/* 243 */       AtomicLongArray r = this.requests;
/* 244 */       long[] e = this.emissions;
/* 245 */       int n = e.length;
/* 246 */       int idx = this.index;
/* 247 */       int consumed = this.produced;
/*     */ 
/*     */       
/*     */       while (true) {
/* 251 */         int notReady = 0;
/*     */         
/*     */         do {
/* 254 */           if (this.cancelled) {
/* 255 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 259 */           boolean d = this.done;
/* 260 */           if (d) {
/* 261 */             Throwable ex = this.error;
/* 262 */             if (ex != null) {
/* 263 */               q.clear();
/* 264 */               for (Subscriber<? super T> s : a) {
/* 265 */                 s.onError(ex);
/*     */               }
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 271 */           boolean empty = q.isEmpty();
/*     */           
/* 273 */           if (d && empty) {
/* 274 */             for (Subscriber<? super T> s : a) {
/* 275 */               s.onComplete();
/*     */             }
/*     */             
/*     */             return;
/*     */           } 
/* 280 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 284 */           long requestAtIndex = r.get(idx);
/* 285 */           long emissionAtIndex = e[idx];
/* 286 */           if (requestAtIndex != emissionAtIndex && r.get(n + idx) == 0L) {
/*     */             T v;
/*     */ 
/*     */             
/*     */             try {
/* 291 */               v = (T)q.poll();
/* 292 */             } catch (Throwable ex) {
/* 293 */               Exceptions.throwIfFatal(ex);
/* 294 */               this.upstream.cancel();
/* 295 */               for (Subscriber<? super T> s : a) {
/* 296 */                 s.onError(ex);
/*     */               }
/*     */               
/*     */               return;
/*     */             } 
/* 301 */             if (v == null) {
/*     */               break;
/*     */             }
/*     */             
/* 305 */             a[idx].onNext(v);
/*     */             
/* 307 */             e[idx] = emissionAtIndex + 1L;
/*     */             
/* 309 */             int c = ++consumed;
/* 310 */             if (c == this.limit) {
/* 311 */               consumed = 0;
/* 312 */               this.upstream.request(c);
/*     */             } 
/* 314 */             notReady = 0;
/*     */           } else {
/* 316 */             notReady++;
/*     */           } 
/*     */           
/* 319 */           idx++;
/* 320 */           if (idx != n)
/* 321 */             continue;  idx = 0;
/*     */         
/*     */         }
/* 324 */         while (notReady != n);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 329 */         int w = get();
/* 330 */         if (w == missed) {
/* 331 */           this.index = idx;
/* 332 */           this.produced = consumed;
/* 333 */           missed = addAndGet(-missed);
/* 334 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 338 */         missed = w;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void drainSync() {
/* 344 */       int missed = 1;
/*     */       
/* 346 */       SimpleQueue<T> q = this.queue;
/* 347 */       Subscriber<? super T>[] a = this.subscribers;
/* 348 */       AtomicLongArray r = this.requests;
/* 349 */       long[] e = this.emissions;
/* 350 */       int n = e.length;
/* 351 */       int idx = this.index;
/*     */ 
/*     */       
/*     */       while (true) {
/* 355 */         int notReady = 0;
/*     */         
/*     */         do {
/* 358 */           if (this.cancelled) {
/* 359 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 363 */           boolean empty = q.isEmpty();
/*     */           
/* 365 */           if (empty) {
/* 366 */             for (Subscriber<? super T> s : a) {
/* 367 */               s.onComplete();
/*     */             }
/*     */             
/*     */             return;
/*     */           } 
/* 372 */           long requestAtIndex = r.get(idx);
/* 373 */           long emissionAtIndex = e[idx];
/* 374 */           if (requestAtIndex != emissionAtIndex && r.get(n + idx) == 0L) {
/*     */             T v;
/*     */ 
/*     */             
/*     */             try {
/* 379 */               v = (T)q.poll();
/* 380 */             } catch (Throwable ex) {
/* 381 */               Exceptions.throwIfFatal(ex);
/* 382 */               this.upstream.cancel();
/* 383 */               for (Subscriber<? super T> s : a) {
/* 384 */                 s.onError(ex);
/*     */               }
/*     */               
/*     */               return;
/*     */             } 
/* 389 */             if (v == null) {
/* 390 */               for (Subscriber<? super T> s : a) {
/* 391 */                 s.onComplete();
/*     */               }
/*     */               
/*     */               return;
/*     */             } 
/* 396 */             a[idx].onNext(v);
/*     */             
/* 398 */             e[idx] = emissionAtIndex + 1L;
/*     */             
/* 400 */             notReady = 0;
/*     */           } else {
/* 402 */             notReady++;
/*     */           } 
/*     */           
/* 405 */           idx++;
/* 406 */           if (idx != n)
/* 407 */             continue;  idx = 0;
/*     */         
/*     */         }
/* 410 */         while (notReady != n);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 415 */         int w = get();
/* 416 */         if (w == missed) {
/* 417 */           this.index = idx;
/* 418 */           missed = addAndGet(-missed);
/* 419 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 423 */         missed = w;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 429 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 433 */       if (this.sourceMode == 1) {
/* 434 */         drainSync();
/*     */       } else {
/* 436 */         drainAsync();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelFromPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */