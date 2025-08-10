/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.ECDomainParameters;
/*    */ import org.bouncycastle.crypto.params.ECKeyParameters;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ 
/*    */ 
/*    */ public class ECPublicKeyParameters
/*    */   extends ECKeyParameters
/*    */ {
/*    */   private final ECPoint q;
/*    */   
/*    */   public ECPublicKeyParameters(ECPoint paramECPoint, ECDomainParameters paramECDomainParameters) {
/* 14 */     super(false, paramECDomainParameters);
/*    */     
/* 16 */     this.q = paramECDomainParameters.validatePublicPoint(paramECPoint);
/*    */   }
/*    */ 
/*    */   
/*    */   public ECPoint getQ() {
/* 21 */     return this.q;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/ECPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */