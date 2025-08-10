/*      */ package META-INF.versions.9.org.bouncycastle.asn1.sec;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*      */ import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
/*      */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*      */ import org.bouncycastle.asn1.x9.X9ECParametersHolder;
/*      */ import org.bouncycastle.asn1.x9.X9ECPoint;
/*      */ import org.bouncycastle.math.ec.ECCurve;
/*      */ import org.bouncycastle.math.ec.WNafUtil;
/*      */ import org.bouncycastle.math.ec.endo.ECEndomorphism;
/*      */ import org.bouncycastle.math.ec.endo.GLVTypeBEndomorphism;
/*      */ import org.bouncycastle.math.ec.endo.GLVTypeBParameters;
/*      */ import org.bouncycastle.util.Strings;
/*      */ import org.bouncycastle.util.encoders.Hex;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SECNamedCurves
/*      */ {
/*      */   private static X9ECPoint configureBasepoint(ECCurve paramECCurve, String paramString) {
/*   24 */     X9ECPoint x9ECPoint = new X9ECPoint(paramECCurve, Hex.decodeStrict(paramString));
/*   25 */     WNafUtil.configureBasepoint(x9ECPoint.getPoint());
/*   26 */     return x9ECPoint;
/*      */   }
/*      */ 
/*      */   
/*      */   private static ECCurve configureCurve(ECCurve paramECCurve) {
/*   31 */     return paramECCurve;
/*      */   }
/*      */ 
/*      */   
/*      */   private static ECCurve configureCurveGLV(ECCurve paramECCurve, GLVTypeBParameters paramGLVTypeBParameters) {
/*   36 */     return paramECCurve.configure().setEndomorphism((ECEndomorphism)new GLVTypeBEndomorphism(paramECCurve, paramGLVTypeBParameters)).create();
/*      */   }
/*      */ 
/*      */   
/*      */   private static BigInteger fromHex(String paramString) {
/*   41 */     return new BigInteger(1, Hex.decodeStrict(paramString));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   47 */   static X9ECParametersHolder secp112r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   71 */   static X9ECParametersHolder secp112r2 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   static X9ECParametersHolder secp128r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  119 */   static X9ECParametersHolder secp128r2 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   static X9ECParametersHolder secp160k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  181 */   static X9ECParametersHolder secp160r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  205 */   static X9ECParametersHolder secp160r2 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  229 */   static X9ECParametersHolder secp192k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  267 */   static X9ECParametersHolder secp192r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  291 */   static X9ECParametersHolder secp224k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  329 */   static X9ECParametersHolder secp224r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  353 */   static X9ECParametersHolder secp256k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  391 */   static X9ECParametersHolder secp256r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  415 */   static X9ECParametersHolder secp384r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  440 */   static X9ECParametersHolder secp521r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  465 */   static X9ECParametersHolder sect113r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  490 */   static X9ECParametersHolder sect113r2 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  515 */   static X9ECParametersHolder sect131r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  542 */   static X9ECParametersHolder sect131r2 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  569 */   static X9ECParametersHolder sect163k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  596 */   static X9ECParametersHolder sect163r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  623 */   static X9ECParametersHolder sect163r2 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  650 */   static X9ECParametersHolder sect193r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  675 */   static X9ECParametersHolder sect193r2 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  700 */   static X9ECParametersHolder sect233k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  725 */   static X9ECParametersHolder sect233r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  750 */   static X9ECParametersHolder sect239k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  775 */   static X9ECParametersHolder sect283k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  803 */   static X9ECParametersHolder sect283r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  831 */   static X9ECParametersHolder sect409k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  857 */   static X9ECParametersHolder sect409r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  883 */   static X9ECParametersHolder sect571k1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  911 */   static X9ECParametersHolder sect571r1 = (X9ECParametersHolder)new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  937 */   static final Hashtable objIds = new Hashtable<>();
/*  938 */   static final Hashtable curves = new Hashtable<>();
/*  939 */   static final Hashtable names = new Hashtable<>();
/*      */ 
/*      */   
/*      */   static void defineCurve(String paramString, ASN1ObjectIdentifier paramASN1ObjectIdentifier, X9ECParametersHolder paramX9ECParametersHolder) {
/*  943 */     objIds.put(paramString, paramASN1ObjectIdentifier);
/*  944 */     names.put(paramASN1ObjectIdentifier, paramString);
/*  945 */     curves.put(paramASN1ObjectIdentifier, paramX9ECParametersHolder);
/*      */   }
/*      */ 
/*      */   
/*      */   static {
/*  950 */     defineCurve("secp112r1", SECObjectIdentifiers.secp112r1, secp112r1);
/*  951 */     defineCurve("secp112r2", SECObjectIdentifiers.secp112r2, secp112r2);
/*  952 */     defineCurve("secp128r1", SECObjectIdentifiers.secp128r1, secp128r1);
/*  953 */     defineCurve("secp128r2", SECObjectIdentifiers.secp128r2, secp128r2);
/*  954 */     defineCurve("secp160k1", SECObjectIdentifiers.secp160k1, secp160k1);
/*  955 */     defineCurve("secp160r1", SECObjectIdentifiers.secp160r1, secp160r1);
/*  956 */     defineCurve("secp160r2", SECObjectIdentifiers.secp160r2, secp160r2);
/*  957 */     defineCurve("secp192k1", SECObjectIdentifiers.secp192k1, secp192k1);
/*  958 */     defineCurve("secp192r1", SECObjectIdentifiers.secp192r1, secp192r1);
/*  959 */     defineCurve("secp224k1", SECObjectIdentifiers.secp224k1, secp224k1);
/*  960 */     defineCurve("secp224r1", SECObjectIdentifiers.secp224r1, secp224r1);
/*  961 */     defineCurve("secp256k1", SECObjectIdentifiers.secp256k1, secp256k1);
/*  962 */     defineCurve("secp256r1", SECObjectIdentifiers.secp256r1, secp256r1);
/*  963 */     defineCurve("secp384r1", SECObjectIdentifiers.secp384r1, secp384r1);
/*  964 */     defineCurve("secp521r1", SECObjectIdentifiers.secp521r1, secp521r1);
/*      */     
/*  966 */     defineCurve("sect113r1", SECObjectIdentifiers.sect113r1, sect113r1);
/*  967 */     defineCurve("sect113r2", SECObjectIdentifiers.sect113r2, sect113r2);
/*  968 */     defineCurve("sect131r1", SECObjectIdentifiers.sect131r1, sect131r1);
/*  969 */     defineCurve("sect131r2", SECObjectIdentifiers.sect131r2, sect131r2);
/*  970 */     defineCurve("sect163k1", SECObjectIdentifiers.sect163k1, sect163k1);
/*  971 */     defineCurve("sect163r1", SECObjectIdentifiers.sect163r1, sect163r1);
/*  972 */     defineCurve("sect163r2", SECObjectIdentifiers.sect163r2, sect163r2);
/*  973 */     defineCurve("sect193r1", SECObjectIdentifiers.sect193r1, sect193r1);
/*  974 */     defineCurve("sect193r2", SECObjectIdentifiers.sect193r2, sect193r2);
/*  975 */     defineCurve("sect233k1", SECObjectIdentifiers.sect233k1, sect233k1);
/*  976 */     defineCurve("sect233r1", SECObjectIdentifiers.sect233r1, sect233r1);
/*  977 */     defineCurve("sect239k1", SECObjectIdentifiers.sect239k1, sect239k1);
/*  978 */     defineCurve("sect283k1", SECObjectIdentifiers.sect283k1, sect283k1);
/*  979 */     defineCurve("sect283r1", SECObjectIdentifiers.sect283r1, sect283r1);
/*  980 */     defineCurve("sect409k1", SECObjectIdentifiers.sect409k1, sect409k1);
/*  981 */     defineCurve("sect409r1", SECObjectIdentifiers.sect409r1, sect409r1);
/*  982 */     defineCurve("sect571k1", SECObjectIdentifiers.sect571k1, sect571k1);
/*  983 */     defineCurve("sect571r1", SECObjectIdentifiers.sect571r1, sect571r1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static X9ECParameters getByName(String paramString) {
/*  989 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = getOID(paramString);
/*  990 */     return (aSN1ObjectIdentifier == null) ? null : getByOID(aSN1ObjectIdentifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static X9ECParameters getByOID(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 1002 */     X9ECParametersHolder x9ECParametersHolder = (X9ECParametersHolder)curves.get(paramASN1ObjectIdentifier);
/* 1003 */     return (x9ECParametersHolder == null) ? null : x9ECParametersHolder.getParameters();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ASN1ObjectIdentifier getOID(String paramString) {
/* 1015 */     return (ASN1ObjectIdentifier)objIds.get(Strings.toLowerCase(paramString));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 1024 */     return (String)names.get(paramASN1ObjectIdentifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Enumeration getNames() {
/* 1033 */     return names.elements();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/sec/SECNamedCurves.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */