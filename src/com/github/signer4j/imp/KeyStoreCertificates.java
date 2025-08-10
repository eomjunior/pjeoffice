/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.IToken;
/*    */ import com.github.signer4j.cert.ICertificateFactory;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.util.Enumeration;
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
/*    */ class KeyStoreCertificates
/*    */   extends AbstractCertificates
/*    */ {
/*    */   private final transient IToken token;
/*    */   
/*    */   KeyStoreCertificates(IToken token, IKeyStore keyStore, ICertificateFactory factory) throws Signer4JException {
/* 45 */     this.token = (IToken)Args.requireNonNull(token, "token is null");
/* 46 */     setup(keyStore, factory);
/*    */   }
/*    */ 
/*    */   
/*    */   public final IToken getToken() {
/* 51 */     return this.token;
/*    */   }
/*    */   
/*    */   private void setup(IKeyStore keyStore, ICertificateFactory factory) throws Signer4JException {
/* 55 */     Args.requireNonNull(keyStore, "keyStore is null");
/* 56 */     Enumeration<String> aliases = keyStore.getAliases();
/* 57 */     while (aliases.hasMoreElements()) {
/* 58 */       ICertificate certificate; String aliasName = aliases.nextElement();
/*    */       
/*    */       try {
/* 61 */         certificate = (ICertificate)factory.create(keyStore.getCertificate(aliasName), aliasName);
/* 62 */       } catch (CertificateException e) {
/* 63 */         reset();
/* 64 */         throw new Signer4JException(e);
/* 65 */       } catch (Signer4JException e) {
/* 66 */         reset();
/* 67 */         throw e;
/*    */       } 
/* 69 */       this.certificates.add(certificate);
/*    */     } 
/*    */     
/* 72 */     this.certificates.sort((a, b) -> b.getAfterDate().compareTo(a.getAfterDate()));
/*    */   }
/*    */   
/*    */   private void reset() {
/* 76 */     this.certificates.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/KeyStoreCertificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */