/*    */ package org.apache.tools.ant.loader;
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
/*    */ public class AntClassLoader5
/*    */   extends AntClassLoader
/*    */ {
/*    */   static {
/* 31 */     registerAsParallelCapable();
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AntClassLoader5(ClassLoader parent, Project project, Path classpath, boolean parentFirst) {
/* 52 */     super(parent, project, classpath, parentFirst);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/loader/AntClassLoader5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */