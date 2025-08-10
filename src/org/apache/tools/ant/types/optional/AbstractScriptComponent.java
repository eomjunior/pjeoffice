/*     */ package org.apache.tools.ant.types.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
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
/*     */ public abstract class AbstractScriptComponent
/*     */   extends ProjectComponent
/*     */ {
/*  37 */   private ScriptRunnerHelper helper = new ScriptRunnerHelper();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private ScriptRunnerBase runner = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/*  50 */     super.setProject(project);
/*  51 */     this.helper.setProjectComponent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptRunnerBase getRunner() {
/*  59 */     initScriptRunner();
/*  60 */     return this.runner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File file) {
/*  69 */     this.helper.setSrc(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/*  78 */     this.helper.addText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManager(String manager) {
/*  87 */     this.helper.setManager(manager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/*  96 */     this.helper.setLanguage(language);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initScriptRunner() {
/* 103 */     if (this.runner != null) {
/*     */       return;
/*     */     }
/* 106 */     this.helper.setProjectComponent(this);
/* 107 */     this.runner = this.helper.getScriptRunner();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 115 */     this.helper.setClasspath(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 124 */     return this.helper.createClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 134 */     this.helper.setClasspathRef(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void executeScript(String execName) {
/* 142 */     getRunner().executeScript(execName);
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
/* 157 */     this.helper.setSetBeans(setBeans);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 167 */     this.helper.setEncoding(encoding);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/optional/AbstractScriptComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */