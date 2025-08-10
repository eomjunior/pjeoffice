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
/*    */ class StackProgressFactory
/*    */   extends ProgressFactory<StackProgressView>
/*    */ {
/*    */   public StackProgressFactory() {
/* 33 */     super(() -> new StackProgressView());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/StackProgressFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */