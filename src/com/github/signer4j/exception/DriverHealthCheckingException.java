/*    */ package com.github.signer4j.exception;
/*    */ 
/*    */ import com.github.utils4j.imp.Directory;
/*    */ import java.io.File;
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
/*    */ 
/*    */ 
/*    */ public class DriverHealthCheckingException
/*    */   extends DriverException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final String MESSAGE = "This library %s apparently is not compatible with your operating system and has been rejected by crash prevention (see log details).\n %s";
/*    */   
/*    */   public DriverHealthCheckingException(File library, String message) {
/* 41 */     this(library, message, null);
/*    */   }
/*    */   
/*    */   public DriverHealthCheckingException(File library, String message, Exception cause) {
/* 45 */     super(String.format("This library %s apparently is not compatible with your operating system and has been rejected by crash prevention (see log details).\n %s", new Object[] { Directory.stringPath(library, true), message }), cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/exception/DriverHealthCheckingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */