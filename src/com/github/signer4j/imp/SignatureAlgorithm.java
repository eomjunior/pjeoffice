/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonValue;
/*     */ import com.github.signer4j.IAlgorithm;
/*     */ import com.github.signer4j.IHashAlgorithm;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.provider.ANYwithRSASignature;
/*     */ import com.github.signer4j.provider.ProviderInstaller;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.security.Signature;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.Optional;
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
/*     */ public enum SignatureAlgorithm
/*     */   implements ISignatureAlgorithm
/*     */ {
/*     */   private static final SignatureAlgorithm[] VALUES;
/*     */   private String name;
/*     */   private HashAlgorithm hash;
/*  45 */   MD2withRSA("MD2WITHRSA", HashAlgorithm.MD2),
/*  46 */   MD5withRSA("MD5WITHRSA", HashAlgorithm.MD5),
/*  47 */   SHA1withDSA("SHA1withDSA", HashAlgorithm.SHA_1),
/*  48 */   SHA1withECDSA("SHA1withECDSA", HashAlgorithm.SHA_1),
/*  49 */   SHA1withPLAIN_ECDSA("SHA1WITHPLAIN-ECDSA", HashAlgorithm.SHA_1),
/*  50 */   SHA1withRSA("SHA1WITHRSA", HashAlgorithm.SHA_1),
/*  51 */   SHA224withCVC_ECDSA("SHA224WITHCVC-ECDSA", HashAlgorithm.SHA_224),
/*  52 */   SHA224withDSA("SHA224WITHDSA", HashAlgorithm.SHA_224),
/*  53 */   SHA224withECDSA("SHA224WITHECDSA", HashAlgorithm.SHA_224),
/*  54 */   SHA224withPLAIN_ECDSA("SHA224WITHPLAIN-ECDSA", HashAlgorithm.SHA_224),
/*  55 */   SHA224withRSA("SHA224WITHRSA", HashAlgorithm.SHA_224),
/*  56 */   SHA256withCVC_ECDSA("SHA256WITHCVC-ECDSA", HashAlgorithm.SHA_256),
/*  57 */   SHA256withDSA("SHA256WITHDSA", HashAlgorithm.SHA_256),
/*  58 */   SHA256withECDSA("SHA256WITHECDSA", HashAlgorithm.SHA_256),
/*  59 */   SHA256withPLAIN_ECDSA("SHA256WITHPLAIN-ECDSA", HashAlgorithm.SHA_256),
/*  60 */   SHA256withRSA("SHA256WITHRSA", HashAlgorithm.SHA_256),
/*  61 */   SHA384withCVC_ECDSA("SHA384WITHCVC-ECDSA", HashAlgorithm.SHA_384),
/*  62 */   SHA384withDSA("SHA384WITHDSA", HashAlgorithm.SHA_384),
/*  63 */   SHA384withECDSA("SHA384WITHECDSA", HashAlgorithm.SHA_384),
/*  64 */   SHA384withPLAIN_ECDSA("SHA384WITHPLAIN-ECDSA", HashAlgorithm.SHA_384),
/*  65 */   SHA384withRSA("SHA384WITHRSA", HashAlgorithm.SHA_384),
/*  66 */   SHA512withCVC_ECDSA("SHA512WITHCVC-ECDSA", HashAlgorithm.SHA_512),
/*  67 */   SHA512withDSA("SHA512WITHDSA", HashAlgorithm.SHA_512),
/*  68 */   SHA512withECDSA("SHA512WITHECDSA", HashAlgorithm.SHA_512),
/*  69 */   SHA512withPLAIN_ECDSA("SHA512WITHPLAIN-ECDSA", HashAlgorithm.SHA_512),
/*  70 */   SHA512withRSA("SHA512WITHRSA", HashAlgorithm.SHA_512),
/*     */ 
/*     */   
/*  73 */   ASN1MD2withRSA("ASN1MD2withRSA", HashAlgorithm.ASN1MD2),
/*  74 */   ASN1MD5withRSA("ASN1MD5withRSA", HashAlgorithm.ASN1MD5),
/*  75 */   ASN1SHA1withRSA("ASN1SHA1withRSA", HashAlgorithm.ASN1SHA1),
/*  76 */   ASN1SHA224withRSA("ASN1SHA224withRSA", HashAlgorithm.ASN1SHA224),
/*  77 */   ASN1SHA256withRSA("ASN1SHA256withRSA", HashAlgorithm.ASN1SHA256),
/*  78 */   ASN1SHA384withRSA("ASN1SHA384withRSA", HashAlgorithm.ASN1SHA384),
/*  79 */   ASN1SHA512withRSA("ASN1SHA512withRSA", HashAlgorithm.ASN1SHA512);
/*     */   
/*     */   static {
/*  82 */     ProviderInstaller.BC.install();
/*  83 */     ProviderInstaller.SIGNER4J.install();
/*     */ 
/*     */ 
/*     */     
/*  87 */     VALUES = values();
/*     */   }
/*     */   @JsonCreator
/*     */   public static SignatureAlgorithm fromString(String key) {
/*  91 */     return from(key).orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SignatureAlgorithm(String name, HashAlgorithm hash) {
/*  99 */     this.name = name;
/* 100 */     this.hash = hash;
/*     */   }
/*     */   
/*     */   @JsonValue
/*     */   public String getKey() {
/* 105 */     return name();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 110 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IHashAlgorithm getHashAlgorithm() {
/* 115 */     return this.hash;
/*     */   }
/*     */   
/*     */   public static SignatureAlgorithm getDefault() {
/* 119 */     return SHA1withRSA;
/*     */   }
/*     */   
/*     */   public static IAlgorithm getOrDefault(String name) {
/* 123 */     return (IAlgorithm)getOfDefault(name, getDefault());
/*     */   }
/*     */   
/*     */   public static SignatureAlgorithm getOfDefault(String name, SignatureAlgorithm defaultIfNot) {
/* 127 */     return from(name).orElse(defaultIfNot);
/*     */   }
/*     */   
/*     */   public static boolean isSupported(String algorithm) {
/* 131 */     return from(algorithm).isPresent();
/*     */   }
/*     */   
/*     */   public static boolean isSupported(IAlgorithm algorithm) {
/* 135 */     return (algorithm != null && from(algorithm.getName()).isPresent());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsTwoSteps() {
/* 140 */     return (this.hash.supportsTwoSteps() && getKey().endsWith("RSA"));
/*     */   }
/*     */   
/*     */   public static Optional<SignatureAlgorithm> from(String name) {
/* 144 */     for (SignatureAlgorithm a : VALUES) {
/* 145 */       if (a.name.equalsIgnoreCase(name))
/* 146 */         return Optional.of(a); 
/*     */     } 
/* 148 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Signature toSignature(String providerName) throws Exception {
/* 153 */     Args.requireNonNull(providerName, "providerName is null");
/* 154 */     return Signature.getInstance(getName(), providerName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Signature toSignature() throws Exception {
/* 159 */     if (!supportsTwoSteps()) {
/* 160 */       return Signature.getInstance(getName(), ProviderInstaller.SIGNER4J.defaultName());
/*     */     }
/* 162 */     Signature signature = Signature.getInstance("TWOSTEPSwithRSA", ProviderInstaller.SIGNER4J.defaultName());
/* 163 */     ANYwithRSASignature.HashName hashName = this.hash::getStandardName;
/* 164 */     signature.setParameter((AlgorithmParameterSpec)hashName);
/* 165 */     return signature;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SignatureAlgorithm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */