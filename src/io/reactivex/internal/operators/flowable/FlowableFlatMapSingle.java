/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ public final class FlowableFlatMapSingle<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   final boolean delayErrors;
/*     */   final int maxConcurrency;
/*     */   
/*     */   public FlowableFlatMapSingle(Flowable<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayError, int maxConcurrency) {
/*  46 */     super(source);
/*  47 */     this.mapper = mapper;
/*  48 */     this.delayErrors = delayError;
/*  49 */     this.maxConcurrency = maxConcurrency;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  54 */     this.source.subscribe(new FlatMapSingleSubscriber<T, R>(s, this.mapper, this.delayErrors, this.maxConcurrency));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapSingleSubscriber<T, R>
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
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     final AtomicReference<SpscLinkedArrayQueue<R>> queue;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     FlatMapSingleSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
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
/*     */       SingleSource<? extends R> ms;
/*     */       try {
/* 119 */         ms = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null SingleSource");
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
/*     */     void drain() {
/* 246 */       if (getAndIncrement() == 0) {
/* 247 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void clear() {
/* 252 */       SpscLinkedArrayQueue<R> q = this.queue.get();
/* 253 */       if (q != null) {
/* 254 */         q.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 259 */       int missed = 1;
/* 260 */       Subscriber<? super R> a = this.downstream;
/* 261 */       AtomicInteger n = this.active;
/* 262 */       AtomicReference<SpscLinkedArrayQueue<R>> qr = this.queue;
/*     */       
/*     */       do {
/* 265 */         long r = this.requested.get();
/* 266 */         long e = 0L;
/*     */         
/* 268 */         while (e != r) {
/* 269 */           if (this.cancelled) {
/* 270 */             clear();
/*     */             
/*     */             return;
/*     */           } 
/* 274 */           if (!this.delayErrors) {
/* 275 */             Throwable ex = (Throwable)this.errors.get();
/* 276 */             if (ex != null) {
/* 277 */               ex = this.errors.terminate();
/* 278 */               clear();
/* 279 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 284 */           boolean d = (n.get() == 0);
/* 285 */           SpscLinkedArrayQueue<R> q = qr.get();
/* 286 */           R v = (q != null) ? (R)q.poll() : null;
/* 287 */           boolean empty = (v == null);
/*     */           
/* 289 */           if (d && empty) {
/* 290 */             Throwable ex = this.errors.terminate();
/* 291 */             if (ex != null) {
/* 292 */               a.onError(ex);
/*     */             } else {
/* 294 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 299 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 303 */           a.onNext(v);
/*     */           
/* 305 */           e++;
/*     */         } 
/*     */         
/* 308 */         if (e == r) {
/* 309 */           if (this.cancelled) {
/* 310 */             clear();
/*     */             
/*     */             return;
/*     */           } 
/* 314 */           if (!this.delayErrors) {
/* 315 */             Throwable ex = (Throwable)this.errors.get();
/* 316 */             if (ex != null) {
/* 317 */               ex = this.errors.terminate();
/* 318 */               clear();
/* 319 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 324 */           boolean d = (n.get() == 0);
/* 325 */           SpscLinkedArrayQueue<R> q = qr.get();
/* 326 */           boolean empty = (q == null || q.isEmpty());
/*     */           
/* 328 */           if (d && empty) {
/* 329 */             Throwable ex = this.errors.terminate();
/* 330 */             if (ex != null) {
/* 331 */               a.onError(ex);
/*     */             } else {
/* 333 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 339 */         if (e != 0L) {
/* 340 */           BackpressureHelper.produced(this.requested, e);
/* 341 */           if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 342 */             this.upstream.request(e);
/*     */           }
/*     */         } 
/*     */         
/* 346 */         missed = addAndGet(-missed);
/* 347 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     final class InnerObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements SingleObserver<R>, Disposable
/*     */     {
/*     */       private static final long serialVersionUID = -502562646270949838L;
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 359 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R value) {
/* 364 */         FlowableFlatMapSingle.FlatMapSingleSubscriber.this.innerSuccess(this, value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 369 */         FlowableFlatMapSingle.FlatMapSingleSubscriber.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 374 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 379 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */