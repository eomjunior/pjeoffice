/*    */ package com.itextpdf.text.pdf.security;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ import java.security.MessageDigest;
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
/*    */ 
/*    */ 
/*    */ public class ProviderDigest
/*    */   implements ExternalDigest
/*    */ {
/*    */   private String provider;
/*    */   
/*    */   public ProviderDigest(String provider) {
/* 57 */     this.provider = provider;
/*    */   }
/*    */   
/*    */   public MessageDigest getMessageDigest(String hashAlgorithm) throws GeneralSecurityException {
/* 61 */     return DigestAlgorithms.getMessageDigest(hashAlgorithm, this.provider);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/ProviderDigest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */