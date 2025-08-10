/*   */ package org.zeroturnaround.zip;
/*   */ 
/*   */ import java.io.File;
/*   */ 
/*   */ public class MaliciousZipException
/*   */   extends ZipException {
/*   */   public MaliciousZipException(File outputDir, String name) {
/* 8 */     super("The file " + name + " is trying to leave the target output directory of " + outputDir);
/*   */   }
/*   */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/MaliciousZipException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */