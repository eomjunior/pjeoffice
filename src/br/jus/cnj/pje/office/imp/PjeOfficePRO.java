/*    */ package br.jus.cnj.pje.office.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.IPjeFrontEnd;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeLifeCycleFactory;
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
/*    */ public class PjeOfficePRO
/*    */   extends PjeOfficeClassic
/*    */ {
/*    */   private static PjeOfficeClassic createInstance(IPjeFrontEnd front) {
/* 39 */     return new PjeOfficePRO(front, "s");
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 43 */     SwingTools.invokeLater(() -> createInstance(PjeOfficeFrontEnd.getBest()).start());
/*    */   }
/*    */   
/*    */   protected PjeOfficePRO(IPjeFrontEnd frontEnd, String model) {
/* 47 */     super(frontEnd, PjeLifeCycleFactory.PRO, model);
/*    */   }
/*    */ 
/*    */   
/*    */   protected PjeOfficeClassic newInstance(IPjeFrontEnd front) {
/* 52 */     return createInstance(front);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preInstall() {
/* 57 */     this.office.setUnsafe(false);
/* 58 */     this.office.setDevMode(false);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficePRO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */