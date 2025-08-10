/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.MissingBackpressureException;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.reactivestreams.Subscriber;
/*    */ import org.reactivestreams.Subscription;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableTimer
/*    */   extends Flowable<Long>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   final long delay;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public FlowableTimer(long delay, TimeUnit unit, Scheduler scheduler) {
/* 32 */     this.delay = delay;
/* 33 */     this.unit = unit;
/* 34 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super Long> s) {
/* 39 */     TimerSubscriber ios = new TimerSubscriber(s);
/* 40 */     s.onSubscribe(ios);
/*    */     
/* 42 */     Disposable d = this.scheduler.scheduleDirect(ios, this.delay, this.unit);
/*    */     
/* 44 */     ios.setResource(d);
/*    */   }
/*    */ 
/*    */   
/*    */   static final class TimerSubscriber
/*    */     extends AtomicReference<Disposable>
/*    */     implements Subscription, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = -2809475196591179431L;
/*    */     final Subscriber<? super Long> downstream;
/*    */     volatile boolean requested;
/*    */     
/*    */     TimerSubscriber(Subscriber<? super Long> downstream) {
/* 57 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 62 */       if (SubscriptionHelper.validate(n)) {
/* 63 */         this.requested = true;
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 69 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 74 */       if (get() != DisposableHelper.DISPOSED) {
/* 75 */         if (this.requested) {
/* 76 */           this.downstream.onNext(Long.valueOf(0L));
/* 77 */           lazySet((Disposable)EmptyDisposable.INSTANCE);
/* 78 */           this.downstream.onComplete();
/*    */         } else {
/* 80 */           lazySet((Disposable)EmptyDisposable.INSTANCE);
/* 81 */           this.downstream.onError((Throwable)new MissingBackpressureException("Can't deliver value due to lack of requests"));
/*    */         } 
/*    */       }
/*    */     }
/*    */     
/*    */     public void setResource(Disposable d) {
/* 87 */       DisposableHelper.trySet(this, d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */