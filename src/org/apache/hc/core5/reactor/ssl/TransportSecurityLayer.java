/*    */ package org.apache.hc.core5.reactor.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLContext;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.util.Timeout;
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
/*    */ public interface TransportSecurityLayer
/*    */ {
/*    */   void startTls(SSLContext paramSSLContext, NamedEndpoint paramNamedEndpoint, SSLBufferMode paramSSLBufferMode, SSLSessionInitializer paramSSLSessionInitializer, SSLSessionVerifier paramSSLSessionVerifier, Timeout paramTimeout) throws UnsupportedOperationException;
/*    */   
/*    */   default void startTls(SSLContext sslContext, NamedEndpoint endpoint, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) throws UnsupportedOperationException {
/* 85 */     startTls(sslContext, endpoint, sslBufferMode, initializer, verifier, handshakeTimeout);
/* 86 */     if (callback != null)
/* 87 */       callback.completed(null); 
/*    */   }
/*    */   
/*    */   TlsDetails getTlsDetails();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ssl/TransportSecurityLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */