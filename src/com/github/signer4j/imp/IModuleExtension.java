/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IExtensionProvider;
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
/*    */ interface IModuleExtension
/*    */   extends IExtensionProvider
/*    */ {
/*    */   default String defaultModule() {
/* 34 */     return "pkcs11" + getExtension();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/IModuleExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */