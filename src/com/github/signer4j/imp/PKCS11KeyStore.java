/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.imp.exception.PrivateKeyNotFound;
/*    */ import com.github.signer4j.provider.ProviderInstaller;
/*    */ import java.security.KeyStore;
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
/*    */ class PKCS11KeyStore
/*    */   extends AbstractKeyStore
/*    */ {
/*    */   PKCS11KeyStore(KeyStore keystore, IDevice device, Runnable logout) throws PrivateKeyNotFound {
/* 39 */     super(keystore, device, logout);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doClose() throws Exception {
/* 44 */     ProviderInstaller.uninstall(this.keyStore.getProvider());
/* 45 */     super.doClose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11KeyStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */