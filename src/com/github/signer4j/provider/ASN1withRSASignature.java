/*    */ package com.github.signer4j.provider;
/*    */ 
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
/*    */ public abstract class ASN1withRSASignature
/*    */   extends ANYwithRSASignature
/*    */ {
/*    */   private final MessageDigest digester;
/*    */   
/*    */   public ASN1withRSASignature(String asn1EncoderName) throws Exception {
/* 39 */     super(asn1EncoderName + "withRSA");
/* 40 */     this.digester = MessageDigest.getInstance(asn1EncoderName, ProviderInstaller.SIGNER4J.defaultName());
/*    */   }
/*    */ 
/*    */   
/*    */   protected final MessageDigest getDigester() {
/* 45 */     return this.digester;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ASN1withRSASignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */