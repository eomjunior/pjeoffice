/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Arrays;
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
/*     */ public final class FlowableZip<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final Publisher<? extends T>[] sources;
/*     */   final Iterable<? extends Publisher<? extends T>> sourcesIterable;
/*     */   final Function<? super Object[], ? extends R> zipper;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public FlowableZip(Publisher<? extends T>[] sources, Iterable<? extends Publisher<? extends T>> sourcesIterable, Function<? super Object[], ? extends R> zipper, int bufferSize, boolean delayError) {
/*  44 */     this.sources = sources;
/*  45 */     this.sourcesIterable = sourcesIterable;
/*  46 */     this.zipper = zipper;
/*  47 */     this.bufferSize = bufferSize;
/*  48 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super R> s) {
/*     */     Publisher[] arrayOfPublisher;
/*  54 */     Publisher<? extends T>[] sources = this.sources;
/*  55 */     int count = 0;
/*  56 */     if (sources == null) {
/*  57 */       arrayOfPublisher = new Publisher[8];
/*  58 */       for (Publisher<? extends T> p : this.sourcesIterable) {
/*  59 */         if (count == arrayOfPublisher.length) {
/*  60 */           Publisher[] arrayOfPublisher1 = new Publisher[count + (count >> 2)];
/*  61 */           System.arraycopy(arrayOfPublisher, 0, arrayOfPublisher1, 0, count);
/*  62 */           arrayOfPublisher = arrayOfPublisher1;
/*     */         } 
/*  64 */         arrayOfPublisher[count++] = p;
/*     */       } 
/*     */     } else {
/*  67 */       count = arrayOfPublisher.length;
/*     */     } 
/*     */     
/*  70 */     if (count == 0) {
/*  71 */       EmptySubscription.complete(s);
/*     */       
/*     */       return;
/*     */     } 
/*  75 */     ZipCoordinator<T, R> coordinator = new ZipCoordinator<T, R>(s, this.zipper, count, this.bufferSize, this.delayError);
/*     */     
/*  77 */     s.onSubscribe(coordinator);
/*     */     
/*  79 */     coordinator.subscribe((Publisher<? extends T>[])arrayOfPublisher, count);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipCoordinator<T, R>
/*     */     extends AtomicInteger
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -2434867452883857743L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final FlowableZip.ZipSubscriber<T, R>[] subscribers;
/*     */     
/*     */     final Function<? super Object[], ? extends R> zipper;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     final Object[] current;
/*     */     
/*     */     ZipCoordinator(Subscriber<? super R> actual, Function<? super Object[], ? extends R> zipper, int n, int prefetch, boolean delayErrors) {
/* 106 */       this.downstream = actual;
/* 107 */       this.zipper = zipper;
/* 108 */       this.delayErrors = delayErrors;
/*     */       
/* 110 */       FlowableZip.ZipSubscriber[] arrayOfZipSubscriber = new FlowableZip.ZipSubscriber[n];
/* 111 */       for (int i = 0; i < n; i++) {
/* 112 */         arrayOfZipSubscriber[i] = new FlowableZip.ZipSubscriber<T, R>(this, prefetch);
/*     */       }
/* 114 */       this.current = new Object[n];
/* 115 */       this.subscribers = (FlowableZip.ZipSubscriber<T, R>[])arrayOfZipSubscriber;
/* 116 */       this.requested = new AtomicLong();
/* 117 */       this.errors = new AtomicThrowable();
/*     */     }
/*     */     
/*     */     void subscribe(Publisher<? extends T>[] sources, int n) {
/* 121 */       FlowableZip.ZipSubscriber<T, R>[] a = this.subscribers;
/* 122 */       for (int i = 0; i < n; i++) {
/* 123 */         if (this.cancelled || (!this.delayErrors && this.errors.get() != null)) {
/*     */           return;
/*     */         }
/* 126 */         sources[i].subscribe((Subscriber)a[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 132 */       if (SubscriptionHelper.validate(n)) {
/* 133 */         BackpressureHelper.add(this.requested, n);
/* 134 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 140 */       if (!this.cancelled) {
/* 141 */         this.cancelled = true;
/*     */         
/* 143 */         cancelAll();
/*     */       } 
/*     */     }
/*     */     
/*     */     void error(FlowableZip.ZipSubscriber<T, R> inner, Throwable e) {
/* 148 */       if (this.errors.addThrowable(e)) {
/* 149 */         inner.done = true;
/* 150 */         drain();
/*     */       } else {
/* 152 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 157 */       for (FlowableZip.ZipSubscriber<T, R> s : this.subscribers) {
/* 158 */         s.cancel();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 164 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 168 */       Subscriber<? super R> a = this.downstream;
/* 169 */       FlowableZip.ZipSubscriber<T, R>[] qs = this.subscribers;
/* 170 */       int n = qs.length;
/* 171 */       Object[] values = this.current;
/*     */       
/* 173 */       int missed = 1;
/*     */ 
/*     */       
/*     */       do {
/* 177 */         long r = this.requested.get();
/* 178 */         long e = 0L;
/*     */         
/* 180 */         while (r != e) {
/*     */           R v;
/* 182 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 186 */           if (!this.delayErrors && this.errors.get() != null) {
/* 187 */             cancelAll();
/* 188 */             a.onError(this.errors.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 192 */           boolean empty = false;
/*     */           
/* 194 */           for (int j = 0; j < n; j++) {
/* 195 */             FlowableZip.ZipSubscriber<T, R> inner = qs[j];
/* 196 */             if (values[j] == null) {
/*     */               try {
/* 198 */                 boolean d = inner.done;
/* 199 */                 SimpleQueue<T> q = inner.queue;
/*     */                 
/* 201 */                 T t = (q != null) ? (T)q.poll() : null;
/*     */                 
/* 203 */                 boolean sourceEmpty = (t == null);
/* 204 */                 if (d && sourceEmpty) {
/* 205 */                   cancelAll();
/* 206 */                   Throwable ex = (Throwable)this.errors.get();
/* 207 */                   if (ex != null) {
/* 208 */                     a.onError(this.errors.terminate());
/*     */                   } else {
/* 210 */                     a.onComplete();
/*     */                   } 
/*     */                   return;
/*     */                 } 
/* 214 */                 if (!sourceEmpty) {
/* 215 */                   values[j] = t;
/*     */                 } else {
/* 217 */                   empty = true;
/*     */                 } 
/* 219 */               } catch (Throwable ex) {
/* 220 */                 Exceptions.throwIfFatal(ex);
/*     */                 
/* 222 */                 this.errors.addThrowable(ex);
/* 223 */                 if (!this.delayErrors) {
/* 224 */                   cancelAll();
/* 225 */                   a.onError(this.errors.terminate());
/*     */                   return;
/*     */                 } 
/* 228 */                 empty = true;
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 233 */           if (empty) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 240 */             v = (R)ObjectHelper.requireNonNull(this.zipper.apply(values.clone()), "The zipper returned a null value");
/* 241 */           } catch (Throwable ex) {
/* 242 */             Exceptions.throwIfFatal(ex);
/* 243 */             cancelAll();
/* 244 */             this.errors.addThrowable(ex);
/* 245 */             a.onError(this.errors.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 249 */           a.onNext(v);
/*     */           
/* 251 */           e++;
/*     */           
/* 253 */           Arrays.fill(values, (Object)null);
/*     */         } 
/*     */         
/* 256 */         if (r == e) {
/* 257 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 261 */           if (!this.delayErrors && this.errors.get() != null) {
/* 262 */             cancelAll();
/* 263 */             a.onError(this.errors.terminate());
/*     */             
/*     */             return;
/*     */           } 
/* 267 */           for (int j = 0; j < n; j++) {
/* 268 */             FlowableZip.ZipSubscriber<T, R> inner = qs[j];
/* 269 */             if (values[j] == null) {
/*     */               try {
/* 271 */                 boolean d = inner.done;
/* 272 */                 SimpleQueue<T> q = inner.queue;
/* 273 */                 T v = (q != null) ? (T)q.poll() : null;
/*     */                 
/* 275 */                 boolean empty = (v == null);
/* 276 */                 if (d && empty) {
/* 277 */                   cancelAll();
/* 278 */                   Throwable ex = (Throwable)this.errors.get();
/* 279 */                   if (ex != null) {
/* 280 */                     a.onError(this.errors.terminate());
/*     */                   } else {
/* 282 */                     a.onComplete();
/*     */                   } 
/*     */                   return;
/*     */                 } 
/* 286 */                 if (!empty) {
/* 287 */                   values[j] = v;
/*     */                 }
/* 289 */               } catch (Throwable ex) {
/* 290 */                 Exceptions.throwIfFatal(ex);
/* 291 */                 this.errors.addThrowable(ex);
/* 292 */                 if (!this.delayErrors) {
/* 293 */                   cancelAll();
/* 294 */                   a.onError(this.errors.terminate());
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 303 */         if (e != 0L) {
/*     */           
/* 305 */           for (FlowableZip.ZipSubscriber<T, R> inner : qs) {
/* 306 */             inner.request(e);
/*     */           }
/*     */           
/* 309 */           if (r != Long.MAX_VALUE) {
/* 310 */             this.requested.addAndGet(-e);
/*     */           }
/*     */         } 
/*     */         
/* 314 */         missed = addAndGet(-missed);
/* 315 */       } while (missed != 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipSubscriber<T, R>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -4627193790118206028L;
/*     */     
/*     */     final FlowableZip.ZipCoordinator<T, R> parent;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     long produced;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     int sourceMode;
/*     */     
/*     */     ZipSubscriber(FlowableZip.ZipCoordinator<T, R> parent, int prefetch) {
/* 341 */       this.parent = parent;
/* 342 */       this.prefetch = prefetch;
/* 343 */       this.limit = prefetch - (prefetch >> 2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 349 */       if (SubscriptionHelper.setOnce(this, s)) {
/* 350 */         if (s instanceof QueueSubscription) {
/* 351 */           QueueSubscription<T> f = (QueueSubscription<T>)s;
/*     */           
/* 353 */           int m = f.requestFusion(7);
/*     */           
/* 355 */           if (m == 1) {
/* 356 */             this.sourceMode = m;
/* 357 */             this.queue = (SimpleQueue<T>)f;
/* 358 */             this.done = true;
/* 359 */             this.parent.drain();
/*     */             return;
/*     */           } 
/* 362 */           if (m == 2) {
/* 363 */             this.sourceMode = m;
/* 364 */             this.queue = (SimpleQueue<T>)f;
/* 365 */             s.request(this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 370 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
/*     */         
/* 372 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 378 */       if (this.sourceMode != 2) {
/* 379 */         this.queue.offer(t);
/*     */       }
/* 381 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 386 */       this.parent.error(this, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 391 */       this.done = true;
/* 392 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 397 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 402 */       if (this.sourceMode != 1) {
/* 403 */         long p = this.produced + n;
/* 404 */         if (p >= this.limit) {
/* 405 */           this.produced = 0L;
/* 406 */           get().request(p);
/*     */         } else {
/* 408 */           this.produced = p;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableZip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */