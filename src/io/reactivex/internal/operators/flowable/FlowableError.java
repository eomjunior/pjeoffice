/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*    */ import java.util.concurrent.Callable;
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
/*    */ 
/*    */ public final class FlowableError<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final Callable<? extends Throwable> errorSupplier;
/*    */   
/*    */   public FlowableError(Callable<? extends Throwable> errorSupplier) {
/* 28 */     this.errorSupplier = errorSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super T> s) {
/*    */     Throwable error;
/*    */     try {
/* 35 */       error = (Throwable)ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
/* 36 */     } catch (Throwable t) {
/* 37 */       Exceptions.throwIfFatal(t);
/* 38 */       error = t;
/*    */     } 
/* 40 */     EmptySubscription.error(error, s);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */