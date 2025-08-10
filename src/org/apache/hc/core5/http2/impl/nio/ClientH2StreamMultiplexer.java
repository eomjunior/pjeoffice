/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.command.ExecutableCommand;
/*     */ import org.apache.hc.core5.http.nio.command.RequestExecutionCommand;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http2.H2ConnectionException;
/*     */ import org.apache.hc.core5.http2.H2Error;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.frame.DefaultFrameFactory;
/*     */ import org.apache.hc.core5.http2.frame.FrameFactory;
/*     */ import org.apache.hc.core5.http2.frame.StreamIdGenerator;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
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
/*     */ @Internal
/*     */ public class ClientH2StreamMultiplexer
/*     */   extends AbstractH2StreamMultiplexer
/*     */ {
/*     */   private final HandlerFactory<AsyncPushConsumer> pushHandlerFactory;
/*     */   
/*     */   public ClientH2StreamMultiplexer(ProtocolIOSession ioSession, FrameFactory frameFactory, HttpProcessor httpProcessor, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, H2Config h2Config, CharCodingConfig charCodingConfig, H2StreamListener streamListener) {
/*  69 */     super(ioSession, frameFactory, StreamIdGenerator.ODD, httpProcessor, charCodingConfig, h2Config, streamListener);
/*  70 */     this.pushHandlerFactory = pushHandlerFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientH2StreamMultiplexer(ProtocolIOSession ioSession, HttpProcessor httpProcessor, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, H2Config h2Config, CharCodingConfig charCodingConfig) {
/*  79 */     this(ioSession, DefaultFrameFactory.INSTANCE, httpProcessor, pushHandlerFactory, h2Config, charCodingConfig, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientH2StreamMultiplexer(ProtocolIOSession ioSession, HttpProcessor httpProcessor, H2Config h2Config, CharCodingConfig charCodingConfig) {
/*  87 */     this(ioSession, httpProcessor, null, h2Config, charCodingConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   void acceptHeaderFrame() throws H2ConnectionException {
/*  92 */     throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal HEADERS frame");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void acceptPushFrame() throws H2ConnectionException {}
/*     */ 
/*     */   
/*     */   void acceptPushRequest() throws H2ConnectionException {
/* 101 */     throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Illegal attempt to push a response");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   H2StreamHandler createLocallyInitiatedStream(ExecutableCommand command, H2StreamChannel channel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics) throws IOException {
/* 110 */     if (command instanceof RequestExecutionCommand) {
/* 111 */       RequestExecutionCommand executionCommand = (RequestExecutionCommand)command;
/* 112 */       AsyncClientExchangeHandler exchangeHandler = executionCommand.getExchangeHandler();
/* 113 */       HandlerFactory<AsyncPushConsumer> pushHandlerFactory = executionCommand.getPushHandlerFactory();
/* 114 */       HttpCoreContext context = HttpCoreContext.adapt(executionCommand.getContext());
/* 115 */       context.setAttribute("http.ssl-session", getSSLSession());
/* 116 */       context.setAttribute("http.connection-endpoint", getEndpointDetails());
/* 117 */       return new ClientH2StreamHandler(channel, httpProcessor, connMetrics, exchangeHandler, (pushHandlerFactory != null) ? pushHandlerFactory : this.pushHandlerFactory, context);
/*     */     } 
/*     */ 
/*     */     
/* 121 */     throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Unexpected executable command");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   H2StreamHandler createRemotelyInitiatedStream(H2StreamChannel channel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics, HandlerFactory<AsyncPushConsumer> pushHandlerFactory) throws IOException {
/* 130 */     HttpCoreContext context = HttpCoreContext.create();
/* 131 */     context.setAttribute("http.ssl-session", getSSLSession());
/* 132 */     context.setAttribute("http.connection-endpoint", getEndpointDetails());
/* 133 */     return new ClientPushH2StreamHandler(channel, httpProcessor, connMetrics, (pushHandlerFactory != null) ? pushHandlerFactory : this.pushHandlerFactory, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 140 */     StringBuilder buf = new StringBuilder();
/* 141 */     buf.append("[");
/* 142 */     appendState(buf);
/* 143 */     buf.append("]");
/* 144 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ClientH2StreamMultiplexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */