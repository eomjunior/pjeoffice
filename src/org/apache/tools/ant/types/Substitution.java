/*    */ package org.apache.tools.ant.types;
/*    */ 
/*    */ import org.apache.tools.ant.Project;
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
/*    */ public class Substitution
/*    */   extends DataType
/*    */ {
/*    */   public static final String DATA_TYPE_NAME = "substitution";
/* 41 */   private String expression = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExpression(String expression) {
/* 49 */     this.expression = expression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getExpression(Project p) {
/* 60 */     if (isReference()) {
/* 61 */       return getRef(p).getExpression(p);
/*    */     }
/*    */     
/* 64 */     return this.expression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Substitution getRef(Project p) {
/* 74 */     return getCheckedRef(Substitution.class, getDataTypeName(), p);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Substitution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */