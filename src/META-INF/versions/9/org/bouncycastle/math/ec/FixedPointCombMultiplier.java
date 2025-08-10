/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECLookupTable;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.FixedPointPreCompInfo;
/*    */ import org.bouncycastle.math.ec.FixedPointUtil;
/*    */ 
/*    */ public class FixedPointCombMultiplier extends AbstractECMultiplier {
/*    */   protected ECPoint multiplyPositive(ECPoint paramECPoint, BigInteger paramBigInteger) {
/* 11 */     ECCurve eCCurve = paramECPoint.getCurve();
/* 12 */     int i = FixedPointUtil.getCombSize(eCCurve);
/*    */     
/* 14 */     if (paramBigInteger.bitLength() > i)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 22 */       throw new IllegalStateException("fixed-point comb doesn't support scalars larger than the curve order");
/*    */     }
/*    */     
/* 25 */     FixedPointPreCompInfo fixedPointPreCompInfo = FixedPointUtil.precompute(paramECPoint);
/* 26 */     ECLookupTable eCLookupTable = fixedPointPreCompInfo.getLookupTable();
/* 27 */     int j = fixedPointPreCompInfo.getWidth();
/*    */     
/* 29 */     int k = (i + j - 1) / j;
/*    */     
/* 31 */     ECPoint eCPoint = eCCurve.getInfinity();
/*    */     
/* 33 */     int m = k * j;
/* 34 */     int[] arrayOfInt = Nat.fromBigInteger(m, paramBigInteger);
/*    */     
/* 36 */     int n = m - 1;
/* 37 */     for (byte b = 0; b < k; b++) {
/*    */       
/* 39 */       int i1 = 0;
/*    */       int i2;
/* 41 */       for (i2 = n - b; i2 >= 0; i2 -= k) {
/*    */         
/* 43 */         int i3 = arrayOfInt[i2 >>> 5] >>> (i2 & 0x1F);
/* 44 */         i1 ^= i3 >>> 1;
/* 45 */         i1 <<= 1;
/* 46 */         i1 ^= i3;
/*    */       } 
/*    */       
/* 49 */       ECPoint eCPoint1 = eCLookupTable.lookup(i1);
/*    */       
/* 51 */       eCPoint = eCPoint.twicePlus(eCPoint1);
/*    */     } 
/*    */     
/* 54 */     return eCPoint.add(fixedPointPreCompInfo.getOffset());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/FixedPointCombMultiplier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */