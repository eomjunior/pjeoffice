/*    */ package com.github.signer4j;
/*    */ 
/*    */ import com.github.signer4j.imp.CanceledOperationException;
/*    */ import com.github.signer4j.imp.ResponseCallback;
/*    */ import java.io.IOException;
/*    */ import javax.security.auth.callback.Callback;
/*    */ import javax.security.auth.callback.CallbackHandler;
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
/*    */ public interface IPasswordCallbackHandler
/*    */   extends CallbackHandler
/*    */ {
/*    */   default void handle(Callback callback) throws IOException {
/* 42 */     handle(new Callback[] { callback });
/*    */   }
/*    */   
/*    */   default void handle(Callback[] callbacks) throws IOException {
/* 46 */     for (Callback callback : callbacks) {
/* 47 */       if (callback instanceof PasswordCallback) {
/* 48 */         PasswordCallback pc = (PasswordCallback)callback;
/* 49 */         pc.clearPassword();
/* 50 */         if (ResponseCallback.CANCEL.equals(doHandle(pc))) {
/* 51 */           throw new CanceledOperationException();
/*    */         }
/*    */         return;
/*    */       } 
/*    */     } 
/* 56 */     throw new CanceledOperationException();
/*    */   }
/*    */   
/*    */   ResponseCallback doHandle(PasswordCallback paramPasswordCallback);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IPasswordCallbackHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */