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
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.TokenType;
/*     */ import com.github.signer4j.cert.ICertificateFactory;
/*     */ import com.github.signer4j.cert.imp.CertificateFactory;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import com.github.signer4j.exception.NotAuthenticatedException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ abstract class AbstractToken<S extends ISlot>
/*     */   implements IToken
/*     */ {
/*  64 */   protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractToken.class);
/*     */   
/*     */   private final transient S slot;
/*     */   
/*     */   private final TokenType type;
/*     */   
/*     */   protected String label;
/*     */   
/*     */   protected String model;
/*     */   protected String serial;
/*     */   protected String manufacturer;
/*     */   protected long minPinLen;
/*     */   protected long maxPinLen;
/*  77 */   private final BehaviorSubject<Boolean> status = BehaviorSubject.create();
/*     */   
/*  79 */   private final Runnable disposeAction = this::logout;
/*     */   
/*     */   protected ICertificates certificates;
/*     */   
/*  83 */   private final AtomicReference<IKeyStore> keyStore = new AtomicReference<>();
/*     */   
/*  85 */   private Optional<ICertificate> defaultCert = Optional.empty();
/*     */   
/*     */   protected IPasswordCallbackHandler passwordCallback;
/*     */   
/*     */   protected AbstractToken(S slot, TokenType type) {
/*  90 */     this.slot = (S)Args.requireNonNull(slot, "slot is null");
/*  91 */     this.type = (TokenType)Args.requireNonNull(type, "type is null");
/*     */   }
/*     */   
/*     */   protected final Runnable getDispose() {
/*  95 */     return this.disposeAction;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isExpired() {
/* 100 */     return (this.defaultCert.isPresent() && ((ICertificate)this.defaultCert.get()).isExpired());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMscapi() {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<Boolean> getStatus() {
/* 110 */     return (Observable<Boolean>)this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public final TokenType getType() {
/* 115 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getCategory() {
/* 120 */     return getType().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getLabel() {
/* 125 */     return this.label;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getModel() {
/* 130 */     return this.model;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getSerial() {
/* 135 */     return this.serial;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getManufacturer() {
/* 140 */     return this.manufacturer;
/*     */   }
/*     */ 
/*     */   
/*     */   public final S getSlot() {
/* 145 */     return this.slot;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IKeyStoreAccess> getKeyStoreAccess() {
/* 150 */     return Optional.ofNullable(this.keyStore.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICertificates getCertificates() {
/* 155 */     return this.certificates;
/*     */   }
/*     */   
/*     */   public final boolean isAuthenticated() {
/* 159 */     return (this.keyStore.get() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setDefaultCertificate(ICertificate certificate) {
/* 164 */     this.defaultCert = Optional.ofNullable(certificate);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<ICertificate> getDefaultCertificate() {
/* 169 */     return this.defaultCert;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IToken login(IPasswordCallbackHandler callback) throws Signer4JException {
/* 174 */     if (callback == null) {
/* 175 */       callback = this.passwordCallback;
/*     */     }
/* 177 */     synchronized (this.keyStore) {
/* 178 */       if (!isAuthenticated()) {
/*     */         try {
/*     */           IKeyStore ks;
/* 181 */           doLogin(ks = getKeyStore(callback));
/* 182 */           this.keyStore.set(ks);
/* 183 */           this.status.onNext(Boolean.valueOf(true));
/* 184 */         } catch (Signer4JException e) {
/* 185 */           this.disposeAction.run();
/* 186 */           throw e;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doLogin(IKeyStore keyStore) throws Signer4JException {}
/*     */ 
/*     */   
/*     */   public final void logout() {
/* 199 */     Optional.ofNullable(this.keyStore.getAndSet(null)).ifPresent(ks -> {
/*     */           try {
/*     */             ks.close();
/* 202 */           } catch (Exception e) {
/*     */             LOGGER.warn("Unabled to logout gracefully", e);
/*     */           } finally {
/*     */             this.defaultCert = Optional.empty();
/*     */             this.status.onNext(Boolean.valueOf(false));
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public final ISignerBuilder signerBuilder() {
/* 213 */     return signerBuilder(ICertificateChooserFactory.fromCertificate(this.defaultCert));
/*     */   }
/*     */ 
/*     */   
/*     */   public final ISignerBuilder signerBuilder(ICertificateChooserFactory factory) {
/* 218 */     return createBuilder(createChooser(factory));
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICMSSignerBuilder cmsSignerBuilder() {
/* 223 */     return cmsSignerBuilder(ICertificateChooserFactory.fromCertificate(this.defaultCert));
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICMSSignerBuilder cmsSignerBuilder(ICertificateChooserFactory factory) {
/* 228 */     return createCMSSignerBuilder(createChooser(factory));
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICertificateChooser createChooser(ICertificateChooserFactory factory) {
/* 233 */     Args.requireNonNull(factory, "factory is null");
/* 234 */     return (ICertificateChooser)factory.apply(getKeyStoreAccess().orElseThrow(() -> new NotAuthenticatedException("Unabled to prepare signer with no authenticated token")), this.certificates);
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPKCS7SignerBuilder pkcs7SignerBuilder() {
/* 239 */     return pkcs7SignerBuilder(ICertificateChooserFactory.fromCertificate(this.defaultCert));
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPKCS7SignerBuilder pkcs7SignerBuilder(ICertificateChooserFactory factory) {
/* 244 */     return createPKCS7SignerBuilder(createChooser(factory));
/*     */   }
/*     */   
/*     */   private ISignerBuilder createBuilder(ICertificateChooser chooser) {
/* 248 */     return new SimpleSigner.Builder(chooser, getDispose());
/*     */   }
/*     */   
/*     */   private ICMSSignerBuilder createCMSSignerBuilder(ICertificateChooser chooser) {
/* 252 */     return new CMSSigner.Builder(chooser, getDispose());
/*     */   }
/*     */   
/*     */   private IPKCS7SignerBuilder createPKCS7SignerBuilder(ICertificateChooser chooser) {
/* 256 */     return new PKCS7Signer.Builder(chooser, getDispose());
/*     */   }
/*     */   
/*     */   protected abstract IKeyStore getKeyStore(IPasswordCallbackHandler paramIPasswordCallbackHandler) throws Signer4JException;
/*     */   
/*     */   protected abstract IToken loadCertificates(ICertificateFactory paramICertificateFactory) throws DriverException;
/*     */   
/*     */   static abstract class Builder<S extends ISlot, T extends IToken>
/*     */   {
/*     */     private S slot;
/*     */     private String label;
/* 267 */     private ICertificateFactory factory = (ICertificateFactory)CertificateFactory.DEFAULT; private String model; private String serial; private String manufacturer;
/* 268 */     private IPasswordCallbackHandler passwordCallback = PasswordCallbackHandler.CONSOLE;
/*     */     
/*     */     Builder(S slot) {
/* 271 */       this.slot = (S)Args.requireNonNull(slot, "Unabled to create token with null slot");
/*     */     }
/*     */     
/*     */     public final Builder<S, T> withLabel(String label) {
/* 275 */       this.label = Strings.trim(label);
/* 276 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder<S, T> withModel(String model) {
/* 280 */       this.model = Strings.trim(model);
/* 281 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder<S, T> withSerial(String serial) {
/* 285 */       this.serial = Strings.trim(serial);
/* 286 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder<S, T> withManufacture(String manufacture) {
/* 290 */       this.manufacturer = Strings.trim(manufacture);
/* 291 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder<S, T> withCertificateFactory(ICertificateFactory factory) {
/* 295 */       this.factory = Optional.<ICertificateFactory>ofNullable(factory).orElse(this.factory);
/* 296 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder<S, T> withPasswordCallback(IPasswordCallbackHandler callback) {
/* 300 */       this.passwordCallback = Optional.<IPasswordCallbackHandler>ofNullable(callback).orElse(this.passwordCallback);
/* 301 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public final T build() throws DriverException {
/* 306 */       AbstractToken<S> token = createToken(this.slot);
/* 307 */       initialize(token);
/* 308 */       return (T)token.loadCertificates(this.factory);
/*     */     }
/*     */     
/*     */     protected void initialize(AbstractToken<S> token) {
/* 312 */       token.label = this.label;
/* 313 */       token.model = this.model;
/* 314 */       token.serial = this.serial;
/* 315 */       token.manufacturer = this.manufacturer;
/* 316 */       token.passwordCallback = this.passwordCallback;
/*     */     }
/*     */     
/*     */     protected abstract AbstractToken<S> createToken(S param1S) throws DriverException;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AbstractToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */