/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.imp.exception.PrivateKeyNotFound;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.signer4j.provider.ProviderInstaller;
/*    */ import java.security.KeyStore;
/*    */ import java.security.PrivateKey;
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
/*    */ class MSCAPIKeyStore
/*    */   extends AbstractKeyStore
/*    */ {
/*    */   MSCAPIKeyStore(KeyStore keystore, IDevice device, Runnable logout) throws PrivateKeyNotFound {
/* 43 */     super(keystore, device, logout);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getProvider() throws Signer4JException {
/* 48 */     checkIfAvailable();
/* 49 */     return ProviderInstaller.MSCAPI.defaultName();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doClose() throws Exception {
/* 54 */     ProviderInstaller.uninstall(this.keyStore.getProvider());
/* 55 */     super.doClose();
/*    */   }
/*    */   
/*    */   protected void onInitKey(PrivateKey key) throws Exception {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/MSCAPIKeyStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */