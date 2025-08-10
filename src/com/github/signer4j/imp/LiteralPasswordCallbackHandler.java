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
/*    */ public class LiteralPasswordCallbackHandler
/*    */   implements IPasswordCallbackHandler
/*    */ {
/*    */   private char[] password;
/*    */   
/*    */   public LiteralPasswordCallbackHandler(char[] password) {
/* 39 */     this.password = password;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResponseCallback doHandle(PasswordCallback callback) {
/* 44 */     callback.setPassword(this.password);
/* 45 */     return ResponseCallback.OK;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/LiteralPasswordCallbackHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */