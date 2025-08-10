/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IPersonalData;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Base64;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.List;
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
/*     */ public class SignedData
/*     */   extends CertificateAware
/*     */   implements ISignedData
/*     */ {
/*     */   private final byte[] signedData;
/*     */   private final IPersonalData personalData;
/*     */   private String signature64Cache;
/*     */   
/*     */   public static ISignedData from(byte[] signedBytes, IPersonalData personalData) {
/*  47 */     return new SignedData(signedBytes, personalData);
/*     */   }
/*     */   
/*     */   public static ISignedData forTest() {
/*  51 */     return new SignedData("ASSINATURA_MODO_TESTE".getBytes(IConstants.DEFAULT_CHARSET), (IPersonalData)Choice.CANCEL)
/*     */       {
/*     */         public String getCertificateChain64() throws CertificateException {
/*  54 */           return Base64.base64Encode("coração intrépido infalível".getBytes(IConstants.DEFAULT_CHARSET));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SignedData(byte[] signedBytes, IPersonalData personalData) {
/*  64 */     this.signedData = Args.requireNonEmpty(signedBytes, "empty signed bytes");
/*  65 */     this.personalData = (IPersonalData)Args.requireNonNull(personalData, "personalData is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final PrivateKey getPrivateKey() {
/*  70 */     return this.personalData.getPrivateKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Certificate getCertificate() {
/*  75 */     return this.personalData.getCertificate();
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<Certificate> getCertificateChain() {
/*  80 */     return this.personalData.getCertificateChain();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int chainSize() {
/*  85 */     return this.personalData.chainSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte[] getSignature() {
/*  90 */     return this.signedData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getSignature64() {
/*  96 */     return (this.signature64Cache != null) ? this.signature64Cache : (this
/*  97 */       .signature64Cache = Base64.base64Encode(this.signedData));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeTo(OutputStream out) throws IOException {
/* 102 */     out.write(this.signedData);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SignedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */