/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.RequestHeaderFieldsTooLargeException;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.command.ExecutableCommand;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http2.H2ConnectionException;
/*     */ import org.apache.hc.core5.http2.H2Error;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.frame.DefaultFrameFactory;
/*     */ import org.apache.hc.core5.http2.frame.FrameFactory;
/*     */ import org.apache.hc.core5.http2.frame.StreamIdGenerator;
/*     */ import org.apache.hc.core5.http2.hpack.HeaderListConstraintException;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
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
/*     */ @Internal
/*     */ public class ServerH2StreamMultiplexer
/*     */   extends AbstractH2StreamMultiplexer
/*     */ {
/*     */   private final HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory;
/*     */   
/*     */   public ServerH2StreamMultiplexer(ProtocolIOSession ioSession, FrameFactory frameFactory, HttpProcessor httpProcessor, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, CharCodingConfig charCodingConfig, H2Config h2Config, H2StreamListener streamListener) {
/*  75 */     super(ioSession, frameFactory, StreamIdGenerator.EVEN, httpProcessor, charCodingConfig, h2Config, streamListener);
/*  76 */     this.exchangeHandlerFactory = (HandlerFactory<AsyncServerExchangeHandler>)Args.notNull(exchangeHandlerFactory, "Handler factory");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerH2StreamMultiplexer(ProtocolIOSession ioSession, HttpProcessor httpProcessor, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, CharCodingConfig charCodingConfig, H2Config h2Config) {
/*  85 */     this(ioSession, DefaultFrameFactory.INSTANCE, httpProcessor, exchangeHandlerFactory, charCodingConfig, h2Config, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void acceptHeaderFrame() throws H2ConnectionException {}
/*     */ 
/*     */ 
/*     */   
/*     */   void acceptPushRequest() throws H2ConnectionException {}
/*     */ 
/*     */   
/*     */   void acceptPushFrame() throws H2ConnectionException {
/*  98 */     throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Push not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   H2StreamHandler createRemotelyInitiatedStream(H2StreamChannel channel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics, HandlerFactory<AsyncPushConsumer> pushHandlerFactory) throws IOException {
/* 107 */     HttpCoreContext context = HttpCoreContext.create();
/* 108 */     context.setAttribute("http.ssl-session", getSSLSession());
/* 109 */     context.setAttribute("http.connection-endpoint", getEndpointDetails());
/* 110 */     return new ServerH2StreamHandler(channel, httpProcessor, connMetrics, this.exchangeHandlerFactory, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   H2StreamHandler createLocallyInitiatedStream(ExecutableCommand command, H2StreamChannel channel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics) throws IOException {
/* 119 */     throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Illegal attempt to execute a request");
/*     */   }
/*     */ 
/*     */   
/*     */   List<Header> decodeHeaders(ByteBuffer payload) throws HttpException {
/*     */     try {
/* 125 */       return super.decodeHeaders(payload);
/* 126 */     } catch (HeaderListConstraintException ex) {
/* 127 */       throw new RequestHeaderFieldsTooLargeException(ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     StringBuilder buf = new StringBuilder();
/* 134 */     buf.append("[");
/* 135 */     appendState(buf);
/* 136 */     buf.append("]");
/* 137 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerH2StreamMultiplexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */