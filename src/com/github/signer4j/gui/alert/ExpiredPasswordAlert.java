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
/*    */ public final class ExpiredPasswordAlert
/*    */ {
/*    */   private static final String MESSAGE_FORMAT = "A senha do seu dispositivo expirou e deve ser renovada!\n\nO PjeOffice Pro não renova a sua senha! A renovação deve ser feita utilizando o software\nfornecido pelo fabricante do seu dispostivo.";
/* 45 */   private static final String[] OPTIONS = new String[] { "ENTENDI" };
/*    */   
/*    */   public static boolean display() {
/* 48 */     return (new ExpiredPasswordAlert()).reveal();
/*    */   }
/*    */   private final JOptionPane jop;
/*    */   public static void show() {
/* 52 */     SwingTools.invokeLater(ExpiredPasswordAlert::display);
/*    */   }
/*    */   
/*    */   public static void showAndWait() {
/* 56 */     SwingTools.invokeAndWait(ExpiredPasswordAlert::display);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ExpiredPasswordAlert() {
/* 62 */     this
/*    */ 
/*    */ 
/*    */       
/* 66 */       .jop = new JOptionPane("A senha do seu dispositivo expirou e deve ser renovada!\n\nO PjeOffice Pro não renova a sua senha! A renovação deve ser feita utilizando o software\nfornecido pelo fabricante do seu dispostivo.", 1, 0, Images.LOCK.asIcon(), (Object[])OPTIONS, OPTIONS[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean reveal() {
/* 73 */     JDialog dialog = this.jop.createDialog("Senha expirada");
/* 74 */     dialog.setAlwaysOnTop(true);
/* 75 */     dialog.setModal(true);
/* 76 */     dialog.setIconImage(Config.getIcon());
/* 77 */     dialog.setVisible(true);
/* 78 */     dialog.dispose();
/* 79 */     Object selectedValue = this.jop.getValue();
/* 80 */     return OPTIONS[0].equals(selectedValue);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/ExpiredPasswordAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */