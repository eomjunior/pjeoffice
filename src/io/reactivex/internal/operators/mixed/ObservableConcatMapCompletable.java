/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Observable;
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
/*     */ import io.reactivex.internal.util.ExceptionHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableConcatMapCompletable<T>
/*     */   extends Completable
/*     */ {
/*     */   final Observable<T> source;
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final ErrorMode errorMode;
/*     */   final int prefetch;
/*     */   
/*     */   public ObservableConcatMapCompletable(Observable<T> source, Function<? super T, ? extends CompletableSource> mapper, ErrorMode errorMode, int prefetch) {
/*  50 */     this.source = source;
/*  51 */     this.mapper = mapper;
/*  52 */     this.errorMode = errorMode;
/*  53 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  58 */     if (!ScalarXMapZHelper.tryAsCompletable(this.source, this.mapper, observer)) {
/*  59 */       this.source.subscribe(new ConcatMapCompletableObserver<T>(observer, this.mapper, this.errorMode, this.prefetch));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatMapCompletableObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 3610901111000061034L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     
/*     */     final ErrorMode errorMode;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final ConcatMapInnerObserver inner;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile boolean active;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean disposed;
/*     */ 
/*     */     
/*     */     ConcatMapCompletableObserver(CompletableObserver downstream, Function<? super T, ? extends CompletableSource> mapper, ErrorMode errorMode, int prefetch) {
/*  94 */       this.downstream = downstream;
/*  95 */       this.mapper = mapper;
/*  96 */       this.errorMode = errorMode;
/*  97 */       this.prefetch = prefetch;
/*  98 */       this.errors = new AtomicThrowable();
/*  99 */       this.inner = new ConcatMapInnerObserver(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 104 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 105 */         this.upstream = d;
/* 106 */         if (d instanceof QueueDisposable) {
/*     */           
/* 108 */           QueueDisposable<T> qd = (QueueDisposable<T>)d;
/*     */           
/* 110 */           int m = qd.requestFusion(3);
/* 111 */           if (m == 1) {
/* 112 */             this.queue = (SimpleQueue<T>)qd;
/* 113 */             this.done = true;
/* 114 */             this.downstream.onSubscribe(this);
/* 115 */             drain();
/*     */             return;
/*     */           } 
/* 118 */           if (m == 2) {
/* 119 */             this.queue = (SimpleQueue<T>)qd;
/* 120 */             this.downstream.onSubscribe(this);
/*     */             return;
/*     */           } 
/*     */         } 
/* 124 */         this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.prefetch);
/* 125 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 131 */       if (t != null) {
/* 132 */         this.queue.offer(t);
/*     */       }
/* 134 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 139 */       if (this.errors.addThrowable(t)) {
/* 140 */         if (this.errorMode == ErrorMode.IMMEDIATE) {
/* 141 */           this.disposed = true;
/* 142 */           this.inner.dispose();
/* 143 */           t = this.errors.terminate();
/* 144 */           if (t != ExceptionHelper.TERMINATED) {
/* 145 */             this.downstream.onError(t);
/*     */           }
/* 147 */           if (getAndIncrement() == 0) {
/* 148 */             this.queue.clear();
/*     */           }
/*     */         } else {
/* 151 */           this.done = true;
/* 152 */           drain();
/*     */         } 
/*     */       } else {
/* 155 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 161 */       this.done = true;
/* 162 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 167 */       this.disposed = true;
/* 168 */       this.upstream.dispose();
/* 169 */       this.inner.dispose();
/* 170 */       if (getAndIncrement() == 0) {
/* 171 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 177 */       return this.disposed;
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex) {
/* 181 */       if (this.errors.addThrowable(ex)) {
/* 182 */         if (this.errorMode == ErrorMode.IMMEDIATE) {
/* 183 */           this.disposed = true;
/* 184 */           this.upstream.dispose();
/* 185 */           ex = this.errors.terminate();
/* 186 */           if (ex != ExceptionHelper.TERMINATED) {
/* 187 */             this.downstream.onError(ex);
/*     */           }
/* 189 */           if (getAndIncrement() == 0) {
/* 190 */             this.queue.clear();
/*     */           }
/*     */         } else {
/* 193 */           this.active = false;
/* 194 */           drain();
/*     */         } 
/*     */       } else {
/* 197 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 202 */       this.active = false;
/* 203 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 207 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 211 */       AtomicThrowable errors = this.errors;
/* 212 */       ErrorMode errorMode = this.errorMode;
/*     */       
/*     */       do {
/* 215 */         if (this.disposed) {
/* 216 */           this.queue.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 220 */         if (this.active)
/*     */           continue; 
/* 222 */         if (errorMode == ErrorMode.BOUNDARY && 
/* 223 */           errors.get() != null) {
/* 224 */           this.disposed = true;
/* 225 */           this.queue.clear();
/* 226 */           Throwable ex = errors.terminate();
/* 227 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 232 */         boolean d = this.done;
/* 233 */         boolean empty = true;
/* 234 */         CompletableSource cs = null;
/*     */         try {
/* 236 */           T v = (T)this.queue.poll();
/* 237 */           if (v != null) {
/* 238 */             cs = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null CompletableSource");
/* 239 */             empty = false;
/*     */           } 
/* 241 */         } catch (Throwable ex) {
/* 242 */           Exceptions.throwIfFatal(ex);
/* 243 */           this.disposed = true;
/* 244 */           this.queue.clear();
/* 245 */           this.upstream.dispose();
/* 246 */           errors.addThrowable(ex);
/* 247 */           ex = errors.terminate();
/* 248 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 252 */         if (d && empty) {
/* 253 */           this.disposed = true;
/* 254 */           Throwable ex = errors.terminate();
/* 255 */           if (ex != null) {
/* 256 */             this.downstream.onError(ex);
/*     */           } else {
/* 258 */             this.downstream.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 263 */         if (empty)
/* 264 */           continue;  this.active = true;
/* 265 */         cs.subscribe(this.inner);
/*     */       
/*     */       }
/* 268 */       while (decrementAndGet() != 0);
/*     */     }
/*     */     
/*     */     static final class ConcatMapInnerObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements CompletableObserver
/*     */     {
/*     */       private static final long serialVersionUID = 5638352172918776687L;
/*     */       final ObservableConcatMapCompletable.ConcatMapCompletableObserver<?> parent;
/*     */       
/*     */       ConcatMapInnerObserver(ObservableConcatMapCompletable.ConcatMapCompletableObserver<?> parent) {
/* 279 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 284 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 289 */         this.parent.innerError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 294 */         this.parent.innerComplete();
/*     */       }
/*     */       
/*     */       void dispose() {
/* 298 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/ObservableConcatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */