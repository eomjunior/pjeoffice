/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.tools.ant.AntTypeDefinition;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ComponentHelper;
/*     */ import org.apache.tools.ant.Location;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
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
/*     */ public abstract class Definer
/*     */   extends DefBase
/*     */ {
/*     */   private static final String ANTLIB_XML = "/antlib.xml";
/*  57 */   private static final ThreadLocal<Map<URL, Location>> RESOURCE_STACK = ThreadLocal.withInitial(java.util.HashMap::new);
/*     */   
/*     */   private String name;
/*     */   
/*     */   private String classname;
/*     */   private File file;
/*     */   private String resource;
/*     */   private boolean restrict = false;
/*  65 */   private int format = 0;
/*     */   private boolean definerSet = false;
/*  67 */   private int onError = 0;
/*     */ 
/*     */   
/*     */   private String adapter;
/*     */ 
/*     */   
/*     */   private String adaptTo;
/*     */ 
/*     */   
/*     */   private Class<?> adapterClass;
/*     */ 
/*     */   
/*     */   private Class<?> adaptToClass;
/*     */ 
/*     */ 
/*     */   
/*     */   public static class OnError
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public static final int FAIL = 0;
/*     */ 
/*     */     
/*     */     public static final int REPORT = 1;
/*     */ 
/*     */     
/*     */     public static final int IGNORE = 2;
/*     */ 
/*     */     
/*     */     public static final int FAIL_ALL = 3;
/*     */ 
/*     */     
/*     */     public static final String POLICY_FAIL = "fail";
/*     */ 
/*     */     
/*     */     public static final String POLICY_REPORT = "report";
/*     */     
/*     */     public static final String POLICY_IGNORE = "ignore";
/*     */     
/*     */     public static final String POLICY_FAILALL = "failall";
/*     */ 
/*     */     
/*     */     public OnError() {}
/*     */ 
/*     */     
/*     */     public OnError(String value) {
/* 112 */       setValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 121 */       return new String[] { "fail", "report", "ignore", "failall" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Format
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public static final int PROPERTIES = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int XML = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 141 */       return new String[] { "properties", "xml" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setRestrict(boolean restrict) {
/* 151 */     this.restrict = restrict;
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
/*     */   public void setOnError(OnError onError) {
/* 166 */     this.onError = onError.getIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormat(Format format) {
/* 174 */     this.format = format.getIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 181 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 188 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResource() {
/* 195 */     return this.resource;
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
/* 206 */     ClassLoader al = createLoader();
/*     */     
/* 208 */     if (!this.definerSet) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 213 */       if (getURI() == null) {
/* 214 */         throw new BuildException("name, file or resource attribute of " + 
/*     */             
/* 216 */             getTaskName() + " is undefined", 
/* 217 */             getLocation());
/*     */       }
/*     */       
/* 220 */       if (getURI().startsWith("antlib:")) {
/*     */         
/* 222 */         String uri1 = getURI();
/* 223 */         setResource(makeResourceFromURI(uri1));
/*     */       } else {
/* 225 */         throw new BuildException("Only antlib URIs can be located from the URI alone, not the URI '" + 
/*     */             
/* 227 */             getURI() + "'");
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     if (this.name != null) {
/* 232 */       if (this.classname == null) {
/* 233 */         throw new BuildException("classname attribute of " + 
/* 234 */             getTaskName() + " element is undefined", getLocation());
/*     */       }
/* 236 */       addDefinition(al, this.name, this.classname);
/*     */     } else {
/* 238 */       Enumeration<URL> urls; if (this.classname != null) {
/* 239 */         throw new BuildException("You must not specify classname together with file or resource.", 
/*     */             
/* 241 */             getLocation());
/*     */       }
/*     */       
/* 244 */       if (this.file == null) {
/* 245 */         urls = resourceToURLs(al);
/*     */       } else {
/* 247 */         URL url = fileToURL();
/* 248 */         if (url == null) {
/*     */           return;
/*     */         }
/* 251 */         urls = Collections.enumeration(Collections.singleton(url));
/*     */       } 
/*     */       
/* 254 */       while (urls.hasMoreElements()) {
/* 255 */         URL url = urls.nextElement();
/*     */         
/* 257 */         int fmt = this.format;
/* 258 */         if (url.getPath().toLowerCase(Locale.ENGLISH).endsWith(".xml")) {
/* 259 */           fmt = 1;
/*     */         }
/*     */         
/* 262 */         if (fmt == 0) {
/* 263 */           loadProperties(al, url); break;
/*     */         } 
/* 265 */         if (((Map)RESOURCE_STACK.get()).get(url) != null) {
/* 266 */           log("Warning: Recursive loading of " + url + " ignored at " + 
/*     */               
/* 268 */               getLocation() + " originally loaded at " + ((Map)RESOURCE_STACK
/*     */               
/* 270 */               .get()).get(url), 1);
/*     */           continue;
/*     */         } 
/*     */         try {
/* 274 */           ((Map<URL, Location>)RESOURCE_STACK.get()).put(url, getLocation());
/* 275 */           loadAntlib(al, url);
/*     */         } finally {
/* 277 */           ((Map)RESOURCE_STACK.get()).remove(url);
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
/*     */ 
/*     */   
/*     */   public static String makeResourceFromURI(String uri) {
/* 291 */     String resource, path = uri.substring("antlib:".length());
/*     */     
/* 293 */     if (path.startsWith("//")) {
/*     */ 
/*     */       
/* 296 */       resource = path.substring("//".length());
/* 297 */       if (!resource.endsWith(".xml"))
/*     */       {
/* 299 */         resource = resource + "/antlib.xml";
/*     */       }
/*     */     } else {
/*     */       
/* 303 */       resource = path.replace('.', '/') + "/antlib.xml";
/*     */     } 
/* 305 */     return resource;
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
/*     */   private URL fileToURL() {
/* 317 */     String message = null;
/* 318 */     if (!this.file.exists()) {
/* 319 */       message = "File " + this.file + " does not exist";
/*     */     }
/* 321 */     if (message == null && !this.file.isFile()) {
/* 322 */       message = "File " + this.file + " is not a file";
/*     */     }
/* 324 */     if (message == null) {
/*     */       try {
/* 326 */         return FileUtils.getFileUtils().getFileURL(this.file);
/* 327 */       } catch (Exception ex) {
/*     */ 
/*     */         
/* 330 */         message = "File " + this.file + " cannot use as URL: " + ex.toString();
/*     */       } 
/*     */     }
/*     */     
/* 334 */     switch (this.onError) {
/*     */       case 3:
/* 336 */         throw new BuildException(message);
/*     */       
/*     */       case 0:
/*     */       case 1:
/* 340 */         log(message, 1);
/*     */         break;
/*     */       
/*     */       case 2:
/* 344 */         log(message, 3);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 350 */     return null;
/*     */   }
/*     */   
/*     */   private Enumeration<URL> resourceToURLs(ClassLoader classLoader) {
/*     */     Enumeration<URL> ret;
/*     */     try {
/* 356 */       ret = classLoader.getResources(this.resource);
/* 357 */     } catch (IOException e) {
/* 358 */       throw new BuildException("Could not fetch resources named " + this.resource, e, 
/*     */           
/* 360 */           getLocation());
/*     */     } 
/* 362 */     if (!ret.hasMoreElements()) {
/* 363 */       String message = "Could not load definitions from resource " + this.resource + ". It could not be found.";
/*     */       
/* 365 */       switch (this.onError) {
/*     */         case 3:
/* 367 */           throw new BuildException(message);
/*     */         case 0:
/*     */         case 1:
/* 370 */           log(message, 1);
/*     */           break;
/*     */         case 2:
/* 373 */           log(message, 3);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 380 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadProperties(ClassLoader al, URL url) {
/*     */     
/* 390 */     try { InputStream is = url.openStream(); 
/* 391 */       try { if (is == null)
/* 392 */         { log("Could not load definitions from " + url, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 403 */           if (is != null) is.close();  return; }  Properties props = new Properties(); props.load(is); for (String key : props.stringPropertyNames()) { this.name = key; this.classname = props.getProperty(this.name); addDefinition(al, this.name, this.classname); }  if (is != null) is.close();  } catch (Throwable throwable) { if (is != null) try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
/* 404 */     { throw new BuildException(ex, getLocation()); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadAntlib(ClassLoader classLoader, URL url) {
/*     */     try {
/* 416 */       Antlib antlib = Antlib.createAntlib(getProject(), url, getURI());
/* 417 */       antlib.setClassLoader(classLoader);
/* 418 */       antlib.setURI(getURI());
/* 419 */       antlib.execute();
/* 420 */     } catch (BuildException ex) {
/* 421 */       throw ProjectHelper.addLocationToBuildException(ex, 
/* 422 */           getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 432 */     if (this.definerSet) {
/* 433 */       tooManyDefinitions();
/*     */     }
/* 435 */     this.definerSet = true;
/* 436 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResource(String res) {
/* 445 */     if (this.definerSet) {
/* 446 */       tooManyDefinitions();
/*     */     }
/* 448 */     this.definerSet = true;
/* 449 */     this.resource = res;
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
/*     */   public void setAntlib(String antlib) {
/* 463 */     if (this.definerSet) {
/* 464 */       tooManyDefinitions();
/*     */     }
/* 466 */     if (!antlib.startsWith("antlib:")) {
/* 467 */       throw new BuildException("Invalid antlib attribute - it must start with antlib:");
/*     */     }
/*     */     
/* 470 */     setURI(antlib);
/* 471 */     this.resource = antlib.substring("antlib:".length()).replace('.', '/') + "/antlib.xml";
/*     */     
/* 473 */     this.definerSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 481 */     if (this.definerSet) {
/* 482 */       tooManyDefinitions();
/*     */     }
/* 484 */     this.definerSet = true;
/* 485 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassname() {
/* 494 */     return this.classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassname(String classname) {
/* 504 */     this.classname = classname;
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
/*     */   public void setAdapter(String adapter) {
/* 519 */     this.adapter = adapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setAdapterClass(Class<?> adapterClass) {
/* 528 */     this.adapterClass = adapterClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdaptTo(String adaptTo) {
/* 539 */     this.adaptTo = adaptTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setAdaptToClass(Class<?> adaptToClass) {
/* 550 */     this.adaptToClass = adaptToClass;
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
/*     */   protected void addDefinition(ClassLoader al, String name, String classname) throws BuildException {
/* 563 */     Class<?> cl = null;
/*     */     try {
/*     */       try {
/* 566 */         name = ProjectHelper.genComponentName(getURI(), name);
/*     */         
/* 568 */         if (this.onError != 2) {
/* 569 */           cl = Class.forName(classname, true, al);
/*     */         }
/*     */         
/* 572 */         if (this.adapter != null) {
/* 573 */           this.adapterClass = Class.forName(this.adapter, true, al);
/*     */         }
/*     */         
/* 576 */         if (this.adaptTo != null) {
/* 577 */           this.adaptToClass = Class.forName(this.adaptTo, true, al);
/*     */         }
/*     */         
/* 580 */         AntTypeDefinition def = new AntTypeDefinition();
/* 581 */         def.setName(name);
/* 582 */         def.setClassName(classname);
/* 583 */         def.setClass(cl);
/* 584 */         def.setAdapterClass(this.adapterClass);
/* 585 */         def.setAdaptToClass(this.adaptToClass);
/* 586 */         def.setRestrict(this.restrict);
/* 587 */         def.setClassLoader(al);
/* 588 */         if (cl != null) {
/* 589 */           def.checkClass(getProject());
/*     */         }
/* 591 */         ComponentHelper.getComponentHelper(getProject())
/* 592 */           .addDataTypeDefinition(def);
/* 593 */       } catch (ClassNotFoundException cnfe) {
/* 594 */         throw new BuildException(
/* 595 */             getTaskName() + " class " + classname + " cannot be found\n using the classloader " + al, cnfe, 
/*     */             
/* 597 */             getLocation());
/* 598 */       } catch (NoClassDefFoundError ncdfe) {
/* 599 */         throw new BuildException(
/* 600 */             getTaskName() + " A class needed by class " + classname + " cannot be found: " + ncdfe
/* 601 */             .getMessage() + "\n using the classloader " + al, ncdfe, 
/*     */             
/* 603 */             getLocation());
/*     */       } 
/* 605 */     } catch (BuildException ex) {
/* 606 */       switch (this.onError) {
/*     */         case 0:
/*     */         case 3:
/* 609 */           throw ex;
/*     */         case 1:
/* 611 */           log(ex.getLocation() + "Warning: " + ex.getMessage(), 1);
/*     */           return;
/*     */       } 
/*     */       
/* 615 */       log(ex.getLocation() + ex.getMessage(), 4);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tooManyDefinitions() {
/* 626 */     throw new BuildException("Only one of the attributes name, file and resource can be set", 
/*     */         
/* 628 */         getLocation());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Definer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */