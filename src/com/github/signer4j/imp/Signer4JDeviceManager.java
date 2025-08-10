/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.IDeviceAccessor;
/*     */ import com.github.signer4j.IDeviceManager;
/*     */ import com.github.signer4j.IDriverLookupStrategy;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ class Signer4JDeviceManager
/*     */   implements IDeviceManager
/*     */ {
/*     */   private final IDeviceAccessor pkcs11;
/*     */   private final IDeviceManager pkcs12;
/*     */   
/*     */   Signer4JDeviceManager(IDeviceAccessor pkcs11, IDeviceManager pkcs12) {
/*  52 */     this.pkcs11 = (IDeviceAccessor)Args.requireNonNull(pkcs11, "pkcs11 is null");
/*  53 */     this.pkcs12 = (IDeviceManager)Args.requireNonNull(pkcs12, "pkcs12 is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final Repository getRepository() {
/*  58 */     return Repository.NATIVE;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isLoaded() {
/*  63 */     return (this.pkcs11.isLoaded() && this.pkcs12.isLoaded());
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setStrategy(IDriverLookupStrategy strategy) {
/*  68 */     this.pkcs11.setStrategy(strategy);
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices() {
/*  73 */     return merge(this.pkcs11.getDevices(), this.pkcs12.getDevices());
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices(boolean forceReload) {
/*  78 */     return merge(this.pkcs11.getDevices(forceReload), this.pkcs12.getDevices(forceReload));
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices(Predicate<IDevice> predicate) {
/*  83 */     Args.requireNonNull(predicate, "predicate is null");
/*  84 */     return merge(this.pkcs11.getDevices(predicate), this.pkcs12.getDevices(predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices(Predicate<IDevice> predicate, boolean forceReload) {
/*  89 */     Args.requireNonNull(predicate, "predicate is null");
/*  90 */     return merge(this.pkcs11.getDevices(predicate, forceReload), this.pkcs12.getDevices(predicate, forceReload));
/*     */   }
/*     */ 
/*     */   
/*     */   public final IDeviceManager install(Path... pkcs12Files) {
/*  95 */     this.pkcs12.install(pkcs12Files);
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IDeviceManager uninstall(Path... pkcs12File) {
/* 101 */     this.pkcs12.uninstall(pkcs12File);
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IDeviceManager uninstallPkcs12() {
/* 107 */     this.pkcs12.uninstallPkcs12();
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/* 113 */     Throwables.quietly(this.pkcs11::close);
/* 114 */     Throwables.quietly(this.pkcs12::close);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice() {
/* 119 */     Optional<IDevice> dev = this.pkcs11.firstDevice();
/* 120 */     if (dev.isPresent())
/* 121 */       return dev; 
/* 122 */     return this.pkcs12.firstDevice();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice(boolean forceReload) {
/* 127 */     Optional<IDevice> dev = this.pkcs11.firstDevice(forceReload);
/* 128 */     if (dev.isPresent())
/* 129 */       return dev; 
/* 130 */     return this.pkcs12.firstDevice(forceReload);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice(Predicate<IDevice> predicate) {
/* 135 */     Args.requireNonNull(predicate, "predicate is null");
/* 136 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice(Predicate<IDevice> predicate, boolean forceReload) {
/* 141 */     Args.requireNonNull(predicate, "predicate is null");
/* 142 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static List<IDevice> merge(List<IDevice> pkcs11List, List<IDevice> pkcs12List) {
/* 146 */     List<IDevice> output = new ArrayList<>(pkcs11List.size() + pkcs12List.size());
/* 147 */     output.addAll(pkcs11List);
/* 148 */     output.addAll(pkcs12List);
/* 149 */     return Collections.unmodifiableList(output);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Signer4JDeviceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */