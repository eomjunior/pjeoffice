/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.types.Reference;
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
/*    */ public class IsReference
/*    */   extends ProjectComponent
/*    */   implements Condition
/*    */ {
/*    */   private Reference ref;
/*    */   private String type;
/*    */   
/*    */   public void setRefid(Reference r) {
/* 42 */     this.ref = r;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(String type) {
/* 51 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 61 */     if (this.ref == null) {
/* 62 */       throw new BuildException("No reference specified for isreference condition");
/*    */     }
/*    */ 
/*    */     
/* 66 */     String key = this.ref.getRefId();
/* 67 */     if (!getProject().hasReference(key)) {
/* 68 */       return false;
/*    */     }
/*    */     
/* 71 */     if (this.type == null) {
/* 72 */       return true;
/*    */     }
/* 74 */     Class<?> typeClass = (Class)getProject().getDataTypeDefinitions().get(this.type);
/* 75 */     if (typeClass == null) {
/* 76 */       typeClass = (Class)getProject().getTaskDefinitions().get(this.type);
/*    */     }
/*    */ 
/*    */     
/* 80 */     return (typeClass != null && typeClass
/* 81 */       .isAssignableFrom(getProject().getReference(key).getClass()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */