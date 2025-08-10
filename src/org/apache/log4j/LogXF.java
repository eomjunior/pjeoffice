/*     */ package org.apache.log4j;
/*     */ 
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
/*     */ public abstract class LogXF
/*     */ {
/*  33 */   protected static final Level TRACE = new Level(5000, "TRACE", 7);
/*     */ 
/*     */ 
/*     */   
/*  37 */   private static final String FQCN = LogXF.class.getName();
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
/*     */   protected static Boolean valueOf(boolean b) {
/*  50 */     if (b) {
/*  51 */       return Boolean.TRUE;
/*     */     }
/*  53 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Character valueOf(char c) {
/*  64 */     return new Character(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Byte valueOf(byte b) {
/*  75 */     return new Byte(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Short valueOf(short b) {
/*  86 */     return new Short(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Integer valueOf(int b) {
/*  97 */     return new Integer(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Long valueOf(long b) {
/* 108 */     return new Long(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Float valueOf(float b) {
/* 119 */     return new Float(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Double valueOf(double b) {
/* 130 */     return new Double(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Object[] toArray(Object param1) {
/* 140 */     return new Object[] { param1 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Object[] toArray(Object param1, Object param2) {
/* 151 */     return new Object[] { param1, param2 };
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
/*     */   protected static Object[] toArray(Object param1, Object param2, Object param3) {
/* 163 */     return new Object[] { param1, param2, param3 };
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
/*     */   
/*     */   protected static Object[] toArray(Object param1, Object param2, Object param3, Object param4) {
/* 177 */     return new Object[] { param1, param2, param3, param4 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void entering(Logger logger, String sourceClass, String sourceMethod) {
/* 188 */     if (logger.isDebugEnabled()) {
/* 189 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " ENTRY", null));
/*     */     }
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
/*     */   
/*     */   public static void entering(Logger logger, String sourceClass, String sourceMethod, String param) {
/* 204 */     if (logger.isDebugEnabled()) {
/* 205 */       String msg = sourceClass + "." + sourceMethod + " ENTRY " + param;
/* 206 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
/*     */     } 
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
/*     */   public static void entering(Logger logger, String sourceClass, String sourceMethod, Object param) {
/* 220 */     if (logger.isDebugEnabled()) {
/* 221 */       String msg = sourceClass + "." + sourceMethod + " ENTRY ";
/* 222 */       if (param == null) {
/* 223 */         msg = msg + "null";
/*     */       } else {
/*     */         try {
/* 226 */           msg = msg + param;
/* 227 */         } catch (Throwable ex) {
/* 228 */           msg = msg + "?";
/*     */         } 
/*     */       } 
/* 231 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
/*     */     } 
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
/*     */   public static void entering(Logger logger, String sourceClass, String sourceMethod, Object[] params) {
/* 245 */     if (logger.isDebugEnabled()) {
/* 246 */       String msg = sourceClass + "." + sourceMethod + " ENTRY ";
/* 247 */       if (params != null && params.length > 0) {
/* 248 */         String delim = "{";
/* 249 */         for (int i = 0; i < params.length; i++) {
/*     */           try {
/* 251 */             msg = msg + delim + params[i];
/* 252 */           } catch (Throwable ex) {
/* 253 */             msg = msg + delim + "?";
/*     */           } 
/* 255 */           delim = ",";
/*     */         } 
/* 257 */         msg = msg + "}";
/*     */       } else {
/* 259 */         msg = msg + "{}";
/*     */       } 
/* 261 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void exiting(Logger logger, String sourceClass, String sourceMethod) {
/* 273 */     if (logger.isDebugEnabled()) {
/* 274 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " RETURN", null));
/*     */     }
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
/*     */   
/*     */   public static void exiting(Logger logger, String sourceClass, String sourceMethod, String result) {
/* 289 */     if (logger.isDebugEnabled()) {
/* 290 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " RETURN " + result, null));
/*     */     }
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
/*     */   
/*     */   public static void exiting(Logger logger, String sourceClass, String sourceMethod, Object result) {
/* 305 */     if (logger.isDebugEnabled()) {
/* 306 */       String msg = sourceClass + "." + sourceMethod + " RETURN ";
/* 307 */       if (result == null) {
/* 308 */         msg = msg + "null";
/*     */       } else {
/*     */         try {
/* 311 */           msg = msg + result;
/* 312 */         } catch (Throwable ex) {
/* 313 */           msg = msg + "?";
/*     */         } 
/*     */       } 
/* 316 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
/*     */     } 
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
/*     */   public static void throwing(Logger logger, String sourceClass, String sourceMethod, Throwable thrown) {
/* 330 */     if (logger.isDebugEnabled())
/* 331 */       logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " THROW", thrown)); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/LogXF.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */