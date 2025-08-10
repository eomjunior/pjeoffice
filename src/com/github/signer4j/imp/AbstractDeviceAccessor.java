/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.IDeviceAccessor;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ abstract class AbstractDeviceAccessor
/*     */   extends LoadCycle
/*     */   implements IDeviceAccessor
/*     */ {
/*  49 */   private List<IDriver> drivers = Collections.emptyList();
/*     */ 
/*     */ 
/*     */   
/*     */   private List<IDevice> cache;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void close() {
/*  58 */     unload();
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices() {
/*  63 */     return getDevices(!isLoaded());
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices(boolean forceReload) {
/*  68 */     boolean doForce = (forceReload || Containers.isEmpty(this.cache));
/*  69 */     if (doForce) {
/*  70 */       reload();
/*  71 */       this.cache = null;
/*     */     } 
/*  73 */     if (this.cache == null) {
/*  74 */       this.cache = Containers.toUnmodifiableList((Set)this.drivers.stream().flatMap(d -> d.getDevices().stream()).collect(Collectors.toSet()));
/*     */     }
/*  76 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices(Predicate<IDevice> predicate) {
/*  81 */     return (List<IDevice>)getDevices().stream().filter((Predicate)Args.requireNonNull(predicate, "predicate is null")).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices(Predicate<IDevice> predicate, boolean forceReload) {
/*  86 */     return (List<IDevice>)getDevices(forceReload).stream().filter((Predicate)Args.requireNonNull(predicate, "predicate is null")).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice() {
/*  91 */     return getDevices().stream().findFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice(Predicate<IDevice> predicate) {
/*  96 */     return getDevices().stream().filter(predicate).findFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice(boolean forceReload) {
/* 101 */     return getDevices(forceReload).stream().findFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IDevice> firstDevice(Predicate<IDevice> predicate, boolean forceReload) {
/* 106 */     return getDevices(forceReload).stream().filter(predicate).findFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void doUnload() throws Exception {
/* 111 */     this.drivers.forEach(ILoadCycle::unload);
/* 112 */     this.drivers = Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void doLoad() throws Exception {
/* 117 */     Set<IDriver> drivers = new HashSet<>();
/* 118 */     load(drivers);
/* 119 */     this.drivers = Containers.toUnmodifiableList(drivers);
/*     */   }
/*     */   
/*     */   protected abstract void load(Set<IDriver> paramSet);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AbstractDeviceAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */