/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public final class FlowableFromFuture<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final Future<? extends T> future;
/*    */   final long timeout;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public FlowableFromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
/* 30 */     this.future = future;
/* 31 */     this.timeout = timeout;
/* 32 */     this.unit = unit;
/*    */   }
/*    */   
/*    */   public void subscribeActual(Subscriber<? super T> s) {
/*    */     V v;
/* 37 */     DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription(s);
/* 38 */     s.onSubscribe((Subscription)deferred);
/*    */ 
/*    */     
/*    */     try {
/* 42 */       v = (this.unit != null) ? (V)this.future.get(this.timeout, this.unit) : (V)this.future.get();
/* 43 */     } catch (Throwable ex) {
/* 44 */       Exceptions.throwIfFatal(ex);
/* 45 */       if (!deferred.isCancelled()) {
/* 46 */         s.onError(ex);
/*    */       }
/*    */       return;
/*    */     } 
/* 50 */     if (v == null) {
/* 51 */       s.onError(new NullPointerException("The future returned null"));
/*    */     } else {
/* 53 */       deferred.complete(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFromFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */