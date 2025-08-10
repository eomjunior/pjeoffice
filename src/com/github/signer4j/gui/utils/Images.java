/*    */ package com.github.signer4j.gui.utils;
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
/*    */ 
/*    */ public enum Images
/*    */   implements IPicture
/*    */ {
/* 34 */   ICON_A1("/a1.png"),
/*    */   
/* 36 */   ICON_A3("/a3.png"),
/*    */   
/* 38 */   LOCK("/lock.png"),
/*    */   
/* 40 */   KEY("/key.png"),
/*    */   
/* 42 */   REFRESH("/update.png"),
/*    */   
/* 44 */   GEAR("/gear.png"),
/*    */   
/* 46 */   SMALL_LOCK("/small-lock.png"),
/*    */   
/* 48 */   MSCAPITIP("/mscapi.png"),
/*    */   
/* 50 */   WINDOWS("/windows.png"),
/*    */   
/* 52 */   NATIVE_INTEGRATION("/native-integration.gif"),
/*    */   
/* 54 */   CERTIFICATE("/certificate.png");
/*    */   
/*    */   final String path;
/*    */ 
/*    */   
/*    */   Images(String path) {
/* 60 */     this.path = path;
/*    */   }
/*    */ 
/*    */   
/*    */   public String path() {
/* 65 */     return this.path;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/utils/Images.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */