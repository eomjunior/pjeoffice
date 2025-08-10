/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.FilterSetCollection;
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
/*     */ public class Move
/*     */   extends Copy
/*     */ {
/*  54 */   private boolean performGc = Os.isFamily("windows");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Move() {
/*  64 */     setOverwrite(true);
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
/*     */   public void setPerformGcOnFailedDelete(boolean b) {
/*  78 */     this.performGc = b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateAttributes() throws BuildException {
/*  84 */     if (this.file != null && this.file.isDirectory()) {
/*  85 */       if ((this.destFile != null && this.destDir != null) || (this.destFile == null && this.destDir == null))
/*     */       {
/*  87 */         throw new BuildException("One and only one of tofile and todir must be set.");
/*     */       }
/*  89 */       this.destFile = (this.destFile == null) ? new File(this.destDir, this.file.getName()) : this.destFile;
/*  90 */       this.destDir = (this.destDir == null) ? this.destFile.getParentFile() : this.destDir;
/*     */       
/*  92 */       this.completeDirMap.put(this.file, this.destFile);
/*  93 */       this.file = null;
/*     */     } else {
/*  95 */       super.validateAttributes();
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
/*     */   protected void doFileOperations() {
/* 109 */     if (this.completeDirMap.size() > 0) {
/* 110 */       for (Map.Entry<File, File> entry : this.completeDirMap.entrySet()) {
/* 111 */         File fromDir = entry.getKey();
/* 112 */         File toDir = entry.getValue();
/* 113 */         boolean renamed = false;
/*     */         try {
/* 115 */           log("Attempting to rename dir: " + fromDir + " to " + toDir, this.verbosity);
/* 116 */           renamed = renameFile(fromDir, toDir, this.filtering, this.forceOverwrite);
/* 117 */         } catch (IOException ioe) {
/*     */           
/* 119 */           String msg = "Failed to rename dir " + fromDir + " to " + toDir + " due to " + ioe.getMessage();
/* 120 */           throw new BuildException(msg, ioe, getLocation());
/*     */         } 
/* 122 */         if (!renamed) {
/* 123 */           FileSet fs = new FileSet();
/* 124 */           fs.setProject(getProject());
/* 125 */           fs.setDir(fromDir);
/* 126 */           addFileset(fs);
/* 127 */           DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/* 128 */           scan(fromDir, toDir, ds.getIncludedFiles(), ds.getIncludedDirectories());
/*     */         } 
/*     */       } 
/*     */     }
/* 132 */     int moveCount = this.fileCopyMap.size();
/* 133 */     if (moveCount > 0) {
/* 134 */       log("Moving " + moveCount + " file" + ((moveCount == 1) ? "" : "s") + " to " + this.destDir
/* 135 */           .getAbsolutePath());
/*     */       
/* 137 */       for (Map.Entry<String, String[]> entry : this.fileCopyMap.entrySet()) {
/* 138 */         String fromFile = entry.getKey();
/* 139 */         File f = new File(fromFile);
/* 140 */         boolean selfMove = false;
/* 141 */         if (f.exists()) {
/* 142 */           String[] toFiles = entry.getValue();
/* 143 */           for (int i = 0; i < toFiles.length; i++) {
/* 144 */             String toFile = toFiles[i];
/*     */             
/* 146 */             if (fromFile.equals(toFile)) {
/* 147 */               log("Skipping self-move of " + fromFile, this.verbosity);
/* 148 */               selfMove = true;
/*     */             
/*     */             }
/*     */             else {
/*     */ 
/*     */               
/* 154 */               File d = new File(toFile);
/* 155 */               if (i + 1 == toFiles.length && !selfMove) {
/*     */ 
/*     */                 
/* 158 */                 moveFile(f, d, this.filtering, this.forceOverwrite);
/*     */               } else {
/* 160 */                 copyFile(f, d, this.filtering, this.forceOverwrite);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 167 */     if (this.includeEmpty) {
/* 168 */       int createCount = 0;
/* 169 */       for (Map.Entry<String, String[]> entry : this.dirCopyMap.entrySet()) {
/* 170 */         String fromDirName = entry.getKey();
/* 171 */         boolean selfMove = false;
/* 172 */         for (String toDirName : (String[])entry.getValue()) {
/* 173 */           if (fromDirName.equals(toDirName)) {
/* 174 */             log("Skipping self-move of " + fromDirName, this.verbosity);
/* 175 */             selfMove = true;
/*     */           } else {
/*     */             
/* 178 */             File d = new File(toDirName);
/* 179 */             if (!d.exists())
/* 180 */               if (!d.mkdirs() && !d.exists()) {
/* 181 */                 log("Unable to create directory " + d
/* 182 */                     .getAbsolutePath(), 0);
/*     */               } else {
/* 184 */                 createCount++;
/*     */               }  
/*     */           } 
/*     */         } 
/* 188 */         File fromDir = new File(fromDirName);
/* 189 */         if (!selfMove && okToDelete(fromDir)) {
/* 190 */           deleteDir(fromDir);
/*     */         }
/*     */       } 
/* 193 */       if (createCount > 0) {
/* 194 */         log("Moved " + this.dirCopyMap.size() + " empty director" + (
/*     */             
/* 196 */             (this.dirCopyMap.size() == 1) ? "y" : "ies") + " to " + createCount + " empty director" + (
/*     */ 
/*     */             
/* 199 */             (createCount == 1) ? "y" : "ies") + " under " + this.destDir
/* 200 */             .getAbsolutePath());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void moveFile(File fromFile, File toFile, boolean filtering, boolean overwrite) {
/* 210 */     boolean moved = false;
/*     */     try {
/* 212 */       log("Attempting to rename: " + fromFile + " to " + toFile, this.verbosity);
/* 213 */       moved = renameFile(fromFile, toFile, filtering, this.forceOverwrite);
/* 214 */     } catch (IOException ioe) {
/* 215 */       throw new BuildException("Failed to rename " + fromFile + " to " + toFile + " due to " + ioe
/* 216 */           .getMessage(), ioe, getLocation());
/*     */     } 
/*     */     
/* 219 */     if (!moved) {
/* 220 */       copyFile(fromFile, toFile, filtering, overwrite);
/* 221 */       if (!getFileUtils().tryHardToDelete(fromFile, this.performGc)) {
/* 222 */         throw new BuildException("Unable to delete file %s", new Object[] { fromFile
/* 223 */               .getAbsolutePath() });
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
/*     */   private void copyFile(File fromFile, File toFile, boolean filtering, boolean overwrite) {
/*     */     try {
/* 237 */       log("Copying " + fromFile + " to " + toFile, this.verbosity);
/*     */       
/* 239 */       FilterSetCollection executionFilters = new FilterSetCollection();
/* 240 */       if (filtering) {
/* 241 */         executionFilters.addFilterSet(getProject().getGlobalFilterSet());
/*     */       }
/* 243 */       Objects.requireNonNull(executionFilters); getFilterSets().forEach(executionFilters::addFilterSet);
/* 244 */       getFileUtils().copyFile(fromFile, toFile, executionFilters, 
/* 245 */           getFilterChains(), this.forceOverwrite, 
/*     */           
/* 247 */           getPreserveLastModified(), false, 
/*     */           
/* 249 */           getEncoding(), 
/* 250 */           getOutputEncoding(), 
/* 251 */           getProject(), getForce());
/* 252 */     } catch (IOException ioe) {
/* 253 */       throw new BuildException("Failed to copy " + fromFile + " to " + toFile + " due to " + ioe
/* 254 */           .getMessage(), ioe, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean okToDelete(File d) {
/* 264 */     String[] list = d.list();
/* 265 */     if (list == null) {
/* 266 */       return false;
/*     */     }
/*     */     
/* 269 */     for (String s : list) {
/* 270 */       File f = new File(d, s);
/* 271 */       if (f.isDirectory()) {
/* 272 */         if (!okToDelete(f)) {
/* 273 */           return false;
/*     */         }
/*     */       } else {
/* 276 */         return false;
/*     */       } 
/*     */     } 
/* 279 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deleteDir(File d) {
/* 287 */     deleteDir(d, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deleteDir(File d, boolean deleteFiles) {
/* 296 */     String[] list = d.list();
/* 297 */     if (list == null) {
/*     */       return;
/*     */     }
/*     */     
/* 301 */     for (String s : list) {
/* 302 */       File f = new File(d, s);
/* 303 */       if (f.isDirectory())
/* 304 */       { deleteDir(f); }
/* 305 */       else { if (deleteFiles && 
/* 306 */           !getFileUtils().tryHardToDelete(f, this.performGc)) {
/* 307 */           throw new BuildException("Unable to delete file %s", new Object[] { f
/* 308 */                 .getAbsolutePath() });
/*     */         }
/* 310 */         throw new BuildException("UNEXPECTED ERROR - The file %s should not exist!", new Object[] { f
/*     */               
/* 312 */               .getAbsolutePath() }); }
/*     */     
/*     */     } 
/* 315 */     log("Deleting directory " + d.getAbsolutePath(), this.verbosity);
/* 316 */     if (!getFileUtils().tryHardToDelete(d, this.performGc)) {
/* 317 */       throw new BuildException("Unable to delete directory %s", new Object[] { d
/* 318 */             .getAbsolutePath() });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean renameFile(File sourceFile, File destFile, boolean filtering, boolean overwrite) throws IOException, BuildException {
/* 342 */     if (destFile.isDirectory() || filtering || !getFilterSets().isEmpty() || 
/* 343 */       !getFilterChains().isEmpty()) {
/* 344 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 348 */     if (destFile.isFile() && !destFile.canWrite()) {
/* 349 */       if (!getForce()) {
/* 350 */         throw new IOException(String.format("can't replace read-only destination file %s", new Object[] { destFile }));
/*     */       }
/*     */       
/* 353 */       if (!getFileUtils().tryHardToDelete(destFile)) {
/* 354 */         throw new IOException(String.format("failed to delete read-only destination file %s", new Object[] { destFile }));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     File parent = destFile.getParentFile();
/* 362 */     if (parent != null && !parent.exists()) {
/* 363 */       parent.mkdirs();
/* 364 */     } else if (destFile.isFile()) {
/* 365 */       sourceFile = getFileUtils().normalize(sourceFile.getAbsolutePath()).getCanonicalFile();
/* 366 */       destFile = getFileUtils().normalize(destFile.getAbsolutePath());
/* 367 */       if (destFile.getAbsolutePath().equals(sourceFile.getAbsolutePath())) {
/*     */         
/* 369 */         log("Rename of " + sourceFile + " to " + destFile + " is a no-op.", 3);
/*     */         
/* 371 */         return true;
/*     */       } 
/* 373 */       if (!getFileUtils().areSame(sourceFile, destFile) && 
/* 374 */         !getFileUtils().tryHardToDelete(destFile, this.performGc)) {
/* 375 */         throw new BuildException("Unable to remove existing file %s", new Object[] { destFile });
/*     */       }
/*     */     } 
/*     */     
/* 379 */     return sourceFile.renameTo(destFile);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Move.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */