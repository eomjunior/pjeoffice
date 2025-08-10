/*    */ package org.apache.tools.ant.taskdefs.compilers;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.types.Commandline;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Javac13
/*    */   extends DefaultCompilerAdapter
/*    */ {
/*    */   private static final int MODERN_COMPILER_SUCCESS = 0;
/*    */   
/*    */   public boolean execute() throws BuildException {
/* 49 */     this.attributes.log("Using modern compiler", 3);
/* 50 */     Commandline cmd = setupModernJavacCommand();
/*    */ 
/*    */     
/*    */     try {
/* 54 */       Class<?> c = Class.forName("com.sun.tools.javac.Main");
/* 55 */       Object compiler = c.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 56 */       Method compile = c.getMethod("compile", new Class[] { String[].class });
/* 57 */       int result = ((Integer)compile.invoke(compiler, new Object[] { cmd
/* 58 */             .getArguments() })).intValue();
/* 59 */       return (result == 0);
/* 60 */     } catch (Exception ex) {
/* 61 */       if (ex instanceof BuildException) {
/* 62 */         throw (BuildException)ex;
/*    */       }
/* 64 */       throw new BuildException("Error starting modern compiler", ex, this.location);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/Javac13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */