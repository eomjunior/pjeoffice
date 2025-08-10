/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.property.GetProperty;
/*     */ import org.apache.tools.ant.property.ResolvePropertyMap;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public class Property
/*     */   extends Task
/*     */ {
/*     */   protected String name;
/*     */   protected String value;
/*     */   protected File file;
/*     */   protected URL url;
/*     */   protected String resource;
/*     */   protected Path classpath;
/*     */   protected String env;
/*     */   protected Reference ref;
/*     */   protected String prefix;
/*     */   private String runtime;
/*     */   private Project fallback;
/*     */   private Object untypedValue;
/*     */   private boolean valueAttributeUsed = false;
/*     */   private boolean relative = false;
/*     */   private File basedir;
/*     */   private boolean prefixValues = false;
/*     */   protected boolean userProperty;
/*     */   
/*     */   public Property() {
/* 109 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Property(boolean userProperty) {
/* 118 */     this(userProperty, (Project)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Property(boolean userProperty, Project fallback) {
/* 129 */     this.userProperty = userProperty;
/* 130 */     this.fallback = fallback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRelative(boolean relative) {
/* 139 */     this.relative = relative;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBasedir(File basedir) {
/* 148 */     this.basedir = basedir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 156 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 164 */     return this.name;
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
/*     */   public void setLocation(File location) {
/* 178 */     if (this.relative) {
/* 179 */       internalSetValue(location);
/*     */     } else {
/* 181 */       setValue(location.getAbsolutePath());
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
/*     */   
/*     */   public void setValue(Object value) {
/* 194 */     this.valueAttributeUsed = true;
/* 195 */     internalSetValue(value);
/*     */   }
/*     */   
/*     */   private void internalSetValue(Object value) {
/* 199 */     this.untypedValue = value;
/*     */     
/* 201 */     this.value = (value == null) ? null : value.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/* 211 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String msg) {
/* 220 */     if (!this.valueAttributeUsed) {
/* 221 */       msg = getProject().replaceProperties(msg);
/* 222 */       String currentValue = getValue();
/* 223 */       if (currentValue != null) {
/* 224 */         msg = currentValue + msg;
/*     */       }
/* 226 */       internalSetValue(msg);
/* 227 */     } else if (!msg.trim().isEmpty()) {
/* 228 */       throw new BuildException("can't combine nested text with value attribute");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 237 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 247 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 255 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrl(URL url) {
/* 265 */     this.url = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getUrl() {
/* 273 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 284 */     this.prefix = prefix;
/* 285 */     if (prefix != null && !prefix.endsWith(".")) {
/* 286 */       this.prefix += ".";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 296 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefixValues(boolean b) {
/* 307 */     this.prefixValues = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getPrefixValues() {
/* 318 */     return this.prefixValues;
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
/*     */   public void setRefid(Reference ref) {
/* 331 */     this.ref = ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference getRefid() {
/* 339 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResource(String resource) {
/* 349 */     this.resource = resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResource() {
/* 357 */     return this.resource;
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
/*     */   public void setEnvironment(String env) {
/* 383 */     this.env = env;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnvironment() {
/* 392 */     return this.env;
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
/*     */   public void setRuntime(String prefix) {
/* 419 */     this.runtime = prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRuntime() {
/* 428 */     return this.runtime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 436 */     if (this.classpath == null) {
/* 437 */       this.classpath = classpath;
/*     */     } else {
/* 439 */       this.classpath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 448 */     if (this.classpath == null) {
/* 449 */       this.classpath = new Path(getProject());
/*     */     }
/* 451 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 460 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 469 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setUserProperty(boolean userProperty) {
/* 481 */     log("DEPRECATED: Ignoring request to set user property in Property task.", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 491 */     return (this.value == null) ? "" : this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 502 */     if (getProject() == null) {
/* 503 */       throw new IllegalStateException("project has not been set");
/*     */     }
/*     */     
/* 506 */     if (this.name != null) {
/* 507 */       if (this.untypedValue == null && this.ref == null) {
/* 508 */         throw new BuildException("You must specify value, location or refid with the name attribute", 
/*     */             
/* 510 */             getLocation());
/*     */       }
/*     */     }
/* 513 */     else if (this.url == null && this.file == null && this.resource == null && this.env == null && this.runtime == null) {
/*     */       
/* 515 */       throw new BuildException("You must specify url, file, resource, environment or runtime when not using the name attribute", 
/*     */           
/* 517 */           getLocation());
/*     */     } 
/*     */ 
/*     */     
/* 521 */     if (this.url == null && this.file == null && this.resource == null && this.prefix != null) {
/* 522 */       throw new BuildException("Prefix is only valid when loading from a url, file or resource", 
/*     */           
/* 524 */           getLocation());
/*     */     }
/*     */     
/* 527 */     if (this.name != null && this.untypedValue != null) {
/* 528 */       if (this.relative) {
/*     */ 
/*     */         
/*     */         try {
/* 532 */           File from = (this.untypedValue instanceof File) ? (File)this.untypedValue : new File(this.untypedValue.toString());
/* 533 */           File to = (this.basedir != null) ? this.basedir : getProject().getBaseDir();
/* 534 */           String relPath = FileUtils.getRelativePath(to, from);
/* 535 */           relPath = relPath.replace('/', File.separatorChar);
/* 536 */           addProperty(this.name, relPath);
/* 537 */         } catch (Exception e) {
/* 538 */           throw new BuildException(e, getLocation());
/*     */         } 
/*     */       } else {
/* 541 */         addProperty(this.name, this.untypedValue);
/*     */       } 
/*     */     }
/*     */     
/* 545 */     if (this.file != null) {
/* 546 */       loadFile(this.file);
/*     */     }
/*     */     
/* 549 */     if (this.url != null) {
/* 550 */       loadUrl(this.url);
/*     */     }
/*     */     
/* 553 */     if (this.resource != null) {
/* 554 */       loadResource(this.resource);
/*     */     }
/*     */     
/* 557 */     if (this.env != null) {
/* 558 */       loadEnvironment(this.env);
/*     */     }
/*     */     
/* 561 */     if (this.runtime != null) {
/* 562 */       loadRuntime(this.runtime);
/*     */     }
/*     */     
/* 565 */     if (this.name != null && this.ref != null) {
/*     */       try {
/* 567 */         addProperty(this.name, this.ref
/* 568 */             .getReferencedObject(getProject()).toString());
/* 569 */       } catch (BuildException be) {
/* 570 */         if (this.fallback != null) {
/* 571 */           addProperty(this.name, this.ref
/* 572 */               .getReferencedObject(this.fallback).toString());
/*     */         } else {
/* 574 */           throw be;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadUrl(URL url) throws BuildException {
/* 586 */     Properties props = new Properties();
/* 587 */     log("Loading " + url, 3);
/*     */     try {
/* 589 */       InputStream is = url.openStream(); 
/* 590 */       try { loadProperties(props, is, url.getFile().endsWith(".xml"));
/* 591 */         if (is != null) is.close();  } catch (Throwable throwable) { if (is != null)
/* 592 */           try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  addProperties(props);
/* 593 */     } catch (IOException ex) {
/* 594 */       throw new BuildException(ex, getLocation());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadProperties(Properties props, InputStream is, boolean isXml) throws IOException {
/* 612 */     if (isXml) {
/*     */       
/* 614 */       props.loadFromXML(is);
/*     */     } else {
/*     */       
/* 617 */       props.load(is);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadFile(File file) throws BuildException {
/* 627 */     Properties props = new Properties();
/* 628 */     log("Loading " + file.getAbsolutePath(), 3);
/*     */     try {
/* 630 */       if (file.exists()) {
/* 631 */         InputStream fis = Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]); 
/* 632 */         try { loadProperties(props, fis, file.getName().endsWith(".xml"));
/* 633 */           if (fis != null) fis.close();  } catch (Throwable throwable) { if (fis != null)
/* 634 */             try { fis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  addProperties(props);
/*     */       } else {
/* 636 */         log("Unable to find property file: " + file.getAbsolutePath(), 3);
/*     */       }
/*     */     
/* 639 */     } catch (IOException ex) {
/* 640 */       throw new BuildException(ex, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadResource(String name) {
/* 649 */     Properties props = new Properties();
/* 650 */     log("Resource Loading " + name, 3);
/*     */     
/* 652 */     ClassLoader cL = (this.classpath == null) ? getClass().getClassLoader() : (ClassLoader)getProject().createClassLoader(this.classpath);
/*     */     
/* 654 */     try { InputStream is = (cL == null) ? ClassLoader.getSystemResourceAsStream(name) : cL.getResourceAsStream(name); 
/* 655 */       try { if (is == null) {
/* 656 */           log("Unable to find resource " + name, 1);
/*     */         } else {
/* 658 */           loadProperties(props, is, name.endsWith(".xml"));
/* 659 */           addProperties(props);
/*     */         } 
/* 661 */         if (is != null) is.close();  } catch (Throwable throwable) { if (is != null) try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
/* 662 */     { throw new BuildException(ex, getLocation()); }
/*     */     finally
/* 664 */     { if (this.classpath != null && cL != null) {
/* 665 */         ((AntClassLoader)cL).cleanup();
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadEnvironment(String prefix) {
/* 675 */     Properties props = new Properties();
/* 676 */     if (!prefix.endsWith(".")) {
/* 677 */       prefix = prefix + ".";
/*     */     }
/* 679 */     log("Loading Environment " + prefix, 3);
/* 680 */     Map<String, String> osEnv = Execute.getEnvironmentVariables();
/* 681 */     for (Map.Entry<String, String> entry : osEnv.entrySet()) {
/* 682 */       props.put(prefix + (String)entry.getKey(), entry.getValue());
/*     */     }
/* 684 */     addProperties(props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadRuntime(String prefix) {
/* 693 */     Properties props = new Properties();
/* 694 */     if (!prefix.endsWith(".")) {
/* 695 */       prefix = prefix + ".";
/*     */     }
/* 697 */     log("Loading Runtime properties " + prefix, 3);
/* 698 */     Runtime r = Runtime.getRuntime();
/* 699 */     props.put(prefix + "availableProcessors", String.valueOf(r.availableProcessors()));
/* 700 */     props.put(prefix + "freeMemory", String.valueOf(r.freeMemory()));
/* 701 */     props.put(prefix + "maxMemory", String.valueOf(r.maxMemory()));
/* 702 */     props.put(prefix + "totalMemory", String.valueOf(r.totalMemory()));
/* 703 */     addProperties(props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addProperties(Properties props) {
/* 712 */     Map<String, Object> m = new HashMap<>();
/* 713 */     props.forEach((k, v) -> {
/*     */           if (k instanceof String) {
/*     */             m.put((String)k, v);
/*     */           }
/*     */         });
/* 718 */     resolveAllProperties(m);
/* 719 */     m.forEach((k, v) -> {
/*     */           String propertyName = (this.prefix == null) ? k : (this.prefix + k);
/*     */           addProperty(propertyName, v);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addProperty(String n, String v) {
/* 731 */     addProperty(n, v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addProperty(String n, Object v) {
/* 741 */     PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
/* 742 */     if (this.userProperty) {
/* 743 */       if (ph.getUserProperty(n) == null) {
/* 744 */         ph.setInheritedProperty(n, v);
/*     */       } else {
/* 746 */         log("Override ignored for " + n, 3);
/*     */       } 
/*     */     } else {
/* 749 */       ph.setNewProperty(n, v);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resolveAllProperties(Map<String, Object> props) throws BuildException {
/* 759 */     PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
/* 760 */     (new ResolvePropertyMap(
/* 761 */         getProject(), (GetProperty)propertyHelper, propertyHelper
/*     */         
/* 763 */         .getExpanders()))
/* 764 */       .resolveAllProperties(props, getPrefix(), getPrefixValues());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */