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
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.crypto.digests.SHA256Digest;
/*     */ import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
/*     */ import org.bouncycastle.pqc.asn1.McEliecePrivateKey;
/*     */ import org.bouncycastle.pqc.asn1.McEliecePublicKey;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey;
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
/*     */ public class McElieceKeyFactorySpi
/*     */   extends KeyFactorySpi
/*     */   implements AsymmetricKeyInfoConverter
/*     */ {
/*     */   public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.1";
/*     */   
/*     */   protected PublicKey engineGeneratePublic(KeySpec paramKeySpec) throws InvalidKeySpecException {
/*  53 */     if (paramKeySpec instanceof X509EncodedKeySpec) {
/*     */       SubjectPublicKeyInfo subjectPublicKeyInfo;
/*     */       
/*  56 */       byte[] arrayOfByte = ((X509EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  62 */         subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrayOfByte));
/*     */       }
/*  64 */       catch (IOException iOException) {
/*     */         
/*  66 */         throw new InvalidKeySpecException(iOException.toString());
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/*  71 */         if (PQCObjectIdentifiers.mcEliece.equals((ASN1Primitive)subjectPublicKeyInfo.getAlgorithm().getAlgorithm())) {
/*     */           
/*  73 */           McEliecePublicKey mcEliecePublicKey = McEliecePublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
/*     */           
/*  75 */           return (PublicKey)new BCMcEliecePublicKey(new McEliecePublicKeyParameters(mcEliecePublicKey.getN(), mcEliecePublicKey.getT(), mcEliecePublicKey.getG()));
/*     */         } 
/*     */ 
/*     */         
/*  79 */         throw new InvalidKeySpecException("Unable to recognise OID in McEliece public key");
/*     */       
/*     */       }
/*  82 */       catch (IOException iOException) {
/*     */         
/*  84 */         throw new InvalidKeySpecException("Unable to decode X509EncodedKeySpec: " + iOException
/*     */             
/*  86 */             .getMessage());
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     throw new InvalidKeySpecException("Unsupported key specification: " + paramKeySpec
/*  91 */         .getClass() + ".");
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
/*     */   protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec) throws InvalidKeySpecException {
/* 105 */     if (paramKeySpec instanceof PKCS8EncodedKeySpec) {
/*     */       PrivateKeyInfo privateKeyInfo;
/*     */       
/* 108 */       byte[] arrayOfByte = ((PKCS8EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 115 */         privateKeyInfo = PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrayOfByte));
/*     */       }
/* 117 */       catch (IOException iOException) {
/*     */         
/* 119 */         throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec: " + iOException);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 124 */         if (PQCObjectIdentifiers.mcEliece.equals((ASN1Primitive)privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm())) {
/*     */           
/* 126 */           McEliecePrivateKey mcEliecePrivateKey = McEliecePrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
/*     */           
/* 128 */           return (PrivateKey)new BCMcEliecePrivateKey(new McEliecePrivateKeyParameters(mcEliecePrivateKey.getN(), mcEliecePrivateKey.getK(), mcEliecePrivateKey.getField(), mcEliecePrivateKey.getGoppaPoly(), mcEliecePrivateKey.getP1(), mcEliecePrivateKey.getP2(), mcEliecePrivateKey.getSInv()));
/*     */         } 
/*     */ 
/*     */         
/* 132 */         throw new InvalidKeySpecException("Unable to recognise OID in McEliece private key");
/*     */       
/*     */       }
/* 135 */       catch (IOException iOException) {
/*     */         
/* 137 */         throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec.");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 142 */     throw new InvalidKeySpecException("Unsupported key specification: " + paramKeySpec
/* 143 */         .getClass() + ".");
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
/* 161 */     if (paramKey instanceof BCMcEliecePrivateKey) {
/*     */       
/* 163 */       if (PKCS8EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 165 */         return new PKCS8EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/*     */     }
/* 168 */     else if (paramKey instanceof BCMcEliecePublicKey) {
/*     */       
/* 170 */       if (X509EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/* 172 */         return new X509EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 177 */       throw new InvalidKeySpecException("Unsupported key type: " + paramKey
/* 178 */           .getClass() + ".");
/*     */     } 
/*     */     
/* 181 */     throw new InvalidKeySpecException("Unknown key specification: " + paramClass + ".");
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
/* 197 */     if (paramKey instanceof BCMcEliecePrivateKey || paramKey instanceof BCMcEliecePublicKey)
/*     */     {
/*     */       
/* 200 */       return paramKey;
/*     */     }
/* 202 */     throw new InvalidKeyException("Unsupported key type.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicKey generatePublic(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/* 210 */     ASN1Primitive aSN1Primitive = paramSubjectPublicKeyInfo.parsePublicKey();
/* 211 */     McEliecePublicKey mcEliecePublicKey = McEliecePublicKey.getInstance(aSN1Primitive);
/* 212 */     return (PublicKey)new BCMcEliecePublicKey(new McEliecePublicKeyParameters(mcEliecePublicKey.getN(), mcEliecePublicKey.getT(), mcEliecePublicKey.getG()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey generatePrivate(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 219 */     ASN1Primitive aSN1Primitive = paramPrivateKeyInfo.parsePrivateKey().toASN1Primitive();
/* 220 */     McEliecePrivateKey mcEliecePrivateKey = McEliecePrivateKey.getInstance(aSN1Primitive);
/* 221 */     return (PrivateKey)new BCMcEliecePrivateKey(new McEliecePrivateKeyParameters(mcEliecePrivateKey.getN(), mcEliecePrivateKey.getK(), mcEliecePrivateKey.getField(), mcEliecePrivateKey.getGoppaPoly(), mcEliecePrivateKey.getP1(), mcEliecePrivateKey.getP2(), mcEliecePrivateKey.getSInv()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected KeySpec engineGetKeySpec(Key paramKey, Class paramClass) throws InvalidKeySpecException {
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Key engineTranslateKey(Key paramKey) throws InvalidKeyException {
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Digest getDigest(AlgorithmIdentifier paramAlgorithmIdentifier) {
/* 240 */     return (Digest)new SHA256Digest();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/mceliece/McElieceKeyFactorySpi.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */