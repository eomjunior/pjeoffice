/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamPublisher;
/*    */ import org.reactivestreams.Publisher;
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
/*    */ 
/*    */ 
/*    */ abstract class AbstractFlowableWithUpstream<T, R>
/*    */   extends Flowable<R>
/*    */   implements HasUpstreamPublisher<T>
/*    */ {
/*    */   protected final Flowable<T> source;
/*    */   
/*    */   AbstractFlowableWithUpstream(Flowable<T> source) {
/* 42 */     this.source = (Flowable<T>)ObjectHelper.requireNonNull(source, "source is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final Publisher<T> source() {
/* 47 */     return (Publisher<T>)this.source;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/AbstractFlowableWithUpstream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */