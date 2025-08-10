/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.subscribers.QueueDrainSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.processors.UnicastProcessor;
/*     */ import io.reactivex.subscribers.DisposableSubscriber;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class FlowableWindowBoundarySelector<T, B, V>
/*     */   extends AbstractFlowableWithUpstream<T, Flowable<T>>
/*     */ {
/*     */   final Publisher<B> open;
/*     */   final Function<? super B, ? extends Publisher<V>> close;
/*     */   final int bufferSize;
/*     */   
/*     */   public FlowableWindowBoundarySelector(Flowable<T> source, Publisher<B> open, Function<? super B, ? extends Publisher<V>> close, int bufferSize) {
/*  45 */     super(source);
/*  46 */     this.open = open;
/*  47 */     this.close = close;
/*  48 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super Flowable<T>> s) {
/*  53 */     this.source.subscribe((FlowableSubscriber)new WindowBoundaryMainSubscriber<Object, B, V>((Subscriber<? super Flowable<?>>)new SerializedSubscriber(s), this.open, this.close, this.bufferSize));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowBoundaryMainSubscriber<T, B, V>
/*     */     extends QueueDrainSubscriber<T, Object, Flowable<T>>
/*     */     implements Subscription
/*     */   {
/*     */     final Publisher<B> open;
/*     */     
/*     */     final Function<? super B, ? extends Publisher<V>> close;
/*     */     
/*     */     final int bufferSize;
/*     */     final CompositeDisposable resources;
/*     */     Subscription upstream;
/*  68 */     final AtomicReference<Disposable> boundary = new AtomicReference<Disposable>();
/*     */     
/*     */     final List<UnicastProcessor<T>> ws;
/*     */     
/*  72 */     final AtomicLong windows = new AtomicLong();
/*     */     
/*  74 */     final AtomicBoolean stopWindows = new AtomicBoolean();
/*     */ 
/*     */     
/*     */     WindowBoundaryMainSubscriber(Subscriber<? super Flowable<T>> actual, Publisher<B> open, Function<? super B, ? extends Publisher<V>> close, int bufferSize) {
/*  78 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  79 */       this.open = open;
/*  80 */       this.close = close;
/*  81 */       this.bufferSize = bufferSize;
/*  82 */       this.resources = new CompositeDisposable();
/*  83 */       this.ws = new ArrayList<UnicastProcessor<T>>();
/*  84 */       this.windows.lazySet(1L);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  89 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  90 */         this.upstream = s;
/*     */         
/*  92 */         this.downstream.onSubscribe(this);
/*     */         
/*  94 */         if (this.stopWindows.get()) {
/*     */           return;
/*     */         }
/*     */         
/*  98 */         FlowableWindowBoundarySelector.OperatorWindowBoundaryOpenSubscriber<T, B> os = new FlowableWindowBoundarySelector.OperatorWindowBoundaryOpenSubscriber<T, B>(this);
/*     */         
/* 100 */         if (this.boundary.compareAndSet(null, os)) {
/* 101 */           s.request(Long.MAX_VALUE);
/* 102 */           this.open.subscribe((Subscriber)os);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 109 */       if (this.done) {
/*     */         return;
/*     */       }
/* 112 */       if (fastEnter()) {
/* 113 */         for (UnicastProcessor<T> w : this.ws) {
/* 114 */           w.onNext(t);
/*     */         }
/* 116 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 120 */         this.queue.offer(NotificationLite.next(t));
/* 121 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 125 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 130 */       if (this.done) {
/* 131 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 134 */       this.error = t;
/* 135 */       this.done = true;
/*     */       
/* 137 */       if (enter()) {
/* 138 */         drainLoop();
/*     */       }
/*     */       
/* 141 */       if (this.windows.decrementAndGet() == 0L) {
/* 142 */         this.resources.dispose();
/*     */       }
/*     */       
/* 145 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 150 */       if (this.done) {
/*     */         return;
/*     */       }
/* 153 */       this.done = true;
/*     */       
/* 155 */       if (enter()) {
/* 156 */         drainLoop();
/*     */       }
/*     */       
/* 159 */       if (this.windows.decrementAndGet() == 0L) {
/* 160 */         this.resources.dispose();
/*     */       }
/*     */       
/* 163 */       this.downstream.onComplete();
/*     */     }
/*     */     
/*     */     void error(Throwable t) {
/* 167 */       this.upstream.cancel();
/* 168 */       this.resources.dispose();
/* 169 */       DisposableHelper.dispose(this.boundary);
/*     */       
/* 171 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 176 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 181 */       if (this.stopWindows.compareAndSet(false, true)) {
/* 182 */         DisposableHelper.dispose(this.boundary);
/* 183 */         if (this.windows.decrementAndGet() == 0L) {
/* 184 */           this.upstream.cancel();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void dispose() {
/* 190 */       this.resources.dispose();
/* 191 */       DisposableHelper.dispose(this.boundary);
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 195 */       SimplePlainQueue<Object> q = this.queue;
/* 196 */       Subscriber<? super Flowable<T>> a = this.downstream;
/* 197 */       List<UnicastProcessor<T>> ws = this.ws;
/* 198 */       int missed = 1;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 203 */         boolean d = this.done;
/* 204 */         Object o = q.poll();
/*     */         
/* 206 */         boolean empty = (o == null);
/*     */         
/* 208 */         if (d && empty) {
/* 209 */           dispose();
/* 210 */           Throwable e = this.error;
/* 211 */           if (e != null) {
/* 212 */             for (UnicastProcessor<T> w : ws) {
/* 213 */               w.onError(e);
/*     */             }
/*     */           } else {
/* 216 */             for (UnicastProcessor<T> w : ws) {
/* 217 */               w.onComplete();
/*     */             }
/*     */           } 
/* 220 */           ws.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 224 */         if (empty)
/*     */         
/*     */         { 
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
/* 290 */           missed = leave(-missed);
/* 291 */           if (missed == 0)
/*     */             break;  continue; }  if (o instanceof FlowableWindowBoundarySelector.WindowOperation) { Publisher<V> p; FlowableWindowBoundarySelector.WindowOperation<T, B> wo = (FlowableWindowBoundarySelector.WindowOperation<T, B>)o; UnicastProcessor<T> w = wo.w; if (w != null) { if (ws.remove(wo.w)) { wo.w.onComplete(); if (this.windows.decrementAndGet() == 0L) { dispose(); return; }  }  continue; }  if (this.stopWindows.get())
/*     */             continue;  w = UnicastProcessor.create(this.bufferSize); long r = requested(); if (r != 0L) { ws.add(w); a.onNext(w); if (r != Long.MAX_VALUE)
/*     */               produced(1L);  } else { cancel(); a.onError((Throwable)new MissingBackpressureException("Could not deliver new window due to lack of requests")); continue; }  try { p = (Publisher<V>)ObjectHelper.requireNonNull(this.close.apply(wo.open), "The publisher supplied is null"); }
/*     */           catch (Throwable e) { cancel(); a.onError(e); continue; }
/*     */            FlowableWindowBoundarySelector.OperatorWindowBoundaryCloseSubscriber<T, V> cl = new FlowableWindowBoundarySelector.OperatorWindowBoundaryCloseSubscriber<T, V>(this, w); if (this.resources.add((Disposable)cl)) { this.windows.getAndIncrement(); p.subscribe((Subscriber)cl); }
/*     */            continue; }
/*     */          for (UnicastProcessor<T> w : ws)
/*     */           w.onNext(NotificationLite.getValue(o)); 
/* 300 */       }  } public boolean accept(Subscriber<? super Flowable<T>> a, Object v) { return false; }
/*     */ 
/*     */     
/*     */     void open(B b) {
/* 304 */       this.queue.offer(new FlowableWindowBoundarySelector.WindowOperation<Object, B>(null, b));
/* 305 */       if (enter()) {
/* 306 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void close(FlowableWindowBoundarySelector.OperatorWindowBoundaryCloseSubscriber<T, V> w) {
/* 311 */       this.resources.delete((Disposable)w);
/* 312 */       this.queue.offer(new FlowableWindowBoundarySelector.WindowOperation<T, Object>(w.w, null));
/* 313 */       if (enter())
/* 314 */         drainLoop(); 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WindowOperation<T, B> {
/*     */     final UnicastProcessor<T> w;
/*     */     final B open;
/*     */     
/*     */     WindowOperation(UnicastProcessor<T> w, B open) {
/* 323 */       this.w = w;
/* 324 */       this.open = open;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class OperatorWindowBoundaryOpenSubscriber<T, B> extends DisposableSubscriber<B> {
/*     */     final FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, B, ?> parent;
/*     */     
/*     */     OperatorWindowBoundaryOpenSubscriber(FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, B, ?> parent) {
/* 332 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(B t) {
/* 337 */       this.parent.open(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 342 */       this.parent.error(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 347 */       this.parent.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class OperatorWindowBoundaryCloseSubscriber<T, V>
/*     */     extends DisposableSubscriber<V> {
/*     */     final FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, ?, V> parent;
/*     */     final UnicastProcessor<T> w;
/*     */     boolean done;
/*     */     
/*     */     OperatorWindowBoundaryCloseSubscriber(FlowableWindowBoundarySelector.WindowBoundaryMainSubscriber<T, ?, V> parent, UnicastProcessor<T> w) {
/* 358 */       this.parent = parent;
/* 359 */       this.w = w;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(V t) {
/* 364 */       cancel();
/* 365 */       onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 370 */       if (this.done) {
/* 371 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 374 */       this.done = true;
/* 375 */       this.parent.error(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 380 */       if (this.done) {
/*     */         return;
/*     */       }
/* 383 */       this.done = true;
/* 384 */       this.parent.close(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableWindowBoundarySelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */