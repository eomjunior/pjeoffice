/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.types.Resource;
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
/*    */ public class ResourceExists
/*    */   extends ProjectComponent
/*    */   implements Condition
/*    */ {
/*    */   private Resource resource;
/*    */   
/*    */   public void add(Resource r) {
/* 39 */     if (this.resource != null) {
/* 40 */       throw new BuildException("only one resource can be tested");
/*    */     }
/* 42 */     this.resource = r;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void validate() throws BuildException {
/* 49 */     if (this.resource == null) {
/* 50 */       throw new BuildException("resource is required");
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 55 */     validate();
/* 56 */     return this.resource.isExists();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/ResourceExists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */