/*     */ package META-INF.versions.9.org.bouncycastle.asn1.nist;
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
/*     */ 
/*     */ public interface NISTObjectIdentifiers
/*     */ {
/*  16 */   public static final ASN1ObjectIdentifier nistAlgorithm = new ASN1ObjectIdentifier("2.16.840.1.101.3.4");
/*     */ 
/*     */   
/*  19 */   public static final ASN1ObjectIdentifier hashAlgs = nistAlgorithm.branch("2");
/*     */ 
/*     */   
/*  22 */   public static final ASN1ObjectIdentifier id_sha256 = hashAlgs.branch("1");
/*     */   
/*  24 */   public static final ASN1ObjectIdentifier id_sha384 = hashAlgs.branch("2");
/*     */   
/*  26 */   public static final ASN1ObjectIdentifier id_sha512 = hashAlgs.branch("3");
/*     */   
/*  28 */   public static final ASN1ObjectIdentifier id_sha224 = hashAlgs.branch("4");
/*     */   
/*  30 */   public static final ASN1ObjectIdentifier id_sha512_224 = hashAlgs.branch("5");
/*     */   
/*  32 */   public static final ASN1ObjectIdentifier id_sha512_256 = hashAlgs.branch("6");
/*     */ 
/*     */   
/*  35 */   public static final ASN1ObjectIdentifier id_sha3_224 = hashAlgs.branch("7");
/*     */   
/*  37 */   public static final ASN1ObjectIdentifier id_sha3_256 = hashAlgs.branch("8");
/*     */   
/*  39 */   public static final ASN1ObjectIdentifier id_sha3_384 = hashAlgs.branch("9");
/*     */   
/*  41 */   public static final ASN1ObjectIdentifier id_sha3_512 = hashAlgs.branch("10");
/*     */   
/*  43 */   public static final ASN1ObjectIdentifier id_shake128 = hashAlgs.branch("11");
/*     */   
/*  45 */   public static final ASN1ObjectIdentifier id_shake256 = hashAlgs.branch("12");
/*     */   
/*  47 */   public static final ASN1ObjectIdentifier id_hmacWithSHA3_224 = hashAlgs.branch("13");
/*     */   
/*  49 */   public static final ASN1ObjectIdentifier id_hmacWithSHA3_256 = hashAlgs.branch("14");
/*     */   
/*  51 */   public static final ASN1ObjectIdentifier id_hmacWithSHA3_384 = hashAlgs.branch("15");
/*     */   
/*  53 */   public static final ASN1ObjectIdentifier id_hmacWithSHA3_512 = hashAlgs.branch("16");
/*     */   
/*  55 */   public static final ASN1ObjectIdentifier id_shake128_len = hashAlgs.branch("17");
/*     */   
/*  57 */   public static final ASN1ObjectIdentifier id_shake256_len = hashAlgs.branch("18");
/*     */   
/*  59 */   public static final ASN1ObjectIdentifier id_KmacWithSHAKE128 = hashAlgs.branch("19");
/*     */   
/*  61 */   public static final ASN1ObjectIdentifier id_KmacWithSHAKE256 = hashAlgs.branch("20");
/*     */ 
/*     */   
/*  64 */   public static final ASN1ObjectIdentifier aes = nistAlgorithm.branch("1");
/*     */ 
/*     */   
/*  67 */   public static final ASN1ObjectIdentifier id_aes128_ECB = aes.branch("1");
/*     */   
/*  69 */   public static final ASN1ObjectIdentifier id_aes128_CBC = aes.branch("2");
/*     */   
/*  71 */   public static final ASN1ObjectIdentifier id_aes128_OFB = aes.branch("3");
/*     */   
/*  73 */   public static final ASN1ObjectIdentifier id_aes128_CFB = aes.branch("4");
/*     */   
/*  75 */   public static final ASN1ObjectIdentifier id_aes128_wrap = aes.branch("5");
/*     */   
/*  77 */   public static final ASN1ObjectIdentifier id_aes128_GCM = aes.branch("6");
/*     */   
/*  79 */   public static final ASN1ObjectIdentifier id_aes128_CCM = aes.branch("7");
/*     */   
/*  81 */   public static final ASN1ObjectIdentifier id_aes128_wrap_pad = aes.branch("8");
/*     */ 
/*     */   
/*  84 */   public static final ASN1ObjectIdentifier id_aes192_ECB = aes.branch("21");
/*     */   
/*  86 */   public static final ASN1ObjectIdentifier id_aes192_CBC = aes.branch("22");
/*     */   
/*  88 */   public static final ASN1ObjectIdentifier id_aes192_OFB = aes.branch("23");
/*     */   
/*  90 */   public static final ASN1ObjectIdentifier id_aes192_CFB = aes.branch("24");
/*     */   
/*  92 */   public static final ASN1ObjectIdentifier id_aes192_wrap = aes.branch("25");
/*     */   
/*  94 */   public static final ASN1ObjectIdentifier id_aes192_GCM = aes.branch("26");
/*     */   
/*  96 */   public static final ASN1ObjectIdentifier id_aes192_CCM = aes.branch("27");
/*     */   
/*  98 */   public static final ASN1ObjectIdentifier id_aes192_wrap_pad = aes.branch("28");
/*     */ 
/*     */   
/* 101 */   public static final ASN1ObjectIdentifier id_aes256_ECB = aes.branch("41");
/*     */   
/* 103 */   public static final ASN1ObjectIdentifier id_aes256_CBC = aes.branch("42");
/*     */   
/* 105 */   public static final ASN1ObjectIdentifier id_aes256_OFB = aes.branch("43");
/*     */   
/* 107 */   public static final ASN1ObjectIdentifier id_aes256_CFB = aes.branch("44");
/*     */   
/* 109 */   public static final ASN1ObjectIdentifier id_aes256_wrap = aes.branch("45");
/*     */   
/* 111 */   public static final ASN1ObjectIdentifier id_aes256_GCM = aes.branch("46");
/*     */   
/* 113 */   public static final ASN1ObjectIdentifier id_aes256_CCM = aes.branch("47");
/*     */   
/* 115 */   public static final ASN1ObjectIdentifier id_aes256_wrap_pad = aes.branch("48");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final ASN1ObjectIdentifier sigAlgs = nistAlgorithm.branch("3");
/*     */   
/* 123 */   public static final ASN1ObjectIdentifier id_dsa_with_sha2 = sigAlgs;
/*     */ 
/*     */   
/* 126 */   public static final ASN1ObjectIdentifier dsa_with_sha224 = sigAlgs.branch("1");
/*     */   
/* 128 */   public static final ASN1ObjectIdentifier dsa_with_sha256 = sigAlgs.branch("2");
/*     */   
/* 130 */   public static final ASN1ObjectIdentifier dsa_with_sha384 = sigAlgs.branch("3");
/*     */   
/* 132 */   public static final ASN1ObjectIdentifier dsa_with_sha512 = sigAlgs.branch("4");
/*     */   
/* 134 */   public static final ASN1ObjectIdentifier id_dsa_with_sha3_224 = sigAlgs.branch("5");
/*     */   
/* 136 */   public static final ASN1ObjectIdentifier id_dsa_with_sha3_256 = sigAlgs.branch("6");
/*     */   
/* 138 */   public static final ASN1ObjectIdentifier id_dsa_with_sha3_384 = sigAlgs.branch("7");
/*     */   
/* 140 */   public static final ASN1ObjectIdentifier id_dsa_with_sha3_512 = sigAlgs.branch("8");
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static final ASN1ObjectIdentifier id_ecdsa_with_sha3_224 = sigAlgs.branch("9");
/*     */   
/* 146 */   public static final ASN1ObjectIdentifier id_ecdsa_with_sha3_256 = sigAlgs.branch("10");
/*     */   
/* 148 */   public static final ASN1ObjectIdentifier id_ecdsa_with_sha3_384 = sigAlgs.branch("11");
/*     */   
/* 150 */   public static final ASN1ObjectIdentifier id_ecdsa_with_sha3_512 = sigAlgs.branch("12");
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final ASN1ObjectIdentifier id_rsassa_pkcs1_v1_5_with_sha3_224 = sigAlgs.branch("13");
/*     */   
/* 156 */   public static final ASN1ObjectIdentifier id_rsassa_pkcs1_v1_5_with_sha3_256 = sigAlgs.branch("14");
/*     */   
/* 158 */   public static final ASN1ObjectIdentifier id_rsassa_pkcs1_v1_5_with_sha3_384 = sigAlgs.branch("15");
/*     */   
/* 160 */   public static final ASN1ObjectIdentifier id_rsassa_pkcs1_v1_5_with_sha3_512 = sigAlgs.branch("16");
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/nist/NISTObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */