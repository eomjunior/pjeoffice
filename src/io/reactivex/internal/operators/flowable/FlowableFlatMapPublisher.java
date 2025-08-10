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
/*    */ public final class FlowableFlatMapPublisher<T, U>
/*    */   extends Flowable<U>
/*    */ {
/*    */   final Publisher<T> source;
/*    */   final Function<? super T, ? extends Publisher<? extends U>> mapper;
/*    */   final boolean delayErrors;
/*    */   final int maxConcurrency;
/*    */   final int bufferSize;
/*    */   
/*    */   public FlowableFlatMapPublisher(Publisher<T> source, Function<? super T, ? extends Publisher<? extends U>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/* 31 */     this.source = source;
/* 32 */     this.mapper = mapper;
/* 33 */     this.delayErrors = delayErrors;
/* 34 */     this.maxConcurrency = maxConcurrency;
/* 35 */     this.bufferSize = bufferSize;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super U> s) {
/* 40 */     if (FlowableScalarXMap.tryScalarXMapSubscribe(this.source, s, this.mapper)) {
/*    */       return;
/*    */     }
/* 43 */     this.source.subscribe((Subscriber)FlowableFlatMap.subscribe(s, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */