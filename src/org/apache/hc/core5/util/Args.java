/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static void check(boolean expression, String message) {
/*  40 */     if (!expression) {
/*  41 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void check(boolean expression, String message, Object... args) {
/*  46 */     if (!expression) {
/*  47 */       throw new IllegalArgumentException(String.format(message, args));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void check(boolean expression, String message, Object arg) {
/*  52 */     if (!expression) {
/*  53 */       throw new IllegalArgumentException(String.format(message, new Object[] { arg }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long checkContentLength(EntityDetails entityDetails) {
/*  61 */     return checkRange(entityDetails.getContentLength(), -1L, 2147483647L, "HTTP entity too large to be buffered in memory)");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int checkRange(int value, int lowInclusive, int highInclusive, String message) {
/*  67 */     if (value < lowInclusive || value > highInclusive) {
/*  68 */       throw illegalArgumentException("%s: %d is out of range [%d, %d]", new Object[] { message, Integer.valueOf(value), 
/*  69 */             Integer.valueOf(lowInclusive), Integer.valueOf(highInclusive) });
/*     */     }
/*  71 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long checkRange(long value, long lowInclusive, long highInclusive, String message) {
/*  76 */     if (value < lowInclusive || value > highInclusive) {
/*  77 */       throw illegalArgumentException("%s: %d is out of range [%d, %d]", new Object[] { message, Long.valueOf(value), 
/*  78 */             Long.valueOf(lowInclusive), Long.valueOf(highInclusive) });
/*     */     }
/*  80 */     return value;
/*     */   }
/*     */   
/*     */   public static <T extends CharSequence> T containsNoBlanks(T argument, String name) {
/*  84 */     notNull(argument, name);
/*  85 */     if (isEmpty(argument)) {
/*  86 */       throw illegalArgumentExceptionNotEmpty(name);
/*     */     }
/*  88 */     if (TextUtils.containsBlanks((CharSequence)argument)) {
/*  89 */       throw new IllegalArgumentException(name + " must not contain blanks");
/*     */     }
/*  91 */     return argument;
/*     */   }
/*     */   
/*     */   private static IllegalArgumentException illegalArgumentException(String format, Object... args) {
/*  95 */     return new IllegalArgumentException(String.format(format, args));
/*     */   }
/*     */   
/*     */   private static IllegalArgumentException illegalArgumentExceptionNotEmpty(String name) {
/*  99 */     return new IllegalArgumentException(name + " must not be empty");
/*     */   }
/*     */   
/*     */   private static NullPointerException NullPointerException(String name) {
/* 103 */     return new NullPointerException(name + " must not be null");
/*     */   }
/*     */   
/*     */   public static <T extends CharSequence> T notBlank(T argument, String name) {
/* 107 */     notNull(argument, name);
/* 108 */     if (TextUtils.isBlank((CharSequence)argument)) {
/* 109 */       throw new IllegalArgumentException(name + " must not be blank");
/*     */     }
/* 111 */     return argument;
/*     */   }
/*     */   
/*     */   public static <T extends CharSequence> T notEmpty(T argument, String name) {
/* 115 */     notNull(argument, name);
/* 116 */     if (isEmpty(argument)) {
/* 117 */       throw illegalArgumentExceptionNotEmpty(name);
/*     */     }
/* 119 */     return argument;
/*     */   }
/*     */   
/*     */   public static <E, T extends Collection<E>> T notEmpty(T argument, String name) {
/* 123 */     notNull(argument, name);
/* 124 */     if (isEmpty(argument)) {
/* 125 */       throw illegalArgumentExceptionNotEmpty(name);
/*     */     }
/* 127 */     return argument;
/*     */   }
/*     */   
/*     */   public static <T> T notEmpty(T argument, String name) {
/* 131 */     notNull(argument, name);
/* 132 */     if (isEmpty(argument)) {
/* 133 */       throw illegalArgumentExceptionNotEmpty(name);
/*     */     }
/* 135 */     return argument;
/*     */   }
/*     */   
/*     */   public static int notNegative(int n, String name) {
/* 139 */     if (n < 0) {
/* 140 */       throw illegalArgumentException("%s must not be negative: %d", new Object[] { name, Integer.valueOf(n) });
/*     */     }
/* 142 */     return n;
/*     */   }
/*     */   
/*     */   public static long notNegative(long n, String name) {
/* 146 */     if (n < 0L) {
/* 147 */       throw illegalArgumentException("%s must not be negative: %d", new Object[] { name, Long.valueOf(n) });
/*     */     }
/* 149 */     return n;
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
/*     */   public static <T> T notNull(T argument, String name) {
/* 165 */     return Objects.requireNonNull(argument, name);
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
/*     */   public static boolean isEmpty(Object object) {
/* 194 */     if (object == null) {
/* 195 */       return true;
/*     */     }
/* 197 */     if (object instanceof CharSequence) {
/* 198 */       return (((CharSequence)object).length() == 0);
/*     */     }
/* 200 */     if (object.getClass().isArray()) {
/* 201 */       return (Array.getLength(object) == 0);
/*     */     }
/* 203 */     if (object instanceof Collection) {
/* 204 */       return ((Collection)object).isEmpty();
/*     */     }
/* 206 */     if (object instanceof Map) {
/* 207 */       return ((Map)object).isEmpty();
/*     */     }
/* 209 */     return false;
/*     */   }
/*     */   
/*     */   public static int positive(int n, String name) {
/* 213 */     if (n <= 0) {
/* 214 */       throw illegalArgumentException("%s must not be negative or zero: %d", new Object[] { name, Integer.valueOf(n) });
/*     */     }
/* 216 */     return n;
/*     */   }
/*     */   
/*     */   public static long positive(long n, String name) {
/* 220 */     if (n <= 0L) {
/* 221 */       throw illegalArgumentException("%s must not be negative or zero: %d", new Object[] { name, Long.valueOf(n) });
/*     */     }
/* 223 */     return n;
/*     */   }
/*     */   
/*     */   public static <T extends TimeValue> T positive(T timeValue, String name) {
/* 227 */     positive(timeValue.getDuration(), name);
/* 228 */     return timeValue;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/Args.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */