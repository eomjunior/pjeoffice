/*    */ package com.github.utils4j;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ICanceller {
/*    */   public static final ICanceller NOTHING = r -> {
/*    */     
/*    */     };
/*    */   
/*    */   void cancelCode(Runnable paramRunnable) throws InterruptedException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/ICanceller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */