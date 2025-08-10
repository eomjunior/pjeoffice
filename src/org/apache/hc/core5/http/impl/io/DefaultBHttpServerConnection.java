/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.UnsupportedHttpVersionException;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParser;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriter;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.io.HttpServerConnection;
/*     */ import org.apache.hc.core5.io.CloseMode;
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
/*     */ public class DefaultBHttpServerConnection
/*     */   extends BHttpConnectionBase
/*     */   implements HttpServerConnection
/*     */ {
/*     */   private final String scheme;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final HttpMessageParser<ClassicHttpRequest> requestParser;
/*     */   private final HttpMessageWriter<ClassicHttpResponse> responseWriter;
/*     */   
/*     */   public DefaultBHttpServerConnection(String scheme, Http1Config http1Config, CharsetDecoder charDecoder, CharsetEncoder charEncoder, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageParserFactory<ClassicHttpRequest> requestParserFactory, HttpMessageWriterFactory<ClassicHttpResponse> responseWriterFactory) {
/*  95 */     super(http1Config, charDecoder, charEncoder);
/*  96 */     this.scheme = scheme;
/*  97 */     this
/*  98 */       .requestParser = ((requestParserFactory != null) ? requestParserFactory : DefaultHttpRequestParserFactory.INSTANCE).create(http1Config);
/*  99 */     this
/* 100 */       .responseWriter = ((responseWriterFactory != null) ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE).create();
/* 101 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/* 103 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpServerConnection(String scheme, Http1Config http1Config, CharsetDecoder charDecoder, CharsetEncoder charEncoder) {
/* 112 */     this(scheme, http1Config, charDecoder, charEncoder, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory<ClassicHttpRequest>)null, (HttpMessageWriterFactory<ClassicHttpResponse>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpServerConnection(String scheme, Http1Config http1Config) {
/* 118 */     this(scheme, http1Config, (CharsetDecoder)null, (CharsetEncoder)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRequestReceived(ClassicHttpRequest request) {}
/*     */ 
/*     */   
/*     */   protected void onResponseSubmitted(ClassicHttpResponse response) {}
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 129 */     super.bind(socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicHttpRequest receiveRequestHeader() throws HttpException, IOException {
/* 134 */     SocketHolder socketHolder = ensureOpen();
/* 135 */     ClassicHttpRequest request = (ClassicHttpRequest)this.requestParser.parse(this.inBuffer, socketHolder.getInputStream());
/* 136 */     if (request == null) {
/* 137 */       return null;
/*     */     }
/* 139 */     ProtocolVersion transportVersion = request.getVersion();
/* 140 */     if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 141 */       throw new UnsupportedHttpVersionException(transportVersion);
/*     */     }
/* 143 */     request.setScheme(this.scheme);
/* 144 */     this.version = transportVersion;
/* 145 */     onRequestReceived(request);
/* 146 */     incrementRequestCount();
/* 147 */     return request;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void receiveRequestEntity(ClassicHttpRequest request) throws HttpException, IOException {
/* 153 */     Args.notNull(request, "HTTP request");
/* 154 */     SocketHolder socketHolder = ensureOpen();
/*     */     
/* 156 */     long len = this.incomingContentStrategy.determineLength((HttpMessage)request);
/* 157 */     if (len == -9223372036854775807L) {
/*     */       return;
/*     */     }
/* 160 */     request.setEntity(createIncomingEntity((HttpMessage)request, this.inBuffer, socketHolder.getInputStream(), len));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendResponseHeader(ClassicHttpResponse response) throws HttpException, IOException {
/* 166 */     Args.notNull(response, "HTTP response");
/* 167 */     SocketHolder socketHolder = ensureOpen();
/* 168 */     this.responseWriter.write((MessageHeaders)response, this.outbuffer, socketHolder.getOutputStream());
/* 169 */     onResponseSubmitted(response);
/* 170 */     if (response.getCode() >= 200) {
/* 171 */       incrementResponseCount();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendResponseEntity(ClassicHttpResponse response) throws HttpException, IOException {
/* 178 */     Args.notNull(response, "HTTP response");
/* 179 */     SocketHolder socketHolder = ensureOpen();
/* 180 */     HttpEntity entity = response.getEntity();
/* 181 */     if (entity == null) {
/*     */       return;
/*     */     }
/* 184 */     long len = this.outgoingContentStrategy.determineLength((HttpMessage)response);
/* 185 */     try (OutputStream outStream = createContentOutputStream(len, this.outbuffer, socketHolder.getOutputStream(), entity.getTrailers())) {
/* 186 */       entity.writeTo(outStream);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultBHttpServerConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */