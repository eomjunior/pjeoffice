/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
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
/*     */ class AbstractHttp1IOEventHandler
/*     */   implements HttpConnectionEventHandler
/*     */ {
/*     */   final AbstractHttp1StreamDuplexer<?, ?> streamDuplexer;
/*     */   
/*     */   AbstractHttp1IOEventHandler(AbstractHttp1StreamDuplexer<?, ?> streamDuplexer) {
/*  49 */     this.streamDuplexer = (AbstractHttp1StreamDuplexer<?, ?>)Args.notNull(streamDuplexer, "Stream multiplexer");
/*     */   }
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) throws IOException {
/*     */     try {
/*  55 */       this.streamDuplexer.onConnect();
/*  56 */     } catch (HttpException ex) {
/*  57 */       this.streamDuplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void inputReady(IOSession session, ByteBuffer src) throws IOException {
/*     */     try {
/*  64 */       this.streamDuplexer.onInput(src);
/*  65 */     } catch (HttpException ex) {
/*  66 */       this.streamDuplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void outputReady(IOSession session) throws IOException {
/*     */     try {
/*  73 */       this.streamDuplexer.onOutput();
/*  74 */     } catch (HttpException ex) {
/*  75 */       this.streamDuplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void timeout(IOSession session, Timeout timeout) throws IOException {
/*     */     try {
/*  82 */       this.streamDuplexer.onTimeout(timeout);
/*  83 */     } catch (HttpException ex) {
/*  84 */       this.streamDuplexer.onException((Exception)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void exception(IOSession session, Exception cause) {
/*  90 */     this.streamDuplexer.onException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnected(IOSession session) {
/*  95 */     this.streamDuplexer.onDisconnect();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 100 */     this.streamDuplexer.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 105 */     this.streamDuplexer.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 110 */     return this.streamDuplexer.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 115 */     this.streamDuplexer.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 120 */     return this.streamDuplexer.getSSLSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 125 */     return this.streamDuplexer.getEndpointDetails();
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 130 */     return this.streamDuplexer.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 135 */     return this.streamDuplexer.getProtocolVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 140 */     return this.streamDuplexer.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 145 */     return this.streamDuplexer.getLocalAddress();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/AbstractHttp1IOEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */