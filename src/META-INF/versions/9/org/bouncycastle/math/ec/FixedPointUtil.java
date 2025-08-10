/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.FixedPointPreCompInfo;
/*    */ import org.bouncycastle.math.ec.PreCompCallback;
/*    */ import org.bouncycastle.math.ec.PreCompInfo;
/*    */ 
/*    */ public class FixedPointUtil {
/*    */   public static int getCombSize(ECCurve paramECCurve) {
/* 11 */     BigInteger bigInteger = paramECCurve.getOrder();
/* 12 */     return (bigInteger == null) ? (paramECCurve.getFieldSize() + 1) : bigInteger.bitLength();
/*    */   }
/*    */   public static final String PRECOMP_NAME = "bc_fixed_point";
/*    */   
/*    */   public static FixedPointPreCompInfo getFixedPointPreCompInfo(PreCompInfo paramPreCompInfo) {
/* 17 */     return (paramPreCompInfo instanceof FixedPointPreCompInfo) ? (FixedPointPreCompInfo)paramPreCompInfo : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static FixedPointPreCompInfo precompute(ECPoint paramECPoint) {
/* 22 */     ECCurve eCCurve = paramECPoint.getCurve();
/*    */     
/* 24 */     return (FixedPointPreCompInfo)eCCurve.precompute(paramECPoint, "bc_fixed_point", (PreCompCallback)new Object(eCCurve, paramECPoint));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/FixedPointUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */