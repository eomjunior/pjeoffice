/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.functions.Function;
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
/*    */ public enum MaybeToPublisher
/*    */   implements Function<MaybeSource<Object>, Publisher<Object>>
/*    */ {
/* 25 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public static <T> Function<MaybeSource<T>, Publisher<T>> instance() {
/* 29 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Publisher<Object> apply(MaybeSource<Object> t) throws Exception {
/* 34 */     return (Publisher<Object>)new MaybeToFlowable(t);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeToPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */