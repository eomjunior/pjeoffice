/*    */ package io.reactivex.internal.operators.parallel;
/*    */ 
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.operators.flowable.FlowableFlatMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ParallelFlatMap<T, R>
/*    */   extends ParallelFlowable<R>
/*    */ {
/*    */   final ParallelFlowable<T> source;
/*    */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*    */   final boolean delayError;
/*    */   final int maxConcurrency;
/*    */   final int prefetch;
/*    */   
/*    */   public ParallelFlatMap(ParallelFlowable<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayError, int maxConcurrency, int prefetch) {
/* 46 */     this.source = source;
/* 47 */     this.mapper = mapper;
/* 48 */     this.delayError = delayError;
/* 49 */     this.maxConcurrency = maxConcurrency;
/* 50 */     this.prefetch = prefetch;
/*    */   }
/*    */ 
/*    */   
/*    */   public int parallelism() {
/* 55 */     return this.source.parallelism();
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribe(Subscriber<? super R>[] subscribers) {
/* 60 */     if (!validate((Subscriber[])subscribers)) {
/*    */       return;
/*    */     }
/*    */     
/* 64 */     int n = subscribers.length;
/*    */ 
/*    */     
/* 67 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*    */     
/* 69 */     for (int i = 0; i < n; i++) {
/* 70 */       arrayOfSubscriber[i] = (Subscriber)FlowableFlatMap.subscribe(subscribers[i], this.mapper, this.delayError, this.maxConcurrency, this.prefetch);
/*    */     }
/*    */     
/* 73 */     this.source.subscribe(arrayOfSubscriber);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */