/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilesMatch
/*    */   implements Condition
/*    */ {
/* 38 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*    */ 
/*    */ 
/*    */   
/*    */   private File file1;
/*    */ 
/*    */   
/*    */   private File file2;
/*    */ 
/*    */   
/*    */   private boolean textfile = false;
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFile1(File file1) {
/* 53 */     this.file1 = file1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFile2(File file2) {
/* 62 */     this.file2 = file2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTextfile(boolean textfile) {
/* 70 */     this.textfile = textfile;
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
/*    */   public boolean eval() throws BuildException {
/* 84 */     if (this.file1 == null || this.file2 == null) {
/* 85 */       throw new BuildException("both file1 and file2 are required in filesmatch");
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 90 */     boolean matches = false;
/*    */     try {
/* 92 */       matches = FILE_UTILS.contentEquals(this.file1, this.file2, this.textfile);
/* 93 */     } catch (IOException ioe) {
/* 94 */       throw new BuildException("when comparing files: " + ioe
/* 95 */           .getMessage(), ioe);
/*    */     } 
/* 97 */     return matches;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/FilesMatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */