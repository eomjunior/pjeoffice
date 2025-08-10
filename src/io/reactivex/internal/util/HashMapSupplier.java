/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public enum HashMapSupplier
/*    */   implements Callable<Map<Object, Object>>
/*    */ {
/* 21 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public static <K, V> Callable<Map<K, V>> asCallable() {
/* 25 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public Map<Object, Object> call() throws Exception {
/* 29 */     return new HashMap<Object, Object>();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/HashMapSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */