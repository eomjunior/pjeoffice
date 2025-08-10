/*    */ package META-INF.versions.15.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.interfaces.XECPrivateKey;
/*    */ import java.security.spec.AlgorithmParameterSpec;
/*    */ import java.security.spec.NamedParameterSpec;
/*    */ import java.util.Optional;
/*    */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
/*    */ import org.bouncycastle.crypto.params.X448PrivateKeyParameters;
/*    */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCXDHPrivateKey;
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
/*    */ class BC11XDHPrivateKey
/*    */   extends BCXDHPrivateKey
/*    */   implements XECPrivateKey
/*    */ {
/*    */   BC11XDHPrivateKey(AsymmetricKeyParameter paramAsymmetricKeyParameter) {
/* 30 */     super(paramAsymmetricKeyParameter);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   BC11XDHPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 36 */     super(paramPrivateKeyInfo);
/*    */   }
/*    */ 
/*    */   
/*    */   public AlgorithmParameterSpec getParams() {
/* 41 */     if (this.xdhPrivateKey instanceof X448PrivateKeyParameters)
/*    */     {
/* 43 */       return NamedParameterSpec.X448;
/*    */     }
/*    */ 
/*    */     
/* 47 */     return NamedParameterSpec.X25519;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<byte[]> getScalar() {
/* 53 */     if (this.xdhPrivateKey instanceof X448PrivateKeyParameters)
/*    */     {
/* 55 */       return (Optional)Optional.of(((X448PrivateKeyParameters)this.xdhPrivateKey).getEncoded());
/*    */     }
/*    */ 
/*    */     
/* 59 */     return (Optional)Optional.of(((X25519PrivateKeyParameters)this.xdhPrivateKey).getEncoded());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/15/org/bouncycastle/jcajce/provider/asymmetric/edec/BC11XDHPrivateKey.class
 * Java compiler version: 15 (59.0)
 * JD-Core Version:       1.1.3
 */