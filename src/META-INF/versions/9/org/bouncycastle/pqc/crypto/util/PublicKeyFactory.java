/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.isara.IsaraObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PublicKeyFactory
/*     */ {
/*  40 */   private static Map converters = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  44 */     converters.put(PQCObjectIdentifiers.qTESLA_p_I, new QTeslaConverter(null));
/*  45 */     converters.put(PQCObjectIdentifiers.qTESLA_p_III, new QTeslaConverter(null));
/*  46 */     converters.put(PQCObjectIdentifiers.sphincs256, new SPHINCSConverter(null));
/*  47 */     converters.put(PQCObjectIdentifiers.newHope, new NHConverter(null));
/*  48 */     converters.put(PQCObjectIdentifiers.xmss, new XMSSConverter(null));
/*  49 */     converters.put(PQCObjectIdentifiers.xmss_mt, new XMSSMTConverter(null));
/*  50 */     converters.put(IsaraObjectIdentifiers.id_alg_xmss, new XMSSConverter(null));
/*  51 */     converters.put(IsaraObjectIdentifiers.id_alg_xmssmt, new XMSSMTConverter(null));
/*  52 */     converters.put(PKCSObjectIdentifiers.id_alg_hss_lms_hashsig, new LMSConverter(null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsymmetricKeyParameter createKey(byte[] paramArrayOfbyte) throws IOException {
/*  65 */     return createKey(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(paramArrayOfbyte)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsymmetricKeyParameter createKey(InputStream paramInputStream) throws IOException {
/*  78 */     return createKey(SubjectPublicKeyInfo.getInstance((new ASN1InputStream(paramInputStream)).readObject()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsymmetricKeyParameter createKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  91 */     return createKey(paramSubjectPublicKeyInfo, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsymmetricKeyParameter createKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo, Object paramObject) throws IOException {
/* 105 */     AlgorithmIdentifier algorithmIdentifier = paramSubjectPublicKeyInfo.getAlgorithm();
/* 106 */     SubjectPublicKeyInfoConverter subjectPublicKeyInfoConverter = (SubjectPublicKeyInfoConverter)converters.get(algorithmIdentifier.getAlgorithm());
/*     */     
/* 108 */     if (subjectPublicKeyInfoConverter != null)
/*     */     {
/* 110 */       return subjectPublicKeyInfoConverter.getPublicKeyParameters(paramSubjectPublicKeyInfo, paramObject);
/*     */     }
/*     */ 
/*     */     
/* 114 */     throw new IOException("algorithm identifier in public key not recognised: " + algorithmIdentifier.getAlgorithm());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/util/PublicKeyFactory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */