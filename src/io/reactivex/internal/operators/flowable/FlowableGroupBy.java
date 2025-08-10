/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.flowables.GroupedFlowable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.EmptyComponent;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class FlowableGroupBy<T, K, V>
/*     */   extends AbstractFlowableWithUpstream<T, GroupedFlowable<K, V>>
/*     */ {
/*     */   final Function<? super T, ? extends K> keySelector;
/*     */   final Function<? super T, ? extends V> valueSelector;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   final Function<? super Consumer<Object>, ? extends Map<K, Object>> mapFactory;
/*     */   
/*     */   public FlowableGroupBy(Flowable<T> source, Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, int bufferSize, boolean delayError, Function<? super Consumer<Object>, ? extends Map<K, Object>> mapFactory) {
/*  46 */     super(source);
/*  47 */     this.keySelector = keySelector;
/*  48 */     this.valueSelector = valueSelector;
/*  49 */     this.bufferSize = bufferSize;
/*  50 */     this.delayError = delayError;
/*  51 */     this.mapFactory = mapFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super GroupedFlowable<K, V>> s) {
/*     */     Map<Object, GroupedUnicast<K, V>> groups;
/*     */     Queue<GroupedUnicast<K, V>> evictedGroups;
/*     */     try {
/*  62 */       if (this.mapFactory == null) {
/*  63 */         evictedGroups = null;
/*  64 */         groups = new ConcurrentHashMap<Object, GroupedUnicast<K, V>>();
/*     */       } else {
/*  66 */         evictedGroups = new ConcurrentLinkedQueue<GroupedUnicast<K, V>>();
/*  67 */         Consumer<Object> evictionAction = (Consumer)new EvictionAction<K, V>(evictedGroups);
/*  68 */         groups = (Map<Object, GroupedUnicast<K, V>>)this.mapFactory.apply(evictionAction);
/*     */       } 
/*  70 */     } catch (Exception e) {
/*  71 */       Exceptions.throwIfFatal(e);
/*  72 */       s.onSubscribe((Subscription)EmptyComponent.INSTANCE);
/*  73 */       s.onError(e);
/*     */       return;
/*     */     } 
/*  76 */     GroupBySubscriber<T, K, V> subscriber = new GroupBySubscriber<T, K, V>(s, this.keySelector, this.valueSelector, this.bufferSize, this.delayError, groups, evictedGroups);
/*     */     
/*  78 */     this.source.subscribe(subscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class GroupBySubscriber<T, K, V>
/*     */     extends BasicIntQueueSubscription<GroupedFlowable<K, V>>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3688291656102519502L;
/*     */     
/*     */     final Subscriber<? super GroupedFlowable<K, V>> downstream;
/*     */     final Function<? super T, ? extends K> keySelector;
/*     */     final Function<? super T, ? extends V> valueSelector;
/*     */     final int bufferSize;
/*     */     final boolean delayError;
/*     */     final Map<Object, FlowableGroupBy.GroupedUnicast<K, V>> groups;
/*     */     final SpscLinkedArrayQueue<GroupedFlowable<K, V>> queue;
/*     */     final Queue<FlowableGroupBy.GroupedUnicast<K, V>> evictedGroups;
/*  96 */     static final Object NULL_KEY = new Object();
/*     */     
/*     */     Subscription upstream;
/*     */     
/* 100 */     final AtomicBoolean cancelled = new AtomicBoolean();
/*     */     
/* 102 */     final AtomicLong requested = new AtomicLong();
/*     */     
/* 104 */     final AtomicInteger groupCount = new AtomicInteger(1);
/*     */     
/*     */     Throwable error;
/*     */     
/*     */     volatile boolean finished;
/*     */     
/*     */     boolean done;
/*     */     
/*     */     boolean outputFused;
/*     */     
/*     */     public GroupBySubscriber(Subscriber<? super GroupedFlowable<K, V>> actual, Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, int bufferSize, boolean delayError, Map<Object, FlowableGroupBy.GroupedUnicast<K, V>> groups, Queue<FlowableGroupBy.GroupedUnicast<K, V>> evictedGroups) {
/* 115 */       this.downstream = actual;
/* 116 */       this.keySelector = keySelector;
/* 117 */       this.valueSelector = valueSelector;
/* 118 */       this.bufferSize = bufferSize;
/* 119 */       this.delayError = delayError;
/* 120 */       this.groups = groups;
/* 121 */       this.evictedGroups = evictedGroups;
/* 122 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 127 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 128 */         this.upstream = s;
/* 129 */         this.downstream.onSubscribe((Subscription)this);
/* 130 */         s.request(this.bufferSize);
/*     */       } 
/*     */     }
/*     */     public void onNext(T t) {
/*     */       K key;
/*     */       V v;
/* 136 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 140 */       SpscLinkedArrayQueue<GroupedFlowable<K, V>> q = this.queue;
/*     */ 
/*     */       
/*     */       try {
/* 144 */         key = (K)this.keySelector.apply(t);
/* 145 */       } catch (Throwable ex) {
/* 146 */         Exceptions.throwIfFatal(ex);
/* 147 */         this.upstream.cancel();
/* 148 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 152 */       boolean newGroup = false;
/* 153 */       Object mapKey = (key != null) ? key : NULL_KEY;
/* 154 */       FlowableGroupBy.GroupedUnicast<K, V> group = this.groups.get(mapKey);
/* 155 */       if (group == null) {
/*     */ 
/*     */         
/* 158 */         if (this.cancelled.get()) {
/*     */           return;
/*     */         }
/*     */         
/* 162 */         group = FlowableGroupBy.GroupedUnicast.createWith(key, this.bufferSize, this, this.delayError);
/* 163 */         this.groups.put(mapKey, group);
/*     */         
/* 165 */         this.groupCount.getAndIncrement();
/*     */         
/* 167 */         newGroup = true;
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 172 */         v = (V)ObjectHelper.requireNonNull(this.valueSelector.apply(t), "The valueSelector returned null");
/* 173 */       } catch (Throwable ex) {
/* 174 */         Exceptions.throwIfFatal(ex);
/* 175 */         this.upstream.cancel();
/* 176 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 180 */       group.onNext(v);
/*     */       
/* 182 */       completeEvictions();
/*     */       
/* 184 */       if (newGroup) {
/* 185 */         q.offer(group);
/* 186 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 192 */       if (this.done) {
/* 193 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 196 */       this.done = true;
/* 197 */       for (FlowableGroupBy.GroupedUnicast<K, V> g : this.groups.values()) {
/* 198 */         g.onError(t);
/*     */       }
/* 200 */       this.groups.clear();
/* 201 */       if (this.evictedGroups != null) {
/* 202 */         this.evictedGroups.clear();
/*     */       }
/* 204 */       this.error = t;
/* 205 */       this.finished = true;
/* 206 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 211 */       if (!this.done) {
/* 212 */         for (FlowableGroupBy.GroupedUnicast<K, V> g : this.groups.values()) {
/* 213 */           g.onComplete();
/*     */         }
/* 215 */         this.groups.clear();
/* 216 */         if (this.evictedGroups != null) {
/* 217 */           this.evictedGroups.clear();
/*     */         }
/* 219 */         this.done = true;
/* 220 */         this.finished = true;
/* 221 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 227 */       if (SubscriptionHelper.validate(n)) {
/* 228 */         BackpressureHelper.add(this.requested, n);
/* 229 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 237 */       if (this.cancelled.compareAndSet(false, true)) {
/* 238 */         completeEvictions();
/* 239 */         if (this.groupCount.decrementAndGet() == 0) {
/* 240 */           this.upstream.cancel();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void completeEvictions() {
/* 246 */       if (this.evictedGroups != null) {
/* 247 */         int count = 0;
/*     */         FlowableGroupBy.GroupedUnicast<K, V> evictedGroup;
/* 249 */         while ((evictedGroup = this.evictedGroups.poll()) != null) {
/* 250 */           evictedGroup.onComplete();
/* 251 */           count++;
/*     */         } 
/* 253 */         if (count != 0) {
/* 254 */           this.groupCount.addAndGet(-count);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void cancel(K key) {
/* 260 */       Object mapKey = (key != null) ? key : NULL_KEY;
/* 261 */       this.groups.remove(mapKey);
/* 262 */       if (this.groupCount.decrementAndGet() == 0) {
/* 263 */         this.upstream.cancel();
/*     */         
/* 265 */         if (!this.outputFused && getAndIncrement() == 0) {
/* 266 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 272 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 276 */       if (this.outputFused) {
/* 277 */         drainFused();
/*     */       } else {
/* 279 */         drainNormal();
/*     */       } 
/*     */     }
/*     */     
/*     */     void drainFused() {
/* 284 */       int missed = 1;
/*     */       
/* 286 */       SpscLinkedArrayQueue<GroupedFlowable<K, V>> q = this.queue;
/* 287 */       Subscriber<? super GroupedFlowable<K, V>> a = this.downstream;
/*     */       
/*     */       do {
/* 290 */         if (this.cancelled.get()) {
/*     */           return;
/*     */         }
/*     */         
/* 294 */         boolean d = this.finished;
/*     */         
/* 296 */         if (d && !this.delayError) {
/* 297 */           Throwable ex = this.error;
/* 298 */           if (ex != null) {
/* 299 */             q.clear();
/* 300 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 305 */         a.onNext(null);
/*     */         
/* 307 */         if (d) {
/* 308 */           Throwable ex = this.error;
/* 309 */           if (ex != null) {
/* 310 */             a.onError(ex);
/*     */           } else {
/* 312 */             a.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 317 */         missed = addAndGet(-missed);
/* 318 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void drainNormal() {
/* 325 */       int missed = 1;
/*     */       
/* 327 */       SpscLinkedArrayQueue<GroupedFlowable<K, V>> q = this.queue;
/* 328 */       Subscriber<? super GroupedFlowable<K, V>> a = this.downstream;
/*     */ 
/*     */       
/*     */       do {
/* 332 */         long r = this.requested.get();
/* 333 */         long e = 0L;
/*     */         
/* 335 */         while (e != r) {
/* 336 */           boolean d = this.finished;
/*     */           
/* 338 */           GroupedFlowable<K, V> t = (GroupedFlowable<K, V>)q.poll();
/*     */           
/* 340 */           boolean empty = (t == null);
/*     */           
/* 342 */           if (checkTerminated(d, empty, a, q)) {
/*     */             return;
/*     */           }
/*     */           
/* 346 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 350 */           a.onNext(t);
/*     */           
/* 352 */           e++;
/*     */         } 
/*     */         
/* 355 */         if (e == r && checkTerminated(this.finished, q.isEmpty(), a, q)) {
/*     */           return;
/*     */         }
/*     */         
/* 359 */         if (e != 0L) {
/* 360 */           if (r != Long.MAX_VALUE) {
/* 361 */             this.requested.addAndGet(-e);
/*     */           }
/* 363 */           this.upstream.request(e);
/*     */         } 
/*     */         
/* 366 */         missed = addAndGet(-missed);
/* 367 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SpscLinkedArrayQueue<?> q) {
/* 374 */       if (this.cancelled.get()) {
/* 375 */         q.clear();
/* 376 */         return true;
/*     */       } 
/*     */       
/* 379 */       if (this.delayError) {
/* 380 */         if (d && empty) {
/* 381 */           Throwable ex = this.error;
/* 382 */           if (ex != null) {
/* 383 */             a.onError(ex);
/*     */           } else {
/* 385 */             a.onComplete();
/*     */           } 
/* 387 */           return true;
/*     */         }
/*     */       
/* 390 */       } else if (d) {
/* 391 */         Throwable ex = this.error;
/* 392 */         if (ex != null) {
/* 393 */           q.clear();
/* 394 */           a.onError(ex);
/* 395 */           return true;
/* 396 */         }  if (empty) {
/* 397 */           a.onComplete();
/* 398 */           return true;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 403 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 408 */       if ((mode & 0x2) != 0) {
/* 409 */         this.outputFused = true;
/* 410 */         return 2;
/*     */       } 
/* 412 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public GroupedFlowable<K, V> poll() {
/* 418 */       return (GroupedFlowable<K, V>)this.queue.poll();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 423 */       this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 428 */       return this.queue.isEmpty();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EvictionAction<K, V>
/*     */     implements Consumer<GroupedUnicast<K, V>> {
/*     */     final Queue<FlowableGroupBy.GroupedUnicast<K, V>> evictedGroups;
/*     */     
/*     */     EvictionAction(Queue<FlowableGroupBy.GroupedUnicast<K, V>> evictedGroups) {
/* 437 */       this.evictedGroups = evictedGroups;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(FlowableGroupBy.GroupedUnicast<K, V> value) {
/* 442 */       this.evictedGroups.offer(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class GroupedUnicast<K, T>
/*     */     extends GroupedFlowable<K, T> {
/*     */     final FlowableGroupBy.State<T, K> state;
/*     */     
/*     */     public static <T, K> GroupedUnicast<K, T> createWith(K key, int bufferSize, FlowableGroupBy.GroupBySubscriber<?, K, T> parent, boolean delayError) {
/* 451 */       FlowableGroupBy.State<T, K> state = new FlowableGroupBy.State<T, K>(bufferSize, parent, key, delayError);
/* 452 */       return new GroupedUnicast<K, T>(key, state);
/*     */     }
/*     */     
/*     */     protected GroupedUnicast(K key, FlowableGroupBy.State<T, K> state) {
/* 456 */       super(key);
/* 457 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void subscribeActual(Subscriber<? super T> s) {
/* 462 */       this.state.subscribe(s);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/* 466 */       this.state.onNext(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/* 470 */       this.state.onError(e);
/*     */     }
/*     */     
/*     */     public void onComplete() {
/* 474 */       this.state.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class State<T, K>
/*     */     extends BasicIntQueueSubscription<T>
/*     */     implements Publisher<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3852313036005250360L;
/*     */     final K key;
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     final FlowableGroupBy.GroupBySubscriber<?, K, T> parent;
/*     */     final boolean delayError;
/* 487 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Throwable error;
/* 492 */     final AtomicBoolean cancelled = new AtomicBoolean();
/*     */     
/* 494 */     final AtomicReference<Subscriber<? super T>> actual = new AtomicReference<Subscriber<? super T>>();
/*     */     
/* 496 */     final AtomicBoolean once = new AtomicBoolean();
/*     */     
/*     */     boolean outputFused;
/*     */     
/*     */     int produced;
/*     */     
/*     */     State(int bufferSize, FlowableGroupBy.GroupBySubscriber<?, K, T> parent, K key, boolean delayError) {
/* 503 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/* 504 */       this.parent = parent;
/* 505 */       this.key = key;
/* 506 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 511 */       if (SubscriptionHelper.validate(n)) {
/* 512 */         BackpressureHelper.add(this.requested, n);
/* 513 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 519 */       if (this.cancelled.compareAndSet(false, true)) {
/* 520 */         this.parent.cancel(this.key);
/* 521 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void subscribe(Subscriber<? super T> s) {
/* 527 */       if (this.once.compareAndSet(false, true)) {
/* 528 */         s.onSubscribe((Subscription)this);
/* 529 */         this.actual.lazySet(s);
/* 530 */         drain();
/*     */       } else {
/* 532 */         EmptySubscription.error(new IllegalStateException("Only one Subscriber allowed!"), s);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/* 537 */       this.queue.offer(t);
/* 538 */       drain();
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/* 542 */       this.error = e;
/* 543 */       this.done = true;
/* 544 */       drain();
/*     */     }
/*     */     
/*     */     public void onComplete() {
/* 548 */       this.done = true;
/* 549 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 553 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/* 556 */       if (this.outputFused) {
/* 557 */         drainFused();
/*     */       } else {
/* 559 */         drainNormal();
/*     */       } 
/*     */     }
/*     */     
/*     */     void drainFused() {
/* 564 */       int missed = 1;
/*     */       
/* 566 */       SpscLinkedArrayQueue<T> q = this.queue;
/* 567 */       Subscriber<? super T> a = this.actual.get();
/*     */       
/*     */       while (true) {
/* 570 */         if (a != null) {
/* 571 */           if (this.cancelled.get()) {
/*     */             return;
/*     */           }
/*     */           
/* 575 */           boolean d = this.done;
/*     */           
/* 577 */           if (d && !this.delayError) {
/* 578 */             Throwable ex = this.error;
/* 579 */             if (ex != null) {
/* 580 */               q.clear();
/* 581 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 586 */           a.onNext(null);
/*     */           
/* 588 */           if (d) {
/* 589 */             Throwable ex = this.error;
/* 590 */             if (ex != null) {
/* 591 */               a.onError(ex);
/*     */             } else {
/* 593 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 599 */         missed = addAndGet(-missed);
/* 600 */         if (missed == 0) {
/*     */           return;
/*     */         }
/*     */         
/* 604 */         if (a == null) {
/* 605 */           a = this.actual.get();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drainNormal() {
/* 611 */       int missed = 1;
/*     */       
/* 613 */       SpscLinkedArrayQueue<T> q = this.queue;
/* 614 */       boolean delayError = this.delayError;
/* 615 */       Subscriber<? super T> a = this.actual.get();
/*     */       while (true) {
/* 617 */         if (a != null) {
/* 618 */           long r = this.requested.get();
/* 619 */           long e = 0L;
/*     */           
/* 621 */           while (e != r) {
/* 622 */             boolean d = this.done;
/* 623 */             T v = (T)q.poll();
/* 624 */             boolean empty = (v == null);
/*     */             
/* 626 */             if (checkTerminated(d, empty, a, delayError, e)) {
/*     */               return;
/*     */             }
/*     */             
/* 630 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 634 */             a.onNext(v);
/*     */             
/* 636 */             e++;
/*     */           } 
/*     */           
/* 639 */           if (e == r && checkTerminated(this.done, q.isEmpty(), a, delayError, e)) {
/*     */             return;
/*     */           }
/*     */           
/* 643 */           if (e != 0L) {
/* 644 */             if (r != Long.MAX_VALUE) {
/* 645 */               this.requested.addAndGet(-e);
/*     */             }
/* 647 */             this.parent.upstream.request(e);
/*     */           } 
/*     */         } 
/*     */         
/* 651 */         missed = addAndGet(-missed);
/* 652 */         if (missed == 0) {
/*     */           break;
/*     */         }
/* 655 */         if (a == null) {
/* 656 */           a = this.actual.get();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<? super T> a, boolean delayError, long emitted) {
/* 662 */       if (this.cancelled.get()) {
/*     */         
/* 664 */         while (this.queue.poll() != null) {
/* 665 */           emitted++;
/*     */         }
/* 667 */         if (emitted != 0L) {
/* 668 */           this.parent.upstream.request(emitted);
/*     */         }
/* 670 */         return true;
/*     */       } 
/*     */       
/* 673 */       if (d) {
/* 674 */         if (delayError) {
/* 675 */           if (empty) {
/* 676 */             Throwable e = this.error;
/* 677 */             if (e != null) {
/* 678 */               a.onError(e);
/*     */             } else {
/* 680 */               a.onComplete();
/*     */             } 
/* 682 */             return true;
/*     */           } 
/*     */         } else {
/* 685 */           Throwable e = this.error;
/* 686 */           if (e != null) {
/* 687 */             this.queue.clear();
/* 688 */             a.onError(e);
/* 689 */             return true;
/*     */           } 
/* 691 */           if (empty) {
/* 692 */             a.onComplete();
/* 693 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 698 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 703 */       if ((mode & 0x2) != 0) {
/* 704 */         this.outputFused = true;
/* 705 */         return 2;
/*     */       } 
/* 707 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() {
/* 713 */       T v = (T)this.queue.poll();
/* 714 */       if (v != null) {
/* 715 */         this.produced++;
/* 716 */         return v;
/*     */       } 
/* 718 */       tryReplenish();
/* 719 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 724 */       if (this.queue.isEmpty()) {
/* 725 */         tryReplenish();
/* 726 */         return true;
/*     */       } 
/* 728 */       return false;
/*     */     }
/*     */     
/*     */     void tryReplenish() {
/* 732 */       int p = this.produced;
/* 733 */       if (p != 0) {
/* 734 */         this.produced = 0;
/* 735 */         this.parent.upstream.request(p);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 742 */       SpscLinkedArrayQueue<T> q = this.queue;
/* 743 */       while (q.poll() != null) {
/* 744 */         this.produced++;
/*     */       }
/* 746 */       tryReplenish();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableGroupBy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */