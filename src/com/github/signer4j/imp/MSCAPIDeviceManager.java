/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IDevice;
/*     */ import com.github.signer4j.IDeviceAccessor;
/*     */ import com.github.signer4j.IDeviceManager;
/*     */ import com.github.signer4j.IPasswordCallbackHandler;
/*     */ import com.github.signer4j.ISlot;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.TokenType;
/*     */ import com.github.signer4j.cert.ICertificateFactory;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import com.github.signer4j.exception.DriverFailException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class MSCAPIDeviceManager
/*     */   extends AbstractDeviceAccessor
/*     */   implements IDeviceManager
/*     */ {
/*     */   protected void load(Set<IDriver> drivers) {
/*  51 */     drivers.add(new MSCAPIDriver());
/*     */   }
/*     */ 
/*     */   
/*     */   public final Repository getRepository() {
/*  56 */     return Repository.MSCAPI;
/*     */   }
/*     */   
/*     */   private static final class MSCAPIDriver extends AbstractDriver {
/*     */     private MSCAPIDriver() {}
/*     */     
/*     */     public String getId() {
/*  63 */       return "mscapi";
/*     */     }
/*     */ 
/*     */     
/*     */     protected void loadSlots(List<ISlot> output) throws DriverException {
/*  68 */       ISlot slot = new MSCAPIDeviceManager.MSCAPISlot();
/*  69 */       output.add(slot);
/*  70 */       addDevice(slot.toDevice());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class MSCAPISlot
/*     */     extends VirtualSlot {
/*     */     private MSCAPIDeviceManager.MSCAPIToken token;
/*     */     
/*     */     protected MSCAPISlot() throws DriverException {
/*  79 */       super(-1L, "Mscapi");
/*  80 */       setup();
/*     */     }
/*     */ 
/*     */     
/*     */     public IToken getToken() {
/*  85 */       return this.token;
/*     */     }
/*     */     
/*     */     private void setup() throws DriverException {
/*  89 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  95 */         .device = (new DefaultDevice.Builder(IDevice.Type.VIRTUAL)).withDriver(getFirmewareVersion()).withSlot(this).withLabel("mscapi").withModel("mscapi").withSerial("mscapi").build();
/*     */       
/*  97 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 102 */         .token = (MSCAPIDeviceManager.MSCAPIToken)(new MSCAPIDeviceManager.MSCAPIToken.Builder(this, this.device)).withLabel(this.device.getLabel()).withModel(this.device.getModel()).withSerial(getHardwareVersion()).withManufacture(getManufacturer()).build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MSCAPIToken
/*     */     extends AbstractToken<MSCAPISlot>
/*     */   {
/*     */     private static final int MIN_PASSWORD_LENGTH = 1;
/*     */     private static final int MAX_PASSWORD_LENGTH = 31;
/*     */     private DefaultDevice device;
/*     */     private MSCAPIKeyStoreLoader loader;
/*     */     
/*     */     protected MSCAPIToken(MSCAPIDeviceManager.MSCAPISlot slot, DefaultDevice device) {
/* 116 */       super(slot, TokenType.VIRTUAL);
/* 117 */       this.loader = new MSCAPIKeyStoreLoader(this.device = device, getDispose());
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isMscapi() {
/* 122 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public final long getMinPinLen() {
/* 127 */       return 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public final long getMaxPinLen() {
/* 132 */       return 31L;
/*     */     }
/*     */ 
/*     */     
/*     */     protected IKeyStore getKeyStore(IPasswordCallbackHandler callback) throws Signer4JException {
/* 137 */       return this.loader.getKeyStore();
/*     */     }
/*     */ 
/*     */     
/*     */     protected IToken loadCertificates(ICertificateFactory factory) throws DriverException {
/* 142 */       try (IKeyStore keyStore = this.loader.getKeyStore()) {
/* 143 */         this.device.setCertificates(this.certificates = new MSCAPIDeviceManager.MSCAPICertificates(this, keyStore, factory));
/* 144 */       } catch (Exception e) {
/* 145 */         throw new DriverFailException("Não foi possível carregar os certificados", e);
/*     */       } 
/* 147 */       return this;
/*     */     }
/*     */     
/*     */     static class Builder
/*     */       extends AbstractToken.Builder<MSCAPIDeviceManager.MSCAPISlot, MSCAPIToken> {
/*     */       private DefaultDevice device;
/*     */       
/*     */       Builder(MSCAPIDeviceManager.MSCAPISlot slot, DefaultDevice device) {
/* 155 */         super(slot);
/* 156 */         this.device = (DefaultDevice)Args.requireNonNull(device, "device is null");
/*     */       }
/*     */ 
/*     */       
/*     */       protected AbstractToken<MSCAPIDeviceManager.MSCAPISlot> createToken(MSCAPIDeviceManager.MSCAPISlot slot) throws DriverException {
/* 161 */         return new MSCAPIDeviceManager.MSCAPIToken(slot, this.device);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MSCAPICertificates extends KeyStoreCertificates {
/*     */     public MSCAPICertificates(MSCAPIDeviceManager.MSCAPIToken token, IKeyStore keyStore, ICertificateFactory factory) throws Signer4JException {
/* 168 */       super(token, keyStore, factory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IDeviceAccessor install(Path... pkcs12Files) {
/* 175 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IDeviceAccessor uninstall(Path... pkcs12File) {
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IDeviceAccessor uninstallPkcs12() {
/* 187 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/MSCAPIDeviceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */