/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Unpack
/*     */   extends Task
/*     */ {
/*     */   protected File source;
/*     */   protected File dest;
/*     */   protected Resource srcResource;
/*     */   
/*     */   @Deprecated
/*     */   public void setSrc(String src) {
/*  55 */     log("DEPRECATED - The setSrc(String) method has been deprecated. Use setSrc(File) instead.");
/*     */     
/*  57 */     setSrc(getProject().resolveFile(src));
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
/*     */   @Deprecated
/*     */   public void setDest(String dest) {
/*  71 */     log("DEPRECATED - The setDest(String) method has been deprecated. Use setDest(File) instead.");
/*     */     
/*  73 */     setDest(getProject().resolveFile(dest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File src) {
/*  81 */     setSrcResource((Resource)new FileResource(src));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcResource(Resource src) {
/*  89 */     if (!src.isExists()) {
/*  90 */       throw new BuildException("the archive %s doesn't exist", new Object[] { src
/*  91 */             .getName() });
/*     */     }
/*  93 */     if (src.isDirectory()) {
/*  94 */       throw new BuildException("the archive %s can't be a directory", new Object[] { src
/*  95 */             .getName() });
/*     */     }
/*  97 */     FileProvider fp = (FileProvider)src.as(FileProvider.class);
/*  98 */     if (fp != null) {
/*  99 */       this.source = fp.getFile();
/* 100 */     } else if (!supportsNonFileResources()) {
/* 101 */       throw new BuildException("The source %s is not a FileSystem Only FileSystem resources are supported.", new Object[] { src
/*     */             
/* 103 */             .getName() });
/*     */     } 
/* 105 */     this.srcResource = src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection a) {
/* 113 */     if (a.size() != 1) {
/* 114 */       throw new BuildException("only single argument resource collections are supported as archives");
/*     */     }
/*     */     
/* 117 */     setSrcResource(a.iterator().next());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File dest) {
/* 125 */     this.dest = dest;
/*     */   }
/*     */   
/*     */   private void validate() throws BuildException {
/* 129 */     if (this.srcResource == null) {
/* 130 */       throw new BuildException("No Src specified", getLocation());
/*     */     }
/*     */     
/* 133 */     if (this.dest == null) {
/* 134 */       if (this.source == null) {
/* 135 */         throw new BuildException("dest is required when using a non-filesystem source", 
/* 136 */             getLocation());
/*     */       }
/* 138 */       this.dest = new File(this.source.getParent());
/*     */     } 
/*     */     
/* 141 */     if (this.dest.isDirectory()) {
/* 142 */       String defaultExtension = getDefaultExtension();
/* 143 */       createDestFile(defaultExtension);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void createDestFile(String defaultExtension) {
/* 149 */     String sourceName = (this.source == null) ? getLastNamePart(this.srcResource) : this.source.getName();
/* 150 */     int len = sourceName.length();
/* 151 */     if (defaultExtension != null && len > defaultExtension
/* 152 */       .length() && defaultExtension
/* 153 */       .equalsIgnoreCase(sourceName
/* 154 */         .substring(len - defaultExtension.length()))) {
/* 155 */       this.dest = new File(this.dest, sourceName.substring(0, len - defaultExtension
/* 156 */             .length()));
/*     */     } else {
/* 158 */       this.dest = new File(this.dest, sourceName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 168 */     File savedDest = this.dest;
/*     */     try {
/* 170 */       validate();
/* 171 */       extract();
/*     */     } finally {
/* 173 */       this.dest = savedDest;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String getDefaultExtension();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void extract();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supportsNonFileResources() {
/* 198 */     return false;
/*     */   }
/*     */   
/*     */   private String getLastNamePart(Resource r) {
/* 202 */     String n = r.getName();
/* 203 */     int idx = n.lastIndexOf('/');
/* 204 */     return (idx < 0) ? n : n.substring(idx + 1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Unpack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */