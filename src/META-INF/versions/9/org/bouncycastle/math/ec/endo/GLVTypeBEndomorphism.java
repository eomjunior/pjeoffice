/*    */ package META-INF.versions.9.org.bouncycastle.math.ec.endo;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECPointMap;
/*    */ import org.bouncycastle.math.ec.ScaleXPointMap;
/*    */ import org.bouncycastle.math.ec.endo.EndoUtil;
/*    */ import org.bouncycastle.math.ec.endo.GLVEndomorphism;
/*    */ import org.bouncycastle.math.ec.endo.GLVTypeBParameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GLVTypeBEndomorphism
/*    */   implements GLVEndomorphism
/*    */ {
/*    */   protected final GLVTypeBParameters parameters;
/*    */   protected final ECPointMap pointMap;
/*    */   
/*    */   public GLVTypeBEndomorphism(ECCurve paramECCurve, GLVTypeBParameters paramGLVTypeBParameters) {
/* 22 */     this.parameters = paramGLVTypeBParameters;
/* 23 */     this.pointMap = (ECPointMap)new ScaleXPointMap(paramECCurve.fromBigInteger(paramGLVTypeBParameters.getBeta()));
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger[] decomposeScalar(BigInteger paramBigInteger) {
/* 28 */     return EndoUtil.decomposeScalar(this.parameters.getSplitParams(), paramBigInteger);
/*    */   }
/*    */ 
/*    */   
/*    */   public ECPointMap getPointMap() {
/* 33 */     return this.pointMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasEfficientPointMap() {
/* 38 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/endo/GLVTypeBEndomorphism.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */