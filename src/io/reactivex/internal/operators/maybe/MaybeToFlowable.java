/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
/*    */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*    */ import org.reactivestreams.Subscriber;
/*    */ import org.reactivestreams.Subscription;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeToFlowable<T>
/*    */   extends Flowable<T>
/*    */   implements HasUpstreamMaybeSource<T>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   
/*    */   public MaybeToFlowable(MaybeSource<T> source) {
/* 35 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public MaybeSource<T> source() {
/* 40 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 45 */     this.source.subscribe(new MaybeToFlowableSubscriber<T>(s));
/*    */   }
/*    */   
/*    */   static final class MaybeToFlowableSubscriber<T>
/*    */     extends DeferredScalarSubscription<T>
/*    */     implements MaybeObserver<T>
/*    */   {
/*    */     private static final long serialVersionUID = 7603343402964826922L;
/*    */     Disposable upstream;
/*    */     
/*    */     MaybeToFlowableSubscriber(Subscriber<? super T> downstream) {
/* 56 */       super(downstream);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 61 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 62 */         this.upstream = d;
/*    */         
/* 64 */         this.downstream.onSubscribe((Subscription)this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 70 */       complete(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 75 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 80 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 85 */       super.cancel();
/* 86 */       this.upstream.dispose();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeToFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */