/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.DirectoryScanner;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.FilterChain;
/*      */ import org.apache.tools.ant.types.FilterSet;
/*      */ import org.apache.tools.ant.types.FilterSetCollection;
/*      */ import org.apache.tools.ant.types.Mapper;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.util.FileNameMapper;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.FlatFileNameMapper;
/*      */ import org.apache.tools.ant.util.IdentityMapper;
/*      */ import org.apache.tools.ant.util.LinkedHashtable;
/*      */ import org.apache.tools.ant.util.ResourceUtils;
/*      */ import org.apache.tools.ant.util.SourceFileScanner;
/*      */ import org.apache.tools.ant.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Copy
/*      */   extends Task
/*      */ {
/*      */   private static final String MSG_WHEN_COPYING_EMPTY_RC_TO_FILE = "Cannot perform operation from directory to file.";
/*      */   @Deprecated
/*   75 */   static final String LINE_SEPARATOR = StringUtils.LINE_SEP;
/*   76 */   static final File NULL_FILE_PLACEHOLDER = new File("/NULL_FILE");
/*      */   
/*   78 */   protected File file = null;
/*   79 */   protected File destFile = null;
/*   80 */   protected File destDir = null;
/*   81 */   protected Vector<ResourceCollection> rcs = new Vector<>();
/*      */   
/*   83 */   protected Vector<ResourceCollection> filesets = this.rcs;
/*      */   
/*      */   private boolean enableMultipleMappings = false;
/*      */   protected boolean filtering = false;
/*      */   protected boolean preserveLastModified = false;
/*      */   protected boolean forceOverwrite = false;
/*      */   protected boolean flatten = false;
/*   90 */   protected int verbosity = 3;
/*      */   
/*      */   protected boolean includeEmpty = true;
/*      */   protected boolean failonerror = true;
/*   94 */   protected Hashtable<String, String[]> fileCopyMap = (Hashtable<String, String[]>)new LinkedHashtable();
/*   95 */   protected Hashtable<String, String[]> dirCopyMap = (Hashtable<String, String[]>)new LinkedHashtable();
/*   96 */   protected Hashtable<File, File> completeDirMap = (Hashtable<File, File>)new LinkedHashtable();
/*      */   
/*   98 */   protected Mapper mapperElement = null;
/*      */   
/*      */   protected FileUtils fileUtils;
/*  101 */   private final Vector<FilterChain> filterChains = new Vector<>();
/*  102 */   private final Vector<FilterSet> filterSets = new Vector<>();
/*  103 */   private String inputEncoding = null;
/*  104 */   private String outputEncoding = null;
/*  105 */   private long granularity = 0L;
/*      */   
/*      */   private boolean force = false;
/*      */   
/*      */   private boolean quiet = false;
/*      */   
/*  111 */   private Resource singleResource = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Copy() {
/*  117 */     this.fileUtils = FileUtils.getFileUtils();
/*  118 */     this.granularity = this.fileUtils.getFileTimestampGranularity();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected FileUtils getFileUtils() {
/*  126 */     return this.fileUtils;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFile(File file) {
/*  134 */     this.file = file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTofile(File destFile) {
/*  142 */     this.destFile = destFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTodir(File destDir) {
/*  150 */     this.destDir = destDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FilterChain createFilterChain() {
/*  158 */     FilterChain filterChain = new FilterChain();
/*  159 */     this.filterChains.addElement(filterChain);
/*  160 */     return filterChain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FilterSet createFilterSet() {
/*  168 */     FilterSet filterSet = new FilterSet();
/*  169 */     this.filterSets.addElement(filterSet);
/*  170 */     return filterSet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setPreserveLastModified(String preserve) {
/*  183 */     setPreserveLastModified(Project.toBoolean(preserve));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPreserveLastModified(boolean preserve) {
/*  191 */     this.preserveLastModified = preserve;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getPreserveLastModified() {
/*  202 */     return this.preserveLastModified;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<FilterSet> getFilterSets() {
/*  211 */     return this.filterSets;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<FilterChain> getFilterChains() {
/*  220 */     return this.filterChains;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFiltering(boolean filtering) {
/*  228 */     this.filtering = filtering;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOverwrite(boolean overwrite) {
/*  238 */     this.forceOverwrite = overwrite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setForce(boolean f) {
/*  250 */     this.force = f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getForce() {
/*  260 */     return this.force;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFlatten(boolean flatten) {
/*  273 */     this.flatten = flatten;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVerbose(boolean verbose) {
/*  282 */     this.verbosity = verbose ? 2 : 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIncludeEmptyDirs(boolean includeEmpty) {
/*  290 */     this.includeEmpty = includeEmpty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setQuiet(boolean quiet) {
/*  302 */     this.quiet = quiet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnableMultipleMappings(boolean enableMultipleMappings) {
/*  317 */     this.enableMultipleMappings = enableMultipleMappings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableMultipleMapping() {
/*  325 */     return this.enableMultipleMappings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailOnError(boolean failonerror) {
/*  334 */     this.failonerror = failonerror;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFileset(FileSet set) {
/*  342 */     add((ResourceCollection)set);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(ResourceCollection res) {
/*  351 */     this.rcs.add(res);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Mapper createMapper() throws BuildException {
/*  360 */     if (this.mapperElement != null) {
/*  361 */       throw new BuildException("Cannot define more than one mapper", 
/*  362 */           getLocation());
/*      */     }
/*  364 */     this.mapperElement = new Mapper(getProject());
/*  365 */     return this.mapperElement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(FileNameMapper fileNameMapper) {
/*  374 */     createMapper().add(fileNameMapper);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(String encoding) {
/*  383 */     this.inputEncoding = encoding;
/*  384 */     if (this.outputEncoding == null) {
/*  385 */       this.outputEncoding = encoding;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEncoding() {
/*  396 */     return this.inputEncoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutputEncoding(String encoding) {
/*  405 */     this.outputEncoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOutputEncoding() {
/*  416 */     return this.outputEncoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGranularity(long granularity) {
/*  429 */     this.granularity = granularity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/*  438 */     File savedFile = this.file;
/*  439 */     File savedDestFile = this.destFile;
/*  440 */     File savedDestDir = this.destDir;
/*  441 */     ResourceCollection savedRc = null;
/*  442 */     if (this.file == null && this.destFile != null && this.rcs.size() == 1)
/*      */     {
/*  444 */       savedRc = this.rcs.elementAt(0);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*      */       try {
/*  450 */         validateAttributes();
/*  451 */       } catch (BuildException e) {
/*  452 */         if (this.failonerror || 
/*      */           
/*  454 */           !getMessage((Exception)e).equals("Cannot perform operation from directory to file.")) {
/*  455 */           throw e;
/*      */         }
/*  457 */         log("Warning: " + getMessage((Exception)e), 0);
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  463 */       copySingleFile();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  482 */       Map<File, List<String>> filesByBasedir = new HashMap<>();
/*  483 */       Map<File, List<String>> dirsByBasedir = new HashMap<>();
/*  484 */       Set<File> baseDirs = new HashSet<>();
/*  485 */       List<Resource> nonFileResources = new ArrayList<>();
/*      */       
/*  487 */       for (ResourceCollection rc : this.rcs) {
/*      */ 
/*      */         
/*  490 */         if (rc instanceof FileSet && rc.isFilesystemOnly()) {
/*  491 */           DirectoryScanner ds; FileSet fs = (FileSet)rc;
/*      */           
/*      */           try {
/*  494 */             ds = fs.getDirectoryScanner(getProject());
/*  495 */           } catch (BuildException e) {
/*  496 */             if (this.failonerror || 
/*  497 */               !getMessage((Exception)e).endsWith(" does not exist."))
/*      */             {
/*  499 */               throw e;
/*      */             }
/*  501 */             if (!this.quiet) {
/*  502 */               log("Warning: " + getMessage((Exception)e), 0);
/*      */             }
/*      */             continue;
/*      */           } 
/*  506 */           File fromDir = fs.getDir(getProject());
/*      */           
/*  508 */           if (!this.flatten && this.mapperElement == null && ds
/*  509 */             .isEverythingIncluded() && !fs.hasPatterns()) {
/*  510 */             this.completeDirMap.put(fromDir, this.destDir);
/*      */           }
/*  512 */           add(fromDir, ds.getIncludedFiles(), filesByBasedir);
/*  513 */           add(fromDir, ds.getIncludedDirectories(), dirsByBasedir);
/*  514 */           baseDirs.add(fromDir);
/*      */           continue;
/*      */         } 
/*  517 */         if (!rc.isFilesystemOnly() && !supportsNonFileResources()) {
/*  518 */           throw new BuildException("Only FileSystem resources are supported.");
/*      */         }
/*      */ 
/*      */         
/*  522 */         for (Resource r : rc) {
/*  523 */           if (!r.isExists()) {
/*      */             
/*  525 */             String message = "Warning: Could not find resource " + r.toLongString() + " to copy.";
/*  526 */             if (!this.failonerror) {
/*  527 */               if (!this.quiet)
/*  528 */                 log(message, 0); 
/*      */               continue;
/*      */             } 
/*  531 */             throw new BuildException(message);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  536 */           File baseDir = NULL_FILE_PLACEHOLDER;
/*  537 */           String name = r.getName();
/*  538 */           FileProvider fp = (FileProvider)r.as(FileProvider.class);
/*  539 */           if (fp != null) {
/*  540 */             FileResource fr = ResourceUtils.asFileResource(fp);
/*  541 */             baseDir = getKeyFile(fr.getBaseDir());
/*  542 */             if (fr.getBaseDir() == null) {
/*  543 */               name = fr.getFile().getAbsolutePath();
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  550 */           if (r.isDirectory() || fp != null) {
/*  551 */             add(baseDir, name, 
/*  552 */                 r.isDirectory() ? dirsByBasedir : 
/*  553 */                 filesByBasedir);
/*  554 */             baseDirs.add(baseDir);
/*      */             continue;
/*      */           } 
/*  557 */           nonFileResources.add(r);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  563 */       iterateOverBaseDirs(baseDirs, dirsByBasedir, filesByBasedir);
/*      */ 
/*      */       
/*      */       try {
/*  567 */         doFileOperations();
/*  568 */       } catch (BuildException e) {
/*  569 */         if (!this.failonerror) {
/*  570 */           if (!this.quiet) {
/*  571 */             log("Warning: " + getMessage((Exception)e), 0);
/*      */           }
/*      */         } else {
/*  574 */           throw e;
/*      */         } 
/*      */       } 
/*      */       
/*  578 */       if (!nonFileResources.isEmpty() || this.singleResource != null) {
/*      */         
/*  580 */         Resource[] nonFiles = nonFileResources.<Resource>toArray(new Resource[0]);
/*      */         
/*  582 */         Map<Resource, String[]> map = scan(nonFiles, this.destDir);
/*  583 */         if (this.singleResource != null) {
/*  584 */           map.put(this.singleResource, new String[] { this.destFile
/*  585 */                 .getAbsolutePath() });
/*      */         }
/*      */         try {
/*  588 */           doResourceOperations(map);
/*  589 */         } catch (BuildException e) {
/*  590 */           if (!this.failonerror) {
/*  591 */             if (!this.quiet) {
/*  592 */               log("Warning: " + getMessage((Exception)e), 0);
/*      */             }
/*      */           } else {
/*  595 */             throw e;
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } finally {
/*      */       
/*  602 */       this.singleResource = null;
/*  603 */       this.file = savedFile;
/*  604 */       this.destFile = savedDestFile;
/*  605 */       this.destDir = savedDestDir;
/*  606 */       if (savedRc != null) {
/*  607 */         this.rcs.insertElementAt(savedRc, 0);
/*      */       }
/*  609 */       this.fileCopyMap.clear();
/*  610 */       this.dirCopyMap.clear();
/*  611 */       this.completeDirMap.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void copySingleFile() {
/*  621 */     if (this.file != null) {
/*  622 */       if (this.file.exists()) {
/*  623 */         if (this.destFile == null) {
/*  624 */           this.destFile = new File(this.destDir, this.file.getName());
/*      */         }
/*  626 */         if (this.forceOverwrite || !this.destFile.exists() || this.file
/*  627 */           .lastModified() - this.granularity > this.destFile
/*  628 */           .lastModified()) {
/*  629 */           this.fileCopyMap.put(this.file.getAbsolutePath(), new String[] { this.destFile
/*  630 */                 .getAbsolutePath() });
/*      */         } else {
/*  632 */           log(this.file + " omitted as " + this.destFile + " is up to date.", 3);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  637 */         String message = "Warning: Could not find file " + this.file.getAbsolutePath() + " to copy.";
/*  638 */         if (!this.failonerror) {
/*  639 */           if (!this.quiet) {
/*  640 */             log(message, 0);
/*      */           }
/*      */         } else {
/*  643 */           throw new BuildException(message);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void iterateOverBaseDirs(Set<File> baseDirs, Map<File, List<String>> dirsByBasedir, Map<File, List<String>> filesByBasedir) {
/*  653 */     for (File f : baseDirs) {
/*  654 */       List<String> files = filesByBasedir.get(f);
/*  655 */       List<String> dirs = dirsByBasedir.get(f);
/*      */       
/*  657 */       String[] srcFiles = new String[0];
/*  658 */       if (files != null) {
/*  659 */         srcFiles = files.<String>toArray(srcFiles);
/*      */       }
/*  661 */       String[] srcDirs = new String[0];
/*  662 */       if (dirs != null) {
/*  663 */         srcDirs = dirs.<String>toArray(srcDirs);
/*      */       }
/*  665 */       scan((f == NULL_FILE_PLACEHOLDER) ? null : f, this.destDir, srcFiles, srcDirs);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void validateAttributes() throws BuildException {
/*  677 */     if (this.file == null && this.rcs.isEmpty()) {
/*  678 */       throw new BuildException("Specify at least one source--a file or a resource collection.");
/*      */     }
/*      */     
/*  681 */     if (this.destFile != null && this.destDir != null) {
/*  682 */       throw new BuildException("Only one of tofile and todir may be set.");
/*      */     }
/*      */     
/*  685 */     if (this.destFile == null && this.destDir == null) {
/*  686 */       throw new BuildException("One of tofile or todir must be set.");
/*      */     }
/*  688 */     if (this.file != null && this.file.isDirectory()) {
/*  689 */       throw new BuildException("Use a resource collection to copy directories.");
/*      */     }
/*  691 */     if (this.destFile != null && !this.rcs.isEmpty()) {
/*  692 */       if (this.rcs.size() > 1) {
/*  693 */         throw new BuildException("Cannot concatenate multiple files into a single file.");
/*      */       }
/*      */       
/*  696 */       ResourceCollection rc = this.rcs.elementAt(0);
/*  697 */       if (!rc.isFilesystemOnly() && !supportsNonFileResources()) {
/*  698 */         throw new BuildException("Only FileSystem resources are supported.");
/*      */       }
/*      */       
/*  701 */       if (rc.isEmpty()) {
/*  702 */         throw new BuildException("Cannot perform operation from directory to file.");
/*      */       }
/*  704 */       if (rc.size() == 1) {
/*  705 */         Resource res = rc.iterator().next();
/*  706 */         FileProvider r = (FileProvider)res.as(FileProvider.class);
/*  707 */         if (this.file == null) {
/*  708 */           if (r != null) {
/*  709 */             this.file = r.getFile();
/*      */           } else {
/*  711 */             this.singleResource = res;
/*      */           } 
/*  713 */           this.rcs.removeElementAt(0);
/*      */         } else {
/*  715 */           throw new BuildException("Cannot concatenate multiple files into a single file.");
/*      */         } 
/*      */       } else {
/*      */         
/*  719 */         throw new BuildException("Cannot concatenate multiple files into a single file.");
/*      */       } 
/*      */     } 
/*      */     
/*  723 */     if (this.destFile != null) {
/*  724 */       this.destDir = this.destFile.getParentFile();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void scan(File fromDir, File toDir, String[] files, String[] dirs) {
/*  739 */     FileNameMapper mapper = getMapper();
/*  740 */     buildMap(fromDir, toDir, files, mapper, this.fileCopyMap);
/*      */     
/*  742 */     if (this.includeEmpty) {
/*  743 */       buildMap(fromDir, toDir, dirs, mapper, this.dirCopyMap);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Map<Resource, String[]> scan(Resource[] fromResources, File toDir) {
/*  760 */     return buildMap(fromResources, toDir, getMapper());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void buildMap(File fromDir, File toDir, String[] names, FileNameMapper mapper, Hashtable<String, String[]> map) {
/*  774 */     String[] toCopy = null;
/*  775 */     if (this.forceOverwrite) {
/*  776 */       List<String> v = new ArrayList<>();
/*  777 */       for (String name : names) {
/*  778 */         if (mapper.mapFileName(name) != null) {
/*  779 */           v.add(name);
/*      */         }
/*      */       } 
/*  782 */       toCopy = v.<String>toArray(new String[0]);
/*      */     } else {
/*  784 */       SourceFileScanner ds = new SourceFileScanner(this);
/*  785 */       toCopy = ds.restrict(names, fromDir, toDir, mapper, this.granularity);
/*      */     } 
/*  787 */     for (String name : toCopy) {
/*  788 */       File src = new File(fromDir, name);
/*  789 */       String[] mappedFiles = mapper.mapFileName(name);
/*  790 */       if (mappedFiles != null && mappedFiles.length != 0)
/*      */       {
/*      */ 
/*      */         
/*  794 */         if (!this.enableMultipleMappings) {
/*  795 */           map.put(src.getAbsolutePath(), new String[] { (new File(toDir, mappedFiles[0]))
/*  796 */                 .getAbsolutePath() });
/*      */         } else {
/*      */           
/*  799 */           for (int k = 0; k < mappedFiles.length; k++) {
/*  800 */             mappedFiles[k] = (new File(toDir, mappedFiles[k])).getAbsolutePath();
/*      */           }
/*  802 */           map.put(src.getAbsolutePath(), mappedFiles);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Map<Resource, String[]> buildMap(Resource[] fromResources, File toDir, FileNameMapper mapper) {
/*      */     Resource[] toCopy;
/*  818 */     Map<Resource, String[]> map = (Map)new HashMap<>();
/*      */     
/*  820 */     if (this.forceOverwrite) {
/*  821 */       List<Resource> v = new ArrayList<>();
/*  822 */       for (Resource rc : fromResources) {
/*  823 */         if (mapper.mapFileName(rc.getName()) != null) {
/*  824 */           v.add(rc);
/*      */         }
/*      */       } 
/*  827 */       toCopy = v.<Resource>toArray(new Resource[0]);
/*      */     } else {
/*  829 */       toCopy = ResourceUtils.selectOutOfDateSources((ProjectComponent)this, fromResources, mapper, name -> new FileResource(toDir, name), this.granularity);
/*      */     } 
/*      */     
/*  832 */     for (Resource rc : toCopy) {
/*  833 */       String[] mappedFiles = mapper.mapFileName(rc.getName());
/*  834 */       if (mappedFiles == null || mappedFiles.length == 0) {
/*  835 */         throw new BuildException("Can't copy a resource without a name if the mapper doesn't provide one.");
/*      */       }
/*      */ 
/*      */       
/*  839 */       if (!this.enableMultipleMappings) {
/*  840 */         map.put(rc, new String[] { (new File(toDir, mappedFiles[0])).getAbsolutePath() });
/*      */       } else {
/*      */         
/*  843 */         for (int k = 0; k < mappedFiles.length; k++) {
/*  844 */           mappedFiles[k] = (new File(toDir, mappedFiles[k])).getAbsolutePath();
/*      */         }
/*  846 */         map.put(rc, mappedFiles);
/*      */       } 
/*      */     } 
/*  849 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doFileOperations() {
/*  857 */     if (!this.fileCopyMap.isEmpty()) {
/*  858 */       log("Copying " + this.fileCopyMap.size() + " file" + (
/*  859 */           (this.fileCopyMap.size() == 1) ? "" : "s") + " to " + this.destDir
/*  860 */           .getAbsolutePath());
/*      */       
/*  862 */       for (Map.Entry<String, String[]> e : this.fileCopyMap.entrySet()) {
/*  863 */         String fromFile = e.getKey();
/*      */         
/*  865 */         for (String toFile : (String[])e.getValue()) {
/*  866 */           if (fromFile.equals(toFile)) {
/*  867 */             log("Skipping self-copy of " + fromFile, this.verbosity);
/*      */           } else {
/*      */             
/*      */             try {
/*  871 */               log("Copying " + fromFile + " to " + toFile, this.verbosity);
/*      */               
/*  873 */               FilterSetCollection executionFilters = new FilterSetCollection();
/*      */               
/*  875 */               if (this.filtering) {
/*  876 */                 executionFilters
/*  877 */                   .addFilterSet(getProject().getGlobalFilterSet());
/*      */               }
/*  879 */               for (FilterSet filterSet : this.filterSets) {
/*  880 */                 executionFilters.addFilterSet(filterSet);
/*      */               }
/*  882 */               this.fileUtils.copyFile(new File(fromFile), new File(toFile), executionFilters, this.filterChains, this.forceOverwrite, this.preserveLastModified, false, this.inputEncoding, this.outputEncoding, 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*  887 */                   getProject(), 
/*  888 */                   getForce());
/*  889 */             } catch (IOException ioe) {
/*      */               
/*  891 */               String msg = "Failed to copy " + fromFile + " to " + toFile + " due to " + getDueTo(ioe);
/*  892 */               File targetFile = new File(toFile);
/*  893 */               if (!(ioe instanceof ResourceUtils.ReadOnlyTargetFileException) && targetFile
/*      */                 
/*  895 */                 .exists() && !targetFile.delete()) {
/*  896 */                 msg = msg + " and I couldn't delete the corrupt " + toFile;
/*      */               }
/*  898 */               if (this.failonerror) {
/*  899 */                 throw new BuildException(msg, ioe, getLocation());
/*      */               }
/*  901 */               log(msg, 0);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  906 */     }  if (this.includeEmpty) {
/*  907 */       int createCount = 0;
/*  908 */       for (String[] dirs : this.dirCopyMap.values()) {
/*  909 */         for (String dir : dirs) {
/*  910 */           File d = new File(dir);
/*  911 */           if (!d.exists()) {
/*  912 */             if (!d.mkdirs() && !d.isDirectory()) {
/*  913 */               log("Unable to create directory " + d
/*  914 */                   .getAbsolutePath(), 0);
/*      */             } else {
/*  916 */               createCount++;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*  921 */       if (createCount > 0) {
/*  922 */         log("Copied " + this.dirCopyMap.size() + " empty director" + (
/*      */             
/*  924 */             (this.dirCopyMap.size() == 1) ? "y" : "ies") + " to " + createCount + " empty director" + (
/*      */ 
/*      */             
/*  927 */             (createCount == 1) ? "y" : "ies") + " under " + this.destDir
/*  928 */             .getAbsolutePath());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doResourceOperations(Map<Resource, String[]> map) {
/*  940 */     if (!map.isEmpty()) {
/*  941 */       log("Copying " + map.size() + " resource" + (
/*  942 */           (map.size() == 1) ? "" : "s") + " to " + this.destDir
/*  943 */           .getAbsolutePath());
/*      */       
/*  945 */       for (Map.Entry<Resource, String[]> e : map.entrySet()) {
/*  946 */         Resource fromResource = e.getKey();
/*  947 */         for (String toFile : (String[])e.getValue()) {
/*      */           try {
/*  949 */             log("Copying " + fromResource + " to " + toFile, this.verbosity);
/*      */ 
/*      */             
/*  952 */             FilterSetCollection executionFilters = new FilterSetCollection();
/*  953 */             if (this.filtering) {
/*  954 */               executionFilters
/*  955 */                 .addFilterSet(getProject().getGlobalFilterSet());
/*      */             }
/*  957 */             for (FilterSet filterSet : this.filterSets) {
/*  958 */               executionFilters.addFilterSet(filterSet);
/*      */             }
/*  960 */             ResourceUtils.copyResource(fromResource, (Resource)new FileResource(this.destDir, toFile), executionFilters, this.filterChains, this.forceOverwrite, this.preserveLastModified, false, this.inputEncoding, this.outputEncoding, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  970 */                 getProject(), 
/*  971 */                 getForce());
/*  972 */           } catch (IOException ioe) {
/*      */ 
/*      */             
/*  975 */             String msg = "Failed to copy " + fromResource + " to " + toFile + " due to " + getDueTo(ioe);
/*  976 */             File targetFile = new File(toFile);
/*  977 */             if (!(ioe instanceof ResourceUtils.ReadOnlyTargetFileException) && targetFile
/*      */               
/*  979 */               .exists() && !targetFile.delete()) {
/*  980 */               msg = msg + " and I couldn't delete the corrupt " + toFile;
/*      */             }
/*  982 */             if (this.failonerror) {
/*  983 */               throw new BuildException(msg, ioe, getLocation());
/*      */             }
/*  985 */             log(msg, 0);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean supportsNonFileResources() {
/* 1007 */     return getClass().equals(Copy.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void add(File baseDir, String[] names, Map<File, List<String>> m) {
/* 1015 */     if (names != null) {
/* 1016 */       baseDir = getKeyFile(baseDir);
/* 1017 */       List<String> l = m.computeIfAbsent(baseDir, k -> new ArrayList(names.length));
/* 1018 */       l.addAll(Arrays.asList(names));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void add(File baseDir, String name, Map<File, List<String>> m) {
/* 1027 */     if (name != null) {
/* 1028 */       add(baseDir, new String[] { name }, m);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static File getKeyFile(File f) {
/* 1036 */     return (f == null) ? NULL_FILE_PLACEHOLDER : f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FileNameMapper getMapper() {
/*      */     IdentityMapper identityMapper;
/* 1044 */     FileNameMapper mapper = null;
/* 1045 */     if (this.mapperElement != null) {
/* 1046 */       mapper = this.mapperElement.getImplementation();
/* 1047 */     } else if (this.flatten) {
/* 1048 */       FlatFileNameMapper flatFileNameMapper = new FlatFileNameMapper();
/*      */     } else {
/* 1050 */       identityMapper = new IdentityMapper();
/*      */     } 
/* 1052 */     return (FileNameMapper)identityMapper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getMessage(Exception ex) {
/* 1062 */     return (ex.getMessage() == null) ? ex.toString() : ex.getMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getDueTo(Exception ex) {
/* 1073 */     boolean baseIOException = (ex.getClass() == IOException.class);
/* 1074 */     StringBuilder message = new StringBuilder();
/* 1075 */     if (!baseIOException || ex.getMessage() == null) {
/* 1076 */       message.append(ex.getClass().getName());
/*      */     }
/* 1078 */     if (ex.getMessage() != null) {
/* 1079 */       if (!baseIOException) {
/* 1080 */         message.append(" ");
/*      */       }
/* 1082 */       message.append(ex.getMessage());
/*      */     } 
/* 1084 */     if (ex.getClass().getName().contains("MalformedInput"))
/* 1085 */       message.append(String.format("%nThis is normally due to the input file containing invalid%nbytes for the character encoding used : %s%n", new Object[] {
/*      */ 
/*      */               
/* 1088 */               (this.inputEncoding == null) ? this.fileUtils.getDefaultEncoding() : this.inputEncoding
/*      */             })); 
/* 1090 */     return message.toString();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Copy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */