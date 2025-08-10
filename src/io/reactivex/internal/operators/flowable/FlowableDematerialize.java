/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Notification;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
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
/*     */ 
/*     */ public final class FlowableDematerialize<T, R>
/*     */   extends AbstractFlowableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends Notification<R>> selector;
/*     */   
/*     */   public FlowableDematerialize(Flowable<T> source, Function<? super T, ? extends Notification<R>> selector) {
/*  30 */     super(source);
/*  31 */     this.selector = selector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> subscriber) {
/*  36 */     this.source.subscribe(new DematerializeSubscriber<T, R>(subscriber, this.selector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DematerializeSubscriber<T, R>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends Notification<R>> selector;
/*     */     boolean done;
/*     */     Subscription upstream;
/*     */     
/*     */     DematerializeSubscriber(Subscriber<? super R> downstream, Function<? super T, ? extends Notification<R>> selector) {
/*  50 */       this.downstream = downstream;
/*  51 */       this.selector = selector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  56 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  57 */         this.upstream = s;
/*  58 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T item) {
/*     */       Notification<R> notification;
/*  64 */       if (this.done) {
/*  65 */         if (item instanceof Notification) {
/*  66 */           notification = (Notification)item;
/*  67 */           if (notification.isOnError()) {
/*  68 */             RxJavaPlugins.onError(notification.getError());
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/*  77 */         notification = (Notification<R>)ObjectHelper.requireNonNull(this.selector.apply(item), "The selector returned a null Notification");
/*  78 */       } catch (Throwable ex) {
/*  79 */         Exceptions.throwIfFatal(ex);
/*  80 */         this.upstream.cancel();
/*  81 */         onError(ex);
/*     */         return;
/*     */       } 
/*  84 */       if (notification.isOnError()) {
/*  85 */         this.upstream.cancel();
/*  86 */         onError(notification.getError());
/*  87 */       } else if (notification.isOnComplete()) {
/*  88 */         this.upstream.cancel();
/*  89 */         onComplete();
/*     */       } else {
/*  91 */         this.downstream.onNext(notification.getValue());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  97 */       if (this.done) {
/*  98 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 101 */       this.done = true;
/*     */       
/* 103 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 108 */       if (this.done) {
/*     */         return;
/*     */       }
/* 111 */       this.done = true;
/*     */       
/* 113 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 118 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 123 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDematerialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */