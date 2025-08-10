/*    */ package META-INF.versions.9.org.bouncycastle.asn1.teletrust;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TeleTrusTObjectIdentifiers
/*    */ {
/* 15 */   public static final ASN1ObjectIdentifier teleTrusTAlgorithm = new ASN1ObjectIdentifier("1.3.36.3");
/*    */ 
/*    */   
/* 18 */   public static final ASN1ObjectIdentifier ripemd160 = teleTrusTAlgorithm.branch("2.1");
/*    */   
/* 20 */   public static final ASN1ObjectIdentifier ripemd128 = teleTrusTAlgorithm.branch("2.2");
/*    */   
/* 22 */   public static final ASN1ObjectIdentifier ripemd256 = teleTrusTAlgorithm.branch("2.3");
/*    */ 
/*    */   
/* 25 */   public static final ASN1ObjectIdentifier teleTrusTRSAsignatureAlgorithm = teleTrusTAlgorithm.branch("3.1");
/*    */ 
/*    */   
/* 28 */   public static final ASN1ObjectIdentifier rsaSignatureWithripemd160 = teleTrusTRSAsignatureAlgorithm.branch("2");
/*    */   
/* 30 */   public static final ASN1ObjectIdentifier rsaSignatureWithripemd128 = teleTrusTRSAsignatureAlgorithm.branch("3");
/*    */   
/* 32 */   public static final ASN1ObjectIdentifier rsaSignatureWithripemd256 = teleTrusTRSAsignatureAlgorithm.branch("4");
/*    */ 
/*    */   
/* 35 */   public static final ASN1ObjectIdentifier ecSign = teleTrusTAlgorithm.branch("3.2");
/*    */ 
/*    */   
/* 38 */   public static final ASN1ObjectIdentifier ecSignWithSha1 = ecSign.branch("1");
/*    */   
/* 40 */   public static final ASN1ObjectIdentifier ecSignWithRipemd160 = ecSign.branch("2");
/*    */ 
/*    */   
/* 43 */   public static final ASN1ObjectIdentifier ecc_brainpool = teleTrusTAlgorithm.branch("3.2.8");
/*    */   
/* 45 */   public static final ASN1ObjectIdentifier ellipticCurve = ecc_brainpool.branch("1");
/*    */   
/* 47 */   public static final ASN1ObjectIdentifier versionOne = ellipticCurve.branch("1");
/*    */ 
/*    */   
/* 50 */   public static final ASN1ObjectIdentifier brainpoolP160r1 = versionOne.branch("1");
/*    */   
/* 52 */   public static final ASN1ObjectIdentifier brainpoolP160t1 = versionOne.branch("2");
/*    */   
/* 54 */   public static final ASN1ObjectIdentifier brainpoolP192r1 = versionOne.branch("3");
/*    */   
/* 56 */   public static final ASN1ObjectIdentifier brainpoolP192t1 = versionOne.branch("4");
/*    */   
/* 58 */   public static final ASN1ObjectIdentifier brainpoolP224r1 = versionOne.branch("5");
/*    */   
/* 60 */   public static final ASN1ObjectIdentifier brainpoolP224t1 = versionOne.branch("6");
/*    */   
/* 62 */   public static final ASN1ObjectIdentifier brainpoolP256r1 = versionOne.branch("7");
/*    */   
/* 64 */   public static final ASN1ObjectIdentifier brainpoolP256t1 = versionOne.branch("8");
/*    */   
/* 66 */   public static final ASN1ObjectIdentifier brainpoolP320r1 = versionOne.branch("9");
/*    */   
/* 68 */   public static final ASN1ObjectIdentifier brainpoolP320t1 = versionOne.branch("10");
/*    */   
/* 70 */   public static final ASN1ObjectIdentifier brainpoolP384r1 = versionOne.branch("11");
/*    */   
/* 72 */   public static final ASN1ObjectIdentifier brainpoolP384t1 = versionOne.branch("12");
/*    */   
/* 74 */   public static final ASN1ObjectIdentifier brainpoolP512r1 = versionOne.branch("13");
/*    */   
/* 76 */   public static final ASN1ObjectIdentifier brainpoolP512t1 = versionOne.branch("14");
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/teletrust/TeleTrusTObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */