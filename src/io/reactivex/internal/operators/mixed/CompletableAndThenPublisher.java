/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ 
/*     */ public final class CompletableAndThenPublisher<R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final CompletableSource source;
/*     */   final Publisher<? extends R> other;
/*     */   
/*     */   public CompletableAndThenPublisher(CompletableSource source, Publisher<? extends R> other) {
/*  40 */     this.source = source;
/*  41 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  46 */     this.source.subscribe(new AndThenPublisherSubscriber<R>(s, this.other));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AndThenPublisherSubscriber<R>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<R>, CompletableObserver, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -8948264376121066672L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     Publisher<? extends R> other;
/*     */     
/*     */     Disposable upstream;
/*     */     final AtomicLong requested;
/*     */     
/*     */     AndThenPublisherSubscriber(Subscriber<? super R> downstream, Publisher<? extends R> other) {
/*  64 */       this.downstream = downstream;
/*  65 */       this.other = other;
/*  66 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R t) {
/*  71 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  76 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  81 */       Publisher<? extends R> p = this.other;
/*  82 */       if (p == null) {
/*  83 */         this.downstream.onComplete();
/*     */       } else {
/*  85 */         this.other = null;
/*  86 */         p.subscribe((Subscriber)this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  92 */       SubscriptionHelper.deferredRequest(this, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  97 */       this.upstream.dispose();
/*  98 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 103 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 104 */         this.upstream = d;
/* 105 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 111 */       SubscriptionHelper.deferredSetOnce(this, this.requested, s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/CompletableAndThenPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */