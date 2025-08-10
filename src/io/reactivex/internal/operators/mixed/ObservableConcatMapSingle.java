/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ public final class ObservableConcatMapSingle<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final Observable<T> source;
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   final ErrorMode errorMode;
/*     */   final int prefetch;
/*     */   
/*     */   public ObservableConcatMapSingle(Observable<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper, ErrorMode errorMode, int prefetch) {
/*  51 */     this.source = source;
/*  52 */     this.mapper = mapper;
/*  53 */     this.errorMode = errorMode;
/*  54 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  59 */     if (!ScalarXMapZHelper.tryAsSingle(this.source, this.mapper, observer)) {
/*  60 */       this.source.subscribe(new ConcatMapSingleMainObserver<T, R>(observer, this.mapper, this.prefetch, this.errorMode));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapSingleMainObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -9140123220065488293L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final ConcatMapSingleObserver<R> inner;
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
/*     */     ConcatMapSingleMainObserver(Observer<? super R> downstream, Function<? super T, ? extends SingleSource<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/* 102 */       this.downstream = downstream;
/* 103 */       this.mapper = mapper;
/* 104 */       this.errorMode = errorMode;
/* 105 */       this.errors = new AtomicThrowable();
/* 106 */       this.inner = new ConcatMapSingleObserver<R>(this);
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
/*     */     void innerError(Throwable ex) {
/* 166 */       if (this.errors.addThrowable(ex)) {
/* 167 */         if (this.errorMode != ErrorMode.END) {
/* 168 */           this.upstream.dispose();
/*     */         }
/* 170 */         this.state = 0;
/* 171 */         drain();
/*     */       } else {
/* 173 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 178 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 182 */       int missed = 1;
/* 183 */       Observer<? super R> downstream = this.downstream;
/* 184 */       ErrorMode errorMode = this.errorMode;
/* 185 */       SimplePlainQueue<T> queue = this.queue;
/* 186 */       AtomicThrowable errors = this.errors;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 191 */         if (this.cancelled) {
/* 192 */           queue.clear();
/* 193 */           this.item = null;
/*     */         }
/*     */         else {
/*     */           
/* 197 */           int s = this.state;
/*     */           
/* 199 */           if (errors.get() != null && (
/* 200 */             errorMode == ErrorMode.IMMEDIATE || (errorMode == ErrorMode.BOUNDARY && s == 0))) {
/*     */             
/* 202 */             queue.clear();
/* 203 */             this.item = null;
/* 204 */             Throwable ex = errors.terminate();
/* 205 */             downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 210 */           if (s == 0) {
/* 211 */             boolean d = this.done;
/* 212 */             T v = (T)queue.poll();
/* 213 */             boolean empty = (v == null);
/*     */             
/* 215 */             if (d && empty) {
/* 216 */               Throwable ex = errors.terminate();
/* 217 */               if (ex == null) {
/* 218 */                 downstream.onComplete();
/*     */               } else {
/* 220 */                 downstream.onError(ex);
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/* 225 */             if (!empty) {
/*     */               SingleSource<? extends R> ss;
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 232 */                 ss = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null SingleSource");
/* 233 */               } catch (Throwable ex) {
/* 234 */                 Exceptions.throwIfFatal(ex);
/* 235 */                 this.upstream.dispose();
/* 236 */                 queue.clear();
/* 237 */                 errors.addThrowable(ex);
/* 238 */                 ex = errors.terminate();
/* 239 */                 downstream.onError(ex);
/*     */                 
/*     */                 return;
/*     */               } 
/* 243 */               this.state = 1;
/* 244 */               ss.subscribe(this.inner);
/*     */             } 
/* 246 */           } else if (s == 2) {
/* 247 */             R w = this.item;
/* 248 */             this.item = null;
/* 249 */             downstream.onNext(w);
/*     */             
/* 251 */             this.state = 0;
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */         
/* 257 */         missed = addAndGet(-missed);
/* 258 */         if (missed == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     static final class ConcatMapSingleObserver<R>
/*     */       extends AtomicReference<Disposable>
/*     */       implements SingleObserver<R>
/*     */     {
/*     */       private static final long serialVersionUID = -3051469169682093892L;
/*     */       final ObservableConcatMapSingle.ConcatMapSingleMainObserver<?, R> parent;
/*     */       
/*     */       ConcatMapSingleObserver(ObservableConcatMapSingle.ConcatMapSingleMainObserver<?, R> parent) {
/* 273 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 278 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R t) {
/* 283 */         this.parent.innerSuccess(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 288 */         this.parent.innerError(e);
/*     */       }
/*     */       
/*     */       void dispose() {
/* 292 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/ObservableConcatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */