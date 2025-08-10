/*    */ package br.jus.cnj.pje.office.gui.echo;
/*    */ 
/*    */ import br.jus.cnj.pje.office.gui.PjeImages;
/*    */ import com.github.utils4j.echo.imp.IconEchoPanel;
/*    */ import javax.swing.ImageIcon;
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
/*    */ public class Signer2BackendPanel
/*    */   extends IconEchoPanel
/*    */ {
/*    */   protected static final String DEFAULT_SEND_HEADER_FORMAT = "==========================================================================\n Enviada requisição %s: \n==========================================================================\n";
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Signer2BackendPanel() {
/* 46 */     super("==========================================================================\n Enviada requisição %s: \n==========================================================================\n");
/*    */   }
/*    */ 
/*    */   
/*    */   protected ImageIcon getIcon() {
/* 51 */     return PjeImages.SIGN2DB.asIcon();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/echo/Signer2BackendPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */