/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.functions.Function;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.Callable;
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
/*    */ public enum ArrayListSupplier
/*    */   implements Callable<List<Object>>, Function<Object, List<Object>>
/*    */ {
/* 22 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public static <T> Callable<List<T>> asCallable() {
/* 26 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T, O> Function<O, List<T>> asFunction() {
/* 31 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Object> call() throws Exception {
/* 36 */     return new ArrayList();
/*    */   }
/*    */   
/*    */   public List<Object> apply(Object o) throws Exception {
/* 40 */     return new ArrayList();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/ArrayListSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */