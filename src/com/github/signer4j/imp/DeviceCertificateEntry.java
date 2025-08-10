/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.ICertificateListUI;
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
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
/*    */ public class DeviceCertificateEntry
/*    */   extends DefaultCertificateEntry
/*    */ {
/*    */   private DeviceCertificateEntry(IDevice device, ICertificate certificate) {
/* 44 */     super(device, certificate);
/*    */   }
/*    */   
/*    */   public final IDevice getNative() {
/* 48 */     return this.device;
/*    */   }
/*    */   
/*    */   public static List<ICertificateListUI.ICertificateEntry> from(List<IDevice> devices, Predicate<ICertificate> filter) {
/* 52 */     Args.requireNonNull(devices, "devices is null");
/* 53 */     Args.requireNonNull(filter, "filter is null");
/* 54 */     return (List<ICertificateListUI.ICertificateEntry>)devices.stream().map(d -> (List)d.getCertificates().filter(filter).map(()).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/DeviceCertificateEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */