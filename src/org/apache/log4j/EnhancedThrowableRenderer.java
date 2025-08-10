/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.spi.ThrowableRenderer;
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
/*     */ public final class EnhancedThrowableRenderer
/*     */   implements ThrowableRenderer
/*     */ {
/*     */   private Method getStackTraceMethod;
/*     */   private Method getClassNameMethod;
/*     */   
/*     */   public EnhancedThrowableRenderer() {
/*     */     try {
/*  50 */       Class[] noArgs = null;
/*  51 */       this.getStackTraceMethod = Throwable.class.getMethod("getStackTrace", noArgs);
/*  52 */       Class<?> ste = Class.forName("java.lang.StackTraceElement");
/*  53 */       this.getClassNameMethod = ste.getMethod("getClassName", noArgs);
/*  54 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] doRender(Throwable throwable) {
/*  62 */     if (this.getStackTraceMethod != null) {
/*     */       try {
/*  64 */         Object[] noArgs = null;
/*  65 */         Object[] elements = (Object[])this.getStackTraceMethod.invoke(throwable, noArgs);
/*  66 */         String[] lines = new String[elements.length + 1];
/*  67 */         lines[0] = throwable.toString();
/*  68 */         Map<Object, Object> classMap = new HashMap<Object, Object>();
/*  69 */         for (int i = 0; i < elements.length; i++) {
/*  70 */           lines[i + 1] = formatElement(elements[i], classMap);
/*     */         }
/*  72 */         return lines;
/*  73 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/*  76 */     return DefaultThrowableRenderer.render(throwable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String formatElement(Object element, Map<String, String> classMap) {
/*  87 */     StringBuilder buf = new StringBuilder("\tat ");
/*  88 */     buf.append(element);
/*     */     try {
/*  90 */       String className = this.getClassNameMethod.invoke(element, (Object[])null).toString();
/*  91 */       Object classDetails = classMap.get(className);
/*  92 */       if (classDetails != null) {
/*  93 */         buf.append(classDetails);
/*     */       } else {
/*  95 */         Class cls = findClass(className);
/*  96 */         int detailStart = buf.length();
/*  97 */         buf.append('[');
/*     */         try {
/*  99 */           CodeSource source = cls.getProtectionDomain().getCodeSource();
/* 100 */           if (source != null) {
/* 101 */             URL locationURL = source.getLocation();
/* 102 */             if (locationURL != null)
/*     */             {
/*     */ 
/*     */               
/* 106 */               if ("file".equals(locationURL.getProtocol())) {
/* 107 */                 String path = locationURL.getPath();
/* 108 */                 if (path != null) {
/*     */ 
/*     */ 
/*     */                   
/* 112 */                   int lastSlash = path.lastIndexOf('/');
/* 113 */                   int lastBack = path.lastIndexOf(File.separatorChar);
/* 114 */                   if (lastBack > lastSlash) {
/* 115 */                     lastSlash = lastBack;
/*     */                   }
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 121 */                   if (lastSlash <= 0 || lastSlash == path.length() - 1) {
/* 122 */                     buf.append(locationURL);
/*     */                   } else {
/* 124 */                     buf.append(path.substring(lastSlash + 1));
/*     */                   } 
/*     */                 } 
/*     */               } else {
/* 128 */                 buf.append(locationURL);
/*     */               } 
/*     */             }
/*     */           } 
/* 132 */         } catch (SecurityException securityException) {}
/*     */         
/* 134 */         buf.append(':');
/* 135 */         Package pkg = cls.getPackage();
/* 136 */         if (pkg != null) {
/* 137 */           String implVersion = pkg.getImplementationVersion();
/* 138 */           if (implVersion != null) {
/* 139 */             buf.append(implVersion);
/*     */           }
/*     */         } 
/* 142 */         buf.append(']');
/* 143 */         classMap.put(className, buf.substring(detailStart));
/*     */       } 
/* 145 */     } catch (Exception exception) {}
/*     */     
/* 147 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class findClass(String className) throws ClassNotFoundException {
/*     */     try {
/* 159 */       return Thread.currentThread().getContextClassLoader().loadClass(className);
/* 160 */     } catch (ClassNotFoundException e) {
/*     */       try {
/* 162 */         return Class.forName(className);
/* 163 */       } catch (ClassNotFoundException e1) {
/* 164 */         return getClass().getClassLoader().loadClass(className);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/EnhancedThrowableRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */