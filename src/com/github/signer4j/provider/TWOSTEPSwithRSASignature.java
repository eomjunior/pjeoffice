/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import com.github.utils4j.imp.States;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.NoSuchProviderException;
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
/*    */ public class TWOSTEPSwithRSASignature
/*    */   extends ANYwithRSASignature
/*    */ {
/*    */   private MessageDigest digester;
/*    */   
/*    */   public TWOSTEPSwithRSASignature() throws Exception {
/* 55 */     super("TWOSTEPSwithRSA");
/*    */   }
/*    */   
/*    */   private void checkDigestAvailable() {
/* 59 */     States.requireNonNull(Boolean.valueOf((this.digester != null)), "digest is not defined by engineSetParameter");
/*    */   }
/*    */ 
/*    */   
/*    */   protected MessageDigest getDigester() {
/* 64 */     checkDigestAvailable();
/* 65 */     return this.digester;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupDigester(String hashName) throws NoSuchAlgorithmException, NoSuchProviderException {
/* 70 */     this.digester = new RSAEncoderMessageDigest(hashName);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/TWOSTEPSwithRSASignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */