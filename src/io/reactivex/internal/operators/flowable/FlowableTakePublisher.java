/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableTakePublisher<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final Publisher<T> source;
/*    */   final long limit;
/*    */   
/*    */   public FlowableTakePublisher(Publisher<T> source, long limit) {
/* 32 */     this.source = source;
/* 33 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 38 */     this.source.subscribe((Subscriber)new FlowableTake.TakeSubscriber<T>(s, this.limit));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTakePublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */