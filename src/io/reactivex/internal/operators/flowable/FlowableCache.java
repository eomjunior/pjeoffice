/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
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
/*     */ public final class FlowableCache<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */   implements FlowableSubscriber<T>
/*     */ {
/*     */   final AtomicBoolean once;
/*     */   final int capacityHint;
/*     */   final AtomicReference<CacheSubscription<T>[]> subscribers;
/*  54 */   static final CacheSubscription[] EMPTY = new CacheSubscription[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   static final CacheSubscription[] TERMINATED = new CacheSubscription[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile long size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Node<T> head;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Node<T> tail;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int tailOffset;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile boolean done;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlowableCache(Flowable<T> source, int capacityHint) {
/*  99 */     super(source);
/* 100 */     this.capacityHint = capacityHint;
/* 101 */     this.once = new AtomicBoolean();
/* 102 */     Node<T> n = new Node<T>(capacityHint);
/* 103 */     this.head = n;
/* 104 */     this.tail = n;
/* 105 */     this.subscribers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> t) {
/* 110 */     CacheSubscription<T> consumer = new CacheSubscription<T>(t, this);
/* 111 */     t.onSubscribe(consumer);
/* 112 */     add(consumer);
/*     */     
/* 114 */     if (!this.once.get() && this.once.compareAndSet(false, true)) {
/* 115 */       this.source.subscribe(this);
/*     */     } else {
/* 117 */       replay(consumer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isConnected() {
/* 126 */     return this.once.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasSubscribers() {
/* 134 */     return (((CacheSubscription[])this.subscribers.get()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long cachedEventCount() {
/* 142 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void add(CacheSubscription<T> consumer) {
/*     */     CacheSubscription[] arrayOfCacheSubscription1;
/*     */     CacheSubscription[] arrayOfCacheSubscription2;
/*     */     do {
/* 152 */       arrayOfCacheSubscription1 = (CacheSubscription[])this.subscribers.get();
/* 153 */       if (arrayOfCacheSubscription1 == TERMINATED) {
/*     */         return;
/*     */       }
/* 156 */       int n = arrayOfCacheSubscription1.length;
/*     */ 
/*     */       
/* 159 */       arrayOfCacheSubscription2 = new CacheSubscription[n + 1];
/* 160 */       System.arraycopy(arrayOfCacheSubscription1, 0, arrayOfCacheSubscription2, 0, n);
/* 161 */       arrayOfCacheSubscription2[n] = consumer;
/*     */     }
/* 163 */     while (!this.subscribers.compareAndSet(arrayOfCacheSubscription1, arrayOfCacheSubscription2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(CacheSubscription<T> consumer) {
/*     */     CacheSubscription[] arrayOfCacheSubscription1;
/*     */     CacheSubscription[] arrayOfCacheSubscription2;
/*     */     do {
/* 176 */       arrayOfCacheSubscription1 = (CacheSubscription[])this.subscribers.get();
/* 177 */       int n = arrayOfCacheSubscription1.length;
/* 178 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 182 */       int j = -1;
/* 183 */       for (int i = 0; i < n; i++) {
/* 184 */         if (arrayOfCacheSubscription1[i] == consumer) {
/* 185 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 190 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 195 */       if (n == 1) {
/* 196 */         arrayOfCacheSubscription2 = EMPTY;
/*     */       } else {
/* 198 */         arrayOfCacheSubscription2 = new CacheSubscription[n - 1];
/* 199 */         System.arraycopy(arrayOfCacheSubscription1, 0, arrayOfCacheSubscription2, 0, j);
/* 200 */         System.arraycopy(arrayOfCacheSubscription1, j + 1, arrayOfCacheSubscription2, j, n - j - 1);
/*     */       }
/*     */     
/* 203 */     } while (!this.subscribers.compareAndSet(arrayOfCacheSubscription1, arrayOfCacheSubscription2));
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
/*     */   void replay(CacheSubscription<T> consumer) {
/* 216 */     if (consumer.getAndIncrement() != 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 221 */     int missed = 1;
/*     */     
/* 223 */     long index = consumer.index;
/* 224 */     int offset = consumer.offset;
/* 225 */     Node<T> node = consumer.node;
/* 226 */     AtomicLong requested = consumer.requested;
/* 227 */     Subscriber<? super T> downstream = consumer.downstream;
/* 228 */     int capacity = this.capacityHint;
/*     */ 
/*     */     
/*     */     while (true) {
/* 232 */       boolean sourceDone = this.done;
/*     */       
/* 234 */       boolean empty = (this.size == index);
/*     */ 
/*     */       
/* 237 */       if (sourceDone && empty) {
/*     */         
/* 239 */         consumer.node = null;
/*     */         
/* 241 */         Throwable ex = this.error;
/* 242 */         if (ex != null) {
/* 243 */           downstream.onError(ex);
/*     */         } else {
/* 245 */           downstream.onComplete();
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 251 */       if (!empty) {
/*     */         
/* 253 */         long consumerRequested = requested.get();
/*     */         
/* 255 */         if (consumerRequested == Long.MIN_VALUE) {
/*     */           
/* 257 */           consumer.node = null;
/*     */           
/*     */           return;
/*     */         } 
/* 261 */         if (consumerRequested != index) {
/*     */ 
/*     */           
/* 264 */           if (offset == capacity) {
/*     */             
/* 266 */             node = node.next;
/*     */             
/* 268 */             offset = 0;
/*     */           } 
/*     */ 
/*     */           
/* 272 */           downstream.onNext(node.values[offset]);
/*     */ 
/*     */           
/* 275 */           offset++;
/*     */           
/* 277 */           index++;
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 285 */       consumer.index = index;
/* 286 */       consumer.offset = offset;
/* 287 */       consumer.node = node;
/*     */       
/* 289 */       missed = consumer.addAndGet(-missed);
/* 290 */       if (missed == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 298 */     s.request(Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 303 */     int tailOffset = this.tailOffset;
/*     */     
/* 305 */     if (tailOffset == this.capacityHint) {
/* 306 */       Node<T> n = new Node<T>(tailOffset);
/* 307 */       n.values[0] = t;
/* 308 */       this.tailOffset = 1;
/* 309 */       this.tail.next = n;
/* 310 */       this.tail = n;
/*     */     } else {
/* 312 */       this.tail.values[tailOffset] = t;
/* 313 */       this.tailOffset = tailOffset + 1;
/*     */     } 
/* 315 */     this.size++;
/* 316 */     for (CacheSubscription<T> consumer : (CacheSubscription[])this.subscribers.get()) {
/* 317 */       replay(consumer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 324 */     if (this.done) {
/* 325 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 328 */     this.error = t;
/* 329 */     this.done = true;
/* 330 */     for (CacheSubscription<T> consumer : (CacheSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 331 */       replay(consumer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 338 */     this.done = true;
/* 339 */     for (CacheSubscription<T> consumer : (CacheSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 340 */       replay(consumer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CacheSubscription<T>
/*     */     extends AtomicInteger
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 6770240836423125754L;
/*     */ 
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */ 
/*     */     
/*     */     final FlowableCache<T> parent;
/*     */ 
/*     */     
/*     */     final AtomicLong requested;
/*     */ 
/*     */     
/*     */     FlowableCache.Node<T> node;
/*     */ 
/*     */     
/*     */     int offset;
/*     */ 
/*     */     
/*     */     long index;
/*     */ 
/*     */ 
/*     */     
/*     */     CacheSubscription(Subscriber<? super T> downstream, FlowableCache<T> parent) {
/* 373 */       this.downstream = downstream;
/* 374 */       this.parent = parent;
/* 375 */       this.node = parent.head;
/* 376 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 381 */       if (SubscriptionHelper.validate(n)) {
/* 382 */         BackpressureHelper.addCancel(this.requested, n);
/* 383 */         this.parent.replay(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 389 */       if (this.requested.getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
/* 390 */         this.parent.remove(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Node<T>
/*     */   {
/*     */     final T[] values;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     volatile Node<T> next;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Node(int capacityHint) {
/* 414 */       this.values = (T[])new Object[capacityHint];
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */