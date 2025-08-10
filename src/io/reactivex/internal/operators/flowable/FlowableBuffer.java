/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BooleanSupplier;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class FlowableBuffer<T, C extends Collection<? super T>>
/*     */   extends AbstractFlowableWithUpstream<T, C>
/*     */ {
/*     */   final int size;
/*     */   final int skip;
/*     */   final Callable<C> bufferSupplier;
/*     */   
/*     */   public FlowableBuffer(Flowable<T> source, int size, int skip, Callable<C> bufferSupplier) {
/*  38 */     super(source);
/*  39 */     this.size = size;
/*  40 */     this.skip = skip;
/*  41 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super C> s) {
/*  46 */     if (this.size == this.skip) {
/*  47 */       this.source.subscribe(new PublisherBufferExactSubscriber<Object, C>(s, this.size, this.bufferSupplier));
/*  48 */     } else if (this.skip > this.size) {
/*  49 */       this.source.subscribe(new PublisherBufferSkipSubscriber<Object, C>(s, this.size, this.skip, this.bufferSupplier));
/*     */     } else {
/*  51 */       this.source.subscribe(new PublisherBufferOverlappingSubscriber<Object, C>(s, this.size, this.skip, this.bufferSupplier));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class PublisherBufferExactSubscriber<T, C extends Collection<? super T>>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super C> downstream;
/*     */     
/*     */     final Callable<C> bufferSupplier;
/*     */     
/*     */     final int size;
/*     */     
/*     */     C buffer;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     boolean done;
/*     */     int index;
/*     */     
/*     */     PublisherBufferExactSubscriber(Subscriber<? super C> actual, int size, Callable<C> bufferSupplier) {
/*  73 */       this.downstream = actual;
/*  74 */       this.size = size;
/*  75 */       this.bufferSupplier = bufferSupplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  80 */       if (SubscriptionHelper.validate(n)) {
/*  81 */         this.upstream.request(BackpressureHelper.multiplyCap(n, this.size));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  87 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  92 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  93 */         this.upstream = s;
/*     */         
/*  95 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       Collection<T> collection;
/* 101 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 105 */       C b = this.buffer;
/* 106 */       if (b == null) {
/*     */         
/*     */         try {
/* 109 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
/* 110 */         } catch (Throwable e) {
/* 111 */           Exceptions.throwIfFatal(e);
/* 112 */           cancel();
/* 113 */           onError(e);
/*     */           
/*     */           return;
/*     */         } 
/* 117 */         this.buffer = (C)collection;
/*     */       } 
/*     */       
/* 120 */       collection.add(t);
/*     */       
/* 122 */       int i = this.index + 1;
/* 123 */       if (i == this.size) {
/* 124 */         this.index = 0;
/* 125 */         this.buffer = null;
/* 126 */         this.downstream.onNext(collection);
/*     */       } else {
/* 128 */         this.index = i;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 134 */       if (this.done) {
/* 135 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 138 */       this.done = true;
/* 139 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 144 */       if (this.done) {
/*     */         return;
/*     */       }
/* 147 */       this.done = true;
/*     */       
/* 149 */       C b = this.buffer;
/*     */       
/* 151 */       if (b != null && !b.isEmpty()) {
/* 152 */         this.downstream.onNext(b);
/*     */       }
/* 154 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class PublisherBufferSkipSubscriber<T, C extends Collection<? super T>>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -5616169793639412593L;
/*     */     
/*     */     final Subscriber<? super C> downstream;
/*     */     
/*     */     final Callable<C> bufferSupplier;
/*     */     
/*     */     final int size;
/*     */     
/*     */     final int skip;
/*     */     
/*     */     C buffer;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     boolean done;
/*     */     
/*     */     int index;
/*     */     
/*     */     PublisherBufferSkipSubscriber(Subscriber<? super C> actual, int size, int skip, Callable<C> bufferSupplier) {
/* 182 */       this.downstream = actual;
/* 183 */       this.size = size;
/* 184 */       this.skip = skip;
/* 185 */       this.bufferSupplier = bufferSupplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 190 */       if (SubscriptionHelper.validate(n)) {
/* 191 */         if (get() == 0 && compareAndSet(0, 1)) {
/*     */           
/* 193 */           long u = BackpressureHelper.multiplyCap(n, this.size);
/*     */           
/* 195 */           long v = BackpressureHelper.multiplyCap((this.skip - this.size), n - 1L);
/*     */           
/* 197 */           this.upstream.request(BackpressureHelper.addCap(u, v));
/*     */         } else {
/*     */           
/* 200 */           this.upstream.request(BackpressureHelper.multiplyCap(this.skip, n));
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 207 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 212 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 213 */         this.upstream = s;
/*     */         
/* 215 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       Collection<T> collection;
/* 221 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 225 */       C b = this.buffer;
/*     */       
/* 227 */       int i = this.index;
/*     */       
/* 229 */       if (i++ == 0) {
/*     */         try {
/* 231 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
/* 232 */         } catch (Throwable e) {
/* 233 */           Exceptions.throwIfFatal(e);
/* 234 */           cancel();
/*     */           
/* 236 */           onError(e);
/*     */           
/*     */           return;
/*     */         } 
/* 240 */         this.buffer = (C)collection;
/*     */       } 
/*     */       
/* 243 */       if (collection != null) {
/* 244 */         collection.add(t);
/* 245 */         if (collection.size() == this.size) {
/* 246 */           this.buffer = null;
/* 247 */           this.downstream.onNext(collection);
/*     */         } 
/*     */       } 
/*     */       
/* 251 */       if (i == this.skip) {
/* 252 */         i = 0;
/*     */       }
/* 254 */       this.index = i;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 259 */       if (this.done) {
/* 260 */         RxJavaPlugins.onError(t);
/*     */         
/*     */         return;
/*     */       } 
/* 264 */       this.done = true;
/* 265 */       this.buffer = null;
/*     */       
/* 267 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 272 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 276 */       this.done = true;
/* 277 */       C b = this.buffer;
/* 278 */       this.buffer = null;
/*     */       
/* 280 */       if (b != null) {
/* 281 */         this.downstream.onNext(b);
/*     */       }
/*     */       
/* 284 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class PublisherBufferOverlappingSubscriber<T, C extends Collection<? super T>>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription, BooleanSupplier
/*     */   {
/*     */     private static final long serialVersionUID = -7370244972039324525L;
/*     */     
/*     */     final Subscriber<? super C> downstream;
/*     */     
/*     */     final Callable<C> bufferSupplier;
/*     */     
/*     */     final int size;
/*     */     
/*     */     final int skip;
/*     */     
/*     */     final ArrayDeque<C> buffers;
/*     */     
/*     */     final AtomicBoolean once;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     boolean done;
/*     */     
/*     */     int index;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     long produced;
/*     */     
/*     */     PublisherBufferOverlappingSubscriber(Subscriber<? super C> actual, int size, int skip, Callable<C> bufferSupplier) {
/* 318 */       this.downstream = actual;
/* 319 */       this.size = size;
/* 320 */       this.skip = skip;
/* 321 */       this.bufferSupplier = bufferSupplier;
/* 322 */       this.once = new AtomicBoolean();
/* 323 */       this.buffers = new ArrayDeque<C>();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getAsBoolean() {
/* 328 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 333 */       if (SubscriptionHelper.validate(n)) {
/* 334 */         if (QueueDrainHelper.postCompleteRequest(n, this.downstream, this.buffers, this, this)) {
/*     */           return;
/*     */         }
/*     */         
/* 338 */         if (!this.once.get() && this.once.compareAndSet(false, true)) {
/*     */           
/* 340 */           long u = BackpressureHelper.multiplyCap(this.skip, n - 1L);
/*     */ 
/*     */           
/* 343 */           long r = BackpressureHelper.addCap(this.size, u);
/* 344 */           this.upstream.request(r);
/*     */         } else {
/*     */           
/* 347 */           long r = BackpressureHelper.multiplyCap(this.skip, n);
/* 348 */           this.upstream.request(r);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 355 */       this.cancelled = true;
/* 356 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 361 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 362 */         this.upstream = s;
/*     */         
/* 364 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 370 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 374 */       ArrayDeque<C> bs = this.buffers;
/*     */       
/* 376 */       int i = this.index;
/*     */       
/* 378 */       if (i++ == 0) {
/*     */         Collection collection1;
/*     */         
/*     */         try {
/* 382 */           collection1 = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
/* 383 */         } catch (Throwable e) {
/* 384 */           Exceptions.throwIfFatal(e);
/* 385 */           cancel();
/* 386 */           onError(e);
/*     */           
/*     */           return;
/*     */         } 
/* 390 */         bs.offer((C)collection1);
/*     */       } 
/*     */       
/* 393 */       Collection<T> collection = (Collection)bs.peek();
/*     */       
/* 395 */       if (collection != null && collection.size() + 1 == this.size) {
/* 396 */         bs.poll();
/*     */         
/* 398 */         collection.add(t);
/*     */         
/* 400 */         this.produced++;
/*     */         
/* 402 */         this.downstream.onNext(collection);
/*     */       } 
/*     */       
/* 405 */       for (Collection<T> collection1 : bs) {
/* 406 */         collection1.add(t);
/*     */       }
/*     */       
/* 409 */       if (i == this.skip) {
/* 410 */         i = 0;
/*     */       }
/* 412 */       this.index = i;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 417 */       if (this.done) {
/* 418 */         RxJavaPlugins.onError(t);
/*     */         
/*     */         return;
/*     */       } 
/* 422 */       this.done = true;
/* 423 */       this.buffers.clear();
/*     */       
/* 425 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 430 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 434 */       this.done = true;
/*     */       
/* 436 */       long p = this.produced;
/* 437 */       if (p != 0L) {
/* 438 */         BackpressureHelper.produced(this, p);
/*     */       }
/* 440 */       QueueDrainHelper.postComplete(this.downstream, this.buffers, this, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */