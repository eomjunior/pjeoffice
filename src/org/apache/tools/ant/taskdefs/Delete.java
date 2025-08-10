/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResourceIterator;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.types.resources.Restrict;
/*     */ import org.apache.tools.ant.types.resources.Sort;
/*     */ import org.apache.tools.ant.types.resources.comparators.FileSystem;
/*     */ import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
/*     */ import org.apache.tools.ant.types.resources.comparators.Reverse;
/*     */ import org.apache.tools.ant.types.resources.selectors.Exists;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*     */ import org.apache.tools.ant.types.selectors.AndSelector;
/*     */ import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
/*     */ import org.apache.tools.ant.types.selectors.ContainsSelector;
/*     */ import org.apache.tools.ant.types.selectors.DateSelector;
/*     */ import org.apache.tools.ant.types.selectors.DependSelector;
/*     */ import org.apache.tools.ant.types.selectors.DepthSelector;
/*     */ import org.apache.tools.ant.types.selectors.ExtendSelector;
/*     */ import org.apache.tools.ant.types.selectors.FileSelector;
/*     */ import org.apache.tools.ant.types.selectors.FilenameSelector;
/*     */ import org.apache.tools.ant.types.selectors.MajoritySelector;
/*     */ import org.apache.tools.ant.types.selectors.NoneSelector;
/*     */ import org.apache.tools.ant.types.selectors.NotSelector;
/*     */ import org.apache.tools.ant.types.selectors.OrSelector;
/*     */ import org.apache.tools.ant.types.selectors.PresentSelector;
/*     */ import org.apache.tools.ant.types.selectors.SelectSelector;
/*     */ import org.apache.tools.ant.types.selectors.SizeSelector;
/*     */ import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;
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
/*     */ public class Delete
/*     */   extends MatchingTask
/*     */ {
/*  82 */   private static final ResourceComparator REVERSE_FILESYSTEM = (ResourceComparator)new Reverse((ResourceComparator)new FileSystem());
/*  83 */   private static final ResourceSelector EXISTS = (ResourceSelector)new Exists();
/*  84 */   private static FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private static class ReverseDirs
/*     */     implements ResourceCollection {
/*     */     private Project project;
/*     */     private File basedir;
/*     */     private String[] dirs;
/*     */     
/*     */     ReverseDirs(Project project, File basedir, String[] dirs) {
/*  93 */       this.project = project;
/*  94 */       this.basedir = basedir;
/*  95 */       this.dirs = dirs;
/*  96 */       Arrays.sort(this.dirs, Comparator.reverseOrder());
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Resource> iterator() {
/* 101 */       return (Iterator<Resource>)new FileResourceIterator(this.project, this.basedir, this.dirs);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFilesystemOnly() {
/* 106 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 111 */       return this.dirs.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 116 */   protected File file = null;
/* 117 */   protected File dir = null;
/* 118 */   protected Vector<FileSet> filesets = new Vector<>();
/*     */   
/*     */   protected boolean usedMatchingTask = false;
/*     */   
/*     */   protected boolean includeEmpty = false;
/*     */   
/* 124 */   private int verbosity = 3;
/*     */   private boolean quiet = false;
/*     */   private boolean failonerror = true;
/*     */   private boolean deleteOnExit = false;
/*     */   private boolean removeNotFollowedSymlinks = false;
/* 129 */   private Resources rcs = null;
/* 130 */   private boolean performGc = Os.isFamily("windows");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 138 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File dir) {
/* 147 */     this.dir = dir;
/* 148 */     getImplicitFileSet().setDir(dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 157 */     if (verbose) {
/* 158 */       this.verbosity = 2;
/*     */     } else {
/* 160 */       this.verbosity = 3;
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
/*     */   public void setQuiet(boolean quiet) {
/* 174 */     this.quiet = quiet;
/* 175 */     if (quiet) {
/* 176 */       this.failonerror = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failonerror) {
/* 186 */     this.failonerror = failonerror;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeleteOnExit(boolean deleteOnExit) {
/* 196 */     this.deleteOnExit = deleteOnExit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeEmptyDirs(boolean includeEmpty) {
/* 205 */     this.includeEmpty = includeEmpty;
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
/*     */   public void setPerformGcOnFailedDelete(boolean b) {
/* 219 */     this.performGc = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 227 */     this.filesets.addElement(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 235 */     if (rc == null) {
/*     */       return;
/*     */     }
/* 238 */     if (this.rcs == null) {
/* 239 */       this.rcs = new Resources();
/* 240 */       this.rcs.setCache(true);
/*     */     } 
/* 242 */     this.rcs.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createInclude() {
/* 251 */     this.usedMatchingTask = true;
/* 252 */     return super.createInclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createIncludesFile() {
/* 261 */     this.usedMatchingTask = true;
/* 262 */     return super.createIncludesFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createExclude() {
/* 271 */     this.usedMatchingTask = true;
/* 272 */     return super.createExclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createExcludesFile() {
/* 281 */     this.usedMatchingTask = true;
/* 282 */     return super.createExcludesFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet createPatternSet() {
/* 291 */     this.usedMatchingTask = true;
/* 292 */     return super.createPatternSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludes(String includes) {
/* 303 */     this.usedMatchingTask = true;
/* 304 */     super.setIncludes(includes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludes(String excludes) {
/* 315 */     this.usedMatchingTask = true;
/* 316 */     super.setExcludes(excludes);
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
/*     */   public void setDefaultexcludes(boolean useDefaultExcludes) {
/* 328 */     this.usedMatchingTask = true;
/* 329 */     super.setDefaultexcludes(useDefaultExcludes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludesfile(File includesfile) {
/* 340 */     this.usedMatchingTask = true;
/* 341 */     super.setIncludesfile(includesfile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludesfile(File excludesfile) {
/* 352 */     this.usedMatchingTask = true;
/* 353 */     super.setExcludesfile(excludesfile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean isCaseSensitive) {
/* 364 */     this.usedMatchingTask = true;
/* 365 */     super.setCaseSensitive(isCaseSensitive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFollowSymlinks(boolean followSymlinks) {
/* 375 */     this.usedMatchingTask = true;
/* 376 */     super.setFollowSymlinks(followSymlinks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveNotFollowedSymlinks(boolean b) {
/* 387 */     this.removeNotFollowedSymlinks = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSelector(SelectSelector selector) {
/* 397 */     this.usedMatchingTask = true;
/* 398 */     super.addSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnd(AndSelector selector) {
/* 408 */     this.usedMatchingTask = true;
/* 409 */     super.addAnd(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOr(OrSelector selector) {
/* 419 */     this.usedMatchingTask = true;
/* 420 */     super.addOr(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNot(NotSelector selector) {
/* 430 */     this.usedMatchingTask = true;
/* 431 */     super.addNot(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNone(NoneSelector selector) {
/* 441 */     this.usedMatchingTask = true;
/* 442 */     super.addNone(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMajority(MajoritySelector selector) {
/* 452 */     this.usedMatchingTask = true;
/* 453 */     super.addMajority(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDate(DateSelector selector) {
/* 463 */     this.usedMatchingTask = true;
/* 464 */     super.addDate(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSize(SizeSelector selector) {
/* 474 */     this.usedMatchingTask = true;
/* 475 */     super.addSize(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilename(FilenameSelector selector) {
/* 485 */     this.usedMatchingTask = true;
/* 486 */     super.addFilename(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustom(ExtendSelector selector) {
/* 496 */     this.usedMatchingTask = true;
/* 497 */     super.addCustom(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContains(ContainsSelector selector) {
/* 507 */     this.usedMatchingTask = true;
/* 508 */     super.addContains(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPresent(PresentSelector selector) {
/* 518 */     this.usedMatchingTask = true;
/* 519 */     super.addPresent(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepth(DepthSelector selector) {
/* 529 */     this.usedMatchingTask = true;
/* 530 */     super.addDepth(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepend(DependSelector selector) {
/* 540 */     this.usedMatchingTask = true;
/* 541 */     super.addDepend(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsRegexp(ContainsRegexpSelector selector) {
/* 551 */     this.usedMatchingTask = true;
/* 552 */     super.addContainsRegexp(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addModified(ModifiedSelector selector) {
/* 563 */     this.usedMatchingTask = true;
/* 564 */     super.addModified(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileSelector selector) {
/* 575 */     this.usedMatchingTask = true;
/* 576 */     super.add(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 586 */     if (this.usedMatchingTask) {
/* 587 */       log("DEPRECATED - Use of the implicit FileSet is deprecated.  Use a nested fileset element instead.", 
/* 588 */           this.quiet ? 3 : this.verbosity);
/*     */     }
/*     */     
/* 591 */     if (this.file == null && this.dir == null && this.filesets.isEmpty() && this.rcs == null) {
/* 592 */       throw new BuildException("At least one of the file or dir attributes, or a nested resource collection, must be set.");
/*     */     }
/*     */ 
/*     */     
/* 596 */     if (this.quiet && this.failonerror) {
/* 597 */       throw new BuildException("quiet and failonerror cannot both be set to true", 
/* 598 */           getLocation());
/*     */     }
/*     */ 
/*     */     
/* 602 */     if (this.file != null) {
/* 603 */       if (this.file.exists()) {
/* 604 */         if (this.file.isDirectory()) {
/* 605 */           log("Directory " + this.file.getAbsolutePath() + " cannot be removed using the file attribute.  Use dir instead.", 
/*     */               
/* 607 */               this.quiet ? 3 : this.verbosity);
/*     */         } else {
/* 609 */           log("Deleting: " + this.file.getAbsolutePath());
/*     */           
/* 611 */           if (!delete(this.file)) {
/* 612 */             handle("Unable to delete file " + this.file.getAbsolutePath());
/*     */           }
/*     */         } 
/* 615 */       } else if (isDanglingSymlink(this.file)) {
/* 616 */         log("Trying to delete file " + this.file.getAbsolutePath() + " which looks like a broken symlink.", 
/*     */             
/* 618 */             this.quiet ? 3 : this.verbosity);
/* 619 */         if (!delete(this.file)) {
/* 620 */           handle("Unable to delete file " + this.file.getAbsolutePath());
/*     */         }
/*     */       } else {
/* 623 */         log("Could not find file " + this.file.getAbsolutePath() + " to delete.", 
/* 624 */             this.quiet ? 3 : this.verbosity);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 629 */     if (this.dir != null && !this.usedMatchingTask) {
/* 630 */       if (this.dir.exists() && this.dir.isDirectory()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 638 */         if (this.verbosity == 3) {
/* 639 */           log("Deleting directory " + this.dir.getAbsolutePath());
/*     */         }
/* 641 */         removeDir(this.dir);
/* 642 */       } else if (isDanglingSymlink(this.dir)) {
/* 643 */         log("Trying to delete directory " + this.dir.getAbsolutePath() + " which looks like a broken symlink.", 
/*     */             
/* 645 */             this.quiet ? 3 : this.verbosity);
/* 646 */         if (!delete(this.dir)) {
/* 647 */           handle("Unable to delete directory " + this.dir.getAbsolutePath());
/*     */         }
/*     */       } 
/*     */     }
/* 651 */     Resources resourcesToDelete = new Resources();
/* 652 */     resourcesToDelete.setProject(getProject());
/* 653 */     resourcesToDelete.setCache(true);
/* 654 */     Resources filesetDirs = new Resources();
/* 655 */     filesetDirs.setProject(getProject());
/* 656 */     filesetDirs.setCache(true);
/* 657 */     FileSet implicit = null;
/* 658 */     if (this.usedMatchingTask && this.dir != null && this.dir.isDirectory()) {
/*     */       
/* 660 */       implicit = getImplicitFileSet();
/* 661 */       implicit.setProject(getProject());
/* 662 */       this.filesets.add(implicit);
/*     */     } 
/*     */     
/* 665 */     for (FileSet fs : this.filesets) {
/* 666 */       if (fs.getProject() == null) {
/* 667 */         log("Deleting fileset with no project specified; assuming executing project", 3);
/*     */         
/* 669 */         fs = (FileSet)fs.clone();
/* 670 */         fs.setProject(getProject());
/*     */       } 
/* 672 */       final File fsDir = fs.getDir();
/* 673 */       if (!fs.getErrorOnMissingDir() && (fsDir == null || !fsDir.exists())) {
/*     */         continue;
/*     */       }
/* 676 */       if (fsDir == null)
/* 677 */         throw new BuildException("File or Resource without directory or file specified"); 
/* 678 */       if (!fsDir.isDirectory()) {
/* 679 */         handle("Directory does not exist: " + fsDir); continue;
/*     */       } 
/* 681 */       DirectoryScanner ds = fs.getDirectoryScanner();
/*     */ 
/*     */ 
/*     */       
/* 685 */       final String[] files = ds.getIncludedFiles();
/* 686 */       resourcesToDelete.add(new ResourceCollection()
/*     */           {
/*     */             public boolean isFilesystemOnly() {
/* 689 */               return true;
/*     */             }
/*     */ 
/*     */             
/*     */             public int size() {
/* 694 */               return files.length;
/*     */             }
/*     */ 
/*     */             
/*     */             public Iterator<Resource> iterator() {
/* 699 */               return (Iterator<Resource>)new FileResourceIterator(Delete.this.getProject(), fsDir, files);
/*     */             }
/*     */           });
/*     */       
/* 703 */       if (this.includeEmpty) {
/* 704 */         filesetDirs.add(new ReverseDirs(getProject(), fsDir, ds
/* 705 */               .getIncludedDirectories()));
/*     */       }
/*     */       
/* 708 */       if (this.removeNotFollowedSymlinks) {
/* 709 */         String[] n = ds.getNotFollowedSymlinks();
/* 710 */         if (n.length > 0) {
/* 711 */           String[] links = new String[n.length];
/* 712 */           System.arraycopy(n, 0, links, 0, n.length);
/* 713 */           Arrays.sort(links, Comparator.reverseOrder());
/* 714 */           for (String link : links) {
/* 715 */             Path filePath = Paths.get(link, new String[0]);
/* 716 */             if (Files.isSymbolicLink(filePath)) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 721 */               boolean deleted = filePath.toFile().delete();
/* 722 */               if (!deleted) {
/* 723 */                 handle("Could not delete symbolic link at " + filePath);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 730 */     resourcesToDelete.add((ResourceCollection)filesetDirs);
/* 731 */     if (this.rcs != null) {
/*     */       
/* 733 */       Restrict exists = new Restrict();
/* 734 */       exists.add(EXISTS);
/* 735 */       exists.add((ResourceCollection)this.rcs);
/* 736 */       Sort s = new Sort();
/* 737 */       s.add(REVERSE_FILESYSTEM);
/* 738 */       s.add((ResourceCollection)exists);
/* 739 */       resourcesToDelete.add((ResourceCollection)s);
/*     */     } 
/*     */     try {
/* 742 */       if (resourcesToDelete.isFilesystemOnly()) {
/* 743 */         for (Resource r : resourcesToDelete) {
/*     */ 
/*     */           
/* 746 */           File f = ((FileProvider)r.as(FileProvider.class)).getFile();
/* 747 */           if (!f.exists()) {
/*     */             continue;
/*     */           }
/* 750 */           if (!f.isDirectory() || (f.list()).length == 0) {
/* 751 */             log("Deleting " + f, this.verbosity);
/* 752 */             if (!delete(f) && this.failonerror) {
/* 753 */               handle("Unable to delete " + (
/* 754 */                   f.isDirectory() ? "directory " : "file ") + f);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } else {
/* 759 */         handle(getTaskName() + " handles only filesystem resources");
/*     */       } 
/* 761 */     } catch (Exception e) {
/* 762 */       handle(e);
/*     */     } finally {
/* 764 */       if (implicit != null) {
/* 765 */         this.filesets.remove(implicit);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handle(String msg) {
/* 775 */     handle((Exception)new BuildException(msg));
/*     */   }
/*     */   
/*     */   private void handle(Exception e) {
/* 779 */     if (this.failonerror) {
/* 780 */       throw (e instanceof BuildException) ? (BuildException)e : new BuildException(e);
/*     */     }
/* 782 */     log(e, this.quiet ? 3 : this.verbosity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean delete(File f) {
/* 791 */     if (!FILE_UTILS.tryHardToDelete(f, this.performGc)) {
/* 792 */       if (this.deleteOnExit) {
/* 793 */         int level = this.quiet ? 3 : 2;
/* 794 */         log("Failed to delete " + f + ", calling deleteOnExit. This attempts to delete the file when the Ant jvm has exited and might not succeed.", level);
/*     */ 
/*     */         
/* 797 */         f.deleteOnExit();
/* 798 */         return true;
/*     */       } 
/* 800 */       return false;
/*     */     } 
/* 802 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeDir(File d) {
/* 811 */     String[] list = d.list();
/* 812 */     if (list == null) {
/* 813 */       list = new String[0];
/*     */     }
/* 815 */     for (String s : list) {
/* 816 */       File f = new File(d, s);
/* 817 */       if (f.isDirectory()) {
/* 818 */         removeDir(f);
/*     */       } else {
/* 820 */         log("Deleting " + f.getAbsolutePath(), this.quiet ? 3 : this.verbosity);
/* 821 */         if (!delete(f)) {
/* 822 */           handle("Unable to delete file " + f.getAbsolutePath());
/*     */         }
/*     */       } 
/*     */     } 
/* 826 */     log("Deleting directory " + d.getAbsolutePath(), this.verbosity);
/* 827 */     if (!delete(d)) {
/* 828 */       handle("Unable to delete directory " + d.getAbsolutePath());
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
/*     */   protected void removeFiles(File d, String[] files, String[] dirs) {
/* 840 */     if (files.length > 0) {
/* 841 */       log("Deleting " + files.length + " files from " + d
/* 842 */           .getAbsolutePath(), this.quiet ? 3 : this.verbosity);
/* 843 */       for (String filename : files) {
/* 844 */         File f = new File(d, filename);
/* 845 */         log("Deleting " + f.getAbsolutePath(), 
/* 846 */             this.quiet ? 3 : this.verbosity);
/* 847 */         if (!delete(f)) {
/* 848 */           handle("Unable to delete file " + f.getAbsolutePath());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 853 */     if (dirs.length > 0 && this.includeEmpty) {
/* 854 */       int dirCount = 0;
/* 855 */       for (int j = dirs.length - 1; j >= 0; j--) {
/* 856 */         File currDir = new File(d, dirs[j]);
/* 857 */         String[] dirFiles = currDir.list();
/* 858 */         if (dirFiles == null || dirFiles.length == 0) {
/* 859 */           log("Deleting " + currDir.getAbsolutePath(), 
/* 860 */               this.quiet ? 3 : this.verbosity);
/* 861 */           if (!delete(currDir)) {
/* 862 */             handle("Unable to delete directory " + currDir.getAbsolutePath());
/*     */           } else {
/* 864 */             dirCount++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 869 */       if (dirCount > 0) {
/* 870 */         log("Deleted " + dirCount + " director" + (
/* 871 */             (dirCount == 1) ? "y" : "ies") + " form " + d
/* 872 */             .getAbsolutePath(), 
/* 873 */             this.quiet ? 3 : this.verbosity);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isDanglingSymlink(File f) {
/* 879 */     if (!Files.isSymbolicLink(f.toPath()))
/*     */     {
/* 881 */       return false;
/*     */     }
/*     */     
/* 884 */     boolean targetFileExists = Files.exists(f.toPath(), new java.nio.file.LinkOption[0]);
/* 885 */     return !targetFileExists;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Delete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */