/*     */ package META-INF.versions.9.org.bouncycastle.crypto.ec;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.cryptlib.CryptlibObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.asn1.x9.X9ECParametersHolder;
/*     */ import org.bouncycastle.asn1.x9.X9ECPoint;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.WNafUtil;
/*     */ import org.bouncycastle.math.ec.endo.ECEndomorphism;
/*     */ import org.bouncycastle.math.ec.endo.GLVTypeBEndomorphism;
/*     */ import org.bouncycastle.math.ec.endo.GLVTypeBParameters;
/*     */ import org.bouncycastle.util.Strings;
/*     */ import org.bouncycastle.util.encoders.Hex;
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
/*     */ public class CustomNamedCurves
/*     */ {
/*     */   private static X9ECPoint configureBasepoint(ECCurve paramECCurve, String paramString) {
/*  59 */     X9ECPoint x9ECPoint = new X9ECPoint(paramECCurve, Hex.decodeStrict(paramString));
/*  60 */     WNafUtil.configureBasepoint(x9ECPoint.getPoint());
/*  61 */     return x9ECPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECCurve configureCurve(ECCurve paramECCurve) {
/*  66 */     return paramECCurve;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECCurve configureCurveGLV(ECCurve paramECCurve, GLVTypeBParameters paramGLVTypeBParameters) {
/*  71 */     return paramECCurve.configure().setEndomorphism((ECEndomorphism)new GLVTypeBEndomorphism(paramECCurve, paramGLVTypeBParameters)).create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   static X9ECParametersHolder curve25519 = (X9ECParametersHolder)new Object();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   static X9ECParametersHolder secp128r1 = (X9ECParametersHolder)new Object();
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
/* 118 */   static X9ECParametersHolder secp160k1 = (X9ECParametersHolder)new Object();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   static X9ECParametersHolder secp160r1 = (X9ECParametersHolder)new Object();
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
/* 161 */   static X9ECParametersHolder secp160r2 = (X9ECParametersHolder)new Object();
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
/* 176 */   static X9ECParametersHolder secp192k1 = (X9ECParametersHolder)new Object();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   static X9ECParametersHolder secp192r1 = (X9ECParametersHolder)new Object();
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
/* 219 */   static X9ECParametersHolder secp224k1 = (X9ECParametersHolder)new Object();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   static X9ECParametersHolder secp224r1 = (X9ECParametersHolder)new Object();
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
/* 262 */   static X9ECParametersHolder secp256k1 = (X9ECParametersHolder)new Object();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   static X9ECParametersHolder secp256r1 = (X9ECParametersHolder)new Object();
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
/* 305 */   static X9ECParametersHolder secp384r1 = (X9ECParametersHolder)new Object();
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
/* 321 */   static X9ECParametersHolder secp521r1 = (X9ECParametersHolder)new Object();
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
/* 337 */   static X9ECParametersHolder sect113r1 = (X9ECParametersHolder)new Object();
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
/* 352 */   static X9ECParametersHolder sect113r2 = (X9ECParametersHolder)new Object();
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
/* 367 */   static X9ECParametersHolder sect131r1 = (X9ECParametersHolder)new Object();
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
/* 382 */   static X9ECParametersHolder sect131r2 = (X9ECParametersHolder)new Object();
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
/* 397 */   static X9ECParametersHolder sect163k1 = (X9ECParametersHolder)new Object();
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
/* 412 */   static X9ECParametersHolder sect163r1 = (X9ECParametersHolder)new Object();
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
/* 427 */   static X9ECParametersHolder sect163r2 = (X9ECParametersHolder)new Object();
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
/* 442 */   static X9ECParametersHolder sect193r1 = (X9ECParametersHolder)new Object();
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
/* 457 */   static X9ECParametersHolder sect193r2 = (X9ECParametersHolder)new Object();
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
/* 472 */   static X9ECParametersHolder sect233k1 = (X9ECParametersHolder)new Object();
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
/* 487 */   static X9ECParametersHolder sect233r1 = (X9ECParametersHolder)new Object();
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
/* 502 */   static X9ECParametersHolder sect239k1 = (X9ECParametersHolder)new Object();
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
/* 517 */   static X9ECParametersHolder sect283k1 = (X9ECParametersHolder)new Object();
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
/* 533 */   static X9ECParametersHolder sect283r1 = (X9ECParametersHolder)new Object();
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
/* 549 */   static X9ECParametersHolder sect409k1 = (X9ECParametersHolder)new Object();
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
/* 565 */   static X9ECParametersHolder sect409r1 = (X9ECParametersHolder)new Object();
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
/* 581 */   static X9ECParametersHolder sect571k1 = (X9ECParametersHolder)new Object();
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
/* 597 */   static X9ECParametersHolder sect571r1 = (X9ECParametersHolder)new Object();
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
/* 613 */   static X9ECParametersHolder sm2p256v1 = (X9ECParametersHolder)new Object();
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
/* 626 */   static final Hashtable nameToCurve = new Hashtable<>();
/* 627 */   static final Hashtable nameToOID = new Hashtable<>();
/* 628 */   static final Hashtable oidToCurve = new Hashtable<>();
/* 629 */   static final Hashtable oidToName = new Hashtable<>();
/* 630 */   static final Vector names = new Vector();
/*     */ 
/*     */   
/*     */   static void defineCurve(String paramString, X9ECParametersHolder paramX9ECParametersHolder) {
/* 634 */     names.addElement(paramString);
/* 635 */     paramString = Strings.toLowerCase(paramString);
/* 636 */     nameToCurve.put(paramString, paramX9ECParametersHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   static void defineCurveWithOID(String paramString, ASN1ObjectIdentifier paramASN1ObjectIdentifier, X9ECParametersHolder paramX9ECParametersHolder) {
/* 641 */     names.addElement(paramString);
/* 642 */     oidToName.put(paramASN1ObjectIdentifier, paramString);
/* 643 */     oidToCurve.put(paramASN1ObjectIdentifier, paramX9ECParametersHolder);
/* 644 */     paramString = Strings.toLowerCase(paramString);
/* 645 */     nameToOID.put(paramString, paramASN1ObjectIdentifier);
/* 646 */     nameToCurve.put(paramString, paramX9ECParametersHolder);
/*     */   }
/*     */ 
/*     */   
/*     */   static void defineCurveAlias(String paramString, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 651 */     Object object = oidToCurve.get(paramASN1ObjectIdentifier);
/* 652 */     if (object == null)
/*     */     {
/* 654 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 657 */     paramString = Strings.toLowerCase(paramString);
/* 658 */     nameToOID.put(paramString, paramASN1ObjectIdentifier);
/* 659 */     nameToCurve.put(paramString, object);
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 664 */     defineCurveWithOID("curve25519", CryptlibObjectIdentifiers.curvey25519, curve25519);
/*     */ 
/*     */ 
/*     */     
/* 668 */     defineCurveWithOID("secp128r1", SECObjectIdentifiers.secp128r1, secp128r1);
/*     */     
/* 670 */     defineCurveWithOID("secp160k1", SECObjectIdentifiers.secp160k1, secp160k1);
/* 671 */     defineCurveWithOID("secp160r1", SECObjectIdentifiers.secp160r1, secp160r1);
/* 672 */     defineCurveWithOID("secp160r2", SECObjectIdentifiers.secp160r2, secp160r2);
/* 673 */     defineCurveWithOID("secp192k1", SECObjectIdentifiers.secp192k1, secp192k1);
/* 674 */     defineCurveWithOID("secp192r1", SECObjectIdentifiers.secp192r1, secp192r1);
/* 675 */     defineCurveWithOID("secp224k1", SECObjectIdentifiers.secp224k1, secp224k1);
/* 676 */     defineCurveWithOID("secp224r1", SECObjectIdentifiers.secp224r1, secp224r1);
/* 677 */     defineCurveWithOID("secp256k1", SECObjectIdentifiers.secp256k1, secp256k1);
/* 678 */     defineCurveWithOID("secp256r1", SECObjectIdentifiers.secp256r1, secp256r1);
/* 679 */     defineCurveWithOID("secp384r1", SECObjectIdentifiers.secp384r1, secp384r1);
/* 680 */     defineCurveWithOID("secp521r1", SECObjectIdentifiers.secp521r1, secp521r1);
/*     */     
/* 682 */     defineCurveWithOID("sect113r1", SECObjectIdentifiers.sect113r1, sect113r1);
/* 683 */     defineCurveWithOID("sect113r2", SECObjectIdentifiers.sect113r2, sect113r2);
/* 684 */     defineCurveWithOID("sect131r1", SECObjectIdentifiers.sect131r1, sect131r1);
/* 685 */     defineCurveWithOID("sect131r2", SECObjectIdentifiers.sect131r2, sect131r2);
/* 686 */     defineCurveWithOID("sect163k1", SECObjectIdentifiers.sect163k1, sect163k1);
/* 687 */     defineCurveWithOID("sect163r1", SECObjectIdentifiers.sect163r1, sect163r1);
/* 688 */     defineCurveWithOID("sect163r2", SECObjectIdentifiers.sect163r2, sect163r2);
/* 689 */     defineCurveWithOID("sect193r1", SECObjectIdentifiers.sect193r1, sect193r1);
/* 690 */     defineCurveWithOID("sect193r2", SECObjectIdentifiers.sect193r2, sect193r2);
/* 691 */     defineCurveWithOID("sect233k1", SECObjectIdentifiers.sect233k1, sect233k1);
/* 692 */     defineCurveWithOID("sect233r1", SECObjectIdentifiers.sect233r1, sect233r1);
/* 693 */     defineCurveWithOID("sect239k1", SECObjectIdentifiers.sect239k1, sect239k1);
/* 694 */     defineCurveWithOID("sect283k1", SECObjectIdentifiers.sect283k1, sect283k1);
/* 695 */     defineCurveWithOID("sect283r1", SECObjectIdentifiers.sect283r1, sect283r1);
/* 696 */     defineCurveWithOID("sect409k1", SECObjectIdentifiers.sect409k1, sect409k1);
/* 697 */     defineCurveWithOID("sect409r1", SECObjectIdentifiers.sect409r1, sect409r1);
/* 698 */     defineCurveWithOID("sect571k1", SECObjectIdentifiers.sect571k1, sect571k1);
/* 699 */     defineCurveWithOID("sect571r1", SECObjectIdentifiers.sect571r1, sect571r1);
/*     */     
/* 701 */     defineCurveWithOID("sm2p256v1", GMObjectIdentifiers.sm2p256v1, sm2p256v1);
/*     */     
/* 703 */     defineCurveAlias("B-163", SECObjectIdentifiers.sect163r2);
/* 704 */     defineCurveAlias("B-233", SECObjectIdentifiers.sect233r1);
/* 705 */     defineCurveAlias("B-283", SECObjectIdentifiers.sect283r1);
/* 706 */     defineCurveAlias("B-409", SECObjectIdentifiers.sect409r1);
/* 707 */     defineCurveAlias("B-571", SECObjectIdentifiers.sect571r1);
/*     */     
/* 709 */     defineCurveAlias("K-163", SECObjectIdentifiers.sect163k1);
/* 710 */     defineCurveAlias("K-233", SECObjectIdentifiers.sect233k1);
/* 711 */     defineCurveAlias("K-283", SECObjectIdentifiers.sect283k1);
/* 712 */     defineCurveAlias("K-409", SECObjectIdentifiers.sect409k1);
/* 713 */     defineCurveAlias("K-571", SECObjectIdentifiers.sect571k1);
/*     */     
/* 715 */     defineCurveAlias("P-192", SECObjectIdentifiers.secp192r1);
/* 716 */     defineCurveAlias("P-224", SECObjectIdentifiers.secp224r1);
/* 717 */     defineCurveAlias("P-256", SECObjectIdentifiers.secp256r1);
/* 718 */     defineCurveAlias("P-384", SECObjectIdentifiers.secp384r1);
/* 719 */     defineCurveAlias("P-521", SECObjectIdentifiers.secp521r1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static X9ECParameters getByName(String paramString) {
/* 724 */     X9ECParametersHolder x9ECParametersHolder = (X9ECParametersHolder)nameToCurve.get(Strings.toLowerCase(paramString));
/* 725 */     return (x9ECParametersHolder == null) ? null : x9ECParametersHolder.getParameters();
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
/* 737 */     X9ECParametersHolder x9ECParametersHolder = (X9ECParametersHolder)oidToCurve.get(paramASN1ObjectIdentifier);
/* 738 */     return (x9ECParametersHolder == null) ? null : x9ECParametersHolder.getParameters();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASN1ObjectIdentifier getOID(String paramString) {
/* 749 */     return (ASN1ObjectIdentifier)nameToOID.get(Strings.toLowerCase(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 757 */     return (String)oidToName.get(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Enumeration getNames() {
/* 765 */     return names.elements();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/ec/CustomNamedCurves.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */