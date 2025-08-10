/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class ToDoubleRounder<X extends Number & Comparable<X>>
/*     */ {
/*     */   abstract double roundToDoubleArbitrarily(X paramX);
/*     */   
/*     */   abstract int sign(X paramX);
/*     */   
/*     */   abstract X toX(double paramDouble, RoundingMode paramRoundingMode);
/*     */   
/*     */   abstract X minus(X paramX1, X paramX2);
/*     */   
/*     */   final double roundToDouble(X x, RoundingMode mode) {
/*     */     X roundFloor;
/*     */     double roundFloorAsDouble;
/*     */     X roundCeiling;
/*     */     double roundCeilingAsDouble;
/*     */     X deltaToFloor, deltaToCeiling;
/*     */     int diff;
/*  49 */     Preconditions.checkNotNull(x, "x");
/*  50 */     Preconditions.checkNotNull(mode, "mode");
/*  51 */     double roundArbitrarily = roundToDoubleArbitrarily(x);
/*  52 */     if (Double.isInfinite(roundArbitrarily)) {
/*  53 */       switch (mode) {
/*     */         case DOWN:
/*     */         case HALF_EVEN:
/*     */         case HALF_DOWN:
/*     */         case HALF_UP:
/*  58 */           return Double.MAX_VALUE * sign(x);
/*     */         case FLOOR:
/*  60 */           return (roundArbitrarily == Double.POSITIVE_INFINITY) ? 
/*  61 */             Double.MAX_VALUE : 
/*  62 */             Double.NEGATIVE_INFINITY;
/*     */         case CEILING:
/*  64 */           return (roundArbitrarily == Double.POSITIVE_INFINITY) ? 
/*  65 */             Double.POSITIVE_INFINITY : 
/*  66 */             -1.7976931348623157E308D;
/*     */         case UP:
/*  68 */           return roundArbitrarily;
/*     */         case UNNECESSARY:
/*  70 */           throw new ArithmeticException((new StringBuilder()).append(x).append(" cannot be represented precisely as a double").toString());
/*     */       } 
/*     */     }
/*  73 */     X roundArbitrarilyAsX = toX(roundArbitrarily, RoundingMode.UNNECESSARY);
/*  74 */     int cmpXToRoundArbitrarily = ((Comparable<X>)x).compareTo(roundArbitrarilyAsX);
/*  75 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  77 */         MathPreconditions.checkRoundingUnnecessary((cmpXToRoundArbitrarily == 0));
/*  78 */         return roundArbitrarily;
/*     */       case FLOOR:
/*  80 */         return (cmpXToRoundArbitrarily >= 0) ? 
/*  81 */           roundArbitrarily : 
/*  82 */           DoubleUtils.nextDown(roundArbitrarily);
/*     */       case CEILING:
/*  84 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*     */       case DOWN:
/*  86 */         if (sign(x) >= 0) {
/*  87 */           return (cmpXToRoundArbitrarily >= 0) ? 
/*  88 */             roundArbitrarily : 
/*  89 */             DoubleUtils.nextDown(roundArbitrarily);
/*     */         }
/*  91 */         return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*     */       
/*     */       case UP:
/*  94 */         if (sign(x) >= 0) {
/*  95 */           return (cmpXToRoundArbitrarily <= 0) ? roundArbitrarily : Math.nextUp(roundArbitrarily);
/*     */         }
/*  97 */         return (cmpXToRoundArbitrarily >= 0) ? 
/*  98 */           roundArbitrarily : 
/*  99 */           DoubleUtils.nextDown(roundArbitrarily);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case HALF_EVEN:
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/* 110 */         if (cmpXToRoundArbitrarily >= 0) {
/* 111 */           roundFloorAsDouble = roundArbitrarily;
/* 112 */           roundFloor = roundArbitrarilyAsX;
/* 113 */           roundCeilingAsDouble = Math.nextUp(roundArbitrarily);
/* 114 */           if (roundCeilingAsDouble == Double.POSITIVE_INFINITY) {
/* 115 */             return roundFloorAsDouble;
/*     */           }
/* 117 */           roundCeiling = toX(roundCeilingAsDouble, RoundingMode.CEILING);
/*     */         } else {
/* 119 */           roundCeilingAsDouble = roundArbitrarily;
/* 120 */           roundCeiling = roundArbitrarilyAsX;
/* 121 */           roundFloorAsDouble = DoubleUtils.nextDown(roundArbitrarily);
/* 122 */           if (roundFloorAsDouble == Double.NEGATIVE_INFINITY) {
/* 123 */             return roundCeilingAsDouble;
/*     */           }
/* 125 */           roundFloor = toX(roundFloorAsDouble, RoundingMode.FLOOR);
/*     */         } 
/*     */         
/* 128 */         deltaToFloor = minus(x, roundFloor);
/* 129 */         deltaToCeiling = minus(roundCeiling, x);
/* 130 */         diff = ((Comparable<X>)deltaToFloor).compareTo(deltaToCeiling);
/* 131 */         if (diff < 0)
/* 132 */           return roundFloorAsDouble; 
/* 133 */         if (diff > 0) {
/* 134 */           return roundCeilingAsDouble;
/*     */         }
/*     */         
/* 137 */         switch (mode) {
/*     */ 
/*     */           
/*     */           case HALF_EVEN:
/* 141 */             return ((Double.doubleToRawLongBits(roundFloorAsDouble) & 0x1L) == 0L) ? 
/* 142 */               roundFloorAsDouble : 
/* 143 */               roundCeilingAsDouble;
/*     */           case HALF_DOWN:
/* 145 */             return (sign(x) >= 0) ? roundFloorAsDouble : roundCeilingAsDouble;
/*     */           case HALF_UP:
/* 147 */             return (sign(x) >= 0) ? roundCeilingAsDouble : roundFloorAsDouble;
/*     */         } 
/* 149 */         throw new AssertionError("impossible");
/*     */     } 
/*     */ 
/*     */     
/* 153 */     throw new AssertionError("impossible");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/ToDoubleRounder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */