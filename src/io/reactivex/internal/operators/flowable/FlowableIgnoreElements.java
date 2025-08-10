/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ public final class FlowableIgnoreElements<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   public FlowableIgnoreElements(Flowable<T> source) {
/*  26 */     super(source);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> t) {
/*  31 */     this.source.subscribe(new IgnoreElementsSubscriber<T>(t));
/*     */   }
/*     */   
/*     */   static final class IgnoreElementsSubscriber<T>
/*     */     implements FlowableSubscriber<T>, QueueSubscription<T> {
/*     */     final Subscriber<? super T> downstream;
/*     */     Subscription upstream;
/*     */     
/*     */     IgnoreElementsSubscriber(Subscriber<? super T> downstream) {
/*  40 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  45 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  46 */         this.upstream = s;
/*  47 */         this.downstream.onSubscribe((Subscription)this);
/*  48 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  59 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  64 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(T e) {
/*  69 */       throw new UnsupportedOperationException("Should not be called!");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(T v1, T v2) {
/*  74 */       throw new UnsupportedOperationException("Should not be called!");
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() {
/*  80 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/*  85 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void request(long n) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 100 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 105 */       return mode & 0x2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableIgnoreElements.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */