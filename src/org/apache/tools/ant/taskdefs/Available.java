/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
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
/*     */ public class Available
/*     */   extends Task
/*     */   implements Condition
/*     */ {
/*  44 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private String property;
/*     */   private String classname;
/*     */   private String filename;
/*     */   private File file;
/*     */   private Path filepath;
/*     */   private String resource;
/*     */   private FileDir type;
/*     */   private Path classpath;
/*     */   private AntClassLoader loader;
/*  55 */   private Object value = "true";
/*     */ 
/*     */   
/*     */   private boolean isTask = false;
/*     */ 
/*     */   
/*     */   private boolean ignoreSystemclasses = false;
/*     */ 
/*     */   
/*     */   private boolean searchParents = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSearchParents(boolean searchParents) {
/*  69 */     this.searchParents = searchParents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/*  78 */     createClasspath().append(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/*  87 */     if (this.classpath == null) {
/*  88 */       this.classpath = new Path(getProject());
/*     */     }
/*  90 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 100 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilepath(Path filepath) {
/* 109 */     createFilepath().append(filepath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createFilepath() {
/* 119 */     if (this.filepath == null) {
/* 120 */       this.filepath = new Path(getProject());
/*     */     }
/* 122 */     return this.filepath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/* 132 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/* 142 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/* 152 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassname(String classname) {
/* 162 */     if (!classname.isEmpty()) {
/* 163 */       this.classname = classname;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 174 */     this.file = file;
/* 175 */     this.filename = FILE_UTILS.removeLeadingPath(getProject().getBaseDir(), file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResource(String resource) {
/* 184 */     this.resource = resource;
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
/*     */   @Deprecated
/*     */   public void setType(String type) {
/* 197 */     log("DEPRECATED - The setType(String) method has been deprecated. Use setType(Available.FileDir) instead.", 1);
/*     */     
/* 199 */     this.type = new FileDir();
/* 200 */     this.type.setValue(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(FileDir type) {
/* 211 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoresystemclasses(boolean ignore) {
/* 221 */     this.ignoreSystemclasses = ignore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 231 */     if (this.property == null) {
/* 232 */       throw new BuildException("property attribute is required", 
/* 233 */           getLocation());
/*     */     }
/*     */     
/* 236 */     this.isTask = true;
/*     */     try {
/* 238 */       if (eval()) {
/* 239 */         PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
/* 240 */         Object oldvalue = ph.getProperty(this.property);
/* 241 */         if (null != oldvalue && !oldvalue.equals(this.value)) {
/* 242 */           log(String.format("DEPRECATED - <available> used to override an existing property.%n  Build file should not reuse the same property name for different values.", new Object[0]), 1);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 248 */         ph.setProperty(this.property, this.value, true);
/*     */       } 
/*     */     } finally {
/* 251 */       this.isTask = false;
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
/*     */   public boolean eval() throws BuildException {
/*     */     try {
/* 264 */       if (this.classname == null && this.file == null && this.resource == null) {
/* 265 */         throw new BuildException("At least one of (classname|file|resource) is required", 
/*     */             
/* 267 */             getLocation());
/*     */       }
/* 269 */       if (this.type != null && 
/* 270 */         this.file == null) {
/* 271 */         throw new BuildException("The type attribute is only valid when specifying the file attribute.", 
/*     */             
/* 273 */             getLocation());
/*     */       }
/*     */       
/* 276 */       if (this.classpath != null) {
/* 277 */         this.classpath.setProject(getProject());
/* 278 */         this.loader = getProject().createClassLoader(this.classpath);
/*     */       } 
/* 280 */       String appendix = "";
/* 281 */       if (this.isTask) {
/* 282 */         appendix = " to set property " + this.property;
/*     */       } else {
/* 284 */         setTaskName("available");
/*     */       } 
/* 286 */       if (this.classname != null && !checkClass(this.classname)) {
/* 287 */         log("Unable to load class " + this.classname + appendix, 3);
/*     */         
/* 289 */         return false;
/*     */       } 
/* 291 */       if (this.file != null && !checkFile()) {
/* 292 */         StringBuilder buf = new StringBuilder("Unable to find ");
/* 293 */         if (this.type != null) {
/* 294 */           buf.append(this.type).append(' ');
/*     */         }
/* 296 */         buf.append(this.filename).append(appendix);
/* 297 */         log(buf.toString(), 3);
/* 298 */         return false;
/*     */       } 
/* 300 */       if (this.resource != null && !checkResource(this.resource)) {
/* 301 */         log("Unable to load resource " + this.resource + appendix, 3);
/*     */         
/* 303 */         return false;
/*     */       } 
/*     */     } finally {
/* 306 */       if (this.loader != null) {
/* 307 */         this.loader.cleanup();
/* 308 */         this.loader = null;
/*     */       } 
/* 310 */       if (!this.isTask) {
/* 311 */         setTaskName(null);
/*     */       }
/*     */     } 
/* 314 */     return true;
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
/*     */   private boolean checkFile() {
/* 334 */     if (this.filepath == null) {
/* 335 */       return checkFile(this.file, this.filename);
/*     */     }
/* 337 */     String[] paths = this.filepath.list();
/* 338 */     for (String p : paths) {
/* 339 */       log("Searching " + p, 3);
/* 340 */       File path = new File(p);
/*     */ 
/*     */ 
/*     */       
/* 344 */       if (path.exists() && (this.filename
/* 345 */         .equals(p) || this.filename
/* 346 */         .equals(path.getName()))) {
/* 347 */         if (this.type == null) {
/* 348 */           log("Found: " + path, 3);
/* 349 */           return true;
/*     */         } 
/* 351 */         if (this.type.isDir() && path
/* 352 */           .isDirectory()) {
/* 353 */           log("Found directory: " + path, 3);
/* 354 */           return true;
/*     */         } 
/* 356 */         if (this.type.isFile() && path
/* 357 */           .isFile()) {
/* 358 */           log("Found file: " + path, 3);
/* 359 */           return true;
/*     */         } 
/*     */         
/* 362 */         return false;
/*     */       } 
/* 364 */       File parent = path.getParentFile();
/*     */       
/* 366 */       if (parent != null && parent.exists() && this.filename
/* 367 */         .equals(parent.getAbsolutePath())) {
/* 368 */         if (this.type == null) {
/* 369 */           log("Found: " + parent, 3);
/* 370 */           return true;
/*     */         } 
/* 372 */         if (this.type.isDir()) {
/* 373 */           log("Found directory: " + parent, 3);
/* 374 */           return true;
/*     */         } 
/*     */         
/* 377 */         return false;
/*     */       } 
/*     */       
/* 380 */       if (path.exists() && path.isDirectory() && 
/* 381 */         checkFile(new File(path, this.filename), this.filename + " in " + path))
/*     */       {
/* 383 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 388 */       while (this.searchParents && parent != null && parent.exists()) {
/* 389 */         if (checkFile(new File(parent, this.filename), this.filename + " in " + parent))
/*     */         {
/* 391 */           return true;
/*     */         }
/* 393 */         parent = parent.getParentFile();
/*     */       } 
/*     */     } 
/* 396 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkFile(File f, String text) {
/* 403 */     if (this.type != null) {
/* 404 */       if (this.type.isDir()) {
/* 405 */         if (f.isDirectory()) {
/* 406 */           log("Found directory: " + text, 3);
/*     */         }
/* 408 */         return f.isDirectory();
/*     */       } 
/* 410 */       if (this.type.isFile()) {
/* 411 */         if (f.isFile()) {
/* 412 */           log("Found file: " + text, 3);
/*     */         }
/* 414 */         return f.isFile();
/*     */       } 
/*     */     } 
/* 417 */     if (f.exists()) {
/* 418 */       log("Found: " + text, 3);
/*     */     }
/* 420 */     return f.exists();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkResource(String resource) {
/* 427 */     InputStream is = null;
/*     */     try {
/* 429 */       if (this.loader != null) {
/* 430 */         is = this.loader.getResourceAsStream(resource);
/*     */       } else {
/* 432 */         ClassLoader cL = getClass().getClassLoader();
/* 433 */         if (cL != null) {
/* 434 */           is = cL.getResourceAsStream(resource);
/*     */         } else {
/* 436 */           is = ClassLoader.getSystemResourceAsStream(resource);
/*     */         } 
/*     */       } 
/* 439 */       return (is != null);
/*     */     } finally {
/* 441 */       FileUtils.close(is);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkClass(String classname) {
/*     */     try {
/* 450 */       if (this.ignoreSystemclasses) {
/* 451 */         this.loader = getProject().createClassLoader(this.classpath);
/* 452 */         this.loader.setParentFirst(false);
/* 453 */         this.loader.addJavaLibraries();
/*     */         try {
/* 455 */           this.loader.findClass(classname);
/* 456 */         } catch (SecurityException se) {
/*     */ 
/*     */ 
/*     */           
/* 460 */           return true;
/*     */         } 
/* 462 */       } else if (this.loader != null) {
/* 463 */         this.loader.loadClass(classname);
/*     */       } else {
/* 465 */         ClassLoader l = getClass().getClassLoader();
/*     */ 
/*     */         
/* 468 */         if (l != null) {
/* 469 */           Class.forName(classname, true, l);
/*     */         } else {
/* 471 */           Class.forName(classname);
/*     */         } 
/*     */       } 
/* 474 */       return true;
/* 475 */     } catch (ClassNotFoundException e) {
/* 476 */       log("class \"" + classname + "\" was not found", 4);
/*     */       
/* 478 */       return false;
/* 479 */     } catch (NoClassDefFoundError e) {
/* 480 */       log("Could not load dependent class \"" + e.getMessage() + "\" for class \"" + classname + "\"", 4);
/*     */ 
/*     */       
/* 483 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FileDir
/*     */     extends EnumeratedAttribute
/*     */   {
/* 493 */     private static final String[] VALUES = new String[] { "file", "dir" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 501 */       return VALUES;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDir() {
/* 510 */       return "dir".equalsIgnoreCase(getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isFile() {
/* 519 */       return "file".equalsIgnoreCase(getValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Available.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */