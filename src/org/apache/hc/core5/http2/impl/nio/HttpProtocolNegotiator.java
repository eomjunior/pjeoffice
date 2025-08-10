/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.nio.HttpConnectionEventHandler;
/*     */ import org.apache.hc.core5.http.nio.command.CommandSupport;
/*     */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.SocketTimeoutExceptionFactory;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TextUtils;
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
/*     */ @Internal
/*     */ public class HttpProtocolNegotiator
/*     */   implements HttpConnectionEventHandler
/*     */ {
/*     */   private final ProtocolIOSession ioSession;
/*     */   private final FutureCallback<ProtocolIOSession> resultCallback;
/*     */   private final AtomicBoolean completed;
/*     */   private final AtomicReference<ProtocolVersion> negotiatedProtocolRef;
/*     */   
/*     */   public HttpProtocolNegotiator(ProtocolIOSession ioSession, FutureCallback<ProtocolIOSession> resultCallback) {
/*  70 */     this.ioSession = (ProtocolIOSession)Args.notNull(ioSession, "I/O session");
/*  71 */     this.resultCallback = resultCallback;
/*  72 */     this.completed = new AtomicBoolean();
/*  73 */     this.negotiatedProtocolRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   void startProtocol(HttpVersion httpVersion) {
/*  77 */     this.ioSession.switchProtocol((httpVersion == HttpVersion.HTTP_2) ? ApplicationProtocol.HTTP_2.id : ApplicationProtocol.HTTP_1_1.id, this.resultCallback);
/*     */ 
/*     */     
/*  80 */     this.negotiatedProtocolRef.set(httpVersion);
/*     */   }
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) throws IOException {
/*     */     HttpVersion httpVersion;
/*  86 */     TlsDetails tlsDetails = this.ioSession.getTlsDetails();
/*  87 */     if (tlsDetails != null) {
/*  88 */       String appProtocol = tlsDetails.getApplicationProtocol();
/*  89 */       if (TextUtils.isEmpty(appProtocol)) {
/*  90 */         httpVersion = HttpVersion.HTTP_1_1;
/*  91 */       } else if (appProtocol.equals(ApplicationProtocol.HTTP_1_1.id)) {
/*  92 */         httpVersion = HttpVersion.HTTP_1_1;
/*  93 */       } else if (appProtocol.equals(ApplicationProtocol.HTTP_2.id)) {
/*  94 */         httpVersion = HttpVersion.HTTP_2;
/*     */       } else {
/*  96 */         throw new ProtocolNegotiationException("Unsupported application protocol: " + appProtocol);
/*     */       } 
/*     */     } else {
/*  99 */       httpVersion = HttpVersion.HTTP_1_1;
/*     */     } 
/* 101 */     startProtocol(httpVersion);
/*     */   }
/*     */   
/*     */   public void inputReady(IOSession session, ByteBuffer src) throws IOException {
/* 105 */     throw new ProtocolNegotiationException("Unexpected input");
/*     */   }
/*     */ 
/*     */   
/*     */   public void outputReady(IOSession session) throws IOException {
/* 110 */     throw new ProtocolNegotiationException("Unexpected output");
/*     */   }
/*     */ 
/*     */   
/*     */   public void timeout(IOSession session, Timeout timeout) {
/* 115 */     exception(session, SocketTimeoutExceptionFactory.create(timeout));
/*     */   }
/*     */ 
/*     */   
/*     */   public void exception(IOSession session, Exception cause) {
/*     */     try {
/* 121 */       session.close(CloseMode.IMMEDIATE);
/* 122 */       CommandSupport.failCommands(session, cause);
/* 123 */     } catch (Exception ex) {
/* 124 */       if (this.completed.compareAndSet(false, true) && this.resultCallback != null) {
/* 125 */         this.resultCallback.failed(ex);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnected(IOSession session) {
/*     */     try {
/* 133 */       CommandSupport.cancelCommands(session);
/*     */     } finally {
/* 135 */       if (this.completed.compareAndSet(false, true) && this.resultCallback != null) {
/* 136 */         this.resultCallback.failed((Exception)new ConnectionClosedException());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 143 */     TlsDetails tlsDetails = this.ioSession.getTlsDetails();
/* 144 */     return (tlsDetails != null) ? tlsDetails.getSSLSession() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 154 */     this.ioSession.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 159 */     return this.ioSession.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 164 */     return this.negotiatedProtocolRef.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 169 */     return this.ioSession.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 174 */     return this.ioSession.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 179 */     return this.ioSession.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 184 */     this.ioSession.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 189 */     this.ioSession.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 194 */     return getClass().getName();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/HttpProtocolNegotiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */