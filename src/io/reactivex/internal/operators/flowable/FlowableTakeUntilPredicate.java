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
/*     */ public final class FlowableTakeUntilPredicate<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public FlowableTakeUntilPredicate(Flowable<T> source, Predicate<? super T> predicate) {
/*  27 */     super(source);
/*  28 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  33 */     this.source.subscribe(new InnerSubscriber<T>(s, this.predicate));
/*     */   }
/*     */   
/*     */   static final class InnerSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Subscription {
/*     */     final Subscriber<? super T> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     
/*     */     InnerSubscriber(Subscriber<? super T> actual, Predicate<? super T> predicate) {
/*  42 */       this.downstream = actual;
/*  43 */       this.predicate = predicate;
/*     */     }
/*     */     Subscription upstream; boolean done;
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  48 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  49 */         this.upstream = s;
/*  50 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  56 */       if (!this.done) {
/*  57 */         boolean b; this.downstream.onNext(t);
/*     */         
/*     */         try {
/*  60 */           b = this.predicate.test(t);
/*  61 */         } catch (Throwable e) {
/*  62 */           Exceptions.throwIfFatal(e);
/*  63 */           this.upstream.cancel();
/*  64 */           onError(e);
/*     */           return;
/*     */         } 
/*  67 */         if (b) {
/*  68 */           this.done = true;
/*  69 */           this.upstream.cancel();
/*  70 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  77 */       if (!this.done) {
/*  78 */         this.done = true;
/*  79 */         this.downstream.onError(t);
/*     */       } else {
/*  81 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  87 */       if (!this.done) {
/*  88 */         this.done = true;
/*  89 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  95 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 100 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTakeUntilPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */