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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TokenLockedAlert
/*    */ {
/*    */   private static final String MESSAGE_FORMAT = "Infelizmente seu dispositivo encontra-se BLOQUEADO.\n\nA única forma de desbloqueá-lo é fazendo uso da sua senha de administração,\ntambém conhecida como PUK.\n\nSe você desconhece sua senha PUK, seu certificado foi perdido para sempre\ne um novo certificado deverá ser providenciado.";
/* 47 */   private static final String[] OPTIONS = new String[] { "ENTENDI" };
/*    */   
/*    */   public static boolean display() {
/* 50 */     return (new TokenLockedAlert()).reveal();
/*    */   }
/*    */   private final JOptionPane jop;
/*    */   public static void show() {
/* 54 */     SwingTools.invokeLater(TokenLockedAlert::display);
/*    */   }
/*    */   
/*    */   public static void showAndWait() {
/* 58 */     SwingTools.invokeAndWait(TokenLockedAlert::display);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private TokenLockedAlert() {
/* 64 */     this
/*    */ 
/*    */ 
/*    */       
/* 68 */       .jop = new JOptionPane("Infelizmente seu dispositivo encontra-se BLOQUEADO.\n\nA única forma de desbloqueá-lo é fazendo uso da sua senha de administração,\ntambém conhecida como PUK.\n\nSe você desconhece sua senha PUK, seu certificado foi perdido para sempre\ne um novo certificado deverá ser providenciado.", 1, 0, Images.LOCK.asIcon(), (Object[])OPTIONS, OPTIONS[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean reveal() {
/* 75 */     JDialog dialog = this.jop.createDialog("Dispositivo bloqueado");
/* 76 */     dialog.setAlwaysOnTop(true);
/* 77 */     dialog.setModal(true);
/* 78 */     dialog.setIconImage(Config.getIcon());
/* 79 */     dialog.setVisible(true);
/* 80 */     dialog.dispose();
/* 81 */     Object selectedValue = this.jop.getValue();
/* 82 */     return OPTIONS[0].equals(selectedValue);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/TokenLockedAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */