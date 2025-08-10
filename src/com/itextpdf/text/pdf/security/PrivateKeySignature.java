/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Signature;
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
/*     */ public class PrivateKeySignature
/*     */   implements ExternalSignature
/*     */ {
/*     */   private PrivateKey pk;
/*     */   private String hashAlgorithm;
/*     */   private String encryptionAlgorithm;
/*     */   private String provider;
/*     */   
/*     */   public PrivateKeySignature(PrivateKey pk, String hashAlgorithm, String provider) {
/*  73 */     this.pk = pk;
/*  74 */     this.provider = provider;
/*  75 */     this.hashAlgorithm = DigestAlgorithms.getDigest(DigestAlgorithms.getAllowedDigests(hashAlgorithm));
/*  76 */     this.encryptionAlgorithm = pk.getAlgorithm();
/*  77 */     if (this.encryptionAlgorithm.startsWith("EC")) {
/*  78 */       this.encryptionAlgorithm = "ECDSA";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHashAlgorithm() {
/*  88 */     return this.hashAlgorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncryptionAlgorithm() {
/*  97 */     return this.encryptionAlgorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] sign(byte[] b) throws GeneralSecurityException {
/*     */     Signature sig;
/* 108 */     String signMode = this.hashAlgorithm + "with" + this.encryptionAlgorithm;
/*     */     
/* 110 */     if (this.provider == null) {
/* 111 */       sig = Signature.getInstance(signMode);
/*     */     } else {
/* 113 */       sig = Signature.getInstance(signMode, this.provider);
/* 114 */     }  sig.initSign(this.pk);
/* 115 */     sig.update(b);
/* 116 */     return sig.sign();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/PrivateKeySignature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */