/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Task;
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
/*    */ public class AntlibDefinition
/*    */   extends Task
/*    */ {
/* 33 */   private String uri = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ClassLoader antlibClassLoader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setURI(String uri) throws BuildException {
/* 47 */     if ("antlib:org.apache.tools.ant".equals(uri)) {
/* 48 */       uri = "";
/*    */     }
/* 50 */     if (uri.startsWith("ant:")) {
/* 51 */       throw new BuildException("Attempt to use a reserved URI %s", new Object[] { uri });
/*    */     }
/* 53 */     this.uri = uri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getURI() {
/* 61 */     return this.uri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAntlibClassLoader(ClassLoader classLoader) {
/* 70 */     this.antlibClassLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassLoader getAntlibClassLoader() {
/* 79 */     return this.antlibClassLoader;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/AntlibDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */