/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.util.ErrorMode;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableConcatMapEagerPublisher<T, R>
/*    */   extends Flowable<R>
/*    */ {
/*    */   final Publisher<T> source;
/*    */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*    */   final int maxConcurrency;
/*    */   final int prefetch;
/*    */   final ErrorMode errorMode;
/*    */   
/*    */   public FlowableConcatMapEagerPublisher(Publisher<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch, ErrorMode errorMode) {
/* 47 */     this.source = source;
/* 48 */     this.mapper = mapper;
/* 49 */     this.maxConcurrency = maxConcurrency;
/* 50 */     this.prefetch = prefetch;
/* 51 */     this.errorMode = errorMode;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super R> s) {
/* 56 */     this.source.subscribe((Subscriber)new FlowableConcatMapEager.ConcatMapEagerDelayErrorSubscriber<T, R>(s, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatMapEagerPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */