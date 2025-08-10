/*    */ package org.apache.tools.ant.taskdefs.optional.native2ascii;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.taskdefs.ExecuteJava;
/*    */ import org.apache.tools.ant.taskdefs.optional.Native2Ascii;
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
/*    */ public final class KaffeNative2Ascii
/*    */   extends DefaultNative2Ascii
/*    */ {
/* 34 */   private static final String[] N2A_CLASSNAMES = new String[] { "gnu.classpath.tools.native2ascii.Native2ASCII", "kaffe.tools.native2ascii.Native2Ascii" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final String IMPLEMENTATION_NAME = "kaffe";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setup(Commandline cmd, Native2Ascii args) throws BuildException {
/* 49 */     if (args.getReverse()) {
/* 50 */       throw new BuildException("-reverse is not supported by Kaffe");
/*    */     }
/* 52 */     super.setup(cmd, args);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean run(Commandline cmd, ProjectComponent log) throws BuildException {
/* 59 */     ExecuteJava ej = new ExecuteJava();
/* 60 */     Class<?> c = getN2aClass();
/* 61 */     if (c == null) {
/* 62 */       throw new BuildException("Couldn't load Kaffe's Native2Ascii class");
/*    */     }
/*    */ 
/*    */     
/* 66 */     cmd.setExecutable(c.getName());
/* 67 */     ej.setJavaCommand(cmd);
/* 68 */     ej.execute(log.getProject());
/*    */     
/* 70 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Class<?> getN2aClass() {
/* 80 */     for (String className : N2A_CLASSNAMES) {
/*    */       try {
/* 82 */         return Class.forName(className);
/* 83 */       } catch (ClassNotFoundException classNotFoundException) {}
/*    */     } 
/*    */ 
/*    */     
/* 87 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/native2ascii/KaffeNative2Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */