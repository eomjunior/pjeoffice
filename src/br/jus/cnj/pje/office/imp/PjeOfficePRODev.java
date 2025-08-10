/*    */ package br.jus.cnj.pje.office.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.IPjeFrontEnd;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import java.awt.CheckboxMenuItem;
/*    */ import java.awt.Menu;
/*    */ import java.awt.PopupMenu;
/*    */ import java.awt.event.ItemEvent;
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
/*    */ public class PjeOfficePRODev
/*    */   extends PjeOfficePRO
/*    */ {
/*    */   private static PjeOfficeClassic createInstance(IPjeFrontEnd front) {
/* 43 */     return new PjeOfficePRODev(front);
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 47 */     SwingTools.invokeLater(() -> createInstance(PjeOfficeFrontEnd.getBest()).start());
/*    */   }
/*    */   
/*    */   private PjeOfficePRODev(IPjeFrontEnd frontEnd) {
/* 51 */     super(frontEnd, "d");
/*    */   }
/*    */ 
/*    */   
/*    */   protected PjeOfficeClassic newInstance(IPjeFrontEnd front) {
/* 56 */     return createInstance(front);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preInstall() {
/* 61 */     this.office.setUnsafe(this.office.isUnsafe());
/* 62 */     this.office.setDevMode(this.office.isDevMode());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Menu doOptions(PopupMenu popup, Menu mnuOption) {
/* 67 */     switchMode(mnuOption);
/* 68 */     if (PjeOfficeFrontEnd.supportsSystray()) {
/* 69 */       mnuOption.addSeparator();
/*    */     }
/*    */     
/* 72 */     CheckboxMenuItem mnuSafeMode = new CheckboxMenuItem("Modo inseguro (evite usar)");
/* 73 */     mnuSafeMode.addItemListener(e -> this.office.setUnsafe((e.getStateChange() == 1)));
/* 74 */     mnuSafeMode.setState(this.office.isUnsafe());
/*    */     
/* 76 */     CheckboxMenuItem mnuDev = new CheckboxMenuItem("Modo desenvolvimento (evite usar)");
/* 77 */     mnuDev.addItemListener(e -> this.office.setDevMode((e.getStateChange() == 1)));
/* 78 */     mnuDev.setState(this.office.isDevMode());
/*    */     
/* 80 */     mnuOption.add(mnuSafeMode);
/* 81 */     mnuOption.add(mnuDev);
/* 82 */     return mnuOption;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/imp/PjeOfficePRODev.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */