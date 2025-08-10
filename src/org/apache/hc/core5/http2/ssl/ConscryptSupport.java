/*    */ package org.apache.hc.core5.http2.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLEngine;
/*    */ import javax.net.ssl.SSLException;
/*    */ import javax.net.ssl.SSLParameters;
/*    */ import org.apache.hc.core5.http.ssl.TLS;
/*    */ import org.apache.hc.core5.http.ssl.TlsCiphers;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.reactor.ssl.SSLSessionInitializer;
/*    */ import org.apache.hc.core5.reactor.ssl.SSLSessionVerifier;
/*    */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*    */ import org.conscrypt.Conscrypt;
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
/*    */ public final class ConscryptSupport
/*    */ {
/*    */   public static SSLSessionInitializer initialize(Object attachment, SSLSessionInitializer initializer) {
/* 49 */     return (endpoint, sslEngine) -> {
/*    */         SSLParameters sslParameters = sslEngine.getSSLParameters();
/*    */         sslParameters.setProtocols(TLS.excludeWeak(sslParameters.getProtocols()));
/*    */         sslParameters.setCipherSuites(TlsCiphers.excludeH2Blacklisted(sslParameters.getCipherSuites()));
/*    */         H2TlsSupport.setEnableRetransmissions(sslParameters, false);
/*    */         String[] appProtocols = H2TlsSupport.selectApplicationProtocols(attachment);
/*    */         if (Conscrypt.isConscrypt(sslEngine)) {
/*    */           sslEngine.setSSLParameters(sslParameters);
/*    */           Conscrypt.setApplicationProtocols(sslEngine, appProtocols);
/*    */         } else {
/*    */           sslParameters.setApplicationProtocols(appProtocols);
/*    */           sslEngine.setSSLParameters(sslParameters);
/*    */         } 
/*    */         if (initializer != null) {
/*    */           initializer.initialize(endpoint, sslEngine);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static SSLSessionVerifier verify(SSLSessionVerifier verifier) {
/* 69 */     return (endpoint, sslEngine) -> {
/*    */         TlsDetails tlsDetails = (verifier != null) ? verifier.verify(endpoint, sslEngine) : null;
/*    */         if (tlsDetails == null && Conscrypt.isConscrypt(sslEngine))
/*    */           tlsDetails = new TlsDetails(sslEngine.getSession(), Conscrypt.getApplicationProtocol(sslEngine)); 
/*    */         return tlsDetails;
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/ssl/ConscryptSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */