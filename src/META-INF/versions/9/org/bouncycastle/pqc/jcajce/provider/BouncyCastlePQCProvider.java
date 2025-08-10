/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider;
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
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
/*     */ import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
/*     */ import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;
/*     */ import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BouncyCastlePQCProvider
/*     */   extends Provider
/*     */   implements ConfigurableProvider
/*     */ {
/*  25 */   private static String info = "BouncyCastle Post-Quantum Security Provider v1.67";
/*     */   
/*  27 */   public static String PROVIDER_NAME = "BCPQC";
/*     */   
/*  29 */   public static final ProviderConfiguration CONFIGURATION = null;
/*     */ 
/*     */   
/*  32 */   private static final Map keyInfoConverters = new HashMap<>();
/*     */ 
/*     */   
/*     */   private static final String ALGORITHM_PACKAGE = "org.bouncycastle.pqc.jcajce.provider.";
/*     */ 
/*     */   
/*  38 */   private static final String[] ALGORITHMS = new String[] { "Rainbow", "McEliece", "SPHINCS", "LMS", "NH", "XMSS", "QTESLA" };
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
/*     */   public BouncyCastlePQCProvider() {
/*  50 */     super(PROVIDER_NAME, 1.67D, info);
/*     */     
/*  52 */     AccessController.doPrivileged((PrivilegedAction<?>)new Object(this));
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
/*  64 */     loadAlgorithms("org.bouncycastle.pqc.jcajce.provider.", ALGORITHMS);
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadAlgorithms(String paramString, String[] paramArrayOfString) {
/*  69 */     for (byte b = 0; b != paramArrayOfString.length; b++) {
/*     */       
/*  71 */       Class<AlgorithmProvider> clazz = loadClass(org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider.class, paramString + paramString + "$Mappings");
/*     */       
/*  73 */       if (clazz != null) {
/*     */         
/*     */         try {
/*     */           
/*  77 */           ((AlgorithmProvider)clazz.newInstance()).configure(this);
/*     */         }
/*  79 */         catch (Exception exception) {
/*     */           
/*  81 */           throw new InternalError("cannot create instance of " + paramString + paramArrayOfString[b] + "$Mappings : " + exception);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameter(String paramString, Object paramObject) {
/*  90 */     synchronized (CONFIGURATION) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAlgorithm(String paramString1, String paramString2) {
/*  98 */     return (containsKey(paramString1 + "." + paramString1) || containsKey("Alg.Alias." + paramString1 + "." + paramString2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAlgorithm(String paramString1, String paramString2) {
/* 103 */     if (containsKey(paramString1))
/*     */     {
/* 105 */       throw new IllegalStateException("duplicate provider key (" + paramString1 + ") found");
/*     */     }
/*     */     
/* 108 */     put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAlgorithm(String paramString1, ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString2) {
/* 113 */     if (!containsKey(paramString1 + "." + paramString1))
/*     */     {
/* 115 */       throw new IllegalStateException("primary key (" + paramString1 + "." + paramString2 + ") not found");
/*     */     }
/*     */     
/* 118 */     addAlgorithm(paramString1 + "." + paramString1, paramString2);
/* 119 */     addAlgorithm(paramString1 + ".OID." + paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addKeyInfoConverter(ASN1ObjectIdentifier paramASN1ObjectIdentifier, AsymmetricKeyInfoConverter paramAsymmetricKeyInfoConverter) {
/* 124 */     synchronized (keyInfoConverters) {
/*     */       
/* 126 */       keyInfoConverters.put(paramASN1ObjectIdentifier, paramAsymmetricKeyInfoConverter);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AsymmetricKeyInfoConverter getKeyInfoConverter(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 132 */     return (AsymmetricKeyInfoConverter)keyInfoConverters.get(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAttributes(String paramString, Map<String, String> paramMap) {
/* 137 */     for (String str1 : paramMap.keySet()) {
/*     */ 
/*     */       
/* 140 */       String str2 = paramString + " " + paramString;
/* 141 */       if (containsKey(str2))
/*     */       {
/* 143 */         throw new IllegalStateException("duplicate provider attribute key (" + str2 + ") found");
/*     */       }
/*     */       
/* 146 */       put(str2, paramMap.get(str1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static AsymmetricKeyInfoConverter getAsymmetricKeyInfoConverter(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 152 */     synchronized (keyInfoConverters) {
/*     */       
/* 154 */       return (AsymmetricKeyInfoConverter)keyInfoConverters.get(paramASN1ObjectIdentifier);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey getPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/* 161 */     AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(paramSubjectPublicKeyInfo.getAlgorithm().getAlgorithm());
/*     */     
/* 163 */     if (asymmetricKeyInfoConverter == null)
/*     */     {
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     return asymmetricKeyInfoConverter.generatePublic(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey getPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 174 */     AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
/*     */     
/* 176 */     if (asymmetricKeyInfoConverter == null)
/*     */     {
/* 178 */       return null;
/*     */     }
/*     */     
/* 181 */     return asymmetricKeyInfoConverter.generatePrivate(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Class loadClass(Class paramClass, String paramString) {
/*     */     try {
/* 188 */       ClassLoader classLoader = paramClass.getClassLoader();
/* 189 */       if (classLoader != null)
/*     */       {
/* 191 */         return classLoader.loadClass(paramString);
/*     */       }
/*     */ 
/*     */       
/* 195 */       return AccessController.<Class<?>>doPrivileged((PrivilegedAction<Class<?>>)new Object(paramString));
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
/*     */     }
/* 213 */     catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 218 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/BouncyCastlePQCProvider.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */