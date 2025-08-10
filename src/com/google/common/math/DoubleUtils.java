/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ final class DoubleUtils
/*     */ {
/*     */   static final long SIGNIFICAND_MASK = 4503599627370495L;
/*     */   static final long EXPONENT_MASK = 9218868437227405312L;
/*     */   static final long SIGN_MASK = -9223372036854775808L;
/*     */   static final int SIGNIFICAND_BITS = 52;
/*     */   static final int EXPONENT_BIAS = 1023;
/*     */   static final long IMPLICIT_BIT = 4503599627370496L;
/*     */   @VisibleForTesting
/*     */   static final long ONE_BITS = 4607182418800017408L;
/*     */   
/*     */   static double nextDown(double d) {
/*  41 */     return -Math.nextUp(-d);
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
/*     */   static long getSignificand(double d) {
/*  64 */     Preconditions.checkArgument(isFinite(d), "not a normal value");
/*  65 */     int exponent = Math.getExponent(d);
/*  66 */     long bits = Double.doubleToRawLongBits(d);
/*  67 */     bits &= 0xFFFFFFFFFFFFFL;
/*  68 */     return (exponent == -1023) ? (bits << 1L) : (bits | 0x10000000000000L);
/*     */   }
/*     */   
/*     */   static boolean isFinite(double d) {
/*  72 */     return (Math.getExponent(d) <= 1023);
/*     */   }
/*     */   
/*     */   static boolean isNormal(double d) {
/*  76 */     return (Math.getExponent(d) >= -1022);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static double scaleNormalize(double x) {
/*  84 */     long significand = Double.doubleToRawLongBits(x) & 0xFFFFFFFFFFFFFL;
/*  85 */     return Double.longBitsToDouble(significand | 0x3FF0000000000000L);
/*     */   }
/*     */ 
/*     */   
/*     */   static double bigToDouble(BigInteger x) {
/*  90 */     BigInteger absX = x.abs();
/*  91 */     int exponent = absX.bitLength() - 1;
/*     */     
/*  93 */     if (exponent < 63)
/*  94 */       return x.longValue(); 
/*  95 */     if (exponent > 1023) {
/*  96 */       return x.signum() * Double.POSITIVE_INFINITY;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     int shift = exponent - 52 - 1;
/* 108 */     long twiceSignifFloor = absX.shiftRight(shift).longValue();
/* 109 */     long signifFloor = twiceSignifFloor >> 1L;
/* 110 */     signifFloor &= 0xFFFFFFFFFFFFFL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     boolean increment = ((twiceSignifFloor & 0x1L) != 0L && ((signifFloor & 0x1L) != 0L || absX.getLowestSetBit() < shift));
/* 119 */     long signifRounded = increment ? (signifFloor + 1L) : signifFloor;
/* 120 */     long bits = (exponent + 1023) << 52L;
/* 121 */     bits += signifRounded;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     bits |= x.signum() & Long.MIN_VALUE;
/* 129 */     return Double.longBitsToDouble(bits);
/*     */   }
/*     */ 
/*     */   
/*     */   static double ensureNonNegative(double value) {
/* 134 */     Preconditions.checkArgument(!Double.isNaN(value));
/* 135 */     return Math.max(value, 0.0D);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/DoubleUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */