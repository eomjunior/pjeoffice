/*    */ package org.apache.tools.ant;
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
/*    */ public final class DefaultDefinitions
/*    */ {
/*    */   private static final String IF_NAMESPACE = "ant:if";
/*    */   private static final String UNLESS_NAMESPACE = "ant:unless";
/*    */   private final ComponentHelper componentHelper;
/*    */   
/*    */   public DefaultDefinitions(ComponentHelper componentHelper) {
/* 36 */     this.componentHelper = componentHelper;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() {
/* 43 */     attributeNamespaceDef("ant:if");
/* 44 */     attributeNamespaceDef("ant:unless");
/*    */     
/* 46 */     ifUnlessDef("true", "IfTrueAttribute");
/* 47 */     ifUnlessDef("set", "IfSetAttribute");
/* 48 */     ifUnlessDef("blank", "IfBlankAttribute");
/*    */   }
/*    */   
/*    */   private void attributeNamespaceDef(String ns) {
/* 52 */     AntTypeDefinition def = new AntTypeDefinition();
/* 53 */     def.setName(ProjectHelper.nsToComponentName(ns));
/* 54 */     def.setClassName("org.apache.tools.ant.attribute.AttributeNamespace");
/* 55 */     def.setClassLoader(getClass().getClassLoader());
/* 56 */     def.setRestrict(true);
/* 57 */     this.componentHelper.addDataTypeDefinition(def);
/*    */   }
/*    */   
/*    */   private void ifUnlessDef(String name, String base) {
/* 61 */     String classname = "org.apache.tools.ant.attribute." + base;
/* 62 */     componentDef("ant:if", name, classname);
/* 63 */     componentDef("ant:unless", name, classname + "$Unless");
/*    */   }
/*    */   
/*    */   private void componentDef(String ns, String name, String classname) {
/* 67 */     AntTypeDefinition def = new AntTypeDefinition();
/* 68 */     def.setName(ProjectHelper.genComponentName(ns, name));
/* 69 */     def.setClassName(classname);
/* 70 */     def.setClassLoader(getClass().getClassLoader());
/* 71 */     def.setRestrict(true);
/* 72 */     this.componentHelper.addDataTypeDefinition(def);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/DefaultDefinitions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */