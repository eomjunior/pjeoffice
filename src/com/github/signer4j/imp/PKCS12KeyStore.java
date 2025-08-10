/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.imp.exception.PrivateKeyNotFound;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.IConstants;
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
/*    */ 
/*    */ 
/*    */ class PKCS12KeyStore
/*    */   extends AbstractKeyStore
/*    */ {
/*    */   private byte[] password;
/* 45 */   private PrivateKey privateKey = null;
/*    */   
/*    */   PKCS12KeyStore(KeyStore keystore, IDevice device, Runnable logout, char[] password) throws PrivateKeyNotFound {
/* 48 */     super(keystore, device, logout);
/* 49 */     this.password = (new String(password)).getBytes(IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */ 
/*    */   
/*    */   public final PrivateKey getPrivateKey(String alias) throws Signer4JException {
/* 54 */     checkIfAvailable();
/* 55 */     if (this.privateKey == null)
/* 56 */       this.privateKey = getPrivateKey(alias, (new String(this.password, IConstants.DEFAULT_CHARSET)).toCharArray()); 
/* 57 */     this.password = null;
/* 58 */     return this.privateKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getProvider() throws Signer4JException {
/* 63 */     checkIfAvailable();
/* 64 */     return "BC";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doClose() throws Exception {
/* 69 */     this.privateKey = null;
/* 70 */     this.password = null;
/* 71 */     super.doClose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS12KeyStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */