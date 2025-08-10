/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ public final class FlowableSequenceEqual<T>
/*     */   extends Flowable<Boolean>
/*     */ {
/*     */   final Publisher<? extends T> first;
/*     */   final Publisher<? extends T> second;
/*     */   final BiPredicate<? super T, ? super T> comparer;
/*     */   final int prefetch;
/*     */   
/*     */   public FlowableSequenceEqual(Publisher<? extends T> first, Publisher<? extends T> second, BiPredicate<? super T, ? super T> comparer, int prefetch) {
/*  37 */     this.first = first;
/*  38 */     this.second = second;
/*  39 */     this.comparer = comparer;
/*  40 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super Boolean> s) {
/*  45 */     EqualCoordinator<T> parent = new EqualCoordinator<T>(s, this.prefetch, this.comparer);
/*  46 */     s.onSubscribe((Subscription)parent);
/*  47 */     parent.subscribe(this.first, this.second);
/*     */   }
/*     */ 
/*     */   
/*     */   static interface EqualCoordinatorHelper
/*     */   {
/*     */     void drain();
/*     */ 
/*     */     
/*     */     void innerError(Throwable param1Throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EqualCoordinator<T>
/*     */     extends DeferredScalarSubscription<Boolean>
/*     */     implements EqualCoordinatorHelper
/*     */   {
/*     */     private static final long serialVersionUID = -6178010334400373240L;
/*     */     
/*     */     final BiPredicate<? super T, ? super T> comparer;
/*     */     
/*     */     final FlowableSequenceEqual.EqualSubscriber<T> first;
/*     */     
/*     */     final FlowableSequenceEqual.EqualSubscriber<T> second;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final AtomicInteger wip;
/*     */     
/*     */     T v1;
/*     */     T v2;
/*     */     
/*     */     EqualCoordinator(Subscriber<? super Boolean> actual, int prefetch, BiPredicate<? super T, ? super T> comparer) {
/*  80 */       super(actual);
/*  81 */       this.comparer = comparer;
/*  82 */       this.wip = new AtomicInteger();
/*  83 */       this.first = new FlowableSequenceEqual.EqualSubscriber<T>(this, prefetch);
/*  84 */       this.second = new FlowableSequenceEqual.EqualSubscriber<T>(this, prefetch);
/*  85 */       this.error = new AtomicThrowable();
/*     */     }
/*     */     
/*     */     void subscribe(Publisher<? extends T> source1, Publisher<? extends T> source2) {
/*  89 */       source1.subscribe((Subscriber)this.first);
/*  90 */       source2.subscribe((Subscriber)this.second);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  95 */       super.cancel();
/*  96 */       this.first.cancel();
/*  97 */       this.second.cancel();
/*  98 */       if (this.wip.getAndIncrement() == 0) {
/*  99 */         this.first.clear();
/* 100 */         this.second.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     void cancelAndClear() {
/* 105 */       this.first.cancel();
/* 106 */       this.first.clear();
/* 107 */       this.second.cancel();
/* 108 */       this.second.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public void drain() {
/* 113 */       if (this.wip.getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 117 */       int missed = 1;
/*     */       
/*     */       do {
/* 120 */         SimpleQueue<T> q1 = this.first.queue;
/* 121 */         SimpleQueue<T> q2 = this.second.queue;
/*     */         
/* 123 */         if (q1 != null && q2 != null) {
/*     */           while (true) {
/* 125 */             boolean c; if (isCancelled()) {
/* 126 */               this.first.clear();
/* 127 */               this.second.clear();
/*     */               
/*     */               return;
/*     */             } 
/* 131 */             Throwable ex = (Throwable)this.error.get();
/* 132 */             if (ex != null) {
/* 133 */               cancelAndClear();
/*     */               
/* 135 */               this.downstream.onError(this.error.terminate());
/*     */               
/*     */               return;
/*     */             } 
/* 139 */             boolean d1 = this.first.done;
/*     */             
/* 141 */             T a = this.v1;
/* 142 */             if (a == null) {
/*     */               try {
/* 144 */                 a = (T)q1.poll();
/* 145 */               } catch (Throwable exc) {
/* 146 */                 Exceptions.throwIfFatal(exc);
/* 147 */                 cancelAndClear();
/* 148 */                 this.error.addThrowable(exc);
/* 149 */                 this.downstream.onError(this.error.terminate());
/*     */                 return;
/*     */               } 
/* 152 */               this.v1 = a;
/*     */             } 
/* 154 */             boolean e1 = (a == null);
/*     */             
/* 156 */             boolean d2 = this.second.done;
/* 157 */             T b = this.v2;
/* 158 */             if (b == null) {
/*     */               try {
/* 160 */                 b = (T)q2.poll();
/* 161 */               } catch (Throwable exc) {
/* 162 */                 Exceptions.throwIfFatal(exc);
/* 163 */                 cancelAndClear();
/* 164 */                 this.error.addThrowable(exc);
/* 165 */                 this.downstream.onError(this.error.terminate());
/*     */                 return;
/*     */               } 
/* 168 */               this.v2 = b;
/*     */             } 
/*     */             
/* 171 */             boolean e2 = (b == null);
/*     */             
/* 173 */             if (d1 && d2 && e1 && e2) {
/* 174 */               complete(Boolean.valueOf(true));
/*     */               return;
/*     */             } 
/* 177 */             if (d1 && d2 && e1 != e2) {
/* 178 */               cancelAndClear();
/* 179 */               complete(Boolean.valueOf(false));
/*     */               
/*     */               return;
/*     */             } 
/* 183 */             if (e1 || e2) {
/*     */               break;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 190 */               c = this.comparer.test(a, b);
/* 191 */             } catch (Throwable exc) {
/* 192 */               Exceptions.throwIfFatal(exc);
/* 193 */               cancelAndClear();
/* 194 */               this.error.addThrowable(exc);
/* 195 */               this.downstream.onError(this.error.terminate());
/*     */               
/*     */               return;
/*     */             } 
/* 199 */             if (!c) {
/* 200 */               cancelAndClear();
/* 201 */               complete(Boolean.valueOf(false));
/*     */               
/*     */               return;
/*     */             } 
/* 205 */             this.v1 = null;
/* 206 */             this.v2 = null;
/*     */             
/* 208 */             this.first.request();
/* 209 */             this.second.request();
/*     */           } 
/*     */         } else {
/*     */           
/* 213 */           if (isCancelled()) {
/* 214 */             this.first.clear();
/* 215 */             this.second.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 219 */           Throwable ex = (Throwable)this.error.get();
/* 220 */           if (ex != null) {
/* 221 */             cancelAndClear();
/*     */             
/* 223 */             this.downstream.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 228 */         missed = this.wip.addAndGet(-missed);
/* 229 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void innerError(Throwable t) {
/* 237 */       if (this.error.addThrowable(t)) {
/* 238 */         drain();
/*     */       } else {
/* 240 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EqualSubscriber<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4804128302091633067L;
/*     */     
/*     */     final FlowableSequenceEqual.EqualCoordinatorHelper parent;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     long produced;
/*     */     
/*     */     volatile SimpleQueue<T> queue;
/*     */     
/*     */     volatile boolean done;
/*     */     int sourceMode;
/*     */     
/*     */     EqualSubscriber(FlowableSequenceEqual.EqualCoordinatorHelper parent, int prefetch) {
/* 266 */       this.parent = parent;
/* 267 */       this.limit = prefetch - (prefetch >> 2);
/* 268 */       this.prefetch = prefetch;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 273 */       if (SubscriptionHelper.setOnce(this, s)) {
/* 274 */         if (s instanceof QueueSubscription) {
/*     */           
/* 276 */           QueueSubscription<T> qs = (QueueSubscription<T>)s;
/*     */           
/* 278 */           int m = qs.requestFusion(3);
/* 279 */           if (m == 1) {
/* 280 */             this.sourceMode = m;
/* 281 */             this.queue = (SimpleQueue<T>)qs;
/* 282 */             this.done = true;
/* 283 */             this.parent.drain();
/*     */             return;
/*     */           } 
/* 286 */           if (m == 2) {
/* 287 */             this.sourceMode = m;
/* 288 */             this.queue = (SimpleQueue<T>)qs;
/* 289 */             s.request(this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 294 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
/*     */         
/* 296 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 302 */       if (this.sourceMode == 0 && 
/* 303 */         !this.queue.offer(t)) {
/* 304 */         onError((Throwable)new MissingBackpressureException());
/*     */         
/*     */         return;
/*     */       } 
/* 308 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 313 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 318 */       this.done = true;
/* 319 */       this.parent.drain();
/*     */     }
/*     */     
/*     */     public void request() {
/* 323 */       if (this.sourceMode != 1) {
/* 324 */         long p = this.produced + 1L;
/* 325 */         if (p >= this.limit) {
/* 326 */           this.produced = 0L;
/* 327 */           get().request(p);
/*     */         } else {
/* 329 */           this.produced = p;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void cancel() {
/* 335 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */     
/*     */     void clear() {
/* 339 */       SimpleQueue<T> sq = this.queue;
/* 340 */       if (sq != null)
/* 341 */         sq.clear(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSequenceEqual.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */