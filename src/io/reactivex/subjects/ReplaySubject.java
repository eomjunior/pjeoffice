/*      */ package io.reactivex.subjects;
/*      */ 
/*      */ import io.reactivex.Observer;
/*      */ import io.reactivex.Scheduler;
/*      */ import io.reactivex.annotations.CheckReturnValue;
/*      */ import io.reactivex.annotations.NonNull;
/*      */ import io.reactivex.annotations.Nullable;
/*      */ import io.reactivex.disposables.Disposable;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.util.NotificationLite;
/*      */ import io.reactivex.plugins.RxJavaPlugins;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ReplaySubject<T>
/*      */   extends Subject<T>
/*      */ {
/*      */   final ReplayBuffer<T> buffer;
/*      */   final AtomicReference<ReplayDisposable<T>[]> observers;
/*  140 */   static final ReplayDisposable[] EMPTY = new ReplayDisposable[0];
/*      */ 
/*      */   
/*  143 */   static final ReplayDisposable[] TERMINATED = new ReplayDisposable[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean done;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   @NonNull
/*      */   public static <T> ReplaySubject<T> create() {
/*  163 */     return new ReplaySubject<T>(new UnboundedReplayBuffer<T>(16));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   @NonNull
/*      */   public static <T> ReplaySubject<T> create(int capacityHint) {
/*  184 */     return new ReplaySubject<T>(new UnboundedReplayBuffer<T>(capacityHint));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   @NonNull
/*      */   public static <T> ReplaySubject<T> createWithSize(int maxSize) {
/*  210 */     return new ReplaySubject<T>(new SizeBoundReplayBuffer<T>(maxSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> ReplaySubject<T> createUnbounded() {
/*  227 */     return new ReplaySubject<T>(new SizeBoundReplayBuffer<T>(2147483647));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   @NonNull
/*      */   public static <T> ReplaySubject<T> createWithTime(long maxAge, TimeUnit unit, Scheduler scheduler) {
/*  265 */     return new ReplaySubject<T>(new SizeAndTimeBoundReplayBuffer<T>(2147483647, maxAge, unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   @NonNull
/*      */   public static <T> ReplaySubject<T> createWithTimeAndSize(long maxAge, TimeUnit unit, Scheduler scheduler, int maxSize) {
/*  305 */     return new ReplaySubject<T>(new SizeAndTimeBoundReplayBuffer<T>(maxSize, maxAge, unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ReplaySubject(ReplayBuffer<T> buffer) {
/*  314 */     this.buffer = buffer;
/*  315 */     this.observers = new AtomicReference(EMPTY);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void subscribeActual(Observer<? super T> observer) {
/*  320 */     ReplayDisposable<T> rs = new ReplayDisposable<T>(observer, this);
/*  321 */     observer.onSubscribe(rs);
/*      */     
/*  323 */     if (!rs.cancelled) {
/*  324 */       if (add(rs) && 
/*  325 */         rs.cancelled) {
/*  326 */         remove(rs);
/*      */         
/*      */         return;
/*      */       } 
/*  330 */       this.buffer.replay(rs);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onSubscribe(Disposable d) {
/*  336 */     if (this.done) {
/*  337 */       d.dispose();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onNext(T t) {
/*  343 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/*  344 */     if (this.done) {
/*      */       return;
/*      */     }
/*      */     
/*  348 */     ReplayBuffer<T> b = this.buffer;
/*  349 */     b.add(t);
/*      */     
/*  351 */     for (ReplayDisposable<T> rs : (ReplayDisposable[])this.observers.get()) {
/*  352 */       b.replay(rs);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onError(Throwable t) {
/*  358 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*  359 */     if (this.done) {
/*  360 */       RxJavaPlugins.onError(t);
/*      */       return;
/*      */     } 
/*  363 */     this.done = true;
/*      */     
/*  365 */     Object o = NotificationLite.error(t);
/*      */     
/*  367 */     ReplayBuffer<T> b = this.buffer;
/*      */     
/*  369 */     b.addFinal(o);
/*      */     
/*  371 */     for (ReplayDisposable<T> rs : terminate(o)) {
/*  372 */       b.replay(rs);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onComplete() {
/*  378 */     if (this.done) {
/*      */       return;
/*      */     }
/*  381 */     this.done = true;
/*      */     
/*  383 */     Object o = NotificationLite.complete();
/*      */     
/*  385 */     ReplayBuffer<T> b = this.buffer;
/*      */     
/*  387 */     b.addFinal(o);
/*      */     
/*  389 */     for (ReplayDisposable<T> rs : terminate(o)) {
/*  390 */       b.replay(rs);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasObservers() {
/*  396 */     return (((ReplayDisposable[])this.observers.get()).length != 0);
/*      */   }
/*      */   
/*      */   int observerCount() {
/*  400 */     return ((ReplayDisposable[])this.observers.get()).length;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Throwable getThrowable() {
/*  406 */     Object o = this.buffer.get();
/*  407 */     if (NotificationLite.isError(o)) {
/*  408 */       return NotificationLite.getError(o);
/*      */     }
/*  410 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public T getValue() {
/*  420 */     return this.buffer.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void cleanupBuffer() {
/*  438 */     this.buffer.trimHead();
/*      */   }
/*      */ 
/*      */   
/*  442 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] getValues() {
/*  451 */     T[] a = (T[])EMPTY_ARRAY;
/*  452 */     T[] b = getValues(a);
/*  453 */     if (b == EMPTY_ARRAY) {
/*  454 */       return new Object[0];
/*      */     }
/*  456 */     return (Object[])b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public T[] getValues(T[] array) {
/*  469 */     return this.buffer.getValues(array);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasComplete() {
/*  474 */     Object o = this.buffer.get();
/*  475 */     return NotificationLite.isComplete(o);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasThrowable() {
/*  480 */     Object o = this.buffer.get();
/*  481 */     return NotificationLite.isError(o);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasValue() {
/*  490 */     return (this.buffer.size() != 0);
/*      */   }
/*      */   
/*      */   int size() {
/*  494 */     return this.buffer.size();
/*      */   }
/*      */   
/*      */   boolean add(ReplayDisposable<T> rs) {
/*      */     while (true) {
/*  499 */       ReplayDisposable[] arrayOfReplayDisposable1 = (ReplayDisposable[])this.observers.get();
/*  500 */       if (arrayOfReplayDisposable1 == TERMINATED) {
/*  501 */         return false;
/*      */       }
/*  503 */       int len = arrayOfReplayDisposable1.length;
/*      */       
/*  505 */       ReplayDisposable[] arrayOfReplayDisposable2 = new ReplayDisposable[len + 1];
/*  506 */       System.arraycopy(arrayOfReplayDisposable1, 0, arrayOfReplayDisposable2, 0, len);
/*  507 */       arrayOfReplayDisposable2[len] = rs;
/*  508 */       if (this.observers.compareAndSet(arrayOfReplayDisposable1, arrayOfReplayDisposable2))
/*  509 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   void remove(ReplayDisposable<T> rs) {
/*      */     ReplayDisposable[] arrayOfReplayDisposable1;
/*      */     ReplayDisposable[] arrayOfReplayDisposable2;
/*      */     do {
/*  517 */       arrayOfReplayDisposable1 = (ReplayDisposable[])this.observers.get();
/*  518 */       if (arrayOfReplayDisposable1 == TERMINATED || arrayOfReplayDisposable1 == EMPTY) {
/*      */         return;
/*      */       }
/*  521 */       int len = arrayOfReplayDisposable1.length;
/*  522 */       int j = -1;
/*  523 */       for (int i = 0; i < len; i++) {
/*  524 */         if (arrayOfReplayDisposable1[i] == rs) {
/*  525 */           j = i;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  530 */       if (j < 0) {
/*      */         return;
/*      */       }
/*      */       
/*  534 */       if (len == 1) {
/*  535 */         arrayOfReplayDisposable2 = EMPTY;
/*      */       } else {
/*  537 */         arrayOfReplayDisposable2 = new ReplayDisposable[len - 1];
/*  538 */         System.arraycopy(arrayOfReplayDisposable1, 0, arrayOfReplayDisposable2, 0, j);
/*  539 */         System.arraycopy(arrayOfReplayDisposable1, j + 1, arrayOfReplayDisposable2, j, len - j - 1);
/*      */       } 
/*  541 */     } while (!this.observers.compareAndSet(arrayOfReplayDisposable1, arrayOfReplayDisposable2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ReplayDisposable<T>[] terminate(Object terminalValue) {
/*  549 */     if (this.buffer.compareAndSet(null, terminalValue)) {
/*  550 */       return (ReplayDisposable<T>[])this.observers.getAndSet(TERMINATED);
/*      */     }
/*  552 */     return (ReplayDisposable<T>[])TERMINATED;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static interface ReplayBuffer<T>
/*      */   {
/*      */     void add(T param1T);
/*      */ 
/*      */ 
/*      */     
/*      */     void addFinal(Object param1Object);
/*      */ 
/*      */ 
/*      */     
/*      */     void replay(ReplaySubject.ReplayDisposable<T> param1ReplayDisposable);
/*      */ 
/*      */ 
/*      */     
/*      */     int size();
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     T getValue();
/*      */ 
/*      */     
/*      */     T[] getValues(T[] param1ArrayOfT);
/*      */ 
/*      */     
/*      */     Object get();
/*      */ 
/*      */     
/*      */     boolean compareAndSet(Object param1Object1, Object param1Object2);
/*      */ 
/*      */     
/*      */     void trimHead();
/*      */   }
/*      */ 
/*      */   
/*      */   static final class ReplayDisposable<T>
/*      */     extends AtomicInteger
/*      */     implements Disposable
/*      */   {
/*      */     private static final long serialVersionUID = 466549804534799122L;
/*      */     
/*      */     final Observer<? super T> downstream;
/*      */     
/*      */     final ReplaySubject<T> state;
/*      */     
/*      */     Object index;
/*      */     
/*      */     volatile boolean cancelled;
/*      */ 
/*      */     
/*      */     ReplayDisposable(Observer<? super T> actual, ReplaySubject<T> state) {
/*  608 */       this.downstream = actual;
/*  609 */       this.state = state;
/*      */     }
/*      */ 
/*      */     
/*      */     public void dispose() {
/*  614 */       if (!this.cancelled) {
/*  615 */         this.cancelled = true;
/*  616 */         this.state.remove(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDisposed() {
/*  622 */       return this.cancelled;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class UnboundedReplayBuffer<T>
/*      */     extends AtomicReference<Object>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = -733876083048047795L;
/*      */     
/*      */     final List<Object> buffer;
/*      */     
/*      */     volatile boolean done;
/*      */     volatile int size;
/*      */     
/*      */     UnboundedReplayBuffer(int capacityHint) {
/*  639 */       this.buffer = new ArrayList(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(T value) {
/*  644 */       this.buffer.add(value);
/*  645 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void addFinal(Object notificationLite) {
/*  650 */       this.buffer.add(notificationLite);
/*  651 */       trimHead();
/*  652 */       this.size++;
/*  653 */       this.done = true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void trimHead() {}
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public T getValue() {
/*  665 */       int s = this.size;
/*  666 */       if (s != 0) {
/*  667 */         List<Object> b = this.buffer;
/*  668 */         Object o = b.get(s - 1);
/*  669 */         if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/*  670 */           if (s == 1) {
/*  671 */             return null;
/*      */           }
/*  673 */           return (T)b.get(s - 2);
/*      */         } 
/*  675 */         return (T)o;
/*      */       } 
/*  677 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public T[] getValues(T[] array) {
/*  683 */       int s = this.size;
/*  684 */       if (s == 0) {
/*  685 */         if (array.length != 0) {
/*  686 */           array[0] = null;
/*      */         }
/*  688 */         return array;
/*      */       } 
/*  690 */       List<Object> b = this.buffer;
/*  691 */       Object o = b.get(s - 1);
/*      */ 
/*      */       
/*  694 */       s--;
/*  695 */       if ((NotificationLite.isComplete(o) || NotificationLite.isError(o)) && s == 0) {
/*  696 */         if (array.length != 0) {
/*  697 */           array[0] = null;
/*      */         }
/*  699 */         return array;
/*      */       } 
/*      */ 
/*      */       
/*  703 */       if (array.length < s) {
/*  704 */         array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
/*      */       }
/*  706 */       for (int i = 0; i < s; i++) {
/*  707 */         array[i] = (T)b.get(i);
/*      */       }
/*  709 */       if (array.length > s) {
/*  710 */         array[s] = null;
/*      */       }
/*      */       
/*  713 */       return array;
/*      */     }
/*      */ 
/*      */     
/*      */     public void replay(ReplaySubject.ReplayDisposable<T> rs) {
/*      */       int index;
/*  719 */       if (rs.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/*  723 */       int missed = 1;
/*  724 */       List<Object> b = this.buffer;
/*  725 */       Observer<? super T> a = rs.downstream;
/*      */       
/*  727 */       Integer indexObject = (Integer)rs.index;
/*      */       
/*  729 */       if (indexObject != null) {
/*  730 */         index = indexObject.intValue();
/*      */       } else {
/*  732 */         index = 0;
/*  733 */         rs.index = Integer.valueOf(0);
/*      */       } 
/*      */ 
/*      */       
/*      */       while (true) {
/*  738 */         if (rs.cancelled) {
/*  739 */           rs.index = null;
/*      */           
/*      */           return;
/*      */         } 
/*  743 */         int s = this.size;
/*      */         
/*  745 */         while (s != index) {
/*      */           
/*  747 */           if (rs.cancelled) {
/*  748 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/*  752 */           Object o = b.get(index);
/*      */           
/*  754 */           if (this.done && 
/*  755 */             index + 1 == s) {
/*  756 */             s = this.size;
/*  757 */             if (index + 1 == s) {
/*  758 */               if (NotificationLite.isComplete(o)) {
/*  759 */                 a.onComplete();
/*      */               } else {
/*  761 */                 a.onError(NotificationLite.getError(o));
/*      */               } 
/*  763 */               rs.index = null;
/*  764 */               rs.cancelled = true;
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */           
/*  770 */           a.onNext(o);
/*  771 */           index++;
/*      */         } 
/*      */         
/*  774 */         if (index != this.size) {
/*      */           continue;
/*      */         }
/*      */         
/*  778 */         rs.index = Integer.valueOf(index);
/*      */         
/*  780 */         missed = rs.addAndGet(-missed);
/*  781 */         if (missed == 0) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  789 */       int s = this.size;
/*  790 */       if (s != 0) {
/*  791 */         Object o = this.buffer.get(s - 1);
/*  792 */         if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/*  793 */           return s - 1;
/*      */         }
/*  795 */         return s;
/*      */       } 
/*  797 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Node<T>
/*      */     extends AtomicReference<Node<T>>
/*      */   {
/*      */     private static final long serialVersionUID = 6404226426336033100L;
/*      */     final T value;
/*      */     
/*      */     Node(T value) {
/*  808 */       this.value = value;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class TimedNode<T>
/*      */     extends AtomicReference<TimedNode<T>>
/*      */   {
/*      */     private static final long serialVersionUID = 6404226426336033100L;
/*      */     final T value;
/*      */     final long time;
/*      */     
/*      */     TimedNode(T value, long time) {
/*  820 */       this.value = value;
/*  821 */       this.time = time;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class SizeBoundReplayBuffer<T>
/*      */     extends AtomicReference<Object>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = 1107649250281456395L;
/*      */     
/*      */     final int maxSize;
/*      */     
/*      */     int size;
/*      */     
/*      */     volatile ReplaySubject.Node<Object> head;
/*      */     ReplaySubject.Node<Object> tail;
/*      */     volatile boolean done;
/*      */     
/*      */     SizeBoundReplayBuffer(int maxSize) {
/*  841 */       this.maxSize = ObjectHelper.verifyPositive(maxSize, "maxSize");
/*  842 */       ReplaySubject.Node<Object> h = new ReplaySubject.Node(null);
/*  843 */       this.tail = h;
/*  844 */       this.head = h;
/*      */     }
/*      */     
/*      */     void trim() {
/*  848 */       if (this.size > this.maxSize) {
/*  849 */         this.size--;
/*  850 */         ReplaySubject.Node<Object> h = this.head;
/*  851 */         this.head = h.get();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(T value) {
/*  857 */       ReplaySubject.Node<Object> n = new ReplaySubject.Node(value);
/*  858 */       ReplaySubject.Node<Object> t = this.tail;
/*      */       
/*  860 */       this.tail = n;
/*  861 */       this.size++;
/*  862 */       t.set(n);
/*      */       
/*  864 */       trim();
/*      */     }
/*      */ 
/*      */     
/*      */     public void addFinal(Object notificationLite) {
/*  869 */       ReplaySubject.Node<Object> n = new ReplaySubject.Node(notificationLite);
/*  870 */       ReplaySubject.Node<Object> t = this.tail;
/*      */       
/*  872 */       this.tail = n;
/*  873 */       this.size++;
/*  874 */       t.lazySet(n);
/*      */       
/*  876 */       trimHead();
/*  877 */       this.done = true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void trimHead() {
/*  886 */       ReplaySubject.Node<Object> h = this.head;
/*  887 */       if (h.value != null) {
/*  888 */         ReplaySubject.Node<Object> n = new ReplaySubject.Node(null);
/*  889 */         n.lazySet(h.get());
/*  890 */         this.head = n;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public T getValue() {
/*  898 */       ReplaySubject.Node<Object> prev = null;
/*  899 */       ReplaySubject.Node<Object> h = this.head;
/*      */       
/*      */       while (true) {
/*  902 */         ReplaySubject.Node<Object> next = h.get();
/*  903 */         if (next == null) {
/*      */           break;
/*      */         }
/*  906 */         prev = h;
/*  907 */         h = next;
/*      */       } 
/*      */       
/*  910 */       Object v = h.value;
/*  911 */       if (v == null) {
/*  912 */         return null;
/*      */       }
/*  914 */       if (NotificationLite.isComplete(v) || NotificationLite.isError(v)) {
/*  915 */         return prev.value;
/*      */       }
/*      */       
/*  918 */       return (T)v;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public T[] getValues(T[] array) {
/*  924 */       ReplaySubject.Node<Object> h = this.head;
/*  925 */       int s = size();
/*      */       
/*  927 */       if (s == 0) {
/*  928 */         if (array.length != 0) {
/*  929 */           array[0] = null;
/*      */         }
/*      */       } else {
/*  932 */         if (array.length < s) {
/*  933 */           array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
/*      */         }
/*      */         
/*  936 */         int i = 0;
/*  937 */         while (i != s) {
/*  938 */           ReplaySubject.Node<Object> next = h.get();
/*  939 */           array[i] = next.value;
/*  940 */           i++;
/*  941 */           h = next;
/*      */         } 
/*  943 */         if (array.length > s) {
/*  944 */           array[s] = null;
/*      */         }
/*      */       } 
/*      */       
/*  948 */       return array;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void replay(ReplaySubject.ReplayDisposable<T> rs) {
/*  954 */       if (rs.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/*  958 */       int missed = 1;
/*  959 */       Observer<? super T> a = rs.downstream;
/*      */       
/*  961 */       ReplaySubject.Node<Object> index = (ReplaySubject.Node<Object>)rs.index;
/*  962 */       if (index == null) {
/*  963 */         index = this.head;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/*  969 */         if (rs.cancelled) {
/*  970 */           rs.index = null;
/*      */           
/*      */           return;
/*      */         } 
/*  974 */         ReplaySubject.Node<Object> n = index.get();
/*      */         
/*  976 */         if (n == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1001 */           if (index.get() != null) {
/*      */             continue;
/*      */           }
/*      */           
/* 1005 */           rs.index = index;
/*      */           
/* 1007 */           missed = rs.addAndGet(-missed);
/* 1008 */           if (missed == 0)
/*      */             break;  continue;
/*      */         }  Object o = n.value; if (this.done && n.get() == null) { if (NotificationLite.isComplete(o)) {
/*      */             a.onComplete();
/*      */           } else {
/*      */             a.onError(NotificationLite.getError(o));
/*      */           }  rs.index = null; rs.cancelled = true; return; }
/*      */          a.onNext(o); index = n;
/* 1016 */       }  } public int size() { int s = 0;
/* 1017 */       ReplaySubject.Node<Object> h = this.head;
/* 1018 */       while (s != Integer.MAX_VALUE) {
/* 1019 */         ReplaySubject.Node<Object> next = h.get();
/* 1020 */         if (next == null) {
/* 1021 */           Object o = h.value;
/* 1022 */           if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/* 1023 */             s--;
/*      */           }
/*      */           break;
/*      */         } 
/* 1027 */         s++;
/* 1028 */         h = next;
/*      */       } 
/*      */       
/* 1031 */       return s; }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   static final class SizeAndTimeBoundReplayBuffer<T>
/*      */     extends AtomicReference<Object>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     private static final long serialVersionUID = -8056260896137901749L;
/*      */     
/*      */     final int maxSize;
/*      */     
/*      */     final long maxAge;
/*      */     
/*      */     final TimeUnit unit;
/*      */     final Scheduler scheduler;
/*      */     int size;
/*      */     volatile ReplaySubject.TimedNode<Object> head;
/*      */     ReplaySubject.TimedNode<Object> tail;
/*      */     volatile boolean done;
/*      */     
/*      */     SizeAndTimeBoundReplayBuffer(int maxSize, long maxAge, TimeUnit unit, Scheduler scheduler) {
/* 1054 */       this.maxSize = ObjectHelper.verifyPositive(maxSize, "maxSize");
/* 1055 */       this.maxAge = ObjectHelper.verifyPositive(maxAge, "maxAge");
/* 1056 */       this.unit = (TimeUnit)ObjectHelper.requireNonNull(unit, "unit is null");
/* 1057 */       this.scheduler = (Scheduler)ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 1058 */       ReplaySubject.TimedNode<Object> h = new ReplaySubject.TimedNode(null, 0L);
/* 1059 */       this.tail = h;
/* 1060 */       this.head = h;
/*      */     }
/*      */     
/*      */     void trim() {
/* 1064 */       if (this.size > this.maxSize) {
/* 1065 */         this.size--;
/* 1066 */         ReplaySubject.TimedNode<Object> timedNode = this.head;
/* 1067 */         this.head = timedNode.get();
/*      */       } 
/* 1069 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/* 1071 */       ReplaySubject.TimedNode<Object> h = this.head;
/*      */       
/*      */       while (true) {
/* 1074 */         if (this.size <= 1) {
/* 1075 */           this.head = h;
/*      */           break;
/*      */         } 
/* 1078 */         ReplaySubject.TimedNode<Object> next = h.get();
/* 1079 */         if (next == null) {
/* 1080 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/* 1084 */         if (next.time > limit) {
/* 1085 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/* 1089 */         h = next;
/* 1090 */         this.size--;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void trimFinal() {
/* 1096 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/* 1098 */       ReplaySubject.TimedNode<Object> h = this.head;
/*      */       
/*      */       while (true) {
/* 1101 */         ReplaySubject.TimedNode<Object> next = h.get();
/* 1102 */         if (next.get() == null) {
/* 1103 */           if (h.value != null) {
/* 1104 */             ReplaySubject.TimedNode<Object> lasth = new ReplaySubject.TimedNode(null, 0L);
/* 1105 */             lasth.lazySet(h.get());
/* 1106 */             this.head = lasth; break;
/*      */           } 
/* 1108 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1113 */         if (next.time > limit) {
/* 1114 */           if (h.value != null) {
/* 1115 */             ReplaySubject.TimedNode<Object> lasth = new ReplaySubject.TimedNode(null, 0L);
/* 1116 */             lasth.lazySet(h.get());
/* 1117 */             this.head = lasth; break;
/*      */           } 
/* 1119 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1124 */         h = next;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(T value) {
/* 1130 */       ReplaySubject.TimedNode<Object> n = new ReplaySubject.TimedNode(value, this.scheduler.now(this.unit));
/* 1131 */       ReplaySubject.TimedNode<Object> t = this.tail;
/*      */       
/* 1133 */       this.tail = n;
/* 1134 */       this.size++;
/* 1135 */       t.set(n);
/*      */       
/* 1137 */       trim();
/*      */     }
/*      */ 
/*      */     
/*      */     public void addFinal(Object notificationLite) {
/* 1142 */       ReplaySubject.TimedNode<Object> n = new ReplaySubject.TimedNode(notificationLite, Long.MAX_VALUE);
/* 1143 */       ReplaySubject.TimedNode<Object> t = this.tail;
/*      */       
/* 1145 */       this.tail = n;
/* 1146 */       this.size++;
/* 1147 */       t.lazySet(n);
/* 1148 */       trimFinal();
/*      */       
/* 1150 */       this.done = true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void trimHead() {
/* 1159 */       ReplaySubject.TimedNode<Object> h = this.head;
/* 1160 */       if (h.value != null) {
/* 1161 */         ReplaySubject.TimedNode<Object> n = new ReplaySubject.TimedNode(null, 0L);
/* 1162 */         n.lazySet(h.get());
/* 1163 */         this.head = n;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public T getValue() {
/* 1171 */       ReplaySubject.TimedNode<Object> prev = null;
/* 1172 */       ReplaySubject.TimedNode<Object> h = this.head;
/*      */       
/*      */       while (true) {
/* 1175 */         ReplaySubject.TimedNode<Object> next = h.get();
/* 1176 */         if (next == null) {
/*      */           break;
/*      */         }
/* 1179 */         prev = h;
/* 1180 */         h = next;
/*      */       } 
/*      */       
/* 1183 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/* 1184 */       if (h.time < limit) {
/* 1185 */         return null;
/*      */       }
/*      */       
/* 1188 */       Object v = h.value;
/* 1189 */       if (v == null) {
/* 1190 */         return null;
/*      */       }
/* 1192 */       if (NotificationLite.isComplete(v) || NotificationLite.isError(v)) {
/* 1193 */         return prev.value;
/*      */       }
/*      */       
/* 1196 */       return (T)v;
/*      */     }
/*      */     
/*      */     ReplaySubject.TimedNode<Object> getHead() {
/* 1200 */       ReplaySubject.TimedNode<Object> index = this.head;
/*      */       
/* 1202 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/* 1203 */       ReplaySubject.TimedNode<Object> next = index.get();
/* 1204 */       while (next != null) {
/* 1205 */         long ts = next.time;
/* 1206 */         if (ts > limit) {
/*      */           break;
/*      */         }
/* 1209 */         index = next;
/* 1210 */         next = index.get();
/*      */       } 
/* 1212 */       return index;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public T[] getValues(T[] array) {
/* 1218 */       ReplaySubject.TimedNode<Object> h = getHead();
/* 1219 */       int s = size(h);
/*      */       
/* 1221 */       if (s == 0) {
/* 1222 */         if (array.length != 0) {
/* 1223 */           array[0] = null;
/*      */         }
/*      */       } else {
/* 1226 */         if (array.length < s) {
/* 1227 */           array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
/*      */         }
/*      */         
/* 1230 */         int i = 0;
/* 1231 */         while (i != s) {
/* 1232 */           ReplaySubject.TimedNode<Object> next = h.get();
/* 1233 */           array[i] = next.value;
/* 1234 */           i++;
/* 1235 */           h = next;
/*      */         } 
/* 1237 */         if (array.length > s) {
/* 1238 */           array[s] = null;
/*      */         }
/*      */       } 
/*      */       
/* 1242 */       return array;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void replay(ReplaySubject.ReplayDisposable<T> rs) {
/* 1248 */       if (rs.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/* 1252 */       int missed = 1;
/* 1253 */       Observer<? super T> a = rs.downstream;
/*      */       
/* 1255 */       ReplaySubject.TimedNode<Object> index = (ReplaySubject.TimedNode<Object>)rs.index;
/* 1256 */       if (index == null) {
/* 1257 */         index = getHead();
/*      */       }
/*      */ 
/*      */       
/*      */       while (true) {
/* 1262 */         if (rs.cancelled) {
/* 1263 */           rs.index = null;
/*      */           
/*      */           return;
/*      */         } 
/*      */         while (true) {
/* 1268 */           if (rs.cancelled) {
/* 1269 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/* 1273 */           ReplaySubject.TimedNode<Object> n = index.get();
/*      */           
/* 1275 */           if (n == null) {
/*      */             break;
/*      */           }
/*      */           
/* 1279 */           Object o = n.value;
/*      */           
/* 1281 */           if (this.done && 
/* 1282 */             n.get() == null) {
/*      */             
/* 1284 */             if (NotificationLite.isComplete(o)) {
/* 1285 */               a.onComplete();
/*      */             } else {
/* 1287 */               a.onError(NotificationLite.getError(o));
/*      */             } 
/* 1289 */             rs.index = null;
/* 1290 */             rs.cancelled = true;
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/* 1295 */           a.onNext(o);
/*      */           
/* 1297 */           index = n;
/*      */         } 
/*      */         
/* 1300 */         if (index.get() != null) {
/*      */           continue;
/*      */         }
/*      */         
/* 1304 */         rs.index = index;
/*      */         
/* 1306 */         missed = rs.addAndGet(-missed);
/* 1307 */         if (missed == 0) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1315 */       return size(getHead());
/*      */     }
/*      */     
/*      */     int size(ReplaySubject.TimedNode<Object> h) {
/* 1319 */       int s = 0;
/* 1320 */       while (s != Integer.MAX_VALUE) {
/* 1321 */         ReplaySubject.TimedNode<Object> next = h.get();
/* 1322 */         if (next == null) {
/* 1323 */           Object o = h.value;
/* 1324 */           if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
/* 1325 */             s--;
/*      */           }
/*      */           break;
/*      */         } 
/* 1329 */         s++;
/* 1330 */         h = next;
/*      */       } 
/*      */       
/* 1333 */       return s;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/ReplaySubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */