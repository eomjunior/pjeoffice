/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DifferentSelector
/*     */   extends MappingSelector
/*     */ {
/*  53 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*     */   private boolean ignoreFileTimes = true;
/*     */ 
/*     */   
/*     */   private boolean ignoreContents = false;
/*     */ 
/*     */   
/*     */   public void setIgnoreFileTimes(boolean ignoreFileTimes) {
/*  63 */     this.ignoreFileTimes = ignoreFileTimes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreContents(boolean ignoreContents) {
/*  72 */     this.ignoreContents = ignoreContents;
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
/*     */   protected boolean selectionTest(File srcfile, File destfile) {
/*  84 */     if (srcfile.exists() != destfile.exists()) {
/*  85 */       return true;
/*     */     }
/*     */     
/*  88 */     if (srcfile.length() != destfile.length())
/*     */     {
/*  90 */       return true;
/*     */     }
/*     */     
/*  93 */     if (!this.ignoreFileTimes) {
/*     */ 
/*     */ 
/*     */       
/*  97 */       boolean sameDate = (destfile.lastModified() >= srcfile.lastModified() - this.granularity && destfile.lastModified() <= srcfile.lastModified() + this.granularity);
/*     */ 
/*     */       
/* 100 */       if (!sameDate) {
/* 101 */         return true;
/*     */       }
/*     */     } 
/* 104 */     if (this.ignoreContents) {
/* 105 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 109 */       return !FILE_UTILS.contentEquals(srcfile, destfile);
/* 110 */     } catch (IOException e) {
/* 111 */       throw new BuildException("while comparing " + srcfile + " and " + destfile, e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/DifferentSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */