/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import org.apache.log4j.Appender;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.spi.ErrorHandler;
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OnlyOnceErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 45 */   final String WARN_PREFIX = "log4j warning: ";
/* 46 */   final String ERROR_PREFIX = "log4j error: ";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean firstTime = true;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogger(Logger logger) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void activateOptions() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message, Exception e, int errorCode) {
/* 67 */     error(message, e, errorCode, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message, Exception e, int errorCode, LoggingEvent event) {
/* 75 */     if (e instanceof java.io.InterruptedIOException || e instanceof InterruptedException) {
/* 76 */       Thread.currentThread().interrupt();
/*    */     }
/* 78 */     if (this.firstTime) {
/* 79 */       LogLog.error(message, e);
/* 80 */       this.firstTime = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message) {
/* 88 */     if (this.firstTime) {
/* 89 */       LogLog.error(message);
/* 90 */       this.firstTime = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setAppender(Appender appender) {}
/*    */   
/*    */   public void setBackupAppender(Appender appender) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/OnlyOnceErrorHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */