/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
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
/*     */ public final class FlowableMergeWithMaybe<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final MaybeSource<? extends T> other;
/*     */   
/*     */   public FlowableMergeWithMaybe(Flowable<T> source, MaybeSource<? extends T> other) {
/*  41 */     super(source);
/*  42 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> subscriber) {
/*  47 */     MergeWithObserver<T> parent = new MergeWithObserver<T>(subscriber);
/*  48 */     subscriber.onSubscribe(parent);
/*  49 */     this.source.subscribe(parent);
/*  50 */     this.other.subscribe(parent.otherObserver);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeWithObserver<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -4592979584110982903L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final AtomicReference<Subscription> mainSubscription;
/*     */     
/*     */     final OtherObserver<T> otherObserver;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     volatile SimplePlainQueue<T> queue;
/*     */     
/*     */     T singleItem;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean mainDone;
/*     */     
/*     */     volatile int otherState;
/*     */     
/*     */     long emitted;
/*     */     
/*     */     int consumed;
/*     */     static final int OTHER_STATE_HAS_VALUE = 1;
/*     */     static final int OTHER_STATE_CONSUMED_OR_EMPTY = 2;
/*     */     
/*     */     MergeWithObserver(Subscriber<? super T> downstream) {
/*  91 */       this.downstream = downstream;
/*  92 */       this.mainSubscription = new AtomicReference<Subscription>();
/*  93 */       this.otherObserver = new OtherObserver<T>(this);
/*  94 */       this.error = new AtomicThrowable();
/*  95 */       this.requested = new AtomicLong();
/*  96 */       this.prefetch = Flowable.bufferSize();
/*  97 */       this.limit = this.prefetch - (this.prefetch >> 2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 102 */       SubscriptionHelper.setOnce(this.mainSubscription, s, this.prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 107 */       if (compareAndSet(0, 1)) {
/* 108 */         long e = this.emitted;
/* 109 */         if (this.requested.get() != e) {
/* 110 */           SimplePlainQueue<T> q = this.queue;
/* 111 */           if (q == null || q.isEmpty()) {
/*     */             
/* 113 */             this.emitted = e + 1L;
/* 114 */             this.downstream.onNext(t);
/*     */             
/* 116 */             int c = this.consumed + 1;
/* 117 */             if (c == this.limit) {
/* 118 */               this.consumed = 0;
/* 119 */               ((Subscription)this.mainSubscription.get()).request(c);
/*     */             } else {
/* 121 */               this.consumed = c;
/*     */             } 
/*     */           } else {
/* 124 */             q.offer(t);
/*     */           } 
/*     */         } else {
/* 127 */           SimplePlainQueue<T> q = getOrCreateQueue();
/* 128 */           q.offer(t);
/*     */         } 
/* 130 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 134 */         SimplePlainQueue<T> q = getOrCreateQueue();
/* 135 */         q.offer(t);
/* 136 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 140 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/* 145 */       if (this.error.addThrowable(ex)) {
/* 146 */         DisposableHelper.dispose(this.otherObserver);
/* 147 */         drain();
/*     */       } else {
/* 149 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 155 */       this.mainDone = true;
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
/* 168 */       SubscriptionHelper.cancel(this.mainSubscription);
/* 169 */       DisposableHelper.dispose(this.otherObserver);
/* 170 */       if (getAndIncrement() == 0) {
/* 171 */         this.queue = null;
/* 172 */         this.singleItem = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     void otherSuccess(T value) {
/* 177 */       if (compareAndSet(0, 1)) {
/* 178 */         long e = this.emitted;
/* 179 */         if (this.requested.get() != e) {
/*     */           
/* 181 */           this.emitted = e + 1L;
/* 182 */           this.downstream.onNext(value);
/* 183 */           this.otherState = 2;
/*     */         } else {
/* 185 */           this.singleItem = value;
/* 186 */           this.otherState = 1;
/* 187 */           if (decrementAndGet() == 0) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */       } else {
/* 192 */         this.singleItem = value;
/* 193 */         this.otherState = 1;
/* 194 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 198 */       drainLoop();
/*     */     }
/*     */     
/*     */     void otherError(Throwable ex) {
/* 202 */       if (this.error.addThrowable(ex)) {
/* 203 */         SubscriptionHelper.cancel(this.mainSubscription);
/* 204 */         drain();
/*     */       } else {
/* 206 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void otherComplete() {
/* 211 */       this.otherState = 2;
/* 212 */       drain();
/*     */     }
/*     */     SimplePlainQueue<T> getOrCreateQueue() {
/*     */       SpscArrayQueue spscArrayQueue;
/* 216 */       SimplePlainQueue<T> q = this.queue;
/* 217 */       if (q == null) {
/* 218 */         spscArrayQueue = new SpscArrayQueue(Flowable.bufferSize());
/* 219 */         this.queue = (SimplePlainQueue<T>)spscArrayQueue;
/*     */       } 
/* 221 */       return (SimplePlainQueue<T>)spscArrayQueue;
/*     */     }
/*     */     
/*     */     void drain() {
/* 225 */       if (getAndIncrement() == 0) {
/* 226 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 231 */       Subscriber<? super T> actual = this.downstream;
/* 232 */       int missed = 1;
/* 233 */       long e = this.emitted;
/* 234 */       int c = this.consumed;
/* 235 */       int lim = this.limit;
/*     */       
/*     */       do {
/* 238 */         long r = this.requested.get();
/*     */         
/* 240 */         while (e != r) {
/* 241 */           if (this.cancelled) {
/* 242 */             this.singleItem = null;
/* 243 */             this.queue = null;
/*     */             
/*     */             return;
/*     */           } 
/* 247 */           if (this.error.get() != null) {
/* 248 */             this.singleItem = null;
/* 249 */             this.queue = null;
/* 250 */             actual.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 254 */           int os = this.otherState;
/* 255 */           if (os == 1) {
/* 256 */             T t = this.singleItem;
/* 257 */             this.singleItem = null;
/* 258 */             this.otherState = 2;
/* 259 */             os = 2;
/* 260 */             actual.onNext(t);
/*     */             
/* 262 */             e++;
/*     */             
/*     */             continue;
/*     */           } 
/* 266 */           boolean d = this.mainDone;
/* 267 */           SimplePlainQueue<T> q = this.queue;
/* 268 */           T v = (q != null) ? (T)q.poll() : null;
/* 269 */           boolean empty = (v == null);
/*     */           
/* 271 */           if (d && empty && os == 2) {
/* 272 */             this.queue = null;
/* 273 */             actual.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 277 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 281 */           actual.onNext(v);
/*     */           
/* 283 */           e++;
/*     */           
/* 285 */           if (++c == lim) {
/* 286 */             c = 0;
/* 287 */             ((Subscription)this.mainSubscription.get()).request(lim);
/*     */           } 
/*     */         } 
/*     */         
/* 291 */         if (e == r) {
/* 292 */           if (this.cancelled) {
/* 293 */             this.singleItem = null;
/* 294 */             this.queue = null;
/*     */             
/*     */             return;
/*     */           } 
/* 298 */           if (this.error.get() != null) {
/* 299 */             this.singleItem = null;
/* 300 */             this.queue = null;
/* 301 */             actual.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 305 */           boolean d = this.mainDone;
/* 306 */           SimplePlainQueue<T> q = this.queue;
/* 307 */           boolean empty = (q == null || q.isEmpty());
/*     */           
/* 309 */           if (d && empty && this.otherState == 2) {
/* 310 */             this.queue = null;
/* 311 */             actual.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 316 */         this.emitted = e;
/* 317 */         this.consumed = c;
/* 318 */         missed = addAndGet(-missed);
/* 319 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     static final class OtherObserver<T>
/*     */       extends AtomicReference<Disposable>
/*     */       implements MaybeObserver<T>
/*     */     {
/*     */       private static final long serialVersionUID = -2935427570954647017L;
/*     */       
/*     */       final FlowableMergeWithMaybe.MergeWithObserver<T> parent;
/*     */ 
/*     */       
/*     */       OtherObserver(FlowableMergeWithMaybe.MergeWithObserver<T> parent) {
/* 333 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 338 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T t) {
/* 343 */         this.parent.otherSuccess(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 348 */         this.parent.otherError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 353 */         this.parent.otherComplete(); } } } static final class OtherObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> { public void onComplete() { this.parent.otherComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -2935427570954647017L;
/*     */     final FlowableMergeWithMaybe.MergeWithObserver<T> parent;
/*     */     
/*     */     OtherObserver(FlowableMergeWithMaybe.MergeWithObserver<T> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onSuccess(T t) {
/*     */       this.parent.otherSuccess(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       this.parent.otherError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableMergeWithMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */