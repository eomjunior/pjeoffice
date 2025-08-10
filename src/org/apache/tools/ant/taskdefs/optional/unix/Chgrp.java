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
/*    */ public class Chgrp
/*    */   extends AbstractAccessTask
/*    */ {
/*    */   private boolean haveGroup = false;
/*    */   
/*    */   public Chgrp() {
/* 49 */     super.setExecutable("chgrp");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setGroup(String group) {
/* 58 */     createArg().setValue(group);
/* 59 */     this.haveGroup = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void checkConfiguration() {
/* 68 */     if (!this.haveGroup) {
/* 69 */       throw new BuildException("Required attribute group not set in chgrp", 
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


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/unix/Chgrp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */