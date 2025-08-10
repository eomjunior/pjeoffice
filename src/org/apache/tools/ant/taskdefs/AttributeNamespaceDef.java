/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.AntTypeDefinition;
/*    */ import org.apache.tools.ant.ComponentHelper;
/*    */ import org.apache.tools.ant.ProjectHelper;
/*    */ import org.apache.tools.ant.attribute.AttributeNamespace;
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
/*    */ public final class AttributeNamespaceDef
/*    */   extends AntlibDefinition
/*    */ {
/*    */   public void execute() {
/* 40 */     String componentName = ProjectHelper.nsToComponentName(
/* 41 */         getURI());
/* 42 */     AntTypeDefinition def = new AntTypeDefinition();
/* 43 */     def.setName(componentName);
/* 44 */     def.setClassName(AttributeNamespace.class.getName());
/* 45 */     def.setClass(AttributeNamespace.class);
/* 46 */     def.setRestrict(true);
/* 47 */     def.setClassLoader(AttributeNamespace.class.getClassLoader());
/* 48 */     ComponentHelper.getComponentHelper(getProject())
/* 49 */       .addDataTypeDefinition(def);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/AttributeNamespaceDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */