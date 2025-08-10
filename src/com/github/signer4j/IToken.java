/*     */ package com.github.signer4j;
/*     */ 
/*     */ import com.github.signer4j.gui.PasswordDialogCallbackHandler;
/*     */ import com.github.signer4j.gui.PasswordStrategyDialogCallbackHandler;
/*     */ import com.github.signer4j.imp.LiteralPasswordCallbackHandler;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
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
/*     */ public interface IToken
/*     */   extends IGadget, IStatusMonitor
/*     */ {
/*     */   String getManufacturer();
/*     */   
/*     */   long getMinPinLen();
/*     */   
/*     */   long getMaxPinLen();
/*     */   
/*     */   boolean isAuthenticated();
/*     */   
/*     */   TokenType getType();
/*     */   
/*     */   ISlot getSlot();
/*     */   
/*     */   Optional<IKeyStoreAccess> getKeyStoreAccess();
/*     */   
/*     */   ICertificates getCertificates();
/*     */   
/*     */   ISignerBuilder signerBuilder();
/*     */   
/*     */   ISignerBuilder signerBuilder(ICertificateChooserFactory paramICertificateChooserFactory);
/*     */   
/*     */   ICMSSignerBuilder cmsSignerBuilder();
/*     */   
/*     */   ICMSSignerBuilder cmsSignerBuilder(ICertificateChooserFactory paramICertificateChooserFactory);
/*     */   
/*     */   IPKCS7SignerBuilder pkcs7SignerBuilder();
/*     */   
/*     */   IPKCS7SignerBuilder pkcs7SignerBuilder(ICertificateChooserFactory paramICertificateChooserFactory);
/*     */   
/*     */   ICertificateChooser createChooser(ICertificateChooserFactory paramICertificateChooserFactory);
/*     */   
/*     */   boolean isExpired();
/*     */   
/*     */   boolean isMscapi();
/*     */   
/*     */   void logout();
/*     */   
/*     */   void setDefaultCertificate(ICertificate paramICertificate);
/*     */   
/*     */   Optional<ICertificate> getDefaultCertificate();
/*     */   
/*     */   IToken login(IPasswordCallbackHandler paramIPasswordCallbackHandler) throws Signer4JException;
/*     */   
/*     */   default ICertificateChooser createChooser() {
/*  82 */     return createChooser(ICertificateChooserFactory.DEFAULT);
/*     */   }
/*     */   
/*     */   default IToken login() throws Signer4JException {
/*  86 */     return login(IPasswordCollector.NOTHING);
/*     */   }
/*     */   
/*     */   default IToken login(IPasswordCollector collector) throws Signer4JException {
/*  90 */     return login((IPasswordCallbackHandler)new PasswordDialogCallbackHandler(this, collector));
/*     */   }
/*     */   
/*     */   default IToken login(IPasswordCollector collector, Object strategy) throws Signer4JException {
/*  94 */     return login((IPasswordCallbackHandler)new PasswordStrategyDialogCallbackHandler(this, collector));
/*     */   }
/*     */   
/*     */   default IToken login(Object strategy) throws Signer4JException {
/*  98 */     return login((IPasswordCallbackHandler)new PasswordStrategyDialogCallbackHandler(this));
/*     */   }
/*     */   
/*     */   default IToken login(char[] password) throws Signer4JException {
/* 102 */     return (password == null) ? login(Boolean.valueOf(true)) : login((IPasswordCallbackHandler)new LiteralPasswordCallbackHandler(password));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */