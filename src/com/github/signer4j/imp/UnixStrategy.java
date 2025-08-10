/*    */ package com.github.signer4j.imp;
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
/*    */ abstract class UnixStrategy
/*    */   extends PreloadedStrategy
/*    */ {
/*    */   protected final void add(String library) {
/* 33 */     load("/usr/lib/" + library);
/* 34 */     load("/usr/local/lib/" + library);
/* 35 */     load("/usr/local/lib/pkcs11/" + library);
/* 36 */     load("/usr/lib/pkcs11/" + library);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/UnixStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */