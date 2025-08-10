/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class MathPreconditions
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   static int checkPositive(String role, int x) {
/*  32 */     if (x <= 0) {
/*  33 */       throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
/*     */     }
/*  35 */     return x;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkPositive(String role, long x) {
/*  40 */     if (x <= 0L) {
/*  41 */       throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
/*     */     }
/*  43 */     return x;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static BigInteger checkPositive(String role, BigInteger x) {
/*  48 */     if (x.signum() <= 0) {
/*  49 */       throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
/*     */     }
/*  51 */     return x;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNonNegative(String role, int x) {
/*  56 */     if (x < 0) {
/*  57 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  59 */     return x;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkNonNegative(String role, long x) {
/*  64 */     if (x < 0L) {
/*  65 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  67 */     return x;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static BigInteger checkNonNegative(String role, BigInteger x) {
/*  72 */     if (x.signum() < 0) {
/*  73 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  75 */     return x;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static double checkNonNegative(String role, double x) {
/*  80 */     if (x < 0.0D) {
/*  81 */       throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
/*     */     }
/*  83 */     return x;
/*     */   }
/*     */   
/*     */   static void checkRoundingUnnecessary(boolean condition) {
/*  87 */     if (!condition) {
/*  88 */       throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
/*     */     }
/*     */   }
/*     */   
/*     */   static void checkInRangeForRoundingInputs(boolean condition, double input, RoundingMode mode) {
/*  93 */     if (!condition) {
/*  94 */       throw new ArithmeticException("rounded value is out of range for input " + input + " and rounding mode " + mode);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoOverflow(boolean condition, String methodName, int a, int b) {
/* 100 */     if (!condition) {
/* 101 */       throw new ArithmeticException("overflow: " + methodName + "(" + a + ", " + b + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   static void checkNoOverflow(boolean condition, String methodName, long a, long b) {
/* 106 */     if (!condition)
/* 107 */       throw new ArithmeticException("overflow: " + methodName + "(" + a + ", " + b + ")"); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/MathPreconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */