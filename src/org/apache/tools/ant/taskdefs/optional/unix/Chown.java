/*    */ package org.apache.tools.ant.taskdefs.optional.unix;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class Chown
/*    */   extends AbstractAccessTask
/*    */ {
/*    */   private boolean haveOwner = false;
/*    */   
/*    */   public Chown() {
/* 49 */     super.setExecutable("chown");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOwner(String owner) {
/* 58 */     createArg().setValue(owner);
/* 59 */     this.haveOwner = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void checkConfiguration() {
/* 68 */     if (!this.haveOwner) {
/* 69 */       throw new BuildException("Required attribute owner not set in chown", 
/* 70 */           getLocation());
/*    */     }
/* 72 */     super.checkConfiguration();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExecutable(String e) {
/* 82 */     throw new BuildException(getTaskType() + " doesn't support the executable attribute", 
/*    */         
/* 84 */         getLocation());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/unix/Chown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */