/*    */ package com.github.utils4j.imp;
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
/*    */ public final class Booleans
/*    */ {
/*    */   public static boolean isTrue(Boolean value) {
/* 36 */     return (value != null && value.booleanValue());
/*    */   }
/*    */   
/*    */   public static boolean isTrue(Boolean value, boolean nullIsTrue) {
/* 40 */     return nullIsTrue ? ((value == null || value.booleanValue())) : ((value != null && value.booleanValue()));
/*    */   }
/*    */   
/*    */   public static boolean isFalse(Boolean value) {
/* 44 */     return (value != null && !value.booleanValue());
/*    */   }
/*    */   
/*    */   public static boolean isTrue(Object value) {
/* 48 */     return (value != null && Boolean.valueOf(value.toString()).booleanValue());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Booleans.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */