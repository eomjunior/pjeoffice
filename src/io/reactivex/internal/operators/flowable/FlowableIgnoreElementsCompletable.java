/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public final class FlowableIgnoreElementsCompletable<T>
/*    */   extends Completable
/*    */   implements FuseToFlowable<T>
/*    */ {
/*    */   final Flowable<T> source;
/*    */   
/*    */   public FlowableIgnoreElementsCompletable(Flowable<T> source) {
/* 29 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver t) {
/* 34 */     this.source.subscribe(new IgnoreElementsSubscriber(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public Flowable<T> fuseToFlowable() {
/* 39 */     return RxJavaPlugins.onAssembly(new FlowableIgnoreElements<T>(this.source));
/*    */   }
/*    */   
/*    */   static final class IgnoreElementsSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Disposable {
/*    */     final CompletableObserver downstream;
/*    */     Subscription upstream;
/*    */     
/*    */     IgnoreElementsSubscriber(CompletableObserver downstream) {
/* 48 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 53 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 54 */         this.upstream = s;
/* 55 */         this.downstream.onSubscribe(this);
/* 56 */         s.request(Long.MAX_VALUE);
/*    */       } 
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onNext(T t) {}
/*    */ 
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 67 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 68 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 73 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 74 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 79 */       this.upstream.cancel();
/* 80 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 85 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableIgnoreElementsCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */