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
/*    */ public class ExtensionPoint
/*    */   extends Target
/*    */ {
/*    */   private static final String NO_CHILDREN_ALLOWED = "you must not nest child elements into an extension-point";
/*    */   
/*    */   public ExtensionPoint() {}
/*    */   
/*    */   public ExtensionPoint(Target other) {
/* 37 */     super(other);
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
/*    */   public final void addTask(Task task) {
/* 49 */     throw new BuildException("you must not nest child elements into an extension-point");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void addDataType(RuntimeConfigurable r) {
/* 57 */     throw new BuildException("you must not nest child elements into an extension-point");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ExtensionPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */