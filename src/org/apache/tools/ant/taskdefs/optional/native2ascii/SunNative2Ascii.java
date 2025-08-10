/*    */ package org.apache.tools.ant.taskdefs.optional.native2ascii;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SunNative2Ascii
/*    */   extends DefaultNative2Ascii
/*    */ {
/*    */   public static final String IMPLEMENTATION_NAME = "sun";
/*    */   private static final String SUN_TOOLS_NATIVE2ASCII_MAIN = "sun.tools.native2ascii.Main";
/*    */   
/*    */   protected void setup(Commandline cmd, Native2Ascii args) throws BuildException {
/* 45 */     if (args.getReverse()) {
/* 46 */       cmd.createArgument().setValue("-reverse");
/*    */     }
/* 48 */     super.setup(cmd, args);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean run(Commandline cmd, ProjectComponent log) throws BuildException {
/*    */     try {
/* 56 */       Class<?> n2aMain = Class.forName("sun.tools.native2ascii.Main");
/* 57 */       Method convert = n2aMain.getMethod("convert", new Class[] { String[].class });
/* 58 */       return Boolean.TRUE.equals(convert.invoke(n2aMain.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[] { cmd
/* 59 */               .getArguments() }));
/* 60 */     } catch (BuildException ex) {
/*    */       
/* 62 */       throw ex;
/* 63 */     } catch (NoSuchMethodException ex) {
/* 64 */       throw new BuildException("Could not find convert() method in %s", new Object[] { "sun.tools.native2ascii.Main" });
/*    */     }
/* 66 */     catch (Exception ex) {
/*    */       
/* 68 */       throw new BuildException("Error starting Sun's native2ascii: ", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/native2ascii/SunNative2Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */