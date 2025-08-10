/*     */ package META-INF.versions.9.org.bouncycastle.asn1.pkcs;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface PKCSObjectIdentifiers
/*     */ {
/*  13 */   public static final ASN1ObjectIdentifier pkcs_1 = new ASN1ObjectIdentifier("1.2.840.113549.1.1");
/*     */   
/*  15 */   public static final ASN1ObjectIdentifier rsaEncryption = pkcs_1.branch("1");
/*     */   
/*  17 */   public static final ASN1ObjectIdentifier md2WithRSAEncryption = pkcs_1.branch("2");
/*     */   
/*  19 */   public static final ASN1ObjectIdentifier md4WithRSAEncryption = pkcs_1.branch("3");
/*     */   
/*  21 */   public static final ASN1ObjectIdentifier md5WithRSAEncryption = pkcs_1.branch("4");
/*     */   
/*  23 */   public static final ASN1ObjectIdentifier sha1WithRSAEncryption = pkcs_1.branch("5");
/*     */   
/*  25 */   public static final ASN1ObjectIdentifier srsaOAEPEncryptionSET = pkcs_1.branch("6");
/*     */   
/*  27 */   public static final ASN1ObjectIdentifier id_RSAES_OAEP = pkcs_1.branch("7");
/*     */   
/*  29 */   public static final ASN1ObjectIdentifier id_mgf1 = pkcs_1.branch("8");
/*     */   
/*  31 */   public static final ASN1ObjectIdentifier id_pSpecified = pkcs_1.branch("9");
/*     */   
/*  33 */   public static final ASN1ObjectIdentifier id_RSASSA_PSS = pkcs_1.branch("10");
/*     */   
/*  35 */   public static final ASN1ObjectIdentifier sha256WithRSAEncryption = pkcs_1.branch("11");
/*     */   
/*  37 */   public static final ASN1ObjectIdentifier sha384WithRSAEncryption = pkcs_1.branch("12");
/*     */   
/*  39 */   public static final ASN1ObjectIdentifier sha512WithRSAEncryption = pkcs_1.branch("13");
/*     */   
/*  41 */   public static final ASN1ObjectIdentifier sha224WithRSAEncryption = pkcs_1.branch("14");
/*     */   
/*  43 */   public static final ASN1ObjectIdentifier sha512_224WithRSAEncryption = pkcs_1.branch("15");
/*     */   
/*  45 */   public static final ASN1ObjectIdentifier sha512_256WithRSAEncryption = pkcs_1.branch("16");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final ASN1ObjectIdentifier pkcs_3 = new ASN1ObjectIdentifier("1.2.840.113549.1.3");
/*     */   
/*  54 */   public static final ASN1ObjectIdentifier dhKeyAgreement = pkcs_3.branch("1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final ASN1ObjectIdentifier pkcs_5 = new ASN1ObjectIdentifier("1.2.840.113549.1.5");
/*     */ 
/*     */   
/*  64 */   public static final ASN1ObjectIdentifier pbeWithMD2AndDES_CBC = pkcs_5.branch("1");
/*     */   
/*  66 */   public static final ASN1ObjectIdentifier pbeWithMD2AndRC2_CBC = pkcs_5.branch("4");
/*     */   
/*  68 */   public static final ASN1ObjectIdentifier pbeWithMD5AndDES_CBC = pkcs_5.branch("3");
/*     */   
/*  70 */   public static final ASN1ObjectIdentifier pbeWithMD5AndRC2_CBC = pkcs_5.branch("6");
/*     */   
/*  72 */   public static final ASN1ObjectIdentifier pbeWithSHA1AndDES_CBC = pkcs_5.branch("10");
/*     */   
/*  74 */   public static final ASN1ObjectIdentifier pbeWithSHA1AndRC2_CBC = pkcs_5.branch("11");
/*     */   
/*  76 */   public static final ASN1ObjectIdentifier id_PBES2 = pkcs_5.branch("13");
/*     */   
/*  78 */   public static final ASN1ObjectIdentifier id_PBKDF2 = pkcs_5.branch("12");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final ASN1ObjectIdentifier encryptionAlgorithm = new ASN1ObjectIdentifier("1.2.840.113549.3");
/*     */ 
/*     */   
/*  88 */   public static final ASN1ObjectIdentifier des_EDE3_CBC = encryptionAlgorithm.branch("7");
/*     */   
/*  90 */   public static final ASN1ObjectIdentifier RC2_CBC = encryptionAlgorithm.branch("2");
/*     */   
/*  92 */   public static final ASN1ObjectIdentifier rc4 = encryptionAlgorithm.branch("4");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final ASN1ObjectIdentifier digestAlgorithm = new ASN1ObjectIdentifier("1.2.840.113549.2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static final ASN1ObjectIdentifier md2 = digestAlgorithm.branch("2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public static final ASN1ObjectIdentifier md4 = digestAlgorithm.branch("4");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static final ASN1ObjectIdentifier md5 = digestAlgorithm.branch("5");
/*     */ 
/*     */   
/* 121 */   public static final ASN1ObjectIdentifier id_hmacWithSHA1 = digestAlgorithm.branch("7").intern();
/*     */   
/* 123 */   public static final ASN1ObjectIdentifier id_hmacWithSHA224 = digestAlgorithm.branch("8").intern();
/*     */   
/* 125 */   public static final ASN1ObjectIdentifier id_hmacWithSHA256 = digestAlgorithm.branch("9").intern();
/*     */   
/* 127 */   public static final ASN1ObjectIdentifier id_hmacWithSHA384 = digestAlgorithm.branch("10").intern();
/*     */   
/* 129 */   public static final ASN1ObjectIdentifier id_hmacWithSHA512 = digestAlgorithm.branch("11").intern();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public static final ASN1ObjectIdentifier pkcs_7 = (new ASN1ObjectIdentifier("1.2.840.113549.1.7")).intern();
/*     */   
/* 138 */   public static final ASN1ObjectIdentifier data = (new ASN1ObjectIdentifier("1.2.840.113549.1.7.1")).intern();
/*     */   
/* 140 */   public static final ASN1ObjectIdentifier signedData = (new ASN1ObjectIdentifier("1.2.840.113549.1.7.2")).intern();
/*     */   
/* 142 */   public static final ASN1ObjectIdentifier envelopedData = (new ASN1ObjectIdentifier("1.2.840.113549.1.7.3")).intern();
/*     */   
/* 144 */   public static final ASN1ObjectIdentifier signedAndEnvelopedData = (new ASN1ObjectIdentifier("1.2.840.113549.1.7.4")).intern();
/*     */   
/* 146 */   public static final ASN1ObjectIdentifier digestedData = (new ASN1ObjectIdentifier("1.2.840.113549.1.7.5")).intern();
/*     */   
/* 148 */   public static final ASN1ObjectIdentifier encryptedData = (new ASN1ObjectIdentifier("1.2.840.113549.1.7.6")).intern();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public static final ASN1ObjectIdentifier pkcs_9 = new ASN1ObjectIdentifier("1.2.840.113549.1.9");
/*     */ 
/*     */   
/* 158 */   public static final ASN1ObjectIdentifier pkcs_9_at_emailAddress = pkcs_9.branch("1").intern();
/*     */   
/* 160 */   public static final ASN1ObjectIdentifier pkcs_9_at_unstructuredName = pkcs_9.branch("2").intern();
/*     */   
/* 162 */   public static final ASN1ObjectIdentifier pkcs_9_at_contentType = pkcs_9.branch("3").intern();
/*     */   
/* 164 */   public static final ASN1ObjectIdentifier pkcs_9_at_messageDigest = pkcs_9.branch("4").intern();
/*     */   
/* 166 */   public static final ASN1ObjectIdentifier pkcs_9_at_signingTime = pkcs_9.branch("5").intern();
/*     */   
/* 168 */   public static final ASN1ObjectIdentifier pkcs_9_at_counterSignature = pkcs_9.branch("6").intern();
/*     */   
/* 170 */   public static final ASN1ObjectIdentifier pkcs_9_at_challengePassword = pkcs_9.branch("7").intern();
/*     */   
/* 172 */   public static final ASN1ObjectIdentifier pkcs_9_at_unstructuredAddress = pkcs_9.branch("8").intern();
/*     */   
/* 174 */   public static final ASN1ObjectIdentifier pkcs_9_at_extendedCertificateAttributes = pkcs_9.branch("9").intern();
/*     */ 
/*     */   
/* 177 */   public static final ASN1ObjectIdentifier pkcs_9_at_signingDescription = pkcs_9.branch("13").intern();
/*     */   
/* 179 */   public static final ASN1ObjectIdentifier pkcs_9_at_extensionRequest = pkcs_9.branch("14").intern();
/*     */   
/* 181 */   public static final ASN1ObjectIdentifier pkcs_9_at_smimeCapabilities = pkcs_9.branch("15").intern();
/*     */   
/* 183 */   public static final ASN1ObjectIdentifier id_smime = pkcs_9.branch("16").intern();
/*     */ 
/*     */   
/* 186 */   public static final ASN1ObjectIdentifier pkcs_9_at_friendlyName = pkcs_9.branch("20").intern();
/*     */   
/* 188 */   public static final ASN1ObjectIdentifier pkcs_9_at_localKeyId = pkcs_9.branch("21").intern();
/*     */ 
/*     */ 
/*     */   
/* 192 */   public static final ASN1ObjectIdentifier x509certType = pkcs_9.branch("22.1");
/*     */ 
/*     */   
/* 195 */   public static final ASN1ObjectIdentifier certTypes = pkcs_9.branch("22");
/*     */   
/* 197 */   public static final ASN1ObjectIdentifier x509Certificate = certTypes.branch("1").intern();
/*     */   
/* 199 */   public static final ASN1ObjectIdentifier sdsiCertificate = certTypes.branch("2").intern();
/*     */ 
/*     */   
/* 202 */   public static final ASN1ObjectIdentifier crlTypes = pkcs_9.branch("23");
/*     */   
/* 204 */   public static final ASN1ObjectIdentifier x509Crl = crlTypes.branch("1").intern();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   public static final ASN1ObjectIdentifier id_aa_cmsAlgorithmProtect = pkcs_9.branch("52").intern();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   public static final ASN1ObjectIdentifier preferSignedData = pkcs_9.branch("15.1");
/*     */   
/* 217 */   public static final ASN1ObjectIdentifier canNotDecryptAny = pkcs_9.branch("15.2");
/*     */   
/* 219 */   public static final ASN1ObjectIdentifier sMIMECapabilitiesVersions = pkcs_9.branch("15.3");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public static final ASN1ObjectIdentifier id_ct = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.1");
/*     */ 
/*     */   
/* 229 */   public static final ASN1ObjectIdentifier id_ct_authData = id_ct.branch("2");
/*     */   
/* 231 */   public static final ASN1ObjectIdentifier id_ct_TSTInfo = id_ct.branch("4");
/*     */   
/* 233 */   public static final ASN1ObjectIdentifier id_ct_compressedData = id_ct.branch("9");
/*     */   
/* 235 */   public static final ASN1ObjectIdentifier id_ct_authEnvelopedData = id_ct.branch("23");
/*     */   
/* 237 */   public static final ASN1ObjectIdentifier id_ct_timestampedData = id_ct.branch("31");
/*     */ 
/*     */ 
/*     */   
/* 241 */   public static final ASN1ObjectIdentifier id_alg = id_smime.branch("3");
/*     */   
/* 243 */   public static final ASN1ObjectIdentifier id_alg_PWRI_KEK = id_alg.branch("9");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 254 */   public static final ASN1ObjectIdentifier id_rsa_KEM = id_alg.branch("14");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   public static final ASN1ObjectIdentifier id_alg_hss_lms_hashsig = id_alg.branch("17");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   public static final ASN1ObjectIdentifier id_alg_AEADChaCha20Poly1305 = id_alg.branch("18");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 281 */   public static final ASN1ObjectIdentifier id_alg_hkdf_with_sha256 = id_alg.branch("28");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   public static final ASN1ObjectIdentifier id_alg_hkdf_with_sha384 = id_alg.branch("29");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 297 */   public static final ASN1ObjectIdentifier id_alg_hkdf_with_sha512 = id_alg.branch("30");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 304 */   public static final ASN1ObjectIdentifier id_cti = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.6");
/*     */ 
/*     */   
/* 307 */   public static final ASN1ObjectIdentifier id_cti_ets_proofOfOrigin = id_cti.branch("1");
/*     */   
/* 309 */   public static final ASN1ObjectIdentifier id_cti_ets_proofOfReceipt = id_cti.branch("2");
/*     */   
/* 311 */   public static final ASN1ObjectIdentifier id_cti_ets_proofOfDelivery = id_cti.branch("3");
/*     */   
/* 313 */   public static final ASN1ObjectIdentifier id_cti_ets_proofOfSender = id_cti.branch("4");
/*     */   
/* 315 */   public static final ASN1ObjectIdentifier id_cti_ets_proofOfApproval = id_cti.branch("5");
/*     */   
/* 317 */   public static final ASN1ObjectIdentifier id_cti_ets_proofOfCreation = id_cti.branch("6");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 324 */   public static final ASN1ObjectIdentifier id_aa = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.2");
/*     */ 
/*     */ 
/*     */   
/* 328 */   public static final ASN1ObjectIdentifier id_aa_receiptRequest = id_aa.branch("1");
/*     */ 
/*     */   
/* 331 */   public static final ASN1ObjectIdentifier id_aa_contentHint = id_aa.branch("4");
/*     */   
/* 333 */   public static final ASN1ObjectIdentifier id_aa_msgSigDigest = id_aa.branch("5");
/*     */   
/* 335 */   public static final ASN1ObjectIdentifier id_aa_contentReference = id_aa.branch("10");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 341 */   public static final ASN1ObjectIdentifier id_aa_encrypKeyPref = id_aa.branch("11");
/*     */   
/* 343 */   public static final ASN1ObjectIdentifier id_aa_signingCertificate = id_aa.branch("12");
/*     */   
/* 345 */   public static final ASN1ObjectIdentifier id_aa_signingCertificateV2 = id_aa.branch("47");
/*     */ 
/*     */   
/* 348 */   public static final ASN1ObjectIdentifier id_aa_contentIdentifier = id_aa.branch("7");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 354 */   public static final ASN1ObjectIdentifier id_aa_signatureTimeStampToken = id_aa.branch("14");
/*     */ 
/*     */   
/* 357 */   public static final ASN1ObjectIdentifier id_aa_ets_sigPolicyId = id_aa.branch("15");
/*     */   
/* 359 */   public static final ASN1ObjectIdentifier id_aa_ets_commitmentType = id_aa.branch("16");
/*     */   
/* 361 */   public static final ASN1ObjectIdentifier id_aa_ets_signerLocation = id_aa.branch("17");
/*     */   
/* 363 */   public static final ASN1ObjectIdentifier id_aa_ets_signerAttr = id_aa.branch("18");
/*     */   
/* 365 */   public static final ASN1ObjectIdentifier id_aa_ets_otherSigCert = id_aa.branch("19");
/*     */   
/* 367 */   public static final ASN1ObjectIdentifier id_aa_ets_contentTimestamp = id_aa.branch("20");
/*     */   
/* 369 */   public static final ASN1ObjectIdentifier id_aa_ets_certificateRefs = id_aa.branch("21");
/*     */   
/* 371 */   public static final ASN1ObjectIdentifier id_aa_ets_revocationRefs = id_aa.branch("22");
/*     */   
/* 373 */   public static final ASN1ObjectIdentifier id_aa_ets_certValues = id_aa.branch("23");
/*     */   
/* 375 */   public static final ASN1ObjectIdentifier id_aa_ets_revocationValues = id_aa.branch("24");
/*     */   
/* 377 */   public static final ASN1ObjectIdentifier id_aa_ets_escTimeStamp = id_aa.branch("25");
/*     */   
/* 379 */   public static final ASN1ObjectIdentifier id_aa_ets_certCRLTimestamp = id_aa.branch("26");
/*     */   
/* 381 */   public static final ASN1ObjectIdentifier id_aa_ets_archiveTimestamp = id_aa.branch("27");
/*     */ 
/*     */   
/* 384 */   public static final ASN1ObjectIdentifier id_aa_decryptKeyID = id_aa.branch("37");
/*     */ 
/*     */   
/* 387 */   public static final ASN1ObjectIdentifier id_aa_implCryptoAlgs = id_aa.branch("38");
/*     */ 
/*     */   
/* 390 */   public static final ASN1ObjectIdentifier id_aa_asymmDecryptKeyID = id_aa.branch("54");
/*     */ 
/*     */   
/* 393 */   public static final ASN1ObjectIdentifier id_aa_implCompressAlgs = id_aa.branch("43");
/*     */   
/* 395 */   public static final ASN1ObjectIdentifier id_aa_communityIdentifiers = id_aa.branch("40");
/*     */ 
/*     */   
/* 398 */   public static final ASN1ObjectIdentifier id_aa_sigPolicyId = id_aa_ets_sigPolicyId;
/*     */   
/* 400 */   public static final ASN1ObjectIdentifier id_aa_commitmentType = id_aa_ets_commitmentType;
/*     */   
/* 402 */   public static final ASN1ObjectIdentifier id_aa_signerLocation = id_aa_ets_signerLocation;
/*     */   
/* 404 */   public static final ASN1ObjectIdentifier id_aa_otherSigCert = id_aa_ets_otherSigCert;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String id_spq = "1.2.840.113549.1.9.16.5";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 414 */   public static final ASN1ObjectIdentifier id_spq_ets_uri = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.5.1");
/*     */   
/* 416 */   public static final ASN1ObjectIdentifier id_spq_ets_unotice = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.5.2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 423 */   public static final ASN1ObjectIdentifier pkcs_12 = new ASN1ObjectIdentifier("1.2.840.113549.1.12");
/*     */   
/* 425 */   public static final ASN1ObjectIdentifier bagtypes = pkcs_12.branch("10.1");
/*     */ 
/*     */   
/* 428 */   public static final ASN1ObjectIdentifier keyBag = bagtypes.branch("1");
/*     */   
/* 430 */   public static final ASN1ObjectIdentifier pkcs8ShroudedKeyBag = bagtypes.branch("2");
/*     */   
/* 432 */   public static final ASN1ObjectIdentifier certBag = bagtypes.branch("3");
/*     */   
/* 434 */   public static final ASN1ObjectIdentifier crlBag = bagtypes.branch("4");
/*     */   
/* 436 */   public static final ASN1ObjectIdentifier secretBag = bagtypes.branch("5");
/*     */   
/* 438 */   public static final ASN1ObjectIdentifier safeContentsBag = bagtypes.branch("6");
/*     */ 
/*     */   
/* 441 */   public static final ASN1ObjectIdentifier pkcs_12PbeIds = pkcs_12.branch("1");
/*     */ 
/*     */   
/* 444 */   public static final ASN1ObjectIdentifier pbeWithSHAAnd128BitRC4 = pkcs_12PbeIds.branch("1");
/*     */   
/* 446 */   public static final ASN1ObjectIdentifier pbeWithSHAAnd40BitRC4 = pkcs_12PbeIds.branch("2");
/*     */   
/* 448 */   public static final ASN1ObjectIdentifier pbeWithSHAAnd3_KeyTripleDES_CBC = pkcs_12PbeIds.branch("3");
/*     */   
/* 450 */   public static final ASN1ObjectIdentifier pbeWithSHAAnd2_KeyTripleDES_CBC = pkcs_12PbeIds.branch("4");
/*     */   
/* 452 */   public static final ASN1ObjectIdentifier pbeWithSHAAnd128BitRC2_CBC = pkcs_12PbeIds.branch("5");
/*     */   
/* 454 */   public static final ASN1ObjectIdentifier pbeWithSHAAnd40BitRC2_CBC = pkcs_12PbeIds.branch("6");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 460 */   public static final ASN1ObjectIdentifier pbewithSHAAnd40BitRC2_CBC = pkcs_12PbeIds.branch("6");
/*     */ 
/*     */   
/* 463 */   public static final ASN1ObjectIdentifier id_alg_CMS3DESwrap = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.3.6");
/*     */   
/* 465 */   public static final ASN1ObjectIdentifier id_alg_CMSRC2wrap = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.3.7");
/*     */   
/* 467 */   public static final ASN1ObjectIdentifier id_alg_ESDH = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.3.5");
/*     */   
/* 469 */   public static final ASN1ObjectIdentifier id_alg_SSDH = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.3.10");
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/pkcs/PKCSObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */