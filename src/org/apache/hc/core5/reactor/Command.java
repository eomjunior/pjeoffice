/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import org.apache.hc.core5.concurrent.Cancellable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Command
/*    */   extends Cancellable
/*    */ {
/*    */   public enum Priority
/*    */   {
/* 40 */     NORMAL, IMMEDIATE;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */