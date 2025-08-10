/*    */ package org.apache.hc.core5.http2.impl.nio;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*    */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*    */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*    */ import org.apache.hc.core5.http2.config.H2Config;
/*    */ import org.apache.hc.core5.http2.frame.DefaultFrameFactory;
/*    */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ @Internal
/*    */ public final class ClientH2StreamMultiplexerFactory
/*    */ {
/*    */   private final HttpProcessor httpProcessor;
/*    */   private final HandlerFactory<AsyncPushConsumer> pushHandlerFactory;
/*    */   private final H2Config h2Config;
/*    */   private final CharCodingConfig charCodingConfig;
/*    */   private final H2StreamListener streamListener;
/*    */   
/*    */   public ClientH2StreamMultiplexerFactory(HttpProcessor httpProcessor, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, H2Config h2Config, CharCodingConfig charCodingConfig, H2StreamListener streamListener) {
/* 63 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/* 64 */     this.pushHandlerFactory = pushHandlerFactory;
/* 65 */     this.h2Config = (h2Config != null) ? h2Config : H2Config.DEFAULT;
/* 66 */     this.charCodingConfig = (charCodingConfig != null) ? charCodingConfig : CharCodingConfig.DEFAULT;
/* 67 */     this.streamListener = streamListener;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientH2StreamMultiplexerFactory(HttpProcessor httpProcessor, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, H2StreamListener streamListener) {
/* 74 */     this(httpProcessor, pushHandlerFactory, null, null, streamListener);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientH2StreamMultiplexerFactory(HttpProcessor httpProcessor, H2StreamListener streamListener) {
/* 80 */     this(httpProcessor, null, streamListener);
/*    */   }
/*    */   
/*    */   public ClientH2StreamMultiplexer create(ProtocolIOSession ioSession) {
/* 84 */     return new ClientH2StreamMultiplexer(ioSession, DefaultFrameFactory.INSTANCE, this.httpProcessor, this.pushHandlerFactory, this.h2Config, this.charCodingConfig, this.streamListener);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ClientH2StreamMultiplexerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */