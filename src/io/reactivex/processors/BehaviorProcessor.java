/*     */ package io.reactivex.processors;
/*     */ 
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ public final class BehaviorProcessor<T>
/*     */   extends FlowableProcessor<T>
/*     */ {
/*     */   final AtomicReference<BehaviorSubscription<T>[]> subscribers;
/* 168 */   static final Object[] EMPTY_ARRAY = new Object[0];
/*     */ 
/*     */   
/* 171 */   static final BehaviorSubscription[] EMPTY = new BehaviorSubscription[0];
/*     */ 
/*     */   
/* 174 */   static final BehaviorSubscription[] TERMINATED = new BehaviorSubscription[0];
/*     */ 
/*     */   
/*     */   final ReadWriteLock lock;
/*     */ 
/*     */   
/*     */   final Lock readLock;
/*     */ 
/*     */   
/*     */   final Lock writeLock;
/*     */ 
/*     */   
/*     */   final AtomicReference<Object> value;
/*     */   
/*     */   final AtomicReference<Throwable> terminalEvent;
/*     */   
/*     */   long index;
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> BehaviorProcessor<T> create() {
/* 196 */     return new BehaviorProcessor<T>();
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
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> BehaviorProcessor<T> createDefault(T defaultValue) {
/* 213 */     ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
/* 214 */     return new BehaviorProcessor<T>(defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BehaviorProcessor() {
/* 223 */     this.value = new AtomicReference();
/* 224 */     this.lock = new ReentrantReadWriteLock();
/* 225 */     this.readLock = this.lock.readLock();
/* 226 */     this.writeLock = this.lock.writeLock();
/* 227 */     this.subscribers = new AtomicReference(EMPTY);
/* 228 */     this.terminalEvent = new AtomicReference<Throwable>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BehaviorProcessor(T defaultValue) {
/* 238 */     this();
/* 239 */     this.value.lazySet(ObjectHelper.requireNonNull(defaultValue, "defaultValue is null"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/* 244 */     BehaviorSubscription<T> bs = new BehaviorSubscription<T>(s, this);
/* 245 */     s.onSubscribe(bs);
/* 246 */     if (add(bs)) {
/* 247 */       if (bs.cancelled) {
/* 248 */         remove(bs);
/*     */       } else {
/* 250 */         bs.emitFirst();
/*     */       } 
/*     */     } else {
/* 253 */       Throwable ex = this.terminalEvent.get();
/* 254 */       if (ex == ExceptionHelper.TERMINATED) {
/* 255 */         s.onComplete();
/*     */       } else {
/* 257 */         s.onError(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 264 */     if (this.terminalEvent.get() != null) {
/* 265 */       s.cancel();
/*     */       return;
/*     */     } 
/* 268 */     s.request(Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 273 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */     
/* 275 */     if (this.terminalEvent.get() != null) {
/*     */       return;
/*     */     }
/* 278 */     Object o = NotificationLite.next(t);
/* 279 */     setCurrent(o);
/* 280 */     for (BehaviorSubscription<T> bs : (BehaviorSubscription[])this.subscribers.get()) {
/* 281 */       bs.emitNext(o, this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 287 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 288 */     if (!this.terminalEvent.compareAndSet(null, t)) {
/* 289 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 292 */     Object o = NotificationLite.error(t);
/* 293 */     for (BehaviorSubscription<T> bs : terminate(o)) {
/* 294 */       bs.emitNext(o, this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 300 */     if (!this.terminalEvent.compareAndSet(null, ExceptionHelper.TERMINATED)) {
/*     */       return;
/*     */     }
/* 303 */     Object o = NotificationLite.complete();
/* 304 */     for (BehaviorSubscription<T> bs : terminate(o)) {
/* 305 */       bs.emitNext(o, this.index);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(T t) {
/* 324 */     if (t == null) {
/* 325 */       onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/* 326 */       return true;
/*     */     } 
/* 328 */     BehaviorSubscription[] arrayOfBehaviorSubscription = (BehaviorSubscription[])this.subscribers.get();
/*     */     
/* 330 */     for (BehaviorSubscription<T> s : arrayOfBehaviorSubscription) {
/* 331 */       if (s.isFull()) {
/* 332 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 336 */     Object o = NotificationLite.next(t);
/* 337 */     setCurrent(o);
/* 338 */     for (BehaviorSubscription<T> bs : arrayOfBehaviorSubscription) {
/* 339 */       bs.emitNext(o, this.index);
/*     */     }
/* 341 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSubscribers() {
/* 346 */     return (((BehaviorSubscription[])this.subscribers.get()).length != 0);
/*     */   }
/*     */   
/*     */   int subscriberCount() {
/* 350 */     return ((BehaviorSubscription[])this.subscribers.get()).length;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 356 */     Object o = this.value.get();
/* 357 */     if (NotificationLite.isError(o)) {
/* 358 */       return NotificationLite.getError(o);
/*     */     }
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getValue() {
/* 370 */     Object o = this.value.get();
/* 371 */     if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/* 372 */       return null;
/*     */     }
/* 374 */     return (T)NotificationLite.getValue(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Object[] getValues() {
/* 386 */     T[] a = (T[])EMPTY_ARRAY;
/* 387 */     T[] b = getValues(a);
/* 388 */     if (b == EMPTY_ARRAY) {
/* 389 */       return new Object[0];
/*     */     }
/* 391 */     return (Object[])b;
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public T[] getValues(T[] array) {
/* 407 */     Object o = this.value.get();
/* 408 */     if (o == null || NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/* 409 */       if (array.length != 0) {
/* 410 */         array[0] = null;
/*     */       }
/* 412 */       return array;
/*     */     } 
/* 414 */     T v = (T)NotificationLite.getValue(o);
/* 415 */     if (array.length != 0) {
/* 416 */       array[0] = v;
/* 417 */       if (array.length != 1) {
/* 418 */         array[1] = null;
/*     */       }
/*     */     } else {
/* 421 */       array = (T[])Array.newInstance(array.getClass().getComponentType(), 1);
/* 422 */       array[0] = v;
/*     */     } 
/* 424 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 429 */     Object o = this.value.get();
/* 430 */     return NotificationLite.isComplete(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 435 */     Object o = this.value.get();
/* 436 */     return NotificationLite.isError(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasValue() {
/* 445 */     Object o = this.value.get();
/* 446 */     return (o != null && !NotificationLite.isComplete(o) && !NotificationLite.isError(o));
/*     */   }
/*     */   
/*     */   boolean add(BehaviorSubscription<T> rs) {
/*     */     while (true) {
/* 451 */       BehaviorSubscription[] arrayOfBehaviorSubscription1 = (BehaviorSubscription[])this.subscribers.get();
/* 452 */       if (arrayOfBehaviorSubscription1 == TERMINATED) {
/* 453 */         return false;
/*     */       }
/* 455 */       int len = arrayOfBehaviorSubscription1.length;
/*     */       
/* 457 */       BehaviorSubscription[] arrayOfBehaviorSubscription2 = new BehaviorSubscription[len + 1];
/* 458 */       System.arraycopy(arrayOfBehaviorSubscription1, 0, arrayOfBehaviorSubscription2, 0, len);
/* 459 */       arrayOfBehaviorSubscription2[len] = rs;
/* 460 */       if (this.subscribers.compareAndSet(arrayOfBehaviorSubscription1, arrayOfBehaviorSubscription2))
/* 461 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void remove(BehaviorSubscription<T> rs) {
/*     */     BehaviorSubscription[] arrayOfBehaviorSubscription1;
/*     */     BehaviorSubscription[] arrayOfBehaviorSubscription2;
/*     */     do {
/* 469 */       arrayOfBehaviorSubscription1 = (BehaviorSubscription[])this.subscribers.get();
/* 470 */       int len = arrayOfBehaviorSubscription1.length;
/* 471 */       if (len == 0) {
/*     */         return;
/*     */       }
/* 474 */       int j = -1;
/* 475 */       for (int i = 0; i < len; i++) {
/* 476 */         if (arrayOfBehaviorSubscription1[i] == rs) {
/* 477 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 482 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */       
/* 486 */       if (len == 1) {
/* 487 */         arrayOfBehaviorSubscription2 = EMPTY;
/*     */       } else {
/* 489 */         arrayOfBehaviorSubscription2 = new BehaviorSubscription[len - 1];
/* 490 */         System.arraycopy(arrayOfBehaviorSubscription1, 0, arrayOfBehaviorSubscription2, 0, j);
/* 491 */         System.arraycopy(arrayOfBehaviorSubscription1, j + 1, arrayOfBehaviorSubscription2, j, len - j - 1);
/*     */       } 
/* 493 */     } while (!this.subscribers.compareAndSet(arrayOfBehaviorSubscription1, arrayOfBehaviorSubscription2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BehaviorSubscription<T>[] terminate(Object terminalValue) {
/* 502 */     BehaviorSubscription[] arrayOfBehaviorSubscription = (BehaviorSubscription[])this.subscribers.get();
/* 503 */     if (arrayOfBehaviorSubscription != TERMINATED) {
/* 504 */       arrayOfBehaviorSubscription = (BehaviorSubscription[])this.subscribers.getAndSet(TERMINATED);
/* 505 */       if (arrayOfBehaviorSubscription != TERMINATED)
/*     */       {
/* 507 */         setCurrent(terminalValue);
/*     */       }
/*     */     } 
/*     */     
/* 511 */     return (BehaviorSubscription<T>[])arrayOfBehaviorSubscription;
/*     */   }
/*     */   
/*     */   void setCurrent(Object o) {
/* 515 */     Lock wl = this.writeLock;
/* 516 */     wl.lock();
/* 517 */     this.index++;
/* 518 */     this.value.lazySet(o);
/* 519 */     wl.unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BehaviorSubscription<T>
/*     */     extends AtomicLong
/*     */     implements Subscription, AppendOnlyLinkedArrayList.NonThrowingPredicate<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 3293175281126227086L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final BehaviorProcessor<T> state;
/*     */     boolean next;
/*     */     boolean emitting;
/*     */     AppendOnlyLinkedArrayList<Object> queue;
/*     */     boolean fastPath;
/*     */     volatile boolean cancelled;
/*     */     long index;
/*     */     
/*     */     BehaviorSubscription(Subscriber<? super T> actual, BehaviorProcessor<T> state) {
/* 540 */       this.downstream = actual;
/* 541 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 546 */       if (SubscriptionHelper.validate(n)) {
/* 547 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 553 */       if (!this.cancelled) {
/* 554 */         this.cancelled = true;
/*     */         
/* 556 */         this.state.remove(this);
/*     */       } 
/*     */     }
/*     */     void emitFirst() {
/*     */       Object o;
/* 561 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/*     */       
/* 565 */       synchronized (this) {
/* 566 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 569 */         if (this.next) {
/*     */           return;
/*     */         }
/*     */         
/* 573 */         BehaviorProcessor<T> s = this.state;
/*     */         
/* 575 */         Lock readLock = s.readLock;
/* 576 */         readLock.lock();
/* 577 */         this.index = s.index;
/* 578 */         o = s.value.get();
/* 579 */         readLock.unlock();
/*     */         
/* 581 */         this.emitting = (o != null);
/* 582 */         this.next = true;
/*     */       } 
/*     */       
/* 585 */       if (o != null) {
/* 586 */         if (test(o)) {
/*     */           return;
/*     */         }
/*     */         
/* 590 */         emitLoop();
/*     */       } 
/*     */     }
/*     */     
/*     */     void emitNext(Object value, long stateIndex) {
/* 595 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 598 */       if (!this.fastPath) {
/* 599 */         synchronized (this) {
/* 600 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/* 603 */           if (this.index == stateIndex) {
/*     */             return;
/*     */           }
/* 606 */           if (this.emitting) {
/* 607 */             AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 608 */             if (q == null) {
/* 609 */               q = new AppendOnlyLinkedArrayList(4);
/* 610 */               this.queue = q;
/*     */             } 
/* 612 */             q.add(value);
/*     */             return;
/*     */           } 
/* 615 */           this.next = true;
/*     */         } 
/* 617 */         this.fastPath = true;
/*     */       } 
/*     */       
/* 620 */       test(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Object o) {
/* 625 */       if (this.cancelled) {
/* 626 */         return true;
/*     */       }
/*     */       
/* 629 */       if (NotificationLite.isComplete(o)) {
/* 630 */         this.downstream.onComplete();
/* 631 */         return true;
/*     */       } 
/* 633 */       if (NotificationLite.isError(o)) {
/* 634 */         this.downstream.onError(NotificationLite.getError(o));
/* 635 */         return true;
/*     */       } 
/*     */       
/* 638 */       long r = get();
/* 639 */       if (r != 0L) {
/* 640 */         this.downstream.onNext(NotificationLite.getValue(o));
/* 641 */         if (r != Long.MAX_VALUE) {
/* 642 */           decrementAndGet();
/*     */         }
/* 644 */         return false;
/*     */       } 
/* 646 */       cancel();
/* 647 */       this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver value due to lack of requests"));
/* 648 */       return true;
/*     */     }
/*     */     void emitLoop() {
/*     */       while (true) {
/*     */         AppendOnlyLinkedArrayList<Object> q;
/* 653 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 657 */         synchronized (this) {
/* 658 */           q = this.queue;
/* 659 */           if (q == null) {
/* 660 */             this.emitting = false;
/*     */             return;
/*     */           } 
/* 663 */           this.queue = null;
/*     */         } 
/*     */         
/* 666 */         q.forEachWhile(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isFull() {
/* 671 */       return (get() == 0L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/BehaviorProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */