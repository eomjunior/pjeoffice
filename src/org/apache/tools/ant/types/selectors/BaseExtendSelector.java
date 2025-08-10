/*    */ package org.apache.tools.ant.types.selectors;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.Parameter;
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
/*    */ public abstract class BaseExtendSelector
/*    */   extends BaseSelector
/*    */   implements ExtendFileSelector
/*    */ {
/* 39 */   protected Parameter[] parameters = null;
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
/*    */   public void setParameters(Parameter... parameters) {
/* 51 */     this.parameters = parameters;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Parameter[] getParameters() {
/* 61 */     return this.parameters;
/*    */   }
/*    */   
/*    */   public abstract boolean isSelected(File paramFile1, String paramString, File paramFile2) throws BuildException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/BaseExtendSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */