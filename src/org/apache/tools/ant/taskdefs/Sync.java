/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.AbstractFileSet;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.types.resources.Restrict;
/*     */ import org.apache.tools.ant.types.resources.selectors.Exists;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*     */ import org.apache.tools.ant.types.selectors.FileSelector;
/*     */ import org.apache.tools.ant.types.selectors.NoneSelector;
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
/*     */ public class Sync
/*     */   extends Task
/*     */ {
/*     */   private MyCopy myCopy;
/*     */   private SyncTarget syncTarget;
/*  68 */   private Resources resources = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() throws BuildException {
/*  80 */     this.myCopy = new MyCopy();
/*  81 */     configureTask(this.myCopy);
/*     */ 
/*     */     
/*  84 */     this.myCopy.setFiltering(false);
/*  85 */     this.myCopy.setIncludeEmptyDirs(false);
/*  86 */     this.myCopy.setPreserveLastModified(true);
/*     */   }
/*     */   
/*     */   private void configureTask(Task helper) {
/*  90 */     helper.setProject(getProject());
/*  91 */     helper.setTaskName(getTaskName());
/*  92 */     helper.setOwningTarget(getOwningTarget());
/*  93 */     helper.init();
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
/*     */   public void execute() throws BuildException {
/* 106 */     File toDir = this.myCopy.getToDir();
/*     */ 
/*     */     
/* 109 */     Set<String> allFiles = this.myCopy.nonOrphans;
/*     */ 
/*     */ 
/*     */     
/* 113 */     boolean noRemovalNecessary = (!toDir.exists() || (toDir.list()).length < 1);
/*     */ 
/*     */     
/* 116 */     log("PASS#1: Copying files to " + toDir, 4);
/* 117 */     this.myCopy.execute();
/*     */ 
/*     */     
/* 120 */     if (noRemovalNecessary) {
/* 121 */       log("NO removing necessary in " + toDir, 4);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 128 */     Set<File> preservedDirectories = new LinkedHashSet<>();
/*     */ 
/*     */     
/* 131 */     log("PASS#2: Removing orphan files from " + toDir, 4);
/* 132 */     int[] removedFileCount = removeOrphanFiles(allFiles, toDir, preservedDirectories);
/*     */     
/* 134 */     logRemovedCount(removedFileCount[0], "dangling director", "y", "ies");
/* 135 */     logRemovedCount(removedFileCount[1], "dangling file", "", "s");
/*     */ 
/*     */     
/* 138 */     if (!this.myCopy.getIncludeEmptyDirs() || 
/* 139 */       getExplicitPreserveEmptyDirs() == Boolean.FALSE) {
/* 140 */       log("PASS#3: Removing empty directories from " + toDir, 4);
/*     */ 
/*     */       
/* 143 */       int removedDirCount = 0;
/* 144 */       if (!this.myCopy.getIncludeEmptyDirs()) {
/*     */         
/* 146 */         removedDirCount = removeEmptyDirectories(toDir, false, preservedDirectories);
/*     */       } else {
/*     */         
/* 149 */         removedDirCount = removeEmptyDirectories(preservedDirectories);
/*     */       } 
/* 151 */       logRemovedCount(removedDirCount, "empty director", "y", "ies");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void logRemovedCount(int count, String prefix, String singularSuffix, String pluralSuffix) {
/* 157 */     File toDir = this.myCopy.getToDir();
/*     */     
/* 159 */     String what = (prefix == null) ? "" : prefix;
/* 160 */     what = what + ((count < 2) ? singularSuffix : pluralSuffix);
/*     */     
/* 162 */     if (count > 0) {
/* 163 */       log("Removed " + count + " " + what + " from " + toDir, 2);
/*     */     } else {
/*     */       
/* 166 */       log("NO " + what + " to remove from " + toDir, 3);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] removeOrphanFiles(Set<String> nonOrphans, File toDir, Set<File> preservedDirectories) {
/*     */     DirectoryScanner ds;
/* 192 */     int[] removedCount = { 0, 0 };
/*     */     
/* 194 */     String[] excls = nonOrphans.<String>toArray(new String[nonOrphans.size() + 1]);
/*     */     
/* 196 */     excls[nonOrphans.size()] = "";
/*     */ 
/*     */     
/* 199 */     if (this.syncTarget != null) {
/* 200 */       FileSet fs = this.syncTarget.toFileSet(false);
/* 201 */       fs.setDir(toDir);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 206 */       PatternSet ps = this.syncTarget.mergePatterns(getProject());
/* 207 */       fs.appendExcludes(ps.getIncludePatterns(getProject()));
/* 208 */       fs.appendIncludes(ps.getExcludePatterns(getProject()));
/* 209 */       fs.setDefaultexcludes(!this.syncTarget.getDefaultexcludes());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 214 */       FileSelector[] s = this.syncTarget.getSelectors(getProject());
/* 215 */       if (s.length > 0) {
/* 216 */         NoneSelector ns = new NoneSelector();
/* 217 */         for (FileSelector element : s) {
/* 218 */           ns.appendSelector(element);
/*     */         }
/* 220 */         fs.appendSelector((FileSelector)ns);
/*     */       } 
/* 222 */       ds = fs.getDirectoryScanner(getProject());
/*     */     } else {
/* 224 */       ds = new DirectoryScanner();
/* 225 */       ds.setBasedir(toDir);
/*     */ 
/*     */ 
/*     */       
/* 229 */       Objects.requireNonNull(ds); FileUtils.isCaseSensitiveFileSystem(toDir.toPath()).ifPresent(ds::setCaseSensitive);
/*     */     } 
/* 231 */     ds.addExcludes(excls);
/*     */     
/* 233 */     ds.scan();
/* 234 */     for (String file : ds.getIncludedFiles()) {
/* 235 */       File f = new File(toDir, file);
/* 236 */       log("Removing orphan file: " + f, 4);
/* 237 */       f.delete();
/* 238 */       removedCount[1] = removedCount[1] + 1;
/*     */     } 
/* 240 */     String[] dirs = ds.getIncludedDirectories();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     for (int i = dirs.length - 1; i >= 0; i--) {
/* 247 */       File f = new File(toDir, dirs[i]);
/* 248 */       String[] children = f.list();
/* 249 */       if (children == null || children.length < 1) {
/* 250 */         log("Removing orphan directory: " + f, 4);
/* 251 */         f.delete();
/* 252 */         removedCount[0] = removedCount[0] + 1;
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     Boolean ped = getExplicitPreserveEmptyDirs();
/* 257 */     if (ped != null && ped.booleanValue() != this.myCopy.getIncludeEmptyDirs()) {
/* 258 */       FileSet fs = this.syncTarget.toFileSet(true);
/* 259 */       fs.setDir(toDir);
/*     */       
/* 261 */       String[] preservedDirs = fs.getDirectoryScanner(getProject()).getIncludedDirectories();
/* 262 */       for (int j = preservedDirs.length - 1; j >= 0; j--) {
/* 263 */         preservedDirectories.add(new File(toDir, preservedDirs[j]));
/*     */       }
/*     */     } 
/*     */     
/* 267 */     return removedCount;
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
/*     */   private int removeEmptyDirectories(File dir, boolean removeIfEmpty, Set<File> preservedEmptyDirectories) {
/* 290 */     int removedCount = 0;
/* 291 */     if (dir.isDirectory()) {
/* 292 */       File[] children = dir.listFiles();
/* 293 */       for (File file : children) {
/*     */         
/* 295 */         if (file.isDirectory()) {
/* 296 */           removedCount += removeEmptyDirectories(file, true, preservedEmptyDirectories);
/*     */         }
/*     */       } 
/*     */       
/* 300 */       if (children.length > 0)
/*     */       {
/*     */         
/* 303 */         children = dir.listFiles();
/*     */       }
/* 305 */       if (children.length < 1 && removeIfEmpty && 
/* 306 */         !preservedEmptyDirectories.contains(dir)) {
/* 307 */         log("Removing empty directory: " + dir, 4);
/* 308 */         dir.delete();
/* 309 */         removedCount++;
/*     */       } 
/*     */     } 
/* 312 */     return removedCount;
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
/*     */   private int removeEmptyDirectories(Set<File> preservedEmptyDirectories) {
/* 329 */     int removedCount = 0;
/* 330 */     for (File f : preservedEmptyDirectories) {
/* 331 */       String[] s = f.list();
/* 332 */       if (s == null || s.length == 0) {
/* 333 */         log("Removing empty directory: " + f, 4);
/* 334 */         f.delete();
/* 335 */         removedCount++;
/*     */       } 
/*     */     } 
/* 338 */     return removedCount;
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
/*     */   public void setTodir(File destDir) {
/* 350 */     this.myCopy.setTodir(destDir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 358 */     this.myCopy.setVerbose(verbose);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverwrite(boolean overwrite) {
/* 366 */     this.myCopy.setOverwrite(overwrite);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeEmptyDirs(boolean includeEmpty) {
/* 374 */     this.myCopy.setIncludeEmptyDirs(includeEmpty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failonerror) {
/* 382 */     this.myCopy.setFailOnError(failonerror);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 390 */     add((ResourceCollection)set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 399 */     if (rc instanceof FileSet && rc.isFilesystemOnly()) {
/*     */       
/* 401 */       this.myCopy.add(rc);
/*     */     } else {
/* 403 */       if (this.resources == null) {
/* 404 */         Restrict r = new Restrict();
/* 405 */         r.add((ResourceSelector)new Exists());
/* 406 */         this.resources = new Resources();
/* 407 */         r.add((ResourceCollection)this.resources);
/* 408 */         this.myCopy.add((ResourceCollection)r);
/*     */       } 
/* 410 */       this.resources.add(rc);
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
/*     */   public void setGranularity(long granularity) {
/* 423 */     this.myCopy.setGranularity(granularity);
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
/*     */   public void addPreserveInTarget(SyncTarget s) {
/* 436 */     if (this.syncTarget != null) {
/* 437 */       throw new BuildException("you must not specify multiple preserveintarget elements.");
/*     */     }
/*     */     
/* 440 */     this.syncTarget = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean getExplicitPreserveEmptyDirs() {
/* 450 */     return (this.syncTarget == null) ? null : this.syncTarget.getPreserveEmptyDirs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MyCopy
/*     */     extends Copy
/*     */   {
/* 460 */     private Set<String> nonOrphans = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void scan(File fromDir, File toDir, String[] files, String[] dirs) {
/* 469 */       Sync.assertTrue("No mapper", (this.mapperElement == null));
/*     */       
/* 471 */       super.scan(fromDir, toDir, files, dirs);
/*     */       
/* 473 */       Collections.addAll(this.nonOrphans, files);
/* 474 */       Collections.addAll(this.nonOrphans, dirs);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Map<Resource, String[]> scan(Resource[] resources, File toDir) {
/* 483 */       Sync.assertTrue("No mapper", (this.mapperElement == null));
/*     */       
/* 485 */       Objects.requireNonNull(this.nonOrphans); Stream.<Resource>of(resources).map(Resource::getName).forEach(this.nonOrphans::add);
/*     */       
/* 487 */       return super.scan(resources, toDir);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public File getToDir() {
/* 495 */       return this.destDir;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getIncludeEmptyDirs() {
/* 503 */       return this.includeEmpty;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean supportsNonFileResources() {
/* 513 */       return true;
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
/*     */   public static class SyncTarget
/*     */     extends AbstractFileSet
/*     */   {
/*     */     private Boolean preserveEmptyDirs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDir(File dir) throws BuildException {
/* 546 */       throw new BuildException("preserveintarget doesn't support the dir attribute");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPreserveEmptyDirs(boolean b) {
/* 558 */       this.preserveEmptyDirs = Boolean.valueOf(b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Boolean getPreserveEmptyDirs() {
/* 569 */       return this.preserveEmptyDirs;
/*     */     }
/*     */     
/*     */     private FileSet toFileSet(boolean withPatterns) {
/* 573 */       FileSet fs = new FileSet();
/* 574 */       fs.setCaseSensitive(isCaseSensitive());
/* 575 */       fs.setFollowSymlinks(isFollowSymlinks());
/* 576 */       fs.setMaxLevelsOfSymlinks(getMaxLevelsOfSymlinks());
/* 577 */       fs.setProject(getProject());
/*     */       
/* 579 */       if (withPatterns) {
/* 580 */         PatternSet ps = mergePatterns(getProject());
/* 581 */         fs.appendIncludes(ps.getIncludePatterns(getProject()));
/* 582 */         fs.appendExcludes(ps.getExcludePatterns(getProject()));
/* 583 */         for (FileSelector sel : getSelectors(getProject())) {
/* 584 */           fs.appendSelector(sel);
/*     */         }
/* 586 */         fs.setDefaultexcludes(getDefaultexcludes());
/*     */       } 
/* 588 */       return fs;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void assertTrue(String message, boolean condition) {
/* 596 */     if (!condition)
/* 597 */       throw new BuildException("Assertion Error: " + message); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Sync.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */