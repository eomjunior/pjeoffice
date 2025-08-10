/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ public final class FlowableConcatMapSingle<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   final ErrorMode errorMode;
/*     */   final int prefetch;
/*     */   
/*     */   public FlowableConcatMapSingle(Flowable<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper, ErrorMode errorMode, int prefetch) {
/*  54 */     this.source = source;
/*  55 */     this.mapper = mapper;
/*  56 */     this.errorMode = errorMode;
/*  57 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  62 */     this.source.subscribe(new ConcatMapSingleSubscriber<T, R>(s, this.mapper, this.prefetch, this.errorMode));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ConcatMapSingleSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -9140123220065488293L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final ConcatMapSingleObserver<R> inner;
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
/*     */     ConcatMapSingleSubscriber(Subscriber<? super R> downstream, Function<? super T, ? extends SingleSource<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/* 111 */       this.downstream = downstream;
/* 112 */       this.mapper = mapper;
/* 113 */       this.prefetch = prefetch;
/* 114 */       this.errorMode = errorMode;
/* 115 */       this.requested = new AtomicLong();
/* 116 */       this.errors = new AtomicThrowable();
/* 117 */       this.inner = new ConcatMapSingleObserver<R>(this);
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
/*     */     void innerError(Throwable ex) {
/* 183 */       if (this.errors.addThrowable(ex)) {
/* 184 */         if (this.errorMode != ErrorMode.END) {
/* 185 */           this.upstream.cancel();
/*     */         }
/* 187 */         this.state = 0;
/* 188 */         drain();
/*     */       } else {
/* 190 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 195 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 199 */       int missed = 1;
/* 200 */       Subscriber<? super R> downstream = this.downstream;
/* 201 */       ErrorMode errorMode = this.errorMode;
/* 202 */       SimplePlainQueue<T> queue = this.queue;
/* 203 */       AtomicThrowable errors = this.errors;
/* 204 */       AtomicLong requested = this.requested;
/* 205 */       int limit = this.prefetch - (this.prefetch >> 1);
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 210 */         if (this.cancelled) {
/* 211 */           queue.clear();
/* 212 */           this.item = null;
/*     */         }
/*     */         else {
/*     */           
/* 216 */           int s = this.state;
/*     */           
/* 218 */           if (errors.get() != null && (
/* 219 */             errorMode == ErrorMode.IMMEDIATE || (errorMode == ErrorMode.BOUNDARY && s == 0))) {
/*     */             
/* 221 */             queue.clear();
/* 222 */             this.item = null;
/* 223 */             Throwable ex = errors.terminate();
/* 224 */             downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 229 */           if (s == 0) {
/* 230 */             boolean d = this.done;
/* 231 */             T v = (T)queue.poll();
/* 232 */             boolean empty = (v == null);
/*     */             
/* 234 */             if (d && empty) {
/* 235 */               Throwable ex = errors.terminate();
/* 236 */               if (ex == null) {
/* 237 */                 downstream.onComplete();
/*     */               } else {
/* 239 */                 downstream.onError(ex);
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/* 244 */             if (!empty) {
/*     */               SingleSource<? extends R> ss;
/*     */ 
/*     */               
/* 248 */               int c = this.consumed + 1;
/* 249 */               if (c == limit) {
/* 250 */                 this.consumed = 0;
/* 251 */                 this.upstream.request(limit);
/*     */               } else {
/* 253 */                 this.consumed = c;
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 259 */                 ss = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null SingleSource");
/* 260 */               } catch (Throwable ex) {
/* 261 */                 Exceptions.throwIfFatal(ex);
/* 262 */                 this.upstream.cancel();
/* 263 */                 queue.clear();
/* 264 */                 errors.addThrowable(ex);
/* 265 */                 ex = errors.terminate();
/* 266 */                 downstream.onError(ex);
/*     */                 
/*     */                 return;
/*     */               } 
/* 270 */               this.state = 1;
/* 271 */               ss.subscribe(this.inner);
/*     */             } 
/* 273 */           } else if (s == 2) {
/* 274 */             long e = this.emitted;
/* 275 */             if (e != requested.get()) {
/* 276 */               R w = this.item;
/* 277 */               this.item = null;
/*     */               
/* 279 */               downstream.onNext(w);
/*     */               
/* 281 */               this.emitted = e + 1L;
/* 282 */               this.state = 0;
/*     */ 
/*     */               
/*     */               continue;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 291 */         missed = addAndGet(-missed);
/* 292 */         if (missed == 0) {
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
/*     */       final FlowableConcatMapSingle.ConcatMapSingleSubscriber<?, R> parent;
/*     */       
/*     */       ConcatMapSingleObserver(FlowableConcatMapSingle.ConcatMapSingleSubscriber<?, R> parent) {
/* 307 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 312 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R t) {
/* 317 */         this.parent.innerSuccess(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 322 */         this.parent.innerError(e);
/*     */       }
/*     */       
/*     */       void dispose() {
/* 326 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/FlowableConcatMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */