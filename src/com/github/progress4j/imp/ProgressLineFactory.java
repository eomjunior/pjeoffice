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
/*    */ public class ProgressLineFactory
/*    */   extends ProgressFactory<ProgressLineView>
/*    */ {
/*    */   public ProgressLineFactory(boolean showCancel) {
/* 33 */     super(() -> new ProgressLineView(showCancel));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressLineFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */