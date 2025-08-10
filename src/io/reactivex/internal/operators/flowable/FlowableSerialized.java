/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.subscribers.SerializedSubscriber;
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
/*    */ public final class FlowableSerialized<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   public FlowableSerialized(Flowable<T> source) {
/* 22 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 27 */     this.source.subscribe((FlowableSubscriber)new SerializedSubscriber(s));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSerialized.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */