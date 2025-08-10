/*      */ package io.reactivex.processors;
/*      */ 
/*      */ import io.reactivex.Scheduler;
/*      */ import io.reactivex.annotations.CheckReturnValue;
/*      */ import io.reactivex.annotations.NonNull;
/*      */ import io.reactivex.annotations.Nullable;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*      */ import io.reactivex.internal.util.BackpressureHelper;
/*      */ import io.reactivex.plugins.RxJavaPlugins;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import org.reactivestreams.Subscriber;
/*      */ import org.reactivestreams.Subscription;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ReplayProcessor<T>
/*      */   extends FlowableProcessor<T>
/*      */ {
/*  146 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*      */ 
/*      */   
/*      */   final ReplayBuffer<T> buffer;
/*      */   
/*      */   boolean done;
/*      */   
/*      */   final AtomicReference<ReplaySubscription<T>[]> subscribers;
/*      */   
/*  155 */   static final ReplaySubscription[] EMPTY = new ReplaySubscription[0];
/*      */ 
/*      */   
/*  158 */   static final ReplaySubscription[] TERMINATED = new ReplaySubscription[0];
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
/*      */   public static <T> ReplayProcessor<T> create() {
/*  176 */     return new ReplayProcessor<T>(new UnboundedReplayBuffer<T>(16));
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
/*      */   public static <T> ReplayProcessor<T> create(int capacityHint) {
/*  197 */     return new ReplayProcessor<T>(new UnboundedReplayBuffer<T>(capacityHint));
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
/*      */   public static <T> ReplayProcessor<T> createWithSize(int maxSize) {
/*  223 */     return new ReplayProcessor<T>(new SizeBoundReplayBuffer<T>(maxSize));
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
/*      */   static <T> ReplayProcessor<T> createUnbounded() {
/*  240 */     return new ReplayProcessor<T>(new SizeBoundReplayBuffer<T>(2147483647));
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
/*      */   public static <T> ReplayProcessor<T> createWithTime(long maxAge, TimeUnit unit, Scheduler scheduler) {
/*  278 */     return new ReplayProcessor<T>(new SizeAndTimeBoundReplayBuffer<T>(2147483647, maxAge, unit, scheduler));
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
/*      */   public static <T> ReplayProcessor<T> createWithTimeAndSize(long maxAge, TimeUnit unit, Scheduler scheduler, int maxSize) {
/*  318 */     return new ReplayProcessor<T>(new SizeAndTimeBoundReplayBuffer<T>(maxSize, maxAge, unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ReplayProcessor(ReplayBuffer<T> buffer) {
/*  327 */     this.buffer = buffer;
/*  328 */     this.subscribers = new AtomicReference(EMPTY);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void subscribeActual(Subscriber<? super T> s) {
/*  333 */     ReplaySubscription<T> rs = new ReplaySubscription<T>(s, this);
/*  334 */     s.onSubscribe(rs);
/*      */     
/*  336 */     if (add(rs) && 
/*  337 */       rs.cancelled) {
/*  338 */       remove(rs);
/*      */       
/*      */       return;
/*      */     } 
/*  342 */     this.buffer.replay(rs);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onSubscribe(Subscription s) {
/*  347 */     if (this.done) {
/*  348 */       s.cancel();
/*      */       return;
/*      */     } 
/*  351 */     s.request(Long.MAX_VALUE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onNext(T t) {
/*  356 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/*      */     
/*  358 */     if (this.done) {
/*      */       return;
/*      */     }
/*      */     
/*  362 */     ReplayBuffer<T> b = this.buffer;
/*  363 */     b.next(t);
/*      */     
/*  365 */     for (ReplaySubscription<T> rs : (ReplaySubscription[])this.subscribers.get()) {
/*  366 */       b.replay(rs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onError(Throwable t) {
/*  373 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*      */     
/*  375 */     if (this.done) {
/*  376 */       RxJavaPlugins.onError(t);
/*      */       return;
/*      */     } 
/*  379 */     this.done = true;
/*      */     
/*  381 */     ReplayBuffer<T> b = this.buffer;
/*  382 */     b.error(t);
/*      */     
/*  384 */     for (ReplaySubscription<T> rs : (ReplaySubscription[])this.subscribers.getAndSet(TERMINATED)) {
/*  385 */       b.replay(rs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onComplete() {
/*  392 */     if (this.done) {
/*      */       return;
/*      */     }
/*  395 */     this.done = true;
/*      */     
/*  397 */     ReplayBuffer<T> b = this.buffer;
/*      */     
/*  399 */     b.complete();
/*      */     
/*  401 */     for (ReplaySubscription<T> rs : (ReplaySubscription[])this.subscribers.getAndSet(TERMINATED)) {
/*  402 */       b.replay(rs);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasSubscribers() {
/*  408 */     return (((ReplaySubscription[])this.subscribers.get()).length != 0);
/*      */   }
/*      */   
/*      */   int subscriberCount() {
/*  412 */     return ((ReplaySubscription[])this.subscribers.get()).length;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Throwable getThrowable() {
/*  418 */     ReplayBuffer<T> b = this.buffer;
/*  419 */     if (b.isDone()) {
/*  420 */       return b.getError();
/*      */     }
/*  422 */     return null;
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
/*  440 */     this.buffer.trimHead();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public T getValue() {
/*  449 */     return this.buffer.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] getValues() {
/*  459 */     T[] a = (T[])EMPTY_ARRAY;
/*  460 */     T[] b = getValues(a);
/*  461 */     if (b == EMPTY_ARRAY) {
/*  462 */       return new Object[0];
/*      */     }
/*  464 */     return (Object[])b;
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
/*  477 */     return this.buffer.getValues(array);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasComplete() {
/*  482 */     ReplayBuffer<T> b = this.buffer;
/*  483 */     return (b.isDone() && b.getError() == null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasThrowable() {
/*  488 */     ReplayBuffer<T> b = this.buffer;
/*  489 */     return (b.isDone() && b.getError() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasValue() {
/*  498 */     return (this.buffer.size() != 0);
/*      */   }
/*      */   
/*      */   int size() {
/*  502 */     return this.buffer.size();
/*      */   }
/*      */   
/*      */   boolean add(ReplaySubscription<T> rs) {
/*      */     while (true) {
/*  507 */       ReplaySubscription[] arrayOfReplaySubscription1 = (ReplaySubscription[])this.subscribers.get();
/*  508 */       if (arrayOfReplaySubscription1 == TERMINATED) {
/*  509 */         return false;
/*      */       }
/*  511 */       int len = arrayOfReplaySubscription1.length;
/*      */       
/*  513 */       ReplaySubscription[] arrayOfReplaySubscription2 = new ReplaySubscription[len + 1];
/*  514 */       System.arraycopy(arrayOfReplaySubscription1, 0, arrayOfReplaySubscription2, 0, len);
/*  515 */       arrayOfReplaySubscription2[len] = rs;
/*  516 */       if (this.subscribers.compareAndSet(arrayOfReplaySubscription1, arrayOfReplaySubscription2))
/*  517 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   void remove(ReplaySubscription<T> rs) {
/*      */     ReplaySubscription[] arrayOfReplaySubscription1;
/*      */     ReplaySubscription[] arrayOfReplaySubscription2;
/*      */     do {
/*  525 */       arrayOfReplaySubscription1 = (ReplaySubscription[])this.subscribers.get();
/*  526 */       if (arrayOfReplaySubscription1 == TERMINATED || arrayOfReplaySubscription1 == EMPTY) {
/*      */         return;
/*      */       }
/*  529 */       int len = arrayOfReplaySubscription1.length;
/*  530 */       int j = -1;
/*  531 */       for (int i = 0; i < len; i++) {
/*  532 */         if (arrayOfReplaySubscription1[i] == rs) {
/*  533 */           j = i;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  538 */       if (j < 0) {
/*      */         return;
/*      */       }
/*      */       
/*  542 */       if (len == 1) {
/*  543 */         arrayOfReplaySubscription2 = EMPTY;
/*      */       } else {
/*  545 */         arrayOfReplaySubscription2 = new ReplaySubscription[len - 1];
/*  546 */         System.arraycopy(arrayOfReplaySubscription1, 0, arrayOfReplaySubscription2, 0, j);
/*  547 */         System.arraycopy(arrayOfReplaySubscription1, j + 1, arrayOfReplaySubscription2, j, len - j - 1);
/*      */       } 
/*  549 */     } while (!this.subscribers.compareAndSet(arrayOfReplaySubscription1, arrayOfReplaySubscription2));
/*      */   }
/*      */ 
/*      */   
/*      */   static interface ReplayBuffer<T>
/*      */   {
/*      */     void next(T param1T);
/*      */ 
/*      */     
/*      */     void error(Throwable param1Throwable);
/*      */ 
/*      */     
/*      */     void complete();
/*      */ 
/*      */     
/*      */     void replay(ReplayProcessor.ReplaySubscription<T> param1ReplaySubscription);
/*      */ 
/*      */     
/*      */     int size();
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     T getValue();
/*      */ 
/*      */     
/*      */     T[] getValues(T[] param1ArrayOfT);
/*      */ 
/*      */     
/*      */     boolean isDone();
/*      */ 
/*      */     
/*      */     Throwable getError();
/*      */ 
/*      */     
/*      */     void trimHead();
/*      */   }
/*      */ 
/*      */   
/*      */   static final class ReplaySubscription<T>
/*      */     extends AtomicInteger
/*      */     implements Subscription
/*      */   {
/*      */     private static final long serialVersionUID = 466549804534799122L;
/*      */     
/*      */     final Subscriber<? super T> downstream;
/*      */     
/*      */     final ReplayProcessor<T> state;
/*      */     
/*      */     Object index;
/*      */     
/*      */     final AtomicLong requested;
/*      */     volatile boolean cancelled;
/*      */     long emitted;
/*      */     
/*      */     ReplaySubscription(Subscriber<? super T> actual, ReplayProcessor<T> state) {
/*  604 */       this.downstream = actual;
/*  605 */       this.state = state;
/*  606 */       this.requested = new AtomicLong();
/*      */     }
/*      */ 
/*      */     
/*      */     public void request(long n) {
/*  611 */       if (SubscriptionHelper.validate(n)) {
/*  612 */         BackpressureHelper.add(this.requested, n);
/*  613 */         this.state.buffer.replay(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void cancel() {
/*  619 */       if (!this.cancelled) {
/*  620 */         this.cancelled = true;
/*  621 */         this.state.remove(this);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class UnboundedReplayBuffer<T>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     final List<T> buffer;
/*      */     
/*      */     Throwable error;
/*      */     volatile boolean done;
/*      */     volatile int size;
/*      */     
/*      */     UnboundedReplayBuffer(int capacityHint) {
/*  637 */       this.buffer = new ArrayList<T>(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
/*      */     }
/*      */ 
/*      */     
/*      */     public void next(T value) {
/*  642 */       this.buffer.add(value);
/*  643 */       this.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void error(Throwable ex) {
/*  648 */       this.error = ex;
/*  649 */       this.done = true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void complete() {
/*  654 */       this.done = true;
/*      */     }
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
/*  666 */       if (s == 0) {
/*  667 */         return null;
/*      */       }
/*  669 */       return this.buffer.get(s - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public T[] getValues(T[] array) {
/*  675 */       int s = this.size;
/*  676 */       if (s == 0) {
/*  677 */         if (array.length != 0) {
/*  678 */           array[0] = null;
/*      */         }
/*  680 */         return array;
/*      */       } 
/*  682 */       List<T> b = this.buffer;
/*      */       
/*  684 */       if (array.length < s) {
/*  685 */         array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
/*      */       }
/*  687 */       for (int i = 0; i < s; i++) {
/*  688 */         array[i] = b.get(i);
/*      */       }
/*  690 */       if (array.length > s) {
/*  691 */         array[s] = null;
/*      */       }
/*      */       
/*  694 */       return array;
/*      */     }
/*      */     
/*      */     public void replay(ReplayProcessor.ReplaySubscription<T> rs) {
/*      */       int index;
/*  699 */       if (rs.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/*  703 */       int missed = 1;
/*  704 */       List<T> b = this.buffer;
/*  705 */       Subscriber<? super T> a = rs.downstream;
/*      */       
/*  707 */       Integer indexObject = (Integer)rs.index;
/*      */       
/*  709 */       if (indexObject != null) {
/*  710 */         index = indexObject.intValue();
/*      */       } else {
/*  712 */         index = 0;
/*  713 */         rs.index = Integer.valueOf(0);
/*      */       } 
/*  715 */       long e = rs.emitted;
/*      */ 
/*      */       
/*      */       do {
/*  719 */         long r = rs.requested.get();
/*      */         
/*  721 */         while (e != r) {
/*  722 */           if (rs.cancelled) {
/*  723 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/*  727 */           boolean d = this.done;
/*  728 */           int s = this.size;
/*      */           
/*  730 */           if (d && index == s) {
/*  731 */             rs.index = null;
/*  732 */             rs.cancelled = true;
/*  733 */             Throwable ex = this.error;
/*  734 */             if (ex == null) {
/*  735 */               a.onComplete();
/*      */             } else {
/*  737 */               a.onError(ex);
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*  742 */           if (index == s) {
/*      */             break;
/*      */           }
/*      */           
/*  746 */           a.onNext(b.get(index));
/*      */           
/*  748 */           index++;
/*  749 */           e++;
/*      */         } 
/*      */         
/*  752 */         if (e == r) {
/*  753 */           if (rs.cancelled) {
/*  754 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/*  758 */           boolean d = this.done;
/*  759 */           int s = this.size;
/*      */           
/*  761 */           if (d && index == s) {
/*  762 */             rs.index = null;
/*  763 */             rs.cancelled = true;
/*  764 */             Throwable ex = this.error;
/*  765 */             if (ex == null) {
/*  766 */               a.onComplete();
/*      */             } else {
/*  768 */               a.onError(ex);
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*  774 */         rs.index = Integer.valueOf(index);
/*  775 */         rs.emitted = e;
/*  776 */         missed = rs.addAndGet(-missed);
/*  777 */       } while (missed != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/*  785 */       return this.size;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDone() {
/*  790 */       return this.done;
/*      */     }
/*      */ 
/*      */     
/*      */     public Throwable getError() {
/*  795 */       return this.error;
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
/*  806 */       this.value = value;
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
/*  818 */       this.value = value;
/*  819 */       this.time = time;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class SizeBoundReplayBuffer<T>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     final int maxSize;
/*      */     
/*      */     int size;
/*      */     
/*      */     volatile ReplayProcessor.Node<T> head;
/*      */     ReplayProcessor.Node<T> tail;
/*      */     Throwable error;
/*      */     volatile boolean done;
/*      */     
/*      */     SizeBoundReplayBuffer(int maxSize) {
/*  837 */       this.maxSize = ObjectHelper.verifyPositive(maxSize, "maxSize");
/*  838 */       ReplayProcessor.Node<T> h = new ReplayProcessor.Node<T>(null);
/*  839 */       this.tail = h;
/*  840 */       this.head = h;
/*      */     }
/*      */     
/*      */     void trim() {
/*  844 */       if (this.size > this.maxSize) {
/*  845 */         this.size--;
/*  846 */         ReplayProcessor.Node<T> h = this.head;
/*  847 */         this.head = h.get();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void next(T value) {
/*  853 */       ReplayProcessor.Node<T> n = new ReplayProcessor.Node<T>(value);
/*  854 */       ReplayProcessor.Node<T> t = this.tail;
/*      */       
/*  856 */       this.tail = n;
/*  857 */       this.size++;
/*  858 */       t.set(n);
/*      */       
/*  860 */       trim();
/*      */     }
/*      */ 
/*      */     
/*      */     public void error(Throwable ex) {
/*  865 */       this.error = ex;
/*  866 */       trimHead();
/*  867 */       this.done = true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void complete() {
/*  872 */       trimHead();
/*  873 */       this.done = true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void trimHead() {
/*  878 */       if (this.head.value != null) {
/*  879 */         ReplayProcessor.Node<T> n = new ReplayProcessor.Node<T>(null);
/*  880 */         n.lazySet(this.head.get());
/*  881 */         this.head = n;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDone() {
/*  887 */       return this.done;
/*      */     }
/*      */ 
/*      */     
/*      */     public Throwable getError() {
/*  892 */       return this.error;
/*      */     }
/*      */ 
/*      */     
/*      */     public T getValue() {
/*  897 */       ReplayProcessor.Node<T> h = this.head;
/*      */       while (true) {
/*  899 */         ReplayProcessor.Node<T> n = h.get();
/*  900 */         if (n == null) {
/*  901 */           return h.value;
/*      */         }
/*  903 */         h = n;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public T[] getValues(T[] array) {
/*  910 */       int s = 0;
/*  911 */       ReplayProcessor.Node<T> h = this.head;
/*  912 */       ReplayProcessor.Node<T> h0 = h;
/*      */       while (true) {
/*  914 */         ReplayProcessor.Node<T> next = h0.get();
/*  915 */         if (next == null) {
/*      */           break;
/*      */         }
/*  918 */         s++;
/*  919 */         h0 = next;
/*      */       } 
/*  921 */       if (array.length < s) {
/*  922 */         array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
/*      */       }
/*      */       
/*  925 */       for (int j = 0; j < s; j++) {
/*  926 */         h = h.get();
/*  927 */         array[j] = h.value;
/*      */       } 
/*      */       
/*  930 */       if (array.length > s) {
/*  931 */         array[s] = null;
/*      */       }
/*  933 */       return array;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void replay(ReplayProcessor.ReplaySubscription<T> rs) {
/*  939 */       if (rs.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/*  943 */       int missed = 1;
/*  944 */       Subscriber<? super T> a = rs.downstream;
/*      */       
/*  946 */       ReplayProcessor.Node<T> index = (ReplayProcessor.Node<T>)rs.index;
/*  947 */       if (index == null) {
/*  948 */         index = this.head;
/*      */       }
/*      */       
/*  951 */       long e = rs.emitted;
/*      */ 
/*      */       
/*      */       do {
/*  955 */         long r = rs.requested.get();
/*      */         
/*  957 */         while (e != r) {
/*  958 */           if (rs.cancelled) {
/*  959 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/*  963 */           boolean d = this.done;
/*  964 */           ReplayProcessor.Node<T> next = index.get();
/*  965 */           boolean empty = (next == null);
/*      */           
/*  967 */           if (d && empty) {
/*  968 */             rs.index = null;
/*  969 */             rs.cancelled = true;
/*  970 */             Throwable ex = this.error;
/*  971 */             if (ex == null) {
/*  972 */               a.onComplete();
/*      */             } else {
/*  974 */               a.onError(ex);
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*  979 */           if (empty) {
/*      */             break;
/*      */           }
/*      */           
/*  983 */           a.onNext(next.value);
/*  984 */           e++;
/*  985 */           index = next;
/*      */         } 
/*      */         
/*  988 */         if (e == r) {
/*  989 */           if (rs.cancelled) {
/*  990 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/*  994 */           boolean d = this.done;
/*      */           
/*  996 */           if (d && index.get() == null) {
/*  997 */             rs.index = null;
/*  998 */             rs.cancelled = true;
/*  999 */             Throwable ex = this.error;
/* 1000 */             if (ex == null) {
/* 1001 */               a.onComplete();
/*      */             } else {
/* 1003 */               a.onError(ex);
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/* 1009 */         rs.index = index;
/* 1010 */         rs.emitted = e;
/*      */         
/* 1012 */         missed = rs.addAndGet(-missed);
/* 1013 */       } while (missed != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/* 1021 */       int s = 0;
/* 1022 */       ReplayProcessor.Node<T> h = this.head;
/* 1023 */       while (s != Integer.MAX_VALUE) {
/* 1024 */         ReplayProcessor.Node<T> next = h.get();
/* 1025 */         if (next == null) {
/*      */           break;
/*      */         }
/* 1028 */         s++;
/* 1029 */         h = next;
/*      */       } 
/*      */       
/* 1032 */       return s;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class SizeAndTimeBoundReplayBuffer<T>
/*      */     implements ReplayBuffer<T>
/*      */   {
/*      */     final int maxSize;
/*      */     
/*      */     final long maxAge;
/*      */     
/*      */     final TimeUnit unit;
/*      */     final Scheduler scheduler;
/*      */     int size;
/*      */     volatile ReplayProcessor.TimedNode<T> head;
/*      */     ReplayProcessor.TimedNode<T> tail;
/*      */     Throwable error;
/*      */     volatile boolean done;
/*      */     
/*      */     SizeAndTimeBoundReplayBuffer(int maxSize, long maxAge, TimeUnit unit, Scheduler scheduler) {
/* 1053 */       this.maxSize = ObjectHelper.verifyPositive(maxSize, "maxSize");
/* 1054 */       this.maxAge = ObjectHelper.verifyPositive(maxAge, "maxAge");
/* 1055 */       this.unit = (TimeUnit)ObjectHelper.requireNonNull(unit, "unit is null");
/* 1056 */       this.scheduler = (Scheduler)ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 1057 */       ReplayProcessor.TimedNode<T> h = new ReplayProcessor.TimedNode<T>(null, 0L);
/* 1058 */       this.tail = h;
/* 1059 */       this.head = h;
/*      */     }
/*      */     
/*      */     void trim() {
/* 1063 */       if (this.size > this.maxSize) {
/* 1064 */         this.size--;
/* 1065 */         ReplayProcessor.TimedNode<T> timedNode = this.head;
/* 1066 */         this.head = timedNode.get();
/*      */       } 
/* 1068 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/* 1070 */       ReplayProcessor.TimedNode<T> h = this.head;
/*      */       
/*      */       while (true) {
/* 1073 */         if (this.size <= 1) {
/* 1074 */           this.head = h;
/*      */           break;
/*      */         } 
/* 1077 */         ReplayProcessor.TimedNode<T> next = h.get();
/* 1078 */         if (next == null) {
/* 1079 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/* 1083 */         if (next.time > limit) {
/* 1084 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/* 1088 */         h = next;
/* 1089 */         this.size--;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void trimFinal() {
/* 1095 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/*      */       
/* 1097 */       ReplayProcessor.TimedNode<T> h = this.head;
/*      */       
/*      */       while (true) {
/* 1100 */         ReplayProcessor.TimedNode<T> next = h.get();
/* 1101 */         if (next == null) {
/* 1102 */           if (h.value != null) {
/* 1103 */             this.head = new ReplayProcessor.TimedNode<T>(null, 0L); break;
/*      */           } 
/* 1105 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1110 */         if (next.time > limit) {
/* 1111 */           if (h.value != null) {
/* 1112 */             ReplayProcessor.TimedNode<T> n = new ReplayProcessor.TimedNode<T>(null, 0L);
/* 1113 */             n.lazySet(h.get());
/* 1114 */             this.head = n; break;
/*      */           } 
/* 1116 */           this.head = h;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 1121 */         h = next;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void trimHead() {
/* 1127 */       if (this.head.value != null) {
/* 1128 */         ReplayProcessor.TimedNode<T> n = new ReplayProcessor.TimedNode<T>(null, 0L);
/* 1129 */         n.lazySet(this.head.get());
/* 1130 */         this.head = n;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void next(T value) {
/* 1136 */       ReplayProcessor.TimedNode<T> n = new ReplayProcessor.TimedNode<T>(value, this.scheduler.now(this.unit));
/* 1137 */       ReplayProcessor.TimedNode<T> t = this.tail;
/*      */       
/* 1139 */       this.tail = n;
/* 1140 */       this.size++;
/* 1141 */       t.set(n);
/*      */       
/* 1143 */       trim();
/*      */     }
/*      */ 
/*      */     
/*      */     public void error(Throwable ex) {
/* 1148 */       trimFinal();
/* 1149 */       this.error = ex;
/* 1150 */       this.done = true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void complete() {
/* 1155 */       trimFinal();
/* 1156 */       this.done = true;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public T getValue() {
/* 1162 */       ReplayProcessor.TimedNode<T> h = this.head;
/*      */       
/*      */       while (true) {
/* 1165 */         ReplayProcessor.TimedNode<T> next = h.get();
/* 1166 */         if (next == null) {
/*      */           break;
/*      */         }
/* 1169 */         h = next;
/*      */       } 
/*      */       
/* 1172 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/* 1173 */       if (h.time < limit) {
/* 1174 */         return null;
/*      */       }
/*      */       
/* 1177 */       return h.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public T[] getValues(T[] array) {
/* 1183 */       ReplayProcessor.TimedNode<T> h = getHead();
/* 1184 */       int s = size(h);
/*      */       
/* 1186 */       if (s == 0) {
/* 1187 */         if (array.length != 0) {
/* 1188 */           array[0] = null;
/*      */         }
/*      */       } else {
/* 1191 */         if (array.length < s) {
/* 1192 */           array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
/*      */         }
/*      */         
/* 1195 */         int i = 0;
/* 1196 */         while (i != s) {
/* 1197 */           ReplayProcessor.TimedNode<T> next = h.get();
/* 1198 */           array[i] = next.value;
/* 1199 */           i++;
/* 1200 */           h = next;
/*      */         } 
/* 1202 */         if (array.length > s) {
/* 1203 */           array[s] = null;
/*      */         }
/*      */       } 
/*      */       
/* 1207 */       return array;
/*      */     }
/*      */     
/*      */     ReplayProcessor.TimedNode<T> getHead() {
/* 1211 */       ReplayProcessor.TimedNode<T> index = this.head;
/*      */       
/* 1213 */       long limit = this.scheduler.now(this.unit) - this.maxAge;
/* 1214 */       ReplayProcessor.TimedNode<T> next = index.get();
/* 1215 */       while (next != null) {
/* 1216 */         long ts = next.time;
/* 1217 */         if (ts > limit) {
/*      */           break;
/*      */         }
/* 1220 */         index = next;
/* 1221 */         next = index.get();
/*      */       } 
/* 1223 */       return index;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void replay(ReplayProcessor.ReplaySubscription<T> rs) {
/* 1229 */       if (rs.getAndIncrement() != 0) {
/*      */         return;
/*      */       }
/*      */       
/* 1233 */       int missed = 1;
/* 1234 */       Subscriber<? super T> a = rs.downstream;
/*      */       
/* 1236 */       ReplayProcessor.TimedNode<T> index = (ReplayProcessor.TimedNode<T>)rs.index;
/* 1237 */       if (index == null) {
/* 1238 */         index = getHead();
/*      */       }
/*      */       
/* 1241 */       long e = rs.emitted;
/*      */ 
/*      */       
/*      */       do {
/* 1245 */         long r = rs.requested.get();
/*      */         
/* 1247 */         while (e != r) {
/* 1248 */           if (rs.cancelled) {
/* 1249 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/* 1253 */           boolean d = this.done;
/* 1254 */           ReplayProcessor.TimedNode<T> next = index.get();
/* 1255 */           boolean empty = (next == null);
/*      */           
/* 1257 */           if (d && empty) {
/* 1258 */             rs.index = null;
/* 1259 */             rs.cancelled = true;
/* 1260 */             Throwable ex = this.error;
/* 1261 */             if (ex == null) {
/* 1262 */               a.onComplete();
/*      */             } else {
/* 1264 */               a.onError(ex);
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/* 1269 */           if (empty) {
/*      */             break;
/*      */           }
/*      */           
/* 1273 */           a.onNext(next.value);
/* 1274 */           e++;
/* 1275 */           index = next;
/*      */         } 
/*      */         
/* 1278 */         if (e == r) {
/* 1279 */           if (rs.cancelled) {
/* 1280 */             rs.index = null;
/*      */             
/*      */             return;
/*      */           } 
/* 1284 */           boolean d = this.done;
/*      */           
/* 1286 */           if (d && index.get() == null) {
/* 1287 */             rs.index = null;
/* 1288 */             rs.cancelled = true;
/* 1289 */             Throwable ex = this.error;
/* 1290 */             if (ex == null) {
/* 1291 */               a.onComplete();
/*      */             } else {
/* 1293 */               a.onError(ex);
/*      */             } 
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/* 1299 */         rs.index = index;
/* 1300 */         rs.emitted = e;
/*      */         
/* 1302 */         missed = rs.addAndGet(-missed);
/* 1303 */       } while (missed != 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/* 1311 */       return size(getHead());
/*      */     }
/*      */     
/*      */     int size(ReplayProcessor.TimedNode<T> h) {
/* 1315 */       int s = 0;
/* 1316 */       while (s != Integer.MAX_VALUE) {
/* 1317 */         ReplayProcessor.TimedNode<T> next = h.get();
/* 1318 */         if (next == null) {
/*      */           break;
/*      */         }
/* 1321 */         s++;
/* 1322 */         h = next;
/*      */       } 
/*      */       
/* 1325 */       return s;
/*      */     }
/*      */ 
/*      */     
/*      */     public Throwable getError() {
/* 1330 */       return this.error;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDone() {
/* 1335 */       return this.done;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/ReplayProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */