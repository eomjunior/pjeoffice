/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.IKeyStoreAccess;
/*     */ import com.github.signer4j.IPasswordCallbackHandler;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.signer4j.provider.ProviderInstaller;
/*     */ import com.github.utils4j.IParams;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.security.AuthProvider;
/*     */ import java.security.KeyStore;
/*     */ import java.util.function.Supplier;
/*     */ import javax.security.auth.callback.CallbackHandler;
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
/*     */ class PKCS11KeyStoreLoader
/*     */   implements IKeyStoreLoader
/*     */ {
/*     */   private final IPasswordCallbackHandler handler;
/*     */   private final IDevice device;
/*     */   private final Runnable dispose;
/*     */   
/*     */   PKCS11KeyStoreLoader(IDevice device) {
/*  54 */     this(device, PasswordCallbackHandler.CONSOLE);
/*     */   }
/*     */   
/*     */   PKCS11KeyStoreLoader(IDevice device, IPasswordCallbackHandler handler) {
/*  58 */     this(device, handler, () -> {
/*     */         
/*     */         });
/*     */   } PKCS11KeyStoreLoader(IDevice device, IPasswordCallbackHandler handler, Runnable dispose) {
/*  62 */     this.handler = (IPasswordCallbackHandler)Args.requireNonNull(handler, "Unabled to create loader with null handler");
/*  63 */     this.dispose = (Runnable)Args.requireNonNull(dispose, "dispose is null");
/*  64 */     this.device = (IDevice)Args.requireNonNull(device, "device is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public IKeyStore getKeyStore(IParams p) throws Signer4JException {
/*  69 */     Args.requireNonNull(p, "Params is null");
/*  70 */     return getKeyStore((String)p
/*  71 */         .orElseThrow("driverPath", validate()), ((Long)p
/*  72 */         .orElse("driverSlot", Long.valueOf(0L))).longValue());
/*     */   }
/*     */ 
/*     */   
/*     */   private IKeyStore getKeyStore(String libraryPath, long slot) throws Signer4JException {
/*  77 */     Args.requireZeroPositive(slot, "slot must be 0 or positive value");
/*  78 */     libraryPath = Args.requireText(libraryPath, "driver path can't be null").trim().replace('\\', '/');
/*  79 */     int s = libraryPath.lastIndexOf('/');
/*  80 */     s = (s <= -1) ? 0 : (s + 1);
/*  81 */     String fileName = libraryPath.substring(s, libraryPath.length());
/*  82 */     return getKeyStore(
/*  83 */         String.format("%s-slot:%s", new Object[] { fileName, Long.valueOf(slot) }), libraryPath, slot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IKeyStore getKeyStore(String providerName, String libraryPath, long slot) throws Signer4JException {
/*  90 */     return getKeyStore(providerName, 
/*     */         
/*  92 */         String.format("name = %s\nlibrary = %s\nslot = %s\ninsertionCheckInterval = 1500\nattributes = compatibility", new Object[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 100 */             providerName, libraryPath, Long.valueOf(slot)
/* 101 */           }).toString());
/*     */   }
/*     */ 
/*     */   
/*     */   private IKeyStore getKeyStore(String providerName, String configString) throws Signer4JException {
/* 106 */     return (IKeyStore)Signer4JInvoker.SIGNER4J.invoke(() -> {
/*     */           AuthProvider provider = (AuthProvider)ProviderInstaller.SUNPKCS11.install(providerName, configString);
/*     */           
/*     */           try {
/*     */             provider.login(null, (CallbackHandler)this.handler);
/*     */             KeyStore keyStore = KeyStore.getInstance("PKCS11", provider);
/*     */             keyStore.load(null, null);
/*     */             return new PKCS11KeyStore(keyStore, this.device, this.dispose);
/* 114 */           } catch (Throwable e) {
/*     */             ProviderInstaller.uninstall(provider);
/*     */             throw e;
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Supplier<? extends IllegalArgumentException> validate() {
/* 123 */     return () -> new IllegalArgumentException("driverPath is undefined");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11KeyStoreLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */