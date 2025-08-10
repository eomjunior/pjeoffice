/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.nio.HttpConnectionEventHandler;
/*     */ import org.apache.hc.core5.http.nio.command.CommandSupport;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.SocketTimeoutExceptionFactory;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ abstract class PrefaceHandlerBase
/*     */   implements HttpConnectionEventHandler
/*     */ {
/*     */   final ProtocolIOSession ioSession;
/*     */   private final AtomicReference<HttpConnectionEventHandler> protocolHandlerRef;
/*     */   private final FutureCallback<ProtocolIOSession> resultCallback;
/*     */   private final AtomicBoolean completed;
/*     */   
/*     */   PrefaceHandlerBase(ProtocolIOSession ioSession, FutureCallback<ProtocolIOSession> resultCallback) {
/*  63 */     this.ioSession = (ProtocolIOSession)Args.notNull(ioSession, "I/O session");
/*  64 */     this.protocolHandlerRef = new AtomicReference<>();
/*  65 */     this.resultCallback = resultCallback;
/*  66 */     this.completed = new AtomicBoolean();
/*     */   }
/*     */   
/*     */   void startProtocol(HttpConnectionEventHandler protocolHandler, ByteBuffer data) throws IOException {
/*  70 */     this.protocolHandlerRef.set(protocolHandler);
/*  71 */     this.ioSession.upgrade((IOEventHandler)protocolHandler);
/*  72 */     protocolHandler.connected((IOSession)this.ioSession);
/*  73 */     if (data != null && data.hasRemaining()) {
/*  74 */       protocolHandler.inputReady((IOSession)this.ioSession, data);
/*     */     }
/*  76 */     if (this.completed.compareAndSet(false, true) && this.resultCallback != null) {
/*  77 */       this.resultCallback.completed(this.ioSession);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void timeout(IOSession session, Timeout timeout) {
/*  83 */     exception(session, SocketTimeoutExceptionFactory.create(timeout));
/*     */   }
/*     */ 
/*     */   
/*     */   public void exception(IOSession session, Exception cause) {
/*  88 */     HttpConnectionEventHandler protocolHandler = this.protocolHandlerRef.get();
/*     */     try {
/*  90 */       session.close(CloseMode.IMMEDIATE);
/*  91 */       if (protocolHandler != null) {
/*  92 */         protocolHandler.exception(session, cause);
/*     */       } else {
/*  94 */         CommandSupport.failCommands(session, cause);
/*     */       } 
/*  96 */     } catch (Exception ex) {
/*  97 */       if (this.completed.compareAndSet(false, true) && this.resultCallback != null) {
/*  98 */         this.resultCallback.failed(ex);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnected(IOSession session) {
/* 105 */     HttpConnectionEventHandler protocolHandler = this.protocolHandlerRef.getAndSet(null);
/*     */     try {
/* 107 */       if (protocolHandler != null) {
/* 108 */         protocolHandler.disconnected((IOSession)this.ioSession);
/*     */       } else {
/* 110 */         CommandSupport.cancelCommands(session);
/*     */       } 
/*     */     } finally {
/* 113 */       if (this.completed.compareAndSet(false, true) && this.resultCallback != null) {
/* 114 */         this.resultCallback.failed((Exception)new ConnectionClosedException());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 121 */     TlsDetails tlsDetails = this.ioSession.getTlsDetails();
/* 122 */     return (tlsDetails != null) ? tlsDetails.getSSLSession() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 132 */     this.ioSession.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 137 */     return this.ioSession.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 142 */     return (ProtocolVersion)HttpVersion.HTTP_2;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 147 */     return this.ioSession.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 152 */     return this.ioSession.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 157 */     return this.ioSession.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 162 */     this.ioSession.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 167 */     this.ioSession.close(closeMode);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/PrefaceHandlerBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */