/*      */ package com.google.common.math;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Longs;
/*      */ import com.google.common.primitives.UnsignedLongs;
/*      */ import java.math.RoundingMode;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class LongMath
/*      */ {
/*      */   @VisibleForTesting
/*      */   static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
/*      */   @VisibleForTesting
/*      */   static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
/*      */   
/*      */   public static long ceilingPowerOfTwo(long x) {
/*   68 */     MathPreconditions.checkPositive("x", x);
/*   69 */     if (x > 4611686018427387904L) {
/*   70 */       throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") is not representable as a long");
/*      */     }
/*   72 */     return 1L << -Long.numberOfLeadingZeros(x - 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long floorPowerOfTwo(long x) {
/*   83 */     MathPreconditions.checkPositive("x", x);
/*      */ 
/*      */ 
/*      */     
/*   87 */     return 1L << 63 - Long.numberOfLeadingZeros(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPowerOfTwo(long x) {
/*   98 */     return ((x > 0L)) & (((x & x - 1L) == 0L));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static int lessThanBranchFree(long x, long y) {
/*  109 */     return (int)((x - y ^ 0xFFFFFFFFFFFFFFFFL ^ 0xFFFFFFFFFFFFFFFFL) >>> 63L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int log2(long x, RoundingMode mode) {
/*      */     int leadingZeros;
/*      */     long cmp;
/*      */     int logFloor;
/*  122 */     MathPreconditions.checkPositive("x", x);
/*  123 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  125 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*      */       
/*      */       case DOWN:
/*      */       case FLOOR:
/*  129 */         return 63 - Long.numberOfLeadingZeros(x);
/*      */       
/*      */       case UP:
/*      */       case CEILING:
/*  133 */         return 64 - Long.numberOfLeadingZeros(x - 1L);
/*      */ 
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  139 */         leadingZeros = Long.numberOfLeadingZeros(x);
/*  140 */         cmp = -5402926248376769404L >>> leadingZeros;
/*      */         
/*  142 */         logFloor = 63 - leadingZeros;
/*  143 */         return logFloor + lessThanBranchFree(cmp, x);
/*      */     } 
/*  145 */     throw new AssertionError("impossible");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static int log10(long x, RoundingMode mode) {
/*  163 */     MathPreconditions.checkPositive("x", x);
/*  164 */     int logFloor = log10Floor(x);
/*  165 */     long floorPow = powersOf10[logFloor];
/*  166 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  168 */         MathPreconditions.checkRoundingUnnecessary((x == floorPow));
/*      */       
/*      */       case DOWN:
/*      */       case FLOOR:
/*  172 */         return logFloor;
/*      */       case UP:
/*      */       case CEILING:
/*  175 */         return logFloor + lessThanBranchFree(floorPow, x);
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  180 */         return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
/*      */     } 
/*  182 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static int log10Floor(long x) {
/*  195 */     int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  200 */     return y - lessThanBranchFree(x, powersOf10[y]);
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  205 */   static final byte[] maxLog10ForLeadingZeros = new byte[] { 19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  214 */   static final long[] powersOf10 = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  240 */   static final long[] halfPowersOf10 = new long[] { 3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static long pow(long b, int k) {
/*  272 */     MathPreconditions.checkNonNegative("exponent", k);
/*  273 */     if (-2L <= b && b <= 2L) {
/*  274 */       switch ((int)b) {
/*      */         case 0:
/*  276 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  278 */           return 1L;
/*      */         case -1:
/*  280 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  282 */           return (k < 64) ? (1L << k) : 0L;
/*      */         case -2:
/*  284 */           if (k < 64) {
/*  285 */             return ((k & 0x1) == 0) ? (1L << k) : -(1L << k);
/*      */           }
/*  287 */           return 0L;
/*      */       } 
/*      */       
/*  290 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  293 */     for (long accum = 1L;; k >>= 1) {
/*  294 */       switch (k) {
/*      */         case 0:
/*  296 */           return accum;
/*      */         case 1:
/*  298 */           return accum * b;
/*      */       } 
/*  300 */       accum *= ((k & 0x1) == 0) ? 1L : b;
/*  301 */       b *= b;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static long sqrt(long x, RoundingMode mode) {
/*      */     long sqrtFloor, halfSquare;
/*  316 */     MathPreconditions.checkNonNegative("x", x);
/*  317 */     if (fitsInInt(x)) {
/*  318 */       return IntMath.sqrt((int)x, mode);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  335 */     long guess = (long)Math.sqrt(x);
/*      */     
/*  337 */     long guessSquared = guess * guess;
/*      */ 
/*      */     
/*  340 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  342 */         MathPreconditions.checkRoundingUnnecessary((guessSquared == x));
/*  343 */         return guess;
/*      */       case DOWN:
/*      */       case FLOOR:
/*  346 */         if (x < guessSquared) {
/*  347 */           return guess - 1L;
/*      */         }
/*  349 */         return guess;
/*      */       case UP:
/*      */       case CEILING:
/*  352 */         if (x > guessSquared) {
/*  353 */           return guess + 1L;
/*      */         }
/*  355 */         return guess;
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  359 */         sqrtFloor = guess - ((x < guessSquared) ? 1L : 0L);
/*  360 */         halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  372 */         return sqrtFloor + lessThanBranchFree(halfSquare, x);
/*      */     } 
/*  374 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static long divide(long p, long q, RoundingMode mode) {
/*      */     boolean increment;
/*      */     long absRem, cmpRemToHalfDivisor;
/*  388 */     Preconditions.checkNotNull(mode);
/*  389 */     long div = p / q;
/*  390 */     long rem = p - q * div;
/*      */     
/*  392 */     if (rem == 0L) {
/*  393 */       return div;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  403 */     int signum = 0x1 | (int)((p ^ q) >> 63L);
/*      */     
/*  405 */     switch (mode) {
/*      */       case UNNECESSARY:
/*  407 */         MathPreconditions.checkRoundingUnnecessary((rem == 0L));
/*      */       
/*      */       case DOWN:
/*  410 */         increment = false;
/*      */         break;
/*      */       case UP:
/*  413 */         increment = true;
/*      */         break;
/*      */       case CEILING:
/*  416 */         increment = (signum > 0);
/*      */         break;
/*      */       case FLOOR:
/*  419 */         increment = (signum < 0);
/*      */         break;
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/*  424 */         absRem = Math.abs(rem);
/*  425 */         cmpRemToHalfDivisor = absRem - Math.abs(q) - absRem;
/*      */ 
/*      */         
/*  428 */         if (cmpRemToHalfDivisor == 0L) {
/*  429 */           increment = (mode == RoundingMode.HALF_UP || (mode == RoundingMode.HALF_EVEN && (div & 0x1L) != 0L)); break;
/*      */         } 
/*  431 */         increment = (cmpRemToHalfDivisor > 0L);
/*      */         break;
/*      */       
/*      */       default:
/*  435 */         throw new AssertionError();
/*      */     } 
/*  437 */     return increment ? (div + signum) : div;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static int mod(long x, int m) {
/*  462 */     return (int)mod(x, m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static long mod(long x, long m) {
/*  486 */     if (m <= 0L) {
/*  487 */       throw new ArithmeticException("Modulus must be positive");
/*      */     }
/*  489 */     long result = x % m;
/*  490 */     return (result >= 0L) ? result : (result + m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long gcd(long a, long b) {
/*  505 */     MathPreconditions.checkNonNegative("a", a);
/*  506 */     MathPreconditions.checkNonNegative("b", b);
/*  507 */     if (a == 0L)
/*      */     {
/*      */       
/*  510 */       return b; } 
/*  511 */     if (b == 0L) {
/*  512 */       return a;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  518 */     int aTwos = Long.numberOfTrailingZeros(a);
/*  519 */     a >>= aTwos;
/*  520 */     int bTwos = Long.numberOfTrailingZeros(b);
/*  521 */     b >>= bTwos;
/*  522 */     while (a != b) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  530 */       long delta = a - b;
/*      */       
/*  532 */       long minDeltaOrZero = delta & delta >> 63L;
/*      */ 
/*      */       
/*  535 */       a = delta - minDeltaOrZero - minDeltaOrZero;
/*      */ 
/*      */       
/*  538 */       b += minDeltaOrZero;
/*  539 */       a >>= Long.numberOfTrailingZeros(a);
/*      */     } 
/*  541 */     return a << Math.min(aTwos, bTwos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long checkedAdd(long a, long b) {
/*  551 */     long result = a + b;
/*  552 */     MathPreconditions.checkNoOverflow((((a ^ b) < 0L)) | (((a ^ result) >= 0L)), "checkedAdd", a, b);
/*  553 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static long checkedSubtract(long a, long b) {
/*  565 */     long result = a - b;
/*  566 */     MathPreconditions.checkNoOverflow((((a ^ b) >= 0L)) | (((a ^ result) >= 0L)), "checkedSubtract", a, b);
/*  567 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long checkedMultiply(long a, long b) {
/*  582 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFFL) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFFL);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  593 */     if (leadingZeros > 65) {
/*  594 */       return a * b;
/*      */     }
/*  596 */     MathPreconditions.checkNoOverflow((leadingZeros >= 64), "checkedMultiply", a, b);
/*  597 */     MathPreconditions.checkNoOverflow(((a >= 0L)) | ((b != Long.MIN_VALUE)), "checkedMultiply", a, b);
/*  598 */     long result = a * b;
/*  599 */     MathPreconditions.checkNoOverflow((a == 0L || result / a == b), "checkedMultiply", a, b);
/*  600 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static long checkedPow(long b, int k) {
/*  613 */     MathPreconditions.checkNonNegative("exponent", k);
/*  614 */     if ((((b >= -2L) ? 1 : 0) & ((b <= 2L) ? 1 : 0)) != 0) {
/*  615 */       switch ((int)b) {
/*      */         case 0:
/*  617 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  619 */           return 1L;
/*      */         case -1:
/*  621 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  623 */           MathPreconditions.checkNoOverflow((k < 63), "checkedPow", b, k);
/*  624 */           return 1L << k;
/*      */         case -2:
/*  626 */           MathPreconditions.checkNoOverflow((k < 64), "checkedPow", b, k);
/*  627 */           return ((k & 0x1) == 0) ? (1L << k) : (-1L << k);
/*      */       } 
/*  629 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  632 */     long accum = 1L;
/*      */     while (true) {
/*  634 */       switch (k) {
/*      */         case 0:
/*  636 */           return accum;
/*      */         case 1:
/*  638 */           return checkedMultiply(accum, b);
/*      */       } 
/*  640 */       if ((k & 0x1) != 0) {
/*  641 */         accum = checkedMultiply(accum, b);
/*      */       }
/*  643 */       k >>= 1;
/*  644 */       if (k > 0) {
/*  645 */         MathPreconditions.checkNoOverflow((-3037000499L <= b && b <= 3037000499L), "checkedPow", b, k);
/*      */         
/*  647 */         b *= b;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long saturatedAdd(long a, long b) {
/*  661 */     long naiveSum = a + b;
/*  662 */     if (((((a ^ b) < 0L) ? 1 : 0) | (((a ^ naiveSum) >= 0L) ? 1 : 0)) != 0)
/*      */     {
/*      */       
/*  665 */       return naiveSum;
/*      */     }
/*      */     
/*  668 */     return Long.MAX_VALUE + (naiveSum >>> 63L ^ 0x1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long saturatedSubtract(long a, long b) {
/*  679 */     long naiveDifference = a - b;
/*  680 */     if (((((a ^ b) >= 0L) ? 1 : 0) | (((a ^ naiveDifference) >= 0L) ? 1 : 0)) != 0)
/*      */     {
/*      */       
/*  683 */       return naiveDifference;
/*      */     }
/*      */     
/*  686 */     return Long.MAX_VALUE + (naiveDifference >>> 63L ^ 0x1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long saturatedMultiply(long a, long b) {
/*  702 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFFL) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFFL);
/*  703 */     if (leadingZeros > 65) {
/*  704 */       return a * b;
/*      */     }
/*      */     
/*  707 */     long limit = Long.MAX_VALUE + ((a ^ b) >>> 63L);
/*  708 */     if ((((leadingZeros < 64) ? 1 : 0) | ((a < 0L) ? 1 : 0) & ((b == Long.MIN_VALUE) ? 1 : 0)) != 0)
/*      */     {
/*  710 */       return limit;
/*      */     }
/*  712 */     long result = a * b;
/*  713 */     if (a == 0L || result / a == b) {
/*  714 */       return result;
/*      */     }
/*  716 */     return limit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long saturatedPow(long b, int k) {
/*  727 */     MathPreconditions.checkNonNegative("exponent", k);
/*  728 */     if ((((b >= -2L) ? 1 : 0) & ((b <= 2L) ? 1 : 0)) != 0) {
/*  729 */       switch ((int)b) {
/*      */         case 0:
/*  731 */           return (k == 0) ? 1L : 0L;
/*      */         case 1:
/*  733 */           return 1L;
/*      */         case -1:
/*  735 */           return ((k & 0x1) == 0) ? 1L : -1L;
/*      */         case 2:
/*  737 */           if (k >= 63) {
/*  738 */             return Long.MAX_VALUE;
/*      */           }
/*  740 */           return 1L << k;
/*      */         case -2:
/*  742 */           if (k >= 64) {
/*  743 */             return Long.MAX_VALUE + (k & 0x1);
/*      */           }
/*  745 */           return ((k & 0x1) == 0) ? (1L << k) : (-1L << k);
/*      */       } 
/*  747 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  750 */     long accum = 1L;
/*      */     
/*  752 */     long limit = Long.MAX_VALUE + (b >>> 63L & (k & 0x1));
/*      */     while (true) {
/*  754 */       switch (k) {
/*      */         case 0:
/*  756 */           return accum;
/*      */         case 1:
/*  758 */           return saturatedMultiply(accum, b);
/*      */       } 
/*  760 */       if ((k & 0x1) != 0) {
/*  761 */         accum = saturatedMultiply(accum, b);
/*      */       }
/*  763 */       k >>= 1;
/*  764 */       if (k > 0) {
/*  765 */         if ((((-3037000499L > b) ? 1 : 0) | ((b > 3037000499L) ? 1 : 0)) != 0) {
/*  766 */           return limit;
/*      */         }
/*  768 */         b *= b;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static long factorial(int n) {
/*  785 */     MathPreconditions.checkNonNegative("n", n);
/*  786 */     return (n < factorials.length) ? factorials[n] : Long.MAX_VALUE;
/*      */   }
/*      */   
/*  789 */   static final long[] factorials = new long[] { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binomial(int n, int k) {
/*  820 */     MathPreconditions.checkNonNegative("n", n);
/*  821 */     MathPreconditions.checkNonNegative("k", k);
/*  822 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", k, n);
/*  823 */     if (k > n >> 1) {
/*  824 */       k = n - k;
/*      */     }
/*  826 */     switch (k) {
/*      */       case 0:
/*  828 */         return 1L;
/*      */       case 1:
/*  830 */         return n;
/*      */     } 
/*  832 */     if (n < factorials.length)
/*  833 */       return factorials[n] / factorials[k] * factorials[n - k]; 
/*  834 */     if (k >= biggestBinomials.length || n > biggestBinomials[k])
/*  835 */       return Long.MAX_VALUE; 
/*  836 */     if (k < biggestSimpleBinomials.length && n <= biggestSimpleBinomials[k]) {
/*      */       
/*  838 */       long l = n--;
/*  839 */       for (int j = 2; j <= k; n--, j++) {
/*  840 */         l *= n;
/*  841 */         l /= j;
/*      */       } 
/*  843 */       return l;
/*      */     } 
/*  845 */     int nBits = log2(n, RoundingMode.CEILING);
/*      */     
/*  847 */     long result = 1L;
/*  848 */     long numerator = n--;
/*  849 */     long denominator = 1L;
/*      */     
/*  851 */     int numeratorBits = nBits;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  859 */     for (int i = 2; i <= k; i++, n--) {
/*  860 */       if (numeratorBits + nBits < 63) {
/*      */         
/*  862 */         numerator *= n;
/*  863 */         denominator *= i;
/*  864 */         numeratorBits += nBits;
/*      */       }
/*      */       else {
/*      */         
/*  868 */         result = multiplyFraction(result, numerator, denominator);
/*  869 */         numerator = n;
/*  870 */         denominator = i;
/*  871 */         numeratorBits = nBits;
/*      */       } 
/*      */     } 
/*  874 */     return multiplyFraction(result, numerator, denominator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long multiplyFraction(long x, long numerator, long denominator) {
/*  881 */     if (x == 1L) {
/*  882 */       return numerator / denominator;
/*      */     }
/*  884 */     long commonDivisor = gcd(x, denominator);
/*  885 */     x /= commonDivisor;
/*  886 */     denominator /= commonDivisor;
/*      */ 
/*      */     
/*  889 */     return x * numerator / denominator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  895 */   static final int[] biggestBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  937 */   static final int[] biggestSimpleBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int SIEVE_30 = -545925251;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean fitsInInt(long x) {
/*  974 */     return ((int)x == x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long mean(long x, long y) {
/*  987 */     return (x & y) + ((x ^ y) >> 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static boolean isPrime(long n) {
/* 1014 */     if (n < 2L) {
/* 1015 */       MathPreconditions.checkNonNegative("n", n);
/* 1016 */       return false;
/*      */     } 
/* 1018 */     if (n < 66L) {
/*      */       
/* 1020 */       long mask = 722865708377213483L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1040 */       return ((mask >> (int)n - 2 & 0x1L) != 0L);
/*      */     } 
/*      */     
/* 1043 */     if ((0xDF75D77D & 1 << (int)(n % 30L)) != 0) {
/* 1044 */       return false;
/*      */     }
/* 1046 */     if (n % 7L == 0L || n % 11L == 0L || n % 13L == 0L) {
/* 1047 */       return false;
/*      */     }
/* 1049 */     if (n < 289L) {
/* 1050 */       return true;
/*      */     }
/*      */     
/* 1053 */     for (long[] baseSet : millerRabinBaseSets) {
/* 1054 */       if (n <= baseSet[0]) {
/* 1055 */         for (int i = 1; i < baseSet.length; i++) {
/* 1056 */           if (!MillerRabinTester.test(baseSet[i], n)) {
/* 1057 */             return false;
/*      */           }
/*      */         } 
/* 1060 */         return true;
/*      */       } 
/*      */     } 
/* 1063 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1073 */   private static final long[][] millerRabinBaseSets = new long[][] { { 291830L, 126401071349994536L }, { 885594168L, 725270293939359937L, 3569819667048198375L }, { 273919523040L, 15L, 7363882082L, 992620450144556L }, { 47636622961200L, 2L, 2570940L, 211991001L, 3749873356L }, { 7999252175582850L, 2L, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L }, { 585226005592931976L, 2L, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L }, { Long.MAX_VALUE, 2L, 325L, 9375L, 28178L, 450775L, 9780504L, 1795265022L } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private enum MillerRabinTester
/*      */   {
/* 1100 */     SMALL
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       long mulMod(long a, long b, long m)
/*      */       {
/* 1109 */         return a * b % m;
/*      */       }
/*      */ 
/*      */       
/*      */       long squareMod(long a, long m) {
/* 1114 */         return a * a % m;
/*      */       }
/*      */     },
/*      */     
/* 1118 */     LARGE
/*      */     {
/*      */       private long plusMod(long a, long b, long m) {
/* 1121 */         return (a >= m - b) ? (a + b - m) : (a + b);
/*      */       }
/*      */ 
/*      */       
/*      */       private long times2ToThe32Mod(long a, long m) {
/* 1126 */         int remainingPowersOf2 = 32;
/*      */         while (true) {
/* 1128 */           int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a));
/*      */ 
/*      */           
/* 1131 */           a = UnsignedLongs.remainder(a << shift, m);
/* 1132 */           remainingPowersOf2 -= shift;
/* 1133 */           if (remainingPowersOf2 <= 0)
/* 1134 */             return a; 
/*      */         } 
/*      */       }
/*      */       
/*      */       long mulMod(long a, long b, long m) {
/* 1139 */         long aHi = a >>> 32L;
/* 1140 */         long bHi = b >>> 32L;
/* 1141 */         long aLo = a & 0xFFFFFFFFL;
/* 1142 */         long bLo = b & 0xFFFFFFFFL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1152 */         long result = times2ToThe32Mod(aHi * bHi, m);
/* 1153 */         result += aHi * bLo;
/* 1154 */         if (result < 0L) {
/* 1155 */           result = UnsignedLongs.remainder(result, m);
/*      */         }
/*      */         
/* 1158 */         result += aLo * bHi;
/* 1159 */         result = times2ToThe32Mod(result, m);
/* 1160 */         return plusMod(result, UnsignedLongs.remainder(aLo * bLo, m), m);
/*      */       }
/*      */ 
/*      */       
/*      */       long squareMod(long a, long m) {
/* 1165 */         long aHi = a >>> 32L;
/* 1166 */         long aLo = a & 0xFFFFFFFFL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1175 */         long result = times2ToThe32Mod(aHi * aHi, m);
/* 1176 */         long hiLo = aHi * aLo * 2L;
/* 1177 */         if (hiLo < 0L) {
/* 1178 */           hiLo = UnsignedLongs.remainder(hiLo, m);
/*      */         }
/*      */         
/* 1181 */         result += hiLo;
/* 1182 */         result = times2ToThe32Mod(result, m);
/* 1183 */         return plusMod(result, UnsignedLongs.remainder(aLo * aLo, m), m);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */     
/*      */     static boolean test(long base, long n) {
/* 1190 */       return ((n <= 3037000499L) ? SMALL : LARGE).testWitness(base, n);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private long powMod(long a, long p, long m) {
/* 1201 */       long res = 1L;
/* 1202 */       for (; p != 0L; p >>= 1L) {
/* 1203 */         if ((p & 0x1L) != 0L) {
/* 1204 */           res = mulMod(res, a, m);
/*      */         }
/* 1206 */         a = squareMod(a, m);
/*      */       } 
/* 1208 */       return res;
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean testWitness(long base, long n) {
/* 1213 */       int r = Long.numberOfTrailingZeros(n - 1L);
/* 1214 */       long d = n - 1L >> r;
/* 1215 */       base %= n;
/* 1216 */       if (base == 0L) {
/* 1217 */         return true;
/*      */       }
/*      */       
/* 1220 */       long a = powMod(base, d, n);
/*      */ 
/*      */ 
/*      */       
/* 1224 */       if (a == 1L) {
/* 1225 */         return true;
/*      */       }
/* 1227 */       int j = 0;
/* 1228 */       while (a != n - 1L) {
/* 1229 */         if (++j == r) {
/* 1230 */           return false;
/*      */         }
/* 1232 */         a = squareMod(a, n);
/*      */       } 
/* 1234 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract long mulMod(long param1Long1, long param1Long2, long param1Long3);
/*      */ 
/*      */ 
/*      */     
/*      */     abstract long squareMod(long param1Long1, long param1Long2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static double roundToDouble(long x, RoundingMode mode) {
/*      */     int cmpXToRoundArbitrarily;
/*      */     long roundFloor;
/*      */     double roundFloorAsDouble;
/*      */     long roundCeiling;
/*      */     double roundCeilingAsDouble;
/*      */     long deltaToFloor, deltaToCeiling;
/*      */     int diff;
/* 1259 */     double roundArbitrarily = x;
/* 1260 */     long roundArbitrarilyAsLong = (long)roundArbitrarily;
/*      */ 
/*      */     
/* 1263 */     if (roundArbitrarilyAsLong == Long.MAX_VALUE) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1274 */       cmpXToRoundArbitrarily = -1;
/*      */     } else {
/* 1276 */       cmpXToRoundArbitrarily = Longs.compare(x, roundArbitrarilyAsLong);
/*      */     } 
/*      */     
/* 1279 */     switch (mode) {
/*      */       case UNNECESSARY:
/* 1281 */         MathPreconditions.checkRoundingUnnecessary((cmpXToRoundArbitrarily == 0));
/* 1282 */         return roundArbitrarily;
/*      */       case FLOOR:
/* 1284 */         return (cmpXToRoundArbitrarily >= 0) ? 
/* 1285 */           roundArbitrarily : 
/* 1286 */           DoubleUtils.nextDown(roundArbitrarily);
/*      */       case CEILING:
/* 1288 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*      */       case DOWN:
/* 1290 */         if (x >= 0L) {
/* 1291 */           return (cmpXToRoundArbitrarily >= 0) ? 
/* 1292 */             roundArbitrarily : 
/* 1293 */             DoubleUtils.nextDown(roundArbitrarily);
/*      */         }
/* 1295 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*      */       
/*      */       case UP:
/* 1298 */         if (x >= 0L) {
/* 1299 */           return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*      */         }
/* 1301 */         return (cmpXToRoundArbitrarily >= 0) ? 
/* 1302 */           roundArbitrarily : 
/* 1303 */           DoubleUtils.nextDown(roundArbitrarily);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case HALF_DOWN:
/*      */       case HALF_UP:
/*      */       case HALF_EVEN:
/* 1314 */         if (cmpXToRoundArbitrarily >= 0) {
/* 1315 */           roundFloorAsDouble = roundArbitrarily;
/* 1316 */           roundFloor = roundArbitrarilyAsLong;
/* 1317 */           roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
/* 1318 */           roundCeiling = (long)Math.ceil(roundCeilingAsDouble);
/*      */         } else {
/* 1320 */           roundCeilingAsDouble = roundArbitrarily;
/* 1321 */           roundCeiling = roundArbitrarilyAsLong;
/* 1322 */           roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
/* 1323 */           roundFloor = (long)Math.floor(roundFloorAsDouble);
/*      */         } 
/*      */         
/* 1326 */         deltaToFloor = x - roundFloor;
/* 1327 */         deltaToCeiling = roundCeiling - x;
/*      */         
/* 1329 */         if (roundCeiling == Long.MAX_VALUE)
/*      */         {
/*      */           
/* 1332 */           deltaToCeiling++;
/*      */         }
/*      */         
/* 1335 */         diff = Longs.compare(deltaToFloor, deltaToCeiling);
/* 1336 */         if (diff < 0)
/* 1337 */           return roundFloorAsDouble; 
/* 1338 */         if (diff > 0) {
/* 1339 */           return roundCeilingAsDouble;
/*      */         }
/*      */         
/* 1342 */         switch (mode) {
/*      */           case HALF_EVEN:
/* 1344 */             return ((DoubleUtils.getSignificand(roundFloorAsDouble) & 0x1L) == 0L) ? 
/* 1345 */               roundFloorAsDouble : 
/* 1346 */               roundCeilingAsDouble;
/*      */           case HALF_DOWN:
/* 1348 */             return (x >= 0L) ? roundFloorAsDouble : roundCeilingAsDouble;
/*      */           case HALF_UP:
/* 1350 */             return (x >= 0L) ? roundCeilingAsDouble : roundFloorAsDouble;
/*      */         } 
/* 1352 */         throw new AssertionError("impossible");
/*      */     } 
/*      */ 
/*      */     
/* 1356 */     throw new AssertionError("impossible");
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/LongMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */