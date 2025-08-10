/*    */ package org.apache.log4j.pattern;
/*    */ 
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
/*    */ public final class LoggerPatternConverter
/*    */   extends NamePatternConverter
/*    */ {
/* 32 */   private static final LoggerPatternConverter INSTANCE = new LoggerPatternConverter(null);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LoggerPatternConverter(String[] options) {
/* 40 */     super("Logger", "logger", options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LoggerPatternConverter newInstance(String[] options) {
/* 50 */     if (options == null || options.length == 0) {
/* 51 */       return INSTANCE;
/*    */     }
/*    */     
/* 54 */     return new LoggerPatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 61 */     int initialLength = toAppendTo.length();
/* 62 */     toAppendTo.append(event.getLoggerName());
/* 63 */     abbreviate(initialLength, toAppendTo);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/LoggerPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */