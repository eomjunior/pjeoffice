/*    */ package org.apache.tools.ant.types;
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
/*    */ public class LogLevel
/*    */   extends EnumeratedAttribute
/*    */ {
/* 29 */   public static final LogLevel ERR = new LogLevel("error");
/*    */ 
/*    */   
/* 32 */   public static final LogLevel WARN = new LogLevel("warn");
/*    */ 
/*    */   
/* 35 */   public static final LogLevel INFO = new LogLevel("info");
/*    */ 
/*    */   
/* 38 */   public static final LogLevel VERBOSE = new LogLevel("verbose");
/*    */ 
/*    */   
/* 41 */   public static final LogLevel DEBUG = new LogLevel("debug");
/*    */ 
/*    */ 
/*    */   
/*    */   public LogLevel() {}
/*    */ 
/*    */ 
/*    */   
/*    */   private LogLevel(String value) {
/* 50 */     this();
/* 51 */     setValue(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getValues() {
/* 60 */     return new String[] { "error", "warn", "warning", "info", "verbose", "debug" };
/*    */   }
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
/* 72 */   private static int[] levels = new int[] { 0, 1, 1, 2, 3, 4 };
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
/*    */   public int getLevel() {
/* 86 */     return levels[getIndex()];
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/LogLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */