/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ import org.apache.hc.core5.http.LengthRequiredException;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.NoHttpResponseException;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.UnsupportedHttpVersionException;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParser;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriter;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.io.ResponseOutOfOrderStrategy;
/*     */ import org.apache.hc.core5.http.message.BasicTokenIterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultBHttpClientConnection
/*     */   extends BHttpConnectionBase
/*     */   implements HttpClientConnection
/*     */ {
/*     */   private final HttpMessageParser<ClassicHttpResponse> responseParser;
/*     */   private final HttpMessageWriter<ClassicHttpRequest> requestWriter;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final ResponseOutOfOrderStrategy responseOutOfOrderStrategy;
/*     */   private volatile boolean consistent;
/*     */   
/*     */   public DefaultBHttpClientConnection(Http1Config http1Config, CharsetDecoder charDecoder, CharsetEncoder charEncoder, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, ResponseOutOfOrderStrategy responseOutOfOrderStrategy, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 107 */     super(http1Config, charDecoder, charEncoder);
/* 108 */     this
/* 109 */       .requestWriter = ((requestWriterFactory != null) ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE).create();
/* 110 */     this
/* 111 */       .responseParser = ((responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE).create(http1Config);
/* 112 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/* 114 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/* 116 */     this.responseOutOfOrderStrategy = (responseOutOfOrderStrategy != null) ? responseOutOfOrderStrategy : NoResponseOutOfOrderStrategy.INSTANCE;
/*     */     
/* 118 */     this.consistent = true;
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
/*     */   public DefaultBHttpClientConnection(Http1Config http1Config, CharsetDecoder charDecoder, CharsetEncoder charEncoder, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 147 */     this(http1Config, charDecoder, charEncoder, incomingContentStrategy, outgoingContentStrategy, (ResponseOutOfOrderStrategy)null, requestWriterFactory, responseParserFactory);
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
/*     */   
/*     */   public DefaultBHttpClientConnection(Http1Config http1Config, CharsetDecoder charDecoder, CharsetEncoder charEncoder) {
/* 162 */     this(http1Config, charDecoder, charEncoder, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory<ClassicHttpRequest>)null, (HttpMessageParserFactory<ClassicHttpResponse>)null);
/*     */   }
/*     */   
/*     */   public DefaultBHttpClientConnection(Http1Config http1Config) {
/* 166 */     this(http1Config, (CharsetDecoder)null, (CharsetEncoder)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(ClassicHttpResponse response) {}
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(ClassicHttpRequest request) {}
/*     */ 
/*     */   
/*     */   public void bind(Socket socket) throws IOException {
/* 177 */     super.bind(socket);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRequestHeader(ClassicHttpRequest request) throws HttpException, IOException {
/* 183 */     Args.notNull(request, "HTTP request");
/* 184 */     SocketHolder socketHolder = ensureOpen();
/* 185 */     this.requestWriter.write((MessageHeaders)request, this.outbuffer, socketHolder.getOutputStream());
/* 186 */     onRequestSubmitted(request);
/* 187 */     incrementRequestCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequestEntity(final ClassicHttpRequest request) throws HttpException, IOException {
/* 192 */     Args.notNull(request, "HTTP request");
/* 193 */     final SocketHolder socketHolder = ensureOpen();
/* 194 */     HttpEntity entity = request.getEntity();
/* 195 */     if (entity == null) {
/*     */       return;
/*     */     }
/* 198 */     long len = this.outgoingContentStrategy.determineLength((HttpMessage)request);
/* 199 */     if (len == -9223372036854775807L) {
/* 200 */       throw new LengthRequiredException();
/*     */     }
/* 202 */     try (OutputStream outStream = createContentOutputStream(len, this.outbuffer, new OutputStream()
/*     */           {
/*     */             
/* 205 */             final OutputStream socketOutputStream = socketHolder.getOutputStream();
/* 206 */             final InputStream socketInputStream = socketHolder.getInputStream();
/*     */             
/*     */             long totalBytes;
/*     */             
/*     */             void checkForEarlyResponse(long totalBytesSent, int nextWriteSize) throws IOException {
/* 211 */               if (DefaultBHttpClientConnection.this.responseOutOfOrderStrategy.isEarlyResponseDetected(request, DefaultBHttpClientConnection.this, this.socketInputStream, totalBytesSent, nextWriteSize))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 217 */                 throw new ResponseOutOfOrderException();
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void write(byte[] b) throws IOException {
/* 223 */               checkForEarlyResponse(this.totalBytes, b.length);
/* 224 */               this.totalBytes += b.length;
/* 225 */               this.socketOutputStream.write(b);
/*     */             }
/*     */ 
/*     */             
/*     */             public void write(byte[] b, int off, int len) throws IOException {
/* 230 */               checkForEarlyResponse(this.totalBytes, len);
/* 231 */               this.totalBytes += len;
/* 232 */               this.socketOutputStream.write(b, off, len);
/*     */             }
/*     */ 
/*     */             
/*     */             public void write(int b) throws IOException {
/* 237 */               checkForEarlyResponse(this.totalBytes, 1);
/* 238 */               this.totalBytes++;
/* 239 */               this.socketOutputStream.write(b);
/*     */             }
/*     */ 
/*     */             
/*     */             public void flush() throws IOException {
/* 244 */               this.socketOutputStream.flush();
/*     */             }
/*     */ 
/*     */             
/*     */             public void close() throws IOException {
/* 249 */               this.socketOutputStream.close();
/*     */             }
/* 252 */           }entity.getTrailers())) {
/* 253 */       entity.writeTo(outStream);
/* 254 */     } catch (ResponseOutOfOrderException ex) {
/* 255 */       if (len > 0L) {
/* 256 */         this.consistent = false;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConsistent() {
/* 263 */     return this.consistent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void terminateRequest(ClassicHttpRequest request) throws HttpException, IOException {
/* 268 */     Args.notNull(request, "HTTP request");
/* 269 */     SocketHolder socketHolder = ensureOpen();
/* 270 */     HttpEntity entity = request.getEntity();
/* 271 */     if (entity == null) {
/*     */       return;
/*     */     }
/* 274 */     BasicTokenIterator<String> basicTokenIterator = new BasicTokenIterator(request.headerIterator("Connection"));
/* 275 */     while (basicTokenIterator.hasNext()) {
/* 276 */       String token = basicTokenIterator.next();
/* 277 */       if ("close".equalsIgnoreCase(token)) {
/* 278 */         this.consistent = false;
/*     */         return;
/*     */       } 
/*     */     } 
/* 282 */     long len = this.outgoingContentStrategy.determineLength((HttpMessage)request);
/* 283 */     if (len == -1L) {
/* 284 */       OutputStream outStream = createContentOutputStream(len, this.outbuffer, socketHolder.getOutputStream(), entity.getTrailers()); Throwable throwable = null;
/*     */       
/* 286 */       if (outStream != null) if (throwable != null) { try { outStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  } else { outStream.close(); }  
/* 287 */     } else if (len >= 0L && len <= 1024L) {
/* 288 */       try (OutputStream outStream = createContentOutputStream(len, this.outbuffer, socketHolder.getOutputStream(), null)) {
/* 289 */         entity.writeTo(outStream);
/*     */       } 
/*     */     } else {
/* 292 */       this.consistent = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse receiveResponseHeader() throws HttpException, IOException {
/* 298 */     SocketHolder socketHolder = ensureOpen();
/* 299 */     ClassicHttpResponse response = (ClassicHttpResponse)this.responseParser.parse(this.inBuffer, socketHolder.getInputStream());
/* 300 */     if (response == null) {
/* 301 */       throw new NoHttpResponseException("The target server failed to respond");
/*     */     }
/* 303 */     ProtocolVersion transportVersion = response.getVersion();
/* 304 */     if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 305 */       throw new UnsupportedHttpVersionException(transportVersion);
/*     */     }
/* 307 */     this.version = transportVersion;
/* 308 */     onResponseReceived(response);
/* 309 */     int status = response.getCode();
/* 310 */     if (status < 100) {
/* 311 */       throw new ProtocolException("Invalid response: " + status);
/*     */     }
/* 313 */     if (response.getCode() >= 200) {
/* 314 */       incrementResponseCount();
/*     */     }
/* 316 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void receiveResponseEntity(ClassicHttpResponse response) throws HttpException, IOException {
/* 321 */     Args.notNull(response, "HTTP response");
/* 322 */     SocketHolder socketHolder = ensureOpen();
/* 323 */     long len = this.incomingContentStrategy.determineLength((HttpMessage)response);
/* 324 */     response.setEntity(createIncomingEntity((HttpMessage)response, this.inBuffer, socketHolder.getInputStream(), len));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultBHttpClientConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */