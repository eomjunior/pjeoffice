/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
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
/*     */ 
/*     */ public abstract class Pack
/*     */   extends Task
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   protected File zipFile;
/*     */   protected File source;
/*     */   private Resource src;
/*     */   
/*     */   public void setZipfile(File zipFile) {
/*  53 */     this.zipFile = zipFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestfile(File zipFile) {
/*  61 */     setZipfile(zipFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File src) {
/*  69 */     setSrcResource((Resource)new FileResource(src));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcResource(Resource src) {
/*  77 */     if (src.isDirectory()) {
/*  78 */       throw new BuildException("the source can't be a directory");
/*     */     }
/*  80 */     FileProvider fp = (FileProvider)src.as(FileProvider.class);
/*  81 */     if (fp != null) {
/*  82 */       this.source = fp.getFile();
/*  83 */     } else if (!supportsNonFileResources()) {
/*  84 */       throw new BuildException("Only FileSystem resources are supported.");
/*     */     } 
/*  86 */     this.src = src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection a) {
/*  94 */     if (a.size() == 0)
/*  95 */       throw new BuildException("No resource selected, %s needs exactly one resource.", new Object[] {
/*     */             
/*  97 */             getTaskName()
/*     */           }); 
/*  99 */     if (a.size() != 1)
/* 100 */       throw new BuildException("%s cannot handle multiple resources at once. (%d resources were selected.)", new Object[] {
/*     */             
/* 102 */             getTaskName(), Integer.valueOf(a.size())
/*     */           }); 
/* 104 */     setSrcResource(a.iterator().next());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 112 */     if (this.zipFile == null) {
/* 113 */       throw new BuildException("zipfile attribute is required", getLocation());
/*     */     }
/*     */     
/* 116 */     if (this.zipFile.isDirectory()) {
/* 117 */       throw new BuildException("zipfile attribute must not represent a directory!", 
/*     */           
/* 119 */           getLocation());
/*     */     }
/*     */     
/* 122 */     if (getSrcResource() == null) {
/* 123 */       throw new BuildException("src attribute or nested resource is required", 
/* 124 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 134 */     validate();
/*     */     
/* 136 */     Resource s = getSrcResource();
/* 137 */     if (!s.isExists()) {
/* 138 */       log("Nothing to do: " + s.toString() + " doesn't exist.");
/*     */     }
/* 140 */     else if (this.zipFile.lastModified() < s.getLastModified()) {
/* 141 */       log("Building: " + this.zipFile.getAbsolutePath());
/* 142 */       pack();
/*     */     } else {
/* 144 */       log("Nothing to do: " + this.zipFile.getAbsolutePath() + " is up to date.");
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
/*     */   private void zipFile(InputStream in, OutputStream zOut) throws IOException {
/* 157 */     byte[] buffer = new byte[8192];
/* 158 */     int count = 0;
/*     */     do {
/* 160 */       zOut.write(buffer, 0, count);
/* 161 */       count = in.read(buffer, 0, buffer.length);
/* 162 */     } while (count != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void zipFile(File file, OutputStream zOut) throws IOException {
/* 173 */     zipResource((Resource)new FileResource(file), zOut);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void zipResource(Resource resource, OutputStream zOut) throws IOException {
/* 184 */     InputStream rIn = resource.getInputStream(); try {
/* 185 */       zipFile(rIn, zOut);
/* 186 */       if (rIn != null) rIn.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (rIn != null)
/*     */         try {
/*     */           rIn.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   protected abstract void pack();
/*     */   
/*     */   public Resource getSrcResource() {
/* 200 */     return this.src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supportsNonFileResources() {
/* 211 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Pack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */