/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.BasicEndpointDetails;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.io.BHttpConnection;
/*     */ import org.apache.hc.core5.http.io.HttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.io.SessionInputBuffer;
/*     */ import org.apache.hc.core5.http.io.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.http.io.entity.EmptyInputStream;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.net.InetAddressUtils;
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
/*     */ class BHttpConnectionBase
/*     */   implements BHttpConnection
/*     */ {
/*  70 */   private static final Timeout STALE_CHECK_TIMEOUT = Timeout.ofMilliseconds(1L);
/*     */   
/*     */   final Http1Config http1Config;
/*     */   
/*     */   final SessionInputBufferImpl inBuffer;
/*     */   
/*     */   final SessionOutputBufferImpl outbuffer;
/*     */   
/*     */   final BasicHttpConnectionMetrics connMetrics;
/*     */   
/*     */   final AtomicReference<SocketHolder> socketHolderRef;
/*     */   private byte[] chunkedRequestBuffer;
/*     */   volatile ProtocolVersion version;
/*     */   volatile EndpointDetails endpointDetails;
/*     */   
/*     */   BHttpConnectionBase(Http1Config http1Config, CharsetDecoder charDecoder, CharsetEncoder charEncoder) {
/*  86 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/*  87 */     BasicHttpTransportMetrics inTransportMetrics = new BasicHttpTransportMetrics();
/*  88 */     BasicHttpTransportMetrics outTransportMetrics = new BasicHttpTransportMetrics();
/*  89 */     this
/*     */       
/*  91 */       .inBuffer = new SessionInputBufferImpl(inTransportMetrics, this.http1Config.getBufferSize(), -1, this.http1Config.getMaxLineLength(), charDecoder);
/*  92 */     this
/*     */       
/*  94 */       .outbuffer = new SessionOutputBufferImpl(outTransportMetrics, this.http1Config.getBufferSize(), this.http1Config.getChunkSizeHint(), charEncoder);
/*  95 */     this.connMetrics = new BasicHttpConnectionMetrics((HttpTransportMetrics)inTransportMetrics, (HttpTransportMetrics)outTransportMetrics);
/*  96 */     this.socketHolderRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   protected SocketHolder ensureOpen() throws IOException {
/* 100 */     SocketHolder socketHolder = this.socketHolderRef.get();
/* 101 */     if (socketHolder == null) {
/* 102 */       throw new ConnectionClosedException();
/*     */     }
/* 104 */     return socketHolder;
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
/*     */   protected void bind(Socket socket) throws IOException {
/* 118 */     Args.notNull(socket, "Socket");
/* 119 */     bind(new SocketHolder(socket));
/*     */   }
/*     */   
/*     */   protected void bind(SocketHolder socketHolder) throws IOException {
/* 123 */     Args.notNull(socketHolder, "Socket holder");
/* 124 */     this.socketHolderRef.set(socketHolder);
/* 125 */     this.endpointDetails = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 130 */     return (this.socketHolderRef.get() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 138 */     return this.version;
/*     */   }
/*     */   
/*     */   protected SocketHolder getSocketHolder() {
/* 142 */     return this.socketHolderRef.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream createContentOutputStream(long len, SessionOutputBuffer buffer, OutputStream outputStream, Supplier<List<? extends Header>> trailers) {
/* 150 */     if (len >= 0L)
/* 151 */       return new ContentLengthOutputStream(buffer, outputStream, len); 
/* 152 */     if (len == -1L) {
/* 153 */       return new ChunkedOutputStream(buffer, outputStream, getChunkedRequestBuffer(), trailers);
/*     */     }
/* 155 */     return new IdentityOutputStream(buffer, outputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] getChunkedRequestBuffer() {
/* 160 */     if (this.chunkedRequestBuffer == null) {
/* 161 */       int chunkSizeHint = this.http1Config.getChunkSizeHint();
/* 162 */       this.chunkedRequestBuffer = new byte[(chunkSizeHint > 0) ? chunkSizeHint : 8192];
/*     */     } 
/* 164 */     return this.chunkedRequestBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream createContentInputStream(long len, SessionInputBuffer buffer, InputStream inputStream) {
/* 171 */     if (len > 0L)
/* 172 */       return new ContentLengthInputStream(buffer, inputStream, len); 
/* 173 */     if (len == 0L)
/* 174 */       return (InputStream)EmptyInputStream.INSTANCE; 
/* 175 */     if (len == -1L) {
/* 176 */       return new ChunkedInputStream(buffer, inputStream, this.http1Config);
/*     */     }
/* 178 */     return new IdentityInputStream(buffer, inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HttpEntity createIncomingEntity(HttpMessage message, SessionInputBuffer inBuffer, InputStream inputStream, long len) {
/* 187 */     return new IncomingHttpEntity(
/* 188 */         createContentInputStream(len, inBuffer, inputStream), (len >= 0L) ? len : -1L, (len == -1L), message
/*     */         
/* 190 */         .getFirstHeader("Content-Type"), message
/* 191 */         .getFirstHeader("Content-Encoding"));
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 196 */     SocketHolder socketHolder = this.socketHolderRef.get();
/* 197 */     return (socketHolder != null) ? socketHolder.getSocket().getRemoteSocketAddress() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 202 */     SocketHolder socketHolder = this.socketHolderRef.get();
/* 203 */     return (socketHolder != null) ? socketHolder.getSocket().getLocalSocketAddress() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 208 */     SocketHolder socketHolder = this.socketHolderRef.get();
/* 209 */     if (socketHolder != null) {
/*     */       try {
/* 211 */         socketHolder.getSocket().setSoTimeout(Timeout.defaultsToDisabled(timeout).toMillisecondsIntBound());
/* 212 */       } catch (SocketException socketException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 222 */     SocketHolder socketHolder = this.socketHolderRef.get();
/* 223 */     if (socketHolder != null) {
/*     */       try {
/* 225 */         return Timeout.ofMilliseconds(socketHolder.getSocket().getSoTimeout());
/* 226 */       } catch (SocketException socketException) {}
/*     */     }
/*     */     
/* 229 */     return Timeout.DISABLED;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 234 */     SocketHolder socketHolder = this.socketHolderRef.getAndSet(null);
/* 235 */     if (socketHolder != null) {
/* 236 */       Socket socket = socketHolder.getSocket();
/*     */       
/* 238 */       try { if (closeMode == CloseMode.IMMEDIATE)
/*     */         {
/* 240 */           socket.setSoLinger(true, 0);
/*     */         } }
/* 242 */       catch (IOException iOException) {  }
/*     */       finally
/* 244 */       { Closer.closeQuietly(socket); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 251 */     SocketHolder socketHolder = this.socketHolderRef.getAndSet(null);
/* 252 */     if (socketHolder != null) {
/* 253 */       try (Socket socket = socketHolder.getSocket()) {
/* 254 */         this.inBuffer.clear();
/* 255 */         this.outbuffer.flush(socketHolder.getOutputStream());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private int fillInputBuffer(Timeout timeout) throws IOException {
/* 261 */     SocketHolder socketHolder = ensureOpen();
/* 262 */     Socket socket = socketHolder.getSocket();
/* 263 */     int oldtimeout = socket.getSoTimeout();
/*     */     try {
/* 265 */       socket.setSoTimeout(timeout.toMillisecondsIntBound());
/* 266 */       return this.inBuffer.fillBuffer(socketHolder.getInputStream());
/*     */     } finally {
/* 268 */       socket.setSoTimeout(oldtimeout);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean awaitInput(Timeout timeout) throws IOException {
/* 273 */     if (this.inBuffer.hasBufferedData()) {
/* 274 */       return true;
/*     */     }
/* 276 */     fillInputBuffer(timeout);
/* 277 */     return this.inBuffer.hasBufferedData();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDataAvailable(Timeout timeout) throws IOException {
/* 282 */     ensureOpen();
/*     */     try {
/* 284 */       return awaitInput(timeout);
/* 285 */     } catch (SocketTimeoutException ex) {
/* 286 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() throws IOException {
/* 292 */     if (!isOpen()) {
/* 293 */       return true;
/*     */     }
/*     */     try {
/* 296 */       int bytesRead = fillInputBuffer(STALE_CHECK_TIMEOUT);
/* 297 */       return (bytesRead < 0);
/* 298 */     } catch (SocketTimeoutException ex) {
/* 299 */       return false;
/* 300 */     } catch (SocketException ex) {
/* 301 */       return true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 307 */     SocketHolder socketHolder = ensureOpen();
/* 308 */     this.outbuffer.flush(socketHolder.getOutputStream());
/*     */   }
/*     */   
/*     */   protected void incrementRequestCount() {
/* 312 */     this.connMetrics.incrementRequestCount();
/*     */   }
/*     */   
/*     */   protected void incrementResponseCount() {
/* 316 */     this.connMetrics.incrementResponseCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 321 */     SocketHolder socketHolder = this.socketHolderRef.get();
/* 322 */     if (socketHolder != null) {
/* 323 */       Socket socket = socketHolder.getSocket();
/* 324 */       return (socket instanceof SSLSocket) ? ((SSLSocket)socket).getSession() : null;
/*     */     } 
/* 326 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 331 */     if (this.endpointDetails == null) {
/* 332 */       SocketHolder socketHolder = this.socketHolderRef.get();
/* 333 */       if (socketHolder != null) {
/*     */         Timeout socketTimeout;
/* 335 */         Socket socket = socketHolder.getSocket();
/*     */         
/*     */         try {
/* 338 */           socketTimeout = Timeout.ofMilliseconds(socket.getSoTimeout());
/* 339 */         } catch (SocketException e) {
/* 340 */           socketTimeout = Timeout.DISABLED;
/*     */         } 
/* 342 */         this
/*     */           
/* 344 */           .endpointDetails = (EndpointDetails)new BasicEndpointDetails(socket.getRemoteSocketAddress(), socket.getLocalSocketAddress(), (HttpConnectionMetrics)this.connMetrics, socketTimeout);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 349 */     return this.endpointDetails;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 354 */     SocketHolder socketHolder = this.socketHolderRef.get();
/* 355 */     if (socketHolder != null) {
/* 356 */       Socket socket = socketHolder.getSocket();
/* 357 */       StringBuilder buffer = new StringBuilder();
/* 358 */       SocketAddress remoteAddress = socket.getRemoteSocketAddress();
/* 359 */       SocketAddress localAddress = socket.getLocalSocketAddress();
/* 360 */       if (remoteAddress != null && localAddress != null) {
/* 361 */         InetAddressUtils.formatAddress(buffer, localAddress);
/* 362 */         buffer.append("<->");
/* 363 */         InetAddressUtils.formatAddress(buffer, remoteAddress);
/*     */       } 
/* 365 */       return buffer.toString();
/*     */     } 
/* 367 */     return "[Not bound]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/BHttpConnectionBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */