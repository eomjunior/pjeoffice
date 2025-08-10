/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.SignedBytes;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.stream.Collector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Beta
/*     */ public final class BloomFilter<T>
/*     */   implements Predicate<T>, Serializable
/*     */ {
/*     */   private final BloomFilterStrategies.LockFreeBitArray bits;
/*     */   private final int numHashFunctions;
/*     */   private final Funnel<? super T> funnel;
/*     */   private final Strategy strategy;
/*     */   private static final long serialVersionUID = -889275714L;
/*     */   
/*     */   private BloomFilter(BloomFilterStrategies.LockFreeBitArray bits, int numHashFunctions, Funnel<? super T> funnel, Strategy strategy) {
/* 126 */     Preconditions.checkArgument((numHashFunctions > 0), "numHashFunctions (%s) must be > 0", numHashFunctions);
/* 127 */     Preconditions.checkArgument((numHashFunctions <= 255), "numHashFunctions (%s) must be <= 255", numHashFunctions);
/*     */     
/* 129 */     this.bits = (BloomFilterStrategies.LockFreeBitArray)Preconditions.checkNotNull(bits);
/* 130 */     this.numHashFunctions = numHashFunctions;
/* 131 */     this.funnel = (Funnel<? super T>)Preconditions.checkNotNull(funnel);
/* 132 */     this.strategy = (Strategy)Preconditions.checkNotNull(strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BloomFilter<T> copy() {
/* 142 */     return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mightContain(@ParametricNullness T object) {
/* 150 */     return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(@ParametricNullness T input) {
/* 160 */     return mightContain(input);
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
/*     */   public boolean put(@ParametricNullness T object) {
/* 176 */     return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits);
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
/*     */   public double expectedFpp() {
/* 191 */     return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long approximateElementCount() {
/* 202 */     long bitSize = this.bits.bitSize();
/* 203 */     long bitCount = this.bits.bitCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     double fractionOfBitsSet = bitCount / bitSize;
/* 212 */     return DoubleMath.roundToLong(
/* 213 */         -Math.log1p(-fractionOfBitsSet) * bitSize / this.numHashFunctions, RoundingMode.HALF_UP);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   long bitSize() {
/* 219 */     return this.bits.bitSize();
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
/*     */   public boolean isCompatible(BloomFilter<T> that) {
/* 238 */     Preconditions.checkNotNull(that);
/* 239 */     return (this != that && this.numHashFunctions == that.numHashFunctions && 
/*     */       
/* 241 */       bitSize() == that.bitSize() && this.strategy
/* 242 */       .equals(that.strategy) && this.funnel
/* 243 */       .equals(that.funnel));
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
/*     */   public void putAll(BloomFilter<T> that) {
/* 256 */     Preconditions.checkNotNull(that);
/* 257 */     Preconditions.checkArgument((this != that), "Cannot combine a BloomFilter with itself.");
/* 258 */     Preconditions.checkArgument((this.numHashFunctions == that.numHashFunctions), "BloomFilters must have the same number of hash functions (%s != %s)", this.numHashFunctions, that.numHashFunctions);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 263 */     Preconditions.checkArgument(
/* 264 */         (bitSize() == that.bitSize()), "BloomFilters must have the same size underlying bit arrays (%s != %s)", 
/*     */         
/* 266 */         bitSize(), that
/* 267 */         .bitSize());
/* 268 */     Preconditions.checkArgument(this.strategy
/* 269 */         .equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, that.strategy);
/*     */ 
/*     */ 
/*     */     
/* 273 */     Preconditions.checkArgument(this.funnel
/* 274 */         .equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, that.funnel);
/*     */ 
/*     */ 
/*     */     
/* 278 */     this.bits.putAll(that.bits);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 283 */     if (object == this) {
/* 284 */       return true;
/*     */     }
/* 286 */     if (object instanceof BloomFilter) {
/* 287 */       BloomFilter<?> that = (BloomFilter)object;
/* 288 */       return (this.numHashFunctions == that.numHashFunctions && this.funnel
/* 289 */         .equals(that.funnel) && this.bits
/* 290 */         .equals(that.bits) && this.strategy
/* 291 */         .equals(that.strategy));
/*     */     } 
/* 293 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 298 */     return Objects.hashCode(new Object[] { Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits });
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
/*     */   public static <T> Collector<T, ?, BloomFilter<T>> toBloomFilter(Funnel<? super T> funnel, long expectedInsertions) {
/* 324 */     return toBloomFilter(funnel, expectedInsertions, 0.03D);
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
/*     */   public static <T> Collector<T, ?, BloomFilter<T>> toBloomFilter(Funnel<? super T> funnel, long expectedInsertions, double fpp) {
/* 351 */     Preconditions.checkNotNull(funnel);
/* 352 */     Preconditions.checkArgument((expectedInsertions >= 0L), "Expected insertions (%s) must be >= 0", expectedInsertions);
/*     */     
/* 354 */     Preconditions.checkArgument((fpp > 0.0D), "False positive probability (%s) must be > 0.0", Double.valueOf(fpp));
/* 355 */     Preconditions.checkArgument((fpp < 1.0D), "False positive probability (%s) must be < 1.0", Double.valueOf(fpp));
/* 356 */     return (Collector)Collector.of(() -> create(funnel, expectedInsertions, fpp), BloomFilter::put, (bf1, bf2) -> { bf1.putAll(bf2); return bf1; }new Collector.Characteristics[] { Collector.Characteristics.UNORDERED, Collector.Characteristics.CONCURRENT });
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions, double fpp) {
/* 389 */     return create(funnel, expectedInsertions, fpp);
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp) {
/* 415 */     return create(funnel, expectedInsertions, fpp, BloomFilterStrategies.MURMUR128_MITZ_64);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy) {
/* 421 */     Preconditions.checkNotNull(funnel);
/* 422 */     Preconditions.checkArgument((expectedInsertions >= 0L), "Expected insertions (%s) must be >= 0", expectedInsertions);
/*     */     
/* 424 */     Preconditions.checkArgument((fpp > 0.0D), "False positive probability (%s) must be > 0.0", Double.valueOf(fpp));
/* 425 */     Preconditions.checkArgument((fpp < 1.0D), "False positive probability (%s) must be < 1.0", Double.valueOf(fpp));
/* 426 */     Preconditions.checkNotNull(strategy);
/*     */     
/* 428 */     if (expectedInsertions == 0L) {
/* 429 */       expectedInsertions = 1L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 436 */     long numBits = optimalNumOfBits(expectedInsertions, fpp);
/* 437 */     int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
/*     */     try {
/* 439 */       return new BloomFilter<>(new BloomFilterStrategies.LockFreeBitArray(numBits), numHashFunctions, funnel, strategy);
/* 440 */     } catch (IllegalArgumentException e) {
/* 441 */       throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions) {
/* 466 */     return create(funnel, expectedInsertions);
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions) {
/* 491 */     return create(funnel, expectedInsertions, 0.03D);
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
/*     */   @VisibleForTesting
/*     */   static int optimalNumOfHashFunctions(long n, long m) {
/* 518 */     return Math.max(1, (int)Math.round(m / n * Math.log(2.0D)));
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
/*     */   @VisibleForTesting
/*     */   static long optimalNumOfBits(long n, double p) {
/* 533 */     if (p == 0.0D) {
/* 534 */       p = Double.MIN_VALUE;
/*     */     }
/* 536 */     return (long)(-n * Math.log(p) / Math.log(2.0D) * Math.log(2.0D));
/*     */   }
/*     */   
/*     */   private Object writeReplace() {
/* 540 */     return new SerialForm<>(this);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 544 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   private static class SerialForm<T> implements Serializable { final long[] data;
/*     */     final int numHashFunctions;
/*     */     final Funnel<? super T> funnel;
/*     */     final BloomFilter.Strategy strategy;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     SerialForm(BloomFilter<T> bf) {
/* 554 */       this.data = BloomFilterStrategies.LockFreeBitArray.toPlainArray(bf.bits.data);
/* 555 */       this.numHashFunctions = bf.numHashFunctions;
/* 556 */       this.funnel = bf.funnel;
/* 557 */       this.strategy = bf.strategy;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 561 */       return new BloomFilter(new BloomFilterStrategies.LockFreeBitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 580 */     DataOutputStream dout = new DataOutputStream(out);
/* 581 */     dout.writeByte(SignedBytes.checkedCast(this.strategy.ordinal()));
/* 582 */     dout.writeByte(UnsignedBytes.checkedCast(this.numHashFunctions));
/* 583 */     dout.writeInt(this.bits.data.length());
/* 584 */     for (int i = 0; i < this.bits.data.length(); i++) {
/* 585 */       dout.writeLong(this.bits.data.get(i));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BloomFilter<T> readFrom(InputStream in, Funnel<? super T> funnel) throws IOException {
/* 603 */     Preconditions.checkNotNull(in, "InputStream");
/* 604 */     Preconditions.checkNotNull(funnel, "Funnel");
/* 605 */     int strategyOrdinal = -1;
/* 606 */     int numHashFunctions = -1;
/* 607 */     int dataLength = -1;
/*     */     try {
/* 609 */       DataInputStream din = new DataInputStream(in);
/*     */ 
/*     */ 
/*     */       
/* 613 */       strategyOrdinal = din.readByte();
/* 614 */       numHashFunctions = UnsignedBytes.toInt(din.readByte());
/* 615 */       dataLength = din.readInt();
/*     */       
/* 617 */       Strategy strategy = BloomFilterStrategies.values()[strategyOrdinal];
/*     */       
/* 619 */       BloomFilterStrategies.LockFreeBitArray dataArray = new BloomFilterStrategies.LockFreeBitArray(LongMath.checkedMultiply(dataLength, 64L));
/* 620 */       for (int i = 0; i < dataLength; i++) {
/* 621 */         dataArray.putData(i, din.readLong());
/*     */       }
/*     */       
/* 624 */       return new BloomFilter<>(dataArray, numHashFunctions, funnel, strategy);
/* 625 */     } catch (IOException e) {
/* 626 */       throw e;
/* 627 */     } catch (Exception e) {
/* 628 */       String message = "Unable to deserialize BloomFilter from InputStream. strategyOrdinal: " + strategyOrdinal + " numHashFunctions: " + numHashFunctions + " dataLength: " + dataLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 636 */       throw new IOException(message, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface Strategy extends Serializable {
/*     */     <T> boolean put(@ParametricNullness T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.LockFreeBitArray param1LockFreeBitArray);
/*     */     
/*     */     <T> boolean mightContain(@ParametricNullness T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.LockFreeBitArray param1LockFreeBitArray);
/*     */     
/*     */     int ordinal();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/BloomFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */