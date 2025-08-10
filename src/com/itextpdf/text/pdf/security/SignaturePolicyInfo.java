/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.pdf.codec.Base64;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.DERIA5String;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.esf.OtherHashAlgAndValue;
/*     */ import org.bouncycastle.asn1.esf.SigPolicyQualifierInfo;
/*     */ import org.bouncycastle.asn1.esf.SigPolicyQualifiers;
/*     */ import org.bouncycastle.asn1.esf.SignaturePolicyId;
/*     */ import org.bouncycastle.asn1.esf.SignaturePolicyIdentifier;
/*     */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
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
/*     */ public class SignaturePolicyInfo
/*     */ {
/*     */   private String policyIdentifier;
/*     */   private byte[] policyHash;
/*     */   private String policyDigestAlgorithm;
/*     */   private String policyUri;
/*     */   
/*     */   public SignaturePolicyInfo(String policyIdentifier, byte[] policyHash, String policyDigestAlgorithm, String policyUri) {
/*  69 */     if (policyIdentifier == null || policyIdentifier.length() == 0) {
/*  70 */       throw new IllegalArgumentException("Policy identifier cannot be null");
/*     */     }
/*  72 */     if (policyHash == null) {
/*  73 */       throw new IllegalArgumentException("Policy hash cannot be null");
/*     */     }
/*  75 */     if (policyDigestAlgorithm == null || policyDigestAlgorithm.length() == 0) {
/*  76 */       throw new IllegalArgumentException("Policy digest algorithm cannot be null");
/*     */     }
/*     */     
/*  79 */     this.policyIdentifier = policyIdentifier;
/*  80 */     this.policyHash = policyHash;
/*  81 */     this.policyDigestAlgorithm = policyDigestAlgorithm;
/*  82 */     this.policyUri = policyUri;
/*     */   }
/*     */   
/*     */   public SignaturePolicyInfo(String policyIdentifier, String policyHashBase64, String policyDigestAlgorithm, String policyUri) {
/*  86 */     this(policyIdentifier, (policyHashBase64 != null) ? Base64.decode(policyHashBase64) : null, policyDigestAlgorithm, policyUri);
/*     */   }
/*     */   
/*     */   public String getPolicyIdentifier() {
/*  90 */     return this.policyIdentifier;
/*     */   }
/*     */   
/*     */   public byte[] getPolicyHash() {
/*  94 */     return this.policyHash;
/*     */   }
/*     */   
/*     */   public String getPolicyDigestAlgorithm() {
/*  98 */     return this.policyDigestAlgorithm;
/*     */   }
/*     */   
/*     */   public String getPolicyUri() {
/* 102 */     return this.policyUri;
/*     */   }
/*     */   
/*     */   SignaturePolicyIdentifier toSignaturePolicyIdentifier() {
/* 106 */     String algId = DigestAlgorithms.getAllowedDigests(this.policyDigestAlgorithm);
/*     */     
/* 108 */     if (algId == null || algId.length() == 0) {
/* 109 */       throw new IllegalArgumentException("Invalid policy hash algorithm");
/*     */     }
/*     */     
/* 112 */     SignaturePolicyIdentifier signaturePolicyIdentifier = null;
/* 113 */     SigPolicyQualifierInfo spqi = null;
/*     */     
/* 115 */     if (this.policyUri != null && this.policyUri.length() > 0) {
/* 116 */       spqi = new SigPolicyQualifierInfo(PKCSObjectIdentifiers.id_spq_ets_uri, (ASN1Encodable)new DERIA5String(this.policyUri));
/*     */     }
/* 118 */     SigPolicyQualifiers qualifiers = new SigPolicyQualifiers(new SigPolicyQualifierInfo[] { spqi });
/*     */     
/* 120 */     signaturePolicyIdentifier = new SignaturePolicyIdentifier(new SignaturePolicyId(ASN1ObjectIdentifier.getInstance(new ASN1ObjectIdentifier(this.policyIdentifier.replace("urn:oid:", ""))), new OtherHashAlgAndValue(new AlgorithmIdentifier(new ASN1ObjectIdentifier(algId)), (ASN1OctetString)new DEROctetString(this.policyHash)), qualifiers));
/*     */ 
/*     */     
/* 123 */     return signaturePolicyIdentifier;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/SignaturePolicyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */