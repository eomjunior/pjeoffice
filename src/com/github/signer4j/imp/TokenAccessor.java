/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.imp.ProgressFactories;
/*     */ import com.github.signer4j.IAuthStrategy;
/*     */ import com.github.signer4j.ICertificate;
/*     */ import com.github.signer4j.ICertificateListUI;
/*     */ import com.github.signer4j.IDeviceManager;
/*     */ import com.github.signer4j.IFilePath;
/*     */ import com.github.signer4j.IStatusMonitor;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.ITokenAccess;
/*     */ import com.github.signer4j.TokenType;
/*     */ import com.github.signer4j.gui.CertificateListDialog;
/*     */ import com.github.signer4j.gui.alert.ExpiredPasswordAlert;
/*     */ import com.github.signer4j.gui.alert.NoTokenPresentInfo;
/*     */ import com.github.signer4j.gui.alert.TokenLockedAlert;
/*     */ import com.github.signer4j.gui.utils.InvalidPinAlert;
/*     */ import com.github.signer4j.imp.exception.ExpiredCredentialException;
/*     */ import com.github.signer4j.imp.exception.InterruptedOperationException;
/*     */ import com.github.signer4j.imp.exception.InvalidPinException;
/*     */ import com.github.signer4j.imp.exception.LoginCanceledException;
/*     */ import com.github.signer4j.imp.exception.NoTokenPresentException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.signer4j.imp.exception.TokenLockedException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TokenAccessor<T extends IToken>
/*     */   implements ITokenAccess<T>
/*     */ {
/*     */   private volatile T token;
/*     */   private Disposable ticketTokenLogout;
/*     */   protected final IDeviceManager manager;
/*     */   private volatile IAuthStrategy strategy;
/*     */   private volatile boolean autoForce = true;
/*  82 */   private final ReentrantLock lock = new ReentrantLock();
/*     */   
/*  84 */   private BehaviorSubject<IStatusMonitor> tokenCycle = BehaviorSubject.create();
/*     */   
/*     */   protected TokenAccessor(AuthStrategy strategy, IDeviceManager deviceManager) {
/*  87 */     this.strategy = (IAuthStrategy)Args.requireNonNull(strategy, "strategy is null");
/*  88 */     this.manager = (IDeviceManager)Args.requireNonNull(deviceManager, "deviceManager is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStatusMonitor> newToken() {
/*     */     try {
/*  94 */       this.lock.lock();
/*  95 */       return (Observable<IStatusMonitor>)this.tokenCycle;
/*     */     } finally {
/*  97 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final Repository getRepository() {
/*     */     try {
/* 104 */       this.lock.lock();
/* 105 */       return this.manager.getRepository();
/*     */     } finally {
/* 107 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final ReentrantLock lockInstance() {
/* 112 */     return this.lock;
/*     */   }
/*     */   
/*     */   protected final Optional<T> token() {
/* 116 */     return Optional.ofNullable(this.token);
/*     */   }
/*     */   
/*     */   public final IAuthStrategy getAuthStrategy() {
/* 120 */     return this.strategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logout() {
/*     */     try {
/* 126 */       this.lock.lock();
/* 127 */       doLogout();
/*     */     } finally {
/* 129 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/*     */     try {
/* 136 */       this.lock.lock();
/* 137 */       doClose();
/*     */     } finally {
/* 139 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doClose() {
/*     */     try {
/* 145 */       doLogout();
/*     */     } finally {
/* 147 */       this.manager.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doLogout() {
/* 152 */     if (this.token != null) {
/* 153 */       doDispose(this.token);
/* 154 */       this.token = null;
/* 155 */       this.autoForce = true;
/*     */     } 
/* 157 */     if (this.ticketTokenLogout != null) {
/* 158 */       this.ticketTokenLogout.dispose();
/* 159 */       this.ticketTokenLogout = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAuthenticated() {
/*     */     try {
/* 166 */       this.lock.lock();
/* 167 */       return (this.token != null && this.token.isAuthenticated());
/*     */     } finally {
/* 169 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void reset() {
/*     */     try {
/* 176 */       this.lock.lock();
/* 177 */       doClose();
/* 178 */       this.tokenCycle.onComplete();
/* 179 */       this.tokenCycle = BehaviorSubject.create();
/*     */     } finally {
/* 181 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setAuthStrategy(IAuthStrategy strategy) {
/* 187 */     if (strategy != null) {
/* 188 */       doSaveStrategy(this.strategy = strategy);
/*     */     }
/*     */   }
/*     */   
/*     */   private T newToken(DeviceCertificateEntry selectedEntry) {
/* 193 */     T token = createToken(selectedEntry.getNative().getSlot().getToken(), this.lock);
/* 194 */     token.setDefaultCertificate(selectedEntry.getCertificate());
/* 195 */     this.ticketTokenLogout = token.getStatus().subscribe(this::checkStatus);
/* 196 */     this.tokenCycle.onNext(token);
/* 197 */     return token;
/*     */   }
/*     */   
/*     */   private void checkStatus(Boolean online) {
/* 201 */     if (!online.booleanValue()) {
/* 202 */       logout();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void onCertificateAvailable(List<IFilePath> a1List, List<IFilePath> a3List) {
/*     */     try {
/* 209 */       doCertificateAvailable(a1List, a3List);
/*     */     } finally {
/* 211 */       this.autoForce = true;
/* 212 */       doClose();
/* 213 */       this.manager.install(IFilePath.toPaths(a1List));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doCertificateAvailable(List<IFilePath> a1List, List<IFilePath> a3List) {}
/*     */ 
/*     */   
/*     */   private T getToken() throws SwitchRepositoryException {
/* 223 */     if (this.token != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 233 */       if (!this.token.isExpired())
/* 234 */         return this.token; 
/* 235 */       close();
/*     */     } 
/*     */     
/* 238 */     Optional<ICertificateListUI.ICertificateEntry> selected = showCertificates(false, true, true);
/* 239 */     if (!selected.isPresent())
/*     */     {
/* 241 */       Signer4jContext.discard();
/*     */     }
/*     */     
/* 244 */     return this.token = newToken((DeviceCertificateEntry)selected.get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Optional<ICertificateListUI.ICertificateEntry> showCertificates(boolean force, boolean autoSelect, boolean repoWaiting) throws SwitchRepositoryException {
/* 250 */     Supplier<List<ICertificateListUI.ICertificateEntry>> supplier = () -> DeviceCertificateEntry.from(this.manager.getDevices((this.autoForce || force)), getCertificateFilter());
/*     */     try {
/*     */       ICertificateListUI.IChoice choice;
/* 253 */       this.lock.lock();
/*     */       
/*     */       try {
/*     */         do {
/* 257 */           boolean showProgress = (this.autoForce || force || !this.manager.isLoaded());
/*     */ 
/*     */ 
/*     */           
/* 261 */           List<ICertificateListUI.ICertificateEntry> entries = showProgress ? callWithProgress(supplier) : supplier.get();
/*     */           
/* 263 */           choice = CertificateListDialog.display(entries, repoWaiting, autoSelect, this::onCertificateAvailable, this.manager
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 268 */               .getRepository());
/*     */         
/*     */         }
/* 271 */         while (this.autoForce = (choice == ICertificateListUI.IChoice.NEED_RELOAD));
/*     */       }
/* 273 */       catch (SwitchRepositoryException e) {
/* 274 */         this.autoForce = true;
/* 275 */         Signer4jContext.discardQuietly(e::setAvailable);
/* 276 */         throw e;
/*     */       } 
/*     */       
/* 279 */       return (Optional)choice.get();
/*     */     } finally {
/*     */       
/* 282 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private List<ICertificateListUI.ICertificateEntry> callWithProgress(Supplier<List<ICertificateListUI.ICertificateEntry>> supplier) {
/* 288 */     close();
/*     */     
/* 290 */     return (List<ICertificateListUI.ICertificateEntry>)ProgressFactories.withSimple(p -> {
/*     */           try {
/*     */             p.begin("Lendo certificados do dispositivo...");
/*     */ 
/*     */             
/*     */             AtomicBoolean displayDelayed = new AtomicBoolean(true);
/*     */ 
/*     */             
/*     */             p.displayAsync(650L, displayDelayed::get);
/*     */ 
/*     */             
/*     */             List<ICertificateListUI.ICertificateEntry> entries = DriverHealth.CHECKER.<List<ICertificateListUI.ICertificateEntry>>check(supplier, Duration.ofSeconds(60L));
/*     */             
/*     */             displayDelayed.set(false);
/*     */             
/*     */             p.end();
/*     */             
/*     */             return entries;
/* 308 */           } catch (InterruptedException e) {
/*     */             throw new InterruptedOperationException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final T call() throws SwitchRepositoryException {
/*     */     try {
/* 318 */       this.lock.lock();
/* 319 */       Signer4jContext.checkDiscarded();
/*     */       
/* 321 */       int times = 0;
/*     */       while (true) {
/* 323 */         T token = getToken();
/*     */         
/*     */         try {
/* 326 */           return (T)token.login();
/*     */         }
/* 328 */         catch (LoginCanceledException e) {
/* 329 */           Signer4jContext.discard((Throwable)e);
/*     */         }
/* 331 */         catch (NoTokenPresentException e) {
/* 332 */           NoTokenPresentInfo.showInfoOnly();
/* 333 */           this.autoForce = true;
/* 334 */           close();
/* 335 */           Signer4jContext.discard((Throwable)e);
/*     */         }
/* 337 */         catch (TokenLockedException e) {
/* 338 */           TokenLockedAlert.showAndWait();
/* 339 */           Signer4jContext.discard((Throwable)e);
/*     */         }
/* 341 */         catch (ExpiredCredentialException e) {
/* 342 */           ExpiredPasswordAlert.showAndWait();
/* 343 */           Signer4jContext.discard((Throwable)e);
/*     */         }
/* 345 */         catch (InvalidPinException e) {
/* 346 */           if (TokenType.A3.equals(token.getType())) {
/* 347 */             times++;
/*     */           }
/* 349 */           if (InvalidPinAlert.isYes(times)) {
/*     */             continue;
/*     */           }
/* 352 */           Signer4jContext.discard((Throwable)e);
/*     */         }
/* 354 */         catch (Signer4JException e) {
/* 355 */           Signer4jContext.discard((Throwable)e);
/*     */         }
/*     */       
/*     */       } 
/*     */     } finally {
/*     */       
/* 361 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void doDispose(T paramT);
/*     */   
/*     */   protected abstract T createToken(IToken paramIToken, ReentrantLock paramReentrantLock);
/*     */   
/*     */   protected abstract void doSaveStrategy(IAuthStrategy paramIAuthStrategy);
/*     */   
/*     */   protected abstract Predicate<ICertificate> getCertificateFilter();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/TokenAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */