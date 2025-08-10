/*     */ package org.apache.tools.ant.types.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.selectors.BaseSelector;
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
/*     */ public class ScriptSelector
/*     */   extends BaseSelector
/*     */ {
/*  39 */   private ScriptRunnerHelper helper = new ScriptRunnerHelper();
/*     */ 
/*     */ 
/*     */   
/*     */   private ScriptRunnerBase runner;
/*     */ 
/*     */ 
/*     */   
/*     */   private File basedir;
/*     */ 
/*     */ 
/*     */   
/*     */   private String filename;
/*     */ 
/*     */ 
/*     */   
/*     */   private File file;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean selected;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/*  64 */     super.setProject(project);
/*  65 */     this.helper.setProjectComponent((ProjectComponent)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManager(String manager) {
/*  74 */     this.helper.setManager(manager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/*  83 */     this.helper.setLanguage(language);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() throws BuildException {
/*  93 */     if (this.runner != null) {
/*     */       return;
/*     */     }
/*  96 */     this.runner = this.helper.getScriptRunner();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File file) {
/* 105 */     this.helper.setSrc(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 114 */     this.helper.addText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 123 */     this.helper.setClasspath(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 132 */     return this.helper.createClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 142 */     this.helper.setClasspathRef(r);
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
/*     */   public void setSetBeans(boolean setBeans) {
/* 156 */     this.helper.setSetBeans(setBeans);
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
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 172 */     init();
/* 173 */     setSelected(true);
/* 174 */     this.file = file;
/* 175 */     this.basedir = basedir;
/* 176 */     this.filename = filename;
/* 177 */     this.runner.addBean("basedir", basedir);
/* 178 */     this.runner.addBean("filename", filename);
/* 179 */     this.runner.addBean("file", file);
/* 180 */     this.runner.executeScript("ant_selector");
/* 181 */     return isSelected();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getBasedir() {
/* 189 */     return this.basedir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 197 */     return this.filename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 205 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected() {
/* 213 */     return this.selected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelected(boolean selected) {
/* 222 */     this.selected = selected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 232 */     this.helper.setEncoding(encoding);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/optional/ScriptSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */