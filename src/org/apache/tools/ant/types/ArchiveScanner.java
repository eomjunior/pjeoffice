/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.FileResourceIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ArchiveScanner
/*     */   extends DirectoryScanner
/*     */ {
/*     */   protected File srcFile;
/*     */   private Resource src;
/*     */   private Resource lastScannedResource;
/*  64 */   private Map<String, Resource> fileEntries = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private Map<String, Resource> dirEntries = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private Map<String, Resource> matchFileEntries = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private Map<String, Resource> matchDirEntries = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean errorOnMissingArchive = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorOnMissingArchive(boolean errorOnMissingArchive) {
/* 101 */     this.errorOnMissingArchive = errorOnMissingArchive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan() {
/* 109 */     if (this.src == null || (!this.src.isExists() && !this.errorOnMissingArchive)) {
/*     */       return;
/*     */     }
/* 112 */     super.scan();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File srcFile) {
/* 122 */     setSrc((Resource)new FileResource(srcFile));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(Resource src) {
/* 132 */     this.src = src;
/* 133 */     FileProvider fp = src.<FileProvider>as(FileProvider.class);
/* 134 */     if (fp != null) {
/* 135 */       this.srcFile = fp.getFile();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 145 */     this.encoding = encoding;
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
/*     */   public String[] getIncludedFiles() {
/* 157 */     if (this.src == null) {
/* 158 */       return super.getIncludedFiles();
/*     */     }
/* 160 */     scanme();
/* 161 */     return (String[])this.matchFileEntries.keySet().toArray((Object[])new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIncludedFilesCount() {
/* 170 */     if (this.src == null) {
/* 171 */       return super.getIncludedFilesCount();
/*     */     }
/* 173 */     scanme();
/* 174 */     return this.matchFileEntries.size();
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
/*     */   public String[] getIncludedDirectories() {
/* 186 */     if (this.src == null) {
/* 187 */       return super.getIncludedDirectories();
/*     */     }
/* 189 */     scanme();
/* 190 */     return (String[])this.matchDirEntries.keySet().toArray((Object[])new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIncludedDirsCount() {
/* 199 */     if (this.src == null) {
/* 200 */       return super.getIncludedDirsCount();
/*     */     }
/* 202 */     scanme();
/* 203 */     return this.matchDirEntries.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Resource> getResourceFiles(Project project) {
/* 213 */     if (this.src == null) {
/* 214 */       return (Iterator<Resource>)new FileResourceIterator(project, getBasedir(), getIncludedFiles());
/*     */     }
/* 216 */     scanme();
/* 217 */     return this.matchFileEntries.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Resource> getResourceDirectories(Project project) {
/* 227 */     if (this.src == null) {
/* 228 */       return (Iterator<Resource>)new FileResourceIterator(project, getBasedir(), getIncludedDirectories());
/*     */     }
/* 230 */     scanme();
/* 231 */     return this.matchDirEntries.values().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 238 */     if (this.includes == null) {
/*     */       
/* 240 */       this.includes = new String[1];
/* 241 */       this.includes[0] = "**";
/*     */     } 
/* 243 */     if (this.excludes == null) {
/* 244 */       this.excludes = new String[0];
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
/*     */   public boolean match(String path) {
/* 258 */     String vpath = path;
/* 259 */     if (!path.isEmpty()) {
/*     */       
/* 261 */       vpath = path.replace('/', File.separatorChar).replace('\\', File.separatorChar);
/* 262 */       if (vpath.charAt(0) == File.separatorChar) {
/* 263 */         vpath = vpath.substring(1);
/*     */       }
/*     */     } 
/* 266 */     return (isIncluded(vpath) && !isExcluded(vpath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(String name) {
/* 277 */     if (this.src == null) {
/* 278 */       return super.getResource(name);
/*     */     }
/* 280 */     if (name.isEmpty())
/*     */     {
/* 282 */       return new Resource("", true, Long.MAX_VALUE, true);
/*     */     }
/*     */     
/* 285 */     scanme();
/* 286 */     if (this.fileEntries.containsKey(name)) {
/* 287 */       return this.fileEntries.get(name);
/*     */     }
/* 289 */     name = trimSeparator(name);
/*     */     
/* 291 */     if (this.dirEntries.containsKey(name)) {
/* 292 */       return this.dirEntries.get(name);
/*     */     }
/* 294 */     return new Resource(name);
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
/*     */   protected abstract void fillMapsFromArchive(Resource paramResource, String paramString, Map<String, Resource> paramMap1, Map<String, Resource> paramMap2, Map<String, Resource> paramMap3, Map<String, Resource> paramMap4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void scanme() {
/* 327 */     if (!this.src.isExists() && !this.errorOnMissingArchive) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     Resource thisresource = new Resource(this.src.getName(), this.src.isExists(), this.src.getLastModified());
/*     */     
/* 336 */     if (this.lastScannedResource != null && this.lastScannedResource
/* 337 */       .getName().equals(thisresource.getName()) && this.lastScannedResource
/* 338 */       .getLastModified() == thisresource
/* 339 */       .getLastModified()) {
/*     */       return;
/*     */     }
/* 342 */     init();
/*     */     
/* 344 */     this.fileEntries.clear();
/* 345 */     this.dirEntries.clear();
/* 346 */     this.matchFileEntries.clear();
/* 347 */     this.matchDirEntries.clear();
/* 348 */     fillMapsFromArchive(this.src, this.encoding, this.fileEntries, this.matchFileEntries, this.dirEntries, this.matchDirEntries);
/*     */ 
/*     */ 
/*     */     
/* 352 */     this.lastScannedResource = thisresource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String trimSeparator(String s) {
/* 361 */     return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/ArchiveScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */