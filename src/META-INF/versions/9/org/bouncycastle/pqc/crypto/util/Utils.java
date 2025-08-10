/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.crypto.digests.SHA256Digest;
/*     */ import org.bouncycastle.crypto.digests.SHA512Digest;
/*     */ import org.bouncycastle.crypto.digests.SHAKEDigest;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.asn1.SPHINCS256KeyParams;
/*     */ import org.bouncycastle.util.Integers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Utils
/*     */ {
/*  22 */   static final AlgorithmIdentifier AlgID_qTESLA_p_I = new AlgorithmIdentifier(PQCObjectIdentifiers.qTESLA_p_I);
/*  23 */   static final AlgorithmIdentifier AlgID_qTESLA_p_III = new AlgorithmIdentifier(PQCObjectIdentifiers.qTESLA_p_III);
/*     */   
/*  25 */   static final AlgorithmIdentifier SPHINCS_SHA3_256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha3_256);
/*  26 */   static final AlgorithmIdentifier SPHINCS_SHA512_256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512_256);
/*     */   
/*  28 */   static final AlgorithmIdentifier XMSS_SHA256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
/*  29 */   static final AlgorithmIdentifier XMSS_SHA512 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512);
/*  30 */   static final AlgorithmIdentifier XMSS_SHAKE128 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_shake128);
/*  31 */   static final AlgorithmIdentifier XMSS_SHAKE256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_shake256);
/*     */   
/*  33 */   static final Map categories = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  37 */     categories.put(PQCObjectIdentifiers.qTESLA_p_I, Integers.valueOf(5));
/*  38 */     categories.put(PQCObjectIdentifiers.qTESLA_p_III, Integers.valueOf(6));
/*     */   }
/*     */ 
/*     */   
/*     */   static int qTeslaLookupSecurityCategory(AlgorithmIdentifier paramAlgorithmIdentifier) {
/*  43 */     return ((Integer)categories.get(paramAlgorithmIdentifier.getAlgorithm())).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   static AlgorithmIdentifier qTeslaLookupAlgID(int paramInt) {
/*  48 */     switch (paramInt) {
/*     */       
/*     */       case 5:
/*  51 */         return AlgID_qTESLA_p_I;
/*     */       case 6:
/*  53 */         return AlgID_qTESLA_p_III;
/*     */     } 
/*  55 */     throw new IllegalArgumentException("unknown security category: " + paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static AlgorithmIdentifier sphincs256LookupTreeAlgID(String paramString) {
/*  61 */     if (paramString.equals("SHA3-256"))
/*     */     {
/*  63 */       return SPHINCS_SHA3_256;
/*     */     }
/*  65 */     if (paramString.equals("SHA-512/256"))
/*     */     {
/*  67 */       return SPHINCS_SHA512_256;
/*     */     }
/*     */ 
/*     */     
/*  71 */     throw new IllegalArgumentException("unknown tree digest: " + paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static AlgorithmIdentifier xmssLookupTreeAlgID(String paramString) {
/*  77 */     if (paramString.equals("SHA-256"))
/*     */     {
/*  79 */       return XMSS_SHA256;
/*     */     }
/*  81 */     if (paramString.equals("SHA-512"))
/*     */     {
/*  83 */       return XMSS_SHA512;
/*     */     }
/*  85 */     if (paramString.equals("SHAKE128"))
/*     */     {
/*  87 */       return XMSS_SHAKE128;
/*     */     }
/*  89 */     if (paramString.equals("SHAKE256"))
/*     */     {
/*  91 */       return XMSS_SHAKE256;
/*     */     }
/*     */ 
/*     */     
/*  95 */     throw new IllegalArgumentException("unknown tree digest: " + paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String sphincs256LookupTreeAlgName(SPHINCS256KeyParams paramSPHINCS256KeyParams) {
/* 101 */     AlgorithmIdentifier algorithmIdentifier = paramSPHINCS256KeyParams.getTreeDigest();
/*     */     
/* 103 */     if (algorithmIdentifier.getAlgorithm().equals((ASN1Primitive)SPHINCS_SHA3_256.getAlgorithm()))
/*     */     {
/* 105 */       return "SHA3-256";
/*     */     }
/* 107 */     if (algorithmIdentifier.getAlgorithm().equals((ASN1Primitive)SPHINCS_SHA512_256.getAlgorithm()))
/*     */     {
/* 109 */       return "SHA-512/256";
/*     */     }
/*     */ 
/*     */     
/* 113 */     throw new IllegalArgumentException("unknown tree digest: " + algorithmIdentifier.getAlgorithm());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Digest getDigest(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 119 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha256))
/*     */     {
/* 121 */       return (Digest)new SHA256Digest();
/*     */     }
/* 123 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha512))
/*     */     {
/* 125 */       return (Digest)new SHA512Digest();
/*     */     }
/* 127 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake128))
/*     */     {
/* 129 */       return (Digest)new SHAKEDigest(128);
/*     */     }
/* 131 */     if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake256))
/*     */     {
/* 133 */       return (Digest)new SHAKEDigest(256);
/*     */     }
/*     */     
/* 136 */     throw new IllegalArgumentException("unrecognized digest OID: " + paramASN1ObjectIdentifier);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/util/Utils.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */