/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverVisitor;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Environment;
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
/*    */ public class EnvironmentStrategy
/*    */   extends AbstractStrategy
/*    */ {
/*    */   private static final String VAR_PKCS11_DRIVER = "PKCS11_DRIVER";
/*    */   private IModuleExtension support;
/*    */   
/*    */   public EnvironmentStrategy() {
/* 41 */     this(SystemSupport.getDefault());
/*    */   }
/*    */   
/*    */   public EnvironmentStrategy(IModuleExtension support) {
/* 45 */     this.support = (IModuleExtension)Args.requireNonNull(support, "support can't be null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void lookup(IDriverVisitor visitor) {
/* 50 */     Environment.resolveTo("PKCS11_DRIVER", this.support
/*    */         
/* 52 */         .defaultModule(), true, true)
/*    */ 
/*    */       
/* 55 */       .ifPresent(p -> createAndVisit(p, visitor));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/EnvironmentStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */