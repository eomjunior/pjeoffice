/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.pdf.ByteBuffer;
/*     */ import com.itextpdf.text.pdf.PRTokeniser;
/*     */ import com.itextpdf.text.pdf.PdfEncodings;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utilities
/*     */ {
/*     */   @Deprecated
/*     */   public static <K, V> Set<K> getKeySet(Hashtable<K, V> table) {
/*  79 */     return (table == null) ? Collections.<K>emptySet() : table.keySet();
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
/*     */   public static Object[][] addToArray(Object[][] original, Object[] item) {
/*  92 */     if (original == null) {
/*  93 */       original = new Object[1][];
/*  94 */       original[0] = item;
/*  95 */       return original;
/*     */     } 
/*  97 */     Object[][] original2 = new Object[original.length + 1][];
/*  98 */     System.arraycopy(original, 0, original2, 0, original.length);
/*  99 */     original2[original.length] = item;
/* 100 */     return original2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkTrueOrFalse(Properties attributes, String key) {
/* 111 */     return "true".equalsIgnoreCase(attributes.getProperty(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String unEscapeURL(String src) {
/* 120 */     StringBuffer bf = new StringBuffer();
/* 121 */     char[] s = src.toCharArray();
/* 122 */     for (int k = 0; k < s.length; k++) {
/* 123 */       char c = s[k];
/* 124 */       if (c == '%')
/* 125 */       { if (k + 2 >= s.length) {
/* 126 */           bf.append(c);
/*     */         } else {
/*     */           
/* 129 */           int a0 = PRTokeniser.getHex(s[k + 1]);
/* 130 */           int a1 = PRTokeniser.getHex(s[k + 2]);
/* 131 */           if (a0 < 0 || a1 < 0) {
/* 132 */             bf.append(c);
/*     */           } else {
/*     */             
/* 135 */             bf.append((char)(a0 * 16 + a1));
/* 136 */             k += 2;
/*     */           } 
/*     */         }  }
/* 139 */       else { bf.append(c); }
/*     */     
/* 141 */     }  return bf.toString();
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
/*     */   public static URL toURL(String filename) throws MalformedURLException {
/*     */     try {
/* 157 */       return new URL(filename);
/*     */     }
/* 159 */     catch (Exception e) {
/* 160 */       return (new File(filename)).toURI().toURL();
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
/*     */   public static void skip(InputStream is, int size) throws IOException {
/* 177 */     while (size > 0) {
/* 178 */       long n = is.skip(size);
/* 179 */       if (n <= 0L)
/*     */         break; 
/* 181 */       size = (int)(size - n);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float millimetersToPoints(float value) {
/* 192 */     return inchesToPoints(millimetersToInches(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float millimetersToInches(float value) {
/* 202 */     return value / 25.4F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float pointsToMillimeters(float value) {
/* 212 */     return inchesToMillimeters(pointsToInches(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float pointsToInches(float value) {
/* 222 */     return value / 72.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float inchesToMillimeters(float value) {
/* 232 */     return value * 25.4F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float inchesToPoints(float value) {
/* 242 */     return value * 72.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSurrogateHigh(char c) {
/* 253 */     return (c >= '?' && c <= '?');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSurrogateLow(char c) {
/* 264 */     return (c >= '?' && c <= '?');
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
/*     */   public static boolean isSurrogatePair(String text, int idx) {
/* 277 */     if (idx < 0 || idx > text.length() - 2)
/* 278 */       return false; 
/* 279 */     return (isSurrogateHigh(text.charAt(idx)) && isSurrogateLow(text.charAt(idx + 1)));
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
/*     */   public static boolean isSurrogatePair(char[] text, int idx) {
/* 292 */     if (idx < 0 || idx > text.length - 2)
/* 293 */       return false; 
/* 294 */     return (isSurrogateHigh(text[idx]) && isSurrogateLow(text[idx + 1]));
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
/*     */   public static int convertToUtf32(char highSurrogate, char lowSurrogate) {
/* 306 */     return (highSurrogate - 55296) * 1024 + lowSurrogate - 56320 + 65536;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int convertToUtf32(char[] text, int idx) {
/* 317 */     return (text[idx] - 55296) * 1024 + text[idx + 1] - 56320 + 65536;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int convertToUtf32(String text, int idx) {
/* 328 */     return (text.charAt(idx) - 55296) * 1024 + text.charAt(idx + 1) - 56320 + 65536;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertFromUtf32(int codePoint) {
/* 338 */     if (codePoint < 65536)
/* 339 */       return Character.toString((char)codePoint); 
/* 340 */     codePoint -= 65536;
/* 341 */     return new String(new char[] { (char)(codePoint / 1024 + 55296), (char)(codePoint % 1024 + 56320) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String readFileToString(String path) throws IOException {
/* 352 */     return readFileToString(new File(path));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String readFileToString(File file) throws IOException {
/* 363 */     byte[] jsBytes = new byte[(int)file.length()];
/* 364 */     FileInputStream f = new FileInputStream(file);
/* 365 */     f.read(jsBytes);
/* 366 */     return new String(jsBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertToHex(byte[] bytes) {
/* 375 */     ByteBuffer buf = new ByteBuffer();
/* 376 */     for (byte b : bytes) {
/* 377 */       buf.appendHex(b);
/*     */     }
/* 379 */     return PdfEncodings.convertToString(buf.toByteArray(), null).toUpperCase();
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
/*     */   public static char[] copyOfRange(char[] original, int from, int to) {
/* 409 */     int newLength = to - from;
/* 410 */     if (newLength < 0)
/* 411 */       throw new IllegalArgumentException(from + " > " + to); 
/* 412 */     char[] copy = new char[newLength];
/* 413 */     System.arraycopy(original, from, copy, 0, 
/* 414 */         Math.min(original.length - from, newLength));
/* 415 */     return copy;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Utilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */