/*     */ package META-INF.versions.9.org.bouncycastle.asn1.bc;
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
/*     */ 
/*     */ public interface BCObjectIdentifiers
/*     */ {
/*  15 */   public static final ASN1ObjectIdentifier bc = new ASN1ObjectIdentifier("1.3.6.1.4.1.22554");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  22 */   public static final ASN1ObjectIdentifier bc_pbe = bc.branch("1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   public static final ASN1ObjectIdentifier bc_pbe_sha1 = bc_pbe.branch("1");
/*     */ 
/*     */   
/*  32 */   public static final ASN1ObjectIdentifier bc_pbe_sha256 = bc_pbe.branch("2.1");
/*     */   
/*  34 */   public static final ASN1ObjectIdentifier bc_pbe_sha384 = bc_pbe.branch("2.2");
/*     */   
/*  36 */   public static final ASN1ObjectIdentifier bc_pbe_sha512 = bc_pbe.branch("2.3");
/*     */   
/*  38 */   public static final ASN1ObjectIdentifier bc_pbe_sha224 = bc_pbe.branch("2.4");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs5 = bc_pbe_sha1.branch("1");
/*     */   
/*  46 */   public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12 = bc_pbe_sha1.branch("2");
/*     */ 
/*     */   
/*  49 */   public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs5 = bc_pbe_sha256.branch("1");
/*     */   
/*  51 */   public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12 = bc_pbe_sha256.branch("2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes128_cbc = bc_pbe_sha1_pkcs12.branch("1.2");
/*     */   
/*  59 */   public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes192_cbc = bc_pbe_sha1_pkcs12.branch("1.22");
/*     */   
/*  61 */   public static final ASN1ObjectIdentifier bc_pbe_sha1_pkcs12_aes256_cbc = bc_pbe_sha1_pkcs12.branch("1.42");
/*     */ 
/*     */   
/*  64 */   public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes128_cbc = bc_pbe_sha256_pkcs12.branch("1.2");
/*     */   
/*  66 */   public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes192_cbc = bc_pbe_sha256_pkcs12.branch("1.22");
/*     */   
/*  68 */   public static final ASN1ObjectIdentifier bc_pbe_sha256_pkcs12_aes256_cbc = bc_pbe_sha256_pkcs12.branch("1.42");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final ASN1ObjectIdentifier bc_sig = bc.branch("2");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final ASN1ObjectIdentifier sphincs256 = bc_sig.branch("1");
/*  79 */   public static final ASN1ObjectIdentifier sphincs256_with_BLAKE512 = sphincs256.branch("1");
/*  80 */   public static final ASN1ObjectIdentifier sphincs256_with_SHA512 = sphincs256.branch("2");
/*  81 */   public static final ASN1ObjectIdentifier sphincs256_with_SHA3_512 = sphincs256.branch("3");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static final ASN1ObjectIdentifier xmss = bc_sig.branch("2");
/*  87 */   public static final ASN1ObjectIdentifier xmss_SHA256ph = xmss.branch("1");
/*  88 */   public static final ASN1ObjectIdentifier xmss_SHA512ph = xmss.branch("2");
/*  89 */   public static final ASN1ObjectIdentifier xmss_SHAKE128ph = xmss.branch("3");
/*  90 */   public static final ASN1ObjectIdentifier xmss_SHAKE256ph = xmss.branch("4");
/*  91 */   public static final ASN1ObjectIdentifier xmss_SHA256 = xmss.branch("5");
/*  92 */   public static final ASN1ObjectIdentifier xmss_SHA512 = xmss.branch("6");
/*  93 */   public static final ASN1ObjectIdentifier xmss_SHAKE128 = xmss.branch("7");
/*  94 */   public static final ASN1ObjectIdentifier xmss_SHAKE256 = xmss.branch("8");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final ASN1ObjectIdentifier xmss_mt = bc_sig.branch("3");
/* 100 */   public static final ASN1ObjectIdentifier xmss_mt_SHA256ph = xmss_mt.branch("1");
/* 101 */   public static final ASN1ObjectIdentifier xmss_mt_SHA512ph = xmss_mt.branch("2");
/* 102 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE128ph = xmss_mt.branch("3");
/* 103 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE256ph = xmss_mt.branch("4");
/* 104 */   public static final ASN1ObjectIdentifier xmss_mt_SHA256 = xmss_mt.branch("5");
/* 105 */   public static final ASN1ObjectIdentifier xmss_mt_SHA512 = xmss_mt.branch("6");
/* 106 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE128 = xmss_mt.branch("7");
/* 107 */   public static final ASN1ObjectIdentifier xmss_mt_SHAKE256 = xmss_mt.branch("8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final ASN1ObjectIdentifier xmss_with_SHA256 = xmss_SHA256ph;
/*     */ 
/*     */ 
/*     */   
/* 117 */   public static final ASN1ObjectIdentifier xmss_with_SHA512 = xmss_SHA512ph;
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final ASN1ObjectIdentifier xmss_with_SHAKE128 = xmss_SHAKE128ph;
/*     */ 
/*     */ 
/*     */   
/* 125 */   public static final ASN1ObjectIdentifier xmss_with_SHAKE256 = xmss_SHAKE256ph;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHA256 = xmss_mt_SHA256ph;
/*     */ 
/*     */ 
/*     */   
/* 134 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHA512 = xmss_mt_SHA512ph;
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHAKE128 = xmss_mt_SHAKE128;
/*     */ 
/*     */ 
/*     */   
/* 142 */   public static final ASN1ObjectIdentifier xmss_mt_with_SHAKE256 = xmss_mt_SHAKE256;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static final ASN1ObjectIdentifier qTESLA = bc_sig.branch("4");
/*     */   
/* 149 */   public static final ASN1ObjectIdentifier qTESLA_Rnd1_I = qTESLA.branch("1");
/* 150 */   public static final ASN1ObjectIdentifier qTESLA_Rnd1_III_size = qTESLA.branch("2");
/* 151 */   public static final ASN1ObjectIdentifier qTESLA_Rnd1_III_speed = qTESLA.branch("3");
/* 152 */   public static final ASN1ObjectIdentifier qTESLA_Rnd1_p_I = qTESLA.branch("4");
/* 153 */   public static final ASN1ObjectIdentifier qTESLA_Rnd1_p_III = qTESLA.branch("5");
/*     */ 
/*     */   
/* 156 */   public static final ASN1ObjectIdentifier qTESLA_p_I = qTESLA.branch("11");
/* 157 */   public static final ASN1ObjectIdentifier qTESLA_p_III = qTESLA.branch("12");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static final ASN1ObjectIdentifier bc_exch = bc.branch("3");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public static final ASN1ObjectIdentifier newHope = bc_exch.branch("1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final ASN1ObjectIdentifier bc_ext = bc.branch("4");
/*     */   
/* 176 */   public static final ASN1ObjectIdentifier linkedCertificate = bc_ext.branch("1");
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/bc/BCObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */