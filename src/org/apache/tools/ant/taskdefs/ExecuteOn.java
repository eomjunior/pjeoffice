/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.types.AbstractFileSet;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.DirSet;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.FileList;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.ResourceUtils;
/*     */ import org.apache.tools.ant.util.SourceFileScanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecuteOn
/*     */   extends ExecTask
/*     */ {
/*  67 */   protected Vector<AbstractFileSet> filesets = new Vector<>();
/*     */   
/*  69 */   private Union resources = null;
/*     */   private boolean relative = false;
/*     */   private boolean parallel = false;
/*     */   private boolean forwardSlash = false;
/*  73 */   protected String type = "file";
/*  74 */   protected Commandline.Marker srcFilePos = null;
/*     */   private boolean skipEmpty = false;
/*  76 */   protected Commandline.Marker targetFilePos = null;
/*  77 */   protected Mapper mapperElement = null;
/*  78 */   protected FileNameMapper mapper = null;
/*  79 */   protected File destDir = null;
/*  80 */   private int maxParallel = -1;
/*     */ 
/*     */   
/*     */   private boolean addSourceFile = true;
/*     */ 
/*     */   
/*     */   private boolean verbose = false;
/*     */ 
/*     */   
/*     */   private boolean ignoreMissing = true;
/*     */   
/*     */   private boolean force = false;
/*     */   
/*     */   protected boolean srcIsFirst = true;
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/*  97 */     this.filesets.addElement(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDirset(DirSet set) {
/* 108 */     this.filesets.addElement(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilelist(FileList list) {
/* 116 */     add((ResourceCollection)list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 125 */     if (this.resources == null) {
/* 126 */       this.resources = new Union();
/*     */     }
/* 128 */     this.resources.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRelative(boolean relative) {
/* 139 */     this.relative = relative;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParallel(boolean parallel) {
/* 150 */     this.parallel = parallel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(FileDirBoth type) {
/* 158 */     this.type = type.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipEmptyFilesets(boolean skip) {
/* 168 */     this.skipEmpty = skip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File destDir) {
/* 176 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForwardslash(boolean forwardSlash) {
/* 185 */     this.forwardSlash = forwardSlash;
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
/*     */   public void setMaxParallel(int max) {
/* 200 */     this.maxParallel = max;
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
/*     */   public void setAddsourcefile(boolean b) {
/* 213 */     this.addSourceFile = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean b) {
/* 224 */     this.verbose = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoremissing(boolean b) {
/* 234 */     this.ignoreMissing = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForce(boolean b) {
/* 244 */     this.force = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline.Marker createSrcfile() {
/* 253 */     if (this.srcFilePos != null) {
/* 254 */       throw new BuildException(getTaskType() + " doesn't support multiple srcfile elements.", 
/* 255 */           getLocation());
/*     */     }
/* 257 */     this.srcFilePos = this.cmdl.createMarker();
/* 258 */     return this.srcFilePos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline.Marker createTargetfile() {
/* 267 */     if (this.targetFilePos != null) {
/* 268 */       throw new BuildException(getTaskType() + " doesn't support multiple targetfile elements.", 
/* 269 */           getLocation());
/*     */     }
/* 271 */     this.targetFilePos = this.cmdl.createMarker();
/* 272 */     this.srcIsFirst = (this.srcFilePos != null);
/* 273 */     return this.targetFilePos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() throws BuildException {
/* 283 */     if (this.mapperElement != null) {
/* 284 */       throw new BuildException("Cannot define more than one mapper", 
/* 285 */           getLocation());
/*     */     }
/* 287 */     this.mapperElement = new Mapper(getProject());
/* 288 */     return this.mapperElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/* 297 */     createMapper().add(fileNameMapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkConfiguration() {
/* 307 */     if ("execon".equals(getTaskName())) {
/* 308 */       log("!! execon is deprecated. Use apply instead. !!");
/*     */     }
/* 310 */     super.checkConfiguration();
/* 311 */     if (this.filesets.isEmpty() && this.resources == null) {
/* 312 */       throw new BuildException("no resources specified", 
/* 313 */           getLocation());
/*     */     }
/* 315 */     if (this.targetFilePos != null && this.mapperElement == null) {
/* 316 */       throw new BuildException("targetfile specified without mapper", 
/* 317 */           getLocation());
/*     */     }
/* 319 */     if (this.destDir != null && this.mapperElement == null) {
/* 320 */       throw new BuildException("dest specified without mapper", 
/* 321 */           getLocation());
/*     */     }
/* 323 */     if (this.mapperElement != null) {
/* 324 */       this.mapper = this.mapperElement.getImplementation();
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
/*     */   protected ExecuteStreamHandler createHandler() throws BuildException {
/* 337 */     return (this.redirectorElement == null) ? super.createHandler() : new PumpStreamHandler();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setupRedirector() {
/* 345 */     super.setupRedirector();
/* 346 */     this.redirector.setAppendProperties(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void runExec(Execute exe) throws BuildException {
/* 356 */     int totalFiles = 0;
/* 357 */     int totalDirs = 0;
/* 358 */     boolean haveExecuted = false;
/*     */     try {
/* 360 */       Vector<String> fileNames = new Vector<>();
/* 361 */       Vector<File> baseDirs = new Vector<>();
/* 362 */       for (AbstractFileSet fs : this.filesets) {
/* 363 */         String currentType = this.type;
/* 364 */         if (fs instanceof DirSet && 
/* 365 */           !"dir".equals(this.type)) {
/* 366 */           log("Found a nested dirset but type is " + this.type + ". Temporarily switching to type=\"dir\" on the assumption that you really did mean <dirset> not <fileset>.", 4);
/*     */ 
/*     */           
/* 369 */           currentType = "dir";
/*     */         } 
/*     */         
/* 372 */         File base = fs.getDir(getProject());
/*     */         
/* 374 */         DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/*     */         
/* 376 */         if (!"dir".equals(currentType)) {
/* 377 */           for (String value : getFiles(base, ds)) {
/* 378 */             totalFiles++;
/* 379 */             fileNames.add(value);
/* 380 */             baseDirs.add(base);
/*     */           } 
/*     */         }
/* 383 */         if (!"file".equals(currentType)) {
/* 384 */           for (String value : getDirs(base, ds)) {
/* 385 */             totalDirs++;
/* 386 */             fileNames.add(value);
/* 387 */             baseDirs.add(base);
/*     */           } 
/*     */         }
/* 390 */         if (fileNames.isEmpty() && this.skipEmpty) {
/* 391 */           logSkippingFileset(currentType, ds, base);
/*     */           continue;
/*     */         } 
/* 394 */         if (!this.parallel) {
/* 395 */           for (String srcFile : fileNames) {
/* 396 */             String[] command = getCommandline(srcFile, base);
/* 397 */             log(Commandline.describeCommand(command), 3);
/* 398 */             exe.setCommandline(command);
/*     */             
/* 400 */             if (this.redirectorElement != null) {
/* 401 */               setupRedirector();
/* 402 */               this.redirectorElement.configure(this.redirector, srcFile);
/*     */             } 
/* 404 */             if (this.redirectorElement != null || haveExecuted)
/*     */             {
/*     */ 
/*     */               
/* 408 */               exe.setStreamHandler(this.redirector.createHandler());
/*     */             }
/* 410 */             runExecute(exe);
/* 411 */             haveExecuted = true;
/*     */           } 
/* 413 */           fileNames.clear();
/* 414 */           baseDirs.clear();
/*     */         } 
/*     */       } 
/*     */       
/* 418 */       if (this.resources != null) {
/* 419 */         for (Resource res : this.resources) {
/*     */           
/* 421 */           if (!res.isExists() && this.ignoreMissing) {
/*     */             continue;
/*     */           }
/*     */           
/* 425 */           File base = null;
/* 426 */           String name = res.getName();
/* 427 */           FileProvider fp = (FileProvider)res.as(FileProvider.class);
/* 428 */           if (fp != null) {
/* 429 */             FileResource fr = ResourceUtils.asFileResource(fp);
/* 430 */             base = fr.getBaseDir();
/* 431 */             if (base == null) {
/* 432 */               name = fr.getFile().getAbsolutePath();
/*     */             }
/*     */           } 
/*     */           
/* 436 */           if ((restrict(new String[] { name }, base)).length == 0) {
/*     */             continue;
/*     */           }
/*     */           
/* 440 */           if ((!res.isDirectory() || !res.isExists()) && !"dir".equals(this.type)) {
/* 441 */             totalFiles++;
/* 442 */           } else if (res.isDirectory() && !"file".equals(this.type)) {
/* 443 */             totalDirs++;
/*     */           } else {
/*     */             continue;
/*     */           } 
/*     */           
/* 448 */           baseDirs.add(base);
/* 449 */           fileNames.add(name);
/*     */           
/* 451 */           if (!this.parallel) {
/* 452 */             String[] command = getCommandline(name, base);
/* 453 */             log(Commandline.describeCommand(command), 3);
/* 454 */             exe.setCommandline(command);
/*     */             
/* 456 */             if (this.redirectorElement != null) {
/* 457 */               setupRedirector();
/* 458 */               this.redirectorElement.configure(this.redirector, name);
/*     */             } 
/* 460 */             if (this.redirectorElement != null || haveExecuted)
/*     */             {
/*     */ 
/*     */               
/* 464 */               exe.setStreamHandler(this.redirector.createHandler());
/*     */             }
/* 466 */             runExecute(exe);
/* 467 */             haveExecuted = true;
/* 468 */             fileNames.clear();
/* 469 */             baseDirs.clear();
/*     */           } 
/*     */         } 
/*     */       }
/* 473 */       if (this.parallel && (!fileNames.isEmpty() || !this.skipEmpty)) {
/* 474 */         runParallel(exe, fileNames, baseDirs);
/* 475 */         haveExecuted = true;
/*     */       } 
/* 477 */       if (haveExecuted) {
/* 478 */         log("Applied " + this.cmdl.getExecutable() + " to " + totalFiles + " file" + (
/* 479 */             (totalFiles != 1) ? "s" : "") + " and " + totalDirs + " director" + (
/* 480 */             (totalDirs != 1) ? "ies" : "y") + ".", 
/* 481 */             this.verbose ? 2 : 3);
/*     */       }
/* 483 */     } catch (IOException e) {
/* 484 */       throw new BuildException("Execute failed: " + e, e, getLocation());
/*     */     } finally {
/*     */       
/* 487 */       logFlush();
/* 488 */       this.redirector.setAppendProperties(false);
/* 489 */       this.redirector.setProperties();
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
/*     */   private void logSkippingFileset(String currentType, DirectoryScanner ds, File base) {
/* 502 */     int includedCount = (!"dir".equals(currentType) ? ds.getIncludedFilesCount() : 0) + (!"file".equals(currentType) ? ds.getIncludedDirsCount() : 0);
/*     */     
/* 504 */     log("Skipping fileset for directory " + base + ". It is " + (
/* 505 */         (includedCount > 0) ? "up to date." : "empty."), 
/* 506 */         this.verbose ? 2 : 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getCommandline(String[] srcFiles, File[] baseDirs) {
/* 517 */     char fileSeparator = File.separatorChar;
/* 518 */     List<String> targets = new ArrayList<>();
/* 519 */     if (this.targetFilePos != null) {
/* 520 */       Set<String> addedFiles = new HashSet<>();
/* 521 */       for (String srcFile : srcFiles) {
/* 522 */         String[] subTargets = this.mapper.mapFileName(srcFile);
/* 523 */         if (subTargets != null) {
/* 524 */           for (String subTarget : subTargets) {
/*     */             String name;
/* 526 */             if (this.relative) {
/* 527 */               name = subTarget;
/*     */             } else {
/* 529 */               name = (new File(this.destDir, subTarget)).getAbsolutePath();
/*     */             } 
/* 531 */             if (this.forwardSlash && fileSeparator != '/') {
/* 532 */               name = name.replace(fileSeparator, '/');
/*     */             }
/* 534 */             if (!addedFiles.contains(name)) {
/* 535 */               targets.add(name);
/* 536 */               addedFiles.add(name);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 542 */     String[] targetFiles = targets.<String>toArray(new String[0]);
/*     */     
/* 544 */     if (!this.addSourceFile) {
/* 545 */       srcFiles = new String[0];
/*     */     }
/* 547 */     String[] orig = this.cmdl.getCommandline();
/* 548 */     String[] result = new String[orig.length + srcFiles.length + targetFiles.length];
/*     */ 
/*     */     
/* 551 */     int srcIndex = orig.length;
/* 552 */     if (this.srcFilePos != null) {
/* 553 */       srcIndex = this.srcFilePos.getPosition();
/*     */     }
/* 555 */     if (this.targetFilePos != null) {
/* 556 */       int targetIndex = this.targetFilePos.getPosition();
/*     */       
/* 558 */       if (srcIndex < targetIndex || (srcIndex == targetIndex && this.srcIsFirst))
/*     */       {
/*     */ 
/*     */         
/* 562 */         System.arraycopy(orig, 0, result, 0, srcIndex);
/*     */ 
/*     */         
/* 565 */         System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length, targetIndex - srcIndex);
/*     */ 
/*     */ 
/*     */         
/* 569 */         insertTargetFiles(targetFiles, result, targetIndex + srcFiles.length, this.targetFilePos
/*     */             
/* 571 */             .getPrefix(), this.targetFilePos
/* 572 */             .getSuffix());
/*     */ 
/*     */         
/* 575 */         System.arraycopy(orig, targetIndex, result, targetIndex + srcFiles.length + targetFiles.length, orig.length - targetIndex);
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 580 */         System.arraycopy(orig, 0, result, 0, targetIndex);
/*     */         
/* 582 */         insertTargetFiles(targetFiles, result, targetIndex, this.targetFilePos
/* 583 */             .getPrefix(), this.targetFilePos
/* 584 */             .getSuffix());
/*     */ 
/*     */         
/* 587 */         System.arraycopy(orig, targetIndex, result, targetIndex + targetFiles.length, srcIndex - targetIndex);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 592 */         System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length + targetFiles.length, orig.length - srcIndex);
/*     */ 
/*     */         
/* 595 */         srcIndex += targetFiles.length;
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 601 */       System.arraycopy(orig, 0, result, 0, srcIndex);
/*     */       
/* 603 */       System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length, orig.length - srcIndex);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 608 */     for (int i = 0; i < srcFiles.length; i++) {
/*     */       String src;
/* 610 */       if (this.relative) {
/* 611 */         src = srcFiles[i];
/*     */       } else {
/* 613 */         src = (new File(baseDirs[i], srcFiles[i])).getAbsolutePath();
/*     */       } 
/* 615 */       if (this.forwardSlash && fileSeparator != '/') {
/* 616 */         src = src.replace(fileSeparator, '/');
/*     */       }
/* 618 */       if (this.srcFilePos != null && (
/* 619 */         !this.srcFilePos.getPrefix().isEmpty() || !this.srcFilePos.getSuffix().isEmpty())) {
/* 620 */         src = this.srcFilePos.getPrefix() + src + this.srcFilePos.getSuffix();
/*     */       }
/* 622 */       result[srcIndex + i] = src;
/*     */     } 
/* 624 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getCommandline(String srcFile, File baseDir) {
/* 635 */     return getCommandline(new String[] { srcFile }, new File[] { baseDir });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getFiles(File baseDir, DirectoryScanner ds) {
/* 646 */     return restrict(ds.getIncludedFiles(), baseDir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getDirs(File baseDir, DirectoryScanner ds) {
/* 657 */     return restrict(ds.getIncludedDirectories(), baseDir);
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
/*     */   protected String[] getFilesAndDirs(FileList list) {
/* 669 */     return restrict(list.getFiles(getProject()), list.getDir(getProject()));
/*     */   }
/*     */   
/*     */   private String[] restrict(String[] s, File baseDir) {
/* 673 */     return (this.mapper == null || this.force) ? s : (
/* 674 */       new SourceFileScanner(this)).restrict(s, baseDir, this.destDir, this.mapper);
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
/*     */   protected void runParallel(Execute exe, Vector<String> fileNames, Vector<File> baseDirs) throws IOException, BuildException {
/* 690 */     String[] s = fileNames.<String>toArray(new String[0]);
/* 691 */     File[] b = baseDirs.<File>toArray(new File[0]);
/*     */     
/* 693 */     if (this.maxParallel <= 0 || s.length == 0) {
/* 694 */       String[] command = getCommandline(s, b);
/* 695 */       log(Commandline.describeCommand(command), 3);
/* 696 */       exe.setCommandline(command);
/* 697 */       if (this.redirectorElement != null) {
/* 698 */         setupRedirector();
/* 699 */         this.redirectorElement.configure(this.redirector, null);
/* 700 */         exe.setStreamHandler(this.redirector.createHandler());
/*     */       } 
/* 702 */       runExecute(exe);
/*     */     } else {
/* 704 */       int stillToDo = fileNames.size();
/* 705 */       int currentOffset = 0;
/* 706 */       while (stillToDo > 0) {
/* 707 */         int currentAmount = Math.min(stillToDo, this.maxParallel);
/* 708 */         String[] cs = new String[currentAmount];
/* 709 */         System.arraycopy(s, currentOffset, cs, 0, currentAmount);
/* 710 */         File[] cb = new File[currentAmount];
/* 711 */         System.arraycopy(b, currentOffset, cb, 0, currentAmount);
/* 712 */         String[] command = getCommandline(cs, cb);
/* 713 */         log(Commandline.describeCommand(command), 3);
/* 714 */         exe.setCommandline(command);
/* 715 */         if (this.redirectorElement != null) {
/* 716 */           setupRedirector();
/* 717 */           this.redirectorElement.configure(this.redirector, null);
/*     */         } 
/* 719 */         if (this.redirectorElement != null || currentOffset > 0)
/*     */         {
/*     */ 
/*     */           
/* 723 */           exe.setStreamHandler(this.redirector.createHandler());
/*     */         }
/* 725 */         runExecute(exe);
/*     */         
/* 727 */         stillToDo -= currentAmount;
/* 728 */         currentOffset += currentAmount;
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
/*     */   private static void insertTargetFiles(String[] targetFiles, String[] arguments, int insertPosition, String prefix, String suffix) {
/* 742 */     if (prefix.isEmpty() && suffix.isEmpty()) {
/* 743 */       System.arraycopy(targetFiles, 0, arguments, insertPosition, targetFiles.length);
/*     */     } else {
/*     */       
/* 746 */       for (int i = 0; i < targetFiles.length; i++) {
/* 747 */         arguments[insertPosition + i] = prefix + targetFiles[i] + suffix;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FileDirBoth
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public static final String FILE = "file";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DIR = "dir";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 769 */       return new String[] { "file", "dir", "both" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ExecuteOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */