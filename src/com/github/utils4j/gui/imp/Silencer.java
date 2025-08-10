/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public abstract class Silencer
/*    */ {
/* 35 */   private static Logger LOGGER = LoggerFactory.getLogger(Silencer.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public static void failAs(boolean silent, String message, Throwable e) {
/* 40 */     failAs(silent, message, e, "");
/*    */   }
/*    */   
/*    */   public static void failAs(boolean silent, String message, Throwable e, String detail) {
/* 44 */     runAs(silent, () -> {
/*    */           LOGGER.warn(message, e);
/*    */           ExceptionAlert.show(message, detail, e);
/*    */         });
/*    */   }
/*    */   
/*    */   public static void infoAs(boolean silent, String message) {
/* 51 */     runAs(silent, () -> {
/*    */           LOGGER.info(message);
/*    */           MessageAlert.showInfo(message);
/*    */         });
/*    */   }
/*    */   
/*    */   public static void runAs(boolean silent, Runnable r) {
/* 58 */     if (!silent && r != null)
/* 59 */       r.run(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/Silencer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */