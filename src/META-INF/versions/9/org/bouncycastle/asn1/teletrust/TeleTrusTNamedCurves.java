/*     */ package META-INF.versions.9.org.bouncycastle.asn1.teletrust;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.asn1.x9.X9ECParametersHolder;
/*     */ import org.bouncycastle.asn1.x9.X9ECPoint;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.WNafUtil;
/*     */ import org.bouncycastle.util.Strings;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeleTrusTNamedCurves
/*     */ {
/*     */   private static X9ECPoint configureBasepoint(ECCurve paramECCurve, String paramString) {
/*  24 */     X9ECPoint x9ECPoint = new X9ECPoint(paramECCurve, Hex.decodeStrict(paramString));
/*  25 */     WNafUtil.configureBasepoint(x9ECPoint.getPoint());
/*  26 */     return x9ECPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECCurve configureCurve(ECCurve paramECCurve) {
/*  31 */     return paramECCurve;
/*     */   }
/*     */ 
/*     */   
/*     */   private static BigInteger fromHex(String paramString) {
/*  36 */     return new BigInteger(1, Hex.decodeStrict(paramString));
/*     */   }
/*     */   
/*  39 */   static X9ECParametersHolder brainpoolP160r1 = (X9ECParametersHolder)new Object();
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
/*  59 */   static X9ECParametersHolder brainpoolP160t1 = (X9ECParametersHolder)new Object();
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
/*  80 */   static X9ECParametersHolder brainpoolP192r1 = (X9ECParametersHolder)new Object();
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
/* 100 */   static X9ECParametersHolder brainpoolP192t1 = (X9ECParametersHolder)new Object();
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
/* 121 */   static X9ECParametersHolder brainpoolP224r1 = (X9ECParametersHolder)new Object();
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
/* 141 */   static X9ECParametersHolder brainpoolP224t1 = (X9ECParametersHolder)new Object();
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
/* 162 */   static X9ECParametersHolder brainpoolP256r1 = (X9ECParametersHolder)new Object();
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
/* 182 */   static X9ECParametersHolder brainpoolP256t1 = (X9ECParametersHolder)new Object();
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
/* 203 */   static X9ECParametersHolder brainpoolP320r1 = (X9ECParametersHolder)new Object();
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
/* 223 */   static X9ECParametersHolder brainpoolP320t1 = (X9ECParametersHolder)new Object();
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
/* 244 */   static X9ECParametersHolder brainpoolP384r1 = (X9ECParametersHolder)new Object();
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
/* 264 */   static X9ECParametersHolder brainpoolP384t1 = (X9ECParametersHolder)new Object();
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
/* 285 */   static X9ECParametersHolder brainpoolP512r1 = (X9ECParametersHolder)new Object();
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
/* 305 */   static X9ECParametersHolder brainpoolP512t1 = (X9ECParametersHolder)new Object();
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
/* 327 */   static final Hashtable objIds = new Hashtable<>();
/* 328 */   static final Hashtable curves = new Hashtable<>();
/* 329 */   static final Hashtable names = new Hashtable<>();
/*     */ 
/*     */   
/*     */   static void defineCurve(String paramString, ASN1ObjectIdentifier paramASN1ObjectIdentifier, X9ECParametersHolder paramX9ECParametersHolder) {
/* 333 */     objIds.put(Strings.toLowerCase(paramString), paramASN1ObjectIdentifier);
/* 334 */     names.put(paramASN1ObjectIdentifier, paramString);
/* 335 */     curves.put(paramASN1ObjectIdentifier, paramX9ECParametersHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 340 */     defineCurve("brainpoolP160r1", TeleTrusTObjectIdentifiers.brainpoolP160r1, brainpoolP160r1);
/* 341 */     defineCurve("brainpoolP160t1", TeleTrusTObjectIdentifiers.brainpoolP160t1, brainpoolP160t1);
/* 342 */     defineCurve("brainpoolP192r1", TeleTrusTObjectIdentifiers.brainpoolP192r1, brainpoolP192r1);
/* 343 */     defineCurve("brainpoolP192t1", TeleTrusTObjectIdentifiers.brainpoolP192t1, brainpoolP192t1);
/* 344 */     defineCurve("brainpoolP224r1", TeleTrusTObjectIdentifiers.brainpoolP224r1, brainpoolP224r1);
/* 345 */     defineCurve("brainpoolP224t1", TeleTrusTObjectIdentifiers.brainpoolP224t1, brainpoolP224t1);
/* 346 */     defineCurve("brainpoolP256r1", TeleTrusTObjectIdentifiers.brainpoolP256r1, brainpoolP256r1);
/* 347 */     defineCurve("brainpoolP256t1", TeleTrusTObjectIdentifiers.brainpoolP256t1, brainpoolP256t1);
/* 348 */     defineCurve("brainpoolP320r1", TeleTrusTObjectIdentifiers.brainpoolP320r1, brainpoolP320r1);
/* 349 */     defineCurve("brainpoolP320t1", TeleTrusTObjectIdentifiers.brainpoolP320t1, brainpoolP320t1);
/* 350 */     defineCurve("brainpoolP384r1", TeleTrusTObjectIdentifiers.brainpoolP384r1, brainpoolP384r1);
/* 351 */     defineCurve("brainpoolP384t1", TeleTrusTObjectIdentifiers.brainpoolP384t1, brainpoolP384t1);
/* 352 */     defineCurve("brainpoolP512r1", TeleTrusTObjectIdentifiers.brainpoolP512r1, brainpoolP512r1);
/* 353 */     defineCurve("brainpoolP512t1", TeleTrusTObjectIdentifiers.brainpoolP512t1, brainpoolP512t1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static X9ECParameters getByName(String paramString) {
/* 359 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)objIds.get(Strings.toLowerCase(paramString));
/*     */     
/* 361 */     if (aSN1ObjectIdentifier != null)
/*     */     {
/* 363 */       return getByOID(aSN1ObjectIdentifier);
/*     */     }
/*     */     
/* 366 */     return null;
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
/*     */   public static X9ECParameters getByOID(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 378 */     X9ECParametersHolder x9ECParametersHolder = (X9ECParametersHolder)curves.get(paramASN1ObjectIdentifier);
/*     */     
/* 380 */     if (x9ECParametersHolder != null)
/*     */     {
/* 382 */       return x9ECParametersHolder.getParameters();
/*     */     }
/*     */     
/* 385 */     return null;
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
/*     */   public static ASN1ObjectIdentifier getOID(String paramString) {
/* 397 */     return (ASN1ObjectIdentifier)objIds.get(Strings.toLowerCase(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 406 */     return (String)names.get(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Enumeration getNames() {
/* 415 */     return names.elements();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ASN1ObjectIdentifier getOID(short paramShort, boolean paramBoolean) {
/* 420 */     return getOID("brainpoolP" + paramShort + (paramBoolean ? "t" : "r") + "1");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/teletrust/TeleTrusTNamedCurves.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */