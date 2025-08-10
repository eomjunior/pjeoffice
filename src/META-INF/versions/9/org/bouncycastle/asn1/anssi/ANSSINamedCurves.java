/*     */ package META-INF.versions.9.org.bouncycastle.asn1.anssi;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.anssi.ANSSIObjectIdentifiers;
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
/*     */ public class ANSSINamedCurves
/*     */ {
/*     */   private static X9ECPoint configureBasepoint(ECCurve paramECCurve, String paramString) {
/*  23 */     X9ECPoint x9ECPoint = new X9ECPoint(paramECCurve, Hex.decodeStrict(paramString));
/*  24 */     WNafUtil.configureBasepoint(x9ECPoint.getPoint());
/*  25 */     return x9ECPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECCurve configureCurve(ECCurve paramECCurve) {
/*  30 */     return paramECCurve;
/*     */   }
/*     */ 
/*     */   
/*     */   private static BigInteger fromHex(String paramString) {
/*  35 */     return new BigInteger(1, Hex.decodeStrict(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   static X9ECParametersHolder FRP256v1 = (X9ECParametersHolder)new Object();
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
/*  61 */   static final Hashtable objIds = new Hashtable<>();
/*  62 */   static final Hashtable curves = new Hashtable<>();
/*  63 */   static final Hashtable names = new Hashtable<>();
/*     */ 
/*     */   
/*     */   static void defineCurve(String paramString, ASN1ObjectIdentifier paramASN1ObjectIdentifier, X9ECParametersHolder paramX9ECParametersHolder) {
/*  67 */     objIds.put(Strings.toLowerCase(paramString), paramASN1ObjectIdentifier);
/*  68 */     names.put(paramASN1ObjectIdentifier, paramString);
/*  69 */     curves.put(paramASN1ObjectIdentifier, paramX9ECParametersHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/*  74 */     defineCurve("FRP256v1", ANSSIObjectIdentifiers.FRP256v1, FRP256v1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static X9ECParameters getByName(String paramString) {
/*  80 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = getOID(paramString);
/*  81 */     return (aSN1ObjectIdentifier == null) ? null : getByOID(aSN1ObjectIdentifier);
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
/*  93 */     X9ECParametersHolder x9ECParametersHolder = (X9ECParametersHolder)curves.get(paramASN1ObjectIdentifier);
/*  94 */     return (x9ECParametersHolder == null) ? null : x9ECParametersHolder.getParameters();
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
/* 106 */     return (ASN1ObjectIdentifier)objIds.get(Strings.toLowerCase(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 115 */     return (String)names.get(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Enumeration getNames() {
/* 124 */     return names.elements();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/anssi/ANSSINamedCurves.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */