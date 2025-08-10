/*     */ package org.apache.log4j.spi;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.Category;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
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
/*     */ public final class NOPLoggerRepository
/*     */   implements LoggerRepository
/*     */ {
/*     */   public void addHierarchyEventListener(HierarchyEventListener listener) {}
/*     */   
/*     */   public boolean isDisabled(int level) {
/*  44 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreshold(Level level) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreshold(String val) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void emitNoAppenderWarning(Category cat) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getThreshold() {
/*  69 */     return Level.OFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger(String name) {
/*  76 */     return new NOPLogger(this, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger(String name, LoggerFactory factory) {
/*  83 */     return new NOPLogger(this, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getRootLogger() {
/*  90 */     return new NOPLogger(this, "root");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger exists(String name) {
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getCurrentLoggers() {
/* 110 */     return (new Vector()).elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getCurrentCategories() {
/* 117 */     return getCurrentLoggers();
/*     */   }
/*     */   
/*     */   public void fireAddAppenderEvent(Category logger, Appender appender) {}
/*     */   
/*     */   public void resetConfiguration() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/NOPLoggerRepository.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */