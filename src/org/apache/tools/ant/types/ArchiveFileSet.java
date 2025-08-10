/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Iterator;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.FileScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ArchiveFileSet
/*     */   extends FileSet
/*     */ {
/*     */   private static final int BASE_OCTAL = 8;
/*     */   public static final int DEFAULT_DIR_MODE = 16877;
/*     */   public static final int DEFAULT_FILE_MODE = 33188;
/*  61 */   private Resource src = null;
/*  62 */   private String prefix = "";
/*  63 */   private String fullpath = "";
/*     */   private boolean hasDir = false;
/*  65 */   private int fileMode = 33188;
/*  66 */   private int dirMode = 16877;
/*     */   
/*     */   private boolean fileModeHasBeenSet = false;
/*     */   
/*     */   private boolean dirModeHasBeenSet = false;
/*     */   
/*     */   private static final String ERROR_DIR_AND_SRC_ATTRIBUTES = "Cannot set both dir and src attributes";
/*     */   private static final String ERROR_PATH_AND_PREFIX = "Cannot set both fullpath and prefix attributes";
/*     */   private boolean errorOnMissingArchive = true;
/*  75 */   private String encoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveFileSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArchiveFileSet(FileSet fileset) {
/*  87 */     super(fileset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArchiveFileSet(ArchiveFileSet fileset) {
/*  95 */     super(fileset);
/*  96 */     this.src = fileset.src;
/*  97 */     this.prefix = fileset.prefix;
/*  98 */     this.fullpath = fileset.fullpath;
/*  99 */     this.hasDir = fileset.hasDir;
/* 100 */     this.fileMode = fileset.fileMode;
/* 101 */     this.dirMode = fileset.dirMode;
/* 102 */     this.fileModeHasBeenSet = fileset.fileModeHasBeenSet;
/* 103 */     this.dirModeHasBeenSet = fileset.dirModeHasBeenSet;
/* 104 */     this.errorOnMissingArchive = fileset.errorOnMissingArchive;
/* 105 */     this.encoding = fileset.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File dir) throws BuildException {
/* 115 */     checkAttributesAllowed();
/* 116 */     if (this.src != null) {
/* 117 */       throw new BuildException("Cannot set both dir and src attributes");
/*     */     }
/* 119 */     super.setDir(dir);
/* 120 */     this.hasDir = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection a) {
/* 129 */     checkChildrenAllowed();
/* 130 */     if (a.size() != 1) {
/* 131 */       throw new BuildException("only single argument resource collections are supported as archives");
/*     */     }
/*     */     
/* 134 */     setSrcResource(a.iterator().next());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File srcFile) {
/* 144 */     setSrcResource((Resource)new FileResource(srcFile));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcResource(Resource src) {
/* 154 */     checkArchiveAttributesAllowed();
/* 155 */     if (this.hasDir) {
/* 156 */       throw new BuildException("Cannot set both dir and src attributes");
/*     */     }
/* 158 */     this.src = src;
/* 159 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getSrc(Project p) {
/* 168 */     if (isReference()) {
/* 169 */       return ((ArchiveFileSet)getRef(p)).getSrc(p);
/*     */     }
/* 171 */     return getSrc();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorOnMissingArchive(boolean errorOnMissingArchive) {
/* 182 */     checkAttributesAllowed();
/* 183 */     this.errorOnMissingArchive = errorOnMissingArchive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getSrc() {
/* 191 */     if (isReference()) {
/* 192 */       return ((ArchiveFileSet)getCheckedRef(ArchiveFileSet.class)).getSrc();
/*     */     }
/* 194 */     dieOnCircularReference();
/* 195 */     if (this.src == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     return this.src.<FileProvider>asOptional(FileProvider.class).map(FileProvider::getFile).orElse(null);
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
/*     */   protected AbstractFileSet getRef() {
/* 214 */     return getCheckedRef(AbstractFileSet.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 224 */     checkArchiveAttributesAllowed();
/* 225 */     if (!prefix.isEmpty() && !this.fullpath.isEmpty()) {
/* 226 */       throw new BuildException("Cannot set both fullpath and prefix attributes");
/*     */     }
/* 228 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix(Project p) {
/* 237 */     if (isReference()) {
/* 238 */       return ((ArchiveFileSet)getRef(p)).getPrefix(p);
/*     */     }
/* 240 */     dieOnCircularReference(p);
/* 241 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFullpath(String fullpath) {
/* 251 */     checkArchiveAttributesAllowed();
/* 252 */     if (!this.prefix.isEmpty() && !fullpath.isEmpty()) {
/* 253 */       throw new BuildException("Cannot set both fullpath and prefix attributes");
/*     */     }
/* 255 */     this.fullpath = fullpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFullpath(Project p) {
/* 264 */     if (isReference()) {
/* 265 */       return ((ArchiveFileSet)getRef(p)).getFullpath(p);
/*     */     }
/* 267 */     dieOnCircularReference(p);
/* 268 */     return this.fullpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String enc) {
/* 277 */     checkAttributesAllowed();
/* 278 */     this.encoding = enc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 287 */     if (isReference()) {
/* 288 */       AbstractFileSet ref = getRef();
/* 289 */       return (ref instanceof ArchiveFileSet) ? ((ArchiveFileSet)ref).getEncoding() : null;
/*     */     } 
/* 291 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ArchiveScanner newArchiveScanner();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryScanner getDirectoryScanner(Project p) {
/* 309 */     if (isReference()) {
/* 310 */       return getRef(p).getDirectoryScanner(p);
/*     */     }
/* 312 */     dieOnCircularReference();
/* 313 */     if (this.src == null) {
/* 314 */       return super.getDirectoryScanner(p);
/*     */     }
/* 316 */     if (!this.src.isExists() && this.errorOnMissingArchive) {
/* 317 */       throw new BuildException("The archive " + this.src
/* 318 */           .getName() + " doesn't exist");
/*     */     }
/* 320 */     if (this.src.isDirectory()) {
/* 321 */       throw new BuildException("The archive " + this.src.getName() + " can't be a directory");
/*     */     }
/*     */     
/* 324 */     ArchiveScanner as = newArchiveScanner();
/* 325 */     as.setErrorOnMissingArchive(this.errorOnMissingArchive);
/* 326 */     as.setSrc(this.src);
/* 327 */     super.setDir(p.getBaseDir());
/* 328 */     setupDirectoryScanner((FileScanner)as, p);
/* 329 */     as.init();
/* 330 */     return as;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/* 340 */     if (isReference()) {
/* 341 */       return ((ResourceCollection)getRef()).iterator();
/*     */     }
/* 343 */     if (this.src == null) {
/* 344 */       return super.iterator();
/*     */     }
/* 346 */     return ((ArchiveScanner)getDirectoryScanner()).getResourceFiles(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 356 */     if (isReference()) {
/* 357 */       return ((ResourceCollection)getRef()).size();
/*     */     }
/* 359 */     if (this.src == null) {
/* 360 */       return super.size();
/*     */     }
/* 362 */     return getDirectoryScanner().getIncludedFilesCount();
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
/*     */   public boolean isFilesystemOnly() {
/* 375 */     if (isReference()) {
/* 376 */       return ((ArchiveFileSet)getRef()).isFilesystemOnly();
/*     */     }
/* 378 */     dieOnCircularReference();
/* 379 */     return (this.src == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileMode(String octalString) {
/* 389 */     checkArchiveAttributesAllowed();
/* 390 */     integerSetFileMode(Integer.parseInt(octalString, 8));
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
/*     */   public void integerSetFileMode(int mode) {
/* 404 */     this.fileModeHasBeenSet = true;
/* 405 */     this.fileMode = 0x8000 | mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFileMode(Project p) {
/* 414 */     if (isReference()) {
/* 415 */       return ((ArchiveFileSet)getRef(p)).getFileMode(p);
/*     */     }
/* 417 */     dieOnCircularReference();
/* 418 */     return this.fileMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasFileModeBeenSet() {
/* 426 */     if (isReference()) {
/* 427 */       return ((ArchiveFileSet)getRef()).hasFileModeBeenSet();
/*     */     }
/* 429 */     dieOnCircularReference();
/* 430 */     return this.fileModeHasBeenSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirMode(String octalString) {
/* 440 */     checkArchiveAttributesAllowed();
/* 441 */     integerSetDirMode(Integer.parseInt(octalString, 8));
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
/*     */   public void integerSetDirMode(int mode) {
/* 454 */     this.dirModeHasBeenSet = true;
/* 455 */     this.dirMode = 0x4000 | mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDirMode(Project p) {
/* 464 */     if (isReference()) {
/* 465 */       return ((ArchiveFileSet)getRef(p)).getDirMode(p);
/*     */     }
/* 467 */     dieOnCircularReference();
/* 468 */     return this.dirMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDirModeBeenSet() {
/* 477 */     if (isReference()) {
/* 478 */       return ((ArchiveFileSet)getRef()).hasDirModeBeenSet();
/*     */     }
/* 480 */     dieOnCircularReference();
/* 481 */     return this.dirModeHasBeenSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureFileSet(ArchiveFileSet zfs) {
/* 490 */     zfs.setPrefix(this.prefix);
/* 491 */     zfs.setFullpath(this.fullpath);
/* 492 */     zfs.fileModeHasBeenSet = this.fileModeHasBeenSet;
/* 493 */     zfs.fileMode = this.fileMode;
/* 494 */     zfs.dirModeHasBeenSet = this.dirModeHasBeenSet;
/* 495 */     zfs.dirMode = this.dirMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 506 */     if (isReference()) {
/* 507 */       return ((ArchiveFileSet)getCheckedRef(ArchiveFileSet.class)).clone();
/*     */     }
/* 509 */     return super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 520 */     if (this.hasDir && getProject() != null) {
/* 521 */       return super.toString();
/*     */     }
/* 523 */     return (this.src == null) ? null : this.src.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getPrefix() {
/* 533 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getFullpath() {
/* 543 */     return this.fullpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getFileMode() {
/* 552 */     return this.fileMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getDirMode() {
/* 561 */     return this.dirMode;
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
/*     */   private void checkArchiveAttributesAllowed() {
/* 573 */     if (getProject() == null || (
/* 574 */       isReference() && 
/* 575 */       getRefid().getReferencedObject(
/* 576 */         getProject()) instanceof ArchiveFileSet))
/*     */     {
/* 578 */       checkAttributesAllowed();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 585 */     if (isChecked()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 590 */     super.dieOnCircularReference(stk, p);
/*     */     
/* 592 */     if (!isReference()) {
/* 593 */       if (this.src != null) {
/* 594 */         pushAndInvokeCircularReferenceCheck(this.src, stk, p);
/*     */       }
/* 596 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/ArchiveFileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */