/*     */ package META-INF.versions.9.org.bouncycastle.jcajce.provider.asymmetric.util;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PublicKey;
/*     */ import java.security.interfaces.ECPrivateKey;
/*     */ import java.security.interfaces.ECPublicKey;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.asn1.x9.ECNamedCurveTable;
/*     */ import org.bouncycastle.asn1.x9.X962Parameters;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.crypto.ec.CustomNamedCurves;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.crypto.params.ECDomainParameters;
/*     */ import org.bouncycastle.crypto.params.ECNamedDomainParameters;
/*     */ import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
/*     */ import org.bouncycastle.crypto.params.ECPublicKeyParameters;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
/*     */ import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
/*     */ import org.bouncycastle.jce.interfaces.ECPrivateKey;
/*     */ import org.bouncycastle.jce.interfaces.ECPublicKey;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
/*     */ import org.bouncycastle.jce.spec.ECParameterSpec;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.FixedPointCombMultiplier;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Fingerprint;
/*     */ import org.bouncycastle.util.Strings;
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
/*     */ public class ECUtil
/*     */ {
/*     */   static int[] convertMidTerms(int[] paramArrayOfint) {
/*  55 */     int[] arrayOfInt = new int[3];
/*     */     
/*  57 */     if (paramArrayOfint.length == 1) {
/*     */       
/*  59 */       arrayOfInt[0] = paramArrayOfint[0];
/*     */     }
/*     */     else {
/*     */       
/*  63 */       if (paramArrayOfint.length != 3)
/*     */       {
/*  65 */         throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
/*     */       }
/*     */       
/*  68 */       if (paramArrayOfint[0] < paramArrayOfint[1] && paramArrayOfint[0] < paramArrayOfint[2]) {
/*     */         
/*  70 */         arrayOfInt[0] = paramArrayOfint[0];
/*  71 */         if (paramArrayOfint[1] < paramArrayOfint[2])
/*     */         {
/*  73 */           arrayOfInt[1] = paramArrayOfint[1];
/*  74 */           arrayOfInt[2] = paramArrayOfint[2];
/*     */         }
/*     */         else
/*     */         {
/*  78 */           arrayOfInt[1] = paramArrayOfint[2];
/*  79 */           arrayOfInt[2] = paramArrayOfint[1];
/*     */         }
/*     */       
/*  82 */       } else if (paramArrayOfint[1] < paramArrayOfint[2]) {
/*     */         
/*  84 */         arrayOfInt[0] = paramArrayOfint[1];
/*  85 */         if (paramArrayOfint[0] < paramArrayOfint[2])
/*     */         {
/*  87 */           arrayOfInt[1] = paramArrayOfint[0];
/*  88 */           arrayOfInt[2] = paramArrayOfint[2];
/*     */         }
/*     */         else
/*     */         {
/*  92 */           arrayOfInt[1] = paramArrayOfint[2];
/*  93 */           arrayOfInt[2] = paramArrayOfint[0];
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  98 */         arrayOfInt[0] = paramArrayOfint[2];
/*  99 */         if (paramArrayOfint[0] < paramArrayOfint[1]) {
/*     */           
/* 101 */           arrayOfInt[1] = paramArrayOfint[0];
/* 102 */           arrayOfInt[2] = paramArrayOfint[1];
/*     */         }
/*     */         else {
/*     */           
/* 106 */           arrayOfInt[1] = paramArrayOfint[1];
/* 107 */           arrayOfInt[2] = paramArrayOfint[0];
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECDomainParameters getDomainParameters(ProviderConfiguration paramProviderConfiguration, ECParameterSpec paramECParameterSpec) {
/*     */     ECDomainParameters eCDomainParameters;
/* 121 */     if (paramECParameterSpec instanceof ECNamedCurveParameterSpec) {
/*     */       
/* 123 */       ECNamedCurveParameterSpec eCNamedCurveParameterSpec = (ECNamedCurveParameterSpec)paramECParameterSpec;
/* 124 */       ASN1ObjectIdentifier aSN1ObjectIdentifier = getNamedCurveOid(eCNamedCurveParameterSpec.getName());
/*     */       
/* 126 */       ECNamedDomainParameters eCNamedDomainParameters = new ECNamedDomainParameters(aSN1ObjectIdentifier, eCNamedCurveParameterSpec.getCurve(), eCNamedCurveParameterSpec.getG(), eCNamedCurveParameterSpec.getN(), eCNamedCurveParameterSpec.getH(), eCNamedCurveParameterSpec.getSeed());
/*     */     }
/* 128 */     else if (paramECParameterSpec == null) {
/*     */       
/* 130 */       ECParameterSpec eCParameterSpec = paramProviderConfiguration.getEcImplicitlyCa();
/*     */       
/* 132 */       eCDomainParameters = new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed());
/*     */     }
/*     */     else {
/*     */       
/* 136 */       eCDomainParameters = new ECDomainParameters(paramECParameterSpec.getCurve(), paramECParameterSpec.getG(), paramECParameterSpec.getN(), paramECParameterSpec.getH(), paramECParameterSpec.getSeed());
/*     */     } 
/*     */     
/* 139 */     return eCDomainParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECDomainParameters getDomainParameters(ProviderConfiguration paramProviderConfiguration, X962Parameters paramX962Parameters) {
/*     */     ECDomainParameters eCDomainParameters;
/* 148 */     if (paramX962Parameters.isNamedCurve()) {
/*     */       
/* 150 */       ASN1ObjectIdentifier aSN1ObjectIdentifier = ASN1ObjectIdentifier.getInstance(paramX962Parameters.getParameters());
/* 151 */       X9ECParameters x9ECParameters = getNamedCurveByOid(aSN1ObjectIdentifier);
/* 152 */       if (x9ECParameters == null) {
/*     */         
/* 154 */         Map map = paramProviderConfiguration.getAdditionalECParameters();
/*     */         
/* 156 */         x9ECParameters = (X9ECParameters)map.get(aSN1ObjectIdentifier);
/*     */       } 
/* 158 */       ECNamedDomainParameters eCNamedDomainParameters = new ECNamedDomainParameters(aSN1ObjectIdentifier, x9ECParameters);
/*     */     }
/* 160 */     else if (paramX962Parameters.isImplicitlyCA()) {
/*     */       
/* 162 */       ECParameterSpec eCParameterSpec = paramProviderConfiguration.getEcImplicitlyCa();
/*     */       
/* 164 */       eCDomainParameters = new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed());
/*     */     }
/*     */     else {
/*     */       
/* 168 */       X9ECParameters x9ECParameters = X9ECParameters.getInstance(paramX962Parameters.getParameters());
/*     */       
/* 170 */       eCDomainParameters = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
/*     */     } 
/*     */     
/* 173 */     return eCDomainParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey paramPublicKey) throws InvalidKeyException {
/* 180 */     if (paramPublicKey instanceof ECPublicKey) {
/*     */       
/* 182 */       ECPublicKey eCPublicKey = (ECPublicKey)paramPublicKey;
/* 183 */       ECParameterSpec eCParameterSpec = eCPublicKey.getParameters();
/*     */       
/* 185 */       return (AsymmetricKeyParameter)new ECPublicKeyParameters(eCPublicKey
/* 186 */           .getQ(), new ECDomainParameters(eCParameterSpec
/* 187 */             .getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed()));
/*     */     } 
/* 189 */     if (paramPublicKey instanceof ECPublicKey) {
/*     */       
/* 191 */       ECPublicKey eCPublicKey = (ECPublicKey)paramPublicKey;
/* 192 */       ECParameterSpec eCParameterSpec = EC5Util.convertSpec(eCPublicKey.getParams());
/* 193 */       return (AsymmetricKeyParameter)new ECPublicKeyParameters(
/* 194 */           EC5Util.convertPoint(eCPublicKey.getParams(), eCPublicKey.getW()), new ECDomainParameters(eCParameterSpec
/* 195 */             .getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 202 */       byte[] arrayOfByte = paramPublicKey.getEncoded();
/*     */       
/* 204 */       if (arrayOfByte == null)
/*     */       {
/* 206 */         throw new InvalidKeyException("no encoding for EC public key");
/*     */       }
/*     */       
/* 209 */       PublicKey publicKey = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */       
/* 211 */       if (publicKey instanceof ECPublicKey)
/*     */       {
/* 213 */         return generatePublicKeyParameter(publicKey);
/*     */       }
/*     */     }
/* 216 */     catch (Exception exception) {
/*     */       
/* 218 */       throw new InvalidKeyException("cannot identify EC public key: " + exception.toString());
/*     */     } 
/*     */ 
/*     */     
/* 222 */     throw new InvalidKeyException("cannot identify EC public key.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey paramPrivateKey) throws InvalidKeyException {
/* 229 */     if (paramPrivateKey instanceof ECPrivateKey) {
/*     */       
/* 231 */       ECPrivateKey eCPrivateKey = (ECPrivateKey)paramPrivateKey;
/* 232 */       ECParameterSpec eCParameterSpec = eCPrivateKey.getParameters();
/*     */       
/* 234 */       if (eCParameterSpec == null)
/*     */       {
/* 236 */         eCParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
/*     */       }
/*     */       
/* 239 */       if (eCPrivateKey.getParameters() instanceof ECNamedCurveParameterSpec) {
/*     */         
/* 241 */         String str = ((ECNamedCurveParameterSpec)eCPrivateKey.getParameters()).getName();
/* 242 */         return (AsymmetricKeyParameter)new ECPrivateKeyParameters(eCPrivateKey
/* 243 */             .getD(), (ECDomainParameters)new ECNamedDomainParameters(
/* 244 */               ECNamedCurveTable.getOID(str), eCParameterSpec
/* 245 */               .getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed()));
/*     */       } 
/*     */ 
/*     */       
/* 249 */       return (AsymmetricKeyParameter)new ECPrivateKeyParameters(eCPrivateKey
/* 250 */           .getD(), new ECDomainParameters(eCParameterSpec
/* 251 */             .getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed()));
/*     */     } 
/*     */     
/* 254 */     if (paramPrivateKey instanceof ECPrivateKey) {
/*     */       
/* 256 */       ECPrivateKey eCPrivateKey = (ECPrivateKey)paramPrivateKey;
/* 257 */       ECParameterSpec eCParameterSpec = EC5Util.convertSpec(eCPrivateKey.getParams());
/* 258 */       return (AsymmetricKeyParameter)new ECPrivateKeyParameters(eCPrivateKey
/* 259 */           .getS(), new ECDomainParameters(eCParameterSpec
/* 260 */             .getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 267 */       byte[] arrayOfByte = paramPrivateKey.getEncoded();
/*     */       
/* 269 */       if (arrayOfByte == null)
/*     */       {
/* 271 */         throw new InvalidKeyException("no encoding for EC private key");
/*     */       }
/*     */       
/* 274 */       PrivateKey privateKey = BouncyCastleProvider.getPrivateKey(PrivateKeyInfo.getInstance(arrayOfByte));
/*     */       
/* 276 */       if (privateKey instanceof ECPrivateKey)
/*     */       {
/* 278 */         return generatePrivateKeyParameter(privateKey);
/*     */       }
/*     */     }
/* 281 */     catch (Exception exception) {
/*     */       
/* 283 */       throw new InvalidKeyException("cannot identify EC private key: " + exception.toString());
/*     */     } 
/*     */ 
/*     */     
/* 287 */     throw new InvalidKeyException("can't identify EC private key.");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getOrderBitLength(ProviderConfiguration paramProviderConfiguration, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 292 */     if (paramBigInteger1 == null) {
/*     */       
/* 294 */       ECParameterSpec eCParameterSpec = paramProviderConfiguration.getEcImplicitlyCa();
/*     */       
/* 296 */       if (eCParameterSpec == null)
/*     */       {
/* 298 */         return paramBigInteger2.bitLength();
/*     */       }
/*     */       
/* 301 */       return eCParameterSpec.getN().bitLength();
/*     */     } 
/*     */ 
/*     */     
/* 305 */     return paramBigInteger1.bitLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASN1ObjectIdentifier getNamedCurveOid(String paramString) {
/* 312 */     String str = paramString;
/*     */     
/* 314 */     int i = str.indexOf(' ');
/* 315 */     if (i > 0)
/*     */     {
/* 317 */       str = str.substring(i + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 322 */       if (str.charAt(0) >= '0' && str.charAt(0) <= '2')
/*     */       {
/* 324 */         return new ASN1ObjectIdentifier(str);
/*     */       }
/*     */     }
/* 327 */     catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */ 
/*     */     
/* 331 */     return ECNamedCurveTable.getOID(str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASN1ObjectIdentifier getNamedCurveOid(ECParameterSpec paramECParameterSpec) {
/* 337 */     for (Enumeration<String> enumeration = ECNamedCurveTable.getNames(); enumeration.hasMoreElements(); ) {
/*     */       
/* 339 */       String str = enumeration.nextElement();
/*     */       
/* 341 */       X9ECParameters x9ECParameters = ECNamedCurveTable.getByName(str);
/*     */       
/* 343 */       if (x9ECParameters.getN().equals(paramECParameterSpec.getN()) && x9ECParameters
/* 344 */         .getH().equals(paramECParameterSpec.getH()) && x9ECParameters
/* 345 */         .getCurve().equals(paramECParameterSpec.getCurve()) && x9ECParameters
/* 346 */         .getG().equals(paramECParameterSpec.getG()))
/*     */       {
/* 348 */         return ECNamedCurveTable.getOID(str);
/*     */       }
/*     */     } 
/*     */     
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static X9ECParameters getNamedCurveByOid(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 358 */     X9ECParameters x9ECParameters = CustomNamedCurves.getByOID(paramASN1ObjectIdentifier);
/*     */     
/* 360 */     if (x9ECParameters == null)
/*     */     {
/* 362 */       x9ECParameters = ECNamedCurveTable.getByOID(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 365 */     return x9ECParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static X9ECParameters getNamedCurveByName(String paramString) {
/* 371 */     X9ECParameters x9ECParameters = CustomNamedCurves.getByName(paramString);
/*     */     
/* 373 */     if (x9ECParameters == null)
/*     */     {
/* 375 */       x9ECParameters = ECNamedCurveTable.getByName(paramString);
/*     */     }
/*     */     
/* 378 */     return x9ECParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCurveName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 384 */     return ECNamedCurveTable.getName(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String privateKeyToString(String paramString, BigInteger paramBigInteger, ECParameterSpec paramECParameterSpec) {
/* 389 */     StringBuffer stringBuffer = new StringBuffer();
/* 390 */     String str = Strings.lineSeparator();
/*     */     
/* 392 */     ECPoint eCPoint = (new FixedPointCombMultiplier()).multiply(paramECParameterSpec.getG(), paramBigInteger).normalize();
/*     */     
/* 394 */     stringBuffer.append(paramString);
/* 395 */     stringBuffer.append(" Private Key [").append(generateKeyFingerprint(eCPoint, paramECParameterSpec)).append("]").append(str);
/* 396 */     stringBuffer.append("            X: ").append(eCPoint.getAffineXCoord().toBigInteger().toString(16)).append(str);
/* 397 */     stringBuffer.append("            Y: ").append(eCPoint.getAffineYCoord().toBigInteger().toString(16)).append(str);
/*     */     
/* 399 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String publicKeyToString(String paramString, ECPoint paramECPoint, ECParameterSpec paramECParameterSpec) {
/* 404 */     StringBuffer stringBuffer = new StringBuffer();
/* 405 */     String str = Strings.lineSeparator();
/*     */     
/* 407 */     stringBuffer.append(paramString);
/* 408 */     stringBuffer.append(" Public Key [").append(generateKeyFingerprint(paramECPoint, paramECParameterSpec)).append("]").append(str);
/* 409 */     stringBuffer.append("            X: ").append(paramECPoint.getAffineXCoord().toBigInteger().toString(16)).append(str);
/* 410 */     stringBuffer.append("            Y: ").append(paramECPoint.getAffineYCoord().toBigInteger().toString(16)).append(str);
/*     */     
/* 412 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String generateKeyFingerprint(ECPoint paramECPoint, ECParameterSpec paramECParameterSpec) {
/* 417 */     ECCurve eCCurve = paramECParameterSpec.getCurve();
/* 418 */     ECPoint eCPoint = paramECParameterSpec.getG();
/*     */     
/* 420 */     if (eCCurve != null)
/*     */     {
/* 422 */       return (new Fingerprint(Arrays.concatenate(paramECPoint.getEncoded(false), eCCurve.getA().getEncoded(), eCCurve.getB().getEncoded(), eCPoint.getEncoded(false)))).toString();
/*     */     }
/*     */     
/* 425 */     return (new Fingerprint(paramECPoint.getEncoded(false))).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getNameFrom(AlgorithmParameterSpec paramAlgorithmParameterSpec) {
/* 430 */     return AccessController.<String>doPrivileged((PrivilegedAction<String>)new Object(paramAlgorithmParameterSpec));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jcajce/provider/asymmetric/util/ECUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */