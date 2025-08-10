/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public final class StreamUtil
/*     */ {
/*     */   public static byte[] inputStreamToArray(InputStream is) throws IOException {
/*  67 */     byte[] b = new byte[8192];
/*  68 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     while (true) {
/*  70 */       int read = is.read(b);
/*  71 */       if (read < 1)
/*     */         break; 
/*  73 */       out.write(b, 0, read);
/*     */     } 
/*  75 */     out.close();
/*  76 */     return out.toByteArray();
/*     */   }
/*     */   
/*     */   public static void CopyBytes(RandomAccessSource source, long start, long length, OutputStream outs) throws IOException {
/*  80 */     if (length <= 0L)
/*     */       return; 
/*  82 */     long idx = start;
/*  83 */     byte[] buf = new byte[8192];
/*  84 */     while (length > 0L) {
/*  85 */       long n = source.get(idx, buf, 0, (int)Math.min(buf.length, length));
/*  86 */       if (n <= 0L)
/*  87 */         throw new EOFException(); 
/*  88 */       outs.write(buf, 0, (int)n);
/*  89 */       idx += n;
/*  90 */       length -= n;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream getResourceStream(String key) {
/* 101 */     return getResourceStream(key, null);
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
/*     */   public static InputStream getResourceStream(String key, ClassLoader loader) {
/* 113 */     if (key.startsWith("/"))
/* 114 */       key = key.substring(1); 
/* 115 */     InputStream is = null;
/* 116 */     if (loader != null) {
/* 117 */       is = loader.getResourceAsStream(key);
/* 118 */       if (is != null) {
/* 119 */         return is;
/*     */       }
/*     */     } 
/*     */     try {
/* 123 */       ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/* 124 */       if (contextClassLoader != null) {
/* 125 */         is = contextClassLoader.getResourceAsStream(key);
/*     */       }
/* 127 */     } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (is == null) {
/* 132 */       is = StreamUtil.class.getResourceAsStream("/" + key);
/*     */     }
/* 134 */     if (is == null) {
/* 135 */       is = ClassLoader.getSystemResourceAsStream(key);
/*     */     }
/* 137 */     return is;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/StreamUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */