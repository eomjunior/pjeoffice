/*    */ package META-INF.versions.9.org.bouncycastle.math.ec.endo;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.ECConstants;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.PreCompCallback;
/*    */ import org.bouncycastle.math.ec.endo.ECEndomorphism;
/*    */ import org.bouncycastle.math.ec.endo.EndoPreCompInfo;
/*    */ import org.bouncycastle.math.ec.endo.ScalarSplitParameters;
/*    */ 
/*    */ public abstract class EndoUtil
/*    */ {
/*    */   public static final String PRECOMP_NAME = "bc_endo";
/*    */   
/*    */   public static BigInteger[] decomposeScalar(ScalarSplitParameters paramScalarSplitParameters, BigInteger paramBigInteger) {
/* 17 */     int i = paramScalarSplitParameters.getBits();
/* 18 */     BigInteger bigInteger1 = calculateB(paramBigInteger, paramScalarSplitParameters.getG1(), i);
/* 19 */     BigInteger bigInteger2 = calculateB(paramBigInteger, paramScalarSplitParameters.getG2(), i);
/*    */     
/* 21 */     BigInteger bigInteger3 = paramBigInteger.subtract(bigInteger1.multiply(paramScalarSplitParameters.getV1A()).add(bigInteger2.multiply(paramScalarSplitParameters.getV2A())));
/* 22 */     BigInteger bigInteger4 = bigInteger1.multiply(paramScalarSplitParameters.getV1B()).add(bigInteger2.multiply(paramScalarSplitParameters.getV2B())).negate();
/*    */     
/* 24 */     return new BigInteger[] { bigInteger3, bigInteger4 };
/*    */   }
/*    */ 
/*    */   
/*    */   public static ECPoint mapPoint(ECEndomorphism paramECEndomorphism, ECPoint paramECPoint) {
/* 29 */     ECCurve eCCurve = paramECPoint.getCurve();
/*    */     
/* 31 */     EndoPreCompInfo endoPreCompInfo = (EndoPreCompInfo)eCCurve.precompute(paramECPoint, "bc_endo", (PreCompCallback)new Object(paramECEndomorphism, paramECPoint));
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
/* 58 */     return endoPreCompInfo.getMappedPoint();
/*    */   }
/*    */ 
/*    */   
/*    */   private static BigInteger calculateB(BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt) {
/* 63 */     boolean bool = (paramBigInteger2.signum() < 0) ? true : false;
/* 64 */     BigInteger bigInteger = paramBigInteger1.multiply(paramBigInteger2.abs());
/* 65 */     boolean bool1 = bigInteger.testBit(paramInt - 1);
/* 66 */     bigInteger = bigInteger.shiftRight(paramInt);
/* 67 */     if (bool1)
/*    */     {
/* 69 */       bigInteger = bigInteger.add(ECConstants.ONE);
/*    */     }
/* 71 */     return bool ? bigInteger.negate() : bigInteger;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/endo/EndoUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */