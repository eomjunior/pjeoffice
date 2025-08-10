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
/*    */ 
/*    */ class ProgressFrameView
/*    */   extends ProgressHandlerView<ProgressFrame>
/*    */ {
/*    */   public ProgressFrameView() {
/* 35 */     this(Images.LOG.asImage());
/*    */   }
/*    */   
/*    */   public ProgressFrameView(Image icon) {
/* 39 */     this(new ProgressFrame(icon));
/*    */   }
/*    */   
/*    */   protected ProgressFrameView(ProgressFrame frame) {
/* 43 */     super(frame);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void display() {
/* 48 */     asContainer().reveal();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void undisplay() {
/* 53 */     asContainer().unreveal();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDispose() {
/* 58 */     asContainer().exit();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressFrameView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */