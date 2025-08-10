/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class FlowableDelay<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long delay;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final boolean delayError;
/*     */   
/*     */   public FlowableDelay(Flowable<T> source, long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/*  32 */     super(source);
/*  33 */     this.delay = delay;
/*  34 */     this.unit = unit;
/*  35 */     this.scheduler = scheduler;
/*  36 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> t) {
/*     */     SerializedSubscriber serializedSubscriber;
/*  42 */     if (this.delayError) {
/*  43 */       Subscriber<? super T> downstream = t;
/*     */     } else {
/*  45 */       serializedSubscriber = new SerializedSubscriber(t);
/*     */     } 
/*     */     
/*  48 */     Scheduler.Worker w = this.scheduler.createWorker();
/*     */     
/*  50 */     this.source.subscribe(new DelaySubscriber((Subscriber<?>)serializedSubscriber, this.delay, this.unit, w, this.delayError));
/*     */   }
/*     */   
/*     */   static final class DelaySubscriber<T>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     final long delay;
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker w;
/*     */     final boolean delayError;
/*     */     Subscription upstream;
/*     */     
/*     */     DelaySubscriber(Subscriber<? super T> actual, long delay, TimeUnit unit, Scheduler.Worker w, boolean delayError) {
/*  64 */       this.downstream = actual;
/*  65 */       this.delay = delay;
/*  66 */       this.unit = unit;
/*  67 */       this.w = w;
/*  68 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  73 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  74 */         this.upstream = s;
/*  75 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  81 */       this.w.schedule(new OnNext(t), this.delay, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  86 */       this.w.schedule(new OnError(t), this.delayError ? this.delay : 0L, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  91 */       this.w.schedule(new OnComplete(), this.delay, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  96 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 101 */       this.upstream.cancel();
/* 102 */       this.w.dispose();
/*     */     }
/*     */     
/*     */     final class OnNext implements Runnable {
/*     */       private final T t;
/*     */       
/*     */       OnNext(T t) {
/* 109 */         this.t = t;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 114 */         FlowableDelay.DelaySubscriber.this.downstream.onNext(this.t);
/*     */       }
/*     */     }
/*     */     
/*     */     final class OnError implements Runnable {
/*     */       private final Throwable t;
/*     */       
/*     */       OnError(Throwable t) {
/* 122 */         this.t = t;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/*     */         try {
/* 128 */           FlowableDelay.DelaySubscriber.this.downstream.onError(this.t);
/*     */         } finally {
/* 130 */           FlowableDelay.DelaySubscriber.this.w.dispose();
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     final class OnComplete
/*     */       implements Runnable {
/*     */       public void run() {
/*     */         try {
/* 139 */           FlowableDelay.DelaySubscriber.this.downstream.onComplete();
/*     */         } finally {
/* 141 */           FlowableDelay.DelaySubscriber.this.w.dispose();
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */