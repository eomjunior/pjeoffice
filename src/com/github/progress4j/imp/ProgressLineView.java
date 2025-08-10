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
/*    */ class ProgressLineView
/*    */   extends ProgressHandlerView<ProgressLine>
/*    */ {
/*    */   public ProgressLineView() {
/* 35 */     this(new ProgressLine(false));
/*    */   }
/*    */   
/*    */   public ProgressLineView(boolean showCancel) {
/* 39 */     this(new ProgressLine(showCancel));
/*    */   }
/*    */   
/*    */   protected ProgressLineView(ProgressLine line) {
/* 43 */     super(line);
/*    */   }
/*    */ 
/*    */   
/*    */   public void display() {
/* 48 */     SwingTools.invokeLater(() -> asContainer().setVisible(true));
/*    */   }
/*    */ 
/*    */   
/*    */   public void undisplay() {
/* 53 */     SwingTools.invokeLater(() -> asContainer().setVisible(false));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressLineView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */