/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
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
/*     */ public interface X9ObjectIdentifiers
/*     */ {
/*  16 */   public static final ASN1ObjectIdentifier ansi_X9_62 = new ASN1ObjectIdentifier("1.2.840.10045");
/*     */ 
/*     */   
/*  19 */   public static final ASN1ObjectIdentifier id_fieldType = ansi_X9_62.branch("1");
/*     */ 
/*     */   
/*  22 */   public static final ASN1ObjectIdentifier prime_field = id_fieldType.branch("1");
/*     */ 
/*     */   
/*  25 */   public static final ASN1ObjectIdentifier characteristic_two_field = id_fieldType.branch("2");
/*     */ 
/*     */   
/*  28 */   public static final ASN1ObjectIdentifier gnBasis = characteristic_two_field.branch("3.1");
/*     */ 
/*     */   
/*  31 */   public static final ASN1ObjectIdentifier tpBasis = characteristic_two_field.branch("3.2");
/*     */ 
/*     */   
/*  34 */   public static final ASN1ObjectIdentifier ppBasis = characteristic_two_field.branch("3.3");
/*     */ 
/*     */   
/*  37 */   public static final ASN1ObjectIdentifier id_ecSigType = ansi_X9_62.branch("4");
/*     */ 
/*     */   
/*  40 */   public static final ASN1ObjectIdentifier ecdsa_with_SHA1 = id_ecSigType.branch("1");
/*     */ 
/*     */   
/*  43 */   public static final ASN1ObjectIdentifier id_publicKeyType = ansi_X9_62.branch("2");
/*     */ 
/*     */   
/*  46 */   public static final ASN1ObjectIdentifier id_ecPublicKey = id_publicKeyType.branch("1");
/*     */ 
/*     */   
/*  49 */   public static final ASN1ObjectIdentifier ecdsa_with_SHA2 = id_ecSigType.branch("3");
/*     */ 
/*     */   
/*  52 */   public static final ASN1ObjectIdentifier ecdsa_with_SHA224 = ecdsa_with_SHA2.branch("1");
/*     */ 
/*     */   
/*  55 */   public static final ASN1ObjectIdentifier ecdsa_with_SHA256 = ecdsa_with_SHA2.branch("2");
/*     */ 
/*     */   
/*  58 */   public static final ASN1ObjectIdentifier ecdsa_with_SHA384 = ecdsa_with_SHA2.branch("3");
/*     */ 
/*     */   
/*  61 */   public static final ASN1ObjectIdentifier ecdsa_with_SHA512 = ecdsa_with_SHA2.branch("4");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static final ASN1ObjectIdentifier ellipticCurve = ansi_X9_62.branch("3");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final ASN1ObjectIdentifier cTwoCurve = ellipticCurve.branch("0");
/*     */ 
/*     */   
/*  78 */   public static final ASN1ObjectIdentifier c2pnb163v1 = cTwoCurve.branch("1");
/*     */   
/*  80 */   public static final ASN1ObjectIdentifier c2pnb163v2 = cTwoCurve.branch("2");
/*     */   
/*  82 */   public static final ASN1ObjectIdentifier c2pnb163v3 = cTwoCurve.branch("3");
/*     */   
/*  84 */   public static final ASN1ObjectIdentifier c2pnb176w1 = cTwoCurve.branch("4");
/*     */   
/*  86 */   public static final ASN1ObjectIdentifier c2tnb191v1 = cTwoCurve.branch("5");
/*     */   
/*  88 */   public static final ASN1ObjectIdentifier c2tnb191v2 = cTwoCurve.branch("6");
/*     */   
/*  90 */   public static final ASN1ObjectIdentifier c2tnb191v3 = cTwoCurve.branch("7");
/*     */   
/*  92 */   public static final ASN1ObjectIdentifier c2onb191v4 = cTwoCurve.branch("8");
/*     */   
/*  94 */   public static final ASN1ObjectIdentifier c2onb191v5 = cTwoCurve.branch("9");
/*     */   
/*  96 */   public static final ASN1ObjectIdentifier c2pnb208w1 = cTwoCurve.branch("10");
/*     */   
/*  98 */   public static final ASN1ObjectIdentifier c2tnb239v1 = cTwoCurve.branch("11");
/*     */   
/* 100 */   public static final ASN1ObjectIdentifier c2tnb239v2 = cTwoCurve.branch("12");
/*     */   
/* 102 */   public static final ASN1ObjectIdentifier c2tnb239v3 = cTwoCurve.branch("13");
/*     */   
/* 104 */   public static final ASN1ObjectIdentifier c2onb239v4 = cTwoCurve.branch("14");
/*     */   
/* 106 */   public static final ASN1ObjectIdentifier c2onb239v5 = cTwoCurve.branch("15");
/*     */   
/* 108 */   public static final ASN1ObjectIdentifier c2pnb272w1 = cTwoCurve.branch("16");
/*     */   
/* 110 */   public static final ASN1ObjectIdentifier c2pnb304w1 = cTwoCurve.branch("17");
/*     */   
/* 112 */   public static final ASN1ObjectIdentifier c2tnb359v1 = cTwoCurve.branch("18");
/*     */   
/* 114 */   public static final ASN1ObjectIdentifier c2pnb368w1 = cTwoCurve.branch("19");
/*     */   
/* 116 */   public static final ASN1ObjectIdentifier c2tnb431r1 = cTwoCurve.branch("20");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final ASN1ObjectIdentifier primeCurve = ellipticCurve.branch("1");
/*     */ 
/*     */   
/* 126 */   public static final ASN1ObjectIdentifier prime192v1 = primeCurve.branch("1");
/*     */   
/* 128 */   public static final ASN1ObjectIdentifier prime192v2 = primeCurve.branch("2");
/*     */   
/* 130 */   public static final ASN1ObjectIdentifier prime192v3 = primeCurve.branch("3");
/*     */   
/* 132 */   public static final ASN1ObjectIdentifier prime239v1 = primeCurve.branch("4");
/*     */   
/* 134 */   public static final ASN1ObjectIdentifier prime239v2 = primeCurve.branch("5");
/*     */   
/* 136 */   public static final ASN1ObjectIdentifier prime239v3 = primeCurve.branch("6");
/*     */   
/* 138 */   public static final ASN1ObjectIdentifier prime256v1 = primeCurve.branch("7");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static final ASN1ObjectIdentifier id_dsa = new ASN1ObjectIdentifier("1.2.840.10040.4.1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public static final ASN1ObjectIdentifier id_dsa_with_sha1 = new ASN1ObjectIdentifier("1.2.840.10040.4.3");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static final ASN1ObjectIdentifier x9_63_scheme = new ASN1ObjectIdentifier("1.3.133.16.840.63.0");
/*     */   
/* 166 */   public static final ASN1ObjectIdentifier dhSinglePass_stdDH_sha1kdf_scheme = x9_63_scheme.branch("2");
/*     */   
/* 168 */   public static final ASN1ObjectIdentifier dhSinglePass_cofactorDH_sha1kdf_scheme = x9_63_scheme.branch("3");
/*     */   
/* 170 */   public static final ASN1ObjectIdentifier mqvSinglePass_sha1kdf_scheme = x9_63_scheme.branch("16");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public static final ASN1ObjectIdentifier ansi_X9_42 = new ASN1ObjectIdentifier("1.2.840.10046");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public static final ASN1ObjectIdentifier dhpublicnumber = ansi_X9_42.branch("2.1");
/*     */ 
/*     */   
/* 190 */   public static final ASN1ObjectIdentifier x9_42_schemes = ansi_X9_42.branch("3");
/*     */   
/* 192 */   public static final ASN1ObjectIdentifier dhStatic = x9_42_schemes.branch("1");
/*     */   
/* 194 */   public static final ASN1ObjectIdentifier dhEphem = x9_42_schemes.branch("2");
/*     */   
/* 196 */   public static final ASN1ObjectIdentifier dhOneFlow = x9_42_schemes.branch("3");
/*     */   
/* 198 */   public static final ASN1ObjectIdentifier dhHybrid1 = x9_42_schemes.branch("4");
/*     */   
/* 200 */   public static final ASN1ObjectIdentifier dhHybrid2 = x9_42_schemes.branch("5");
/*     */   
/* 202 */   public static final ASN1ObjectIdentifier dhHybridOneFlow = x9_42_schemes.branch("6");
/*     */   
/* 204 */   public static final ASN1ObjectIdentifier mqv2 = x9_42_schemes.branch("7");
/*     */   
/* 206 */   public static final ASN1ObjectIdentifier mqv1 = x9_42_schemes.branch("8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public static final ASN1ObjectIdentifier x9_44 = new ASN1ObjectIdentifier("1.3.133.16.840.9.44");
/*     */   
/* 220 */   public static final ASN1ObjectIdentifier x9_44_components = x9_44.branch("1");
/*     */   
/* 222 */   public static final ASN1ObjectIdentifier id_kdf_kdf2 = x9_44_components.branch("1");
/* 223 */   public static final ASN1ObjectIdentifier id_kdf_kdf3 = x9_44_components.branch("2");
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9ObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */