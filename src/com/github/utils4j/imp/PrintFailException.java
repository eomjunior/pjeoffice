/*   */ package com.github.utils4j.imp;
/*   */ 
/*   */ public class PrintFailException extends Exception {
/*   */   public PrintFailException(Exception e) {
/* 5 */     super(e);
/*   */   }
/*   */   
/*   */   public PrintFailException(String message, Exception e) {
/* 9 */     super(message, e);
/*   */   }
/*   */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/PrintFailException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */