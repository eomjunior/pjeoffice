/*    */ package org.apache.hc.core5.http.nio.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLEngine;
/*    */ import javax.net.ssl.SSLParameters;
/*    */ import org.apache.hc.core5.http.ssl.TLS;
/*    */ import org.apache.hc.core5.http.ssl.TlsCiphers;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.reactor.ssl.SSLSessionInitializer;
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
/*    */ public final class TlsSupport
/*    */ {
/*    */   public static SSLSessionInitializer enforceStrongSecurity(SSLSessionInitializer initializer) {
/* 44 */     return (endpoint, sslEngine) -> {
/*    */         SSLParameters sslParameters = sslEngine.getSSLParameters();
/*    */         sslParameters.setProtocols(TLS.excludeWeak(sslParameters.getProtocols()));
/*    */         sslParameters.setCipherSuites(TlsCiphers.excludeWeak(sslParameters.getCipherSuites()));
/*    */         sslEngine.setSSLParameters(sslParameters);
/*    */         if (initializer != null)
/*    */           initializer.initialize(endpoint, sslEngine); 
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/ssl/TlsSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */