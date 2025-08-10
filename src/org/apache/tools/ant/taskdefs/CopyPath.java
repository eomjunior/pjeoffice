/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
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
/*     */ @Deprecated
/*     */ public class CopyPath
/*     */   extends Task
/*     */ {
/*     */   public static final String ERROR_NO_DESTDIR = "No destDir specified";
/*     */   public static final String ERROR_NO_PATH = "No path specified";
/*     */   public static final String ERROR_NO_MAPPER = "No mapper specified";
/*  56 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*     */   private FileNameMapper mapper;
/*     */ 
/*     */   
/*     */   private Path path;
/*     */   
/*     */   private File destDir;
/*     */   
/*  66 */   private long granularity = FILE_UTILS
/*  67 */     .getFileTimestampGranularity();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean preserveLastModified = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestDir(File destDir) {
/*  76 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileNameMapper newmapper) {
/*  85 */     if (this.mapper != null) {
/*  86 */       throw new BuildException("Only one mapper allowed");
/*     */     }
/*  88 */     this.mapper = newmapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(Path s) {
/*  98 */     createPath().append(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathRef(Reference r) {
/* 108 */     createPath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath() {
/* 117 */     if (this.path == null) {
/* 118 */       this.path = new Path(getProject());
/*     */     }
/* 120 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGranularity(long granularity) {
/* 131 */     this.granularity = granularity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveLastModified(boolean preserveLastModified) {
/* 140 */     this.preserveLastModified = preserveLastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateAttributes() throws BuildException {
/* 151 */     if (this.destDir == null) {
/* 152 */       throw new BuildException("No destDir specified");
/*     */     }
/* 154 */     if (this.mapper == null) {
/* 155 */       throw new BuildException("No mapper specified");
/*     */     }
/* 157 */     if (this.path == null) {
/* 158 */       throw new BuildException("No path specified");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 169 */     log("This task should have never been released and was obsoleted by ResourceCollection support in <copy> available since Ant 1.7.0.  Don't use it.", 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     validateAttributes();
/* 175 */     String[] sourceFiles = this.path.list();
/* 176 */     if (sourceFiles.length == 0) {
/* 177 */       log("Path is empty", 3);
/*     */       
/*     */       return;
/*     */     } 
/* 181 */     for (String sourceFileName : sourceFiles) {
/*     */       
/* 183 */       File sourceFile = new File(sourceFileName);
/* 184 */       String[] toFiles = this.mapper.mapFileName(sourceFileName);
/* 185 */       if (toFiles != null)
/*     */       {
/*     */ 
/*     */         
/* 189 */         for (String destFileName : toFiles) {
/* 190 */           File destFile = new File(this.destDir, destFileName);
/*     */           
/* 192 */           if (sourceFile.equals(destFile)) {
/* 193 */             log("Skipping self-copy of " + sourceFileName, 3);
/*     */           
/*     */           }
/* 196 */           else if (sourceFile.isDirectory()) {
/* 197 */             log("Skipping directory " + sourceFileName);
/*     */           } else {
/*     */             
/*     */             try {
/* 201 */               log("Copying " + sourceFile + " to " + destFile, 3);
/*     */               
/* 203 */               FILE_UTILS.copyFile(sourceFile, destFile, null, null, false, this.preserveLastModified, null, null, 
/* 204 */                   getProject());
/* 205 */             } catch (IOException ioe) {
/*     */               
/* 207 */               String msg = "Failed to copy " + sourceFile + " to " + destFile + " due to " + ioe.getMessage();
/* 208 */               if (destFile.exists() && !destFile.delete()) {
/* 209 */                 msg = msg + " and I couldn't delete the corrupt " + destFile;
/*     */               }
/* 211 */               throw new BuildException(msg, ioe, getLocation());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/CopyPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */