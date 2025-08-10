/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableElementAtMaybe<T>
/*     */   extends Maybe<T>
/*     */   implements FuseToFlowable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final long index;
/*     */   
/*     */   public FlowableElementAtMaybe(Flowable<T> source, long index) {
/*  30 */     this.source = source;
/*  31 */     this.index = index;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  36 */     this.source.subscribe(new ElementAtSubscriber<T>(observer, this.index));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<T> fuseToFlowable() {
/*  41 */     return RxJavaPlugins.onAssembly(new FlowableElementAt<T>(this.source, this.index, null, false));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ElementAtSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final long index;
/*     */     
/*     */     Subscription upstream;
/*     */     long count;
/*     */     boolean done;
/*     */     
/*     */     ElementAtSubscriber(MaybeObserver<? super T> actual, long index) {
/*  57 */       this.downstream = actual;
/*  58 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  63 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  64 */         this.upstream = s;
/*  65 */         this.downstream.onSubscribe(this);
/*  66 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  72 */       if (this.done) {
/*     */         return;
/*     */       }
/*  75 */       long c = this.count;
/*  76 */       if (c == this.index) {
/*  77 */         this.done = true;
/*  78 */         this.upstream.cancel();
/*  79 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  80 */         this.downstream.onSuccess(t);
/*     */         return;
/*     */       } 
/*  83 */       this.count = c + 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  88 */       if (this.done) {
/*  89 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  92 */       this.done = true;
/*  93 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  94 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  99 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 100 */       if (!this.done) {
/* 101 */         this.done = true;
/* 102 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 108 */       this.upstream.cancel();
/* 109 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 114 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableElementAtMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */