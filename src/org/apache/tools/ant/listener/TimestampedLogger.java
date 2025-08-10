/*    */ package org.apache.tools.ant.listener;
/*    */ 
/*    */ import org.apache.tools.ant.DefaultLogger;
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
/*    */ public class TimestampedLogger
/*    */   extends DefaultLogger
/*    */ {
/*    */   public static final String SPACER = " - at ";
/*    */   
/*    */   protected String getBuildFailedMessage() {
/* 41 */     return super.getBuildFailedMessage() + " - at " + getTimestamp();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getBuildSuccessfulMessage() {
/* 52 */     return super.getBuildSuccessfulMessage() + " - at " + getTimestamp();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/listener/TimestampedLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */