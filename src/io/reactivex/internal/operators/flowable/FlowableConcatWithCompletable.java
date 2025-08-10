/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableConcatWithCompletable<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final CompletableSource other;
/*     */   
/*     */   public FlowableConcatWithCompletable(Flowable<T> source, CompletableSource other) {
/*  37 */     super(source);
/*  38 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  43 */     this.source.subscribe(new ConcatWithSubscriber<T>(s, this.other));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatWithSubscriber<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements FlowableSubscriber<T>, CompletableObserver, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -7346385463600070225L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     CompletableSource other;
/*     */     boolean inCompletable;
/*     */     
/*     */     ConcatWithSubscriber(Subscriber<? super T> actual, CompletableSource other) {
/*  61 */       this.downstream = actual;
/*  62 */       this.other = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  67 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  68 */         this.upstream = s;
/*  69 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  75 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  80 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  85 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  90 */       if (this.inCompletable) {
/*  91 */         this.downstream.onComplete();
/*     */       } else {
/*  93 */         this.inCompletable = true;
/*  94 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  95 */         CompletableSource cs = this.other;
/*  96 */         this.other = null;
/*  97 */         cs.subscribe(this);
/*     */       } 
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
/* 109 */       DisposableHelper.dispose(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */