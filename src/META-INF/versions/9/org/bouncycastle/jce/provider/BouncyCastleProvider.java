/*     */ package META-INF.versions.9.org.bouncycastle.jce.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.isara.IsaraObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
/*     */ import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
/*     */ import org.bouncycastle.jcajce.provider.symmetric.util.ClassUtil;
/*     */ import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;
/*     */ import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProviderConfiguration;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.jcajce.provider.lms.LMSKeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceCCA2KeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceKeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.newhope.NHKeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.qtesla.QTESLAKeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.rainbow.RainbowKeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.sphincs.Sphincs256KeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.xmss.XMSSKeyFactorySpi;
/*     */ import org.bouncycastle.pqc.jcajce.provider.xmss.XMSSMTKeyFactorySpi;
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
/*     */ 
/*     */ 
/*     */ public final class BouncyCastleProvider
/*     */   extends Provider
/*     */   implements ConfigurableProvider
/*     */ {
/*  61 */   private static String info = "BouncyCastle Security Provider v1.68";
/*     */   
/*     */   public static final String PROVIDER_NAME = "BC";
/*     */   
/*  65 */   public static final ProviderConfiguration CONFIGURATION = (ProviderConfiguration)new BouncyCastleProviderConfiguration();
/*     */   
/*  67 */   private static final Map keyInfoConverters = new HashMap<>();
/*     */   
/*  69 */   private static final Class revChkClass = ClassUtil.loadClass(org.bouncycastle.jce.provider.BouncyCastleProvider.class, "java.security.cert.PKIXRevocationChecker");
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SYMMETRIC_PACKAGE = "org.bouncycastle.jcajce.provider.symmetric.";
/*     */ 
/*     */   
/*  76 */   private static final String[] SYMMETRIC_GENERIC = new String[] { "PBEPBKDF1", "PBEPBKDF2", "PBEPKCS12", "TLSKDF", "SCRYPT" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final String[] SYMMETRIC_MACS = new String[] { "SipHash", "SipHash128", "Poly1305" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private static final String[] SYMMETRIC_CIPHERS = new String[] { "AES", "ARC4", "ARIA", "Blowfish", "Camellia", "CAST5", "CAST6", "ChaCha", "DES", "DESede", "GOST28147", "Grainv1", "Grain128", "HC128", "HC256", "IDEA", "Noekeon", "RC2", "RC5", "RC6", "Rijndael", "Salsa20", "SEED", "Serpent", "Shacal2", "Skipjack", "SM4", "TEA", "Twofish", "Threefish", "VMPC", "VMPCKSA3", "XTEA", "XSalsa20", "OpenSSLPBKDF", "DSTU7624", "GOST3412_2015", "Zuc" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ASYMMETRIC_PACKAGE = "org.bouncycastle.jcajce.provider.asymmetric.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private static final String[] ASYMMETRIC_GENERIC = new String[] { "X509", "IES", "COMPOSITE" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static final String[] ASYMMETRIC_CIPHERS = new String[] { "DSA", "DH", "EC", "RSA", "GOST", "ECGOST", "ElGamal", "DSTU4145", "GM", "EdEC" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DIGEST_PACKAGE = "org.bouncycastle.jcajce.provider.digest.";
/*     */ 
/*     */ 
/*     */   
/* 115 */   private static final String[] DIGESTS = new String[] { "GOST3411", "Keccak", "MD2", "MD4", "MD5", "SHA1", "RIPEMD128", "RIPEMD160", "RIPEMD256", "RIPEMD320", "SHA224", "SHA256", "SHA384", "SHA512", "SHA3", "Skein", "SM3", "Tiger", "Whirlpool", "Blake2b", "Blake2s", "DSTU7564", "Haraka" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String KEYSTORE_PACKAGE = "org.bouncycastle.jcajce.provider.keystore.";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   private static final String[] KEYSTORES = new String[] { "BC", "BCFKS", "PKCS12" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SECURE_RANDOM_PACKAGE = "org.bouncycastle.jcajce.provider.drbg.";
/*     */ 
/*     */ 
/*     */   
/* 135 */   private static final String[] SECURE_RANDOMS = new String[] { "DRBG" };
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
/*     */   public BouncyCastleProvider() {
/* 147 */     super("BC", 1.68D, info);
/*     */     
/* 149 */     AccessController.doPrivileged((PrivilegedAction<?>)new Object(this));
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
/*     */   private void setup() {
/* 161 */     loadAlgorithms("org.bouncycastle.jcajce.provider.digest.", DIGESTS);
/*     */     
/* 163 */     loadAlgorithms("org.bouncycastle.jcajce.provider.symmetric.", SYMMETRIC_GENERIC);
/*     */     
/* 165 */     loadAlgorithms("org.bouncycastle.jcajce.provider.symmetric.", SYMMETRIC_MACS);
/*     */     
/* 167 */     loadAlgorithms("org.bouncycastle.jcajce.provider.symmetric.", SYMMETRIC_CIPHERS);
/*     */     
/* 169 */     loadAlgorithms("org.bouncycastle.jcajce.provider.asymmetric.", ASYMMETRIC_GENERIC);
/*     */     
/* 171 */     loadAlgorithms("org.bouncycastle.jcajce.provider.asymmetric.", ASYMMETRIC_CIPHERS);
/*     */     
/* 173 */     loadAlgorithms("org.bouncycastle.jcajce.provider.keystore.", KEYSTORES);
/*     */     
/* 175 */     loadAlgorithms("org.bouncycastle.jcajce.provider.drbg.", SECURE_RANDOMS);
/*     */     
/* 177 */     loadPQCKeys();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     put("X509Store.CERTIFICATE/COLLECTION", "org.bouncycastle.jce.provider.X509StoreCertCollection");
/* 183 */     put("X509Store.ATTRIBUTECERTIFICATE/COLLECTION", "org.bouncycastle.jce.provider.X509StoreAttrCertCollection");
/* 184 */     put("X509Store.CRL/COLLECTION", "org.bouncycastle.jce.provider.X509StoreCRLCollection");
/* 185 */     put("X509Store.CERTIFICATEPAIR/COLLECTION", "org.bouncycastle.jce.provider.X509StoreCertPairCollection");
/*     */     
/* 187 */     put("X509Store.CERTIFICATE/LDAP", "org.bouncycastle.jce.provider.X509StoreLDAPCerts");
/* 188 */     put("X509Store.CRL/LDAP", "org.bouncycastle.jce.provider.X509StoreLDAPCRLs");
/* 189 */     put("X509Store.ATTRIBUTECERTIFICATE/LDAP", "org.bouncycastle.jce.provider.X509StoreLDAPAttrCerts");
/* 190 */     put("X509Store.CERTIFICATEPAIR/LDAP", "org.bouncycastle.jce.provider.X509StoreLDAPCertPairs");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     put("X509StreamParser.CERTIFICATE", "org.bouncycastle.jce.provider.X509CertParser");
/* 196 */     put("X509StreamParser.ATTRIBUTECERTIFICATE", "org.bouncycastle.jce.provider.X509AttrCertParser");
/* 197 */     put("X509StreamParser.CRL", "org.bouncycastle.jce.provider.X509CRLParser");
/* 198 */     put("X509StreamParser.CERTIFICATEPAIR", "org.bouncycastle.jce.provider.X509CertPairParser");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     put("Cipher.BROKENPBEWITHMD5ANDDES", "org.bouncycastle.jce.provider.BrokenJCEBlockCipher$BrokePBEWithMD5AndDES");
/*     */     
/* 205 */     put("Cipher.BROKENPBEWITHSHA1ANDDES", "org.bouncycastle.jce.provider.BrokenJCEBlockCipher$BrokePBEWithSHA1AndDES");
/*     */ 
/*     */     
/* 208 */     put("Cipher.OLDPBEWITHSHAANDTWOFISH-CBC", "org.bouncycastle.jce.provider.BrokenJCEBlockCipher$OldPBEWithSHAAndTwofish");
/*     */ 
/*     */     
/* 211 */     if (revChkClass != null) {
/*     */       
/* 213 */       put("CertPathValidator.RFC3281", "org.bouncycastle.jce.provider.PKIXAttrCertPathValidatorSpi");
/* 214 */       put("CertPathBuilder.RFC3281", "org.bouncycastle.jce.provider.PKIXAttrCertPathBuilderSpi");
/* 215 */       put("CertPathValidator.RFC3280", "org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi_8");
/* 216 */       put("CertPathBuilder.RFC3280", "org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi_8");
/* 217 */       put("CertPathValidator.PKIX", "org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi_8");
/* 218 */       put("CertPathBuilder.PKIX", "org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi_8");
/*     */     }
/*     */     else {
/*     */       
/* 222 */       put("CertPathValidator.RFC3281", "org.bouncycastle.jce.provider.PKIXAttrCertPathValidatorSpi");
/* 223 */       put("CertPathBuilder.RFC3281", "org.bouncycastle.jce.provider.PKIXAttrCertPathBuilderSpi");
/* 224 */       put("CertPathValidator.RFC3280", "org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi");
/* 225 */       put("CertPathBuilder.RFC3280", "org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi");
/* 226 */       put("CertPathValidator.PKIX", "org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi");
/* 227 */       put("CertPathBuilder.PKIX", "org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi");
/*     */     } 
/* 229 */     put("CertStore.Collection", "org.bouncycastle.jce.provider.CertStoreCollectionSpi");
/* 230 */     put("CertStore.LDAP", "org.bouncycastle.jce.provider.X509LDAPCertStoreSpi");
/* 231 */     put("CertStore.Multi", "org.bouncycastle.jce.provider.MultiCertStoreSpi");
/* 232 */     put("Alg.Alias.CertStore.X509LDAP", "LDAP");
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadAlgorithms(String paramString, String[] paramArrayOfString) {
/* 237 */     for (byte b = 0; b != paramArrayOfString.length; b++) {
/*     */       
/* 239 */       Class<AlgorithmProvider> clazz = ClassUtil.loadClass(org.bouncycastle.jce.provider.BouncyCastleProvider.class, paramString + paramString + "$Mappings");
/*     */       
/* 241 */       if (clazz != null) {
/*     */         
/*     */         try {
/*     */           
/* 245 */           ((AlgorithmProvider)clazz.newInstance()).configure(this);
/*     */         }
/* 247 */         catch (Exception exception) {
/*     */           
/* 249 */           throw new InternalError("cannot create instance of " + paramString + paramArrayOfString[b] + "$Mappings : " + exception);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadPQCKeys() {
/* 258 */     addKeyInfoConverter(PQCObjectIdentifiers.sphincs256, (AsymmetricKeyInfoConverter)new Sphincs256KeyFactorySpi());
/* 259 */     addKeyInfoConverter(PQCObjectIdentifiers.newHope, (AsymmetricKeyInfoConverter)new NHKeyFactorySpi());
/* 260 */     addKeyInfoConverter(PQCObjectIdentifiers.xmss, (AsymmetricKeyInfoConverter)new XMSSKeyFactorySpi());
/* 261 */     addKeyInfoConverter(IsaraObjectIdentifiers.id_alg_xmss, (AsymmetricKeyInfoConverter)new XMSSKeyFactorySpi());
/* 262 */     addKeyInfoConverter(PQCObjectIdentifiers.xmss_mt, (AsymmetricKeyInfoConverter)new XMSSMTKeyFactorySpi());
/* 263 */     addKeyInfoConverter(IsaraObjectIdentifiers.id_alg_xmssmt, (AsymmetricKeyInfoConverter)new XMSSMTKeyFactorySpi());
/* 264 */     addKeyInfoConverter(PQCObjectIdentifiers.mcEliece, (AsymmetricKeyInfoConverter)new McElieceKeyFactorySpi());
/* 265 */     addKeyInfoConverter(PQCObjectIdentifiers.mcElieceCca2, (AsymmetricKeyInfoConverter)new McElieceCCA2KeyFactorySpi());
/* 266 */     addKeyInfoConverter(PQCObjectIdentifiers.rainbow, (AsymmetricKeyInfoConverter)new RainbowKeyFactorySpi());
/* 267 */     addKeyInfoConverter(PQCObjectIdentifiers.qTESLA_p_I, (AsymmetricKeyInfoConverter)new QTESLAKeyFactorySpi());
/* 268 */     addKeyInfoConverter(PQCObjectIdentifiers.qTESLA_p_III, (AsymmetricKeyInfoConverter)new QTESLAKeyFactorySpi());
/* 269 */     addKeyInfoConverter(PKCSObjectIdentifiers.id_alg_hss_lms_hashsig, (AsymmetricKeyInfoConverter)new LMSKeyFactorySpi());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParameter(String paramString, Object paramObject) {
/* 274 */     synchronized (CONFIGURATION) {
/*     */       
/* 276 */       ((BouncyCastleProviderConfiguration)CONFIGURATION).setParameter(paramString, paramObject);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAlgorithm(String paramString1, String paramString2) {
/* 282 */     return (containsKey(paramString1 + "." + paramString1) || containsKey("Alg.Alias." + paramString1 + "." + paramString2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAlgorithm(String paramString1, String paramString2) {
/* 287 */     if (containsKey(paramString1))
/*     */     {
/* 289 */       throw new IllegalStateException("duplicate provider key (" + paramString1 + ") found");
/*     */     }
/*     */     
/* 292 */     put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAlgorithm(String paramString1, ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString2) {
/* 297 */     addAlgorithm(paramString1 + "." + paramString1, paramString2);
/* 298 */     addAlgorithm(paramString1 + ".OID." + paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addKeyInfoConverter(ASN1ObjectIdentifier paramASN1ObjectIdentifier, AsymmetricKeyInfoConverter paramAsymmetricKeyInfoConverter) {
/* 303 */     synchronized (keyInfoConverters) {
/*     */       
/* 305 */       keyInfoConverters.put(paramASN1ObjectIdentifier, paramAsymmetricKeyInfoConverter);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AsymmetricKeyInfoConverter getKeyInfoConverter(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 311 */     return (AsymmetricKeyInfoConverter)keyInfoConverters.get(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAttributes(String paramString, Map<String, String> paramMap) {
/* 316 */     for (String str1 : paramMap.keySet()) {
/*     */ 
/*     */       
/* 319 */       String str2 = paramString + " " + paramString;
/* 320 */       if (containsKey(str2))
/*     */       {
/* 322 */         throw new IllegalStateException("duplicate provider attribute key (" + str2 + ") found");
/*     */       }
/*     */       
/* 325 */       put(str2, paramMap.get(str1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static AsymmetricKeyInfoConverter getAsymmetricKeyInfoConverter(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 331 */     synchronized (keyInfoConverters) {
/*     */       
/* 333 */       return (AsymmetricKeyInfoConverter)keyInfoConverters.get(paramASN1ObjectIdentifier);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey getPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/* 340 */     AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(paramSubjectPublicKeyInfo.getAlgorithm().getAlgorithm());
/*     */     
/* 342 */     if (asymmetricKeyInfoConverter == null)
/*     */     {
/* 344 */       return null;
/*     */     }
/*     */     
/* 347 */     return asymmetricKeyInfoConverter.generatePublic(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey getPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 353 */     AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
/*     */     
/* 355 */     if (asymmetricKeyInfoConverter == null)
/*     */     {
/* 357 */       return null;
/*     */     }
/*     */     
/* 360 */     return asymmetricKeyInfoConverter.generatePrivate(paramPrivateKeyInfo);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jce/provider/BouncyCastleProvider.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */