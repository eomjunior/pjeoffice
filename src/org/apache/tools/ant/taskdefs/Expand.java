/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.types.selectors.SelectorUtils;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.IdentityMapper;
/*     */ import org.apache.tools.zip.ZipEntry;
/*     */ import org.apache.tools.zip.ZipFile;
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
/*     */ public class Expand
/*     */   extends Task
/*     */ {
/*     */   public static final String NATIVE_ENCODING = "native-encoding";
/*     */   public static final String ERROR_MULTIPLE_MAPPERS = "Cannot define more than one mapper";
/*  67 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private static final int BUFFER_SIZE = 1024;
/*     */   private File dest;
/*     */   private File source;
/*     */   private boolean overwrite = true;
/*  73 */   private Mapper mapperElement = null;
/*  74 */   private List<PatternSet> patternsets = new Vector<>();
/*  75 */   private Union resources = new Union();
/*     */   private boolean resourcesSpecified = false;
/*     */   private boolean failOnEmptyArchive = false;
/*     */   private boolean stripAbsolutePathSpec = true;
/*     */   private boolean scanForUnicodeExtraFields = true;
/*  80 */   private Boolean allowFilesToEscapeDest = null;
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */   
/*     */   public Expand() {
/*  88 */     this("UTF8");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Expand(String encoding) {
/*  98 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnEmptyArchive(boolean b) {
/* 108 */     this.failOnEmptyArchive = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFailOnEmptyArchive() {
/* 118 */     return this.failOnEmptyArchive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 128 */     if ("expand".equals(getTaskType())) {
/* 129 */       log("!! expand is deprecated. Use unzip instead. !!");
/*     */     }
/*     */     
/* 132 */     if (this.source == null && !this.resourcesSpecified) {
/* 133 */       throw new BuildException("src attribute and/or resources must be specified");
/*     */     }
/*     */ 
/*     */     
/* 137 */     if (this.dest == null) {
/* 138 */       throw new BuildException("Dest attribute must be specified");
/*     */     }
/*     */ 
/*     */     
/* 142 */     if (this.dest.exists() && !this.dest.isDirectory()) {
/* 143 */       throw new BuildException("Dest must be a directory.", getLocation());
/*     */     }
/*     */     
/* 146 */     if (this.source != null) {
/* 147 */       if (this.source.isDirectory()) {
/* 148 */         throw new BuildException("Src must not be a directory. Use nested filesets instead.", 
/* 149 */             getLocation());
/*     */       }
/* 151 */       if (!this.source.exists()) {
/* 152 */         throw new BuildException("src '" + this.source + "' doesn't exist.");
/*     */       }
/* 154 */       if (!this.source.canRead()) {
/* 155 */         throw new BuildException("src '" + this.source + "' cannot be read.");
/*     */       }
/* 157 */       expandFile(FILE_UTILS, this.source, this.dest);
/*     */     } 
/* 159 */     for (Resource r : this.resources) {
/* 160 */       if (!r.isExists()) {
/* 161 */         log("Skipping '" + r.getName() + "' because it doesn't exist.");
/*     */         
/*     */         continue;
/*     */       } 
/* 165 */       FileProvider fp = (FileProvider)r.as(FileProvider.class);
/* 166 */       if (fp != null) {
/* 167 */         expandFile(FILE_UTILS, fp.getFile(), this.dest); continue;
/*     */       } 
/* 169 */       expandResource(r, this.dest);
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
/*     */   protected void expandFile(FileUtils fileUtils, File srcF, File dir) {
/* 182 */     log("Expanding: " + srcF + " into " + dir, 2);
/* 183 */     FileNameMapper mapper = getMapper();
/* 184 */     if (!srcF.exists())
/* 185 */       throw new BuildException("Unable to expand " + srcF + " as the file does not exist", 
/*     */ 
/*     */           
/* 188 */           getLocation()); 
/*     */     
/* 190 */     try { ZipFile zf = new ZipFile(srcF, this.encoding, this.scanForUnicodeExtraFields); 
/* 191 */       try { boolean empty = true;
/* 192 */         Enumeration<ZipEntry> entries = zf.getEntries();
/* 193 */         while (entries.hasMoreElements()) {
/* 194 */           ZipEntry ze = entries.nextElement();
/* 195 */           empty = false;
/* 196 */           InputStream is = null;
/* 197 */           log("extracting " + ze.getName(), 4);
/*     */           try {
/* 199 */             extractFile(fileUtils, srcF, dir, 
/* 200 */                 is = zf.getInputStream(ze), ze
/* 201 */                 .getName(), new Date(ze.getTime()), ze
/* 202 */                 .isDirectory(), mapper);
/*     */           } finally {
/* 204 */             FileUtils.close(is);
/*     */           } 
/*     */         } 
/* 207 */         if (empty && getFailOnEmptyArchive()) {
/* 208 */           throw new BuildException("archive '%s' is empty", new Object[] { srcF });
/*     */         }
/* 210 */         log("expand complete", 3);
/* 211 */         zf.close(); } catch (Throwable throwable) { try { zf.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 212 */     { throw new BuildException("Error while expanding " + srcF
/* 213 */           .getPath() + "\n" + ioe
/* 214 */           .toString(), ioe); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void expandResource(Resource srcR, File dir) {
/* 226 */     throw new BuildException("only filesystem based resources are supported by this task.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileNameMapper getMapper() {
/* 235 */     if (this.mapperElement != null) {
/* 236 */       return this.mapperElement.getImplementation();
/*     */     }
/* 238 */     return (FileNameMapper)new IdentityMapper();
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
/*     */   
/*     */   protected void extractFile(FileUtils fileUtils, File srcF, File dir, InputStream compressedInputStream, String entryName, Date entryDate, boolean isDirectory, FileNameMapper mapper) throws IOException {
/* 263 */     boolean entryNameStartsWithPathSpec = (!entryName.isEmpty() && (entryName.charAt(0) == File.separatorChar || entryName.charAt(0) == '/' || entryName.charAt(0) == '\\'));
/* 264 */     if (this.stripAbsolutePathSpec && entryNameStartsWithPathSpec) {
/* 265 */       log("stripped absolute path spec from " + entryName, 3);
/*     */       
/* 267 */       entryName = entryName.substring(1);
/*     */     } 
/*     */     
/* 270 */     boolean allowedOutsideOfDest = (Boolean.TRUE == getAllowFilesToEscapeDest() || (null == getAllowFilesToEscapeDest() && !this.stripAbsolutePathSpec && entryNameStartsWithPathSpec));
/*     */     
/* 272 */     if (this.patternsets != null && !this.patternsets.isEmpty()) {
/*     */       
/* 274 */       String name = entryName.replace('/', File.separatorChar).replace('\\', File.separatorChar);
/*     */       
/* 276 */       Set<String> includePatterns = new HashSet<>();
/* 277 */       Set<String> excludePatterns = new HashSet<>();
/* 278 */       for (PatternSet p : this.patternsets) {
/* 279 */         String[] incls = p.getIncludePatterns(getProject());
/* 280 */         if (incls == null || incls.length == 0)
/*     */         {
/* 282 */           incls = new String[] { "**" };
/*     */         }
/*     */         
/* 285 */         for (String incl : incls) {
/*     */           
/* 287 */           String pattern = incl.replace('/', File.separatorChar).replace('\\', File.separatorChar);
/* 288 */           if (pattern.endsWith(File.separator)) {
/* 289 */             pattern = pattern + "**";
/*     */           }
/* 291 */           includePatterns.add(pattern);
/*     */         } 
/*     */         
/* 294 */         String[] excls = p.getExcludePatterns(getProject());
/* 295 */         if (excls != null) {
/* 296 */           for (String excl : excls) {
/*     */             
/* 298 */             String pattern = excl.replace('/', File.separatorChar).replace('\\', File.separatorChar);
/* 299 */             if (pattern.endsWith(File.separator)) {
/* 300 */               pattern = pattern + "**";
/*     */             }
/* 302 */             excludePatterns.add(pattern);
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 307 */       boolean included = false;
/* 308 */       for (String pattern : includePatterns) {
/* 309 */         if (SelectorUtils.matchPath(pattern, name)) {
/* 310 */           included = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 315 */       for (String pattern : excludePatterns) {
/* 316 */         if (SelectorUtils.matchPath(pattern, name)) {
/* 317 */           included = false;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 322 */       if (!included) {
/*     */         
/* 324 */         log("skipping " + entryName + " as it is excluded or not included.", 3);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 330 */     String[] mappedNames = mapper.mapFileName(entryName);
/* 331 */     if (mappedNames == null || mappedNames.length == 0) {
/* 332 */       mappedNames = new String[] { entryName };
/*     */     }
/* 334 */     File f = fileUtils.resolveFile(dir, mappedNames[0]);
/* 335 */     if (!allowedOutsideOfDest && !fileUtils.isLeadingPath(dir, f, true)) {
/* 336 */       log("skipping " + entryName + " as its target " + f.getCanonicalPath() + " is outside of " + dir
/* 337 */           .getCanonicalPath() + ".", 3);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 342 */       if (!this.overwrite && f.exists() && f
/* 343 */         .lastModified() >= entryDate.getTime()) {
/* 344 */         log("Skipping " + f + " as it is up-to-date", 4);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 349 */       log("expanding " + entryName + " to " + f, 3);
/*     */ 
/*     */       
/* 352 */       File dirF = f.getParentFile();
/* 353 */       if (dirF != null) {
/* 354 */         dirF.mkdirs();
/*     */       }
/*     */       
/* 357 */       if (isDirectory) {
/* 358 */         f.mkdirs();
/*     */       } else {
/* 360 */         byte[] buffer = new byte[1024];
/* 361 */         OutputStream fos = Files.newOutputStream(f.toPath(), new java.nio.file.OpenOption[0]); 
/*     */         try { int length;
/* 363 */           while ((length = compressedInputStream.read(buffer)) >= 0) {
/* 364 */             fos.write(buffer, 0, length);
/*     */           }
/* 366 */           if (fos != null) fos.close();  } catch (Throwable throwable) { if (fos != null)
/*     */             try { fos.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */       
/* 369 */       }  fileUtils.setFileLastModified(f, entryDate.getTime());
/* 370 */     } catch (FileNotFoundException ex) {
/* 371 */       log("Unable to expand to file " + f.getPath(), ex, 1);
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
/*     */   public void setDest(File d) {
/* 386 */     this.dest = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File s) {
/* 395 */     this.source = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverwrite(boolean b) {
/* 404 */     this.overwrite = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPatternset(PatternSet set) {
/* 412 */     this.patternsets.add(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 420 */     add((ResourceCollection)set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/* 429 */     this.resourcesSpecified = true;
/* 430 */     this.resources.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() throws BuildException {
/* 440 */     if (this.mapperElement != null) {
/* 441 */       throw new BuildException("Cannot define more than one mapper", 
/* 442 */           getLocation());
/*     */     }
/* 444 */     this.mapperElement = new Mapper(getProject());
/* 445 */     return this.mapperElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/* 454 */     createMapper().add(fileNameMapper);
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
/*     */   public void setEncoding(String encoding) {
/* 467 */     internalSetEncoding(encoding);
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
/*     */   protected void internalSetEncoding(String encoding) {
/* 479 */     if ("native-encoding".equals(encoding)) {
/* 480 */       encoding = null;
/*     */     }
/* 482 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 490 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStripAbsolutePathSpec(boolean b) {
/* 500 */     this.stripAbsolutePathSpec = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScanForUnicodeExtraFields(boolean b) {
/* 510 */     internalSetScanForUnicodeExtraFields(b);
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
/*     */   protected void internalSetScanForUnicodeExtraFields(boolean b) {
/* 522 */     this.scanForUnicodeExtraFields = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getScanForUnicodeExtraFields() {
/* 530 */     return this.scanForUnicodeExtraFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowFilesToEscapeDest(boolean b) {
/* 540 */     this.allowFilesToEscapeDest = Boolean.valueOf(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean getAllowFilesToEscapeDest() {
/* 551 */     return this.allowFilesToEscapeDest;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Expand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */