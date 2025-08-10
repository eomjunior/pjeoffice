/*     */ package org.apache.tools.ant.util.regexp;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.util.ClasspathUtils;
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
/*     */ public class RegexpMatcherFactory
/*     */ {
/*     */   public RegexpMatcher newRegexpMatcher() throws BuildException {
/*  42 */     return newRegexpMatcher(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpMatcher newRegexpMatcher(Project p) throws BuildException {
/*     */     String systemDefault;
/*  54 */     if (p == null) {
/*  55 */       systemDefault = System.getProperty("ant.regexp.regexpimpl");
/*     */     } else {
/*  57 */       systemDefault = p.getProperty("ant.regexp.regexpimpl");
/*     */     } 
/*     */     
/*  60 */     if (systemDefault != null) {
/*  61 */       return createInstance(systemDefault);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  66 */     return new Jdk14RegexpMatcher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RegexpMatcher createInstance(String className) throws BuildException {
/*  77 */     return (RegexpMatcher)ClasspathUtils.newInstance(className, RegexpMatcherFactory.class
/*  78 */         .getClassLoader(), RegexpMatcher.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void testAvailability(String className) throws BuildException {
/*     */     try {
/*  89 */       Class.forName(className);
/*  90 */     } catch (Throwable t) {
/*  91 */       throw new BuildException(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean regexpMatcherPresent(Project project) {
/*     */     try {
/* 104 */       (new RegexpMatcherFactory()).newRegexpMatcher(project);
/* 105 */       return true;
/* 106 */     } catch (Throwable ex) {
/* 107 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/regexp/RegexpMatcherFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */