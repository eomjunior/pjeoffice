/*   */ package org.zeroturnaround.zip;
/*   */ 
/*   */ public class ZipBreakException extends RuntimeException {
/*   */   public ZipBreakException(String msg) {
/* 5 */     super(msg);
/*   */   }
/*   */   
/*   */   public ZipBreakException(Exception e) {
/* 9 */     super(e);
/*   */   }
/*   */   
/*   */   public ZipBreakException() {}
/*   */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZipBreakException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */