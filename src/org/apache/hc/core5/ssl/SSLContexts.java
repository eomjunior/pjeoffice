/*    */ package org.apache.hc.core5.ssl;
/*    */ 
/*    */ import java.security.GeneralSecurityException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import javax.net.ssl.SSLContext;
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
/*    */ public final class SSLContexts
/*    */ {
/*    */   public static SSLContext createDefault() throws SSLInitializationException {
/*    */     try {
/* 66 */       SSLContext sslContext = SSLContext.getInstance("TLS");
/* 67 */       sslContext.init(null, null, null);
/* 68 */       return sslContext;
/* 69 */     } catch (NoSuchAlgorithmException|java.security.KeyManagementException ex) {
/* 70 */       throw new SSLInitializationException(ex.getMessage(), ex);
/*    */     } 
/*    */   }
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
/*    */   public static SSLContext createSystemDefault() throws SSLInitializationException {
/*    */     try {
/* 86 */       return SSLContext.getDefault();
/* 87 */     } catch (NoSuchAlgorithmException ex) {
/* 88 */       return createDefault();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SSLContextBuilder custom() {
/* 98 */     return SSLContextBuilder.create();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/ssl/SSLContexts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */