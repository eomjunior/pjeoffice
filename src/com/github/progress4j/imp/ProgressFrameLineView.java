/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import java.awt.Image;
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
/*    */ class ProgressFrameLineView
/*    */   extends ProgressFrameView
/*    */ {
/*    */   public ProgressFrameLineView() {
/* 34 */     this(Images.LOG.asImage(), false);
/*    */   }
/*    */   
/*    */   public ProgressFrameLineView(boolean simple) {
/* 38 */     this(Images.LOG.asImage(), simple);
/*    */   }
/*    */   
/*    */   public ProgressFrameLineView(Image icon, boolean simple) {
/* 42 */     super(new ProgressFrame(icon, new ProgressLine(!simple)));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressFrameLineView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */