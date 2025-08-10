/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverLookupStrategy;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class LookupStrategy
/*    */ {
/*    */   public static NotDuplicatedStrategy notDuplicated() {
/* 36 */     return notDuplicated(SystemSupport.getDefault());
/*    */   }
/*    */   
/*    */   public static NotDuplicatedStrategy notDuplicated(SystemSupport support) {
/* 40 */     Args.requireNonNull(support, "Unabled to use null SystemSupport");
/* 41 */     return new NotDuplicatedStrategy(new IDriverLookupStrategy[] { (IDriverLookupStrategy)support.getStrategy() });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/LookupStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */