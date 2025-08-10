/*     */ package org.apache.hc.client5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.client5.http.nio.ManagedAsyncClientConnection;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLSessionInitializer;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLSessionVerifier;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.util.Identifiable;
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
/*     */ final class DefaultManagedAsyncClientConnection
/*     */   implements ManagedAsyncClientConnection, Identifiable
/*     */ {
/*  62 */   private static final Logger LOG = LoggerFactory.getLogger(DefaultManagedAsyncClientConnection.class);
/*     */   
/*     */   private final IOSession ioSession;
/*     */   private final Timeout socketTimeout;
/*     */   private final AtomicBoolean closed;
/*     */   
/*     */   public DefaultManagedAsyncClientConnection(IOSession ioSession) {
/*  69 */     this.ioSession = ioSession;
/*  70 */     this.socketTimeout = ioSession.getSocketTimeout();
/*  71 */     this.closed = new AtomicBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  76 */     return this.ioSession.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/*  81 */     if (this.closed.compareAndSet(false, true)) {
/*  82 */       if (LOG.isDebugEnabled()) {
/*  83 */         LOG.debug("{} Shutdown connection {}", getId(), closeMode);
/*     */       }
/*  85 */       this.ioSession.close(closeMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  91 */     if (this.closed.compareAndSet(false, true)) {
/*  92 */       if (LOG.isDebugEnabled()) {
/*  93 */         LOG.debug("{} Close connection", getId());
/*     */       }
/*  95 */       this.ioSession.enqueue((Command)new ShutdownCommand(CloseMode.GRACEFUL), Command.Priority.IMMEDIATE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 101 */     return this.ioSession.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 106 */     this.ioSession.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 111 */     return this.ioSession.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 116 */     return this.ioSession.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 121 */     return this.ioSession.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 126 */     IOEventHandler handler = this.ioSession.getHandler();
/* 127 */     if (handler instanceof HttpConnection) {
/* 128 */       return ((HttpConnection)handler).getEndpointDetails();
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 135 */     IOEventHandler handler = this.ioSession.getHandler();
/* 136 */     if (handler instanceof HttpConnection) {
/* 137 */       ProtocolVersion version = ((HttpConnection)handler).getProtocolVersion();
/* 138 */       if (version != null) {
/* 139 */         return version;
/*     */       }
/*     */     } 
/* 142 */     return (ProtocolVersion)HttpVersion.DEFAULT;
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
/*     */   public void startTls(SSLContext sslContext, NamedEndpoint endpoint, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) throws UnsupportedOperationException {
/* 154 */     if (LOG.isDebugEnabled()) {
/* 155 */       LOG.debug("{} start TLS", getId());
/*     */     }
/* 157 */     if (this.ioSession instanceof TransportSecurityLayer) {
/* 158 */       ((TransportSecurityLayer)this.ioSession).startTls(sslContext, endpoint, sslBufferMode, initializer, verifier, handshakeTimeout, callback);
/*     */     } else {
/*     */       
/* 161 */       throw new UnsupportedOperationException("TLS upgrade not supported");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTls(SSLContext sslContext, NamedEndpoint endpoint, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier, Timeout handshakeTimeout) throws UnsupportedOperationException {
/* 173 */     startTls(sslContext, endpoint, sslBufferMode, initializer, verifier, handshakeTimeout, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public TlsDetails getTlsDetails() {
/* 178 */     return (this.ioSession instanceof TransportSecurityLayer) ? ((TransportSecurityLayer)this.ioSession).getTlsDetails() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 183 */     TlsDetails tlsDetails = getTlsDetails();
/* 184 */     return (tlsDetails != null) ? tlsDetails.getSSLSession() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitCommand(Command command, Command.Priority priority) {
/* 189 */     if (LOG.isDebugEnabled()) {
/* 190 */       LOG.debug("{} {} with {} priority", new Object[] { getId(), command.getClass().getSimpleName(), priority });
/*     */     }
/* 192 */     this.ioSession.enqueue(command, Command.Priority.IMMEDIATE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void passivate() {
/* 197 */     this.ioSession.setSocketTimeout(Timeout.ZERO_MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activate() {
/* 202 */     this.ioSession.setSocketTimeout(this.socketTimeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void switchProtocol(String protocolId, FutureCallback<ProtocolIOSession> callback) throws UnsupportedOperationException {
/* 208 */     if (this.ioSession instanceof ProtocolIOSession) {
/* 209 */       ((ProtocolIOSession)this.ioSession).switchProtocol(protocolId, callback);
/*     */     } else {
/* 211 */       throw new UnsupportedOperationException("Protocol switch not supported");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/nio/DefaultManagedAsyncClientConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */