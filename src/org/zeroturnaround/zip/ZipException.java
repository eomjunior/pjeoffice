/*    */ package org.zeroturnaround.zip;
/*    */ 
/*    */ public class ZipException extends RuntimeException {
/*    */   public ZipException(String msg) {
/*  5 */     super(msg);
/*    */   }
/*    */   
/*    */   public ZipException(Exception e) {
/*  9 */     super(e);
/*    */   }
/*    */   
/*    */   public ZipException(String msg, Throwable cause) {
/* 13 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZipException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */