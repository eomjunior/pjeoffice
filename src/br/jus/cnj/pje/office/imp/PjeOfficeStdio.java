/*    */ package br.jus.cnj.pje.office.imp;
/*    */ 
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
/*    */ public class PjeOfficeStdio
/*    */   extends PjeOfficeApp
/*    */ {
/*    */   private static void main(String[] args) {
/* 37 */     SwingTools.invokeLater(() -> (new PjeOfficeStdio()).start());
/*    */   }
/*    */   
/*    */   private PjeOfficeStdio() {
/* 41 */     super(PjeLifeCycleFactory.STDIO, "s");
/* 42 */     this.office.setDevMode(true);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficeStdio.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */