/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.crypto.params.ECDomainParameters;
/*    */ 
/*    */ public class ECKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   private final ECDomainParameters parameters;
/*    */   
/*    */   protected ECKeyParameters(boolean paramBoolean, ECDomainParameters paramECDomainParameters) {
/* 12 */     super(paramBoolean);
/*    */     
/* 14 */     if (null == paramECDomainParameters)
/*    */     {
/* 16 */       throw new NullPointerException("'parameters' cannot be null");
/*    */     }
/*    */     
/* 19 */     this.parameters = paramECDomainParameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public ECDomainParameters getParameters() {
/* 24 */     return this.parameters;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/ECKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */