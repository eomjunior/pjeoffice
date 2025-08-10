/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IPasswordCallbackHandler;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.Scanner;
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
/*    */ public enum PasswordCallbackHandler
/*    */   implements IPasswordCallbackHandler
/*    */ {
/* 39 */   NULL
/*    */   {
/*    */     public ResponseCallback doHandle(PasswordCallback callback) {
/* 42 */       Args.requireNonNull(callback, "Password callback is null");
/* 43 */       callback.setPassword(null);
/* 44 */       return ResponseCallback.OK;
/*    */     }
/*    */   },
/* 47 */   CONSOLE
/*    */   {
/*    */     public ResponseCallback doHandle(PasswordCallback callback) {
/* 50 */       Args.requireNonNull(callback, "Password callback is null");
/* 51 */       System.out.print("Password: ");
/*    */       
/* 53 */       Scanner sc = new Scanner(System.in);
/* 54 */       String password = sc.nextLine();
/* 55 */       if (Strings.isEmpty(password))
/* 56 */         return ResponseCallback.CANCEL; 
/* 57 */       callback.setPassword(password.toCharArray());
/* 58 */       return ResponseCallback.OK;
/*    */     }
/*    */   };
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PasswordCallbackHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */