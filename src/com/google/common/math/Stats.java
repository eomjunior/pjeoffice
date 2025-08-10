/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.DoubleStream;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class Stats
/*     */   implements Serializable
/*     */ {
/*     */   private final long count;
/*     */   private final double mean;
/*     */   private final double sumOfSquaresOfDeltas;
/*     */   private final double min;
/*     */   private final double max;
/*     */   static final int BYTES = 40;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Stats(long count, double mean, double sumOfSquaresOfDeltas, double min, double max) {
/*  91 */     this.count = count;
/*  92 */     this.mean = mean;
/*  93 */     this.sumOfSquaresOfDeltas = sumOfSquaresOfDeltas;
/*  94 */     this.min = min;
/*  95 */     this.max = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterable<? extends Number> values) {
/* 105 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 106 */     accumulator.addAll(values);
/* 107 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterator<? extends Number> values) {
/* 118 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 119 */     accumulator.addAll(values);
/* 120 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(double... values) {
/* 129 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 130 */     accumulator.addAll(values);
/* 131 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(int... values) {
/* 140 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 141 */     accumulator.addAll(values);
/* 142 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(long... values) {
/* 152 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 153 */     accumulator.addAll(values);
/* 154 */     return accumulator.snapshot();
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
/*     */   public static Stats of(DoubleStream values) {
/* 168 */     return ((StatsAccumulator)values
/* 169 */       .<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll))
/* 170 */       .snapshot();
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
/*     */   public static Stats of(IntStream values) {
/* 184 */     return ((StatsAccumulator)values
/* 185 */       .<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll))
/* 186 */       .snapshot();
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
/*     */   public static Stats of(LongStream values) {
/* 201 */     return ((StatsAccumulator)values
/* 202 */       .<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll))
/* 203 */       .snapshot();
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
/*     */   public static Collector<Number, StatsAccumulator, Stats> toStats() {
/* 218 */     return Collector.of(StatsAccumulator::new, (a, x) -> a.add(x.doubleValue()), (l, r) -> { l.addAll(r); return l; }StatsAccumulator::snapshot, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   public long count() {
/* 231 */     return this.count;
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
/*     */   public double mean() {
/* 256 */     Preconditions.checkState((this.count != 0L));
/* 257 */     return this.mean;
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
/*     */   public double sum() {
/* 273 */     return this.mean * this.count;
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
/*     */   public double populationVariance() {
/* 292 */     Preconditions.checkState((this.count > 0L));
/* 293 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 294 */       return Double.NaN;
/*     */     }
/* 296 */     if (this.count == 1L) {
/* 297 */       return 0.0D;
/*     */     }
/* 299 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / count();
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
/*     */   public double populationStandardDeviation() {
/* 319 */     return Math.sqrt(populationVariance());
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
/*     */   public double sampleVariance() {
/* 339 */     Preconditions.checkState((this.count > 1L));
/* 340 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 341 */       return Double.NaN;
/*     */     }
/* 343 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public double sampleStandardDeviation() {
/* 365 */     return Math.sqrt(sampleVariance());
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
/*     */   public double min() {
/* 382 */     Preconditions.checkState((this.count != 0L));
/* 383 */     return this.min;
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
/*     */   public double max() {
/* 400 */     Preconditions.checkState((this.count != 0L));
/* 401 */     return this.max;
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
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 421 */     if (obj == null) {
/* 422 */       return false;
/*     */     }
/* 424 */     if (getClass() != obj.getClass()) {
/* 425 */       return false;
/*     */     }
/* 427 */     Stats other = (Stats)obj;
/* 428 */     return (this.count == other.count && 
/* 429 */       Double.doubleToLongBits(this.mean) == Double.doubleToLongBits(other.mean) && 
/* 430 */       Double.doubleToLongBits(this.sumOfSquaresOfDeltas) == Double.doubleToLongBits(other.sumOfSquaresOfDeltas) && 
/* 431 */       Double.doubleToLongBits(this.min) == Double.doubleToLongBits(other.min) && 
/* 432 */       Double.doubleToLongBits(this.max) == Double.doubleToLongBits(other.max));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 443 */     return Objects.hashCode(new Object[] { Long.valueOf(this.count), Double.valueOf(this.mean), Double.valueOf(this.sumOfSquaresOfDeltas), Double.valueOf(this.min), Double.valueOf(this.max) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 448 */     if (count() > 0L) {
/* 449 */       return MoreObjects.toStringHelper(this)
/* 450 */         .add("count", this.count)
/* 451 */         .add("mean", this.mean)
/* 452 */         .add("populationStandardDeviation", populationStandardDeviation())
/* 453 */         .add("min", this.min)
/* 454 */         .add("max", this.max)
/* 455 */         .toString();
/*     */     }
/* 457 */     return MoreObjects.toStringHelper(this).add("count", this.count).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 462 */     return this.sumOfSquaresOfDeltas;
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
/*     */   public static double meanOf(Iterable<? extends Number> values) {
/* 476 */     return meanOf(values.iterator());
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
/*     */   public static double meanOf(Iterator<? extends Number> values) {
/* 490 */     Preconditions.checkArgument(values.hasNext());
/* 491 */     long count = 1L;
/* 492 */     double mean = ((Number)values.next()).doubleValue();
/* 493 */     while (values.hasNext()) {
/* 494 */       double value = ((Number)values.next()).doubleValue();
/* 495 */       count++;
/* 496 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 498 */         mean += (value - mean) / count; continue;
/*     */       } 
/* 500 */       mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */     } 
/*     */     
/* 503 */     return mean;
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
/*     */   public static double meanOf(double... values) {
/* 516 */     Preconditions.checkArgument((values.length > 0));
/* 517 */     double mean = values[0];
/* 518 */     for (int index = 1; index < values.length; index++) {
/* 519 */       double value = values[index];
/* 520 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 522 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 524 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 527 */     return mean;
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
/*     */   public static double meanOf(int... values) {
/* 540 */     Preconditions.checkArgument((values.length > 0));
/* 541 */     double mean = values[0];
/* 542 */     for (int index = 1; index < values.length; index++) {
/* 543 */       double value = values[index];
/* 544 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 546 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 548 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 551 */     return mean;
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
/*     */   public static double meanOf(long... values) {
/* 565 */     Preconditions.checkArgument((values.length > 0));
/* 566 */     double mean = values[0];
/* 567 */     for (int index = 1; index < values.length; index++) {
/* 568 */       double value = values[index];
/* 569 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 571 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 573 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 576 */     return mean;
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
/*     */   public byte[] toByteArray() {
/* 591 */     ByteBuffer buff = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
/* 592 */     writeTo(buff);
/* 593 */     return buff.array();
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
/*     */   void writeTo(ByteBuffer buffer) {
/* 607 */     Preconditions.checkNotNull(buffer);
/* 608 */     Preconditions.checkArgument(
/* 609 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 612 */         .remaining());
/* 613 */     buffer
/* 614 */       .putLong(this.count)
/* 615 */       .putDouble(this.mean)
/* 616 */       .putDouble(this.sumOfSquaresOfDeltas)
/* 617 */       .putDouble(this.min)
/* 618 */       .putDouble(this.max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats fromByteArray(byte[] byteArray) {
/* 629 */     Preconditions.checkNotNull(byteArray);
/* 630 */     Preconditions.checkArgument((byteArray.length == 40), "Expected Stats.BYTES = %s remaining , got %s", 40, byteArray.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 635 */     return readFrom(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN));
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
/*     */   static Stats readFrom(ByteBuffer buffer) {
/* 649 */     Preconditions.checkNotNull(buffer);
/* 650 */     Preconditions.checkArgument(
/* 651 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 654 */         .remaining());
/* 655 */     return new Stats(buffer
/* 656 */         .getLong(), buffer
/* 657 */         .getDouble(), buffer
/* 658 */         .getDouble(), buffer
/* 659 */         .getDouble(), buffer
/* 660 */         .getDouble());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/Stats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */