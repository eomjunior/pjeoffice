/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TextUtils
/*     */ {
/*     */   public static boolean isEmpty(CharSequence s) {
/*  45 */     return (length(s) == 0);
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
/*     */   public static boolean isBlank(CharSequence s) {
/*  65 */     int strLen = length(s);
/*  66 */     if (strLen == 0) {
/*  67 */       return true;
/*     */     }
/*  69 */     for (int i = 0; i < strLen; i++) {
/*  70 */       if (!Character.isWhitespace(s.charAt(i))) {
/*  71 */         return false;
/*     */       }
/*     */     } 
/*  74 */     return true;
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
/*     */   public static int length(CharSequence cs) {
/*  88 */     return (cs == null) ? 0 : cs.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsBlanks(CharSequence s) {
/*  95 */     int strLen = length(s);
/*  96 */     if (strLen == 0) {
/*  97 */       return false;
/*     */     }
/*  99 */     for (int i = 0; i < s.length(); i++) {
/* 100 */       if (Character.isWhitespace(s.charAt(i))) {
/* 101 */         return true;
/*     */       }
/*     */     } 
/* 104 */     return false;
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
/*     */   public static String toHexString(byte[] bytes) {
/* 117 */     if (bytes == null) {
/* 118 */       return null;
/*     */     }
/* 120 */     StringBuilder buffer = new StringBuilder();
/* 121 */     for (int i = 0; i < bytes.length; i++) {
/* 122 */       int unsignedB = bytes[i] & 0xFF;
/* 123 */       if (unsignedB < 16) {
/* 124 */         buffer.append('0');
/*     */       }
/* 126 */       buffer.append(Integer.toHexString(unsignedB));
/*     */     } 
/* 128 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toLowerCase(String s) {
/* 138 */     if (s == null) {
/* 139 */       return null;
/*     */     }
/* 141 */     return s.toLowerCase(Locale.ROOT);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/TextUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */