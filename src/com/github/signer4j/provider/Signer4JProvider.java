/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import java.security.Provider;
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
/*    */ public class Signer4JProvider
/*    */   extends Provider
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Signer4JProvider() {
/* 38 */     this("Signer4J", 1.0D, "Signer4J Security Provider v1.0");
/*    */   }
/*    */   
/*    */   protected Signer4JProvider(String name, double version, String info) {
/* 42 */     super(name, version, info);
/* 43 */     put("Signature.ASN1MD2withRSA", "com.github.signer4j.provider.ASN1MD2withRSASignature");
/* 44 */     put("Signature.ASN1MD5withRSA", "com.github.signer4j.provider.ASN1MD5withRSASignature");
/* 45 */     put("Signature.ASN1SHA1withRSA", "com.github.signer4j.provider.ASN1SHA1withRSASignature");
/* 46 */     put("Signature.ASN1SHA224withRSA", "com.github.signer4j.provider.ASN1SHA224withRSASignature");
/* 47 */     put("Signature.ASN1SHA256withRSA", "com.github.signer4j.provider.ASN1SHA256withRSASignature");
/* 48 */     put("Signature.ASN1SHA384withRSA", "com.github.signer4j.provider.ASN1SHA384withRSASignature");
/* 49 */     put("Signature.ASN1SHA512withRSA", "com.github.signer4j.provider.ASN1SHA512withRSASignature");
/*    */     
/* 51 */     put("MessageDigest.ASN1MD2", "com.github.signer4j.provider.ASN1MD2MessageDigest");
/* 52 */     put("MessageDigest.ASN1MD5", "com.github.signer4j.provider.ASN1MD5MessageDigest");
/* 53 */     put("MessageDigest.ASN1SHA1", "com.github.signer4j.provider.ASN1SHA1MessageDigest");
/* 54 */     put("MessageDigest.ASN1SHA224", "com.github.signer4j.provider.ASN1SHA224MessageDigest");
/* 55 */     put("MessageDigest.ASN1SHA256", "com.github.signer4j.provider.ASN1SHA256MessageDigest");
/* 56 */     put("MessageDigest.ASN1SHA384", "com.github.signer4j.provider.ASN1SHA384MessageDigest");
/* 57 */     put("MessageDigest.ASN1SHA512", "com.github.signer4j.provider.ASN1SHA512MessageDigest");
/*    */     
/* 59 */     put("Signature.TWOSTEPSwithRSA", "com.github.signer4j.provider.TWOSTEPSwithRSASignature");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/Signer4JProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */