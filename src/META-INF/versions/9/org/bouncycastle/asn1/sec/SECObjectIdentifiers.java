/*     */ package META-INF.versions.9.org.bouncycastle.asn1.sec;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SECObjectIdentifiers
/*     */ {
/*  19 */   public static final ASN1ObjectIdentifier ellipticCurve = new ASN1ObjectIdentifier("1.3.132.0");
/*     */ 
/*     */   
/*  22 */   public static final ASN1ObjectIdentifier sect163k1 = ellipticCurve.branch("1");
/*     */   
/*  24 */   public static final ASN1ObjectIdentifier sect163r1 = ellipticCurve.branch("2");
/*     */   
/*  26 */   public static final ASN1ObjectIdentifier sect239k1 = ellipticCurve.branch("3");
/*     */   
/*  28 */   public static final ASN1ObjectIdentifier sect113r1 = ellipticCurve.branch("4");
/*     */   
/*  30 */   public static final ASN1ObjectIdentifier sect113r2 = ellipticCurve.branch("5");
/*     */   
/*  32 */   public static final ASN1ObjectIdentifier secp112r1 = ellipticCurve.branch("6");
/*     */   
/*  34 */   public static final ASN1ObjectIdentifier secp112r2 = ellipticCurve.branch("7");
/*     */   
/*  36 */   public static final ASN1ObjectIdentifier secp160r1 = ellipticCurve.branch("8");
/*     */   
/*  38 */   public static final ASN1ObjectIdentifier secp160k1 = ellipticCurve.branch("9");
/*     */   
/*  40 */   public static final ASN1ObjectIdentifier secp256k1 = ellipticCurve.branch("10");
/*     */   
/*  42 */   public static final ASN1ObjectIdentifier sect163r2 = ellipticCurve.branch("15");
/*     */   
/*  44 */   public static final ASN1ObjectIdentifier sect283k1 = ellipticCurve.branch("16");
/*     */   
/*  46 */   public static final ASN1ObjectIdentifier sect283r1 = ellipticCurve.branch("17");
/*     */   
/*  48 */   public static final ASN1ObjectIdentifier sect131r1 = ellipticCurve.branch("22");
/*     */   
/*  50 */   public static final ASN1ObjectIdentifier sect131r2 = ellipticCurve.branch("23");
/*     */   
/*  52 */   public static final ASN1ObjectIdentifier sect193r1 = ellipticCurve.branch("24");
/*     */   
/*  54 */   public static final ASN1ObjectIdentifier sect193r2 = ellipticCurve.branch("25");
/*     */   
/*  56 */   public static final ASN1ObjectIdentifier sect233k1 = ellipticCurve.branch("26");
/*     */   
/*  58 */   public static final ASN1ObjectIdentifier sect233r1 = ellipticCurve.branch("27");
/*     */   
/*  60 */   public static final ASN1ObjectIdentifier secp128r1 = ellipticCurve.branch("28");
/*     */   
/*  62 */   public static final ASN1ObjectIdentifier secp128r2 = ellipticCurve.branch("29");
/*     */   
/*  64 */   public static final ASN1ObjectIdentifier secp160r2 = ellipticCurve.branch("30");
/*     */   
/*  66 */   public static final ASN1ObjectIdentifier secp192k1 = ellipticCurve.branch("31");
/*     */   
/*  68 */   public static final ASN1ObjectIdentifier secp224k1 = ellipticCurve.branch("32");
/*     */   
/*  70 */   public static final ASN1ObjectIdentifier secp224r1 = ellipticCurve.branch("33");
/*     */   
/*  72 */   public static final ASN1ObjectIdentifier secp384r1 = ellipticCurve.branch("34");
/*     */   
/*  74 */   public static final ASN1ObjectIdentifier secp521r1 = ellipticCurve.branch("35");
/*     */   
/*  76 */   public static final ASN1ObjectIdentifier sect409k1 = ellipticCurve.branch("36");
/*     */   
/*  78 */   public static final ASN1ObjectIdentifier sect409r1 = ellipticCurve.branch("37");
/*     */   
/*  80 */   public static final ASN1ObjectIdentifier sect571k1 = ellipticCurve.branch("38");
/*     */   
/*  82 */   public static final ASN1ObjectIdentifier sect571r1 = ellipticCurve.branch("39");
/*     */ 
/*     */   
/*  85 */   public static final ASN1ObjectIdentifier secp192r1 = X9ObjectIdentifiers.prime192v1;
/*     */   
/*  87 */   public static final ASN1ObjectIdentifier secp256r1 = X9ObjectIdentifiers.prime256v1;
/*     */   
/*  89 */   public static final ASN1ObjectIdentifier secg_scheme = new ASN1ObjectIdentifier("1.3.132.1");
/*     */   
/*  91 */   public static final ASN1ObjectIdentifier dhSinglePass_stdDH_sha224kdf_scheme = secg_scheme.branch("11.0");
/*  92 */   public static final ASN1ObjectIdentifier dhSinglePass_stdDH_sha256kdf_scheme = secg_scheme.branch("11.1");
/*  93 */   public static final ASN1ObjectIdentifier dhSinglePass_stdDH_sha384kdf_scheme = secg_scheme.branch("11.2");
/*  94 */   public static final ASN1ObjectIdentifier dhSinglePass_stdDH_sha512kdf_scheme = secg_scheme.branch("11.3");
/*     */   
/*  96 */   public static final ASN1ObjectIdentifier dhSinglePass_cofactorDH_sha224kdf_scheme = secg_scheme.branch("14.0");
/*  97 */   public static final ASN1ObjectIdentifier dhSinglePass_cofactorDH_sha256kdf_scheme = secg_scheme.branch("14.1");
/*  98 */   public static final ASN1ObjectIdentifier dhSinglePass_cofactorDH_sha384kdf_scheme = secg_scheme.branch("14.2");
/*  99 */   public static final ASN1ObjectIdentifier dhSinglePass_cofactorDH_sha512kdf_scheme = secg_scheme.branch("14.3");
/*     */   
/* 101 */   public static final ASN1ObjectIdentifier mqvSinglePass_sha224kdf_scheme = secg_scheme.branch("15.0");
/* 102 */   public static final ASN1ObjectIdentifier mqvSinglePass_sha256kdf_scheme = secg_scheme.branch("15.1");
/* 103 */   public static final ASN1ObjectIdentifier mqvSinglePass_sha384kdf_scheme = secg_scheme.branch("15.2");
/* 104 */   public static final ASN1ObjectIdentifier mqvSinglePass_sha512kdf_scheme = secg_scheme.branch("15.3");
/*     */   
/* 106 */   public static final ASN1ObjectIdentifier mqvFull_sha224kdf_scheme = secg_scheme.branch("16.0");
/* 107 */   public static final ASN1ObjectIdentifier mqvFull_sha256kdf_scheme = secg_scheme.branch("16.1");
/* 108 */   public static final ASN1ObjectIdentifier mqvFull_sha384kdf_scheme = secg_scheme.branch("16.2");
/* 109 */   public static final ASN1ObjectIdentifier mqvFull_sha512kdf_scheme = secg_scheme.branch("16.3");
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/sec/SECObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */