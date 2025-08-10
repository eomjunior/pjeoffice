/*    */ package com.github.signer4j.gui.alert;
/*    */ 
/*    */ import com.github.signer4j.gui.utils.Images;
/*    */ import com.github.signer4j.imp.Config;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
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
/*    */ public final class NoTokenPresentAlert
/*    */   extends NoTokenPresentMessage
/*    */ {
/* 40 */   private static final String[] OPTIONS = new String[] { "SIM", "NÃO" };
/*    */   
/*    */   public static boolean isYes() {
/* 43 */     return SwingTools.isTrue(NoTokenPresentAlert::display);
/*    */   }
/*    */   private final JOptionPane jop;
/*    */   public static boolean isNo() {
/* 47 */     return !isYes();
/*    */   }
/*    */   
/*    */   private static boolean display() {
/* 51 */     return (new NoTokenPresentAlert()).show();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private NoTokenPresentAlert() {
/* 57 */     this
/*    */ 
/*    */ 
/*    */       
/* 61 */       .jop = new JOptionPane(MESSAGE_FORMAT, 3, 0, Images.CERTIFICATE.asIcon(), (Object[])OPTIONS, OPTIONS[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean show() {
/* 68 */     JDialog dialog = this.jop.createDialog("Certificado não encontrado");
/* 69 */     dialog.setAlwaysOnTop(true);
/* 70 */     dialog.setModal(true);
/* 71 */     dialog.setIconImage(Config.getIcon());
/* 72 */     dialog.setVisible(true);
/* 73 */     dialog.dispose();
/* 74 */     Object selectedValue = this.jop.getValue();
/* 75 */     return OPTIONS[0].equals(selectedValue);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/NoTokenPresentAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */