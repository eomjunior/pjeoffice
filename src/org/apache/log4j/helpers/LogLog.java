/*     */ package org.apache.log4j.helpers;
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
/*     */ public class LogLog
/*     */ {
/*     */   public static final String DEBUG_KEY = "log4j.debug";
/*     */   public static final String CONFIG_DEBUG_KEY = "log4j.configDebug";
/*     */   protected static boolean debugEnabled = false;
/*     */   private static boolean quietMode = false;
/*     */   private static final String PREFIX = "log4j: ";
/*     */   private static final String ERR_PREFIX = "log4j:ERROR ";
/*     */   private static final String WARN_PREFIX = "log4j:WARN ";
/*     */   
/*     */   static {
/*  76 */     String key = OptionConverter.getSystemProperty("log4j.debug", null);
/*     */     
/*  78 */     if (key == null) {
/*  79 */       key = OptionConverter.getSystemProperty("log4j.configDebug", null);
/*     */     }
/*     */     
/*  82 */     if (key != null) {
/*  83 */       debugEnabled = OptionConverter.toBoolean(key, true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setInternalDebugging(boolean enabled) {
/*  91 */     debugEnabled = enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void debug(String msg) {
/*  99 */     if (debugEnabled && !quietMode) {
/* 100 */       System.out.println("log4j: " + msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void debug(String msg, Throwable t) {
/* 109 */     if (debugEnabled && !quietMode) {
/* 110 */       System.out.println("log4j: " + msg);
/* 111 */       if (t != null) {
/* 112 */         t.printStackTrace(System.out);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void error(String msg) {
/* 121 */     if (quietMode)
/*     */       return; 
/* 123 */     System.err.println("log4j:ERROR " + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void error(String msg, Throwable t) {
/* 131 */     if (quietMode) {
/*     */       return;
/*     */     }
/* 134 */     System.err.println("log4j:ERROR " + msg);
/* 135 */     if (t != null) {
/* 136 */       t.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setQuietMode(boolean quietMode) {
/* 146 */     LogLog.quietMode = quietMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void warn(String msg) {
/* 154 */     if (quietMode) {
/*     */       return;
/*     */     }
/* 157 */     System.err.println("log4j:WARN " + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void warn(String msg, Throwable t) {
/* 165 */     if (quietMode) {
/*     */       return;
/*     */     }
/* 168 */     System.err.println("log4j:WARN " + msg);
/* 169 */     if (t != null)
/* 170 */       t.printStackTrace(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/LogLog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */