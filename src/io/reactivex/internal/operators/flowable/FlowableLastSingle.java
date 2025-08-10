/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class FlowableLastSingle<T>
/*     */   extends Single<T>
/*     */ {
/*     */   final Publisher<T> source;
/*     */   final T defaultItem;
/*     */   
/*     */   public FlowableLastSingle(Publisher<T> source, T defaultItem) {
/*  37 */     this.source = source;
/*  38 */     this.defaultItem = defaultItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  45 */     this.source.subscribe((Subscriber)new LastSubscriber<T>(observer, this.defaultItem));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LastSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final T defaultItem;
/*     */     Subscription upstream;
/*     */     T item;
/*     */     
/*     */     LastSubscriber(SingleObserver<? super T> actual, T defaultItem) {
/*  59 */       this.downstream = actual;
/*  60 */       this.defaultItem = defaultItem;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  65 */       this.upstream.cancel();
/*  66 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  71 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  76 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  77 */         this.upstream = s;
/*     */         
/*  79 */         this.downstream.onSubscribe(this);
/*     */         
/*  81 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  87 */       this.item = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  92 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  93 */       this.item = null;
/*  94 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  99 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 100 */       T v = this.item;
/* 101 */       if (v != null) {
/* 102 */         this.item = null;
/* 103 */         this.downstream.onSuccess(v);
/*     */       } else {
/* 105 */         v = this.defaultItem;
/*     */         
/* 107 */         if (v != null) {
/* 108 */           this.downstream.onSuccess(v);
/*     */         } else {
/* 110 */           this.downstream.onError(new NoSuchElementException());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableLastSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */