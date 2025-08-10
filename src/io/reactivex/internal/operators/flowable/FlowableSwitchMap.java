/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class FlowableSwitchMap<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */   final int bufferSize;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public FlowableSwitchMap(Flowable<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize, boolean delayErrors) {
/*  38 */     super(source);
/*  39 */     this.mapper = mapper;
/*  40 */     this.bufferSize = bufferSize;
/*  41 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  46 */     if (FlowableScalarXMap.tryScalarXMapSubscribe((Publisher<T>)this.source, s, this.mapper)) {
/*     */       return;
/*     */     }
/*  49 */     this.source.subscribe(new SwitchMapSubscriber<T, R>(s, this.mapper, this.bufferSize, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -3491074160481096299L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */     final int bufferSize;
/*     */     final boolean delayErrors;
/*     */     volatile boolean done;
/*     */     final AtomicThrowable error;
/*     */     volatile boolean cancelled;
/*     */     Subscription upstream;
/*  67 */     final AtomicReference<FlowableSwitchMap.SwitchMapInnerSubscriber<T, R>> active = new AtomicReference<FlowableSwitchMap.SwitchMapInnerSubscriber<T, R>>();
/*     */     
/*  69 */     final AtomicLong requested = new AtomicLong();
/*     */ 
/*     */ 
/*     */     
/*  73 */     static final FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object> CANCELLED = new FlowableSwitchMap.SwitchMapInnerSubscriber<Object, Object>(null, -1L, 1); static {
/*  74 */       CANCELLED.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     volatile long unique;
/*     */ 
/*     */     
/*     */     SwitchMapSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize, boolean delayErrors) {
/*  82 */       this.downstream = actual;
/*  83 */       this.mapper = mapper;
/*  84 */       this.bufferSize = bufferSize;
/*  85 */       this.delayErrors = delayErrors;
/*  86 */       this.error = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  91 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  92 */         this.upstream = s;
/*  93 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       Publisher<? extends R> p;
/*  99 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 103 */       long c = this.unique + 1L;
/* 104 */       this.unique = c;
/*     */       
/* 106 */       FlowableSwitchMap.SwitchMapInnerSubscriber<T, R> inner = this.active.get();
/* 107 */       if (inner != null) {
/* 108 */         inner.cancel();
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 113 */         p = (Publisher<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The publisher returned is null");
/* 114 */       } catch (Throwable e) {
/* 115 */         Exceptions.throwIfFatal(e);
/* 116 */         this.upstream.cancel();
/* 117 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 121 */       FlowableSwitchMap.SwitchMapInnerSubscriber<T, R> nextInner = new FlowableSwitchMap.SwitchMapInnerSubscriber<T, R>(this, c, this.bufferSize);
/*     */       
/*     */       while (true) {
/* 124 */         inner = this.active.get();
/* 125 */         if (inner == CANCELLED) {
/*     */           break;
/*     */         }
/* 128 */         if (this.active.compareAndSet(inner, nextInner)) {
/* 129 */           p.subscribe((Subscriber)nextInner);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 137 */       if (!this.done && this.error.addThrowable(t)) {
/* 138 */         if (!this.delayErrors) {
/* 139 */           disposeInner();
/*     */         }
/* 141 */         this.done = true;
/* 142 */         drain();
/*     */       } else {
/* 144 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 150 */       if (this.done) {
/*     */         return;
/*     */       }
/* 153 */       this.done = true;
/* 154 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 159 */       if (SubscriptionHelper.validate(n)) {
/* 160 */         BackpressureHelper.add(this.requested, n);
/* 161 */         if (this.unique == 0L) {
/* 162 */           this.upstream.request(Long.MAX_VALUE);
/*     */         } else {
/* 164 */           drain();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 171 */       if (!this.cancelled) {
/* 172 */         this.cancelled = true;
/* 173 */         this.upstream.cancel();
/*     */         
/* 175 */         disposeInner();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeInner() {
/* 181 */       FlowableSwitchMap.SwitchMapInnerSubscriber<T, R> a = this.active.get();
/* 182 */       if (a != CANCELLED) {
/* 183 */         a = (FlowableSwitchMap.SwitchMapInnerSubscriber<T, R>)this.active.getAndSet(CANCELLED);
/* 184 */         if (a != CANCELLED && a != null) {
/* 185 */           a.cancel();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 191 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 195 */       Subscriber<? super R> a = this.downstream;
/*     */       
/* 197 */       int missing = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 201 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 205 */         if (this.done) {
/* 206 */           if (this.delayErrors) {
/* 207 */             if (this.active.get() == null) {
/* 208 */               Throwable err = (Throwable)this.error.get();
/* 209 */               if (err != null) {
/* 210 */                 a.onError(this.error.terminate());
/*     */               } else {
/* 212 */                 a.onComplete();
/*     */               } 
/*     */               return;
/*     */             } 
/*     */           } else {
/* 217 */             Throwable err = (Throwable)this.error.get();
/* 218 */             if (err != null) {
/* 219 */               disposeInner();
/* 220 */               a.onError(this.error.terminate());
/*     */               return;
/*     */             } 
/* 223 */             if (this.active.get() == null) {
/* 224 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         }
/* 230 */         FlowableSwitchMap.SwitchMapInnerSubscriber<T, R> inner = this.active.get();
/* 231 */         SimpleQueue<R> q = (inner != null) ? inner.queue : null;
/* 232 */         if (q != null) {
/* 233 */           if (inner.done) {
/* 234 */             if (!this.delayErrors) {
/* 235 */               Throwable err = (Throwable)this.error.get();
/* 236 */               if (err != null) {
/* 237 */                 disposeInner();
/* 238 */                 a.onError(this.error.terminate());
/*     */                 return;
/*     */               } 
/* 241 */               if (q.isEmpty()) {
/* 242 */                 this.active.compareAndSet(inner, null);
/*     */                 
/*     */                 continue;
/*     */               } 
/* 246 */             } else if (q.isEmpty()) {
/* 247 */               this.active.compareAndSet(inner, null);
/*     */               
/*     */               continue;
/*     */             } 
/*     */           }
/*     */           
/* 253 */           long r = this.requested.get();
/* 254 */           long e = 0L;
/* 255 */           boolean retry = false;
/*     */           
/* 257 */           while (e != r) {
/* 258 */             R v; if (this.cancelled) {
/*     */               return;
/*     */             }
/*     */             
/* 262 */             boolean d = inner.done;
/*     */ 
/*     */             
/*     */             try {
/* 266 */               v = (R)q.poll();
/* 267 */             } catch (Throwable ex) {
/* 268 */               Exceptions.throwIfFatal(ex);
/* 269 */               inner.cancel();
/* 270 */               this.error.addThrowable(ex);
/* 271 */               d = true;
/* 272 */               v = null;
/*     */             } 
/* 274 */             boolean empty = (v == null);
/*     */             
/* 276 */             if (inner != this.active.get()) {
/* 277 */               retry = true;
/*     */               
/*     */               break;
/*     */             } 
/* 281 */             if (d) {
/* 282 */               if (!this.delayErrors) {
/* 283 */                 Throwable err = (Throwable)this.error.get();
/* 284 */                 if (err != null) {
/* 285 */                   a.onError(this.error.terminate());
/*     */                   return;
/*     */                 } 
/* 288 */                 if (empty) {
/* 289 */                   this.active.compareAndSet(inner, null);
/* 290 */                   retry = true;
/*     */                   
/*     */                   break;
/*     */                 } 
/* 294 */               } else if (empty) {
/* 295 */                 this.active.compareAndSet(inner, null);
/* 296 */                 retry = true;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             }
/*     */             
/* 302 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 306 */             a.onNext(v);
/*     */             
/* 308 */             e++;
/*     */           } 
/*     */           
/* 311 */           if (e != 0L && 
/* 312 */             !this.cancelled) {
/* 313 */             if (r != Long.MAX_VALUE) {
/* 314 */               this.requested.addAndGet(-e);
/*     */             }
/* 316 */             inner.request(e);
/*     */           } 
/*     */ 
/*     */           
/* 320 */           if (retry) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */         
/* 325 */         missing = addAndGet(-missing);
/* 326 */         if (missing == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapInnerSubscriber<T, R>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<R>
/*     */   {
/*     */     private static final long serialVersionUID = 3837284832786408377L;
/*     */     
/*     */     final FlowableSwitchMap.SwitchMapSubscriber<T, R> parent;
/*     */     final long index;
/*     */     final int bufferSize;
/*     */     volatile SimpleQueue<R> queue;
/*     */     volatile boolean done;
/*     */     int fusionMode;
/*     */     
/*     */     SwitchMapInnerSubscriber(FlowableSwitchMap.SwitchMapSubscriber<T, R> parent, long index, int bufferSize) {
/* 348 */       this.parent = parent;
/* 349 */       this.index = index;
/* 350 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 355 */       if (SubscriptionHelper.setOnce(this, s)) {
/* 356 */         if (s instanceof QueueSubscription) {
/*     */           
/* 358 */           QueueSubscription<R> qs = (QueueSubscription<R>)s;
/*     */           
/* 360 */           int m = qs.requestFusion(7);
/* 361 */           if (m == 1) {
/* 362 */             this.fusionMode = m;
/* 363 */             this.queue = (SimpleQueue<R>)qs;
/* 364 */             this.done = true;
/* 365 */             this.parent.drain();
/*     */             return;
/*     */           } 
/* 368 */           if (m == 2) {
/* 369 */             this.fusionMode = m;
/* 370 */             this.queue = (SimpleQueue<R>)qs;
/* 371 */             s.request(this.bufferSize);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 376 */         this.queue = (SimpleQueue<R>)new SpscArrayQueue(this.bufferSize);
/*     */         
/* 378 */         s.request(this.bufferSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R t) {
/* 384 */       FlowableSwitchMap.SwitchMapSubscriber<T, R> p = this.parent;
/* 385 */       if (this.index == p.unique) {
/* 386 */         if (this.fusionMode == 0 && !this.queue.offer(t)) {
/* 387 */           onError((Throwable)new MissingBackpressureException("Queue full?!"));
/*     */           return;
/*     */         } 
/* 390 */         p.drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 396 */       FlowableSwitchMap.SwitchMapSubscriber<T, R> p = this.parent;
/* 397 */       if (this.index == p.unique && p.error.addThrowable(t)) {
/* 398 */         if (!p.delayErrors) {
/* 399 */           p.upstream.cancel();
/* 400 */           p.done = true;
/*     */         } 
/* 402 */         this.done = true;
/* 403 */         p.drain();
/*     */       } else {
/* 405 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 411 */       FlowableSwitchMap.SwitchMapSubscriber<T, R> p = this.parent;
/* 412 */       if (this.index == p.unique) {
/* 413 */         this.done = true;
/* 414 */         p.drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void cancel() {
/* 419 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */     
/*     */     public void request(long n) {
/* 423 */       if (this.fusionMode != 1)
/* 424 */         get().request(n); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSwitchMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */