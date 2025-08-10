/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Args
/*     */ {
/*     */   public static <T> T requireNonNull(T o, String message) {
/*  41 */     if (o == null)
/*  42 */       throw new IllegalArgumentException(message); 
/*  43 */     return o;
/*     */   }
/*     */   
/*     */   public static int requirePositive(int value, String message) {
/*  47 */     if (value <= 0)
/*  48 */       throw new IllegalArgumentException(message); 
/*  49 */     return value;
/*     */   }
/*     */   
/*     */   public static long requirePositive(long value, String message) {
/*  53 */     if (value <= 0L)
/*  54 */       throw new IllegalArgumentException(message); 
/*  55 */     return value;
/*     */   }
/*     */   
/*     */   public static float requirePositive(float value, String message) {
/*  59 */     if (value <= 0.0F)
/*  60 */       throw new IllegalArgumentException(message); 
/*  61 */     return value;
/*     */   }
/*     */   
/*     */   public static double requirePositive(double value, String message) {
/*  65 */     if (value <= 0.0D)
/*  66 */       throw new IllegalArgumentException(message); 
/*  67 */     return value;
/*     */   }
/*     */   
/*     */   public static long requireZeroPositive(long value, String message) {
/*  71 */     if (value < 0L)
/*  72 */       throw new IllegalArgumentException(message); 
/*  73 */     return value;
/*     */   }
/*     */   
/*     */   public static String requireText(Object value, String message) {
/*     */     String text;
/*  78 */     if (value == null || !Strings.hasText(text = value.toString()))
/*  79 */       throw new IllegalArgumentException(message); 
/*  80 */     return text;
/*     */   }
/*     */   
/*     */   public static String requireText(String value, String message) {
/*  84 */     if (!Strings.hasText(value))
/*  85 */       throw new IllegalArgumentException(message); 
/*  86 */     return value;
/*     */   }
/*     */   
/*     */   public static void requireText(String[] roots, String message) {
/*  90 */     requireNonNull(roots, message);
/*  91 */     requirePositive(roots.length, message);
/*  92 */     for (String root : roots)
/*  93 */       requireText(root, message); 
/*     */   }
/*     */   
/*     */   public static int requireZeroPositive(int value, String message) {
/*  97 */     if (value < 0)
/*  98 */       throw new IllegalArgumentException(message); 
/*  99 */     return value;
/*     */   }
/*     */   
/*     */   public static int requireNegative(int value, String message) {
/* 103 */     if (value >= 0)
/* 104 */       throw new IllegalArgumentException(message); 
/* 105 */     return value;
/*     */   }
/*     */   
/*     */   public static int requireZeroNegative(int value, String message) {
/* 109 */     if (value > 0)
/* 110 */       throw new IllegalArgumentException(message); 
/* 111 */     return value;
/*     */   }
/*     */   
/*     */   public static boolean requireTrue(boolean value, String message) {
/* 115 */     if (!value)
/* 116 */       throw new IllegalArgumentException(message); 
/* 117 */     return value;
/*     */   }
/*     */   
/*     */   public static Path requireExists(Path path, String message) {
/* 121 */     return requireExists(path, message, LinkOption.NOFOLLOW_LINKS);
/*     */   }
/*     */   
/*     */   public static Path requireFolderExists(Path path, String message) {
/* 125 */     if (!requireExists(path, message, LinkOption.NOFOLLOW_LINKS).toFile().isDirectory())
/* 126 */       throw new IllegalArgumentException(message); 
/* 127 */     return path;
/*     */   }
/*     */   
/*     */   public static Path requireExists(Path path, String message, LinkOption options) {
/* 131 */     if (!Files.exists(requireNonNull(path, message), new LinkOption[] { options }))
/* 132 */       throw new IllegalArgumentException(message); 
/* 133 */     return path;
/*     */   }
/*     */   
/*     */   public static <T extends Collection<?>> T requireEmpty(T collection, String message) {
/* 137 */     if (!Containers.isEmpty((Collection<?>)collection))
/* 138 */       throw new IllegalArgumentException(message); 
/* 139 */     return collection;
/*     */   }
/*     */   
/*     */   public static <T extends Collection<?>> T requireNonEmpty(T collection, String message) {
/* 143 */     if (Containers.isEmpty((Collection<?>)collection))
/* 144 */       throw new IllegalArgumentException(message); 
/* 145 */     return collection;
/*     */   }
/*     */   
/*     */   public static <T> T[] requireNonEmpty(T[] collection, String message) {
/* 149 */     if (Containers.isEmpty(collection))
/* 150 */       throw new IllegalArgumentException(message); 
/* 151 */     return collection;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] requireNonEmpty(byte[] collection, String message) {
/* 156 */     if (Containers.isEmpty(collection))
/* 157 */       throw new IllegalArgumentException(message); 
/* 158 */     return collection;
/*     */   }
/*     */   
/*     */   public static Certificate[] requireNonEmpty(Certificate[] collection, String message) {
/* 162 */     if (Containers.isEmpty(collection))
/* 163 */       throw new IllegalArgumentException(message); 
/* 164 */     return collection;
/*     */   }
/*     */   
/*     */   public static long requireLong(String value, String message) {
/* 168 */     if (!Strings.isLong(value))
/* 169 */       throw new IllegalArgumentException(message); 
/* 170 */     return Long.valueOf(value).longValue();
/*     */   }
/*     */   
/*     */   public static int requireInt(String value, String message) {
/* 174 */     if (!Strings.isInt(value))
/* 175 */       throw new IllegalArgumentException(message); 
/* 176 */     return Integer.valueOf(value).intValue();
/*     */   }
/*     */   
/*     */   public static float requireFloat(String value, String message) {
/* 180 */     if (!Strings.isFloat(value))
/* 181 */       throw new IllegalArgumentException(message); 
/* 182 */     return Float.valueOf(value).floatValue();
/*     */   }
/*     */   
/*     */   public static double requireDouble(String value, String message) {
/* 186 */     if (!Strings.isDouble(value))
/* 187 */       throw new IllegalArgumentException(message); 
/* 188 */     return Double.valueOf(value).doubleValue();
/*     */   }
/*     */   
/*     */   public static boolean requireBoolean(String value, String message) {
/* 192 */     if (!Strings.isBoolean(value))
/* 193 */       throw new IllegalArgumentException(message); 
/* 194 */     return Boolean.valueOf(value).booleanValue();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Args.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */