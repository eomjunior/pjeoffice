/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.mceliece;
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
/*     */ import org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey;
/*     */ import org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PrivateKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PublicKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.Utils;
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
/*     */ public class McElieceCCA2KeyFactorySpi
/*     */   extends KeyFactorySpi
/*     */   implements AsymmetricKeyInfoConverter
/*     */ {
/*     */   public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2";
/*     */   
/*     */   protected PublicKey engineGeneratePublic(KeySpec paramKeySpec) throws InvalidKeySpecException {
/*  54 */     if (paramKeySpec instanceof X509EncodedKeySpec) {
/*     */       SubjectPublicKeyInfo subjectPublicKeyInfo;
/*     */       
/*  57 */       byte[] arrayOfByte = ((X509EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  63 */         subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrayOfByte));
/*     */       }
/*  65 */       catch (IOException iOException) {
/*     */         
/*  67 */         throw new InvalidKeySpecException(iOException.toString());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  73 */         if (PQCObjectIdentifiers.mcElieceCca2.equals((ASN1Primitive)subjectPublicKeyInfo.getAlgorithm().getAlgorithm())) {
/*     */           
/*  75 */           McElieceCCA2PublicKey mcElieceCCA2PublicKey = McElieceCCA2PublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
/*     */           
/*  77 */           return (PublicKey)new BCMcElieceCCA2PublicKey(new McElieceCCA2PublicKeyParameters(mcElieceCCA2PublicKey.getN(), mcElieceCCA2PublicKey.getT(), mcElieceCCA2PublicKey.getG(), Utils.getDigest(mcElieceCCA2PublicKey.getDigest()).getAlgorithmName()));
/*     */         } 
/*     */ 
/*     */         
/*  81 */         throw new InvalidKeySpecException("Unable to recognise OID in McEliece private key");
/*     */       
/*     */       }
/*  84 */       catch (IOException iOException) {
/*     */         
/*  86 */         throw new InvalidKeySpecException("Unable to decode X509EncodedKeySpec: " + iOException
/*     */             
/*  88 */             .getMessage());
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     throw new InvalidKeySpecException("Unsupported key specification: " + paramKeySpec
/*  93 */         .getClass() + ".");
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
/*     */   protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec) throws InvalidKeySpecException {
/* 109 */     if (paramKeySpec instanceof PKCS8EncodedKeySpec) {
/*     */       PrivateKeyInfo privateKeyInfo;
/*     */       
/* 112 */       byte[] arrayOfByte = ((PKCS8EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 119 */         privateKeyInfo = PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrayOfByte));
/*     */       }
/* 121 */       catch (IOException iOException) {
/*     */         
/* 123 */         throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec: " + iOException);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 128 */         if (PQCObjectIdentifiers.mcElieceCca2.equals((ASN1Primitive)privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm())) {
/*     */           
/* 130 */           McElieceCCA2PrivateKey mcElieceCCA2PrivateKey = McElieceCCA2PrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
/*     */           
/* 132 */           return (PrivateKey)new BCMcElieceCCA2PrivateKey(new McElieceCCA2PrivateKeyParameters(mcElieceCCA2PrivateKey.getN(), mcElieceCCA2PrivateKey.getK(), mcElieceCCA2PrivateKey.getField(), mcElieceCCA2PrivateKey.getGoppaPoly(), mcElieceCCA2PrivateKey.getP(), Utils.getDigest(mcElieceCCA2PrivateKey.getDigest()).getAlgorithmName()));
/*     */         } 
/*     */ 
/*     */         
/* 136 */         throw new InvalidKeySpecException("Unable to recognise OID in McEliece public key");
/*     */       
/*     */       }
/* 139 */       catch (IOException iOException) {
/*     */         
/* 141 */         throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec.");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 146 */     throw new InvalidKeySpecException("Unsupported key specification: " + paramKeySpec.getClass() + ".");
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
/*     */   public KeySpec getKeySpec(Key paramKey, Class<?> paramClass) throws InvalidKeySpecException {
/* 164 */     if (paramKey instanceof BCMcElieceCCA2PrivateKey) {
/*     */       
/* 166 */       if (PKCS8EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 168 */         return new PKCS8EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/*     */     }
/* 171 */     else if (paramKey instanceof BCMcElieceCCA2PublicKey) {
/*     */       
/* 173 */       if (X509EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 175 */         return new X509EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 180 */       throw new InvalidKeySpecException("Unsupported key type: " + paramKey
/* 181 */           .getClass() + ".");
/*     */     } 
/*     */     
/* 184 */     throw new InvalidKeySpecException("Unknown key specification: " + paramClass + ".");
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
/*     */   public Key translateKey(Key paramKey) throws InvalidKeyException {
/* 200 */     if (paramKey instanceof BCMcElieceCCA2PrivateKey || paramKey instanceof BCMcElieceCCA2PublicKey)
/*     */     {
/*     */       
/* 203 */       return paramKey;
/*     */     }
/* 205 */     throw new InvalidKeyException("Unsupported key type.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicKey generatePublic(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/* 213 */     ASN1Primitive aSN1Primitive = paramSubjectPublicKeyInfo.parsePublicKey();
/* 214 */     McElieceCCA2PublicKey mcElieceCCA2PublicKey = McElieceCCA2PublicKey.getInstance(aSN1Primitive);
/* 215 */     return (PublicKey)new BCMcElieceCCA2PublicKey(new McElieceCCA2PublicKeyParameters(mcElieceCCA2PublicKey.getN(), mcElieceCCA2PublicKey.getT(), mcElieceCCA2PublicKey.getG(), Utils.getDigest(mcElieceCCA2PublicKey.getDigest()).getAlgorithmName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey generatePrivate(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 222 */     ASN1Primitive aSN1Primitive = paramPrivateKeyInfo.parsePrivateKey().toASN1Primitive();
/* 223 */     McElieceCCA2PrivateKey mcElieceCCA2PrivateKey = McElieceCCA2PrivateKey.getInstance(aSN1Primitive);
/* 224 */     return (PrivateKey)new BCMcElieceCCA2PrivateKey(new McElieceCCA2PrivateKeyParameters(mcElieceCCA2PrivateKey.getN(), mcElieceCCA2PrivateKey.getK(), mcElieceCCA2PrivateKey.getField(), mcElieceCCA2PrivateKey.getGoppaPoly(), mcElieceCCA2PrivateKey.getP(), null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected KeySpec engineGetKeySpec(Key paramKey, Class paramClass) throws InvalidKeySpecException {
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Key engineTranslateKey(Key paramKey) throws InvalidKeyException {
/* 238 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/mceliece/McElieceCCA2KeyFactorySpi.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */