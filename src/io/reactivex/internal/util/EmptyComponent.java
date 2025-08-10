/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public enum EmptyComponent
/*    */   implements FlowableSubscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>, CompletableObserver, Subscription, Disposable
/*    */ {
/* 27 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public static <T> Subscriber<T> asSubscriber() {
/* 31 */     return (Subscriber<T>)INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> Observer<T> asObserver() {
/* 36 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void dispose() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void request(long n) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void cancel() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 61 */     d.dispose();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Subscription s) {
/* 66 */     s.cancel();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onNext(Object t) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 76 */     RxJavaPlugins.onError(t);
/*    */   }
/*    */   
/*    */   public void onComplete() {}
/*    */   
/*    */   public void onSuccess(Object value) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/EmptyComponent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */