/*    */ package com.yworks.yguard.obf;
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
/*    */ public class NoSuchMappingException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   private String key;
/*    */   
/*    */   public NoSuchMappingException(String key) {
/* 24 */     super("No mapping found for: " + key);
/* 25 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoSuchMappingException() {
/* 33 */     super("No mapping found!");
/* 34 */     this.key = "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 43 */     return this.key;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/NoSuchMappingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */