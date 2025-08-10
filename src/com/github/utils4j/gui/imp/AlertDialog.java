/*    */ package com.github.utils4j.gui.imp;
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
/*    */ public final class AlertDialog
/*    */ {
/*    */   public static void info(String message) {
/* 37 */     SwingTools.invokeLater(() -> Dialogs.info(message));
/*    */   }
/*    */   
/*    */   public static void warning(String message) {
/* 41 */     SwingTools.invokeLater(() -> Dialogs.warning(message));
/*    */   }
/*    */   
/*    */   public static void error(String message) {
/* 45 */     SwingTools.invokeLater(() -> Dialogs.error(message));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/AlertDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */