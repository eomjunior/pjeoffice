/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public abstract class LinearTransformation
/*     */ {
/*     */   public static LinearTransformationBuilder mapping(double x1, double y1) {
/*  48 */     Preconditions.checkArgument((DoubleUtils.isFinite(x1) && DoubleUtils.isFinite(y1)));
/*  49 */     return new LinearTransformationBuilder(x1, y1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class LinearTransformationBuilder
/*     */   {
/*     */     private final double x1;
/*     */ 
/*     */ 
/*     */     
/*     */     private final double y1;
/*     */ 
/*     */ 
/*     */     
/*     */     private LinearTransformationBuilder(double x1, double y1) {
/*  66 */       this.x1 = x1;
/*  67 */       this.y1 = y1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LinearTransformation and(double x2, double y2) {
/*  77 */       Preconditions.checkArgument((DoubleUtils.isFinite(x2) && DoubleUtils.isFinite(y2)));
/*  78 */       if (x2 == this.x1) {
/*  79 */         Preconditions.checkArgument((y2 != this.y1));
/*  80 */         return new LinearTransformation.VerticalLinearTransformation(this.x1);
/*     */       } 
/*  82 */       return withSlope((y2 - this.y1) / (x2 - this.x1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LinearTransformation withSlope(double slope) {
/*  92 */       Preconditions.checkArgument(!Double.isNaN(slope));
/*  93 */       if (DoubleUtils.isFinite(slope)) {
/*  94 */         double yIntercept = this.y1 - this.x1 * slope;
/*  95 */         return new LinearTransformation.RegularLinearTransformation(slope, yIntercept);
/*     */       } 
/*  97 */       return new LinearTransformation.VerticalLinearTransformation(this.x1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation vertical(double x) {
/* 107 */     Preconditions.checkArgument(DoubleUtils.isFinite(x));
/* 108 */     return new VerticalLinearTransformation(x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation horizontal(double y) {
/* 116 */     Preconditions.checkArgument(DoubleUtils.isFinite(y));
/* 117 */     double slope = 0.0D;
/* 118 */     return new RegularLinearTransformation(slope, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation forNaN() {
/* 128 */     return NaNLinearTransformation.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isVertical();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isHorizontal();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract double slope();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract double transform(double paramDouble);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract LinearTransformation inverse();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RegularLinearTransformation
/*     */     extends LinearTransformation
/*     */   {
/*     */     final double slope;
/*     */ 
/*     */     
/*     */     final double yIntercept;
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     @LazyInit
/*     */     LinearTransformation inverse;
/*     */ 
/*     */ 
/*     */     
/*     */     RegularLinearTransformation(double slope, double yIntercept) {
/* 169 */       this.slope = slope;
/* 170 */       this.yIntercept = yIntercept;
/* 171 */       this.inverse = null;
/*     */     }
/*     */     
/*     */     RegularLinearTransformation(double slope, double yIntercept, LinearTransformation inverse) {
/* 175 */       this.slope = slope;
/* 176 */       this.yIntercept = yIntercept;
/* 177 */       this.inverse = inverse;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 182 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 187 */       return (this.slope == 0.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 192 */       return this.slope;
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 197 */       return x * this.slope + this.yIntercept;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 202 */       LinearTransformation result = this.inverse;
/* 203 */       return (result == null) ? (this.inverse = createInverse()) : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 208 */       return String.format("y = %g * x + %g", new Object[] { Double.valueOf(this.slope), Double.valueOf(this.yIntercept) });
/*     */     }
/*     */     
/*     */     private LinearTransformation createInverse() {
/* 212 */       if (this.slope != 0.0D) {
/* 213 */         return new RegularLinearTransformation(1.0D / this.slope, -1.0D * this.yIntercept / this.slope, this);
/*     */       }
/* 215 */       return new LinearTransformation.VerticalLinearTransformation(this.yIntercept, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class VerticalLinearTransformation
/*     */     extends LinearTransformation {
/*     */     final double x;
/*     */     @CheckForNull
/*     */     @LazyInit
/*     */     LinearTransformation inverse;
/*     */     
/*     */     VerticalLinearTransformation(double x) {
/* 227 */       this.x = x;
/* 228 */       this.inverse = null;
/*     */     }
/*     */     
/*     */     VerticalLinearTransformation(double x, LinearTransformation inverse) {
/* 232 */       this.x = x;
/* 233 */       this.inverse = inverse;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 238 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 243 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 248 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 253 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 258 */       LinearTransformation result = this.inverse;
/* 259 */       return (result == null) ? (this.inverse = createInverse()) : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 264 */       return String.format("x = %g", new Object[] { Double.valueOf(this.x) });
/*     */     }
/*     */     
/*     */     private LinearTransformation createInverse() {
/* 268 */       return new LinearTransformation.RegularLinearTransformation(0.0D, this.x, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NaNLinearTransformation
/*     */     extends LinearTransformation {
/* 274 */     static final NaNLinearTransformation INSTANCE = new NaNLinearTransformation();
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 278 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 283 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 288 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 293 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 298 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 303 */       return "NaN";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/LinearTransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */