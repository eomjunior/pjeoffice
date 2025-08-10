/*     */ package META-INF.versions.11.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.interfaces.XECPrivateKey;
/*     */ import java.security.interfaces.XECPublicKey;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.DerivationFunction;
/*     */ import org.bouncycastle.crypto.RawAgreement;
/*     */ import org.bouncycastle.crypto.agreement.X25519Agreement;
/*     */ import org.bouncycastle.crypto.agreement.X448Agreement;
/*     */ import org.bouncycastle.crypto.agreement.XDHUnifiedAgreement;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
/*     */ import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
/*     */ import org.bouncycastle.crypto.params.X448PrivateKeyParameters;
/*     */ import org.bouncycastle.crypto.params.X448PublicKeyParameters;
/*     */ import org.bouncycastle.crypto.params.XDHUPrivateParameters;
/*     */ import org.bouncycastle.crypto.params.XDHUPublicParameters;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCXDHPrivateKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCXDHPublicKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.BaseAgreementSpi;
/*     */ import org.bouncycastle.jcajce.spec.DHUParameterSpec;
/*     */ import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.BigIntegers;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeyAgreementSpi
/*     */   extends BaseAgreementSpi
/*     */ {
/*     */   private RawAgreement agreement;
/*     */   private DHUParameterSpec dhuSpec;
/*     */   private byte[] result;
/*     */   
/*     */   KeyAgreementSpi(String paramString) {
/*  41 */     super(paramString, null);
/*     */   }
/*     */ 
/*     */   
/*     */   KeyAgreementSpi(String paramString, DerivationFunction paramDerivationFunction) {
/*  46 */     super(paramString, paramDerivationFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] calcSecret() {
/*  51 */     return this.result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void engineInit(Key paramKey, SecureRandom paramSecureRandom) throws InvalidKeyException {
/*  57 */     AsymmetricKeyParameter asymmetricKeyParameter = getLwXDHKey(paramKey);
/*     */     
/*  59 */     if (asymmetricKeyParameter instanceof X448PrivateKeyParameters) {
/*     */       
/*  61 */       this.agreement = getAgreement("X448");
/*     */     }
/*     */     else {
/*     */       
/*  65 */       this.agreement = getAgreement("X25519");
/*     */     } 
/*     */     
/*  68 */     this.agreement.init((CipherParameters)asymmetricKeyParameter);
/*  69 */     if (this.kdf != null) {
/*     */       
/*  71 */       this.ukmParameters = new byte[0];
/*     */     }
/*     */     else {
/*     */       
/*  75 */       this.ukmParameters = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void engineInit(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
/*  82 */     AsymmetricKeyParameter asymmetricKeyParameter = getLwXDHKey(paramKey);
/*     */     
/*  84 */     if (asymmetricKeyParameter instanceof X448PrivateKeyParameters) {
/*     */       
/*  86 */       this.agreement = getAgreement("X448");
/*     */     }
/*     */     else {
/*     */       
/*  90 */       this.agreement = getAgreement("X25519");
/*     */     } 
/*     */     
/*  93 */     this.ukmParameters = null;
/*  94 */     if (paramAlgorithmParameterSpec instanceof DHUParameterSpec) {
/*     */       
/*  96 */       if (this.kaAlgorithm.indexOf('U') < 0)
/*     */       {
/*  98 */         throw new InvalidAlgorithmParameterException("agreement algorithm not DHU based");
/*     */       }
/*     */       
/* 101 */       this.dhuSpec = (DHUParameterSpec)paramAlgorithmParameterSpec;
/*     */       
/* 103 */       this.ukmParameters = this.dhuSpec.getUserKeyingMaterial();
/*     */       
/* 105 */       this.agreement.init((CipherParameters)new XDHUPrivateParameters(asymmetricKeyParameter, ((BCXDHPrivateKey)this.dhuSpec
/* 106 */             .getEphemeralPrivateKey()).engineGetKeyParameters(), ((BCXDHPublicKey)this.dhuSpec
/* 107 */             .getEphemeralPublicKey()).engineGetKeyParameters()));
/*     */     }
/*     */     else {
/*     */       
/* 111 */       this.agreement.init((CipherParameters)asymmetricKeyParameter);
/*     */       
/* 113 */       if (paramAlgorithmParameterSpec instanceof UserKeyingMaterialSpec) {
/*     */         
/* 115 */         if (this.kdf == null)
/*     */         {
/* 117 */           throw new InvalidAlgorithmParameterException("no KDF specified for UserKeyingMaterialSpec");
/*     */         }
/* 119 */         this.ukmParameters = ((UserKeyingMaterialSpec)paramAlgorithmParameterSpec).getUserKeyingMaterial();
/*     */       }
/*     */       else {
/*     */         
/* 123 */         throw new InvalidAlgorithmParameterException("unknown ParameterSpec");
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     if (this.kdf != null && this.ukmParameters == null)
/*     */     {
/* 129 */       this.ukmParameters = new byte[0];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Key engineDoPhase(Key paramKey, boolean paramBoolean) throws InvalidKeyException, IllegalStateException {
/*     */     X25519PublicKeyParameters x25519PublicKeyParameters;
/* 136 */     if (this.agreement == null)
/*     */     {
/* 138 */       throw new IllegalStateException(this.kaAlgorithm + " not initialised.");
/*     */     }
/*     */     
/* 141 */     if (!paramBoolean)
/*     */     {
/* 143 */       throw new IllegalStateException(this.kaAlgorithm + " can only be between two parties.");
/*     */     }
/*     */ 
/*     */     
/* 147 */     if (paramKey instanceof BCXDHPublicKey) {
/*     */       
/* 149 */       AsymmetricKeyParameter asymmetricKeyParameter = ((BCXDHPublicKey)paramKey).engineGetKeyParameters();
/*     */     }
/* 151 */     else if (paramKey instanceof XECPublicKey) {
/*     */       
/* 153 */       XECPublicKey xECPublicKey = (XECPublicKey)paramKey;
/*     */       
/* 155 */       byte[] arrayOfByte = Arrays.reverse(BigIntegers.asUnsignedByteArray(xECPublicKey.getU()));
/*     */       
/* 157 */       if (arrayOfByte.length == 56)
/*     */       {
/* 159 */         X448PublicKeyParameters x448PublicKeyParameters = new X448PublicKeyParameters(arrayOfByte, 0);
/*     */       }
/*     */       else
/*     */       {
/* 163 */         x25519PublicKeyParameters = new X25519PublicKeyParameters(arrayOfByte, 0);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 168 */       throw new InvalidKeyException("cannot identify XDH private key");
/*     */     } 
/*     */     
/* 171 */     this.result = new byte[this.agreement.getAgreementSize()];
/*     */     
/* 173 */     if (this.dhuSpec != null) {
/*     */       
/* 175 */       this.agreement.calculateAgreement((CipherParameters)new XDHUPublicParameters((AsymmetricKeyParameter)x25519PublicKeyParameters, ((BCXDHPublicKey)this.dhuSpec.getOtherPartyEphemeralKey()).engineGetKeyParameters()), this.result, 0);
/*     */     }
/*     */     else {
/*     */       
/* 179 */       this.agreement.calculateAgreement((CipherParameters)x25519PublicKeyParameters, this.result, 0);
/*     */     } 
/*     */     
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RawAgreement getAgreement(String paramString) throws InvalidKeyException {
/* 188 */     if (!this.kaAlgorithm.equals("XDH") && !this.kaAlgorithm.startsWith(paramString))
/*     */     {
/* 190 */       throw new InvalidKeyException("inappropriate key for " + this.kaAlgorithm);
/*     */     }
/*     */     
/* 193 */     if (this.kaAlgorithm.indexOf('U') > 0) {
/*     */       
/* 195 */       if (paramString.startsWith("X448"))
/*     */       {
/* 197 */         return (RawAgreement)new XDHUnifiedAgreement((RawAgreement)new X448Agreement());
/*     */       }
/*     */ 
/*     */       
/* 201 */       return (RawAgreement)new XDHUnifiedAgreement((RawAgreement)new X25519Agreement());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 206 */     if (paramString.startsWith("X448"))
/*     */     {
/* 208 */       return (RawAgreement)new X448Agreement();
/*     */     }
/*     */ 
/*     */     
/* 212 */     return (RawAgreement)new X25519Agreement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AsymmetricKeyParameter getLwXDHKey(Key paramKey) throws InvalidKeyException {
/*     */     X25519PrivateKeyParameters x25519PrivateKeyParameters;
/* 221 */     if (paramKey instanceof BCXDHPrivateKey) {
/*     */       
/* 223 */       AsymmetricKeyParameter asymmetricKeyParameter = ((BCXDHPrivateKey)paramKey).engineGetKeyParameters();
/*     */     }
/* 225 */     else if (paramKey instanceof XECPrivateKey) {
/*     */       
/* 227 */       XECPrivateKey xECPrivateKey = (XECPrivateKey)paramKey;
/*     */       
/* 229 */       if (xECPrivateKey.getScalar().isPresent()) {
/*     */         
/* 231 */         byte[] arrayOfByte = xECPrivateKey.getScalar().get();
/*     */         
/* 233 */         if (arrayOfByte.length == 56)
/*     */         {
/* 235 */           X448PrivateKeyParameters x448PrivateKeyParameters = new X448PrivateKeyParameters(arrayOfByte, 0);
/*     */         }
/*     */         else
/*     */         {
/* 239 */           x25519PrivateKeyParameters = new X25519PrivateKeyParameters(arrayOfByte, 0);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 244 */         throw new InvalidKeyException("cannot use other provider XEC private key");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 249 */       throw new InvalidKeyException("cannot identify XDH private key");
/*     */     } 
/* 251 */     return (AsymmetricKeyParameter)x25519PrivateKeyParameters;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/11/org/bouncycastle/jcajce/provider/asymmetric/edec/KeyAgreementSpi.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */