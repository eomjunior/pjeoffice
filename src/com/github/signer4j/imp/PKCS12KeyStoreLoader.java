/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.IKeyStoreAccess;
/*    */ import com.github.signer4j.IPasswordCallbackHandler;
/*    */ import com.github.signer4j.imp.exception.Pkcs12FileNotFoundException;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.IParams;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.security.KeyStore;
/*    */ import java.util.function.Supplier;
/*    */ import javax.security.auth.callback.PasswordCallback;
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
/*    */ 
/*    */ 
/*    */ class PKCS12KeyStoreLoader
/*    */   implements IKeyStoreLoader
/*    */ {
/*    */   private final IPasswordCallbackHandler handler;
/*    */   private final IDevice device;
/*    */   private final Runnable dispose;
/*    */   
/*    */   public PKCS12KeyStoreLoader(IPasswordCallbackHandler handler, IDevice device, Runnable dispose) {
/* 58 */     this.handler = (IPasswordCallbackHandler)Args.requireNonNull(handler, "Unabled to create loader with null handler");
/* 59 */     this.device = (IDevice)Args.requireNonNull(device, "device is null");
/* 60 */     this.dispose = (Runnable)Args.requireNonNull(dispose, "dispose is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public IKeyStore getKeyStore(IParams params) throws Signer4JException {
/* 65 */     Args.requireNonNull(params, "params is null");
/* 66 */     String certPath = (String)params.orElseThrow("certificatePath", validate());
/* 67 */     return getKeyStore(new File(certPath));
/*    */   }
/*    */   
/*    */   private static Supplier<? extends IllegalArgumentException> validate() {
/* 71 */     return () -> new IllegalArgumentException("certificatePath is undefined");
/*    */   }
/*    */   
/*    */   private IKeyStore getKeyStore(File input) throws Signer4JException {
/* 75 */     try (InputStream stream = new FileInputStream(input)) {
/* 76 */       PasswordCallback callback = new PasswordCallback(Strings.space(), false);
/* 77 */       return (IKeyStore)Signer4JInvoker.SIGNER4J.invoke(() -> { this.handler.handle(callback); KeyStore keyStore = KeyStore.getInstance("PKCS12"); keyStore.load(stream, callback.getPassword()); return new PKCS12KeyStore(keyStore, this.device, this.dispose, callback.getPassword()); }callback::clearPassword);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 83 */     catch (FileNotFoundException e) {
/* 84 */       throw new Pkcs12FileNotFoundException("Arquivo não encontrado: " + input.getAbsolutePath(), e);
/* 85 */     } catch (IOException e) {
/* 86 */       throw new Signer4JException("Não foi possível ler o arquivo: " + input.getAbsolutePath(), e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS12KeyStoreLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */