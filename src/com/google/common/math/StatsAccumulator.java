/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.DoubleStream;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class StatsAccumulator
/*     */ {
/*  45 */   private long count = 0L;
/*  46 */   private double mean = 0.0D;
/*  47 */   private double sumOfSquaresOfDeltas = 0.0D;
/*  48 */   private double min = Double.NaN;
/*  49 */   private double max = Double.NaN;
/*     */ 
/*     */   
/*     */   public void add(double value) {
/*  53 */     if (this.count == 0L) {
/*  54 */       this.count = 1L;
/*  55 */       this.mean = value;
/*  56 */       this.min = value;
/*  57 */       this.max = value;
/*  58 */       if (!Doubles.isFinite(value)) {
/*  59 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       }
/*     */     } else {
/*  62 */       this.count++;
/*  63 */       if (Doubles.isFinite(value) && Doubles.isFinite(this.mean)) {
/*     */         
/*  65 */         double delta = value - this.mean;
/*  66 */         this.mean += delta / this.count;
/*  67 */         this.sumOfSquaresOfDeltas += delta * (value - this.mean);
/*     */       } else {
/*  69 */         this.mean = calculateNewMeanNonFinite(this.mean, value);
/*  70 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/*  72 */       this.min = Math.min(this.min, value);
/*  73 */       this.max = Math.max(this.max, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterable<? extends Number> values) {
/*  84 */     for (Number value : values) {
/*  85 */       add(value.doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterator<? extends Number> values) {
/*  96 */     while (values.hasNext()) {
/*  97 */       add(((Number)values.next()).doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(double... values) {
/* 107 */     for (double value : values) {
/* 108 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(int... values) {
/* 118 */     for (int value : values) {
/* 119 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(long... values) {
/* 130 */     for (long value : values) {
/* 131 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(DoubleStream values) {
/* 142 */     addAll(values.<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(IntStream values) {
/* 152 */     addAll(values.<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(LongStream values) {
/* 163 */     addAll(values.<StatsAccumulator>collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Stats values) {
/* 171 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/* 174 */     merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(StatsAccumulator values) {
/* 184 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/* 187 */     merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void merge(long otherCount, double otherMean, double otherSumOfSquaresOfDeltas, double otherMin, double otherMax) {
/* 196 */     if (this.count == 0L) {
/* 197 */       this.count = otherCount;
/* 198 */       this.mean = otherMean;
/* 199 */       this.sumOfSquaresOfDeltas = otherSumOfSquaresOfDeltas;
/* 200 */       this.min = otherMin;
/* 201 */       this.max = otherMax;
/*     */     } else {
/* 203 */       this.count += otherCount;
/* 204 */       if (Doubles.isFinite(this.mean) && Doubles.isFinite(otherMean)) {
/*     */         
/* 206 */         double delta = otherMean - this.mean;
/* 207 */         this.mean += delta * otherCount / this.count;
/* 208 */         this.sumOfSquaresOfDeltas += otherSumOfSquaresOfDeltas + delta * (otherMean - this.mean) * otherCount;
/*     */       } else {
/* 210 */         this.mean = calculateNewMeanNonFinite(this.mean, otherMean);
/* 211 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/* 213 */       this.min = Math.min(this.min, otherMin);
/* 214 */       this.max = Math.max(this.max, otherMax);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats snapshot() {
/* 220 */     return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/* 225 */     return this.count;
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
/*     */   public double mean() {
/* 247 */     Preconditions.checkState((this.count != 0L));
/* 248 */     return this.mean;
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
/*     */   public final double sum() {
/* 264 */     return this.mean * this.count;
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
/*     */   public final double populationVariance() {
/* 283 */     Preconditions.checkState((this.count != 0L));
/* 284 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 285 */       return Double.NaN;
/*     */     }
/* 287 */     if (this.count == 1L) {
/* 288 */       return 0.0D;
/*     */     }
/* 290 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count;
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
/*     */   public final double populationStandardDeviation() {
/* 310 */     return Math.sqrt(populationVariance());
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
/*     */   public final double sampleVariance() {
/* 330 */     Preconditions.checkState((this.count > 1L));
/* 331 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 332 */       return Double.NaN;
/*     */     }
/* 334 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public final double sampleStandardDeviation() {
/* 356 */     return Math.sqrt(sampleVariance());
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
/* 373 */     Preconditions.checkState((this.count != 0L));
/* 374 */     return this.min;
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
/* 391 */     Preconditions.checkState((this.count != 0L));
/* 392 */     return this.max;
/*     */   }
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 396 */     return this.sumOfSquaresOfDeltas;
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
/*     */   static double calculateNewMeanNonFinite(double previousMean, double value) {
/* 418 */     if (Doubles.isFinite(previousMean))
/*     */     {
/* 420 */       return value; } 
/* 421 */     if (Doubles.isFinite(value) || previousMean == value)
/*     */     {
/* 423 */       return previousMean;
/*     */     }
/*     */     
/* 426 */     return Double.NaN;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/StatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */