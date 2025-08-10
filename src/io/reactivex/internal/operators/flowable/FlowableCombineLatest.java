/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Iterator;
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
/*     */ public final class FlowableCombineLatest<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   @Nullable
/*     */   final Publisher<? extends T>[] array;
/*     */   @Nullable
/*     */   final Iterable<? extends Publisher<? extends T>> iterable;
/*     */   final Function<? super Object[], ? extends R> combiner;
/*     */   final int bufferSize;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public FlowableCombineLatest(@NonNull Publisher<? extends T>[] array, @NonNull Function<? super Object[], ? extends R> combiner, int bufferSize, boolean delayErrors) {
/*  56 */     this.array = array;
/*  57 */     this.iterable = null;
/*  58 */     this.combiner = combiner;
/*  59 */     this.bufferSize = bufferSize;
/*  60 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FlowableCombineLatest(@NonNull Iterable<? extends Publisher<? extends T>> iterable, @NonNull Function<? super Object[], ? extends R> combiner, int bufferSize, boolean delayErrors) {
/*  66 */     this.array = null;
/*  67 */     this.iterable = iterable;
/*  68 */     this.combiner = combiner;
/*  69 */     this.bufferSize = bufferSize;
/*  70 */     this.delayErrors = delayErrors;
/*     */   }
/*     */   
/*     */   public void subscribeActual(Subscriber<? super R> s) {
/*     */     Publisher[] arrayOfPublisher;
/*     */     int n;
/*  76 */     Publisher<? extends T>[] a = this.array;
/*     */     
/*  78 */     if (a == null) {
/*  79 */       Iterator<? extends Publisher<? extends T>> it; n = 0;
/*  80 */       arrayOfPublisher = new Publisher[8];
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  85 */         it = (Iterator<? extends Publisher<? extends T>>)ObjectHelper.requireNonNull(this.iterable.iterator(), "The iterator returned is null");
/*  86 */       } catch (Throwable e) {
/*  87 */         Exceptions.throwIfFatal(e);
/*  88 */         EmptySubscription.error(e, s);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       while (true) {
/*     */         boolean b;
/*     */         Publisher<? extends T> p;
/*     */         try {
/*  97 */           b = it.hasNext();
/*  98 */         } catch (Throwable e) {
/*  99 */           Exceptions.throwIfFatal(e);
/* 100 */           EmptySubscription.error(e, s);
/*     */           
/*     */           return;
/*     */         } 
/* 104 */         if (!b) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 111 */           p = (Publisher<? extends T>)ObjectHelper.requireNonNull(it.next(), "The publisher returned by the iterator is null");
/* 112 */         } catch (Throwable e) {
/* 113 */           Exceptions.throwIfFatal(e);
/* 114 */           EmptySubscription.error(e, s);
/*     */           
/*     */           return;
/*     */         } 
/* 118 */         if (n == arrayOfPublisher.length) {
/* 119 */           Publisher[] arrayOfPublisher1 = new Publisher[n + (n >> 2)];
/* 120 */           System.arraycopy(arrayOfPublisher, 0, arrayOfPublisher1, 0, n);
/* 121 */           arrayOfPublisher = arrayOfPublisher1;
/*     */         } 
/* 123 */         arrayOfPublisher[n++] = p;
/*     */       } 
/*     */     } else {
/*     */       
/* 127 */       n = arrayOfPublisher.length;
/*     */     } 
/*     */     
/* 130 */     if (n == 0) {
/* 131 */       EmptySubscription.complete(s);
/*     */       return;
/*     */     } 
/* 134 */     if (n == 1) {
/* 135 */       arrayOfPublisher[0].subscribe((Subscriber)new FlowableMap.MapSubscriber<Object, R>(s, new SingletonArrayFunc()));
/*     */       
/*     */       return;
/*     */     } 
/* 139 */     CombineLatestCoordinator<T, R> coordinator = new CombineLatestCoordinator<T, R>(s, this.combiner, n, this.bufferSize, this.delayErrors);
/*     */ 
/*     */     
/* 142 */     s.onSubscribe((Subscription)coordinator);
/*     */     
/* 144 */     coordinator.subscribe((Publisher<? extends T>[])arrayOfPublisher, n);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CombineLatestCoordinator<T, R>
/*     */     extends BasicIntQueueSubscription<R>
/*     */   {
/*     */     private static final long serialVersionUID = -5082275438355852221L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super Object[], ? extends R> combiner;
/*     */     
/*     */     final FlowableCombineLatest.CombineLatestInnerSubscriber<T>[] subscribers;
/*     */     
/*     */     final SpscLinkedArrayQueue<Object> queue;
/*     */     
/*     */     final Object[] latest;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     boolean outputFused;
/*     */     
/*     */     int nonEmptySources;
/*     */     
/*     */     int completedSources;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     final AtomicReference<Throwable> error;
/*     */ 
/*     */     
/*     */     CombineLatestCoordinator(Subscriber<? super R> actual, Function<? super Object[], ? extends R> combiner, int n, int bufferSize, boolean delayErrors) {
/* 181 */       this.downstream = actual;
/* 182 */       this.combiner = combiner;
/*     */       
/* 184 */       FlowableCombineLatest.CombineLatestInnerSubscriber[] arrayOfCombineLatestInnerSubscriber = new FlowableCombineLatest.CombineLatestInnerSubscriber[n];
/* 185 */       for (int i = 0; i < n; i++) {
/* 186 */         arrayOfCombineLatestInnerSubscriber[i] = new FlowableCombineLatest.CombineLatestInnerSubscriber<T>(this, i, bufferSize);
/*     */       }
/* 188 */       this.subscribers = (FlowableCombineLatest.CombineLatestInnerSubscriber<T>[])arrayOfCombineLatestInnerSubscriber;
/* 189 */       this.latest = new Object[n];
/* 190 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/* 191 */       this.requested = new AtomicLong();
/* 192 */       this.error = new AtomicReference<Throwable>();
/* 193 */       this.delayErrors = delayErrors;
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
/* 206 */       this.cancelled = true;
/* 207 */       cancelAll();
/*     */     }
/*     */     
/*     */     void subscribe(Publisher<? extends T>[] sources, int n) {
/* 211 */       FlowableCombineLatest.CombineLatestInnerSubscriber<T>[] a = this.subscribers;
/*     */       
/* 213 */       for (int i = 0; i < n; i++) {
/* 214 */         if (this.done || this.cancelled) {
/*     */           return;
/*     */         }
/* 217 */         sources[i].subscribe((Subscriber)a[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void innerValue(int index, T value) {
/*     */       boolean replenishInsteadOfDrain;
/* 225 */       synchronized (this) {
/* 226 */         Object[] os = this.latest;
/*     */         
/* 228 */         int localNonEmptySources = this.nonEmptySources;
/*     */         
/* 230 */         if (os[index] == null)
/*     */         {
/* 232 */           this.nonEmptySources = ++localNonEmptySources;
/*     */         }
/*     */         
/* 235 */         os[index] = value;
/*     */         
/* 237 */         if (os.length == localNonEmptySources) {
/*     */           
/* 239 */           this.queue.offer(this.subscribers[index], os.clone());
/*     */           
/* 241 */           replenishInsteadOfDrain = false;
/*     */         } else {
/* 243 */           replenishInsteadOfDrain = true;
/*     */         } 
/*     */       } 
/*     */       
/* 247 */       if (replenishInsteadOfDrain) {
/* 248 */         this.subscribers[index].requestOne();
/*     */       } else {
/* 250 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete(int index) {
/* 255 */       synchronized (this) {
/* 256 */         Object[] os = this.latest;
/*     */         
/* 258 */         if (os[index] != null) {
/* 259 */           int localCompletedSources = this.completedSources + 1;
/*     */           
/* 261 */           if (localCompletedSources == os.length) {
/* 262 */             this.done = true;
/*     */           } else {
/* 264 */             this.completedSources = localCompletedSources;
/*     */             return;
/*     */           } 
/*     */         } else {
/* 268 */           this.done = true;
/*     */         } 
/*     */       } 
/* 271 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void innerError(int index, Throwable e) {
/* 276 */       if (ExceptionHelper.addThrowable(this.error, e)) {
/* 277 */         if (!this.delayErrors) {
/* 278 */           cancelAll();
/* 279 */           this.done = true;
/* 280 */           drain();
/*     */         } else {
/* 282 */           innerComplete(index);
/*     */         } 
/*     */       } else {
/* 285 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void drainOutput() {
/* 290 */       Subscriber<? super R> a = this.downstream;
/* 291 */       SpscLinkedArrayQueue<Object> q = this.queue;
/*     */       
/* 293 */       int missed = 1;
/*     */ 
/*     */       
/*     */       do {
/* 297 */         if (this.cancelled) {
/* 298 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 302 */         Throwable ex = this.error.get();
/* 303 */         if (ex != null) {
/* 304 */           q.clear();
/*     */           
/* 306 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 310 */         boolean d = this.done;
/*     */         
/* 312 */         boolean empty = q.isEmpty();
/*     */         
/* 314 */         if (!empty) {
/* 315 */           a.onNext(null);
/*     */         }
/*     */         
/* 318 */         if (d && empty) {
/* 319 */           a.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 323 */         missed = addAndGet(-missed);
/* 324 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void drainAsync() {
/* 332 */       Subscriber<? super R> a = this.downstream;
/* 333 */       SpscLinkedArrayQueue<Object> q = this.queue;
/*     */       
/* 335 */       int missed = 1;
/*     */ 
/*     */       
/*     */       do {
/* 339 */         long r = this.requested.get();
/* 340 */         long e = 0L;
/*     */         
/* 342 */         while (e != r) {
/* 343 */           R w; boolean d = this.done;
/*     */           
/* 345 */           Object v = q.poll();
/*     */           
/* 347 */           boolean empty = (v == null);
/*     */           
/* 349 */           if (checkTerminated(d, empty, a, q)) {
/*     */             return;
/*     */           }
/*     */           
/* 353 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 357 */           T[] va = (T[])q.poll();
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 362 */             w = (R)ObjectHelper.requireNonNull(this.combiner.apply(va), "The combiner returned a null value");
/* 363 */           } catch (Throwable ex) {
/* 364 */             Exceptions.throwIfFatal(ex);
/*     */             
/* 366 */             cancelAll();
/* 367 */             ExceptionHelper.addThrowable(this.error, ex);
/* 368 */             ex = ExceptionHelper.terminate(this.error);
/*     */             
/* 370 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 374 */           a.onNext(w);
/*     */           
/* 376 */           ((FlowableCombineLatest.CombineLatestInnerSubscriber)v).requestOne();
/*     */           
/* 378 */           e++;
/*     */         } 
/*     */         
/* 381 */         if (e == r && 
/* 382 */           checkTerminated(this.done, q.isEmpty(), a, q)) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 387 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 388 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 391 */         missed = addAndGet(-missed);
/* 392 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void drain() {
/* 399 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 403 */       if (this.outputFused) {
/* 404 */         drainOutput();
/*     */       } else {
/* 406 */         drainAsync();
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SpscLinkedArrayQueue<?> q) {
/* 411 */       if (this.cancelled) {
/* 412 */         cancelAll();
/* 413 */         q.clear();
/* 414 */         return true;
/*     */       } 
/*     */       
/* 417 */       if (d) {
/* 418 */         if (this.delayErrors) {
/* 419 */           if (empty) {
/* 420 */             cancelAll();
/* 421 */             Throwable e = ExceptionHelper.terminate(this.error);
/*     */             
/* 423 */             if (e != null && e != ExceptionHelper.TERMINATED) {
/* 424 */               a.onError(e);
/*     */             } else {
/* 426 */               a.onComplete();
/*     */             } 
/* 428 */             return true;
/*     */           } 
/*     */         } else {
/* 431 */           Throwable e = ExceptionHelper.terminate(this.error);
/*     */           
/* 433 */           if (e != null && e != ExceptionHelper.TERMINATED) {
/* 434 */             cancelAll();
/* 435 */             q.clear();
/* 436 */             a.onError(e);
/* 437 */             return true;
/*     */           } 
/* 439 */           if (empty) {
/* 440 */             cancelAll();
/*     */             
/* 442 */             a.onComplete();
/* 443 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/* 447 */       return false;
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 451 */       for (FlowableCombineLatest.CombineLatestInnerSubscriber<T> inner : this.subscribers) {
/* 452 */         inner.cancel();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int requestedMode) {
/* 458 */       if ((requestedMode & 0x4) != 0) {
/* 459 */         return 0;
/*     */       }
/* 461 */       int m = requestedMode & 0x2;
/* 462 */       this.outputFused = (m != 0);
/* 463 */       return m;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public R poll() throws Exception {
/* 470 */       Object e = this.queue.poll();
/* 471 */       if (e == null) {
/* 472 */         return null;
/*     */       }
/* 474 */       T[] a = (T[])this.queue.poll();
/* 475 */       R r = (R)ObjectHelper.requireNonNull(this.combiner.apply(a), "The combiner returned a null value");
/* 476 */       ((FlowableCombineLatest.CombineLatestInnerSubscriber)e).requestOne();
/* 477 */       return r;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 482 */       this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 487 */       return this.queue.isEmpty();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CombineLatestInnerSubscriber<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -8730235182291002949L;
/*     */     
/*     */     final FlowableCombineLatest.CombineLatestCoordinator<T, ?> parent;
/*     */     
/*     */     final int index;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     int produced;
/*     */     
/*     */     CombineLatestInnerSubscriber(FlowableCombineLatest.CombineLatestCoordinator<T, ?> parent, int index, int prefetch) {
/* 508 */       this.parent = parent;
/* 509 */       this.index = index;
/* 510 */       this.prefetch = prefetch;
/* 511 */       this.limit = prefetch - (prefetch >> 2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 516 */       SubscriptionHelper.setOnce(this, s, this.prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 521 */       this.parent.innerValue(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 526 */       this.parent.innerError(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 531 */       this.parent.innerComplete(this.index);
/*     */     }
/*     */     
/*     */     public void cancel() {
/* 535 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void requestOne() {
/* 540 */       int p = this.produced + 1;
/* 541 */       if (p == this.limit) {
/* 542 */         this.produced = 0;
/* 543 */         get().request(p);
/*     */       } else {
/* 545 */         this.produced = p;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   final class SingletonArrayFunc
/*     */     implements Function<T, R>
/*     */   {
/*     */     public R apply(T t) throws Exception {
/* 554 */       return (R)FlowableCombineLatest.this.combiner.apply(new Object[] { t });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableCombineLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */