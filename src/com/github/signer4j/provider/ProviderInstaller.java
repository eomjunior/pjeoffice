/*     */ package com.github.signer4j.provider;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.security.AuthProvider;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.function.Supplier;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ProviderInstaller
/*     */ {
/*  50 */   JSR105("XMLDSig")
/*     */   {
/*     */     public Provider install(String providerName, Object config) {
/*  53 */       return setup(defaultName(), org.jcp.xml.dsig.internal.dom.XMLDSigRI::new);
/*     */     }
/*     */   },
/*     */   
/*  57 */   MSCAPI("SunMSCAPI")
/*     */   {
/*     */     public Provider install(String providerName, Object config) {
/*  60 */       return setup(defaultName(), () -> (Provider)Throwables.runtime(()));
/*     */     }
/*     */   },
/*     */   
/*  64 */   BC("BC")
/*     */   {
/*     */     public Provider install(String providerName, Object config) {
/*  67 */       return setup(defaultName(), () -> {
/*     */             System.setProperty("org.bouncycastle.asn1.allow_unsafe_integer", "true");
/*     */             
/*     */             return (Provider)new BouncyCastleProvider();
/*     */           });
/*     */     }
/*     */   },
/*  74 */   SIGNER4J("Signer4J")
/*     */   {
/*     */     public Provider install(String providerName, Object config) {
/*  77 */       return setup(defaultName(), Signer4JProvider::new);
/*     */     }
/*     */   },
/*     */   
/*  81 */   SUNPKCS11
/*     */   {
/*     */     public Provider install(String providerName, Object config) {
/*  84 */       Args.requireText(providerName, "provider name is empty");
/*  85 */       Args.requireNonNull(config, "config is null");
/*  86 */       String sunProviderName = SunPKCS11Creator.providerName(providerName);
/*  87 */       return setup(sunProviderName, () -> SunPKCS11Creator.create(config.toString()));
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */   
/*     */   ProviderInstaller(String name) {
/*  98 */     this.name = (String)Args.requireNonNull(name, "name is null");
/*     */   }
/*     */   
/*     */   public boolean is(String name) {
/* 102 */     return defaultName().equals(name);
/*     */   }
/*     */   
/*     */   public String defaultName() {
/* 106 */     return this.name;
/*     */   }
/*     */   
/*     */   public Provider install() {
/* 110 */     return install(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void uninstall(Provider provider) {
/* 116 */     if (provider == null) {
/*     */       return;
/*     */     }
/*     */     
/* 120 */     if (provider instanceof AuthProvider) {
/* 121 */       Throwables.quietly((AuthProvider)provider::logout);
/*     */     }
/*     */     
/* 124 */     Throwables.quietly(() -> Security.removeProvider(provider.getName()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Provider setup(String providerName, Supplier<Provider> supplier) {
/* 129 */     Provider provider = Security.getProvider(providerName);
/* 130 */     if (provider != null) {
/* 131 */       return provider;
/*     */     }
/*     */     
/* 134 */     provider = supplier.get();
/* 135 */     if (provider == null) {
/* 136 */       throw new RuntimeException("Unabled to create provider " + providerName);
/*     */     }
/*     */     
/* 139 */     if (provider instanceof AuthProvider) {
/* 140 */       Throwables.quietly((AuthProvider)provider::logout);
/*     */     }
/*     */     
/* 143 */     int code = Security.addProvider(provider);
/* 144 */     if (code < 0) {
/* 145 */       throw new RuntimeException("Unabled to install provider " + providerName);
/*     */     }
/* 147 */     return provider;
/*     */   }
/*     */   
/*     */   static {
/* 151 */     Security.removeProvider(MSCAPI.defaultName());
/*     */   }
/*     */   
/*     */   public abstract Provider install(String paramString, Object paramObject);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ProviderInstaller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */