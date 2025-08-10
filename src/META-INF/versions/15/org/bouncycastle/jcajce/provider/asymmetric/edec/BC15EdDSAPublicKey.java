/*    */ package META-INF.versions.15.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.security.interfaces.EdECPublicKey;
/*    */ import java.security.spec.EdECPoint;
/*    */ import java.security.spec.InvalidKeySpecException;
/*    */ import java.security.spec.NamedParameterSpec;
/*    */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.crypto.params.Ed448PublicKeyParameters;
/*    */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ class BC15EdDSAPublicKey
/*    */   extends BCEdDSAPublicKey
/*    */   implements EdECPublicKey
/*    */ {
/*    */   BC15EdDSAPublicKey(AsymmetricKeyParameter paramAsymmetricKeyParameter) {
/* 20 */     super(paramAsymmetricKeyParameter);
/*    */   }
/*    */ 
/*    */   
/*    */   BC15EdDSAPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) {
/* 25 */     super(paramSubjectPublicKeyInfo);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   BC15EdDSAPublicKey(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws InvalidKeySpecException {
/* 31 */     super(paramArrayOfbyte1, paramArrayOfbyte2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EdECPoint getPoint() {
/*    */     byte[] arrayOfByte;
/* 38 */     if (this.eddsaPublicKey instanceof Ed448PublicKeyParameters) {
/*    */       
/* 40 */       arrayOfByte = ((Ed448PublicKeyParameters)this.eddsaPublicKey).getEncoded();
/*    */     }
/*    */     else {
/*    */       
/* 44 */       arrayOfByte = ((Ed448PublicKeyParameters)this.eddsaPublicKey).getEncoded();
/*    */     } 
/* 46 */     return new EdECPoint((arrayOfByte[arrayOfByte.length - 1] != 0), new BigInteger(1, 
/* 47 */           Arrays.reverse(Arrays.copyOfRange(arrayOfByte, 0, arrayOfByte.length - 1))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NamedParameterSpec getParams() {
/* 53 */     if (this.eddsaPublicKey instanceof Ed448PublicKeyParameters)
/*    */     {
/* 55 */       return NamedParameterSpec.ED448;
/*    */     }
/*    */ 
/*    */     
/* 59 */     return NamedParameterSpec.ED25519;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/15/org/bouncycastle/jcajce/provider/asymmetric/edec/BC15EdDSAPublicKey.class
 * Java compiler version: 15 (59.0)
 * JD-Core Version:       1.1.3
 */