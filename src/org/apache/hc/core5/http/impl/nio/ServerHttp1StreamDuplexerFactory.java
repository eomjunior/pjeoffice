/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Internal
/*     */ public final class ServerHttp1StreamDuplexerFactory
/*     */ {
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory;
/*     */   private final ConnectionReuseStrategy connectionReuseStrategy;
/*     */   private final Http1Config http1Config;
/*     */   private final CharCodingConfig charCodingConfig;
/*     */   private final NHttpMessageParserFactory<HttpRequest> requestParserFactory;
/*     */   private final NHttpMessageWriterFactory<HttpResponse> responseWriterFactory;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final Http1StreamListener streamListener;
/*     */   
/*     */   public ServerHttp1StreamDuplexerFactory(HttpProcessor httpProcessor, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, Http1Config http1Config, CharCodingConfig charCodingConfig, ConnectionReuseStrategy connectionReuseStrategy, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, Http1StreamListener streamListener) {
/*  81 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/*  82 */     this.exchangeHandlerFactory = (HandlerFactory<AsyncServerExchangeHandler>)Args.notNull(exchangeHandlerFactory, "Exchange handler factory");
/*  83 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/*  84 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/*  85 */     this.connectionReuseStrategy = (connectionReuseStrategy != null) ? connectionReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/*     */     
/*  87 */     this.requestParserFactory = (requestParserFactory != null) ? requestParserFactory : new DefaultHttpRequestParserFactory(http1Config);
/*     */     
/*  89 */     this.responseWriterFactory = (responseWriterFactory != null) ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE;
/*     */     
/*  91 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/*  93 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/*  95 */     this.streamListener = streamListener;
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
/*     */   public ServerHttp1StreamDuplexerFactory(HttpProcessor httpProcessor, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, Http1Config http1Config, CharCodingConfig charCodingConfig, ConnectionReuseStrategy connectionReuseStrategy, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory, Http1StreamListener streamListener) {
/* 107 */     this(httpProcessor, exchangeHandlerFactory, http1Config, charCodingConfig, connectionReuseStrategy, requestParserFactory, responseWriterFactory, null, null, streamListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttp1StreamDuplexerFactory(HttpProcessor httpProcessor, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, Http1Config http1Config, CharCodingConfig charCodingConfig, Http1StreamListener streamListener) {
/* 118 */     this(httpProcessor, exchangeHandlerFactory, http1Config, charCodingConfig, null, null, null, streamListener);
/*     */   }
/*     */   
/*     */   public ServerHttp1StreamDuplexer create(String scheme, ProtocolIOSession ioSession) {
/* 122 */     return new ServerHttp1StreamDuplexer(ioSession, this.httpProcessor, this.exchangeHandlerFactory, scheme, this.http1Config, this.charCodingConfig, this.connectionReuseStrategy, this.requestParserFactory
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 127 */         .create(), this.responseWriterFactory
/* 128 */         .create(), this.incomingContentStrategy, this.outgoingContentStrategy, this.streamListener);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ServerHttp1StreamDuplexerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */