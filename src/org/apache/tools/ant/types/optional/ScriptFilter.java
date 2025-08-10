/*     */ package org.apache.tools.ant.types.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.filters.TokenFilter;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.util.ScriptRunnerBase;
/*     */ import org.apache.tools.ant.util.ScriptRunnerHelper;
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
/*     */ public class ScriptFilter
/*     */   extends TokenFilter.ChainableReaderFilter
/*     */ {
/*  44 */   private ScriptRunnerHelper helper = new ScriptRunnerHelper();
/*     */ 
/*     */   
/*  47 */   private ScriptRunnerBase runner = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String token;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/*  58 */     super.setProject(project);
/*  59 */     this.helper.setProjectComponent((ProjectComponent)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/*  68 */     this.helper.setLanguage(language);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() throws BuildException {
/*  77 */     if (this.runner != null) {
/*     */       return;
/*     */     }
/*  80 */     this.runner = this.helper.getScriptRunner();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToken(String token) {
/*  89 */     this.token = token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToken() {
/*  98 */     return this.token;
/*     */   }
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
/*     */   public String filter(String token) {
/* 111 */     init();
/* 112 */     setToken(token);
/* 113 */     this.runner.executeScript("ant_filter");
/* 114 */     return getToken();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File file) {
/* 123 */     this.helper.setSrc(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 132 */     this.helper.addText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManager(String manager) {
/* 141 */     this.helper.setManager(manager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 149 */     this.helper.setClasspath(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 158 */     return this.helper.createClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 168 */     this.helper.setClasspathRef(r);
/*     */   }
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
/*     */   public void setSetBeans(boolean setBeans) {
/* 183 */     this.helper.setSetBeans(setBeans);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 193 */     this.helper.setEncoding(encoding);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/optional/ScriptFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */