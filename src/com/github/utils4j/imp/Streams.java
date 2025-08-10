/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.IAcumulator;
/*     */ import com.github.utils4j.IConstants;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.codec.digest.DigestUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Streams
/*     */ {
/*  56 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Closeable c) {
/*  63 */     if (c != null)
/*     */       try {
/*  65 */         c.close();
/*  66 */       } catch (IOException e) {
/*  67 */         e.printStackTrace();
/*     */       }  
/*     */   }
/*     */   
/*     */   public static void closeQuietly(AutoCloseable c) {
/*  72 */     if (c != null)
/*     */       try {
/*  74 */         c.close();
/*  75 */       } catch (Exception e) {
/*  76 */         e.printStackTrace();
/*     */       }  
/*     */   }
/*     */   
/*     */   public static void closeQuietly(OutputStream s) {
/*  81 */     if (s != null) {
/*  82 */       flushQuietly(s);
/*  83 */       closeQuietly(s);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void flushQuietly(OutputStream s) {
/*  88 */     if (s != null) {
/*     */       try {
/*  90 */         s.flush();
/*  91 */       } catch (IOException e) {
/*  92 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void consumeQuietly(InputStream s) {
/*  98 */     if (s != null) {
/*     */       try {
/* 100 */         s.skip(Long.MAX_VALUE);
/* 101 */       } catch (IOException e) {
/* 102 */         e.printStackTrace();
/*     */       } finally {
/* 104 */         closeQuietly(s);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static CompletableFuture<String> readOutStream(InputStream is) {
/* 110 */     return readOutStream(is, IConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<String> readOutStream(InputStream is, Charset charset) {
/* 114 */     return readOutStream(is, charset, new LineAppender());
/*     */   }
/*     */   
/*     */   public static CompletableFuture<String> readOutStream(InputStream is, Charset charset, IAcumulator<String> acumulator) {
/* 118 */     return CompletableFuture.supplyAsync(() -> {
/*     */           Thread thread = Thread.currentThread();
/*     */           try {
/*     */             BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
/*     */             String inputLine;
/*     */             while (!thread.isInterrupted() && (inputLine = br.readLine()) != null) {
/*     */               acumulator.accept(inputLine);
/*     */             }
/*     */             return (String)acumulator.get();
/* 127 */           } catch (IOException e) {
/*     */             return (String)acumulator.handleFail(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static String md5(File file) throws IOException {
/* 134 */     try (InputStream fis = new FileInputStream(file)) {
/* 135 */       return DigestUtils.md5Hex(fis);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String sha1(File file) throws IOException {
/* 140 */     try (InputStream fis = new FileInputStream(file)) {
/* 141 */       return DigestUtils.sha1Hex(fis);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String sha1(byte[] input) {
/* 146 */     return DigestUtils.sha1Hex(input);
/*     */   }
/*     */   
/*     */   public static boolean isSame(Path path1, Path path2) {
/*     */     try {
/* 151 */       return Files.isSameFile(path2, path1);
/* 152 */     } catch (IOException e) {
/* 153 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isSame(String path1, String path2) {
/* 158 */     return isSame(Paths.get(path1, new String[0]), Paths.get(path2, new String[0]));
/*     */   }
/*     */   
/*     */   public static byte[] fromResourceQuietly(String name) {
/* 162 */     try (InputStream is = Streams.class.getResourceAsStream(name)) {
/* 163 */       return fromStream(is);
/* 164 */     } catch (IOException e) {
/* 165 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static byte[] fromResource(String name) throws IOException {
/* 170 */     try (InputStream is = Streams.class.getResourceAsStream(name)) {
/* 171 */       if (is == null)
/* 172 */         throw new IOException("Resource does not exists: " + name); 
/* 173 */       return fromStream(is);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static ByteArrayInputStream stringByteArray(InputStream is) throws IOException {
/* 178 */     return new ByteArrayInputStream(fromStream(is))
/*     */       {
/*     */         public final String toString() {
/* 181 */           return new String(this.buf, 0, this.count);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static byte[] fromStream(InputStream is) throws IOException {
/* 187 */     if (is == null)
/* 188 */       return EMPTY_BYTE_ARRAY; 
/* 189 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 190 */     byte[] data = new byte[1024];
/*     */     int read;
/* 192 */     while ((read = is.read(data, 0, data.length)) != -1) {
/* 193 */       buffer.write(data, 0, read);
/*     */     }
/* 195 */     return buffer.toByteArray();
/*     */   }
/*     */   
/*     */   public static void resourceLines(String name, Consumer<Stream<String>> consumer) {
/* 199 */     resourceLines(name, consumer, IConstants.UTF_8);
/*     */   }
/*     */   
/*     */   public static void resourceLines(String name, Consumer<Stream<String>> consumer, Charset charsetName) {
/* 203 */     try (BufferedReader r = new BufferedReader(new InputStreamReader(Streams.class.getResourceAsStream(name), charsetName))) {
/* 204 */       consumer.accept(r.lines());
/* 205 */     } catch (Exception e) {
/* 206 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void copy(byte[] data, OutputStream out) throws IOException {
/* 211 */     copy(data, out, 32768);
/*     */   }
/*     */   
/*     */   public static void copy(byte[] data, OutputStream out, int bufferSize) throws IOException {
/* 215 */     try (InputStream input = new ByteArrayInputStream(data)) {
/* 216 */       copy(input, out, bufferSize);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
/* 221 */     byte[] buffer = new byte[bufferSize];
/*     */     int read;
/* 223 */     while ((read = input.read(buffer)) > 0)
/* 224 */       output.write(buffer, 0, read); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Streams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */