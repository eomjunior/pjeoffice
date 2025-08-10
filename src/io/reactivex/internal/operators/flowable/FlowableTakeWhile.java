/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
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
/*     */ public final class FlowableTakeWhile<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public FlowableTakeWhile(Flowable<T> source, Predicate<? super T> predicate) {
/*  27 */     super(source);
/*  28 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  33 */     this.source.subscribe(new TakeWhileSubscriber<T>(s, this.predicate));
/*     */   }
/*     */   
/*     */   static final class TakeWhileSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     TakeWhileSubscriber(Subscriber<? super T> actual, Predicate<? super T> predicate) {
/*  45 */       this.downstream = actual;
/*  46 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  51 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  52 */         this.upstream = s;
/*  53 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  59 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  64 */         b = this.predicate.test(t);
/*  65 */       } catch (Throwable e) {
/*  66 */         Exceptions.throwIfFatal(e);
/*  67 */         this.upstream.cancel();
/*  68 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  72 */       if (!b) {
/*  73 */         this.done = true;
/*  74 */         this.upstream.cancel();
/*  75 */         this.downstream.onComplete();
/*     */         
/*     */         return;
/*     */       } 
/*  79 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  84 */       if (this.done) {
/*  85 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  88 */       this.done = true;
/*  89 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  94 */       if (this.done) {
/*     */         return;
/*     */       }
/*  97 */       this.done = true;
/*  98 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 103 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 108 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTakeWhile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */