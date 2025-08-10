/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.ICertificate;
/*     */ import com.github.signer4j.IPasswordCallbackHandler;
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.TokenType;
/*     */ import com.github.signer4j.cert.ICertificateFactory;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import com.github.signer4j.exception.DriverFailException;
/*     */ import com.github.signer4j.exception.DriverSessionException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.IParams;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import sun.security.pkcs11.wrapper.PKCS11;
/*     */ import sun.security.pkcs11.wrapper.PKCS11Exception;
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
/*     */ class PKCS11Token
/*     */   extends AbstractToken<PKCS11Slot>
/*     */ {
/*     */   private long minPinLen;
/*     */   private long maxPinLen;
/*     */   
/*     */   private PKCS11Token(PKCS11Slot slot) throws DriverException {
/*  55 */     super(slot, TokenType.A3);
/*     */   }
/*     */   
/*     */   final PKCS11 getPk() {
/*  59 */     return getSlot().getPK();
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getMinPinLen() {
/*  64 */     return this.minPinLen;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getMaxPinLen() {
/*  69 */     return this.maxPinLen;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void doLogin(IKeyStore keyStore) throws Signer4JException {
/*  74 */     for (ICertificate c : this.certificates) {
/*  75 */       c.setAlias(keyStore.getCertificateAlias(c.toX509()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final IKeyStore getKeyStore(IPasswordCallbackHandler callback) throws Signer4JException {
/*  81 */     return (new PKCS11KeyStoreLoader(getSlot().toDevice(), callback, getDispose()))
/*  82 */       .getKeyStore(
/*  83 */         (IParams)Params.create()
/*  84 */         .of("driverPath", getSlot().getLibrary())
/*  85 */         .of("driverSlot", Long.valueOf(getSlot().getNumber())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  91 */     return "PKCS11Token [label=" + this.label + ", model=" + this.model + ", serial=" + this.serial + ", manufacture=" + this.manufacturer + ", minPinLen=" + this.minPinLen + ", maxPinLen=" + this.maxPinLen + ", certificates=" + this.certificates + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   protected final IToken loadCertificates(ICertificateFactory factory) throws DriverException {
/*     */     long session;
/*  97 */     PKCS11 pk = getPk();
/*     */     
/*     */     try {
/* 100 */       session = pk.C_OpenSession(
/* 101 */           getSlot().getNumber(), 4L, null, null);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 106 */     catch (PKCS11Exception e) {
/* 107 */       throw new DriverFailException("Unabled to open session on token " + this, e);
/*     */     } 
/*     */     
/*     */     try {
/* 111 */       this.certificates = new PKCS11Certificates(this, session, factory);
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 118 */         pk.C_CloseSession(session);
/* 119 */       } catch (PKCS11Exception e) {
/* 120 */         throw new DriverSessionException("Unabled to close token session " + this, e);
/*     */       } 
/*     */     } 
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   static class Builder
/*     */     extends AbstractToken.Builder<PKCS11Slot, PKCS11Token> {
/*     */     private long minPinLen;
/*     */     private long maxPinLen;
/*     */     
/*     */     Builder(PKCS11Slot slot) {
/* 132 */       super(slot);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void initialize(AbstractToken<PKCS11Slot> tk) {
/* 137 */       super.initialize(tk);
/* 138 */       PKCS11Token token = (PKCS11Token)tk;
/* 139 */       token.minPinLen = this.minPinLen;
/* 140 */       token.maxPinLen = this.maxPinLen;
/*     */     }
/*     */     
/*     */     public final Builder withMinPinLen(long minPinLen) {
/* 144 */       this.minPinLen = minPinLen;
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder withMaxPinLen(long maxPinLen) {
/* 149 */       this.maxPinLen = maxPinLen;
/* 150 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected AbstractToken<PKCS11Slot> createToken(PKCS11Slot slot) throws DriverException {
/* 155 */       return new PKCS11Token(slot);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */