/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.frame.FramePrinter;
/*     */ import org.apache.hc.core5.http2.frame.RawFrame;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2PrefaceHandler;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2StreamMultiplexerFactory;
/*     */ import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
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
/*     */ 
/*     */ class H2AsyncClientProtocolStarter
/*     */   implements IOEventHandlerFactory
/*     */ {
/*  54 */   private static final Logger HEADER_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http.headers");
/*  55 */   private static final Logger FRAME_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http2.frame");
/*  56 */   private static final Logger FRAME_PAYLOAD_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http2.frame.payload");
/*  57 */   private static final Logger FLOW_CTRL_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http2.flow");
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   private final HandlerFactory<AsyncPushConsumer> exchangeHandlerFactory;
/*     */   
/*     */   private final H2Config h2Config;
/*     */   
/*     */   private final CharCodingConfig charCodingConfig;
/*     */ 
/*     */   
/*     */   H2AsyncClientProtocolStarter(HttpProcessor httpProcessor, HandlerFactory<AsyncPushConsumer> exchangeHandlerFactory, H2Config h2Config, CharCodingConfig charCodingConfig) {
/*  69 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/*  70 */     this.exchangeHandlerFactory = exchangeHandlerFactory;
/*  71 */     this.h2Config = (h2Config != null) ? h2Config : H2Config.DEFAULT;
/*  72 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IOEventHandler createHandler(ProtocolIOSession ioSession, Object attachment) {
/*  77 */     if (HEADER_LOG.isDebugEnabled() || FRAME_LOG
/*  78 */       .isDebugEnabled() || FRAME_PAYLOAD_LOG
/*  79 */       .isDebugEnabled() || FLOW_CTRL_LOG
/*  80 */       .isDebugEnabled()) {
/*  81 */       final String id = ioSession.getId();
/*  82 */       ClientH2StreamMultiplexerFactory clientH2StreamMultiplexerFactory = new ClientH2StreamMultiplexerFactory(this.httpProcessor, this.exchangeHandlerFactory, this.h2Config, this.charCodingConfig, new H2StreamListener()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  89 */             final FramePrinter framePrinter = new FramePrinter();
/*     */             
/*     */             private void logFrameInfo(String prefix, RawFrame frame) {
/*     */               try {
/*  93 */                 LogAppendable logAppendable = new LogAppendable(H2AsyncClientProtocolStarter.FRAME_LOG, prefix);
/*  94 */                 this.framePrinter.printFrameInfo(frame, logAppendable);
/*  95 */                 logAppendable.flush();
/*  96 */               } catch (IOException iOException) {}
/*     */             }
/*     */ 
/*     */             
/*     */             private void logFramePayload(String prefix, RawFrame frame) {
/*     */               try {
/* 102 */                 LogAppendable logAppendable = new LogAppendable(H2AsyncClientProtocolStarter.FRAME_PAYLOAD_LOG, prefix);
/* 103 */                 this.framePrinter.printPayload(frame, logAppendable);
/* 104 */                 logAppendable.flush();
/* 105 */               } catch (IOException iOException) {}
/*     */             }
/*     */ 
/*     */             
/*     */             private void logFlowControl(String prefix, int streamId, int delta, int actualSize) {
/* 110 */               H2AsyncClientProtocolStarter.FLOW_CTRL_LOG.debug("{} stream {} flow control {} -> {}", new Object[] { prefix, Integer.valueOf(streamId), Integer.valueOf(delta), Integer.valueOf(actualSize) });
/*     */             }
/*     */ 
/*     */             
/*     */             public void onHeaderInput(HttpConnection connection, int streamId, List<? extends Header> headers) {
/* 115 */               if (H2AsyncClientProtocolStarter.HEADER_LOG.isDebugEnabled()) {
/* 116 */                 for (int i = 0; i < headers.size(); i++) {
/* 117 */                   H2AsyncClientProtocolStarter.HEADER_LOG.debug("{} << {}", id, headers.get(i));
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onHeaderOutput(HttpConnection connection, int streamId, List<? extends Header> headers) {
/* 124 */               if (H2AsyncClientProtocolStarter.HEADER_LOG.isDebugEnabled()) {
/* 125 */                 for (int i = 0; i < headers.size(); i++) {
/* 126 */                   H2AsyncClientProtocolStarter.HEADER_LOG.debug("{} >> {}", id, headers.get(i));
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onFrameInput(HttpConnection connection, int streamId, RawFrame frame) {
/* 133 */               if (H2AsyncClientProtocolStarter.FRAME_LOG.isDebugEnabled()) {
/* 134 */                 logFrameInfo(id + " <<", frame);
/*     */               }
/* 136 */               if (H2AsyncClientProtocolStarter.FRAME_PAYLOAD_LOG.isDebugEnabled()) {
/* 137 */                 logFramePayload(id + " <<", frame);
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onFrameOutput(HttpConnection connection, int streamId, RawFrame frame) {
/* 143 */               if (H2AsyncClientProtocolStarter.FRAME_LOG.isDebugEnabled()) {
/* 144 */                 logFrameInfo(id + " >>", frame);
/*     */               }
/* 146 */               if (H2AsyncClientProtocolStarter.FRAME_PAYLOAD_LOG.isDebugEnabled()) {
/* 147 */                 logFramePayload(id + " >>", frame);
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onInputFlowControl(HttpConnection connection, int streamId, int delta, int actualSize) {
/* 153 */               if (H2AsyncClientProtocolStarter.FLOW_CTRL_LOG.isDebugEnabled()) {
/* 154 */                 logFlowControl(id + " <<", streamId, delta, actualSize);
/*     */               }
/*     */             }
/*     */ 
/*     */             
/*     */             public void onOutputFlowControl(HttpConnection connection, int streamId, int delta, int actualSize) {
/* 160 */               if (H2AsyncClientProtocolStarter.FLOW_CTRL_LOG.isDebugEnabled()) {
/* 161 */                 logFlowControl(id + " >>", streamId, delta, actualSize);
/*     */               }
/*     */             }
/*     */           });
/*     */       
/* 166 */       return (IOEventHandler)new ClientH2PrefaceHandler(ioSession, clientH2StreamMultiplexerFactory, false);
/*     */     } 
/* 168 */     ClientH2StreamMultiplexerFactory http2StreamHandlerFactory = new ClientH2StreamMultiplexerFactory(this.httpProcessor, this.exchangeHandlerFactory, this.h2Config, this.charCodingConfig, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     return (IOEventHandler)new ClientH2PrefaceHandler(ioSession, http2StreamHandlerFactory, false);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/H2AsyncClientProtocolStarter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */