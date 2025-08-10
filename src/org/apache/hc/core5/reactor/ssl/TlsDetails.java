/*    */ package org.apache.hc.core5.reactor.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLSession;
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
/*    */ public final class TlsDetails
/*    */ {
/*    */   private final SSLSession sslSession;
/*    */   private final String applicationProtocol;
/*    */   
/*    */   public TlsDetails(SSLSession sslSession, String applicationProtocol) {
/* 43 */     this.sslSession = sslSession;
/* 44 */     this.applicationProtocol = applicationProtocol;
/*    */   }
/*    */   
/*    */   public SSLSession getSSLSession() {
/* 48 */     return this.sslSession;
/*    */   }
/*    */   
/*    */   public String getApplicationProtocol() {
/* 52 */     return this.applicationProtocol;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "TlsDetails{sslSession=" + this.sslSession + ", applicationProtocol='" + this.applicationProtocol + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ssl/TlsDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */