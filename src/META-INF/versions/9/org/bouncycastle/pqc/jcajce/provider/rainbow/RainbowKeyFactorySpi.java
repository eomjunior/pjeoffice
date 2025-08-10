/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.rainbow;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactorySpi;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
/*     */ import org.bouncycastle.pqc.asn1.RainbowPrivateKey;
/*     */ import org.bouncycastle.pqc.asn1.RainbowPublicKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;
/*     */ import org.bouncycastle.pqc.jcajce.spec.RainbowPrivateKeySpec;
/*     */ import org.bouncycastle.pqc.jcajce.spec.RainbowPublicKeySpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RainbowKeyFactorySpi
/*     */   extends KeyFactorySpi
/*     */   implements AsymmetricKeyInfoConverter
/*     */ {
/*     */   public PrivateKey engineGeneratePrivate(KeySpec paramKeySpec) throws InvalidKeySpecException {
/*  70 */     if (paramKeySpec instanceof RainbowPrivateKeySpec)
/*     */     {
/*  72 */       return (PrivateKey)new BCRainbowPrivateKey((RainbowPrivateKeySpec)paramKeySpec);
/*     */     }
/*  74 */     if (paramKeySpec instanceof PKCS8EncodedKeySpec) {
/*     */ 
/*     */       
/*  77 */       byte[] arrayOfByte = ((PKCS8EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */       
/*     */       try {
/*  81 */         return generatePrivate(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrayOfByte)));
/*     */       }
/*  83 */       catch (Exception exception) {
/*     */         
/*  85 */         throw new InvalidKeySpecException(exception.toString());
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     throw new InvalidKeySpecException("Unsupported key specification: " + paramKeySpec
/*  90 */         .getClass() + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicKey engineGeneratePublic(KeySpec paramKeySpec) throws InvalidKeySpecException {
/* 116 */     if (paramKeySpec instanceof RainbowPublicKeySpec)
/*     */     {
/* 118 */       return (PublicKey)new BCRainbowPublicKey((RainbowPublicKeySpec)paramKeySpec);
/*     */     }
/* 120 */     if (paramKeySpec instanceof X509EncodedKeySpec) {
/*     */ 
/*     */       
/* 123 */       byte[] arrayOfByte = ((X509EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 128 */         return generatePublic(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */       }
/* 130 */       catch (Exception exception) {
/*     */         
/* 132 */         throw new InvalidKeySpecException(exception.toString());
/*     */       } 
/*     */     } 
/*     */     
/* 136 */     throw new InvalidKeySpecException("Unknown key specification: " + paramKeySpec + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final KeySpec engineGetKeySpec(Key paramKey, Class<?> paramClass) throws InvalidKeySpecException {
/* 155 */     if (paramKey instanceof BCRainbowPrivateKey) {
/*     */       
/* 157 */       if (PKCS8EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 159 */         return new PKCS8EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/* 161 */       if (RainbowPrivateKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 163 */         BCRainbowPrivateKey bCRainbowPrivateKey = (BCRainbowPrivateKey)paramKey;
/* 164 */         return (KeySpec)new RainbowPrivateKeySpec(bCRainbowPrivateKey.getInvA1(), bCRainbowPrivateKey
/* 165 */             .getB1(), bCRainbowPrivateKey.getInvA2(), bCRainbowPrivateKey.getB2(), bCRainbowPrivateKey
/* 166 */             .getVi(), bCRainbowPrivateKey.getLayers());
/*     */       }
/*     */     
/* 169 */     } else if (paramKey instanceof BCRainbowPublicKey) {
/*     */       
/* 171 */       if (X509EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 173 */         return new X509EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/* 175 */       if (RainbowPublicKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 177 */         BCRainbowPublicKey bCRainbowPublicKey = (BCRainbowPublicKey)paramKey;
/* 178 */         return (KeySpec)new RainbowPublicKeySpec(bCRainbowPublicKey.getDocLength(), bCRainbowPublicKey
/* 179 */             .getCoeffQuadratic(), bCRainbowPublicKey.getCoeffSingular(), bCRainbowPublicKey
/* 180 */             .getCoeffScalar());
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 185 */       throw new InvalidKeySpecException("Unsupported key type: " + paramKey
/* 186 */           .getClass() + ".");
/*     */     } 
/*     */     
/* 189 */     throw new InvalidKeySpecException("Unknown key specification: " + paramClass + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Key engineTranslateKey(Key paramKey) throws InvalidKeyException {
/* 204 */     if (paramKey instanceof BCRainbowPrivateKey || paramKey instanceof BCRainbowPublicKey)
/*     */     {
/* 206 */       return paramKey;
/*     */     }
/*     */     
/* 209 */     throw new InvalidKeyException("Unsupported key type");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey generatePrivate(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 215 */     RainbowPrivateKey rainbowPrivateKey = RainbowPrivateKey.getInstance(paramPrivateKeyInfo.parsePrivateKey());
/*     */     
/* 217 */     return (PrivateKey)new BCRainbowPrivateKey(rainbowPrivateKey.getInvA1(), rainbowPrivateKey.getB1(), rainbowPrivateKey.getInvA2(), rainbowPrivateKey.getB2(), rainbowPrivateKey.getVi(), rainbowPrivateKey.getLayers());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicKey generatePublic(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/* 223 */     RainbowPublicKey rainbowPublicKey = RainbowPublicKey.getInstance(paramSubjectPublicKeyInfo.parsePublicKey());
/*     */     
/* 225 */     return (PublicKey)new BCRainbowPublicKey(rainbowPublicKey.getDocLength(), rainbowPublicKey.getCoeffQuadratic(), rainbowPublicKey.getCoeffSingular(), rainbowPublicKey.getCoeffScalar());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/rainbow/RainbowKeyFactorySpi.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */