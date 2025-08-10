/*     */ package META-INF.versions.9.org.bouncycastle.jcajce.provider.asymmetric.util;
/*     */ import java.math.BigInteger;
/*     */ import java.security.spec.ECField;
/*     */ import java.security.spec.ECFieldF2m;
/*     */ import java.security.spec.ECFieldFp;
/*     */ import java.security.spec.ECParameterSpec;
/*     */ import java.security.spec.ECPoint;
/*     */ import java.security.spec.EllipticCurve;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
/*     */ import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
/*     */ import org.bouncycastle.asn1.x9.ECNamedCurveTable;
/*     */ import org.bouncycastle.asn1.x9.X962Parameters;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.crypto.ec.CustomNamedCurves;
/*     */ import org.bouncycastle.crypto.params.ECDomainParameters;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
/*     */ import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
/*     */ import org.bouncycastle.jce.ECGOST3410NamedCurveTable;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
/*     */ import org.bouncycastle.jce.spec.ECNamedCurveSpec;
/*     */ import org.bouncycastle.jce.spec.ECParameterSpec;
/*     */ import org.bouncycastle.math.ec.ECAlgorithms;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.field.FiniteField;
/*     */ import org.bouncycastle.math.field.Polynomial;
/*     */ import org.bouncycastle.math.field.PolynomialExtensionField;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class EC5Util {
/*  38 */   private static Map customCurves = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  42 */     Enumeration<String> enumeration = CustomNamedCurves.getNames();
/*  43 */     while (enumeration.hasMoreElements()) {
/*     */       
/*  45 */       String str = enumeration.nextElement();
/*     */       
/*  47 */       X9ECParameters x9ECParameters1 = ECNamedCurveTable.getByName(str);
/*  48 */       if (x9ECParameters1 != null)
/*     */       {
/*  50 */         customCurves.put(x9ECParameters1.getCurve(), CustomNamedCurves.getByName(str).getCurve());
/*     */       }
/*     */     } 
/*     */     
/*  54 */     X9ECParameters x9ECParameters = CustomNamedCurves.getByName("Curve25519");
/*  55 */     ECCurve eCCurve = x9ECParameters.getCurve();
/*     */     
/*  57 */     customCurves.put(new ECCurve.Fp(eCCurve
/*  58 */           .getField().getCharacteristic(), eCCurve
/*  59 */           .getA().toBigInteger(), eCCurve
/*  60 */           .getB().toBigInteger(), eCCurve
/*  61 */           .getOrder(), eCCurve
/*  62 */           .getCofactor()), eCCurve);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECCurve getCurve(ProviderConfiguration paramProviderConfiguration, X962Parameters paramX962Parameters) {
/*     */     ECCurve eCCurve;
/*  71 */     Set set = paramProviderConfiguration.getAcceptableNamedCurves();
/*     */     
/*  73 */     if (paramX962Parameters.isNamedCurve()) {
/*     */       
/*  75 */       ASN1ObjectIdentifier aSN1ObjectIdentifier = ASN1ObjectIdentifier.getInstance(paramX962Parameters.getParameters());
/*     */       
/*  77 */       if (set.isEmpty() || set.contains(aSN1ObjectIdentifier))
/*     */       {
/*  79 */         X9ECParameters x9ECParameters = ECUtil.getNamedCurveByOid(aSN1ObjectIdentifier);
/*     */         
/*  81 */         if (x9ECParameters == null)
/*     */         {
/*  83 */           x9ECParameters = (X9ECParameters)paramProviderConfiguration.getAdditionalECParameters().get(aSN1ObjectIdentifier);
/*     */         }
/*     */         
/*  86 */         eCCurve = x9ECParameters.getCurve();
/*     */       }
/*     */       else
/*     */       {
/*  90 */         throw new IllegalStateException("named curve not acceptable");
/*     */       }
/*     */     
/*  93 */     } else if (paramX962Parameters.isImplicitlyCA()) {
/*     */       
/*  95 */       eCCurve = paramProviderConfiguration.getEcImplicitlyCa().getCurve();
/*     */     }
/*     */     else {
/*     */       
/*  99 */       ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramX962Parameters.getParameters());
/* 100 */       if (set.isEmpty()) {
/*     */         
/* 102 */         if (aSN1Sequence.size() > 3)
/*     */         {
/* 104 */           X9ECParameters x9ECParameters = X9ECParameters.getInstance(aSN1Sequence);
/*     */           
/* 106 */           eCCurve = x9ECParameters.getCurve();
/*     */         }
/*     */         else
/*     */         {
/* 110 */           ASN1ObjectIdentifier aSN1ObjectIdentifier = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
/*     */           
/* 112 */           eCCurve = ECGOST3410NamedCurves.getByOIDX9(aSN1ObjectIdentifier).getCurve();
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 117 */         throw new IllegalStateException("encoded parameters not acceptable");
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     return eCCurve;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECDomainParameters getDomainParameters(ProviderConfiguration paramProviderConfiguration, ECParameterSpec paramECParameterSpec) {
/*     */     ECDomainParameters eCDomainParameters;
/* 130 */     if (paramECParameterSpec == null) {
/*     */       
/* 132 */       ECParameterSpec eCParameterSpec = paramProviderConfiguration.getEcImplicitlyCa();
/*     */       
/* 134 */       eCDomainParameters = new ECDomainParameters(eCParameterSpec.getCurve(), eCParameterSpec.getG(), eCParameterSpec.getN(), eCParameterSpec.getH(), eCParameterSpec.getSeed());
/*     */     }
/*     */     else {
/*     */       
/* 138 */       eCDomainParameters = ECUtil.getDomainParameters(paramProviderConfiguration, convertSpec(paramECParameterSpec));
/*     */     } 
/*     */     
/* 141 */     return eCDomainParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECParameterSpec convertToSpec(X962Parameters paramX962Parameters, ECCurve paramECCurve) {
/*     */     ECNamedCurveSpec eCNamedCurveSpec;
/* 150 */     if (paramX962Parameters.isNamedCurve()) {
/*     */       
/* 152 */       ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)paramX962Parameters.getParameters();
/* 153 */       X9ECParameters x9ECParameters = ECUtil.getNamedCurveByOid(aSN1ObjectIdentifier);
/* 154 */       if (x9ECParameters == null) {
/*     */         
/* 156 */         Map map = BouncyCastleProvider.CONFIGURATION.getAdditionalECParameters();
/* 157 */         if (!map.isEmpty())
/*     */         {
/* 159 */           x9ECParameters = (X9ECParameters)map.get(aSN1ObjectIdentifier);
/*     */         }
/*     */       } 
/*     */       
/* 163 */       EllipticCurve ellipticCurve = convertCurve(paramECCurve, x9ECParameters.getSeed());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 170 */       eCNamedCurveSpec = new ECNamedCurveSpec(ECUtil.getCurveName(aSN1ObjectIdentifier), ellipticCurve, convertPoint(x9ECParameters.getG()), x9ECParameters.getN(), x9ECParameters.getH());
/*     */     }
/* 172 */     else if (paramX962Parameters.isImplicitlyCA()) {
/*     */       
/* 174 */       eCNamedCurveSpec = null;
/*     */     }
/*     */     else {
/*     */       
/* 178 */       ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramX962Parameters.getParameters());
/* 179 */       if (aSN1Sequence.size() > 3) {
/*     */         
/* 181 */         X9ECParameters x9ECParameters = X9ECParameters.getInstance(aSN1Sequence);
/*     */         
/* 183 */         EllipticCurve ellipticCurve = convertCurve(paramECCurve, x9ECParameters.getSeed());
/*     */         
/* 185 */         if (x9ECParameters.getH() != null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 191 */           ECParameterSpec eCParameterSpec = new ECParameterSpec(ellipticCurve, convertPoint(x9ECParameters.getG()), x9ECParameters.getN(), x9ECParameters.getH().intValue());
/*     */ 
/*     */         
/*     */         }
/*     */         else
/*     */         {
/*     */           
/* 198 */           ECParameterSpec eCParameterSpec = new ECParameterSpec(ellipticCurve, convertPoint(x9ECParameters.getG()), x9ECParameters.getN(), 1);
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 204 */         GOST3410PublicKeyAlgParameters gOST3410PublicKeyAlgParameters = GOST3410PublicKeyAlgParameters.getInstance(aSN1Sequence);
/*     */         
/* 206 */         ECNamedCurveParameterSpec eCNamedCurveParameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(gOST3410PublicKeyAlgParameters
/* 207 */               .getPublicKeyParamSet()));
/*     */         
/* 209 */         paramECCurve = eCNamedCurveParameterSpec.getCurve();
/* 210 */         EllipticCurve ellipticCurve = convertCurve(paramECCurve, eCNamedCurveParameterSpec.getSeed());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 216 */         eCNamedCurveSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(gOST3410PublicKeyAlgParameters.getPublicKeyParamSet()), ellipticCurve, convertPoint(eCNamedCurveParameterSpec.getG()), eCNamedCurveParameterSpec.getN(), eCNamedCurveParameterSpec.getH());
/*     */       } 
/*     */     } 
/*     */     
/* 220 */     return (ECParameterSpec)eCNamedCurveSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECParameterSpec convertToSpec(X9ECParameters paramX9ECParameters) {
/* 226 */     return new ECParameterSpec(
/* 227 */         convertCurve(paramX9ECParameters.getCurve(), null), 
/* 228 */         convertPoint(paramX9ECParameters.getG()), paramX9ECParameters
/* 229 */         .getN(), paramX9ECParameters
/* 230 */         .getH().intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECParameterSpec convertToSpec(ECDomainParameters paramECDomainParameters) {
/* 236 */     return new ECParameterSpec(
/* 237 */         convertCurve(paramECDomainParameters.getCurve(), null), 
/* 238 */         convertPoint(paramECDomainParameters.getG()), paramECDomainParameters
/* 239 */         .getN(), paramECDomainParameters
/* 240 */         .getH().intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EllipticCurve convertCurve(ECCurve paramECCurve, byte[] paramArrayOfbyte) {
/* 247 */     ECField eCField = convertField(paramECCurve.getField());
/* 248 */     BigInteger bigInteger1 = paramECCurve.getA().toBigInteger(), bigInteger2 = paramECCurve.getB().toBigInteger();
/*     */ 
/*     */ 
/*     */     
/* 252 */     return new EllipticCurve(eCField, bigInteger1, bigInteger2, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECCurve convertCurve(EllipticCurve paramEllipticCurve) {
/* 258 */     ECField eCField = paramEllipticCurve.getField();
/* 259 */     BigInteger bigInteger1 = paramEllipticCurve.getA();
/* 260 */     BigInteger bigInteger2 = paramEllipticCurve.getB();
/*     */     
/* 262 */     if (eCField instanceof ECFieldFp) {
/*     */       
/* 264 */       ECCurve.Fp fp = new ECCurve.Fp(((ECFieldFp)eCField).getP(), bigInteger1, bigInteger2);
/*     */       
/* 266 */       if (customCurves.containsKey(fp))
/*     */       {
/* 268 */         return (ECCurve)customCurves.get(fp);
/*     */       }
/*     */       
/* 271 */       return (ECCurve)fp;
/*     */     } 
/*     */ 
/*     */     
/* 275 */     ECFieldF2m eCFieldF2m = (ECFieldF2m)eCField;
/* 276 */     int i = eCFieldF2m.getM();
/* 277 */     int[] arrayOfInt = ECUtil.convertMidTerms(eCFieldF2m.getMidTermsOfReductionPolynomial());
/* 278 */     return (ECCurve)new ECCurve.F2m(i, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], bigInteger1, bigInteger2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECField convertField(FiniteField paramFiniteField) {
/* 284 */     if (ECAlgorithms.isFpField(paramFiniteField))
/*     */     {
/* 286 */       return new ECFieldFp(paramFiniteField.getCharacteristic());
/*     */     }
/*     */ 
/*     */     
/* 290 */     Polynomial polynomial = ((PolynomialExtensionField)paramFiniteField).getMinimalPolynomial();
/* 291 */     int[] arrayOfInt1 = polynomial.getExponentsPresent();
/* 292 */     int[] arrayOfInt2 = Arrays.reverse(Arrays.copyOfRange(arrayOfInt1, 1, arrayOfInt1.length - 1));
/* 293 */     return new ECFieldF2m(polynomial.getDegree(), arrayOfInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECParameterSpec convertSpec(EllipticCurve paramEllipticCurve, ECParameterSpec paramECParameterSpec) {
/* 301 */     ECPoint eCPoint = convertPoint(paramECParameterSpec.getG());
/*     */     
/* 303 */     if (paramECParameterSpec instanceof ECNamedCurveParameterSpec) {
/*     */       
/* 305 */       String str = ((ECNamedCurveParameterSpec)paramECParameterSpec).getName();
/*     */       
/* 307 */       return (ECParameterSpec)new ECNamedCurveSpec(str, paramEllipticCurve, eCPoint, paramECParameterSpec.getN(), paramECParameterSpec.getH());
/*     */     } 
/*     */ 
/*     */     
/* 311 */     return new ECParameterSpec(paramEllipticCurve, eCPoint, paramECParameterSpec.getN(), paramECParameterSpec.getH().intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECParameterSpec convertSpec(ECParameterSpec paramECParameterSpec) {
/* 317 */     ECCurve eCCurve = convertCurve(paramECParameterSpec.getCurve());
/*     */     
/* 319 */     ECPoint eCPoint = convertPoint(eCCurve, paramECParameterSpec.getGenerator());
/* 320 */     BigInteger bigInteger1 = paramECParameterSpec.getOrder();
/* 321 */     BigInteger bigInteger2 = BigInteger.valueOf(paramECParameterSpec.getCofactor());
/* 322 */     byte[] arrayOfByte = paramECParameterSpec.getCurve().getSeed();
/*     */     
/* 324 */     if (paramECParameterSpec instanceof ECNamedCurveSpec)
/*     */     {
/* 326 */       return (ECParameterSpec)new ECNamedCurveParameterSpec(((ECNamedCurveSpec)paramECParameterSpec).getName(), eCCurve, eCPoint, bigInteger1, bigInteger2, arrayOfByte);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 331 */     return new ECParameterSpec(eCCurve, eCPoint, bigInteger1, bigInteger2, arrayOfByte);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECPoint convertPoint(ECParameterSpec paramECParameterSpec, ECPoint paramECPoint) {
/* 337 */     return convertPoint(convertCurve(paramECParameterSpec.getCurve()), paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ECPoint convertPoint(ECCurve paramECCurve, ECPoint paramECPoint) {
/* 342 */     return paramECCurve.createPoint(paramECPoint.getAffineX(), paramECPoint.getAffineY());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ECPoint convertPoint(ECPoint paramECPoint) {
/* 347 */     paramECPoint = paramECPoint.normalize();
/*     */     
/* 349 */     return new ECPoint(paramECPoint
/* 350 */         .getAffineXCoord().toBigInteger(), paramECPoint
/* 351 */         .getAffineYCoord().toBigInteger());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jcajce/provider/asymmetric/util/EC5Util.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */