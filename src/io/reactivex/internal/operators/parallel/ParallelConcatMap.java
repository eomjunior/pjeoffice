/*    */ package io.reactivex.internal.operators.parallel;
/*    */ 
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.operators.flowable.FlowableConcatMap;
/*    */ import io.reactivex.internal.util.ErrorMode;
/*    */ import io.reactivex.parallel.ParallelFlowable;
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
/*    */ public final class ParallelConcatMap<T, R>
/*    */   extends ParallelFlowable<R>
/*    */ {
/*    */   final ParallelFlowable<T> source;
/*    */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*    */   final int prefetch;
/*    */   final ErrorMode errorMode;
/*    */   
/*    */   public ParallelConcatMap(ParallelFlowable<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, ErrorMode errorMode) {
/* 44 */     this.source = source;
/* 45 */     this.mapper = (Function<? super T, ? extends Publisher<? extends R>>)ObjectHelper.requireNonNull(mapper, "mapper");
/* 46 */     this.prefetch = prefetch;
/* 47 */     this.errorMode = (ErrorMode)ObjectHelper.requireNonNull(errorMode, "errorMode");
/*    */   }
/*    */ 
/*    */   
/*    */   public int parallelism() {
/* 52 */     return this.source.parallelism();
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribe(Subscriber<? super R>[] subscribers) {
/* 57 */     if (!validate((Subscriber[])subscribers)) {
/*    */       return;
/*    */     }
/*    */     
/* 61 */     int n = subscribers.length;
/*    */ 
/*    */     
/* 64 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*    */     
/* 66 */     for (int i = 0; i < n; i++) {
/* 67 */       arrayOfSubscriber[i] = FlowableConcatMap.subscribe(subscribers[i], this.mapper, this.prefetch, this.errorMode);
/*    */     }
/*    */     
/* 70 */     this.source.subscribe(arrayOfSubscriber);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelConcatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */