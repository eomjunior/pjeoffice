/*    */ package org.apache.tools.ant.listener;
/*    */ 
/*    */ import org.apache.tools.ant.BuildEvent;
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
/*    */ public class SilentLogger
/*    */   extends DefaultLogger
/*    */ {
/*    */   public void buildStarted(BuildEvent event) {}
/*    */   
/*    */   public void buildFinished(BuildEvent event) {
/* 37 */     if (event.getException() != null)
/* 38 */       super.buildFinished(event); 
/*    */   }
/*    */   
/*    */   public void targetStarted(BuildEvent event) {}
/*    */   
/*    */   public void targetFinished(BuildEvent event) {}
/*    */   
/*    */   public void taskStarted(BuildEvent event) {}
/*    */   
/*    */   public void taskFinished(BuildEvent event) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/listener/SilentLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */