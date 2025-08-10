/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileList;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.Touchable;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.util.DateUtils;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
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
/*     */ 
/*     */ 
/*     */ public class Touch
/*     */   extends Task
/*     */ {
/*  67 */   public static final DateFormatFactory DEFAULT_DF_FACTORY = new DateFormatFactory()
/*     */     {
/*     */       
/*     */       public DateFormat getPrimaryFormat()
/*     */       {
/*  72 */         return DateUtils.EN_US_DATE_FORMAT_MIN.get();
/*     */       }
/*     */       
/*     */       public DateFormat getFallbackFormat() {
/*  76 */         return DateUtils.EN_US_DATE_FORMAT_SEC.get();
/*     */       }
/*     */     };
/*  79 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private File file;
/*  82 */   private long millis = -1L;
/*     */   private String dateTime;
/*  84 */   private List<FileSet> filesets = new Vector<>();
/*     */   private Union resources;
/*     */   private boolean dateTimeConfigured;
/*     */   private boolean mkdirs;
/*     */   private boolean verbose = true;
/*  89 */   private FileNameMapper fileNameMapper = null;
/*  90 */   private DateFormatFactory dfFactory = DEFAULT_DF_FACTORY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/*  98 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMillis(long millis) {
/* 107 */     this.millis = millis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatetime(String dateTime) {
/* 118 */     if (this.dateTime != null) {
/* 119 */       log("Resetting datetime attribute to " + dateTime, 3);
/*     */     }
/* 121 */     this.dateTime = dateTime;
/* 122 */     this.dateTimeConfigured = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMkdirs(boolean mkdirs) {
/* 132 */     this.mkdirs = mkdirs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 142 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(final String pattern) {
/* 151 */     this.dfFactory = new DateFormatFactory()
/*     */       {
/*     */         public DateFormat getPrimaryFormat() {
/* 154 */           return new SimpleDateFormat(pattern);
/*     */         }
/*     */         
/*     */         public DateFormat getFallbackFormat() {
/* 158 */           return null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredMapper(Mapper mapper) {
/* 169 */     add(mapper.getImplementation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) throws BuildException {
/* 179 */     if (this.fileNameMapper != null)
/* 180 */       throw new BuildException("Only one mapper may be added to the %s task.", new Object[] {
/* 181 */             getTaskName()
/*     */           }); 
/* 183 */     this.fileNameMapper = fileNameMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 191 */     this.filesets.add(set);
/* 192 */     add((ResourceCollection)set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilelist(FileList list) {
/* 200 */     add((ResourceCollection)list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceCollection rc) {
/* 209 */     this.resources = (this.resources == null) ? new Union() : this.resources;
/* 210 */     this.resources.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void checkConfiguration() throws BuildException {
/* 219 */     if (this.file == null && this.resources == null) {
/* 220 */       throw new BuildException("Specify at least one source--a file or resource collection.");
/*     */     }
/*     */     
/* 223 */     if (this.file != null && this.file.exists() && this.file.isDirectory()) {
/* 224 */       throw new BuildException("Use a resource collection to touch directories.");
/*     */     }
/* 226 */     if (this.dateTime != null && !this.dateTimeConfigured) {
/* 227 */       long workmillis = this.millis;
/* 228 */       if ("now".equalsIgnoreCase(this.dateTime)) {
/* 229 */         workmillis = System.currentTimeMillis();
/*     */       } else {
/* 231 */         DateFormat df = this.dfFactory.getPrimaryFormat();
/* 232 */         ParseException pe = null;
/*     */         try {
/* 234 */           workmillis = df.parse(this.dateTime).getTime();
/* 235 */         } catch (ParseException peOne) {
/* 236 */           df = this.dfFactory.getFallbackFormat();
/* 237 */           if (df == null) {
/* 238 */             pe = peOne;
/*     */           } else {
/*     */             try {
/* 241 */               workmillis = df.parse(this.dateTime).getTime();
/* 242 */             } catch (ParseException peTwo) {
/* 243 */               pe = peTwo;
/*     */             } 
/*     */           } 
/*     */         } 
/* 247 */         if (pe != null) {
/* 248 */           throw new BuildException(pe.getMessage(), pe, getLocation());
/*     */         }
/* 250 */         if (workmillis < 0L) {
/* 251 */           throw new BuildException("Date of %s results in negative milliseconds value relative to epoch (January 1, 1970, 00:00:00 GMT).", new Object[] { this.dateTime });
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 256 */       log("Setting millis to " + workmillis + " from datetime attribute", 
/* 257 */           (this.millis < 0L) ? 4 : 3);
/* 258 */       setMillis(workmillis);
/*     */       
/* 260 */       this.dateTimeConfigured = true;
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
/*     */   public void execute() throws BuildException {
/* 272 */     checkConfiguration();
/* 273 */     touch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void touch() throws BuildException {
/* 281 */     long defaultTimestamp = getTimestamp();
/*     */     
/* 283 */     if (this.file != null) {
/* 284 */       touch((Resource)new FileResource(this.file.getParentFile(), this.file.getName()), defaultTimestamp);
/*     */     }
/*     */     
/* 287 */     if (this.resources == null) {
/*     */       return;
/*     */     }
/*     */     
/* 291 */     for (Resource r : this.resources) {
/* 292 */       Touchable t = (Touchable)r.as(Touchable.class);
/* 293 */       if (t == null) {
/* 294 */         throw new BuildException("Can't touch " + r);
/*     */       }
/* 296 */       touch(r, defaultTimestamp);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     for (FileSet fs : this.filesets) {
/* 303 */       DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/* 304 */       File fromDir = fs.getDir(getProject());
/*     */       
/* 306 */       for (String srcDir : ds.getIncludedDirectories()) {
/* 307 */         touch((Resource)new FileResource(fromDir, srcDir), defaultTimestamp);
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
/*     */   @Deprecated
/*     */   protected void touch(File file) {
/* 322 */     touch(file, getTimestamp());
/*     */   }
/*     */   
/*     */   private long getTimestamp() {
/* 326 */     return (this.millis < 0L) ? System.currentTimeMillis() : this.millis;
/*     */   }
/*     */   
/*     */   private void touch(Resource r, long defaultTimestamp) {
/* 330 */     if (this.fileNameMapper == null) {
/* 331 */       FileProvider fp = (FileProvider)r.as(FileProvider.class);
/* 332 */       if (fp != null) {
/*     */         
/* 334 */         touch(fp.getFile(), defaultTimestamp);
/*     */       } else {
/* 336 */         ((Touchable)r.as(Touchable.class)).touch(defaultTimestamp);
/*     */       } 
/*     */     } else {
/* 339 */       String[] mapped = this.fileNameMapper.mapFileName(r.getName());
/* 340 */       if (mapped != null && mapped.length > 0) {
/* 341 */         long modTime = defaultTimestamp;
/* 342 */         if (this.millis < 0L && r.isExists()) {
/* 343 */           modTime = r.getLastModified();
/*     */         }
/* 345 */         for (String fileName : mapped) {
/* 346 */           touch(getProject().resolveFile(fileName), modTime);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void touch(File file, long modTime) {
/* 353 */     if (!file.exists()) {
/* 354 */       log("Creating " + file, 
/* 355 */           this.verbose ? 2 : 3);
/*     */       try {
/* 357 */         FILE_UTILS.createNewFile(file, this.mkdirs);
/* 358 */       } catch (IOException ioe) {
/* 359 */         throw new BuildException("Could not create " + file, ioe, 
/* 360 */             getLocation());
/*     */       } 
/*     */     } 
/* 363 */     if (!file.canWrite()) {
/* 364 */       throw new BuildException("Can not change modification date of read-only file %s", new Object[] { file });
/*     */     }
/*     */     
/* 367 */     FILE_UTILS.setFileLastModified(file, modTime);
/*     */   }
/*     */   
/*     */   public static interface DateFormatFactory {
/*     */     DateFormat getPrimaryFormat();
/*     */     
/*     */     DateFormat getFallbackFormat();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Touch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */