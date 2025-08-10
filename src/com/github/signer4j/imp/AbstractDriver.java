/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ abstract class AbstractDriver
/*     */   extends LoadCycle
/*     */   implements IDriver
/*     */ {
/*  47 */   private List<ISlot> slots = Collections.emptyList();
/*     */   
/*  49 */   private final List<IDevice> devices = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  56 */     return getId();
/*     */   }
/*     */   
/*     */   protected boolean isSame(AbstractDriver obj) {
/*  60 */     return getId().equals(obj.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<IToken> firstToken() {
/*  65 */     return (this.slots.size() == 0) ? Optional.<IToken>empty() : Optional.<IToken>ofNullable(((ISlot)this.slots.get(0)).getToken());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isLibraryAware() {
/*  70 */     return this instanceof com.github.signer4j.ILibraryAware;
/*     */   }
/*     */   
/*     */   protected final void addDevice(IDevice device) {
/*  74 */     if (device != null) {
/*  75 */       this.devices.add(device);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<IDevice> getDevices() {
/*  81 */     load();
/*  82 */     return Collections.unmodifiableList(this.devices);
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<ISlot> getSlots() {
/*  87 */     load();
/*  88 */     return this.slots;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  93 */     int prime = 31;
/*  94 */     int result = 1;
/*  95 */     result = 31 * result + getId().hashCode();
/*  96 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 101 */     if (this == obj)
/* 102 */       return true; 
/* 103 */     if (obj == null)
/* 104 */       return false; 
/* 105 */     if (getClass() != obj.getClass())
/* 106 */       return false; 
/* 107 */     return isSame((AbstractDriver)obj);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void doUnload() {
/*     */     try {
/* 113 */       this.slots.stream().map(ISlot::getToken).forEach(IToken::logout);
/*     */     } finally {
/* 115 */       this.devices.clear();
/* 116 */       this.slots = Collections.emptyList();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void doLoad() throws Exception {
/* 122 */     List<ISlot> slots = new ArrayList<>();
/* 123 */     loadSlots(slots);
/* 124 */     this.slots = Collections.unmodifiableList(slots);
/*     */   }
/*     */   
/*     */   protected abstract void loadSlots(List<ISlot> paramList) throws DriverException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AbstractDriver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */