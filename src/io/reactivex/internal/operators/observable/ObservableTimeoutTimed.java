/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class ObservableTimeoutTimed<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final ObservableSource<? extends T> other;
/*     */   
/*     */   public ObservableTimeoutTimed(Observable<T> source, long timeout, TimeUnit unit, Scheduler scheduler, ObservableSource<? extends T> other) {
/*  34 */     super((ObservableSource<T>)source);
/*  35 */     this.timeout = timeout;
/*  36 */     this.unit = unit;
/*  37 */     this.scheduler = scheduler;
/*  38 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  43 */     if (this.other == null) {
/*  44 */       TimeoutObserver<T> parent = new TimeoutObserver<T>(observer, this.timeout, this.unit, this.scheduler.createWorker());
/*  45 */       observer.onSubscribe(parent);
/*  46 */       parent.startTimeout(0L);
/*  47 */       this.source.subscribe(parent);
/*     */     } else {
/*  49 */       TimeoutFallbackObserver<T> parent = new TimeoutFallbackObserver<T>(observer, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
/*  50 */       observer.onSubscribe(parent);
/*  51 */       parent.startTimeout(0L);
/*  52 */       this.source.subscribe(parent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutObserver<T>
/*     */     extends AtomicLong
/*     */     implements Observer<T>, Disposable, TimeoutSupport
/*     */   {
/*     */     private static final long serialVersionUID = 3764492702657003550L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     final SequentialDisposable task;
/*     */     final AtomicReference<Disposable> upstream;
/*     */     
/*     */     TimeoutObserver(Observer<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker) {
/*  74 */       this.downstream = actual;
/*  75 */       this.timeout = timeout;
/*  76 */       this.unit = unit;
/*  77 */       this.worker = worker;
/*  78 */       this.task = new SequentialDisposable();
/*  79 */       this.upstream = new AtomicReference<Disposable>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  84 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  89 */       long idx = get();
/*  90 */       if (idx == Long.MAX_VALUE || !compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/*  94 */       ((Disposable)this.task.get()).dispose();
/*     */       
/*  96 */       this.downstream.onNext(t);
/*     */       
/*  98 */       startTimeout(idx + 1L);
/*     */     }
/*     */     
/*     */     void startTimeout(long nextIndex) {
/* 102 */       this.task.replace(this.worker.schedule(new ObservableTimeoutTimed.TimeoutTask(nextIndex, this), this.timeout, this.unit));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 107 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 108 */         this.task.dispose();
/*     */         
/* 110 */         this.downstream.onError(t);
/*     */         
/* 112 */         this.worker.dispose();
/*     */       } else {
/* 114 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 120 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 121 */         this.task.dispose();
/*     */         
/* 123 */         this.downstream.onComplete();
/*     */         
/* 125 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 131 */       if (compareAndSet(idx, Long.MAX_VALUE)) {
/* 132 */         DisposableHelper.dispose(this.upstream);
/*     */         
/* 134 */         this.downstream.onError(new TimeoutException(ExceptionHelper.timeoutMessage(this.timeout, this.unit)));
/*     */         
/* 136 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 142 */       DisposableHelper.dispose(this.upstream);
/* 143 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 148 */       return DisposableHelper.isDisposed(this.upstream.get());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TimeoutTask
/*     */     implements Runnable
/*     */   {
/*     */     final ObservableTimeoutTimed.TimeoutSupport parent;
/*     */     final long idx;
/*     */     
/*     */     TimeoutTask(long idx, ObservableTimeoutTimed.TimeoutSupport parent) {
/* 159 */       this.idx = idx;
/* 160 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 165 */       this.parent.onTimeout(this.idx);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutFallbackObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<T>, Disposable, TimeoutSupport
/*     */   {
/*     */     private static final long serialVersionUID = 3764492702657003550L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final SequentialDisposable task;
/*     */     
/*     */     final AtomicLong index;
/*     */     
/*     */     final AtomicReference<Disposable> upstream;
/*     */     ObservableSource<? extends T> fallback;
/*     */     
/*     */     TimeoutFallbackObserver(Observer<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker, ObservableSource<? extends T> fallback) {
/* 192 */       this.downstream = actual;
/* 193 */       this.timeout = timeout;
/* 194 */       this.unit = unit;
/* 195 */       this.worker = worker;
/* 196 */       this.fallback = fallback;
/* 197 */       this.task = new SequentialDisposable();
/* 198 */       this.index = new AtomicLong();
/* 199 */       this.upstream = new AtomicReference<Disposable>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 204 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 209 */       long idx = this.index.get();
/* 210 */       if (idx == Long.MAX_VALUE || !this.index.compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/* 214 */       ((Disposable)this.task.get()).dispose();
/*     */       
/* 216 */       this.downstream.onNext(t);
/*     */       
/* 218 */       startTimeout(idx + 1L);
/*     */     }
/*     */     
/*     */     void startTimeout(long nextIndex) {
/* 222 */       this.task.replace(this.worker.schedule(new ObservableTimeoutTimed.TimeoutTask(nextIndex, this), this.timeout, this.unit));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 227 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 228 */         this.task.dispose();
/*     */         
/* 230 */         this.downstream.onError(t);
/*     */         
/* 232 */         this.worker.dispose();
/*     */       } else {
/* 234 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 240 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 241 */         this.task.dispose();
/*     */         
/* 243 */         this.downstream.onComplete();
/*     */         
/* 245 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 251 */       if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
/* 252 */         DisposableHelper.dispose(this.upstream);
/*     */         
/* 254 */         ObservableSource<? extends T> f = this.fallback;
/* 255 */         this.fallback = null;
/*     */         
/* 257 */         f.subscribe(new ObservableTimeoutTimed.FallbackObserver<T>(this.downstream, this));
/*     */         
/* 259 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 265 */       DisposableHelper.dispose(this.upstream);
/* 266 */       DisposableHelper.dispose(this);
/* 267 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 272 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FallbackObserver<T>
/*     */     implements Observer<T>
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final AtomicReference<Disposable> arbiter;
/*     */     
/*     */     FallbackObserver(Observer<? super T> actual, AtomicReference<Disposable> arbiter) {
/* 283 */       this.downstream = actual;
/* 284 */       this.arbiter = arbiter;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 289 */       DisposableHelper.replace(this.arbiter, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 294 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 299 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 304 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static interface TimeoutSupport {
/*     */     void onTimeout(long param1Long);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTimeoutTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */