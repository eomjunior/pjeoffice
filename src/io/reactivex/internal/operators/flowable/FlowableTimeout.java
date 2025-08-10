/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public final class FlowableTimeout<T, U, V>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Publisher<U> firstTimeoutIndicator;
/*     */   final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
/*     */   final Publisher<? extends T> other;
/*     */   
/*     */   public FlowableTimeout(Flowable<T> source, Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Publisher<? extends T> other) {
/*  41 */     super(source);
/*  42 */     this.firstTimeoutIndicator = firstTimeoutIndicator;
/*  43 */     this.itemTimeoutIndicator = itemTimeoutIndicator;
/*  44 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  49 */     if (this.other == null) {
/*  50 */       TimeoutSubscriber<T> parent = new TimeoutSubscriber<T>(s, this.itemTimeoutIndicator);
/*  51 */       s.onSubscribe(parent);
/*  52 */       parent.startFirstTimeout(this.firstTimeoutIndicator);
/*  53 */       this.source.subscribe(parent);
/*     */     } else {
/*  55 */       TimeoutFallbackSubscriber<T> parent = new TimeoutFallbackSubscriber<T>(s, this.itemTimeoutIndicator, this.other);
/*  56 */       s.onSubscribe((Subscription)parent);
/*  57 */       parent.startFirstTimeout(this.firstTimeoutIndicator);
/*  58 */       this.source.subscribe(parent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static interface TimeoutSelectorSupport
/*     */     extends FlowableTimeoutTimed.TimeoutSupport
/*     */   {
/*     */     void onTimeoutError(long param1Long, Throwable param1Throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription, TimeoutSelectorSupport
/*     */   {
/*     */     private static final long serialVersionUID = 3764492702657003550L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator;
/*     */     final SequentialDisposable task;
/*     */     final AtomicReference<Subscription> upstream;
/*     */     final AtomicLong requested;
/*     */     
/*     */     TimeoutSubscriber(Subscriber<? super T> actual, Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator) {
/*  82 */       this.downstream = actual;
/*  83 */       this.itemTimeoutIndicator = itemTimeoutIndicator;
/*  84 */       this.task = new SequentialDisposable();
/*  85 */       this.upstream = new AtomicReference<Subscription>();
/*  86 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  91 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       Publisher<?> itemTimeoutPublisher;
/*  96 */       long idx = get();
/*  97 */       if (idx == Long.MAX_VALUE || !compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/* 101 */       Disposable d = (Disposable)this.task.get();
/* 102 */       if (d != null) {
/* 103 */         d.dispose();
/*     */       }
/*     */       
/* 106 */       this.downstream.onNext(t);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 111 */         itemTimeoutPublisher = (Publisher)ObjectHelper.requireNonNull(this.itemTimeoutIndicator
/* 112 */             .apply(t), "The itemTimeoutIndicator returned a null Publisher.");
/*     */       }
/* 114 */       catch (Throwable ex) {
/* 115 */         Exceptions.throwIfFatal(ex);
/* 116 */         ((Subscription)this.upstream.get()).cancel();
/* 117 */         getAndSet(Long.MAX_VALUE);
/* 118 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 122 */       FlowableTimeout.TimeoutConsumer consumer = new FlowableTimeout.TimeoutConsumer(idx + 1L, this);
/* 123 */       if (this.task.replace(consumer)) {
/* 124 */         itemTimeoutPublisher.subscribe((Subscriber)consumer);
/*     */       }
/*     */     }
/*     */     
/*     */     void startFirstTimeout(Publisher<?> firstTimeoutIndicator) {
/* 129 */       if (firstTimeoutIndicator != null) {
/* 130 */         FlowableTimeout.TimeoutConsumer consumer = new FlowableTimeout.TimeoutConsumer(0L, this);
/* 131 */         if (this.task.replace(consumer)) {
/* 132 */           firstTimeoutIndicator.subscribe((Subscriber)consumer);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 139 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 140 */         this.task.dispose();
/*     */         
/* 142 */         this.downstream.onError(t);
/*     */       } else {
/* 144 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 150 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 151 */         this.task.dispose();
/*     */         
/* 153 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 159 */       if (compareAndSet(idx, Long.MAX_VALUE)) {
/* 160 */         SubscriptionHelper.cancel(this.upstream);
/*     */         
/* 162 */         this.downstream.onError(new TimeoutException());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeoutError(long idx, Throwable ex) {
/* 168 */       if (compareAndSet(idx, Long.MAX_VALUE)) {
/* 169 */         SubscriptionHelper.cancel(this.upstream);
/*     */         
/* 171 */         this.downstream.onError(ex);
/*     */       } else {
/* 173 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 179 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 184 */       SubscriptionHelper.cancel(this.upstream);
/* 185 */       this.task.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutFallbackSubscriber<T>
/*     */     extends SubscriptionArbiter
/*     */     implements FlowableSubscriber<T>, TimeoutSelectorSupport
/*     */   {
/*     */     private static final long serialVersionUID = 3764492702657003550L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator;
/*     */     
/*     */     final SequentialDisposable task;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicLong index;
/*     */     
/*     */     Publisher<? extends T> fallback;
/*     */     
/*     */     long consumed;
/*     */     
/*     */     TimeoutFallbackSubscriber(Subscriber<? super T> actual, Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator, Publisher<? extends T> fallback) {
/* 211 */       super(true);
/* 212 */       this.downstream = actual;
/* 213 */       this.itemTimeoutIndicator = itemTimeoutIndicator;
/* 214 */       this.task = new SequentialDisposable();
/* 215 */       this.upstream = new AtomicReference<Subscription>();
/* 216 */       this.fallback = fallback;
/* 217 */       this.index = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 222 */       if (SubscriptionHelper.setOnce(this.upstream, s)) {
/* 223 */         setSubscription(s);
/*     */       }
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       Publisher<?> itemTimeoutPublisher;
/* 229 */       long idx = this.index.get();
/* 230 */       if (idx == Long.MAX_VALUE || !this.index.compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/* 234 */       Disposable d = (Disposable)this.task.get();
/* 235 */       if (d != null) {
/* 236 */         d.dispose();
/*     */       }
/*     */       
/* 239 */       this.consumed++;
/*     */       
/* 241 */       this.downstream.onNext(t);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 246 */         itemTimeoutPublisher = (Publisher)ObjectHelper.requireNonNull(this.itemTimeoutIndicator
/* 247 */             .apply(t), "The itemTimeoutIndicator returned a null Publisher.");
/*     */       }
/* 249 */       catch (Throwable ex) {
/* 250 */         Exceptions.throwIfFatal(ex);
/* 251 */         ((Subscription)this.upstream.get()).cancel();
/* 252 */         this.index.getAndSet(Long.MAX_VALUE);
/* 253 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 257 */       FlowableTimeout.TimeoutConsumer consumer = new FlowableTimeout.TimeoutConsumer(idx + 1L, this);
/* 258 */       if (this.task.replace(consumer)) {
/* 259 */         itemTimeoutPublisher.subscribe((Subscriber)consumer);
/*     */       }
/*     */     }
/*     */     
/*     */     void startFirstTimeout(Publisher<?> firstTimeoutIndicator) {
/* 264 */       if (firstTimeoutIndicator != null) {
/* 265 */         FlowableTimeout.TimeoutConsumer consumer = new FlowableTimeout.TimeoutConsumer(0L, this);
/* 266 */         if (this.task.replace(consumer)) {
/* 267 */           firstTimeoutIndicator.subscribe((Subscriber)consumer);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 274 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 275 */         this.task.dispose();
/*     */         
/* 277 */         this.downstream.onError(t);
/*     */         
/* 279 */         this.task.dispose();
/*     */       } else {
/* 281 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 287 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 288 */         this.task.dispose();
/*     */         
/* 290 */         this.downstream.onComplete();
/*     */         
/* 292 */         this.task.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 298 */       if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
/* 299 */         SubscriptionHelper.cancel(this.upstream);
/*     */         
/* 301 */         Publisher<? extends T> f = this.fallback;
/* 302 */         this.fallback = null;
/*     */         
/* 304 */         long c = this.consumed;
/* 305 */         if (c != 0L) {
/* 306 */           produced(c);
/*     */         }
/*     */         
/* 309 */         f.subscribe((Subscriber)new FlowableTimeoutTimed.FallbackSubscriber<T>(this.downstream, this));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeoutError(long idx, Throwable ex) {
/* 315 */       if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
/* 316 */         SubscriptionHelper.cancel(this.upstream);
/*     */         
/* 318 */         this.downstream.onError(ex);
/*     */       } else {
/* 320 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 326 */       super.cancel();
/* 327 */       this.task.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutConsumer
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 8708641127342403073L;
/*     */     final FlowableTimeout.TimeoutSelectorSupport parent;
/*     */     final long idx;
/*     */     
/*     */     TimeoutConsumer(long idx, FlowableTimeout.TimeoutSelectorSupport parent) {
/* 341 */       this.idx = idx;
/* 342 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 347 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 352 */       Subscription upstream = get();
/* 353 */       if (upstream != SubscriptionHelper.CANCELLED) {
/* 354 */         upstream.cancel();
/* 355 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 356 */         this.parent.onTimeout(this.idx);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 362 */       if (get() != SubscriptionHelper.CANCELLED) {
/* 363 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 364 */         this.parent.onTimeoutError(this.idx, t);
/*     */       } else {
/* 366 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 372 */       if (get() != SubscriptionHelper.CANCELLED) {
/* 373 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 374 */         this.parent.onTimeout(this.idx);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 380 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 385 */       return (get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */