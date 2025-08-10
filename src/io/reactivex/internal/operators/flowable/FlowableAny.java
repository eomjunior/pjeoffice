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
/*     */ public final class FlowableAny<T>
/*     */   extends AbstractFlowableWithUpstream<T, Boolean>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public FlowableAny(Flowable<T> source, Predicate<? super T> predicate) {
/*  26 */     super(source);
/*  27 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super Boolean> s) {
/*  32 */     this.source.subscribe(new AnySubscriber<T>(s, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AnySubscriber<T>
/*     */     extends DeferredScalarSubscription<Boolean>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -2311252482644620661L;
/*     */     final Predicate<? super T> predicate;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     AnySubscriber(Subscriber<? super Boolean> actual, Predicate<? super T> predicate) {
/*  46 */       super(actual);
/*  47 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  52 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  53 */         this.upstream = s;
/*  54 */         this.downstream.onSubscribe((Subscription)this);
/*  55 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  61 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  66 */         b = this.predicate.test(t);
/*  67 */       } catch (Throwable e) {
/*  68 */         Exceptions.throwIfFatal(e);
/*  69 */         this.upstream.cancel();
/*  70 */         onError(e);
/*     */         return;
/*     */       } 
/*  73 */       if (b) {
/*  74 */         this.done = true;
/*  75 */         this.upstream.cancel();
/*  76 */         complete(Boolean.valueOf(true));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  82 */       if (this.done) {
/*  83 */         RxJavaPlugins.onError(t);
/*     */         
/*     */         return;
/*     */       } 
/*  87 */       this.done = true;
/*  88 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  93 */       if (!this.done) {
/*  94 */         this.done = true;
/*  95 */         complete(Boolean.valueOf(false));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 101 */       super.cancel();
/* 102 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableAny.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */