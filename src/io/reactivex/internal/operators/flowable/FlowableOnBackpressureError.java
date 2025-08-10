/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableOnBackpressureError<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   public FlowableOnBackpressureError(Flowable<T> source) {
/*  29 */     super(source);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  34 */     this.source.subscribe(new BackpressureErrorSubscriber<T>(s));
/*     */   }
/*     */   
/*     */   static final class BackpressureErrorSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription {
/*     */     private static final long serialVersionUID = -3176480756392482682L;
/*     */     final Subscriber<? super T> downstream;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     BackpressureErrorSubscriber(Subscriber<? super T> downstream) {
/*  46 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  51 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  52 */         this.upstream = s;
/*  53 */         this.downstream.onSubscribe(this);
/*  54 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  60 */       if (this.done) {
/*     */         return;
/*     */       }
/*  63 */       long r = get();
/*  64 */       if (r != 0L) {
/*  65 */         this.downstream.onNext(t);
/*  66 */         BackpressureHelper.produced(this, 1L);
/*     */       } else {
/*  68 */         this.upstream.cancel();
/*  69 */         onError((Throwable)new MissingBackpressureException("could not emit value due to lack of requests"));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  75 */       if (this.done) {
/*  76 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  79 */       this.done = true;
/*  80 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  85 */       if (this.done) {
/*     */         return;
/*     */       }
/*  88 */       this.done = true;
/*  89 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  94 */       if (SubscriptionHelper.validate(n)) {
/*  95 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 101 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */