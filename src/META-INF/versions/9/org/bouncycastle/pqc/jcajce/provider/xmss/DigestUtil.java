/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.xmss;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.crypto.Xof;
/*     */ import org.bouncycastle.crypto.digests.SHA256Digest;
/*     */ import org.bouncycastle.crypto.digests.SHA512Digest;
/*     */ import org.bouncycastle.crypto.digests.SHAKEDigest;
/*     */ 
/*     */ 
/*     */ class DigestUtil
/*     */ {
/*     */   static Digest getDigest(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/*  16 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha256))
/*     */     {
/*  18 */       return (Digest)new SHA256Digest();
/*     */     }
/*  20 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha512))
/*     */     {
/*  22 */       return (Digest)new SHA512Digest();
/*     */     }
/*  24 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake128))
/*     */     {
/*  26 */       return (Digest)new SHAKEDigest(128);
/*     */     }
/*  28 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake256))
/*     */     {
/*  30 */       return (Digest)new SHAKEDigest(256);
/*     */     }
/*     */     
/*  33 */     throw new IllegalArgumentException("unrecognized digest OID: " + paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   static ASN1ObjectIdentifier getDigestOID(String paramString) {
/*  38 */     if (paramString.equals("SHA-256"))
/*     */     {
/*  40 */       return NISTObjectIdentifiers.id_sha256;
/*     */     }
/*  42 */     if (paramString.equals("SHA-512"))
/*     */     {
/*  44 */       return NISTObjectIdentifiers.id_sha512;
/*     */     }
/*  46 */     if (paramString.equals("SHAKE128"))
/*     */     {
/*  48 */       return NISTObjectIdentifiers.id_shake128;
/*     */     }
/*  50 */     if (paramString.equals("SHAKE256"))
/*     */     {
/*  52 */       return NISTObjectIdentifiers.id_shake256;
/*     */     }
/*     */     
/*  55 */     throw new IllegalArgumentException("unrecognized digest: " + paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getDigestResult(Digest paramDigest) {
/*  60 */     byte[] arrayOfByte = new byte[getDigestSize(paramDigest)];
/*     */     
/*  62 */     if (paramDigest instanceof Xof) {
/*     */       
/*  64 */       ((Xof)paramDigest).doFinal(arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/*     */     else {
/*     */       
/*  68 */       paramDigest.doFinal(arrayOfByte, 0);
/*     */     } 
/*     */     
/*  71 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getDigestSize(Digest paramDigest) {
/*  76 */     if (paramDigest instanceof Xof)
/*     */     {
/*  78 */       return paramDigest.getDigestSize() * 2;
/*     */     }
/*     */     
/*  81 */     return paramDigest.getDigestSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getXMSSDigestName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/*  86 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha256))
/*     */     {
/*  88 */       return "SHA256";
/*     */     }
/*  90 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha512))
/*     */     {
/*  92 */       return "SHA512";
/*     */     }
/*  94 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake128))
/*     */     {
/*  96 */       return "SHAKE128";
/*     */     }
/*  98 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake256))
/*     */     {
/* 100 */       return "SHAKE256";
/*     */     }
/*     */     
/* 103 */     throw new IllegalArgumentException("unrecognized digest OID: " + paramASN1ObjectIdentifier);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/xmss/DigestUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */