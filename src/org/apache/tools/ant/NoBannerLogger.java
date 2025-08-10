/*    */ package org.apache.tools.ant;
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
/*    */ public class NoBannerLogger
/*    */   extends DefaultLogger
/*    */ {
/*    */   protected String targetName;
/*    */   
/*    */   public synchronized void targetStarted(BuildEvent event) {
/* 50 */     this.targetName = extractTargetName(event);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String extractTargetName(BuildEvent event) {
/* 60 */     return event.getTarget().getName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void targetFinished(BuildEvent event) {
/* 69 */     this.targetName = null;
/*    */   }
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
/*    */   public void messageLogged(BuildEvent event) {
/* 83 */     if (event.getPriority() > this.msgOutputLevel || null == event
/* 84 */       .getMessage() || event
/* 85 */       .getMessage().trim().isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 89 */     synchronized (this) {
/* 90 */       if (null != this.targetName) {
/* 91 */         this.out.printf("%n%s:%n", new Object[] { this.targetName });
/* 92 */         this.targetName = null;
/*    */       } 
/*    */     } 
/*    */     
/* 96 */     super.messageLogged(event);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/NoBannerLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */