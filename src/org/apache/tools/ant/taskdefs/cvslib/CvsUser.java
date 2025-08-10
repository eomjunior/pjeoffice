/*    */ package org.apache.tools.ant.taskdefs.cvslib;
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
/*    */ public class CvsUser
/*    */ {
/*    */   private String userID;
/*    */   private String displayName;
/*    */   
/*    */   public void setDisplayname(String displayName) {
/* 39 */     this.displayName = displayName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setUserid(String userID) {
/* 48 */     this.userID = userID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserID() {
/* 57 */     return this.userID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDisplayname() {
/* 66 */     return this.displayName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate() throws BuildException {
/* 76 */     if (null == this.userID) {
/* 77 */       throw new BuildException("Username attribute must be set.");
/*    */     }
/* 79 */     if (null == this.displayName)
/* 80 */       throw new BuildException("Displayname attribute must be set for userID %s", new Object[] { this.userID }); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/CvsUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */