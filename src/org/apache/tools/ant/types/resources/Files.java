/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.selectors.AbstractSelectorContainer;
/*     */ import org.apache.tools.ant.types.selectors.FileSelector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Files
/*     */   extends AbstractSelectorContainer
/*     */   implements ResourceCollection
/*     */ {
/*  43 */   private PatternSet defaultPatterns = new PatternSet();
/*  44 */   private Vector<PatternSet> additionalPatterns = new Vector<>();
/*     */   
/*     */   private boolean useDefaultExcludes = true;
/*     */   
/*     */   private boolean caseSensitive = true;
/*     */   
/*     */   private boolean followSymlinks = true;
/*  51 */   private DirectoryScanner ds = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Files(Files f) {
/*  66 */     this.defaultPatterns = f.defaultPatterns;
/*  67 */     this.additionalPatterns = f.additionalPatterns;
/*  68 */     this.useDefaultExcludes = f.useDefaultExcludes;
/*  69 */     this.caseSensitive = f.caseSensitive;
/*  70 */     this.followSymlinks = f.followSymlinks;
/*  71 */     this.ds = f.ds;
/*  72 */     setProject(f.getProject());
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
/*  85 */     if (hasPatterns(this.defaultPatterns)) {
/*  86 */       throw tooManyAttributes();
/*     */     }
/*  88 */     if (!this.additionalPatterns.isEmpty()) {
/*  89 */       throw noChildrenAllowed();
/*     */     }
/*  91 */     if (hasSelectors()) {
/*  92 */       throw noChildrenAllowed();
/*     */     }
/*  94 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet createPatternSet() {
/* 102 */     if (isReference()) {
/* 103 */       throw noChildrenAllowed();
/*     */     }
/* 105 */     PatternSet patterns = new PatternSet();
/* 106 */     this.additionalPatterns.addElement(patterns);
/* 107 */     this.ds = null;
/* 108 */     setChecked(false);
/* 109 */     return patterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createInclude() {
/* 117 */     if (isReference()) {
/* 118 */       throw noChildrenAllowed();
/*     */     }
/* 120 */     this.ds = null;
/* 121 */     return this.defaultPatterns.createInclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createIncludesFile() {
/* 129 */     if (isReference()) {
/* 130 */       throw noChildrenAllowed();
/*     */     }
/* 132 */     this.ds = null;
/* 133 */     return this.defaultPatterns.createIncludesFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createExclude() {
/* 141 */     if (isReference()) {
/* 142 */       throw noChildrenAllowed();
/*     */     }
/* 144 */     this.ds = null;
/* 145 */     return this.defaultPatterns.createExclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createExcludesFile() {
/* 153 */     if (isReference()) {
/* 154 */       throw noChildrenAllowed();
/*     */     }
/* 156 */     this.ds = null;
/* 157 */     return this.defaultPatterns.createExcludesFile();
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
/*     */   public synchronized void setIncludes(String includes) {
/* 169 */     checkAttributesAllowed();
/* 170 */     this.defaultPatterns.setIncludes(includes);
/* 171 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void appendIncludes(String[] includes) {
/* 181 */     checkAttributesAllowed();
/* 182 */     if (includes != null) {
/* 183 */       for (String include : includes) {
/* 184 */         this.defaultPatterns.createInclude().setName(include);
/*     */       }
/* 186 */       this.ds = null;
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
/*     */   public synchronized void setExcludes(String excludes) {
/* 199 */     checkAttributesAllowed();
/* 200 */     this.defaultPatterns.setExcludes(excludes);
/* 201 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void appendExcludes(String[] excludes) {
/* 211 */     checkAttributesAllowed();
/* 212 */     if (excludes != null) {
/* 213 */       for (String exclude : excludes) {
/* 214 */         this.defaultPatterns.createExclude().setName(exclude);
/*     */       }
/* 216 */       this.ds = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setIncludesfile(File incl) throws BuildException {
/* 227 */     checkAttributesAllowed();
/* 228 */     this.defaultPatterns.setIncludesfile(incl);
/* 229 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setExcludesfile(File excl) throws BuildException {
/* 239 */     checkAttributesAllowed();
/* 240 */     this.defaultPatterns.setExcludesfile(excl);
/* 241 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
/* 250 */     checkAttributesAllowed();
/* 251 */     this.useDefaultExcludes = useDefaultExcludes;
/* 252 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean getDefaultexcludes() {
/* 260 */     return isReference() ? 
/* 261 */       getRef().getDefaultexcludes() : this.useDefaultExcludes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCaseSensitive(boolean caseSensitive) {
/* 270 */     checkAttributesAllowed();
/* 271 */     this.caseSensitive = caseSensitive;
/* 272 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isCaseSensitive() {
/* 282 */     return isReference() ? 
/* 283 */       getRef().isCaseSensitive() : this.caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setFollowSymlinks(boolean followSymlinks) {
/* 292 */     checkAttributesAllowed();
/* 293 */     this.followSymlinks = followSymlinks;
/* 294 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isFollowSymlinks() {
/* 304 */     return isReference() ? 
/* 305 */       getRef().isFollowSymlinks() : this.followSymlinks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Iterator<Resource> iterator() {
/* 314 */     if (isReference()) {
/* 315 */       return getRef().iterator();
/*     */     }
/* 317 */     ensureDirectoryScannerSetup();
/* 318 */     this.ds.scan();
/* 319 */     int fct = this.ds.getIncludedFilesCount();
/* 320 */     int dct = this.ds.getIncludedDirsCount();
/* 321 */     if (fct + dct == 0) {
/* 322 */       return Collections.emptyIterator();
/*     */     }
/* 324 */     FileResourceIterator result = new FileResourceIterator(getProject());
/* 325 */     if (fct > 0) {
/* 326 */       result.addFiles(this.ds.getIncludedFiles());
/*     */     }
/* 328 */     if (dct > 0) {
/* 329 */       result.addFiles(this.ds.getIncludedDirectories());
/*     */     }
/* 331 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 340 */     if (isReference()) {
/* 341 */       return getRef().size();
/*     */     }
/* 343 */     ensureDirectoryScannerSetup();
/* 344 */     this.ds.scan();
/* 345 */     return this.ds.getIncludedFilesCount() + this.ds.getIncludedDirsCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean hasPatterns() {
/* 354 */     if (isReference()) {
/* 355 */       return getRef().hasPatterns();
/*     */     }
/* 357 */     dieOnCircularReference();
/* 358 */     return (hasPatterns(this.defaultPatterns) || this.additionalPatterns
/* 359 */       .stream().anyMatch(this::hasPatterns));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void appendSelector(FileSelector selector) {
/* 369 */     if (isReference()) {
/* 370 */       throw noChildrenAllowed();
/*     */     }
/* 372 */     super.appendSelector(selector);
/* 373 */     this.ds = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 382 */     if (isReference()) {
/* 383 */       return getRef().toString();
/*     */     }
/* 385 */     return isEmpty() ? "" : stream().map(Object::toString)
/* 386 */       .collect(Collectors.joining(File.pathSeparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object clone() {
/* 396 */     if (isReference()) {
/* 397 */       return getRef().clone();
/*     */     }
/* 399 */     Files f = (Files)super.clone();
/* 400 */     f.defaultPatterns = (PatternSet)this.defaultPatterns.clone();
/* 401 */     f.additionalPatterns = new Vector<>(this.additionalPatterns.size());
/* 402 */     for (PatternSet ps : this.additionalPatterns) {
/* 403 */       f.additionalPatterns.add((PatternSet)ps.clone());
/*     */     }
/* 405 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] mergeIncludes(Project p) {
/* 415 */     return mergePatterns(p).getIncludePatterns(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] mergeExcludes(Project p) {
/* 425 */     return mergePatterns(p).getExcludePatterns(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet mergePatterns(Project p) {
/* 435 */     if (isReference()) {
/* 436 */       return getRef().mergePatterns(p);
/*     */     }
/* 438 */     dieOnCircularReference();
/* 439 */     PatternSet ps = new PatternSet();
/* 440 */     ps.append(this.defaultPatterns, p);
/* 441 */     this.additionalPatterns.forEach(pat -> ps.append(pat, p));
/* 442 */     return ps;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 452 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Files getRef() {
/* 461 */     return (Files)getCheckedRef(Files.class);
/*     */   }
/*     */   
/*     */   private synchronized void ensureDirectoryScannerSetup() {
/* 465 */     dieOnCircularReference();
/* 466 */     if (this.ds == null) {
/* 467 */       this.ds = new DirectoryScanner();
/* 468 */       PatternSet ps = mergePatterns(getProject());
/* 469 */       this.ds.setIncludes(ps.getIncludePatterns(getProject()));
/* 470 */       this.ds.setExcludes(ps.getExcludePatterns(getProject()));
/* 471 */       this.ds.setSelectors(getSelectors(getProject()));
/* 472 */       if (this.useDefaultExcludes) {
/* 473 */         this.ds.addDefaultExcludes();
/*     */       }
/* 475 */       this.ds.setCaseSensitive(this.caseSensitive);
/* 476 */       this.ds.setFollowSymlinks(this.followSymlinks);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasPatterns(PatternSet ps) {
/* 481 */     String[] includePatterns = ps.getIncludePatterns(getProject());
/* 482 */     String[] excludePatterns = ps.getExcludePatterns(getProject());
/* 483 */     return ((includePatterns != null && includePatterns.length > 0) || (excludePatterns != null && excludePatterns.length > 0));
/*     */   }
/*     */   
/*     */   public Files() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Files.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */