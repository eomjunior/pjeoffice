/*    */ package org.slf4j.reload4j;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.helpers.Reporter;
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
/*    */ public class Reload4jLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/*    */   private static final String LOG4J_DELEGATION_LOOP_URL = "http://www.slf4j.org/codes.html#log4jDelegationLoop";
/*    */   ConcurrentMap<String, Logger> loggerMap;
/*    */   
/*    */   static {
/*    */     try {
/* 49 */       Class.forName("org.apache.log4j.Log4jLoggerFactory");
/* 50 */       String part1 = "Detected both log4j-over-slf4j.jar AND bound slf4j-log4j12.jar on the class path, preempting StackOverflowError. ";
/* 51 */       String part2 = "See also http://www.slf4j.org/codes.html#log4jDelegationLoop for more details.";
/*    */       
/* 53 */       Reporter.error(part1);
/* 54 */       Reporter.error(part2);
/* 55 */       throw new IllegalStateException(part1 + part2);
/* 56 */     } catch (ClassNotFoundException classNotFoundException) {
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Reload4jLoggerFactory() {
/* 65 */     this.loggerMap = new ConcurrentHashMap<>();
/*    */     
/* 67 */     LogManager.getRootLogger();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Logger getLogger(String name) {
/*    */     Logger log4jLogger;
/* 76 */     Logger slf4jLogger = this.loggerMap.get(name);
/* 77 */     if (slf4jLogger != null) {
/* 78 */       return slf4jLogger;
/*    */     }
/*    */     
/* 81 */     if (name.equalsIgnoreCase("ROOT")) {
/* 82 */       log4jLogger = LogManager.getRootLogger();
/*    */     } else {
/* 84 */       log4jLogger = LogManager.getLogger(name);
/*    */     } 
/* 86 */     Reload4jLoggerAdapter reload4jLoggerAdapter = new Reload4jLoggerAdapter(log4jLogger);
/* 87 */     Logger oldInstance = (Logger)this.loggerMap.putIfAbsent(name, reload4jLoggerAdapter);
/* 88 */     return (oldInstance == null) ? (Logger)reload4jLoggerAdapter : oldInstance;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/reload4j/Reload4jLoggerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */