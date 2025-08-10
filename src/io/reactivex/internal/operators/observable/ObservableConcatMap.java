/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ErrorMode;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ObservableConcatMap<T, U>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
/*     */   final int bufferSize;
/*     */   final ErrorMode delayErrors;
/*     */   
/*     */   public ObservableConcatMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends U>> mapper, int bufferSize, ErrorMode delayErrors) {
/*  38 */     super(source);
/*  39 */     this.mapper = mapper;
/*  40 */     this.delayErrors = delayErrors;
/*  41 */     this.bufferSize = Math.max(8, bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super U> observer) {
/*  47 */     if (ObservableScalarXMap.tryScalarXMapSubscribe(this.source, observer, this.mapper)) {
/*     */       return;
/*     */     }
/*     */     
/*  51 */     if (this.delayErrors == ErrorMode.IMMEDIATE) {
/*  52 */       SerializedObserver<U> serial = new SerializedObserver(observer);
/*  53 */       this.source.subscribe(new SourceObserver<T, U>((Observer<? super U>)serial, this.mapper, this.bufferSize));
/*     */     } else {
/*  55 */       this.source.subscribe(new ConcatMapDelayErrorObserver<T, U>(observer, this.mapper, this.bufferSize, (this.delayErrors == ErrorMode.END)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SourceObserver<T, U>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 8828587559905699186L;
/*     */     
/*     */     final Observer<? super U> downstream;
/*     */     
/*     */     final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
/*     */     
/*     */     final InnerObserver<U> inner;
/*     */     
/*     */     final int bufferSize;
/*     */     SimpleQueue<T> queue;
/*     */     Disposable upstream;
/*     */     volatile boolean active;
/*     */     volatile boolean disposed;
/*     */     volatile boolean done;
/*     */     int fusionMode;
/*     */     
/*     */     SourceObserver(Observer<? super U> actual, Function<? super T, ? extends ObservableSource<? extends U>> mapper, int bufferSize) {
/*  81 */       this.downstream = actual;
/*  82 */       this.mapper = mapper;
/*  83 */       this.bufferSize = bufferSize;
/*  84 */       this.inner = new InnerObserver<U>(actual, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  89 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  90 */         this.upstream = d;
/*  91 */         if (d instanceof QueueDisposable) {
/*     */           
/*  93 */           QueueDisposable<T> qd = (QueueDisposable<T>)d;
/*     */           
/*  95 */           int m = qd.requestFusion(3);
/*  96 */           if (m == 1) {
/*  97 */             this.fusionMode = m;
/*  98 */             this.queue = (SimpleQueue<T>)qd;
/*  99 */             this.done = true;
/*     */             
/* 101 */             this.downstream.onSubscribe(this);
/*     */             
/* 103 */             drain();
/*     */             
/*     */             return;
/*     */           } 
/* 107 */           if (m == 2) {
/* 108 */             this.fusionMode = m;
/* 109 */             this.queue = (SimpleQueue<T>)qd;
/*     */             
/* 111 */             this.downstream.onSubscribe(this);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 117 */         this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.bufferSize);
/*     */         
/* 119 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 125 */       if (this.done) {
/*     */         return;
/*     */       }
/* 128 */       if (this.fusionMode == 0) {
/* 129 */         this.queue.offer(t);
/*     */       }
/* 131 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 136 */       if (this.done) {
/* 137 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 140 */       this.done = true;
/* 141 */       dispose();
/* 142 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 147 */       if (this.done) {
/*     */         return;
/*     */       }
/* 150 */       this.done = true;
/* 151 */       drain();
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 155 */       this.active = false;
/* 156 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 161 */       return this.disposed;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 166 */       this.disposed = true;
/* 167 */       this.inner.dispose();
/* 168 */       this.upstream.dispose();
/*     */       
/* 170 */       if (getAndIncrement() == 0) {
/* 171 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void drain() {
/* 176 */       if (getAndIncrement() != 0)
/*     */         return; 
/*     */       do {
/*     */         T t;
/*     */         ObservableSource<? extends U> o;
/* 181 */         if (this.disposed) {
/* 182 */           this.queue.clear();
/*     */           return;
/*     */         } 
/* 185 */         if (this.active)
/*     */           continue; 
/* 187 */         boolean d = this.done;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 192 */           t = (T)this.queue.poll();
/* 193 */         } catch (Throwable ex) {
/* 194 */           Exceptions.throwIfFatal(ex);
/* 195 */           dispose();
/* 196 */           this.queue.clear();
/* 197 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 201 */         boolean empty = (t == null);
/*     */         
/* 203 */         if (d && empty) {
/* 204 */           this.disposed = true;
/* 205 */           this.downstream.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 209 */         if (empty) {
/*     */           continue;
/*     */         }
/*     */         try {
/* 213 */           o = (ObservableSource<? extends U>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null ObservableSource");
/* 214 */         } catch (Throwable ex) {
/* 215 */           Exceptions.throwIfFatal(ex);
/* 216 */           dispose();
/* 217 */           this.queue.clear();
/* 218 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 222 */         this.active = true;
/* 223 */         o.subscribe(this.inner);
/*     */ 
/*     */       
/*     */       }
/* 227 */       while (decrementAndGet() != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     static final class InnerObserver<U>
/*     */       extends AtomicReference<Disposable>
/*     */       implements Observer<U>
/*     */     {
/*     */       private static final long serialVersionUID = -7449079488798789337L;
/*     */       
/*     */       final Observer<? super U> downstream;
/*     */       final ObservableConcatMap.SourceObserver<?, ?> parent;
/*     */       
/*     */       InnerObserver(Observer<? super U> actual, ObservableConcatMap.SourceObserver<?, ?> parent) {
/* 241 */         this.downstream = actual;
/* 242 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 247 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(U t) {
/* 252 */         this.downstream.onNext(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 257 */         this.parent.dispose();
/* 258 */         this.downstream.onError(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 263 */         this.parent.innerComplete();
/*     */       }
/*     */       
/*     */       void dispose() {
/* 267 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatMapDelayErrorObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -6951100001833242599L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */     
/*     */     final int bufferSize;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final DelayErrorInnerObserver<R> observer;
/*     */     
/*     */     final boolean tillTheEnd;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile boolean active;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     int sourceMode;
/*     */ 
/*     */     
/*     */     ConcatMapDelayErrorObserver(Observer<? super R> actual, Function<? super T, ? extends ObservableSource<? extends R>> mapper, int bufferSize, boolean tillTheEnd) {
/* 305 */       this.downstream = actual;
/* 306 */       this.mapper = mapper;
/* 307 */       this.bufferSize = bufferSize;
/* 308 */       this.tillTheEnd = tillTheEnd;
/* 309 */       this.error = new AtomicThrowable();
/* 310 */       this.observer = new DelayErrorInnerObserver<R>(actual, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 315 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 316 */         this.upstream = d;
/*     */         
/* 318 */         if (d instanceof QueueDisposable) {
/*     */           
/* 320 */           QueueDisposable<T> qd = (QueueDisposable<T>)d;
/*     */           
/* 322 */           int m = qd.requestFusion(3);
/* 323 */           if (m == 1) {
/* 324 */             this.sourceMode = m;
/* 325 */             this.queue = (SimpleQueue<T>)qd;
/* 326 */             this.done = true;
/*     */             
/* 328 */             this.downstream.onSubscribe(this);
/*     */             
/* 330 */             drain();
/*     */             return;
/*     */           } 
/* 333 */           if (m == 2) {
/* 334 */             this.sourceMode = m;
/* 335 */             this.queue = (SimpleQueue<T>)qd;
/*     */             
/* 337 */             this.downstream.onSubscribe(this);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 343 */         this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.bufferSize);
/*     */         
/* 345 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/* 351 */       if (this.sourceMode == 0) {
/* 352 */         this.queue.offer(value);
/*     */       }
/* 354 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 359 */       if (this.error.addThrowable(e)) {
/* 360 */         this.done = true;
/* 361 */         drain();
/*     */       } else {
/* 363 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 369 */       this.done = true;
/* 370 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 375 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 380 */       this.cancelled = true;
/* 381 */       this.upstream.dispose();
/* 382 */       this.observer.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 387 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 391 */       Observer<? super R> actual = this.downstream;
/* 392 */       SimpleQueue<T> queue = this.queue;
/* 393 */       AtomicThrowable error = this.error;
/*     */ 
/*     */       
/*     */       while (true) {
/* 397 */         if (!this.active) {
/*     */           T v;
/* 399 */           if (this.cancelled) {
/* 400 */             queue.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 404 */           if (!this.tillTheEnd) {
/* 405 */             Throwable ex = (Throwable)error.get();
/* 406 */             if (ex != null) {
/* 407 */               queue.clear();
/* 408 */               this.cancelled = true;
/* 409 */               actual.onError(error.terminate());
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 414 */           boolean d = this.done;
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 419 */             v = (T)queue.poll();
/* 420 */           } catch (Throwable ex) {
/* 421 */             Exceptions.throwIfFatal(ex);
/* 422 */             this.cancelled = true;
/* 423 */             this.upstream.dispose();
/* 424 */             error.addThrowable(ex);
/* 425 */             actual.onError(error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 429 */           boolean empty = (v == null);
/*     */           
/* 431 */           if (d && empty) {
/* 432 */             this.cancelled = true;
/* 433 */             Throwable ex = error.terminate();
/* 434 */             if (ex != null) {
/* 435 */               actual.onError(ex);
/*     */             } else {
/* 437 */               actual.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 442 */           if (!empty) {
/*     */             ObservableSource<? extends R> o;
/*     */ 
/*     */             
/*     */             try {
/* 447 */               o = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null ObservableSource");
/* 448 */             } catch (Throwable ex) {
/* 449 */               Exceptions.throwIfFatal(ex);
/* 450 */               this.cancelled = true;
/* 451 */               this.upstream.dispose();
/* 452 */               queue.clear();
/* 453 */               error.addThrowable(ex);
/* 454 */               actual.onError(error.terminate());
/*     */               
/*     */               return;
/*     */             } 
/* 458 */             if (o instanceof Callable) {
/*     */               R w;
/*     */               
/*     */               try {
/* 462 */                 w = ((Callable)o).call();
/* 463 */               } catch (Throwable ex) {
/* 464 */                 Exceptions.throwIfFatal(ex);
/* 465 */                 error.addThrowable(ex);
/*     */                 
/*     */                 continue;
/*     */               } 
/* 469 */               if (w != null && !this.cancelled) {
/* 470 */                 actual.onNext(w);
/*     */               }
/*     */               continue;
/*     */             } 
/* 474 */             this.active = true;
/* 475 */             o.subscribe(this.observer);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 480 */         if (decrementAndGet() == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class DelayErrorInnerObserver<R>
/*     */       extends AtomicReference<Disposable>
/*     */       implements Observer<R>
/*     */     {
/*     */       private static final long serialVersionUID = 2620149119579502636L;
/*     */       final Observer<? super R> downstream;
/*     */       final ObservableConcatMap.ConcatMapDelayErrorObserver<?, R> parent;
/*     */       
/*     */       DelayErrorInnerObserver(Observer<? super R> actual, ObservableConcatMap.ConcatMapDelayErrorObserver<?, R> parent) {
/* 495 */         this.downstream = actual;
/* 496 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 501 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(R value) {
/* 506 */         this.downstream.onNext(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 511 */         ObservableConcatMap.ConcatMapDelayErrorObserver<?, R> p = this.parent;
/* 512 */         if (p.error.addThrowable(e)) {
/* 513 */           if (!p.tillTheEnd) {
/* 514 */             p.upstream.dispose();
/*     */           }
/* 516 */           p.active = false;
/* 517 */           p.drain();
/*     */         } else {
/* 519 */           RxJavaPlugins.onError(e);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 525 */         ObservableConcatMap.ConcatMapDelayErrorObserver<?, R> p = this.parent;
/* 526 */         p.active = false;
/* 527 */         p.drain();
/*     */       }
/*     */       
/*     */       void dispose() {
/* 531 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableConcatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */