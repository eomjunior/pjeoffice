/*    */ package com.sun.jna;
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
/*    */ public class FromNativeContext
/*    */ {
/*    */   private Class<?> type;
/*    */   
/*    */   FromNativeContext(Class<?> javaType) {
/* 30 */     this.type = javaType;
/*    */   }
/*    */   
/*    */   public Class<?> getTargetType() {
/* 34 */     return this.type;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/FromNativeContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */