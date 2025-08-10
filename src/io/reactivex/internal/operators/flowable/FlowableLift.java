/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableOperator;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableLift<R, T>
/*    */   extends AbstractFlowableWithUpstream<T, R>
/*    */ {
/*    */   final FlowableOperator<? extends R, ? super T> operator;
/*    */   
/*    */   public FlowableLift(Flowable<T> source, FlowableOperator<? extends R, ? super T> operator) {
/* 36 */     super(source);
/* 37 */     this.operator = operator;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super R> s) {
/*    */     try {
/* 43 */       Subscriber<? super T> st = this.operator.apply(s);
/*    */       
/* 45 */       if (st == null) {
/* 46 */         throw new NullPointerException("Operator " + this.operator + " returned a null Subscriber");
/*    */       }
/*    */       
/* 49 */       this.source.subscribe(st);
/* 50 */     } catch (NullPointerException e) {
/* 51 */       throw e;
/* 52 */     } catch (Throwable e) {
/* 53 */       Exceptions.throwIfFatal(e);
/*    */ 
/*    */       
/* 56 */       RxJavaPlugins.onError(e);
/*    */       
/* 58 */       NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
/* 59 */       npe.initCause(e);
/* 60 */       throw npe;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableLift.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */