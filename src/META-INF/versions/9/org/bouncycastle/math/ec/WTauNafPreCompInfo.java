/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ 
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.PreCompInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WTauNafPreCompInfo
/*    */   implements PreCompInfo
/*    */ {
/* 13 */   protected ECPoint.AbstractF2m[] preComp = null;
/*    */ 
/*    */   
/*    */   public ECPoint.AbstractF2m[] getPreComp() {
/* 17 */     return this.preComp;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPreComp(ECPoint.AbstractF2m[] paramArrayOfAbstractF2m) {
/* 22 */     this.preComp = paramArrayOfAbstractF2m;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/WTauNafPreCompInfo.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */