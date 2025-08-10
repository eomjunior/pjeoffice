/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.ECAlgorithms;
/*    */ import org.bouncycastle.math.ec.ECMultiplier;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ 
/*    */ public abstract class AbstractECMultiplier implements ECMultiplier {
/*    */   public ECPoint multiply(ECPoint paramECPoint, BigInteger paramBigInteger) {
/*  9 */     int i = paramBigInteger.signum();
/* 10 */     if (i == 0 || paramECPoint.isInfinity())
/*    */     {
/* 12 */       return paramECPoint.getCurve().getInfinity();
/*    */     }
/*    */     
/* 15 */     ECPoint eCPoint1 = multiplyPositive(paramECPoint, paramBigInteger.abs());
/* 16 */     ECPoint eCPoint2 = (i > 0) ? eCPoint1 : eCPoint1.negate();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 22 */     return checkResult(eCPoint2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract ECPoint multiplyPositive(ECPoint paramECPoint, BigInteger paramBigInteger);
/*    */   
/*    */   protected ECPoint checkResult(ECPoint paramECPoint) {
/* 29 */     return ECAlgorithms.implCheckResult(paramECPoint);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/AbstractECMultiplier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */