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
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
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
/*     */ public final class ObservableFlatMap<T, U>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
/*     */   final boolean delayErrors;
/*     */   final int maxConcurrency;
/*     */   final int bufferSize;
/*     */   
/*     */   public ObservableFlatMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends U>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  41 */     super(source);
/*  42 */     this.mapper = mapper;
/*  43 */     this.delayErrors = delayErrors;
/*  44 */     this.maxConcurrency = maxConcurrency;
/*  45 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super U> t) {
/*  51 */     if (ObservableScalarXMap.tryScalarXMapSubscribe(this.source, t, this.mapper)) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     this.source.subscribe(new MergeObserver<T, U>(t, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeObserver<T, U>
/*     */     extends AtomicInteger
/*     */     implements Disposable, Observer<T>
/*     */   {
/*     */     private static final long serialVersionUID = -2117620485640801370L;
/*     */     
/*     */     final Observer<? super U> downstream;
/*     */     final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
/*     */     final boolean delayErrors;
/*     */     final int maxConcurrency;
/*     */     final int bufferSize;
/*     */     volatile SimplePlainQueue<U> queue;
/*     */     volatile boolean done;
/*  72 */     final AtomicThrowable errors = new AtomicThrowable();
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     final AtomicReference<ObservableFlatMap.InnerObserver<?, ?>[]> observers;
/*     */     
/*  78 */     static final ObservableFlatMap.InnerObserver<?, ?>[] EMPTY = (ObservableFlatMap.InnerObserver<?, ?>[])new ObservableFlatMap.InnerObserver[0];
/*     */     
/*  80 */     static final ObservableFlatMap.InnerObserver<?, ?>[] CANCELLED = (ObservableFlatMap.InnerObserver<?, ?>[])new ObservableFlatMap.InnerObserver[0];
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     long uniqueId;
/*     */     
/*     */     long lastId;
/*     */     
/*     */     int lastIndex;
/*     */     
/*     */     Queue<ObservableSource<? extends U>> sources;
/*     */     int wip;
/*     */     
/*     */     MergeObserver(Observer<? super U> actual, Function<? super T, ? extends ObservableSource<? extends U>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  94 */       this.downstream = actual;
/*  95 */       this.mapper = mapper;
/*  96 */       this.delayErrors = delayErrors;
/*  97 */       this.maxConcurrency = maxConcurrency;
/*  98 */       this.bufferSize = bufferSize;
/*  99 */       if (maxConcurrency != Integer.MAX_VALUE) {
/* 100 */         this.sources = new ArrayDeque<ObservableSource<? extends U>>(maxConcurrency);
/*     */       }
/* 102 */       this.observers = (AtomicReference)new AtomicReference<ObservableFlatMap.InnerObserver<?, ?>>(EMPTY);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 107 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 108 */         this.upstream = d;
/* 109 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       ObservableSource<? extends U> p;
/* 116 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 121 */         p = (ObservableSource<? extends U>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null ObservableSource");
/* 122 */       } catch (Throwable e) {
/* 123 */         Exceptions.throwIfFatal(e);
/* 124 */         this.upstream.dispose();
/* 125 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 129 */       if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 130 */         synchronized (this) {
/* 131 */           if (this.wip == this.maxConcurrency) {
/* 132 */             this.sources.offer(p);
/*     */             return;
/*     */           } 
/* 135 */           this.wip++;
/*     */         } 
/*     */       }
/*     */       
/* 139 */       subscribeInner(p);
/*     */     }
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
/*     */     void subscribeInner(ObservableSource<? extends U> p) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: instanceof java/util/concurrent/Callable
/*     */       //   4: ifeq -> 88
/*     */       //   7: aload_0
/*     */       //   8: aload_1
/*     */       //   9: checkcast java/util/concurrent/Callable
/*     */       //   12: invokevirtual tryEmitScalar : (Ljava/util/concurrent/Callable;)Z
/*     */       //   15: ifeq -> 126
/*     */       //   18: aload_0
/*     */       //   19: getfield maxConcurrency : I
/*     */       //   22: ldc 2147483647
/*     */       //   24: if_icmpeq -> 126
/*     */       //   27: iconst_0
/*     */       //   28: istore_2
/*     */       //   29: aload_0
/*     */       //   30: dup
/*     */       //   31: astore_3
/*     */       //   32: monitorenter
/*     */       //   33: aload_0
/*     */       //   34: getfield sources : Ljava/util/Queue;
/*     */       //   37: invokeinterface poll : ()Ljava/lang/Object;
/*     */       //   42: checkcast io/reactivex/ObservableSource
/*     */       //   45: astore_1
/*     */       //   46: aload_1
/*     */       //   47: ifnonnull -> 62
/*     */       //   50: aload_0
/*     */       //   51: dup
/*     */       //   52: getfield wip : I
/*     */       //   55: iconst_1
/*     */       //   56: isub
/*     */       //   57: putfield wip : I
/*     */       //   60: iconst_1
/*     */       //   61: istore_2
/*     */       //   62: aload_3
/*     */       //   63: monitorexit
/*     */       //   64: goto -> 74
/*     */       //   67: astore #4
/*     */       //   69: aload_3
/*     */       //   70: monitorexit
/*     */       //   71: aload #4
/*     */       //   73: athrow
/*     */       //   74: iload_2
/*     */       //   75: ifeq -> 85
/*     */       //   78: aload_0
/*     */       //   79: invokevirtual drain : ()V
/*     */       //   82: goto -> 126
/*     */       //   85: goto -> 0
/*     */       //   88: new io/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver
/*     */       //   91: dup
/*     */       //   92: aload_0
/*     */       //   93: aload_0
/*     */       //   94: dup
/*     */       //   95: getfield uniqueId : J
/*     */       //   98: dup2_x1
/*     */       //   99: lconst_1
/*     */       //   100: ladd
/*     */       //   101: putfield uniqueId : J
/*     */       //   104: invokespecial <init> : (Lio/reactivex/internal/operators/observable/ObservableFlatMap$MergeObserver;J)V
/*     */       //   107: astore_2
/*     */       //   108: aload_0
/*     */       //   109: aload_2
/*     */       //   110: invokevirtual addInner : (Lio/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver;)Z
/*     */       //   113: ifeq -> 126
/*     */       //   116: aload_1
/*     */       //   117: aload_2
/*     */       //   118: invokeinterface subscribe : (Lio/reactivex/Observer;)V
/*     */       //   123: goto -> 126
/*     */       //   126: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       //   #146	-> 7
/*     */       //   #147	-> 27
/*     */       //   #148	-> 29
/*     */       //   #149	-> 33
/*     */       //   #150	-> 46
/*     */       //   #151	-> 50
/*     */       //   #152	-> 60
/*     */       //   #154	-> 62
/*     */       //   #155	-> 74
/*     */       //   #156	-> 78
/*     */       //   #157	-> 82
/*     */       //   #159	-> 85
/*     */       //   #163	-> 88
/*     */       //   #164	-> 108
/*     */       //   #165	-> 116
/*     */       //   #170	-> 126
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   29	56	2	empty	Z
/*     */       //   108	18	2	inner	Lio/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver;
/*     */       //   0	127	0	this	Lio/reactivex/internal/operators/observable/ObservableFlatMap$MergeObserver;
/*     */       //   0	127	1	p	Lio/reactivex/ObservableSource;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   108	18	2	inner	Lio/reactivex/internal/operators/observable/ObservableFlatMap$InnerObserver<TT;TU;>;
/*     */       //   0	127	0	this	Lio/reactivex/internal/operators/observable/ObservableFlatMap$MergeObserver<TT;TU;>;
/*     */       //   0	127	1	p	Lio/reactivex/ObservableSource<+TU;>;
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   33	64	67	finally
/*     */       //   67	71	67	finally
/*     */     }
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
/*     */     boolean addInner(ObservableFlatMap.InnerObserver<T, U> inner) {
/*     */       while (true) {
/* 174 */         ObservableFlatMap.InnerObserver[] arrayOfInnerObserver1 = (ObservableFlatMap.InnerObserver[])this.observers.get();
/* 175 */         if (arrayOfInnerObserver1 == CANCELLED) {
/* 176 */           inner.dispose();
/* 177 */           return false;
/*     */         } 
/* 179 */         int n = arrayOfInnerObserver1.length;
/* 180 */         ObservableFlatMap.InnerObserver[] arrayOfInnerObserver2 = new ObservableFlatMap.InnerObserver[n + 1];
/* 181 */         System.arraycopy(arrayOfInnerObserver1, 0, arrayOfInnerObserver2, 0, n);
/* 182 */         arrayOfInnerObserver2[n] = inner;
/* 183 */         if (this.observers.compareAndSet(arrayOfInnerObserver1, arrayOfInnerObserver2))
/* 184 */           return true; 
/*     */       } 
/*     */     }
/*     */     void removeInner(ObservableFlatMap.InnerObserver<T, U> inner) {
/*     */       ObservableFlatMap.InnerObserver[] arrayOfInnerObserver1;
/*     */       ObservableFlatMap.InnerObserver[] arrayOfInnerObserver2;
/*     */       do {
/* 191 */         arrayOfInnerObserver1 = (ObservableFlatMap.InnerObserver[])this.observers.get();
/* 192 */         int n = arrayOfInnerObserver1.length;
/* 193 */         if (n == 0) {
/*     */           return;
/*     */         }
/* 196 */         int j = -1;
/* 197 */         for (int i = 0; i < n; i++) {
/* 198 */           if (arrayOfInnerObserver1[i] == inner) {
/* 199 */             j = i;
/*     */             break;
/*     */           } 
/*     */         } 
/* 203 */         if (j < 0) {
/*     */           return;
/*     */         }
/*     */         
/* 207 */         if (n == 1) {
/* 208 */           ObservableFlatMap.InnerObserver<?, ?>[] b = EMPTY;
/*     */         } else {
/* 210 */           arrayOfInnerObserver2 = new ObservableFlatMap.InnerObserver[n - 1];
/* 211 */           System.arraycopy(arrayOfInnerObserver1, 0, arrayOfInnerObserver2, 0, j);
/* 212 */           System.arraycopy(arrayOfInnerObserver1, j + 1, arrayOfInnerObserver2, j, n - j - 1);
/*     */         } 
/* 214 */       } while (!this.observers.compareAndSet(arrayOfInnerObserver1, arrayOfInnerObserver2));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean tryEmitScalar(Callable<? extends U> value) {
/*     */       U u;
/*     */       try {
/* 223 */         u = value.call();
/* 224 */       } catch (Throwable ex) {
/* 225 */         Exceptions.throwIfFatal(ex);
/* 226 */         this.errors.addThrowable(ex);
/* 227 */         drain();
/* 228 */         return true;
/*     */       } 
/*     */       
/* 231 */       if (u == null) {
/* 232 */         return true;
/*     */       }
/*     */       
/* 235 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 236 */         this.downstream.onNext(u);
/* 237 */         if (decrementAndGet() == 0)
/* 238 */           return true; 
/*     */       } else {
/*     */         SpscArrayQueue spscArrayQueue;
/* 241 */         SimplePlainQueue<U> q = this.queue;
/* 242 */         if (q == null) {
/* 243 */           if (this.maxConcurrency == Integer.MAX_VALUE) {
/* 244 */             SpscLinkedArrayQueue spscLinkedArrayQueue = new SpscLinkedArrayQueue(this.bufferSize);
/*     */           } else {
/* 246 */             spscArrayQueue = new SpscArrayQueue(this.maxConcurrency);
/*     */           } 
/* 248 */           this.queue = (SimplePlainQueue<U>)spscArrayQueue;
/*     */         } 
/*     */         
/* 251 */         if (!spscArrayQueue.offer(u)) {
/* 252 */           onError(new IllegalStateException("Scalar queue full?!"));
/* 253 */           return true;
/*     */         } 
/* 255 */         if (getAndIncrement() != 0) {
/* 256 */           return false;
/*     */         }
/*     */       } 
/* 259 */       drainLoop();
/* 260 */       return true;
/*     */     }
/*     */     
/*     */     void tryEmit(U value, ObservableFlatMap.InnerObserver<T, U> inner) {
/* 264 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 265 */         this.downstream.onNext(value);
/* 266 */         if (decrementAndGet() == 0)
/*     */           return; 
/*     */       } else {
/*     */         SpscLinkedArrayQueue spscLinkedArrayQueue;
/* 270 */         SimpleQueue<U> q = inner.queue;
/* 271 */         if (q == null) {
/* 272 */           spscLinkedArrayQueue = new SpscLinkedArrayQueue(this.bufferSize);
/* 273 */           inner.queue = (SimpleQueue<U>)spscLinkedArrayQueue;
/*     */         } 
/* 275 */         spscLinkedArrayQueue.offer(value);
/* 276 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 280 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 285 */       if (this.done) {
/* 286 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 289 */       if (this.errors.addThrowable(t)) {
/* 290 */         this.done = true;
/* 291 */         drain();
/*     */       } else {
/* 293 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 299 */       if (this.done) {
/*     */         return;
/*     */       }
/* 302 */       this.done = true;
/* 303 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 308 */       if (!this.cancelled) {
/* 309 */         this.cancelled = true;
/* 310 */         if (disposeAll()) {
/* 311 */           Throwable ex = this.errors.terminate();
/* 312 */           if (ex != null && ex != ExceptionHelper.TERMINATED) {
/* 313 */             RxJavaPlugins.onError(ex);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 321 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void drain() {
/* 325 */       if (getAndIncrement() == 0) {
/* 326 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 331 */       Observer<? super U> child = this.downstream;
/* 332 */       int missed = 1;
/*     */       while (true) {
/* 334 */         if (checkTerminate()) {
/*     */           return;
/*     */         }
/* 337 */         int innerCompleted = 0;
/* 338 */         SimplePlainQueue<U> svq = this.queue;
/*     */         
/* 340 */         if (svq != null) {
/*     */           while (true) {
/* 342 */             if (checkTerminate()) {
/*     */               return;
/*     */             }
/*     */             
/* 346 */             U o = (U)svq.poll();
/*     */             
/* 348 */             if (o == null) {
/*     */               break;
/*     */             }
/*     */             
/* 352 */             child.onNext(o);
/* 353 */             innerCompleted++;
/*     */           } 
/*     */         }
/*     */         
/* 357 */         if (innerCompleted != 0) {
/* 358 */           if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 359 */             subscribeMore(innerCompleted);
/* 360 */             innerCompleted = 0;
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/* 365 */         boolean d = this.done;
/* 366 */         svq = this.queue;
/* 367 */         ObservableFlatMap.InnerObserver[] arrayOfInnerObserver = (ObservableFlatMap.InnerObserver[])this.observers.get();
/* 368 */         int n = arrayOfInnerObserver.length;
/*     */         
/* 370 */         int nSources = 0;
/* 371 */         if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 372 */           synchronized (this) {
/* 373 */             nSources = this.sources.size();
/*     */           } 
/*     */         }
/*     */         
/* 377 */         if (d && (svq == null || svq.isEmpty()) && n == 0 && nSources == 0) {
/* 378 */           Throwable ex = this.errors.terminate();
/* 379 */           if (ex != ExceptionHelper.TERMINATED) {
/* 380 */             if (ex == null) {
/* 381 */               child.onComplete();
/*     */             } else {
/* 383 */               child.onError(ex);
/*     */             } 
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 389 */         if (n != 0) {
/* 390 */           long startId = this.lastId;
/* 391 */           int index = this.lastIndex;
/*     */           
/* 393 */           if (n <= index || (arrayOfInnerObserver[index]).id != startId) {
/* 394 */             if (n <= index) {
/* 395 */               index = 0;
/*     */             }
/* 397 */             int k = index;
/* 398 */             for (int m = 0; m < n && 
/* 399 */               (arrayOfInnerObserver[k]).id != startId; m++) {
/*     */ 
/*     */               
/* 402 */               k++;
/* 403 */               if (k == n) {
/* 404 */                 k = 0;
/*     */               }
/*     */             } 
/* 407 */             index = k;
/* 408 */             this.lastIndex = k;
/* 409 */             this.lastId = (arrayOfInnerObserver[k]).id;
/*     */           } 
/*     */           
/* 412 */           int j = index;
/*     */           int i;
/* 414 */           label99: for (i = 0; i < n; i++) {
/* 415 */             if (checkTerminate()) {
/*     */               return;
/*     */             }
/*     */ 
/*     */             
/* 420 */             ObservableFlatMap.InnerObserver<T, U> is = arrayOfInnerObserver[j];
/* 421 */             SimpleQueue<U> q = is.queue;
/* 422 */             if (q != null) {
/*     */               while (true) {
/*     */                 U o;
/*     */                 try {
/* 426 */                   o = (U)q.poll();
/* 427 */                 } catch (Throwable ex) {
/* 428 */                   Exceptions.throwIfFatal(ex);
/* 429 */                   is.dispose();
/* 430 */                   this.errors.addThrowable(ex);
/* 431 */                   if (checkTerminate()) {
/*     */                     return;
/*     */                   }
/* 434 */                   removeInner(is);
/* 435 */                   innerCompleted++;
/* 436 */                   j++;
/* 437 */                   if (j == n) {
/* 438 */                     j = 0;
/*     */                   }
/*     */                   break;
/*     */                 } 
/* 442 */                 if (o == null) {
/*     */                   continue label99;
/*     */                 }
/*     */                 
/* 446 */                 child.onNext(o);
/*     */                 
/* 448 */                 if (checkTerminate()) {
/*     */                   return;
/*     */                 }
/*     */               } 
/*     */             } else {
/*     */               
/* 454 */               boolean innerDone = is.done;
/* 455 */               SimpleQueue<U> innerQueue = is.queue;
/* 456 */               if (innerDone && (innerQueue == null || innerQueue.isEmpty())) {
/* 457 */                 removeInner(is);
/* 458 */                 if (checkTerminate()) {
/*     */                   return;
/*     */                 }
/* 461 */                 innerCompleted++;
/*     */               } 
/*     */               
/* 464 */               j++;
/* 465 */               if (j == n)
/* 466 */                 j = 0; 
/*     */             } 
/*     */           } 
/* 469 */           this.lastIndex = j;
/* 470 */           this.lastId = (arrayOfInnerObserver[j]).id;
/*     */         } 
/*     */         
/* 473 */         if (innerCompleted != 0) {
/* 474 */           if (this.maxConcurrency != Integer.MAX_VALUE) {
/* 475 */             subscribeMore(innerCompleted);
/* 476 */             innerCompleted = 0;
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/* 481 */         missed = addAndGet(-missed);
/* 482 */         if (missed == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void subscribeMore(int innerCompleted) {
/* 489 */       while (innerCompleted-- != 0) {
/*     */         ObservableSource<? extends U> p;
/* 491 */         synchronized (this) {
/* 492 */           p = this.sources.poll();
/* 493 */           if (p == null) {
/* 494 */             this.wip--;
/*     */             continue;
/*     */           } 
/*     */         } 
/* 498 */         subscribeInner(p);
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean checkTerminate() {
/* 503 */       if (this.cancelled) {
/* 504 */         return true;
/*     */       }
/* 506 */       Throwable e = (Throwable)this.errors.get();
/* 507 */       if (!this.delayErrors && e != null) {
/* 508 */         disposeAll();
/* 509 */         e = this.errors.terminate();
/* 510 */         if (e != ExceptionHelper.TERMINATED) {
/* 511 */           this.downstream.onError(e);
/*     */         }
/* 513 */         return true;
/*     */       } 
/* 515 */       return false;
/*     */     }
/*     */     
/*     */     boolean disposeAll() {
/* 519 */       this.upstream.dispose();
/* 520 */       ObservableFlatMap.InnerObserver[] arrayOfInnerObserver = (ObservableFlatMap.InnerObserver[])this.observers.get();
/* 521 */       if (arrayOfInnerObserver != CANCELLED) {
/* 522 */         arrayOfInnerObserver = (ObservableFlatMap.InnerObserver[])this.observers.getAndSet(CANCELLED);
/* 523 */         if (arrayOfInnerObserver != CANCELLED) {
/* 524 */           for (ObservableFlatMap.InnerObserver<?, ?> inner : arrayOfInnerObserver) {
/* 525 */             inner.dispose();
/*     */           }
/* 527 */           return true;
/*     */         } 
/*     */       } 
/* 530 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class InnerObserver<T, U>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<U>
/*     */   {
/*     */     private static final long serialVersionUID = -4606175640614850599L;
/*     */     final long id;
/*     */     final ObservableFlatMap.MergeObserver<T, U> parent;
/*     */     volatile boolean done;
/*     */     volatile SimpleQueue<U> queue;
/*     */     int fusionMode;
/*     */     
/*     */     InnerObserver(ObservableFlatMap.MergeObserver<T, U> parent, long id) {
/* 547 */       this.id = id;
/* 548 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 553 */       if (DisposableHelper.setOnce(this, d) && 
/* 554 */         d instanceof QueueDisposable) {
/*     */         
/* 556 */         QueueDisposable<U> qd = (QueueDisposable<U>)d;
/*     */         
/* 558 */         int m = qd.requestFusion(7);
/* 559 */         if (m == 1) {
/* 560 */           this.fusionMode = m;
/* 561 */           this.queue = (SimpleQueue<U>)qd;
/* 562 */           this.done = true;
/* 563 */           this.parent.drain();
/*     */           return;
/*     */         } 
/* 566 */         if (m == 2) {
/* 567 */           this.fusionMode = m;
/* 568 */           this.queue = (SimpleQueue<U>)qd;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(U t) {
/* 576 */       if (this.fusionMode == 0) {
/* 577 */         this.parent.tryEmit(t, this);
/*     */       } else {
/* 579 */         this.parent.drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 585 */       if (this.parent.errors.addThrowable(t)) {
/* 586 */         if (!this.parent.delayErrors) {
/* 587 */           this.parent.disposeAll();
/*     */         }
/* 589 */         this.done = true;
/* 590 */         this.parent.drain();
/*     */       } else {
/* 592 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 598 */       this.done = true;
/* 599 */       this.parent.drain();
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 603 */       DisposableHelper.dispose(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */