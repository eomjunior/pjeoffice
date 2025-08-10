/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
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
/*     */ public final class FlowableConcatWithMaybe<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final MaybeSource<? extends T> other;
/*     */   
/*     */   public FlowableConcatWithMaybe(Flowable<T> source, MaybeSource<? extends T> other) {
/*  38 */     super(source);
/*  39 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  44 */     this.source.subscribe((FlowableSubscriber)new ConcatWithSubscriber<T>(s, this.other));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatWithSubscriber<T>
/*     */     extends SinglePostCompleteSubscriber<T, T>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7346385463600070225L;
/*     */     
/*     */     final AtomicReference<Disposable> otherDisposable;
/*     */     
/*     */     MaybeSource<? extends T> other;
/*     */     boolean inMaybe;
/*     */     
/*     */     ConcatWithSubscriber(Subscriber<? super T> actual, MaybeSource<? extends T> other) {
/*  60 */       super(actual);
/*  61 */       this.other = other;
/*  62 */       this.otherDisposable = new AtomicReference<Disposable>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  67 */       DisposableHelper.setOnce(this.otherDisposable, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  72 */       this.produced++;
/*  73 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  78 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T t) {
/*  83 */       complete(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  88 */       if (this.inMaybe) {
/*  89 */         this.downstream.onComplete();
/*     */       } else {
/*  91 */         this.inMaybe = true;
/*  92 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  93 */         MaybeSource<? extends T> ms = this.other;
/*  94 */         this.other = null;
/*  95 */         ms.subscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 101 */       super.cancel();
/* 102 */       DisposableHelper.dispose(this.otherDisposable);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatWithMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */