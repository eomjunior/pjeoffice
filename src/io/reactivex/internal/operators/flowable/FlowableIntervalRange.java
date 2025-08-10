/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableIntervalRange
/*     */   extends Flowable<Long>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   final long start;
/*     */   final long end;
/*     */   final long initialDelay;
/*     */   final long period;
/*     */   final TimeUnit unit;
/*     */   
/*     */   public FlowableIntervalRange(long start, long end, long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/*  39 */     this.initialDelay = initialDelay;
/*  40 */     this.period = period;
/*  41 */     this.unit = unit;
/*  42 */     this.scheduler = scheduler;
/*  43 */     this.start = start;
/*  44 */     this.end = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super Long> s) {
/*  49 */     IntervalRangeSubscriber is = new IntervalRangeSubscriber(s, this.start, this.end);
/*  50 */     s.onSubscribe(is);
/*     */     
/*  52 */     Scheduler sch = this.scheduler;
/*     */     
/*  54 */     if (sch instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
/*  55 */       Scheduler.Worker worker = sch.createWorker();
/*  56 */       is.setResource((Disposable)worker);
/*  57 */       worker.schedulePeriodically(is, this.initialDelay, this.period, this.unit);
/*     */     } else {
/*  59 */       Disposable d = sch.schedulePeriodicallyDirect(is, this.initialDelay, this.period, this.unit);
/*  60 */       is.setResource(d);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class IntervalRangeSubscriber
/*     */     extends AtomicLong
/*     */     implements Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -2809475196591179431L;
/*     */     
/*     */     final Subscriber<? super Long> downstream;
/*     */     final long end;
/*     */     long count;
/*  74 */     final AtomicReference<Disposable> resource = new AtomicReference<Disposable>();
/*     */     
/*     */     IntervalRangeSubscriber(Subscriber<? super Long> actual, long start, long end) {
/*  77 */       this.downstream = actual;
/*  78 */       this.count = start;
/*  79 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  84 */       if (SubscriptionHelper.validate(n)) {
/*  85 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  91 */       DisposableHelper.dispose(this.resource);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  96 */       if (this.resource.get() != DisposableHelper.DISPOSED) {
/*  97 */         long r = get();
/*     */         
/*  99 */         if (r != 0L) {
/* 100 */           long c = this.count;
/* 101 */           this.downstream.onNext(Long.valueOf(c));
/*     */           
/* 103 */           if (c == this.end) {
/* 104 */             if (this.resource.get() != DisposableHelper.DISPOSED) {
/* 105 */               this.downstream.onComplete();
/*     */             }
/* 107 */             DisposableHelper.dispose(this.resource);
/*     */             
/*     */             return;
/*     */           } 
/* 111 */           this.count = c + 1L;
/*     */           
/* 113 */           if (r != Long.MAX_VALUE) {
/* 114 */             decrementAndGet();
/*     */           }
/*     */         } else {
/* 117 */           this.downstream.onError((Throwable)new MissingBackpressureException("Can't deliver value " + this.count + " due to lack of requests"));
/* 118 */           DisposableHelper.dispose(this.resource);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setResource(Disposable d) {
/* 124 */       DisposableHelper.setOnce(this.resource, d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableIntervalRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */