/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.functions.Function;
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
/*    */ 
/*    */ 
/*    */ public final class FlowableMapPublisher<T, U>
/*    */   extends Flowable<U>
/*    */ {
/*    */   final Publisher<T> source;
/*    */   final Function<? super T, ? extends U> mapper;
/*    */   
/*    */   public FlowableMapPublisher(Publisher<T> source, Function<? super T, ? extends U> mapper) {
/* 35 */     this.source = source;
/* 36 */     this.mapper = mapper;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super U> s) {
/* 41 */     this.source.subscribe((Subscriber)new FlowableMap.MapSubscriber<T, U>(s, this.mapper));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */