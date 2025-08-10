/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BehaviorSubject<T>
/*     */   extends Subject<T>
/*     */ {
/* 157 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */ 
/*     */   
/*     */   final AtomicReference<Object> value;
/*     */   
/*     */   final AtomicReference<BehaviorDisposable<T>[]> subscribers;
/*     */   
/* 164 */   static final BehaviorDisposable[] EMPTY = new BehaviorDisposable[0];
/*     */ 
/*     */   
/* 167 */   static final BehaviorDisposable[] TERMINATED = new BehaviorDisposable[0];
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
/*     */   final AtomicReference<Throwable> terminalEvent;
/*     */   
/*     */   long index;
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> BehaviorSubject<T> create() {
/* 186 */     return new BehaviorSubject<T>();
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
/*     */   public static <T> BehaviorSubject<T> createDefault(T defaultValue) {
/* 203 */     return new BehaviorSubject<T>(defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BehaviorSubject() {
/* 212 */     this.lock = new ReentrantReadWriteLock();
/* 213 */     this.readLock = this.lock.readLock();
/* 214 */     this.writeLock = this.lock.writeLock();
/* 215 */     this.subscribers = new AtomicReference(EMPTY);
/* 216 */     this.value = new AtomicReference();
/* 217 */     this.terminalEvent = new AtomicReference<Throwable>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BehaviorSubject(T defaultValue) {
/* 227 */     this();
/* 228 */     this.value.lazySet(ObjectHelper.requireNonNull(defaultValue, "defaultValue is null"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/* 233 */     BehaviorDisposable<T> bs = new BehaviorDisposable<T>(observer, this);
/* 234 */     observer.onSubscribe(bs);
/* 235 */     if (add(bs)) {
/* 236 */       if (bs.cancelled) {
/* 237 */         remove(bs);
/*     */       } else {
/* 239 */         bs.emitFirst();
/*     */       } 
/*     */     } else {
/* 242 */       Throwable ex = this.terminalEvent.get();
/* 243 */       if (ex == ExceptionHelper.TERMINATED) {
/* 244 */         observer.onComplete();
/*     */       } else {
/* 246 */         observer.onError(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 253 */     if (this.terminalEvent.get() != null) {
/* 254 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 260 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */     
/* 262 */     if (this.terminalEvent.get() != null) {
/*     */       return;
/*     */     }
/* 265 */     Object o = NotificationLite.next(t);
/* 266 */     setCurrent(o);
/* 267 */     for (BehaviorDisposable<T> bs : (BehaviorDisposable[])this.subscribers.get()) {
/* 268 */       bs.emitNext(o, this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 274 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 275 */     if (!this.terminalEvent.compareAndSet(null, t)) {
/* 276 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 279 */     Object o = NotificationLite.error(t);
/* 280 */     for (BehaviorDisposable<T> bs : terminate(o)) {
/* 281 */       bs.emitNext(o, this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 287 */     if (!this.terminalEvent.compareAndSet(null, ExceptionHelper.TERMINATED)) {
/*     */       return;
/*     */     }
/* 290 */     Object o = NotificationLite.complete();
/* 291 */     for (BehaviorDisposable<T> bs : terminate(o)) {
/* 292 */       bs.emitNext(o, this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 298 */     return (((BehaviorDisposable[])this.subscribers.get()).length != 0);
/*     */   }
/*     */   
/*     */   int subscriberCount() {
/* 302 */     return ((BehaviorDisposable[])this.subscribers.get()).length;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 308 */     Object o = this.value.get();
/* 309 */     if (NotificationLite.isError(o)) {
/* 310 */       return NotificationLite.getError(o);
/*     */     }
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getValue() {
/* 322 */     Object o = this.value.get();
/* 323 */     if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/* 324 */       return null;
/*     */     }
/* 326 */     return (T)NotificationLite.getValue(o);
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
/* 338 */     T[] a = (T[])EMPTY_ARRAY;
/* 339 */     T[] b = getValues(a);
/* 340 */     if (b == EMPTY_ARRAY) {
/* 341 */       return new Object[0];
/*     */     }
/* 343 */     return (Object[])b;
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
/* 359 */     Object o = this.value.get();
/* 360 */     if (o == null || NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/* 361 */       if (array.length != 0) {
/* 362 */         array[0] = null;
/*     */       }
/* 364 */       return array;
/*     */     } 
/* 366 */     T v = (T)NotificationLite.getValue(o);
/* 367 */     if (array.length != 0) {
/* 368 */       array[0] = v;
/* 369 */       if (array.length != 1) {
/* 370 */         array[1] = null;
/*     */       }
/*     */     } else {
/* 373 */       array = (T[])Array.newInstance(array.getClass().getComponentType(), 1);
/* 374 */       array[0] = v;
/*     */     } 
/* 376 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 381 */     Object o = this.value.get();
/* 382 */     return NotificationLite.isComplete(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 387 */     Object o = this.value.get();
/* 388 */     return NotificationLite.isError(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasValue() {
/* 397 */     Object o = this.value.get();
/* 398 */     return (o != null && !NotificationLite.isComplete(o) && !NotificationLite.isError(o));
/*     */   }
/*     */   
/*     */   boolean add(BehaviorDisposable<T> rs) {
/*     */     while (true) {
/* 403 */       BehaviorDisposable[] arrayOfBehaviorDisposable1 = (BehaviorDisposable[])this.subscribers.get();
/* 404 */       if (arrayOfBehaviorDisposable1 == TERMINATED) {
/* 405 */         return false;
/*     */       }
/* 407 */       int len = arrayOfBehaviorDisposable1.length;
/*     */       
/* 409 */       BehaviorDisposable[] arrayOfBehaviorDisposable2 = new BehaviorDisposable[len + 1];
/* 410 */       System.arraycopy(arrayOfBehaviorDisposable1, 0, arrayOfBehaviorDisposable2, 0, len);
/* 411 */       arrayOfBehaviorDisposable2[len] = rs;
/* 412 */       if (this.subscribers.compareAndSet(arrayOfBehaviorDisposable1, arrayOfBehaviorDisposable2))
/* 413 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void remove(BehaviorDisposable<T> rs) {
/*     */     BehaviorDisposable[] arrayOfBehaviorDisposable1;
/*     */     BehaviorDisposable[] arrayOfBehaviorDisposable2;
/*     */     do {
/* 421 */       arrayOfBehaviorDisposable1 = (BehaviorDisposable[])this.subscribers.get();
/* 422 */       int len = arrayOfBehaviorDisposable1.length;
/* 423 */       if (len == 0) {
/*     */         return;
/*     */       }
/* 426 */       int j = -1;
/* 427 */       for (int i = 0; i < len; i++) {
/* 428 */         if (arrayOfBehaviorDisposable1[i] == rs) {
/* 429 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 434 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */       
/* 438 */       if (len == 1) {
/* 439 */         arrayOfBehaviorDisposable2 = EMPTY;
/*     */       } else {
/* 441 */         arrayOfBehaviorDisposable2 = new BehaviorDisposable[len - 1];
/* 442 */         System.arraycopy(arrayOfBehaviorDisposable1, 0, arrayOfBehaviorDisposable2, 0, j);
/* 443 */         System.arraycopy(arrayOfBehaviorDisposable1, j + 1, arrayOfBehaviorDisposable2, j, len - j - 1);
/*     */       } 
/* 445 */     } while (!this.subscribers.compareAndSet(arrayOfBehaviorDisposable1, arrayOfBehaviorDisposable2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BehaviorDisposable<T>[] terminate(Object terminalValue) {
/* 454 */     BehaviorDisposable[] arrayOfBehaviorDisposable = (BehaviorDisposable[])this.subscribers.getAndSet(TERMINATED);
/* 455 */     if (arrayOfBehaviorDisposable != TERMINATED)
/*     */     {
/* 457 */       setCurrent(terminalValue);
/*     */     }
/*     */     
/* 460 */     return (BehaviorDisposable<T>[])arrayOfBehaviorDisposable;
/*     */   }
/*     */   
/*     */   void setCurrent(Object o) {
/* 464 */     this.writeLock.lock();
/* 465 */     this.index++;
/* 466 */     this.value.lazySet(o);
/* 467 */     this.writeLock.unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BehaviorDisposable<T>
/*     */     implements Disposable, AppendOnlyLinkedArrayList.NonThrowingPredicate<Object>
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final BehaviorSubject<T> state;
/*     */     
/*     */     boolean next;
/*     */     boolean emitting;
/*     */     AppendOnlyLinkedArrayList<Object> queue;
/*     */     boolean fastPath;
/*     */     volatile boolean cancelled;
/*     */     long index;
/*     */     
/*     */     BehaviorDisposable(Observer<? super T> actual, BehaviorSubject<T> state) {
/* 486 */       this.downstream = actual;
/* 487 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 492 */       if (!this.cancelled) {
/* 493 */         this.cancelled = true;
/*     */         
/* 495 */         this.state.remove(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 501 */       return this.cancelled;
/*     */     }
/*     */     void emitFirst() {
/*     */       Object o;
/* 505 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/*     */       
/* 509 */       synchronized (this) {
/* 510 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 513 */         if (this.next) {
/*     */           return;
/*     */         }
/*     */         
/* 517 */         BehaviorSubject<T> s = this.state;
/* 518 */         Lock lock = s.readLock;
/*     */         
/* 520 */         lock.lock();
/* 521 */         this.index = s.index;
/* 522 */         o = s.value.get();
/* 523 */         lock.unlock();
/*     */         
/* 525 */         this.emitting = (o != null);
/* 526 */         this.next = true;
/*     */       } 
/*     */       
/* 529 */       if (o != null) {
/* 530 */         if (test(o)) {
/*     */           return;
/*     */         }
/*     */         
/* 534 */         emitLoop();
/*     */       } 
/*     */     }
/*     */     
/*     */     void emitNext(Object value, long stateIndex) {
/* 539 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 542 */       if (!this.fastPath) {
/* 543 */         synchronized (this) {
/* 544 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/* 547 */           if (this.index == stateIndex) {
/*     */             return;
/*     */           }
/* 550 */           if (this.emitting) {
/* 551 */             AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 552 */             if (q == null) {
/* 553 */               q = new AppendOnlyLinkedArrayList(4);
/* 554 */               this.queue = q;
/*     */             } 
/* 556 */             q.add(value);
/*     */             return;
/*     */           } 
/* 559 */           this.next = true;
/*     */         } 
/* 561 */         this.fastPath = true;
/*     */       } 
/*     */       
/* 564 */       test(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(Object o) {
/* 569 */       return (this.cancelled || NotificationLite.accept(o, this.downstream));
/*     */     }
/*     */     void emitLoop() {
/*     */       while (true) {
/*     */         AppendOnlyLinkedArrayList<Object> q;
/* 574 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 578 */         synchronized (this) {
/* 579 */           q = this.queue;
/* 580 */           if (q == null) {
/* 581 */             this.emitting = false;
/*     */             return;
/*     */           } 
/* 584 */           this.queue = null;
/*     */         } 
/*     */         
/* 587 */         q.forEachWhile(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/BehaviorSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */