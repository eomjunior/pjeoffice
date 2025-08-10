/*     */ package org.checkerframework.checker.formatter.qual;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringJoiner;
/*     */ import org.checkerframework.dataflow.qual.Pure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ConversionCategory
/*     */ {
/*  40 */   GENERAL("bBhHsS", (Class[])null),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   CHAR("cC", new Class[] { Character.class, Byte.class, Short.class, Integer.class
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  53 */   INT("doxX", new Class[] { Byte.class, Short.class, Integer.class, Long.class, BigInteger.class
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  59 */   FLOAT("eEfgGaA", new Class[] { Float.class, Double.class, BigDecimal.class
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  65 */   TIME("tT", new Class[] { Long.class, Calendar.class, Date.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  88 */   CHAR_AND_INT(null, new Class[] { Byte.class, Short.class, Integer.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/*  95 */   INT_AND_TIME(null, new Class[] { Long.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 107 */   NULL(null, new Class[0]),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   UNUSED(null, (Class[])null);
/*     */ 
/*     */   
/*     */   public final Class<?>[] types;
/*     */ 
/*     */   
/*     */   public final String chars;
/*     */   
/*     */   private static final ConversionCategory[] conversionCategoriesWithChar;
/*     */   
/*     */   private static final ConversionCategory[] conversionCategoriesForIntersect;
/*     */   
/*     */   private static final ConversionCategory[] conversionCategoriesForUnion;
/*     */ 
/*     */   
/*     */   ConversionCategory(String chars, Class<?>... types) {
/* 135 */     this.chars = chars;
/* 136 */     if (types == null) {
/* 137 */       this.types = types;
/*     */     } else {
/* 139 */       List<Class<?>> typesWithPrimitives = new ArrayList<>(types.length);
/* 140 */       for (Class<?> type : types) {
/* 141 */         typesWithPrimitives.add(type);
/* 142 */         Class<?> unwrapped = unwrapPrimitive(type);
/* 143 */         if (unwrapped != null) {
/* 144 */           typesWithPrimitives.add(unwrapped);
/*     */         }
/*     */       } 
/* 147 */       this.types = (Class[])typesWithPrimitives.<Class<?>[]>toArray((Class<?>[][])new Class[typesWithPrimitives.size()]);
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
/*     */   private static Class<? extends Object> unwrapPrimitive(Class<?> c) {
/* 159 */     if (c == Byte.class) {
/* 160 */       return (Class)byte.class;
/*     */     }
/* 162 */     if (c == Character.class) {
/* 163 */       return (Class)char.class;
/*     */     }
/* 165 */     if (c == Short.class) {
/* 166 */       return (Class)short.class;
/*     */     }
/* 168 */     if (c == Integer.class) {
/* 169 */       return (Class)int.class;
/*     */     }
/* 171 */     if (c == Long.class) {
/* 172 */       return (Class)long.class;
/*     */     }
/* 174 */     if (c == Float.class) {
/* 175 */       return (Class)float.class;
/*     */     }
/* 177 */     if (c == Double.class) {
/* 178 */       return (Class)double.class;
/*     */     }
/* 180 */     if (c == Boolean.class) {
/* 181 */       return (Class)boolean.class;
/*     */     }
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 190 */     conversionCategoriesWithChar = new ConversionCategory[] { GENERAL, CHAR, INT, FLOAT, TIME };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     conversionCategoriesForIntersect = new ConversionCategory[] { CHAR, INT, FLOAT, TIME, CHAR_AND_INT, INT_AND_TIME, NULL };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     conversionCategoriesForUnion = new ConversionCategory[] { NULL, CHAR_AND_INT, INT_AND_TIME, CHAR, INT, FLOAT, TIME };
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConversionCategory fromConversionChar(char c) {
/*     */     for (ConversionCategory v : conversionCategoriesWithChar) {
/*     */       if (v.chars.contains(String.valueOf(c))) {
/*     */         return v;
/*     */       }
/*     */     } 
/*     */     throw new IllegalArgumentException("Bad conversion character " + c);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> Set<E> arrayToSet(E[] a) {
/*     */     return new HashSet<>(Arrays.asList(a));
/*     */   }
/*     */   
/*     */   public static ConversionCategory union(ConversionCategory a, ConversionCategory b) {
/* 292 */     if (a == UNUSED || b == UNUSED) {
/* 293 */       return UNUSED;
/*     */     }
/* 295 */     if (a == GENERAL || b == GENERAL) {
/* 296 */       return GENERAL;
/*     */     }
/* 298 */     if ((a == CHAR_AND_INT && b == INT_AND_TIME) || (a == INT_AND_TIME && b == CHAR_AND_INT))
/*     */     {
/*     */ 
/*     */       
/* 302 */       return INT;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 307 */     Set<Class<?>> as = arrayToSet(a.types);
/*     */ 
/*     */     
/* 310 */     Set<Class<?>> bs = arrayToSet(b.types);
/* 311 */     as.addAll(bs);
/* 312 */     for (ConversionCategory v : conversionCategoriesForUnion) {
/*     */ 
/*     */       
/* 315 */       Set<Class<?>> vs = arrayToSet(v.types);
/* 316 */       if (vs.equals(as)) {
/* 317 */         return v;
/*     */       }
/*     */     } 
/*     */     
/* 321 */     return GENERAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isSubsetOf(ConversionCategory a, ConversionCategory b) {
/*     */     return (intersect(a, b) == a);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAssignableFrom(Class<?> argType) {
/* 331 */     if (this.types == null) {
/* 332 */       return true;
/*     */     }
/* 334 */     if (argType == void.class) {
/* 335 */       return true;
/*     */     }
/* 337 */     for (Class<?> c : this.types) {
/* 338 */       if (c.isAssignableFrom(argType)) {
/* 339 */         return true;
/*     */       }
/*     */     } 
/* 342 */     return false;
/*     */   } public static ConversionCategory intersect(ConversionCategory a, ConversionCategory b) { if (a == UNUSED)
/*     */       return b;  if (b == UNUSED)
/*     */       return a;  if (a == GENERAL)
/*     */       return b;  if (b == GENERAL)
/*     */       return a;  Set<Class<?>> as = arrayToSet(a.types); Set<Class<?>> bs = arrayToSet(b.types); as.retainAll(bs); for (ConversionCategory v : conversionCategoriesForIntersect) { Set<Class<?>> vs = arrayToSet(v.types); if (vs.equals(as))
/*     */         return v;  }
/* 349 */      throw new RuntimeException(); } @Pure public String toString() { StringBuilder sb = new StringBuilder();
/* 350 */     sb.append(name());
/* 351 */     sb.append(" conversion category");
/*     */     
/* 353 */     if (this.types == null || this.types.length == 0) {
/* 354 */       return sb.toString();
/*     */     }
/*     */     
/* 357 */     StringJoiner sj = new StringJoiner(", ", "(one of: ", ")");
/* 358 */     for (Class<?> cls : this.types) {
/* 359 */       sj.add(cls.getSimpleName());
/*     */     }
/* 361 */     sb.append(" ");
/* 362 */     sb.append(sj);
/*     */     
/* 364 */     return sb.toString(); }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/checker/formatter/qual/ConversionCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */