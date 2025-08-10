/*    */ package META-INF.versions.9.org.bouncycastle.asn1.gm;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ 
/*    */ public interface GMObjectIdentifiers
/*    */ {
/*  7 */   public static final ASN1ObjectIdentifier sm_scheme = new ASN1ObjectIdentifier("1.2.156.10197.1");
/*    */   
/*  9 */   public static final ASN1ObjectIdentifier sm6_ecb = sm_scheme.branch("101.1");
/* 10 */   public static final ASN1ObjectIdentifier sm6_cbc = sm_scheme.branch("101.2");
/* 11 */   public static final ASN1ObjectIdentifier sm6_ofb128 = sm_scheme.branch("101.3");
/* 12 */   public static final ASN1ObjectIdentifier sm6_cfb128 = sm_scheme.branch("101.4");
/*    */   
/* 14 */   public static final ASN1ObjectIdentifier sm1_ecb = sm_scheme.branch("102.1");
/* 15 */   public static final ASN1ObjectIdentifier sm1_cbc = sm_scheme.branch("102.2");
/* 16 */   public static final ASN1ObjectIdentifier sm1_ofb128 = sm_scheme.branch("102.3");
/* 17 */   public static final ASN1ObjectIdentifier sm1_cfb128 = sm_scheme.branch("102.4");
/* 18 */   public static final ASN1ObjectIdentifier sm1_cfb1 = sm_scheme.branch("102.5");
/* 19 */   public static final ASN1ObjectIdentifier sm1_cfb8 = sm_scheme.branch("102.6");
/*    */   
/* 21 */   public static final ASN1ObjectIdentifier ssf33_ecb = sm_scheme.branch("103.1");
/* 22 */   public static final ASN1ObjectIdentifier ssf33_cbc = sm_scheme.branch("103.2");
/* 23 */   public static final ASN1ObjectIdentifier ssf33_ofb128 = sm_scheme.branch("103.3");
/* 24 */   public static final ASN1ObjectIdentifier ssf33_cfb128 = sm_scheme.branch("103.4");
/* 25 */   public static final ASN1ObjectIdentifier ssf33_cfb1 = sm_scheme.branch("103.5");
/* 26 */   public static final ASN1ObjectIdentifier ssf33_cfb8 = sm_scheme.branch("103.6");
/*    */   
/* 28 */   public static final ASN1ObjectIdentifier sms4_ecb = sm_scheme.branch("104.1");
/* 29 */   public static final ASN1ObjectIdentifier sms4_cbc = sm_scheme.branch("104.2");
/* 30 */   public static final ASN1ObjectIdentifier sms4_ofb128 = sm_scheme.branch("104.3");
/* 31 */   public static final ASN1ObjectIdentifier sms4_cfb128 = sm_scheme.branch("104.4");
/* 32 */   public static final ASN1ObjectIdentifier sms4_cfb1 = sm_scheme.branch("104.5");
/* 33 */   public static final ASN1ObjectIdentifier sms4_cfb8 = sm_scheme.branch("104.6");
/* 34 */   public static final ASN1ObjectIdentifier sms4_ctr = sm_scheme.branch("104.7");
/* 35 */   public static final ASN1ObjectIdentifier sms4_gcm = sm_scheme.branch("104.8");
/* 36 */   public static final ASN1ObjectIdentifier sms4_ccm = sm_scheme.branch("104.9");
/* 37 */   public static final ASN1ObjectIdentifier sms4_xts = sm_scheme.branch("104.10");
/* 38 */   public static final ASN1ObjectIdentifier sms4_wrap = sm_scheme.branch("104.11");
/* 39 */   public static final ASN1ObjectIdentifier sms4_wrap_pad = sm_scheme.branch("104.12");
/* 40 */   public static final ASN1ObjectIdentifier sms4_ocb = sm_scheme.branch("104.100");
/*    */   
/* 42 */   public static final ASN1ObjectIdentifier sm5 = sm_scheme.branch("201");
/*    */   
/* 44 */   public static final ASN1ObjectIdentifier sm2p256v1 = sm_scheme.branch("301");
/* 45 */   public static final ASN1ObjectIdentifier sm2sign = sm_scheme.branch("301.1");
/* 46 */   public static final ASN1ObjectIdentifier sm2exchange = sm_scheme.branch("301.2");
/* 47 */   public static final ASN1ObjectIdentifier sm2encrypt = sm_scheme.branch("301.3");
/*    */   
/* 49 */   public static final ASN1ObjectIdentifier wapip192v1 = sm_scheme.branch("301.101");
/*    */   
/* 51 */   public static final ASN1ObjectIdentifier sm2encrypt_recommendedParameters = sm2encrypt.branch("1");
/* 52 */   public static final ASN1ObjectIdentifier sm2encrypt_specifiedParameters = sm2encrypt.branch("2");
/* 53 */   public static final ASN1ObjectIdentifier sm2encrypt_with_sm3 = sm2encrypt.branch("2.1");
/* 54 */   public static final ASN1ObjectIdentifier sm2encrypt_with_sha1 = sm2encrypt.branch("2.2");
/* 55 */   public static final ASN1ObjectIdentifier sm2encrypt_with_sha224 = sm2encrypt.branch("2.3");
/* 56 */   public static final ASN1ObjectIdentifier sm2encrypt_with_sha256 = sm2encrypt.branch("2.4");
/* 57 */   public static final ASN1ObjectIdentifier sm2encrypt_with_sha384 = sm2encrypt.branch("2.5");
/* 58 */   public static final ASN1ObjectIdentifier sm2encrypt_with_sha512 = sm2encrypt.branch("2.6");
/* 59 */   public static final ASN1ObjectIdentifier sm2encrypt_with_rmd160 = sm2encrypt.branch("2.7");
/* 60 */   public static final ASN1ObjectIdentifier sm2encrypt_with_whirlpool = sm2encrypt.branch("2.8");
/* 61 */   public static final ASN1ObjectIdentifier sm2encrypt_with_blake2b512 = sm2encrypt.branch("2.9");
/* 62 */   public static final ASN1ObjectIdentifier sm2encrypt_with_blake2s256 = sm2encrypt.branch("2.10");
/* 63 */   public static final ASN1ObjectIdentifier sm2encrypt_with_md5 = sm2encrypt.branch("2.11");
/*    */   
/* 65 */   public static final ASN1ObjectIdentifier id_sm9PublicKey = sm_scheme.branch("302");
/* 66 */   public static final ASN1ObjectIdentifier sm9sign = sm_scheme.branch("302.1");
/* 67 */   public static final ASN1ObjectIdentifier sm9keyagreement = sm_scheme.branch("302.2");
/* 68 */   public static final ASN1ObjectIdentifier sm9encrypt = sm_scheme.branch("302.3");
/*    */   
/* 70 */   public static final ASN1ObjectIdentifier sm3 = sm_scheme.branch("401");
/*    */   
/* 72 */   public static final ASN1ObjectIdentifier hmac_sm3 = sm3.branch("2");
/*    */   
/* 74 */   public static final ASN1ObjectIdentifier sm2sign_with_sm3 = sm_scheme.branch("501");
/* 75 */   public static final ASN1ObjectIdentifier sm2sign_with_sha1 = sm_scheme.branch("502");
/* 76 */   public static final ASN1ObjectIdentifier sm2sign_with_sha256 = sm_scheme.branch("503");
/* 77 */   public static final ASN1ObjectIdentifier sm2sign_with_sha512 = sm_scheme.branch("504");
/* 78 */   public static final ASN1ObjectIdentifier sm2sign_with_sha224 = sm_scheme.branch("505");
/* 79 */   public static final ASN1ObjectIdentifier sm2sign_with_sha384 = sm_scheme.branch("506");
/* 80 */   public static final ASN1ObjectIdentifier sm2sign_with_rmd160 = sm_scheme.branch("507");
/* 81 */   public static final ASN1ObjectIdentifier sm2sign_with_whirlpool = sm_scheme.branch("520");
/* 82 */   public static final ASN1ObjectIdentifier sm2sign_with_blake2b512 = sm_scheme.branch("521");
/* 83 */   public static final ASN1ObjectIdentifier sm2sign_with_blake2s256 = sm_scheme.branch("522");
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/gm/GMObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */