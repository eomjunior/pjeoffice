/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.file.Files;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import java.util.stream.Stream;
/*      */ import java.util.zip.CRC32;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.DirectoryScanner;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.types.ArchiveFileSet;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.ResourceFactory;
/*      */ import org.apache.tools.ant.types.ZipFileSet;
/*      */ import org.apache.tools.ant.types.ZipScanner;
/*      */ import org.apache.tools.ant.types.resources.ArchiveResource;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.resources.Union;
/*      */ import org.apache.tools.ant.types.resources.ZipResource;
/*      */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*      */ import org.apache.tools.ant.util.DateUtils;
/*      */ import org.apache.tools.ant.util.FileNameMapper;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.GlobPatternMapper;
/*      */ import org.apache.tools.ant.util.IdentityMapper;
/*      */ import org.apache.tools.ant.util.MergingMapper;
/*      */ import org.apache.tools.ant.util.ResourceUtils;
/*      */ import org.apache.tools.zip.Zip64Mode;
/*      */ import org.apache.tools.zip.ZipEntry;
/*      */ import org.apache.tools.zip.ZipExtraField;
/*      */ import org.apache.tools.zip.ZipFile;
/*      */ import org.apache.tools.zip.ZipOutputStream;
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
/*      */ public class Zip
/*      */   extends MatchingTask
/*      */ {
/*      */   private static final int BUFFER_SIZE = 8192;
/*      */   private static final int ZIP_FILE_TIMESTAMP_GRANULARITY = 2000;
/*      */   private static final int ROUNDUP_MILLIS = 1999;
/*   87 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */ 
/*      */   
/*   90 */   private static final long EMPTY_CRC = (new CRC32()).getValue(); private static final ResourceSelector MISSING_SELECTOR;
/*      */   static {
/*   92 */     MISSING_SELECTOR = (target -> !target.isExists());
/*      */   }
/*      */ 
/*      */   
/*      */   private static final ResourceUtils.ResourceSelectorProvider MISSING_DIR_PROVIDER = sr -> MISSING_SELECTOR;
/*      */   
/*      */   protected File zipFile;
/*      */   
/*      */   private ZipScanner zs;
/*      */   private File baseDir;
/*  102 */   protected Hashtable<String, String> entries = new Hashtable<>();
/*  103 */   private final List<FileSet> groupfilesets = new Vector<>();
/*  104 */   private final List<ZipFileSet> filesetsFromGroupfilesets = new Vector<>();
/*  105 */   protected String duplicate = "add";
/*      */   
/*      */   private boolean doCompress = true;
/*      */   private boolean doUpdate = false;
/*      */   private boolean savedDoUpdate = false;
/*      */   private boolean doFilesonly = false;
/*  111 */   protected String archiveType = "zip";
/*      */   
/*  113 */   protected String emptyBehavior = "skip";
/*  114 */   private final List<ResourceCollection> resources = new Vector<>();
/*  115 */   protected Hashtable<String, String> addedDirs = new Hashtable<>();
/*  116 */   private final List<String> addedFiles = new Vector<>();
/*      */   
/*  118 */   private String fixedModTime = null;
/*  119 */   private long modTimeMillis = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean doubleFilePass = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean skipWriting = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean isFirstPass() {
/*  149 */     return (!this.doubleFilePass || this.skipWriting);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean updatedFile = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addingNewFiles = false;
/*      */ 
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
/*      */   private boolean keepCompression = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean roundUp = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  190 */   private String comment = "";
/*      */   
/*  192 */   private int level = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean preserve0Permissions = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useLanguageEncodingFlag = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  212 */   private UnicodeExtraField createUnicodeExtraFields = UnicodeExtraField.NEVER;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fallBackToUTF8 = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  228 */   private Zip64ModeAttribute zip64Mode = Zip64ModeAttribute.AS_NEEDED;
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
/*      */   public void setZipfile(File zipFile) {
/*  240 */     setDestFile(zipFile);
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
/*      */   @Deprecated
/*      */   public void setFile(File file) {
/*  254 */     setDestFile(file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestFile(File destFile) {
/*  264 */     this.zipFile = destFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getDestFile() {
/*  273 */     return this.zipFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBasedir(File baseDir) {
/*  282 */     this.baseDir = baseDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCompress(boolean c) {
/*  291 */     this.doCompress = c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCompress() {
/*  300 */     return this.doCompress;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFilesonly(boolean f) {
/*  309 */     this.doFilesonly = f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUpdate(boolean c) {
/*  318 */     this.doUpdate = c;
/*  319 */     this.savedDoUpdate = c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInUpdateMode() {
/*  327 */     return this.doUpdate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFileset(FileSet set) {
/*  335 */     add((ResourceCollection)set);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addZipfileset(ZipFileSet set) {
/*  344 */     add((ResourceCollection)set);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(ResourceCollection a) {
/*  353 */     this.resources.add(a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addZipGroupFileset(FileSet set) {
/*  361 */     this.groupfilesets.add(set);
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
/*      */   public void setDuplicate(Duplicate df) {
/*  374 */     this.duplicate = df.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class WhenEmpty
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/*  388 */       return new String[] { "fail", "skip", "create" };
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
/*      */   public void setWhenempty(WhenEmpty we) {
/*  403 */     this.emptyBehavior = we.getValue();
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
/*      */   public void setEncoding(String encoding) {
/*  415 */     this.encoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEncoding() {
/*  424 */     return this.encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKeepCompression(boolean keep) {
/*  435 */     this.keepCompression = keep;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComment(String comment) {
/*  445 */     this.comment = comment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getComment() {
/*  455 */     return this.comment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLevel(int level) {
/*  465 */     this.level = level;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLevel() {
/*  474 */     return this.level;
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
/*      */   public void setRoundUp(boolean r) {
/*  492 */     this.roundUp = r;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPreserve0Permissions(boolean b) {
/*  501 */     this.preserve0Permissions = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getPreserve0Permissions() {
/*  510 */     return this.preserve0Permissions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseLanguageEncodingFlag(boolean b) {
/*  519 */     this.useLanguageEncodingFlag = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getUseLanguageEnodingFlag() {
/*  528 */     return this.useLanguageEncodingFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreateUnicodeExtraFields(UnicodeExtraField b) {
/*  537 */     this.createUnicodeExtraFields = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnicodeExtraField getCreateUnicodeExtraFields() {
/*  546 */     return this.createUnicodeExtraFields;
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
/*      */   public void setFallBackToUTF8(boolean b) {
/*  559 */     this.fallBackToUTF8 = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getFallBackToUTF8() {
/*  570 */     return this.fallBackToUTF8;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZip64Mode(Zip64ModeAttribute b) {
/*  579 */     this.zip64Mode = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Zip64ModeAttribute getZip64Mode() {
/*  588 */     return this.zip64Mode;
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
/*      */   public void setModificationtime(String time) {
/*  602 */     this.fixedModTime = time;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getModificationtime() {
/*  612 */     return this.fixedModTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/*  622 */     if (this.doubleFilePass) {
/*  623 */       this.skipWriting = true;
/*  624 */       executeMain();
/*  625 */       this.skipWriting = false;
/*      */     } 
/*  627 */     executeMain();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasUpdatedFile() {
/*  637 */     return this.updatedFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void executeMain() throws BuildException {
/*  647 */     checkAttributesAndElements();
/*      */ 
/*      */     
/*  650 */     File renamedFile = null;
/*  651 */     this.addingNewFiles = true;
/*      */     
/*  653 */     processDoUpdate();
/*  654 */     processGroupFilesets();
/*      */ 
/*      */     
/*  657 */     List<ResourceCollection> vfss = new ArrayList<>();
/*  658 */     if (this.baseDir != null) {
/*  659 */       FileSet fs = (FileSet)getImplicitFileSet().clone();
/*  660 */       fs.setDir(this.baseDir);
/*  661 */       vfss.add(fs);
/*      */     } 
/*  663 */     vfss.addAll(this.resources);
/*      */ 
/*      */     
/*  666 */     ResourceCollection[] fss = vfss.<ResourceCollection>toArray(new ResourceCollection[0]);
/*      */     
/*  668 */     boolean success = false;
/*      */     
/*      */     try {
/*  671 */       ArchiveState state = getResourcesToAdd(fss, this.zipFile, false);
/*      */ 
/*      */       
/*  674 */       if (!state.isOutOfDate()) {
/*      */         return;
/*      */       }
/*      */       
/*  678 */       File parent = this.zipFile.getParentFile();
/*  679 */       if (parent != null && !parent.isDirectory() && 
/*  680 */         !parent.mkdirs() && !parent.isDirectory()) {
/*  681 */         throw new BuildException("Failed to create missing parent directory for %s", new Object[] { this.zipFile });
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  686 */       this.updatedFile = true;
/*  687 */       if (!this.zipFile.exists() && state.isWithoutAnyResources()) {
/*  688 */         createEmptyZip(this.zipFile);
/*      */         return;
/*      */       } 
/*  691 */       Resource[][] addThem = state.getResourcesToAdd();
/*      */       
/*  693 */       if (this.doUpdate) {
/*  694 */         renamedFile = renameFile();
/*      */       }
/*      */       
/*  697 */       String action = this.doUpdate ? "Updating " : "Building ";
/*      */       
/*  699 */       if (!this.skipWriting) {
/*  700 */         log(action + this.archiveType + ": " + this.zipFile.getAbsolutePath());
/*      */       }
/*      */       
/*  703 */       ZipOutputStream zOut = null;
/*      */       try {
/*  705 */         if (!this.skipWriting) {
/*  706 */           zOut = new ZipOutputStream(this.zipFile);
/*      */           
/*  708 */           zOut.setEncoding(this.encoding);
/*  709 */           zOut.setUseLanguageEncodingFlag(this.useLanguageEncodingFlag);
/*  710 */           zOut.setCreateUnicodeExtraFields(this.createUnicodeExtraFields
/*  711 */               .getPolicy());
/*  712 */           zOut.setFallbackToUTF8(this.fallBackToUTF8);
/*  713 */           zOut.setMethod(this.doCompress ? 
/*  714 */               8 : 0);
/*  715 */           zOut.setLevel(this.level);
/*  716 */           zOut.setUseZip64(this.zip64Mode.getMode());
/*      */         } 
/*  718 */         initZipOutputStream(zOut);
/*      */ 
/*      */         
/*  721 */         for (int i = 0; i < fss.length; i++) {
/*  722 */           if ((addThem[i]).length != 0) {
/*  723 */             addResources(fss[i], addThem[i], zOut);
/*      */           }
/*      */         } 
/*      */         
/*  727 */         if (this.doUpdate) {
/*  728 */           this.addingNewFiles = false;
/*  729 */           ZipFileSet oldFiles = new ZipFileSet();
/*  730 */           oldFiles.setProject(getProject());
/*  731 */           oldFiles.setSrc(renamedFile);
/*  732 */           oldFiles.setDefaultexcludes(false);
/*      */           
/*  734 */           for (String addedFile : this.addedFiles) {
/*  735 */             oldFiles.createExclude().setName(addedFile);
/*      */           }
/*      */           
/*  738 */           DirectoryScanner ds = oldFiles.getDirectoryScanner(getProject());
/*  739 */           ((ZipScanner)ds).setEncoding(this.encoding);
/*      */ 
/*      */           
/*  742 */           Stream<String> includedResourceNames = Stream.of(ds.getIncludedFiles());
/*      */           
/*  744 */           if (!this.doFilesonly)
/*      */           {
/*  746 */             includedResourceNames = Stream.concat(includedResourceNames, 
/*  747 */                 Stream.of(ds.getIncludedDirectories()));
/*      */           }
/*      */           
/*  750 */           Objects.requireNonNull(ds);
/*  751 */           Resource[] r = (Resource[])includedResourceNames.map(ds::getResource).toArray(x$0 -> new Resource[x$0]);
/*      */           
/*  753 */           addResources((FileSet)oldFiles, r, zOut);
/*      */         } 
/*  755 */         if (zOut != null) {
/*  756 */           zOut.setComment(this.comment);
/*      */         }
/*  758 */         finalizeZipOutputStream(zOut);
/*      */ 
/*      */ 
/*      */         
/*  762 */         if (this.doUpdate && 
/*  763 */           !renamedFile.delete()) {
/*  764 */           log("Warning: unable to delete temporary file " + renamedFile
/*  765 */               .getName(), 1);
/*      */         }
/*      */         
/*  768 */         success = true;
/*      */       } finally {
/*      */         
/*  771 */         closeZout(zOut, success);
/*      */       } 
/*  773 */     } catch (IOException ioe) {
/*      */       
/*  775 */       String msg = "Problem creating " + this.archiveType + ": " + ioe.getMessage();
/*      */ 
/*      */       
/*  778 */       if ((!this.doUpdate || renamedFile != null) && !this.zipFile.delete()) {
/*  779 */         msg = msg + " (and the archive is probably corrupt but I could not delete it)";
/*      */       }
/*      */ 
/*      */       
/*  783 */       if (this.doUpdate && renamedFile != null) {
/*      */         try {
/*  785 */           FILE_UTILS.rename(renamedFile, this.zipFile);
/*  786 */         } catch (IOException e) {
/*      */           
/*  788 */           msg = msg + " (and I couldn't rename the temporary file " + renamedFile.getName() + " back)";
/*      */         } 
/*      */       }
/*      */       
/*  792 */       throw new BuildException(msg, ioe, getLocation());
/*      */     } finally {
/*  794 */       cleanUp();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private File renameFile() {
/*  800 */     File renamedFile = FILE_UTILS.createTempFile(
/*  801 */         getProject(), "zip", ".tmp", this.zipFile.getParentFile(), true, false);
/*      */     try {
/*  803 */       FILE_UTILS.rename(this.zipFile, renamedFile);
/*  804 */     } catch (SecurityException|IOException e) {
/*  805 */       throw new BuildException("Unable to rename old file (%s) to temporary file", new Object[] { this.zipFile
/*      */             
/*  807 */             .getAbsolutePath() });
/*      */     } 
/*  809 */     return renamedFile;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void closeZout(ZipOutputStream zOut, boolean success) throws IOException {
/*  815 */     if (zOut == null) {
/*      */       return;
/*      */     }
/*      */     try {
/*  819 */       zOut.close();
/*  820 */     } catch (IOException ex) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  830 */       if (success) {
/*  831 */         throw ex;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkAttributesAndElements() {
/*  838 */     if (this.baseDir == null && this.resources.isEmpty() && this.groupfilesets.isEmpty() && "zip"
/*  839 */       .equals(this.archiveType)) {
/*  840 */       throw new BuildException("basedir attribute must be set, or at least one resource collection must be given!");
/*      */     }
/*      */ 
/*      */     
/*  844 */     if (this.zipFile == null) {
/*  845 */       throw new BuildException("You must specify the %s file to create!", new Object[] { this.archiveType });
/*      */     }
/*      */ 
/*      */     
/*  849 */     if (this.fixedModTime != null) {
/*      */       try {
/*  851 */         this.modTimeMillis = DateUtils.parseLenientDateTime(this.fixedModTime).getTime();
/*  852 */       } catch (ParseException pe) {
/*  853 */         throw new BuildException("Failed to parse date string %s.", new Object[] { this.fixedModTime });
/*      */       } 
/*  855 */       if (this.roundUp) {
/*  856 */         this.modTimeMillis += 1999L;
/*      */       }
/*      */     } 
/*      */     
/*  860 */     if (this.zipFile.exists() && !this.zipFile.isFile()) {
/*  861 */       throw new BuildException("%s is not a file.", new Object[] { this.zipFile });
/*      */     }
/*      */     
/*  864 */     if (this.zipFile.exists() && !this.zipFile.canWrite()) {
/*  865 */       throw new BuildException("%s is read-only.", new Object[] { this.zipFile });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processDoUpdate() {
/*  873 */     if (this.doUpdate && !this.zipFile.exists()) {
/*  874 */       this.doUpdate = false;
/*  875 */       logWhenWriting("ignoring update attribute as " + this.archiveType + " doesn't exist.", 4);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processGroupFilesets() {
/*  883 */     for (FileSet fs : this.groupfilesets) {
/*  884 */       logWhenWriting("Processing groupfileset ", 3);
/*  885 */       DirectoryScanner directoryScanner = fs.getDirectoryScanner(getProject());
/*  886 */       File basedir = directoryScanner.getBasedir();
/*  887 */       for (String file : directoryScanner.getIncludedFiles()) {
/*  888 */         logWhenWriting("Adding file " + file + " to fileset", 3);
/*      */         
/*  890 */         ZipFileSet zf = new ZipFileSet();
/*  891 */         zf.setProject(getProject());
/*  892 */         zf.setSrc(new File(basedir, file));
/*  893 */         add((ResourceCollection)zf);
/*  894 */         this.filesetsFromGroupfilesets.add(zf);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean isAddingNewFiles() {
/*  905 */     return this.addingNewFiles;
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
/*      */   protected final void addResources(FileSet fileset, Resource[] resources, ZipOutputStream zOut) throws IOException {
/*  923 */     String prefix = "";
/*  924 */     String fullpath = "";
/*  925 */     int dirMode = 16877;
/*  926 */     int fileMode = 33188;
/*      */     
/*  928 */     ArchiveFileSet zfs = null;
/*  929 */     if (fileset instanceof ArchiveFileSet) {
/*  930 */       zfs = (ArchiveFileSet)fileset;
/*  931 */       prefix = zfs.getPrefix(getProject());
/*  932 */       fullpath = zfs.getFullpath(getProject());
/*  933 */       dirMode = zfs.getDirMode(getProject());
/*  934 */       fileMode = zfs.getFileMode(getProject());
/*      */     } 
/*      */     
/*  937 */     if (!prefix.isEmpty() && !fullpath.isEmpty()) {
/*  938 */       throw new BuildException("Both prefix and fullpath attributes must not be set on the same fileset.");
/*      */     }
/*      */ 
/*      */     
/*  942 */     if (resources.length != 1 && !fullpath.isEmpty()) {
/*  943 */       throw new BuildException("fullpath attribute may only be specified for filesets that specify a single file.");
/*      */     }
/*      */ 
/*      */     
/*  947 */     if (!prefix.isEmpty()) {
/*  948 */       if (!prefix.endsWith("/") && !prefix.endsWith("\\")) {
/*  949 */         prefix = prefix + "/";
/*      */       }
/*  951 */       addParentDirs((File)null, prefix, zOut, "", dirMode);
/*      */     } 
/*      */     
/*  954 */     ZipFile zf = null;
/*      */     try {
/*  956 */       boolean dealingWithFiles = false;
/*  957 */       File base = null;
/*      */       
/*  959 */       if (zfs == null || zfs.getSrc(getProject()) == null) {
/*  960 */         dealingWithFiles = true;
/*  961 */         base = fileset.getDir(getProject());
/*  962 */       } else if (zfs instanceof ZipFileSet) {
/*  963 */         zf = new ZipFile(zfs.getSrc(getProject()), this.encoding);
/*      */       } 
/*      */       
/*  966 */       for (Resource resource : resources) {
/*      */         
/*  968 */         if (fullpath.isEmpty()) {
/*  969 */           name = resource.getName();
/*      */         } else {
/*  971 */           name = fullpath;
/*      */         } 
/*  973 */         String name = name.replace(File.separatorChar, '/');
/*      */         
/*  975 */         if (!name.isEmpty())
/*      */         {
/*      */ 
/*      */           
/*  979 */           if (resource.isDirectory()) {
/*  980 */             if (!this.doFilesonly)
/*      */             {
/*      */ 
/*      */               
/*  984 */               int thisDirMode = (zfs != null && zfs.hasDirModeBeenSet()) ? dirMode : getUnixMode(resource, zf, dirMode);
/*  985 */               addDirectoryResource(resource, name, prefix, base, zOut, dirMode, thisDirMode);
/*      */             }
/*      */           
/*      */           }
/*      */           else {
/*      */             
/*  991 */             addParentDirs(base, name, zOut, prefix, dirMode);
/*      */             
/*  993 */             if (dealingWithFiles) {
/*  994 */               File f = FILE_UTILS.resolveFile(base, resource
/*  995 */                   .getName());
/*  996 */               zipFile(f, zOut, prefix + name, fileMode);
/*      */             }
/*      */             else {
/*      */               
/* 1000 */               int thisFileMode = (zfs != null && zfs.hasFileModeBeenSet()) ? fileMode : getUnixMode(resource, zf, fileMode);
/*      */               
/* 1002 */               addResource(resource, name, prefix, zOut, thisFileMode, zf, 
/*      */                   
/* 1004 */                   (zfs == null) ? 
/* 1005 */                   null : zfs.getSrc(getProject()));
/*      */             } 
/*      */           }  } 
/*      */       } 
/*      */     } finally {
/* 1010 */       if (zf != null) {
/* 1011 */         zf.close();
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
/*      */   private void addDirectoryResource(Resource r, String name, String prefix, File base, ZipOutputStream zOut, int defaultDirMode, int thisDirMode) throws IOException {
/* 1026 */     if (!name.endsWith("/")) {
/* 1027 */       name = name + "/";
/*      */     }
/*      */     
/* 1030 */     int nextToLastSlash = name.lastIndexOf('/', name.length() - 2);
/* 1031 */     if (nextToLastSlash != -1) {
/* 1032 */       addParentDirs(base, name.substring(0, nextToLastSlash + 1), zOut, prefix, defaultDirMode);
/*      */     }
/*      */     
/* 1035 */     zipDir(r, zOut, prefix + name, thisDirMode, 
/* 1036 */         (r instanceof ZipResource) ? (
/* 1037 */         (ZipResource)r).getExtraFields() : null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getUnixMode(Resource r, ZipFile zf, int defaultMode) {
/* 1046 */     int unixMode = defaultMode;
/* 1047 */     if (zf != null) {
/* 1048 */       ZipEntry ze = zf.getEntry(r.getName());
/* 1049 */       unixMode = ze.getUnixMode();
/* 1050 */       if ((unixMode == 0 || unixMode == 16384) && !this.preserve0Permissions)
/*      */       {
/* 1052 */         unixMode = defaultMode;
/*      */       }
/* 1054 */     } else if (r instanceof ArchiveResource) {
/* 1055 */       unixMode = ((ArchiveResource)r).getMode();
/*      */     } 
/* 1057 */     return unixMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addResource(Resource r, String name, String prefix, ZipOutputStream zOut, int mode, ZipFile zf, File fromArchive) throws IOException {
/* 1068 */     if (zf != null) {
/* 1069 */       ZipEntry ze = zf.getEntry(r.getName());
/*      */       
/* 1071 */       if (ze != null) {
/* 1072 */         boolean oldCompress = this.doCompress;
/* 1073 */         if (this.keepCompression)
/* 1074 */           this.doCompress = (ze.getMethod() == 8); 
/*      */         
/* 1076 */         try { BufferedInputStream is = new BufferedInputStream(zf.getInputStream(ze)); 
/* 1077 */           try { zipFile(is, zOut, prefix + name, ze.getTime(), fromArchive, mode, ze
/* 1078 */                 .getExtraFields(true));
/* 1079 */             is.close(); } catch (Throwable throwable) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  }
/* 1080 */         finally { this.doCompress = oldCompress; }
/*      */       
/*      */       } 
/*      */     } else {
/* 1084 */       BufferedInputStream is = new BufferedInputStream(r.getInputStream()); try {
/* 1085 */         zipFile(is, zOut, prefix + name, r.getLastModified(), fromArchive, mode, 
/* 1086 */             (r instanceof ZipResource) ? (
/* 1087 */             (ZipResource)r).getExtraFields() : null);
/* 1088 */         is.close();
/*      */       } catch (Throwable throwable) {
/*      */         try {
/*      */           is.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         } 
/*      */         throw throwable;
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
/*      */   protected final void addResources(ResourceCollection rc, Resource[] resources, ZipOutputStream zOut) throws IOException {
/* 1107 */     if (rc instanceof FileSet) {
/* 1108 */       addResources((FileSet)rc, resources, zOut);
/*      */       return;
/*      */     } 
/* 1111 */     for (Resource resource : resources) {
/* 1112 */       String name = resource.getName();
/* 1113 */       if (name != null) {
/*      */ 
/*      */         
/* 1116 */         name = name.replace(File.separatorChar, '/');
/*      */         
/* 1118 */         if (!name.isEmpty())
/*      */         {
/*      */           
/* 1121 */           if (!resource.isDirectory() || !this.doFilesonly) {
/*      */ 
/*      */             
/* 1124 */             File base = null;
/* 1125 */             FileProvider fp = (FileProvider)resource.as(FileProvider.class);
/* 1126 */             if (fp != null) {
/* 1127 */               base = ResourceUtils.asFileResource(fp).getBaseDir();
/*      */             }
/*      */             
/* 1130 */             if (resource.isDirectory()) {
/* 1131 */               addDirectoryResource(resource, name, "", base, zOut, 16877, 16877);
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 1136 */               addParentDirs(base, name, zOut, "", 16877);
/*      */ 
/*      */               
/* 1139 */               if (fp != null) {
/* 1140 */                 File f = fp.getFile();
/* 1141 */                 zipFile(f, zOut, name, 33188);
/*      */               } else {
/* 1143 */                 addResource(resource, name, "", zOut, 33188, (ZipFile)null, (File)null);
/*      */               } 
/*      */             } 
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
/*      */   protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalizeZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean createEmptyZip(File zipFile) throws BuildException {
/* 1181 */     if (!this.skipWriting) {
/* 1182 */       log("Note: creating empty " + this.archiveType + " archive " + zipFile, 2);
/*      */     }
/*      */     
/* 1185 */     try { OutputStream os = Files.newOutputStream(zipFile.toPath(), new java.nio.file.OpenOption[0]);
/*      */ 
/*      */       
/* 1188 */       try { byte[] empty = new byte[22];
/* 1189 */         empty[0] = 80;
/* 1190 */         empty[1] = 75;
/* 1191 */         empty[2] = 5;
/* 1192 */         empty[3] = 6;
/*      */ 
/*      */         
/* 1195 */         os.write(empty);
/* 1196 */         if (os != null) os.close();  } catch (Throwable throwable) { if (os != null) try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 1197 */     { throw new BuildException("Could not create empty ZIP archive (" + ioe
/* 1198 */           .getMessage() + ")", ioe, 
/* 1199 */           getLocation()); }
/*      */     
/* 1201 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized ZipScanner getZipScanner() {
/* 1208 */     if (this.zs == null) {
/* 1209 */       this.zs = new ZipScanner();
/* 1210 */       this.zs.setEncoding(this.encoding);
/* 1211 */       this.zs.setSrc(this.zipFile);
/*      */     } 
/* 1213 */     return this.zs;
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
/*      */   protected ArchiveState getResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
/* 1248 */     List<FileSet> filesets = new ArrayList<>();
/* 1249 */     List<ResourceCollection> rest = new ArrayList<>();
/* 1250 */     for (ResourceCollection resourceCollection : rcs) {
/* 1251 */       if (resourceCollection instanceof FileSet) {
/* 1252 */         filesets.add((FileSet)resourceCollection);
/*      */       } else {
/* 1254 */         rest.add(resourceCollection);
/*      */       } 
/*      */     } 
/*      */     
/* 1258 */     ResourceCollection[] rc = rest.<ResourceCollection>toArray(new ResourceCollection[0]);
/* 1259 */     ArchiveState as = getNonFileSetResourcesToAdd(rc, zipFile, needsUpdate);
/*      */ 
/*      */     
/* 1262 */     FileSet[] fs = filesets.<FileSet>toArray(new FileSet[0]);
/* 1263 */     ArchiveState as2 = getResourcesToAdd(fs, zipFile, as.isOutOfDate());
/* 1264 */     if (!as.isOutOfDate() && as2.isOutOfDate())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1273 */       as = getNonFileSetResourcesToAdd(rc, zipFile, true);
/*      */     }
/*      */     
/* 1276 */     Resource[][] toAdd = new Resource[rcs.length][];
/* 1277 */     int fsIndex = 0;
/* 1278 */     int restIndex = 0;
/* 1279 */     for (int i = 0; i < rcs.length; i++) {
/* 1280 */       if (rcs[i] instanceof FileSet) {
/* 1281 */         toAdd[i] = as2.getResourcesToAdd()[fsIndex++];
/*      */       } else {
/* 1283 */         toAdd[i] = as.getResourcesToAdd()[restIndex++];
/*      */       } 
/*      */     } 
/* 1286 */     return new ArchiveState(as2.isOutOfDate(), toAdd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1296 */   private static final ThreadLocal<Boolean> HAVE_NON_FILE_SET_RESOURCES_TO_ADD = ThreadLocal.withInitial(() -> Boolean.FALSE);
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
/*      */   protected ArchiveState getResourcesToAdd(FileSet[] filesets, File zipFile, boolean needsUpdate) throws BuildException {
/* 1325 */     Resource[][] initialResources = grabResources(filesets);
/* 1326 */     if (isEmpty(initialResources)) {
/* 1327 */       if (Boolean.FALSE.equals(HAVE_NON_FILE_SET_RESOURCES_TO_ADD.get())) {
/* 1328 */         if (needsUpdate && this.doUpdate)
/*      */         {
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
/* 1347 */           return new ArchiveState(true, initialResources);
/*      */         }
/*      */         
/* 1350 */         if ("skip".equals(this.emptyBehavior)) {
/* 1351 */           if (this.doUpdate) {
/* 1352 */             logWhenWriting(this.archiveType + " archive " + zipFile + " not updated because no new files were included.", 3);
/*      */           }
/*      */           else {
/*      */             
/* 1356 */             logWhenWriting("Warning: skipping " + this.archiveType + " archive " + zipFile + " because no files were included.", 1);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1361 */           if ("fail".equals(this.emptyBehavior)) {
/* 1362 */             throw new BuildException("Cannot create " + this.archiveType + " archive " + zipFile + ": no files were included.", 
/*      */ 
/*      */                 
/* 1365 */                 getLocation());
/*      */           }
/*      */           
/* 1368 */           if (!zipFile.exists()) {
/* 1369 */             needsUpdate = true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1376 */       return new ArchiveState(needsUpdate, initialResources);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1381 */     if (!zipFile.exists()) {
/* 1382 */       return new ArchiveState(true, initialResources);
/*      */     }
/*      */     
/* 1385 */     if (needsUpdate && !this.doUpdate)
/*      */     {
/* 1387 */       return new ArchiveState(true, initialResources);
/*      */     }
/*      */     
/* 1390 */     Resource[][] newerResources = new Resource[filesets.length][];
/*      */     int i;
/* 1392 */     for (i = 0; i < filesets.length; i++) {
/* 1393 */       if (!(this.fileset instanceof ZipFileSet) || ((ZipFileSet)this.fileset)
/* 1394 */         .getSrc(getProject()) == null) {
/* 1395 */         File base = filesets[i].getDir(getProject());
/*      */         
/* 1397 */         for (int j = 0; j < (initialResources[i]).length; j++) {
/*      */           
/* 1399 */           File resourceAsFile = FILE_UTILS.resolveFile(base, initialResources[i][j]
/* 1400 */               .getName());
/* 1401 */           if (resourceAsFile.equals(zipFile)) {
/* 1402 */             throw new BuildException("A zip file cannot include itself", 
/* 1403 */                 getLocation());
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1409 */     for (i = 0; i < filesets.length; i++) {
/* 1410 */       if ((initialResources[i]).length == 0) {
/* 1411 */         newerResources[i] = new Resource[0];
/*      */       } else {
/*      */         GlobPatternMapper globPatternMapper;
/*      */         
/* 1415 */         IdentityMapper identityMapper = new IdentityMapper();
/* 1416 */         if (filesets[i] instanceof ZipFileSet) {
/* 1417 */           ZipFileSet zfs = (ZipFileSet)filesets[i];
/* 1418 */           if (zfs.getFullpath(getProject()) != null && 
/* 1419 */             !zfs.getFullpath(getProject()).isEmpty()) {
/*      */ 
/*      */ 
/*      */             
/* 1423 */             MergingMapper fm = new MergingMapper();
/* 1424 */             fm.setTo(zfs.getFullpath(getProject()));
/* 1425 */             MergingMapper mergingMapper1 = fm;
/*      */           }
/* 1427 */           else if (zfs.getPrefix(getProject()) != null && 
/* 1428 */             !zfs.getPrefix(getProject()).isEmpty()) {
/* 1429 */             GlobPatternMapper gm = new GlobPatternMapper();
/* 1430 */             gm.setFrom("*");
/* 1431 */             String prefix = zfs.getPrefix(getProject());
/* 1432 */             if (!prefix.endsWith("/") && !prefix.endsWith("\\")) {
/* 1433 */               prefix = prefix + "/";
/*      */             }
/* 1435 */             gm.setTo(prefix + "*");
/* 1436 */             globPatternMapper = gm;
/*      */           } 
/*      */         } 
/*      */         
/* 1440 */         newerResources[i] = selectOutOfDateResources(initialResources[i], (FileNameMapper)globPatternMapper);
/*      */         
/* 1442 */         needsUpdate = (needsUpdate || (newerResources[i]).length > 0);
/*      */         
/* 1444 */         if (needsUpdate && !this.doUpdate) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1451 */     if (needsUpdate && !this.doUpdate)
/*      */     {
/* 1453 */       return new ArchiveState(true, initialResources);
/*      */     }
/*      */     
/* 1456 */     return new ArchiveState(needsUpdate, newerResources);
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
/*      */   protected ArchiveState getNonFileSetResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
/* 1490 */     Resource[][] initialResources = grabNonFileSetResources(rcs);
/* 1491 */     boolean empty = isEmpty(initialResources);
/* 1492 */     HAVE_NON_FILE_SET_RESOURCES_TO_ADD.set(Boolean.valueOf(!empty));
/* 1493 */     if (empty)
/*      */     {
/*      */       
/* 1496 */       return new ArchiveState(needsUpdate, initialResources);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1501 */     if (!zipFile.exists()) {
/* 1502 */       return new ArchiveState(true, initialResources);
/*      */     }
/*      */     
/* 1505 */     if (needsUpdate && !this.doUpdate)
/*      */     {
/* 1507 */       return new ArchiveState(true, initialResources);
/*      */     }
/*      */     
/* 1510 */     Resource[][] newerResources = new Resource[rcs.length][];
/*      */     
/* 1512 */     for (int i = 0; i < rcs.length; i++) {
/* 1513 */       if ((initialResources[i]).length == 0) {
/* 1514 */         newerResources[i] = new Resource[0];
/*      */       }
/*      */       else {
/*      */         
/* 1518 */         for (int j = 0; j < (initialResources[i]).length; j++) {
/*      */           
/* 1520 */           FileProvider fp = (FileProvider)initialResources[i][j].as(FileProvider.class);
/* 1521 */           if (fp != null && zipFile.equals(fp.getFile())) {
/* 1522 */             throw new BuildException("A zip file cannot include itself", 
/* 1523 */                 getLocation());
/*      */           }
/*      */         } 
/*      */         
/* 1527 */         newerResources[i] = selectOutOfDateResources(initialResources[i], (FileNameMapper)new IdentityMapper());
/*      */         
/* 1529 */         needsUpdate = (needsUpdate || (newerResources[i]).length > 0);
/*      */         
/* 1531 */         if (needsUpdate && !this.doUpdate) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1538 */     if (needsUpdate && !this.doUpdate)
/*      */     {
/* 1540 */       return new ArchiveState(true, initialResources);
/*      */     }
/*      */     
/* 1543 */     return new ArchiveState(needsUpdate, newerResources);
/*      */   }
/*      */ 
/*      */   
/*      */   private Resource[] selectOutOfDateResources(Resource[] initial, FileNameMapper mapper) {
/* 1548 */     Resource[] rs = selectFileResources(initial);
/*      */     
/* 1550 */     Resource[] result = ResourceUtils.selectOutOfDateSources((ProjectComponent)this, rs, mapper, (ResourceFactory)
/* 1551 */         getZipScanner(), 2000L);
/*      */     
/* 1553 */     if (!this.doFilesonly) {
/* 1554 */       Union u = new Union();
/* 1555 */       u.addAll(Arrays.asList(selectDirectoryResources(initial)));
/*      */       
/* 1557 */       ResourceCollection rc = ResourceUtils.selectSources((ProjectComponent)this, (ResourceCollection)u, mapper, (ResourceFactory)
/* 1558 */           getZipScanner(), MISSING_DIR_PROVIDER);
/*      */       
/* 1560 */       if (!rc.isEmpty()) {
/* 1561 */         List<Resource> newer = new ArrayList<>();
/* 1562 */         newer.addAll(Arrays.asList(((Union)rc).listResources()));
/* 1563 */         newer.addAll(Arrays.asList(result));
/* 1564 */         result = newer.<Resource>toArray(result);
/*      */       } 
/*      */     } 
/* 1567 */     return result;
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
/*      */   protected Resource[][] grabResources(FileSet[] filesets) {
/* 1579 */     Resource[][] result = new Resource[filesets.length][];
/* 1580 */     for (int i = 0; i < filesets.length; i++) {
/* 1581 */       boolean skipEmptyNames = true;
/* 1582 */       if (filesets[i] instanceof ZipFileSet) {
/* 1583 */         ZipFileSet zfs = (ZipFileSet)filesets[i];
/*      */         
/* 1585 */         skipEmptyNames = (zfs.getPrefix(getProject()).isEmpty() && zfs.getFullpath(getProject()).isEmpty());
/*      */       } 
/*      */       
/* 1588 */       DirectoryScanner rs = filesets[i].getDirectoryScanner(getProject());
/* 1589 */       if (rs instanceof ZipScanner) {
/* 1590 */         ((ZipScanner)rs).setEncoding(this.encoding);
/*      */       }
/* 1592 */       List<Resource> resources = new Vector<>();
/* 1593 */       if (!this.doFilesonly) {
/* 1594 */         for (String d : rs.getIncludedDirectories()) {
/* 1595 */           if (!d.isEmpty() || !skipEmptyNames) {
/* 1596 */             resources.add(rs.getResource(d));
/*      */           }
/*      */         } 
/*      */       }
/* 1600 */       for (String f : rs.getIncludedFiles()) {
/* 1601 */         if (!f.isEmpty() || !skipEmptyNames) {
/* 1602 */           resources.add(rs.getResource(f));
/*      */         }
/*      */       } 
/* 1605 */       result[i] = resources.<Resource>toArray(new Resource[0]);
/*      */     } 
/* 1607 */     return result;
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
/*      */   protected Resource[][] grabNonFileSetResources(ResourceCollection[] rcs) {
/* 1619 */     Resource[][] result = new Resource[rcs.length][];
/* 1620 */     for (int i = 0; i < rcs.length; i++) {
/* 1621 */       List<Resource> dirs = new ArrayList<>();
/* 1622 */       List<Resource> files = new ArrayList<>();
/* 1623 */       for (Resource r : rcs[i]) {
/* 1624 */         if (r.isDirectory()) {
/* 1625 */           dirs.add(r); continue;
/* 1626 */         }  if (r.isExists()) {
/* 1627 */           files.add(r);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1632 */       dirs.sort(Comparator.comparing(Resource::getName));
/* 1633 */       List<Resource> rs = new ArrayList<>(dirs);
/* 1634 */       rs.addAll(files);
/* 1635 */       result[i] = rs.<Resource>toArray(new Resource[0]);
/*      */     } 
/* 1637 */     return result;
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
/*      */   protected void zipDir(File dir, ZipOutputStream zOut, String vPath, int mode) throws IOException {
/* 1652 */     zipDir(dir, zOut, vPath, mode, (ZipExtraField[])null);
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
/*      */   protected void zipDir(File dir, ZipOutputStream zOut, String vPath, int mode, ZipExtraField[] extra) throws IOException {
/* 1668 */     zipDir((dir == null) ? null : (Resource)new FileResource(dir), zOut, vPath, mode, extra);
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
/*      */   protected void zipDir(Resource dir, ZipOutputStream zOut, String vPath, int mode, ZipExtraField[] extra) throws IOException {
/* 1685 */     if (this.doFilesonly) {
/* 1686 */       logWhenWriting("skipping directory " + vPath + " for file-only archive", 3);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1691 */     if (this.addedDirs.get(vPath) != null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1697 */     logWhenWriting("adding directory " + vPath, 3);
/* 1698 */     this.addedDirs.put(vPath, vPath);
/*      */     
/* 1700 */     if (!this.skipWriting) {
/* 1701 */       ZipEntry ze = new ZipEntry(vPath);
/*      */ 
/*      */       
/* 1704 */       int millisToAdd = this.roundUp ? 1999 : 0;
/*      */       
/* 1706 */       if (this.fixedModTime != null) {
/* 1707 */         ze.setTime(this.modTimeMillis);
/* 1708 */       } else if (dir != null && dir.isExists()) {
/* 1709 */         ze.setTime(dir.getLastModified() + millisToAdd);
/*      */       } else {
/* 1711 */         ze.setTime(System.currentTimeMillis() + millisToAdd);
/*      */       } 
/* 1713 */       ze.setSize(0L);
/* 1714 */       ze.setMethod(0);
/*      */       
/* 1716 */       ze.setCrc(EMPTY_CRC);
/* 1717 */       ze.setUnixMode(mode);
/*      */       
/* 1719 */       if (extra != null) {
/* 1720 */         ze.setExtraFields(extra);
/*      */       }
/*      */       
/* 1723 */       zOut.putNextEntry(ze);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1732 */   private static final ThreadLocal<ZipExtraField[]> CURRENT_ZIP_EXTRA = (ThreadLocal)new ThreadLocal<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final ZipExtraField[] getCurrentExtraFields() {
/* 1741 */     return CURRENT_ZIP_EXTRA.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void setCurrentExtraFields(ZipExtraField[] extra) {
/* 1751 */     CURRENT_ZIP_EXTRA.set(extra);
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
/*      */   protected void zipFile(InputStream in, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode) throws IOException {
/* 1774 */     if (this.entries.containsKey(vPath)) {
/*      */       
/* 1776 */       if ("preserve".equals(this.duplicate)) {
/* 1777 */         logWhenWriting(vPath + " already added, skipping", 2);
/*      */         
/*      */         return;
/*      */       } 
/* 1781 */       if ("fail".equals(this.duplicate)) {
/* 1782 */         throw new BuildException("Duplicate file %s was found and the duplicate attribute is 'fail'.", new Object[] { vPath });
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1787 */       logWhenWriting("duplicate file " + vPath + " found, adding.", 3);
/*      */     } else {
/*      */       
/* 1790 */       logWhenWriting("adding entry " + vPath, 3);
/*      */     } 
/*      */     
/* 1793 */     this.entries.put(vPath, vPath);
/*      */     
/* 1795 */     if (!this.skipWriting) {
/* 1796 */       ZipEntry ze = new ZipEntry(vPath);
/* 1797 */       ze.setTime((this.fixedModTime != null) ? this.modTimeMillis : lastModified);
/* 1798 */       ze.setMethod(this.doCompress ? 8 : 0);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1804 */       InputStream markableInputStream = in.markSupported() ? in : new BufferedInputStream(in);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1812 */       if (!zOut.isSeekable() && !this.doCompress) {
/* 1813 */         long size = 0L;
/* 1814 */         CRC32 cal = new CRC32();
/* 1815 */         markableInputStream.mark(2147483647);
/* 1816 */         byte[] arrayOfByte = new byte[8192];
/* 1817 */         int i = 0;
/*      */         while (true) {
/* 1819 */           size += i;
/* 1820 */           cal.update(arrayOfByte, 0, i);
/* 1821 */           i = markableInputStream.read(arrayOfByte, 0, arrayOfByte.length);
/* 1822 */           if (i == -1) {
/* 1823 */             markableInputStream.reset();
/* 1824 */             ze.setSize(size);
/* 1825 */             ze.setCrc(cal.getValue()); break;
/*      */           } 
/*      */         } 
/* 1828 */       }  ze.setUnixMode(mode);
/* 1829 */       ZipExtraField[] extra = getCurrentExtraFields();
/* 1830 */       if (extra != null) {
/* 1831 */         ze.setExtraFields(extra);
/*      */       }
/*      */       
/* 1834 */       zOut.putNextEntry(ze);
/*      */       
/* 1836 */       byte[] buffer = new byte[8192];
/* 1837 */       int count = 0;
/*      */       do {
/* 1839 */         if (count != 0) {
/* 1840 */           zOut.write(buffer, 0, count);
/*      */         }
/* 1842 */         count = markableInputStream.read(buffer, 0, buffer.length);
/* 1843 */       } while (count != -1);
/*      */     } 
/* 1845 */     this.addedFiles.add(vPath);
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
/*      */   
/*      */   protected final void zipFile(InputStream in, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode, ZipExtraField[] extra) throws IOException {
/*      */     try {
/* 1870 */       setCurrentExtraFields(extra);
/* 1871 */       zipFile(in, zOut, vPath, lastModified, fromArchive, mode);
/*      */     } finally {
/* 1873 */       setCurrentExtraFields((ZipExtraField[])null);
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
/*      */   
/*      */   protected void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
/* 1893 */     if (file.equals(this.zipFile)) {
/* 1894 */       throw new BuildException("A zip file cannot include itself", 
/* 1895 */           getLocation());
/*      */     }
/*      */     
/* 1898 */     BufferedInputStream bIn = new BufferedInputStream(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]));
/*      */     try {
/* 1900 */       zipFile(bIn, zOut, vPath, file
/* 1901 */           .lastModified() + (this.roundUp ? 1999L : 0L), (File)null, mode);
/*      */       
/* 1903 */       bIn.close();
/*      */     } catch (Throwable throwable) {
/*      */       try {
/*      */         bIn.close();
/*      */       } catch (Throwable throwable1) {
/*      */         throwable.addSuppressed(throwable1);
/*      */       } 
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void addParentDirs(File baseDir, String entry, ZipOutputStream zOut, String prefix, int dirMode) throws IOException {
/* 1920 */     if (!this.doFilesonly) {
/* 1921 */       Stack<String> directories = new Stack<>();
/* 1922 */       int slashPos = entry.length();
/*      */ 
/*      */       
/* 1925 */       String dir = entry.substring(0, slashPos + 1);
/* 1926 */       while ((slashPos = entry.lastIndexOf('/', slashPos - 1)) != -1 && this.addedDirs.get(prefix + dir) == null)
/*      */       {
/*      */         
/* 1929 */         directories.push(dir);
/*      */       }
/*      */       
/* 1932 */       while (!directories.isEmpty()) {
/* 1933 */         File f; dir = directories.pop();
/*      */         
/* 1935 */         if (baseDir != null) {
/* 1936 */           f = new File(baseDir, dir);
/*      */         } else {
/* 1938 */           f = new File(dir);
/*      */         } 
/* 1940 */         zipDir(f, zOut, prefix + dir, dirMode);
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
/*      */   protected void cleanUp() {
/* 1960 */     this.addedDirs.clear();
/* 1961 */     this.addedFiles.clear();
/* 1962 */     this.entries.clear();
/* 1963 */     this.addingNewFiles = false;
/* 1964 */     this.doUpdate = this.savedDoUpdate;
/* 1965 */     this.resources.removeAll(this.filesetsFromGroupfilesets);
/* 1966 */     this.filesetsFromGroupfilesets.clear();
/* 1967 */     HAVE_NON_FILE_SET_RESOURCES_TO_ADD.set(Boolean.FALSE);
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
/*      */   public void reset() {
/* 1979 */     this.resources.clear();
/* 1980 */     this.zipFile = null;
/* 1981 */     this.baseDir = null;
/* 1982 */     this.groupfilesets.clear();
/* 1983 */     this.duplicate = "add";
/* 1984 */     this.archiveType = "zip";
/* 1985 */     this.doCompress = true;
/* 1986 */     this.emptyBehavior = "skip";
/* 1987 */     this.doUpdate = false;
/* 1988 */     this.doFilesonly = false;
/* 1989 */     this.encoding = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final boolean isEmpty(Resource[][] r) {
/* 2000 */     for (Resource[] element : r) {
/* 2001 */       if (element.length > 0) {
/* 2002 */         return false;
/*      */       }
/*      */     } 
/* 2005 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Resource[] selectFileResources(Resource[] orig) {
/* 2015 */     return selectResources(orig, r -> {
/*      */           if (!r.isDirectory()) {
/*      */             return true;
/*      */           }
/*      */           if (this.doFilesonly) {
/*      */             logWhenWriting("Ignoring directory " + r.getName() + " as only files will be added.", 3);
/*      */           }
/*      */           return false;
/*      */         });
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
/*      */   protected Resource[] selectDirectoryResources(Resource[] orig) {
/* 2038 */     return selectResources(orig, Resource::isDirectory);
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
/*      */   protected Resource[] selectResources(Resource[] orig, ResourceSelector selector) {
/* 2050 */     if (orig.length == 0) {
/* 2051 */       return orig;
/*      */     }
/* 2053 */     Objects.requireNonNull(selector);
/* 2054 */     Resource[] result = (Resource[])Stream.<Resource>of(orig).filter(selector::isSelected).toArray(x$0 -> new Resource[x$0]);
/* 2055 */     return (result.length == orig.length) ? orig : result;
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
/*      */   protected void logWhenWriting(String msg, int level) {
/* 2067 */     if (!this.skipWriting) {
/* 2068 */       log(msg, level);
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
/*      */   public static class Duplicate
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/* 2083 */       return new String[] { "add", "preserve", "fail" };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ArchiveState
/*      */   {
/*      */     private final boolean outOfDate;
/*      */ 
/*      */     
/*      */     private final Resource[][] resourcesToAdd;
/*      */ 
/*      */     
/*      */     ArchiveState(boolean state, Resource[][] r) {
/* 2098 */       this.outOfDate = state;
/* 2099 */       this.resourcesToAdd = r;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isOutOfDate() {
/* 2107 */       return this.outOfDate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Resource[][] getResourcesToAdd() {
/* 2115 */       return this.resourcesToAdd;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isWithoutAnyResources() {
/* 2123 */       if (this.resourcesToAdd == null) {
/* 2124 */         return true;
/*      */       }
/* 2126 */       for (Resource[] element : this.resourcesToAdd) {
/* 2127 */         if (element != null && element.length > 0) {
/* 2128 */           return false;
/*      */         }
/*      */       } 
/* 2131 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class UnicodeExtraField
/*      */     extends EnumeratedAttribute
/*      */   {
/* 2142 */     private static final Map<String, ZipOutputStream.UnicodeExtraFieldPolicy> POLICIES = new HashMap<>(); private static final String NEVER_KEY = "never";
/*      */     private static final String ALWAYS_KEY = "always";
/*      */     private static final String N_E_KEY = "not-encodeable";
/*      */     
/*      */     static {
/* 2147 */       POLICIES.put("never", ZipOutputStream.UnicodeExtraFieldPolicy.NEVER);
/*      */       
/* 2149 */       POLICIES.put("always", ZipOutputStream.UnicodeExtraFieldPolicy.ALWAYS);
/*      */       
/* 2151 */       POLICIES.put("not-encodeable", ZipOutputStream.UnicodeExtraFieldPolicy.NOT_ENCODEABLE);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/* 2158 */       return new String[] { "never", "always", "not-encodeable" };
/*      */     }
/*      */     
/* 2161 */     public static final UnicodeExtraField NEVER = new UnicodeExtraField("never");
/*      */ 
/*      */     
/*      */     private UnicodeExtraField(String name) {
/* 2165 */       setValue(name);
/*      */     }
/*      */ 
/*      */     
/*      */     public UnicodeExtraField() {}
/*      */     
/*      */     public ZipOutputStream.UnicodeExtraFieldPolicy getPolicy() {
/* 2172 */       return POLICIES.get(getValue());
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
/*      */   public static final class Zip64ModeAttribute
/*      */     extends EnumeratedAttribute
/*      */   {
/* 2203 */     private static final Map<String, Zip64Mode> MODES = new HashMap<>();
/*      */     private static final String NEVER_KEY = "never";
/*      */     private static final String ALWAYS_KEY = "always";
/*      */     private static final String A_N_KEY = "as-needed";
/*      */     
/*      */     static {
/* 2209 */       MODES.put("never", Zip64Mode.Never);
/* 2210 */       MODES.put("always", Zip64Mode.Always);
/* 2211 */       MODES.put("as-needed", Zip64Mode.AsNeeded);
/*      */     }
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/* 2216 */       return new String[] { "never", "always", "as-needed" };
/*      */     }
/*      */     
/* 2219 */     public static final Zip64ModeAttribute NEVER = new Zip64ModeAttribute("never");
/*      */     
/* 2221 */     public static final Zip64ModeAttribute AS_NEEDED = new Zip64ModeAttribute("as-needed");
/*      */ 
/*      */     
/*      */     private Zip64ModeAttribute(String name) {
/* 2225 */       setValue(name);
/*      */     }
/*      */ 
/*      */     
/*      */     public Zip64ModeAttribute() {}
/*      */     
/*      */     public Zip64Mode getMode() {
/* 2232 */       return MODES.get(getValue());
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Zip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */