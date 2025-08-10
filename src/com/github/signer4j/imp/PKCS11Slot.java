/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.ILibraryAware;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import com.github.signer4j.exception.DriverFailException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Objects;
/*     */ import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
/*     */ import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PKCS11Slot
/*     */   extends AbstractSlot
/*     */   implements ILibraryAware
/*     */ {
/*     */   private static final String UNKNOWN_INFORMATION = "Unknown";
/*     */   protected PKCS11Token token;
/*     */   private String description;
/*     */   private String manufacturerId;
/*     */   private String firmwareVersion;
/*     */   private String hardwareVersion;
/*     */   private final transient PKCS11Driver driver;
/*     */   
/*     */   PKCS11Slot(PKCS11Driver driver, long number) throws DriverException {
/*  63 */     super(number);
/*  64 */     this.driver = (PKCS11Driver)Args.requireNonNull(driver, "Unabled to create slot with null driver instance");
/*  65 */     setup();
/*     */   }
/*     */   
/*     */   final PKCS11 getPK() {
/*  69 */     return this.driver.getPK();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getDescription() {
/*  74 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getManufacturer() {
/*  79 */     return this.manufacturerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getHardwareVersion() {
/*  84 */     return this.hardwareVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getFirmewareVersion() {
/*  89 */     return this.firmwareVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IToken getToken() {
/*  94 */     return this.token;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getLibrary() {
/*  99 */     return this.driver.getLibrary();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return super.toString() + ":  library=" + getLibrary();
/*     */   }
/*     */   private void setup() throws DriverException {
/*     */     CK_SLOT_INFO slotInfo;
/*     */     CK_TOKEN_INFO tokenInfo;
/* 109 */     PKCS11 pk = getPK();
/*     */ 
/*     */     
/*     */     try {
/* 113 */       slotInfo = pk.C_GetSlotInfo(getNumber());
/* 114 */     } catch (PKCS11Exception e) {
/* 115 */       throw new DriverFailException("Unabled to get SLOT information from number " + getNumber() + " - driver: " + this.driver, e);
/*     */     } 
/*     */     
/* 118 */     this.description = Objects.iso2utf8(slotInfo.slotDescription, "Unknown").trim();
/* 119 */     this.manufacturerId = Objects.iso2utf8(slotInfo.manufacturerID, "Unknown").trim();
/* 120 */     this.firmwareVersion = Objects.toString(slotInfo.firmwareVersion, "Unknown").trim();
/* 121 */     this.hardwareVersion = Objects.toString(slotInfo.hardwareVersion, "Unknown").trim();
/*     */ 
/*     */     
/*     */     try {
/* 125 */       tokenInfo = pk.C_GetTokenInfo(getNumber());
/* 126 */     } catch (PKCS11Exception e) {
/* 127 */       throw new DriverFailException("Unabled to get token information on SLOT " + getNumber() + " and " + this, e);
/*     */     } 
/*     */     
/* 130 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 137 */       .token = (PKCS11Token)(new PKCS11Token.Builder(this)).withMinPinLen(tokenInfo.ulMinPinLen).withMaxPinLen(tokenInfo.ulMaxPinLen).withLabel(Objects.iso2utf8(tokenInfo.label, "Unknown").trim()).withModel(Objects.iso2utf8(tokenInfo.model, "Unknown").trim()).withSerial(Objects.iso2utf8(tokenInfo.serialNumber, "Unknown").trim()).withManufacture(Objects.iso2utf8(tokenInfo.manufacturerID, "Unknown").trim()).build();
/*     */     
/* 139 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       .device = (new DefaultDevice.Builder(IDevice.Type.A3)).withDriver(getLibrary()).withSlot(this).withLabel(this.token.getLabel()).withModel(this.token.getModel()).withSerial(this.token.getSerial()).withCertificates(this.token.getCertificates()).build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11Slot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */