/*     */ package com.yworks.logging;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public abstract class Logger
/*     */ {
/*     */   private static List<Logger> instances;
/*     */   
/*     */   public enum ShrinkType
/*     */   {
/*  21 */     CLASS,
/*     */ 
/*     */ 
/*     */     
/*  25 */     METHOD,
/*     */ 
/*     */ 
/*     */     
/*  29 */     FIELD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void register() {
/*  38 */     if (null == instances) {
/*  39 */       instances = new ArrayList<>();
/*     */     }
/*  41 */     instances.add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void unregister() {
/*  49 */     instances.remove(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void log(String s) {
/*  59 */     if (null != instances) {
/*  60 */       for (Logger logger : instances) {
/*  61 */         logger.doLog(s);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void err(String s) {
/*  72 */     if (null != instances) {
/*  73 */       for (Logger logger : instances) {
/*  74 */         logger.doErr(s);
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
/*     */   public static void err(String s, Throwable ex) {
/*  86 */     if (null != instances) {
/*  87 */       for (Logger logger : instances) {
/*  88 */         logger.doErr(s, ex);
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
/*     */   public static void warn(String s) {
/* 100 */     if (null != instances) {
/* 101 */       for (Logger logger : instances) {
/* 102 */         logger.doWarn(s);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void warnToLog(String s) {
/* 113 */     if (null != instances) {
/* 114 */       for (Logger logger : instances) {
/* 115 */         logger.doWarnToLog(s);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shrinkLog(String s) {
/* 126 */     if (null != instances)
/* 127 */       for (Logger logger : instances)
/* 128 */         logger.doShrinkLog(s);  
/*     */   }
/*     */   
/*     */   public abstract void doLog(String paramString);
/*     */   
/*     */   public abstract void doErr(String paramString);
/*     */   
/*     */   public abstract void doWarn(String paramString);
/*     */   
/*     */   public abstract void doWarnToLog(String paramString);
/*     */   
/*     */   public abstract void doShrinkLog(String paramString);
/*     */   
/*     */   public abstract void doErr(String paramString, Throwable paramThrowable);
/*     */   
/*     */   public abstract void close();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/logging/Logger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */