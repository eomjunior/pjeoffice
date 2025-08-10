/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.selectors.AbstractSelectorContainer;
/*    */ import org.apache.tools.ant.types.selectors.FileSelector;
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
/*    */ public class IsFileSelected
/*    */   extends AbstractSelectorContainer
/*    */   implements Condition
/*    */ {
/* 31 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*    */ 
/*    */   
/*    */   private File file;
/*    */ 
/*    */   
/*    */   private File baseDir;
/*    */ 
/*    */   
/*    */   public void setFile(File file) {
/* 41 */     this.file = file;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBaseDir(File baseDir) {
/* 50 */     this.baseDir = baseDir;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate() {
/* 57 */     if (selectorCount() != 1) {
/* 58 */       throw new BuildException("Only one selector allowed");
/*    */     }
/* 60 */     super.validate();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() {
/* 68 */     if (this.file == null) {
/* 69 */       throw new BuildException("file attribute not set");
/*    */     }
/* 71 */     validate();
/* 72 */     File myBaseDir = this.baseDir;
/* 73 */     if (myBaseDir == null) {
/* 74 */       myBaseDir = getProject().getBaseDir();
/*    */     }
/*    */     
/* 77 */     FileSelector f = getSelectors(getProject())[0];
/* 78 */     return f.isSelected(myBaseDir, FILE_UTILS
/* 79 */         .removeLeadingPath(myBaseDir, this.file), this.file);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsFileSelected.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */