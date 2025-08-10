/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.exception.DriverHealthCheckingException;
/*     */ import com.github.signer4j.gui.alert.DriverHealthAlert;
/*     */ import com.github.utils4j.IDisposable;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import com.github.utils4j.imp.DaemonThreadFactory;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Environment;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import com.github.utils4j.imp.Services;
/*     */ import com.github.utils4j.imp.States;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Supplier;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public enum DriverHealth
/*     */   implements IDisposable
/*     */ {
/*  32 */   CHECKER; private final ExecutorService waiter;
/*     */   private final AtomicBoolean disposed;
/*     */   private static final Logger LOGGER;
/*     */   
/*  36 */   DriverHealth() { this.disposed = new AtomicBoolean(false);
/*     */     
/*  38 */     this.waiter = Executors.newCachedThreadPool((ThreadFactory)new DaemonThreadFactory("driver health monitor")); }
/*     */   
/*     */   final <T> T check(Supplier<T> task, Duration timeout) {
/*  41 */     States.requireFalse(this.disposed.get(), "Driver health already disposed for async call");
/*     */     
/*     */     try {
/*  44 */       return this.waiter.<T>submit(task::get).get(timeout.getSeconds(), TimeUnit.SECONDS);
/*     */     }
/*  46 */     catch (InterruptedException|java.util.concurrent.ExecutionException e) {
/*  47 */       LOGGER.warn("Tentativa de leitura de dados cancelada", e);
/*  48 */       Signer4jContext.discard(e);
/*     */     }
/*  50 */     catch (TimeoutException e) {
/*  51 */       LOGGER.warn("Timeout durante tentativa de leitura de dados", e);
/*  52 */       DriverHealthAlert.showInfo("O driver do seu dispositivo (token/smartcard) não responde.\nRevise a instalação do mesmo e/ou reinicie o seu computador!");
/*     */ 
/*     */ 
/*     */       
/*  56 */       Signer4jContext.discard(e);
/*     */     } 
/*     */     
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   final void check(String libraryPath) throws DriverHealthCheckingException {
/*  64 */     if (!Jvms.isMac()) {
/*     */       return;
/*     */     }
/*  67 */     File lib = new File(libraryPath);
/*  68 */     if (!lib.exists()) {
/*  69 */       throw new DriverHealthCheckingException(lib, "Biblioteca não existe");
/*     */     }
/*     */     
/*  72 */     Optional<Path> javaHomeOpt = Environment.requirePathFrom("java.home");
/*  73 */     if (!javaHomeOpt.isPresent())
/*     */     {
/*  75 */       throw new DriverHealthCheckingException(lib, "Sua instalação aparentemente corrompida porque não foi encontrada propriedade java.home");
/*     */     }
/*     */     
/*  78 */     Path javaHome = javaHomeOpt.get();
/*     */     
/*  80 */     Path bin = javaHome.resolve("bin");
/*     */     
/*  82 */     File jvm = bin.resolve("javaw.exe").toFile();
/*     */     
/*  84 */     if (!jvm.exists()) {
/*  85 */       jvm = bin.resolve("java").toFile();
/*  86 */       if (!jvm.exists()) {
/*  87 */         throw new DriverHealthCheckingException(lib, "Sua instalação aparentemente corrompida. Não foi encontrado: " + Directory.stringPath(jvm));
/*     */       }
/*     */     } 
/*     */     
/*  91 */     File pkcs11Checker = javaHome.resolve("lib").resolve("ext").resolve("pkcs11-health-checker.jar").toFile();
/*     */     
/*  93 */     if (!pkcs11Checker.exists()) {
/*  94 */       throw new DriverHealthCheckingException(lib, "Sua instalação aparentemente corrompida. Não foi encontrado: " + Directory.stringPath(pkcs11Checker));
/*     */     }
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
/*     */     try {
/* 107 */       boolean checkingStatus = ((new ProcessBuilder(Containers.arrayList((Object[])new String[] { Directory.stringPath(jvm), "-jar", Directory.stringPath(pkcs11Checker), libraryPath }))).directory(bin.toFile()).start().waitFor() == 0);
/*     */       
/* 109 */       if (checkingStatus) {
/* 110 */         LOGGER.info("healthy driver: " + libraryPath);
/*     */         
/*     */         return;
/*     */       } 
/* 114 */       throw new DriverHealthCheckingException(lib, "A carga da biblioteca provocará o crash do aplicativo. Driver rejeitado!");
/*     */     }
/* 116 */     catch (IOException|InterruptedException e) {
/* 117 */       throw new DriverHealthCheckingException(lib, "Não foi possível atestar a saúde do driver", e);
/*     */     } 
/*     */   } static {
/*     */     LOGGER = LoggerFactory.getLogger(DriverHealth.class);
/*     */   }
/*     */   public void dispose() {
/* 123 */     if (!this.disposed.getAndSet(true))
/* 124 */       Services.shutdownNow(this.waiter, 3L, 1); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/DriverHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */