/*    */ package com.github.signer4j.provider;
/*    */ 
/*    */ import java.security.ProviderException;
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
/*    */ public class UnavailableProviderException
/*    */   extends ProviderException
/*    */ {
/*    */   private static final long serialVersionUID = 3552588314412255123L;
/*    */   
/*    */   public UnavailableProviderException(String message) {
/* 36 */     super(message);
/*    */   }
/*    */   
/*    */   public UnavailableProviderException(Throwable cause) {
/* 40 */     super(cause);
/*    */   }
/*    */   
/*    */   public UnavailableProviderException(String message, Throwable cause) {
/* 44 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/UnavailableProviderException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */