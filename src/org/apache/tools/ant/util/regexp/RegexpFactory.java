/*    */ package org.apache.tools.ant.util.regexp;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.util.ClasspathUtils;
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
/*    */ public class RegexpFactory
/*    */   extends RegexpMatcherFactory
/*    */ {
/*    */   public Regexp newRegexp() throws BuildException {
/* 39 */     return newRegexp(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Regexp newRegexp(Project p) throws BuildException {
/*    */     String systemDefault;
/* 51 */     if (p == null) {
/* 52 */       systemDefault = System.getProperty("ant.regexp.regexpimpl");
/*    */     } else {
/* 54 */       systemDefault = p.getProperty("ant.regexp.regexpimpl");
/*    */     } 
/*    */     
/* 57 */     if (systemDefault != null) {
/* 58 */       return createRegexpInstance(systemDefault);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 63 */     return new Jdk14RegexpRegexp();
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
/*    */   protected Regexp createRegexpInstance(String classname) throws BuildException {
/* 77 */     return (Regexp)ClasspathUtils.newInstance(classname, RegexpFactory.class
/* 78 */         .getClassLoader(), Regexp.class);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/regexp/RegexpFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */