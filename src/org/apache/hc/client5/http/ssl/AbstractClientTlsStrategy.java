/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Arrays;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.ssl.TLS;
/*     */ import org.apache.hc.core5.http.ssl.TlsCiphers;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
/*     */ import org.apache.hc.core5.http2.ssl.H2TlsSupport;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ abstract class AbstractClientTlsStrategy
/*     */   implements TlsStrategy
/*     */ {
/*  64 */   private static final Logger LOG = LoggerFactory.getLogger(AbstractClientTlsStrategy.class);
/*     */   
/*     */   private final SSLContext sslContext;
/*     */   
/*     */   private final String[] supportedProtocols;
/*     */   
/*     */   private final String[] supportedCipherSuites;
/*     */   
/*     */   private final SSLBufferMode sslBufferManagement;
/*     */   
/*     */   private final HostnameVerifier hostnameVerifier;
/*     */   
/*     */   private final TlsSessionValidator tlsSessionValidator;
/*     */ 
/*     */   
/*     */   AbstractClientTlsStrategy(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, SSLBufferMode sslBufferManagement, HostnameVerifier hostnameVerifier) {
/*  80 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/*  81 */     this.supportedProtocols = supportedProtocols;
/*  82 */     this.supportedCipherSuites = supportedCipherSuites;
/*  83 */     this.sslBufferManagement = (sslBufferManagement != null) ? sslBufferManagement : SSLBufferMode.STATIC;
/*  84 */     this.hostnameVerifier = (hostnameVerifier != null) ? hostnameVerifier : HttpsSupport.getDefaultHostnameVerifier();
/*  85 */     this.tlsSessionValidator = new TlsSessionValidator(LOG);
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
/*     */   @Deprecated
/*     */   public boolean upgrade(TransportSecurityLayer tlsSession, HttpHost host, SocketAddress localAddress, SocketAddress remoteAddress, Object attachment, Timeout handshakeTimeout) {
/* 100 */     upgrade(tlsSession, (NamedEndpoint)host, attachment, handshakeTimeout, null);
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(TransportSecurityLayer tlsSession, NamedEndpoint endpoint, Object attachment, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) {
/* 111 */     tlsSession.startTls(this.sslContext, endpoint, this.sslBufferManagement, (e, sslEngine) -> { TlsConfig tlsConfig = (attachment instanceof TlsConfig) ? (TlsConfig)attachment : TlsConfig.DEFAULT; HttpVersionPolicy versionPolicy = tlsConfig.getHttpVersionPolicy(); SSLParameters sslParameters = sslEngine.getSSLParameters(); String[] supportedProtocols = tlsConfig.getSupportedProtocols(); if (supportedProtocols != null) { sslParameters.setProtocols(supportedProtocols); } else if (this.supportedProtocols != null) { sslParameters.setProtocols(this.supportedProtocols); } else if (versionPolicy != HttpVersionPolicy.FORCE_HTTP_1) { sslParameters.setProtocols(TLS.excludeWeak(sslParameters.getProtocols())); }  String[] supportedCipherSuites = tlsConfig.getSupportedCipherSuites(); if (supportedCipherSuites != null) { sslParameters.setCipherSuites(supportedCipherSuites); } else if (this.supportedCipherSuites != null) { sslParameters.setCipherSuites(this.supportedCipherSuites); } else if (versionPolicy == HttpVersionPolicy.FORCE_HTTP_2) { sslParameters.setCipherSuites(TlsCiphers.excludeH2Blacklisted(sslParameters.getCipherSuites())); }  if (versionPolicy != HttpVersionPolicy.FORCE_HTTP_1) H2TlsSupport.setEnableRetransmissions(sslParameters, false);  applyParameters(sslEngine, sslParameters, H2TlsSupport.selectApplicationProtocols(versionPolicy)); initializeEngine(sslEngine); if (LOG.isDebugEnabled()) { LOG.debug("Enabled protocols: {}", Arrays.asList(sslEngine.getEnabledProtocols())); LOG.debug("Enabled cipher suites:{}", Arrays.asList(sslEngine.getEnabledCipherSuites())); LOG.debug("Starting handshake ({})", handshakeTimeout); }  }(e, sslEngine) -> { verifySession(endpoint.getHostName(), sslEngine.getSession()); TlsDetails tlsDetails = createTlsDetails(sslEngine); String negotiatedCipherSuite = sslEngine.getSession().getCipherSuite(); if (tlsDetails != null && ApplicationProtocol.HTTP_2.id.equals(tlsDetails.getApplicationProtocol()) && TlsCiphers.isH2Blacklisted(negotiatedCipherSuite)) throw new SSLHandshakeException("Cipher suite `" + negotiatedCipherSuite + "` does not provide adequate security for HTTP/2");  return tlsDetails; }handshakeTimeout, callback);
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
/*     */   protected void initializeEngine(SSLEngine sslEngine) {}
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void verifySession(String hostname, SSLSession sslsession) throws SSLException {
/* 171 */     this.tlsSessionValidator.verifySession(hostname, sslsession, this.hostnameVerifier);
/*     */   }
/*     */   
/*     */   abstract void applyParameters(SSLEngine paramSSLEngine, SSLParameters paramSSLParameters, String[] paramArrayOfString);
/*     */   
/*     */   abstract TlsDetails createTlsDetails(SSLEngine paramSSLEngine);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/AbstractClientTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */