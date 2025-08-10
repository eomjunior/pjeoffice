/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.IKeyStoreAccess;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.signer4j.provider.ProviderInstaller;
/*    */ import com.github.utils4j.IParams;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.security.KeyStore;
/*    */ import java.security.Provider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class MSCAPIKeyStoreLoader
/*    */   implements IKeyStoreLoader
/*    */ {
/*    */   private static final String MSCAPI_TYPE = "Windows-MY";
/*    */   private IDevice device;
/*    */   private Runnable dispose;
/*    */   
/*    */   public MSCAPIKeyStoreLoader(IDevice device, Runnable dispose) {
/* 52 */     this.device = (IDevice)Args.requireNonNull(device, "device is null");
/* 53 */     this.dispose = (Runnable)Args.requireNonNull(dispose, "dispose is null");
/*    */   }
/*    */   
/*    */   public IKeyStore getKeyStore() throws Signer4JException {
/* 57 */     return getKeyStore((IParams)Params.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public IKeyStore getKeyStore(IParams params) throws Signer4JException {
/* 62 */     return (IKeyStore)Signer4JInvoker.SIGNER4J.invoke(() -> {
/*    */           Provider provider = ProviderInstaller.MSCAPI.install();
/*    */           
/*    */           provider.put("Signature.ASN1MD2withRSA", "sun.security.mscapi.LITERALwithRSASignature$MD2withRSA");
/*    */           
/*    */           provider.put("Signature.ASN1MD5withRSA", "sun.security.mscapi.LITERALwithRSASignature$MD5withRSA");
/*    */           
/*    */           provider.put("Signature.ASN1SHA1withRSA", "sun.security.mscapi.LITERALwithRSASignature$SHA1withRSA");
/*    */           provider.put("Signature.ASN1SHA256withRSA", "sun.security.mscapi.LITERALwithRSASignature$SHA256withRSA");
/*    */           provider.put("Signature.ASN1SHA384withRSA", "sun.security.mscapi.LITERALwithRSASignature$SHA384withRSA");
/*    */           provider.put("Signature.ASN1SHA512withRSA", "sun.security.mscapi.LITERALwithRSASignature$SHA512withRSA");
/*    */           try {
/*    */             KeyStore keyStore = KeyStore.getInstance("Windows-MY", provider);
/*    */             keyStore.load(null, null);
/*    */             return new MSCAPIKeyStore(keyStore, this.device, this.dispose);
/* 77 */           } catch (Throwable e) {
/*    */             ProviderInstaller.uninstall(provider);
/*    */             throw e;
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/MSCAPIKeyStoreLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */