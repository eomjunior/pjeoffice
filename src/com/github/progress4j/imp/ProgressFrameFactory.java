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
/*    */ public class ProgressFrameFactory
/*    */   extends ProgressFactory<ProgressFrameView>
/*    */ {
/*    */   public ProgressFrameFactory() {
/* 35 */     super(() -> new ProgressFrameView());
/*    */   }
/*    */   
/*    */   public ProgressFrameFactory(Image icon) {
/* 39 */     super(() -> new ProgressFrameView(icon));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressFrameFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */