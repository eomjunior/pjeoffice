/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.HalfSerializer;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class FlowableMergeWithCompletable<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final CompletableSource other;
/*     */   
/*     */   public FlowableMergeWithCompletable(Flowable<T> source, CompletableSource other) {
/*  38 */     super(source);
/*  39 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> subscriber) {
/*  44 */     MergeWithSubscriber<T> parent = new MergeWithSubscriber<T>(subscriber);
/*  45 */     subscriber.onSubscribe(parent);
/*  46 */     this.source.subscribe(parent);
/*  47 */     this.other.subscribe(parent.otherObserver);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeWithSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -4592979584110982903L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final AtomicReference<Subscription> mainSubscription;
/*     */     
/*     */     final OtherObserver otherObserver;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final AtomicLong requested;
/*     */     volatile boolean mainDone;
/*     */     volatile boolean otherDone;
/*     */     
/*     */     MergeWithSubscriber(Subscriber<? super T> downstream) {
/*  70 */       this.downstream = downstream;
/*  71 */       this.mainSubscription = new AtomicReference<Subscription>();
/*  72 */       this.otherObserver = new OtherObserver(this);
/*  73 */       this.error = new AtomicThrowable();
/*  74 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  79 */       SubscriptionHelper.deferredSetOnce(this.mainSubscription, this.requested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  84 */       HalfSerializer.onNext(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/*  89 */       DisposableHelper.dispose(this.otherObserver);
/*  90 */       HalfSerializer.onError(this.downstream, ex, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       this.mainDone = true;
/*  96 */       if (this.otherDone) {
/*  97 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 103 */       SubscriptionHelper.deferredRequest(this.mainSubscription, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 108 */       SubscriptionHelper.cancel(this.mainSubscription);
/* 109 */       DisposableHelper.dispose(this.otherObserver);
/*     */     }
/*     */     
/*     */     void otherError(Throwable ex) {
/* 113 */       SubscriptionHelper.cancel(this.mainSubscription);
/* 114 */       HalfSerializer.onError(this.downstream, ex, this, this.error);
/*     */     }
/*     */     
/*     */     void otherComplete() {
/* 118 */       this.otherDone = true;
/* 119 */       if (this.mainDone) {
/* 120 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       }
/*     */     }
/*     */     
/*     */     static final class OtherObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements CompletableObserver
/*     */     {
/*     */       private static final long serialVersionUID = -2935427570954647017L;
/*     */       final FlowableMergeWithCompletable.MergeWithSubscriber<?> parent;
/*     */       
/*     */       OtherObserver(FlowableMergeWithCompletable.MergeWithSubscriber<?> parent) {
/* 132 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 137 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 142 */         this.parent.otherError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 147 */         this.parent.otherComplete(); } } } static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver { public void onComplete() { this.parent.otherComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -2935427570954647017L;
/*     */     final FlowableMergeWithCompletable.MergeWithSubscriber<?> parent;
/*     */     
/*     */     OtherObserver(FlowableMergeWithCompletable.MergeWithSubscriber<?> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       this.parent.otherError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableMergeWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */