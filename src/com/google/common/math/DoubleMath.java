/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class DoubleMath
/*     */ {
/*     */   private static final double MIN_INT_AS_DOUBLE = -2.147483648E9D;
/*     */   private static final double MAX_INT_AS_DOUBLE = 2.147483647E9D;
/*     */   private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18D;
/*     */   private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18D;
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   static double roundIntermediate(double x, RoundingMode mode) {
/*     */     double z;
/*  59 */     if (!DoubleUtils.isFinite(x)) {
/*  60 */       throw new ArithmeticException("input is infinite or NaN");
/*     */     }
/*  62 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  64 */         MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
/*  65 */         return x;
/*     */       
/*     */       case FLOOR:
/*  68 */         if (x >= 0.0D || isMathematicalInteger(x)) {
/*  69 */           return x;
/*     */         }
/*  71 */         return ((long)x - 1L);
/*     */ 
/*     */       
/*     */       case CEILING:
/*  75 */         if (x <= 0.0D || isMathematicalInteger(x)) {
/*  76 */           return x;
/*     */         }
/*  78 */         return ((long)x + 1L);
/*     */ 
/*     */       
/*     */       case DOWN:
/*  82 */         return x;
/*     */       
/*     */       case UP:
/*  85 */         if (isMathematicalInteger(x)) {
/*  86 */           return x;
/*     */         }
/*  88 */         return ((long)x + ((x > 0.0D) ? 1L : -1L));
/*     */ 
/*     */       
/*     */       case HALF_EVEN:
/*  92 */         return Math.rint(x);
/*     */ 
/*     */       
/*     */       case HALF_UP:
/*  96 */         z = Math.rint(x);
/*  97 */         if (Math.abs(x - z) == 0.5D) {
/*  98 */           return x + Math.copySign(0.5D, x);
/*     */         }
/* 100 */         return z;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/* 106 */         z = Math.rint(x);
/* 107 */         if (Math.abs(x - z) == 0.5D) {
/* 108 */           return x;
/*     */         }
/* 110 */         return z;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 115 */     throw new AssertionError();
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static int roundToInt(double x, RoundingMode mode) {
/* 136 */     double z = roundIntermediate(x, mode);
/* 137 */     MathPreconditions.checkInRangeForRoundingInputs(((z > -2.147483649E9D)) & ((z < 2.147483648E9D)), x, mode);
/*     */     
/* 139 */     return (int)z;
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static long roundToLong(double x, RoundingMode mode) {
/* 162 */     double z = roundIntermediate(x, mode);
/* 163 */     MathPreconditions.checkInRangeForRoundingInputs(((-9.223372036854776E18D - z < 1.0D)) & ((z < 9.223372036854776E18D)), x, mode);
/*     */     
/* 165 */     return (long)z;
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
/* 190 */     x = roundIntermediate(x, mode);
/* 191 */     if ((((-9.223372036854776E18D - x < 1.0D) ? 1 : 0) & ((x < 9.223372036854776E18D) ? 1 : 0)) != 0) {
/* 192 */       return BigInteger.valueOf((long)x);
/*     */     }
/* 194 */     int exponent = Math.getExponent(x);
/* 195 */     long significand = DoubleUtils.getSignificand(x);
/* 196 */     BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
/* 197 */     return (x < 0.0D) ? result.negate() : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static boolean isPowerOfTwo(double x) {
/* 207 */     if (x > 0.0D && DoubleUtils.isFinite(x)) {
/* 208 */       long significand = DoubleUtils.getSignificand(x);
/* 209 */       return ((significand & significand - 1L) == 0L);
/*     */     } 
/* 211 */     return false;
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
/*     */   public static double log2(double x) {
/* 231 */     return Math.log(x) / LN_2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static int log2(double x, RoundingMode mode) {
/*     */     boolean bool1;
/*     */     int j, i;
/*     */     boolean increment;
/*     */     double xScaled;
/* 247 */     Preconditions.checkArgument((x > 0.0D && DoubleUtils.isFinite(x)), "x must be positive and finite");
/* 248 */     int exponent = Math.getExponent(x);
/* 249 */     if (!DoubleUtils.isNormal(x)) {
/* 250 */       return log2(x * 4.503599627370496E15D, mode) - 52;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 255 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 257 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       
/*     */       case FLOOR:
/* 260 */         bool1 = false;
/*     */         break;
/*     */       case CEILING:
/* 263 */         bool1 = !isPowerOfTwo(x);
/*     */         break;
/*     */       case DOWN:
/* 266 */         j = ((exponent < 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case UP:
/* 269 */         i = ((exponent >= 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case HALF_EVEN:
/*     */       case HALF_UP:
/*     */       case HALF_DOWN:
/* 274 */         xScaled = DoubleUtils.scaleNormalize(x);
/*     */ 
/*     */         
/* 277 */         increment = (xScaled * xScaled > 2.0D);
/*     */         break;
/*     */       default:
/* 280 */         throw new AssertionError();
/*     */     } 
/* 282 */     return increment ? (exponent + 1) : exponent;
/*     */   }
/*     */   
/* 285 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final int MAX_FACTORIAL = 170;
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static boolean isMathematicalInteger(double x) {
/* 295 */     return (DoubleUtils.isFinite(x) && (x == 0.0D || 52 - 
/*     */       
/* 297 */       Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x)));
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
/*     */   public static double factorial(int n) {
/* 310 */     MathPreconditions.checkNonNegative("n", n);
/* 311 */     if (n > 170) {
/* 312 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */ 
/*     */     
/* 316 */     double accum = 1.0D;
/* 317 */     for (int i = 1 + (n & 0xFFFFFFF0); i <= n; i++) {
/* 318 */       accum *= i;
/*     */     }
/* 320 */     return accum * everySixteenthFactorial[n >> 4];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 327 */   static final double[] everySixteenthFactorial = new double[] { 1.0D, 2.0922789888E13D, 2.631308369336935E35D, 1.2413915592536073E61D, 1.2688693218588417E89D, 7.156945704626381E118D, 9.916779348709496E149D, 1.974506857221074E182D, 3.856204823625804E215D, 5.5502938327393044E249D, 4.7147236359920616E284D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean fuzzyEquals(double a, double b, double tolerance) {
/* 368 */     MathPreconditions.checkNonNegative("tolerance", tolerance);
/* 369 */     return (Math.copySign(a - b, 1.0D) <= tolerance || a == b || (
/*     */ 
/*     */       
/* 372 */       Double.isNaN(a) && Double.isNaN(b)));
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
/*     */   public static int fuzzyCompare(double a, double b, double tolerance) {
/* 389 */     if (fuzzyEquals(a, b, tolerance))
/* 390 */       return 0; 
/* 391 */     if (a < b)
/* 392 */       return -1; 
/* 393 */     if (a > b) {
/* 394 */       return 1;
/*     */     }
/* 396 */     return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
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
/*     */   @Deprecated
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static double mean(double... values) {
/* 417 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/* 418 */     long count = 1L;
/* 419 */     double mean = checkFinite(values[0]);
/* 420 */     for (int index = 1; index < values.length; index++) {
/* 421 */       checkFinite(values[index]);
/* 422 */       count++;
/*     */       
/* 424 */       mean += (values[index] - mean) / count;
/*     */     } 
/* 426 */     return mean;
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
/*     */   @Deprecated
/*     */   public static double mean(int... values) {
/* 443 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/*     */ 
/*     */ 
/*     */     
/* 447 */     long sum = 0L;
/* 448 */     for (int index = 0; index < values.length; index++) {
/* 449 */       sum += values[index];
/*     */     }
/* 451 */     return sum / values.length;
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
/*     */   @Deprecated
/*     */   public static double mean(long... values) {
/* 469 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/* 470 */     long count = 1L;
/* 471 */     double mean = values[0];
/* 472 */     for (int index = 1; index < values.length; index++) {
/* 473 */       count++;
/*     */       
/* 475 */       mean += (values[index] - mean) / count;
/*     */     } 
/* 477 */     return mean;
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
/*     */   @Deprecated
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static double mean(Iterable<? extends Number> values) {
/* 498 */     return mean(values.iterator());
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
/*     */   @Deprecated
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static double mean(Iterator<? extends Number> values) {
/* 519 */     Preconditions.checkArgument(values.hasNext(), "Cannot take mean of 0 values");
/* 520 */     long count = 1L;
/* 521 */     double mean = checkFinite(((Number)values.next()).doubleValue());
/* 522 */     while (values.hasNext()) {
/* 523 */       double value = checkFinite(((Number)values.next()).doubleValue());
/* 524 */       count++;
/*     */       
/* 526 */       mean += (value - mean) / count;
/*     */     } 
/* 528 */     return mean;
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   @CanIgnoreReturnValue
/*     */   private static double checkFinite(double argument) {
/* 535 */     Preconditions.checkArgument(DoubleUtils.isFinite(argument));
/* 536 */     return argument;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/DoubleMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */