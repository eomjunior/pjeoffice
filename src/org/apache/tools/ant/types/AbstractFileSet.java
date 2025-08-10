/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Stack;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.FileScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.selectors.AndSelector;
/*     */ import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
/*     */ import org.apache.tools.ant.types.selectors.ContainsSelector;
/*     */ import org.apache.tools.ant.types.selectors.DateSelector;
/*     */ import org.apache.tools.ant.types.selectors.DependSelector;
/*     */ import org.apache.tools.ant.types.selectors.DepthSelector;
/*     */ import org.apache.tools.ant.types.selectors.DifferentSelector;
/*     */ import org.apache.tools.ant.types.selectors.ExecutableSelector;
/*     */ import org.apache.tools.ant.types.selectors.ExtendSelector;
/*     */ import org.apache.tools.ant.types.selectors.FileSelector;
/*     */ import org.apache.tools.ant.types.selectors.FilenameSelector;
/*     */ import org.apache.tools.ant.types.selectors.MajoritySelector;
/*     */ import org.apache.tools.ant.types.selectors.NoneSelector;
/*     */ import org.apache.tools.ant.types.selectors.NotSelector;
/*     */ import org.apache.tools.ant.types.selectors.OrSelector;
/*     */ import org.apache.tools.ant.types.selectors.OwnedBySelector;
/*     */ import org.apache.tools.ant.types.selectors.PosixGroupSelector;
/*     */ import org.apache.tools.ant.types.selectors.PosixPermissionsSelector;
/*     */ import org.apache.tools.ant.types.selectors.PresentSelector;
/*     */ import org.apache.tools.ant.types.selectors.ReadableSelector;
/*     */ import org.apache.tools.ant.types.selectors.SelectSelector;
/*     */ import org.apache.tools.ant.types.selectors.SelectorContainer;
/*     */ import org.apache.tools.ant.types.selectors.SelectorScanner;
/*     */ import org.apache.tools.ant.types.selectors.SizeSelector;
/*     */ import org.apache.tools.ant.types.selectors.SymlinkSelector;
/*     */ import org.apache.tools.ant.types.selectors.TypeSelector;
/*     */ import org.apache.tools.ant.types.selectors.WritableSelector;
/*     */ import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFileSet
/*     */   extends DataType
/*     */   implements Cloneable, SelectorContainer
/*     */ {
/*  71 */   private PatternSet defaultPatterns = new PatternSet();
/*  72 */   private List<PatternSet> additionalPatterns = new ArrayList<>();
/*  73 */   private List<FileSelector> selectors = new ArrayList<>();
/*     */   
/*     */   private File dir;
/*     */   private boolean fileAttributeUsed;
/*     */   private boolean useDefaultExcludes = true;
/*     */   private boolean caseSensitive = true;
/*     */   private boolean followSymlinks = true;
/*     */   private boolean errorOnMissingDir = true;
/*  81 */   private int maxLevelsOfSymlinks = 5;
/*     */ 
/*     */   
/*  84 */   private DirectoryScanner directoryScanner = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFileSet(AbstractFileSet fileset) {
/*  99 */     this.dir = fileset.dir;
/* 100 */     this.defaultPatterns = fileset.defaultPatterns;
/* 101 */     this.additionalPatterns = fileset.additionalPatterns;
/* 102 */     this.selectors = fileset.selectors;
/* 103 */     this.useDefaultExcludes = fileset.useDefaultExcludes;
/* 104 */     this.caseSensitive = fileset.caseSensitive;
/* 105 */     this.followSymlinks = fileset.followSymlinks;
/* 106 */     this.errorOnMissingDir = fileset.errorOnMissingDir;
/* 107 */     this.maxLevelsOfSymlinks = fileset.maxLevelsOfSymlinks;
/* 108 */     setProject(fileset.getProject());
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
/* 121 */     if (this.dir != null || this.defaultPatterns.hasPatterns(getProject())) {
/* 122 */       throw tooManyAttributes();
/*     */     }
/* 124 */     if (!this.additionalPatterns.isEmpty()) {
/* 125 */       throw noChildrenAllowed();
/*     */     }
/* 127 */     if (!this.selectors.isEmpty()) {
/* 128 */       throw noChildrenAllowed();
/*     */     }
/* 130 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setDir(File dir) throws BuildException {
/* 139 */     if (isReference()) {
/* 140 */       throw tooManyAttributes();
/*     */     }
/* 142 */     if (this.fileAttributeUsed && !getDir().equals(dir)) {
/* 143 */       throw dirAndFileAreMutuallyExclusive();
/*     */     }
/* 145 */     this.dir = dir;
/* 146 */     this.directoryScanner = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDir() {
/* 154 */     return getDir(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized File getDir(Project p) {
/* 164 */     if (isReference()) {
/* 165 */       return getRef(p).getDir(p);
/*     */     }
/* 167 */     dieOnCircularReference();
/* 168 */     return this.dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet createPatternSet() {
/* 176 */     if (isReference()) {
/* 177 */       throw noChildrenAllowed();
/*     */     }
/* 179 */     PatternSet patterns = new PatternSet();
/* 180 */     this.additionalPatterns.add(patterns);
/* 181 */     this.directoryScanner = null;
/* 182 */     return patterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createInclude() {
/* 190 */     if (isReference()) {
/* 191 */       throw noChildrenAllowed();
/*     */     }
/* 193 */     this.directoryScanner = null;
/* 194 */     return this.defaultPatterns.createInclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createIncludesFile() {
/* 202 */     if (isReference()) {
/* 203 */       throw noChildrenAllowed();
/*     */     }
/* 205 */     this.directoryScanner = null;
/* 206 */     return this.defaultPatterns.createIncludesFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createExclude() {
/* 214 */     if (isReference()) {
/* 215 */       throw noChildrenAllowed();
/*     */     }
/* 217 */     this.directoryScanner = null;
/* 218 */     return this.defaultPatterns.createExclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PatternSet.NameEntry createExcludesFile() {
/* 226 */     if (isReference()) {
/* 227 */       throw noChildrenAllowed();
/*     */     }
/* 229 */     this.directoryScanner = null;
/* 230 */     return this.defaultPatterns.createExcludesFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setFile(File file) {
/* 239 */     if (isReference()) {
/* 240 */       throw tooManyAttributes();
/*     */     }
/* 242 */     if (this.fileAttributeUsed) {
/* 243 */       if (getDir().equals(file.getParentFile())) {
/* 244 */         String[] includes = this.defaultPatterns.getIncludePatterns(getProject());
/* 245 */         if (includes.length == 1 && includes[0].equals(file.getName())) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */       
/* 250 */       throw new BuildException("setFile cannot be called twice with different arguments");
/* 251 */     }  if (getDir() != null) {
/* 252 */       throw dirAndFileAreMutuallyExclusive();
/*     */     }
/* 254 */     setDir(file.getParentFile());
/* 255 */     this.fileAttributeUsed = true;
/* 256 */     createInclude().setName(file.getName());
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
/* 268 */     if (isReference()) {
/* 269 */       throw tooManyAttributes();
/*     */     }
/* 271 */     this.defaultPatterns.setIncludes(includes);
/* 272 */     this.directoryScanner = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void appendIncludes(String[] includes) {
/* 283 */     if (isReference()) {
/* 284 */       throw tooManyAttributes();
/*     */     }
/* 286 */     if (includes != null) {
/* 287 */       for (String include : includes) {
/* 288 */         this.defaultPatterns.createInclude().setName(include);
/*     */       }
/* 290 */       this.directoryScanner = null;
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
/* 303 */     if (isReference()) {
/* 304 */       throw tooManyAttributes();
/*     */     }
/* 306 */     this.defaultPatterns.setExcludes(excludes);
/* 307 */     this.directoryScanner = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void appendExcludes(String[] excludes) {
/* 318 */     if (isReference()) {
/* 319 */       throw tooManyAttributes();
/*     */     }
/* 321 */     if (excludes != null) {
/* 322 */       for (String exclude : excludes) {
/* 323 */         this.defaultPatterns.createExclude().setName(exclude);
/*     */       }
/* 325 */       this.directoryScanner = null;
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
/* 336 */     if (isReference()) {
/* 337 */       throw tooManyAttributes();
/*     */     }
/* 339 */     this.defaultPatterns.setIncludesfile(incl);
/* 340 */     this.directoryScanner = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setExcludesfile(File excl) throws BuildException {
/* 350 */     if (isReference()) {
/* 351 */       throw tooManyAttributes();
/*     */     }
/* 353 */     this.defaultPatterns.setExcludesfile(excl);
/* 354 */     this.directoryScanner = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
/* 363 */     if (isReference()) {
/* 364 */       throw tooManyAttributes();
/*     */     }
/* 366 */     this.useDefaultExcludes = useDefaultExcludes;
/* 367 */     this.directoryScanner = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean getDefaultexcludes() {
/* 376 */     if (isReference()) {
/* 377 */       return getRef(getProject()).getDefaultexcludes();
/*     */     }
/* 379 */     dieOnCircularReference();
/* 380 */     return this.useDefaultExcludes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCaseSensitive(boolean caseSensitive) {
/* 389 */     if (isReference()) {
/* 390 */       throw tooManyAttributes();
/*     */     }
/* 392 */     this.caseSensitive = caseSensitive;
/* 393 */     this.directoryScanner = null;
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
/*     */   public synchronized boolean isCaseSensitive() {
/* 405 */     if (isReference()) {
/* 406 */       return getRef(getProject()).isCaseSensitive();
/*     */     }
/* 408 */     dieOnCircularReference();
/* 409 */     return this.caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setFollowSymlinks(boolean followSymlinks) {
/* 418 */     if (isReference()) {
/* 419 */       throw tooManyAttributes();
/*     */     }
/* 421 */     this.followSymlinks = followSymlinks;
/* 422 */     this.directoryScanner = null;
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
/*     */   public synchronized boolean isFollowSymlinks() {
/* 434 */     if (isReference()) {
/* 435 */       return getRef(getProject()).isCaseSensitive();
/*     */     }
/* 437 */     dieOnCircularReference();
/* 438 */     return this.followSymlinks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLevelsOfSymlinks(int max) {
/* 449 */     this.maxLevelsOfSymlinks = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLevelsOfSymlinks() {
/* 460 */     return this.maxLevelsOfSymlinks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorOnMissingDir(boolean errorOnMissingDir) {
/* 470 */     this.errorOnMissingDir = errorOnMissingDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getErrorOnMissingDir() {
/* 481 */     return this.errorOnMissingDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryScanner getDirectoryScanner() {
/* 489 */     return getDirectoryScanner(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryScanner getDirectoryScanner(Project p) {
/*     */     DirectoryScanner ds;
/* 498 */     if (isReference()) {
/* 499 */       return getRef(p).getDirectoryScanner(p);
/*     */     }
/* 501 */     dieOnCircularReference();
/*     */     
/* 503 */     synchronized (this) {
/* 504 */       if (this.directoryScanner != null && p == getProject()) {
/* 505 */         ds = this.directoryScanner;
/*     */       } else {
/* 507 */         if (this.dir == null)
/* 508 */           throw new BuildException("No directory specified for %s.", new Object[] {
/* 509 */                 getDataTypeName()
/*     */               }); 
/* 511 */         if (!this.dir.exists() && this.errorOnMissingDir) {
/* 512 */           throw new BuildException(this.dir.getAbsolutePath() + " does not exist.");
/*     */         }
/*     */ 
/*     */         
/* 516 */         if (!this.dir.isDirectory() && this.dir.exists()) {
/* 517 */           throw new BuildException("%s is not a directory.", new Object[] { this.dir
/* 518 */                 .getAbsolutePath() });
/*     */         }
/* 520 */         ds = new DirectoryScanner();
/* 521 */         setupDirectoryScanner((FileScanner)ds, p);
/* 522 */         ds.setFollowSymlinks(this.followSymlinks);
/* 523 */         ds.setErrorOnMissingDir(this.errorOnMissingDir);
/* 524 */         ds.setMaxLevelsOfSymlinks(this.maxLevelsOfSymlinks);
/* 525 */         this.directoryScanner = (p == getProject()) ? ds : this.directoryScanner;
/*     */       } 
/*     */     } 
/* 528 */     ds.scan();
/* 529 */     return ds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setupDirectoryScanner(FileScanner ds) {
/* 538 */     setupDirectoryScanner(ds, getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setupDirectoryScanner(FileScanner ds, Project p) {
/* 547 */     if (isReference()) {
/* 548 */       getRef(p).setupDirectoryScanner(ds, p);
/*     */       return;
/*     */     } 
/* 551 */     dieOnCircularReference(p);
/* 552 */     if (ds == null) {
/* 553 */       throw new IllegalArgumentException("ds cannot be null");
/*     */     }
/* 555 */     ds.setBasedir(this.dir);
/*     */     
/* 557 */     PatternSet ps = mergePatterns(p);
/* 558 */     p.log(getDataTypeName() + ": Setup scanner in dir " + this.dir + " with " + ps, 4);
/*     */ 
/*     */     
/* 561 */     ds.setIncludes(ps.getIncludePatterns(p));
/* 562 */     ds.setExcludes(ps.getExcludePatterns(p));
/* 563 */     if (ds instanceof SelectorScanner) {
/* 564 */       SelectorScanner ss = (SelectorScanner)ds;
/* 565 */       ss.setSelectors(getSelectors(p));
/*     */     } 
/* 567 */     if (this.useDefaultExcludes) {
/* 568 */       ds.addDefaultExcludes();
/*     */     }
/* 570 */     ds.setCaseSensitive(this.caseSensitive);
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
/*     */   protected AbstractFileSet getRef(Project p) {
/* 585 */     return getCheckedRef(AbstractFileSet.class, getDataTypeName(), p);
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
/*     */   public synchronized boolean hasSelectors() {
/* 597 */     if (isReference()) {
/* 598 */       return getRef(getProject()).hasSelectors();
/*     */     }
/* 600 */     dieOnCircularReference();
/* 601 */     return !this.selectors.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean hasPatterns() {
/* 610 */     if (isReference() && getProject() != null) {
/* 611 */       return getRef(getProject()).hasPatterns();
/*     */     }
/* 613 */     dieOnCircularReference();
/* 614 */     return (this.defaultPatterns.hasPatterns(getProject()) || this.additionalPatterns
/* 615 */       .stream().anyMatch(ps -> ps.hasPatterns(getProject())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int selectorCount() {
/* 625 */     if (isReference()) {
/* 626 */       return getRef(getProject()).selectorCount();
/*     */     }
/* 628 */     dieOnCircularReference();
/* 629 */     return this.selectors.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized FileSelector[] getSelectors(Project p) {
/* 639 */     if (isReference()) {
/* 640 */       return getRef(getProject()).getSelectors(p);
/*     */     }
/* 642 */     dieOnCircularReference(p);
/* 643 */     return this.selectors.<FileSelector>toArray(new FileSelector[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Enumeration<FileSelector> selectorElements() {
/* 653 */     if (isReference()) {
/* 654 */       return getRef(getProject()).selectorElements();
/*     */     }
/* 656 */     dieOnCircularReference();
/* 657 */     return Collections.enumeration(this.selectors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void appendSelector(FileSelector selector) {
/* 667 */     if (isReference()) {
/* 668 */       throw noChildrenAllowed();
/*     */     }
/* 670 */     this.selectors.add(selector);
/* 671 */     this.directoryScanner = null;
/* 672 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSelector(SelectSelector selector) {
/* 683 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnd(AndSelector selector) {
/* 692 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOr(OrSelector selector) {
/* 701 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNot(NotSelector selector) {
/* 710 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNone(NoneSelector selector) {
/* 719 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMajority(MajoritySelector selector) {
/* 728 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDate(DateSelector selector) {
/* 737 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSize(SizeSelector selector) {
/* 746 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDifferent(DifferentSelector selector) {
/* 755 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilename(FilenameSelector selector) {
/* 764 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addType(TypeSelector selector) {
/* 773 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustom(ExtendSelector selector) {
/* 782 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContains(ContainsSelector selector) {
/* 791 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPresent(PresentSelector selector) {
/* 800 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepth(DepthSelector selector) {
/* 809 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepend(DependSelector selector) {
/* 818 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsRegexp(ContainsRegexpSelector selector) {
/* 827 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addModified(ModifiedSelector selector) {
/* 837 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */   
/*     */   public void addReadable(ReadableSelector r) {
/* 841 */     appendSelector((FileSelector)r);
/*     */   }
/*     */   
/*     */   public void addWritable(WritableSelector w) {
/* 845 */     appendSelector((FileSelector)w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExecutable(ExecutableSelector e) {
/* 853 */     appendSelector((FileSelector)e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSymlink(SymlinkSelector e) {
/* 861 */     appendSelector((FileSelector)e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOwnedBy(OwnedBySelector o) {
/* 869 */     appendSelector((FileSelector)o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPosixGroup(PosixGroupSelector o) {
/* 877 */     appendSelector((FileSelector)o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPosixPermissions(PosixPermissionsSelector o) {
/* 885 */     appendSelector((FileSelector)o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileSelector selector) {
/* 895 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 905 */     if (isReference()) {
/* 906 */       return getRef(getProject()).toString();
/*     */     }
/* 908 */     dieOnCircularReference();
/* 909 */     return String.join(";", (CharSequence[])getDirectoryScanner().getIncludedFiles());
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
/*     */   public synchronized Object clone() {
/* 921 */     if (isReference()) {
/* 922 */       return getRef(getProject()).clone();
/*     */     }
/*     */     try {
/* 925 */       AbstractFileSet fs = (AbstractFileSet)super.clone();
/* 926 */       fs.defaultPatterns = (PatternSet)this.defaultPatterns.clone();
/*     */       
/* 928 */       Objects.requireNonNull(PatternSet.class); fs.additionalPatterns = (List<PatternSet>)this.additionalPatterns.stream().map(PatternSet::clone).map(PatternSet.class::cast).collect(Collectors.toList());
/* 929 */       fs.selectors = new ArrayList<>(this.selectors);
/* 930 */       return fs;
/* 931 */     } catch (CloneNotSupportedException e) {
/* 932 */       throw new BuildException(e);
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
/*     */   public String[] mergeIncludes(Project p) {
/* 945 */     return mergePatterns(p).getIncludePatterns(p);
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
/*     */   public String[] mergeExcludes(Project p) {
/* 957 */     return mergePatterns(p).getExcludePatterns(p);
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
/*     */   public synchronized PatternSet mergePatterns(Project p) {
/* 969 */     if (isReference()) {
/* 970 */       return getRef(p).mergePatterns(p);
/*     */     }
/* 972 */     dieOnCircularReference();
/* 973 */     PatternSet ps = (PatternSet)this.defaultPatterns.clone();
/* 974 */     this.additionalPatterns.forEach(pat -> ps.append(pat, p));
/* 975 */     return ps;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 981 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 984 */     if (isReference()) {
/* 985 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 987 */       Objects.requireNonNull(DataType.class); Objects.requireNonNull(DataType.class); this.selectors.stream().filter(DataType.class::isInstance).map(DataType.class::cast)
/* 988 */         .forEach(type -> pushAndInvokeCircularReferenceCheck(type, stk, p));
/* 989 */       this.additionalPatterns.forEach(ps -> pushAndInvokeCircularReferenceCheck(ps, stk, p));
/* 990 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private BuildException dirAndFileAreMutuallyExclusive() {
/* 995 */     return new BuildException("you can only specify one of the dir and file attributes");
/*     */   }
/*     */   
/*     */   public AbstractFileSet() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/AbstractFileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */