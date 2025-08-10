/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.AbstractECMultiplier;
/*    */ import org.bouncycastle.math.ec.ECAlgorithms;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.endo.ECEndomorphism;
/*    */ import org.bouncycastle.math.ec.endo.EndoUtil;
/*    */ import org.bouncycastle.math.ec.endo.GLVEndomorphism;
/*    */ 
/*    */ public class GLVMultiplier extends AbstractECMultiplier {
/*    */   protected final ECCurve curve;
/*    */   
/*    */   public GLVMultiplier(ECCurve paramECCurve, GLVEndomorphism paramGLVEndomorphism) {
/* 15 */     if (paramECCurve == null || paramECCurve.getOrder() == null)
/*    */     {
/* 17 */       throw new IllegalArgumentException("Need curve with known group order");
/*    */     }
/*    */     
/* 20 */     this.curve = paramECCurve;
/* 21 */     this.glvEndomorphism = paramGLVEndomorphism;
/*    */   }
/*    */   protected final GLVEndomorphism glvEndomorphism;
/*    */   
/*    */   protected ECPoint multiplyPositive(ECPoint paramECPoint, BigInteger paramBigInteger) {
/* 26 */     if (!this.curve.equals(paramECPoint.getCurve()))
/*    */     {
/* 28 */       throw new IllegalStateException();
/*    */     }
/*    */     
/* 31 */     BigInteger bigInteger1 = paramECPoint.getCurve().getOrder();
/* 32 */     BigInteger[] arrayOfBigInteger = this.glvEndomorphism.decomposeScalar(paramBigInteger.mod(bigInteger1));
/* 33 */     BigInteger bigInteger2 = arrayOfBigInteger[0], bigInteger3 = arrayOfBigInteger[1];
/*    */     
/* 35 */     if (this.glvEndomorphism.hasEfficientPointMap())
/*    */     {
/* 37 */       return ECAlgorithms.implShamirsTrickWNaf((ECEndomorphism)this.glvEndomorphism, paramECPoint, bigInteger2, bigInteger3);
/*    */     }
/*    */     
/* 40 */     ECPoint eCPoint = EndoUtil.mapPoint((ECEndomorphism)this.glvEndomorphism, paramECPoint);
/*    */     
/* 42 */     return ECAlgorithms.implShamirsTrickWNaf(paramECPoint, bigInteger2, eCPoint, bigInteger3);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/GLVMultiplier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */