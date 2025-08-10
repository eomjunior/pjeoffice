/*    */ package org.apache.hc.core5.util;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
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
/*    */ public final class LangUtils
/*    */ {
/*    */   public static final int HASH_SEED = 17;
/*    */   public static final int HASH_OFFSET = 37;
/*    */   
/*    */   public static int hashCode(int seed, int hashcode) {
/* 50 */     return seed * 37 + hashcode;
/*    */   }
/*    */   
/*    */   public static int hashCode(int seed, boolean b) {
/* 54 */     return hashCode(seed, b ? 1 : 0);
/*    */   }
/*    */   
/*    */   public static int hashCode(int seed, Object obj) {
/* 58 */     return hashCode(seed, (obj != null) ? obj.hashCode() : 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static boolean equals(Object obj1, Object obj2) {
/* 71 */     return Objects.equals(obj1, obj2);
/*    */   }
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
/*    */   @Deprecated
/*    */   public static boolean equals(Object[] a1, Object[] a2) {
/* 91 */     return Arrays.equals(a1, a2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/LangUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */