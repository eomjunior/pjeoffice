/*    */ package io.reactivex.internal.operators.parallel;
/*    */ 
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
/*    */ public final class ParallelFromArray<T>
/*    */   extends ParallelFlowable<T>
/*    */ {
/*    */   final Publisher<T>[] sources;
/*    */   
/*    */   public ParallelFromArray(Publisher<T>[] sources) {
/* 30 */     this.sources = sources;
/*    */   }
/*    */ 
/*    */   
/*    */   public int parallelism() {
/* 35 */     return this.sources.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribe(Subscriber<? super T>[] subscribers) {
/* 40 */     if (!validate((Subscriber[])subscribers)) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     int n = subscribers.length;
/*    */     
/* 46 */     for (int i = 0; i < n; i++)
/* 47 */       this.sources[i].subscribe(subscribers[i]); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelFromArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */