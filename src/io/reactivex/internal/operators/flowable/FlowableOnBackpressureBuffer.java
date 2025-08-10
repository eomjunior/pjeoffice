/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
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
/*     */ public final class FlowableOnBackpressureBuffer<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final int bufferSize;
/*     */   final boolean unbounded;
/*     */   final boolean delayError;
/*     */   final Action onOverflow;
/*     */   
/*     */   public FlowableOnBackpressureBuffer(Flowable<T> source, int bufferSize, boolean unbounded, boolean delayError, Action onOverflow) {
/*  37 */     super(source);
/*  38 */     this.bufferSize = bufferSize;
/*  39 */     this.unbounded = unbounded;
/*  40 */     this.delayError = delayError;
/*  41 */     this.onOverflow = onOverflow;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  46 */     this.source.subscribe(new BackpressureBufferSubscriber<T>(s, this.bufferSize, this.unbounded, this.delayError, this.onOverflow));
/*     */   }
/*     */   
/*     */   static final class BackpressureBufferSubscriber<T>
/*     */     extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T> {
/*     */     private static final long serialVersionUID = -2514538129242366402L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final SimplePlainQueue<T> queue;
/*     */     final boolean delayError;
/*     */     final Action onOverflow;
/*     */     Subscription upstream;
/*     */     volatile boolean cancelled;
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     final AtomicLong requested;
/*     */     boolean outputFused;
/*     */     
/*     */     BackpressureBufferSubscriber(Subscriber<? super T> actual, int bufferSize, boolean unbounded, boolean delayError, Action onOverflow) {
/*     */       SpscArrayQueue spscArrayQueue;
/*  65 */       this.requested = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  71 */       this.downstream = actual;
/*  72 */       this.onOverflow = onOverflow;
/*  73 */       this.delayError = delayError;
/*     */ 
/*     */ 
/*     */       
/*  77 */       if (unbounded) {
/*  78 */         SpscLinkedArrayQueue spscLinkedArrayQueue = new SpscLinkedArrayQueue(bufferSize);
/*     */       } else {
/*  80 */         spscArrayQueue = new SpscArrayQueue(bufferSize);
/*     */       } 
/*     */       
/*  83 */       this.queue = (SimplePlainQueue<T>)spscArrayQueue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  88 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  89 */         this.upstream = s;
/*  90 */         this.downstream.onSubscribe((Subscription)this);
/*  91 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  97 */       if (!this.queue.offer(t)) {
/*  98 */         this.upstream.cancel();
/*  99 */         MissingBackpressureException ex = new MissingBackpressureException("Buffer is full");
/*     */         try {
/* 101 */           this.onOverflow.run();
/* 102 */         } catch (Throwable e) {
/* 103 */           Exceptions.throwIfFatal(e);
/* 104 */           ex.initCause(e);
/*     */         } 
/* 106 */         onError((Throwable)ex);
/*     */         return;
/*     */       } 
/* 109 */       if (this.outputFused) {
/* 110 */         this.downstream.onNext(null);
/*     */       } else {
/* 112 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 118 */       this.error = t;
/* 119 */       this.done = true;
/* 120 */       if (this.outputFused) {
/* 121 */         this.downstream.onError(t);
/*     */       } else {
/* 123 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 129 */       this.done = true;
/* 130 */       if (this.outputFused) {
/* 131 */         this.downstream.onComplete();
/*     */       } else {
/* 133 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 139 */       if (!this.outputFused && 
/* 140 */         SubscriptionHelper.validate(n)) {
/* 141 */         BackpressureHelper.add(this.requested, n);
/* 142 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 149 */       if (!this.cancelled) {
/* 150 */         this.cancelled = true;
/* 151 */         this.upstream.cancel();
/*     */         
/* 153 */         if (!this.outputFused && getAndIncrement() == 0) {
/* 154 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 160 */       if (getAndIncrement() == 0) {
/* 161 */         int missed = 1;
/* 162 */         SimplePlainQueue<T> q = this.queue;
/* 163 */         Subscriber<? super T> a = this.downstream;
/*     */         
/*     */         do {
/* 166 */           if (checkTerminated(this.done, q.isEmpty(), a)) {
/*     */             return;
/*     */           }
/*     */           
/* 170 */           long r = this.requested.get();
/*     */           
/* 172 */           long e = 0L;
/*     */           
/* 174 */           while (e != r) {
/* 175 */             boolean d = this.done;
/* 176 */             T v = (T)q.poll();
/* 177 */             boolean empty = (v == null);
/*     */             
/* 179 */             if (checkTerminated(d, empty, a)) {
/*     */               return;
/*     */             }
/*     */             
/* 183 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 187 */             a.onNext(v);
/*     */             
/* 189 */             e++;
/*     */           } 
/*     */           
/* 192 */           if (e == r) {
/* 193 */             boolean d = this.done;
/* 194 */             boolean empty = q.isEmpty();
/*     */             
/* 196 */             if (checkTerminated(d, empty, a)) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */           
/* 201 */           if (e != 0L && 
/* 202 */             r != Long.MAX_VALUE) {
/* 203 */             this.requested.addAndGet(-e);
/*     */           }
/*     */ 
/*     */           
/* 207 */           missed = addAndGet(-missed);
/* 208 */         } while (missed != 0);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<? super T> a) {
/* 216 */       if (this.cancelled) {
/* 217 */         this.queue.clear();
/* 218 */         return true;
/*     */       } 
/* 220 */       if (d) {
/* 221 */         if (this.delayError) {
/* 222 */           if (empty) {
/* 223 */             Throwable e = this.error;
/* 224 */             if (e != null) {
/* 225 */               a.onError(e);
/*     */             } else {
/* 227 */               a.onComplete();
/*     */             } 
/* 229 */             return true;
/*     */           } 
/*     */         } else {
/* 232 */           Throwable e = this.error;
/* 233 */           if (e != null) {
/* 234 */             this.queue.clear();
/* 235 */             a.onError(e);
/* 236 */             return true;
/*     */           } 
/* 238 */           if (empty) {
/* 239 */             a.onComplete();
/* 240 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/* 244 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 249 */       if ((mode & 0x2) != 0) {
/* 250 */         this.outputFused = true;
/* 251 */         return 2;
/*     */       } 
/* 253 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 259 */       return (T)this.queue.poll();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 264 */       this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 269 */       return this.queue.isEmpty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */