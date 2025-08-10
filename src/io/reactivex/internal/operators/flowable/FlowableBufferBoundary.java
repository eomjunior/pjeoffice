/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableBufferBoundary<T, U extends Collection<? super T>, Open, Close>
/*     */   extends AbstractFlowableWithUpstream<T, U>
/*     */ {
/*     */   final Callable<U> bufferSupplier;
/*     */   final Publisher<? extends Open> bufferOpen;
/*     */   final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
/*     */   
/*     */   public FlowableBufferBoundary(Flowable<T> source, Publisher<? extends Open> bufferOpen, Function<? super Open, ? extends Publisher<? extends Close>> bufferClose, Callable<U> bufferSupplier) {
/*  40 */     super(source);
/*  41 */     this.bufferOpen = bufferOpen;
/*  42 */     this.bufferClose = bufferClose;
/*  43 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super U> s) {
/*  48 */     BufferBoundarySubscriber<T, U, Open, Close> parent = new BufferBoundarySubscriber<T, U, Open, Close>(s, this.bufferOpen, this.bufferClose, this.bufferSupplier);
/*     */ 
/*     */ 
/*     */     
/*  52 */     s.onSubscribe(parent);
/*  53 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class BufferBoundarySubscriber<T, C extends Collection<? super T>, Open, Close>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -8466418554264089604L;
/*     */     
/*     */     final Subscriber<? super C> downstream;
/*     */     
/*     */     final Callable<C> bufferSupplier;
/*     */     
/*     */     final Publisher<? extends Open> bufferOpen;
/*     */     
/*     */     final Function<? super Open, ? extends Publisher<? extends Close>> bufferClose;
/*     */     
/*     */     final CompositeDisposable subscribers;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     final SpscLinkedArrayQueue<C> queue;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     long index;
/*     */     
/*     */     Map<Long, C> buffers;
/*     */     
/*     */     long emitted;
/*     */ 
/*     */     
/*     */     BufferBoundarySubscriber(Subscriber<? super C> actual, Publisher<? extends Open> bufferOpen, Function<? super Open, ? extends Publisher<? extends Close>> bufferClose, Callable<C> bufferSupplier) {
/*  94 */       this.downstream = actual;
/*  95 */       this.bufferSupplier = bufferSupplier;
/*  96 */       this.bufferOpen = bufferOpen;
/*  97 */       this.bufferClose = bufferClose;
/*  98 */       this.queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
/*  99 */       this.subscribers = new CompositeDisposable();
/* 100 */       this.requested = new AtomicLong();
/* 101 */       this.upstream = new AtomicReference<Subscription>();
/* 102 */       this.buffers = new LinkedHashMap<Long, C>();
/* 103 */       this.errors = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 108 */       if (SubscriptionHelper.setOnce(this.upstream, s)) {
/*     */         
/* 110 */         BufferOpenSubscriber<Open> open = new BufferOpenSubscriber<Open>(this);
/* 111 */         this.subscribers.add(open);
/*     */         
/* 113 */         this.bufferOpen.subscribe((Subscriber)open);
/*     */         
/* 115 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 121 */       synchronized (this) {
/* 122 */         Map<Long, C> bufs = this.buffers;
/* 123 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 126 */         for (Collection<T> collection : bufs.values()) {
/* 127 */           collection.add(t);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 134 */       if (this.errors.addThrowable(t)) {
/* 135 */         this.subscribers.dispose();
/* 136 */         synchronized (this) {
/* 137 */           this.buffers = null;
/*     */         } 
/* 139 */         this.done = true;
/* 140 */         drain();
/*     */       } else {
/* 142 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 148 */       this.subscribers.dispose();
/* 149 */       synchronized (this) {
/* 150 */         Map<Long, C> bufs = this.buffers;
/* 151 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 154 */         for (Collection collection : bufs.values()) {
/* 155 */           this.queue.offer(collection);
/*     */         }
/* 157 */         this.buffers = null;
/*     */       } 
/* 159 */       this.done = true;
/* 160 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 165 */       BackpressureHelper.add(this.requested, n);
/* 166 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 171 */       if (SubscriptionHelper.cancel(this.upstream)) {
/* 172 */         this.cancelled = true;
/* 173 */         this.subscribers.dispose();
/* 174 */         synchronized (this) {
/* 175 */           this.buffers = null;
/*     */         } 
/* 177 */         if (getAndIncrement() != 0) {
/* 178 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void open(Open token) {
/*     */       Publisher<? extends Close> p;
/*     */       Collection collection;
/*     */       try {
/* 187 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null Collection");
/* 188 */         p = (Publisher<? extends Close>)ObjectHelper.requireNonNull(this.bufferClose.apply(token), "The bufferClose returned a null Publisher");
/* 189 */       } catch (Throwable ex) {
/* 190 */         Exceptions.throwIfFatal(ex);
/* 191 */         SubscriptionHelper.cancel(this.upstream);
/* 192 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 196 */       long idx = this.index;
/* 197 */       this.index = idx + 1L;
/* 198 */       synchronized (this) {
/* 199 */         Map<Long, C> bufs = this.buffers;
/* 200 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 203 */         bufs.put(Long.valueOf(idx), (C)collection);
/*     */       } 
/*     */       
/* 206 */       FlowableBufferBoundary.BufferCloseSubscriber<T, C> bc = new FlowableBufferBoundary.BufferCloseSubscriber<T, C>(this, idx);
/* 207 */       this.subscribers.add(bc);
/* 208 */       p.subscribe((Subscriber)bc);
/*     */     }
/*     */     
/*     */     void openComplete(BufferOpenSubscriber<Open> os) {
/* 212 */       this.subscribers.delete(os);
/* 213 */       if (this.subscribers.size() == 0) {
/* 214 */         SubscriptionHelper.cancel(this.upstream);
/* 215 */         this.done = true;
/* 216 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     void close(FlowableBufferBoundary.BufferCloseSubscriber<T, C> closer, long idx) {
/* 221 */       this.subscribers.delete(closer);
/* 222 */       boolean makeDone = false;
/* 223 */       if (this.subscribers.size() == 0) {
/* 224 */         makeDone = true;
/* 225 */         SubscriptionHelper.cancel(this.upstream);
/*     */       } 
/* 227 */       synchronized (this) {
/* 228 */         Map<Long, C> bufs = this.buffers;
/* 229 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 232 */         this.queue.offer(this.buffers.remove(Long.valueOf(idx)));
/*     */       } 
/* 234 */       if (makeDone) {
/* 235 */         this.done = true;
/*     */       }
/* 237 */       drain();
/*     */     }
/*     */     
/*     */     void boundaryError(Disposable subscriber, Throwable ex) {
/* 241 */       SubscriptionHelper.cancel(this.upstream);
/* 242 */       this.subscribers.delete(subscriber);
/* 243 */       onError(ex);
/*     */     }
/*     */     
/*     */     void drain() {
/* 247 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 251 */       int missed = 1;
/* 252 */       long e = this.emitted;
/* 253 */       Subscriber<? super C> a = this.downstream;
/* 254 */       SpscLinkedArrayQueue<C> q = this.queue;
/*     */       
/*     */       do {
/* 257 */         long r = this.requested.get();
/*     */         
/* 259 */         while (e != r) {
/* 260 */           if (this.cancelled) {
/* 261 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 265 */           boolean d = this.done;
/* 266 */           if (d && this.errors.get() != null) {
/* 267 */             q.clear();
/* 268 */             Throwable ex = this.errors.terminate();
/* 269 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 273 */           Collection collection = (Collection)q.poll();
/* 274 */           boolean empty = (collection == null);
/*     */           
/* 276 */           if (d && empty) {
/* 277 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 281 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 285 */           a.onNext(collection);
/* 286 */           e++;
/*     */         } 
/*     */         
/* 289 */         if (e == r) {
/* 290 */           if (this.cancelled) {
/* 291 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 295 */           if (this.done) {
/* 296 */             if (this.errors.get() != null) {
/* 297 */               q.clear();
/* 298 */               Throwable ex = this.errors.terminate();
/* 299 */               a.onError(ex); return;
/*     */             } 
/* 301 */             if (q.isEmpty()) {
/* 302 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 308 */         this.emitted = e;
/* 309 */         missed = addAndGet(-missed);
/* 310 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     static final class BufferOpenSubscriber<Open>
/*     */       extends AtomicReference<Subscription>
/*     */       implements FlowableSubscriber<Open>, Disposable
/*     */     {
/*     */       private static final long serialVersionUID = -8498650778633225126L;
/*     */       
/*     */       final FlowableBufferBoundary.BufferBoundarySubscriber<?, ?, Open, ?> parent;
/*     */ 
/*     */       
/*     */       BufferOpenSubscriber(FlowableBufferBoundary.BufferBoundarySubscriber<?, ?, Open, ?> parent) {
/* 325 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Subscription s) {
/* 330 */         SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(Open t) {
/* 335 */         this.parent.open(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 340 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 341 */         this.parent.boundaryError(this, t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 346 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 347 */         this.parent.openComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 352 */         SubscriptionHelper.cancel(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 357 */         return (get() == SubscriptionHelper.CANCELLED);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferCloseSubscriber<T, C extends Collection<? super T>>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -8498650778633225126L;
/*     */     
/*     */     final FlowableBufferBoundary.BufferBoundarySubscriber<T, C, ?, ?> parent;
/*     */     final long index;
/*     */     
/*     */     BufferCloseSubscriber(FlowableBufferBoundary.BufferBoundarySubscriber<T, C, ?, ?> parent, long index) {
/* 373 */       this.parent = parent;
/* 374 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 379 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 384 */       Subscription s = get();
/* 385 */       if (s != SubscriptionHelper.CANCELLED) {
/* 386 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 387 */         s.cancel();
/* 388 */         this.parent.close(this, this.index);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 394 */       if (get() != SubscriptionHelper.CANCELLED) {
/* 395 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 396 */         this.parent.boundaryError(this, t);
/*     */       } else {
/* 398 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 404 */       if (get() != SubscriptionHelper.CANCELLED) {
/* 405 */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 406 */         this.parent.close(this, this.index);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 412 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 417 */       return (get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableBufferBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */