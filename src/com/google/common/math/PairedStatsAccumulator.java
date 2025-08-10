/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class PairedStatsAccumulator
/*     */ {
/*  40 */   private final StatsAccumulator xStats = new StatsAccumulator();
/*  41 */   private final StatsAccumulator yStats = new StatsAccumulator();
/*  42 */   private double sumOfProductsOfDeltas = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(double x, double y) {
/*  57 */     this.xStats.add(x);
/*  58 */     if (Doubles.isFinite(x) && Doubles.isFinite(y)) {
/*  59 */       if (this.xStats.count() > 1L) {
/*  60 */         this.sumOfProductsOfDeltas += (x - this.xStats.mean()) * (y - this.yStats.mean());
/*     */       }
/*     */     } else {
/*  63 */       this.sumOfProductsOfDeltas = Double.NaN;
/*     */     } 
/*  65 */     this.yStats.add(y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(PairedStats values) {
/*  73 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     this.xStats.addAll(values.xStats());
/*  78 */     if (this.yStats.count() == 0L) {
/*  79 */       this.sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  84 */       this.sumOfProductsOfDeltas += values
/*  85 */         .sumOfProductsOfDeltas() + (values
/*  86 */         .xStats().mean() - this.xStats.mean()) * (values
/*  87 */         .yStats().mean() - this.yStats.mean()) * values
/*  88 */         .count();
/*     */     } 
/*  90 */     this.yStats.addAll(values.yStats());
/*     */   }
/*     */ 
/*     */   
/*     */   public PairedStats snapshot() {
/*  95 */     return new PairedStats(this.xStats.snapshot(), this.yStats.snapshot(), this.sumOfProductsOfDeltas);
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/* 100 */     return this.xStats.count();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats xStats() {
/* 105 */     return this.xStats.snapshot();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats yStats() {
/* 110 */     return this.yStats.snapshot();
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
/*     */   public double populationCovariance() {
/* 128 */     Preconditions.checkState((count() != 0L));
/* 129 */     return this.sumOfProductsOfDeltas / count();
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
/*     */   public final double sampleCovariance() {
/* 146 */     Preconditions.checkState((count() > 1L));
/* 147 */     return this.sumOfProductsOfDeltas / (count() - 1L);
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
/*     */   public final double pearsonsCorrelationCoefficient() {
/* 167 */     Preconditions.checkState((count() > 1L));
/* 168 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 169 */       return Double.NaN;
/*     */     }
/* 171 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 172 */     double ySumOfSquaresOfDeltas = this.yStats.sumOfSquaresOfDeltas();
/* 173 */     Preconditions.checkState((xSumOfSquaresOfDeltas > 0.0D));
/* 174 */     Preconditions.checkState((ySumOfSquaresOfDeltas > 0.0D));
/*     */ 
/*     */ 
/*     */     
/* 178 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 179 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
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
/*     */   public final LinearTransformation leastSquaresFit() {
/* 214 */     Preconditions.checkState((count() > 1L));
/* 215 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 216 */       return LinearTransformation.forNaN();
/*     */     }
/* 218 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 219 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 220 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 221 */         return LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean())
/* 222 */           .withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 224 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     } 
/*     */     
/* 227 */     Preconditions.checkState((this.yStats.sumOfSquaresOfDeltas() > 0.0D));
/* 228 */     return LinearTransformation.vertical(this.xStats.mean());
/*     */   }
/*     */ 
/*     */   
/*     */   private double ensurePositive(double value) {
/* 233 */     if (value > 0.0D) {
/* 234 */       return value;
/*     */     }
/* 236 */     return Double.MIN_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double ensureInUnitRange(double value) {
/* 241 */     return Doubles.constrainToRange(value, -1.0D, 1.0D);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/PairedStatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */