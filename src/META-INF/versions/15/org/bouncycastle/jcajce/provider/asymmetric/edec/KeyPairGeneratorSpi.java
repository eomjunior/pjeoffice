/*     */ package META-INF.versions.15.org.bouncycastle.jcajce.provider.asymmetric.edec;
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
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC15EdDSAPrivateKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BC15EdDSAPublicKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
/*     */ import org.bouncycastle.jcajce.spec.EdDSAParameterSpec;
/*     */ import org.bouncycastle.jcajce.spec.XDHParameterSpec;
/*     */ import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeyPairGeneratorSpi
/*     */   extends KeyPairGeneratorSpi
/*     */ {
/*     */   private static final int EdDSA = -1;
/*     */   private static final int XDH = -2;
/*     */   private static final int Ed448 = 0;
/*     */   private static final int Ed25519 = 1;
/*     */   private static final int X448 = 2;
/*     */   
/*     */   KeyPairGeneratorSpi(int paramInt, AsymmetricCipherKeyPairGenerator paramAsymmetricCipherKeyPairGenerator) {
/*  47 */     this.algorithm = paramInt;
/*  48 */     this.generator = paramAsymmetricCipherKeyPairGenerator;
/*     */   }
/*     */   private static final int X25519 = 3; private int algorithm; private AsymmetricCipherKeyPairGenerator generator; private boolean initialised; private SecureRandom secureRandom;
/*     */   
/*     */   public void initialize(int paramInt, SecureRandom paramSecureRandom) {
/*  53 */     this.secureRandom = paramSecureRandom;
/*     */     
/*     */     try {
/*  56 */       switch (paramInt) {
/*     */         
/*     */         case 255:
/*     */         case 256:
/*  60 */           switch (this.algorithm) {
/*     */             
/*     */             case -1:
/*     */             case 1:
/*  64 */               algorithmCheck(1);
/*  65 */               this.generator = (AsymmetricCipherKeyPairGenerator)new Ed25519KeyPairGenerator();
/*  66 */               setupGenerator(1);
/*     */               return;
/*     */             case -2:
/*     */             case 3:
/*  70 */               algorithmCheck(3);
/*  71 */               this.generator = (AsymmetricCipherKeyPairGenerator)new X25519KeyPairGenerator();
/*  72 */               setupGenerator(3);
/*     */               return;
/*     */           } 
/*  75 */           throw new InvalidParameterException("key size not configurable");
/*     */ 
/*     */         
/*     */         case 448:
/*  79 */           switch (this.algorithm) {
/*     */             
/*     */             case -1:
/*     */             case 0:
/*  83 */               algorithmCheck(0);
/*  84 */               this.generator = (AsymmetricCipherKeyPairGenerator)new Ed448KeyPairGenerator();
/*  85 */               setupGenerator(0);
/*     */               return;
/*     */             case -2:
/*     */             case 2:
/*  89 */               algorithmCheck(2);
/*  90 */               this.generator = (AsymmetricCipherKeyPairGenerator)new X448KeyPairGenerator();
/*  91 */               setupGenerator(2);
/*     */               return;
/*     */           } 
/*  94 */           throw new InvalidParameterException("key size not configurable");
/*     */       } 
/*     */ 
/*     */       
/*  98 */       throw new InvalidParameterException("unknown key size");
/*     */     
/*     */     }
/* 101 */     catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
/*     */       
/* 103 */       throw new InvalidParameterException(invalidAlgorithmParameterException.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom) throws InvalidAlgorithmParameterException {
/* 110 */     this.secureRandom = paramSecureRandom;
/*     */     
/* 112 */     if (paramAlgorithmParameterSpec instanceof ECGenParameterSpec) {
/*     */       
/* 114 */       initializeGenerator(((ECGenParameterSpec)paramAlgorithmParameterSpec).getName());
/*     */     }
/* 116 */     else if (paramAlgorithmParameterSpec instanceof ECNamedCurveGenParameterSpec) {
/*     */       
/* 118 */       initializeGenerator(((ECNamedCurveGenParameterSpec)paramAlgorithmParameterSpec).getName());
/*     */     }
/* 120 */     else if (paramAlgorithmParameterSpec instanceof NamedParameterSpec) {
/*     */       
/* 122 */       initializeGenerator(((NamedParameterSpec)paramAlgorithmParameterSpec).getName());
/*     */     }
/* 124 */     else if (paramAlgorithmParameterSpec instanceof EdDSAParameterSpec) {
/*     */       
/* 126 */       initializeGenerator(((EdDSAParameterSpec)paramAlgorithmParameterSpec).getCurveName());
/*     */     }
/* 128 */     else if (paramAlgorithmParameterSpec instanceof XDHParameterSpec) {
/*     */       
/* 130 */       initializeGenerator(((XDHParameterSpec)paramAlgorithmParameterSpec).getCurveName());
/*     */     }
/*     */     else {
/*     */       
/* 134 */       String str = ECUtil.getNameFrom(paramAlgorithmParameterSpec);
/*     */       
/* 136 */       if (str != null) {
/*     */         
/* 138 */         initializeGenerator(str);
/*     */       }
/*     */       else {
/*     */         
/* 142 */         throw new InvalidAlgorithmParameterException("invalid parameterSpec: " + paramAlgorithmParameterSpec);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void algorithmCheck(int paramInt) throws InvalidAlgorithmParameterException {
/* 150 */     if (this.algorithm != paramInt) {
/*     */       
/* 152 */       if (this.algorithm == 1 || this.algorithm == 0)
/*     */       {
/* 154 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/* 156 */       if (this.algorithm == -1 && paramInt != 1 && paramInt != 0)
/*     */       {
/* 158 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/* 160 */       if (this.algorithm == 3 || this.algorithm == 2)
/*     */       {
/* 162 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/* 164 */       if (this.algorithm == -2 && paramInt != 3 && paramInt != 2)
/*     */       {
/* 166 */         throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type");
/*     */       }
/* 168 */       this.algorithm = paramInt;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeGenerator(String paramString) throws InvalidAlgorithmParameterException {
/* 175 */     if (paramString.equalsIgnoreCase("Ed448") || paramString.equals(EdECObjectIdentifiers.id_Ed448.getId())) {
/*     */       
/* 177 */       algorithmCheck(0);
/* 178 */       this.generator = (AsymmetricCipherKeyPairGenerator)new Ed448KeyPairGenerator();
/* 179 */       setupGenerator(0);
/*     */     }
/* 181 */     else if (paramString.equalsIgnoreCase("Ed25519") || paramString.equals(EdECObjectIdentifiers.id_Ed25519.getId())) {
/*     */       
/* 183 */       algorithmCheck(1);
/* 184 */       this.generator = (AsymmetricCipherKeyPairGenerator)new Ed25519KeyPairGenerator();
/* 185 */       setupGenerator(1);
/*     */     }
/* 187 */     else if (paramString.equalsIgnoreCase("X448") || paramString.equals(EdECObjectIdentifiers.id_X448.getId())) {
/*     */       
/* 189 */       algorithmCheck(2);
/* 190 */       this.generator = (AsymmetricCipherKeyPairGenerator)new X448KeyPairGenerator();
/* 191 */       setupGenerator(2);
/*     */     }
/* 193 */     else if (paramString.equalsIgnoreCase("X25519") || paramString.equals(EdECObjectIdentifiers.id_X25519.getId())) {
/*     */       
/* 195 */       algorithmCheck(3);
/* 196 */       this.generator = (AsymmetricCipherKeyPairGenerator)new X25519KeyPairGenerator();
/* 197 */       setupGenerator(3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyPair generateKeyPair() {
/* 203 */     if (this.generator == null)
/*     */     {
/* 205 */       throw new IllegalStateException("generator not correctly initialized");
/*     */     }
/*     */     
/* 208 */     if (!this.initialised)
/*     */     {
/* 210 */       setupGenerator(this.algorithm);
/*     */     }
/*     */     
/* 213 */     AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.generator.generateKeyPair();
/*     */     
/* 215 */     switch (this.algorithm) {
/*     */       
/*     */       case 0:
/* 218 */         return new KeyPair((PublicKey)new BC15EdDSAPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BC15EdDSAPrivateKey(asymmetricCipherKeyPair.getPrivate()));
/*     */       case 1:
/* 220 */         return new KeyPair((PublicKey)new BC15EdDSAPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BC15EdDSAPrivateKey(asymmetricCipherKeyPair.getPrivate()));
/*     */       case 2:
/* 222 */         return new KeyPair((PublicKey)new BC11XDHPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BC11XDHPrivateKey(asymmetricCipherKeyPair.getPrivate()));
/*     */       case 3:
/* 224 */         return new KeyPair((PublicKey)new BC11XDHPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BC11XDHPrivateKey(asymmetricCipherKeyPair.getPrivate()));
/*     */     } 
/*     */     
/* 227 */     throw new IllegalStateException("generator not correctly initialized");
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupGenerator(int paramInt) {
/* 232 */     this.initialised = true;
/*     */     
/* 234 */     if (this.secureRandom == null)
/*     */     {
/* 236 */       this.secureRandom = CryptoServicesRegistrar.getSecureRandom();
/*     */     }
/*     */     
/* 239 */     switch (paramInt) {
/*     */       
/*     */       case 0:
/* 242 */         this.generator.init((KeyGenerationParameters)new Ed448KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */       case -1:
/*     */       case 1:
/* 246 */         this.generator.init((KeyGenerationParameters)new Ed25519KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */       case 2:
/* 249 */         this.generator.init((KeyGenerationParameters)new X448KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */       case -2:
/*     */       case 3:
/* 253 */         this.generator.init((KeyGenerationParameters)new X25519KeyGenerationParameters(this.secureRandom));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/15/org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi.class
 * Java compiler version: 15 (59.0)
 * JD-Core Version:       1.1.3
 */