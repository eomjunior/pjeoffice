/*    */ package com.github.signer4j.gui.alert;
/*    */ 
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.awt.Image;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JOptionPane;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoTokenPresentInfo
/*    */   extends NoTokenPresentMessage
/*    */ {
/* 16 */   public static AtomicBoolean VISIBLE = new AtomicBoolean(false);
/*    */   
/* 18 */   private static final String[] OPTIONS_OK = new String[] { "ENTENDI" };
/*    */   
/*    */   public static void showInfoOnly() {
/* 21 */     SwingTools.invokeLater(() -> displayOnly(MESSAGE_MAIN));
/*    */   }
/*    */   private String[] options;
/*    */   public static void showInfoOnlyAndWait() {
/* 25 */     SwingTools.invokeAndWait(() -> Boolean.valueOf(displayOnly(MESSAGE_MAIN)));
/*    */   }
/*    */   
/*    */   private static boolean displayOnly(String message) {
/* 29 */     if (!VISIBLE.getAndSet(true)) {
/* 30 */       return (new NoTokenPresentInfo(message, OPTIONS_OK, 1)).show((Image)null);
/*    */     }
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private NoTokenPresentInfo(String message) {
/* 38 */     this(message, 0);
/*    */   }
/*    */   
/*    */   private NoTokenPresentInfo(String message, int optionPane) {
/* 42 */     this(message, OPTIONS_OK, optionPane);
/*    */   }
/*    */   
/*    */   private NoTokenPresentInfo(String message, String[] options, int optionPane) {
/* 46 */     this
/* 47 */       .jop = new JOptionPane(Strings.trim(message), optionPane, 0, null, (Object[])(this.options = options), options[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean show(Image icon) {
/* 57 */     JDialog dialog = this.jop.createDialog("Atenção!");
/* 58 */     dialog.setAlwaysOnTop(true);
/* 59 */     dialog.setModal(true);
/* 60 */     dialog.setIconImage(icon);
/* 61 */     dialog.setVisible(true);
/* 62 */     dialog.dispose();
/* 63 */     VISIBLE.set(false);
/* 64 */     Object selectedValue = this.jop.getValue();
/* 65 */     return this.options[0].equals(selectedValue);
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 69 */     showInfoOnly();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/NoTokenPresentInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */