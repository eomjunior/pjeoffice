/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.InlineMe;
/*     */ import com.google.errorprone.annotations.InlineMeValidationDisabled;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Strings
/*     */ {
/*     */   public static String nullToEmpty(@CheckForNull String string) {
/*  47 */     return Platform.nullToEmpty(string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public static String emptyToNull(@CheckForNull String string) {
/*  58 */     return Platform.emptyToNull(string);
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
/*     */   public static boolean isNullOrEmpty(@CheckForNull String string) {
/*  73 */     return Platform.stringIsNullOrEmpty(string);
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
/*     */   public static String padStart(String string, int minLength, char padChar) {
/*  95 */     Preconditions.checkNotNull(string);
/*  96 */     if (string.length() >= minLength) {
/*  97 */       return string;
/*     */     }
/*  99 */     StringBuilder sb = new StringBuilder(minLength);
/* 100 */     for (int i = string.length(); i < minLength; i++) {
/* 101 */       sb.append(padChar);
/*     */     }
/* 103 */     sb.append(string);
/* 104 */     return sb.toString();
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
/*     */   public static String padEnd(String string, int minLength, char padChar) {
/* 126 */     Preconditions.checkNotNull(string);
/* 127 */     if (string.length() >= minLength) {
/* 128 */       return string;
/*     */     }
/* 130 */     StringBuilder sb = new StringBuilder(minLength);
/* 131 */     sb.append(string);
/* 132 */     for (int i = string.length(); i < minLength; i++) {
/* 133 */       sb.append(padChar);
/*     */     }
/* 135 */     return sb.toString();
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
/*     */   @InlineMe(replacement = "string.repeat(count)")
/*     */   @InlineMeValidationDisabled("Java 11+ API only")
/*     */   public static String repeat(String string, int count) {
/* 153 */     Preconditions.checkNotNull(string);
/*     */     
/* 155 */     if (count <= 1) {
/* 156 */       Preconditions.checkArgument((count >= 0), "invalid count: %s", count);
/* 157 */       return (count == 0) ? "" : string;
/*     */     } 
/*     */ 
/*     */     
/* 161 */     int len = string.length();
/* 162 */     long longSize = len * count;
/* 163 */     int size = (int)longSize;
/* 164 */     if (size != longSize) {
/* 165 */       throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
/*     */     }
/*     */     
/* 168 */     char[] array = new char[size];
/* 169 */     string.getChars(0, len, array, 0);
/*     */     int n;
/* 171 */     for (n = len; n < size - n; n <<= 1) {
/* 172 */       System.arraycopy(array, 0, array, n, n);
/*     */     }
/* 174 */     System.arraycopy(array, 0, array, n, size - n);
/* 175 */     return new String(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String commonPrefix(CharSequence a, CharSequence b) {
/* 186 */     Preconditions.checkNotNull(a);
/* 187 */     Preconditions.checkNotNull(b);
/*     */     
/* 189 */     int maxPrefixLength = Math.min(a.length(), b.length());
/* 190 */     int p = 0;
/* 191 */     while (p < maxPrefixLength && a.charAt(p) == b.charAt(p)) {
/* 192 */       p++;
/*     */     }
/* 194 */     if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
/* 195 */       p--;
/*     */     }
/* 197 */     return a.subSequence(0, p).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String commonSuffix(CharSequence a, CharSequence b) {
/* 208 */     Preconditions.checkNotNull(a);
/* 209 */     Preconditions.checkNotNull(b);
/*     */     
/* 211 */     int maxSuffixLength = Math.min(a.length(), b.length());
/* 212 */     int s = 0;
/* 213 */     while (s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1)) {
/* 214 */       s++;
/*     */     }
/* 216 */     if (validSurrogatePairAt(a, a.length() - s - 1) || 
/* 217 */       validSurrogatePairAt(b, b.length() - s - 1)) {
/* 218 */       s--;
/*     */     }
/* 220 */     return a.subSequence(a.length() - s, a.length()).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean validSurrogatePairAt(CharSequence string, int index) {
/* 229 */     return (index >= 0 && index <= string
/* 230 */       .length() - 2 && 
/* 231 */       Character.isHighSurrogate(string.charAt(index)) && 
/* 232 */       Character.isLowSurrogate(string.charAt(index + 1)));
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
/*     */   public static String lenientFormat(@CheckForNull String template, @CheckForNull Object... args) {
/* 270 */     template = String.valueOf(template);
/*     */     
/* 272 */     if (args == null) {
/* 273 */       args = new Object[] { "(Object[])null" };
/*     */     } else {
/* 275 */       for (int j = 0; j < args.length; j++) {
/* 276 */         args[j] = lenientToString(args[j]);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 281 */     StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
/* 282 */     int templateStart = 0;
/* 283 */     int i = 0;
/* 284 */     while (i < args.length) {
/* 285 */       int placeholderStart = template.indexOf("%s", templateStart);
/* 286 */       if (placeholderStart == -1) {
/*     */         break;
/*     */       }
/* 289 */       builder.append(template, templateStart, placeholderStart);
/* 290 */       builder.append(args[i++]);
/* 291 */       templateStart = placeholderStart + 2;
/*     */     } 
/* 293 */     builder.append(template, templateStart, template.length());
/*     */ 
/*     */     
/* 296 */     if (i < args.length) {
/* 297 */       builder.append(" [");
/* 298 */       builder.append(args[i++]);
/* 299 */       while (i < args.length) {
/* 300 */         builder.append(", ");
/* 301 */         builder.append(args[i++]);
/*     */       } 
/* 303 */       builder.append(']');
/*     */     } 
/*     */     
/* 306 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static String lenientToString(@CheckForNull Object o) {
/* 310 */     if (o == null) {
/* 311 */       return "null";
/*     */     }
/*     */     try {
/* 314 */       return o.toString();
/* 315 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 318 */       String objectToString = o.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(o));
/*     */       
/* 320 */       Logger.getLogger("com.google.common.base.Strings")
/* 321 */         .log(Level.WARNING, "Exception during lenientFormat for " + objectToString, e);
/* 322 */       return "<" + objectToString + " threw " + e.getClass().getName() + ">";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */