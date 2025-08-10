/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class UnsignedInteger
/*     */   extends Number
/*     */   implements Comparable<UnsignedInteger>
/*     */ {
/*  45 */   public static final UnsignedInteger ZERO = fromIntBits(0);
/*  46 */   public static final UnsignedInteger ONE = fromIntBits(1);
/*  47 */   public static final UnsignedInteger MAX_VALUE = fromIntBits(-1);
/*     */   
/*     */   private final int value;
/*     */ 
/*     */   
/*     */   private UnsignedInteger(int value) {
/*  53 */     this.value = value & 0xFFFFFFFF;
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
/*     */   public static UnsignedInteger fromIntBits(int bits) {
/*  69 */     return new UnsignedInteger(bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(long value) {
/*  77 */     Preconditions.checkArgument(((value & 0xFFFFFFFFL) == value), "value (%s) is outside the range for an unsigned integer value", value);
/*     */ 
/*     */ 
/*     */     
/*  81 */     return fromIntBits((int)value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(BigInteger value) {
/*  91 */     Preconditions.checkNotNull(value);
/*  92 */     Preconditions.checkArgument((value
/*  93 */         .signum() >= 0 && value.bitLength() <= 32), "value (%s) is outside the range for an unsigned integer value", value);
/*     */ 
/*     */     
/*  96 */     return fromIntBits(value.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(String string) {
/* 107 */     return valueOf(string, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(String string, int radix) {
/* 118 */     return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger plus(UnsignedInteger val) {
/* 128 */     return fromIntBits(this.value + ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger minus(UnsignedInteger val) {
/* 138 */     return fromIntBits(this.value - ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
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
/*     */   public UnsignedInteger times(UnsignedInteger val) {
/* 151 */     return fromIntBits(this.value * ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger dividedBy(UnsignedInteger val) {
/* 161 */     return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedInteger mod(UnsignedInteger val) {
/* 171 */     return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
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
/*     */   public int intValue() {
/* 183 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 189 */     return UnsignedInts.toLong(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 198 */     return (float)longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 207 */     return longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/* 212 */     return BigInteger.valueOf(longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(UnsignedInteger other) {
/* 222 */     Preconditions.checkNotNull(other);
/* 223 */     return UnsignedInts.compare(this.value, other.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 228 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 233 */     if (obj instanceof UnsignedInteger) {
/* 234 */       UnsignedInteger other = (UnsignedInteger)obj;
/* 235 */       return (this.value == other.value);
/*     */     } 
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 243 */     return toString(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int radix) {
/* 252 */     return UnsignedInts.toString(this.value, radix);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/UnsignedInteger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */