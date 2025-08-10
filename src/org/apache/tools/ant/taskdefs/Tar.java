/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.nio.file.Files;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.zip.GZIPOutputStream;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.DirectoryScanner;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.types.ArchiveFileSet;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.resources.ArchiveResource;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.resources.TarResource;
/*      */ import org.apache.tools.ant.types.selectors.SelectorUtils;
/*      */ import org.apache.tools.ant.util.FileNameMapper;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.MergingMapper;
/*      */ import org.apache.tools.ant.util.ResourceUtils;
/*      */ import org.apache.tools.ant.util.SourceFileScanner;
/*      */ import org.apache.tools.bzip2.CBZip2OutputStream;
/*      */ import org.apache.tools.tar.TarEntry;
/*      */ import org.apache.tools.tar.TarOutputStream;
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
/*      */ public class Tar
/*      */   extends MatchingTask
/*      */ {
/*      */   private static final int BUFFER_SIZE = 8192;
/*      */   @Deprecated
/*      */   public static final String WARN = "warn";
/*      */   @Deprecated
/*      */   public static final String FAIL = "fail";
/*      */   @Deprecated
/*      */   public static final String TRUNCATE = "truncate";
/*      */   @Deprecated
/*      */   public static final String GNU = "gnu";
/*      */   @Deprecated
/*      */   public static final String OMIT = "omit";
/*      */   File tarFile;
/*      */   File baseDir;
/*  110 */   private TarLongFileMode longFileMode = new TarLongFileMode();
/*      */ 
/*      */   
/*  113 */   Vector<TarFileSet> filesets = new Vector<>();
/*      */ 
/*      */   
/*  116 */   private final List<ResourceCollection> resourceCollections = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean longWarningGiven = false;
/*      */ 
/*      */ 
/*      */   
/*  125 */   private TarCompressionMethod compression = new TarCompressionMethod();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String encoding;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarFileSet createTarFileSet() {
/*  138 */     TarFileSet fs = new TarFileSet();
/*  139 */     fs.setProject(getProject());
/*  140 */     this.filesets.addElement(fs);
/*  141 */     return fs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(ResourceCollection res) {
/*  150 */     this.resourceCollections.add(res);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setTarfile(File tarFile) {
/*  161 */     this.tarFile = tarFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestFile(File destFile) {
/*  170 */     this.tarFile = destFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBasedir(File baseDir) {
/*  178 */     this.baseDir = baseDir;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setLongfile(String mode) {
/*  202 */     log("DEPRECATED - The setLongfile(String) method has been deprecated. Use setLongfile(Tar.TarLongFileMode) instead.");
/*  203 */     this.longFileMode = new TarLongFileMode();
/*  204 */     this.longFileMode.setValue(mode);
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
/*      */   
/*      */   public void setLongfile(TarLongFileMode mode) {
/*  223 */     this.longFileMode = mode;
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
/*      */   public void setCompression(TarCompressionMethod mode) {
/*  238 */     this.compression = mode;
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
/*      */   public void setEncoding(String encoding) {
/*  252 */     this.encoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/*  261 */     if (this.tarFile == null) {
/*  262 */       throw new BuildException("tarfile attribute must be set!", 
/*  263 */           getLocation());
/*      */     }
/*      */     
/*  266 */     if (this.tarFile.exists() && this.tarFile.isDirectory()) {
/*  267 */       throw new BuildException("tarfile is a directory!", 
/*  268 */           getLocation());
/*      */     }
/*      */     
/*  271 */     if (this.tarFile.exists() && !this.tarFile.canWrite()) {
/*  272 */       throw new BuildException("Can not write to the specified tarfile!", 
/*  273 */           getLocation());
/*      */     }
/*      */     
/*  276 */     Vector<TarFileSet> savedFileSets = new Vector<>(this.filesets);
/*      */     try {
/*  278 */       if (this.baseDir != null) {
/*  279 */         if (!this.baseDir.exists()) {
/*  280 */           throw new BuildException("basedir does not exist!", 
/*  281 */               getLocation());
/*      */         }
/*      */ 
/*      */         
/*  285 */         TarFileSet mainFileSet = new TarFileSet(this.fileset);
/*  286 */         mainFileSet.setDir(this.baseDir);
/*  287 */         this.filesets.addElement(mainFileSet);
/*      */       } 
/*      */       
/*  290 */       if (this.filesets.isEmpty() && this.resourceCollections.isEmpty()) {
/*  291 */         throw new BuildException("You must supply either a basedir attribute or some nested resource collections.", 
/*      */             
/*  293 */             getLocation());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  298 */       boolean upToDate = true;
/*  299 */       for (TarFileSet tfs : this.filesets) {
/*  300 */         upToDate &= check((ResourceCollection)tfs);
/*      */       }
/*  302 */       for (ResourceCollection rcol : this.resourceCollections) {
/*  303 */         upToDate &= check(rcol);
/*      */       }
/*      */       
/*  306 */       if (upToDate) {
/*  307 */         log("Nothing to do: " + this.tarFile.getAbsolutePath() + " is up to date.", 2);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  312 */       File parent = this.tarFile.getParentFile();
/*  313 */       if (parent != null && !parent.isDirectory() && 
/*  314 */         !parent.mkdirs() && !parent.isDirectory()) {
/*  315 */         throw new BuildException("Failed to create missing parent directory for %s", new Object[] { this.tarFile });
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  320 */       log("Building tar: " + this.tarFile.getAbsolutePath(), 2);
/*      */ 
/*      */       
/*  323 */       try { TarOutputStream tOut = new TarOutputStream(this.compression.compress(new BufferedOutputStream(
/*  324 */                 Files.newOutputStream(this.tarFile.toPath(), new java.nio.file.OpenOption[0]))), this.encoding);
/*      */         
/*  326 */         try { tOut.setDebug(true);
/*  327 */           if (this.longFileMode.isTruncateMode()) {
/*  328 */             tOut.setLongFileMode(1);
/*  329 */           } else if (this.longFileMode.isFailMode() || this.longFileMode
/*  330 */             .isOmitMode()) {
/*  331 */             tOut.setLongFileMode(0);
/*  332 */           } else if (this.longFileMode.isPosixMode()) {
/*  333 */             tOut.setLongFileMode(3);
/*      */           } else {
/*      */             
/*  336 */             tOut.setLongFileMode(2);
/*      */           } 
/*      */           
/*  339 */           this.longWarningGiven = false;
/*  340 */           for (TarFileSet tfs : this.filesets) {
/*  341 */             tar((ResourceCollection)tfs, tOut);
/*      */           }
/*  343 */           for (ResourceCollection rcol : this.resourceCollections) {
/*  344 */             tar(rcol, tOut);
/*      */           }
/*  346 */           tOut.close(); } catch (Throwable throwable) { try { tOut.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/*  347 */       { String msg = "Problem creating TAR: " + ioe.getMessage();
/*  348 */         throw new BuildException(msg, ioe, getLocation()); }
/*      */     
/*      */     } finally {
/*  351 */       this.filesets = savedFileSets;
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
/*      */   protected void tarFile(File file, TarOutputStream tOut, String vPath, TarFileSet tarFileSet) throws IOException {
/*  366 */     if (file.equals(this.tarFile)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  378 */     tarResource((Resource)new FileResource(file), tOut, vPath, tarFileSet);
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
/*      */   protected void tarResource(Resource r, TarOutputStream tOut, String vPath, TarFileSet tarFileSet) throws IOException {
/*  394 */     if (!r.isExists()) {
/*      */       return;
/*      */     }
/*      */     
/*  398 */     boolean preserveLeadingSlashes = false;
/*      */     
/*  400 */     if (tarFileSet != null) {
/*  401 */       String fullpath = tarFileSet.getFullpath(getProject());
/*  402 */       if (fullpath.isEmpty()) {
/*      */         
/*  404 */         if (vPath.isEmpty()) {
/*      */           return;
/*      */         }
/*      */         
/*  408 */         vPath = getCanonicalPrefix(tarFileSet, getProject()) + vPath;
/*      */       } else {
/*  410 */         vPath = fullpath;
/*      */       } 
/*      */       
/*  413 */       preserveLeadingSlashes = tarFileSet.getPreserveLeadingSlashes();
/*      */       
/*  415 */       if (vPath.startsWith("/") && !preserveLeadingSlashes) {
/*  416 */         int l = vPath.length();
/*  417 */         if (l <= 1) {
/*      */           return;
/*      */         }
/*      */         
/*  421 */         vPath = vPath.substring(1, l);
/*      */       } 
/*      */     } 
/*      */     
/*  425 */     if (r.isDirectory() && !vPath.endsWith("/")) {
/*  426 */       vPath = vPath + "/";
/*      */     }
/*      */     
/*  429 */     if (vPath.length() >= 100) {
/*  430 */       if (this.longFileMode.isOmitMode()) {
/*  431 */         log("Omitting: " + vPath, 2); return;
/*      */       } 
/*  433 */       if (this.longFileMode.isWarnMode()) {
/*  434 */         log("Entry: " + vPath + " longer than " + 'd' + " characters.", 1);
/*      */ 
/*      */         
/*  437 */         if (!this.longWarningGiven) {
/*  438 */           log("Resulting tar file can only be processed successfully by GNU compatible tar commands", 1);
/*      */ 
/*      */           
/*  441 */           this.longWarningGiven = true;
/*      */         } 
/*  443 */       } else if (this.longFileMode.isFailMode()) {
/*  444 */         throw new BuildException("Entry: " + vPath + " longer than " + 'd' + "characters.", 
/*      */             
/*  446 */             getLocation());
/*      */       } 
/*      */     } 
/*      */     
/*  450 */     TarEntry te = new TarEntry(vPath, preserveLeadingSlashes);
/*  451 */     te.setModTime(r.getLastModified());
/*      */     
/*  453 */     if (r instanceof ArchiveResource) {
/*  454 */       ArchiveResource ar = (ArchiveResource)r;
/*  455 */       te.setMode(ar.getMode());
/*  456 */       if (r instanceof TarResource) {
/*  457 */         TarResource tr = (TarResource)r;
/*  458 */         te.setUserName(tr.getUserName());
/*  459 */         te.setUserId(tr.getLongUid());
/*  460 */         te.setGroupName(tr.getGroup());
/*  461 */         te.setGroupId(tr.getLongGid());
/*  462 */         String linkName = tr.getLinkName();
/*  463 */         byte linkFlag = tr.getLinkFlag();
/*  464 */         if (linkFlag == 49 && linkName != null && linkName
/*  465 */           .length() > 0 && !linkName.startsWith("/")) {
/*  466 */           linkName = getCanonicalPrefix(tarFileSet, getProject()) + linkName;
/*      */         }
/*  468 */         te.setLinkName(linkName);
/*  469 */         te.setLinkFlag(linkFlag);
/*      */       } 
/*      */     } 
/*      */     
/*  473 */     if (!r.isDirectory()) {
/*  474 */       if (r.size() > 8589934591L) {
/*  475 */         throw new BuildException("Resource: " + r + " larger than " + 8589934591L + " bytes.");
/*      */       }
/*      */ 
/*      */       
/*  479 */       te.setSize(r.getSize());
/*      */       
/*  481 */       if (tarFileSet != null && tarFileSet.hasFileModeBeenSet()) {
/*  482 */         te.setMode(tarFileSet.getMode());
/*      */       }
/*  484 */     } else if (tarFileSet != null && tarFileSet.hasDirModeBeenSet()) {
/*      */       
/*  486 */       te.setMode(tarFileSet.getDirMode(getProject()));
/*      */     } 
/*      */     
/*  489 */     if (tarFileSet != null) {
/*      */       
/*  491 */       if (tarFileSet.hasUserNameBeenSet()) {
/*  492 */         te.setUserName(tarFileSet.getUserName());
/*      */       }
/*  494 */       if (tarFileSet.hasGroupBeenSet()) {
/*  495 */         te.setGroupName(tarFileSet.getGroup());
/*      */       }
/*  497 */       if (tarFileSet.hasUserIdBeenSet()) {
/*  498 */         te.setUserId(tarFileSet.getUid());
/*      */       }
/*  500 */       if (tarFileSet.hasGroupIdBeenSet()) {
/*  501 */         te.setGroupId(tarFileSet.getGid());
/*      */       }
/*      */     } 
/*      */     
/*  505 */     InputStream in = null;
/*      */     try {
/*  507 */       tOut.putNextEntry(te);
/*      */       
/*  509 */       if (!r.isDirectory()) {
/*  510 */         in = r.getInputStream();
/*      */         
/*  512 */         byte[] buffer = new byte[8192];
/*  513 */         int count = 0;
/*      */         do {
/*  515 */           tOut.write(buffer, 0, count);
/*  516 */           count = in.read(buffer, 0, buffer.length);
/*  517 */         } while (count != -1);
/*      */       } 
/*      */       
/*  520 */       tOut.closeEntry();
/*      */     } finally {
/*  522 */       FileUtils.close(in);
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
/*      */   @Deprecated
/*      */   protected boolean archiveIsUpToDate(String[] files) {
/*  535 */     return archiveIsUpToDate(files, this.baseDir);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean archiveIsUpToDate(String[] files, File dir) {
/*  546 */     SourceFileScanner sfs = new SourceFileScanner(this);
/*  547 */     MergingMapper mm = new MergingMapper();
/*  548 */     mm.setTo(this.tarFile.getAbsolutePath());
/*  549 */     return ((sfs.restrict(files, dir, null, (FileNameMapper)mm)).length == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean archiveIsUpToDate(Resource r) {
/*  559 */     return SelectorUtils.isOutOfDate((Resource)new FileResource(this.tarFile), r, 
/*  560 */         FileUtils.getFileUtils()
/*  561 */         .getFileTimestampGranularity());
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
/*      */   protected boolean supportsNonFileResources() {
/*  576 */     return getClass().equals(Tar.class);
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
/*      */   protected boolean check(ResourceCollection rc) {
/*  594 */     boolean upToDate = true;
/*  595 */     if (isFileFileSet(rc))
/*  596 */     { FileSet fs = (FileSet)rc;
/*  597 */       upToDate = check(fs.getDir(getProject()), getFileNames(fs)); }
/*  598 */     else { if (!rc.isFilesystemOnly() && !supportsNonFileResources())
/*  599 */         throw new BuildException("only filesystem resources are supported"); 
/*  600 */       if (rc.isFilesystemOnly()) {
/*  601 */         Set<File> basedirs = new HashSet<>();
/*  602 */         Map<File, List<String>> basedirToFilesMap = new HashMap<>();
/*  603 */         for (Resource res : rc) {
/*      */           
/*  605 */           FileResource r = ResourceUtils.asFileResource((FileProvider)res.as(FileProvider.class));
/*  606 */           File base = r.getBaseDir();
/*  607 */           if (base == null) {
/*  608 */             base = Copy.NULL_FILE_PLACEHOLDER;
/*      */           }
/*  610 */           basedirs.add(base);
/*  611 */           List<String> files = basedirToFilesMap.computeIfAbsent(base, k -> new Vector());
/*  612 */           if (base == Copy.NULL_FILE_PLACEHOLDER) {
/*  613 */             files.add(r.getFile().getAbsolutePath()); continue;
/*      */           } 
/*  615 */           files.add(r.getName());
/*      */         } 
/*      */         
/*  618 */         for (File base : basedirs) {
/*  619 */           File tmpBase = (base == Copy.NULL_FILE_PLACEHOLDER) ? null : base;
/*  620 */           List<String> files = basedirToFilesMap.get(base);
/*  621 */           upToDate &= check(tmpBase, files);
/*      */         } 
/*      */       } else {
/*  624 */         for (Resource r : rc)
/*  625 */           upToDate = archiveIsUpToDate(r); 
/*      */       }  }
/*      */     
/*  628 */     return upToDate;
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
/*      */   protected boolean check(File basedir, String[] files) {
/*  641 */     boolean upToDate = archiveIsUpToDate(files, basedir);
/*      */     
/*  643 */     for (String file : files) {
/*  644 */       if (this.tarFile.equals(new File(basedir, file))) {
/*  645 */         throw new BuildException("A tar file cannot include itself", 
/*  646 */             getLocation());
/*      */       }
/*      */     } 
/*  649 */     return upToDate;
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
/*      */   protected boolean check(File basedir, Collection<String> files) {
/*  663 */     return check(basedir, files.<String>toArray(new String[0]));
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
/*      */   protected void tar(ResourceCollection rc, TarOutputStream tOut) throws IOException {
/*  679 */     ArchiveFileSet afs = null;
/*  680 */     if (rc instanceof ArchiveFileSet) {
/*  681 */       afs = (ArchiveFileSet)rc;
/*      */     }
/*  683 */     if (afs != null && afs.size() > 1 && 
/*  684 */       !afs.getFullpath(getProject()).isEmpty()) {
/*  685 */       throw new BuildException("fullpath attribute may only be specified for filesets that specify a single file.");
/*      */     }
/*      */     
/*  688 */     TarFileSet tfs = asTarFileSet(afs);
/*      */     
/*  690 */     if (isFileFileSet(rc)) {
/*  691 */       FileSet fs = (FileSet)rc;
/*  692 */       for (String file : getFileNames(fs)) {
/*  693 */         File f = new File(fs.getDir(getProject()), file);
/*  694 */         String name = file.replace(File.separatorChar, '/');
/*  695 */         tarFile(f, tOut, name, tfs);
/*      */       } 
/*  697 */     } else if (rc.isFilesystemOnly()) {
/*  698 */       for (Resource r : rc) {
/*  699 */         File f = ((FileProvider)r.as(FileProvider.class)).getFile();
/*  700 */         tarFile(f, tOut, f.getName(), tfs);
/*      */       } 
/*      */     } else {
/*  703 */       for (Resource r : rc) {
/*  704 */         tarResource(r, tOut, r.getName(), tfs);
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
/*      */   protected static boolean isFileFileSet(ResourceCollection rc) {
/*  717 */     return (rc instanceof FileSet && rc.isFilesystemOnly());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String[] getFileNames(FileSet fs) {
/*  728 */     DirectoryScanner ds = fs.getDirectoryScanner(fs.getProject());
/*  729 */     String[] directories = ds.getIncludedDirectories();
/*  730 */     String[] filesPerSe = ds.getIncludedFiles();
/*  731 */     String[] files = new String[directories.length + filesPerSe.length];
/*  732 */     System.arraycopy(directories, 0, files, 0, directories.length);
/*  733 */     System.arraycopy(filesPerSe, 0, files, directories.length, filesPerSe.length);
/*      */     
/*  735 */     return files;
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
/*      */   protected TarFileSet asTarFileSet(ArchiveFileSet archiveFileSet) {
/*      */     TarFileSet tfs;
/*  749 */     if (archiveFileSet instanceof TarFileSet) {
/*  750 */       tfs = (TarFileSet)archiveFileSet;
/*      */     } else {
/*  752 */       tfs = new TarFileSet();
/*  753 */       tfs.setProject(getProject());
/*  754 */       if (archiveFileSet != null) {
/*  755 */         tfs.setPrefix(archiveFileSet.getPrefix(getProject()));
/*  756 */         tfs.setFullpath(archiveFileSet.getFullpath(getProject()));
/*  757 */         if (archiveFileSet.hasFileModeBeenSet()) {
/*  758 */           tfs.integerSetFileMode(archiveFileSet
/*  759 */               .getFileMode(getProject()));
/*      */         }
/*  761 */         if (archiveFileSet.hasDirModeBeenSet()) {
/*  762 */           tfs.integerSetDirMode(archiveFileSet
/*  763 */               .getDirMode(getProject()));
/*      */         }
/*      */         
/*  766 */         if (archiveFileSet instanceof org.apache.tools.ant.types.TarFileSet) {
/*      */           
/*  768 */           org.apache.tools.ant.types.TarFileSet t = (org.apache.tools.ant.types.TarFileSet)archiveFileSet;
/*      */           
/*  770 */           if (t.hasUserNameBeenSet()) {
/*  771 */             tfs.setUserName(t.getUserName());
/*      */           }
/*  773 */           if (t.hasGroupBeenSet()) {
/*  774 */             tfs.setGroup(t.getGroup());
/*      */           }
/*  776 */           if (t.hasUserIdBeenSet()) {
/*  777 */             tfs.setUid(t.getUid());
/*      */           }
/*  779 */           if (t.hasGroupIdBeenSet()) {
/*  780 */             tfs.setGid(t.getGid());
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  785 */     return tfs;
/*      */   }
/*      */   
/*      */   private static String getCanonicalPrefix(TarFileSet tarFileSet, Project project) {
/*  789 */     String prefix = tarFileSet.getPrefix(project);
/*      */     
/*  791 */     if (prefix.isEmpty() || prefix.endsWith("/")) {
/*  792 */       return prefix;
/*      */     }
/*  794 */     return prefix = prefix + "/";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class TarFileSet
/*      */     extends org.apache.tools.ant.types.TarFileSet
/*      */   {
/*  803 */     private String[] files = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean preserveLeadingSlashes = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TarFileSet(FileSet fileset) {
/*  814 */       super(fileset);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TarFileSet() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getFiles(Project p) {
/*  832 */       if (this.files == null) {
/*  833 */         this.files = Tar.getFileNames((FileSet)this);
/*      */       }
/*  835 */       return this.files;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setMode(String octalString) {
/*  845 */       setFileMode(octalString);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getMode() {
/*  852 */       return getFileMode(getProject());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreserveLeadingSlashes(boolean b) {
/*  862 */       this.preserveLeadingSlashes = b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getPreserveLeadingSlashes() {
/*  869 */       return this.preserveLeadingSlashes;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class TarLongFileMode
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public static final String WARN = "warn";
/*      */     
/*      */     public static final String FAIL = "fail";
/*      */     
/*      */     public static final String TRUNCATE = "truncate";
/*      */     
/*      */     public static final String GNU = "gnu";
/*      */     
/*      */     public static final String POSIX = "posix";
/*      */     
/*      */     public static final String OMIT = "omit";
/*  888 */     private static final String[] VALID_MODES = new String[] { "warn", "fail", "truncate", "gnu", "posix", "omit" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TarLongFileMode() {
/*  895 */       setValue("warn");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/*  903 */       return VALID_MODES;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isTruncateMode() {
/*  910 */       return "truncate".equalsIgnoreCase(getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isWarnMode() {
/*  917 */       return "warn".equalsIgnoreCase(getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isGnuMode() {
/*  924 */       return "gnu".equalsIgnoreCase(getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isFailMode() {
/*  931 */       return "fail".equalsIgnoreCase(getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isOmitMode() {
/*  938 */       return "omit".equalsIgnoreCase(getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isPosixMode() {
/*  945 */       return "posix".equalsIgnoreCase(getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class TarCompressionMethod
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     private static final String NONE = "none";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String GZIP = "gzip";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String BZIP2 = "bzip2";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String XZ = "xz";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TarCompressionMethod() {
/*  979 */       setValue("none");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/*  988 */       return new String[] { "none", "gzip", "bzip2", "xz" };
/*      */     }
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
/*      */     private OutputStream compress(OutputStream ostream) throws IOException {
/* 1001 */       String v = getValue();
/* 1002 */       if ("gzip".equals(v)) {
/* 1003 */         return new GZIPOutputStream(ostream);
/*      */       }
/* 1005 */       if ("xz".equals(v)) {
/* 1006 */         return newXZOutputStream(ostream);
/*      */       }
/* 1008 */       if ("bzip2".equals(v)) {
/* 1009 */         ostream.write(66);
/* 1010 */         ostream.write(90);
/* 1011 */         return (OutputStream)new CBZip2OutputStream(ostream);
/*      */       } 
/* 1013 */       return ostream;
/*      */     }
/*      */ 
/*      */     
/*      */     private static OutputStream newXZOutputStream(OutputStream ostream) throws BuildException {
/*      */       try {
/* 1019 */         Class<?> fClazz = Class.forName("org.tukaani.xz.FilterOptions");
/* 1020 */         Class<?> oClazz = Class.forName("org.tukaani.xz.LZMA2Options");
/*      */ 
/*      */         
/* 1023 */         Class<? extends OutputStream> sClazz = Class.forName("org.tukaani.xz.XZOutputStream").asSubclass(OutputStream.class);
/*      */         
/* 1025 */         Constructor<? extends OutputStream> c = sClazz.getConstructor(new Class[] { OutputStream.class, fClazz });
/* 1026 */         return c.newInstance(new Object[] { ostream, oClazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]) });
/* 1027 */       } catch (ClassNotFoundException ex) {
/* 1028 */         throw new BuildException("xz compression requires the XZ for Java library", ex);
/*      */       }
/* 1030 */       catch (NoSuchMethodException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1035 */         throw new BuildException("failed to create XZOutputStream", ex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Tar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */