/*     */ package br.jus.cnj.pje.office.signer4j.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.gui.certlist.PjeCertificateListAcessor;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeXmlSignerBuilder;
/*     */ import com.github.signer4j.IAuthStrategy;
/*     */ import com.github.signer4j.ICMSSignerBuilder;
/*     */ import com.github.signer4j.ICertificateChooser;
/*     */ import com.github.signer4j.ICertificateChooserFactory;
/*     */ import com.github.signer4j.ICertificates;
/*     */ import com.github.signer4j.IKeyStoreAccess;
/*     */ import com.github.signer4j.IPKCS7SignerBuilder;
/*     */ import com.github.signer4j.IPasswordCallbackHandler;
/*     */ import com.github.signer4j.IPasswordCollector;
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.imp.TokenCycle;
/*     */ import com.github.signer4j.imp.exception.LoginCanceledException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class PjeToken
/*     */   extends TokenCycle
/*     */   implements IPjeToken
/*     */ {
/*     */   private static final ICertificateChooserFactory PJE;
/*     */   
/*     */   static {
/*  53 */     PJE = ((k, c) -> new PjeCertificateListAcessor(k, c));
/*     */   }
/*     */   PjeToken(IToken token, IAuthStrategy strategy, ReentrantLock lock) {
/*  56 */     super(token, strategy, lock);
/*     */   }
/*     */   
/*     */   final void setStrategy(IAuthStrategy newStrategy) {
/*  60 */     (this.strategy = newStrategy).logout(this.token);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ISignerBuilder signerBuilder() {
/*  65 */     return signerBuilder(ICertificateChooserFactory.fromCertificate(getDefaultCertificate(), PJE));
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICMSSignerBuilder cmsSignerBuilder() {
/*  70 */     return cmsSignerBuilder(ICertificateChooserFactory.fromCertificate(getDefaultCertificate(), PJE));
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPKCS7SignerBuilder pkcs7SignerBuilder() {
/*  75 */     return pkcs7SignerBuilder(ICertificateChooserFactory.fromCertificate(getDefaultCertificate(), PJE));
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPjeXmlSignerBuilder xmlSignerBuilder() {
/*  80 */     return xmlSignerBuilder(ICertificateChooserFactory.fromCertificate(getDefaultCertificate(), PJE));
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPjeXmlSignerBuilder xmlSignerBuilder(ICertificateChooserFactory factory) {
/*  85 */     return createBuilder(createChooser(factory));
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICertificateChooser createChooser() {
/*  90 */     return createChooser(ICertificateChooserFactory.fromCertificate(getDefaultCertificate(), PJE));
/*     */   }
/*     */   
/*     */   private final IPjeXmlSignerBuilder createBuilder(ICertificateChooser chooser) {
/*  94 */     return new PjeXmlSigner.Builder(chooser, () -> logout(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPjeToken login() throws Signer4JException {
/*  99 */     return (IPjeToken)super.login();
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPjeToken login(IPasswordCallbackHandler callback) throws Signer4JException {
/* 104 */     throw new LoginCanceledException("Strategy for callback is not supported!");
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPjeToken login(IPasswordCollector collector) throws Signer4JException {
/* 109 */     throw new LoginCanceledException("Strategy for password collector is not supported!");
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPjeToken login(char[] password) throws Signer4JException {
/* 114 */     throw new LoginCanceledException("Strategy for literal password is not supported!");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/signer4j/imp/PjeToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */