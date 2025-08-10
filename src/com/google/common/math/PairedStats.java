/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class PairedStats
/*     */   implements Serializable
/*     */ {
/*     */   private final Stats xStats;
/*     */   private final Stats yStats;
/*     */   private final double sumOfProductsOfDeltas;
/*     */   private static final int BYTES = 88;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   PairedStats(Stats xStats, Stats yStats, double sumOfProductsOfDeltas) {
/*  62 */     this.xStats = xStats;
/*  63 */     this.yStats = yStats;
/*  64 */     this.sumOfProductsOfDeltas = sumOfProductsOfDeltas;
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/*  69 */     return this.xStats.count();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats xStats() {
/*  74 */     return this.xStats;
/*     */   }
/*     */ 
/*     */   
/*     */   public Stats yStats() {
/*  79 */     return this.yStats;
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
/*  97 */     Preconditions.checkState((count() != 0L));
/*  98 */     return this.sumOfProductsOfDeltas / count();
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
/*     */   public double sampleCovariance() {
/* 115 */     Preconditions.checkState((count() > 1L));
/* 116 */     return this.sumOfProductsOfDeltas / (count() - 1L);
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
/*     */   public double pearsonsCorrelationCoefficient() {
/* 136 */     Preconditions.checkState((count() > 1L));
/* 137 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 138 */       return Double.NaN;
/*     */     }
/* 140 */     double xSumOfSquaresOfDeltas = xStats().sumOfSquaresOfDeltas();
/* 141 */     double ySumOfSquaresOfDeltas = yStats().sumOfSquaresOfDeltas();
/* 142 */     Preconditions.checkState((xSumOfSquaresOfDeltas > 0.0D));
/* 143 */     Preconditions.checkState((ySumOfSquaresOfDeltas > 0.0D));
/*     */ 
/*     */ 
/*     */     
/* 147 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 148 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
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
/*     */   public LinearTransformation leastSquaresFit() {
/* 183 */     Preconditions.checkState((count() > 1L));
/* 184 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 185 */       return LinearTransformation.forNaN();
/*     */     }
/* 187 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 188 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 189 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 190 */         return LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean())
/* 191 */           .withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 193 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     } 
/*     */     
/* 196 */     Preconditions.checkState((this.yStats.sumOfSquaresOfDeltas() > 0.0D));
/* 197 */     return LinearTransformation.vertical(this.xStats.mean());
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
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 218 */     if (obj == null) {
/* 219 */       return false;
/*     */     }
/* 221 */     if (getClass() != obj.getClass()) {
/* 222 */       return false;
/*     */     }
/* 224 */     PairedStats other = (PairedStats)obj;
/* 225 */     return (this.xStats.equals(other.xStats) && this.yStats
/* 226 */       .equals(other.yStats) && 
/* 227 */       Double.doubleToLongBits(this.sumOfProductsOfDeltas) == Double.doubleToLongBits(other.sumOfProductsOfDeltas));
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
/* 238 */     return Objects.hashCode(new Object[] { this.xStats, this.yStats, Double.valueOf(this.sumOfProductsOfDeltas) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 243 */     if (count() > 0L) {
/* 244 */       return MoreObjects.toStringHelper(this)
/* 245 */         .add("xStats", this.xStats)
/* 246 */         .add("yStats", this.yStats)
/* 247 */         .add("populationCovariance", populationCovariance())
/* 248 */         .toString();
/*     */     }
/* 250 */     return MoreObjects.toStringHelper(this)
/* 251 */       .add("xStats", this.xStats)
/* 252 */       .add("yStats", this.yStats)
/* 253 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   double sumOfProductsOfDeltas() {
/* 258 */     return this.sumOfProductsOfDeltas;
/*     */   }
/*     */   
/*     */   private static double ensurePositive(double value) {
/* 262 */     if (value > 0.0D) {
/* 263 */       return value;
/*     */     }
/* 265 */     return Double.MIN_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double ensureInUnitRange(double value) {
/* 270 */     if (value >= 1.0D) {
/* 271 */       return 1.0D;
/*     */     }
/* 273 */     if (value <= -1.0D) {
/* 274 */       return -1.0D;
/*     */     }
/* 276 */     return value;
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
/* 291 */     ByteBuffer buffer = ByteBuffer.allocate(88).order(ByteOrder.LITTLE_ENDIAN);
/* 292 */     this.xStats.writeTo(buffer);
/* 293 */     this.yStats.writeTo(buffer);
/* 294 */     buffer.putDouble(this.sumOfProductsOfDeltas);
/* 295 */     return buffer.array();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PairedStats fromByteArray(byte[] byteArray) {
/* 306 */     Preconditions.checkNotNull(byteArray);
/* 307 */     Preconditions.checkArgument((byteArray.length == 88), "Expected PairedStats.BYTES = %s, got %s", 88, byteArray.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     ByteBuffer buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);
/* 313 */     Stats xStats = Stats.readFrom(buffer);
/* 314 */     Stats yStats = Stats.readFrom(buffer);
/* 315 */     double sumOfProductsOfDeltas = buffer.getDouble();
/* 316 */     return new PairedStats(xStats, yStats, sumOfProductsOfDeltas);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/PairedStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */