/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.hc.client5.http.impl.DefaultClientConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.impl.nio.ClientHttp1IOEventHandler;
/*     */ import org.apache.hc.core5.http.impl.nio.ClientHttp1StreamDuplexerFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpRequestWriterFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpResponseParserFactory;
/*     */ import org.apache.hc.core5.http.message.RequestLine;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.frame.FramePrinter;
/*     */ import org.apache.hc.core5.http2.frame.RawFrame;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2PrefaceHandler;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2StreamMultiplexerFactory;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2UpgradeHandler;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientHttp1UpgradeHandler;
/*     */ import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
/*     */ import org.apache.hc.core5.http2.impl.nio.HttpProtocolNegotiator;
/*     */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ProtocolUpgradeHandler;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ class HttpAsyncClientProtocolNegotiationStarter
/*     */   implements IOEventHandlerFactory
/*     */ {
/*  74 */   private static final Logger STREAM_LOG = LoggerFactory.getLogger(InternalHttpAsyncClient.class);
/*  75 */   private static final Logger HEADER_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http.headers");
/*  76 */   private static final Logger FRAME_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http2.frame");
/*  77 */   private static final Logger FRAME_PAYLOAD_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http2.frame.payload");
/*  78 */   private static final Logger FLOW_CTRL_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http2.flow");
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   private final HandlerFactory<AsyncPushConsumer> exchangeHandlerFactory;
/*     */   
/*     */   private final H2Config h2Config;
/*     */   
/*     */   private final Http1Config h1Config;
/*     */   
/*     */   private final CharCodingConfig charCodingConfig;
/*     */   
/*     */   private final ConnectionReuseStrategy http1ConnectionReuseStrategy;
/*     */   
/*     */   private final NHttpMessageParserFactory<HttpResponse> http1ResponseParserFactory;
/*     */   private final NHttpMessageWriterFactory<HttpRequest> http1RequestWriterFactory;
/*     */   
/*     */   HttpAsyncClientProtocolNegotiationStarter(HttpProcessor httpProcessor, HandlerFactory<AsyncPushConsumer> exchangeHandlerFactory, H2Config h2Config, Http1Config h1Config, CharCodingConfig charCodingConfig, ConnectionReuseStrategy connectionReuseStrategy) {
/*  96 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/*  97 */     this.exchangeHandlerFactory = exchangeHandlerFactory;
/*  98 */     this.h2Config = (h2Config != null) ? h2Config : H2Config.DEFAULT;
/*  99 */     this.h1Config = (h1Config != null) ? h1Config : Http1Config.DEFAULT;
/* 100 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/* 101 */     this.http1ConnectionReuseStrategy = (connectionReuseStrategy != null) ? connectionReuseStrategy : (ConnectionReuseStrategy)DefaultClientConnectionReuseStrategy.INSTANCE;
/* 102 */     this.http1ResponseParserFactory = (NHttpMessageParserFactory<HttpResponse>)new DefaultHttpResponseParserFactory(h1Config);
/* 103 */     this.http1RequestWriterFactory = (NHttpMessageWriterFactory<HttpRequest>)DefaultHttpRequestWriterFactory.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IOEventHandler createHandler(ProtocolIOSession ioSession, Object attachment) {
/*     */     ClientHttp1StreamDuplexerFactory http1StreamHandlerFactory;
/*     */     ClientH2StreamMultiplexerFactory http2StreamHandlerFactory;
/* 111 */     if (STREAM_LOG.isDebugEnabled() || HEADER_LOG
/* 112 */       .isDebugEnabled() || FRAME_LOG
/* 113 */       .isDebugEnabled() || FRAME_PAYLOAD_LOG
/* 114 */       .isDebugEnabled() || FLOW_CTRL_LOG
/* 115 */       .isDebugEnabled()) {
/* 116 */       final String id = ioSession.getId();
/* 117 */       http1StreamHandlerFactory = new ClientHttp1StreamDuplexerFactory(this.httpProcessor, this.h1Config, this.charCodingConfig, this.http1ConnectionReuseStrategy, this.http1ResponseParserFactory, this.http1RequestWriterFactory, new Http1StreamListener()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void onRequestHead(HttpConnection connection, HttpRequest request)
/*     */             {
/* 128 */               if (HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.isDebugEnabled()) {
/* 129 */                 HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.debug("{} >> {}", id, new RequestLine(request));
/* 130 */                 for (Iterator<Header> it = request.headerIterator(); it.hasNext();) {
/* 131 */                   HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.debug("{} >> {}", id, it.next());
/*     */                 }
/*     */               } 
/*     */             }
/*     */ 
/*     */             
/*     */             public void onResponseHead(HttpConnection connection, HttpResponse response) {
/* 138 */               if (HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.isDebugEnabled()) {
/* 139 */                 HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.debug("{} << {}", id, new StatusLine(response));
/* 140 */                 for (Iterator<Header> it = response.headerIterator(); it.hasNext();) {
/* 141 */                   HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.debug("{} << {}", id, it.next());
/*     */                 }
/*     */               } 
/*     */             }
/*     */ 
/*     */             
/*     */             public void onExchangeComplete(HttpConnection connection, boolean keepAlive) {
/* 148 */               if (HttpAsyncClientProtocolNegotiationStarter.STREAM_LOG.isDebugEnabled()) {
/* 149 */                 if (keepAlive) {
/* 150 */                   HttpAsyncClientProtocolNegotiationStarter.STREAM_LOG.debug("{} Connection is kept alive", id);
/*     */                 } else {
/* 152 */                   HttpAsyncClientProtocolNegotiationStarter.STREAM_LOG.debug("{} Connection is not kept alive", id);
/*     */                 } 
/*     */               }
/*     */             }
/*     */           });
/*     */       
/* 158 */       http2StreamHandlerFactory = new ClientH2StreamMultiplexerFactory(this.httpProcessor, this.exchangeHandlerFactory, this.h2Config, this.charCodingConfig, new H2StreamListener()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 165 */             final FramePrinter framePrinter = new FramePrinter();
/*     */             
/*     */             private void logFrameInfo(String prefix, RawFrame frame) {
/*     */               try {
/* 169 */                 LogAppendable logAppendable = new LogAppendable(HttpAsyncClientProtocolNegotiationStarter.FRAME_LOG, prefix);
/* 170 */                 this.framePrinter.printFrameInfo(frame, logAppendable);
/* 171 */                 logAppendable.flush();
/* 172 */               } catch (IOException iOException) {}
/*     */             }
/*     */ 
/*     */             
/*     */             private void logFramePayload(String prefix, RawFrame frame) {
/*     */               try {
/* 178 */                 LogAppendable logAppendable = new LogAppendable(HttpAsyncClientProtocolNegotiationStarter.FRAME_PAYLOAD_LOG, prefix);
/* 179 */                 this.framePrinter.printPayload(frame, logAppendable);
/* 180 */                 logAppendable.flush();
/* 181 */               } catch (IOException iOException) {}
/*     */             }
/*     */ 
/*     */             
/*     */             private void logFlowControl(String prefix, int streamId, int delta, int actualSize) {
/* 186 */               HttpAsyncClientProtocolNegotiationStarter.FLOW_CTRL_LOG.debug("{} stream {} flow control {} -> {}", new Object[] { prefix, Integer.valueOf(streamId), Integer.valueOf(delta), Integer.valueOf(actualSize) });
/*     */             }
/*     */ 
/*     */             
/*     */             public void onHeaderInput(HttpConnection connection, int streamId, List<? extends Header> headers) {
/* 191 */               if (HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.isDebugEnabled()) {
/* 192 */                 for (int i = 0; i < headers.size(); i++) {
/* 193 */                   HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.debug("{} << {}", id, headers.get(i));
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onHeaderOutput(HttpConnection connection, int streamId, List<? extends Header> headers) {
/* 200 */               if (HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.isDebugEnabled()) {
/* 201 */                 for (int i = 0; i < headers.size(); i++) {
/* 202 */                   HttpAsyncClientProtocolNegotiationStarter.HEADER_LOG.debug("{} >> {}", id, headers.get(i));
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onFrameInput(HttpConnection connection, int streamId, RawFrame frame) {
/* 209 */               if (HttpAsyncClientProtocolNegotiationStarter.FRAME_LOG.isDebugEnabled()) {
/* 210 */                 logFrameInfo(id + " <<", frame);
/*     */               }
/* 212 */               if (HttpAsyncClientProtocolNegotiationStarter.FRAME_PAYLOAD_LOG.isDebugEnabled()) {
/* 213 */                 logFramePayload(id + " <<", frame);
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onFrameOutput(HttpConnection connection, int streamId, RawFrame frame) {
/* 219 */               if (HttpAsyncClientProtocolNegotiationStarter.FRAME_LOG.isDebugEnabled()) {
/* 220 */                 logFrameInfo(id + " >>", frame);
/*     */               }
/* 222 */               if (HttpAsyncClientProtocolNegotiationStarter.FRAME_PAYLOAD_LOG.isDebugEnabled()) {
/* 223 */                 logFramePayload(id + " >>", frame);
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onInputFlowControl(HttpConnection connection, int streamId, int delta, int actualSize) {
/* 229 */               if (HttpAsyncClientProtocolNegotiationStarter.FLOW_CTRL_LOG.isDebugEnabled()) {
/* 230 */                 logFlowControl(id + " <<", streamId, delta, actualSize);
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onOutputFlowControl(HttpConnection connection, int streamId, int delta, int actualSize) {
/* 236 */               if (HttpAsyncClientProtocolNegotiationStarter.FLOW_CTRL_LOG.isDebugEnabled()) {
/* 237 */                 logFlowControl(id + " >>", streamId, delta, actualSize);
/*     */               }
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 243 */       http1StreamHandlerFactory = new ClientHttp1StreamDuplexerFactory(this.httpProcessor, this.h1Config, this.charCodingConfig, this.http1ConnectionReuseStrategy, this.http1ResponseParserFactory, this.http1RequestWriterFactory, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 251 */       http2StreamHandlerFactory = new ClientH2StreamMultiplexerFactory(this.httpProcessor, this.exchangeHandlerFactory, this.h2Config, this.charCodingConfig, null);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     ioSession.registerProtocol(ApplicationProtocol.HTTP_1_1.id, (ProtocolUpgradeHandler)new ClientHttp1UpgradeHandler(http1StreamHandlerFactory));
/* 260 */     ioSession.registerProtocol(ApplicationProtocol.HTTP_2.id, (ProtocolUpgradeHandler)new ClientH2UpgradeHandler(http2StreamHandlerFactory));
/*     */     
/* 262 */     HttpVersionPolicy versionPolicy = (attachment instanceof HttpVersionPolicy) ? (HttpVersionPolicy)attachment : HttpVersionPolicy.NEGOTIATE;
/* 263 */     switch (versionPolicy) {
/*     */       case FORCE_HTTP_2:
/* 265 */         return (IOEventHandler)new ClientH2PrefaceHandler(ioSession, http2StreamHandlerFactory, false);
/*     */       case FORCE_HTTP_1:
/* 267 */         return (IOEventHandler)new ClientHttp1IOEventHandler(http1StreamHandlerFactory.create(ioSession));
/*     */     } 
/* 269 */     return (IOEventHandler)new HttpProtocolNegotiator(ioSession, null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/HttpAsyncClientProtocolNegotiationStarter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */