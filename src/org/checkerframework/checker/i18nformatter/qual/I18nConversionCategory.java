/*     */ package org.checkerframework.checker.i18nformatter.qual;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringJoiner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum I18nConversionCategory
/*     */ {
/*  36 */   UNUSED(null, null),
/*     */ 
/*     */   
/*  39 */   GENERAL(null, null),
/*     */ 
/*     */   
/*  42 */   DATE(new Class[] { Date.class, Number.class }, new String[] { "date", "time"
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
/*  53 */   NUMBER(new Class[] { Number.class }, new String[] { "number", "choice" });
/*     */   
/*     */   public final Class<?>[] types;
/*     */   
/*     */   public final String[] strings;
/*     */   private static final I18nConversionCategory[] namedCategories;
/*     */   private static final I18nConversionCategory[] conversionCategoriesForIntersect;
/*     */   
/*     */   I18nConversionCategory(Class<?>[] types, String[] strings) {
/*  62 */     this.types = types;
/*  63 */     this.strings = strings;
/*     */   }
/*     */   
/*     */   static {
/*  67 */     namedCategories = new I18nConversionCategory[] { DATE, NUMBER };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     conversionCategoriesForIntersect = new I18nConversionCategory[] { DATE, NUMBER };
/*     */   }
/*     */   
/*     */   public static I18nConversionCategory stringToI18nConversionCategory(String string) {
/*     */     string = string.toLowerCase();
/*     */     for (I18nConversionCategory v : namedCategories) {
/*     */       for (String s : v.strings) {
/*     */         if (s.equals(string)) {
/*     */           return v;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     throw new IllegalArgumentException("Invalid format type " + string);
/*     */   }
/*     */   
/*     */   public static I18nConversionCategory intersect(I18nConversionCategory a, I18nConversionCategory b) {
/* 123 */     if (a == UNUSED) {
/* 124 */       return b;
/*     */     }
/* 126 */     if (b == UNUSED) {
/* 127 */       return a;
/*     */     }
/* 129 */     if (a == GENERAL) {
/* 130 */       return b;
/*     */     }
/* 132 */     if (b == GENERAL) {
/* 133 */       return a;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 138 */     Set<Class<?>> as = arrayToSet(a.types);
/*     */ 
/*     */     
/* 141 */     Set<Class<?>> bs = arrayToSet(b.types);
/* 142 */     as.retainAll(bs);
/* 143 */     for (I18nConversionCategory v : conversionCategoriesForIntersect) {
/*     */       
/* 145 */       Set<Class<?>> vs = arrayToSet(v.types);
/* 146 */       if (vs.equals(as)) {
/* 147 */         return v;
/*     */       }
/*     */     } 
/* 150 */     throw new RuntimeException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> Set<E> arrayToSet(E[] a) {
/*     */     return new HashSet<>(Arrays.asList(a));
/*     */   }
/*     */ 
/*     */   
/*     */   public static I18nConversionCategory union(I18nConversionCategory a, I18nConversionCategory b) {
/* 161 */     if (a == UNUSED || b == UNUSED) {
/* 162 */       return UNUSED;
/*     */     }
/* 164 */     if (a == GENERAL || b == GENERAL) {
/* 165 */       return GENERAL;
/*     */     }
/* 167 */     if (a == DATE || b == DATE) {
/* 168 */       return DATE;
/*     */     }
/* 170 */     return NUMBER;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isSubsetOf(I18nConversionCategory a, I18nConversionCategory b) {
/*     */     return (intersect(a, b) == a);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAssignableFrom(Class<?> argType) {
/* 180 */     if (this.types == null) {
/* 181 */       return true;
/*     */     }
/* 183 */     if (argType == void.class) {
/* 184 */       return true;
/*     */     }
/* 186 */     for (Class<?> c : this.types) {
/* 187 */       if (c.isAssignableFrom(argType)) {
/* 188 */         return true;
/*     */       }
/*     */     } 
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 197 */     StringBuilder sb = new StringBuilder(name());
/* 198 */     if (this.types == null) {
/* 199 */       sb.append(" conversion category (all types)");
/*     */     } else {
/* 201 */       StringJoiner sj = new StringJoiner(", ", " conversion category (one of: ", ")");
/* 202 */       for (Class<?> cls : this.types) {
/* 203 */         sj.add(cls.getCanonicalName());
/*     */       }
/* 205 */       sb.append(sj);
/*     */     } 
/* 207 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/checker/i18nformatter/qual/I18nConversionCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */