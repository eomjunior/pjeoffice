/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.AbstractECMultiplier;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.WNafPreCompInfo;
/*    */ import org.bouncycastle.math.ec.WNafUtil;
/*    */ import org.bouncycastle.util.Integers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WNafL2RMultiplier
/*    */   extends AbstractECMultiplier
/*    */ {
/*    */   protected ECPoint multiplyPositive(ECPoint paramECPoint, BigInteger paramBigInteger) {
/* 22 */     int i = WNafUtil.getWindowSize(paramBigInteger.bitLength());
/*    */     
/* 24 */     WNafPreCompInfo wNafPreCompInfo = WNafUtil.precompute(paramECPoint, i, true);
/* 25 */     ECPoint[] arrayOfECPoint1 = wNafPreCompInfo.getPreComp();
/* 26 */     ECPoint[] arrayOfECPoint2 = wNafPreCompInfo.getPreCompNeg();
/* 27 */     int j = wNafPreCompInfo.getWidth();
/*    */     
/* 29 */     int[] arrayOfInt = WNafUtil.generateCompactWindowNaf(j, paramBigInteger);
/*    */     
/* 31 */     ECPoint eCPoint = paramECPoint.getCurve().getInfinity();
/*    */     
/* 33 */     int k = arrayOfInt.length;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 39 */     if (k > 1) {
/*    */       
/* 41 */       int m = arrayOfInt[--k];
/* 42 */       int n = m >> 16, i1 = m & 0xFFFF;
/*    */       
/* 44 */       int i2 = Math.abs(n);
/* 45 */       ECPoint[] arrayOfECPoint = (n < 0) ? arrayOfECPoint2 : arrayOfECPoint1;
/*    */ 
/*    */       
/* 48 */       if (i2 << 2 < 1 << j) {
/*    */         
/* 50 */         int i3 = 32 - Integers.numberOfLeadingZeros(i2);
/*    */ 
/*    */         
/* 53 */         int i4 = j - i3;
/* 54 */         int i5 = i2 ^ 1 << i3 - 1;
/*    */         
/* 56 */         int i6 = (1 << j - 1) - 1;
/* 57 */         int i7 = (i5 << i4) + 1;
/* 58 */         eCPoint = arrayOfECPoint[i6 >>> 1].add(arrayOfECPoint[i7 >>> 1]);
/*    */         
/* 60 */         i1 -= i4;
/*    */       
/*    */       }
/*    */       else {
/*    */ 
/*    */         
/* 66 */         eCPoint = arrayOfECPoint[i2 >>> 1];
/*    */       } 
/*    */       
/* 69 */       eCPoint = eCPoint.timesPow2(i1);
/*    */     } 
/*    */     
/* 72 */     while (k > 0) {
/*    */       
/* 74 */       int m = arrayOfInt[--k];
/* 75 */       int n = m >> 16, i1 = m & 0xFFFF;
/*    */       
/* 77 */       int i2 = Math.abs(n);
/* 78 */       ECPoint[] arrayOfECPoint = (n < 0) ? arrayOfECPoint2 : arrayOfECPoint1;
/* 79 */       ECPoint eCPoint1 = arrayOfECPoint[i2 >>> 1];
/*    */       
/* 81 */       eCPoint = eCPoint.twicePlus(eCPoint1);
/* 82 */       eCPoint = eCPoint.timesPow2(i1);
/*    */     } 
/*    */     
/* 85 */     return eCPoint;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/WNafL2RMultiplier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */