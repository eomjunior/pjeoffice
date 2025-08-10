/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.crypto.params.ECDomainParameters;
/*    */ import org.bouncycastle.crypto.params.ECKeyParameters;
/*    */ 
/*    */ 
/*    */ public class ECPrivateKeyParameters
/*    */   extends ECKeyParameters
/*    */ {
/*    */   private final BigInteger d;
/*    */   
/*    */   public ECPrivateKeyParameters(BigInteger paramBigInteger, ECDomainParameters paramECDomainParameters) {
/* 14 */     super(true, paramECDomainParameters);
/*    */     
/* 16 */     this.d = paramECDomainParameters.validatePrivateScalar(paramBigInteger);
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getD() {
/* 21 */     return this.d;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/ECPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */