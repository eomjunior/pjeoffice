/*    */ package META-INF.versions.15.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.interfaces.EdECPrivateKey;
/*    */ import java.security.spec.NamedParameterSpec;
/*    */ import java.util.Optional;
/*    */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
/*    */ import org.bouncycastle.crypto.params.Ed448PrivateKeyParameters;
/*    */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPrivateKey;
/*    */ 
/*    */ 
/*    */ class BC15EdDSAPrivateKey
/*    */   extends BCEdDSAPrivateKey
/*    */   implements EdECPrivateKey
/*    */ {
/*    */   BC15EdDSAPrivateKey(AsymmetricKeyParameter paramAsymmetricKeyParameter) {
/* 19 */     super(paramAsymmetricKeyParameter);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   BC15EdDSAPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 25 */     super(paramPrivateKeyInfo);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<byte[]> getBytes() {
/* 31 */     if (this.eddsaPrivateKey instanceof Ed448PrivateKeyParameters)
/*    */     {
/* 33 */       return (Optional)Optional.of(((Ed448PrivateKeyParameters)this.eddsaPrivateKey).getEncoded());
/*    */     }
/*    */ 
/*    */     
/* 37 */     return (Optional)Optional.of(((Ed25519PrivateKeyParameters)this.eddsaPrivateKey).getEncoded());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NamedParameterSpec getParams() {
/* 44 */     if (this.eddsaPrivateKey instanceof Ed448PrivateKeyParameters)
/*    */     {
/* 46 */       return NamedParameterSpec.ED448;
/*    */     }
/*    */ 
/*    */     
/* 50 */     return NamedParameterSpec.ED25519;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/15/org/bouncycastle/jcajce/provider/asymmetric/edec/BC15EdDSAPrivateKey.class
 * Java compiler version: 15 (59.0)
 * JD-Core Version:       1.1.3
 */