/*     */ package META-INF.versions.9.org.bouncycastle.pqc.asn1;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.bc.BCObjectIdentifiers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface PQCObjectIdentifiers
/*     */ {
/*  14 */   public static final ASN1ObjectIdentifier rainbow = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.5.3.2");
/*     */ 
/*     */   
/*  17 */   public static final ASN1ObjectIdentifier rainbowWithSha1 = rainbow.branch("1");
/*     */   
/*  19 */   public static final ASN1ObjectIdentifier rainbowWithSha224 = rainbow.branch("2");
/*     */   
/*  21 */   public static final ASN1ObjectIdentifier rainbowWithSha256 = rainbow.branch("3");
/*     */   
/*  23 */   public static final ASN1ObjectIdentifier rainbowWithSha384 = rainbow.branch("4");
/*     */   
/*  25 */   public static final ASN1ObjectIdentifier rainbowWithSha512 = rainbow.branch("5");
/*     */ 
/*     */   
/*  28 */   public static final ASN1ObjectIdentifier gmss = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.3");
/*     */ 
/*     */   
/*  31 */   public static final ASN1ObjectIdentifier gmssWithSha1 = gmss.branch("1");
/*     */   
/*  33 */   public static final ASN1ObjectIdentifier gmssWithSha224 = gmss.branch("2");
/*     */   
/*  35 */   public static final ASN1ObjectIdentifier gmssWithSha256 = gmss.branch("3");
/*     */   
/*  37 */   public static final ASN1ObjectIdentifier gmssWithSha384 = gmss.branch("4");
/*     */   
/*  39 */   public static final ASN1ObjectIdentifier gmssWithSha512 = gmss.branch("5");
/*     */ 
/*     */   
/*  42 */   public static final ASN1ObjectIdentifier mcEliece = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.1");
/*     */ 
/*     */   
/*  45 */   public static final ASN1ObjectIdentifier mcElieceCca2 = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.2");
/*     */   
/*  47 */   public static final ASN1ObjectIdentifier mcElieceFujisaki = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.2.1");
/*  48 */   public static final ASN1ObjectIdentifier mcEliecePointcheval = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.2.2");
/*  49 */   public static final ASN1ObjectIdentifier mcElieceKobara_Imai = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.2.3");
/*     */   
/*  51 */   public static final ASN1ObjectIdentifier sphincs256 = BCObjectIdentifiers.sphincs256;
/*  52 */   public static final ASN1ObjectIdentifier sphincs256_with_BLAKE512 = BCObjectIdentifiers.sphincs256_with_BLAKE512;
/*  53 */   public static final ASN1ObjectIdentifier sphincs256_with_SHA512 = BCObjectIdentifiers.sphincs256_with_SHA512;
/*  54 */   public static final ASN1ObjectIdentifier sphincs256_with_SHA3_512 = BCObjectIdentifiers.sphincs256_with_SHA3_512;
/*     */   
/*  56 */   public static final ASN1ObjectIdentifier newHope = BCObjectIdentifiers.newHope;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final ASN1ObjectIdentifier xmss = BCObjectIdentifiers.xmss;
/*  62 */   public static final ASN1ObjectIdentifier xmss_SHA256ph = BCObjectIdentifiers.xmss_SHA256ph;
/*  63 */   public static final ASN1ObjectIdentifier xmss_SHA512ph = BCObjectIdentifiers.xmss_SHA512ph;
/*  64 */   public static final ASN1ObjectIdentifier xmss_SHAKE128ph = BCObjectIdentifiers.xmss_SHAKE128ph;
/*  65 */   public static final ASN1ObjectIdentifier xmss_SHAKE256ph = BCObjectIdentifiers.xmss_SHAKE256ph;
/*  66 */   public static final ASN1ObjectIdentifier xmss_SHA256 = BCObjectIdentifiers.xmss_SHA256;
/*  67 */   public static final ASN1ObjectIdentifier xmss_SHA512 = BCObjectIdentifiers.xmss_SHA512;
/*  68 */   public static final ASN1ObjectIdentifier xmss_SHAKE128 = BCObjectIdentifiers.xmss_SHAKE128;
/*  69 */   public static final ASN1ObjectIdentifier xmss_SHAKE256 = BCObjectIdentifiers.xmss_SHAKE256;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final ASN1ObjectIdentifier xmss_mt = BCObjectIdentifiers.xmss_mt;
/*  76 */   public static final ASN1ObjectIdentifier xmss_mt_SHA256ph = BCObjectIdentifiers.xmss_mt_SHA256ph;
/*  77 */   public static final ASN1ObjectIdentifier xmss_mt_SHA512ph = BCObjectIdentifiers.xmss_mt_SHA512ph;
/*  78 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE128ph = BCObjectIdentifiers.xmss_mt_SHAKE128ph;
/*  79 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE256ph = BCObjectIdentifiers.xmss_mt_SHAKE256ph;
/*  80 */   public static final ASN1ObjectIdentifier xmss_mt_SHA256 = BCObjectIdentifiers.xmss_mt_SHA256;
/*  81 */   public static final ASN1ObjectIdentifier xmss_mt_SHA512 = BCObjectIdentifiers.xmss_mt_SHA512;
/*  82 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE128 = BCObjectIdentifiers.xmss_mt_SHAKE128;
/*  83 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE256 = BCObjectIdentifiers.xmss_mt_SHAKE256;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final ASN1ObjectIdentifier xmss_with_SHA256 = xmss_SHA256ph;
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static final ASN1ObjectIdentifier xmss_with_SHA512 = xmss_SHA512ph;
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final ASN1ObjectIdentifier xmss_with_SHAKE128 = xmss_SHAKE128ph;
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static final ASN1ObjectIdentifier xmss_with_SHAKE256 = xmss_SHAKE256ph;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHA256 = xmss_mt_SHA256ph;
/*     */ 
/*     */ 
/*     */   
/* 110 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHA512 = xmss_mt_SHA512ph;
/*     */ 
/*     */ 
/*     */   
/* 114 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHAKE128 = xmss_mt_SHAKE128;
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHAKE256 = xmss_mt_SHAKE256;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final ASN1ObjectIdentifier qTESLA = BCObjectIdentifiers.qTESLA;
/* 124 */   public static final ASN1ObjectIdentifier qTESLA_p_I = BCObjectIdentifiers.qTESLA_p_I;
/* 125 */   public static final ASN1ObjectIdentifier qTESLA_p_III = BCObjectIdentifiers.qTESLA_p_III;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/PQCObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */