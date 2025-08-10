/*    */ package com.yworks.yguard.obf;
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
/*    */ public class TreeAction
/*    */ {
/*    */   public void packageAction(Pk pk) {
/* 22 */     defaultAction(pk);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void classAction(Cl cl) {
/* 29 */     defaultAction(cl);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void methodAction(Md md) {
/* 36 */     defaultAction(md);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void fieldAction(Fd fd) {
/* 43 */     defaultAction(fd);
/*    */   }
/*    */   
/*    */   public void defaultAction(TreeItem ti) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/TreeAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */