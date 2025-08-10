/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
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
/*     */ public final class FlowableAll<T>
/*     */   extends AbstractFlowableWithUpstream<T, Boolean>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public FlowableAll(Flowable<T> source, Predicate<? super T> predicate) {
/*  28 */     super(source);
/*  29 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super Boolean> s) {
/*  34 */     this.source.subscribe(new AllSubscriber<T>(s, this.predicate));
/*     */   }
/*     */   
/*     */   static final class AllSubscriber<T>
/*     */     extends DeferredScalarSubscription<Boolean>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3521127104134758517L;
/*     */     final Predicate<? super T> predicate;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     AllSubscriber(Subscriber<? super Boolean> actual, Predicate<? super T> predicate) {
/*  47 */       super(actual);
/*  48 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  53 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  54 */         this.upstream = s;
/*  55 */         this.downstream.onSubscribe((Subscription)this);
/*  56 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  62 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  67 */         b = this.predicate.test(t);
/*  68 */       } catch (Throwable e) {
/*  69 */         Exceptions.throwIfFatal(e);
/*  70 */         this.upstream.cancel();
/*  71 */         onError(e);
/*     */         return;
/*     */       } 
/*  74 */       if (!b) {
/*  75 */         this.done = true;
/*  76 */         this.upstream.cancel();
/*  77 */         complete(Boolean.valueOf(false));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  83 */       if (this.done) {
/*  84 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  87 */       this.done = true;
/*  88 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  93 */       if (this.done) {
/*     */         return;
/*     */       }
/*  96 */       this.done = true;
/*     */       
/*  98 */       complete(Boolean.valueOf(true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 103 */       super.cancel();
/* 104 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableAll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */