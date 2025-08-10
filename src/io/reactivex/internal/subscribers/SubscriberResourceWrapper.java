/*    */ package io.reactivex.internal.subscribers;
/*    */ 
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class SubscriberResourceWrapper<T>
/*    */   extends AtomicReference<Disposable>
/*    */   implements FlowableSubscriber<T>, Disposable, Subscription
/*    */ {
/*    */   private static final long serialVersionUID = -8612022020200669122L;
/*    */   final Subscriber<? super T> downstream;
/* 31 */   final AtomicReference<Subscription> upstream = new AtomicReference<Subscription>();
/*    */   
/*    */   public SubscriberResourceWrapper(Subscriber<? super T> downstream) {
/* 34 */     this.downstream = downstream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Subscription s) {
/* 39 */     if (SubscriptionHelper.setOnce(this.upstream, s)) {
/* 40 */       this.downstream.onSubscribe(this);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onNext(T t) {
/* 46 */     this.downstream.onNext(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 51 */     DisposableHelper.dispose(this);
/* 52 */     this.downstream.onError(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 57 */     DisposableHelper.dispose(this);
/* 58 */     this.downstream.onComplete();
/*    */   }
/*    */ 
/*    */   
/*    */   public void request(long n) {
/* 63 */     if (SubscriptionHelper.validate(n)) {
/* 64 */       ((Subscription)this.upstream.get()).request(n);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 70 */     SubscriptionHelper.cancel(this.upstream);
/*    */     
/* 72 */     DisposableHelper.dispose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 77 */     return (this.upstream.get() == SubscriptionHelper.CANCELLED);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 82 */     dispose();
/*    */   }
/*    */   
/*    */   public void setResource(Disposable resource) {
/* 86 */     DisposableHelper.set(this, resource);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/SubscriberResourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */