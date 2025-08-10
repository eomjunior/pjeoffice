/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public final class UnsignedLong
/*     */   extends Number
/*     */   implements Comparable<UnsignedLong>, Serializable
/*     */ {
/*     */   private static final long UNSIGNED_MASK = 9223372036854775807L;
/*  46 */   public static final UnsignedLong ZERO = new UnsignedLong(0L);
/*  47 */   public static final UnsignedLong ONE = new UnsignedLong(1L);
/*  48 */   public static final UnsignedLong MAX_VALUE = new UnsignedLong(-1L);
/*     */   
/*     */   private final long value;
/*     */   
/*     */   private UnsignedLong(long value) {
/*  53 */     this.value = value;
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
/*     */   public static UnsignedLong fromLongBits(long bits) {
/*  71 */     return new UnsignedLong(bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(long value) {
/*  82 */     Preconditions.checkArgument((value >= 0L), "value (%s) is outside the range for an unsigned long value", value);
/*  83 */     return fromLongBits(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(BigInteger value) {
/*  94 */     Preconditions.checkNotNull(value);
/*  95 */     Preconditions.checkArgument((value
/*  96 */         .signum() >= 0 && value.bitLength() <= 64), "value (%s) is outside the range for an unsigned long value", value);
/*     */ 
/*     */     
/*  99 */     return fromLongBits(value.longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(String string) {
/* 111 */     return valueOf(string, 10);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static UnsignedLong valueOf(String string, int radix) {
/* 124 */     return fromLongBits(UnsignedLongs.parseUnsignedLong(string, radix));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong plus(UnsignedLong val) {
/* 134 */     return fromLongBits(this.value + ((UnsignedLong)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong minus(UnsignedLong val) {
/* 144 */     return fromLongBits(this.value - ((UnsignedLong)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong times(UnsignedLong val) {
/* 154 */     return fromLongBits(this.value * ((UnsignedLong)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong dividedBy(UnsignedLong val) {
/* 163 */     return fromLongBits(UnsignedLongs.divide(this.value, ((UnsignedLong)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsignedLong mod(UnsignedLong val) {
/* 172 */     return fromLongBits(UnsignedLongs.remainder(this.value, ((UnsignedLong)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 178 */     return (int)this.value;
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
/*     */   public long longValue() {
/* 190 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 199 */     if (this.value >= 0L) {
/* 200 */       return (float)this.value;
/*     */     }
/*     */ 
/*     */     
/* 204 */     return (float)(this.value >>> 1L | this.value & 0x1L) * 2.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 213 */     if (this.value >= 0L) {
/* 214 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     return (this.value >>> 1L | this.value & 0x1L) * 2.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/* 226 */     BigInteger bigInt = BigInteger.valueOf(this.value & Long.MAX_VALUE);
/* 227 */     if (this.value < 0L) {
/* 228 */       bigInt = bigInt.setBit(63);
/*     */     }
/* 230 */     return bigInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(UnsignedLong o) {
/* 235 */     Preconditions.checkNotNull(o);
/* 236 */     return UnsignedLongs.compare(this.value, o.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 241 */     return Longs.hashCode(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 246 */     if (obj instanceof UnsignedLong) {
/* 247 */       UnsignedLong other = (UnsignedLong)obj;
/* 248 */       return (this.value == other.value);
/*     */     } 
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 256 */     return UnsignedLongs.toString(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int radix) {
/* 265 */     return UnsignedLongs.toString(this.value, radix);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/UnsignedLong.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */