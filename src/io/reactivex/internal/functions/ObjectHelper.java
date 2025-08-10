/*     */ package io.reactivex.internal.functions;
/*     */ 
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectHelper
/*     */ {
/*     */   private ObjectHelper() {
/*  25 */     throw new IllegalStateException("No instances!");
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
/*     */   public static <T> T requireNonNull(T object, String message) {
/*  38 */     if (object == null) {
/*  39 */       throw new NullPointerException(message);
/*     */     }
/*  41 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Object o1, Object o2) {
/*  51 */     return (o1 == o2 || (o1 != null && o1.equals(o2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashCode(Object o) {
/*  60 */     return (o != null) ? o.hashCode() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int compare(int v1, int v2) {
/*  70 */     return (v1 < v2) ? -1 : ((v1 > v2) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int compare(long v1, long v2) {
/*  80 */     return (v1 < v2) ? -1 : ((v1 > v2) ? 1 : 0);
/*     */   }
/*     */   
/*  83 */   static final BiPredicate<Object, Object> EQUALS = new BiObjectPredicate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BiPredicate<T, T> equalsPredicate() {
/*  92 */     return (BiPredicate)EQUALS;
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
/*     */   public static int verifyPositive(int value, String paramName) {
/* 104 */     if (value <= 0) {
/* 105 */       throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
/*     */     }
/* 107 */     return value;
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
/*     */   public static long verifyPositive(long value, String paramName) {
/* 119 */     if (value <= 0L) {
/* 120 */       throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
/*     */     }
/* 122 */     return value;
/*     */   }
/*     */   
/*     */   static final class BiObjectPredicate
/*     */     implements BiPredicate<Object, Object> {
/*     */     public boolean test(Object o1, Object o2) {
/* 128 */       return ObjectHelper.equals(o1, o2);
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
/*     */   @Deprecated
/*     */   public static long requireNonNull(long value, String message) {
/* 142 */     throw new InternalError("Null check on a primitive: " + message);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/functions/ObjectHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */