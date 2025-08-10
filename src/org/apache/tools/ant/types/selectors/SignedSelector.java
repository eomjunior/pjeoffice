/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.taskdefs.condition.IsSigned;
/*    */ import org.apache.tools.ant.types.DataType;
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
/*    */ public class SignedSelector
/*    */   extends DataType
/*    */   implements FileSelector
/*    */ {
/* 31 */   private IsSigned isSigned = new IsSigned();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 39 */     this.isSigned.setName(name);
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
/*    */   public boolean isSelected(File basedir, String filename, File file) {
/* 53 */     if (file.isDirectory()) {
/* 54 */       return false;
/*    */     }
/* 56 */     this.isSigned.setProject(getProject());
/* 57 */     this.isSigned.setFile(file);
/* 58 */     return this.isSigned.eval();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/SignedSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */