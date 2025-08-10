/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Mapper;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.IdentityMapper;
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
/*     */ public abstract class MappingSelector
/*     */   extends BaseSelector
/*     */ {
/*  36 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected File targetdir = null;
/*  41 */   protected Mapper mapperElement = null;
/*  42 */   protected FileNameMapper map = null;
/*  43 */   protected int granularity = (int)FILE_UTILS.getFileTimestampGranularity();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetdir(File targetdir) {
/*  54 */     this.targetdir = targetdir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mapper createMapper() throws BuildException {
/*  63 */     if (this.map != null || this.mapperElement != null) {
/*  64 */       throw new BuildException("Cannot define more than one mapper");
/*     */     }
/*  66 */     this.mapperElement = new Mapper(getProject());
/*  67 */     return this.mapperElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(FileNameMapper fileNameMapper) {
/*  77 */     if (this.map != null || this.mapperElement != null) {
/*  78 */       throw new BuildException("Cannot define more than one mapper");
/*     */     }
/*  80 */     this.map = fileNameMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySettings() {
/*  89 */     if (this.targetdir == null) {
/*  90 */       setError("The targetdir attribute is required.");
/*     */     }
/*  92 */     if (this.map == null) {
/*  93 */       if (this.mapperElement == null) {
/*  94 */         this.map = (FileNameMapper)new IdentityMapper();
/*     */       } else {
/*  96 */         this.map = this.mapperElement.getImplementation();
/*  97 */         if (this.map == null) {
/*  98 */           setError("Could not set <mapper> element.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 117 */     validate();
/*     */ 
/*     */     
/* 120 */     String[] destfiles = this.map.mapFileName(filename);
/*     */ 
/*     */     
/* 123 */     if (destfiles == null) {
/* 124 */       return false;
/*     */     }
/*     */     
/* 127 */     if (destfiles.length != 1 || destfiles[0] == null) {
/* 128 */       throw new BuildException("Invalid destination file results for " + this.targetdir
/* 129 */           .getName() + " with filename " + filename);
/*     */     }
/* 131 */     String destname = destfiles[0];
/* 132 */     File destfile = FILE_UTILS.resolveFile(this.targetdir, destname);
/*     */     
/* 134 */     return selectionTest(file, destfile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean selectionTest(File paramFile1, File paramFile2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGranularity(int granularity) {
/* 152 */     this.granularity = granularity;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/MappingSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */