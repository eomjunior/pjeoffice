/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.IOException;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.DERNull;
/*    */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*    */ import org.bouncycastle.asn1.x509.DigestInfo;
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
/*    */ public abstract class ASN1EncoderMessageDigest
/*    */   extends EncoderMessageDigest
/*    */ {
/*    */   private final ASN1ObjectIdentifier algorithm;
/*    */   
/*    */   protected ASN1EncoderMessageDigest(String name, ASN1ObjectIdentifier hashId) {
/* 45 */     super(name);
/* 46 */     this.algorithm = (ASN1ObjectIdentifier)Args.requireNonNull(hashId, "hashId is null");
/*    */   }
/*    */ 
/*    */   
/*    */   protected final byte[] doDigest(byte[] rawDigest) {
/* 51 */     return rawDigest;
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] encode(byte[] digest) {
/*    */     try {
/* 57 */       return (new DigestInfo(new AlgorithmIdentifier(this.algorithm, (ASN1Encodable)DERNull.INSTANCE), digest)).getEncoded("DER");
/* 58 */     } catch (IOException e) {
/* 59 */       throw new RuntimeException("Unabled to encode bytes to ASN1Encoding.DER", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ASN1EncoderMessageDigest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */