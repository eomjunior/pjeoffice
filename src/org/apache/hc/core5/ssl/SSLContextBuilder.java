/*     */ package org.apache.hc.core5.ssl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Security;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SSLContextBuilder
/*     */ {
/*     */   static final String TLS = "TLS";
/*     */   private String protocol;
/*     */   private final Set<KeyManager> keyManagers;
/*  88 */   private String keyManagerFactoryAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
/*  89 */   private String keyStoreType = KeyStore.getDefaultType();
/*     */   private final Set<TrustManager> trustManagers;
/*  91 */   private String trustManagerFactoryAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
/*     */   
/*     */   private SecureRandom secureRandom;
/*     */   
/*     */   private Provider provider;
/*     */   
/*     */   private Provider tsProvider;
/*     */   
/*     */   private Provider ksProvider;
/* 100 */   private static final KeyManager[] EMPTY_KEY_MANAGER_ARRAY = new KeyManager[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   private static final TrustManager[] EMPTY_TRUST_MANAGER_ARRAY = new TrustManager[0];
/*     */ 
/*     */ 
/*     */   
/*     */   public static SSLContextBuilder create() {
/* 110 */     return new SSLContextBuilder();
/*     */   }
/*     */   
/*     */   public SSLContextBuilder() {
/* 114 */     this.keyManagers = new LinkedHashSet<>();
/* 115 */     this.trustManagers = new LinkedHashSet<>();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setProtocol(String protocol) {
/* 133 */     this.protocol = protocol;
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContextBuilder setProvider(Provider provider) {
/* 138 */     this.provider = provider;
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContextBuilder setProvider(String name) {
/* 143 */     this.provider = Security.getProvider(name);
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setTrustStoreProvider(Provider provider) {
/* 154 */     this.tsProvider = provider;
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setTrustStoreProvider(String name) throws NoSuchProviderException {
/* 165 */     this.tsProvider = requireNonNullProvider(name);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setKeyStoreProvider(Provider provider) {
/* 176 */     this.ksProvider = provider;
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setKeyStoreProvider(String name) throws NoSuchProviderException {
/* 187 */     this.ksProvider = requireNonNullProvider(name);
/* 188 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setKeyStoreType(String keyStoreType) {
/* 207 */     this.keyStoreType = keyStoreType;
/* 208 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setKeyManagerFactoryAlgorithm(String keyManagerFactoryAlgorithm) {
/* 227 */     this.keyManagerFactoryAlgorithm = keyManagerFactoryAlgorithm;
/* 228 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder setTrustManagerFactoryAlgorithm(String trustManagerFactoryAlgorithm) {
/* 247 */     this.trustManagerFactoryAlgorithm = trustManagerFactoryAlgorithm;
/* 248 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
/* 252 */     this.secureRandom = secureRandom;
/* 253 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(KeyStore trustStore, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
/* 261 */     String alg = (this.trustManagerFactoryAlgorithm == null) ? TrustManagerFactory.getDefaultAlgorithm() : this.trustManagerFactoryAlgorithm;
/*     */ 
/*     */     
/* 264 */     TrustManagerFactory tmFactory = (this.tsProvider == null) ? TrustManagerFactory.getInstance(alg) : TrustManagerFactory.getInstance(alg, this.tsProvider);
/*     */     
/* 266 */     tmFactory.init(trustStore);
/* 267 */     TrustManager[] tms = tmFactory.getTrustManagers();
/* 268 */     if (tms != null) {
/* 269 */       if (trustStrategy != null) {
/* 270 */         for (int i = 0; i < tms.length; i++) {
/* 271 */           TrustManager tm = tms[i];
/* 272 */           if (tm instanceof X509TrustManager) {
/* 273 */             tms[i] = new TrustManagerDelegate((X509TrustManager)tm, trustStrategy);
/*     */           }
/*     */         } 
/*     */       }
/* 277 */       Collections.addAll(this.trustManagers, tms);
/*     */     } 
/* 279 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(Path file) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 287 */     return loadTrustMaterial(file, (char[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(Path file, char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 296 */     return loadTrustMaterial(file, storePassword, null, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(Path file, char[] storePassword, TrustStrategy trustStrategy, OpenOption... openOptions) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 307 */     Args.notNull(file, "Truststore file");
/* 308 */     return loadTrustMaterial(loadKeyStore(file, storePassword, openOptions), trustStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
/* 313 */     return loadTrustMaterial((KeyStore)null, trustStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(File file, char[] storePassword, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 320 */     Args.notNull(file, "Truststore file");
/* 321 */     return loadTrustMaterial(file.toPath(), storePassword, trustStrategy, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(File file, char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 327 */     return loadTrustMaterial(file, storePassword, (TrustStrategy)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(File file) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 332 */     return loadTrustMaterial(file, (char[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(URL url, char[] storePassword, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 339 */     Args.notNull(url, "Truststore URL");
/* 340 */     return loadTrustMaterial(loadKeyStore(url, storePassword), trustStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(URL url, char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 346 */     return loadTrustMaterial(url, storePassword, (TrustStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(KeyStore keyStore, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
/* 356 */     String alg = (this.keyManagerFactoryAlgorithm == null) ? KeyManagerFactory.getDefaultAlgorithm() : this.keyManagerFactoryAlgorithm;
/*     */ 
/*     */     
/* 359 */     KeyManagerFactory kmFactory = (this.ksProvider == null) ? KeyManagerFactory.getInstance(alg) : KeyManagerFactory.getInstance(alg, this.ksProvider);
/*     */     
/* 361 */     kmFactory.init(keyStore, keyPassword);
/* 362 */     KeyManager[] kms = kmFactory.getKeyManagers();
/* 363 */     if (kms != null) {
/* 364 */       if (aliasStrategy != null) {
/* 365 */         for (int i = 0; i < kms.length; i++) {
/* 366 */           KeyManager km = kms[i];
/* 367 */           if (km instanceof X509ExtendedKeyManager) {
/* 368 */             kms[i] = new KeyManagerDelegate((X509ExtendedKeyManager)km, aliasStrategy);
/*     */           }
/*     */         } 
/*     */       }
/* 372 */       Collections.addAll(this.keyManagers, kms);
/*     */     } 
/* 374 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(Path file, char[] storePassword, char[] keyPassword, OpenOption... openOptions) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 385 */     return loadKeyMaterial(file, storePassword, keyPassword, null, openOptions);
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
/*     */   public SSLContextBuilder loadKeyMaterial(Path file, char[] storePassword, char[] keyPassword, PrivateKeyStrategy aliasStrategy, OpenOption... openOptions) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 397 */     Args.notNull(file, "Keystore file");
/* 398 */     return loadKeyMaterial(loadKeyStore(file, storePassword, openOptions), keyPassword, aliasStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(KeyStore keyStore, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
/* 404 */     return loadKeyMaterial(keyStore, keyPassword, (PrivateKeyStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(File file, char[] storePassword, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 412 */     Args.notNull(file, "Keystore file");
/* 413 */     return loadKeyMaterial(file.toPath(), storePassword, keyPassword, aliasStrategy, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(File file, char[] storePassword, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 420 */     return loadKeyMaterial(file, storePassword, keyPassword, (PrivateKeyStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(URL url, char[] storePassword, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 428 */     Args.notNull(url, "Keystore URL");
/* 429 */     return loadKeyMaterial(loadKeyStore(url, storePassword), keyPassword, aliasStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(URL url, char[] storePassword, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 436 */     return loadKeyMaterial(url, storePassword, keyPassword, (PrivateKeyStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initSSLContext(SSLContext sslContext, Collection<KeyManager> keyManagers, Collection<TrustManager> trustManagers, SecureRandom secureRandom) throws KeyManagementException {
/* 444 */     sslContext.init(
/* 445 */         !keyManagers.isEmpty() ? keyManagers.<KeyManager>toArray(EMPTY_KEY_MANAGER_ARRAY) : null, 
/* 446 */         !trustManagers.isEmpty() ? trustManagers.<TrustManager>toArray(EMPTY_TRUST_MANAGER_ARRAY) : null, secureRandom);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyStore loadKeyStore(Path file, char[] password, OpenOption... openOptions) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
/* 452 */     KeyStore keyStore = KeyStore.getInstance(this.keyStoreType);
/* 453 */     try (InputStream inputStream = Files.newInputStream(file, openOptions)) {
/* 454 */       keyStore.load(inputStream, password);
/*     */     } 
/* 456 */     return keyStore;
/*     */   }
/*     */ 
/*     */   
/*     */   private KeyStore loadKeyStore(URL url, char[] password) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
/* 461 */     KeyStore keyStore = KeyStore.getInstance(this.keyStoreType);
/* 462 */     try (InputStream inputStream = url.openStream()) {
/* 463 */       keyStore.load(inputStream, password);
/*     */     } 
/* 465 */     return keyStore;
/*     */   }
/*     */   
/*     */   public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
/*     */     SSLContext sslContext;
/* 470 */     String protocolStr = (this.protocol != null) ? this.protocol : "TLS";
/* 471 */     if (this.provider != null) {
/* 472 */       sslContext = SSLContext.getInstance(protocolStr, this.provider);
/*     */     } else {
/* 474 */       sslContext = SSLContext.getInstance(protocolStr);
/*     */     } 
/* 476 */     initSSLContext(sslContext, this.keyManagers, this.trustManagers, this.secureRandom);
/* 477 */     return sslContext;
/*     */   }
/*     */   
/*     */   static class TrustManagerDelegate
/*     */     implements X509TrustManager {
/*     */     private final X509TrustManager trustManager;
/*     */     private final TrustStrategy trustStrategy;
/*     */     
/*     */     TrustManagerDelegate(X509TrustManager trustManager, TrustStrategy trustStrategy) {
/* 486 */       this.trustManager = trustManager;
/* 487 */       this.trustStrategy = trustStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 493 */       this.trustManager.checkClientTrusted(chain, authType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 499 */       if (!this.trustStrategy.isTrusted(chain, authType)) {
/* 500 */         this.trustManager.checkServerTrusted(chain, authType);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/* 506 */       return this.trustManager.getAcceptedIssuers();
/*     */     }
/*     */   }
/*     */   
/*     */   static class KeyManagerDelegate
/*     */     extends X509ExtendedKeyManager
/*     */   {
/*     */     private final X509ExtendedKeyManager keyManager;
/*     */     private final PrivateKeyStrategy aliasStrategy;
/*     */     
/*     */     KeyManagerDelegate(X509ExtendedKeyManager keyManager, PrivateKeyStrategy aliasStrategy) {
/* 517 */       this.keyManager = keyManager;
/* 518 */       this.aliasStrategy = aliasStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getClientAliases(String keyType, Principal[] issuers) {
/* 524 */       return this.keyManager.getClientAliases(keyType, issuers);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, PrivateKeyDetails> getClientAliasMap(String[] keyTypes, Principal[] issuers) {
/* 529 */       Map<String, PrivateKeyDetails> validAliases = new HashMap<>();
/* 530 */       for (String keyType : keyTypes) {
/* 531 */         putPrivateKeyDetails(validAliases, keyType, this.keyManager.getClientAliases(keyType, issuers));
/*     */       }
/* 533 */       return validAliases;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, PrivateKeyDetails> getServerAliasMap(String keyType, Principal[] issuers) {
/* 538 */       Map<String, PrivateKeyDetails> validAliases = new HashMap<>();
/* 539 */       putPrivateKeyDetails(validAliases, keyType, this.keyManager.getServerAliases(keyType, issuers));
/* 540 */       return validAliases;
/*     */     }
/*     */ 
/*     */     
/*     */     private void putPrivateKeyDetails(Map<String, PrivateKeyDetails> validAliases, String keyType, String[] aliases) {
/* 545 */       if (aliases != null) {
/* 546 */         for (String alias : aliases) {
/* 547 */           validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
/* 555 */       Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
/* 556 */       return this.aliasStrategy.chooseAlias(validAliases, (socket instanceof SSLSocket) ? ((SSLSocket)socket)
/* 557 */           .getSSLParameters() : null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getServerAliases(String keyType, Principal[] issuers) {
/* 563 */       return this.keyManager.getServerAliases(keyType, issuers);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
/* 569 */       Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
/* 570 */       return this.aliasStrategy.chooseAlias(validAliases, (socket instanceof SSLSocket) ? ((SSLSocket)socket)
/* 571 */           .getSSLParameters() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public X509Certificate[] getCertificateChain(String alias) {
/* 576 */       return this.keyManager.getCertificateChain(alias);
/*     */     }
/*     */ 
/*     */     
/*     */     public PrivateKey getPrivateKey(String alias) {
/* 581 */       return this.keyManager.getPrivateKey(alias);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseEngineClientAlias(String[] keyTypes, Principal[] issuers, SSLEngine sslEngine) {
/* 587 */       Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
/* 588 */       return this.aliasStrategy.chooseAlias(validAliases, sslEngine.getSSLParameters());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine sslEngine) {
/* 594 */       Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
/* 595 */       return this.aliasStrategy.chooseAlias(validAliases, sslEngine.getSSLParameters());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Provider requireNonNullProvider(String name) throws NoSuchProviderException {
/* 601 */     Provider provider = Security.getProvider(name);
/* 602 */     if (provider == null) {
/* 603 */       throw new NoSuchProviderException(name);
/*     */     }
/* 605 */     return provider;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 610 */     return "[provider=" + this.provider + ", protocol=" + this.protocol + ", keyStoreType=" + this.keyStoreType + ", keyManagerFactoryAlgorithm=" + this.keyManagerFactoryAlgorithm + ", keyManagers=" + this.keyManagers + ", trustManagerFactoryAlgorithm=" + this.trustManagerFactoryAlgorithm + ", trustManagers=" + this.trustManagers + ", secureRandom=" + this.secureRandom + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/ssl/SSLContextBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */