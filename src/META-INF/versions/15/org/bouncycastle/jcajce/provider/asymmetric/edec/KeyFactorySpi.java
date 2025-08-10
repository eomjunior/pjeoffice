/*     */ package META-INF.versions.15.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.edec.EdECObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
/*     */ import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
/*     */ import org.bouncycastle.crypto.util.OpenSSHPrivateKeyUtil;
/*     */ import org.bouncycastle.crypto.util.OpenSSHPublicKeyUtil;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC11XDHPrivateKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC11XDHPublicKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC15EdDSAPrivateKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC15EdDSAPublicKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;
/*     */ import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
/*     */ import org.bouncycastle.jcajce.spec.OpenSSHPrivateKeySpec;
/*     */ import org.bouncycastle.jcajce.spec.OpenSSHPublicKeySpec;
/*     */ import org.bouncycastle.jce.spec.OpenSSHPrivateKeySpec;
/*     */ import org.bouncycastle.jce.spec.OpenSSHPublicKeySpec;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class KeyFactorySpi extends BaseKeyFactorySpi implements AsymmetricKeyInfoConverter {
/*  37 */   static final byte[] x448Prefix = Hex.decode("3042300506032b656f033900");
/*  38 */   static final byte[] x25519Prefix = Hex.decode("302a300506032b656e032100");
/*  39 */   static final byte[] Ed448Prefix = Hex.decode("3043300506032b6571033a00");
/*  40 */   static final byte[] Ed25519Prefix = Hex.decode("302a300506032b6570032100");
/*     */   
/*     */   private static final byte x448_type = 111;
/*     */   
/*     */   private static final byte x25519_type = 110;
/*     */   
/*     */   private static final byte Ed448_type = 113;
/*     */   
/*     */   private static final byte Ed25519_type = 112;
/*     */   
/*     */   String algorithm;
/*     */   
/*     */   private final boolean isXdh;
/*     */   private final int specificBase;
/*     */   
/*     */   public KeyFactorySpi(String paramString, boolean paramBoolean, int paramInt) {
/*  56 */     this.algorithm = paramString;
/*  57 */     this.isXdh = paramBoolean;
/*  58 */     this.specificBase = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Key engineTranslateKey(Key paramKey) throws InvalidKeyException {
/*  65 */     throw new InvalidKeyException("key type unknown");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected KeySpec engineGetKeySpec(Key paramKey, Class paramClass) throws InvalidKeySpecException {
/*  73 */     if (paramClass.isAssignableFrom(OpenSSHPrivateKeySpec.class) && paramKey instanceof BC15EdDSAPrivateKey) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  82 */         ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramKey.getEncoded());
/*  83 */         DEROctetString dEROctetString = (DEROctetString)aSN1Sequence.getObjectAt(2);
/*  84 */         ASN1InputStream aSN1InputStream = new ASN1InputStream(dEROctetString.getOctets());
/*     */         
/*  86 */         return (KeySpec)new OpenSSHPrivateKeySpec(OpenSSHPrivateKeyUtil.encodePrivateKey((AsymmetricKeyParameter)new Ed25519PrivateKeyParameters(ASN1OctetString.getInstance(aSN1InputStream.readObject()).getOctets(), 0)));
/*     */       }
/*  88 */       catch (IOException iOException) {
/*     */         
/*  90 */         throw new InvalidKeySpecException(iOException.getMessage(), iOException.getCause());
/*     */       } 
/*     */     }
/*     */     
/*  94 */     if (paramClass.isAssignableFrom(OpenSSHPublicKeySpec.class) && paramKey instanceof BC15EdDSAPublicKey) {
/*     */       
/*     */       try {
/*     */         
/*  98 */         return (KeySpec)new OpenSSHPublicKeySpec(OpenSSHPublicKeyUtil.encodePublicKey((AsymmetricKeyParameter)new Ed25519PublicKeyParameters(paramKey.getEncoded(), Ed25519Prefix.length)));
/*     */       }
/* 100 */       catch (IOException iOException) {
/*     */         
/* 102 */         throw new InvalidKeySpecException(iOException.getMessage(), iOException.getCause());
/*     */       } 
/*     */     }
/* 105 */     if (paramClass.isAssignableFrom(OpenSSHPrivateKeySpec.class) && paramKey instanceof BC15EdDSAPrivateKey) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 114 */         ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramKey.getEncoded());
/* 115 */         DEROctetString dEROctetString = (DEROctetString)aSN1Sequence.getObjectAt(2);
/* 116 */         ASN1InputStream aSN1InputStream = new ASN1InputStream(dEROctetString.getOctets());
/*     */         
/* 118 */         return (KeySpec)new OpenSSHPrivateKeySpec(OpenSSHPrivateKeyUtil.encodePrivateKey((AsymmetricKeyParameter)new Ed25519PrivateKeyParameters(ASN1OctetString.getInstance(aSN1InputStream.readObject()).getOctets(), 0)));
/*     */       }
/* 120 */       catch (IOException iOException) {
/*     */         
/* 122 */         throw new InvalidKeySpecException(iOException.getMessage(), iOException.getCause());
/*     */       } 
/*     */     }
/*     */     
/* 126 */     if (paramClass.isAssignableFrom(OpenSSHPublicKeySpec.class) && paramKey instanceof BC15EdDSAPublicKey) {
/*     */       
/*     */       try {
/*     */         
/* 130 */         return (KeySpec)new OpenSSHPublicKeySpec(OpenSSHPublicKeyUtil.encodePublicKey((AsymmetricKeyParameter)new Ed25519PublicKeyParameters(paramKey.getEncoded(), Ed25519Prefix.length)));
/*     */       }
/* 132 */       catch (IOException iOException) {
/*     */         
/* 134 */         throw new InvalidKeySpecException(iOException.getMessage(), iOException.getCause());
/*     */       } 
/*     */     }
/*     */     
/* 138 */     return super.engineGetKeySpec(paramKey, paramClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec) throws InvalidKeySpecException {
/* 145 */     if (paramKeySpec instanceof OpenSSHPrivateKeySpec) {
/*     */       
/* 147 */       AsymmetricKeyParameter asymmetricKeyParameter = OpenSSHPrivateKeyUtil.parsePrivateKeyBlob(((OpenSSHPrivateKeySpec)paramKeySpec).getEncoded());
/* 148 */       if (asymmetricKeyParameter instanceof Ed25519PrivateKeyParameters)
/*     */       {
/* 150 */         return (PrivateKey)new BC15EdDSAPrivateKey(asymmetricKeyParameter);
/*     */       }
/* 152 */       throw new IllegalStateException("openssh private key not Ed25519 private key");
/*     */     } 
/*     */     
/* 155 */     return super.engineGeneratePrivate(paramKeySpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PublicKey engineGeneratePublic(KeySpec paramKeySpec) throws InvalidKeySpecException {
/* 162 */     if (paramKeySpec instanceof X509EncodedKeySpec) {
/*     */       
/* 164 */       byte[] arrayOfByte = ((X509EncodedKeySpec)paramKeySpec).getEncoded();
/*     */       
/* 166 */       if (this.specificBase == 0 || this.specificBase == arrayOfByte[8])
/*     */       {
/*     */         
/* 169 */         if (arrayOfByte[9] == 5 && arrayOfByte[10] == 0) {
/*     */           
/* 171 */           SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(arrayOfByte);
/*     */ 
/*     */           
/* 174 */           subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(subjectPublicKeyInfo.getAlgorithm().getAlgorithm()), subjectPublicKeyInfo.getPublicKeyData().getBytes());
/*     */ 
/*     */           
/*     */           try {
/* 178 */             arrayOfByte = subjectPublicKeyInfo.getEncoded("DER");
/*     */           }
/* 180 */           catch (IOException iOException) {
/*     */             
/* 182 */             throw new InvalidKeySpecException("attempt to reconstruct key failed: " + iOException.getMessage());
/*     */           } 
/*     */         } 
/*     */         
/* 186 */         switch (arrayOfByte[8]) {
/*     */           
/*     */           case 111:
/* 189 */             return (PublicKey)new BC11XDHPublicKey(x448Prefix, arrayOfByte);
/*     */           case 110:
/* 191 */             return (PublicKey)new BC11XDHPublicKey(x25519Prefix, arrayOfByte);
/*     */           case 113:
/* 193 */             return (PublicKey)new BC15EdDSAPublicKey(Ed448Prefix, arrayOfByte);
/*     */           case 112:
/* 195 */             return (PublicKey)new BC15EdDSAPublicKey(Ed25519Prefix, arrayOfByte);
/*     */         } 
/* 197 */         return super.engineGeneratePublic(paramKeySpec);
/*     */       }
/*     */     
/*     */     }
/* 201 */     else if (paramKeySpec instanceof OpenSSHPublicKeySpec) {
/*     */       
/* 203 */       AsymmetricKeyParameter asymmetricKeyParameter = OpenSSHPublicKeyUtil.parsePublicKey(((OpenSSHPublicKeySpec)paramKeySpec).getEncoded());
/* 204 */       if (asymmetricKeyParameter instanceof Ed25519PublicKeyParameters)
/*     */       {
/* 206 */         return (PublicKey)new BC15EdDSAPublicKey(new byte[0], ((Ed25519PublicKeyParameters)asymmetricKeyParameter).getEncoded());
/*     */       }
/*     */       
/* 209 */       throw new IllegalStateException("openssh public key not Ed25519 public key");
/*     */     } 
/*     */     
/* 212 */     return super.engineGeneratePublic(paramKeySpec);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey generatePrivate(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 218 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = paramPrivateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
/*     */     
/* 220 */     if (this.isXdh) {
/*     */       
/* 222 */       if ((this.specificBase == 0 || this.specificBase == 111) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_X448))
/*     */       {
/* 224 */         return (PrivateKey)new BC11XDHPrivateKey(paramPrivateKeyInfo);
/*     */       }
/* 226 */       if ((this.specificBase == 0 || this.specificBase == 110) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_X25519))
/*     */       {
/* 228 */         return (PrivateKey)new BC11XDHPrivateKey(paramPrivateKeyInfo);
/*     */       }
/*     */     }
/* 231 */     else if (aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed448) || aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed25519)) {
/*     */       
/* 233 */       if ((this.specificBase == 0 || this.specificBase == 113) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed448))
/*     */       {
/* 235 */         return (PrivateKey)new BC15EdDSAPrivateKey(paramPrivateKeyInfo);
/*     */       }
/* 237 */       if ((this.specificBase == 0 || this.specificBase == 112) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed25519))
/*     */       {
/* 239 */         return (PrivateKey)new BC15EdDSAPrivateKey(paramPrivateKeyInfo);
/*     */       }
/*     */     } 
/*     */     
/* 243 */     throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognized");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicKey generatePublic(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/* 249 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = paramSubjectPublicKeyInfo.getAlgorithm().getAlgorithm();
/*     */     
/* 251 */     if (this.isXdh) {
/*     */       
/* 253 */       if ((this.specificBase == 0 || this.specificBase == 111) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_X448))
/*     */       {
/* 255 */         return (PublicKey)new BC11XDHPublicKey(paramSubjectPublicKeyInfo);
/*     */       }
/* 257 */       if ((this.specificBase == 0 || this.specificBase == 110) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_X25519))
/*     */       {
/* 259 */         return (PublicKey)new BC11XDHPublicKey(paramSubjectPublicKeyInfo);
/*     */       }
/*     */     }
/* 262 */     else if (aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed448) || aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed25519)) {
/*     */       
/* 264 */       if ((this.specificBase == 0 || this.specificBase == 113) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed448))
/*     */       {
/* 266 */         return (PublicKey)new BC15EdDSAPublicKey(paramSubjectPublicKeyInfo);
/*     */       }
/* 268 */       if ((this.specificBase == 0 || this.specificBase == 112) && aSN1ObjectIdentifier.equals((ASN1Primitive)EdECObjectIdentifiers.id_Ed25519))
/*     */       {
/* 270 */         return (PublicKey)new BC15EdDSAPublicKey(paramSubjectPublicKeyInfo);
/*     */       }
/*     */     } 
/*     */     
/* 274 */     throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognized");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/15/org/bouncycastle/jcajce/provider/asymmetric/edec/KeyFactorySpi.class
 * Java compiler version: 15 (59.0)
 * JD-Core Version:       1.1.3
 */