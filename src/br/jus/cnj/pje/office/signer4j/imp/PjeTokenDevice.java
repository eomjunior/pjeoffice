/*     */ package br.jus.cnj.pje.office.signer4j.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.imp.PjeConfig;
/*     */ import br.jus.cnj.pje.office.gui.certlist.PjeCertificateListAcessor;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeTokenAccess;
/*     */ import com.github.signer4j.IAuthStrategy;
/*     */ import com.github.signer4j.ICertificate;
/*     */ import com.github.signer4j.ICertificateListUI;
/*     */ import com.github.signer4j.IStatusMonitor;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.ITokenSupplier;
/*     */ import com.github.signer4j.imp.AuthStrategy;
/*     */ import com.github.signer4j.imp.MSCAPIPTokenAccessor;
/*     */ import com.github.signer4j.imp.PKCS11TokenAccessor;
/*     */ import com.github.signer4j.imp.Repository;
/*     */ import com.github.signer4j.imp.SwitchRepositoryException;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.Predicate;
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
/*     */ public enum PjeTokenDevice
/*     */   implements IPjeTokenAccess
/*     */ {
/*  58 */   ACCESSOR;
/*     */   
/*  60 */   private final AtomicReference<IPjeTokenAccess> tokenAccess = new AtomicReference<>(PjeNativeTokenDevice.NATIVE);
/*     */   
/*  62 */   private final BehaviorSubject<Repository> repositoryCycle = BehaviorSubject.create();
/*     */   
/*     */   PjeTokenDevice() {
/*  65 */     switchRepository(PjeConfig.defaultRepository());
/*     */   }
/*     */   
/*     */   public final ReentrantLock getLock() {
/*  69 */     return ((IPjeTokenAccess)this.tokenAccess.get()).getLock();
/*     */   }
/*     */   
/*     */   public final Observable<Repository> newRepository() {
/*  73 */     return (Observable<Repository>)this.repositoryCycle;
/*     */   }
/*     */   
/*     */   private boolean isNative() {
/*  77 */     return (this.tokenAccess.get() == PjeNativeTokenDevice.NATIVE);
/*     */   }
/*     */   
/*     */   private boolean isMsCapi() {
/*  81 */     return (this.tokenAccess.get() == PjeMSCAPIPTokenAccessor.MSCAPI);
/*     */   }
/*     */   
/*     */   private void toNative() {
/*  85 */     ((IPjeTokenAccess)this.tokenAccess.getAndSet(PjeNativeTokenDevice.NATIVE)).close();
/*  86 */     this.repositoryCycle.onNext(Repository.NATIVE.adaptProgress());
/*     */   }
/*     */   
/*     */   private void toMSCAPI() {
/*  90 */     ((IPjeTokenAccess)this.tokenAccess.getAndSet(PjeMSCAPIPTokenAccessor.MSCAPI)).close();
/*  91 */     this.repositoryCycle.onNext(Repository.MSCAPI.adaptProgress());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAuthenticated() {
/*  96 */     return ((IPjeTokenAccess)this.tokenAccess.get()).isAuthenticated();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStatusMonitor> newToken() {
/* 101 */     return ((IPjeTokenAccess)this.tokenAccess.get()).newToken();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logout() {
/* 106 */     ((IPjeTokenAccess)this.tokenAccess.get()).logout();
/*     */   }
/*     */ 
/*     */   
/*     */   public final IAuthStrategy getAuthStrategy() {
/* 111 */     return ((IPjeTokenAccess)this.tokenAccess.get()).getAuthStrategy();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setAuthStrategy(IAuthStrategy strategy) {
/* 116 */     ((IPjeTokenAccess)this.tokenAccess.get()).setAuthStrategy(strategy);
/*     */   }
/*     */ 
/*     */   
/*     */   public final PjeToken call() {
/* 121 */     return handleSwitch(() -> (PjeToken)((IPjeTokenAccess)this.tokenAccess.get()).call());
/*     */   }
/*     */   
/*     */   protected final <T> T handleSwitch(ITokenSupplier<T> supplier) {
/*     */     while (true) {
/*     */       try {
/* 127 */         return (T)supplier.call();
/* 128 */       } catch (SwitchRepositoryException e) {
/* 129 */         switchRepository(e.getRepository());
/* 130 */         e.waitFor();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void showCertificatesAsync() {
/* 136 */     Threads.startDaemon("showcertificates", () -> Throwables.quietly(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Repository getRepository() {
/* 141 */     return ((IPjeTokenAccess)this.tokenAccess.get()).getRepository();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/* 146 */     ((IPjeTokenAccess)this.tokenAccess.get()).close();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void reset() {
/* 151 */     ((IPjeTokenAccess)this.tokenAccess.get()).reset();
/*     */   }
/*     */   
/*     */   private void switchRepository(Repository targetRepository) {
/* 155 */     if (targetRepository.isMSCAPI() && !isMsCapi()) {
/* 156 */       toMSCAPI();
/* 157 */     } else if (targetRepository.isNative() && !isNative()) {
/* 158 */       toNative();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<ICertificateListUI.ICertificateEntry> showCertificates(boolean force, boolean autoSelect, boolean repoWaiting) {
/* 164 */     return handleSwitch(() -> ((IPjeTokenAccess)this.tokenAccess.get()).showCertificates(force, autoSelect, repoWaiting));
/*     */   }
/*     */   
/*     */   private static class PjeNativeTokenDevice
/*     */     extends PKCS11TokenAccessor<PjeToken> implements IPjeTokenAccess {
/* 169 */     private static final IPjeTokenAccess NATIVE = new PjeNativeTokenDevice();
/*     */     
/*     */     private static AuthStrategy defaultNativeStrategy() {
/* 172 */       return AuthStrategy.forName(PjeConfig.authStrategy().orElse(AuthStrategy.AWAYS.name())).orElse(AuthStrategy.AWAYS);
/*     */     }
/*     */     
/*     */     private PjeNativeTokenDevice() {
/* 176 */       super(defaultNativeStrategy(), (a1, a3) -> {
/*     */             PjeConfig.loadA1Paths(a1::add);
/*     */             PjeConfig.loadA3Paths(a3::add);
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doDispose(PjeToken token) {
/* 184 */       token.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doSaveStrategy(IAuthStrategy strategy) {
/* 189 */       PjeConfig.save(strategy);
/* 190 */       token().ifPresent(token -> token.setStrategy(strategy));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected PjeToken createToken(IToken token, ReentrantLock lock) {
/* 197 */       return new PjeToken(token, getAuthStrategy(), lock);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Predicate<ICertificate> getCertificateFilter() {
/* 202 */       return PjeCertificateListAcessor.SUPPORTED_CERTIFICATE;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantLock getLock() {
/* 207 */       return lockInstance();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PjeMSCAPIPTokenAccessor
/*     */     extends MSCAPIPTokenAccessor<PjeToken> implements IPjeTokenAccess {
/* 213 */     private static final IPjeTokenAccess MSCAPI = new PjeMSCAPIPTokenAccessor();
/*     */ 
/*     */     
/*     */     private static AuthStrategy defaultMscapiStrategy() {
/* 217 */       AuthStrategy capi = AuthStrategy.forName(PjeConfig.authStrategy().orElse(AuthStrategy.ONE_TIME.name())).orElse(AuthStrategy.ONE_TIME);
/* 218 */       if (AuthStrategy.AWAYS == capi)
/* 219 */         PjeConfig.save((IAuthStrategy)(capi = AuthStrategy.ONE_TIME)); 
/* 220 */       return capi;
/*     */     }
/*     */     
/*     */     private PjeMSCAPIPTokenAccessor() {
/* 224 */       super(defaultMscapiStrategy());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doDispose(PjeToken token) {
/* 229 */       token.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doSaveStrategy(IAuthStrategy strategy) {
/* 234 */       PjeConfig.save(strategy);
/* 235 */       token().ifPresent(token -> token.setStrategy(strategy));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected PjeToken createToken(IToken token, ReentrantLock lock) {
/* 242 */       return new PjeToken(token, getAuthStrategy(), lock);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Predicate<ICertificate> getCertificateFilter() {
/* 247 */       return PjeCertificateListAcessor.SUPPORTED_CERTIFICATE;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantLock getLock() {
/* 252 */       return lockInstance();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/signer4j/imp/PjeTokenDevice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */