/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Enumeration;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.selectors.AndSelector;
/*     */ import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
/*     */ import org.apache.tools.ant.types.selectors.ContainsSelector;
/*     */ import org.apache.tools.ant.types.selectors.DateSelector;
/*     */ import org.apache.tools.ant.types.selectors.DependSelector;
/*     */ import org.apache.tools.ant.types.selectors.DepthSelector;
/*     */ import org.apache.tools.ant.types.selectors.DifferentSelector;
/*     */ import org.apache.tools.ant.types.selectors.ExtendSelector;
/*     */ import org.apache.tools.ant.types.selectors.FileSelector;
/*     */ import org.apache.tools.ant.types.selectors.FilenameSelector;
/*     */ import org.apache.tools.ant.types.selectors.MajoritySelector;
/*     */ import org.apache.tools.ant.types.selectors.NoneSelector;
/*     */ import org.apache.tools.ant.types.selectors.NotSelector;
/*     */ import org.apache.tools.ant.types.selectors.OrSelector;
/*     */ import org.apache.tools.ant.types.selectors.PresentSelector;
/*     */ import org.apache.tools.ant.types.selectors.SelectSelector;
/*     */ import org.apache.tools.ant.types.selectors.SelectorContainer;
/*     */ import org.apache.tools.ant.types.selectors.SizeSelector;
/*     */ import org.apache.tools.ant.types.selectors.TypeSelector;
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
/*     */ 
/*     */ 
/*     */ public abstract class MatchingTask
/*     */   extends Task
/*     */   implements SelectorContainer
/*     */ {
/*  61 */   protected FileSet fileset = new FileSet();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/*  67 */     super.setProject(project);
/*  68 */     this.fileset.setProject(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createInclude() {
/*  76 */     return this.fileset.createInclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createIncludesFile() {
/*  84 */     return this.fileset.createIncludesFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createExclude() {
/*  92 */     return this.fileset.createExclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createExcludesFile() {
/* 100 */     return this.fileset.createExcludesFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet createPatternSet() {
/* 108 */     return this.fileset.createPatternSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludes(String includes) {
/* 118 */     this.fileset.setIncludes(includes);
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
/*     */   public void XsetItems(String itemString) {
/* 130 */     log("The items attribute is deprecated. Please use the includes attribute.", 1);
/*     */     
/* 132 */     if (itemString == null || "*".equals(itemString) || "."
/* 133 */       .equals(itemString)) {
/* 134 */       createInclude().setName("**");
/*     */     } else {
/* 136 */       StringTokenizer tok = new StringTokenizer(itemString, ", ");
/* 137 */       while (tok.hasMoreTokens()) {
/* 138 */         String pattern = tok.nextToken().trim();
/* 139 */         if (!pattern.isEmpty()) {
/* 140 */           createInclude().setName(pattern + "/**");
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
/*     */   public void setExcludes(String excludes) {
/* 153 */     this.fileset.setExcludes(excludes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void XsetIgnore(String ignoreString) {
/* 163 */     log("The ignore attribute is deprecated.Please use the excludes attribute.", 1);
/*     */     
/* 165 */     if (ignoreString != null && !ignoreString.isEmpty()) {
/* 166 */       StringTokenizer tok = new StringTokenizer(ignoreString, ", ", false);
/*     */       
/* 168 */       while (tok.hasMoreTokens()) {
/* 169 */         createExclude().setName("**/" + tok.nextToken().trim() + "/**");
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
/*     */ 
/*     */   
/*     */   public void setDefaultexcludes(boolean useDefaultExcludes) {
/* 184 */     this.fileset.setDefaultexcludes(useDefaultExcludes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DirectoryScanner getDirectoryScanner(File baseDir) {
/* 193 */     this.fileset.setDir(baseDir);
/* 194 */     return this.fileset.getDirectoryScanner(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludesfile(File includesfile) {
/* 204 */     this.fileset.setIncludesfile(includesfile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludesfile(File excludesfile) {
/* 214 */     this.fileset.setExcludesfile(excludesfile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean isCaseSensitive) {
/* 224 */     this.fileset.setCaseSensitive(isCaseSensitive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFollowSymlinks(boolean followSymlinks) {
/* 233 */     this.fileset.setFollowSymlinks(followSymlinks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSelectors() {
/* 243 */     return this.fileset.hasSelectors();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int selectorCount() {
/* 253 */     return this.fileset.selectorCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSelector[] getSelectors(Project p) {
/* 263 */     return this.fileset.getSelectors(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<FileSelector> selectorElements() {
/* 273 */     return this.fileset.selectorElements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendSelector(FileSelector selector) {
/* 283 */     this.fileset.appendSelector(selector);
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
/* 294 */     this.fileset.addSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnd(AndSelector selector) {
/* 303 */     this.fileset.addAnd(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOr(OrSelector selector) {
/* 312 */     this.fileset.addOr(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNot(NotSelector selector) {
/* 321 */     this.fileset.addNot(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNone(NoneSelector selector) {
/* 330 */     this.fileset.addNone(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMajority(MajoritySelector selector) {
/* 339 */     this.fileset.addMajority(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDate(DateSelector selector) {
/* 348 */     this.fileset.addDate(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSize(SizeSelector selector) {
/* 357 */     this.fileset.addSize(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilename(FilenameSelector selector) {
/* 366 */     this.fileset.addFilename(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustom(ExtendSelector selector) {
/* 375 */     this.fileset.addCustom(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContains(ContainsSelector selector) {
/* 384 */     this.fileset.addContains(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPresent(PresentSelector selector) {
/* 393 */     this.fileset.addPresent(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepth(DepthSelector selector) {
/* 402 */     this.fileset.addDepth(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepend(DependSelector selector) {
/* 411 */     this.fileset.addDepend(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsRegexp(ContainsRegexpSelector selector) {
/* 420 */     this.fileset.addContainsRegexp(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDifferent(DifferentSelector selector) {
/* 430 */     this.fileset.addDifferent(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addType(TypeSelector selector) {
/* 440 */     this.fileset.addType(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addModified(ModifiedSelector selector) {
/* 450 */     this.fileset.addModified(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileSelector selector) {
/* 460 */     this.fileset.add(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final FileSet getImplicitFileSet() {
/* 469 */     return this.fileset;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/MatchingTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */