/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public final class ClassConstants
/*     */   extends BaseFilterReader
/*     */   implements ChainableReader
/*     */ {
/*  51 */   private String queuedData = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String JAVA_CLASS_HELPER = "org.apache.tools.ant.filters.util.JavaClassHelper";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassConstants() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassConstants(Reader in) {
/*  75 */     super(in);
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
/*     */   public int read() throws IOException {
/*  89 */     int ch = -1;
/*     */     
/*  91 */     if (this.queuedData != null && this.queuedData.isEmpty()) {
/*  92 */       this.queuedData = null;
/*     */     }
/*     */     
/*  95 */     if (this.queuedData == null) {
/*  96 */       String clazz = readFully();
/*  97 */       if (clazz == null || clazz.isEmpty()) {
/*  98 */         ch = -1;
/*     */       } else {
/* 100 */         byte[] bytes = clazz.getBytes(StandardCharsets.ISO_8859_1);
/*     */         try {
/* 102 */           Class<?> javaClassHelper = Class.forName("org.apache.tools.ant.filters.util.JavaClassHelper");
/* 103 */           if (javaClassHelper != null) {
/*     */             
/* 105 */             Method getConstants = javaClassHelper.getMethod("getConstants", new Class[] { byte[].class });
/*     */ 
/*     */ 
/*     */             
/* 109 */             StringBuffer sb = (StringBuffer)getConstants.invoke(null, new Object[] { bytes });
/* 110 */             if (sb.length() > 0) {
/* 111 */               this.queuedData = sb.toString();
/* 112 */               return read();
/*     */             } 
/*     */           } 
/* 115 */         } catch (NoClassDefFoundError|RuntimeException ex) {
/* 116 */           throw ex;
/* 117 */         } catch (InvocationTargetException ex) {
/* 118 */           Throwable t = ex.getTargetException();
/* 119 */           if (t instanceof NoClassDefFoundError) {
/* 120 */             throw (NoClassDefFoundError)t;
/*     */           }
/* 122 */           if (t instanceof RuntimeException) {
/* 123 */             throw (RuntimeException)t;
/*     */           }
/* 125 */           throw new BuildException(t);
/* 126 */         } catch (Exception ex) {
/* 127 */           throw new BuildException(ex);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 131 */       ch = this.queuedData.charAt(0);
/* 132 */       this.queuedData = this.queuedData.substring(1);
/* 133 */       if (this.queuedData.isEmpty()) {
/* 134 */         this.queuedData = null;
/*     */       }
/*     */     } 
/* 137 */     return ch;
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
/*     */   public Reader chain(Reader rdr) {
/* 151 */     return new ClassConstants(rdr);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/ClassConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */