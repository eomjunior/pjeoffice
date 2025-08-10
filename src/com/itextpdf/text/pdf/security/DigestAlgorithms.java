/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.util.HashMap;
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
/*     */ public class DigestAlgorithms
/*     */ {
/*     */   public static final String SHA1 = "SHA-1";
/*     */   public static final String SHA256 = "SHA-256";
/*     */   public static final String SHA384 = "SHA-384";
/*     */   public static final String SHA512 = "SHA-512";
/*     */   public static final String RIPEMD160 = "RIPEMD160";
/*  75 */   private static final HashMap<String, String> digestNames = new HashMap<String, String>();
/*     */ 
/*     */   
/*  78 */   private static final HashMap<String, String> fixNames = new HashMap<String, String>();
/*     */ 
/*     */   
/*  81 */   private static final HashMap<String, String> allowedDigests = new HashMap<String, String>();
/*     */   
/*     */   static {
/*  84 */     digestNames.put("1.2.840.113549.2.5", "MD5");
/*  85 */     digestNames.put("1.2.840.113549.2.2", "MD2");
/*  86 */     digestNames.put("1.3.14.3.2.26", "SHA1");
/*  87 */     digestNames.put("2.16.840.1.101.3.4.2.4", "SHA224");
/*  88 */     digestNames.put("2.16.840.1.101.3.4.2.1", "SHA256");
/*  89 */     digestNames.put("2.16.840.1.101.3.4.2.2", "SHA384");
/*  90 */     digestNames.put("2.16.840.1.101.3.4.2.3", "SHA512");
/*  91 */     digestNames.put("1.3.36.3.2.2", "RIPEMD128");
/*  92 */     digestNames.put("1.3.36.3.2.1", "RIPEMD160");
/*  93 */     digestNames.put("1.3.36.3.2.3", "RIPEMD256");
/*  94 */     digestNames.put("1.2.840.113549.1.1.4", "MD5");
/*  95 */     digestNames.put("1.2.840.113549.1.1.2", "MD2");
/*  96 */     digestNames.put("1.2.840.113549.1.1.5", "SHA1");
/*  97 */     digestNames.put("1.2.840.113549.1.1.14", "SHA224");
/*  98 */     digestNames.put("1.2.840.113549.1.1.11", "SHA256");
/*  99 */     digestNames.put("1.2.840.113549.1.1.12", "SHA384");
/* 100 */     digestNames.put("1.2.840.113549.1.1.13", "SHA512");
/* 101 */     digestNames.put("1.2.840.113549.2.5", "MD5");
/* 102 */     digestNames.put("1.2.840.113549.2.2", "MD2");
/* 103 */     digestNames.put("1.2.840.10040.4.3", "SHA1");
/* 104 */     digestNames.put("2.16.840.1.101.3.4.3.1", "SHA224");
/* 105 */     digestNames.put("2.16.840.1.101.3.4.3.2", "SHA256");
/* 106 */     digestNames.put("2.16.840.1.101.3.4.3.3", "SHA384");
/* 107 */     digestNames.put("2.16.840.1.101.3.4.3.4", "SHA512");
/* 108 */     digestNames.put("1.3.36.3.3.1.3", "RIPEMD128");
/* 109 */     digestNames.put("1.3.36.3.3.1.2", "RIPEMD160");
/* 110 */     digestNames.put("1.3.36.3.3.1.4", "RIPEMD256");
/* 111 */     digestNames.put("1.2.643.2.2.9", "GOST3411");
/*     */     
/* 113 */     fixNames.put("SHA256", "SHA-256");
/* 114 */     fixNames.put("SHA384", "SHA-384");
/* 115 */     fixNames.put("SHA512", "SHA-512");
/*     */     
/* 117 */     allowedDigests.put("MD2", "1.2.840.113549.2.2");
/* 118 */     allowedDigests.put("MD-2", "1.2.840.113549.2.2");
/* 119 */     allowedDigests.put("MD5", "1.2.840.113549.2.5");
/* 120 */     allowedDigests.put("MD-5", "1.2.840.113549.2.5");
/* 121 */     allowedDigests.put("SHA1", "1.3.14.3.2.26");
/* 122 */     allowedDigests.put("SHA-1", "1.3.14.3.2.26");
/* 123 */     allowedDigests.put("SHA224", "2.16.840.1.101.3.4.2.4");
/* 124 */     allowedDigests.put("SHA-224", "2.16.840.1.101.3.4.2.4");
/* 125 */     allowedDigests.put("SHA256", "2.16.840.1.101.3.4.2.1");
/* 126 */     allowedDigests.put("SHA-256", "2.16.840.1.101.3.4.2.1");
/* 127 */     allowedDigests.put("SHA384", "2.16.840.1.101.3.4.2.2");
/* 128 */     allowedDigests.put("SHA-384", "2.16.840.1.101.3.4.2.2");
/* 129 */     allowedDigests.put("SHA512", "2.16.840.1.101.3.4.2.3");
/* 130 */     allowedDigests.put("SHA-512", "2.16.840.1.101.3.4.2.3");
/* 131 */     allowedDigests.put("RIPEMD128", "1.3.36.3.2.2");
/* 132 */     allowedDigests.put("RIPEMD-128", "1.3.36.3.2.2");
/* 133 */     allowedDigests.put("RIPEMD160", "1.3.36.3.2.1");
/* 134 */     allowedDigests.put("RIPEMD-160", "1.3.36.3.2.1");
/* 135 */     allowedDigests.put("RIPEMD256", "1.3.36.3.2.3");
/* 136 */     allowedDigests.put("RIPEMD-256", "1.3.36.3.2.3");
/* 137 */     allowedDigests.put("GOST3411", "1.2.643.2.2.9");
/*     */   }
/*     */ 
/*     */   
/*     */   public static MessageDigest getMessageDigestFromOid(String digestOid, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
/* 142 */     return getMessageDigest(getDigest(digestOid), provider);
/*     */   }
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
/*     */   public static MessageDigest getMessageDigest(String hashAlgorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
/* 156 */     if (provider == null || provider.startsWith("SunPKCS11") || provider.startsWith("SunMSCAPI")) {
/* 157 */       return MessageDigest.getInstance(normalizeDigestName(hashAlgorithm));
/*     */     }
/* 159 */     return MessageDigest.getInstance(hashAlgorithm, provider);
/*     */   }
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
/*     */   public static byte[] digest(InputStream data, String hashAlgorithm, String provider) throws GeneralSecurityException, IOException {
/* 174 */     MessageDigest messageDigest = getMessageDigest(hashAlgorithm, provider);
/* 175 */     return digest(data, messageDigest);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] digest(InputStream data, MessageDigest messageDigest) throws GeneralSecurityException, IOException {
/* 180 */     byte[] buf = new byte[8192];
/*     */     int n;
/* 182 */     while ((n = data.read(buf)) > 0) {
/* 183 */       messageDigest.update(buf, 0, n);
/*     */     }
/* 185 */     return messageDigest.digest();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDigest(String oid) {
/* 194 */     String ret = digestNames.get(oid);
/* 195 */     if (ret == null) {
/* 196 */       return oid;
/*     */     }
/* 198 */     return ret;
/*     */   }
/*     */   
/*     */   public static String normalizeDigestName(String algo) {
/* 202 */     if (fixNames.containsKey(algo))
/* 203 */       return fixNames.get(algo); 
/* 204 */     return algo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getAllowedDigests(String name) {
/* 214 */     return allowedDigests.get(name.toUpperCase());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/DigestAlgorithms.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */