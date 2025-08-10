/*    */ package com.github.progress4j.imp;
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
/*    */ public class ProgressFrameLineFactory
/*    */   extends ProgressFactory<ProgressFrameLineView>
/*    */ {
/*    */   public ProgressFrameLineFactory() {
/* 33 */     this(false);
/*    */   }
/*    */   
/*    */   public ProgressFrameLineFactory(boolean simple) {
/* 37 */     super(() -> new ProgressFrameLineView(simple));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressFrameLineFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */