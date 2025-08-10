/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class FlowablePublishMulticast<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector;
/*     */   final int prefetch;
/*     */   final boolean delayError;
/*     */   
/*     */   public FlowablePublishMulticast(Flowable<T> source, Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector, int prefetch, boolean delayError) {
/*  50 */     super(source);
/*  51 */     this.selector = selector;
/*  52 */     this.prefetch = prefetch;
/*  53 */     this.delayError = delayError;
/*     */   }
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*     */     Publisher<? extends R> other;
/*  58 */     MulticastProcessor<T> mp = new MulticastProcessor<T>(this.prefetch, this.delayError);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  63 */       other = (Publisher<? extends R>)ObjectHelper.requireNonNull(this.selector.apply(mp), "selector returned a null Publisher");
/*  64 */     } catch (Throwable ex) {
/*  65 */       Exceptions.throwIfFatal(ex);
/*  66 */       EmptySubscription.error(ex, s);
/*     */       
/*     */       return;
/*     */     } 
/*  70 */     OutputCanceller<R> out = new OutputCanceller<R>(s, mp);
/*     */     
/*  72 */     other.subscribe((Subscriber)out);
/*     */     
/*  74 */     this.source.subscribe(mp);
/*     */   }
/*     */   
/*     */   static final class OutputCanceller<R>
/*     */     implements FlowableSubscriber<R>, Subscription
/*     */   {
/*     */     final Subscriber<? super R> downstream;
/*     */     final FlowablePublishMulticast.MulticastProcessor<?> processor;
/*     */     Subscription upstream;
/*     */     
/*     */     OutputCanceller(Subscriber<? super R> actual, FlowablePublishMulticast.MulticastProcessor<?> processor) {
/*  85 */       this.downstream = actual;
/*  86 */       this.processor = processor;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  91 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  92 */         this.upstream = s;
/*     */         
/*  94 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R t) {
/* 100 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 105 */       this.downstream.onError(t);
/* 106 */       this.processor.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 111 */       this.downstream.onComplete();
/* 112 */       this.processor.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 117 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 122 */       this.upstream.cancel();
/* 123 */       this.processor.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class MulticastProcessor<T>
/*     */     extends Flowable<T>
/*     */     implements FlowableSubscriber<T>, Disposable {
/* 130 */     static final FlowablePublishMulticast.MulticastSubscription[] EMPTY = new FlowablePublishMulticast.MulticastSubscription[0];
/*     */ 
/*     */     
/* 133 */     static final FlowablePublishMulticast.MulticastSubscription[] TERMINATED = new FlowablePublishMulticast.MulticastSubscription[0];
/*     */     
/*     */     final AtomicInteger wip;
/*     */     
/*     */     final AtomicReference<FlowablePublishMulticast.MulticastSubscription<T>[]> subscribers;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     final boolean delayError;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     volatile SimpleQueue<T> queue;
/*     */     
/*     */     int sourceMode;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Throwable error;
/*     */     
/*     */     int consumed;
/*     */     
/*     */     MulticastProcessor(int prefetch, boolean delayError) {
/* 158 */       this.prefetch = prefetch;
/* 159 */       this.limit = prefetch - (prefetch >> 2);
/* 160 */       this.delayError = delayError;
/* 161 */       this.wip = new AtomicInteger();
/* 162 */       this.upstream = new AtomicReference<Subscription>();
/* 163 */       this.subscribers = new AtomicReference(EMPTY);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 168 */       if (SubscriptionHelper.setOnce(this.upstream, s)) {
/* 169 */         if (s instanceof QueueSubscription) {
/*     */           
/* 171 */           QueueSubscription<T> qs = (QueueSubscription<T>)s;
/*     */           
/* 173 */           int m = qs.requestFusion(3);
/* 174 */           if (m == 1) {
/* 175 */             this.sourceMode = m;
/* 176 */             this.queue = (SimpleQueue<T>)qs;
/* 177 */             this.done = true;
/* 178 */             drain();
/*     */             return;
/*     */           } 
/* 181 */           if (m == 2) {
/* 182 */             this.sourceMode = m;
/* 183 */             this.queue = (SimpleQueue<T>)qs;
/* 184 */             QueueDrainHelper.request(s, this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 189 */         this.queue = QueueDrainHelper.createQueue(this.prefetch);
/*     */         
/* 191 */         QueueDrainHelper.request(s, this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 197 */       SubscriptionHelper.cancel(this.upstream);
/* 198 */       if (this.wip.getAndIncrement() == 0) {
/* 199 */         SimpleQueue<T> q = this.queue;
/* 200 */         if (q != null) {
/* 201 */           q.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 208 */       return (this.upstream.get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 213 */       if (this.done) {
/*     */         return;
/*     */       }
/* 216 */       if (this.sourceMode == 0 && !this.queue.offer(t)) {
/* 217 */         ((Subscription)this.upstream.get()).cancel();
/* 218 */         onError((Throwable)new MissingBackpressureException());
/*     */         return;
/*     */       } 
/* 221 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 226 */       if (this.done) {
/* 227 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 230 */       this.error = t;
/* 231 */       this.done = true;
/* 232 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 237 */       if (!this.done) {
/* 238 */         this.done = true;
/* 239 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean add(FlowablePublishMulticast.MulticastSubscription<T> s) {
/*     */       while (true) {
/* 245 */         FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription1 = (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.get();
/* 246 */         if (arrayOfMulticastSubscription1 == TERMINATED) {
/* 247 */           return false;
/*     */         }
/* 249 */         int n = arrayOfMulticastSubscription1.length;
/*     */         
/* 251 */         FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription2 = new FlowablePublishMulticast.MulticastSubscription[n + 1];
/* 252 */         System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, n);
/* 253 */         arrayOfMulticastSubscription2[n] = s;
/* 254 */         if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2))
/* 255 */           return true; 
/*     */       } 
/*     */     }
/*     */     
/*     */     void remove(FlowablePublishMulticast.MulticastSubscription<T> s) {
/*     */       FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription1;
/*     */       FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription2;
/*     */       do {
/* 263 */         arrayOfMulticastSubscription1 = (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.get();
/* 264 */         int n = arrayOfMulticastSubscription1.length;
/* 265 */         if (n == 0) {
/*     */           return;
/*     */         }
/* 268 */         int j = -1;
/*     */         
/* 270 */         for (int i = 0; i < n; i++) {
/* 271 */           if (arrayOfMulticastSubscription1[i] == s) {
/* 272 */             j = i;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 277 */         if (j < 0) {
/*     */           return;
/*     */         }
/*     */         
/* 281 */         if (n == 1) {
/* 282 */           arrayOfMulticastSubscription2 = EMPTY;
/*     */         } else {
/* 284 */           arrayOfMulticastSubscription2 = new FlowablePublishMulticast.MulticastSubscription[n - 1];
/* 285 */           System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, j);
/* 286 */           System.arraycopy(arrayOfMulticastSubscription1, j + 1, arrayOfMulticastSubscription2, j, n - j - 1);
/*     */         } 
/* 288 */       } while (!this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void subscribeActual(Subscriber<? super T> s) {
/* 296 */       FlowablePublishMulticast.MulticastSubscription<T> ms = new FlowablePublishMulticast.MulticastSubscription<T>(s, this);
/* 297 */       s.onSubscribe(ms);
/* 298 */       if (add(ms)) {
/* 299 */         if (ms.isCancelled()) {
/* 300 */           remove(ms);
/*     */           return;
/*     */         } 
/* 303 */         drain();
/*     */       } else {
/* 305 */         Throwable ex = this.error;
/* 306 */         if (ex != null) {
/* 307 */           s.onError(ex);
/*     */         } else {
/* 309 */           s.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 315 */       if (this.wip.getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 319 */       int missed = 1;
/*     */       
/* 321 */       SimpleQueue<T> q = this.queue;
/*     */       
/* 323 */       int upstreamConsumed = this.consumed;
/* 324 */       int localLimit = this.limit;
/* 325 */       boolean canRequest = (this.sourceMode != 1);
/* 326 */       AtomicReference<FlowablePublishMulticast.MulticastSubscription<T>[]> subs = this.subscribers;
/*     */       
/* 328 */       FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription = (FlowablePublishMulticast.MulticastSubscription[])subs.get();
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 333 */         int n = arrayOfMulticastSubscription.length;
/*     */         
/* 335 */         if (q != null && n != 0) {
/* 336 */           long r = Long.MAX_VALUE;
/*     */           
/* 338 */           for (FlowablePublishMulticast.MulticastSubscription<T> ms : arrayOfMulticastSubscription) {
/* 339 */             long u = ms.get() - ms.emitted;
/* 340 */             if (u != Long.MIN_VALUE) {
/* 341 */               if (r > u) {
/* 342 */                 r = u;
/*     */               }
/*     */             } else {
/* 345 */               n--;
/*     */             } 
/*     */           } 
/*     */           
/* 349 */           if (n == 0) {
/* 350 */             r = 0L;
/*     */           }
/*     */           
/* 353 */           while (r != 0L) {
/* 354 */             T v; if (isDisposed()) {
/* 355 */               q.clear();
/*     */               
/*     */               return;
/*     */             } 
/* 359 */             boolean d = this.done;
/*     */             
/* 361 */             if (d && !this.delayError) {
/* 362 */               Throwable ex = this.error;
/* 363 */               if (ex != null) {
/* 364 */                 errorAll(ex);
/*     */ 
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */             
/*     */             try {
/* 372 */               v = (T)q.poll();
/* 373 */             } catch (Throwable ex) {
/* 374 */               Exceptions.throwIfFatal(ex);
/* 375 */               SubscriptionHelper.cancel(this.upstream);
/* 376 */               errorAll(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 380 */             boolean empty = (v == null);
/*     */             
/* 382 */             if (d && empty) {
/* 383 */               Throwable ex = this.error;
/* 384 */               if (ex != null) {
/* 385 */                 errorAll(ex);
/*     */               } else {
/* 387 */                 completeAll();
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/* 392 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 396 */             boolean subscribersChange = false;
/*     */             
/* 398 */             for (FlowablePublishMulticast.MulticastSubscription<T> ms : arrayOfMulticastSubscription) {
/* 399 */               long msr = ms.get();
/* 400 */               if (msr != Long.MIN_VALUE) {
/* 401 */                 if (msr != Long.MAX_VALUE) {
/* 402 */                   ms.emitted++;
/*     */                 }
/* 404 */                 ms.downstream.onNext(v);
/*     */               } else {
/* 406 */                 subscribersChange = true;
/*     */               } 
/*     */             } 
/*     */             
/* 410 */             r--;
/*     */             
/* 412 */             if (canRequest && ++upstreamConsumed == localLimit) {
/* 413 */               upstreamConsumed = 0;
/* 414 */               ((Subscription)this.upstream.get()).request(localLimit);
/*     */             } 
/*     */             
/* 417 */             FlowablePublishMulticast.MulticastSubscription[] arrayOfMulticastSubscription1 = (FlowablePublishMulticast.MulticastSubscription[])subs.get();
/* 418 */             if (!subscribersChange) { if (arrayOfMulticastSubscription1 != arrayOfMulticastSubscription)
/* 419 */                 continue;  continue; }  arrayOfMulticastSubscription = arrayOfMulticastSubscription1;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 424 */           if (r == 0L) {
/* 425 */             if (isDisposed()) {
/* 426 */               q.clear();
/*     */               
/*     */               return;
/*     */             } 
/* 430 */             boolean d = this.done;
/*     */             
/* 432 */             if (d && !this.delayError) {
/* 433 */               Throwable ex = this.error;
/* 434 */               if (ex != null) {
/* 435 */                 errorAll(ex);
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/* 440 */             if (d && q.isEmpty()) {
/* 441 */               Throwable ex = this.error;
/* 442 */               if (ex != null) {
/* 443 */                 errorAll(ex);
/*     */               } else {
/* 445 */                 completeAll();
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 452 */         this.consumed = upstreamConsumed;
/* 453 */         missed = this.wip.addAndGet(-missed);
/* 454 */         if (missed == 0) {
/*     */           break;
/*     */         }
/* 457 */         if (q == null) {
/* 458 */           q = this.queue;
/*     */         }
/* 460 */         arrayOfMulticastSubscription = (FlowablePublishMulticast.MulticastSubscription[])subs.get();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void errorAll(Throwable ex) {
/* 466 */       for (FlowablePublishMulticast.MulticastSubscription<T> ms : (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 467 */         if (ms.get() != Long.MIN_VALUE) {
/* 468 */           ms.downstream.onError(ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void completeAll() {
/* 475 */       for (FlowablePublishMulticast.MulticastSubscription<T> ms : (FlowablePublishMulticast.MulticastSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 476 */         if (ms.get() != Long.MIN_VALUE) {
/* 477 */           ms.downstream.onComplete();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MulticastSubscription<T>
/*     */     extends AtomicLong
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 8664815189257569791L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final FlowablePublishMulticast.MulticastProcessor<T> parent;
/*     */     long emitted;
/*     */     
/*     */     MulticastSubscription(Subscriber<? super T> actual, FlowablePublishMulticast.MulticastProcessor<T> parent) {
/* 496 */       this.downstream = actual;
/* 497 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 502 */       if (SubscriptionHelper.validate(n)) {
/* 503 */         BackpressureHelper.addCancel(this, n);
/* 504 */         this.parent.drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 510 */       if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
/* 511 */         this.parent.remove(this);
/* 512 */         this.parent.drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isCancelled() {
/* 517 */       return (get() == Long.MIN_VALUE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowablePublishMulticast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */