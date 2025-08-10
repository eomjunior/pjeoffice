/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import org.apache.tools.ant.AntTypeDefinition;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ComponentHelper;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.ProjectHelper;
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
/*    */ public class TypeFound
/*    */   extends ProjectComponent
/*    */   implements Condition
/*    */ {
/*    */   private String name;
/*    */   private String uri;
/*    */   
/*    */   public void setName(String name) {
/* 43 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setURI(String uri) {
/* 52 */     this.uri = uri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean doesTypeExist(String typename) {
/* 62 */     ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
/* 63 */     String componentName = ProjectHelper.genComponentName(this.uri, typename);
/* 64 */     AntTypeDefinition def = helper.getDefinition(componentName);
/* 65 */     if (def == null) {
/* 66 */       return false;
/*    */     }
/*    */     
/* 69 */     boolean found = (def.getExposedClass(getProject()) != null);
/* 70 */     if (!found) {
/* 71 */       String text = helper.diagnoseCreationFailure(componentName, "type");
/* 72 */       log(text, 3);
/*    */     } 
/* 74 */     return found;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 85 */     if (this.name == null) {
/* 86 */       throw new BuildException("No type specified");
/*    */     }
/* 88 */     return doesTypeExist(this.name);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/TypeFound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */