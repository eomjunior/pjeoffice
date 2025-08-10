/*    */ package org.apache.tools.ant.taskdefs.optional.extension;
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
/*    */ public class ExtraAttribute
/*    */ {
/*    */   private String name;
/*    */   private String value;
/*    */   
/*    */   public void setName(String name) {
/* 38 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(String value) {
/* 47 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String getName() {
/* 56 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   String getValue() {
/* 65 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate() throws BuildException {
/* 75 */     if (null == this.name) {
/* 76 */       throw new BuildException("Missing name from parameter.");
/*    */     }
/* 78 */     if (null == this.value)
/* 79 */       throw new BuildException("Missing value from parameter " + this.name + "."); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/ExtraAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */