/*     */ package org.apache.tools.ant.taskdefs.optional.extension.resolvers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.Get;
/*     */ import org.apache.tools.ant.taskdefs.optional.extension.Extension;
/*     */ import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;
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
/*     */ public class URLResolver
/*     */   implements ExtensionResolver
/*     */ {
/*     */   private File destfile;
/*     */   private File destdir;
/*     */   private URL url;
/*     */   
/*     */   public void setUrl(URL url) {
/*  43 */     this.url = url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestfile(File destfile) {
/*  51 */     this.destfile = destfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestdir(File destdir) {
/*  59 */     this.destdir = destdir;
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
/*     */   public File resolve(Extension extension, Project project) throws BuildException {
/*  72 */     validate();
/*     */     
/*  74 */     File file = getDest();
/*     */     
/*  76 */     Get get = new Get();
/*  77 */     get.setProject(project);
/*  78 */     get.setDest(file);
/*  79 */     get.setSrc(this.url);
/*  80 */     get.execute();
/*     */     
/*  82 */     return file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File getDest() {
/*     */     File result;
/*  90 */     if (null != this.destfile) {
/*  91 */       result = this.destfile;
/*     */     } else {
/*  93 */       String filename, file = this.url.getFile();
/*     */       
/*  95 */       if (null == file || file.length() <= 1) {
/*  96 */         filename = "default.file";
/*     */       } else {
/*  98 */         int index = file.lastIndexOf('/');
/*  99 */         if (-1 == index) {
/* 100 */           index = 0;
/*     */         }
/* 102 */         filename = file.substring(index);
/*     */       } 
/* 104 */       result = new File(this.destdir, filename);
/*     */     } 
/* 106 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() {
/* 113 */     if (null == this.url) {
/* 114 */       throw new BuildException("Must specify URL");
/*     */     }
/* 116 */     if (null == this.destdir && null == this.destfile) {
/* 117 */       throw new BuildException("Must specify destination file or directory");
/*     */     }
/*     */     
/* 120 */     if (null != this.destdir && null != this.destfile) {
/* 121 */       throw new BuildException("Must not specify both destination file or directory");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return "URL[" + this.url + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/resolvers/URLResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */