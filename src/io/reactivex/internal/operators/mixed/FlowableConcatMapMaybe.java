/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ErrorMode;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableConcatMapMaybe<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */   final ErrorMode errorMode;
/*     */   final int prefetch;
/*     */   
/*     */   public FlowableConcatMapMaybe(Flowable<T> source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, ErrorMode errorMode, int prefetch) {
/*  54 */     this.source = source;
/*  55 */     this.mapper = mapper;
/*  56 */     this.errorMode = errorMode;
/*  57 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  62 */     this.source.subscribe(new ConcatMapMaybeSubscriber<T, R>(s, this.mapper, this.prefetch, this.errorMode));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapMaybeSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -9140123220065488293L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final ConcatMapMaybeObserver<R> inner;
/*     */     
/*     */     final SimplePlainQueue<T> queue;
/*     */     
/*     */     final ErrorMode errorMode;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     long emitted;
/*     */     
/*     */     int consumed;
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
/*     */     ConcatMapMaybeSubscriber(Subscriber<? super R> downstream, Function<? super T, ? extends MaybeSource<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/* 111 */       this.downstream = downstream;
/* 112 */       this.mapper = mapper;
/* 113 */       this.prefetch = prefetch;
/* 114 */       this.errorMode = errorMode;
/* 115 */       this.requested = new AtomicLong();
/* 116 */       this.errors = new AtomicThrowable();
/* 117 */       this.inner = new ConcatMapMaybeObserver<R>(this);
/* 118 */       this.queue = (SimplePlainQueue<T>)new SpscArrayQueue(prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 123 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 124 */         this.upstream = s;
/* 125 */         this.downstream.onSubscribe(this);
/* 126 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 132 */       if (!this.queue.offer(t)) {
/* 133 */         this.upstream.cancel();
/* 134 */         onError((Throwable)new MissingBackpressureException("queue full?!"));
/*     */         return;
/*     */       } 
/* 137 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 142 */       if (this.errors.addThrowable(t)) {
/* 143 */         if (this.errorMode == ErrorMode.IMMEDIATE) {
/* 144 */           this.inner.dispose();
/*     */         }
/* 146 */         this.done = true;
/* 147 */         drain();
/*     */       } else {
/* 149 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 155 */       this.done = true;
/* 156 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 161 */       BackpressureHelper.add(this.requested, n);
/* 162 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 167 */       this.cancelled = true;
/* 168 */       this.upstream.cancel();
/* 169 */       this.inner.dispose();
/* 170 */       if (getAndIncrement() == 0) {
/* 171 */         this.queue.clear();
/* 172 */         this.item = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerSuccess(R item) {
/* 177 */       this.item = item;
/* 178 */       this.state = 2;
/* 179 */       drain();
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 183 */       this.state = 0;
/* 184 */       drain();
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex) {
/* 188 */       if (this.errors.addThrowable(ex)) {
/* 189 */         if (this.errorMode != ErrorMode.END) {
/* 190 */           this.upstream.cancel();
/*     */         }
/* 192 */         this.state = 0;
/* 193 */         drain();
/*     */       } else {
/* 195 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 200 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 204 */       int missed = 1;
/* 205 */       Subscriber<? super R> downstream = this.downstream;
/* 206 */       ErrorMode errorMode = this.errorMode;
/* 207 */       SimplePlainQueue<T> queue = this.queue;
/* 208 */       AtomicThrowable errors = this.errors;
/* 209 */       AtomicLong requested = this.requested;
/* 210 */       int limit = this.prefetch - (this.prefetch >> 1);
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 215 */         if (this.cancelled) {
/* 216 */           queue.clear();
/* 217 */           this.item = null;
/*     */         }
/*     */         else {
/*     */           
/* 221 */           int s = this.state;
/*     */           
/* 223 */           if (errors.get() != null && (
/* 224 */             errorMode == ErrorMode.IMMEDIATE || (errorMode == ErrorMode.BOUNDARY && s == 0))) {
/*     */             
/* 226 */             queue.clear();
/* 227 */             this.item = null;
/* 228 */             Throwable ex = errors.terminate();
/* 229 */             downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 234 */           if (s == 0) {
/* 235 */             boolean d = this.done;
/* 236 */             T v = (T)queue.poll();
/* 237 */             boolean empty = (v == null);
/*     */             
/* 239 */             if (d && empty) {
/* 240 */               Throwable ex = errors.terminate();
/* 241 */               if (ex == null) {
/* 242 */                 downstream.onComplete();
/*     */               } else {
/* 244 */                 downstream.onError(ex);
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/* 249 */             if (!empty) {
/*     */               MaybeSource<? extends R> ms;
/*     */ 
/*     */               
/* 253 */               int c = this.consumed + 1;
/* 254 */               if (c == limit) {
/* 255 */                 this.consumed = 0;
/* 256 */                 this.upstream.request(limit);
/*     */               } else {
/* 258 */                 this.consumed = c;
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 264 */                 ms = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null MaybeSource");
/* 265 */               } catch (Throwable ex) {
/* 266 */                 Exceptions.throwIfFatal(ex);
/* 267 */                 this.upstream.cancel();
/* 268 */                 queue.clear();
/* 269 */                 errors.addThrowable(ex);
/* 270 */                 ex = errors.terminate();
/* 271 */                 downstream.onError(ex);
/*     */                 
/*     */                 return;
/*     */               } 
/* 275 */               this.state = 1;
/* 276 */               ms.subscribe(this.inner);
/*     */             } 
/* 278 */           } else if (s == 2) {
/* 279 */             long e = this.emitted;
/* 280 */             if (e != requested.get()) {
/* 281 */               R w = this.item;
/* 282 */               this.item = null;
/*     */               
/* 284 */               downstream.onNext(w);
/*     */               
/* 286 */               this.emitted = e + 1L;
/* 287 */               this.state = 0;
/*     */ 
/*     */               
/*     */               continue;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 296 */         missed = addAndGet(-missed);
/* 297 */         if (missed == 0) {
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
/*     */       final FlowableConcatMapMaybe.ConcatMapMaybeSubscriber<?, R> parent;
/*     */       
/*     */       ConcatMapMaybeObserver(FlowableConcatMapMaybe.ConcatMapMaybeSubscriber<?, R> parent) {
/* 312 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 317 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R t) {
/* 322 */         this.parent.innerSuccess(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 327 */         this.parent.innerError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 332 */         this.parent.innerComplete();
/*     */       }
/*     */       
/*     */       void dispose() {
/* 336 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/FlowableConcatMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */