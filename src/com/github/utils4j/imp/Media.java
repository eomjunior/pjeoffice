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
/*    */ public enum Media
/*    */ {
/* 30 */   PDF,
/* 31 */   MP4,
/* 32 */   ANY;
/*    */   
/*    */   public String getExtension() {
/* 35 */     return name().toLowerCase();
/*    */   }
/*    */   
/*    */   public String getExtension(boolean dot) {
/* 39 */     return dot ? ("." + getExtension()) : getExtension();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Media.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */