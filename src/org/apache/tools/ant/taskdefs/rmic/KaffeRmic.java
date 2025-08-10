/*     */ package org.apache.tools.ant.taskdefs.rmic;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteJava;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KaffeRmic
/*     */   extends DefaultRmicAdapter
/*     */ {
/*  33 */   private static final String[] RMIC_CLASSNAMES = new String[] { "gnu.classpath.tools.rmi.rmic.RMIC", "gnu.java.rmi.rmic.RMIC", "kaffe.rmi.rmic.RMIC" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String COMPILER_NAME = "kaffe";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean areIiopAndIdlSupported() {
/*  53 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws BuildException {
/*  59 */     getRmic().log("Using Kaffe rmic", 3);
/*  60 */     Commandline cmd = setupRmicCommand();
/*     */     
/*  62 */     Class<?> c = getRmicClass();
/*  63 */     if (c == null) {
/*  64 */       StringBuilder buf = new StringBuilder("Cannot use Kaffe rmic, as it is not available.  None of ");
/*     */       
/*  66 */       for (String className : RMIC_CLASSNAMES) {
/*  67 */         if (buf.length() > 0) {
/*  68 */           buf.append(", ");
/*     */         }
/*     */         
/*  71 */         buf.append(className);
/*     */       } 
/*  73 */       buf.append(" have been found. A common solution is to set the environment variable JAVA_HOME or CLASSPATH.");
/*     */       
/*  75 */       throw new BuildException(buf.toString(), 
/*  76 */           getRmic().getLocation());
/*     */     } 
/*     */     
/*  79 */     cmd.setExecutable(c.getName());
/*  80 */     if (!c.getName().equals(RMIC_CLASSNAMES[RMIC_CLASSNAMES.length - 1])) {
/*     */       
/*  82 */       cmd.createArgument().setValue("-verbose");
/*  83 */       getRmic().log(Commandline.describeCommand(cmd));
/*     */     } 
/*  85 */     ExecuteJava ej = new ExecuteJava();
/*  86 */     ej.setJavaCommand(cmd);
/*  87 */     return (ej.fork((ProjectComponent)getRmic()) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAvailable() {
/*  95 */     return (getRmicClass() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> getRmicClass() {
/* 105 */     for (String className : RMIC_CLASSNAMES) {
/*     */       try {
/* 107 */         return Class.forName(className);
/* 108 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */     } 
/*     */ 
/*     */     
/* 112 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/KaffeRmic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */