/*    */ package org.apache.tools.ant.taskdefs.condition;
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
/*    */ public class Contains
/*    */   implements Condition
/*    */ {
/*    */   private String string;
/*    */   private String subString;
/*    */   private boolean caseSensitive = true;
/*    */   
/*    */   public void setString(String string) {
/* 40 */     this.string = string;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSubstring(String subString) {
/* 49 */     this.subString = subString;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCasesensitive(boolean b) {
/* 58 */     this.caseSensitive = b;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 68 */     if (this.string == null || this.subString == null) {
/* 69 */       throw new BuildException("both string and substring are required in contains");
/*    */     }
/*    */ 
/*    */     
/* 73 */     return this.caseSensitive ? 
/* 74 */       this.string.contains(this.subString) : 
/* 75 */       this.string.toLowerCase().contains(this.subString.toLowerCase());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Contains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */