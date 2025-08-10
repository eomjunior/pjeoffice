/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.PropertyResource;
/*     */ import org.apache.tools.ant.types.resources.StringResource;
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
/*     */ public abstract class ScriptRunnerBase
/*     */ {
/*     */   private boolean keepEngine = false;
/*     */   private String language;
/*  53 */   private String script = "";
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */   
/*     */   private boolean compiled;
/*     */ 
/*     */   
/*     */   private Project project;
/*     */ 
/*     */   
/*     */   private ClassLoader scriptLoader;
/*     */   
/*  67 */   private final Map<String, Object> beans = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBeans(Map<String, ?> dictionary) {
/*  76 */     dictionary.forEach((k, v) -> {
/*     */           try {
/*     */             addBean(k, v);
/*  79 */           } catch (BuildException buildException) {}
/*     */         });
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
/*     */   public void addBean(String key, Object bean) {
/*  95 */     if (!key.isEmpty() && Character.isJavaIdentifierStart(key.charAt(0)) && key
/*  96 */       .chars().skip(1L).allMatch(Character::isJavaIdentifierPart)) {
/*  97 */       this.beans.put(key, bean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, Object> getBeans() {
/* 106 */     return this.beans;
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
/*     */   public void setLanguage(String language) {
/* 144 */     this.language = language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLanguage() {
/* 152 */     return this.language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScriptClassLoader(ClassLoader classLoader) {
/* 160 */     this.scriptLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader getScriptClassLoader() {
/* 168 */     return this.scriptLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepEngine(boolean keepEngine) {
/* 176 */     this.keepEngine = keepEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getKeepEngine() {
/* 184 */     return this.keepEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCompiled(boolean compiled) {
/* 193 */     this.compiled = compiled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean getCompiled() {
/* 202 */     return this.compiled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 211 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File file) {
/* 219 */     String filename = file.getPath();
/* 220 */     if (!file.exists()) {
/* 221 */       throw new BuildException("file " + filename + " not found.");
/*     */     }
/*     */     
/* 224 */     try { InputStream in = Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]);
/*     */       
/* 226 */       try { Charset charset = (null == this.encoding) ? Charset.defaultCharset() : Charset.forName(this.encoding);
/*     */         
/* 228 */         readSource(in, filename, charset);
/* 229 */         if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*     */     
/* 231 */     { throw new BuildException("file " + filename + " not found.", e); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readSource(InputStream in, String name, Charset charset) {
/*     */     
/* 242 */     try { Reader reader = new BufferedReader(new InputStreamReader(in, charset));
/*     */       
/* 244 */       try { this.script += FileUtils.safeReadFully(reader);
/* 245 */         reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ex)
/* 246 */     { throw new BuildException("Failed to read " + name, ex); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadResource(Resource sourceResource) {
/* 257 */     if (sourceResource instanceof StringResource) {
/* 258 */       this.script += ((StringResource)sourceResource).getValue();
/*     */       return;
/*     */     } 
/* 261 */     if (sourceResource instanceof PropertyResource) {
/* 262 */       this.script += ((PropertyResource)sourceResource).getValue();
/*     */       
/*     */       return;
/*     */     } 
/* 266 */     String name = sourceResource.toLongString(); 
/* 267 */     try { InputStream in = sourceResource.getInputStream(); 
/* 268 */       try { readSource(in, name, Charset.defaultCharset());
/* 269 */         if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 270 */     { throw new BuildException("Failed to open " + name, e); }
/* 271 */     catch (UnsupportedOperationException e)
/* 272 */     { throw new BuildException("Failed to open " + name + " - it is not readable", e); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadResources(ResourceCollection collection) {
/* 284 */     collection.forEach(this::loadResource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 293 */     this.script += text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScript() {
/* 301 */     return this.script;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearScript() {
/* 308 */     this.script = "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/* 316 */     this.project = project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/* 324 */     return this.project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindToComponent(ProjectComponent component) {
/* 334 */     this.project = component.getProject();
/* 335 */     addBeans(this.project.getProperties());
/* 336 */     addBeans(this.project.getUserProperties());
/* 337 */     addBeans(this.project.getCopyOfTargets());
/* 338 */     addBeans(this.project.getCopyOfReferences());
/* 339 */     addBean("project", this.project);
/* 340 */     addBean("self", component);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindToComponentMinimum(ProjectComponent component) {
/* 349 */     this.project = component.getProject();
/* 350 */     addBean("project", this.project);
/* 351 */     addBean("self", component);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkLanguage() {
/* 359 */     if (this.language == null) {
/* 360 */       throw new BuildException("script language must be specified");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader replaceContextLoader() {
/* 371 */     ClassLoader origContextClassLoader = Thread.currentThread().getContextClassLoader();
/* 372 */     if (getScriptClassLoader() == null) {
/* 373 */       setScriptClassLoader(getClass().getClassLoader());
/*     */     }
/* 375 */     Thread.currentThread().setContextClassLoader(getScriptClassLoader());
/* 376 */     return origContextClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void restoreContextLoader(ClassLoader origLoader) {
/* 386 */     Thread.currentThread().setContextClassLoader(origLoader);
/*     */   }
/*     */   
/*     */   public abstract void executeScript(String paramString);
/*     */   
/*     */   public abstract Object evaluateScript(String paramString);
/*     */   
/*     */   public abstract boolean supportsLanguage();
/*     */   
/*     */   public abstract String getManagerName();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ScriptRunnerBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */