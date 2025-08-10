/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ public final class FlowableMergeWithSingle<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final SingleSource<? extends T> other;
/*     */   
/*     */   public FlowableMergeWithSingle(Flowable<T> source, SingleSource<? extends T> other) {
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
/*     */     SimplePlainQueue<T> getOrCreateQueue() {
/*     */       SpscArrayQueue spscArrayQueue;
/* 211 */       SimplePlainQueue<T> q = this.queue;
/* 212 */       if (q == null) {
/* 213 */         spscArrayQueue = new SpscArrayQueue(Flowable.bufferSize());
/* 214 */         this.queue = (SimplePlainQueue<T>)spscArrayQueue;
/*     */       } 
/* 216 */       return (SimplePlainQueue<T>)spscArrayQueue;
/*     */     }
/*     */     
/*     */     void drain() {
/* 220 */       if (getAndIncrement() == 0) {
/* 221 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 226 */       Subscriber<? super T> actual = this.downstream;
/* 227 */       int missed = 1;
/* 228 */       long e = this.emitted;
/* 229 */       int c = this.consumed;
/* 230 */       int lim = this.limit;
/*     */       
/*     */       do {
/* 233 */         long r = this.requested.get();
/*     */         
/* 235 */         while (e != r) {
/* 236 */           if (this.cancelled) {
/* 237 */             this.singleItem = null;
/* 238 */             this.queue = null;
/*     */             
/*     */             return;
/*     */           } 
/* 242 */           if (this.error.get() != null) {
/* 243 */             this.singleItem = null;
/* 244 */             this.queue = null;
/* 245 */             actual.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 249 */           int os = this.otherState;
/* 250 */           if (os == 1) {
/* 251 */             T t = this.singleItem;
/* 252 */             this.singleItem = null;
/* 253 */             this.otherState = 2;
/* 254 */             os = 2;
/* 255 */             actual.onNext(t);
/*     */             
/* 257 */             e++;
/*     */             
/*     */             continue;
/*     */           } 
/* 261 */           boolean d = this.mainDone;
/* 262 */           SimplePlainQueue<T> q = this.queue;
/* 263 */           T v = (q != null) ? (T)q.poll() : null;
/* 264 */           boolean empty = (v == null);
/*     */           
/* 266 */           if (d && empty && os == 2) {
/* 267 */             this.queue = null;
/* 268 */             actual.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 272 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 276 */           actual.onNext(v);
/*     */           
/* 278 */           e++;
/*     */           
/* 280 */           if (++c == lim) {
/* 281 */             c = 0;
/* 282 */             ((Subscription)this.mainSubscription.get()).request(lim);
/*     */           } 
/*     */         } 
/*     */         
/* 286 */         if (e == r) {
/* 287 */           if (this.cancelled) {
/* 288 */             this.singleItem = null;
/* 289 */             this.queue = null;
/*     */             
/*     */             return;
/*     */           } 
/* 293 */           if (this.error.get() != null) {
/* 294 */             this.singleItem = null;
/* 295 */             this.queue = null;
/* 296 */             actual.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 300 */           boolean d = this.mainDone;
/* 301 */           SimplePlainQueue<T> q = this.queue;
/* 302 */           boolean empty = (q == null || q.isEmpty());
/*     */           
/* 304 */           if (d && empty && this.otherState == 2) {
/* 305 */             this.queue = null;
/* 306 */             actual.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 311 */         this.emitted = e;
/* 312 */         this.consumed = c;
/* 313 */         missed = addAndGet(-missed);
/* 314 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     static final class OtherObserver<T>
/*     */       extends AtomicReference<Disposable>
/*     */       implements SingleObserver<T>
/*     */     {
/*     */       private static final long serialVersionUID = -2935427570954647017L;
/*     */       
/*     */       final FlowableMergeWithSingle.MergeWithObserver<T> parent;
/*     */ 
/*     */       
/*     */       OtherObserver(FlowableMergeWithSingle.MergeWithObserver<T> parent) {
/* 328 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 333 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T t) {
/* 338 */         this.parent.otherSuccess(t);
/*     */       }
/*     */       
/*     */       public void onError(Throwable e)
/*     */       {
/* 343 */         this.parent.otherError(e); } } } static final class OtherObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> { public void onError(Throwable e) { this.parent.otherError(e); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -2935427570954647017L;
/*     */     final FlowableMergeWithSingle.MergeWithObserver<T> parent;
/*     */     
/*     */     OtherObserver(FlowableMergeWithSingle.MergeWithObserver<T> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onSuccess(T t) {
/*     */       this.parent.otherSuccess(t);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableMergeWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */