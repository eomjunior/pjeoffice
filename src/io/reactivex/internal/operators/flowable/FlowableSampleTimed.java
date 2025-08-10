/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class FlowableSampleTimed<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long period;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final boolean emitLast;
/*     */   
/*     */   public FlowableSampleTimed(Flowable<T> source, long period, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/*  36 */     super(source);
/*  37 */     this.period = period;
/*  38 */     this.unit = unit;
/*  39 */     this.scheduler = scheduler;
/*  40 */     this.emitLast = emitLast;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  45 */     SerializedSubscriber<T> serial = new SerializedSubscriber(s);
/*  46 */     if (this.emitLast) {
/*  47 */       this.source.subscribe(new SampleTimedEmitLast<T>((Subscriber<? super T>)serial, this.period, this.unit, this.scheduler));
/*     */     } else {
/*  49 */       this.source.subscribe(new SampleTimedNoLast<T>((Subscriber<? super T>)serial, this.period, this.unit, this.scheduler));
/*     */     } 
/*     */   }
/*     */   
/*     */   static abstract class SampleTimedSubscriber<T>
/*     */     extends AtomicReference<T>
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -3517602651313910099L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final long period;
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*  62 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*  64 */     final SequentialDisposable timer = new SequentialDisposable();
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     SampleTimedSubscriber(Subscriber<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
/*  69 */       this.downstream = actual;
/*  70 */       this.period = period;
/*  71 */       this.unit = unit;
/*  72 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  77 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  78 */         this.upstream = s;
/*  79 */         this.downstream.onSubscribe(this);
/*  80 */         this.timer.replace(this.scheduler.schedulePeriodicallyDirect(this, this.period, this.period, this.unit));
/*  81 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  87 */       lazySet(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  92 */       cancelTimer();
/*  93 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  98 */       cancelTimer();
/*  99 */       complete();
/*     */     }
/*     */     
/*     */     void cancelTimer() {
/* 103 */       DisposableHelper.dispose((AtomicReference)this.timer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 108 */       if (SubscriptionHelper.validate(n)) {
/* 109 */         BackpressureHelper.add(this.requested, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 115 */       cancelTimer();
/* 116 */       this.upstream.cancel();
/*     */     }
/*     */     
/*     */     void emit() {
/* 120 */       T value = getAndSet(null);
/* 121 */       if (value != null) {
/* 122 */         long r = this.requested.get();
/* 123 */         if (r != 0L) {
/* 124 */           this.downstream.onNext(value);
/* 125 */           BackpressureHelper.produced(this.requested, 1L);
/*     */         } else {
/* 127 */           cancel();
/* 128 */           this.downstream.onError((Throwable)new MissingBackpressureException("Couldn't emit value due to lack of requests!"));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void complete();
/*     */   }
/*     */   
/*     */   static final class SampleTimedNoLast<T>
/*     */     extends SampleTimedSubscriber<T> {
/*     */     private static final long serialVersionUID = -7139995637533111443L;
/*     */     
/*     */     SampleTimedNoLast(Subscriber<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
/* 141 */       super(actual, period, unit, scheduler);
/*     */     }
/*     */ 
/*     */     
/*     */     void complete() {
/* 146 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 151 */       emit();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SampleTimedEmitLast<T>
/*     */     extends SampleTimedSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7139995637533111443L;
/*     */     final AtomicInteger wip;
/*     */     
/*     */     SampleTimedEmitLast(Subscriber<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
/* 162 */       super(actual, period, unit, scheduler);
/* 163 */       this.wip = new AtomicInteger(1);
/*     */     }
/*     */ 
/*     */     
/*     */     void complete() {
/* 168 */       emit();
/* 169 */       if (this.wip.decrementAndGet() == 0) {
/* 170 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 176 */       if (this.wip.incrementAndGet() == 2) {
/* 177 */         emit();
/* 178 */         if (this.wip.decrementAndGet() == 0)
/* 179 */           this.downstream.onComplete(); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSampleTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */