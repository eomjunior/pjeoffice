/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.CharCodingSupport;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public class DefaultBHttpServerConnectionFactory
/*     */   implements HttpConnectionFactory<DefaultBHttpServerConnection>
/*     */ {
/*     */   private final String scheme;
/*     */   private final Http1Config http1Config;
/*     */   private final CharCodingConfig charCodingConfig;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final HttpMessageParserFactory<ClassicHttpRequest> requestParserFactory;
/*     */   private final HttpMessageWriterFactory<ClassicHttpResponse> responseWriterFactory;
/*     */   
/*     */   public DefaultBHttpServerConnectionFactory(String scheme, Http1Config http1Config, CharCodingConfig charCodingConfig, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageParserFactory<ClassicHttpRequest> requestParserFactory, HttpMessageWriterFactory<ClassicHttpResponse> responseWriterFactory) {
/*  70 */     this.scheme = scheme;
/*  71 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/*  72 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/*  73 */     this.incomingContentStrategy = incomingContentStrategy;
/*  74 */     this.outgoingContentStrategy = outgoingContentStrategy;
/*  75 */     this.requestParserFactory = requestParserFactory;
/*  76 */     this.responseWriterFactory = responseWriterFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpServerConnectionFactory(String scheme, Http1Config http1Config, CharCodingConfig charCodingConfig, HttpMessageParserFactory<ClassicHttpRequest> requestParserFactory, HttpMessageWriterFactory<ClassicHttpResponse> responseWriterFactory) {
/*  85 */     this(scheme, http1Config, charCodingConfig, null, null, requestParserFactory, responseWriterFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpServerConnectionFactory(String scheme, Http1Config http1Config, CharCodingConfig charCodingConfig) {
/*  92 */     this(scheme, http1Config, charCodingConfig, null, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpServerConnection createConnection(Socket socket) throws IOException {
/* 101 */     DefaultBHttpServerConnection conn = new DefaultBHttpServerConnection(this.scheme, this.http1Config, CharCodingSupport.createDecoder(this.charCodingConfig), CharCodingSupport.createEncoder(this.charCodingConfig), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestParserFactory, this.responseWriterFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     conn.bind(socket);
/* 107 */     return conn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 116 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Builder
/*     */   {
/*     */     private String scheme;
/*     */     
/*     */     private Http1Config http1Config;
/*     */     
/*     */     private CharCodingConfig charCodingConfig;
/*     */     
/*     */     private ContentLengthStrategy incomingContentLengthStrategy;
/*     */     private ContentLengthStrategy outgoingContentLengthStrategy;
/*     */     private HttpMessageParserFactory<ClassicHttpRequest> requestParserFactory;
/*     */     private HttpMessageWriterFactory<ClassicHttpResponse> responseWriterFactory;
/*     */     
/*     */     private Builder() {}
/*     */     
/*     */     public Builder scheme(String scheme) {
/* 136 */       this.scheme = scheme;
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     public Builder http1Config(Http1Config http1Config) {
/* 141 */       this.http1Config = http1Config;
/* 142 */       return this;
/*     */     }
/*     */     
/*     */     public Builder charCodingConfig(CharCodingConfig charCodingConfig) {
/* 146 */       this.charCodingConfig = charCodingConfig;
/* 147 */       return this;
/*     */     }
/*     */     
/*     */     public Builder incomingContentLengthStrategy(ContentLengthStrategy incomingContentLengthStrategy) {
/* 151 */       this.incomingContentLengthStrategy = incomingContentLengthStrategy;
/* 152 */       return this;
/*     */     }
/*     */     
/*     */     public Builder outgoingContentLengthStrategy(ContentLengthStrategy outgoingContentLengthStrategy) {
/* 156 */       this.outgoingContentLengthStrategy = outgoingContentLengthStrategy;
/* 157 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder requestParserFactory(HttpMessageParserFactory<ClassicHttpRequest> requestParserFactory) {
/* 162 */       this.requestParserFactory = requestParserFactory;
/* 163 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder responseWriterFactory(HttpMessageWriterFactory<ClassicHttpResponse> responseWriterFactory) {
/* 168 */       this.responseWriterFactory = responseWriterFactory;
/* 169 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBHttpServerConnectionFactory build() {
/* 173 */       return new DefaultBHttpServerConnectionFactory(this.scheme, this.http1Config, this.charCodingConfig, this.incomingContentLengthStrategy, this.outgoingContentLengthStrategy, this.requestParserFactory, this.responseWriterFactory);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultBHttpServerConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */