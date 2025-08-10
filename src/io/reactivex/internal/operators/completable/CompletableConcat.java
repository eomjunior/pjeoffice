/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CompletableConcat
/*     */   extends Completable
/*     */ {
/*     */   final Publisher<? extends CompletableSource> sources;
/*     */   final int prefetch;
/*     */   
/*     */   public CompletableConcat(Publisher<? extends CompletableSource> sources, int prefetch) {
/*  34 */     this.sources = sources;
/*  35 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*  40 */     this.sources.subscribe((Subscriber)new CompletableConcatSubscriber(observer, this.prefetch));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CompletableConcatSubscriber
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<CompletableSource>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 9032184911934499404L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     final ConcatInnerObserver inner;
/*     */     
/*     */     final AtomicBoolean once;
/*     */     
/*     */     int sourceFused;
/*     */     
/*     */     int consumed;
/*     */     
/*     */     SimpleQueue<CompletableSource> queue;
/*     */     
/*     */     Subscription upstream;
/*     */     volatile boolean done;
/*     */     volatile boolean active;
/*     */     
/*     */     CompletableConcatSubscriber(CompletableObserver actual, int prefetch) {
/*  71 */       this.downstream = actual;
/*  72 */       this.prefetch = prefetch;
/*  73 */       this.inner = new ConcatInnerObserver(this);
/*  74 */       this.once = new AtomicBoolean();
/*  75 */       this.limit = prefetch - (prefetch >> 2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  80 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  81 */         this.upstream = s;
/*     */         
/*  83 */         long r = (this.prefetch == Integer.MAX_VALUE) ? Long.MAX_VALUE : this.prefetch;
/*     */         
/*  85 */         if (s instanceof QueueSubscription) {
/*     */           
/*  87 */           QueueSubscription<CompletableSource> qs = (QueueSubscription<CompletableSource>)s;
/*     */           
/*  89 */           int m = qs.requestFusion(3);
/*     */           
/*  91 */           if (m == 1) {
/*  92 */             this.sourceFused = m;
/*  93 */             this.queue = (SimpleQueue<CompletableSource>)qs;
/*  94 */             this.done = true;
/*  95 */             this.downstream.onSubscribe(this);
/*  96 */             drain();
/*     */             return;
/*     */           } 
/*  99 */           if (m == 2) {
/* 100 */             this.sourceFused = m;
/* 101 */             this.queue = (SimpleQueue<CompletableSource>)qs;
/* 102 */             this.downstream.onSubscribe(this);
/* 103 */             s.request(r);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 108 */         if (this.prefetch == Integer.MAX_VALUE) {
/* 109 */           this.queue = (SimpleQueue<CompletableSource>)new SpscLinkedArrayQueue(Flowable.bufferSize());
/*     */         } else {
/* 111 */           this.queue = (SimpleQueue<CompletableSource>)new SpscArrayQueue(this.prefetch);
/*     */         } 
/*     */         
/* 114 */         this.downstream.onSubscribe(this);
/*     */         
/* 116 */         s.request(r);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(CompletableSource t) {
/* 122 */       if (this.sourceFused == 0 && 
/* 123 */         !this.queue.offer(t)) {
/* 124 */         onError((Throwable)new MissingBackpressureException());
/*     */         
/*     */         return;
/*     */       } 
/* 128 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 133 */       if (this.once.compareAndSet(false, true)) {
/* 134 */         DisposableHelper.dispose(this.inner);
/* 135 */         this.downstream.onError(t);
/*     */       } else {
/* 137 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 143 */       this.done = true;
/* 144 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 149 */       this.upstream.cancel();
/* 150 */       DisposableHelper.dispose(this.inner);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 155 */       return DisposableHelper.isDisposed(this.inner.get());
/*     */     }
/*     */     
/*     */     void drain() {
/* 159 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       do {
/*     */         CompletableSource cs;
/* 164 */         if (isDisposed()) {
/*     */           return;
/*     */         }
/*     */         
/* 168 */         if (this.active)
/*     */           continue; 
/* 170 */         boolean d = this.done;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 175 */           cs = (CompletableSource)this.queue.poll();
/* 176 */         } catch (Throwable ex) {
/* 177 */           Exceptions.throwIfFatal(ex);
/* 178 */           innerError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 182 */         boolean empty = (cs == null);
/*     */         
/* 184 */         if (d && empty) {
/* 185 */           if (this.once.compareAndSet(false, true)) {
/* 186 */             this.downstream.onComplete();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 191 */         if (empty)
/* 192 */           continue;  this.active = true;
/* 193 */         cs.subscribe(this.inner);
/* 194 */         request();
/*     */ 
/*     */       
/*     */       }
/* 198 */       while (decrementAndGet() != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void request() {
/* 205 */       if (this.sourceFused != 1) {
/* 206 */         int p = this.consumed + 1;
/* 207 */         if (p == this.limit) {
/* 208 */           this.consumed = 0;
/* 209 */           this.upstream.request(p);
/*     */         } else {
/* 211 */           this.consumed = p;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerError(Throwable e) {
/* 217 */       if (this.once.compareAndSet(false, true)) {
/* 218 */         this.upstream.cancel();
/* 219 */         this.downstream.onError(e);
/*     */       } else {
/* 221 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 226 */       this.active = false;
/* 227 */       drain();
/*     */     }
/*     */     
/*     */     static final class ConcatInnerObserver
/*     */       extends AtomicReference<Disposable> implements CompletableObserver {
/*     */       private static final long serialVersionUID = -5454794857847146511L;
/*     */       final CompletableConcat.CompletableConcatSubscriber parent;
/*     */       
/*     */       ConcatInnerObserver(CompletableConcat.CompletableConcatSubscriber parent) {
/* 236 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 241 */         DisposableHelper.replace(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 246 */         this.parent.innerError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 251 */         this.parent.innerComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableConcat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */