/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ErrorMode;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class FlowableConcatMap<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */   final int prefetch;
/*     */   final ErrorMode errorMode;
/*     */   
/*     */   public FlowableConcatMap(Flowable<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/*  41 */     super(source);
/*  42 */     this.mapper = mapper;
/*  43 */     this.prefetch = prefetch;
/*  44 */     this.errorMode = errorMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T, R> Subscriber<T> subscribe(Subscriber<? super R> s, Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/*  49 */     switch (errorMode) {
/*     */       case BOUNDARY:
/*  51 */         return (Subscriber)new ConcatMapDelayed<T, R>(s, mapper, prefetch, false);
/*     */       case END:
/*  53 */         return (Subscriber)new ConcatMapDelayed<T, R>(s, mapper, prefetch, true);
/*     */     } 
/*  55 */     return (Subscriber)new ConcatMapImmediate<T, R>(s, mapper, prefetch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  62 */     if (FlowableScalarXMap.tryScalarXMapSubscribe((Publisher<T>)this.source, s, this.mapper)) {
/*     */       return;
/*     */     }
/*     */     
/*  66 */     this.source.subscribe(subscribe(s, this.mapper, this.prefetch, this.errorMode));
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseConcatMapSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, ConcatMapSupport<R>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -3511336836796789179L;
/*     */     
/*     */     final FlowableConcatMap.ConcatMapInner<R> inner;
/*     */     
/*     */     final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     int consumed;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     volatile boolean active;
/*     */     
/*     */     int sourceMode;
/*     */ 
/*     */     
/*     */     BaseConcatMapSubscriber(Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch) {
/* 102 */       this.mapper = mapper;
/* 103 */       this.prefetch = prefetch;
/* 104 */       this.limit = prefetch - (prefetch >> 2);
/* 105 */       this.inner = new FlowableConcatMap.ConcatMapInner<R>(this);
/* 106 */       this.errors = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onSubscribe(Subscription s) {
/* 111 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 112 */         this.upstream = s;
/*     */         
/* 114 */         if (s instanceof QueueSubscription) {
/* 115 */           QueueSubscription<T> f = (QueueSubscription<T>)s;
/* 116 */           int m = f.requestFusion(7);
/* 117 */           if (m == 1) {
/* 118 */             this.sourceMode = m;
/* 119 */             this.queue = (SimpleQueue<T>)f;
/* 120 */             this.done = true;
/*     */             
/* 122 */             subscribeActual();
/*     */             
/* 124 */             drain();
/*     */             return;
/*     */           } 
/* 127 */           if (m == 2) {
/* 128 */             this.sourceMode = m;
/* 129 */             this.queue = (SimpleQueue<T>)f;
/*     */             
/* 131 */             subscribeActual();
/*     */             
/* 133 */             s.request(this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 138 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
/*     */         
/* 140 */         subscribeActual();
/*     */         
/* 142 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     abstract void drain();
/*     */     
/*     */     abstract void subscribeActual();
/*     */     
/*     */     public final void onNext(T t) {
/* 152 */       if (this.sourceMode != 2 && 
/* 153 */         !this.queue.offer(t)) {
/* 154 */         this.upstream.cancel();
/* 155 */         onError(new IllegalStateException("Queue full?!"));
/*     */         
/*     */         return;
/*     */       } 
/* 159 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onComplete() {
/* 164 */       this.done = true;
/* 165 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void innerComplete() {
/* 170 */       this.active = false;
/* 171 */       drain();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapImmediate<T, R>
/*     */     extends BaseConcatMapSubscriber<T, R>
/*     */   {
/*     */     private static final long serialVersionUID = 7898995095634264146L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final AtomicInteger wip;
/*     */ 
/*     */     
/*     */     ConcatMapImmediate(Subscriber<? super R> actual, Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch) {
/* 188 */       super(mapper, prefetch);
/* 189 */       this.downstream = actual;
/* 190 */       this.wip = new AtomicInteger();
/*     */     }
/*     */ 
/*     */     
/*     */     void subscribeActual() {
/* 195 */       this.downstream.onSubscribe(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 200 */       if (this.errors.addThrowable(t)) {
/* 201 */         this.inner.cancel();
/*     */         
/* 203 */         if (getAndIncrement() == 0) {
/* 204 */           this.downstream.onError(this.errors.terminate());
/*     */         }
/*     */       } else {
/* 207 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerNext(R value) {
/* 213 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 214 */         this.downstream.onNext(value);
/* 215 */         if (compareAndSet(1, 0)) {
/*     */           return;
/*     */         }
/* 218 */         this.downstream.onError(this.errors.terminate());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerError(Throwable e) {
/* 224 */       if (this.errors.addThrowable(e)) {
/* 225 */         this.upstream.cancel();
/*     */         
/* 227 */         if (getAndIncrement() == 0) {
/* 228 */           this.downstream.onError(this.errors.terminate());
/*     */         }
/*     */       } else {
/* 231 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 237 */       this.inner.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 242 */       if (!this.cancelled) {
/* 243 */         this.cancelled = true;
/*     */         
/* 245 */         this.inner.cancel();
/* 246 */         this.upstream.cancel();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 252 */       if (this.wip.getAndIncrement() == 0)
/*     */         while (true) {
/* 254 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 258 */           if (!this.active) {
/* 259 */             T v; boolean d = this.done;
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 264 */               v = (T)this.queue.poll();
/* 265 */             } catch (Throwable e) {
/* 266 */               Exceptions.throwIfFatal(e);
/* 267 */               this.upstream.cancel();
/* 268 */               this.errors.addThrowable(e);
/* 269 */               this.downstream.onError(this.errors.terminate());
/*     */               
/*     */               return;
/*     */             } 
/* 273 */             boolean empty = (v == null);
/*     */             
/* 275 */             if (d && empty) {
/* 276 */               this.downstream.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/* 280 */             if (!empty) {
/*     */               Publisher<? extends R> p;
/*     */               
/*     */               try {
/* 284 */                 p = (Publisher<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null Publisher");
/* 285 */               } catch (Throwable e) {
/* 286 */                 Exceptions.throwIfFatal(e);
/*     */                 
/* 288 */                 this.upstream.cancel();
/* 289 */                 this.errors.addThrowable(e);
/* 290 */                 this.downstream.onError(this.errors.terminate());
/*     */                 
/*     */                 return;
/*     */               } 
/* 294 */               if (this.sourceMode != 1) {
/* 295 */                 int c = this.consumed + 1;
/* 296 */                 if (c == this.limit) {
/* 297 */                   this.consumed = 0;
/* 298 */                   this.upstream.request(c);
/*     */                 } else {
/* 300 */                   this.consumed = c;
/*     */                 } 
/*     */               } 
/*     */               
/* 304 */               if (p instanceof Callable) {
/*     */                 R vr;
/* 306 */                 Callable<R> callable = (Callable)p;
/*     */ 
/*     */ 
/*     */                 
/*     */                 try {
/* 311 */                   vr = callable.call();
/* 312 */                 } catch (Throwable e) {
/* 313 */                   Exceptions.throwIfFatal(e);
/* 314 */                   this.upstream.cancel();
/* 315 */                   this.errors.addThrowable(e);
/* 316 */                   this.downstream.onError(this.errors.terminate());
/*     */                   
/*     */                   return;
/*     */                 } 
/* 320 */                 if (vr == null) {
/*     */                   continue;
/*     */                 }
/*     */                 
/* 324 */                 if (this.inner.isUnbounded()) {
/* 325 */                   if (get() == 0 && compareAndSet(0, 1)) {
/* 326 */                     this.downstream.onNext(vr);
/* 327 */                     if (!compareAndSet(1, 0)) {
/* 328 */                       this.downstream.onError(this.errors.terminate());
/*     */                       return;
/*     */                     } 
/*     */                   } 
/*     */                   continue;
/*     */                 } 
/* 334 */                 this.active = true;
/* 335 */                 this.inner.setSubscription(new FlowableConcatMap.SimpleScalarSubscription<R>(vr, (Subscriber<? super R>)this.inner));
/*     */               }
/*     */               else {
/*     */                 
/* 339 */                 this.active = true;
/* 340 */                 p.subscribe((Subscriber)this.inner);
/*     */               } 
/*     */             } 
/*     */           } 
/* 344 */           if (this.wip.decrementAndGet() == 0) {
/*     */             break;
/*     */           }
/*     */         }  
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SimpleScalarSubscription<T>
/*     */     extends AtomicBoolean
/*     */     implements Subscription
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     final T value;
/*     */     
/*     */     SimpleScalarSubscription(T value, Subscriber<? super T> downstream) {
/* 359 */       this.value = value;
/* 360 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 365 */       if (n > 0L && compareAndSet(false, true)) {
/* 366 */         Subscriber<? super T> a = this.downstream;
/* 367 */         a.onNext(this.value);
/* 368 */         a.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapDelayed<T, R>
/*     */     extends BaseConcatMapSubscriber<T, R>
/*     */   {
/*     */     private static final long serialVersionUID = -2945777694260521066L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final boolean veryEnd;
/*     */ 
/*     */     
/*     */     ConcatMapDelayed(Subscriber<? super R> actual, Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, boolean veryEnd) {
/* 390 */       super(mapper, prefetch);
/* 391 */       this.downstream = actual;
/* 392 */       this.veryEnd = veryEnd;
/*     */     }
/*     */ 
/*     */     
/*     */     void subscribeActual() {
/* 397 */       this.downstream.onSubscribe(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 402 */       if (this.errors.addThrowable(t)) {
/* 403 */         this.done = true;
/* 404 */         drain();
/*     */       } else {
/* 406 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerNext(R value) {
/* 412 */       this.downstream.onNext(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerError(Throwable e) {
/* 417 */       if (this.errors.addThrowable(e)) {
/* 418 */         if (!this.veryEnd) {
/* 419 */           this.upstream.cancel();
/* 420 */           this.done = true;
/*     */         } 
/* 422 */         this.active = false;
/* 423 */         drain();
/*     */       } else {
/* 425 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 431 */       this.inner.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 436 */       if (!this.cancelled) {
/* 437 */         this.cancelled = true;
/*     */         
/* 439 */         this.inner.cancel();
/* 440 */         this.upstream.cancel();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 446 */       if (getAndIncrement() == 0) {
/*     */         while (true) {
/*     */           
/* 449 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 453 */           if (!this.active) {
/*     */             T v;
/* 455 */             boolean d = this.done;
/*     */             
/* 457 */             if (d && !this.veryEnd) {
/* 458 */               Throwable ex = (Throwable)this.errors.get();
/* 459 */               if (ex != null) {
/* 460 */                 this.downstream.onError(this.errors.terminate());
/*     */ 
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */             
/*     */             try {
/* 468 */               v = (T)this.queue.poll();
/* 469 */             } catch (Throwable e) {
/* 470 */               Exceptions.throwIfFatal(e);
/* 471 */               this.upstream.cancel();
/* 472 */               this.errors.addThrowable(e);
/* 473 */               this.downstream.onError(this.errors.terminate());
/*     */               
/*     */               return;
/*     */             } 
/* 477 */             boolean empty = (v == null);
/*     */             
/* 479 */             if (d && empty) {
/* 480 */               Throwable ex = this.errors.terminate();
/* 481 */               if (ex != null) {
/* 482 */                 this.downstream.onError(ex);
/*     */               } else {
/* 484 */                 this.downstream.onComplete();
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/* 489 */             if (!empty) {
/*     */               Publisher<? extends R> p;
/*     */               
/*     */               try {
/* 493 */                 p = (Publisher<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null Publisher");
/* 494 */               } catch (Throwable e) {
/* 495 */                 Exceptions.throwIfFatal(e);
/*     */                 
/* 497 */                 this.upstream.cancel();
/* 498 */                 this.errors.addThrowable(e);
/* 499 */                 this.downstream.onError(this.errors.terminate());
/*     */                 
/*     */                 return;
/*     */               } 
/* 503 */               if (this.sourceMode != 1) {
/* 504 */                 int c = this.consumed + 1;
/* 505 */                 if (c == this.limit) {
/* 506 */                   this.consumed = 0;
/* 507 */                   this.upstream.request(c);
/*     */                 } else {
/* 509 */                   this.consumed = c;
/*     */                 } 
/*     */               } 
/*     */               
/* 513 */               if (p instanceof Callable) {
/*     */                 R vr;
/* 515 */                 Callable<R> supplier = (Callable)p;
/*     */ 
/*     */ 
/*     */                 
/*     */                 try {
/* 520 */                   vr = supplier.call();
/* 521 */                 } catch (Throwable e) {
/* 522 */                   Exceptions.throwIfFatal(e);
/* 523 */                   this.errors.addThrowable(e);
/* 524 */                   if (!this.veryEnd) {
/* 525 */                     this.upstream.cancel();
/* 526 */                     this.downstream.onError(this.errors.terminate());
/*     */                     return;
/*     */                   } 
/* 529 */                   vr = null;
/*     */                 } 
/*     */                 
/* 532 */                 if (vr == null) {
/*     */                   continue;
/*     */                 }
/*     */                 
/* 536 */                 if (this.inner.isUnbounded()) {
/* 537 */                   this.downstream.onNext(vr);
/*     */                   continue;
/*     */                 } 
/* 540 */                 this.active = true;
/* 541 */                 this.inner.setSubscription(new FlowableConcatMap.SimpleScalarSubscription<R>(vr, (Subscriber<? super R>)this.inner));
/*     */               } else {
/*     */                 
/* 544 */                 this.active = true;
/* 545 */                 p.subscribe((Subscriber)this.inner);
/*     */               } 
/*     */             } 
/*     */           } 
/* 549 */           if (decrementAndGet() == 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapInner<R>
/*     */     extends SubscriptionArbiter
/*     */     implements FlowableSubscriber<R>
/*     */   {
/*     */     private static final long serialVersionUID = 897683679971470653L;
/*     */ 
/*     */ 
/*     */     
/*     */     final FlowableConcatMap.ConcatMapSupport<R> parent;
/*     */ 
/*     */ 
/*     */     
/*     */     long produced;
/*     */ 
/*     */ 
/*     */     
/*     */     ConcatMapInner(FlowableConcatMap.ConcatMapSupport<R> parent) {
/* 577 */       super(false);
/* 578 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 583 */       setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R t) {
/* 588 */       this.produced++;
/*     */       
/* 590 */       this.parent.innerNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 595 */       long p = this.produced;
/*     */       
/* 597 */       if (p != 0L) {
/* 598 */         this.produced = 0L;
/* 599 */         produced(p);
/*     */       } 
/*     */       
/* 602 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 607 */       long p = this.produced;
/*     */       
/* 609 */       if (p != 0L) {
/* 610 */         this.produced = 0L;
/* 611 */         produced(p);
/*     */       } 
/*     */       
/* 614 */       this.parent.innerComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static interface ConcatMapSupport<T> {
/*     */     void innerNext(T param1T);
/*     */     
/*     */     void innerComplete();
/*     */     
/*     */     void innerError(Throwable param1Throwable);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */