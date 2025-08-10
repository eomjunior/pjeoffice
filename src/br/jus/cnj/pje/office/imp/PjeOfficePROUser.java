/*    */ package br.jus.cnj.pje.office.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.IPjeFrontEnd;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
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
/*    */ public class PjeOfficePROUser
/*    */   extends PjeOfficePRO
/*    */ {
/*    */   private static PjeOfficeClassic createInstance(IPjeFrontEnd front) {
/* 38 */     return new PjeOfficePROUser(front);
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 42 */     SwingTools.invokeLater(() -> createInstance(PjeOfficeFrontEnd.getBest()).start());
/*    */   }
/*    */   
/*    */   private PjeOfficePROUser(IPjeFrontEnd frontEnd) {
/* 46 */     super(frontEnd, "u");
/*    */   }
/*    */ 
/*    */   
/*    */   protected PjeOfficeClassic newInstance(IPjeFrontEnd front) {
/* 51 */     return createInstance(front);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preInstall() {
/* 56 */     this.office.setUnsafe(true);
/* 57 */     this.office.setDevMode(false);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficePROUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */