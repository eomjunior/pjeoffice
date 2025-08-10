/*    */ package com.github.signer4j.imp.exception;
/*    */ 
/*    */ import com.github.utils4j.gui.imp.CancelAlert;
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ public class LoginCanceledException
/*    */   extends Signer4JException
/*    */ {
/* 35 */   private static final String DEFAULT_MESSAGE = CancelAlert.CANCELED_OPERATION_MESSAGE;
/*    */   
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public LoginCanceledException() {
/* 40 */     this(DEFAULT_MESSAGE);
/*    */   }
/*    */   
/*    */   public LoginCanceledException(Throwable e) {
/* 44 */     super(DEFAULT_MESSAGE, e);
/*    */   }
/*    */   
/*    */   public LoginCanceledException(String message) {
/* 48 */     this(message, null);
/*    */   }
/*    */   
/*    */   public LoginCanceledException(String message, Throwable e) {
/* 52 */     super(DEFAULT_MESSAGE + ". " + Strings.text(message), e);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/LoginCanceledException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */