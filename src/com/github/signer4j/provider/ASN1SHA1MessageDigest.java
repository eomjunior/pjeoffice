/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
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
/*    */ public class ASN1SHA1MessageDigest
/*    */   extends ASN1EncoderMessageDigest
/*    */ {
/*    */   public ASN1SHA1MessageDigest() {
/* 35 */     super("ASN1SHA1", OIWObjectIdentifiers.idSHA1);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ASN1SHA1MessageDigest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */