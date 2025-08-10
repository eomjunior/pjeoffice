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
/*     */ public class DefaultBHttpClientConnectionFactory
/*     */   implements HttpConnectionFactory<DefaultBHttpClientConnection>
/*     */ {
/*     */   private final Http1Config http1Config;
/*     */   private final CharCodingConfig charCodingConfig;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final ResponseOutOfOrderStrategy responseOutOfOrderStrategy;
/*     */   private final HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory;
/*     */   private final HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory;
/*     */   
/*     */   private DefaultBHttpClientConnectionFactory(Http1Config http1Config, CharCodingConfig charCodingConfig, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, ResponseOutOfOrderStrategy responseOutOfOrderStrategy, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/*  71 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/*  72 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/*  73 */     this.incomingContentStrategy = incomingContentStrategy;
/*  74 */     this.outgoingContentStrategy = outgoingContentStrategy;
/*  75 */     this.responseOutOfOrderStrategy = responseOutOfOrderStrategy;
/*  76 */     this.requestWriterFactory = requestWriterFactory;
/*  77 */     this.responseParserFactory = responseParserFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpClientConnectionFactory(Http1Config http1Config, CharCodingConfig charCodingConfig, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/*  87 */     this(http1Config, charCodingConfig, incomingContentStrategy, outgoingContentStrategy, null, requestWriterFactory, responseParserFactory);
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
/*     */   public DefaultBHttpClientConnectionFactory(Http1Config http1Config, CharCodingConfig charCodingConfig, HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory, HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 102 */     this(http1Config, charCodingConfig, null, null, requestWriterFactory, responseParserFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpClientConnectionFactory(Http1Config http1Config, CharCodingConfig charCodingConfig) {
/* 108 */     this(http1Config, charCodingConfig, null, null, null, null);
/*     */   }
/*     */   
/*     */   public DefaultBHttpClientConnectionFactory() {
/* 112 */     this(null, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultBHttpClientConnection createConnection(Socket socket) throws IOException {
/* 120 */     DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(this.http1Config, CharCodingSupport.createDecoder(this.charCodingConfig), CharCodingSupport.createEncoder(this.charCodingConfig), this.incomingContentStrategy, this.outgoingContentStrategy, this.responseOutOfOrderStrategy, this.requestWriterFactory, this.responseParserFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     conn.bind(socket);
/* 127 */     return conn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/* 136 */     return new Builder();
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
/*     */     private ResponseOutOfOrderStrategy responseOutOfOrderStrategy;
/*     */     private HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory;
/*     */     private HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory;
/*     */     
/*     */     private Builder() {}
/*     */     
/*     */     public Builder http1Config(Http1Config http1Config) {
/* 156 */       this.http1Config = http1Config;
/* 157 */       return this;
/*     */     }
/*     */     
/*     */     public Builder charCodingConfig(CharCodingConfig charCodingConfig) {
/* 161 */       this.charCodingConfig = charCodingConfig;
/* 162 */       return this;
/*     */     }
/*     */     
/*     */     public Builder incomingContentLengthStrategy(ContentLengthStrategy incomingContentLengthStrategy) {
/* 166 */       this.incomingContentLengthStrategy = incomingContentLengthStrategy;
/* 167 */       return this;
/*     */     }
/*     */     
/*     */     public Builder outgoingContentLengthStrategy(ContentLengthStrategy outgoingContentLengthStrategy) {
/* 171 */       this.outgoingContentLengthStrategy = outgoingContentLengthStrategy;
/* 172 */       return this;
/*     */     }
/*     */     
/*     */     public Builder responseOutOfOrderStrategy(ResponseOutOfOrderStrategy responseOutOfOrderStrategy) {
/* 176 */       this.responseOutOfOrderStrategy = responseOutOfOrderStrategy;
/* 177 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder requestWriterFactory(HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory) {
/* 182 */       this.requestWriterFactory = requestWriterFactory;
/* 183 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder responseParserFactory(HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory) {
/* 188 */       this.responseParserFactory = responseParserFactory;
/* 189 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBHttpClientConnectionFactory build() {
/* 193 */       return new DefaultBHttpClientConnectionFactory(this.http1Config, this.charCodingConfig, this.incomingContentLengthStrategy, this.outgoingContentLengthStrategy, this.responseOutOfOrderStrategy, this.requestWriterFactory, this.responseParserFactory);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultBHttpClientConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */