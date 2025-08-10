/*    */ package io.reactivex.parallel;
/*    */ 
/*    */ import io.reactivex.functions.BiFunction;
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
/*    */ public enum ParallelFailureHandling
/*    */   implements BiFunction<Long, Throwable, ParallelFailureHandling>
/*    */ {
/* 27 */   STOP,
/*    */ 
/*    */ 
/*    */   
/* 31 */   ERROR,
/*    */ 
/*    */ 
/*    */   
/* 35 */   SKIP,
/*    */ 
/*    */ 
/*    */   
/* 39 */   RETRY;
/*    */ 
/*    */   
/*    */   public ParallelFailureHandling apply(Long t1, Throwable t2) {
/* 43 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/parallel/ParallelFailureHandling.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */