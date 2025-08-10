/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ 
/*    */ import org.bouncycastle.math.ec.ECFieldElement;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.ECPointMap;
/*    */ 
/*    */ public class ScaleXPointMap implements ECPointMap {
/*    */   public ScaleXPointMap(ECFieldElement paramECFieldElement) {
/*  9 */     this.scale = paramECFieldElement;
/*    */   }
/*    */ 
/*    */   
/*    */   public ECPoint map(ECPoint paramECPoint) {
/* 14 */     return paramECPoint.scaleX(this.scale);
/*    */   }
/*    */   
/*    */   protected final ECFieldElement scale;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ScaleXPointMap.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */