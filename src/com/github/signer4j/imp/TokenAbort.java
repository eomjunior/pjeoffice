/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.gui.alert.MscapiFailAlert;
/*    */ import com.github.signer4j.gui.alert.NoTokenPresentInfo;
/*    */ import com.github.signer4j.imp.exception.MscapiException;
/*    */ import com.github.signer4j.imp.exception.NoTokenPresentException;
/*    */ import com.github.utils4j.imp.function.IProcedure;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ 
/*    */ enum TokenAbort
/*    */ {
/* 12 */   HANDLER;
/*    */   
/*    */   public <T> T handle(IProcedure<T, Exception> code, ReentrantLock lock, Runnable logout) {
/*    */     try {
/* 16 */       lock.lock();
/*    */       
/* 18 */       Signer4jContext.checkDiscarded();
/*    */       
/*    */       try {
/* 21 */         return (T)code.call();
/*    */       }
/* 23 */       catch (NoTokenPresentException e) {
/* 24 */         NoTokenPresentInfo.showInfoOnly();
/* 25 */         Signer4jContext.discard(logout, (Throwable)e);
/*    */       }
/* 27 */       catch (MscapiException e) {
/* 28 */         MscapiFailAlert.showMessage();
/* 29 */         Signer4jContext.discard(logout, (Throwable)e);
/*    */       }
/* 31 */       catch (Throwable e) {
/* 32 */         Signer4jContext.discard(logout, e);
/*    */       } 
/*    */       
/* 35 */       return null;
/*    */     } finally {
/*    */       
/* 38 */       lock.unlock();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/TokenAbort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */