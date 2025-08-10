/*    */ package org.apache.tools.ant.taskdefs.optional.testing;
/*    */ 
/*    */ import org.apache.tools.ant.taskdefs.WaitFor;
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
/*    */ public class BlockFor
/*    */   extends WaitFor
/*    */ {
/*    */   private String text;
/*    */   
/*    */   public BlockFor() {
/* 39 */     super("blockfor");
/* 40 */     this.text = getTaskName() + " timed out";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockFor(String taskName) {
/* 49 */     super(taskName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void processTimeout() throws BuildTimeoutException {
/* 59 */     super.processTimeout();
/* 60 */     throw new BuildTimeoutException(this.text, getLocation());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addText(String message) {
/* 69 */     this.text = getProject().replaceProperties(message);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/testing/BlockFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */