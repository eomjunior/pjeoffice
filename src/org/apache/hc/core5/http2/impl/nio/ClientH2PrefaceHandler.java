/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.nio.BufferedData;
/*     */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
/*     */ import org.apache.hc.core5.io.CloseMode;
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
/*     */ @Internal
/*     */ public class ClientH2PrefaceHandler
/*     */   extends PrefaceHandlerBase
/*     */ {
/*  56 */   static final byte[] PREFACE = new byte[] { 80, 82, 73, 32, 42, 32, 72, 84, 84, 80, 47, 50, 46, 48, 13, 10, 13, 10, 83, 77, 13, 10, 13, 10 };
/*     */ 
/*     */   
/*     */   private final ClientH2StreamMultiplexerFactory http2StreamHandlerFactory;
/*     */ 
/*     */   
/*     */   private final boolean strictALPNHandshake;
/*     */   
/*     */   private final AtomicBoolean initialized;
/*     */   
/*     */   private volatile ByteBuffer preface;
/*     */   
/*     */   private volatile BufferedData inBuf;
/*     */ 
/*     */   
/*     */   public ClientH2PrefaceHandler(ProtocolIOSession ioSession, ClientH2StreamMultiplexerFactory http2StreamHandlerFactory, boolean strictALPNHandshake) {
/*  72 */     this(ioSession, http2StreamHandlerFactory, strictALPNHandshake, (FutureCallback<ProtocolIOSession>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientH2PrefaceHandler(ProtocolIOSession ioSession, ClientH2StreamMultiplexerFactory http2StreamHandlerFactory, boolean strictALPNHandshake, FutureCallback<ProtocolIOSession> resultCallback) {
/*  83 */     super(ioSession, resultCallback);
/*  84 */     this.http2StreamHandlerFactory = (ClientH2StreamMultiplexerFactory)Args.notNull(http2StreamHandlerFactory, "HTTP/2 stream handler factory");
/*  85 */     this.strictALPNHandshake = strictALPNHandshake;
/*  86 */     this.initialized = new AtomicBoolean();
/*     */   }
/*     */   
/*     */   private void initialize() throws IOException {
/*  90 */     TlsDetails tlsDetails = this.ioSession.getTlsDetails();
/*  91 */     if (tlsDetails != null) {
/*  92 */       String applicationProtocol = tlsDetails.getApplicationProtocol();
/*  93 */       if (TextUtils.isEmpty(applicationProtocol)) {
/*  94 */         if (this.strictALPNHandshake) {
/*  95 */           throw new ProtocolNegotiationException("ALPN: missing application protocol");
/*     */         }
/*     */       }
/*  98 */       else if (!ApplicationProtocol.HTTP_2.id.equals(applicationProtocol)) {
/*  99 */         throw new ProtocolNegotiationException("ALPN: unexpected application protocol '" + applicationProtocol + "'");
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     this.preface = ByteBuffer.wrap(PREFACE);
/* 104 */     this.ioSession.setEvent(4);
/*     */   }
/*     */   
/*     */   private void writeOutPreface(IOSession session) throws IOException {
/* 108 */     if (this.preface.hasRemaining()) {
/* 109 */       session.write(this.preface);
/*     */     }
/* 111 */     if (!this.preface.hasRemaining()) {
/* 112 */       session.clearEvent(4);
/* 113 */       ByteBuffer data = (this.inBuf != null) ? this.inBuf.data() : null;
/* 114 */       startProtocol(new ClientH2IOEventHandler(this.http2StreamHandlerFactory.create(this.ioSession)), data);
/* 115 */       if (this.inBuf != null) {
/* 116 */         this.inBuf.clear();
/*     */       }
/* 118 */       this.preface = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) throws IOException {
/* 124 */     if (this.initialized.compareAndSet(false, true)) {
/* 125 */       initialize();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void outputReady(IOSession session) throws IOException {
/* 131 */     if (this.initialized.compareAndSet(false, true)) {
/* 132 */       initialize();
/*     */     }
/* 134 */     if (this.preface != null) {
/* 135 */       writeOutPreface(session);
/*     */     } else {
/* 137 */       throw new ProtocolNegotiationException("Unexpected output");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void inputReady(IOSession session, ByteBuffer src) throws IOException {
/* 143 */     if (src != null) {
/* 144 */       if (this.inBuf == null) {
/* 145 */         this.inBuf = BufferedData.allocate(src.remaining());
/*     */       }
/* 147 */       this.inBuf.put(src);
/*     */     } 
/* 149 */     if (this.preface != null) {
/* 150 */       writeOutPreface(session);
/*     */     } else {
/* 152 */       throw new ProtocolNegotiationException("Unexpected input");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return getClass().getName();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ClientH2PrefaceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */