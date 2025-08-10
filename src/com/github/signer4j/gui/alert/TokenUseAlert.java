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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TokenUseAlert
/*    */ {
/*    */   private static final String MESSAGE_FORMAT = "Há uma solicitação de uso do seu certificado digital!";
/* 43 */   private static final String[] OPTIONS = new String[] { "Ok! Reconheço e autorizo o uso", "Não reconheço esta solicitação" };
/*    */   
/*    */   public static boolean isOk() {
/* 46 */     return SwingTools.isTrue(TokenUseAlert::display);
/*    */   }
/*    */   private final JOptionPane jop;
/*    */   public static boolean isNotOk() {
/* 50 */     return !isOk();
/*    */   }
/*    */   
/*    */   private static boolean display() {
/* 54 */     return (new TokenUseAlert()).show();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private TokenUseAlert() {
/* 60 */     this
/*    */ 
/*    */ 
/*    */       
/* 64 */       .jop = new JOptionPane("Há uma solicitação de uso do seu certificado digital!", 3, 0, Images.CERTIFICATE.asIcon(), (Object[])OPTIONS, OPTIONS[1]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean show() {
/* 71 */     JDialog dialog = this.jop.createDialog("Uso do certificado");
/* 72 */     dialog.setAlwaysOnTop(true);
/* 73 */     dialog.setModal(true);
/* 74 */     dialog.setIconImage(Config.getIcon());
/* 75 */     dialog.setVisible(true);
/* 76 */     dialog.dispose();
/* 77 */     Object selectedValue = this.jop.getValue();
/* 78 */     return OPTIONS[0].equals(selectedValue);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/TokenUseAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */