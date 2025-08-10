/*    */ package com.github.taskresolver4j.exception;
/*    */ 
/*    */ import com.github.taskresolver4j.IExceptionContext;
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ public final class ExceptionContext
/*    */   implements IExceptionContext
/*    */ {
/*    */   private final String detail;
/*    */   private final String message;
/*    */   private final Throwable cause;
/*    */   
/*    */   public ExceptionContext(String message, String detail, Throwable cause) {
/* 39 */     this.message = Strings.trim(message);
/* 40 */     this.detail = Strings.trim(detail);
/* 41 */     this.cause = cause;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDetail() {
/* 46 */     return this.detail;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 51 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 56 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/exception/ExceptionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */