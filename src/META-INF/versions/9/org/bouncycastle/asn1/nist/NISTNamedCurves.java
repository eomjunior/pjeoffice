/*    */ package META-INF.versions.9.org.bouncycastle.asn1.nist;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Hashtable;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.sec.SECNamedCurves;
/*    */ import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
/*    */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*    */ import org.bouncycastle.util.Strings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NISTNamedCurves
/*    */ {
/* 17 */   static final Hashtable objIds = new Hashtable<>();
/* 18 */   static final Hashtable names = new Hashtable<>();
/*    */ 
/*    */   
/*    */   static void defineCurve(String paramString, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 22 */     objIds.put(paramString, paramASN1ObjectIdentifier);
/* 23 */     names.put(paramASN1ObjectIdentifier, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   static {
/* 28 */     defineCurve("B-571", SECObjectIdentifiers.sect571r1);
/* 29 */     defineCurve("B-409", SECObjectIdentifiers.sect409r1);
/* 30 */     defineCurve("B-283", SECObjectIdentifiers.sect283r1);
/* 31 */     defineCurve("B-233", SECObjectIdentifiers.sect233r1);
/* 32 */     defineCurve("B-163", SECObjectIdentifiers.sect163r2);
/* 33 */     defineCurve("K-571", SECObjectIdentifiers.sect571k1);
/* 34 */     defineCurve("K-409", SECObjectIdentifiers.sect409k1);
/* 35 */     defineCurve("K-283", SECObjectIdentifiers.sect283k1);
/* 36 */     defineCurve("K-233", SECObjectIdentifiers.sect233k1);
/* 37 */     defineCurve("K-163", SECObjectIdentifiers.sect163k1);
/* 38 */     defineCurve("P-521", SECObjectIdentifiers.secp521r1);
/* 39 */     defineCurve("P-384", SECObjectIdentifiers.secp384r1);
/* 40 */     defineCurve("P-256", SECObjectIdentifiers.secp256r1);
/* 41 */     defineCurve("P-224", SECObjectIdentifiers.secp224r1);
/* 42 */     defineCurve("P-192", SECObjectIdentifiers.secp192r1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static X9ECParameters getByName(String paramString) {
/* 48 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)objIds.get(Strings.toUpperCase(paramString));
/*    */     
/* 50 */     if (aSN1ObjectIdentifier != null)
/*    */     {
/* 52 */       return getByOID(aSN1ObjectIdentifier);
/*    */     }
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static X9ECParameters getByOID(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 67 */     return SECNamedCurves.getByOID(paramASN1ObjectIdentifier);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ASN1ObjectIdentifier getOID(String paramString) {
/* 79 */     return (ASN1ObjectIdentifier)objIds.get(Strings.toUpperCase(paramString));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 88 */     return (String)names.get(paramASN1ObjectIdentifier);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Enumeration getNames() {
/* 97 */     return objIds.keys();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/nist/NISTNamedCurves.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */