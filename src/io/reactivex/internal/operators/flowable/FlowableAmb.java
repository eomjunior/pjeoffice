/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableAmb<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Publisher<? extends T>[] sources;
/*     */   final Iterable<? extends Publisher<? extends T>> sourcesIterable;
/*     */   
/*     */   public FlowableAmb(Publisher<? extends T>[] sources, Iterable<? extends Publisher<? extends T>> sourcesIterable) {
/*  30 */     this.sources = sources;
/*  31 */     this.sourcesIterable = sourcesIterable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*     */     Publisher[] arrayOfPublisher;
/*  37 */     Publisher<? extends T>[] sources = this.sources;
/*  38 */     int count = 0;
/*  39 */     if (sources == null) {
/*  40 */       arrayOfPublisher = new Publisher[8];
/*     */       try {
/*  42 */         for (Publisher<? extends T> p : this.sourcesIterable) {
/*  43 */           if (p == null) {
/*  44 */             EmptySubscription.error(new NullPointerException("One of the sources is null"), s);
/*     */             return;
/*     */           } 
/*  47 */           if (count == arrayOfPublisher.length) {
/*  48 */             Publisher[] arrayOfPublisher1 = new Publisher[count + (count >> 2)];
/*  49 */             System.arraycopy(arrayOfPublisher, 0, arrayOfPublisher1, 0, count);
/*  50 */             arrayOfPublisher = arrayOfPublisher1;
/*     */           } 
/*  52 */           arrayOfPublisher[count++] = p;
/*     */         } 
/*  54 */       } catch (Throwable e) {
/*  55 */         Exceptions.throwIfFatal(e);
/*  56 */         EmptySubscription.error(e, s);
/*     */         return;
/*     */       } 
/*     */     } else {
/*  60 */       count = arrayOfPublisher.length;
/*     */     } 
/*     */     
/*  63 */     if (count == 0) {
/*  64 */       EmptySubscription.complete(s);
/*     */       return;
/*     */     } 
/*  67 */     if (count == 1) {
/*  68 */       arrayOfPublisher[0].subscribe(s);
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     AmbCoordinator<T> ac = new AmbCoordinator<T>(s, count);
/*  73 */     ac.subscribe((Publisher<? extends T>[])arrayOfPublisher);
/*     */   }
/*     */   
/*     */   static final class AmbCoordinator<T>
/*     */     implements Subscription {
/*     */     final Subscriber<? super T> downstream;
/*     */     final FlowableAmb.AmbInnerSubscriber<T>[] subscribers;
/*  80 */     final AtomicInteger winner = new AtomicInteger();
/*     */ 
/*     */     
/*     */     AmbCoordinator(Subscriber<? super T> actual, int count) {
/*  84 */       this.downstream = actual;
/*  85 */       this.subscribers = (FlowableAmb.AmbInnerSubscriber<T>[])new FlowableAmb.AmbInnerSubscriber[count];
/*     */     }
/*     */     
/*     */     public void subscribe(Publisher<? extends T>[] sources) {
/*  89 */       FlowableAmb.AmbInnerSubscriber<T>[] as = this.subscribers;
/*  90 */       int len = as.length; int i;
/*  91 */       for (i = 0; i < len; i++) {
/*  92 */         as[i] = new FlowableAmb.AmbInnerSubscriber<T>(this, i + 1, this.downstream);
/*     */       }
/*  94 */       this.winner.lazySet(0);
/*  95 */       this.downstream.onSubscribe(this);
/*     */       
/*  97 */       for (i = 0; i < len; i++) {
/*  98 */         if (this.winner.get() != 0) {
/*     */           return;
/*     */         }
/*     */         
/* 102 */         sources[i].subscribe((Subscriber)as[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 108 */       if (SubscriptionHelper.validate(n)) {
/* 109 */         int w = this.winner.get();
/* 110 */         if (w > 0) {
/* 111 */           this.subscribers[w - 1].request(n);
/*     */         }
/* 113 */         else if (w == 0) {
/* 114 */           for (FlowableAmb.AmbInnerSubscriber<T> a : this.subscribers) {
/* 115 */             a.request(n);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean win(int index) {
/* 122 */       int w = this.winner.get();
/* 123 */       if (w == 0 && 
/* 124 */         this.winner.compareAndSet(0, index)) {
/* 125 */         FlowableAmb.AmbInnerSubscriber<T>[] a = this.subscribers;
/* 126 */         int n = a.length;
/* 127 */         for (int i = 0; i < n; i++) {
/* 128 */           if (i + 1 != index) {
/* 129 */             a[i].cancel();
/*     */           }
/*     */         } 
/* 132 */         return true;
/*     */       } 
/*     */       
/* 135 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 140 */       if (this.winner.get() != -1) {
/* 141 */         this.winner.lazySet(-1);
/*     */         
/* 143 */         for (FlowableAmb.AmbInnerSubscriber<T> a : this.subscribers) {
/* 144 */           a.cancel();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class AmbInnerSubscriber<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -1185974347409665484L;
/*     */     final FlowableAmb.AmbCoordinator<T> parent;
/*     */     final int index;
/*     */     final Subscriber<? super T> downstream;
/*     */     boolean won;
/* 159 */     final AtomicLong missedRequested = new AtomicLong();
/*     */     
/*     */     AmbInnerSubscriber(FlowableAmb.AmbCoordinator<T> parent, int index, Subscriber<? super T> downstream) {
/* 162 */       this.parent = parent;
/* 163 */       this.index = index;
/* 164 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 169 */       SubscriptionHelper.deferredSetOnce(this, this.missedRequested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 174 */       SubscriptionHelper.deferredRequest(this, this.missedRequested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 179 */       if (this.won) {
/* 180 */         this.downstream.onNext(t);
/*     */       }
/* 182 */       else if (this.parent.win(this.index)) {
/* 183 */         this.won = true;
/* 184 */         this.downstream.onNext(t);
/*     */       } else {
/* 186 */         get().cancel();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 193 */       if (this.won) {
/* 194 */         this.downstream.onError(t);
/*     */       }
/* 196 */       else if (this.parent.win(this.index)) {
/* 197 */         this.won = true;
/* 198 */         this.downstream.onError(t);
/*     */       } else {
/* 200 */         get().cancel();
/* 201 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 208 */       if (this.won) {
/* 209 */         this.downstream.onComplete();
/*     */       }
/* 211 */       else if (this.parent.win(this.index)) {
/* 212 */         this.won = true;
/* 213 */         this.downstream.onComplete();
/*     */       } else {
/* 215 */         get().cancel();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 222 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */