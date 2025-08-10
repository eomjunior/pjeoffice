/*    */ package META-INF.versions.9.org.bouncycastle.math.ec.endo;
/*    */ 
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.PreCompInfo;
/*    */ import org.bouncycastle.math.ec.endo.ECEndomorphism;
/*    */ 
/*    */ public class EndoPreCompInfo
/*    */   implements PreCompInfo
/*    */ {
/*    */   protected ECEndomorphism endomorphism;
/*    */   protected ECPoint mappedPoint;
/*    */   
/*    */   public ECEndomorphism getEndomorphism() {
/* 14 */     return this.endomorphism;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEndomorphism(ECEndomorphism paramECEndomorphism) {
/* 19 */     this.endomorphism = paramECEndomorphism;
/*    */   }
/*    */ 
/*    */   
/*    */   public ECPoint getMappedPoint() {
/* 24 */     return this.mappedPoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMappedPoint(ECPoint paramECPoint) {
/* 29 */     this.mappedPoint = paramECPoint;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/endo/EndoPreCompInfo.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */