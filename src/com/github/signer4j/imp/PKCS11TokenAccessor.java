/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverLookupStrategy;
/*    */ import com.github.signer4j.IDriverVisitor;
/*    */ import com.github.signer4j.IFilePath;
/*    */ import com.github.signer4j.IToken;
/*    */ import com.github.utils4j.imp.function.IBiProcedure;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public abstract class PKCS11TokenAccessor<T extends IToken>
/*    */   extends TokenAccessor<T>
/*    */ {
/*    */   protected static interface IFileLoader
/*    */     extends IBiProcedure<List<IFilePath>, List<IFilePath>> {}
/*    */   
/*    */   private class FilePathStrategy
/*    */     extends AbstractStrategy
/*    */   {
/*    */     private FilePathStrategy() {}
/*    */     
/*    */     public void lookup(IDriverVisitor visitor) {
/* 46 */       PKCS11TokenAccessor.this.a3Libraries.forEach(fp -> createAndVisit(Paths.get(fp.getPath(), new String[0]), visitor));
/*    */     }
/*    */   }
/*    */   
/* 50 */   private volatile List<IFilePath> a1Files = new ArrayList<>();
/*    */   
/* 52 */   private volatile List<IFilePath> a3Libraries = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   protected PKCS11TokenAccessor(AuthStrategy strategy, IFileLoader loader) {
/* 57 */     super(strategy, new Signer4JDeviceManager(new PKCS11DeviceAccessor(), new PKCS12DeviceManager()));
/* 58 */     loader.call(this.a1Files, this.a3Libraries);
/* 59 */     this.manager.setStrategy(new NotDuplicatedStrategy(new IDriverLookupStrategy[] { new FilePathStrategy() }));
/* 60 */     this.manager.install(IFilePath.toPaths(this.a1Files));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doCertificateAvailable(List<IFilePath> a1List, List<IFilePath> a3List) {
/* 66 */     this.a3Libraries = a3List;
/* 67 */     this.a1Files = a1List;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11TokenAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */