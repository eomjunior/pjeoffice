/*    */ package br.jus.cnj.pje.office.gui.alert;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeAccessTime;
/*    */ import br.jus.cnj.pje.office.gui.PjeImages;
/*    */ import com.github.signer4j.imp.Config;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public final class PjeServerPermissionOptions
/*    */ {
/*    */   private static final String MESSAGE_FORMAT = "A aplicação '%s' deseja acessar o PJeOffice.\n\nEndereço: %s\n\nDeseja autorizar?\n\n";
/* 47 */   private static final PjeAccessTime[] OPTIONS = PjeAccessTime.values();
/*    */   
/*    */   public static PjeAccessTime choose(IPjeServerAccess access) {
/* 50 */     Args.requireNonNull(access, "access is null");
/* 51 */     return (PjeAccessTime)SwingTools.invokeAndWaitValue(() -> (new PjeServerPermissionOptions(access)).show(), PjeAccessTime.NOT);
/*    */   }
/*    */   
/*    */   private final JOptionPane jop;
/*    */   
/*    */   private PjeServerPermissionOptions(IPjeServerAccess access) {
/* 57 */     this
/*    */ 
/*    */ 
/*    */       
/* 61 */       .jop = new JOptionPane(String.format("A aplicação '%s' deseja acessar o PJeOffice.\n\nEndereço: %s\n\nDeseja autorizar?\n\n", new Object[] { access.getApp(), access.getServer() }), 3, 1, PjeImages.PJE_SERVER.asIcon(), (Object[])OPTIONS, OPTIONS[2]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private PjeAccessTime show() {
/* 68 */     JDialog dialog = this.jop.createDialog("Autorização de site");
/* 69 */     dialog.setAlwaysOnTop(true);
/* 70 */     dialog.setModal(true);
/* 71 */     dialog.setIconImage(Config.getIcon());
/* 72 */     dialog.setVisible(true);
/* 73 */     dialog.dispose();
/* 74 */     Object selectedValue = this.jop.getValue();
/* 75 */     int i = 0, length = OPTIONS.length;
/* 76 */     for (; i < length; i++) {
/* 77 */       PjeAccessTime at = OPTIONS[i];
/* 78 */       if (at.equals(selectedValue)) {
/* 79 */         return at;
/*    */       }
/*    */     } 
/* 82 */     return PjeAccessTime.NOT;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/alert/PjeServerPermissionOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */