/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.BackpressureOverflowStrategy;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableOnBackpressureBufferStrategy<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long bufferSize;
/*     */   final Action onOverflow;
/*     */   final BackpressureOverflowStrategy strategy;
/*     */   
/*     */   public FlowableOnBackpressureBufferStrategy(Flowable<T> source, long bufferSize, Action onOverflow, BackpressureOverflowStrategy strategy) {
/*  43 */     super(source);
/*  44 */     this.bufferSize = bufferSize;
/*  45 */     this.onOverflow = onOverflow;
/*  46 */     this.strategy = strategy;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  51 */     this.source.subscribe(new OnBackpressureBufferStrategySubscriber<T>(s, this.onOverflow, this.strategy, this.bufferSize));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class OnBackpressureBufferStrategySubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 3240706908776709697L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Action onOverflow;
/*     */     
/*     */     final BackpressureOverflowStrategy strategy;
/*     */     
/*     */     final long bufferSize;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final Deque<T> deque;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     
/*     */     OnBackpressureBufferStrategySubscriber(Subscriber<? super T> actual, Action onOverflow, BackpressureOverflowStrategy strategy, long bufferSize) {
/*  81 */       this.downstream = actual;
/*  82 */       this.onOverflow = onOverflow;
/*  83 */       this.strategy = strategy;
/*  84 */       this.bufferSize = bufferSize;
/*  85 */       this.requested = new AtomicLong();
/*  86 */       this.deque = new ArrayDeque<T>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  91 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  92 */         this.upstream = s;
/*     */         
/*  94 */         this.downstream.onSubscribe(this);
/*     */         
/*  96 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 102 */       if (this.done) {
/*     */         return;
/*     */       }
/* 105 */       boolean callOnOverflow = false;
/* 106 */       boolean callError = false;
/* 107 */       Deque<T> dq = this.deque;
/* 108 */       synchronized (dq) {
/* 109 */         if (dq.size() == this.bufferSize) {
/* 110 */           switch (this.strategy) {
/*     */             case DROP_LATEST:
/* 112 */               dq.pollLast();
/* 113 */               dq.offer(t);
/* 114 */               callOnOverflow = true;
/*     */               break;
/*     */             case DROP_OLDEST:
/* 117 */               dq.poll();
/* 118 */               dq.offer(t);
/* 119 */               callOnOverflow = true;
/*     */               break;
/*     */             
/*     */             default:
/* 123 */               callError = true;
/*     */               break;
/*     */           } 
/*     */         } else {
/* 127 */           dq.offer(t);
/*     */         } 
/*     */       } 
/*     */       
/* 131 */       if (callOnOverflow) {
/* 132 */         if (this.onOverflow != null) {
/*     */           try {
/* 134 */             this.onOverflow.run();
/* 135 */           } catch (Throwable ex) {
/* 136 */             Exceptions.throwIfFatal(ex);
/* 137 */             this.upstream.cancel();
/* 138 */             onError(ex);
/*     */           } 
/*     */         }
/* 141 */       } else if (callError) {
/* 142 */         this.upstream.cancel();
/* 143 */         onError((Throwable)new MissingBackpressureException());
/*     */       } else {
/* 145 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 151 */       if (this.done) {
/* 152 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 155 */       this.error = t;
/* 156 */       this.done = true;
/* 157 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 162 */       this.done = true;
/* 163 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 168 */       if (SubscriptionHelper.validate(n)) {
/* 169 */         BackpressureHelper.add(this.requested, n);
/* 170 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 176 */       this.cancelled = true;
/* 177 */       this.upstream.cancel();
/*     */       
/* 179 */       if (getAndIncrement() == 0) {
/* 180 */         clear(this.deque);
/*     */       }
/*     */     }
/*     */     
/*     */     void clear(Deque<T> dq) {
/* 185 */       synchronized (dq) {
/* 186 */         dq.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 191 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 195 */       int missed = 1;
/* 196 */       Deque<T> dq = this.deque;
/* 197 */       Subscriber<? super T> a = this.downstream;
/*     */       do {
/* 199 */         long r = this.requested.get();
/* 200 */         long e = 0L;
/* 201 */         while (e != r) {
/* 202 */           T v; if (this.cancelled) {
/* 203 */             clear(dq);
/*     */             
/*     */             return;
/*     */           } 
/* 207 */           boolean d = this.done;
/*     */ 
/*     */ 
/*     */           
/* 211 */           synchronized (dq) {
/* 212 */             v = dq.poll();
/*     */           } 
/*     */           
/* 215 */           boolean empty = (v == null);
/*     */           
/* 217 */           if (d) {
/* 218 */             Throwable ex = this.error;
/* 219 */             if (ex != null) {
/* 220 */               clear(dq);
/* 221 */               a.onError(ex);
/*     */               return;
/*     */             } 
/* 224 */             if (empty) {
/* 225 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 230 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 234 */           a.onNext(v);
/*     */           
/* 236 */           e++;
/*     */         } 
/*     */         
/* 239 */         if (e == r) {
/* 240 */           boolean empty; if (this.cancelled) {
/* 241 */             clear(dq);
/*     */             
/*     */             return;
/*     */           } 
/* 245 */           boolean d = this.done;
/*     */ 
/*     */ 
/*     */           
/* 249 */           synchronized (dq) {
/* 250 */             empty = dq.isEmpty();
/*     */           } 
/*     */           
/* 253 */           if (d) {
/* 254 */             Throwable ex = this.error;
/* 255 */             if (ex != null) {
/* 256 */               clear(dq);
/* 257 */               a.onError(ex);
/*     */               return;
/*     */             } 
/* 260 */             if (empty) {
/* 261 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 267 */         if (e != 0L) {
/* 268 */           BackpressureHelper.produced(this.requested, e);
/*     */         }
/*     */         
/* 271 */         missed = addAndGet(-missed);
/* 272 */       } while (missed != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureBufferStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */