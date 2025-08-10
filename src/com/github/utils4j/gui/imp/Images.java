/*    */ package com.github.utils4j.gui.imp;
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
/* 33 */   FIRST("/file-first.png"),
/*    */   
/* 35 */   UP("/file-up.png"),
/*    */   
/* 37 */   DOWN("/file-down.png"),
/*    */   
/* 39 */   LAST("/file-last.png"),
/*    */   
/* 41 */   ADD("/file-add.png"),
/*    */   
/* 43 */   REM("/file-rem.png"),
/*    */   
/* 45 */   ECHO("/echo.png"),
/*    */   
/* 47 */   SHIELD("/shield.png");
/*    */   
/*    */   final String path;
/*    */   
/*    */   Images(String path) {
/* 52 */     this.path = path;
/*    */   }
/*    */ 
/*    */   
/*    */   public String path() {
/* 57 */     return this.path;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/Images.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */