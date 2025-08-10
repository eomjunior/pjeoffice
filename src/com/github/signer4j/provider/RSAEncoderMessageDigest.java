/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import sun.security.rsa.RSASignature;
/*    */ import sun.security.x509.AlgorithmId;
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
/*    */ public class RSAEncoderMessageDigest
/*    */   extends EncoderMessageDigest
/*    */ {
/*    */   private final MessageDigest digester;
/*    */   private final AlgorithmId hashId;
/*    */   
/*    */   public RSAEncoderMessageDigest(String hashId) throws NoSuchAlgorithmException {
/* 45 */     super("RSAEncoder");
/* 46 */     this.digester = MessageDigest.getInstance(hashId);
/* 47 */     this.hashId = AlgorithmId.get(hashId);
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] doDigest(byte[] input) {
/* 52 */     return this.digester.digest(input);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] encode(byte[] digest) {
/*    */     try {
/* 59 */       return RSASignature.encodeSignature(this.hashId.getOID(), digest);
/* 60 */     } catch (IOException e) {
/* 61 */       throw new RuntimeException("Unabled to encode bytes to sun.security.util.DerValue", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/RSAEncoderMessageDigest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */