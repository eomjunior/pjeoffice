/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.taskdefs.optional.native2ascii.Native2AsciiAdapter;
/*     */ import org.apache.tools.ant.taskdefs.optional.native2ascii.Native2AsciiAdapterFactory;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.IdentityMapper;
/*     */ import org.apache.tools.ant.util.SourceFileScanner;
/*     */ import org.apache.tools.ant.util.facade.FacadeTaskHelper;
/*     */ import org.apache.tools.ant.util.facade.ImplementationSpecificArgument;
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
/*     */ public class Native2Ascii
/*     */   extends MatchingTask
/*     */ {
/*     */   private boolean reverse = false;
/*  45 */   private String encoding = null;
/*  46 */   private File srcDir = null;
/*  47 */   private File destDir = null;
/*  48 */   private String extension = null;
/*     */   
/*     */   private Mapper mapper;
/*  51 */   private FacadeTaskHelper facade = null;
/*  52 */   private Native2AsciiAdapter nestedAdapter = null;
/*     */ 
/*     */   
/*     */   public Native2Ascii() {
/*  56 */     this.facade = new FacadeTaskHelper(Native2AsciiAdapterFactory.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReverse(boolean reverse) {
/*  67 */     this.reverse = reverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReverse() {
/*  77 */     return this.reverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/*  88 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/*  98 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File srcDir) {
/* 107 */     this.srcDir = srcDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File destDir) {
/* 117 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExt(String ext) {
/* 127 */     this.extension = ext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplementation(String impl) {
/* 137 */     if ("default".equals(impl)) {
/* 138 */       this.facade.setImplementation(Native2AsciiAdapterFactory.getDefault());
/*     */     } else {
/* 140 */       this.facade.setImplementation(impl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() throws BuildException {
/* 151 */     if (this.mapper != null) {
/* 152 */       throw new BuildException("Cannot define more than one mapper", 
/* 153 */           getLocation());
/*     */     }
/* 155 */     this.mapper = new Mapper(getProject());
/* 156 */     return this.mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper fileNameMapper) {
/* 166 */     createMapper().add(fileNameMapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImplementationSpecificArgument createArg() {
/* 176 */     ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
/*     */     
/* 178 */     this.facade.addImplementationArgument(arg);
/* 179 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createImplementationClasspath() {
/* 190 */     return this.facade.getImplementationClasspath(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Native2AsciiAdapter adapter) {
/* 200 */     if (this.nestedAdapter != null) {
/* 201 */       throw new BuildException("Can't have more than one native2ascii adapter");
/*     */     }
/*     */     
/* 204 */     this.nestedAdapter = adapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     FileNameMapper m;
/* 215 */     DirectoryScanner scanner = null;
/*     */ 
/*     */ 
/*     */     
/* 219 */     if (this.srcDir == null) {
/* 220 */       this.srcDir = getProject().resolveFile(".");
/*     */     }
/*     */ 
/*     */     
/* 224 */     if (this.destDir == null) {
/* 225 */       throw new BuildException("The dest attribute must be set.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (this.srcDir.equals(this.destDir) && this.extension == null && this.mapper == null) {
/* 232 */       throw new BuildException("The ext attribute or a mapper must be set if src and dest dirs are the same.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (this.mapper == null) {
/* 238 */       if (this.extension == null) {
/* 239 */         IdentityMapper identityMapper = new IdentityMapper();
/*     */       } else {
/* 241 */         m = new ExtMapper();
/*     */       } 
/*     */     } else {
/* 244 */       m = this.mapper.getImplementation();
/*     */     } 
/*     */     
/* 247 */     scanner = getDirectoryScanner(this.srcDir);
/* 248 */     String[] files = scanner.getIncludedFiles();
/* 249 */     SourceFileScanner sfs = new SourceFileScanner((Task)this);
/* 250 */     files = sfs.restrict(files, this.srcDir, this.destDir, m);
/* 251 */     int count = files.length;
/* 252 */     if (count == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 256 */     String message = "Converting " + count + " file" + ((count != 1) ? "s" : "") + " from ";
/* 257 */     log(message + this.srcDir + " to " + this.destDir);
/* 258 */     for (String file : files) {
/* 259 */       String[] dest = m.mapFileName(file);
/* 260 */       if (dest != null && dest.length > 0) {
/* 261 */         convert(file, dest[0]);
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
/*     */ 
/*     */   
/*     */   private void convert(String srcName, String destName) throws BuildException {
/* 278 */     File srcFile = new File(this.srcDir, srcName);
/* 279 */     File destFile = new File(this.destDir, destName);
/*     */ 
/*     */     
/* 282 */     if (srcFile.equals(destFile)) {
/* 283 */       throw new BuildException("file %s would overwrite itself", new Object[] { srcFile });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 288 */     String parentName = destFile.getParent();
/* 289 */     if (parentName != null) {
/* 290 */       File parentFile = new File(parentName);
/*     */       
/* 292 */       if (!parentFile.exists() && 
/* 293 */         !parentFile.mkdirs() && !parentFile.isDirectory()) {
/* 294 */         throw new BuildException("cannot create parent directory %s", new Object[] { parentName });
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 299 */     log("converting " + srcName, 3);
/*     */     
/* 301 */     Native2AsciiAdapter ad = (this.nestedAdapter != null) ? this.nestedAdapter : Native2AsciiAdapterFactory.getAdapter(this.facade.getImplementation(), (ProjectComponent)this, 
/* 302 */         createImplementationClasspath());
/* 303 */     if (!ad.convert(this, srcFile, destFile)) {
/* 304 */       throw new BuildException("conversion failed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCurrentArgs() {
/* 315 */     return this.facade.getArgs();
/*     */   }
/*     */ 
/*     */   
/*     */   private class ExtMapper
/*     */     implements FileNameMapper
/*     */   {
/*     */     private ExtMapper() {}
/*     */ 
/*     */     
/*     */     public void setFrom(String s) {}
/*     */     
/*     */     public void setTo(String s) {}
/*     */     
/*     */     public String[] mapFileName(String fileName) {
/* 330 */       int lastDot = fileName.lastIndexOf('.');
/* 331 */       if (lastDot >= 0) {
/* 332 */         return new String[] { fileName.substring(0, lastDot) + Native2Ascii.access$100(this.this$0) };
/*     */       }
/* 334 */       return new String[] { fileName + Native2Ascii.access$100(this.this$0) };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/Native2Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */