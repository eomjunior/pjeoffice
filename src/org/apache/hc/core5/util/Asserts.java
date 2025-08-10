/*    */ package org.apache.hc.core5.util;
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
/*    */ public class Asserts
/*    */ {
/*    */   public static void check(boolean expression, String message) {
/* 37 */     if (!expression) {
/* 38 */       throw new IllegalStateException(message);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void check(boolean expression, String message, Object... args) {
/* 43 */     if (!expression) {
/* 44 */       throw new IllegalStateException(String.format(message, args));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void check(boolean expression, String message, Object arg) {
/* 49 */     if (!expression) {
/* 50 */       throw new IllegalStateException(String.format(message, new Object[] { arg }));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void notNull(Object object, String name) {
/* 55 */     if (object == null) {
/* 56 */       throw new IllegalStateException(name + " is null");
/*    */     }
/*    */   }
/*    */   
/*    */   public static void notEmpty(CharSequence s, String name) {
/* 61 */     if (TextUtils.isEmpty(s)) {
/* 62 */       throw new IllegalStateException(name + " is empty");
/*    */     }
/*    */   }
/*    */   
/*    */   public static void notBlank(CharSequence s, String name) {
/* 67 */     if (TextUtils.isBlank(s))
/* 68 */       throw new IllegalStateException(name + " is blank"); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/Asserts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */