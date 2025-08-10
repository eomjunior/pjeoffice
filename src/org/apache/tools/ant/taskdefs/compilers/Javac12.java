/*    */ package org.apache.tools.ant.taskdefs.compilers;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*    */ import org.apache.tools.ant.types.Commandline;
/*    */ import org.apache.tools.ant.util.JavaEnvUtils;
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
/*    */ @Deprecated
/*    */ public class Javac12
/*    */   extends DefaultCompilerAdapter
/*    */ {
/*    */   protected static final String CLASSIC_COMPILER_CLASSNAME = "sun.tools.javac.Main";
/*    */   
/*    */   public boolean execute() throws BuildException {
/* 49 */     this.attributes.log("Using classic compiler", 3);
/* 50 */     Commandline cmd = setupJavacCommand(true);
/*    */     
/* 52 */     try { LogOutputStream logOutputStream = new LogOutputStream((Task)this.attributes, 1);
/*    */ 
/*    */       
/* 55 */       try { Class<?> c = Class.forName("sun.tools.javac.Main");
/* 56 */         Constructor<?> cons = c.getConstructor(new Class[] { OutputStream.class, String.class });
/* 57 */         Object compiler = cons.newInstance(new Object[] { logOutputStream, "javac" });
/*    */ 
/*    */         
/* 60 */         Method compile = c.getMethod("compile", new Class[] { String[].class });
/* 61 */         boolean bool = ((Boolean)compile.invoke(compiler, new Object[] { cmd.getArguments() })).booleanValue();
/* 62 */         logOutputStream.close(); return bool; } catch (Throwable throwable) { try { logOutputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (ClassNotFoundException ex)
/* 63 */     { throw new BuildException("Cannot use classic compiler, as it is not available. \n A common solution is to set the environment variable JAVA_HOME to your jdk directory.\nIt is currently set to \"" + 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 69 */           JavaEnvUtils.getJavaHome() + "\"", this.location);
/*    */        }
/*    */     
/* 72 */     catch (Exception ex)
/* 73 */     { if (ex instanceof BuildException) {
/* 74 */         throw (BuildException)ex;
/*    */       }
/* 76 */       throw new BuildException("Error starting classic compiler: ", ex, this.location); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/Javac12.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */