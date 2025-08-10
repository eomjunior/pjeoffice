/*    */ package com.github.signer4j;
/*    */ 
/*    */ public interface IsAuthStrategy
/*    */ {
/*    */   String getStrategyLabel();
/*    */   
/*    */   default boolean isAwayStrategy() {
/*  8 */     return false;
/*    */   }
/*    */   
/*    */   default boolean isOneTimeStrategy() {
/* 12 */     return false;
/*    */   }
/*    */   
/*    */   default boolean isConfirmStrategy() {
/* 16 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IsAuthStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */