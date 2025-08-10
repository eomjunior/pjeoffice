/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceFactory;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
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
/*     */ public class SourceFileScanner
/*     */   implements ResourceFactory
/*     */ {
/*     */   protected Task task;
/*  44 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*     */   private File destDir;
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceFileScanner(Task task) {
/*  52 */     this.task = task;
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
/*     */   public String[] restrict(String[] files, File srcDir, File destDir, FileNameMapper mapper) {
/*  69 */     return restrict(files, srcDir, destDir, mapper, FILE_UTILS
/*  70 */         .getFileTimestampGranularity());
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
/*     */   public String[] restrict(String[] files, File srcDir, File destDir, FileNameMapper mapper, long granularity) {
/*  92 */     this.destDir = destDir;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     Resource[] sourceResources = (Resource[])Stream.<String>of(files).map(f -> new FileResource(srcDir, f) { public String getName() { return f; } }).toArray(x$0 -> new Resource[x$0]);
/*     */ 
/*     */ 
/*     */     
/* 104 */     return 
/* 105 */       (String[])Stream.<Resource>of(ResourceUtils.selectOutOfDateSources((ProjectComponent)this.task, sourceResources, mapper, this, granularity))
/*     */       
/* 107 */       .map(Resource::getName).toArray(x$0 -> new String[x$0]);
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
/*     */   public File[] restrictAsFiles(String[] files, File srcDir, File destDir, FileNameMapper mapper) {
/* 124 */     return restrictAsFiles(files, srcDir, destDir, mapper, FILE_UTILS
/* 125 */         .getFileTimestampGranularity());
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
/*     */   public File[] restrictAsFiles(String[] files, File srcDir, File destDir, FileNameMapper mapper, long granularity) {
/* 146 */     return (File[])Stream.<String>of(restrict(files, srcDir, destDir, mapper, granularity))
/* 147 */       .map(name -> new File(srcDir, name)).toArray(x$0 -> new File[x$0]);
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
/*     */   public Resource getResource(String name) {
/* 159 */     return (Resource)new FileResource(this.destDir, name);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/SourceFileScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */