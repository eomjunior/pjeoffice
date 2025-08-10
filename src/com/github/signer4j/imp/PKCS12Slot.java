/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificates;
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.ILibraryAware;
/*    */ import com.github.signer4j.IToken;
/*    */ import com.github.signer4j.exception.DriverException;
/*    */ import com.github.signer4j.exception.DriverFailException;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Streams;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PKCS12Slot
/*    */   extends VirtualSlot
/*    */   implements ILibraryAware
/*    */ {
/*    */   private PKCS12Token token;
/*    */   private final Path certPath;
/*    */   
/*    */   PKCS12Slot(Path certPath) throws DriverException {
/* 49 */     super(-1L, "Pkcs12");
/* 50 */     this.certPath = (Path)Args.requireNonNull(certPath, "cert path can't be null");
/* 51 */     setup();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getLibrary() {
/* 56 */     return this.certPath.toFile().getAbsolutePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public final IToken getToken() {
/* 61 */     return this.token;
/*    */   }
/*    */   
/*    */   private void setup() throws DriverException {
/*    */     String md5;
/*    */     try {
/* 67 */       md5 = Streams.md5(this.certPath.toFile());
/* 68 */     } catch (IOException e) {
/* 69 */       throw new DriverFailException("can't access " + this.certPath, e);
/*    */     } 
/* 71 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 76 */       .token = (PKCS12Token)(new PKCS12Token.Builder(this)).withLabel(this.certPath.getFileName().toString()).withModel("arquivo").withSerial(md5).withManufacture(this.certPath.getParent().toString()).build();
/*    */     
/* 78 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 85 */       .device = (new DefaultDevice.Builder(IDevice.Type.A1)).withDriver(getLibrary()).withSlot(this).withLabel(this.token.getLabel()).withModel(this.token.getModel()).withSerial(this.token.getSerial()).withCertificates(this.token.getCertificates()).build();
/*    */   }
/*    */   
/*    */   void updateDevice(ICertificates certificates) {
/* 89 */     this.device.setCertificates(certificates);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS12Slot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */