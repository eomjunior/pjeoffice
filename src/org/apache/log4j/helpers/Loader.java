/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
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
/*     */ public class Loader
/*     */ {
/*     */   static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
/*     */   private static boolean java1 = true;
/*     */   private static boolean ignoreTCL = false;
/*     */   
/*     */   static {
/*  41 */     String prop = OptionConverter.getSystemProperty("java.version", null);
/*     */     
/*  43 */     if (prop != null) {
/*  44 */       int i = prop.indexOf('.');
/*  45 */       if (i != -1 && 
/*  46 */         prop.charAt(i + 1) != '1') {
/*  47 */         java1 = false;
/*     */       }
/*     */     } 
/*  50 */     String ignoreTCLProp = OptionConverter.getSystemProperty("log4j.ignoreTCL", null);
/*  51 */     if (ignoreTCLProp != null) {
/*  52 */       ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
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
/*     */   public static URL getResource(String resource, Class clazz) {
/*  65 */     return getResource(resource);
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
/*     */   public static URL getResource(String resource) {
/*  89 */     ClassLoader classLoader = null;
/*  90 */     URL url = null;
/*     */     
/*     */     try {
/*  93 */       if (!java1 && !ignoreTCL) {
/*  94 */         classLoader = getTCL();
/*  95 */         if (classLoader != null) {
/*  96 */           LogLog.debug("Trying to find [" + resource + "] using context classloader " + classLoader + ".");
/*  97 */           url = classLoader.getResource(resource);
/*  98 */           if (url != null) {
/*  99 */             return url;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 106 */       classLoader = Loader.class.getClassLoader();
/* 107 */       if (classLoader != null) {
/* 108 */         LogLog.debug("Trying to find [" + resource + "] using " + classLoader + " class loader.");
/* 109 */         url = classLoader.getResource(resource);
/* 110 */         if (url != null) {
/* 111 */           return url;
/*     */         }
/*     */       } 
/* 114 */     } catch (IllegalAccessException t) {
/* 115 */       LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/* 116 */     } catch (InvocationTargetException t) {
/* 117 */       if (t.getTargetException() instanceof InterruptedException || t
/* 118 */         .getTargetException() instanceof java.io.InterruptedIOException) {
/* 119 */         Thread.currentThread().interrupt();
/*     */       }
/* 121 */       LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/* 122 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */       
/* 126 */       LogLog.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     LogLog.debug("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
/* 134 */     return ClassLoader.getSystemResource(resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJava1() {
/* 141 */     return java1;
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
/*     */   private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
/* 153 */     Method method = null;
/*     */     try {
/* 155 */       method = Thread.class.getMethod("getContextClassLoader", null);
/* 156 */     } catch (NoSuchMethodException e) {
/*     */       
/* 158 */       return null;
/*     */     } 
/*     */     
/* 161 */     return (ClassLoader)method.invoke(Thread.currentThread(), null);
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
/*     */   public static Class loadClass(String clazz) throws ClassNotFoundException {
/* 173 */     if (java1 || ignoreTCL) {
/* 174 */       return Class.forName(clazz);
/*     */     }
/*     */     try {
/* 177 */       return getTCL().loadClass(clazz);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 182 */     catch (InvocationTargetException e) {
/* 183 */       if (e.getTargetException() instanceof InterruptedException || e
/* 184 */         .getTargetException() instanceof java.io.InterruptedIOException) {
/* 185 */         Thread.currentThread().interrupt();
/*     */       }
/* 187 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/* 190 */     return Class.forName(clazz);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/Loader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */