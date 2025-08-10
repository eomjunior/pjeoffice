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
/*     */ public final class FlowableInterval
/*     */   extends Flowable<Long>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   final long initialDelay;
/*     */   final long period;
/*     */   final TimeUnit unit;
/*     */   
/*     */   public FlowableInterval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/*  37 */     this.initialDelay = initialDelay;
/*  38 */     this.period = period;
/*  39 */     this.unit = unit;
/*  40 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super Long> s) {
/*  45 */     IntervalSubscriber is = new IntervalSubscriber(s);
/*  46 */     s.onSubscribe(is);
/*     */     
/*  48 */     Scheduler sch = this.scheduler;
/*     */     
/*  50 */     if (sch instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
/*  51 */       Scheduler.Worker worker = sch.createWorker();
/*  52 */       is.setResource((Disposable)worker);
/*  53 */       worker.schedulePeriodically(is, this.initialDelay, this.period, this.unit);
/*     */     } else {
/*  55 */       Disposable d = sch.schedulePeriodicallyDirect(is, this.initialDelay, this.period, this.unit);
/*  56 */       is.setResource(d);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class IntervalSubscriber
/*     */     extends AtomicLong
/*     */     implements Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -2809475196591179431L;
/*     */     
/*     */     final Subscriber<? super Long> downstream;
/*     */     long count;
/*  69 */     final AtomicReference<Disposable> resource = new AtomicReference<Disposable>();
/*     */     
/*     */     IntervalSubscriber(Subscriber<? super Long> downstream) {
/*  72 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  77 */       if (SubscriptionHelper.validate(n)) {
/*  78 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  84 */       DisposableHelper.dispose(this.resource);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  89 */       if (this.resource.get() != DisposableHelper.DISPOSED) {
/*  90 */         long r = get();
/*     */         
/*  92 */         if (r != 0L) {
/*  93 */           this.downstream.onNext(Long.valueOf(this.count++));
/*  94 */           BackpressureHelper.produced(this, 1L);
/*     */         } else {
/*  96 */           this.downstream.onError((Throwable)new MissingBackpressureException("Can't deliver value " + this.count + " due to lack of requests"));
/*  97 */           DisposableHelper.dispose(this.resource);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setResource(Disposable d) {
/* 103 */       DisposableHelper.setOnce(this.resource, d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableInterval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */