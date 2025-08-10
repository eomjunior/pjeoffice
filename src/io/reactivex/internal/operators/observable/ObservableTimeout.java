/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class ObservableTimeout<T, U, V>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final ObservableSource<U> firstTimeoutIndicator;
/*     */   final Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator;
/*     */   final ObservableSource<? extends T> other;
/*     */   
/*     */   public ObservableTimeout(Observable<T> source, ObservableSource<U> firstTimeoutIndicator, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator, ObservableSource<? extends T> other) {
/*  38 */     super((ObservableSource<T>)source);
/*  39 */     this.firstTimeoutIndicator = firstTimeoutIndicator;
/*  40 */     this.itemTimeoutIndicator = itemTimeoutIndicator;
/*  41 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  46 */     if (this.other == null) {
/*  47 */       TimeoutObserver<T> parent = new TimeoutObserver<T>(observer, this.itemTimeoutIndicator);
/*  48 */       observer.onSubscribe(parent);
/*  49 */       parent.startFirstTimeout(this.firstTimeoutIndicator);
/*  50 */       this.source.subscribe(parent);
/*     */     } else {
/*  52 */       TimeoutFallbackObserver<T> parent = new TimeoutFallbackObserver<T>(observer, this.itemTimeoutIndicator, this.other);
/*  53 */       observer.onSubscribe(parent);
/*  54 */       parent.startFirstTimeout(this.firstTimeoutIndicator);
/*  55 */       this.source.subscribe(parent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static interface TimeoutSelectorSupport
/*     */     extends ObservableTimeoutTimed.TimeoutSupport
/*     */   {
/*     */     void onTimeoutError(long param1Long, Throwable param1Throwable);
/*     */   }
/*     */   
/*     */   static final class TimeoutObserver<T>
/*     */     extends AtomicLong
/*     */     implements Observer<T>, Disposable, TimeoutSelectorSupport
/*     */   {
/*     */     private static final long serialVersionUID = 3764492702657003550L;
/*     */     final Observer<? super T> downstream;
/*     */     final Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator;
/*     */     final SequentialDisposable task;
/*     */     final AtomicReference<Disposable> upstream;
/*     */     
/*     */     TimeoutObserver(Observer<? super T> actual, Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator) {
/*  77 */       this.downstream = actual;
/*  78 */       this.itemTimeoutIndicator = itemTimeoutIndicator;
/*  79 */       this.task = new SequentialDisposable();
/*  80 */       this.upstream = new AtomicReference<Disposable>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  85 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       ObservableSource<?> itemTimeoutObservableSource;
/*  90 */       long idx = get();
/*  91 */       if (idx == Long.MAX_VALUE || !compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/*  95 */       Disposable d = (Disposable)this.task.get();
/*  96 */       if (d != null) {
/*  97 */         d.dispose();
/*     */       }
/*     */       
/* 100 */       this.downstream.onNext(t);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 105 */         itemTimeoutObservableSource = (ObservableSource)ObjectHelper.requireNonNull(this.itemTimeoutIndicator
/* 106 */             .apply(t), "The itemTimeoutIndicator returned a null ObservableSource.");
/*     */       }
/* 108 */       catch (Throwable ex) {
/* 109 */         Exceptions.throwIfFatal(ex);
/* 110 */         ((Disposable)this.upstream.get()).dispose();
/* 111 */         getAndSet(Long.MAX_VALUE);
/* 112 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 116 */       ObservableTimeout.TimeoutConsumer consumer = new ObservableTimeout.TimeoutConsumer(idx + 1L, this);
/* 117 */       if (this.task.replace(consumer)) {
/* 118 */         itemTimeoutObservableSource.subscribe(consumer);
/*     */       }
/*     */     }
/*     */     
/*     */     void startFirstTimeout(ObservableSource<?> firstTimeoutIndicator) {
/* 123 */       if (firstTimeoutIndicator != null) {
/* 124 */         ObservableTimeout.TimeoutConsumer consumer = new ObservableTimeout.TimeoutConsumer(0L, this);
/* 125 */         if (this.task.replace(consumer)) {
/* 126 */           firstTimeoutIndicator.subscribe(consumer);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 133 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 134 */         this.task.dispose();
/*     */         
/* 136 */         this.downstream.onError(t);
/*     */       } else {
/* 138 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 144 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 145 */         this.task.dispose();
/*     */         
/* 147 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 153 */       if (compareAndSet(idx, Long.MAX_VALUE)) {
/* 154 */         DisposableHelper.dispose(this.upstream);
/*     */         
/* 156 */         this.downstream.onError(new TimeoutException());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeoutError(long idx, Throwable ex) {
/* 162 */       if (compareAndSet(idx, Long.MAX_VALUE)) {
/* 163 */         DisposableHelper.dispose(this.upstream);
/*     */         
/* 165 */         this.downstream.onError(ex);
/*     */       } else {
/* 167 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 173 */       DisposableHelper.dispose(this.upstream);
/* 174 */       this.task.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 179 */       return DisposableHelper.isDisposed(this.upstream.get());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutFallbackObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<T>, Disposable, TimeoutSelectorSupport
/*     */   {
/*     */     private static final long serialVersionUID = -7508389464265974549L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator;
/*     */     
/*     */     final SequentialDisposable task;
/*     */     
/*     */     final AtomicLong index;
/*     */     
/*     */     final AtomicReference<Disposable> upstream;
/*     */     
/*     */     ObservableSource<? extends T> fallback;
/*     */ 
/*     */     
/*     */     TimeoutFallbackObserver(Observer<? super T> actual, Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator, ObservableSource<? extends T> fallback) {
/* 204 */       this.downstream = actual;
/* 205 */       this.itemTimeoutIndicator = itemTimeoutIndicator;
/* 206 */       this.task = new SequentialDisposable();
/* 207 */       this.fallback = fallback;
/* 208 */       this.index = new AtomicLong();
/* 209 */       this.upstream = new AtomicReference<Disposable>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 214 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       ObservableSource<?> itemTimeoutObservableSource;
/* 219 */       long idx = this.index.get();
/* 220 */       if (idx == Long.MAX_VALUE || !this.index.compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/* 224 */       Disposable d = (Disposable)this.task.get();
/* 225 */       if (d != null) {
/* 226 */         d.dispose();
/*     */       }
/*     */       
/* 229 */       this.downstream.onNext(t);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 234 */         itemTimeoutObservableSource = (ObservableSource)ObjectHelper.requireNonNull(this.itemTimeoutIndicator
/* 235 */             .apply(t), "The itemTimeoutIndicator returned a null ObservableSource.");
/*     */       }
/* 237 */       catch (Throwable ex) {
/* 238 */         Exceptions.throwIfFatal(ex);
/* 239 */         ((Disposable)this.upstream.get()).dispose();
/* 240 */         this.index.getAndSet(Long.MAX_VALUE);
/* 241 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 245 */       ObservableTimeout.TimeoutConsumer consumer = new ObservableTimeout.TimeoutConsumer(idx + 1L, this);
/* 246 */       if (this.task.replace(consumer)) {
/* 247 */         itemTimeoutObservableSource.subscribe(consumer);
/*     */       }
/*     */     }
/*     */     
/*     */     void startFirstTimeout(ObservableSource<?> firstTimeoutIndicator) {
/* 252 */       if (firstTimeoutIndicator != null) {
/* 253 */         ObservableTimeout.TimeoutConsumer consumer = new ObservableTimeout.TimeoutConsumer(0L, this);
/* 254 */         if (this.task.replace(consumer)) {
/* 255 */           firstTimeoutIndicator.subscribe(consumer);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 262 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 263 */         this.task.dispose();
/*     */         
/* 265 */         this.downstream.onError(t);
/*     */         
/* 267 */         this.task.dispose();
/*     */       } else {
/* 269 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 275 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 276 */         this.task.dispose();
/*     */         
/* 278 */         this.downstream.onComplete();
/*     */         
/* 280 */         this.task.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 286 */       if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
/* 287 */         DisposableHelper.dispose(this.upstream);
/*     */         
/* 289 */         ObservableSource<? extends T> f = this.fallback;
/* 290 */         this.fallback = null;
/*     */         
/* 292 */         f.subscribe(new ObservableTimeoutTimed.FallbackObserver<T>(this.downstream, this));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeoutError(long idx, Throwable ex) {
/* 298 */       if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
/* 299 */         DisposableHelper.dispose(this);
/*     */         
/* 301 */         this.downstream.onError(ex);
/*     */       } else {
/* 303 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 309 */       DisposableHelper.dispose(this.upstream);
/* 310 */       DisposableHelper.dispose(this);
/* 311 */       this.task.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 316 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutConsumer
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 8708641127342403073L;
/*     */     final ObservableTimeout.TimeoutSelectorSupport parent;
/*     */     final long idx;
/*     */     
/*     */     TimeoutConsumer(long idx, ObservableTimeout.TimeoutSelectorSupport parent) {
/* 330 */       this.idx = idx;
/* 331 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 336 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 341 */       Disposable upstream = get();
/* 342 */       if (upstream != DisposableHelper.DISPOSED) {
/* 343 */         upstream.dispose();
/* 344 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 345 */         this.parent.onTimeout(this.idx);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 351 */       if (get() != DisposableHelper.DISPOSED) {
/* 352 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 353 */         this.parent.onTimeoutError(this.idx, t);
/*     */       } else {
/* 355 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 361 */       if (get() != DisposableHelper.DISPOSED) {
/* 362 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 363 */         this.parent.onTimeout(this.idx);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 369 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 374 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */