/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ErrorMode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableConcatMapMaybe<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final Observable<T> source;
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */   final ErrorMode errorMode;
/*     */   final int prefetch;
/*     */   
/*     */   public ObservableConcatMapMaybe(Observable<T> source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, ErrorMode errorMode, int prefetch) {
/*  51 */     this.source = source;
/*  52 */     this.mapper = mapper;
/*  53 */     this.errorMode = errorMode;
/*  54 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  59 */     if (!ScalarXMapZHelper.tryAsMaybe(this.source, this.mapper, observer)) {
/*  60 */       this.source.subscribe(new ConcatMapMaybeMainObserver<T, R>(observer, this.mapper, this.prefetch, this.errorMode));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapMaybeMainObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -9140123220065488293L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final ConcatMapMaybeObserver<R> inner;
/*     */     
/*     */     final SimplePlainQueue<T> queue;
/*     */     
/*     */     final ErrorMode errorMode;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     R item;
/*     */     
/*     */     volatile int state;
/*     */     
/*     */     static final int STATE_INACTIVE = 0;
/*     */     
/*     */     static final int STATE_ACTIVE = 1;
/*     */     
/*     */     static final int STATE_RESULT_VALUE = 2;
/*     */ 
/*     */     
/*     */     ConcatMapMaybeMainObserver(Observer<? super R> downstream, Function<? super T, ? extends MaybeSource<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/* 102 */       this.downstream = downstream;
/* 103 */       this.mapper = mapper;
/* 104 */       this.errorMode = errorMode;
/* 105 */       this.errors = new AtomicThrowable();
/* 106 */       this.inner = new ConcatMapMaybeObserver<R>(this);
/* 107 */       this.queue = (SimplePlainQueue<T>)new SpscLinkedArrayQueue(prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 112 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 113 */         this.upstream = d;
/* 114 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 120 */       this.queue.offer(t);
/* 121 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 126 */       if (this.errors.addThrowable(t)) {
/* 127 */         if (this.errorMode == ErrorMode.IMMEDIATE) {
/* 128 */           this.inner.dispose();
/*     */         }
/* 130 */         this.done = true;
/* 131 */         drain();
/*     */       } else {
/* 133 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 139 */       this.done = true;
/* 140 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 145 */       this.cancelled = true;
/* 146 */       this.upstream.dispose();
/* 147 */       this.inner.dispose();
/* 148 */       if (getAndIncrement() == 0) {
/* 149 */         this.queue.clear();
/* 150 */         this.item = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 156 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void innerSuccess(R item) {
/* 160 */       this.item = item;
/* 161 */       this.state = 2;
/* 162 */       drain();
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 166 */       this.state = 0;
/* 167 */       drain();
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex) {
/* 171 */       if (this.errors.addThrowable(ex)) {
/* 172 */         if (this.errorMode != ErrorMode.END) {
/* 173 */           this.upstream.dispose();
/*     */         }
/* 175 */         this.state = 0;
/* 176 */         drain();
/*     */       } else {
/* 178 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 183 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 187 */       int missed = 1;
/* 188 */       Observer<? super R> downstream = this.downstream;
/* 189 */       ErrorMode errorMode = this.errorMode;
/* 190 */       SimplePlainQueue<T> queue = this.queue;
/* 191 */       AtomicThrowable errors = this.errors;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 196 */         if (this.cancelled) {
/* 197 */           queue.clear();
/* 198 */           this.item = null;
/*     */         }
/*     */         else {
/*     */           
/* 202 */           int s = this.state;
/*     */           
/* 204 */           if (errors.get() != null && (
/* 205 */             errorMode == ErrorMode.IMMEDIATE || (errorMode == ErrorMode.BOUNDARY && s == 0))) {
/*     */             
/* 207 */             queue.clear();
/* 208 */             this.item = null;
/* 209 */             Throwable ex = errors.terminate();
/* 210 */             downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 215 */           if (s == 0) {
/* 216 */             boolean d = this.done;
/* 217 */             T v = (T)queue.poll();
/* 218 */             boolean empty = (v == null);
/*     */             
/* 220 */             if (d && empty) {
/* 221 */               Throwable ex = errors.terminate();
/* 222 */               if (ex == null) {
/* 223 */                 downstream.onComplete();
/*     */               } else {
/* 225 */                 downstream.onError(ex);
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/* 230 */             if (!empty) {
/*     */               MaybeSource<? extends R> ms;
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 237 */                 ms = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null MaybeSource");
/* 238 */               } catch (Throwable ex) {
/* 239 */                 Exceptions.throwIfFatal(ex);
/* 240 */                 this.upstream.dispose();
/* 241 */                 queue.clear();
/* 242 */                 errors.addThrowable(ex);
/* 243 */                 ex = errors.terminate();
/* 244 */                 downstream.onError(ex);
/*     */                 
/*     */                 return;
/*     */               } 
/* 248 */               this.state = 1;
/* 249 */               ms.subscribe(this.inner);
/*     */             } 
/* 251 */           } else if (s == 2) {
/* 252 */             R w = this.item;
/* 253 */             this.item = null;
/* 254 */             downstream.onNext(w);
/*     */             
/* 256 */             this.state = 0;
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */         
/* 262 */         missed = addAndGet(-missed);
/* 263 */         if (missed == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     static final class ConcatMapMaybeObserver<R>
/*     */       extends AtomicReference<Disposable>
/*     */       implements MaybeObserver<R>
/*     */     {
/*     */       private static final long serialVersionUID = -3051469169682093892L;
/*     */       final ObservableConcatMapMaybe.ConcatMapMaybeMainObserver<?, R> parent;
/*     */       
/*     */       ConcatMapMaybeObserver(ObservableConcatMapMaybe.ConcatMapMaybeMainObserver<?, R> parent) {
/* 278 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 283 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R t) {
/* 288 */         this.parent.innerSuccess(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 293 */         this.parent.innerError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 298 */         this.parent.innerComplete();
/*     */       }
/*     */       
/*     */       void dispose() {
/* 302 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/ObservableConcatMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */