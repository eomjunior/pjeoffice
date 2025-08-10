/*     */ package org.zeroturnaround.zip.commons;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOUtils
/*     */ {
/*     */   private static final int EOF = -1;
/*     */   public static final char DIR_SEPARATOR_UNIX = '/';
/*     */   public static final char DIR_SEPARATOR_WINDOWS = '\\';
/*  84 */   public static final char DIR_SEPARATOR = File.separatorChar;
/*     */ 
/*     */   
/*     */   public static final String LINE_SEPARATOR_UNIX = "\n";
/*     */ 
/*     */   
/*     */   public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
/*     */ 
/*     */   
/*     */   public static final String LINE_SEPARATOR;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_BUFFER_SIZE = 4096;
/*     */ 
/*     */   
/*     */   static {
/* 100 */     StringBuilderWriter buf = new StringBuilderWriter(4);
/* 101 */     PrintWriter out = new PrintWriter(buf);
/* 102 */     out.println();
/* 103 */     LINE_SEPARATOR = buf.toString();
/* 104 */     out.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(InputStream input) {
/* 146 */     closeQuietly(input);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(OutputStream output) {
/* 174 */     closeQuietly(output);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Closeable closeable) {
/*     */     try {
/* 202 */       if (closeable != null) {
/* 203 */         closeable.close();
/*     */       }
/* 205 */     } catch (IOException iOException) {}
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
/*     */   public static byte[] toByteArray(InputStream input) throws IOException {
/* 224 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 225 */     copy(input, output);
/* 226 */     return output.toByteArray();
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
/*     */   public static String toString(InputStream input, String encoding) throws IOException {
/* 247 */     StringBuilderWriter sw = new StringBuilderWriter();
/* 248 */     copy(input, sw, encoding);
/* 249 */     return sw.toString();
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
/*     */   
/*     */   public static int copy(InputStream input, OutputStream output) throws IOException {
/* 274 */     long count = copyLarge(input, output);
/* 275 */     if (count > 2147483647L) {
/* 276 */       return -1;
/*     */     }
/* 278 */     return (int)count;
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
/*     */   public static long copyLarge(InputStream input, OutputStream output) throws IOException {
/* 299 */     return copyLarge(input, output, new byte[4096]);
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
/*     */   public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
/* 320 */     long count = 0L;
/* 321 */     int n = 0;
/* 322 */     while (-1 != (n = input.read(buffer))) {
/* 323 */       output.write(buffer, 0, n);
/* 324 */       count += n;
/*     */     } 
/* 326 */     return count;
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
/*     */   public static void copy(InputStream input, Writer output) throws IOException {
/* 346 */     InputStreamReader in = new InputStreamReader(input);
/* 347 */     copy(in, output);
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
/*     */   public static void copy(InputStream input, Writer output, String encoding) throws IOException {
/* 371 */     if (encoding == null) {
/* 372 */       copy(input, output);
/*     */     } else {
/* 374 */       InputStreamReader in = new InputStreamReader(input, encoding);
/* 375 */       copy(in, output);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int copy(Reader input, Writer output) throws IOException {
/* 400 */     long count = copyLarge(input, output);
/* 401 */     if (count > 2147483647L) {
/* 402 */       return -1;
/*     */     }
/* 404 */     return (int)count;
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
/*     */   public static long copyLarge(Reader input, Writer output) throws IOException {
/* 423 */     return copyLarge(input, output, new char[4096]);
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
/*     */   public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
/* 442 */     long count = 0L;
/* 443 */     int n = 0;
/* 444 */     while (-1 != (n = input.read(buffer))) {
/* 445 */       output.write(buffer, 0, n);
/* 446 */       count += n;
/*     */     } 
/* 448 */     return count;
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
/*     */   public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
/* 469 */     if (!(input1 instanceof BufferedInputStream)) {
/* 470 */       input1 = new BufferedInputStream(input1);
/*     */     }
/* 472 */     if (!(input2 instanceof BufferedInputStream)) {
/* 473 */       input2 = new BufferedInputStream(input2);
/*     */     }
/*     */     
/* 476 */     int ch = input1.read();
/* 477 */     while (-1 != ch) {
/* 478 */       int i = input2.read();
/* 479 */       if (ch != i) {
/* 480 */         return false;
/*     */       }
/* 482 */       ch = input1.read();
/*     */     } 
/*     */     
/* 485 */     int ch2 = input2.read();
/* 486 */     return (ch2 == -1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/commons/IOUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */