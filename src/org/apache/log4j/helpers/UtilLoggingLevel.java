/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.log4j.Level;
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
/*     */ public class UtilLoggingLevel
/*     */   extends Level
/*     */ {
/*     */   private static final long serialVersionUID = 909301162611820211L;
/*     */   public static final int SEVERE_INT = 22000;
/*     */   public static final int WARNING_INT = 21000;
/*     */   public static final int CONFIG_INT = 14000;
/*     */   public static final int FINE_INT = 13000;
/*     */   public static final int FINER_INT = 12000;
/*     */   public static final int FINEST_INT = 11000;
/*     */   public static final int UNKNOWN_INT = 10000;
/*  75 */   public static final UtilLoggingLevel SEVERE = new UtilLoggingLevel(22000, "SEVERE", 0);
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final UtilLoggingLevel WARNING = new UtilLoggingLevel(21000, "WARNING", 4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final UtilLoggingLevel INFO = new UtilLoggingLevel(20000, "INFO", 5);
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final UtilLoggingLevel CONFIG = new UtilLoggingLevel(14000, "CONFIG", 6);
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static final UtilLoggingLevel FINE = new UtilLoggingLevel(13000, "FINE", 7);
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final UtilLoggingLevel FINER = new UtilLoggingLevel(12000, "FINER", 8);
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static final UtilLoggingLevel FINEST = new UtilLoggingLevel(11000, "FINEST", 9);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UtilLoggingLevel(int level, String levelStr, int syslogEquivalent) {
/* 111 */     super(level, levelStr, syslogEquivalent);
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
/*     */   public static UtilLoggingLevel toLevel(int val, UtilLoggingLevel defaultLevel) {
/* 123 */     switch (val) {
/*     */       case 22000:
/* 125 */         return SEVERE;
/*     */       
/*     */       case 21000:
/* 128 */         return WARNING;
/*     */       
/*     */       case 20000:
/* 131 */         return INFO;
/*     */       
/*     */       case 14000:
/* 134 */         return CONFIG;
/*     */       
/*     */       case 13000:
/* 137 */         return FINE;
/*     */       
/*     */       case 12000:
/* 140 */         return FINER;
/*     */       
/*     */       case 11000:
/* 143 */         return FINEST;
/*     */     } 
/*     */     
/* 146 */     return defaultLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(int val) {
/* 157 */     return toLevel(val, FINEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List getAllPossibleLevels() {
/* 166 */     ArrayList<UtilLoggingLevel> list = new ArrayList();
/* 167 */     list.add(FINE);
/* 168 */     list.add(FINER);
/* 169 */     list.add(FINEST);
/* 170 */     list.add(INFO);
/* 171 */     list.add(CONFIG);
/* 172 */     list.add(WARNING);
/* 173 */     list.add(SEVERE);
/* 174 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String s) {
/* 184 */     return toLevel(s, Level.DEBUG);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String sArg, Level defaultLevel) {
/* 195 */     if (sArg == null) {
/* 196 */       return defaultLevel;
/*     */     }
/*     */     
/* 199 */     if (sArg.equalsIgnoreCase("SEVERE")) {
/* 200 */       return SEVERE;
/*     */     }
/*     */     
/* 203 */     if (sArg.equalsIgnoreCase("WARNING")) {
/* 204 */       return WARNING;
/*     */     }
/*     */     
/* 207 */     if (sArg.equalsIgnoreCase("INFO")) {
/* 208 */       return INFO;
/*     */     }
/*     */     
/* 211 */     if (sArg.equalsIgnoreCase("CONFI") || sArg.equalsIgnoreCase("CONFIG")) {
/* 212 */       return CONFIG;
/*     */     }
/*     */     
/* 215 */     if (sArg.equalsIgnoreCase("FINE")) {
/* 216 */       return FINE;
/*     */     }
/*     */     
/* 219 */     if (sArg.equalsIgnoreCase("FINER")) {
/* 220 */       return FINER;
/*     */     }
/*     */     
/* 223 */     if (sArg.equalsIgnoreCase("FINEST")) {
/* 224 */       return FINEST;
/*     */     }
/* 226 */     return defaultLevel;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/UtilLoggingLevel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */