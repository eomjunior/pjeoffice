/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.ICertificates;
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
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
/*     */ class DefaultDevice
/*     */   implements IDevice
/*     */ {
/*     */   private String driver;
/*     */   private String label;
/*     */   private String model;
/*     */   private String serial;
/*     */   private ISlot slot;
/*     */   private IDevice.Type type;
/*     */   private ICertificates certificates;
/*     */   
/*     */   static class Builder
/*     */   {
/*     */     private String driver;
/*     */     private String label;
/*     */     private String model;
/*     */     private String serial;
/*     */     private ISlot slot;
/*     */     private IDevice.Type type;
/*     */     private ICertificates certificates;
/*     */     
/*     */     Builder(IDevice.Type type) {
/*  49 */       this.type = (IDevice.Type)Args.requireNonNull(type, "type is null");
/*     */     }
/*     */     
/*     */     public final Builder withDriver(String driver) {
/*  53 */       this.driver = Strings.trim(driver);
/*  54 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder withLabel(String label) {
/*  58 */       this.label = Strings.trim(label);
/*  59 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder withSlot(ISlot slot) {
/*  63 */       this.slot = slot;
/*  64 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder withModel(String model) {
/*  68 */       this.model = Strings.trim(model);
/*  69 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder withSerial(String serial) {
/*  73 */       this.serial = Strings.trim(serial);
/*  74 */       return this;
/*     */     }
/*     */     
/*     */     public final Builder withCertificates(ICertificates certificates) {
/*  78 */       this.certificates = certificates;
/*  79 */       return this;
/*     */     }
/*     */     
/*     */     final DefaultDevice build() {
/*  83 */       DefaultDevice device = new DefaultDevice();
/*  84 */       device.driver = this.driver;
/*  85 */       device.label = this.label;
/*  86 */       device.model = this.model;
/*  87 */       device.serial = this.serial;
/*  88 */       device.slot = this.slot;
/*  89 */       device.type = this.type;
/*  90 */       device.certificates = this.certificates;
/*  91 */       return device;
/*     */     }
/*     */   }
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
/*     */   public DefaultDevice() {
/* 105 */     this.driver = "";
/* 106 */     this.label = "";
/* 107 */     this.model = "";
/* 108 */     this.serial = "";
/* 109 */     this.slot = null;
/* 110 */     this.type = null;
/* 111 */     this.certificates = Unavailables.getCertificates(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final IDevice.Type getType() {
/* 116 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getCategory() {
/* 121 */     return getType().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getDriver() {
/* 126 */     return this.driver;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getLabel() {
/* 131 */     return this.label;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ISlot getSlot() {
/* 136 */     return this.slot;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getModel() {
/* 141 */     return this.model;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getSerial() {
/* 146 */     return this.serial;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ICertificates getCertificates() {
/* 151 */     return this.certificates;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getManufacturer() {
/* 156 */     return this.slot.getManufacturer();
/*     */   }
/*     */   
/*     */   final void setCertificates(ICertificates certificates) {
/* 160 */     Args.requireNonNull(certificates, "certificates is null");
/* 161 */     this.certificates = certificates;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 166 */     return String.format("DefaultDevice [driver=%s, label=%s, model=%s, serial=%s, slot=%s, certificates=%s]", new Object[] { this.driver, this.label, this.model, this.serial, this.slot, this.certificates });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 172 */     int prime = 31;
/* 173 */     int result = 1;
/* 174 */     result = 31 * result + ((this.serial == null) ? 0 : this.serial.toLowerCase().hashCode());
/* 175 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 180 */     if (this == obj)
/* 181 */       return true; 
/* 182 */     if (obj == null)
/* 183 */       return false; 
/* 184 */     if (getClass() != obj.getClass())
/* 185 */       return false; 
/* 186 */     DefaultDevice other = (DefaultDevice)obj;
/* 187 */     if (this.serial == null) {
/* 188 */       if (other.serial != null)
/* 189 */         return false; 
/* 190 */     } else if (!this.serial.toLowerCase().equals(other.serial.toLowerCase())) {
/* 191 */       return false;
/* 192 */     }  return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/DefaultDevice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */