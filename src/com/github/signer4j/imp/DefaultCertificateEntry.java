/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.ICertificateListUI;
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Dates;
/*    */ import java.util.function.Function;
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
/*    */ 
/*    */ public class DefaultCertificateEntry
/*    */   implements ICertificateListUI.ICertificateEntry
/*    */ {
/*    */   private final boolean expired;
/*    */   protected final IDevice device;
/*    */   protected final ICertificate certificate;
/*    */   protected final Function<String, String> formater;
/*    */   protected boolean remembered;
/*    */   
/*    */   public DefaultCertificateEntry(IDevice device, ICertificate certificate) {
/* 48 */     this.device = (IDevice)Args.requireNonNull(device, "device is null");
/* 49 */     this.certificate = (ICertificate)Args.requireNonNull(certificate, "certificate is null");
/* 50 */     this.expired = certificate.isExpired();
/* 51 */     this.formater = !this.expired ? (s -> s) : (s -> "<html><strike style=\"color:red\">" + s + "</strike></html>");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isExpired() {
/* 56 */     return this.expired;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getDevice() {
/* 61 */     return this.formater.apply(this.device.getType() + ": " + this.device.getSerial());
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 66 */     return this.formater.apply(this.certificate.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getIssuer() {
/* 71 */     return this.formater.apply(this.certificate.getCertificateIssuerDN().getProperty("CN").orElse("Desconhecido"));
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getDate() {
/* 76 */     return this.formater.apply(Dates.defaultFormat(this.certificate.getAfterDate()));
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getId() {
/* 81 */     return this.device.getSerial() + ":" + this.certificate.getSerial();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isRemembered() {
/* 86 */     return this.remembered;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void setRemembered(boolean remembered) {
/* 91 */     this.remembered = remembered;
/*    */   }
/*    */ 
/*    */   
/*    */   public final ICertificate getCertificate() {
/* 96 */     return this.certificate;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/DefaultCertificateEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */