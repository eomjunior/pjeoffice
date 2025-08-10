/*    */ package com.github.signer4j.gui.alert;
/*    */ 
/*    */ import com.github.signer4j.gui.utils.Images;
/*    */ import com.github.signer4j.imp.Config;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JOptionPane;
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
/*    */ public final class DriverHealthAlert
/*    */ {
/* 42 */   private static final AtomicBoolean VISIBLE = new AtomicBoolean(false);
/*    */   
/*    */   public static void showInfo(String message) {
/* 45 */     SwingTools.invokeLater(() -> display(message));
/*    */   }
/*    */   
/*    */   public static boolean display(String message) {
/* 49 */     if (!VISIBLE.getAndSet(true)) {
/* 50 */       return (new DriverHealthAlert(message)).show();
/*    */     }
/* 52 */     return false;
/*    */   }
/*    */   
/* 55 */   private static final String[] OPTIONS = new String[] { "OK" };
/*    */   
/*    */   private final JOptionPane jop;
/*    */   
/*    */   private DriverHealthAlert(String message) {
/* 60 */     this
/*    */ 
/*    */ 
/*    */       
/* 64 */       .jop = new JOptionPane(message, 0, 0, Images.GEAR.asIcon(), (Object[])OPTIONS, OPTIONS[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean show() {
/* 71 */     JDialog dialog = this.jop.createDialog("PJeOffice incapacitado de ler certificados");
/* 72 */     dialog.setAlwaysOnTop(true);
/* 73 */     dialog.setModal(true);
/* 74 */     dialog.setIconImage(Config.getIcon());
/* 75 */     dialog.setVisible(true);
/* 76 */     dialog.dispose();
/* 77 */     VISIBLE.set(false);
/* 78 */     Object selectedValue = this.jop.getValue();
/* 79 */     return OPTIONS[0].equals(selectedValue);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/DriverHealthAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */