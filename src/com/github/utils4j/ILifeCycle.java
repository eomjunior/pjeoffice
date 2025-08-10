/*    */ package com.github.utils4j;
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
/*    */ public interface ILifeCycle<E extends Exception>
/*    */ {
/*    */   boolean isStarted();
/*    */   
/*    */   void start() throws E;
/*    */   
/*    */   void stop() throws E;
/*    */   
/*    */   default void stop(boolean kill) throws E {
/* 39 */     stop();
/*    */   }
/*    */   
/*    */   default void stop(long timeout) throws E {
/* 43 */     stop();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/ILifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */