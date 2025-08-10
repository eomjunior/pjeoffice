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
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableSwitchMap<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */   final int bufferSize;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ObservableSwitchMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> mapper, int bufferSize, boolean delayErrors) {
/*  38 */     super(source);
/*  39 */     this.mapper = mapper;
/*  40 */     this.bufferSize = bufferSize;
/*  41 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super R> t) {
/*  47 */     if (ObservableScalarXMap.tryScalarXMapSubscribe(this.source, t, this.mapper)) {
/*     */       return;
/*     */     }
/*     */     
/*  51 */     this.source.subscribe(new SwitchMapObserver<T, R>(t, this.mapper, this.bufferSize, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -3491074160481096299L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends ObservableSource<? extends R>> mapper;
/*     */     
/*     */     final int bufferSize;
/*     */     final boolean delayErrors;
/*     */     final AtomicThrowable errors;
/*     */     volatile boolean done;
/*     */     volatile boolean cancelled;
/*     */     Disposable upstream;
/*  71 */     final AtomicReference<ObservableSwitchMap.SwitchMapInnerObserver<T, R>> active = new AtomicReference<ObservableSwitchMap.SwitchMapInnerObserver<T, R>>();
/*     */ 
/*     */ 
/*     */     
/*  75 */     static final ObservableSwitchMap.SwitchMapInnerObserver<Object, Object> CANCELLED = new ObservableSwitchMap.SwitchMapInnerObserver<Object, Object>(null, -1L, 1); static {
/*  76 */       CANCELLED.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     volatile long unique;
/*     */ 
/*     */     
/*     */     SwitchMapObserver(Observer<? super R> actual, Function<? super T, ? extends ObservableSource<? extends R>> mapper, int bufferSize, boolean delayErrors) {
/*  84 */       this.downstream = actual;
/*  85 */       this.mapper = mapper;
/*  86 */       this.bufferSize = bufferSize;
/*  87 */       this.delayErrors = delayErrors;
/*  88 */       this.errors = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  93 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  94 */         this.upstream = d;
/*  95 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       ObservableSource<? extends R> p;
/* 101 */       long c = this.unique + 1L;
/* 102 */       this.unique = c;
/*     */       
/* 104 */       ObservableSwitchMap.SwitchMapInnerObserver<T, R> inner = this.active.get();
/* 105 */       if (inner != null) {
/* 106 */         inner.cancel();
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 111 */         p = (ObservableSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The ObservableSource returned is null");
/* 112 */       } catch (Throwable e) {
/* 113 */         Exceptions.throwIfFatal(e);
/* 114 */         this.upstream.dispose();
/* 115 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 119 */       ObservableSwitchMap.SwitchMapInnerObserver<T, R> nextInner = new ObservableSwitchMap.SwitchMapInnerObserver<T, R>(this, c, this.bufferSize);
/*     */       
/*     */       while (true) {
/* 122 */         inner = this.active.get();
/* 123 */         if (inner == CANCELLED) {
/*     */           break;
/*     */         }
/* 126 */         if (this.active.compareAndSet(inner, nextInner)) {
/* 127 */           p.subscribe(nextInner);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 135 */       if (!this.done && this.errors.addThrowable(t)) {
/* 136 */         if (!this.delayErrors) {
/* 137 */           disposeInner();
/*     */         }
/* 139 */         this.done = true;
/* 140 */         drain();
/*     */       } else {
/* 142 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 148 */       if (!this.done) {
/* 149 */         this.done = true;
/* 150 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 156 */       if (!this.cancelled) {
/* 157 */         this.cancelled = true;
/* 158 */         this.upstream.dispose();
/* 159 */         disposeInner();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 165 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeInner() {
/* 170 */       ObservableSwitchMap.SwitchMapInnerObserver<T, R> a = this.active.get();
/* 171 */       if (a != CANCELLED) {
/* 172 */         a = (ObservableSwitchMap.SwitchMapInnerObserver<T, R>)this.active.getAndSet(CANCELLED);
/* 173 */         if (a != CANCELLED && a != null) {
/* 174 */           a.cancel();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 180 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 184 */       Observer<? super R> a = this.downstream;
/* 185 */       AtomicReference<ObservableSwitchMap.SwitchMapInnerObserver<T, R>> active = this.active;
/* 186 */       boolean delayErrors = this.delayErrors;
/*     */       
/* 188 */       int missing = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 192 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 196 */         if (this.done) {
/* 197 */           boolean empty = (active.get() == null);
/* 198 */           if (delayErrors) {
/* 199 */             if (empty) {
/* 200 */               Throwable ex = (Throwable)this.errors.get();
/* 201 */               if (ex != null) {
/* 202 */                 a.onError(ex);
/*     */               } else {
/* 204 */                 a.onComplete();
/*     */               } 
/*     */               return;
/*     */             } 
/*     */           } else {
/* 209 */             Throwable ex = (Throwable)this.errors.get();
/* 210 */             if (ex != null) {
/* 211 */               a.onError(this.errors.terminate());
/*     */               return;
/*     */             } 
/* 214 */             if (empty) {
/* 215 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 221 */         ObservableSwitchMap.SwitchMapInnerObserver<T, R> inner = active.get();
/*     */         
/* 223 */         if (inner != null) {
/* 224 */           SimpleQueue<R> q = inner.queue;
/*     */           
/* 226 */           if (q != null) {
/* 227 */             if (inner.done) {
/* 228 */               boolean empty = q.isEmpty();
/* 229 */               if (delayErrors) {
/* 230 */                 if (empty) {
/* 231 */                   active.compareAndSet(inner, null);
/*     */                   continue;
/*     */                 } 
/*     */               } else {
/* 235 */                 Throwable ex = (Throwable)this.errors.get();
/* 236 */                 if (ex != null) {
/* 237 */                   a.onError(this.errors.terminate());
/*     */                   return;
/*     */                 } 
/* 240 */                 if (empty) {
/* 241 */                   active.compareAndSet(inner, null);
/*     */                   
/*     */                   continue;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 247 */             boolean retry = false;
/*     */             while (true) {
/*     */               R v;
/* 250 */               if (this.cancelled) {
/*     */                 return;
/*     */               }
/* 253 */               if (inner != active.get()) {
/* 254 */                 retry = true;
/*     */                 
/*     */                 break;
/*     */               } 
/* 258 */               if (!delayErrors) {
/* 259 */                 Throwable ex = (Throwable)this.errors.get();
/* 260 */                 if (ex != null) {
/* 261 */                   a.onError(this.errors.terminate());
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               } 
/* 266 */               boolean d = inner.done;
/*     */ 
/*     */               
/*     */               try {
/* 270 */                 v = (R)q.poll();
/* 271 */               } catch (Throwable ex) {
/* 272 */                 Exceptions.throwIfFatal(ex);
/* 273 */                 this.errors.addThrowable(ex);
/* 274 */                 active.compareAndSet(inner, null);
/* 275 */                 if (!delayErrors) {
/* 276 */                   disposeInner();
/* 277 */                   this.upstream.dispose();
/* 278 */                   this.done = true;
/*     */                 } else {
/* 280 */                   inner.cancel();
/*     */                 } 
/* 282 */                 v = null;
/* 283 */                 retry = true;
/*     */               } 
/* 285 */               boolean empty = (v == null);
/*     */               
/* 287 */               if (d && empty) {
/* 288 */                 active.compareAndSet(inner, null);
/* 289 */                 retry = true;
/*     */                 
/*     */                 break;
/*     */               } 
/* 293 */               if (empty) {
/*     */                 break;
/*     */               }
/*     */               
/* 297 */               a.onNext(v);
/*     */             } 
/*     */             
/* 300 */             if (retry) {
/*     */               continue;
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 306 */         missing = addAndGet(-missing);
/* 307 */         if (missing == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerError(ObservableSwitchMap.SwitchMapInnerObserver<T, R> inner, Throwable ex) {
/* 314 */       if (inner.index == this.unique && this.errors.addThrowable(ex)) {
/* 315 */         if (!this.delayErrors) {
/* 316 */           this.upstream.dispose();
/* 317 */           this.done = true;
/*     */         } 
/* 319 */         inner.done = true;
/* 320 */         drain();
/*     */       } else {
/* 322 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapInnerObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<R>
/*     */   {
/*     */     private static final long serialVersionUID = 3837284832786408377L;
/*     */     final ObservableSwitchMap.SwitchMapObserver<T, R> parent;
/*     */     final long index;
/*     */     final int bufferSize;
/*     */     volatile SimpleQueue<R> queue;
/*     */     volatile boolean done;
/*     */     
/*     */     SwitchMapInnerObserver(ObservableSwitchMap.SwitchMapObserver<T, R> parent, long index, int bufferSize) {
/* 340 */       this.parent = parent;
/* 341 */       this.index = index;
/* 342 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 347 */       if (DisposableHelper.setOnce(this, d)) {
/* 348 */         if (d instanceof QueueDisposable) {
/*     */           
/* 350 */           QueueDisposable<R> qd = (QueueDisposable<R>)d;
/*     */           
/* 352 */           int m = qd.requestFusion(7);
/* 353 */           if (m == 1) {
/* 354 */             this.queue = (SimpleQueue<R>)qd;
/* 355 */             this.done = true;
/* 356 */             this.parent.drain();
/*     */             return;
/*     */           } 
/* 359 */           if (m == 2) {
/* 360 */             this.queue = (SimpleQueue<R>)qd;
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 365 */         this.queue = (SimpleQueue<R>)new SpscLinkedArrayQueue(this.bufferSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R t) {
/* 371 */       if (this.index == this.parent.unique) {
/* 372 */         if (t != null) {
/* 373 */           this.queue.offer(t);
/*     */         }
/* 375 */         this.parent.drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 381 */       this.parent.innerError(this, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 386 */       if (this.index == this.parent.unique) {
/* 387 */         this.done = true;
/* 388 */         this.parent.drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void cancel() {
/* 393 */       DisposableHelper.dispose(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSwitchMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */