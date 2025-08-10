/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonValue;
/*     */ import com.github.signer4j.IHashAlgorithm;
/*     */ import com.github.signer4j.provider.ProviderInstaller;
/*     */ import java.util.Optional;
/*     */ import org.bouncycastle.cms.CMSSignedDataStreamGenerator;
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
/*     */ public enum HashAlgorithm
/*     */   implements IHashAlgorithm
/*     */ {
/*  41 */   MD2("MD2"),
/*  42 */   MD5("MD5")
/*     */   {
/*     */     public String getName() {
/*  45 */       return CMSSignedDataStreamGenerator.DIGEST_MD5;
/*     */     }
/*     */   },
/*  48 */   SHA_1("SHA-1")
/*     */   {
/*     */     public String getName() {
/*  51 */       return CMSSignedDataStreamGenerator.DIGEST_SHA1;
/*     */     }
/*     */   },
/*  54 */   SHA_224("SHA-224")
/*     */   {
/*     */     public String getName() {
/*  57 */       return CMSSignedDataStreamGenerator.DIGEST_SHA224;
/*     */     }
/*     */   },
/*  60 */   SHA_256("SHA-256")
/*     */   {
/*     */     public String getName() {
/*  63 */       return CMSSignedDataStreamGenerator.DIGEST_SHA256;
/*     */     }
/*     */   },
/*  66 */   SHA_384("SHA-384")
/*     */   {
/*     */     public String getName() {
/*  69 */       return CMSSignedDataStreamGenerator.DIGEST_SHA384;
/*     */     }
/*     */   },
/*  72 */   SHA_512("SHA-512")
/*     */   {
/*     */     public String getName() {
/*  75 */       return CMSSignedDataStreamGenerator.DIGEST_SHA512;
/*     */     }
/*     */   },
/*  78 */   SHA_512_224("SHA-512/224"),
/*  79 */   SHA_512_256("SHA-512/256"),
/*  80 */   ASN1MD5("ASN1MD5", false),
/*  81 */   ASN1MD2("ASN1MD2", false),
/*  82 */   ASN1SHA1("ASN1SHA1", false),
/*  83 */   ASN1SHA224("ASN1SHA224", false),
/*  84 */   ASN1SHA256("ASN1SHA256", false),
/*  85 */   ASN1SHA384("ASN1SHA384", false),
/*  86 */   ASN1SHA512("ASN1SHA512", false);
/*     */   private final String name; private boolean twoSteps; private static final HashAlgorithm[] VALUES;
/*     */   
/*  89 */   static { ProviderInstaller.BC.install();
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
/* 125 */     VALUES = values(); }
/*     */   @JsonCreator public static IHashAlgorithm fromString(String key) { return get(key).orElse(null); }
/*     */   HashAlgorithm(String name, boolean twoSteps) { this.name = name;
/* 128 */     this.twoSteps = twoSteps; } public static IHashAlgorithm getDefault() { return MD5; }
/*     */   @JsonValue
/*     */   public String getKey() { return this.name; }
/*     */   public String getName() { return getStandardName(); }
/* 132 */   public String getStandardName() { return this.name; } public static IHashAlgorithm getOrDefault(String name) { return getOfDefault(name, getDefault()); }
/*     */ 
/*     */   
/*     */   public static IHashAlgorithm getOfDefault(String name, IHashAlgorithm defaultIfNot) {
/* 136 */     return get(name).orElse(defaultIfNot);
/*     */   }
/*     */   
/*     */   public static boolean isSupported(String algorithm) {
/* 140 */     return get(algorithm).isPresent();
/*     */   }
/*     */   
/*     */   public static boolean isSupported(IHashAlgorithm algorithm) {
/* 144 */     return (algorithm != null && get(algorithm.getName()).isPresent());
/*     */   }
/*     */   
/*     */   public static Optional<IHashAlgorithm> get(String name) {
/* 148 */     for (HashAlgorithm a : VALUES) {
/* 149 */       if (a.name.equalsIgnoreCase(name))
/* 150 */         return Optional.of(a); 
/*     */     } 
/* 152 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsTwoSteps() {
/* 157 */     return this.twoSteps;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/HashAlgorithm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */