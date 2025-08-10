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
/*    */ public class ASN1SHA384MessageDigest
/*    */   extends ASN1EncoderMessageDigest
/*    */ {
/*    */   public ASN1SHA384MessageDigest() {
/* 35 */     super("ASN1SHA384", NISTObjectIdentifiers.id_sha384);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ASN1SHA384MessageDigest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */