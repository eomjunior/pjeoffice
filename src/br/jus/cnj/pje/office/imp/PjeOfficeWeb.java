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
/*    */ public class PjeOfficeWeb
/*    */   extends PjeOfficeClassic
/*    */ {
/*    */   private static PjeOfficeClassic createInstance(IPjeFrontEnd front) {
/* 39 */     return new PjeOfficeWeb(front);
/*    */   }
/*    */   
/*    */   private static void main(String[] args) {
/* 43 */     SwingTools.invokeLater(() -> createInstance(PjeOfficeFrontEnd.getBest()).start());
/*    */   }
/*    */   
/*    */   private PjeOfficeWeb(IPjeFrontEnd frontEnd) {
/* 47 */     super(frontEnd, PjeLifeCycleFactory.WEB, "s");
/*    */   }
/*    */ 
/*    */   
/*    */   protected PjeOfficeClassic newInstance(IPjeFrontEnd front) {
/* 52 */     return createInstance(front);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficeWeb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */