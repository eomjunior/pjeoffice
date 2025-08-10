/*    */ package org.apache.tools.ant.taskdefs.cvslib;
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
/*    */ class RCSFile
/*    */ {
/*    */   private String name;
/*    */   private String revision;
/*    */   private String previousRevision;
/*    */   
/*    */   RCSFile(String name, String revision) {
/* 30 */     this(name, revision, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   RCSFile(String name, String revision, String previousRevision) {
/* 36 */     this.name = name;
/* 37 */     this.revision = revision;
/* 38 */     if (!revision.equals(previousRevision)) {
/* 39 */       this.previousRevision = previousRevision;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String getRevision() {
/* 56 */     return this.revision;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String getPreviousRevision() {
/* 64 */     return this.previousRevision;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/RCSFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */