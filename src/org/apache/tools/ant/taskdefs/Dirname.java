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
/*    */ public class Dirname
/*    */   extends Task
/*    */ {
/*    */   private File file;
/*    */   private String property;
/*    */   
/*    */   public void setFile(File file) {
/* 56 */     this.file = file;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setProperty(String property) {
/* 64 */     this.property = property;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() throws BuildException {
/* 73 */     if (this.property == null) {
/* 74 */       throw new BuildException("property attribute required", getLocation());
/*    */     }
/* 76 */     if (this.file == null) {
/* 77 */       throw new BuildException("file attribute required", getLocation());
/*    */     }
/* 79 */     getProject().setNewProperty(this.property, this.file.getParent());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Dirname.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */