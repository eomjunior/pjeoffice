/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import com.github.signer4j.gui.utils.InvalidPinAlert;
/*     */ import com.github.signer4j.imp.exception.InvalidPinException;
/*     */ import com.github.signer4j.imp.exception.LoginCanceledException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import com.github.utils4j.imp.Streams;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ class PKCS12Driver
/*     */   extends AbstractDriver
/*     */ {
/*  48 */   private static final PKCS12Driver INSTANCE = new PKCS12Driver();
/*     */   
/*     */   public static final PKCS12Driver getInstance() {
/*  51 */     return INSTANCE;
/*     */   }
/*     */   
/*  54 */   private final List<Path> certPaths = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getId() {
/*  60 */     return PKCS12Driver.class.getSimpleName();
/*     */   }
/*     */   
/*     */   final void uninstall() {
/*  64 */     this.certPaths.clear();
/*  65 */     unload();
/*     */   }
/*     */   
/*     */   final void close() {
/*  69 */     uninstall();
/*  70 */     Safe.BOX.clear();
/*     */   }
/*     */   
/*     */   final boolean install(Path... paths) {
/*  74 */     boolean refreshed = false;
/*  75 */     if (!Containers.isEmpty((Object[])paths)) {
/*  76 */       for (Path path : paths) {
/*  77 */         if (path != null)
/*     */         {
/*  79 */           if (!this.certPaths.stream().anyMatch(p -> Streams.isSame(p, path))) {
/*  80 */             this.certPaths.add(path);
/*  81 */             refreshed = true;
/*     */           }  } 
/*     */       } 
/*  84 */       if (isLoaded()) {
/*  85 */         reload();
/*     */       }
/*     */     } 
/*  88 */     return refreshed;
/*     */   }
/*     */   
/*     */   final boolean uninstall(Path... paths) {
/*  92 */     boolean refreshed = false;
/*  93 */     if (!Containers.isEmpty((Object[])paths)) {
/*  94 */       for (Path path : paths) {
/*  95 */         if (path != null)
/*     */         {
/*  97 */           if (this.certPaths.removeIf(p -> Streams.isSame(p, path)))
/*  98 */             refreshed = true; 
/*     */         }
/*     */       } 
/* 101 */       if (isLoaded()) {
/* 102 */         reload();
/*     */       }
/*     */     } 
/* 105 */     return refreshed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadSlots(List<ISlot> output) throws DriverException {
/* 110 */     this.certPaths.stream().forEach(path -> {
/*     */           try {
/*     */             ISlot slot = new PKCS12Slot(path);
/*     */             String key = slot.getSerial();
/*     */             IToken token = slot.getToken();
/*     */             while (true) {
/*     */               char[] password = Safe.BOX.get(key);
/*     */               try {
/*     */                 LOGGER.info("Logando no DRIVER");
/*     */                 if (password != null) {
/*     */                   LOGGER.info("Logando em modo LITERAL");
/*     */                   token.login(password);
/*     */                 } else {
/*     */                   LOGGER.info("Logando em modo DIALOG");
/*     */                   AtomicReference<char[]> pass = (AtomicReference)new AtomicReference<>();
/*     */                   token.login(pass::set, Boolean.valueOf(true));
/*     */                   password = pass.get();
/*     */                 } 
/*     */                 LOGGER.info("Logado com sucesso: " + token.isAuthenticated());
/*     */                 token.logout();
/*     */                 LOGGER.info("Deslogado - Leitura de certificado OK");
/*     */                 Safe.BOX.put(key, password);
/*     */                 output.add(slot);
/*     */                 addDevice(slot.toDevice());
/*     */                 break;
/* 135 */               } catch (LoginCanceledException e) {
/*     */                 break;
/* 137 */               } catch (InvalidPinException e) {
/*     */                 Safe.BOX.remove(key);
/*     */                 if (InvalidPinAlert.isNo(1))
/*     */                   break; 
/* 141 */               } catch (Signer4JException e) {
/*     */                 LOGGER.error("Falha na tentativa de autenticação em PKCS12 Driver: " + path.toString(), (Throwable)e);
/*     */                 break;
/*     */               } 
/*     */             } 
/* 146 */           } catch (DriverException e) {
/*     */             LOGGER.warn("Unabled to loadSlots gracefully", (Throwable)e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS12Driver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */