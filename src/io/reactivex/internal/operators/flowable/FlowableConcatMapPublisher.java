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
/*    */ public final class FlowableConcatMapPublisher<T, R>
/*    */   extends Flowable<R>
/*    */ {
/*    */   final Publisher<T> source;
/*    */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*    */   final int prefetch;
/*    */   final ErrorMode errorMode;
/*    */   
/*    */   public FlowableConcatMapPublisher(Publisher<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/* 34 */     this.source = source;
/* 35 */     this.mapper = mapper;
/* 36 */     this.prefetch = prefetch;
/* 37 */     this.errorMode = errorMode;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super R> s) {
/* 43 */     if (FlowableScalarXMap.tryScalarXMapSubscribe(this.source, s, this.mapper)) {
/*    */       return;
/*    */     }
/*    */     
/* 47 */     this.source.subscribe(FlowableConcatMap.subscribe(s, this.mapper, this.prefetch, this.errorMode));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */