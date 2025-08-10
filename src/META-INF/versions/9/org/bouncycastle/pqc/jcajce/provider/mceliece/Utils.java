/*    */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.mceliece;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.DERNull;
/*    */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*    */ import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
/*    */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*    */ import org.bouncycastle.crypto.Digest;
/*    */ import org.bouncycastle.crypto.util.DigestFactory;
/*    */ 
/*    */ class Utils {
/*    */   static AlgorithmIdentifier getDigAlgId(String paramString) {
/* 14 */     if (paramString.equals("SHA-1"))
/*    */     {
/* 16 */       return new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, (ASN1Encodable)DERNull.INSTANCE);
/*    */     }
/* 18 */     if (paramString.equals("SHA-224"))
/*    */     {
/* 20 */       return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha224, (ASN1Encodable)DERNull.INSTANCE);
/*    */     }
/* 22 */     if (paramString.equals("SHA-256"))
/*    */     {
/* 24 */       return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, (ASN1Encodable)DERNull.INSTANCE);
/*    */     }
/* 26 */     if (paramString.equals("SHA-384"))
/*    */     {
/* 28 */       return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha384, (ASN1Encodable)DERNull.INSTANCE);
/*    */     }
/* 30 */     if (paramString.equals("SHA-512"))
/*    */     {
/* 32 */       return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512, (ASN1Encodable)DERNull.INSTANCE);
/*    */     }
/*    */     
/* 35 */     throw new IllegalArgumentException("unrecognised digest algorithm: " + paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   static Digest getDigest(AlgorithmIdentifier paramAlgorithmIdentifier) {
/* 40 */     if (paramAlgorithmIdentifier.getAlgorithm().equals((ASN1Primitive)OIWObjectIdentifiers.idSHA1))
/*    */     {
/* 42 */       return DigestFactory.createSHA1();
/*    */     }
/* 44 */     if (paramAlgorithmIdentifier.getAlgorithm().equals((ASN1Primitive)NISTObjectIdentifiers.id_sha224))
/*    */     {
/* 46 */       return DigestFactory.createSHA224();
/*    */     }
/* 48 */     if (paramAlgorithmIdentifier.getAlgorithm().equals((ASN1Primitive)NISTObjectIdentifiers.id_sha256))
/*    */     {
/* 50 */       return DigestFactory.createSHA256();
/*    */     }
/* 52 */     if (paramAlgorithmIdentifier.getAlgorithm().equals((ASN1Primitive)NISTObjectIdentifiers.id_sha384))
/*    */     {
/* 54 */       return DigestFactory.createSHA384();
/*    */     }
/* 56 */     if (paramAlgorithmIdentifier.getAlgorithm().equals((ASN1Primitive)NISTObjectIdentifiers.id_sha512))
/*    */     {
/* 58 */       return DigestFactory.createSHA512();
/*    */     }
/* 60 */     throw new IllegalArgumentException("unrecognised OID in digest algorithm identifier: " + paramAlgorithmIdentifier.getAlgorithm());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/mceliece/Utils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */