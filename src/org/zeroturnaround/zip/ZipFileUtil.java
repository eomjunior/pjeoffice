/*     */ package org.zeroturnaround.zip;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.zip.ZipFile;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ZipFileUtil
/*     */ {
/*     */   private static final String MISSING_METHOD_PLEASE_UPGRADE = "Your JRE doesn't support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).";
/*     */   private static final String CONSTRUCTOR_MESSAGE_FOR_ZIPFILE = "Using constructor ZipFile(File, Charset) has failed: ";
/*     */   private static final String CONSTRUCTOR_MESSAGE_FOR_OUTPUT = "Using constructor ZipOutputStream(OutputStream, Charset) has failed: ";
/*     */   private static final String CONSTRUCTOR_MESSAGE_FOR_INPUT = "Using constructor ZipInputStream(InputStream, Charset) has failed: ";
/*     */   
/*     */   static ZipInputStream createZipInputStream(InputStream inStream, Charset charset) {
/*  34 */     if (charset == null) {
/*  35 */       return new ZipInputStream(inStream);
/*     */     }
/*     */     try {
/*  38 */       Constructor<ZipInputStream> constructor = ZipInputStream.class.getConstructor(new Class[] { InputStream.class, Charset.class });
/*  39 */       return constructor.newInstance(new Object[] { inStream, charset });
/*     */     }
/*  41 */     catch (NoSuchMethodException e) {
/*  42 */       throw new IllegalStateException("Your JRE doesn't support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).", e);
/*     */     }
/*  44 */     catch (InstantiationException e) {
/*  45 */       throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/*  47 */     catch (IllegalAccessException e) {
/*  48 */       throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/*  50 */     catch (IllegalArgumentException e) {
/*  51 */       throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/*  53 */     catch (InvocationTargetException e) {
/*  54 */       throw new IllegalStateException("Using constructor ZipInputStream(InputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZipOutputStream createZipOutputStream(BufferedOutputStream outStream, Charset charset) {
/*  63 */     if (charset == null) {
/*  64 */       return new ZipOutputStream(outStream);
/*     */     }
/*     */     try {
/*  67 */       Constructor<ZipOutputStream> constructor = ZipOutputStream.class.getConstructor(new Class[] { OutputStream.class, Charset.class });
/*  68 */       return constructor.newInstance(new Object[] { outStream, charset });
/*     */     }
/*  70 */     catch (NoSuchMethodException e) {
/*  71 */       throw new IllegalStateException("Your JRE doesn't support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).", e);
/*     */     }
/*  73 */     catch (InstantiationException e) {
/*  74 */       throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/*  76 */     catch (IllegalAccessException e) {
/*  77 */       throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/*  79 */     catch (IllegalArgumentException e) {
/*  80 */       throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/*  82 */     catch (InvocationTargetException e) {
/*  83 */       throw new IllegalStateException("Using constructor ZipOutputStream(OutputStream, Charset) has failed: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZipFile getZipFile(File src, Charset charset) throws IOException {
/*  91 */     if (charset == null) {
/*  92 */       return new ZipFile(src);
/*     */     }
/*     */     
/*     */     try {
/*  96 */       Constructor<ZipFile> constructor = ZipFile.class.getConstructor(new Class[] { File.class, Charset.class });
/*  97 */       return constructor.newInstance(new Object[] { src, charset });
/*     */     }
/*  99 */     catch (NoSuchMethodException e) {
/* 100 */       throw new IllegalStateException("Your JRE doesn't support the ZipFile Charset constructor. Please upgrade JRE to 1.7 use this feature. Tried constructor ZipFile(File, Charset).", e);
/*     */     }
/* 102 */     catch (InstantiationException e) {
/* 103 */       throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/* 105 */     catch (IllegalAccessException e) {
/* 106 */       throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/* 108 */     catch (IllegalArgumentException e) {
/* 109 */       throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + e.getMessage(), e);
/*     */     }
/* 111 */     catch (InvocationTargetException e) {
/* 112 */       throw new IllegalStateException("Using constructor ZipFile(File, Charset) has failed: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isCharsetSupported() throws IOException {
/*     */     try {
/* 121 */       ZipFile.class.getConstructor(new Class[] { File.class, Charset.class });
/* 122 */       return true;
/*     */     }
/* 124 */     catch (NoSuchMethodException e) {
/* 125 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZipFileUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */