/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.pdf.security.ExternalDecryptionProcess;
/*    */ import java.security.Key;
/*    */ import java.security.cert.Certificate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReaderProperties
/*    */ {
/* 52 */   Certificate certificate = null;
/* 53 */   Key certificateKey = null;
/* 54 */   String certificateKeyProvider = null;
/* 55 */   ExternalDecryptionProcess externalDecryptionProcess = null;
/* 56 */   byte[] ownerPassword = null;
/*    */   boolean partialRead = false;
/*    */   boolean closeSourceOnconstructorError = true;
/* 59 */   MemoryLimitsAwareHandler memoryLimitsAwareHandler = null;
/*    */   
/*    */   public ReaderProperties setCertificate(Certificate certificate) {
/* 62 */     this.certificate = certificate;
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public ReaderProperties setCertificateKey(Key certificateKey) {
/* 67 */     this.certificateKey = certificateKey;
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public ReaderProperties setCertificateKeyProvider(String certificateKeyProvider) {
/* 72 */     this.certificateKeyProvider = certificateKeyProvider;
/* 73 */     return this;
/*    */   }
/*    */   
/*    */   public ReaderProperties setExternalDecryptionProcess(ExternalDecryptionProcess externalDecryptionProcess) {
/* 77 */     this.externalDecryptionProcess = externalDecryptionProcess;
/* 78 */     return this;
/*    */   }
/*    */   
/*    */   public ReaderProperties setOwnerPassword(byte[] ownerPassword) {
/* 82 */     this.ownerPassword = ownerPassword;
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public ReaderProperties setPartialRead(boolean partialRead) {
/* 87 */     this.partialRead = partialRead;
/* 88 */     return this;
/*    */   }
/*    */   
/*    */   public ReaderProperties setCloseSourceOnconstructorError(boolean closeSourceOnconstructorError) {
/* 92 */     this.closeSourceOnconstructorError = closeSourceOnconstructorError;
/* 93 */     return this;
/*    */   }
/*    */   
/*    */   public ReaderProperties setMemoryLimitsAwareHandler(MemoryLimitsAwareHandler memoryLimitsAwareHandler) {
/* 97 */     this.memoryLimitsAwareHandler = memoryLimitsAwareHandler;
/* 98 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ReaderProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */