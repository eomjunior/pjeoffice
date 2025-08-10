/*     */ package META-INF.versions.15.org.bouncycastle.jcajce.provider.asymmetric.edec;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ import java.security.SignatureSpi;
/*     */ import java.security.interfaces.EdECPrivateKey;
/*     */ import java.security.interfaces.EdECPublicKey;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.CryptoException;
/*     */ import org.bouncycastle.crypto.Signer;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
/*     */ import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
/*     */ import org.bouncycastle.crypto.params.Ed448PrivateKeyParameters;
/*     */ import org.bouncycastle.crypto.params.Ed448PublicKeyParameters;
/*     */ import org.bouncycastle.crypto.signers.Ed25519Signer;
/*     */ import org.bouncycastle.crypto.signers.Ed448Signer;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPrivateKey;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.BigIntegers;
/*     */ 
/*     */ public class SignatureSpi extends SignatureSpi {
/*  27 */   private static final byte[] EMPTY_CONTEXT = new byte[0];
/*     */   
/*     */   private final String algorithm;
/*     */   
/*     */   private Signer signer;
/*     */ 
/*     */   
/*     */   SignatureSpi(String paramString) {
/*  35 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void engineInitVerify(PublicKey paramPublicKey) throws InvalidKeyException {
/*     */     Ed25519PublicKeyParameters ed25519PublicKeyParameters;
/*  42 */     if (paramPublicKey instanceof BCEdDSAPublicKey) {
/*     */       
/*  44 */       AsymmetricKeyParameter asymmetricKeyParameter = ((BCEdDSAPublicKey)paramPublicKey).engineGetKeyParameters();
/*     */     }
/*  46 */     else if (paramPublicKey instanceof EdECPublicKey) {
/*     */       
/*  48 */       EdECPublicKey edECPublicKey = (EdECPublicKey)paramPublicKey;
/*     */       
/*  50 */       byte[] arrayOfByte = Arrays.reverse(BigIntegers.asUnsignedByteArray(edECPublicKey.getPoint().getY()));
/*     */       
/*  52 */       if (arrayOfByte.length == 57)
/*     */       {
/*  54 */         Ed448PublicKeyParameters ed448PublicKeyParameters = new Ed448PublicKeyParameters(arrayOfByte, 0);
/*     */       }
/*     */       else
/*     */       {
/*  58 */         ed25519PublicKeyParameters = new Ed25519PublicKeyParameters(arrayOfByte, 0);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  63 */       throw new InvalidKeyException("cannot identify EdDSA public key");
/*     */     } 
/*     */     
/*  66 */     if (ed25519PublicKeyParameters instanceof Ed448PublicKeyParameters) {
/*     */       
/*  68 */       this.signer = getSigner("Ed448");
/*     */     }
/*     */     else {
/*     */       
/*  72 */       this.signer = getSigner("Ed25519");
/*     */     } 
/*     */     
/*  75 */     this.signer.init(false, (CipherParameters)ed25519PublicKeyParameters);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void engineInitSign(PrivateKey paramPrivateKey) throws InvalidKeyException {
/*     */     Ed25519PrivateKeyParameters ed25519PrivateKeyParameters;
/*  82 */     if (paramPrivateKey instanceof BCEdDSAPrivateKey) {
/*     */       
/*  84 */       AsymmetricKeyParameter asymmetricKeyParameter = ((BCEdDSAPrivateKey)paramPrivateKey).engineGetKeyParameters();
/*     */     }
/*  86 */     else if (paramPrivateKey instanceof EdECPrivateKey) {
/*     */       
/*  88 */       EdECPrivateKey edECPrivateKey = (EdECPrivateKey)paramPrivateKey;
/*     */       
/*  90 */       if (edECPrivateKey.getBytes().isPresent()) {
/*     */         
/*  92 */         byte[] arrayOfByte = edECPrivateKey.getBytes().get();
/*     */         
/*  94 */         if (arrayOfByte.length == 57)
/*     */         {
/*  96 */           Ed448PrivateKeyParameters ed448PrivateKeyParameters = new Ed448PrivateKeyParameters(arrayOfByte, 0);
/*     */         }
/*     */         else
/*     */         {
/* 100 */           ed25519PrivateKeyParameters = new Ed25519PrivateKeyParameters(arrayOfByte, 0);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 105 */         throw new InvalidKeyException("cannot use other provider EdDSA private key");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 110 */       throw new InvalidKeyException("cannot identify EdDSA private key");
/*     */     } 
/*     */     
/* 113 */     if (ed25519PrivateKeyParameters instanceof Ed448PrivateKeyParameters) {
/*     */       
/* 115 */       this.signer = getSigner("Ed448");
/*     */     }
/*     */     else {
/*     */       
/* 119 */       this.signer = getSigner("Ed25519");
/*     */     } 
/*     */     
/* 122 */     this.signer.init(true, (CipherParameters)ed25519PrivateKeyParameters);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Signer getSigner(String paramString) throws InvalidKeyException {
/* 128 */     if (this.algorithm != null && !paramString.equals(this.algorithm))
/*     */     {
/* 130 */       throw new InvalidKeyException("inappropriate key for " + this.algorithm);
/*     */     }
/*     */     
/* 133 */     if (paramString.equals("Ed448"))
/*     */     {
/* 135 */       return (Signer)new Ed448Signer(EMPTY_CONTEXT);
/*     */     }
/*     */ 
/*     */     
/* 139 */     return (Signer)new Ed25519Signer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void engineUpdate(byte paramByte) throws SignatureException {
/* 146 */     this.signer.update(paramByte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void engineUpdate(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws SignatureException {
/* 152 */     this.signer.update(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] engineSign() throws SignatureException {
/*     */     try {
/* 160 */       return this.signer.generateSignature();
/*     */     }
/* 162 */     catch (CryptoException cryptoException) {
/*     */       
/* 164 */       throw new SignatureException(cryptoException.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean engineVerify(byte[] paramArrayOfbyte) throws SignatureException {
/* 171 */     return this.signer.verifySignature(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void engineSetParameter(String paramString, Object paramObject) throws InvalidParameterException {
/* 177 */     throw new UnsupportedOperationException("engineSetParameter unsupported");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object engineGetParameter(String paramString) throws InvalidParameterException {
/* 183 */     throw new UnsupportedOperationException("engineGetParameter unsupported");
/*     */   }
/*     */ 
/*     */   
/*     */   protected AlgorithmParameters engineGetParameters() {
/* 188 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/15/org/bouncycastle/jcajce/provider/asymmetric/edec/SignatureSpi.class
 * Java compiler version: 15 (59.0)
 * JD-Core Version:       1.1.3
 */