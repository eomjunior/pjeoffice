/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.functions.BiFunction;
/*    */ import java.util.List;
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
/*    */ public enum ListAddBiConsumer
/*    */   implements BiFunction<List, Object, List>
/*    */ {
/* 22 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public static <T> BiFunction<List<T>, T, List<T>> instance() {
/* 26 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List apply(List<Object> t1, Object t2) throws Exception {
/* 32 */     t1.add(t2);
/* 33 */     return t1;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/ListAddBiConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */