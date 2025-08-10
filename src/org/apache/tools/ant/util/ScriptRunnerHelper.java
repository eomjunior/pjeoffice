/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.Union;
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
/*     */ public class ScriptRunnerHelper
/*     */ {
/*  32 */   private ClasspathUtils.Delegate cpDelegate = null;
/*     */   private File srcFile;
/*     */   private String encoding;
/*  35 */   private String manager = "auto";
/*     */   private String language;
/*     */   private String text;
/*     */   private boolean compiled = false;
/*     */   private boolean setBeans = true;
/*     */   private ProjectComponent projectComponent;
/*  41 */   private ClassLoader scriptLoader = null;
/*  42 */   private Union resources = new Union();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProjectComponent(ProjectComponent component) {
/*  49 */     this.projectComponent = component;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptRunnerBase getScriptRunner() {
/*  57 */     ScriptRunnerBase runner = getRunner();
/*  58 */     runner.setCompiled(this.compiled);
/*     */     
/*  60 */     if (this.encoding != null)
/*     */     {
/*  62 */       runner.setEncoding(this.encoding);
/*     */     }
/*  64 */     if (this.srcFile != null) {
/*  65 */       runner.setSrc(this.srcFile);
/*     */     }
/*  67 */     if (this.text != null) {
/*  68 */       runner.addText(this.text);
/*     */     }
/*  70 */     if (this.resources != null) {
/*  71 */       runner.loadResources((ResourceCollection)this.resources);
/*     */     }
/*  73 */     if (this.setBeans) {
/*  74 */       runner.bindToComponent(this.projectComponent);
/*     */     } else {
/*  76 */       runner.bindToComponentMinimum(this.projectComponent);
/*     */     } 
/*  78 */     return runner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/*  87 */     return getClassPathDelegate().createClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/*  96 */     getClassPathDelegate().setClasspath(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 106 */     getClassPathDelegate().setClasspathref(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File file) {
/* 115 */     this.srcFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getSrc() {
/* 124 */     return this.srcFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 134 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 143 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 152 */     this.text = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManager(String manager) {
/* 161 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String language) {
/* 170 */     this.language = language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLanguage() {
/* 178 */     return this.language;
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
/*     */   public void setCompiled(boolean compiled) {
/* 192 */     this.compiled = compiled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCompiled() {
/* 200 */     return this.compiled;
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
/* 214 */     this.setBeans = setBeans;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassLoader(ClassLoader loader) {
/* 222 */     this.scriptLoader = loader;
/*     */   }
/*     */   
/*     */   private synchronized ClassLoader generateClassLoader() {
/* 226 */     if (this.scriptLoader != null) {
/* 227 */       return this.scriptLoader;
/*     */     }
/* 229 */     if (this.cpDelegate == null) {
/* 230 */       this.scriptLoader = getClass().getClassLoader();
/* 231 */       return this.scriptLoader;
/*     */     } 
/* 233 */     this.scriptLoader = this.cpDelegate.getClassLoader();
/* 234 */     return this.scriptLoader;
/*     */   }
/*     */   
/*     */   private ClasspathUtils.Delegate getClassPathDelegate() {
/* 238 */     if (this.cpDelegate == null) {
/* 239 */       if (this.projectComponent == null) {
/* 240 */         throw new IllegalStateException("Can't access classpath without a project component");
/*     */       }
/* 242 */       this.cpDelegate = ClasspathUtils.getDelegate(this.projectComponent);
/*     */     } 
/* 244 */     return this.cpDelegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ScriptRunnerBase getRunner() {
/* 251 */     return (new ScriptRunnerCreator(this.projectComponent.getProject())).createRunner(this.manager, this.language, 
/* 252 */         generateClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection resource) {
/* 262 */     this.resources.add(resource);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ScriptRunnerHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */