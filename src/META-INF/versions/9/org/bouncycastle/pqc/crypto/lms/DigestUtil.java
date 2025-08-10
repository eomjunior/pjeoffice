/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*    */ import org.bouncycastle.crypto.Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA256Digest;
/*    */ import org.bouncycastle.crypto.digests.SHA512Digest;
/*    */ import org.bouncycastle.crypto.digests.SHAKEDigest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DigestUtil
/*    */ {
/* 19 */   private static Map<String, ASN1ObjectIdentifier> nameToOid = new HashMap<>();
/* 20 */   private static Map<ASN1ObjectIdentifier, String> oidToName = new HashMap<>();
/*    */ 
/*    */   
/*    */   static {
/* 24 */     nameToOid.put("SHA-256", NISTObjectIdentifiers.id_sha256);
/* 25 */     nameToOid.put("SHA-512", NISTObjectIdentifiers.id_sha512);
/* 26 */     nameToOid.put("SHAKE128", NISTObjectIdentifiers.id_shake128);
/* 27 */     nameToOid.put("SHAKE256", NISTObjectIdentifiers.id_shake256);
/*    */     
/* 29 */     oidToName.put(NISTObjectIdentifiers.id_sha256, "SHA-256");
/* 30 */     oidToName.put(NISTObjectIdentifiers.id_sha512, "SHA-512");
/* 31 */     oidToName.put(NISTObjectIdentifiers.id_shake128, "SHAKE128");
/* 32 */     oidToName.put(NISTObjectIdentifiers.id_shake256, "SHAKE256");
/*    */   }
/*    */ 
/*    */   
/*    */   static Digest getDigest(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 37 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha256))
/*    */     {
/* 39 */       return (Digest)new SHA256Digest();
/*    */     }
/* 41 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha512))
/*    */     {
/* 43 */       return (Digest)new SHA512Digest();
/*    */     }
/* 45 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake128))
/*    */     {
/* 47 */       return (Digest)new SHAKEDigest(128);
/*    */     }
/* 49 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake256))
/*    */     {
/* 51 */       return (Digest)new SHAKEDigest(256);
/*    */     }
/*    */     
/* 54 */     throw new IllegalArgumentException("unrecognized digest OID: " + paramASN1ObjectIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   static String getDigestName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 59 */     String str = oidToName.get(paramASN1ObjectIdentifier);
/* 60 */     if (str != null)
/*    */     {
/* 62 */       return str;
/*    */     }
/*    */     
/* 65 */     throw new IllegalArgumentException("unrecognized digest oid: " + paramASN1ObjectIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   static ASN1ObjectIdentifier getDigestOID(String paramString) {
/* 70 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = nameToOid.get(paramString);
/* 71 */     if (aSN1ObjectIdentifier != null)
/*    */     {
/* 73 */       return aSN1ObjectIdentifier;
/*    */     }
/*    */     
/* 76 */     throw new IllegalArgumentException("unrecognized digest name: " + paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getDigestSize(Digest paramDigest) {
/* 81 */     if (paramDigest instanceof org.bouncycastle.crypto.Xof)
/*    */     {
/* 83 */       return paramDigest.getDigestSize() * 2;
/*    */     }
/*    */     
/* 86 */     return paramDigest.getDigestSize();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/DigestUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */