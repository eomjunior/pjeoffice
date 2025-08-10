/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.processors.UnicastProcessor;
/*     */ import io.reactivex.subscribers.DisposableSubscriber;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableWindowBoundarySupplier<T, B>
/*     */   extends AbstractFlowableWithUpstream<T, Flowable<T>>
/*     */ {
/*     */   final Callable<? extends Publisher<B>> other;
/*     */   final int capacityHint;
/*     */   
/*     */   public FlowableWindowBoundarySupplier(Flowable<T> source, Callable<? extends Publisher<B>> other, int capacityHint) {
/*  38 */     super(source);
/*  39 */     this.other = other;
/*  40 */     this.capacityHint = capacityHint;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super Flowable<T>> subscriber) {
/*  45 */     WindowBoundaryMainSubscriber<T, B> parent = new WindowBoundaryMainSubscriber<T, B>(subscriber, this.capacityHint, this.other);
/*     */     
/*  47 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowBoundaryMainSubscriber<T, B>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 2233020065421370272L;
/*     */     
/*     */     final Subscriber<? super Flowable<T>> downstream;
/*     */     
/*     */     final int capacityHint;
/*     */     
/*     */     final AtomicReference<FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B>> boundarySubscriber;
/*  62 */     static final FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<Object, Object> BOUNDARY_DISPOSED = new FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<Object, Object>(null);
/*     */     
/*     */     final AtomicInteger windows;
/*     */     
/*     */     final MpscLinkedQueue<Object> queue;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final AtomicBoolean stopWindows;
/*     */     
/*     */     final Callable<? extends Publisher<B>> other;
/*     */     
/*  74 */     static final Object NEXT_WINDOW = new Object();
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     UnicastProcessor<T> window;
/*     */     
/*     */     long emitted;
/*     */     
/*     */     WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> downstream, int capacityHint, Callable<? extends Publisher<B>> other) {
/*  87 */       this.downstream = downstream;
/*  88 */       this.capacityHint = capacityHint;
/*  89 */       this.boundarySubscriber = new AtomicReference<FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B>>();
/*  90 */       this.windows = new AtomicInteger(1);
/*  91 */       this.queue = new MpscLinkedQueue();
/*  92 */       this.errors = new AtomicThrowable();
/*  93 */       this.stopWindows = new AtomicBoolean();
/*  94 */       this.other = other;
/*  95 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 100 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 101 */         this.upstream = s;
/* 102 */         this.downstream.onSubscribe(this);
/* 103 */         this.queue.offer(NEXT_WINDOW);
/* 104 */         drain();
/* 105 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 111 */       this.queue.offer(t);
/* 112 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 117 */       disposeBoundary();
/* 118 */       if (this.errors.addThrowable(e)) {
/* 119 */         this.done = true;
/* 120 */         drain();
/*     */       } else {
/* 122 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 128 */       disposeBoundary();
/* 129 */       this.done = true;
/* 130 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 135 */       if (this.stopWindows.compareAndSet(false, true)) {
/* 136 */         disposeBoundary();
/* 137 */         if (this.windows.decrementAndGet() == 0) {
/* 138 */           this.upstream.cancel();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 145 */       BackpressureHelper.add(this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeBoundary() {
/* 150 */       Disposable d = (Disposable)this.boundarySubscriber.getAndSet(BOUNDARY_DISPOSED);
/* 151 */       if (d != null && d != BOUNDARY_DISPOSED) {
/* 152 */         d.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 158 */       if (this.windows.decrementAndGet() == 0) {
/* 159 */         this.upstream.cancel();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerNext(FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B> sender) {
/* 164 */       this.boundarySubscriber.compareAndSet(sender, null);
/* 165 */       this.queue.offer(NEXT_WINDOW);
/* 166 */       drain();
/*     */     }
/*     */     
/*     */     void innerError(Throwable e) {
/* 170 */       this.upstream.cancel();
/* 171 */       if (this.errors.addThrowable(e)) {
/* 172 */         this.done = true;
/* 173 */         drain();
/*     */       } else {
/* 175 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 180 */       this.upstream.cancel();
/* 181 */       this.done = true;
/* 182 */       drain();
/*     */     }
/*     */     
/*     */     void drain()
/*     */     {
/* 187 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 191 */       int missed = 1;
/* 192 */       Subscriber<? super Flowable<T>> downstream = this.downstream;
/* 193 */       MpscLinkedQueue<Object> queue = this.queue;
/* 194 */       AtomicThrowable errors = this.errors;
/* 195 */       long emitted = this.emitted;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 200 */         if (this.windows.get() == 0) {
/* 201 */           queue.clear();
/* 202 */           this.window = null;
/*     */           
/*     */           return;
/*     */         } 
/* 206 */         UnicastProcessor<T> w = this.window;
/*     */         
/* 208 */         boolean d = this.done;
/*     */         
/* 210 */         if (d && errors.get() != null) {
/* 211 */           queue.clear();
/* 212 */           Throwable ex = errors.terminate();
/* 213 */           if (w != null) {
/* 214 */             this.window = null;
/* 215 */             w.onError(ex);
/*     */           } 
/* 217 */           downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 221 */         Object v = queue.poll();
/*     */         
/* 223 */         boolean empty = (v == null);
/*     */         
/* 225 */         if (d && empty) {
/* 226 */           Throwable ex = errors.terminate();
/* 227 */           if (ex == null) {
/* 228 */             if (w != null) {
/* 229 */               this.window = null;
/* 230 */               w.onComplete();
/*     */             } 
/* 232 */             downstream.onComplete();
/*     */           } else {
/* 234 */             if (w != null) {
/* 235 */               this.window = null;
/* 236 */               w.onError(ex);
/*     */             } 
/* 238 */             downstream.onError(ex);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 243 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 291 */           this.emitted = emitted;
/* 292 */           missed = addAndGet(-missed);
/* 293 */           if (missed == 0)
/*     */             break;  continue;
/*     */         }  if (v != NEXT_WINDOW) { w.onNext(v); continue; }
/*     */          if (w != null) { this.window = null; w.onComplete(); }
/*     */          if (!this.stopWindows.get()) { if (emitted != this.requested.get()) { Publisher<B> otherSource; w = UnicastProcessor.create(this.capacityHint, this); this.window = w; this.windows.getAndIncrement(); try {
/*     */               otherSource = (Publisher<B>)ObjectHelper.requireNonNull(this.other.call(), "The other Callable returned a null Publisher");
/*     */             } catch (Throwable ex) {
/*     */               Exceptions.throwIfFatal(ex); errors.addThrowable(ex); this.done = true; continue;
/*     */             }  FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B> bo = new FlowableWindowBoundarySupplier.WindowBoundaryInnerSubscriber<T, B>(this); if (this.boundarySubscriber.compareAndSet(null, bo)) {
/*     */               otherSource.subscribe((Subscriber)bo); emitted++; downstream.onNext(w);
/*     */             }  continue; }
/*     */            this.upstream.cancel(); disposeBoundary(); errors.addThrowable((Throwable)new MissingBackpressureException("Could not deliver a window due to lack of requests")); this.done = true; }
/*     */       
/* 306 */       }  } } static final class WindowBoundaryInnerSubscriber<T, B> extends DisposableSubscriber<B> { final FlowableWindowBoundarySupplier.WindowBoundaryMainSubscriber<T, B> parent; WindowBoundaryInnerSubscriber(FlowableWindowBoundarySupplier.WindowBoundaryMainSubscriber<T, B> parent) { this.parent = parent; }
/*     */     
/*     */     boolean done;
/*     */     
/*     */     public void onNext(B t) {
/* 311 */       if (this.done) {
/*     */         return;
/*     */       }
/* 314 */       this.done = true;
/* 315 */       dispose();
/* 316 */       this.parent.innerNext(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 321 */       if (this.done) {
/* 322 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 325 */       this.done = true;
/* 326 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 331 */       if (this.done) {
/*     */         return;
/*     */       }
/* 334 */       this.done = true;
/* 335 */       this.parent.innerComplete();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableWindowBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */