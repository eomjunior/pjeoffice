/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.imp.exception.InterruptedOperationException;
/*     */ import com.github.signer4j.imp.exception.PrivateKeyNotFound;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ abstract class AbstractKeyStore
/*     */   implements IKeyStore
/*     */ {
/*  54 */   protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractKeyStore.class);
/*     */   
/*     */   private volatile boolean closed = false;
/*     */   
/*     */   private volatile boolean initKey = false;
/*     */   
/*     */   protected final KeyStore keyStore;
/*     */   private final IDevice device;
/*     */   private final Runnable logout;
/*     */   
/*     */   protected AbstractKeyStore(KeyStore keystore, IDevice device) throws PrivateKeyNotFound {
/*  65 */     this(keystore, device, () -> {
/*     */         
/*     */         });
/*     */   } protected AbstractKeyStore(KeyStore keystore, IDevice device, Runnable logout) throws PrivateKeyNotFound {
/*  69 */     this.keyStore = (KeyStore)Args.requireNonNull(keystore, "null keystore is not supported");
/*  70 */     this.logout = (Runnable)Args.requireNonNull(logout, "logout is null");
/*  71 */     this.device = (IDevice)Args.requireNonNull(device, "device is null");
/*     */   }
/*     */   
/*     */   protected final void dispose() {
/*  75 */     this.logout.run();
/*     */   }
/*     */   
/*     */   protected void checkIfAvailable() {
/*  79 */     if (isClosed()) {
/*  80 */       throw new IllegalStateException("KeyStore fechado. Possível perda de conexão com dispositivo");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final IDevice getDevice() {
/*  86 */     checkIfAvailable();
/*  87 */     return this.device;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Enumeration<String> getAliases() throws Signer4JException {
/*  92 */     checkIfAvailable();
/*     */     try {
/*  94 */       return this.keyStore.aliases();
/*  95 */     } catch (KeyStoreException e) {
/*  96 */       dispose();
/*  97 */       throw new Signer4JException("Não foi possível ler os aliases do keystore", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClosed() {
/* 103 */     return this.closed;
/*     */   }
/*     */   
/*     */   private final <T> T invoke(IProvider<T> tryBlock) throws Signer4JException {
/* 107 */     return (T)Signer4JInvoker.SIGNER4J.invoke(tryBlock, Signer4jContext.ifNotInterruptedDiscardWith(this.logout));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Certificate getCertificate(String alias) throws Signer4JException {
/* 112 */     checkIfAvailable();
/* 113 */     return invoke(() -> this.keyStore.getCertificate(alias));
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getCertificateAlias(Certificate certificate) throws Signer4JException {
/* 118 */     checkIfAvailable();
/* 119 */     return invoke(() -> this.keyStore.getCertificateAlias(certificate));
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<Certificate> getCertificateChain(String alias) throws Signer4JException {
/* 124 */     checkIfAvailable();
/* 125 */     return Collections.unmodifiableList(Containers.arrayList(invoke(() -> this.keyStore.getCertificateChain(alias))));
/*     */   }
/*     */ 
/*     */   
/*     */   public final PrivateKey getPrivateKey(String alias, char[] password) throws Signer4JException {
/* 130 */     checkIfAvailable();
/* 131 */     return invoke(() -> {
/*     */           PrivateKey key = (PrivateKey)Optional.<PrivateKey>ofNullable((PrivateKey)this.keyStore.getKey(alias, password)).orElseThrow(());
/*     */           if (!this.initKey) {
/*     */             onInitKey(key);
/*     */             this.initKey = true;
/*     */           } 
/*     */           return key;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onInitKey(PrivateKey key) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProvider() throws Signer4JException {
/* 149 */     checkIfAvailable();
/* 150 */     return invoke(this.keyStore.getProvider()::getName);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() throws Exception {
/* 155 */     if (!isClosed())
/*     */       try {
/* 157 */         doClose();
/* 158 */       } catch (Exception e) {
/* 159 */         LOGGER.warn("Unabled to close keystore gracefully", e);
/*     */       } finally {
/* 161 */         this.closed = true;
/* 162 */         this.initKey = false;
/*     */       }  
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AbstractKeyStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */