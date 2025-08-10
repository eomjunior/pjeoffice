/*    */ package io.reactivex.internal.subscriptions;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class AsyncSubscription
/*    */   extends AtomicLong
/*    */   implements Subscription, Disposable
/*    */ {
/* 38 */   final AtomicReference<Disposable> resource = new AtomicReference<Disposable>();
/* 39 */   final AtomicReference<Subscription> actual = new AtomicReference<Subscription>();
/*    */   private static final long serialVersionUID = 7028635084060361255L;
/*    */   
/*    */   public AsyncSubscription(Disposable resource) {
/* 43 */     this();
/* 44 */     this.resource.lazySet(resource);
/*    */   }
/*    */ 
/*    */   
/*    */   public void request(long n) {
/* 49 */     SubscriptionHelper.deferredRequest(this.actual, this, n);
/*    */   }
/*    */   public AsyncSubscription() {}
/*    */   
/*    */   public void cancel() {
/* 54 */     dispose();
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 59 */     SubscriptionHelper.cancel(this.actual);
/* 60 */     DisposableHelper.dispose(this.resource);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 65 */     return (this.actual.get() == SubscriptionHelper.CANCELLED);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean setResource(Disposable r) {
/* 75 */     return DisposableHelper.set(this.resource, r);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean replaceResource(Disposable r) {
/* 84 */     return DisposableHelper.replace(this.resource, r);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSubscription(Subscription s) {
/* 92 */     SubscriptionHelper.deferredSetOnce(this.actual, this, s);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/AsyncSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */