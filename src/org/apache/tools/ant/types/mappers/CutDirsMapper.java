/*    */ package org.apache.tools.ant.types.mappers;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.util.FileNameMapper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CutDirsMapper
/*    */   implements FileNameMapper
/*    */ {
/* 36 */   private int dirs = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDirs(int dirs) {
/* 43 */     this.dirs = dirs;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFrom(String ignore) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTo(String ignore) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] mapFileName(String sourceFileName) {
/* 65 */     if (this.dirs <= 0) {
/* 66 */       throw new BuildException("dirs must be set to a positive number");
/*    */     }
/* 68 */     char fileSep = File.separatorChar;
/* 69 */     if (sourceFileName == null) {
/* 70 */       return null;
/*    */     }
/*    */     
/* 73 */     String fileSepCorrected = sourceFileName.replace('/', fileSep).replace('\\', fileSep);
/* 74 */     int nthMatch = fileSepCorrected.indexOf(fileSep);
/* 75 */     for (int n = 1; nthMatch > -1 && n < this.dirs; n++) {
/* 76 */       nthMatch = fileSepCorrected.indexOf(fileSep, nthMatch + 1);
/*    */     }
/* 78 */     if (nthMatch == -1) {
/* 79 */       return null;
/*    */     }
/* 81 */     return new String[] { sourceFileName.substring(nthMatch + 1) };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/mappers/CutDirsMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */