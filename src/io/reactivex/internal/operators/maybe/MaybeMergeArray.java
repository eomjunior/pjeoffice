/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*     */ public final class MaybeMergeArray<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final MaybeSource<? extends T>[] sources;
/*     */   
/*     */   public MaybeMergeArray(MaybeSource<? extends T>[] sources) {
/*  40 */     this.sources = sources;
/*     */   }
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*     */     SimpleQueueWithConsumerIndex<Object> queue;
/*  45 */     MaybeSource<? extends T>[] maybes = this.sources;
/*  46 */     int n = maybes.length;
/*     */ 
/*     */ 
/*     */     
/*  50 */     if (n <= bufferSize()) {
/*  51 */       queue = new MpscFillOnceSimpleQueue(n);
/*     */     } else {
/*  53 */       queue = new ClqSimpleQueue();
/*     */     } 
/*  55 */     MergeMaybeObserver<T> parent = new MergeMaybeObserver<T>(s, n, queue);
/*     */     
/*  57 */     s.onSubscribe((Subscription)parent);
/*     */     
/*  59 */     AtomicThrowable e = parent.error;
/*     */     
/*  61 */     for (MaybeSource<? extends T> source : maybes) {
/*  62 */       if (parent.isCancelled() || e.get() != null) {
/*     */         return;
/*     */       }
/*     */       
/*  66 */       source.subscribe(parent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeMaybeObserver<T>
/*     */     extends BasicIntQueueSubscription<T>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -660395290758764731L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final CompositeDisposable set;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> queue;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final int sourceCount;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     boolean outputFused;
/*     */     long consumed;
/*     */     
/*     */     MergeMaybeObserver(Subscriber<? super T> actual, int sourceCount, MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> queue) {
/*  94 */       this.downstream = actual;
/*  95 */       this.sourceCount = sourceCount;
/*  96 */       this.set = new CompositeDisposable();
/*  97 */       this.requested = new AtomicLong();
/*  98 */       this.error = new AtomicThrowable();
/*  99 */       this.queue = queue;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 104 */       if ((mode & 0x2) != 0) {
/* 105 */         this.outputFused = true;
/* 106 */         return 2;
/*     */       } 
/* 108 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       while (true) {
/* 116 */         Object o = this.queue.poll();
/* 117 */         if (o != NotificationLite.COMPLETE) {
/* 118 */           return (T)o;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 125 */       return this.queue.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 130 */       this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 135 */       if (SubscriptionHelper.validate(n)) {
/* 136 */         BackpressureHelper.add(this.requested, n);
/* 137 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 143 */       if (!this.cancelled) {
/* 144 */         this.cancelled = true;
/* 145 */         this.set.dispose();
/* 146 */         if (getAndIncrement() == 0) {
/* 147 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 154 */       this.set.add(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 159 */       this.queue.offer(value);
/* 160 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 165 */       if (this.error.addThrowable(e)) {
/* 166 */         this.set.dispose();
/* 167 */         this.queue.offer(NotificationLite.COMPLETE);
/* 168 */         drain();
/*     */       } else {
/* 170 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 176 */       this.queue.offer(NotificationLite.COMPLETE);
/* 177 */       drain();
/*     */     }
/*     */     
/*     */     boolean isCancelled() {
/* 181 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     void drainNormal() {
/* 186 */       int missed = 1;
/* 187 */       Subscriber<? super T> a = this.downstream;
/* 188 */       MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> q = this.queue;
/* 189 */       long e = this.consumed;
/*     */ 
/*     */       
/*     */       do {
/* 193 */         long r = this.requested.get();
/*     */         
/* 195 */         while (e != r) {
/* 196 */           if (this.cancelled) {
/* 197 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 201 */           Throwable ex = (Throwable)this.error.get();
/* 202 */           if (ex != null) {
/* 203 */             q.clear();
/* 204 */             a.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 208 */           if (q.consumerIndex() == this.sourceCount) {
/* 209 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 213 */           Object v = q.poll();
/*     */           
/* 215 */           if (v == null) {
/*     */             break;
/*     */           }
/*     */           
/* 219 */           if (v != NotificationLite.COMPLETE) {
/* 220 */             a.onNext(v);
/*     */             
/* 222 */             e++;
/*     */           } 
/*     */         } 
/*     */         
/* 226 */         if (e == r) {
/* 227 */           Throwable ex = (Throwable)this.error.get();
/* 228 */           if (ex != null) {
/* 229 */             q.clear();
/* 230 */             a.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 234 */           while (q.peek() == NotificationLite.COMPLETE) {
/* 235 */             q.drop();
/*     */           }
/*     */           
/* 238 */           if (q.consumerIndex() == this.sourceCount) {
/* 239 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 244 */         this.consumed = e;
/* 245 */         missed = addAndGet(-missed);
/* 246 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void drainFused() {
/* 254 */       int missed = 1;
/* 255 */       Subscriber<? super T> a = this.downstream;
/* 256 */       MaybeMergeArray.SimpleQueueWithConsumerIndex<Object> q = this.queue;
/*     */       
/*     */       do {
/* 259 */         if (this.cancelled) {
/* 260 */           q.clear();
/*     */           return;
/*     */         } 
/* 263 */         Throwable ex = (Throwable)this.error.get();
/* 264 */         if (ex != null) {
/* 265 */           q.clear();
/* 266 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 270 */         boolean d = (q.producerIndex() == this.sourceCount);
/*     */         
/* 272 */         if (!q.isEmpty()) {
/* 273 */           a.onNext(null);
/*     */         }
/*     */         
/* 276 */         if (d) {
/* 277 */           a.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 281 */         missed = addAndGet(-missed);
/* 282 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void drain() {
/* 290 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 294 */       if (this.outputFused) {
/* 295 */         drainFused();
/*     */       } else {
/* 297 */         drainNormal();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static interface SimpleQueueWithConsumerIndex<T>
/*     */     extends SimpleQueue<T>
/*     */   {
/*     */     @Nullable
/*     */     T poll();
/*     */     
/*     */     T peek();
/*     */     
/*     */     void drop();
/*     */     
/*     */     int consumerIndex();
/*     */     
/*     */     int producerIndex();
/*     */   }
/*     */   
/*     */   static final class MpscFillOnceSimpleQueue<T>
/*     */     extends AtomicReferenceArray<T>
/*     */     implements SimpleQueueWithConsumerIndex<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7969063454040569579L;
/*     */     final AtomicInteger producerIndex;
/*     */     int consumerIndex;
/*     */     
/*     */     MpscFillOnceSimpleQueue(int length) {
/* 327 */       super(length);
/* 328 */       this.producerIndex = new AtomicInteger();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(T value) {
/* 333 */       ObjectHelper.requireNonNull(value, "value is null");
/* 334 */       int idx = this.producerIndex.getAndIncrement();
/* 335 */       if (idx < length()) {
/* 336 */         lazySet(idx, value);
/* 337 */         return true;
/*     */       } 
/* 339 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(T v1, T v2) {
/* 344 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() {
/* 350 */       int ci = this.consumerIndex;
/* 351 */       if (ci == length()) {
/* 352 */         return null;
/*     */       }
/* 354 */       AtomicInteger pi = this.producerIndex;
/*     */       while (true) {
/* 356 */         T v = get(ci);
/* 357 */         if (v != null) {
/* 358 */           this.consumerIndex = ci + 1;
/* 359 */           lazySet(ci, null);
/* 360 */           return v;
/*     */         } 
/* 362 */         if (pi.get() == ci) {
/* 363 */           return null;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public T peek() {
/* 370 */       int ci = this.consumerIndex;
/* 371 */       if (ci == length()) {
/* 372 */         return null;
/*     */       }
/* 374 */       return get(ci);
/*     */     }
/*     */ 
/*     */     
/*     */     public void drop() {
/* 379 */       int ci = this.consumerIndex;
/* 380 */       lazySet(ci, null);
/* 381 */       this.consumerIndex = ci + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 386 */       return (this.consumerIndex == producerIndex());
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 391 */       while (poll() != null && !isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public int consumerIndex() {
/* 396 */       return this.consumerIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int producerIndex() {
/* 401 */       return this.producerIndex.get();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ClqSimpleQueue<T>
/*     */     extends ConcurrentLinkedQueue<T>
/*     */     implements SimpleQueueWithConsumerIndex<T>
/*     */   {
/*     */     private static final long serialVersionUID = -4025173261791142821L;
/*     */     
/*     */     int consumerIndex;
/*     */     
/* 414 */     final AtomicInteger producerIndex = new AtomicInteger();
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean offer(T v1, T v2) {
/* 419 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(T e) {
/* 424 */       this.producerIndex.getAndIncrement();
/* 425 */       return super.offer(e);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() {
/* 431 */       T v = super.poll();
/* 432 */       if (v != null) {
/* 433 */         this.consumerIndex++;
/*     */       }
/* 435 */       return v;
/*     */     }
/*     */ 
/*     */     
/*     */     public int consumerIndex() {
/* 440 */       return this.consumerIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int producerIndex() {
/* 445 */       return this.producerIndex.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void drop() {
/* 450 */       poll();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeMergeArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */