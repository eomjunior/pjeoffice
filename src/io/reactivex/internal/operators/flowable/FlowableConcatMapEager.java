/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscribers.InnerQueuedSubscriber;
/*     */ import io.reactivex.internal.subscribers.InnerQueuedSubscriberSupport;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ErrorMode;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class FlowableConcatMapEager<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */   final int maxConcurrency;
/*     */   final int prefetch;
/*     */   final ErrorMode errorMode;
/*     */   
/*     */   public FlowableConcatMapEager(Flowable<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch, ErrorMode errorMode) {
/*  46 */     super(source);
/*  47 */     this.mapper = mapper;
/*  48 */     this.maxConcurrency = maxConcurrency;
/*  49 */     this.prefetch = prefetch;
/*  50 */     this.errorMode = errorMode;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  55 */     this.source.subscribe(new ConcatMapEagerDelayErrorSubscriber<T, R>(s, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapEagerDelayErrorSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription, InnerQueuedSubscriberSupport<R>
/*     */   {
/*     */     private static final long serialVersionUID = -4255299542215038287L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */     
/*     */     final int maxConcurrency;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final ErrorMode errorMode;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final SpscLinkedArrayQueue<InnerQueuedSubscriber<R>> subscribers;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile InnerQueuedSubscriber<R> current;
/*     */ 
/*     */     
/*     */     ConcatMapEagerDelayErrorSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch, ErrorMode errorMode) {
/*  92 */       this.downstream = actual;
/*  93 */       this.mapper = mapper;
/*  94 */       this.maxConcurrency = maxConcurrency;
/*  95 */       this.prefetch = prefetch;
/*  96 */       this.errorMode = errorMode;
/*  97 */       this.subscribers = new SpscLinkedArrayQueue(Math.min(prefetch, maxConcurrency));
/*  98 */       this.errors = new AtomicThrowable();
/*  99 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 104 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 105 */         this.upstream = s;
/*     */         
/* 107 */         this.downstream.onSubscribe(this);
/*     */         
/* 109 */         s.request((this.maxConcurrency == Integer.MAX_VALUE) ? Long.MAX_VALUE : this.maxConcurrency);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       Publisher<? extends R> p;
/*     */       try {
/* 119 */         p = (Publisher<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
/* 120 */       } catch (Throwable ex) {
/* 121 */         Exceptions.throwIfFatal(ex);
/* 122 */         this.upstream.cancel();
/* 123 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 127 */       InnerQueuedSubscriber<R> inner = new InnerQueuedSubscriber(this, this.prefetch);
/*     */       
/* 129 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/*     */       
/* 133 */       this.subscribers.offer(inner);
/*     */       
/* 135 */       p.subscribe((Subscriber)inner);
/*     */       
/* 137 */       if (this.cancelled) {
/* 138 */         inner.cancel();
/* 139 */         drainAndCancel();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 145 */       if (this.errors.addThrowable(t)) {
/* 146 */         this.done = true;
/* 147 */         drain();
/*     */       } else {
/* 149 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 155 */       this.done = true;
/* 156 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 161 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 164 */       this.cancelled = true;
/* 165 */       this.upstream.cancel();
/*     */       
/* 167 */       drainAndCancel();
/*     */     }
/*     */     
/*     */     void drainAndCancel() {
/* 171 */       if (getAndIncrement() == 0) {
/*     */         do {
/* 173 */           cancelAll();
/* 174 */         } while (decrementAndGet() != 0);
/*     */       }
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 179 */       InnerQueuedSubscriber<R> inner = this.current;
/* 180 */       this.current = null;
/*     */       
/* 182 */       if (inner != null) {
/* 183 */         inner.cancel();
/*     */       }
/*     */       
/* 186 */       while ((inner = (InnerQueuedSubscriber<R>)this.subscribers.poll()) != null) {
/* 187 */         inner.cancel();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 193 */       if (SubscriptionHelper.validate(n)) {
/* 194 */         BackpressureHelper.add(this.requested, n);
/* 195 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerNext(InnerQueuedSubscriber<R> inner, R value) {
/* 201 */       if (inner.queue().offer(value)) {
/* 202 */         drain();
/*     */       } else {
/* 204 */         inner.cancel();
/* 205 */         innerError(inner, (Throwable)new MissingBackpressureException());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerError(InnerQueuedSubscriber<R> inner, Throwable e) {
/* 211 */       if (this.errors.addThrowable(e)) {
/* 212 */         inner.setDone();
/* 213 */         if (this.errorMode != ErrorMode.END) {
/* 214 */           this.upstream.cancel();
/*     */         }
/* 216 */         drain();
/*     */       } else {
/* 218 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerComplete(InnerQueuedSubscriber<R> inner) {
/* 224 */       inner.setDone();
/* 225 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void drain() {
/* 230 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 234 */       int missed = 1;
/* 235 */       InnerQueuedSubscriber<R> inner = this.current;
/* 236 */       Subscriber<? super R> a = this.downstream;
/* 237 */       ErrorMode em = this.errorMode;
/*     */       
/*     */       while (true) {
/* 240 */         long r = this.requested.get();
/* 241 */         long e = 0L;
/*     */         
/* 243 */         if (inner == null) {
/*     */           
/* 245 */           if (em != ErrorMode.END) {
/* 246 */             Throwable ex = (Throwable)this.errors.get();
/* 247 */             if (ex != null) {
/* 248 */               cancelAll();
/*     */               
/* 250 */               a.onError(this.errors.terminate());
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 255 */           boolean outerDone = this.done;
/*     */           
/* 257 */           inner = (InnerQueuedSubscriber<R>)this.subscribers.poll();
/*     */           
/* 259 */           if (outerDone && inner == null) {
/* 260 */             Throwable ex = this.errors.terminate();
/* 261 */             if (ex != null) {
/* 262 */               a.onError(ex);
/*     */             } else {
/* 264 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 269 */           if (inner != null) {
/* 270 */             this.current = inner;
/*     */           }
/*     */         } 
/*     */         
/* 274 */         boolean continueNextSource = false;
/*     */         
/* 276 */         if (inner != null) {
/* 277 */           SimpleQueue<R> q = inner.queue();
/* 278 */           if (q != null) {
/* 279 */             while (e != r) {
/* 280 */               R v; if (this.cancelled) {
/* 281 */                 cancelAll();
/*     */                 
/*     */                 return;
/*     */               } 
/* 285 */               if (em == ErrorMode.IMMEDIATE) {
/* 286 */                 Throwable ex = (Throwable)this.errors.get();
/* 287 */                 if (ex != null) {
/* 288 */                   this.current = null;
/* 289 */                   inner.cancel();
/* 290 */                   cancelAll();
/*     */                   
/* 292 */                   a.onError(this.errors.terminate());
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               } 
/* 297 */               boolean d = inner.isDone();
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 302 */                 v = (R)q.poll();
/* 303 */               } catch (Throwable ex) {
/* 304 */                 Exceptions.throwIfFatal(ex);
/* 305 */                 this.current = null;
/* 306 */                 inner.cancel();
/* 307 */                 cancelAll();
/* 308 */                 a.onError(ex);
/*     */                 
/*     */                 return;
/*     */               } 
/* 312 */               boolean empty = (v == null);
/*     */               
/* 314 */               if (d && empty) {
/* 315 */                 inner = null;
/* 316 */                 this.current = null;
/* 317 */                 this.upstream.request(1L);
/* 318 */                 continueNextSource = true;
/*     */                 
/*     */                 break;
/*     */               } 
/* 322 */               if (empty) {
/*     */                 break;
/*     */               }
/*     */               
/* 326 */               a.onNext(v);
/*     */               
/* 328 */               e++;
/*     */               
/* 330 */               inner.requestOne();
/*     */             } 
/*     */             
/* 333 */             if (e == r) {
/* 334 */               if (this.cancelled) {
/* 335 */                 cancelAll();
/*     */                 
/*     */                 return;
/*     */               } 
/* 339 */               if (em == ErrorMode.IMMEDIATE) {
/* 340 */                 Throwable ex = (Throwable)this.errors.get();
/* 341 */                 if (ex != null) {
/* 342 */                   this.current = null;
/* 343 */                   inner.cancel();
/* 344 */                   cancelAll();
/*     */                   
/* 346 */                   a.onError(this.errors.terminate());
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               } 
/* 351 */               boolean d = inner.isDone();
/*     */               
/* 353 */               boolean empty = q.isEmpty();
/*     */               
/* 355 */               if (d && empty) {
/* 356 */                 inner = null;
/* 357 */                 this.current = null;
/* 358 */                 this.upstream.request(1L);
/* 359 */                 continueNextSource = true;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 365 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 366 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 369 */         if (continueNextSource) {
/*     */           continue;
/*     */         }
/*     */         
/* 373 */         missed = addAndGet(-missed);
/* 374 */         if (missed == 0)
/*     */           break; 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatMapEager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */