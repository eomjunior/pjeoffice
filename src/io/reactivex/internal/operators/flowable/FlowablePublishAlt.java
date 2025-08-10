/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.flowables.ConnectableFlowable;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.ResettableConnectable;
/*     */ import io.reactivex.internal.fuseable.HasUpstreamPublisher;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowablePublishAlt<T>
/*     */   extends ConnectableFlowable<T>
/*     */   implements HasUpstreamPublisher<T>, ResettableConnectable
/*     */ {
/*     */   final Publisher<T> source;
/*     */   final int bufferSize;
/*     */   final AtomicReference<PublishConnection<T>> current;
/*     */   
/*     */   public FlowablePublishAlt(Publisher<T> source, int bufferSize) {
/*  54 */     this.source = source;
/*  55 */     this.bufferSize = bufferSize;
/*  56 */     this.current = new AtomicReference<PublishConnection<T>>();
/*     */   }
/*     */ 
/*     */   
/*     */   public Publisher<T> source() {
/*  61 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int publishBufferSize() {
/*  69 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect(Consumer<? super Disposable> connection) {
/*     */     PublishConnection<T> conn;
/*  75 */     boolean doConnect = false;
/*     */     
/*     */     while (true) {
/*  78 */       conn = this.current.get();
/*     */       
/*  80 */       if (conn == null || conn.isDisposed()) {
/*  81 */         PublishConnection<T> fresh = new PublishConnection<T>(this.current, this.bufferSize);
/*  82 */         if (!this.current.compareAndSet(conn, fresh)) {
/*     */           continue;
/*     */         }
/*  85 */         conn = fresh;
/*     */       }  break;
/*     */     } 
/*  88 */     doConnect = (!conn.connect.get() && conn.connect.compareAndSet(false, true));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  93 */       connection.accept(conn);
/*  94 */     } catch (Throwable ex) {
/*  95 */       Exceptions.throwIfFatal(ex);
/*  96 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     } 
/*     */     
/*  99 */     if (doConnect) {
/* 100 */       this.source.subscribe((Subscriber)conn);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*     */     PublishConnection<T> conn;
/*     */     while (true) {
/* 109 */       conn = this.current.get();
/*     */ 
/*     */       
/* 112 */       if (conn == null) {
/* 113 */         PublishConnection<T> fresh = new PublishConnection<T>(this.current, this.bufferSize);
/* 114 */         if (!this.current.compareAndSet(conn, fresh)) {
/*     */           continue;
/*     */         }
/* 117 */         conn = fresh;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/*     */     
/* 123 */     InnerSubscription<T> inner = new InnerSubscription<T>(s, conn);
/* 124 */     s.onSubscribe(inner);
/*     */     
/* 126 */     if (conn.add(inner)) {
/* 127 */       if (inner.isCancelled()) {
/* 128 */         conn.remove(inner);
/*     */       } else {
/* 130 */         conn.drain();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 135 */     Throwable ex = conn.error;
/* 136 */     if (ex != null) {
/* 137 */       s.onError(ex);
/*     */     } else {
/* 139 */       s.onComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetIf(Disposable connection) {
/* 146 */     this.current.compareAndSet((PublishConnection<T>)connection, null);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class PublishConnection<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -1672047311619175801L;
/*     */     
/*     */     final AtomicReference<PublishConnection<T>> current;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicBoolean connect;
/*     */     
/*     */     final AtomicReference<FlowablePublishAlt.InnerSubscription<T>[]> subscribers;
/*     */     
/*     */     final int bufferSize;
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
/* 175 */     static final FlowablePublishAlt.InnerSubscription[] EMPTY = new FlowablePublishAlt.InnerSubscription[0];
/*     */     
/* 177 */     static final FlowablePublishAlt.InnerSubscription[] TERMINATED = new FlowablePublishAlt.InnerSubscription[0];
/*     */ 
/*     */     
/*     */     PublishConnection(AtomicReference<PublishConnection<T>> current, int bufferSize) {
/* 181 */       this.current = current;
/* 182 */       this.upstream = new AtomicReference<Subscription>();
/* 183 */       this.connect = new AtomicBoolean();
/* 184 */       this.bufferSize = bufferSize;
/* 185 */       this.subscribers = new AtomicReference(EMPTY);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 191 */       this.subscribers.getAndSet(TERMINATED);
/* 192 */       this.current.compareAndSet(this, null);
/* 193 */       SubscriptionHelper.cancel(this.upstream);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 198 */       return (this.subscribers.get() == TERMINATED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 203 */       if (SubscriptionHelper.setOnce(this.upstream, s)) {
/* 204 */         if (s instanceof QueueSubscription) {
/*     */           
/* 206 */           QueueSubscription<T> qs = (QueueSubscription<T>)s;
/*     */           
/* 208 */           int m = qs.requestFusion(7);
/* 209 */           if (m == 1) {
/* 210 */             this.sourceMode = m;
/* 211 */             this.queue = (SimpleQueue<T>)qs;
/* 212 */             this.done = true;
/* 213 */             drain();
/*     */             return;
/*     */           } 
/* 216 */           if (m == 2) {
/* 217 */             this.sourceMode = m;
/* 218 */             this.queue = (SimpleQueue<T>)qs;
/* 219 */             s.request(this.bufferSize);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 224 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.bufferSize);
/*     */         
/* 226 */         s.request(this.bufferSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 233 */       if (this.sourceMode == 0 && !this.queue.offer(t)) {
/* 234 */         onError((Throwable)new MissingBackpressureException("Prefetch queue is full?!"));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 239 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 244 */       if (this.done) {
/* 245 */         RxJavaPlugins.onError(t);
/*     */       } else {
/* 247 */         this.error = t;
/* 248 */         this.done = true;
/* 249 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 255 */       this.done = true;
/* 256 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 260 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 264 */       int missed = 1;
/* 265 */       SimpleQueue<T> queue = this.queue;
/* 266 */       int consumed = this.consumed;
/* 267 */       int limit = this.bufferSize - (this.bufferSize >> 2);
/* 268 */       boolean async = (this.sourceMode != 1);
/*     */ 
/*     */       
/*     */       label60: while (true) {
/* 272 */         if (queue != null) {
/* 273 */           long minDemand = Long.MAX_VALUE;
/* 274 */           boolean hasDemand = false;
/*     */           
/* 276 */           FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription = (FlowablePublishAlt.InnerSubscription[])this.subscribers.get();
/*     */           
/* 278 */           for (FlowablePublishAlt.InnerSubscription<T> inner : arrayOfInnerSubscription) {
/* 279 */             long request = inner.get();
/* 280 */             if (request != Long.MIN_VALUE) {
/* 281 */               hasDemand = true;
/* 282 */               minDemand = Math.min(request - inner.emitted, minDemand);
/*     */             } 
/*     */           } 
/*     */           
/* 286 */           if (!hasDemand) {
/* 287 */             minDemand = 0L;
/*     */           }
/*     */           
/* 290 */           while (minDemand != 0L) {
/* 291 */             T v; boolean d = this.done;
/*     */ 
/*     */             
/*     */             try {
/* 295 */               v = (T)queue.poll();
/* 296 */             } catch (Throwable ex) {
/* 297 */               Exceptions.throwIfFatal(ex);
/* 298 */               ((Subscription)this.upstream.get()).cancel();
/* 299 */               queue.clear();
/* 300 */               this.done = true;
/* 301 */               signalError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 305 */             boolean empty = (v == null);
/*     */             
/* 307 */             if (checkTerminated(d, empty)) {
/*     */               return;
/*     */             }
/*     */             
/* 311 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 315 */             for (FlowablePublishAlt.InnerSubscription<T> inner : arrayOfInnerSubscription) {
/* 316 */               if (!inner.isCancelled()) {
/* 317 */                 inner.downstream.onNext(v);
/* 318 */                 inner.emitted++;
/*     */               } 
/*     */             } 
/*     */             
/* 322 */             if (async && ++consumed == limit) {
/* 323 */               consumed = 0;
/* 324 */               ((Subscription)this.upstream.get()).request(limit);
/*     */             } 
/* 326 */             minDemand--;
/*     */             
/* 328 */             if (arrayOfInnerSubscription != this.subscribers.get()) {
/*     */               continue label60;
/*     */             }
/*     */           } 
/*     */           
/* 333 */           if (checkTerminated(this.done, queue.isEmpty())) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */         
/* 338 */         this.consumed = consumed;
/* 339 */         missed = addAndGet(-missed);
/* 340 */         if (missed == 0) {
/*     */           break;
/*     */         }
/* 343 */         if (queue == null) {
/* 344 */           queue = this.queue;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     boolean checkTerminated(boolean isDone, boolean isEmpty) {
/* 351 */       if (isDone && isEmpty) {
/* 352 */         Throwable ex = this.error;
/*     */         
/* 354 */         if (ex != null) {
/* 355 */           signalError(ex);
/*     */         } else {
/* 357 */           for (FlowablePublishAlt.InnerSubscription<T> inner : (FlowablePublishAlt.InnerSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 358 */             if (!inner.isCancelled()) {
/* 359 */               inner.downstream.onComplete();
/*     */             }
/*     */           } 
/*     */         } 
/* 363 */         return true;
/*     */       } 
/* 365 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     void signalError(Throwable ex) {
/* 370 */       for (FlowablePublishAlt.InnerSubscription<T> inner : (FlowablePublishAlt.InnerSubscription[])this.subscribers.getAndSet(TERMINATED)) {
/* 371 */         if (!inner.isCancelled()) {
/* 372 */           inner.downstream.onError(ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean add(FlowablePublishAlt.InnerSubscription<T> inner) {
/*     */       while (true) {
/* 381 */         FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription1 = (FlowablePublishAlt.InnerSubscription[])this.subscribers.get();
/*     */ 
/*     */         
/* 384 */         if (arrayOfInnerSubscription1 == TERMINATED) {
/* 385 */           return false;
/*     */         }
/*     */         
/* 388 */         int len = arrayOfInnerSubscription1.length;
/*     */         
/* 390 */         FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription2 = new FlowablePublishAlt.InnerSubscription[len + 1];
/* 391 */         System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, len);
/* 392 */         arrayOfInnerSubscription2[len] = inner;
/*     */         
/* 394 */         if (this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2)) {
/* 395 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void remove(FlowablePublishAlt.InnerSubscription<T> inner) {
/*     */       FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription1;
/*     */       FlowablePublishAlt.InnerSubscription[] arrayOfInnerSubscription2;
/*     */       do {
/* 407 */         arrayOfInnerSubscription1 = (FlowablePublishAlt.InnerSubscription[])this.subscribers.get();
/* 408 */         int len = arrayOfInnerSubscription1.length;
/*     */         
/* 410 */         if (len == 0) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 415 */         int j = -1;
/* 416 */         for (int i = 0; i < len; i++) {
/* 417 */           if (arrayOfInnerSubscription1[i] == inner) {
/* 418 */             j = i;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 423 */         if (j < 0) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 430 */         if (len == 1) {
/* 431 */           arrayOfInnerSubscription2 = EMPTY;
/*     */         } else {
/*     */           
/* 434 */           arrayOfInnerSubscription2 = new FlowablePublishAlt.InnerSubscription[len - 1];
/*     */           
/* 436 */           System.arraycopy(arrayOfInnerSubscription1, 0, arrayOfInnerSubscription2, 0, j);
/*     */           
/* 438 */           System.arraycopy(arrayOfInnerSubscription1, j + 1, arrayOfInnerSubscription2, j, len - j - 1);
/*     */         }
/*     */       
/* 441 */       } while (!this.subscribers.compareAndSet(arrayOfInnerSubscription1, arrayOfInnerSubscription2));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class InnerSubscription<T>
/*     */     extends AtomicLong
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 2845000326761540265L;
/*     */ 
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final FlowablePublishAlt.PublishConnection<T> parent;
/*     */     
/*     */     long emitted;
/*     */ 
/*     */     
/*     */     InnerSubscription(Subscriber<? super T> downstream, FlowablePublishAlt.PublishConnection<T> parent) {
/* 462 */       this.downstream = downstream;
/* 463 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 468 */       BackpressureHelper.addCancel(this, n);
/* 469 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 474 */       if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
/* 475 */         this.parent.remove(this);
/* 476 */         this.parent.drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isCancelled() {
/* 481 */       return (get() == Long.MIN_VALUE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowablePublishAlt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */