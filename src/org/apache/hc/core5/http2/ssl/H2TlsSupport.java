/*    */ package org.apache.hc.core5.http2.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLEngine;
/*    */ import javax.net.ssl.SSLParameters;
/*    */ import org.apache.hc.core5.http.ssl.TLS;
/*    */ import org.apache.hc.core5.http.ssl.TlsCiphers;
/*    */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.reactor.ssl.SSLSessionInitializer;
/*    */ import org.apache.hc.core5.util.ReflectionUtils;
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
/*    */ public final class H2TlsSupport
/*    */ {
/*    */   public static void setEnableRetransmissions(SSLParameters sslParameters, boolean value) {
/* 46 */     ReflectionUtils.callSetter(sslParameters, "EnableRetransmissions", boolean.class, Boolean.valueOf(value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static void setApplicationProtocols(SSLParameters sslParameters, String[] values) {
/* 54 */     ReflectionUtils.callSetter(sslParameters, "ApplicationProtocols", String[].class, values);
/*    */   }
/*    */   
/*    */   public static String[] selectApplicationProtocols(Object attachment) {
/* 58 */     HttpVersionPolicy versionPolicy = (attachment instanceof HttpVersionPolicy) ? (HttpVersionPolicy)attachment : HttpVersionPolicy.NEGOTIATE;
/*    */     
/* 60 */     switch (versionPolicy) {
/*    */       case FORCE_HTTP_1:
/* 62 */         return new String[] { ApplicationProtocol.HTTP_1_1.id };
/*    */       case FORCE_HTTP_2:
/* 64 */         return new String[] { ApplicationProtocol.HTTP_2.id };
/*    */     } 
/* 66 */     return new String[] { ApplicationProtocol.HTTP_2.id, ApplicationProtocol.HTTP_1_1.id };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SSLSessionInitializer enforceRequirements(Object attachment, SSLSessionInitializer initializer) {
/* 73 */     return (endpoint, sslEngine) -> {
/*    */         SSLParameters sslParameters = sslEngine.getSSLParameters();
/*    */         sslParameters.setProtocols(TLS.excludeWeak(sslParameters.getProtocols()));
/*    */         sslParameters.setCipherSuites(TlsCiphers.excludeH2Blacklisted(sslParameters.getCipherSuites()));
/*    */         setEnableRetransmissions(sslParameters, false);
/*    */         sslParameters.setApplicationProtocols(selectApplicationProtocols(attachment));
/*    */         sslEngine.setSSLParameters(sslParameters);
/*    */         if (initializer != null)
/*    */           initializer.initialize(endpoint, sslEngine); 
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/ssl/H2TlsSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */