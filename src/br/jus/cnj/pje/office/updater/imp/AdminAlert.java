/*    */ package br.jus.cnj.pje.office.updater.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.gui.PjeImages;
/*    */ import com.github.utils4j.gui.imp.GifAlert;
/*    */ import com.github.utils4j.gui.imp.Images;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import com.github.utils4j.imp.Jvms;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Color;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JPanel;
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
/*    */ public class AdminAlert
/*    */   extends GifAlert
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static void showMessage(String message) {
/* 48 */     SwingTools.invokeLater(() -> (new AdminAlert(message, PjeImages.PJE_RUNAS_ADMIN.asIcon())).display());
/*    */   }
/*    */   
/*    */   private AdminAlert(String message, ImageIcon animation) {
/* 52 */     super("Execução como administrador", message, Images.SHIELD.asImage(), animation);
/*    */   }
/*    */ 
/*    */   
/*    */   protected JPanel createBody() {
/* 57 */     this.bodyPanel = new JPanel();
/* 58 */     this.bodyPanel.setLayout(new BorderLayout(0, 5));
/* 59 */     if (Jvms.isWindows()) {
/* 60 */       this.bodyPanel.add(createGif(), "Center");
/* 61 */       this.bodyPanel.setBackground(new Color(0, 35, 61));
/*    */     } 
/* 63 */     this.bodyPanel.add(createUnderstand(), "South");
/* 64 */     this.bodyPanel.updateUI();
/* 65 */     return this.bodyPanel;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/imp/AdminAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */