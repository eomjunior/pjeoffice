/*    */ package META-INF.versions.11.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.security.interfaces.XECPublicKey;
/*    */ import java.security.spec.AlgorithmParameterSpec;
/*    */ import java.security.spec.InvalidKeySpecException;
/*    */ import java.security.spec.NamedParameterSpec;
/*    */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
/*    */ import org.bouncycastle.crypto.params.X448PublicKeyParameters;
/*    */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCXDHPublicKey;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ class BC11XDHPublicKey
/*    */   extends BCXDHPublicKey
/*    */   implements XECPublicKey
/*    */ {
/*    */   BC11XDHPublicKey(AsymmetricKeyParameter paramAsymmetricKeyParameter) {
/* 21 */     super(paramAsymmetricKeyParameter);
/*    */   }
/*    */ 
/*    */   
/*    */   BC11XDHPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) {
/* 26 */     super(paramSubjectPublicKeyInfo);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   BC11XDHPublicKey(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws InvalidKeySpecException {
/* 32 */     super(paramArrayOfbyte1, paramArrayOfbyte2);
/*    */   }
/*    */ 
/*    */   
/*    */   public AlgorithmParameterSpec getParams() {
/* 37 */     if (this.xdhPublicKey instanceof X448PublicKeyParameters)
/*    */     {
/* 39 */       return NamedParameterSpec.X448;
/*    */     }
/*    */ 
/*    */     
/* 43 */     return NamedParameterSpec.X25519;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BigInteger getU() {
/* 49 */     if (this.xdhPublicKey instanceof X448PublicKeyParameters)
/*    */     {
/* 51 */       return new BigInteger(1, Arrays.reverse(((X448PublicKeyParameters)this.xdhPublicKey).getEncoded()));
/*    */     }
/*    */ 
/*    */     
/* 55 */     return new BigInteger(1, Arrays.reverse(((X25519PublicKeyParameters)this.xdhPublicKey).getEncoded()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/11/org/bouncycastle/jcajce/provider/asymmetric/edec/BC11XDHPublicKey.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */