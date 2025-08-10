/*    */ package com.itextpdf.text.pdf.security;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import org.bouncycastle.jcajce.provider.digest.GOST3411;
/*    */ import org.bouncycastle.jcajce.provider.digest.MD2;
/*    */ import org.bouncycastle.jcajce.provider.digest.MD5;
/*    */ import org.bouncycastle.jcajce.provider.digest.RIPEMD128;
/*    */ import org.bouncycastle.jcajce.provider.digest.RIPEMD160;
/*    */ import org.bouncycastle.jcajce.provider.digest.RIPEMD256;
/*    */ import org.bouncycastle.jcajce.provider.digest.SHA1;
/*    */ import org.bouncycastle.jcajce.provider.digest.SHA224;
/*    */ import org.bouncycastle.jcajce.provider.digest.SHA256;
/*    */ import org.bouncycastle.jcajce.provider.digest.SHA384;
/*    */ import org.bouncycastle.jcajce.provider.digest.SHA512;
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
/*    */ public class BouncyCastleDigest
/*    */   implements ExternalDigest
/*    */ {
/*    */   public MessageDigest getMessageDigest(String hashAlgorithm) throws GeneralSecurityException {
/* 58 */     String oid = DigestAlgorithms.getAllowedDigests(hashAlgorithm);
/* 59 */     if (oid == null)
/* 60 */       throw new NoSuchAlgorithmException(hashAlgorithm); 
/* 61 */     if (oid.equals("1.2.840.113549.2.2")) {
/* 62 */       return (MessageDigest)new MD2.Digest();
/*    */     }
/* 64 */     if (oid.equals("1.2.840.113549.2.5")) {
/* 65 */       return (MessageDigest)new MD5.Digest();
/*    */     }
/* 67 */     if (oid.equals("1.3.14.3.2.26")) {
/* 68 */       return (MessageDigest)new SHA1.Digest();
/*    */     }
/* 70 */     if (oid.equals("2.16.840.1.101.3.4.2.4")) {
/* 71 */       return (MessageDigest)new SHA224.Digest();
/*    */     }
/* 73 */     if (oid.equals("2.16.840.1.101.3.4.2.1")) {
/* 74 */       return (MessageDigest)new SHA256.Digest();
/*    */     }
/* 76 */     if (oid.equals("2.16.840.1.101.3.4.2.2")) {
/* 77 */       return (MessageDigest)new SHA384.Digest();
/*    */     }
/* 79 */     if (oid.equals("2.16.840.1.101.3.4.2.3")) {
/* 80 */       return (MessageDigest)new SHA512.Digest();
/*    */     }
/* 82 */     if (oid.equals("1.3.36.3.2.2")) {
/* 83 */       return (MessageDigest)new RIPEMD128.Digest();
/*    */     }
/* 85 */     if (oid.equals("1.3.36.3.2.1")) {
/* 86 */       return (MessageDigest)new RIPEMD160.Digest();
/*    */     }
/* 88 */     if (oid.equals("1.3.36.3.2.3")) {
/* 89 */       return (MessageDigest)new RIPEMD256.Digest();
/*    */     }
/* 91 */     if (oid.equals("1.2.643.2.2.9")) {
/* 92 */       return (MessageDigest)new GOST3411.Digest();
/*    */     }
/*    */     
/* 95 */     throw new NoSuchAlgorithmException(hashAlgorithm);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/BouncyCastleDigest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */