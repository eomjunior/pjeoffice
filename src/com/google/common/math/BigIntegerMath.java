/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class BigIntegerMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
/*     */   
/*     */   public static BigInteger ceilingPowerOfTwo(BigInteger x) {
/*  61 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.CEILING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigInteger floorPowerOfTwo(BigInteger x) {
/*  72 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.FLOOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPowerOfTwo(BigInteger x) {
/*  77 */     Preconditions.checkNotNull(x);
/*  78 */     return (x.signum() > 0 && x.getLowestSetBit() == x.bitLength() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int log2(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2;
/*     */     int logX2Floor;
/*  91 */     MathPreconditions.checkPositive("x", (BigInteger)Preconditions.checkNotNull(x));
/*  92 */     int logFloor = x.bitLength() - 1;
/*  93 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  95 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/*  98 */         return logFloor;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 102 */         return isPowerOfTwo(x) ? logFloor : (logFloor + 1);
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 107 */         if (logFloor < 256) {
/*     */           
/* 109 */           BigInteger halfPower = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - logFloor);
/* 110 */           if (x.compareTo(halfPower) <= 0) {
/* 111 */             return logFloor;
/*     */           }
/* 113 */           return logFloor + 1;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 120 */         x2 = x.pow(2);
/* 121 */         logX2Floor = x2.bitLength() - 1;
/* 122 */         return (logX2Floor < 2 * logFloor + 1) ? logFloor : (logFloor + 1);
/*     */     } 
/*     */     
/* 125 */     throw new AssertionError();
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
/*     */   @VisibleForTesting
/* 137 */   static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
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
/*     */   public static int log10(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2, halfPowerSquared;
/* 151 */     MathPreconditions.checkPositive("x", x);
/* 152 */     if (fitsInLong(x)) {
/* 153 */       return LongMath.log10(x.longValue(), mode);
/*     */     }
/*     */     
/* 156 */     int approxLog10 = (int)(log2(x, RoundingMode.FLOOR) * LN_2 / LN_10);
/* 157 */     BigInteger approxPow = BigInteger.TEN.pow(approxLog10);
/* 158 */     int approxCmp = approxPow.compareTo(x);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     if (approxCmp > 0) {
/*     */ 
/*     */       
/*     */       do {
/*     */ 
/*     */ 
/*     */         
/* 172 */         approxLog10--;
/* 173 */         approxPow = approxPow.divide(BigInteger.TEN);
/* 174 */         approxCmp = approxPow.compareTo(x);
/* 175 */       } while (approxCmp > 0);
/*     */     } else {
/* 177 */       BigInteger nextPow = BigInteger.TEN.multiply(approxPow);
/* 178 */       int nextCmp = nextPow.compareTo(x);
/* 179 */       while (nextCmp <= 0) {
/* 180 */         approxLog10++;
/* 181 */         approxPow = nextPow;
/* 182 */         approxCmp = nextCmp;
/* 183 */         nextPow = BigInteger.TEN.multiply(approxPow);
/* 184 */         nextCmp = nextPow.compareTo(x);
/*     */       } 
/*     */     } 
/*     */     
/* 188 */     int floorLog = approxLog10;
/* 189 */     BigInteger floorPow = approxPow;
/* 190 */     int floorCmp = approxCmp;
/*     */     
/* 192 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 194 */         MathPreconditions.checkRoundingUnnecessary((floorCmp == 0));
/*     */       
/*     */       case DOWN:
/*     */       case FLOOR:
/* 198 */         return floorLog;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 202 */         return floorPow.equals(x) ? floorLog : (floorLog + 1);
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 208 */         x2 = x.pow(2);
/* 209 */         halfPowerSquared = floorPow.pow(2).multiply(BigInteger.TEN);
/* 210 */         return (x2.compareTo(halfPowerSquared) <= 0) ? floorLog : (floorLog + 1);
/*     */     } 
/* 212 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 216 */   private static final double LN_10 = Math.log(10.0D);
/* 217 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static BigInteger sqrt(BigInteger x, RoundingMode mode) {
/*     */     int sqrtFloorInt;
/*     */     boolean sqrtFloorIsExact;
/*     */     BigInteger halfSquare;
/* 230 */     MathPreconditions.checkNonNegative("x", x);
/* 231 */     if (fitsInLong(x)) {
/* 232 */       return BigInteger.valueOf(LongMath.sqrt(x.longValue(), mode));
/*     */     }
/* 234 */     BigInteger sqrtFloor = sqrtFloor(x);
/* 235 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 237 */         MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/* 240 */         return sqrtFloor;
/*     */       case UP:
/*     */       case CEILING:
/* 243 */         sqrtFloorInt = sqrtFloor.intValue();
/*     */ 
/*     */         
/* 246 */         sqrtFloorIsExact = (sqrtFloorInt * sqrtFloorInt == x.intValue() && sqrtFloor.pow(2).equals(x));
/* 247 */         return sqrtFloorIsExact ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 251 */         halfSquare = sqrtFloor.pow(2).add(sqrtFloor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 257 */         return (halfSquare.compareTo(x) >= 0) ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */     } 
/* 259 */     throw new AssertionError();
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtFloor(BigInteger x) {
/*     */     BigInteger sqrt0;
/* 286 */     int log2 = log2(x, RoundingMode.FLOOR);
/* 287 */     if (log2 < 1023) {
/* 288 */       sqrt0 = sqrtApproxWithDoubles(x);
/*     */     } else {
/* 290 */       int shift = log2 - 52 & 0xFFFFFFFE;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 295 */       sqrt0 = sqrtApproxWithDoubles(x.shiftRight(shift)).shiftLeft(shift >> 1);
/*     */     } 
/* 297 */     BigInteger sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 298 */     if (sqrt0.equals(sqrt1)) {
/* 299 */       return sqrt0;
/*     */     }
/*     */     while (true) {
/* 302 */       sqrt0 = sqrt1;
/* 303 */       sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 304 */       if (sqrt1.compareTo(sqrt0) >= 0)
/* 305 */         return sqrt0; 
/*     */     } 
/*     */   }
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtApproxWithDoubles(BigInteger x) {
/* 311 */     return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(x)), RoundingMode.HALF_EVEN);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static double roundToDouble(BigInteger x, RoundingMode mode) {
/* 338 */     return BigIntegerToDoubleRounder.INSTANCE.roundToDouble(x, mode);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static class BigIntegerToDoubleRounder extends ToDoubleRounder<BigInteger> {
/* 344 */     static final BigIntegerToDoubleRounder INSTANCE = new BigIntegerToDoubleRounder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double roundToDoubleArbitrarily(BigInteger bigInteger) {
/* 350 */       return DoubleUtils.bigToDouble(bigInteger);
/*     */     }
/*     */ 
/*     */     
/*     */     int sign(BigInteger bigInteger) {
/* 355 */       return bigInteger.signum();
/*     */     }
/*     */ 
/*     */     
/*     */     BigInteger toX(double d, RoundingMode mode) {
/* 360 */       return DoubleMath.roundToBigInteger(d, mode);
/*     */     }
/*     */ 
/*     */     
/*     */     BigInteger minus(BigInteger a, BigInteger b) {
/* 365 */       return a.subtract(b);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static BigInteger divide(BigInteger p, BigInteger q, RoundingMode mode) {
/* 379 */     BigDecimal pDec = new BigDecimal(p);
/* 380 */     BigDecimal qDec = new BigDecimal(q);
/* 381 */     return pDec.divide(qDec, 0, mode).toBigIntegerExact();
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
/*     */   public static BigInteger factorial(int n) {
/* 397 */     MathPreconditions.checkNonNegative("n", n);
/*     */ 
/*     */     
/* 400 */     if (n < LongMath.factorials.length) {
/* 401 */       return BigInteger.valueOf(LongMath.factorials[n]);
/*     */     }
/*     */ 
/*     */     
/* 405 */     int approxSize = IntMath.divide(n * IntMath.log2(n, RoundingMode.CEILING), 64, RoundingMode.CEILING);
/* 406 */     ArrayList<BigInteger> bignums = new ArrayList<>(approxSize);
/*     */ 
/*     */     
/* 409 */     int startingNumber = LongMath.factorials.length;
/* 410 */     long product = LongMath.factorials[startingNumber - 1];
/*     */     
/* 412 */     int shift = Long.numberOfTrailingZeros(product);
/* 413 */     product >>= shift;
/*     */ 
/*     */     
/* 416 */     int productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/* 417 */     int bits = LongMath.log2(startingNumber, RoundingMode.FLOOR) + 1;
/*     */     
/* 419 */     int nextPowerOfTwo = 1 << bits - 1;
/*     */     
/*     */     long num;
/* 422 */     for (num = startingNumber; num <= n; num++) {
/*     */       
/* 424 */       if ((num & nextPowerOfTwo) != 0L) {
/* 425 */         nextPowerOfTwo <<= 1;
/* 426 */         bits++;
/*     */       } 
/*     */       
/* 429 */       int tz = Long.numberOfTrailingZeros(num);
/* 430 */       long normalizedNum = num >> tz;
/* 431 */       shift += tz;
/*     */       
/* 433 */       int normalizedBits = bits - tz;
/*     */       
/* 435 */       if (normalizedBits + productBits >= 64) {
/* 436 */         bignums.add(BigInteger.valueOf(product));
/* 437 */         product = 1L;
/* 438 */         productBits = 0;
/*     */       } 
/* 440 */       product *= normalizedNum;
/* 441 */       productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/*     */     } 
/*     */     
/* 444 */     if (product > 1L) {
/* 445 */       bignums.add(BigInteger.valueOf(product));
/*     */     }
/*     */     
/* 448 */     return listProduct(bignums).shiftLeft(shift);
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums) {
/* 452 */     return listProduct(nums, 0, nums.size());
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums, int start, int end) {
/* 456 */     switch (end - start) {
/*     */       case 0:
/* 458 */         return BigInteger.ONE;
/*     */       case 1:
/* 460 */         return nums.get(start);
/*     */       case 2:
/* 462 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1));
/*     */       case 3:
/* 464 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
/*     */     } 
/*     */     
/* 467 */     int m = end + start >>> 1;
/* 468 */     return listProduct(nums, start, m).multiply(listProduct(nums, m, end));
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
/*     */   public static BigInteger binomial(int n, int k) {
/* 481 */     MathPreconditions.checkNonNegative("n", n);
/* 482 */     MathPreconditions.checkNonNegative("k", k);
/* 483 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", k, n);
/* 484 */     if (k > n >> 1) {
/* 485 */       k = n - k;
/*     */     }
/* 487 */     if (k < LongMath.biggestBinomials.length && n <= LongMath.biggestBinomials[k]) {
/* 488 */       return BigInteger.valueOf(LongMath.binomial(n, k));
/*     */     }
/*     */     
/* 491 */     BigInteger accum = BigInteger.ONE;
/*     */     
/* 493 */     long numeratorAccum = n;
/* 494 */     long denominatorAccum = 1L;
/*     */     
/* 496 */     int bits = LongMath.log2(n, RoundingMode.CEILING);
/*     */     
/* 498 */     int numeratorBits = bits;
/*     */     
/* 500 */     for (int i = 1; i < k; i++) {
/* 501 */       int p = n - i;
/* 502 */       int q = i + 1;
/*     */ 
/*     */ 
/*     */       
/* 506 */       if (numeratorBits + bits >= 63) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 512 */         accum = accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
/* 513 */         numeratorAccum = p;
/* 514 */         denominatorAccum = q;
/* 515 */         numeratorBits = bits;
/*     */       } else {
/*     */         
/* 518 */         numeratorAccum *= p;
/* 519 */         denominatorAccum *= q;
/* 520 */         numeratorBits += bits;
/*     */       } 
/*     */     } 
/* 523 */     return accum
/* 524 */       .multiply(BigInteger.valueOf(numeratorAccum))
/* 525 */       .divide(BigInteger.valueOf(denominatorAccum));
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   static boolean fitsInLong(BigInteger x) {
/* 532 */     return (x.bitLength() <= 63);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/BigIntegerMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */