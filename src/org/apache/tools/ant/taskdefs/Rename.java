/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.util.FileUtils;
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
/*    */ public class Rename
/*    */   extends Task
/*    */ {
/* 38 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*    */ 
/*    */   
/*    */   private File src;
/*    */ 
/*    */   
/*    */   private File dest;
/*    */   
/*    */   private boolean replace = true;
/*    */ 
/*    */   
/*    */   public void setSrc(File src) {
/* 50 */     this.src = src;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDest(File dest) {
/* 58 */     this.dest = dest;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setReplace(String replace) {
/* 66 */     this.replace = Project.toBoolean(replace);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() throws BuildException {
/* 76 */     log("DEPRECATED - The rename task is deprecated.  Use move instead.");
/*    */     
/* 78 */     if (this.dest == null) {
/* 79 */       throw new BuildException("dest attribute is required", getLocation());
/*    */     }
/*    */     
/* 82 */     if (this.src == null) {
/* 83 */       throw new BuildException("src attribute is required", getLocation());
/*    */     }
/*    */     
/* 86 */     if (!this.replace && this.dest.exists()) {
/* 87 */       throw new BuildException(this.dest + " already exists.");
/*    */     }
/*    */     
/*    */     try {
/* 91 */       FILE_UTILS.rename(this.src, this.dest);
/* 92 */     } catch (IOException e) {
/* 93 */       throw new BuildException("Unable to rename " + this.src + " to " + this.dest, e, 
/* 94 */           getLocation());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Rename.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */