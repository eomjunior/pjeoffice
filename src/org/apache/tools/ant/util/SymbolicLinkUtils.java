/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SymbolicLinkUtils
/*     */ {
/*  40 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final SymbolicLinkUtils PRIMARY_INSTANCE = new SymbolicLinkUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SymbolicLinkUtils getSymbolicLinkUtils() {
/*  55 */     return PRIMARY_INSTANCE;
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
/*     */   public boolean isSymbolicLink(File file) throws IOException {
/*  77 */     return isSymbolicLink(file.getParentFile(), file.getName());
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
/*     */   public boolean isSymbolicLink(String name) throws IOException {
/*  93 */     return isSymbolicLink(new File(name));
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
/*     */   public boolean isSymbolicLink(File parent, String name) throws IOException {
/* 113 */     File toTest = (parent != null) ? new File(parent.getCanonicalPath(), name) : new File(name);
/* 114 */     return !toTest.getAbsolutePath().equals(toTest.getCanonicalPath());
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
/*     */   public boolean isDanglingSymbolicLink(String name) throws IOException {
/* 135 */     return isDanglingSymbolicLink(new File(name));
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
/*     */   public boolean isDanglingSymbolicLink(File file) throws IOException {
/* 156 */     return isDanglingSymbolicLink(file.getParentFile(), file.getName());
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
/*     */   public boolean isDanglingSymbolicLink(File parent, String name) throws IOException {
/* 179 */     File f = new File(parent, name);
/* 180 */     if (!f.exists()) {
/* 181 */       String localName = f.getName();
/* 182 */       String[] c = parent.list((d, n) -> localName.equals(n));
/* 183 */       return (c != null && c.length > 0);
/*     */     } 
/* 185 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteSymbolicLink(File link, Task task) throws IOException {
/* 217 */     if (isDanglingSymbolicLink(link)) {
/* 218 */       if (!link.delete()) {
/* 219 */         throw new IOException("failed to remove dangling symbolic link " + link);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 225 */     if (!isSymbolicLink(link)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 230 */     if (!link.exists()) {
/* 231 */       throw new FileNotFoundException("No such symbolic link: " + link);
/*     */     }
/*     */ 
/*     */     
/* 235 */     File target = link.getCanonicalFile();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     if (task == null || target.getParentFile().canWrite()) {
/*     */ 
/*     */       
/* 244 */       Project project = (task == null) ? null : task.getProject();
/* 245 */       File temp = FILE_UTILS.createTempFile(project, "symlink", ".tmp", target
/* 246 */           .getParentFile(), false, false);
/*     */       
/* 248 */       if (FILE_UTILS.isLeadingPath(target, link))
/*     */       {
/*     */ 
/*     */         
/* 252 */         link = new File(temp, FILE_UTILS.removeLeadingPath(target, link));
/*     */       }
/*     */       
/* 255 */       boolean renamedTarget = false;
/* 256 */       boolean success = false;
/*     */       try {
/*     */         try {
/* 259 */           FILE_UTILS.rename(target, temp);
/* 260 */           renamedTarget = true;
/* 261 */         } catch (IOException e) {
/* 262 */           throw new IOException("Couldn't rename resource when attempting to delete '" + link + "'.  Reason: " + e
/*     */               
/* 264 */               .getMessage());
/*     */         } 
/*     */         
/* 267 */         if (!link.delete()) {
/* 268 */           throw new IOException("Couldn't delete symlink: " + link + " (was it a real file? is this not a UNIX system?)");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 273 */         success = true;
/*     */       } finally {
/* 275 */         if (renamedTarget) {
/*     */           
/*     */           try {
/* 278 */             FILE_UTILS.rename(temp, target);
/* 279 */           } catch (IOException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 284 */             String msg = "Couldn't return resource " + temp + " to its original name: " + target.getAbsolutePath() + ". Reason: " + e.getMessage() + "\n THE RESOURCE'S NAME ON DISK HAS BEEN CHANGED BY THIS ERROR!\n";
/*     */ 
/*     */ 
/*     */             
/* 288 */             if (success) {
/* 289 */               throw new IOException(msg);
/*     */             }
/* 291 */             System.err.println(msg);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 297 */       Execute.runCommand(task, new String[] { "rm", link.getAbsolutePath() });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/SymbolicLinkUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */