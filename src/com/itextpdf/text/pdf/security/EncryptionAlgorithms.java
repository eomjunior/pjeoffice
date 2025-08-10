/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
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
/*     */ public class EncryptionAlgorithms
/*     */ {
/*  55 */   static final HashMap<String, String> algorithmNames = new HashMap<String, String>();
/*     */   
/*     */   static {
/*  58 */     algorithmNames.put("1.2.840.113549.1.1.1", "RSA");
/*  59 */     algorithmNames.put("1.2.840.10040.4.1", "DSA");
/*  60 */     algorithmNames.put("1.2.840.113549.1.1.2", "RSA");
/*  61 */     algorithmNames.put("1.2.840.113549.1.1.4", "RSA");
/*  62 */     algorithmNames.put("1.2.840.113549.1.1.5", "RSA");
/*  63 */     algorithmNames.put("1.2.840.113549.1.1.14", "RSA");
/*  64 */     algorithmNames.put("1.2.840.113549.1.1.11", "RSA");
/*  65 */     algorithmNames.put("1.2.840.113549.1.1.12", "RSA");
/*  66 */     algorithmNames.put("1.2.840.113549.1.1.13", "RSA");
/*  67 */     algorithmNames.put("1.2.840.10040.4.3", "DSA");
/*  68 */     algorithmNames.put("2.16.840.1.101.3.4.3.1", "DSA");
/*  69 */     algorithmNames.put("2.16.840.1.101.3.4.3.2", "DSA");
/*  70 */     algorithmNames.put("1.3.14.3.2.29", "RSA");
/*  71 */     algorithmNames.put("1.3.36.3.3.1.2", "RSA");
/*  72 */     algorithmNames.put("1.3.36.3.3.1.3", "RSA");
/*  73 */     algorithmNames.put("1.3.36.3.3.1.4", "RSA");
/*  74 */     algorithmNames.put("1.2.643.2.2.19", "ECGOST3410");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getAlgorithm(String oid) {
/*  83 */     String ret = algorithmNames.get(oid);
/*  84 */     if (ret == null) {
/*  85 */       return oid;
/*     */     }
/*  87 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean addAlgorithm(String oid, String name) throws GeneralSecurityException {
/*  96 */     boolean status = false;
/*  97 */     if (oid != null && !oid.equals("")) {
/*  98 */       if (!algorithmNames.containsKey(oid)) {
/*  99 */         algorithmNames.put(oid, name);
/* 100 */         status = true;
/* 101 */       } else if (((String)algorithmNames.get(oid)).equals(name)) {
/* 102 */         status = false;
/*     */       } else {
/* 104 */         throw new GeneralSecurityException("already registered oid=" + oid + ", with name=" + (String)algorithmNames.get(oid));
/*     */       } 
/*     */     } else {
/* 107 */       throw new GeneralSecurityException("Can not register oid's with null or empty");
/*     */     } 
/* 109 */     return status;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/EncryptionAlgorithms.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */