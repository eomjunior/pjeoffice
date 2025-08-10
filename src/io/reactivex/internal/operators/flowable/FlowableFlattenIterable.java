/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableFlattenIterable<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */   final int prefetch;
/*     */   
/*     */   public FlowableFlattenIterable(Flowable<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper, int prefetch) {
/*  41 */     super(source);
/*  42 */     this.mapper = mapper;
/*  43 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super R> s) {
/*  49 */     if (this.source instanceof Callable) {
/*     */       T v;
/*     */       Iterator<? extends R> it;
/*     */       try {
/*  53 */         v = ((Callable<T>)this.source).call();
/*  54 */       } catch (Throwable ex) {
/*  55 */         Exceptions.throwIfFatal(ex);
/*  56 */         EmptySubscription.error(ex, s);
/*     */         
/*     */         return;
/*     */       } 
/*  60 */       if (v == null) {
/*  61 */         EmptySubscription.complete(s);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/*  68 */         Iterable<? extends R> iterable = (Iterable<? extends R>)this.mapper.apply(v);
/*     */         
/*  70 */         it = iterable.iterator();
/*  71 */       } catch (Throwable ex) {
/*  72 */         Exceptions.throwIfFatal(ex);
/*  73 */         EmptySubscription.error(ex, s);
/*     */         
/*     */         return;
/*     */       } 
/*  77 */       FlowableFromIterable.subscribe(s, it);
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     this.source.subscribe(new FlattenIterableSubscriber<T, R>(s, this.mapper, this.prefetch));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlattenIterableSubscriber<T, R>
/*     */     extends BasicIntQueueSubscription<R>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3096000382929934955L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     final AtomicReference<Throwable> error;
/*     */     
/*     */     Iterator<? extends R> current;
/*     */     
/*     */     int consumed;
/*     */     
/*     */     int fusionMode;
/*     */     
/*     */     FlattenIterableSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends Iterable<? extends R>> mapper, int prefetch) {
/* 118 */       this.downstream = actual;
/* 119 */       this.mapper = mapper;
/* 120 */       this.prefetch = prefetch;
/* 121 */       this.limit = prefetch - (prefetch >> 2);
/* 122 */       this.error = new AtomicReference<Throwable>();
/* 123 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 128 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 129 */         this.upstream = s;
/*     */         
/* 131 */         if (s instanceof QueueSubscription) {
/*     */           
/* 133 */           QueueSubscription<T> qs = (QueueSubscription<T>)s;
/*     */           
/* 135 */           int m = qs.requestFusion(3);
/*     */           
/* 137 */           if (m == 1) {
/* 138 */             this.fusionMode = m;
/* 139 */             this.queue = (SimpleQueue<T>)qs;
/* 140 */             this.done = true;
/*     */             
/* 142 */             this.downstream.onSubscribe((Subscription)this);
/*     */             
/*     */             return;
/*     */           } 
/* 146 */           if (m == 2) {
/* 147 */             this.fusionMode = m;
/* 148 */             this.queue = (SimpleQueue<T>)qs;
/*     */             
/* 150 */             this.downstream.onSubscribe((Subscription)this);
/*     */             
/* 152 */             s.request(this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 157 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
/*     */         
/* 159 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/* 161 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 167 */       if (this.done) {
/*     */         return;
/*     */       }
/* 170 */       if (this.fusionMode == 0 && !this.queue.offer(t)) {
/* 171 */         onError((Throwable)new MissingBackpressureException("Queue is full?!"));
/*     */         return;
/*     */       } 
/* 174 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 179 */       if (!this.done && ExceptionHelper.addThrowable(this.error, t)) {
/* 180 */         this.done = true;
/* 181 */         drain();
/*     */       } else {
/* 183 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 189 */       if (this.done) {
/*     */         return;
/*     */       }
/* 192 */       this.done = true;
/* 193 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 198 */       if (SubscriptionHelper.validate(n)) {
/* 199 */         BackpressureHelper.add(this.requested, n);
/* 200 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 206 */       if (!this.cancelled) {
/* 207 */         this.cancelled = true;
/*     */         
/* 209 */         this.upstream.cancel();
/*     */         
/* 211 */         if (getAndIncrement() == 0) {
/* 212 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 218 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 222 */       Subscriber<? super R> a = this.downstream;
/* 223 */       SimpleQueue<T> q = this.queue;
/* 224 */       boolean replenish = (this.fusionMode != 1);
/*     */       
/* 226 */       int missed = 1;
/*     */       
/* 228 */       Iterator<? extends R> it = this.current;
/*     */ 
/*     */       
/*     */       while (true) {
/* 232 */         if (it == null) {
/*     */           T t;
/* 234 */           boolean d = this.done;
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 239 */             t = (T)q.poll();
/* 240 */           } catch (Throwable ex) {
/* 241 */             Exceptions.throwIfFatal(ex);
/* 242 */             this.upstream.cancel();
/* 243 */             ExceptionHelper.addThrowable(this.error, ex);
/* 244 */             ex = ExceptionHelper.terminate(this.error);
/*     */             
/* 246 */             this.current = null;
/* 247 */             q.clear();
/*     */             
/* 249 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 253 */           boolean empty = (t == null);
/*     */           
/* 255 */           if (checkTerminated(d, empty, a, q)) {
/*     */             return;
/*     */           }
/*     */           
/* 259 */           if (t != null) {
/*     */             boolean b;
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 265 */               Iterable<? extends R> iterable = (Iterable<? extends R>)this.mapper.apply(t);
/*     */               
/* 267 */               it = iterable.iterator();
/*     */               
/* 269 */               b = it.hasNext();
/* 270 */             } catch (Throwable ex) {
/* 271 */               Exceptions.throwIfFatal(ex);
/* 272 */               this.upstream.cancel();
/* 273 */               ExceptionHelper.addThrowable(this.error, ex);
/* 274 */               ex = ExceptionHelper.terminate(this.error);
/* 275 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 279 */             if (!b) {
/* 280 */               it = null;
/* 281 */               consumedOne(replenish);
/*     */               
/*     */               continue;
/*     */             } 
/* 285 */             this.current = it;
/*     */           } 
/*     */         } 
/*     */         
/* 289 */         if (it != null) {
/* 290 */           long r = this.requested.get();
/* 291 */           long e = 0L;
/*     */           
/* 293 */           while (e != r) {
/* 294 */             R v; boolean b; if (checkTerminated(this.done, false, a, q)) {
/*     */               return;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 301 */               v = (R)ObjectHelper.requireNonNull(it.next(), "The iterator returned a null value");
/* 302 */             } catch (Throwable ex) {
/* 303 */               Exceptions.throwIfFatal(ex);
/* 304 */               this.current = null;
/* 305 */               this.upstream.cancel();
/* 306 */               ExceptionHelper.addThrowable(this.error, ex);
/* 307 */               ex = ExceptionHelper.terminate(this.error);
/* 308 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 312 */             a.onNext(v);
/*     */             
/* 314 */             if (checkTerminated(this.done, false, a, q)) {
/*     */               return;
/*     */             }
/*     */             
/* 318 */             e++;
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 323 */               b = it.hasNext();
/* 324 */             } catch (Throwable ex) {
/* 325 */               Exceptions.throwIfFatal(ex);
/* 326 */               this.current = null;
/* 327 */               this.upstream.cancel();
/* 328 */               ExceptionHelper.addThrowable(this.error, ex);
/* 329 */               ex = ExceptionHelper.terminate(this.error);
/* 330 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 334 */             if (!b) {
/* 335 */               consumedOne(replenish);
/* 336 */               it = null;
/* 337 */               this.current = null;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 342 */           if (e == r) {
/* 343 */             boolean d = this.done;
/* 344 */             boolean empty = (q.isEmpty() && it == null);
/*     */             
/* 346 */             if (checkTerminated(d, empty, a, q)) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */           
/* 351 */           if (e != 0L && 
/* 352 */             r != Long.MAX_VALUE) {
/* 353 */             this.requested.addAndGet(-e);
/*     */           }
/*     */ 
/*     */           
/* 357 */           if (it == null) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */         
/* 362 */         missed = addAndGet(-missed);
/* 363 */         if (missed == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void consumedOne(boolean enabled) {
/* 370 */       if (enabled) {
/* 371 */         int c = this.consumed + 1;
/* 372 */         if (c == this.limit) {
/* 373 */           this.consumed = 0;
/* 374 */           this.upstream.request(c);
/*     */         } else {
/* 376 */           this.consumed = c;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SimpleQueue<?> q) {
/* 382 */       if (this.cancelled) {
/* 383 */         this.current = null;
/* 384 */         q.clear();
/* 385 */         return true;
/*     */       } 
/* 387 */       if (d) {
/* 388 */         Throwable ex = this.error.get();
/* 389 */         if (ex != null) {
/* 390 */           ex = ExceptionHelper.terminate(this.error);
/*     */           
/* 392 */           this.current = null;
/* 393 */           q.clear();
/*     */           
/* 395 */           a.onError(ex);
/* 396 */           return true;
/* 397 */         }  if (empty) {
/* 398 */           a.onComplete();
/* 399 */           return true;
/*     */         } 
/*     */       } 
/* 402 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 407 */       this.current = null;
/* 408 */       this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 413 */       return (this.current == null && this.queue.isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public R poll() throws Exception {
/* 419 */       Iterator<? extends R> it = this.current;
/*     */       
/* 421 */       while (it == null) {
/* 422 */         T v = (T)this.queue.poll();
/* 423 */         if (v == null) {
/* 424 */           return null;
/*     */         }
/*     */         
/* 427 */         it = ((Iterable<? extends R>)this.mapper.apply(v)).iterator();
/*     */         
/* 429 */         if (!it.hasNext()) {
/* 430 */           it = null;
/*     */           continue;
/*     */         } 
/* 433 */         this.current = it;
/*     */       } 
/*     */       
/* 436 */       R r = (R)ObjectHelper.requireNonNull(it.next(), "The iterator returned a null value");
/*     */       
/* 438 */       if (!it.hasNext()) {
/* 439 */         this.current = null;
/*     */       }
/*     */       
/* 442 */       return r;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int requestFusion(int requestedMode) {
/* 448 */       if ((requestedMode & 0x1) != 0 && this.fusionMode == 1) {
/* 449 */         return 1;
/*     */       }
/* 451 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFlattenIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */