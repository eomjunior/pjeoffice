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
/*     */ import io.reactivex.internal.observers.InnerQueuedObserver;
/*     */ import io.reactivex.internal.observers.InnerQueuedObserverSupport;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ErrorMode;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ObservableConcatMapEager<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */   final ErrorMode errorMode;
/*     */   final int maxConcurrency;
/*     */   final int prefetch;
/*     */   
/*     */   public ObservableConcatMapEager(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> mapper, ErrorMode errorMode, int maxConcurrency, int prefetch) {
/*  45 */     super(source);
/*  46 */     this.mapper = mapper;
/*  47 */     this.errorMode = errorMode;
/*  48 */     this.maxConcurrency = maxConcurrency;
/*  49 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  54 */     this.source.subscribe(new ConcatMapEagerMainObserver<T, R>(observer, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatMapEagerMainObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable, InnerQueuedObserverSupport<R>
/*     */   {
/*     */     private static final long serialVersionUID = 8080567949447303262L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */     
/*     */     final int maxConcurrency;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final ErrorMode errorMode;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final ArrayDeque<InnerQueuedObserver<R>> observers;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     int sourceMode;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     InnerQueuedObserver<R> current;
/*     */     
/*     */     int activeCount;
/*     */ 
/*     */     
/*     */     ConcatMapEagerMainObserver(Observer<? super R> actual, Function<? super T, ? extends ObservableSource<? extends R>> mapper, int maxConcurrency, int prefetch, ErrorMode errorMode) {
/*  94 */       this.downstream = actual;
/*  95 */       this.mapper = mapper;
/*  96 */       this.maxConcurrency = maxConcurrency;
/*  97 */       this.prefetch = prefetch;
/*  98 */       this.errorMode = errorMode;
/*  99 */       this.error = new AtomicThrowable();
/* 100 */       this.observers = new ArrayDeque<InnerQueuedObserver<R>>();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 106 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 107 */         this.upstream = d;
/*     */         
/* 109 */         if (d instanceof QueueDisposable) {
/* 110 */           QueueDisposable<T> qd = (QueueDisposable<T>)d;
/*     */           
/* 112 */           int m = qd.requestFusion(3);
/* 113 */           if (m == 1) {
/* 114 */             this.sourceMode = m;
/* 115 */             this.queue = (SimpleQueue<T>)qd;
/* 116 */             this.done = true;
/*     */             
/* 118 */             this.downstream.onSubscribe(this);
/*     */             
/* 120 */             drain();
/*     */             return;
/*     */           } 
/* 123 */           if (m == 2) {
/* 124 */             this.sourceMode = m;
/* 125 */             this.queue = (SimpleQueue<T>)qd;
/*     */             
/* 127 */             this.downstream.onSubscribe(this);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 133 */         this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.prefetch);
/*     */         
/* 135 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/* 141 */       if (this.sourceMode == 0) {
/* 142 */         this.queue.offer(value);
/*     */       }
/* 144 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 149 */       if (this.error.addThrowable(e)) {
/* 150 */         this.done = true;
/* 151 */         drain();
/*     */       } else {
/* 153 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 159 */       this.done = true;
/* 160 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 165 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 168 */       this.cancelled = true;
/* 169 */       this.upstream.dispose();
/*     */       
/* 171 */       drainAndDispose();
/*     */     }
/*     */     
/*     */     void drainAndDispose() {
/* 175 */       if (getAndIncrement() == 0) {
/*     */         do {
/* 177 */           this.queue.clear();
/* 178 */           disposeAll();
/* 179 */         } while (decrementAndGet() != 0);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 185 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void disposeAll() {
/* 189 */       InnerQueuedObserver<R> inner = this.current;
/*     */       
/* 191 */       if (inner != null) {
/* 192 */         inner.dispose();
/*     */       }
/*     */ 
/*     */       
/*     */       while (true) {
/* 197 */         inner = this.observers.poll();
/*     */         
/* 199 */         if (inner == null) {
/*     */           return;
/*     */         }
/*     */         
/* 203 */         inner.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerNext(InnerQueuedObserver<R> inner, R value) {
/* 209 */       inner.queue().offer(value);
/* 210 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerError(InnerQueuedObserver<R> inner, Throwable e) {
/* 215 */       if (this.error.addThrowable(e)) {
/* 216 */         if (this.errorMode == ErrorMode.IMMEDIATE) {
/* 217 */           this.upstream.dispose();
/*     */         }
/* 219 */         inner.setDone();
/* 220 */         drain();
/*     */       } else {
/* 222 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void innerComplete(InnerQueuedObserver<R> inner) {
/* 228 */       inner.setDone();
/* 229 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void drain() {
/* 234 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 238 */       int missed = 1;
/*     */       
/* 240 */       SimpleQueue<T> q = this.queue;
/* 241 */       ArrayDeque<InnerQueuedObserver<R>> observers = this.observers;
/* 242 */       Observer<? super R> a = this.downstream;
/* 243 */       ErrorMode errorMode = this.errorMode;
/*     */ 
/*     */ 
/*     */       
/*     */       label92: do {
/* 248 */         int ac = this.activeCount;
/*     */         
/* 250 */         while (ac != this.maxConcurrency) {
/* 251 */           ObservableSource<? extends R> source; if (this.cancelled) {
/* 252 */             q.clear();
/* 253 */             disposeAll();
/*     */             
/*     */             return;
/*     */           } 
/* 257 */           if (errorMode == ErrorMode.IMMEDIATE) {
/* 258 */             Throwable ex = (Throwable)this.error.get();
/* 259 */             if (ex != null) {
/* 260 */               q.clear();
/* 261 */               disposeAll();
/*     */               
/* 263 */               a.onError(this.error.terminate());
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/*     */           try {
/* 272 */             T v = (T)q.poll();
/*     */             
/* 274 */             if (v == null) {
/*     */               break;
/*     */             }
/*     */             
/* 278 */             source = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null ObservableSource");
/* 279 */           } catch (Throwable ex) {
/* 280 */             Exceptions.throwIfFatal(ex);
/* 281 */             this.upstream.dispose();
/* 282 */             q.clear();
/* 283 */             disposeAll();
/* 284 */             this.error.addThrowable(ex);
/* 285 */             a.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 289 */           InnerQueuedObserver<R> inner = new InnerQueuedObserver(this, this.prefetch);
/*     */           
/* 291 */           observers.offer(inner);
/*     */           
/* 293 */           source.subscribe((Observer)inner);
/*     */           
/* 295 */           ac++;
/*     */         } 
/*     */         
/* 298 */         this.activeCount = ac;
/*     */         
/* 300 */         if (this.cancelled) {
/* 301 */           q.clear();
/* 302 */           disposeAll();
/*     */           
/*     */           return;
/*     */         } 
/* 306 */         if (errorMode == ErrorMode.IMMEDIATE) {
/* 307 */           Throwable ex = (Throwable)this.error.get();
/* 308 */           if (ex != null) {
/* 309 */             q.clear();
/* 310 */             disposeAll();
/*     */             
/* 312 */             a.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 317 */         InnerQueuedObserver<R> innerQueuedObserver = this.current;
/*     */         
/* 319 */         if (innerQueuedObserver == null) {
/* 320 */           if (errorMode == ErrorMode.BOUNDARY) {
/* 321 */             Throwable ex = (Throwable)this.error.get();
/* 322 */             if (ex != null) {
/* 323 */               q.clear();
/* 324 */               disposeAll();
/*     */               
/* 326 */               a.onError(this.error.terminate());
/*     */               return;
/*     */             } 
/*     */           } 
/* 330 */           boolean d = this.done;
/*     */           
/* 332 */           innerQueuedObserver = observers.poll();
/*     */           
/* 334 */           boolean empty = (innerQueuedObserver == null);
/*     */           
/* 336 */           if (d && empty) {
/* 337 */             Throwable ex = (Throwable)this.error.get();
/* 338 */             if (ex != null) {
/* 339 */               q.clear();
/* 340 */               disposeAll();
/*     */               
/* 342 */               a.onError(this.error.terminate());
/*     */             } else {
/* 344 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 349 */           if (!empty) {
/* 350 */             this.current = innerQueuedObserver;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 355 */         if (innerQueuedObserver != null) {
/* 356 */           SimpleQueue<R> aq = innerQueuedObserver.queue();
/*     */           while (true) {
/*     */             R w;
/* 359 */             if (this.cancelled) {
/* 360 */               q.clear();
/* 361 */               disposeAll();
/*     */               
/*     */               return;
/*     */             } 
/* 365 */             boolean d = innerQueuedObserver.isDone();
/*     */             
/* 367 */             if (errorMode == ErrorMode.IMMEDIATE) {
/* 368 */               Throwable ex = (Throwable)this.error.get();
/* 369 */               if (ex != null) {
/* 370 */                 q.clear();
/* 371 */                 disposeAll();
/*     */                 
/* 373 */                 a.onError(this.error.terminate());
/*     */ 
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */             
/*     */             try {
/* 381 */               w = (R)aq.poll();
/* 382 */             } catch (Throwable ex) {
/* 383 */               Exceptions.throwIfFatal(ex);
/* 384 */               this.error.addThrowable(ex);
/*     */               
/* 386 */               this.current = null;
/* 387 */               this.activeCount--;
/*     */               
/*     */               continue label92;
/*     */             } 
/* 391 */             boolean empty = (w == null);
/*     */             
/* 393 */             if (d && empty) {
/* 394 */               this.current = null;
/* 395 */               this.activeCount--;
/*     */               
/*     */               continue label92;
/*     */             } 
/* 399 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 403 */             a.onNext(w);
/*     */           } 
/*     */         } 
/*     */         
/* 407 */         missed = addAndGet(-missed);
/* 408 */       } while (missed != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableConcatMapEager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */