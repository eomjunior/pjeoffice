/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.utils4j.gui.IPicture;
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
/*    */ public enum Images
/*    */   implements IPicture
/*    */ {
/* 33 */   PROGRESS_ICON("/progress.png"),
/*    */   
/* 35 */   CANCEL("/cancel.png"),
/*    */   
/* 37 */   LOG("/log.png");
/*    */   
/*    */   final String path;
/*    */   
/*    */   Images(String path) {
/* 42 */     this.path = path;
/*    */   }
/*    */ 
/*    */   
/*    */   public String path() {
/* 47 */     return this.path;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/Images.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */