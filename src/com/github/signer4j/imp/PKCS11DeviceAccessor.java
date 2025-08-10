/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverLookupStrategy;
/*    */ import com.github.signer4j.IDriverSetup;
/*    */ import com.github.signer4j.IPathCollectionStrategy;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class PKCS11DeviceAccessor
/*    */   extends AbstractDeviceAccessor
/*    */ {
/*    */   private IDriverLookupStrategy strategy;
/*    */   
/*    */   public PKCS11DeviceAccessor() {
/* 41 */     this((IDriverLookupStrategy)IPathCollectionStrategy.NOTHING);
/*    */   }
/*    */ 
/*    */   
/*    */   public PKCS11DeviceAccessor(IDriverLookupStrategy strategy) {
/* 46 */     this.strategy = (IDriverLookupStrategy)Args.requireNonNull(strategy, "strategy is null");
/*    */   }
/*    */   
/*    */   protected final void load(Set<IDriver> drivers) {
/* 50 */     this.strategy.lookup(setup -> drivers.add(new PKCS11Driver(setup.getLibrary())));
/*    */   }
/*    */ 
/*    */   
/*    */   public final void setStrategy(IDriverLookupStrategy strategy) {
/* 55 */     this.strategy = (strategy == null) ? (IDriverLookupStrategy)IPathCollectionStrategy.NOTHING : strategy;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11DeviceAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */