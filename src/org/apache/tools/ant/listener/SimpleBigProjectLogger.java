/*    */ package org.apache.tools.ant.listener;
/*    */ 
/*    */ import org.apache.tools.ant.BuildEvent;
/*    */ import org.apache.tools.ant.NoBannerLogger;
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
/*    */ public class SimpleBigProjectLogger
/*    */   extends NoBannerLogger
/*    */ {
/*    */   protected String extractTargetName(BuildEvent event) {
/* 38 */     String targetName = super.extractTargetName(event);
/* 39 */     String projectName = extractProjectName(event);
/* 40 */     if (projectName == null || targetName == null) {
/* 41 */       return targetName;
/*    */     }
/* 43 */     return projectName + '.' + targetName;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/listener/SimpleBigProjectLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */