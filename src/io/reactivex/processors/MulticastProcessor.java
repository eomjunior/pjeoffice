/*     */ package io.reactivex.processors;
/*     */ 
/*     */ import io.reactivex.annotations.BackpressureKind;
/*     */ import io.reactivex.annotations.BackpressureSupport;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.SchedulerSupport;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @BackpressureSupport(BackpressureKind.FULL)
/*     */ @SchedulerSupport("none")
/*     */ public final class MulticastProcessor<T>
/*     */   extends FlowableProcessor<T>
/*     */ {
/*     */   final AtomicInteger wip;
/*     */   final AtomicReference<Subscription> upstream;
/*     */   final AtomicReference<MulticastSubscription<T>[]> subscribers;
/*     */   final AtomicBoolean once;
/*     */   final int bufferSize;
/*     */   final int limit;
/*     */   final boolean refcount;
/*     */   volatile SimpleQueue<T> queue;
/*     */   volatile boolean done;
/*     */   volatile Throwable error;
/*     */   int consumed;
/*     */   int fusionMode;
/* 158 */   static final MulticastSubscription[] EMPTY = new MulticastSubscription[0];
/*     */ 
/*     */   
/* 161 */   static final MulticastSubscription[] TERMINATED = new MulticastSubscription[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> MulticastProcessor<T> create() {
/* 172 */     return new MulticastProcessor<T>(bufferSize(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> MulticastProcessor<T> create(boolean refCount) {
/* 186 */     return new MulticastProcessor<T>(bufferSize(), refCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> MulticastProcessor<T> create(int bufferSize) {
/* 198 */     return new MulticastProcessor<T>(bufferSize, false);
/*     */   }
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> MulticastProcessor<T> create(int bufferSize, boolean refCount) {
/* 213 */     return new MulticastProcessor<T>(bufferSize, refCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MulticastProcessor(int bufferSize, boolean refCount) {
/* 225 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 226 */     this.bufferSize = bufferSize;
/* 227 */     this.limit = bufferSize - (bufferSize >> 2);
/* 228 */     this.wip = new AtomicInteger();
/* 229 */     this.subscribers = new AtomicReference(EMPTY);
/* 230 */     this.upstream = new AtomicReference<Subscription>();
/* 231 */     this.refcount = refCount;
/* 232 */     this.once = new AtomicBoolean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 242 */     if (SubscriptionHelper.setOnce(this.upstream, (Subscription)EmptySubscription.INSTANCE)) {
/* 243 */       this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.bufferSize);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startUnbounded() {
/* 254 */     if (SubscriptionHelper.setOnce(this.upstream, (Subscription)EmptySubscription.INSTANCE)) {
/* 255 */       this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.bufferSize);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 261 */     if (SubscriptionHelper.setOnce(this.upstream, s)) {
/* 262 */       if (s instanceof QueueSubscription) {
/*     */         
/* 264 */         QueueSubscription<T> qs = (QueueSubscription<T>)s;
/*     */         
/* 266 */         int m = qs.requestFusion(3);
/* 267 */         if (m == 1) {
/* 268 */           this.fusionMode = m;
/* 269 */           this.queue = (SimpleQueue<T>)qs;
/* 270 */           this.done = true;
/* 271 */           drain();
/*     */           return;
/*     */         } 
/* 274 */         if (m == 2) {
/* 275 */           this.fusionMode = m;
/* 276 */           this.queue = (SimpleQueue<T>)qs;
/*     */           
/* 278 */           s.request(this.bufferSize);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 283 */       this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.bufferSize);
/*     */       
/* 285 */       s.request(this.bufferSize);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 291 */     if (this.once.get()) {
/*     */       return;
/*     */     }
/* 294 */     if (this.fusionMode == 0) {
/* 295 */       ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 296 */       if (!this.queue.offer(t)) {
/* 297 */         SubscriptionHelper.cancel(this.upstream);
/* 298 */         onError((Throwable)new MissingBackpressureException());
/*     */         return;
/*     */       } 
/*     */     } 
/* 302 */     drain();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(T t) {
/* 312 */     if (this.once.get()) {
/* 313 */       return false;
/*     */     }
/* 315 */     ObjectHelper.requireNonNull(t, "offer called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 316 */     if (this.fusionMode == 0 && 
/* 317 */       this.queue.offer(t)) {
/* 318 */       drain();
/* 319 */       return true;
/*     */     } 
/*     */     
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 327 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 328 */     if (this.once.compareAndSet(false, true)) {
/* 329 */       this.error = t;
/* 330 */       this.done = true;
/* 331 */       drain();
/*     */     } else {
/* 333 */       RxJavaPlugins.onError(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 339 */     if (this.once.compareAndSet(false, true)) {
/* 340 */       this.done = true;
/* 341 */       drain();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSubscribers() {
/* 347 */     return (((MulticastSubscription[])this.subscribers.get()).length != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 352 */     return (this.once.get() && this.error != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 357 */     return (this.once.get() && this.error == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 362 */     return this.once.get() ? this.error : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/* 367 */     MulticastSubscription<T> ms = new MulticastSubscription<T>(s, this);
/* 368 */     s.onSubscribe(ms);
/* 369 */     if (add(ms)) {
/* 370 */       if (ms.get() == Long.MIN_VALUE) {
/* 371 */         remove(ms);
/*     */       } else {
/* 373 */         drain();
/*     */       } 
/*     */     } else {
/* 376 */       if (this.once.get() || !this.refcount) {
/* 377 */         Throwable ex = this.error;
/* 378 */         if (ex != null) {
/* 379 */           s.onError(ex);
/*     */           return;
/*     */         } 
/*     */       } 
/* 383 */       s.onComplete();
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean add(MulticastSubscription<T> inner) {
/*     */     while (true) {
/* 389 */       MulticastSubscription[] arrayOfMulticastSubscription1 = (MulticastSubscription[])this.subscribers.get();
/* 390 */       if (arrayOfMulticastSubscription1 == TERMINATED) {
/* 391 */         return false;
/*     */       }
/* 393 */       int n = arrayOfMulticastSubscription1.length;
/*     */       
/* 395 */       MulticastSubscription[] arrayOfMulticastSubscription2 = new MulticastSubscription[n + 1];
/* 396 */       System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, n);
/* 397 */       arrayOfMulticastSubscription2[n] = inner;
/* 398 */       if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2)) {
/* 399 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void remove(MulticastSubscription<T> inner) {
/*     */     while (true) {
/* 407 */       MulticastSubscription[] arrayOfMulticastSubscription1 = (MulticastSubscription[])this.subscribers.get();
/* 408 */       int n = arrayOfMulticastSubscription1.length;
/* 409 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 413 */       int j = -1;
/* 414 */       for (int i = 0; i < n; i++) {
/* 415 */         if (arrayOfMulticastSubscription1[i] == inner) {
/* 416 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 421 */       if (j < 0) {
/*     */         break;
/*     */       }
/*     */       
/* 425 */       if (n == 1) {
/* 426 */         if (this.refcount) {
/* 427 */           if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, TERMINATED)) {
/* 428 */             SubscriptionHelper.cancel(this.upstream);
/* 429 */             this.once.set(true); break;
/*     */           } 
/*     */           continue;
/*     */         } 
/* 433 */         if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, EMPTY)) {
/*     */           break;
/*     */         }
/*     */         continue;
/*     */       } 
/* 438 */       MulticastSubscription[] arrayOfMulticastSubscription2 = new MulticastSubscription[n - 1];
/* 439 */       System.arraycopy(arrayOfMulticastSubscription1, 0, arrayOfMulticastSubscription2, 0, j);
/* 440 */       System.arraycopy(arrayOfMulticastSubscription1, j + 1, arrayOfMulticastSubscription2, j, n - j - 1);
/* 441 */       if (this.subscribers.compareAndSet(arrayOfMulticastSubscription1, arrayOfMulticastSubscription2)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void drain() {
/* 450 */     if (this.wip.getAndIncrement() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 454 */     int missed = 1;
/* 455 */     AtomicReference<MulticastSubscription<T>[]> subs = this.subscribers;
/* 456 */     int c = this.consumed;
/* 457 */     int lim = this.limit;
/* 458 */     int fm = this.fusionMode;
/*     */ 
/*     */ 
/*     */     
/*     */     label98: while (true) {
/* 463 */       SimpleQueue<T> q = this.queue;
/*     */       
/* 465 */       if (q != null) {
/* 466 */         MulticastSubscription[] arrayOfMulticastSubscription = (MulticastSubscription[])subs.get();
/* 467 */         int n = arrayOfMulticastSubscription.length;
/*     */         
/* 469 */         if (n != 0) {
/* 470 */           long r = -1L;
/*     */           
/* 472 */           for (MulticastSubscription<T> a : arrayOfMulticastSubscription) {
/* 473 */             long ra = a.get();
/* 474 */             if (ra >= 0L) {
/* 475 */               if (r == -1L) {
/* 476 */                 r = ra - a.emitted;
/*     */               } else {
/* 478 */                 r = Math.min(r, ra - a.emitted);
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 483 */           while (r > 0L) {
/* 484 */             T v; MulticastSubscription[] arrayOfMulticastSubscription1 = (MulticastSubscription[])subs.get();
/*     */             
/* 486 */             if (arrayOfMulticastSubscription1 == TERMINATED) {
/* 487 */               q.clear();
/*     */               
/*     */               return;
/*     */             } 
/* 491 */             if (arrayOfMulticastSubscription != arrayOfMulticastSubscription1) {
/*     */               continue label98;
/*     */             }
/*     */             
/* 495 */             boolean d = this.done;
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 500 */               v = (T)q.poll();
/* 501 */             } catch (Throwable ex) {
/* 502 */               Exceptions.throwIfFatal(ex);
/* 503 */               SubscriptionHelper.cancel(this.upstream);
/* 504 */               d = true;
/* 505 */               v = null;
/* 506 */               this.error = ex;
/* 507 */               this.done = true;
/*     */             } 
/* 509 */             boolean empty = (v == null);
/*     */             
/* 511 */             if (d && empty) {
/* 512 */               Throwable ex = this.error;
/* 513 */               if (ex != null) {
/* 514 */                 for (MulticastSubscription<T> inner : (MulticastSubscription[])subs.getAndSet(TERMINATED)) {
/* 515 */                   inner.onError(ex);
/*     */                 }
/*     */               } else {
/* 518 */                 for (MulticastSubscription<T> inner : (MulticastSubscription[])subs.getAndSet(TERMINATED)) {
/* 519 */                   inner.onComplete();
/*     */                 }
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/* 525 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 529 */             for (MulticastSubscription<T> inner : arrayOfMulticastSubscription) {
/* 530 */               inner.onNext(v);
/*     */             }
/*     */             
/* 533 */             r--;
/*     */             
/* 535 */             if (fm != 1 && 
/* 536 */               ++c == lim) {
/* 537 */               c = 0;
/* 538 */               ((Subscription)this.upstream.get()).request(lim);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 543 */           if (r == 0L) {
/* 544 */             MulticastSubscription[] arrayOfMulticastSubscription1 = (MulticastSubscription[])subs.get();
/*     */             
/* 546 */             if (arrayOfMulticastSubscription1 == TERMINATED) {
/* 547 */               q.clear();
/*     */               
/*     */               return;
/*     */             } 
/* 551 */             if (arrayOfMulticastSubscription != arrayOfMulticastSubscription1) {
/*     */               continue;
/*     */             }
/*     */             
/* 555 */             if (this.done && q.isEmpty()) {
/* 556 */               Throwable ex = this.error;
/* 557 */               if (ex != null) {
/* 558 */                 for (MulticastSubscription<T> inner : (MulticastSubscription[])subs.getAndSet(TERMINATED)) {
/* 559 */                   inner.onError(ex);
/*     */                 }
/*     */               } else {
/* 562 */                 for (MulticastSubscription<T> inner : (MulticastSubscription[])subs.getAndSet(TERMINATED)) {
/* 563 */                   inner.onComplete();
/*     */                 }
/*     */               } 
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 572 */       this.consumed = c;
/* 573 */       missed = this.wip.addAndGet(-missed);
/* 574 */       if (missed == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MulticastSubscription<T>
/*     */     extends AtomicLong
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -363282618957264509L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final MulticastProcessor<T> parent;
/*     */     long emitted;
/*     */     
/*     */     MulticastSubscription(Subscriber<? super T> actual, MulticastProcessor<T> parent) {
/* 591 */       this.downstream = actual;
/* 592 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 597 */       if (SubscriptionHelper.validate(n)) {
/*     */         while (true) {
/* 599 */           long r = get();
/* 600 */           if (r == Long.MIN_VALUE || r == Long.MAX_VALUE) {
/*     */             break;
/*     */           }
/* 603 */           long u = r + n;
/* 604 */           if (u < 0L) {
/* 605 */             u = Long.MAX_VALUE;
/*     */           }
/* 607 */           if (compareAndSet(r, u)) {
/* 608 */             this.parent.drain();
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 617 */       if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
/* 618 */         this.parent.remove(this);
/*     */       }
/*     */     }
/*     */     
/*     */     void onNext(T t) {
/* 623 */       if (get() != Long.MIN_VALUE) {
/* 624 */         this.emitted++;
/* 625 */         this.downstream.onNext(t);
/*     */       } 
/*     */     }
/*     */     
/*     */     void onError(Throwable t) {
/* 630 */       if (get() != Long.MIN_VALUE) {
/* 631 */         this.downstream.onError(t);
/*     */       }
/*     */     }
/*     */     
/*     */     void onComplete() {
/* 636 */       if (get() != Long.MIN_VALUE)
/* 637 */         this.downstream.onComplete(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/MulticastProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */