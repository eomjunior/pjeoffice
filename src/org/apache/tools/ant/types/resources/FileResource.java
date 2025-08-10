/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceFactory;
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
/*     */ public class FileResource
/*     */   extends Resource
/*     */   implements Touchable, FileProvider, ResourceFactory, Appendable
/*     */ {
/*  40 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  42 */   private static final int NULL_FILE = Resource.getMagicNumber("null file".getBytes());
/*     */ 
/*     */ 
/*     */   
/*     */   private File file;
/*     */ 
/*     */ 
/*     */   
/*     */   private File baseDir;
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResource() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResource(File b, String name) {
/*  59 */     this.baseDir = b;
/*  60 */     this.file = FILE_UTILS.resolveFile(b, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResource(File f) {
/*  68 */     setFile(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResource(Project p, File f) {
/*  78 */     this(f);
/*  79 */     setProject(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResource(Project p, String s) {
/*  89 */     this(p, p.resolveFile(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File f) {
/*  97 */     checkAttributesAllowed();
/*  98 */     this.file = f;
/*  99 */     if (f != null && (getBaseDir() == null || !FILE_UTILS.isLeadingPath(getBaseDir(), f))) {
/* 100 */       setBaseDir(f.getParentFile());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 110 */     if (isReference()) {
/* 111 */       return getRef().getFile();
/*     */     }
/* 113 */     dieOnCircularReference();
/* 114 */     synchronized (this) {
/* 115 */       if (this.file == null) {
/*     */         
/* 117 */         File d = getBaseDir();
/* 118 */         String n = super.getName();
/* 119 */         if (n != null) {
/* 120 */           setFile(FILE_UTILS.resolveFile(d, n));
/*     */         }
/*     */       } 
/*     */     } 
/* 124 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaseDir(File b) {
/* 132 */     checkAttributesAllowed();
/* 133 */     this.baseDir = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getBaseDir() {
/* 141 */     if (isReference()) {
/* 142 */       return getRef().getBaseDir();
/*     */     }
/* 144 */     dieOnCircularReference();
/* 145 */     return this.baseDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 154 */     if (this.file != null || this.baseDir != null) {
/* 155 */       throw tooManyAttributes();
/*     */     }
/* 157 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 168 */     if (isReference()) {
/* 169 */       return getRef().getName();
/*     */     }
/* 171 */     File b = getBaseDir();
/* 172 */     return (b == null) ? getNotNullFile().getName() : 
/* 173 */       FILE_UTILS.removeLeadingPath(b, getNotNullFile());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/* 182 */     return isReference() ? getRef().isExists() : 
/* 183 */       getNotNullFile().exists();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 192 */     return isReference() ? 
/* 193 */       getRef().getLastModified() : 
/* 194 */       getNotNullFile().lastModified();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 203 */     return isReference() ? getRef().isDirectory() : 
/* 204 */       getNotNullFile().isDirectory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 213 */     return isReference() ? getRef().getSize() : 
/* 214 */       getNotNullFile().length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 224 */     return isReference() ? getRef().getInputStream() : 
/* 225 */       Files.newInputStream(getNotNullFile().toPath(), new java.nio.file.OpenOption[0]);
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
/*     */   public OutputStream getOutputStream() throws IOException {
/* 238 */     if (isReference()) {
/* 239 */       return getRef().getOutputStream();
/*     */     }
/* 241 */     return getOutputStream(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getAppendOutputStream() throws IOException {
/* 249 */     if (isReference()) {
/* 250 */       return getRef().getAppendOutputStream();
/*     */     }
/* 252 */     return getOutputStream(true);
/*     */   }
/*     */   
/*     */   private OutputStream getOutputStream(boolean append) throws IOException {
/* 256 */     File f = getNotNullFile();
/* 257 */     if (f.exists()) {
/* 258 */       if (Files.isSymbolicLink(f.toPath()) && f.isFile() && !append)
/*     */       {
/* 260 */         f.delete();
/*     */       }
/*     */     } else {
/* 263 */       File p = f.getParentFile();
/* 264 */       if (p != null && !p.exists()) {
/* 265 */         p.mkdirs();
/*     */       }
/*     */     } 
/* 268 */     return FileUtils.newOutputStream(f.toPath(), append);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Resource another) {
/* 279 */     if (isReference()) {
/* 280 */       return getRef().compareTo(another);
/*     */     }
/* 282 */     if (equals(another)) {
/* 283 */       return 0;
/*     */     }
/* 285 */     FileProvider otherFP = (FileProvider)another.as(FileProvider.class);
/* 286 */     if (otherFP != null) {
/* 287 */       File f = getFile();
/* 288 */       if (f == null) {
/* 289 */         return -1;
/*     */       }
/* 291 */       File of = otherFP.getFile();
/* 292 */       if (of == null) {
/* 293 */         return 1;
/*     */       }
/* 295 */       int compareFiles = f.compareTo(of);
/* 296 */       return (compareFiles != 0) ? compareFiles : 
/* 297 */         getName().compareTo(another.getName());
/*     */     } 
/* 299 */     return super.compareTo(another);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object another) {
/* 309 */     if (this == another) {
/* 310 */       return true;
/*     */     }
/* 312 */     if (isReference()) {
/* 313 */       return getRef().equals(another);
/*     */     }
/* 315 */     if (another == null || !another.getClass().equals(getClass())) {
/* 316 */       return false;
/*     */     }
/* 318 */     FileResource otherfr = (FileResource)another;
/* 319 */     return (getFile() == null) ? (
/* 320 */       (otherfr.getFile() == null)) : (
/* 321 */       (getFile().equals(otherfr.getFile()) && getName().equals(otherfr.getName())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 330 */     if (isReference()) {
/* 331 */       return getRef().hashCode();
/*     */     }
/* 333 */     return MAGIC * ((getFile() == null) ? NULL_FILE : getFile().hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 342 */     if (isReference()) {
/* 343 */       return getRef().toString();
/*     */     }
/* 345 */     if (this.file == null) {
/* 346 */       return "(unbound file resource)";
/*     */     }
/* 348 */     String absolutePath = this.file.getAbsolutePath();
/* 349 */     return FILE_UTILS.normalize(absolutePath).getAbsolutePath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 358 */     if (isReference()) {
/* 359 */       return getRef().isFilesystemOnly();
/*     */     }
/* 361 */     dieOnCircularReference();
/* 362 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void touch(long modTime) {
/* 371 */     if (isReference()) {
/* 372 */       getRef().touch(modTime);
/*     */       return;
/*     */     } 
/* 375 */     if (!getNotNullFile().setLastModified(modTime)) {
/* 376 */       log("Failed to change file modification time", 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getNotNullFile() {
/* 386 */     if (getFile() == null) {
/* 387 */       throw new BuildException("file attribute is null!");
/*     */     }
/* 389 */     dieOnCircularReference();
/* 390 */     return getFile();
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
/*     */   public Resource getResource(String path) {
/* 403 */     File newfile = FILE_UTILS.resolveFile(getFile(), path);
/* 404 */     FileResource fileResource = new FileResource(newfile);
/* 405 */     if (FILE_UTILS.isLeadingPath(getBaseDir(), newfile)) {
/* 406 */       fileResource.setBaseDir(getBaseDir());
/*     */     }
/* 408 */     return fileResource;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FileResource getRef() {
/* 413 */     return (FileResource)getCheckedRef(FileResource.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/FileResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */