/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
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
/*    */ public class ASN1MD5MessageDigest
/*    */   extends ASN1EncoderMessageDigest
/*    */ {
/*    */   public ASN1MD5MessageDigest() {
/* 35 */     super("ASN1MD5", PKCSObjectIdentifiers.md5);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ASN1MD5MessageDigest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */