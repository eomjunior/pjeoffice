/*     */ package org.apache.hc.client5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultBHttpClientConnection;
/*     */ import org.apache.hc.core5.http.impl.io.SocketHolder;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.io.ResponseOutOfOrderStrategy;
/*     */ import org.apache.hc.core5.http.message.RequestLine;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.io.CloseMode;
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
/*     */ final class DefaultManagedHttpClientConnection
/*     */   extends DefaultBHttpClientConnection
/*     */   implements ManagedHttpClientConnection, Identifiable
/*     */ {
/*  62 */   private static final Logger LOG = LoggerFactory.getLogger(DefaultManagedHttpClientConnection.class);
/*  63 */   private static final Logger HEADER_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http.headers");
/*  64 */   private static final Logger WIRE_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http.wire");
/*     */ 
/*     */ 
/*     */   
/*     */   private final String id;
/*     */ 
/*     */ 
/*     */   
/*     */   private final AtomicBoolean closed;
/*     */ 
/*     */ 
/*     */   
/*     */   private Timeout socketTimeout;
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultManagedHttpClientConnection(String id, CharsetDecoder charDecoder, CharsetEncoder charEncoder, Http1Config h1Config, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, ResponseOutOfOrderStrategy responseOutOfOrderStrategy, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/*  81 */     super(h1Config, charDecoder, charEncoder, incomingContentStrategy, outgoingContentStrategy, responseOutOfOrderStrategy, requestWriterFactory, responseParserFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     this.id = id;
/*  91 */     this.closed = new AtomicBoolean();
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
/*     */   public DefaultManagedHttpClientConnection(String id, CharsetDecoder charDecoder, CharsetEncoder charEncoder, Http1Config h1Config, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 103 */     this(id, charDecoder, charEncoder, h1Config, incomingContentStrategy, outgoingContentStrategy, null, requestWriterFactory, responseParserFactory);
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
/*     */   public DefaultManagedHttpClientConnection(String id) {
/* 116 */     this(id, null, null, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 121 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(SocketHolder socketHolder) throws IOException {
/* 126 */     if (this.closed.get()) {
/* 127 */       Socket socket = socketHolder.getSocket();
/* 128 */       socket.close();
/*     */       
/* 130 */       throw new InterruptedIOException("Connection already shutdown");
/*     */     } 
/* 132 */     super.bind(socketHolder);
/* 133 */     this.socketTimeout = Timeout.ofMilliseconds(socketHolder.getSocket().getSoTimeout());
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 138 */     SocketHolder socketHolder = getSocketHolder();
/* 139 */     return (socketHolder != null) ? socketHolder.getSocket() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 144 */     Socket socket = getSocket();
/* 145 */     if (socket instanceof SSLSocket) {
/* 146 */       return ((SSLSocket)socket).getSession();
/*     */     }
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 154 */     if (this.closed.compareAndSet(false, true)) {
/* 155 */       if (LOG.isDebugEnabled()) {
/* 156 */         LOG.debug("{} Close connection", this.id);
/*     */       }
/* 158 */       super.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 164 */     if (LOG.isDebugEnabled()) {
/* 165 */       LOG.debug("{} set socket timeout to {}", this.id, timeout);
/*     */     }
/* 167 */     super.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 172 */     if (this.closed.compareAndSet(false, true)) {
/* 173 */       if (LOG.isDebugEnabled()) {
/* 174 */         LOG.debug("{} close connection {}", this.id, closeMode);
/*     */       }
/* 176 */       super.close(closeMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 182 */     super.bind(WIRE_LOG.isDebugEnabled() ? new LoggingSocketHolder(socket, this.id, WIRE_LOG) : new SocketHolder(socket));
/* 183 */     this.socketTimeout = Timeout.ofMilliseconds(socket.getSoTimeout());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(ClassicHttpResponse response) {
/* 188 */     if (response != null && HEADER_LOG.isDebugEnabled()) {
/* 189 */       HEADER_LOG.debug("{} << {}", this.id, new StatusLine((HttpResponse)response));
/* 190 */       Header[] headers = response.getHeaders();
/* 191 */       for (Header header : headers) {
/* 192 */         HEADER_LOG.debug("{} << {}", this.id, header);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(ClassicHttpRequest request) {
/* 199 */     if (request != null && HEADER_LOG.isDebugEnabled()) {
/* 200 */       HEADER_LOG.debug("{} >> {}", this.id, new RequestLine((HttpRequest)request));
/* 201 */       Header[] headers = request.getHeaders();
/* 202 */       for (Header header : headers) {
/* 203 */         HEADER_LOG.debug("{} >> {}", this.id, header);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void passivate() {
/* 210 */     super.setSocketTimeout(Timeout.ZERO_MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activate() {
/* 215 */     super.setSocketTimeout(this.socketTimeout);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/DefaultManagedHttpClientConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */