/*     */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
/*     */ 
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncRequester;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.pool.ManagedConnPool;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
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
/*     */ public class H2AsyncRequester
/*     */   extends HttpAsyncRequester
/*     */ {
/*     */   private final HttpVersionPolicy versionPolicy;
/*     */   
/*     */   @Internal
/*     */   public H2AsyncRequester(HttpVersionPolicy versionPolicy, IOReactorConfig ioReactorConfig, IOEventHandlerFactory eventHandlerFactory, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, ManagedConnPool<HttpHost, IOSession> connPool) {
/*  75 */     super(ioReactorConfig, eventHandlerFactory, ioSessionDecorator, exceptionCallback, sessionListener, connPool);
/*  76 */     this.versionPolicy = (versionPolicy != null) ? versionPolicy : HttpVersionPolicy.NEGOTIATE;
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
/*     */   @Internal
/*     */   public H2AsyncRequester(HttpVersionPolicy versionPolicy, IOReactorConfig ioReactorConfig, IOEventHandlerFactory eventHandlerFactory, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, ManagedConnPool<HttpHost, IOSession> connPool, TlsStrategy tlsStrategy, Timeout handshakeTimeout) {
/*  95 */     super(ioReactorConfig, eventHandlerFactory, ioSessionDecorator, exceptionCallback, sessionListener, connPool, tlsStrategy, handshakeTimeout);
/*     */     
/*  97 */     this.versionPolicy = (versionPolicy != null) ? versionPolicy : HttpVersionPolicy.NEGOTIATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<AsyncClientEndpoint> doConnect(HttpHost host, Timeout timeout, Object attachment, FutureCallback<AsyncClientEndpoint> callback) {
/* 106 */     return super.doConnect(host, timeout, (attachment != null) ? attachment : this.versionPolicy, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doTlsUpgrade(ProtocolIOSession ioSession, NamedEndpoint endpoint, final FutureCallback<ProtocolIOSession> callback) {
/* 113 */     super.doTlsUpgrade(ioSession, endpoint, (FutureCallback)new CallbackContribution<ProtocolIOSession>(callback) {
/*     */           public void completed(ProtocolIOSession protocolSession) {
/*     */             boolean switchProtocol;
/*     */             TlsDetails tlsDetails;
/*     */             String appProtocol;
/* 118 */             switch (H2AsyncRequester.this.versionPolicy) {
/*     */               case FORCE_HTTP_2:
/* 120 */                 switchProtocol = true;
/*     */                 break;
/*     */               case NEGOTIATE:
/* 123 */                 tlsDetails = protocolSession.getTlsDetails();
/* 124 */                 appProtocol = (tlsDetails != null) ? tlsDetails.getApplicationProtocol() : null;
/* 125 */                 switchProtocol = ApplicationProtocol.HTTP_2.id.equals(appProtocol);
/*     */                 break;
/*     */               default:
/* 128 */                 switchProtocol = false; break;
/*     */             } 
/* 130 */             if (switchProtocol) {
/* 131 */               protocolSession.switchProtocol(ApplicationProtocol.HTTP_2.id, callback);
/*     */             }
/* 133 */             else if (callback != null) {
/* 134 */               callback.completed(protocolSession);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/H2AsyncRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */