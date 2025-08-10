/*     */ package org.apache.log4j;
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
/*     */ public class Priority
/*     */ {
/*     */   transient int level;
/*     */   transient String levelStr;
/*     */   transient int syslogEquivalent;
/*     */   public static final int OFF_INT = 2147483647;
/*     */   public static final int FATAL_INT = 50000;
/*     */   public static final int ERROR_INT = 40000;
/*     */   public static final int WARN_INT = 30000;
/*     */   public static final int INFO_INT = 20000;
/*     */   public static final int DEBUG_INT = 10000;
/*     */   public static final int ALL_INT = -2147483648;
/*  46 */   public static final Priority FATAL = new Level(50000, "FATAL", 0);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final Priority ERROR = new Level(40000, "ERROR", 3);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final Priority WARN = new Level(30000, "WARN", 4);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final Priority INFO = new Level(20000, "INFO", 6);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final Priority DEBUG = new Level(10000, "DEBUG", 7);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Priority() {
/*  72 */     this.level = 10000;
/*  73 */     this.levelStr = "DEBUG";
/*  74 */     this.syslogEquivalent = 7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Priority(int level, String levelStr, int syslogEquivalent) {
/*  81 */     this.level = level;
/*  82 */     this.levelStr = levelStr;
/*  83 */     this.syslogEquivalent = syslogEquivalent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  92 */     if (o instanceof Priority) {
/*  93 */       Priority r = (Priority)o;
/*  94 */       return (this.level == r.level);
/*     */     } 
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getSyslogEquivalent() {
/* 104 */     return this.syslogEquivalent;
/*     */   }
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
/*     */   public boolean isGreaterOrEqual(Priority r) {
/* 117 */     return (this.level >= r.level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Priority[] getAllPossiblePriorities() {
/* 127 */     return new Priority[] { FATAL, ERROR, Level.WARN, INFO, DEBUG };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 134 */     return this.levelStr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int toInt() {
/* 141 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Priority toPriority(String sArg) {
/* 148 */     return Level.toLevel(sArg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Priority toPriority(int val) {
/* 155 */     return toPriority(val, DEBUG);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Priority toPriority(int val, Priority defaultPriority) {
/* 162 */     return Level.toLevel(val, (Level)defaultPriority);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Priority toPriority(String sArg, Priority defaultPriority) {
/* 170 */     return Level.toLevel(sArg, (Level)defaultPriority);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/Priority.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */