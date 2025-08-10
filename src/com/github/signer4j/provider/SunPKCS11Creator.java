/*     */ package com.github.signer4j.provider;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AuthProvider;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SunPKCS11Creator
/*     */ {
/*  45 */   private static final Logger LOGGER = LoggerFactory.getLogger(SunPKCS11Creator.class);
/*     */ 
/*     */   
/*     */   private static final String SUN_PKCS11_PROVIDERNAME = "SunPKCS11";
/*     */   
/*     */   private static final String SUN_PKCS11_CLASS_NAME = "sun.security.pkcs11.SunPKCS11";
/*     */   
/*     */   private static Class<?> SUN_PKCS11_PROVIDER_CLASS;
/*     */ 
/*     */   
/*     */   static String providerName(String providerNameSuffix) {
/*  56 */     Args.requireText(providerNameSuffix, "suffix is empty");
/*  57 */     return "SunPKCS11-" + providerNameSuffix;
/*     */   }
/*     */   
/*     */   public static AuthProvider create(String configuration) throws UnavailableProviderException {
/*  61 */     Args.requireText(configuration, "configuration is empty");
/*     */     Method configure;
/*  63 */     if ((configure = isJavaGreaterOrEquals9()) != null)
/*  64 */       return createProviderJavaGreaterOrEquals9(configure, configuration); 
/*  65 */     return createProviderJavaLowerThan9(configuration);
/*     */   }
/*     */   
/*     */   private static Class<?> getProviderClass() throws ClassNotFoundException {
/*  69 */     if (SUN_PKCS11_PROVIDER_CLASS == null)
/*  70 */       SUN_PKCS11_PROVIDER_CLASS = Class.forName("sun.security.pkcs11.SunPKCS11"); 
/*  71 */     return SUN_PKCS11_PROVIDER_CLASS;
/*     */   }
/*     */   
/*     */   private static Method isJavaGreaterOrEquals9() throws UnavailableProviderException {
/*     */     try {
/*  76 */       return getProviderClass().getMethod("configure", new Class[] { String.class });
/*  77 */     } catch (NoSuchMethodException e) {
/*  78 */       return null;
/*  79 */     } catch (ClassNotFoundException e) {
/*  80 */       LOGGER.warn("JRE aparentemente corrompida porque não foi possível encontrar a class da dependência sunpkcs11.jar", e);
/*  81 */       throw new UnavailableProviderException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static AuthProvider createProviderJavaLowerThan9(String configuration) throws UnavailableProviderException {
/*  86 */     try (ByteArrayInputStream bais = new ByteArrayInputStream(configuration.getBytes())) {
/*  87 */       return getProviderClass().getConstructor(new Class[] { InputStream.class }).newInstance(new Object[] { bais });
/*  88 */     } catch (Exception e) {
/*  89 */       LOGGER.warn("Incapaz de instanciar PKCS11 (JDK < 9): SunPKCS11 config: " + configuration + "\nWARNING -> " + 
/*  90 */           Throwables.rootMessage(e));
/*  91 */       throw new UnavailableProviderException("Unable to instantiate PKCS11 provider", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static AuthProvider createProviderJavaGreaterOrEquals9(Method configureMethod, String configuration) throws UnavailableProviderException {
/*  96 */     String[] configAttempts = { configuration };
/*     */ 
/*     */ 
/*     */     
/* 100 */     Throwable fail = null;
/* 101 */     for (String config : configAttempts) {
/*     */       try {
/* 103 */         Provider provider = Security.getProvider("SunPKCS11");
/* 104 */         if (provider == null) {
/* 105 */           throw new UnavailableProviderException("JRE aparentemente corrompida porque não foi publicada instância SunPKCS11 em java.security.Security. O sistema exige a dependência sunpkcs11.jar");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 112 */         return (AuthProvider)configureMethod.invoke(provider, new Object[] { "--" + config });
/* 113 */       } catch (Throwable e) {
/* 114 */         if (fail == null) {
/* 115 */           fail = e;
/*     */         } else {
/* 117 */           fail.addSuppressed(e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 121 */     throw new UnavailableProviderException("Unable to instantiate PKCS11 provider", fail);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/SunPKCS11Creator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */