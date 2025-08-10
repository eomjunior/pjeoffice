/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.spi.ErrorHandler;
/*     */ import org.apache.log4j.spi.LoggingEvent;
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
/*     */ public class FallbackErrorHandler
/*     */   implements ErrorHandler
/*     */ {
/*     */   Appender backup;
/*     */   Appender primary;
/*     */   Vector loggers;
/*     */   
/*     */   public void setLogger(Logger logger) {
/*  54 */     LogLog.debug("FB: Adding logger [" + logger.getName() + "].");
/*  55 */     if (this.loggers == null) {
/*  56 */       this.loggers = new Vector();
/*     */     }
/*  58 */     this.loggers.addElement(logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, Exception e, int errorCode) {
/*  72 */     error(message, e, errorCode, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, Exception e, int errorCode, LoggingEvent event) {
/*  80 */     if (e instanceof java.io.InterruptedIOException) {
/*  81 */       Thread.currentThread().interrupt();
/*     */     }
/*  83 */     LogLog.debug("FB: The following error reported: " + message, e);
/*  84 */     LogLog.debug("FB: INITIATING FALLBACK PROCEDURE.");
/*  85 */     if (this.loggers != null) {
/*  86 */       for (int i = 0; i < this.loggers.size(); i++) {
/*  87 */         Logger l = this.loggers.elementAt(i);
/*  88 */         LogLog.debug("FB: Searching for [" + this.primary.getName() + "] in logger [" + l.getName() + "].");
/*  89 */         LogLog.debug("FB: Replacing [" + this.primary.getName() + "] by [" + this.backup.getName() + "] in logger [" + l
/*  90 */             .getName() + "].");
/*  91 */         l.removeAppender(this.primary);
/*  92 */         LogLog.debug("FB: Adding appender [" + this.backup.getName() + "] to logger " + l.getName());
/*  93 */         l.addAppender(this.backup);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppender(Appender primary) {
/* 112 */     LogLog.debug("FB: Setting primary appender to [" + primary.getName() + "].");
/* 113 */     this.primary = primary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackupAppender(Appender backup) {
/* 120 */     LogLog.debug("FB: Setting backup appender to [" + backup.getName() + "].");
/* 121 */     this.backup = backup;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/FallbackErrorHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */