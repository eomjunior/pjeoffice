/*     */ package br.jus.cnj.pje.office.updater.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.config.imp.Global;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeVersion;
/*     */ import br.jus.cnj.pje.office.updater.IPackage;
/*     */ import br.jus.cnj.pje.office.updater.IStatusChecking;
/*     */ import br.jus.cnj.pje.office.updater.IUpdateStatus;
/*     */ import br.jus.cnj.pje.office.updater.IUpdater;
/*     */ import br.jus.cnj.pje.office.updater.IVersionChecker;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.imp.ProgressFactories;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.ExceptionAlert;
/*     */ import com.github.utils4j.gui.imp.Silencer;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Environment;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Updater
/*     */   implements IUpdater
/*     */ {
/*  40 */   INSTANCE; private Optional<Thread> nextChecking; private final ReentrantLock runningLock;
/*     */   private final Logger logger;
/*     */   private static long UPDATE_CHECKING_TIME_INTERVAL;
/*     */   private static final String DEFAULT_MESSAGE = "Você pode desinstalar manualmente a versão atual, baixar a versão mais recente disponível e reinstalar o assinador.";
/*     */   
/*     */   Updater() {
/*  46 */     this.logger = LoggerFactory.getLogger(Updater.class);
/*     */     
/*  48 */     this.runningLock = new ReentrantLock();
/*     */     
/*  50 */     this.nextChecking = Optional.empty();
/*     */   }
/*     */   
/*     */   public final boolean isDisabled() {
/*  54 */     return "disabled".equalsIgnoreCase(Global.CONFIG.update_url());
/*     */   } static {
/*     */     UPDATE_CHECKING_TIME_INTERVAL = 604800000L;
/*     */   } private void prepareNextChecking(IUpdateStatus upStatus) {
/*  58 */     this.nextChecking.ifPresent(Thread::interrupt);
/*  59 */     this.nextChecking = Optional.of(Threads.startDaemon("atualizador", () -> update(upStatus, true), UPDATE_CHECKING_TIME_INTERVAL, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void update(IUpdateStatus upStatus, boolean silent) {
/*  64 */     Args.requireNonNull(upStatus, "status is null");
/*  65 */     if (!isDisabled() && !Thread.currentThread().isInterrupted()) {
/*  66 */       withLock(() -> Threads.startDaemon("atualizador", ()));
/*  67 */       prepareNextChecking(upStatus);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void withLock(Runnable code) {
/*  72 */     if (this.runningLock.tryLock()) {
/*     */       try {
/*  74 */         code.run();
/*     */       } finally {
/*  76 */         this.runningLock.unlock();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkWritingPermission() {
/*  83 */     Path pjeofficeHome = (Path)Environment.requirePathFrom("pjeoffice_home").orElseThrow(() -> new RuntimeException("Não foi encontrada variável de ambiente pjeoffice_home"));
/*     */     try {
/*  85 */       File test = pjeofficeHome.resolve("write.test").toFile();
/*  86 */       if (test.exists() && !test.delete()) {
/*  87 */         throw new IOException("O arquivo " + Directory.stringPath(test) + " não pôde ser apagado!");
/*     */       }
/*  89 */       Files.createFile(test.toPath(), (FileAttribute<?>[])new FileAttribute[0]);
/*  90 */       if (!test.exists()) {
/*  91 */         this.logger.warn("Não há permissão de escrita em: " + pjeofficeHome);
/*  92 */         return false;
/*     */       } 
/*  94 */       test.delete();
/*  95 */       return true;
/*  96 */     } catch (IOException e) {
/*  97 */       this.logger.warn("Não há permissão de escrita em: " + pjeofficeHome, e);
/*  98 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void downloadAndRun(IUpdateStatus upStatus, IProvider<IPackage> downloader) {
/*     */     try {
/*     */       IPackage pack;
/*     */       try {
/* 106 */         pack = (IPackage)downloader.get();
/* 107 */       } catch (InterruptedException e) {
/* 108 */         throw e;
/* 109 */       } catch (Exception e) {
/* 110 */         String message = "Não foi possível baixar o atualizador!";
/* 111 */         this.logger.warn(message, e);
/* 112 */         ExceptionAlert.show(message, "Você pode desinstalar manualmente a versão atual, baixar a versão mais recente disponível e reinstalar o assinador.", e);
/*     */         
/*     */         return;
/*     */       } 
/*     */       try {
/* 117 */         pack.apply();
/* 118 */       } catch (InterruptedException e) {
/* 119 */         throw e;
/* 120 */       } catch (Exception e) {
/* 121 */         String message = "Não foi possível iniciar execução do atualizador!";
/* 122 */         this.logger.warn(message, e);
/* 123 */         ExceptionAlert.show(message, "Você pode desinstalar manualmente a versão atual, baixar a versão mais recente disponível e reinstalar o assinador.", e);
/*     */         return;
/*     */       } 
/* 126 */     } catch (InterruptedException e) {
/* 127 */       String message = "Atualização cancelada!";
/* 128 */       this.logger.warn(message, e);
/* 129 */       ExceptionAlert.show(message, "Você pode desinstalar manualmente a versão atual, baixar a versão mais recente disponível e reinstalar o assinador.", e);
/*     */       
/*     */       return;
/*     */     } 
/* 133 */     Threads.startDaemon("closing app", upStatus::onUpdaterRunning, 500L);
/*     */   }
/*     */   
/*     */   private void doUpdate(IUpdateStatus upStatus, boolean silent) {
/*     */     String rootUri;
/*     */     try {
/* 139 */       rootUri = (new URL((String)Strings.optional(Global.CONFIG.update_url()).orElseThrow(() -> new Exception("configuração 'update.url' é inválida")))).toString();
/* 140 */     } catch (AccessDeniedException e) {
/* 141 */       Silencer.failAs(silent, "É necessário execução como 'administrador' para concluir a atualização!", e);
/*     */       return;
/* 143 */     } catch (Exception e) {
/* 144 */       Silencer.failAs(silent, "Sua versão do PJeOffice encontra-se corrompida e isto impede sua atualização!", e, "Você pode desinstalar manualmente a versão atual, baixar a versão mais recente disponível e reinstalar o assinador.");
/*     */       
/*     */       return;
/*     */     } 
/* 148 */     ProgressFactories.withBox(progress -> {
/*     */           String message;
/*     */           Dialogs.Choice choice;
/*     */           if (!silent)
/*     */             progress.display(); 
/*     */           IVersionChecker checker = new HttpVersionChecker(rootUri);
/*     */           IStatusChecking checking = checker.check(progress, silent);
/*     */           switch (checking.getStatus()) {
/*     */             case UNDEFINED:
/*     */               this.logger.info("Existência de nova versão é inconclusiva");
/*     */               break;
/*     */             case UPDATED:
/*     */               this.logger.info("Versão atual é atualizada: " + PjeVersion.CURRENT + ". Checagem em: " + rootUri);
/*     */               Silencer.infoAs(silent, "Sua versão " + PjeVersion.CURRENT + " já é a mais recente!");
/*     */               break;
/*     */             case OUTDATED:
/*     */               message = "uma nova versão do PJeOffice disponível";
/*     */               if (!checkWritingPermission()) {
/*     */                 message = "Para conhecimento, há " + message + "&nbsp; e&nbsp; poderá<br>ser obtida&nbsp; executando o assinador como 'administrador' para concluir sua<br> atualização!";
/*     */                 AdminAlert.showMessage(message);
/*     */                 return;
/*     */               } 
/*     */               message = "Há " + message + ". Deseja atualizar agora?";
/*     */               choice = Dialogs.getBoolean(message, "Atualizações de software", false);
/*     */               if (Dialogs.Choice.YES.equals(choice)) {
/*     */                 progress.display();
/*     */                 downloadAndRun(upStatus, ());
/*     */               } 
/*     */               break;
/*     */           } 
/*     */         }e -> Silencer.failAs(silent, "Falha na execução da atualização", e, "Você pode desinstalar manualmente a versão atual, baixar a versão mais recente disponível e reinstalar o assinador."));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/imp/Updater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */