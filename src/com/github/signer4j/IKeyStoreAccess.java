/*    */ package com.github.signer4j;
/*    */ 
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.cert.Certificate;
/*    */ import java.util.Enumeration;
/*    */ import java.util.List;
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
/*    */ public interface IKeyStoreAccess
/*    */ {
/*    */   IDevice getDevice();
/*    */   
/*    */   String getProvider() throws Signer4JException;
/*    */   
/*    */   Enumeration<String> getAliases() throws Signer4JException;
/*    */   
/*    */   Certificate getCertificate(String paramString) throws Signer4JException;
/*    */   
/*    */   String getCertificateAlias(Certificate paramCertificate) throws Signer4JException;
/*    */   
/*    */   List<Certificate> getCertificateChain(String paramString) throws Signer4JException;
/*    */   
/*    */   PrivateKey getPrivateKey(String paramString, char[] paramArrayOfchar) throws Signer4JException;
/*    */   
/*    */   default PrivateKey getPrivateKey(String alias) throws Signer4JException {
/* 54 */     return getPrivateKey(alias, null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IKeyStoreAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */