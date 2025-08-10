/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.LengthRequiredException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.message.MessageSupport;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.ContentDecoder;
/*     */ import org.apache.hc.core5.http.nio.ContentEncoder;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageParser;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageWriter;
/*     */ import org.apache.hc.core5.http.nio.SessionInputBuffer;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.http.nio.command.RequestExecutionCommand;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ @Internal
/*     */ public class ClientHttp1StreamDuplexer
/*     */   extends AbstractHttp1StreamDuplexer<HttpResponse, HttpRequest>
/*     */ {
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final ConnectionReuseStrategy connectionReuseStrategy;
/*     */   private final Http1Config http1Config;
/*     */   private final Http1StreamListener streamListener;
/*     */   private final Queue<ClientHttp1StreamHandler> pipeline;
/*     */   private final Http1StreamChannel<HttpRequest> outputChannel;
/*     */   private volatile ClientHttp1StreamHandler outgoing;
/*     */   private volatile ClientHttp1StreamHandler incoming;
/*     */   
/*     */   public ClientHttp1StreamDuplexer(ProtocolIOSession ioSession, HttpProcessor httpProcessor, Http1Config http1Config, CharCodingConfig charCodingConfig, ConnectionReuseStrategy connectionReuseStrategy, NHttpMessageParser<HttpResponse> incomingMessageParser, NHttpMessageWriter<HttpRequest> outgoingMessageWriter, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, final Http1StreamListener streamListener) {
/* 104 */     super(ioSession, http1Config, charCodingConfig, incomingMessageParser, outgoingMessageWriter, incomingContentStrategy, outgoingContentStrategy);
/*     */     
/* 106 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/* 107 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/* 108 */     this.connectionReuseStrategy = (connectionReuseStrategy != null) ? connectionReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/*     */     
/* 110 */     this.streamListener = streamListener;
/* 111 */     this.pipeline = new ConcurrentLinkedQueue<>();
/* 112 */     this.outputChannel = new Http1StreamChannel<HttpRequest>()
/*     */       {
/*     */         public void close()
/*     */         {
/* 116 */           ClientHttp1StreamDuplexer.this.shutdownSession(CloseMode.IMMEDIATE);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void submit(HttpRequest request, boolean endStream, FlushMode flushMode) throws HttpException, IOException {
/* 124 */           if (streamListener != null) {
/* 125 */             streamListener.onRequestHead(ClientHttp1StreamDuplexer.this, request);
/*     */           }
/* 127 */           ClientHttp1StreamDuplexer.this.commitMessageHead(request, endStream, flushMode);
/*     */         }
/*     */ 
/*     */         
/*     */         public void suspendOutput() throws IOException {
/* 132 */           ClientHttp1StreamDuplexer.this.suspendSessionOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public void requestOutput() {
/* 137 */           ClientHttp1StreamDuplexer.this.requestSessionOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public Timeout getSocketTimeout() {
/* 142 */           return ClientHttp1StreamDuplexer.this.getSessionTimeout();
/*     */         }
/*     */ 
/*     */         
/*     */         public void setSocketTimeout(Timeout timeout) {
/* 147 */           ClientHttp1StreamDuplexer.this.setSessionTimeout(timeout);
/*     */         }
/*     */ 
/*     */         
/*     */         public int write(ByteBuffer src) throws IOException {
/* 152 */           return ClientHttp1StreamDuplexer.this.streamOutput(src);
/*     */         }
/*     */ 
/*     */         
/*     */         public void complete(List<? extends Header> trailers) throws IOException {
/* 157 */           ClientHttp1StreamDuplexer.this.endOutputStream(trailers);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isCompleted() {
/* 162 */           return ClientHttp1StreamDuplexer.this.isOutputCompleted();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean abortGracefully() throws IOException {
/* 167 */           AbstractHttp1StreamDuplexer.MessageDelineation messageDelineation = ClientHttp1StreamDuplexer.this.endOutputStream(null);
/* 168 */           return (messageDelineation != AbstractHttp1StreamDuplexer.MessageDelineation.MESSAGE_HEAD);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void activate() throws HttpException, IOException {}
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void terminate(Exception exception) {
/* 180 */     if (this.incoming != null) {
/* 181 */       this.incoming.failed(exception);
/* 182 */       this.incoming.releaseResources();
/* 183 */       this.incoming = null;
/*     */     } 
/* 185 */     if (this.outgoing != null) {
/* 186 */       this.outgoing.failed(exception);
/* 187 */       this.outgoing.releaseResources();
/* 188 */       this.outgoing = null;
/*     */     } 
/*     */     while (true) {
/* 191 */       ClientHttp1StreamHandler handler = this.pipeline.poll();
/* 192 */       if (handler != null) {
/* 193 */         handler.failed(exception);
/* 194 */         handler.releaseResources();
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void disconnected() {
/* 203 */     if (this.incoming != null) {
/* 204 */       if (!this.incoming.isCompleted()) {
/* 205 */         this.incoming.failed((Exception)new ConnectionClosedException());
/*     */       }
/* 207 */       this.incoming.releaseResources();
/* 208 */       this.incoming = null;
/*     */     } 
/* 210 */     if (this.outgoing != null) {
/* 211 */       if (!this.outgoing.isCompleted()) {
/* 212 */         this.outgoing.failed((Exception)new ConnectionClosedException());
/*     */       }
/* 214 */       this.outgoing.releaseResources();
/* 215 */       this.outgoing = null;
/*     */     } 
/*     */     while (true) {
/* 218 */       ClientHttp1StreamHandler handler = this.pipeline.poll();
/* 219 */       if (handler != null) {
/* 220 */         handler.failed((Exception)new ConnectionClosedException());
/* 221 */         handler.releaseResources();
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void updateInputMetrics(HttpResponse response, BasicHttpConnectionMetrics connMetrics) {
/* 230 */     if (response.getCode() >= 200) {
/* 231 */       connMetrics.incrementRequestCount();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void updateOutputMetrics(HttpRequest request, BasicHttpConnectionMetrics connMetrics) {
/* 237 */     connMetrics.incrementRequestCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleIncomingMessage(HttpResponse response) throws HttpException {
/* 243 */     if (this.incoming == null) {
/* 244 */       this.incoming = this.pipeline.poll();
/*     */     }
/* 246 */     if (this.incoming == null) {
/* 247 */       throw new HttpException("Unexpected response");
/*     */     }
/* 249 */     return MessageSupport.canResponseHaveBody(this.incoming.getRequestMethod(), response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContentDecoder createContentDecoder(long len, ReadableByteChannel channel, SessionInputBuffer buffer, BasicHttpTransportMetrics metrics) throws HttpException {
/* 259 */     if (len >= 0L)
/* 260 */       return new LengthDelimitedDecoder(channel, buffer, metrics, len); 
/* 261 */     if (len == -1L) {
/* 262 */       return new ChunkDecoder(channel, buffer, this.http1Config, metrics);
/*     */     }
/* 264 */     return new IdentityDecoder(channel, buffer, metrics);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleOutgoingMessage(HttpRequest request) throws HttpException {
/* 270 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContentEncoder createContentEncoder(long len, WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics) throws HttpException {
/* 279 */     int chunkSizeHint = (this.http1Config.getChunkSizeHint() >= 0) ? this.http1Config.getChunkSizeHint() : 2048;
/* 280 */     if (len >= 0L)
/* 281 */       return new LengthDelimitedEncoder(channel, buffer, metrics, len, chunkSizeHint); 
/* 282 */     if (len == -1L) {
/* 283 */       return new ChunkEncoder(channel, buffer, metrics, chunkSizeHint);
/*     */     }
/* 285 */     throw new LengthRequiredException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean inputIdle() {
/* 291 */     return (this.incoming == null);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean outputIdle() {
/* 296 */     return (this.outgoing == null && this.pipeline.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   void outputEnd() throws HttpException, IOException {
/* 301 */     if (this.outgoing != null) {
/* 302 */       if (this.outgoing.isCompleted()) {
/* 303 */         this.outgoing.releaseResources();
/*     */       }
/* 305 */       this.outgoing = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void execute(RequestExecutionCommand executionCommand) throws HttpException, IOException {
/* 311 */     AsyncClientExchangeHandler exchangeHandler = executionCommand.getExchangeHandler();
/* 312 */     HttpCoreContext context = HttpCoreContext.adapt(executionCommand.getContext());
/* 313 */     context.setAttribute("http.ssl-session", getSSLSession());
/* 314 */     context.setAttribute("http.connection-endpoint", getEndpointDetails());
/* 315 */     ClientHttp1StreamHandler handler = new ClientHttp1StreamHandler(this.outputChannel, this.httpProcessor, this.http1Config, this.connectionReuseStrategy, exchangeHandler, context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     this.pipeline.add(handler);
/* 323 */     this.outgoing = handler;
/*     */     
/* 325 */     if (handler.isOutputReady()) {
/* 326 */       handler.produceOutput();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isOutputReady() {
/* 332 */     return (this.outgoing != null && this.outgoing.isOutputReady());
/*     */   }
/*     */ 
/*     */   
/*     */   void produceOutput() throws HttpException, IOException {
/* 337 */     if (this.outgoing != null) {
/* 338 */       this.outgoing.produceOutput();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void consumeHeader(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException {
/* 344 */     if (this.streamListener != null) {
/* 345 */       this.streamListener.onResponseHead(this, response);
/*     */     }
/* 347 */     Asserts.notNull(this.incoming, "Response stream handler");
/* 348 */     this.incoming.consumeHeader(response, entityDetails);
/*     */   }
/*     */ 
/*     */   
/*     */   void consumeData(ByteBuffer src) throws HttpException, IOException {
/* 353 */     Asserts.notNull(this.incoming, "Response stream handler");
/* 354 */     this.incoming.consumeData(src);
/*     */   }
/*     */ 
/*     */   
/*     */   void updateCapacity(CapacityChannel capacityChannel) throws HttpException, IOException {
/* 359 */     Asserts.notNull(this.incoming, "Response stream handler");
/* 360 */     this.incoming.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   void dataEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 365 */     Asserts.notNull(this.incoming, "Response stream handler");
/* 366 */     this.incoming.dataEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   void inputEnd() throws HttpException, IOException {
/* 371 */     if (this.incoming != null && this.incoming.isResponseFinal()) {
/* 372 */       if (this.streamListener != null) {
/* 373 */         this.streamListener.onExchangeComplete(this, isOpen());
/*     */       }
/* 375 */       if (this.incoming.isCompleted()) {
/* 376 */         this.incoming.releaseResources();
/*     */       }
/* 378 */       this.incoming = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean handleTimeout() {
/* 384 */     return (this.outgoing != null && this.outgoing.handleTimeout());
/*     */   }
/*     */ 
/*     */   
/*     */   void appendState(StringBuilder buf) {
/* 389 */     super.appendState(buf);
/* 390 */     super.appendState(buf);
/* 391 */     buf.append(", incoming=[");
/* 392 */     if (this.incoming != null) {
/* 393 */       this.incoming.appendState(buf);
/*     */     }
/* 395 */     buf.append("], outgoing=[");
/* 396 */     if (this.outgoing != null) {
/* 397 */       this.outgoing.appendState(buf);
/*     */     }
/* 399 */     buf.append("], pipeline=");
/* 400 */     buf.append(this.pipeline.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 405 */     StringBuilder buf = new StringBuilder();
/* 406 */     buf.append("[");
/* 407 */     appendState(buf);
/* 408 */     buf.append("]");
/* 409 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ClientHttp1StreamDuplexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */