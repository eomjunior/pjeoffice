/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.ILibraryAware;
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import com.github.signer4j.exception.DriverFailException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.StopWatch;
/*     */ import com.github.utils4j.imp.Streams;
/*     */ import java.beans.Transient;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
/*     */ import sun.security.pkcs11.wrapper.PKCS11;
/*     */ import sun.security.pkcs11.wrapper.PKCS11Exception;
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
/*     */ class PKCS11Driver
/*     */   extends AbstractDriver
/*     */   implements ILibraryAware
/*     */ {
/*     */   private PKCS11 pk;
/*     */   private final String library;
/*     */   
/*     */   PKCS11Driver(Path library) {
/*  55 */     this
/*     */ 
/*     */       
/*  58 */       .library = ((Path)Args.requireNonNull(library, "null library")).toFile().getAbsolutePath().replace('\\', '/');
/*     */   }
/*     */   
/*     */   @Transient
/*     */   final PKCS11 getPK() {
/*  63 */     return this.pk;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getId() {
/*  68 */     return getLibrary();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getLibrary() {
/*  73 */     return this.library;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean isSame(AbstractDriver obj) {
/*  78 */     return Streams.isSame(this.library, ((PKCS11Driver)obj).library);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadSlots(List<ISlot> output) throws DriverException {
/*  84 */     StopWatch sw = new StopWatch(LOGGER);
/*     */     
/*  86 */     sw.start();
/*  87 */     DriverHealth.CHECKER.check(this.library);
/*  88 */     sw.stop("Driver health checking time for " + this.library);
/*     */     
/*  90 */     sw.start();
/*  91 */     CK_C_INITIALIZE_ARGS initArgs = new CK_C_INITIALIZE_ARGS();
/*     */     try {
/*  93 */       this.pk = PKCS11.getInstance(this.library, "C_GetFunctionList", initArgs, false);
/*  94 */     } catch (IOException|PKCS11Exception e) {
/*  95 */       throw new DriverFailException("Unabled to create PKCS11 instance", e);
/*     */     } finally {
/*  97 */       sw.stop("Initialization library time");
/*     */     } 
/*     */     
/* 100 */     long[] slots = new long[0];
/*     */     
/* 102 */     sw.start();
/*     */     try {
/* 104 */       slots = this.pk.C_GetSlotList(true);
/* 105 */     } catch (PKCS11Exception e) {
/* 106 */       throw new DriverFailException("Unabled to list slot from driver: " + this, e);
/*     */     } finally {
/* 108 */       sw.stop("Slot list loading time");
/*     */     } 
/*     */     
/* 111 */     for (long slot : slots) {
/* 112 */       sw.start();
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
/* 124 */     LOGGER.info("Total loading time: " + sw.getTime() + "ms");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11Driver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */