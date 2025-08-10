/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IChoice;
/*     */ import com.github.signer4j.IKeyStoreAccess;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ class Choice
/*     */   extends CertificateAware
/*     */   implements IChoice
/*     */ {
/*  40 */   static final IChoice CANCEL = new Choice(); private final boolean canceled; private final PrivateKey privateKey;
/*     */   
/*     */   static IChoice from(IKeyStoreAccess keyStore, String choosenAlias) throws Signer4JException {
/*  43 */     return from(keyStore
/*  44 */         .getPrivateKey(choosenAlias), keyStore
/*  45 */         .getCertificate(choosenAlias), keyStore
/*  46 */         .getCertificateChain(choosenAlias), keyStore
/*  47 */         .getProvider());
/*     */   }
/*     */   private final Certificate certificate; private final List<Certificate> chain; private final String provider;
/*     */   
/*     */   private static IChoice from(PrivateKey privateKey, Certificate certificate, List<Certificate> chain, String provider) {
/*  52 */     return new Choice(false, privateKey, certificate, chain, provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Choice() {
/*  62 */     this(true, null, null, Collections.emptyList(), null);
/*     */   }
/*     */   
/*     */   private Choice(boolean canceled, PrivateKey privateKey, Certificate certificate, List<Certificate> chain, String provider) {
/*  66 */     this.canceled = canceled;
/*  67 */     this.privateKey = privateKey;
/*  68 */     this.certificate = certificate;
/*  69 */     this.chain = chain;
/*  70 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCanceled() {
/*  75 */     return this.canceled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final PrivateKey getPrivateKey() {
/*  80 */     return this.privateKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Certificate getCertificate() {
/*  85 */     return this.certificate;
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<Certificate> getCertificateChain() {
/*  90 */     return this.chain;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int chainSize() {
/*  95 */     return this.chain.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getProvider() {
/* 100 */     return this.provider;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Choice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */