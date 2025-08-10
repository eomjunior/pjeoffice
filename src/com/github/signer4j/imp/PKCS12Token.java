/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IPasswordCallbackHandler;
/*    */ import com.github.signer4j.ISlot;
/*    */ import com.github.signer4j.IToken;
/*    */ import com.github.signer4j.TokenType;
/*    */ import com.github.signer4j.cert.ICertificateFactory;
/*    */ import com.github.signer4j.exception.DriverException;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.IParams;
/*    */ import com.github.utils4j.imp.Params;
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
/*    */ class PKCS12Token
/*    */   extends AbstractToken<PKCS12Slot>
/*    */ {
/*    */   private static final int MIN_PASSWORD_LENGTH = 1;
/*    */   private static final int MAX_PASSWORD_LENGTH = 31;
/*    */   private ICertificateFactory factory;
/*    */   
/*    */   private PKCS12Token(PKCS12Slot slot) throws DriverException {
/* 48 */     super(slot, TokenType.A1);
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getMinPinLen() {
/* 53 */     return 1L;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getMaxPinLen() {
/* 58 */     return 31L;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLogin(IKeyStore keyStore) throws Signer4JException {
/* 63 */     getSlot().updateDevice(this.certificates = new PKCS12Certificates(this, keyStore, this.factory));
/*    */   }
/*    */ 
/*    */   
/*    */   protected IKeyStore getKeyStore(IPasswordCallbackHandler callback) throws Signer4JException {
/* 68 */     return (new PKCS12KeyStoreLoader(callback, getSlot().toDevice(), getDispose()))
/* 69 */       .getKeyStore(
/* 70 */         (IParams)Params.create().of("certificatePath", getSlot().getLibrary()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return "PKCS12Token [label=" + this.label + ", model=" + this.model + ", serial=" + this.serial + ", manufacture=" + this.manufacturer + ", certificates=" + this.certificates + "]";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected IToken loadCertificates(ICertificateFactory factory) throws DriverException {
/* 82 */     this.factory = factory;
/* 83 */     this.certificates = Unavailables.getCertificates(this);
/* 84 */     return this;
/*    */   }
/*    */   
/*    */   static class Builder
/*    */     extends AbstractToken.Builder<PKCS12Slot, PKCS12Token> {
/*    */     Builder(PKCS12Slot slot) {
/* 90 */       super(slot);
/*    */     }
/*    */ 
/*    */     
/*    */     protected AbstractToken<PKCS12Slot> createToken(PKCS12Slot slot) throws DriverException {
/* 95 */       return new PKCS12Token(slot);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS12Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */