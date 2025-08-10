/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.config.imp.Global;
/*     */ import br.jus.cnj.pje.office.core.IPjeCommandFactory;
/*     */ import br.jus.cnj.pje.office.core.IPjeLifeCycle;
/*     */ import br.jus.cnj.pje.office.core.IPjeLifeCycleHook;
/*     */ import br.jus.cnj.pje.office.core.IPjeOffice;
/*     */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*     */ import br.jus.cnj.pje.office.gui.servetlist.PjeServerList;
/*     */ import br.jus.cnj.pje.office.signer4j.imp.PjeTokenDevice;
/*     */ import br.jus.cnj.pje.office.updater.imp.Updater;
/*     */ import com.github.signer4j.IAuthStrategy;
/*     */ import com.github.signer4j.IAuthStrategyAware;
/*     */ import com.github.signer4j.IStatusMonitor;
/*     */ import com.github.signer4j.IWindowLockDettector;
/*     */ import com.github.signer4j.IWorkstationLockListener;
/*     */ import com.github.signer4j.gui.PasswordStrategyDialogCallbackHandler;
/*     */ import com.github.signer4j.imp.AuthStrategy;
/*     */ import com.github.signer4j.imp.DriverHealth;
/*     */ import com.github.signer4j.imp.Repository;
/*     */ import com.github.signer4j.imp.Signer4jContext;
/*     */ import com.github.signer4j.imp.WindowLockDettector;
/*     */ import com.github.utils4j.gui.imp.DelayedFileChooser;
/*     */ import com.github.utils4j.gui.imp.MessageAlert;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Browser;
/*     */ import com.github.utils4j.imp.Media;
/*     */ import com.github.utils4j.imp.States;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
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
/*     */ public class PjeOffice
/*     */   implements IWorkstationLockListener, IPjeOffice
/*     */ {
/*  78 */   private static final Logger LOGGER = LoggerFactory.getLogger(PjeOffice.class);
/*     */   
/*     */   private Disposable ticket;
/*     */   
/*     */   private final String model;
/*     */   
/*     */   private IPjeLifeCycle commander;
/*     */   
/*     */   private IPjeLifeCycleHook lifeCycle;
/*     */   
/*     */   private IWindowLockDettector dettector;
/*     */   
/*     */   private Consumer<Boolean> stateChanging;
/*     */   
/*     */   private final DelayedFileChooser chooser;
/*     */   
/*     */   private final IPjeCommandFactory factory;
/*     */   
/*     */   public PjeOffice(IPjeLifeCycleHook lifeCycle, IPjeCommandFactory factory, String model, Consumer<Boolean> stateChanging) {
/*  97 */     this(lifeCycle, factory, model, stateChanging, WindowLockDettector.getBest());
/*     */   }
/*     */   
/*     */   private PjeOffice(IPjeLifeCycleHook lifeCycle, IPjeCommandFactory factory, String model, Consumer<Boolean> stateChanging, IWindowLockDettector dettector) {
/* 101 */     this.lifeCycle = (IPjeLifeCycleHook)Args.requireNonNull(lifeCycle, "hook is null");
/* 102 */     this.factory = (IPjeCommandFactory)Args.requireNonNull(factory, "factory is null");
/* 103 */     this.dettector = ((IWindowLockDettector)Args.requireNonNull(dettector, "dettector is null")).notifyTo(this);
/* 104 */     this.model = Args.requireText(model, "model is empty");
/* 105 */     this.stateChanging = Optional.<Consumer<Boolean>>ofNullable(stateChanging).orElse(b -> { 
/* 106 */         }); this.chooser = DelayedFileChooser.DIALOG;
/* 107 */     PasswordStrategyDialogCallbackHandler.register((IAuthStrategyAware)this);
/*     */   }
/*     */   
/*     */   private void checkIsAlive() throws IllegalStateException {
/* 111 */     States.requireTrue((this.lifeCycle != null), "PjeOffice was killed");
/*     */   }
/*     */ 
/*     */   
/*     */   public final void boot() {
/* 116 */     checkIsAlive();
/* 117 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStatusMonitor> newToken() {
/* 122 */     checkIsAlive();
/* 123 */     return PjeTokenDevice.ACCESSOR.newToken();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<Repository> newRepository() {
/* 128 */     checkIsAlive();
/* 129 */     return PjeTokenDevice.ACCESSOR.newRepository();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showAuthorizedServers() {
/* 134 */     checkIsAlive();
/* 135 */     PjeServerList.ACCESSOR.show();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAwayStrategy() {
/* 140 */     checkIsAlive();
/* 141 */     return (AuthStrategy.AWAYS == PjeTokenDevice.ACCESSOR.getAuthStrategy());
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getStrategyLabel() {
/* 146 */     checkIsAlive();
/* 147 */     return PjeTokenDevice.ACCESSOR.getAuthStrategy().getStrategyLabel();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isOneTimeStrategy() {
/* 152 */     checkIsAlive();
/* 153 */     return (AuthStrategy.ONE_TIME == PjeTokenDevice.ACCESSOR.getAuthStrategy());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isConfirmStrategy() {
/* 158 */     checkIsAlive();
/* 159 */     return (AuthStrategy.CONFIRM == PjeTokenDevice.ACCESSOR.getAuthStrategy());
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showAbout() {
/* 164 */     checkIsAlive();
/* 165 */     this.commander.showAbout();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showHelp() {
/* 170 */     checkIsAlive();
/* 171 */     Browser.navigateTo(Global.CONFIG.help_url(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setDevMode(boolean devMode) {
/* 176 */     checkIsAlive();
/* 177 */     this.commander.setDevmode(PjeSecurity.GRANTOR.setDevMode(devMode));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setUnsafe(boolean unsafe) {
/* 182 */     checkIsAlive();
/* 183 */     PjeSecurity.GRANTOR.setUnsafe(unsafe);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String model() {
/* 188 */     return this.model;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isUnsafe() {
/* 194 */     checkIsAlive();
/* 195 */     return PjeSecurity.GRANTOR.isUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isDevMode() {
/* 200 */     checkIsAlive();
/* 201 */     return PjeSecurity.GRANTOR.isDevMode();
/*     */   }
/*     */   
/*     */   private void reset() {
/* 205 */     stopCommander();
/* 206 */     startCommander();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showCertificates() {
/* 211 */     checkIsAlive();
/* 212 */     checkAndRun(false, PjeTokenDevice.ACCESSOR::showCertificatesAsync);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setAuthStrategy(AuthStrategy strategy, boolean force) {
/* 217 */     checkIsAlive();
/* 218 */     checkAndRun(force, () -> PjeTokenDevice.ACCESSOR.setAuthStrategy((IAuthStrategy)strategy));
/*     */   }
/*     */   
/*     */   private void checkAndRun(boolean force, Runnable runnable) {
/* 222 */     if (!force && this.commander.isProcessing()) {
/* 223 */       MessageAlert.showInfo("Aguarde ou cancele as operações em andamento!");
/*     */     } else {
/* 225 */       runnable.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void onCommanderStart() {
/* 230 */     LOGGER.info("Commander iniciado e pronto para receber requisições.");
/* 231 */     this.dettector.start();
/* 232 */     PjeSecurity.GRANTOR.refresh();
/* 233 */     PjeClientProtocol.open();
/* 234 */     this.lifeCycle.onStartup();
/*     */   }
/*     */   
/*     */   protected final void onCommanderStop() {
/* 238 */     LOGGER.info("Commander parado. Requisições indisponíveis");
/* 239 */     PjeClientProtocol.evict();
/* 240 */     this.lifeCycle.onShutdown();
/*     */   }
/*     */   
/*     */   protected final void onCommanderKill() {
/* 244 */     LOGGER.info("Killing PjeOffice");
/* 245 */     this.dettector.stop();
/* 246 */     LOGGER.info("Fechado detector de logout");
/* 247 */     this.lifeCycle.onKill();
/* 248 */     this.lifeCycle = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onMachineLocked(int value) {
/* 253 */     checkIsAlive();
/* 254 */     LOGGER.info("Máquina bloqueada pelo usuário");
/* 255 */     stopCommander();
/* 256 */     PjeTokenDevice.ACCESSOR.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onMachineUnlocked(int value) {
/* 261 */     checkIsAlive();
/* 262 */     LOGGER.info("Máquina desbloqueada pelo usuário");
/* 263 */     startCommander();
/*     */   }
/*     */   
/*     */   private void startCommander() {
/* 267 */     if (this.commander == null) {
/* 268 */       this.commander = this.factory.create((IBootable)this);
/* 269 */       this.ticket = this.commander.lifeCycle().subscribe(cycle -> {
/*     */             switch (cycle) {
/*     */               case STARTUP:
/*     */                 onCommanderStart(); break;
/*     */               case SHUTDOWN:
/*     */                 onCommanderStop();
/*     */                 break;
/*     */               case KILL:
/*     */                 onCommanderKill();
/*     */                 break;
/*     */             } 
/*     */           });
/*     */       try {
/* 282 */         this.commander.start();
/* 283 */       } catch (IOException e) {
/* 284 */         LOGGER.warn("Não foi possível iniciar o servidor web", e);
/* 285 */         this.lifeCycle.onFailStart(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopCommander() {
/* 291 */     stopCommander(false);
/*     */   }
/*     */   
/*     */   private void stopCommander(boolean kill) {
/* 295 */     if (this.commander != null) {
/*     */       try {
/* 297 */         this.commander.stop(kill);
/* 298 */       } catch (IOException e) {
/* 299 */         LOGGER.warn("Não foi possível parar o servidor em close", e);
/* 300 */         this.lifeCycle.onFailShutdown(e);
/*     */       } finally {
/* 302 */         this.commander = null;
/* 303 */         if (this.ticket != null) {
/* 304 */           this.ticket.dispose();
/* 305 */           this.ticket = null;
/*     */         } 
/*     */       } 
/* 308 */     } else if (kill) {
/* 309 */       onCommanderKill();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void kill() {
/* 315 */     checkIsAlive();
/* 316 */     stopCommander(true);
/* 317 */     this.lifeCycle = null;
/* 318 */     this.dettector = null;
/* 319 */     this.ticket = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void exit(long delay) {
/* 324 */     checkIsAlive();
/* 325 */     Runnable action = () -> {
/*     */         Threads.sleep(delay);
/*     */         kill();
/*     */         PjeClientProtocol.closeAll();
/*     */         DriverHealth.CHECKER.dispose();
/*     */         LOGGER.info("Fechadas instâncias protocol e suas dependências");
/*     */         LOGGER.info("Game over! Bye bye!");
/*     */         Runtime.getRuntime().halt(0);
/*     */       };
/* 334 */     if (Threads.isShutdownHook()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 340 */       LOGGER.info("Pedido de finalização via ShutdownHook");
/* 341 */       action.run();
/*     */       return;
/*     */     } 
/* 344 */     SwingTools.invokeLater(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logout() {
/* 349 */     checkIsAlive();
/* 350 */     Threads.startDaemon("logout", () -> {
/*     */           try {
/*     */             Signer4jContext.discardAndWaitIf(this.commander::isProcessing);
/*     */             PjeTokenDevice.ACCESSOR.logout();
/* 354 */           } catch (InterruptedException e) {
/*     */             LOGGER.warn("Logout interrompido", e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public final void update(boolean silent) {
/* 362 */     Updater.INSTANCE.update(this::exit, silent);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void selectTo(String title, Media media, Function<List<File>, List<File>> function) {
/* 367 */     Args.requireNonNull(title, "title is null");
/* 368 */     Args.requireNonNull(media, "media is null");
/* 369 */     Args.requireNonNull(function, "consumer is null");
/* 370 */     checkIsAlive();
/* 371 */     this.chooser.filesOnly().multiSelect(title, media).map(Arrays::asList).<List<File>>map(function).ifPresent(this.commander::dispatch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stateChanging(boolean closing) {
/* 376 */     this.stateChanging.accept(Boolean.valueOf(closing));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeOffice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */