/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
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
/*    */ public class ASN1SHA256MessageDigest
/*    */   extends ASN1EncoderMessageDigest
/*    */ {
/*    */   public ASN1SHA256MessageDigest() {
/* 35 */     super("ASN1SHA256", NISTObjectIdentifiers.id_sha256);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ASN1SHA256MessageDigest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */