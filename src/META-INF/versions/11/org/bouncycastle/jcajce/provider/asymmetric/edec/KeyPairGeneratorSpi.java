/*     */ package META-INF.versions.11.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGeneratorSpi;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.ECGenParameterSpec;
/*     */ import java.security.spec.NamedParameterSpec;
/*     */ import org.bouncycastle.asn1.edec.EdECObjectIdentifiers;
/*     */ import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
/*     */ import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
/*     */ import org.bouncycastle.crypto.CryptoServicesRegistrar;
/*     */ import org.bouncycastle.crypto.KeyGenerationParameters;
/*     */ import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
/*     */ import org.bouncycastle.crypto.generators.Ed448KeyPairGenerator;
/*     */ import org.bouncycastle.crypto.generators.X25519KeyPairGenerator;
/*     */ import org.bouncycastle.crypto.generators.X448KeyPairGenerator;
/*     */ import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
/*     */ import org.bouncycastle.crypto.params.Ed448KeyGenerationParameters;
/*     */ import org.bouncycastle.crypto.params.X25519KeyGenerationParameters;
/*     */ import org.bouncycastle.crypto.params.X448KeyGenerationParameters;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC11XDHPrivateKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC11XDHPublicKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPrivateKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
/*     */ import org.bouncycastle.jcajce.spec.EdDSAParameterSpec;
/*     */ import org.bouncycastle.jcajce.spec.XDHParameterSpec;
/*     */ import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
/*     */ 
/*     */ public class KeyPairGeneratorSpi
/*     */   extends KeyPairGeneratorSpi {
/*     */   private static final int EdDSA = -1;
/*     */   private static final int XDH = -2;
/*     */   private static final int Ed448 = 0;
/*     */   private static final int Ed25519 = 1;
/*     */   private static final int X448 = 2;
/*     */   private static final int X25519 = 3;
/*     */   private int algorithm;
/*     */   private AsymmetricCipherKeyPairGenerator generator;
/*     */   private boolean initialised;
/*     */   private SecureRandom secureRandom;
/*     */   
/*     */   KeyPairGeneratorSpi(int paramInt, AsymmetricCipherKeyPairGenerator paramAsymmetricCipherKeyPairGenerator) {
/*  49 */     this.algorithm = paramInt;
/*  50 */     this.generator = paramAsymmetricCipherKeyPairGenerator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(int paramInt, SecureRandom paramSecureRandom) {
/*  55 */     this.secureRandom = paramSecureRandom;
/*     */     
/*     */     try {
/*  58 */       switch (paramInt) {
/*     */         
/*     */         case 255:
/*     */         case 256:
/*  62 */           switch (this.algorithm) {
/*     */             
/*     */             case -1:
/*     */             case 1:
/*  66 */               algorithmCheck(1);
/*  67 */               this.generator = (AsymmetricCipherKeyPairGenerator)new Ed25519KeyPairGenerator();
/*  68 */               setupGenerator(1);
/*     */               return;
/*     */             case -2:
/*     */             case 3:
/*  72 */               algorithmCheck(3);
/*  73 */               this.generator = (AsymmetricCipherKeyPairGenerator)new X25519KeyPairGenerator();
/*  74 */               setupGenerator(3);
/*     */               return;
/*     */           } 
/*  77 */           throw new InvalidParameterException("key size not configurable");
/*     */ 
/*     */         
/*     */         case 448:
/*  81 */           switch (this.algorithm) {
/*     */             
/*     */             case -1:
/*     */             case 0:
/*  85 */               algorithmCheck(0);
/*  86 */               this.generator = (AsymmetricCipherKeyPairGenerator)new Ed448KeyPairGenerator();
/*  87 */               setupGenerator(0);
/*     */               return;
/*     */             case -2:
/*     */             case 2:
/*  91 */               algorithmCheck(2);
/*  92 */               this.generator = (AsymmetricCipherKeyPairGenerator)new X448KeyPairGenerator();
/*  93 */               setupGenerator(2);
/*     */               return;
/*     */           } 
/*  96 */           throw new InvalidParameterException("key size not configurable");
/*     */       } 
/*     */ 
/*     */       
/* 100 */       throw new InvalidParameterException("unknown key size");
/*     */     
/*     */     }
/* 103 */     catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
/*     */       
/* 105 */       throw new InvalidParameterException(invalidAlgorithmParameterException.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom) throws InvalidAlgorithmParameterException {
/* 112 */     this.secureRandom = paramSecureRandom;
/*     */     
/* 114 */     if (paramAlgorithmParameterSpec instanceof ECGenParameterSpec) {
/*     */       
/* 116 */       initializeGenerator(((ECGenParameterSpec)paramAlgorithmParameterSpec).getName());
/*     */     }
/* 118 */     else if (paramAlgorithmParameterSpec instanceof ECNamedCurveGenParameterSpec) {
/*     */       
/* 120 */       initializeGenerator(((ECNamedCurveGenParameterSpec)paramAlgorithmParameterSpec).getName());
/*     */     }
/* 122 */     else if (paramAlgorithmParameterSpec instanceof NamedParameterSpec) {
/*     */       
/* 124 */       initializeGenerator(((NamedParameterSpec)paramAlgorithmParameterSpec).getName());
/*     */     }
/* 126 */     else if (paramAlgorithmParameterSpec instanceof EdDSAParameterSpec) {
/*     */       
/* 128 */       initializeGenerator(((EdDSAParameterSpec)paramAlgorithmParameterSpec).getCurveName());
/*     */     }
/* 130 */     else if (paramAlgorithmParameterSpec instanceof XDHParameterSpec) {
/*     */       
/* 132 */       initializeGenerator(((XDHParameterSpec)paramAlgorithmParameterSpec).getCurveName());
/*     */     }
/*     */     else {
/*     */       
/* 136 */       String str = ECUtil.getNameFrom(paramAlgorithmParameterSpec);
/*     */       
/* 138 */       if (str != null) {
/*     */         
/* 140 */         initializeGenerator(str);
/*     */       }
/*     */       else {
/*     */         
/* 144 */         throw new InvalidAlgorithmParameterException("invalid parameterSpec: " + paramAlgorithmParameterSpec);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void algorithmCheck(int paramInt) throws InvalidAlgorithmParameterException {
/* 152 */     if (this.algorithm != paramInt) {
/*     */       
/* 154 */       if (this.algorithm == 1 || this.algorithm == 0)
/*     */       {
/* 156 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/* 158 */       if (this.algorithm == -1 && paramInt != 1 && paramInt != 0)
/*     */       {
/* 160 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/* 162 */       if (this.algorithm == 3 || this.algorithm == 2)
/*     */       {
/* 164 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/* 166 */       if (this.algorithm == -2 && paramInt != 3 && paramInt != 2)
/*     */       {
/* 168 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeGenerator(String paramString) throws InvalidAlgorithmParameterException {
/* 176 */     if (paramString.equalsIgnoreCase("Ed448") || paramString.equals(EdECObjectIdentifiers.id_Ed448.getId())) {
/*     */       
/* 178 */       algorithmCheck(0);
/* 179 */       this.generator = (AsymmetricCipherKeyPairGenerator)new Ed448KeyPairGenerator();
/* 180 */       setupGenerator(0);
/*     */     }
/* 182 */     else if (paramString.equalsIgnoreCase("Ed25519") || paramString.equals(EdECObjectIdentifiers.id_Ed25519.getId())) {
/*     */       
/* 184 */       algorithmCheck(1);
/* 185 */       this.generator = (AsymmetricCipherKeyPairGenerator)new Ed25519KeyPairGenerator();
/* 186 */       setupGenerator(1);
/*     */     }
/* 188 */     else if (paramString.equalsIgnoreCase("X448") || paramString.equals(EdECObjectIdentifiers.id_X448.getId())) {
/*     */       
/* 190 */       algorithmCheck(2);
/* 191 */       this.generator = (AsymmetricCipherKeyPairGenerator)new X448KeyPairGenerator();
/* 192 */       setupGenerator(2);
/*     */     }
/* 194 */     else if (paramString.equalsIgnoreCase("X25519") || paramString.equals(EdECObjectIdentifiers.id_X25519.getId())) {
/*     */       
/* 196 */       algorithmCheck(3);
/* 197 */       this.generator = (AsymmetricCipherKeyPairGenerator)new X25519KeyPairGenerator();
/* 198 */       setupGenerator(3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyPair generateKeyPair() {
/* 204 */     if (this.generator == null)
/*     */     {
/* 206 */       throw new IllegalStateException("generator not correctly initialized");
/*     */     }
/*     */     
/* 209 */     if (!this.initialised)
/*     */     {
/* 211 */       setupGenerator(this.algorithm);
/*     */     }
/*     */     
/* 214 */     AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.generator.generateKeyPair();
/*     */     
/* 216 */     if (asymmetricCipherKeyPair.getPrivate() instanceof org.bouncycastle.crypto.params.X448PrivateKeyParameters || asymmetricCipherKeyPair
/* 217 */       .getPrivate() instanceof org.bouncycastle.crypto.params.X25519PrivateKeyParameters)
/*     */     {
/* 219 */       return new KeyPair((PublicKey)new BC11XDHPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BC11XDHPrivateKey(asymmetricCipherKeyPair.getPrivate()));
/*     */     }
/*     */ 
/*     */     
/* 223 */     return new KeyPair((PublicKey)new BCEdDSAPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BCEdDSAPrivateKey(asymmetricCipherKeyPair.getPrivate()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupGenerator(int paramInt) {
/* 229 */     this.initialised = true;
/*     */     
/* 231 */     if (this.secureRandom == null)
/*     */     {
/* 233 */       this.secureRandom = CryptoServicesRegistrar.getSecureRandom();
/*     */     }
/*     */     
/* 236 */     switch (paramInt) {
/*     */       
/*     */       case 0:
/* 239 */         this.generator.init((KeyGenerationParameters)new Ed448KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */       case -1:
/*     */       case 1:
/* 243 */         this.generator.init((KeyGenerationParameters)new Ed25519KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */       case 2:
/* 246 */         this.generator.init((KeyGenerationParameters)new X448KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */       case -2:
/*     */       case 3:
/* 250 */         this.generator.init((KeyGenerationParameters)new X25519KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/11/org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */