/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableDoFinally<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Action onFinally;
/*     */   
/*     */   public FlowableDoFinally(Flowable<T> source, Action onFinally) {
/*  37 */     super(source);
/*  38 */     this.onFinally = onFinally;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  43 */     if (s instanceof ConditionalSubscriber) {
/*  44 */       this.source.subscribe((FlowableSubscriber)new DoFinallyConditionalSubscriber((ConditionalSubscriber)s, this.onFinally));
/*     */     } else {
/*  46 */       this.source.subscribe(new DoFinallySubscriber<T>(s, this.onFinally));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoFinallySubscriber<T>
/*     */     extends BasicIntQueueSubscription<T>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4109457741734051389L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Action onFinally;
/*     */     Subscription upstream;
/*     */     QueueSubscription<T> qs;
/*     */     boolean syncFused;
/*     */     
/*     */     DoFinallySubscriber(Subscriber<? super T> actual, Action onFinally) {
/*  65 */       this.downstream = actual;
/*  66 */       this.onFinally = onFinally;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  72 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  73 */         this.upstream = s;
/*  74 */         if (s instanceof QueueSubscription) {
/*  75 */           this.qs = (QueueSubscription<T>)s;
/*     */         }
/*     */         
/*  78 */         this.downstream.onSubscribe((Subscription)this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  84 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  89 */       this.downstream.onError(t);
/*  90 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       this.downstream.onComplete();
/*  96 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 101 */       this.upstream.cancel();
/* 102 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 107 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 112 */       QueueSubscription<T> qs = this.qs;
/* 113 */       if (qs != null && (mode & 0x4) == 0) {
/* 114 */         int m = qs.requestFusion(mode);
/* 115 */         if (m != 0) {
/* 116 */           this.syncFused = (m == 1);
/*     */         }
/* 118 */         return m;
/*     */       } 
/* 120 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 125 */       this.qs.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 130 */       return this.qs.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 136 */       T v = (T)this.qs.poll();
/* 137 */       if (v == null && this.syncFused) {
/* 138 */         runFinally();
/*     */       }
/* 140 */       return v;
/*     */     }
/*     */     
/*     */     void runFinally() {
/* 144 */       if (compareAndSet(0, 1)) {
/*     */         try {
/* 146 */           this.onFinally.run();
/* 147 */         } catch (Throwable ex) {
/* 148 */           Exceptions.throwIfFatal(ex);
/* 149 */           RxJavaPlugins.onError(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoFinallyConditionalSubscriber<T>
/*     */     extends BasicIntQueueSubscription<T>
/*     */     implements ConditionalSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4109457741734051389L;
/*     */     
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */     
/*     */     final Action onFinally;
/*     */     Subscription upstream;
/*     */     QueueSubscription<T> qs;
/*     */     boolean syncFused;
/*     */     
/*     */     DoFinallyConditionalSubscriber(ConditionalSubscriber<? super T> actual, Action onFinally) {
/* 170 */       this.downstream = actual;
/* 171 */       this.onFinally = onFinally;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 177 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 178 */         this.upstream = s;
/* 179 */         if (s instanceof QueueSubscription) {
/* 180 */           this.qs = (QueueSubscription<T>)s;
/*     */         }
/*     */         
/* 183 */         this.downstream.onSubscribe((Subscription)this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 189 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 194 */       return this.downstream.tryOnNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 199 */       this.downstream.onError(t);
/* 200 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 205 */       this.downstream.onComplete();
/* 206 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 211 */       this.upstream.cancel();
/* 212 */       runFinally();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 217 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 222 */       QueueSubscription<T> qs = this.qs;
/* 223 */       if (qs != null && (mode & 0x4) == 0) {
/* 224 */         int m = qs.requestFusion(mode);
/* 225 */         if (m != 0) {
/* 226 */           this.syncFused = (m == 1);
/*     */         }
/* 228 */         return m;
/*     */       } 
/* 230 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 235 */       this.qs.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 240 */       return this.qs.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 246 */       T v = (T)this.qs.poll();
/* 247 */       if (v == null && this.syncFused) {
/* 248 */         runFinally();
/*     */       }
/* 250 */       return v;
/*     */     }
/*     */     
/*     */     void runFinally() {
/* 254 */       if (compareAndSet(0, 1))
/*     */         try {
/* 256 */           this.onFinally.run();
/* 257 */         } catch (Throwable ex) {
/* 258 */           Exceptions.throwIfFatal(ex);
/* 259 */           RxJavaPlugins.onError(ex);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDoFinally.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */