/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*    */ import java.util.concurrent.Callable;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.reactivestreams.Subscriber;
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
/*    */ public final class FlowableDefer<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final Callable<? extends Publisher<? extends T>> supplier;
/*    */   
/*    */   public FlowableDefer(Callable<? extends Publisher<? extends T>> supplier) {
/* 28 */     this.supplier = supplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super T> s) {
/*    */     Publisher<? extends T> pub;
/*    */     try {
/* 35 */       pub = (Publisher<? extends T>)ObjectHelper.requireNonNull(this.supplier.call(), "The publisher supplied is null");
/* 36 */     } catch (Throwable t) {
/* 37 */       Exceptions.throwIfFatal(t);
/* 38 */       EmptySubscription.error(t, s);
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     pub.subscribe(s);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */