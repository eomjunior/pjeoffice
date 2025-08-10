/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.PathTokenizer;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.resources.FileResourceIterator;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Path
/*     */   extends DataType
/*     */   implements Cloneable, ResourceCollection
/*     */ {
/*  72 */   public static Path systemClasspath = new Path(null, 
/*  73 */       System.getProperty("java.class.path"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static final Path systemBootClasspath = new Path(null, 
/*  81 */       System.getProperty("sun.boot.class.path"));
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean preserveBC;
/*     */ 
/*     */ 
/*     */   
/*     */   public class PathElement
/*     */     implements ResourceCollection
/*     */   {
/*     */     private String[] parts;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setLocation(File loc) {
/*  97 */       this.parts = new String[] { Path.translateFile(loc.getAbsolutePath()) };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPath(String path) {
/* 106 */       this.parts = Path.translatePath(Path.this.getProject(), path);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getParts() {
/* 115 */       return this.parts;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<Resource> iterator() {
/* 124 */       return (Iterator<Resource>)new FileResourceIterator(Path.this.getProject(), null, this.parts);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isFilesystemOnly() {
/* 133 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 142 */       return (this.parts == null) ? 0 : this.parts.length;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   private Union union = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cache = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(Project p, String path) {
/* 159 */     this(p);
/* 160 */     createPathElement().setPath(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(Project project) {
/* 168 */     setProject(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(File location) throws BuildException {
/* 178 */     checkAttributesAllowed();
/* 179 */     createPathElement().setLocation(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(String path) throws BuildException {
/* 188 */     checkAttributesAllowed();
/* 189 */     createPathElement().setPath(path);
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
/*     */   public void setRefid(Reference r) throws BuildException {
/* 202 */     if (this.union != null) {
/* 203 */       throw tooManyAttributes();
/*     */     }
/* 205 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathElement createPathElement() throws BuildException {
/* 214 */     if (isReference()) {
/* 215 */       throw noChildrenAllowed();
/*     */     }
/* 217 */     PathElement pe = new PathElement();
/* 218 */     add(pe);
/* 219 */     return pe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet fs) throws BuildException {
/* 228 */     if (fs.getProject() == null) {
/* 229 */       fs.setProject(getProject());
/*     */     }
/* 231 */     add(fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilelist(FileList fl) throws BuildException {
/* 240 */     if (fl.getProject() == null) {
/* 241 */       fl.setProject(getProject());
/*     */     }
/* 243 */     add(fl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDirset(DirSet dset) throws BuildException {
/* 252 */     if (dset.getProject() == null) {
/* 253 */       dset.setProject(getProject());
/*     */     }
/* 255 */     add(dset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Path path) throws BuildException {
/* 265 */     if (path == this) {
/* 266 */       throw circularReference();
/*     */     }
/* 268 */     if (path.getProject() == null) {
/* 269 */       path.setProject(getProject());
/*     */     }
/* 271 */     add(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection c) {
/* 280 */     checkChildrenAllowed();
/* 281 */     if (c == null) {
/*     */       return;
/*     */     }
/* 284 */     if (this.union == null) {
/* 285 */       this.union = new Union();
/* 286 */       this.union.setProject(getProject());
/* 287 */       this.union.setCache(this.cache);
/*     */     } 
/* 289 */     this.union.add(c);
/* 290 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath() throws BuildException {
/* 299 */     Path p = new Path(getProject());
/* 300 */     add(p);
/* 301 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(Path other) {
/* 309 */     if (other == null) {
/*     */       return;
/*     */     }
/* 312 */     add(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExisting(Path source) {
/* 322 */     addExisting(source, false);
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
/*     */   public void addExisting(Path source, boolean tryUserDir) {
/* 335 */     File userDir = tryUserDir ? new File(System.getProperty("user.dir")) : null;
/*     */     
/* 337 */     for (String name : source.list()) {
/* 338 */       File f = resolveFile(getProject(), name);
/*     */ 
/*     */ 
/*     */       
/* 342 */       if (tryUserDir && !f.exists()) {
/* 343 */         f = new File(userDir, name);
/*     */       }
/* 345 */       if (f.exists()) {
/* 346 */         setLocation(f);
/* 347 */       } else if (f.getParentFile() != null && f.getParentFile().exists() && 
/* 348 */         containsWildcards(f.getName())) {
/* 349 */         setLocation(f);
/* 350 */         log("adding " + f + " which contains wildcards and may not do what you intend it to do depending on your OS or version of Java", 3);
/*     */       }
/*     */       else {
/*     */         
/* 354 */         log("dropping " + f + " from path as it doesn't exist", 3);
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
/*     */   public void setCache(boolean b) {
/* 366 */     checkAttributesAllowed();
/* 367 */     this.cache = b;
/* 368 */     if (this.union != null) {
/* 369 */       this.union.setCache(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] list() {
/* 378 */     if (isReference()) {
/* 379 */       return getRef().list();
/*     */     }
/* 381 */     return (assertFilesystemOnly((ResourceCollection)this.union) == null) ? 
/* 382 */       new String[0] : this.union.list();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 392 */     return isReference() ? getRef().toString() : (
/* 393 */       (this.union == null) ? "" : this.union.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] translatePath(Project project, String source) {
/* 403 */     if (source == null) {
/* 404 */       return new String[0];
/*     */     }
/* 406 */     List<String> result = new ArrayList<>();
/* 407 */     PathTokenizer tok = new PathTokenizer(source);
/* 408 */     while (tok.hasMoreTokens()) {
/* 409 */       StringBuffer element = new StringBuffer();
/* 410 */       String pathElement = tok.nextToken();
/*     */       try {
/* 412 */         element.append(resolveFile(project, pathElement).getPath());
/* 413 */       } catch (BuildException e) {
/* 414 */         project.log("Dropping path element " + pathElement + " as it is not valid relative to the project", 3);
/*     */       } 
/*     */ 
/*     */       
/* 418 */       for (int i = 0; i < element.length(); i++) {
/* 419 */         translateFileSep(element, i);
/*     */       }
/* 421 */       result.add(element.toString());
/*     */     } 
/* 423 */     return result.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String translateFile(String source) {
/* 433 */     if (source == null) {
/* 434 */       return "";
/*     */     }
/* 436 */     StringBuffer result = new StringBuffer(source);
/* 437 */     for (int i = 0; i < result.length(); i++) {
/* 438 */       translateFileSep(result, i);
/*     */     }
/* 440 */     return result.toString();
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
/*     */   protected static boolean translateFileSep(StringBuffer buffer, int pos) {
/* 452 */     if (buffer.charAt(pos) == '/' || buffer.charAt(pos) == '\\') {
/* 453 */       buffer.setCharAt(pos, File.separatorChar);
/* 454 */       return true;
/*     */     } 
/* 456 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 465 */     if (isReference()) {
/* 466 */       return getRef().size();
/*     */     }
/* 468 */     dieOnCircularReference();
/* 469 */     return (this.union == null) ? 0 : assertFilesystemOnly((ResourceCollection)this.union).size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 479 */       Path result = (Path)super.clone();
/* 480 */       result.union = (this.union == null) ? this.union : (Union)this.union.clone();
/* 481 */       return result;
/* 482 */     } catch (CloneNotSupportedException e) {
/* 483 */       throw new BuildException(e);
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
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 497 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 500 */     if (isReference()) {
/* 501 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 503 */       if (this.union != null) {
/* 504 */         pushAndInvokeCircularReferenceCheck((DataType)this.union, stk, p);
/*     */       }
/* 506 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File resolveFile(Project project, String relativeName) {
/* 514 */     return FileUtils.getFileUtils().resolveFile(
/* 515 */         (project == null) ? null : project.getBaseDir(), relativeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path concatSystemClasspath() {
/* 525 */     return concatSystemClasspath("last");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path concatSystemClasspath(String defValue) {
/* 536 */     return concatSpecialPath(defValue, systemClasspath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path concatSystemBootClasspath(String defValue) {
/* 547 */     return concatSpecialPath(defValue, systemBootClasspath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path concatSpecialPath(String defValue, Path p) {
/* 556 */     Path result = new Path(getProject());
/*     */     
/* 558 */     String order = defValue;
/*     */ 
/*     */     
/* 561 */     String o = (getProject() != null) ? getProject().getProperty("build.sysclasspath") : System.getProperty("build.sysclasspath");
/* 562 */     if (o != null) {
/* 563 */       order = o;
/*     */     }
/* 565 */     if ("only".equals(order)) {
/*     */       
/* 567 */       result.addExisting(p, true);
/*     */     }
/* 569 */     else if ("first".equals(order)) {
/*     */       
/* 571 */       result.addExisting(p, true);
/* 572 */       result.addExisting(this);
/*     */     }
/* 574 */     else if ("ignore".equals(order)) {
/*     */       
/* 576 */       result.addExisting(this);
/*     */     }
/*     */     else {
/*     */       
/* 580 */       if (!"last".equals(order)) {
/* 581 */         log("invalid value for build.sysclasspath: " + order, 1);
/*     */       }
/*     */ 
/*     */       
/* 585 */       result.addExisting(this);
/* 586 */       result.addExisting(p, true);
/*     */     } 
/* 588 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addJavaRuntime() {
/* 595 */     if (JavaEnvUtils.isKaffe()) {
/*     */ 
/*     */       
/* 598 */       File kaffeShare = new File(JavaEnvUtils.getJavaHome() + File.separator + "share" + File.separator + "kaffe");
/*     */ 
/*     */       
/* 601 */       if (kaffeShare.isDirectory()) {
/* 602 */         FileSet kaffeJarFiles = new FileSet();
/* 603 */         kaffeJarFiles.setDir(kaffeShare);
/* 604 */         kaffeJarFiles.setIncludes("*.jar");
/* 605 */         addFileset(kaffeJarFiles);
/*     */       } 
/* 607 */     } else if ("GNU libgcj".equals(System.getProperty("java.vm.name"))) {
/* 608 */       addExisting(systemBootClasspath);
/*     */     } 
/*     */     
/* 611 */     if (System.getProperty("java.vendor").toLowerCase(Locale.ENGLISH).contains("microsoft")) {
/*     */ 
/*     */       
/* 614 */       FileSet msZipFiles = new FileSet();
/* 615 */       msZipFiles.setDir(new File(JavaEnvUtils.getJavaHome() + File.separator + "Packages"));
/*     */       
/* 617 */       msZipFiles.setIncludes("*.ZIP");
/* 618 */       addFileset(msZipFiles);
/*     */     } else {
/*     */       
/* 621 */       addExisting(new Path(null, JavaEnvUtils.getJavaHome() + File.separator + "lib" + File.separator + "rt.jar"));
/*     */ 
/*     */ 
/*     */       
/* 625 */       addExisting(new Path(null, JavaEnvUtils.getJavaHome() + File.separator + "jre" + File.separator + "lib" + File.separator + "rt.jar"));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 630 */       for (String secJar : Arrays.<String>asList(new String[] { "jce", "jsse" })) {
/* 631 */         addExisting(new Path(null, JavaEnvUtils.getJavaHome() + File.separator + "lib" + File.separator + secJar + ".jar"));
/*     */ 
/*     */         
/* 634 */         addExisting(new Path(null, JavaEnvUtils.getJavaHome() + File.separator + ".." + File.separator + "Classes" + File.separator + secJar + ".jar"));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 641 */       for (String ibmJar : Arrays.<String>asList(new String[] { "core", "graphics", "security", "server", "xml" })) {
/* 642 */         addExisting(new Path(null, JavaEnvUtils.getJavaHome() + File.separator + "lib" + File.separator + ibmJar + ".jar"));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 647 */       addExisting(new Path(null, JavaEnvUtils.getJavaHome() + File.separator + ".." + File.separator + "Classes" + File.separator + "classes.jar"));
/*     */ 
/*     */       
/* 650 */       addExisting(new Path(null, JavaEnvUtils.getJavaHome() + File.separator + ".." + File.separator + "Classes" + File.separator + "ui.jar"));
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
/*     */   public void addExtdirs(Path extdirs) {
/* 664 */     if (extdirs == null) {
/* 665 */       String extProp = System.getProperty("java.ext.dirs");
/* 666 */       if (extProp != null) {
/* 667 */         extdirs = new Path(getProject(), extProp);
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 673 */     for (String d : extdirs.list()) {
/* 674 */       File dir = resolveFile(getProject(), d);
/* 675 */       if (dir.exists() && dir.isDirectory()) {
/* 676 */         FileSet fs = new FileSet();
/* 677 */         fs.setDir(dir);
/* 678 */         fs.setIncludes("*");
/* 679 */         addFileset(fs);
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
/*     */   public final synchronized Iterator<Resource> iterator() {
/* 692 */     if (isReference()) {
/* 693 */       return getRef().iterator();
/*     */     }
/* 695 */     dieOnCircularReference();
/* 696 */     if (getPreserveBC()) {
/* 697 */       return (Iterator<Resource>)new FileResourceIterator(getProject(), null, list());
/*     */     }
/* 699 */     return (this.union == null) ? Collections.<Resource>emptySet().iterator() : 
/* 700 */       assertFilesystemOnly((ResourceCollection)this.union).iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isFilesystemOnly() {
/* 709 */     if (isReference()) {
/* 710 */       return getRef().isFilesystemOnly();
/*     */     }
/* 712 */     dieOnCircularReference();
/* 713 */     assertFilesystemOnly((ResourceCollection)this.union);
/* 714 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceCollection assertFilesystemOnly(ResourceCollection rc) {
/* 724 */     if (rc != null && !rc.isFilesystemOnly())
/* 725 */       throw new BuildException("%s allows only filesystem resources.", new Object[] {
/* 726 */             getDataTypeName()
/*     */           }); 
/* 728 */     return rc;
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
/*     */   protected boolean delegateIteratorToList() {
/* 740 */     if (getClass().equals(Path.class)) {
/* 741 */       return false;
/*     */     }
/*     */     try {
/* 744 */       Method listMethod = getClass().getMethod("list", new Class[0]);
/* 745 */       return !listMethod.getDeclaringClass().equals(Path.class);
/* 746 */     } catch (Exception e) {
/*     */       
/* 748 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized boolean getPreserveBC() {
/* 753 */     if (this.preserveBC == null) {
/* 754 */       this.preserveBC = delegateIteratorToList() ? Boolean.TRUE : Boolean.FALSE;
/*     */     }
/* 756 */     return this.preserveBC.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean containsWildcards(String path) {
/* 764 */     return (path != null && (path.contains("*") || path.contains("?")));
/*     */   }
/*     */   
/*     */   private Path getRef() {
/* 768 */     return getCheckedRef(Path.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */