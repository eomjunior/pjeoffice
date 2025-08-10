/*     */ package META-INF.versions.9.org.bouncycastle.asn1.cryptopro;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CryptoProObjectIdentifiers
/*     */ {
/*  14 */   public static final ASN1ObjectIdentifier GOST_id = new ASN1ObjectIdentifier("1.2.643.2.2");
/*     */ 
/*     */   
/*  17 */   public static final ASN1ObjectIdentifier gostR3411 = GOST_id.branch("9");
/*     */   
/*  19 */   public static final ASN1ObjectIdentifier gostR3411Hmac = GOST_id.branch("10");
/*     */   
/*  21 */   public static final ASN1ObjectIdentifier id_Gost28147_89_None_KeyWrap = GOST_id.branch("13.0");
/*  22 */   public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_KeyWrap = GOST_id.branch("13.1");
/*     */ 
/*     */   
/*  25 */   public static final ASN1ObjectIdentifier gostR28147_gcfb = GOST_id.branch("21");
/*     */ 
/*     */   
/*  28 */   public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_TestParamSet = GOST_id.branch("31.0");
/*     */ 
/*     */   
/*  31 */   public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_A_ParamSet = GOST_id.branch("31.1");
/*     */ 
/*     */   
/*  34 */   public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_B_ParamSet = GOST_id.branch("31.2");
/*     */ 
/*     */   
/*  37 */   public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_C_ParamSet = GOST_id.branch("31.3");
/*     */ 
/*     */   
/*  40 */   public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_D_ParamSet = GOST_id.branch("31.4");
/*     */ 
/*     */   
/*  43 */   public static final ASN1ObjectIdentifier gostR3410_94 = GOST_id.branch("20");
/*     */   
/*  45 */   public static final ASN1ObjectIdentifier gostR3410_2001 = GOST_id.branch("19");
/*     */ 
/*     */   
/*  48 */   public static final ASN1ObjectIdentifier gostR3411_94_with_gostR3410_94 = GOST_id.branch("4");
/*     */   
/*  50 */   public static final ASN1ObjectIdentifier gostR3411_94_with_gostR3410_2001 = GOST_id.branch("3");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final ASN1ObjectIdentifier gostR3411_94_CryptoProParamSet = GOST_id.branch("30.1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_A = GOST_id.branch("32.2");
/*     */   
/*  66 */   public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_B = GOST_id.branch("32.3");
/*     */   
/*  68 */   public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_C = GOST_id.branch("32.4");
/*     */   
/*  70 */   public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_D = GOST_id.branch("32.5");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_XchA = GOST_id.branch("33.1");
/*     */   
/*  79 */   public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_XchB = GOST_id.branch("33.2");
/*     */   
/*  81 */   public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_XchC = GOST_id.branch("33.3");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_A = GOST_id.branch("35.1");
/*     */   
/*  90 */   public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_B = GOST_id.branch("35.2");
/*     */   
/*  92 */   public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_C = GOST_id.branch("35.3");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_XchA = GOST_id.branch("36.0");
/*     */   
/* 101 */   public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_XchB = GOST_id.branch("36.1");
/*     */ 
/*     */   
/* 104 */   public static final ASN1ObjectIdentifier gost_ElSgDH3410_default = GOST_id.branch("36.0");
/*     */   
/* 106 */   public static final ASN1ObjectIdentifier gost_ElSgDH3410_1 = GOST_id.branch("36.1");
/*     */   
/* 108 */   public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_ESDH = GOST_id.branch("96");
/*     */   
/* 110 */   public static final ASN1ObjectIdentifier gostR3410_2001DH = GOST_id.branch("98");
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/cryptopro/CryptoProObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */