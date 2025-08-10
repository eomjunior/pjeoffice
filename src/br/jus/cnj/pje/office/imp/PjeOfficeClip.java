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
/*    */ public class PjeOfficeClip
/*    */   extends PjeOfficeApp
/*    */ {
/*    */   private static void main(String[] args) {
/* 37 */     SwingTools.invokeLater(() -> (new PjeOfficeClip()).start());
/*    */   }
/*    */   
/*    */   private PjeOfficeClip() {
/* 41 */     super(PjeLifeCycleFactory.CLIP, "u");
/* 42 */     this.office.setDevMode(true);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficeClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */