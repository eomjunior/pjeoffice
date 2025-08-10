/*    */ package com.github.progress4j.imp;
/*    */ 
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
/*    */ class ProgressPanelView
/*    */   extends ProgressHandlerView<ProgressBox>
/*    */ {
/*    */   public ProgressPanelView() {
/* 35 */     this(new ProgressBox());
/*    */   }
/*    */   
/*    */   protected ProgressPanelView(ProgressBox panel) {
/* 39 */     super(panel);
/*    */   }
/*    */ 
/*    */   
/*    */   public void display() {
/* 44 */     SwingTools.invokeLater(() -> asContainer().setVisible(true));
/*    */   }
/*    */ 
/*    */   
/*    */   public void undisplay() {
/* 49 */     SwingTools.invokeLater(() -> asContainer().setVisible(false));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressPanelView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */