/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import org.apache.tools.ant.AntClassLoader;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.types.Path;
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
/*    */ public final class SplitClassLoader
/*    */   extends AntClassLoader
/*    */ {
/*    */   private final String[] splitClasses;
/*    */   
/*    */   public SplitClassLoader(ClassLoader parent, Path path, Project project, String[] splitClasses) {
/* 42 */     super(parent, project, path, true);
/* 43 */     this.splitClasses = splitClasses;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected synchronized Class<?> loadClass(String classname, boolean resolve) throws ClassNotFoundException {
/* 51 */     Class<?> theClass = findLoadedClass(classname);
/* 52 */     if (theClass != null) {
/* 53 */       return theClass;
/*    */     }
/* 55 */     if (isSplit(classname)) {
/* 56 */       theClass = findClass(classname);
/* 57 */       if (resolve) {
/* 58 */         resolveClass(theClass);
/*    */       }
/* 60 */       return theClass;
/*    */     } 
/* 62 */     return super.loadClass(classname, resolve);
/*    */   }
/*    */   
/*    */   private boolean isSplit(String classname) {
/* 66 */     String simplename = classname.substring(classname.lastIndexOf('.') + 1);
/* 67 */     for (String splitClass : this.splitClasses) {
/* 68 */       if (simplename.equals(splitClass) || simplename
/* 69 */         .startsWith(splitClass + '$')) {
/* 70 */         return true;
/*    */       }
/*    */     } 
/* 73 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/SplitClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */