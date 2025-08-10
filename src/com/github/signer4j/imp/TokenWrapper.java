/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.ICMSSignerBuilder;
/*     */ import com.github.signer4j.ICertificate;
/*     */ import com.github.signer4j.ICertificateChooser;
/*     */ import com.github.signer4j.ICertificateChooserFactory;
/*     */ import com.github.signer4j.ICertificates;
/*     */ import com.github.signer4j.IKeyStoreAccess;
/*     */ import com.github.signer4j.IPKCS7SignerBuilder;
/*     */ import com.github.signer4j.IPasswordCallbackHandler;
/*     */ import com.github.signer4j.IPasswordCollector;
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.TokenType;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import io.reactivex.Observable;
/*     */ import java.util.Optional;
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
/*     */ public class TokenWrapper
/*     */   implements IToken
/*     */ {
/*     */   protected final IToken token;
/*     */   
/*     */   protected TokenWrapper(IToken token) {
/*  55 */     this.token = (IToken)Args.requireNonNull(token, "token is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLabel() {
/*  60 */     return this.token.getLabel();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getModel() {
/*  65 */     return this.token.getModel();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerial() {
/*  70 */     return this.token.getSerial();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getManufacturer() {
/*  75 */     return this.token.getManufacturer();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExpired() {
/*  80 */     return this.token.isExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMscapi() {
/*  85 */     return this.token.isMscapi();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMinPinLen() {
/*  90 */     return this.token.getMinPinLen();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMaxPinLen() {
/*  95 */     return this.token.getMaxPinLen();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAuthenticated() {
/* 100 */     return this.token.isAuthenticated();
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<Boolean> getStatus() {
/* 105 */     return this.token.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public ISlot getSlot() {
/* 110 */     return this.token.getSlot();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<IKeyStoreAccess> getKeyStoreAccess() {
/* 115 */     return this.token.getKeyStoreAccess();
/*     */   }
/*     */ 
/*     */   
/*     */   public ICertificates getCertificates() {
/* 120 */     return this.token.getCertificates();
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignerBuilder signerBuilder() {
/* 125 */     return this.token.signerBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignerBuilder signerBuilder(ICertificateChooserFactory factory) {
/* 130 */     return this.token.signerBuilder(factory);
/*     */   }
/*     */ 
/*     */   
/*     */   public ICMSSignerBuilder cmsSignerBuilder() {
/* 135 */     return this.token.cmsSignerBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ICMSSignerBuilder cmsSignerBuilder(ICertificateChooserFactory factory) {
/* 140 */     return this.token.cmsSignerBuilder(factory);
/*     */   }
/*     */ 
/*     */   
/*     */   public IPKCS7SignerBuilder pkcs7SignerBuilder() {
/* 145 */     return this.token.pkcs7SignerBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public IPKCS7SignerBuilder pkcs7SignerBuilder(ICertificateChooserFactory factory) {
/* 150 */     return this.token.pkcs7SignerBuilder(factory);
/*     */   }
/*     */ 
/*     */   
/*     */   public IToken login(IPasswordCallbackHandler callback) throws Signer4JException {
/* 155 */     this.token.login(callback);
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IToken login() throws Signer4JException {
/* 161 */     this.token.login();
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IToken login(IPasswordCollector collector) throws Signer4JException {
/* 167 */     this.token.login(collector);
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IToken login(char[] password) throws Signer4JException {
/* 173 */     this.token.login(password);
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void logout() {
/* 179 */     this.token.logout();
/*     */   }
/*     */ 
/*     */   
/*     */   public TokenType getType() {
/* 184 */     return this.token.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCategory() {
/* 189 */     return this.token.getCategory();
/*     */   }
/*     */ 
/*     */   
/*     */   public ICertificateChooser createChooser(ICertificateChooserFactory factory) {
/* 194 */     return this.token.createChooser(factory);
/*     */   }
/*     */ 
/*     */   
/*     */   public ICertificateChooser createChooser() {
/* 199 */     return this.token.createChooser();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultCertificate(ICertificate certificate) {
/* 204 */     this.token.setDefaultCertificate(certificate);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ICertificate> getDefaultCertificate() {
/* 209 */     return this.token.getDefaultCertificate();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/TokenWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */