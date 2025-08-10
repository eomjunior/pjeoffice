/*     */ package org.apache.hc.client5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultHttpRequestWriterFactory;
/*     */ import org.apache.hc.core5.http.impl.io.NoResponseOutOfOrderStrategy;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.io.ResponseOutOfOrderStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class ManagedHttpClientConnectionFactory
/*     */   implements HttpConnectionFactory<ManagedHttpClientConnection>
/*     */ {
/*  61 */   private static final AtomicLong COUNTER = new AtomicLong();
/*     */   
/*  63 */   public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
/*     */   
/*     */   private final Http1Config h1Config;
/*     */   
/*     */   private final CharCodingConfig charCodingConfig;
/*     */   
/*     */   private final HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory;
/*     */   
/*     */   private final HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory;
/*     */   
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   
/*     */   private final ResponseOutOfOrderStrategy responseOutOfOrderStrategy;
/*     */ 
/*     */   
/*     */   private ManagedHttpClientConnectionFactory(Http1Config h1Config, CharCodingConfig charCodingConfig, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, ResponseOutOfOrderStrategy responseOutOfOrderStrategy) {
/*  81 */     this.h1Config = (h1Config != null) ? h1Config : Http1Config.DEFAULT;
/*  82 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/*  83 */     this.requestWriterFactory = (requestWriterFactory != null) ? requestWriterFactory : (HttpMessageWriterFactory<ClassicHttpRequest>)DefaultHttpRequestWriterFactory.INSTANCE;
/*     */     
/*  85 */     this.responseParserFactory = (responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE;
/*     */     
/*  87 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/*  89 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/*  91 */     this.responseOutOfOrderStrategy = (responseOutOfOrderStrategy != null) ? responseOutOfOrderStrategy : (ResponseOutOfOrderStrategy)NoResponseOutOfOrderStrategy.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedHttpClientConnectionFactory(Http1Config h1Config, CharCodingConfig charCodingConfig, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
/* 102 */     this(h1Config, charCodingConfig, requestWriterFactory, responseParserFactory, incomingContentStrategy, outgoingContentStrategy, null);
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
/*     */   public ManagedHttpClientConnectionFactory(Http1Config h1Config, CharCodingConfig charCodingConfig, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 117 */     this(h1Config, charCodingConfig, requestWriterFactory, responseParserFactory, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedHttpClientConnectionFactory(Http1Config h1Config, CharCodingConfig charCodingConfig, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 124 */     this(h1Config, charCodingConfig, null, responseParserFactory);
/*     */   }
/*     */   
/*     */   public ManagedHttpClientConnectionFactory() {
/* 128 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedHttpClientConnection createConnection(Socket socket) throws IOException {
/* 133 */     CharsetDecoder charDecoder = null;
/* 134 */     CharsetEncoder charEncoder = null;
/* 135 */     Charset charset = this.charCodingConfig.getCharset();
/*     */     
/* 137 */     CodingErrorAction malformedInputAction = (this.charCodingConfig.getMalformedInputAction() != null) ? this.charCodingConfig.getMalformedInputAction() : CodingErrorAction.REPORT;
/*     */     
/* 139 */     CodingErrorAction unmappableInputAction = (this.charCodingConfig.getUnmappableInputAction() != null) ? this.charCodingConfig.getUnmappableInputAction() : CodingErrorAction.REPORT;
/* 140 */     if (charset != null) {
/* 141 */       charDecoder = charset.newDecoder();
/* 142 */       charDecoder.onMalformedInput(malformedInputAction);
/* 143 */       charDecoder.onUnmappableCharacter(unmappableInputAction);
/* 144 */       charEncoder = charset.newEncoder();
/* 145 */       charEncoder.onMalformedInput(malformedInputAction);
/* 146 */       charEncoder.onUnmappableCharacter(unmappableInputAction);
/*     */     } 
/* 148 */     String id = "http-outgoing-" + COUNTER.getAndIncrement();
/* 149 */     DefaultManagedHttpClientConnection conn = new DefaultManagedHttpClientConnection(id, charDecoder, charEncoder, this.h1Config, this.incomingContentStrategy, this.outgoingContentStrategy, this.responseOutOfOrderStrategy, this.requestWriterFactory, this.responseParserFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     if (socket != null) {
/* 160 */       conn.bind(socket);
/*     */     }
/* 162 */     return conn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 171 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Builder
/*     */   {
/*     */     private Http1Config http1Config;
/*     */     
/*     */     private CharCodingConfig charCodingConfig;
/*     */     
/*     */     private ContentLengthStrategy incomingContentLengthStrategy;
/*     */     
/*     */     private ContentLengthStrategy outgoingContentLengthStrategy;
/*     */     
/*     */     private ResponseOutOfOrderStrategy responseOutOfOrderStrategy;
/*     */     private HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory;
/*     */     private HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory;
/*     */     
/*     */     private Builder() {}
/*     */     
/*     */     public Builder http1Config(Http1Config http1Config) {
/* 192 */       this.http1Config = http1Config;
/* 193 */       return this;
/*     */     }
/*     */     
/*     */     public Builder charCodingConfig(CharCodingConfig charCodingConfig) {
/* 197 */       this.charCodingConfig = charCodingConfig;
/* 198 */       return this;
/*     */     }
/*     */     
/*     */     public Builder incomingContentLengthStrategy(ContentLengthStrategy incomingContentLengthStrategy) {
/* 202 */       this.incomingContentLengthStrategy = incomingContentLengthStrategy;
/* 203 */       return this;
/*     */     }
/*     */     
/*     */     public Builder outgoingContentLengthStrategy(ContentLengthStrategy outgoingContentLengthStrategy) {
/* 207 */       this.outgoingContentLengthStrategy = outgoingContentLengthStrategy;
/* 208 */       return this;
/*     */     }
/*     */     
/*     */     public Builder responseOutOfOrderStrategy(ResponseOutOfOrderStrategy responseOutOfOrderStrategy) {
/* 212 */       this.responseOutOfOrderStrategy = responseOutOfOrderStrategy;
/* 213 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder requestWriterFactory(HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory) {
/* 218 */       this.requestWriterFactory = requestWriterFactory;
/* 219 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder responseParserFactory(HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 224 */       this.responseParserFactory = responseParserFactory;
/* 225 */       return this;
/*     */     }
/*     */     
/*     */     public ManagedHttpClientConnectionFactory build() {
/* 229 */       return new ManagedHttpClientConnectionFactory(this.http1Config, this.charCodingConfig, this.requestWriterFactory, this.responseParserFactory, this.incomingContentLengthStrategy, this.outgoingContentLengthStrategy, this.responseOutOfOrderStrategy);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/ManagedHttpClientConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */