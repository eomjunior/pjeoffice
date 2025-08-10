/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
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
/*     */ public abstract class DiscreteDomain<C extends Comparable>
/*     */ {
/*     */   final boolean supportsFastOffset;
/*     */   
/*     */   public static DiscreteDomain<Integer> integers() {
/*  59 */     return IntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
/*  63 */     private static final IntegerDomain INSTANCE = new IntegerDomain();
/*     */     
/*     */     IntegerDomain() {
/*  66 */       super(true);
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     @CheckForNull
/*     */     public Integer next(Integer value) {
/*  72 */       int i = value.intValue();
/*  73 */       return (i == Integer.MAX_VALUE) ? null : Integer.valueOf(i + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Integer previous(Integer value) {
/*  79 */       int i = value.intValue();
/*  80 */       return (i == Integer.MIN_VALUE) ? null : Integer.valueOf(i - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     Integer offset(Integer origin, long distance) {
/*  85 */       CollectPreconditions.checkNonnegative(distance, "distance");
/*  86 */       return Integer.valueOf(Ints.checkedCast(origin.longValue() + distance));
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(Integer start, Integer end) {
/*  91 */       return end.intValue() - start.intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer minValue() {
/*  96 */       return Integer.valueOf(-2147483648);
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer maxValue() {
/* 101 */       return Integer.valueOf(2147483647);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 105 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 110 */       return "DiscreteDomain.integers()";
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
/*     */ 
/*     */   
/*     */   public static DiscreteDomain<Long> longs() {
/* 125 */     return LongDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
/* 129 */     private static final LongDomain INSTANCE = new LongDomain(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongDomain() {
/* 132 */       super(true);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Long next(Long value) {
/* 138 */       long l = value.longValue();
/* 139 */       return (l == Long.MAX_VALUE) ? null : Long.valueOf(l + 1L);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Long previous(Long value) {
/* 145 */       long l = value.longValue();
/* 146 */       return (l == Long.MIN_VALUE) ? null : Long.valueOf(l - 1L);
/*     */     }
/*     */ 
/*     */     
/*     */     Long offset(Long origin, long distance) {
/* 151 */       CollectPreconditions.checkNonnegative(distance, "distance");
/* 152 */       long result = origin.longValue() + distance;
/* 153 */       if (result < 0L) {
/* 154 */         Preconditions.checkArgument((origin.longValue() < 0L), "overflow");
/*     */       }
/* 156 */       return Long.valueOf(result);
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(Long start, Long end) {
/* 161 */       long result = end.longValue() - start.longValue();
/* 162 */       if (end.longValue() > start.longValue() && result < 0L) {
/* 163 */         return Long.MAX_VALUE;
/*     */       }
/* 165 */       if (end.longValue() < start.longValue() && result > 0L) {
/* 166 */         return Long.MIN_VALUE;
/*     */       }
/* 168 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long minValue() {
/* 173 */       return Long.valueOf(Long.MIN_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public Long maxValue() {
/* 178 */       return Long.valueOf(Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 182 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 187 */       return "DiscreteDomain.longs()";
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
/*     */ 
/*     */   
/*     */   public static DiscreteDomain<BigInteger> bigIntegers() {
/* 202 */     return BigIntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BigIntegerDomain
/*     */     extends DiscreteDomain<BigInteger> implements Serializable {
/* 207 */     private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
/*     */     
/*     */     BigIntegerDomain() {
/* 210 */       super(true);
/*     */     }
/*     */     
/* 213 */     private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
/* 214 */     private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public BigInteger next(BigInteger value) {
/* 218 */       return value.add(BigInteger.ONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public BigInteger previous(BigInteger value) {
/* 223 */       return value.subtract(BigInteger.ONE);
/*     */     }
/*     */ 
/*     */     
/*     */     BigInteger offset(BigInteger origin, long distance) {
/* 228 */       CollectPreconditions.checkNonnegative(distance, "distance");
/* 229 */       return origin.add(BigInteger.valueOf(distance));
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(BigInteger start, BigInteger end) {
/* 234 */       return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 238 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 243 */       return "DiscreteDomain.bigIntegers()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DiscreteDomain() {
/* 253 */     this(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private DiscreteDomain(boolean supportsFastOffset) {
/* 258 */     this.supportsFastOffset = supportsFastOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   C offset(C origin, long distance) {
/* 266 */     C current = origin;
/* 267 */     CollectPreconditions.checkNonnegative(distance, "distance"); long i;
/* 268 */     for (i = 0L; i < distance; i++) {
/* 269 */       current = next(current);
/* 270 */       if (current == null) {
/* 271 */         throw new IllegalArgumentException("overflowed computing offset(" + origin + ", " + distance + ")");
/*     */       }
/*     */     } 
/*     */     
/* 275 */     return current;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public C minValue() {
/* 326 */     throw new NoSuchElementException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public C maxValue() {
/* 342 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   public abstract C next(C paramC);
/*     */   
/*     */   @CheckForNull
/*     */   public abstract C previous(C paramC);
/*     */   
/*     */   public abstract long distance(C paramC1, C paramC2);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/DiscreteDomain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */