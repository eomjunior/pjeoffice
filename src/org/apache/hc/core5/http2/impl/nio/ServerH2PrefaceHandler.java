/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.nio.BufferedData;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
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
/*     */ @Internal
/*     */ public class ServerH2PrefaceHandler
/*     */   extends PrefaceHandlerBase
/*     */ {
/*  50 */   static final byte[] PREFACE = ClientH2PrefaceHandler.PREFACE;
/*     */   
/*     */   private final ServerH2StreamMultiplexerFactory http2StreamHandlerFactory;
/*     */   
/*     */   private final BufferedData inBuf;
/*     */ 
/*     */   
/*     */   public ServerH2PrefaceHandler(ProtocolIOSession ioSession, ServerH2StreamMultiplexerFactory http2StreamHandlerFactory) {
/*  58 */     this(ioSession, http2StreamHandlerFactory, (FutureCallback<ProtocolIOSession>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerH2PrefaceHandler(ProtocolIOSession ioSession, ServerH2StreamMultiplexerFactory http2StreamHandlerFactory, FutureCallback<ProtocolIOSession> resultCallback) {
/*  65 */     super(ioSession, resultCallback);
/*  66 */     this.http2StreamHandlerFactory = (ServerH2StreamMultiplexerFactory)Args.notNull(http2StreamHandlerFactory, "HTTP/2 stream handler factory");
/*  67 */     this.inBuf = BufferedData.allocate(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) throws IOException {}
/*     */ 
/*     */   
/*     */   public void inputReady(IOSession session, ByteBuffer src) throws IOException {
/*  76 */     if (src != null) {
/*  77 */       this.inBuf.put(src);
/*     */     }
/*  79 */     boolean endOfStream = false;
/*  80 */     if (this.inBuf.length() < PREFACE.length) {
/*  81 */       int bytesRead = this.inBuf.readFrom((ReadableByteChannel)session);
/*  82 */       if (bytesRead == -1) {
/*  83 */         endOfStream = true;
/*     */       }
/*     */     } 
/*  86 */     ByteBuffer data = this.inBuf.data();
/*  87 */     if (data.remaining() >= PREFACE.length) {
/*  88 */       for (int i = 0; i < PREFACE.length; i++) {
/*  89 */         if (data.get() != PREFACE[i]) {
/*  90 */           throw new ProtocolNegotiationException("Unexpected HTTP/2 preface");
/*     */         }
/*     */       } 
/*  93 */       startProtocol(new ServerH2IOEventHandler(this.http2StreamHandlerFactory.create(this.ioSession)), data.hasRemaining() ? data : null);
/*     */     }
/*  95 */     else if (endOfStream) {
/*  96 */       throw new ConnectionClosedException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void outputReady(IOSession session) throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return getClass().getName();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerH2PrefaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */