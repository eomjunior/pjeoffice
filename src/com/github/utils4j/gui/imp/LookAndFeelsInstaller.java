/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Throwables;
/*    */ import java.awt.Component;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.SwingUtilities;
/*    */ import javax.swing.UIManager;
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
/*    */ public final class LookAndFeelsInstaller
/*    */ {
/*    */   public static final String UNDEFINED = "undefined";
/*    */   
/*    */   public static void install(String looksAndFeelsName) throws Exception {
/* 58 */     install(looksAndFeelsName, null);
/*    */   }
/*    */   
/*    */   public static void install(String looksAndFeelsName, Component root) throws Exception {
/* 62 */     Args.requireNonNull(looksAndFeelsName, "looksAndFeels is null");
/*    */     
/* 64 */     LookAndFeel laf = UIManager.getLookAndFeel();
/*    */     
/* 66 */     String currentLookAndFeels = (laf == null) ? "undefined" : laf.getName();
/*    */     
/* 68 */     if (currentLookAndFeels.equals(looksAndFeelsName)) {
/*    */       return;
/*    */     }
/* 71 */     boolean installed = false;
/* 72 */     for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/* 73 */       if (info.getName().equals(looksAndFeelsName)) {
/* 74 */         UIManager.setLookAndFeel(info.getClassName());
/* 75 */         installed = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 79 */     if (!installed) {
/* 80 */       Throwables.runtime(() -> UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
/*    */     }
/*    */     
/* 83 */     if (root != null)
/* 84 */       SwingUtilities.updateComponentTreeUI(root); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/LookAndFeelsInstaller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */