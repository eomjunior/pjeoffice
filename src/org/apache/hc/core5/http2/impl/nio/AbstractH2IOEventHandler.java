/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.nio.HttpConnectionEventHandler;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.IOSession;
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
/*     */ class AbstractH2IOEventHandler
/*     */   implements HttpConnectionEventHandler
/*     */ {
/*     */   final AbstractH2StreamMultiplexer streamMultiplexer;
/*     */   
/*     */   AbstractH2IOEventHandler(AbstractH2StreamMultiplexer streamMultiplexer) {
/*  50 */     this.streamMultiplexer = (AbstractH2StreamMultiplexer)Args.notNull(streamMultiplexer, "Stream multiplexer");
/*     */   }
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) throws IOException {
/*     */     try {
/*  56 */       this.streamMultiplexer.onConnect();
/*  57 */     } catch (HttpException ex) {
/*  58 */       this.streamMultiplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void inputReady(IOSession session, ByteBuffer src) throws IOException {
/*     */     try {
/*  65 */       this.streamMultiplexer.onInput(src);
/*  66 */     } catch (HttpException ex) {
/*  67 */       this.streamMultiplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void outputReady(IOSession session) throws IOException {
/*     */     try {
/*  74 */       this.streamMultiplexer.onOutput();
/*  75 */     } catch (HttpException ex) {
/*  76 */       this.streamMultiplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void timeout(IOSession session, Timeout timeout) throws IOException {
/*     */     try {
/*  83 */       this.streamMultiplexer.onTimeout(timeout);
/*  84 */     } catch (HttpException ex) {
/*  85 */       this.streamMultiplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void exception(IOSession session, Exception cause) {
/*  91 */     this.streamMultiplexer.onException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnected(IOSession session) {
/*  96 */     this.streamMultiplexer.onDisconnect();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 101 */     this.streamMultiplexer.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 106 */     this.streamMultiplexer.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 111 */     return this.streamMultiplexer.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 116 */     this.streamMultiplexer.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 121 */     return this.streamMultiplexer.getSSLSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 126 */     return this.streamMultiplexer.getEndpointDetails();
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 131 */     return this.streamMultiplexer.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 136 */     return this.streamMultiplexer.getProtocolVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 141 */     return this.streamMultiplexer.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 146 */     return this.streamMultiplexer.getLocalAddress();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/AbstractH2IOEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */