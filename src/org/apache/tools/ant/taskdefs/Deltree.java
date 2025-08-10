/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Task;
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
/*    */ @Deprecated
/*    */ public class Deltree
/*    */   extends Task
/*    */ {
/*    */   private File dir;
/*    */   
/*    */   public void setDir(File dir) {
/* 45 */     this.dir = dir;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() throws BuildException {
/* 55 */     log("DEPRECATED - The deltree task is deprecated.  Use delete instead.");
/*    */ 
/*    */     
/* 58 */     if (this.dir == null) {
/* 59 */       throw new BuildException("dir attribute must be set!", getLocation());
/*    */     }
/*    */     
/* 62 */     if (this.dir.exists()) {
/* 63 */       if (!this.dir.isDirectory()) {
/* 64 */         if (!this.dir.delete()) {
/* 65 */           throw new BuildException("Unable to delete directory " + this.dir
/* 66 */               .getAbsolutePath(), 
/* 67 */               getLocation());
/*    */         }
/*    */         
/*    */         return;
/*    */       } 
/* 72 */       log("Deleting: " + this.dir.getAbsolutePath());
/*    */       
/* 74 */       removeDir(this.dir);
/*    */     } 
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
/*    */   private void removeDir(File dir) {
/* 87 */     for (String s : dir.list()) {
/* 88 */       File f = new File(dir, s);
/* 89 */       if (f.isDirectory()) {
/* 90 */         removeDir(f);
/* 91 */       } else if (!f.delete()) {
/* 92 */         throw new BuildException("Unable to delete file " + f
/* 93 */             .getAbsolutePath());
/*    */       } 
/*    */     } 
/* 96 */     if (!dir.delete())
/* 97 */       throw new BuildException("Unable to delete directory " + dir
/* 98 */           .getAbsolutePath()); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Deltree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */