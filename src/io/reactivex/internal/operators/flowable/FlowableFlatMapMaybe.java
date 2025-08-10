/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableFlatMapMaybe<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */   final boolean delayErrors;
/*     */   final int maxConcurrency;
/*     */   
/*     */   public FlowableFlatMapMaybe(Flowable<T> source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayError, int maxConcurrency) {
/*  46 */     super(source);
/*  47 */     this.mapper = mapper;
/*  48 */     this.delayErrors = delayError;
/*  49 */     this.maxConcurrency = maxConcurrency;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  54 */     this.source.subscribe(new FlatMapMaybeSubscriber<T, R>(s, this.mapper, this.delayErrors, this.maxConcurrency));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapMaybeSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 8600231336733376951L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final int maxConcurrency;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final CompositeDisposable set;
/*     */     
/*     */     final AtomicInteger active;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */     
/*     */     final AtomicReference<SpscLinkedArrayQueue<R>> queue;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     FlatMapMaybeSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
/*  87 */       this.downstream = actual;
/*  88 */       this.mapper = mapper;
/*  89 */       this.delayErrors = delayErrors;
/*  90 */       this.maxConcurrency = maxConcurrency;
/*  91 */       this.requested = new AtomicLong();
/*  92 */       this.set = new CompositeDisposable();
/*  93 */       this.errors = new AtomicThrowable();
/*  94 */       this.active = new AtomicInteger(1);
/*  95 */       this.queue = new AtomicReference<SpscLinkedArrayQueue<R>>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 100 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 101 */         this.upstream = s;
/*     */         
/* 103 */         this.downstream.onSubscribe(this);
/*     */         
/* 105 */         int m = this.maxConcurrency;
/* 106 */         if (m == Integer.MAX_VALUE) {
/* 107 */           s.request(Long.MAX_VALUE);
/*     */         } else {
/* 109 */           s.request(this.maxConcurrency);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       MaybeSource<? extends R> ms;
/*     */       try {
/* 119 */         ms = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null MaybeSource");
/* 120 */       } catch (Throwable ex) {
/* 121 */         Exceptions.throwIfFatal(ex);
/* 122 */         this.upstream.cancel();
/* 123 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 127 */       this.active.getAndIncrement();
/*     */       
/* 129 */       InnerObserver inner = new InnerObserver();
/*     */       
/* 131 */       if (!this.cancelled && this.set.add(inner)) {
/* 132 */         ms.subscribe(inner);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 138 */       this.active.decrementAndGet();
/* 139 */       if (this.errors.addThrowable(t)) {
/* 140 */         if (!this.delayErrors) {
/* 141 */           this.set.dispose();
/*     */         }
/* 143 */         drain();
/*     */       } else {
/* 145 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 151 */       this.active.decrementAndGet();
/* 152 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 157 */       this.cancelled = true;
/* 158 */       this.upstream.cancel();
/* 159 */       this.set.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 164 */       if (SubscriptionHelper.validate(n)) {
/* 165 */         BackpressureHelper.add(this.requested, n);
/* 166 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerSuccess(InnerObserver inner, R value) {
/* 171 */       this.set.delete(inner);
/* 172 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 173 */         boolean d = (this.active.decrementAndGet() == 0);
/* 174 */         if (this.requested.get() != 0L) {
/* 175 */           this.downstream.onNext(value);
/*     */           
/* 177 */           SpscLinkedArrayQueue<R> q = this.queue.get();
/*     */           
/* 179 */           if (d && (q == null || q.isEmpty())) {
/* 180 */             Throwable ex = this.errors.terminate();
/* 181 */             if (ex != null) {
/* 182 */               this.downstream.onError(ex);
/*     */             } else {
/* 184 */               this.downstream.onComplete();
/*     */             } 
/*     */             return;
/*     */           } 
/* 188 */           BackpressureHelper.produced(this.requested, 1L);
/* 189 */           if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 190 */             this.upstream.request(1L);
/*     */           }
/*     */         } else {
/* 193 */           SpscLinkedArrayQueue<R> q = getOrCreateQueue();
/* 194 */           synchronized (q) {
/* 195 */             q.offer(value);
/*     */           } 
/*     */         } 
/* 198 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 202 */         SpscLinkedArrayQueue<R> q = getOrCreateQueue();
/* 203 */         synchronized (q) {
/* 204 */           q.offer(value);
/*     */         } 
/* 206 */         this.active.decrementAndGet();
/* 207 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 211 */       drainLoop();
/*     */     }
/*     */     
/*     */     SpscLinkedArrayQueue<R> getOrCreateQueue() {
/*     */       while (true) {
/* 216 */         SpscLinkedArrayQueue<R> current = this.queue.get();
/* 217 */         if (current != null) {
/* 218 */           return current;
/*     */         }
/* 220 */         current = new SpscLinkedArrayQueue(Flowable.bufferSize());
/* 221 */         if (this.queue.compareAndSet(null, current)) {
/* 222 */           return current;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerError(InnerObserver inner, Throwable e) {
/* 228 */       this.set.delete(inner);
/* 229 */       if (this.errors.addThrowable(e)) {
/* 230 */         if (!this.delayErrors) {
/* 231 */           this.upstream.cancel();
/* 232 */           this.set.dispose();
/*     */         }
/* 234 */         else if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 235 */           this.upstream.request(1L);
/*     */         } 
/*     */         
/* 238 */         this.active.decrementAndGet();
/* 239 */         drain();
/*     */       } else {
/* 241 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete(InnerObserver inner) {
/* 246 */       this.set.delete(inner);
/*     */       
/* 248 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 249 */         boolean d = (this.active.decrementAndGet() == 0);
/* 250 */         SpscLinkedArrayQueue<R> q = this.queue.get();
/*     */         
/* 252 */         if (d && (q == null || q.isEmpty())) {
/* 253 */           Throwable ex = this.errors.terminate();
/* 254 */           if (ex != null) {
/* 255 */             this.downstream.onError(ex);
/*     */           } else {
/* 257 */             this.downstream.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 262 */         if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 263 */           this.upstream.request(1L);
/*     */         }
/* 265 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/* 268 */         drainLoop();
/*     */       } else {
/* 270 */         this.active.decrementAndGet();
/* 271 */         if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 272 */           this.upstream.request(1L);
/*     */         }
/* 274 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 279 */       if (getAndIncrement() == 0) {
/* 280 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void clear() {
/* 285 */       SpscLinkedArrayQueue<R> q = this.queue.get();
/* 286 */       if (q != null) {
/* 287 */         q.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 292 */       int missed = 1;
/* 293 */       Subscriber<? super R> a = this.downstream;
/* 294 */       AtomicInteger n = this.active;
/* 295 */       AtomicReference<SpscLinkedArrayQueue<R>> qr = this.queue;
/*     */       
/*     */       do {
/* 298 */         long r = this.requested.get();
/* 299 */         long e = 0L;
/*     */         
/* 301 */         while (e != r) {
/* 302 */           if (this.cancelled) {
/* 303 */             clear();
/*     */             
/*     */             return;
/*     */           } 
/* 307 */           if (!this.delayErrors) {
/* 308 */             Throwable ex = (Throwable)this.errors.get();
/* 309 */             if (ex != null) {
/* 310 */               ex = this.errors.terminate();
/* 311 */               clear();
/* 312 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 317 */           boolean d = (n.get() == 0);
/* 318 */           SpscLinkedArrayQueue<R> q = qr.get();
/* 319 */           R v = (q != null) ? (R)q.poll() : null;
/* 320 */           boolean empty = (v == null);
/*     */           
/* 322 */           if (d && empty) {
/* 323 */             Throwable ex = this.errors.terminate();
/* 324 */             if (ex != null) {
/* 325 */               a.onError(ex);
/*     */             } else {
/* 327 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 332 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 336 */           a.onNext(v);
/*     */           
/* 338 */           e++;
/*     */         } 
/*     */         
/* 341 */         if (e == r) {
/* 342 */           if (this.cancelled) {
/* 343 */             clear();
/*     */             
/*     */             return;
/*     */           } 
/* 347 */           if (!this.delayErrors) {
/* 348 */             Throwable ex = (Throwable)this.errors.get();
/* 349 */             if (ex != null) {
/* 350 */               ex = this.errors.terminate();
/* 351 */               clear();
/* 352 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 357 */           boolean d = (n.get() == 0);
/* 358 */           SpscLinkedArrayQueue<R> q = qr.get();
/* 359 */           boolean empty = (q == null || q.isEmpty());
/*     */           
/* 361 */           if (d && empty) {
/* 362 */             Throwable ex = this.errors.terminate();
/* 363 */             if (ex != null) {
/* 364 */               a.onError(ex);
/*     */             } else {
/* 366 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 372 */         if (e != 0L) {
/* 373 */           BackpressureHelper.produced(this.requested, e);
/* 374 */           if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 375 */             this.upstream.request(e);
/*     */           }
/*     */         } 
/*     */         
/* 379 */         missed = addAndGet(-missed);
/* 380 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     final class InnerObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements MaybeObserver<R>, Disposable
/*     */     {
/*     */       private static final long serialVersionUID = -502562646270949838L;
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 392 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R value) {
/* 397 */         FlowableFlatMapMaybe.FlatMapMaybeSubscriber.this.innerSuccess(this, value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 402 */         FlowableFlatMapMaybe.FlatMapMaybeSubscriber.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 407 */         FlowableFlatMapMaybe.FlatMapMaybeSubscriber.this.innerComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 412 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 417 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */