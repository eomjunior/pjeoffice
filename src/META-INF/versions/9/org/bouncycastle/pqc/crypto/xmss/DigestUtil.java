/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
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
/*    */ class DigestUtil
/*    */ {
/* 16 */   private static Map<String, ASN1ObjectIdentifier> nameToOid = new HashMap<>();
/* 17 */   private static Map<ASN1ObjectIdentifier, String> oidToName = new HashMap<>();
/*    */ 
/*    */   
/*    */   static {
/* 21 */     nameToOid.put("SHA-256", NISTObjectIdentifiers.id_sha256);
/* 22 */     nameToOid.put("SHA-512", NISTObjectIdentifiers.id_sha512);
/* 23 */     nameToOid.put("SHAKE128", NISTObjectIdentifiers.id_shake128);
/* 24 */     nameToOid.put("SHAKE256", NISTObjectIdentifiers.id_shake256);
/*    */     
/* 26 */     oidToName.put(NISTObjectIdentifiers.id_sha256, "SHA-256");
/* 27 */     oidToName.put(NISTObjectIdentifiers.id_sha512, "SHA-512");
/* 28 */     oidToName.put(NISTObjectIdentifiers.id_shake128, "SHAKE128");
/* 29 */     oidToName.put(NISTObjectIdentifiers.id_shake256, "SHAKE256");
/*    */   }
/*    */ 
/*    */   
/*    */   static Digest getDigest(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 34 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha256))
/*    */     {
/* 36 */       return (Digest)new SHA256Digest();
/*    */     }
/* 38 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha512))
/*    */     {
/* 40 */       return (Digest)new SHA512Digest();
/*    */     }
/* 42 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake128))
/*    */     {
/* 44 */       return (Digest)new SHAKEDigest(128);
/*    */     }
/* 46 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake256))
/*    */     {
/* 48 */       return (Digest)new SHAKEDigest(256);
/*    */     }
/*    */     
/* 51 */     throw new IllegalArgumentException("unrecognized digest OID: " + paramASN1ObjectIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   static String getDigestName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 56 */     String str = oidToName.get(paramASN1ObjectIdentifier);
/* 57 */     if (str != null)
/*    */     {
/* 59 */       return str;
/*    */     }
/*    */     
/* 62 */     throw new IllegalArgumentException("unrecognized digest oid: " + paramASN1ObjectIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   static ASN1ObjectIdentifier getDigestOID(String paramString) {
/* 67 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = nameToOid.get(paramString);
/* 68 */     if (aSN1ObjectIdentifier != null)
/*    */     {
/* 70 */       return aSN1ObjectIdentifier;
/*    */     }
/*    */     
/* 73 */     throw new IllegalArgumentException("unrecognized digest name: " + paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getDigestSize(Digest paramDigest) {
/* 78 */     if (paramDigest instanceof org.bouncycastle.crypto.Xof)
/*    */     {
/* 80 */       return paramDigest.getDigestSize() * 2;
/*    */     }
/*    */     
/* 83 */     return paramDigest.getDigestSize();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/DigestUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */