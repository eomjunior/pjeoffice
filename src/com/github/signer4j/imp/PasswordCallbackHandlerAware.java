/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IPasswordCallbackHandler;
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
/*    */ public class PasswordCallbackHandlerAware
/*    */   implements IPasswordCallbackHandler
/*    */ {
/*    */   private boolean wasHandle = false;
/*    */   private final IPasswordCallbackHandler handler;
/*    */   
/*    */   public PasswordCallbackHandlerAware(IPasswordCallbackHandler handler) {
/* 41 */     this.handler = handler;
/*    */   }
/*    */   
/*    */   public boolean wasHandle() {
/* 45 */     return this.wasHandle;
/*    */   }
/*    */   
/*    */   public ResponseCallback doHandle(PasswordCallback callback) {
/* 49 */     this.wasHandle = true;
/* 50 */     return this.handler.doHandle(callback);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PasswordCallbackHandlerAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */