/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.processors.UnicastProcessor;
/*     */ import io.reactivex.subscribers.DisposableSubscriber;
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
/*     */ public final class FlowableWindowBoundary<T, B>
/*     */   extends AbstractFlowableWithUpstream<T, Flowable<T>>
/*     */ {
/*     */   final Publisher<B> other;
/*     */   final int capacityHint;
/*     */   
/*     */   public FlowableWindowBoundary(Flowable<T> source, Publisher<B> other, int capacityHint) {
/*  34 */     super(source);
/*  35 */     this.other = other;
/*  36 */     this.capacityHint = capacityHint;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super Flowable<T>> subscriber) {
/*  41 */     WindowBoundaryMainSubscriber<T, B> parent = new WindowBoundaryMainSubscriber<T, B>(subscriber, this.capacityHint);
/*     */     
/*  43 */     subscriber.onSubscribe(parent);
/*     */     
/*  45 */     parent.innerNext();
/*     */     
/*  47 */     this.other.subscribe((Subscriber)parent.boundarySubscriber);
/*     */     
/*  49 */     this.source.subscribe(parent);
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
/*     */     final FlowableWindowBoundary.WindowBoundaryInnerSubscriber<T, B> boundarySubscriber;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicInteger windows;
/*     */     
/*     */     final MpscLinkedQueue<Object> queue;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final AtomicBoolean stopWindows;
/*     */     
/*     */     final AtomicLong requested;
/*  76 */     static final Object NEXT_WINDOW = new Object();
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     UnicastProcessor<T> window;
/*     */     
/*     */     long emitted;
/*     */     
/*     */     WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> downstream, int capacityHint) {
/*  85 */       this.downstream = downstream;
/*  86 */       this.capacityHint = capacityHint;
/*  87 */       this.boundarySubscriber = new FlowableWindowBoundary.WindowBoundaryInnerSubscriber<T, B>(this);
/*  88 */       this.upstream = new AtomicReference<Subscription>();
/*  89 */       this.windows = new AtomicInteger(1);
/*  90 */       this.queue = new MpscLinkedQueue();
/*  91 */       this.errors = new AtomicThrowable();
/*  92 */       this.stopWindows = new AtomicBoolean();
/*  93 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  98 */       SubscriptionHelper.setOnce(this.upstream, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 103 */       this.queue.offer(t);
/* 104 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 109 */       this.boundarySubscriber.dispose();
/* 110 */       if (this.errors.addThrowable(e)) {
/* 111 */         this.done = true;
/* 112 */         drain();
/*     */       } else {
/* 114 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 120 */       this.boundarySubscriber.dispose();
/* 121 */       this.done = true;
/* 122 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 127 */       if (this.stopWindows.compareAndSet(false, true)) {
/* 128 */         this.boundarySubscriber.dispose();
/* 129 */         if (this.windows.decrementAndGet() == 0) {
/* 130 */           SubscriptionHelper.cancel(this.upstream);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 137 */       BackpressureHelper.add(this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 142 */       if (this.windows.decrementAndGet() == 0) {
/* 143 */         SubscriptionHelper.cancel(this.upstream);
/*     */       }
/*     */     }
/*     */     
/*     */     void innerNext() {
/* 148 */       this.queue.offer(NEXT_WINDOW);
/* 149 */       drain();
/*     */     }
/*     */     
/*     */     void innerError(Throwable e) {
/* 153 */       SubscriptionHelper.cancel(this.upstream);
/* 154 */       if (this.errors.addThrowable(e)) {
/* 155 */         this.done = true;
/* 156 */         drain();
/*     */       } else {
/* 158 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 163 */       SubscriptionHelper.cancel(this.upstream);
/* 164 */       this.done = true;
/* 165 */       drain();
/*     */     }
/*     */     
/*     */     void drain()
/*     */     {
/* 170 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 174 */       int missed = 1;
/* 175 */       Subscriber<? super Flowable<T>> downstream = this.downstream;
/* 176 */       MpscLinkedQueue<Object> queue = this.queue;
/* 177 */       AtomicThrowable errors = this.errors;
/* 178 */       long emitted = this.emitted;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 183 */         if (this.windows.get() == 0) {
/* 184 */           queue.clear();
/* 185 */           this.window = null;
/*     */           
/*     */           return;
/*     */         } 
/* 189 */         UnicastProcessor<T> w = this.window;
/*     */         
/* 191 */         boolean d = this.done;
/*     */         
/* 193 */         if (d && errors.get() != null) {
/* 194 */           queue.clear();
/* 195 */           Throwable ex = errors.terminate();
/* 196 */           if (w != null) {
/* 197 */             this.window = null;
/* 198 */             w.onError(ex);
/*     */           } 
/* 200 */           downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 204 */         Object v = queue.poll();
/*     */         
/* 206 */         boolean empty = (v == null);
/*     */         
/* 208 */         if (d && empty) {
/* 209 */           Throwable ex = errors.terminate();
/* 210 */           if (ex == null) {
/* 211 */             if (w != null) {
/* 212 */               this.window = null;
/* 213 */               w.onComplete();
/*     */             } 
/* 215 */             downstream.onComplete();
/*     */           } else {
/* 217 */             if (w != null) {
/* 218 */               this.window = null;
/* 219 */               w.onError(ex);
/*     */             } 
/* 221 */             downstream.onError(ex);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 226 */         if (empty) {
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
/* 257 */           this.emitted = emitted;
/* 258 */           missed = addAndGet(-missed);
/* 259 */           if (missed == 0)
/*     */             break;  continue;
/*     */         }  if (v != NEXT_WINDOW) {
/*     */           w.onNext(v); continue;
/*     */         }  if (w != null) {
/*     */           this.window = null; w.onComplete();
/*     */         }  if (!this.stopWindows.get()) {
/*     */           w = UnicastProcessor.create(this.capacityHint, this); this.window = w; this.windows.getAndIncrement(); if (emitted != this.requested.get()) {
/*     */             emitted++; downstream.onNext(w); continue;
/*     */           }  SubscriptionHelper.cancel(this.upstream);
/*     */           this.boundarySubscriber.dispose();
/*     */           errors.addThrowable((Throwable)new MissingBackpressureException("Could not deliver a window due to lack of requests"));
/*     */           this.done = true;
/*     */         } 
/* 273 */       }  } } static final class WindowBoundaryInnerSubscriber<T, B> extends DisposableSubscriber<B> { final FlowableWindowBoundary.WindowBoundaryMainSubscriber<T, B> parent; WindowBoundaryInnerSubscriber(FlowableWindowBoundary.WindowBoundaryMainSubscriber<T, B> parent) { this.parent = parent; }
/*     */     
/*     */     boolean done;
/*     */     
/*     */     public void onNext(B t) {
/* 278 */       if (this.done) {
/*     */         return;
/*     */       }
/* 281 */       this.parent.innerNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 286 */       if (this.done) {
/* 287 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 290 */       this.done = true;
/* 291 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 296 */       if (this.done) {
/*     */         return;
/*     */       }
/* 299 */       this.done = true;
/* 300 */       this.parent.innerComplete();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableWindowBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */