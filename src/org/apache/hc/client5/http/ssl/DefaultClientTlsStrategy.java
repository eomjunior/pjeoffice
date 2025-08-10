/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Factory;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.ssl.SSLContexts;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class DefaultClientTlsStrategy
/*     */   extends AbstractClientTlsStrategy
/*     */ {
/*     */   @Deprecated
/*     */   private Factory<SSLEngine, TlsDetails> tlsDetailsFactory;
/*     */   
/*     */   public static TlsStrategy getDefault() {
/*  52 */     return new DefaultClientTlsStrategy(
/*  53 */         SSLContexts.createDefault(), 
/*  54 */         HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */   
/*     */   public static TlsStrategy getSystemDefault() {
/*  58 */     return new DefaultClientTlsStrategy(
/*  59 */         SSLContexts.createSystemDefault(), 
/*  60 */         HttpsSupport.getSystemProtocols(), 
/*  61 */         HttpsSupport.getSystemCipherSuits(), SSLBufferMode.STATIC, 
/*     */         
/*  63 */         HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultClientTlsStrategy(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, SSLBufferMode sslBufferManagement, HostnameVerifier hostnameVerifier, Factory<SSLEngine, TlsDetails> tlsDetailsFactory) {
/*  83 */     super(sslContext, supportedProtocols, supportedCipherSuites, sslBufferManagement, hostnameVerifier);
/*  84 */     this.tlsDetailsFactory = tlsDetailsFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultClientTlsStrategy(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, SSLBufferMode sslBufferManagement, HostnameVerifier hostnameVerifier) {
/*  93 */     super(sslContext, supportedProtocols, supportedCipherSuites, sslBufferManagement, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultClientTlsStrategy(SSLContext sslcontext, HostnameVerifier hostnameVerifier) {
/*  99 */     this(sslcontext, null, null, SSLBufferMode.STATIC, hostnameVerifier);
/*     */   }
/*     */   
/*     */   public DefaultClientTlsStrategy(SSLContext sslcontext) {
/* 103 */     this(sslcontext, HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */ 
/*     */   
/*     */   void applyParameters(SSLEngine sslEngine, SSLParameters sslParameters, String[] appProtocols) {
/* 108 */     sslParameters.setApplicationProtocols(appProtocols);
/* 109 */     sslEngine.setSSLParameters(sslParameters);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   TlsDetails createTlsDetails(SSLEngine sslEngine) {
/* 115 */     return (this.tlsDetailsFactory != null) ? (TlsDetails)this.tlsDetailsFactory.create(sslEngine) : null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/DefaultClientTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */