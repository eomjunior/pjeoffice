/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDeviceAccessor;
/*    */ import com.github.signer4j.IDeviceManager;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Set;
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
/*    */ public class PKCS12DeviceManager
/*    */   extends AbstractDeviceAccessor
/*    */   implements IDeviceManager
/*    */ {
/* 39 */   private static final PKCS12Driver PKCS12_DRIVER = PKCS12Driver.getInstance();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Repository getRepository() {
/* 45 */     return Repository.NATIVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public final IDeviceAccessor uninstall(Path... pkcs12Files) {
/* 50 */     Args.requireNonNull(pkcs12Files, "Unabled to uninstall driver with null path's");
/* 51 */     PKCS12_DRIVER.uninstall(pkcs12Files);
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public final IDeviceAccessor uninstallPkcs12() {
/* 57 */     PKCS12_DRIVER.uninstall();
/* 58 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public final IDeviceManager install(Path... pkcs12File) {
/* 63 */     Args.requireNonNull(pkcs12File, "Unabled to install driver with null path's");
/* 64 */     uninstallPkcs12();
/* 65 */     PKCS12_DRIVER.install(pkcs12File);
/* 66 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void load(Set<IDriver> drivers) {
/* 71 */     drivers.add(PKCS12_DRIVER);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS12DeviceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */