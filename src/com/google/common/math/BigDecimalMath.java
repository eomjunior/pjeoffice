/*    */ package com.google.common.math;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.RoundingMode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public class BigDecimalMath
/*    */ {
/*    */   public static double roundToDouble(BigDecimal x, RoundingMode mode) {
/* 56 */     return BigDecimalToDoubleRounder.INSTANCE.roundToDouble(x, mode);
/*    */   }
/*    */   
/*    */   private static class BigDecimalToDoubleRounder extends ToDoubleRounder<BigDecimal> {
/* 60 */     static final BigDecimalToDoubleRounder INSTANCE = new BigDecimalToDoubleRounder();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     double roundToDoubleArbitrarily(BigDecimal bigDecimal) {
/* 66 */       return bigDecimal.doubleValue();
/*    */     }
/*    */ 
/*    */     
/*    */     int sign(BigDecimal bigDecimal) {
/* 71 */       return bigDecimal.signum();
/*    */     }
/*    */ 
/*    */     
/*    */     BigDecimal toX(double d, RoundingMode mode) {
/* 76 */       return new BigDecimal(d);
/*    */     }
/*    */ 
/*    */     
/*    */     BigDecimal minus(BigDecimal a, BigDecimal b) {
/* 81 */       return a.subtract(b);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/BigDecimalMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */