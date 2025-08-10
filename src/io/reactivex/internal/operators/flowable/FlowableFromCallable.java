/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.Callable;
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
/*    */ public final class FlowableFromCallable<T>
/*    */   extends Flowable<T>
/*    */   implements Callable<T>
/*    */ {
/*    */   final Callable<? extends T> callable;
/*    */   
/*    */   public FlowableFromCallable(Callable<? extends T> callable) {
/* 29 */     this.callable = callable;
/*    */   }
/*    */   
/*    */   public void subscribeActual(Subscriber<? super T> s) {
/*    */     T t;
/* 34 */     DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription(s);
/* 35 */     s.onSubscribe((Subscription)deferred);
/*    */ 
/*    */     
/*    */     try {
/* 39 */       t = (T)ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value");
/* 40 */     } catch (Throwable ex) {
/* 41 */       Exceptions.throwIfFatal(ex);
/* 42 */       if (deferred.isCancelled()) {
/* 43 */         RxJavaPlugins.onError(ex);
/*    */       } else {
/* 45 */         s.onError(ex);
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 50 */     deferred.complete(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public T call() throws Exception {
/* 55 */     return (T)ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFromCallable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */