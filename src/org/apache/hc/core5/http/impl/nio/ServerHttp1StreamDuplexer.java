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
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.ContentDecoder;
/*     */ import org.apache.hc.core5.http.nio.ContentEncoder;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
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
/*     */ 
/*     */ 
/*     */ @Internal
/*     */ public class ServerHttp1StreamDuplexer
/*     */   extends AbstractHttp1StreamDuplexer<HttpRequest, HttpResponse>
/*     */ {
/*     */   private final String scheme;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory;
/*     */   private final Http1Config http1Config;
/*     */   private final ConnectionReuseStrategy connectionReuseStrategy;
/*     */   private final Http1StreamListener streamListener;
/*     */   private final Queue<ServerHttp1StreamHandler> pipeline;
/*     */   private final Http1StreamChannel<HttpResponse> outputChannel;
/*     */   private volatile ServerHttp1StreamHandler outgoing;
/*     */   private volatile ServerHttp1StreamHandler incoming;
/*     */   
/*     */   public ServerHttp1StreamDuplexer(ProtocolIOSession ioSession, HttpProcessor httpProcessor, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, String scheme, Http1Config http1Config, CharCodingConfig charCodingConfig, ConnectionReuseStrategy connectionReuseStrategy, NHttpMessageParser<HttpRequest> incomingMessageParser, NHttpMessageWriter<HttpResponse> outgoingMessageWriter, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, final Http1StreamListener streamListener) {
/* 107 */     super(ioSession, http1Config, charCodingConfig, incomingMessageParser, outgoingMessageWriter, incomingContentStrategy, outgoingContentStrategy);
/*     */     
/* 109 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/* 110 */     this.exchangeHandlerFactory = (HandlerFactory<AsyncServerExchangeHandler>)Args.notNull(exchangeHandlerFactory, "Exchange handler factory");
/* 111 */     this.scheme = scheme;
/* 112 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/* 113 */     this.connectionReuseStrategy = (connectionReuseStrategy != null) ? connectionReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/*     */     
/* 115 */     this.streamListener = streamListener;
/* 116 */     this.pipeline = new ConcurrentLinkedQueue<>();
/* 117 */     this.outputChannel = new Http1StreamChannel<HttpResponse>()
/*     */       {
/*     */         public void close()
/*     */         {
/* 121 */           ServerHttp1StreamDuplexer.this.close(CloseMode.GRACEFUL);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void submit(HttpResponse response, boolean endStream, FlushMode flushMode) throws HttpException, IOException {
/* 129 */           if (streamListener != null) {
/* 130 */             streamListener.onResponseHead(ServerHttp1StreamDuplexer.this, response);
/*     */           }
/* 132 */           ServerHttp1StreamDuplexer.this.commitMessageHead(response, endStream, flushMode);
/*     */         }
/*     */ 
/*     */         
/*     */         public void requestOutput() {
/* 137 */           ServerHttp1StreamDuplexer.this.requestSessionOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public void suspendOutput() throws IOException {
/* 142 */           ServerHttp1StreamDuplexer.this.suspendSessionOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public Timeout getSocketTimeout() {
/* 147 */           return ServerHttp1StreamDuplexer.this.getSessionTimeout();
/*     */         }
/*     */ 
/*     */         
/*     */         public void setSocketTimeout(Timeout timeout) {
/* 152 */           ServerHttp1StreamDuplexer.this.setSessionTimeout(timeout);
/*     */         }
/*     */ 
/*     */         
/*     */         public int write(ByteBuffer src) throws IOException {
/* 157 */           return ServerHttp1StreamDuplexer.this.streamOutput(src);
/*     */         }
/*     */ 
/*     */         
/*     */         public void complete(List<? extends Header> trailers) throws IOException {
/* 162 */           ServerHttp1StreamDuplexer.this.endOutputStream(trailers);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isCompleted() {
/* 167 */           return ServerHttp1StreamDuplexer.this.isOutputCompleted();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean abortGracefully() throws IOException {
/* 172 */           AbstractHttp1StreamDuplexer.MessageDelineation messageDelineation = ServerHttp1StreamDuplexer.this.endOutputStream(null);
/* 173 */           return (messageDelineation != AbstractHttp1StreamDuplexer.MessageDelineation.MESSAGE_HEAD);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void activate() throws HttpException, IOException {}
/*     */ 
/*     */ 
/*     */         
/*     */         public String toString() {
/* 183 */           return "Http1StreamChannel[" + ServerHttp1StreamDuplexer.this + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void terminate(Exception exception) {
/* 191 */     if (this.incoming != null) {
/* 192 */       this.incoming.failed(exception);
/* 193 */       this.incoming.releaseResources();
/* 194 */       this.incoming = null;
/*     */     } 
/* 196 */     if (this.outgoing != null) {
/* 197 */       this.outgoing.failed(exception);
/* 198 */       this.outgoing.releaseResources();
/* 199 */       this.outgoing = null;
/*     */     } 
/*     */     while (true) {
/* 202 */       ServerHttp1StreamHandler handler = this.pipeline.poll();
/* 203 */       if (handler != null) {
/* 204 */         handler.failed(exception);
/* 205 */         handler.releaseResources();
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void disconnected() {
/* 214 */     if (this.incoming != null) {
/* 215 */       if (!this.incoming.isCompleted()) {
/* 216 */         this.incoming.failed((Exception)new ConnectionClosedException());
/*     */       }
/* 218 */       this.incoming.releaseResources();
/* 219 */       this.incoming = null;
/*     */     } 
/* 221 */     if (this.outgoing != null) {
/* 222 */       if (!this.outgoing.isCompleted()) {
/* 223 */         this.outgoing.failed((Exception)new ConnectionClosedException());
/*     */       }
/* 225 */       this.outgoing.releaseResources();
/* 226 */       this.outgoing = null;
/*     */     } 
/*     */     while (true) {
/* 229 */       ServerHttp1StreamHandler handler = this.pipeline.poll();
/* 230 */       if (handler != null) {
/* 231 */         handler.failed((Exception)new ConnectionClosedException());
/* 232 */         handler.releaseResources();
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void updateInputMetrics(HttpRequest request, BasicHttpConnectionMetrics connMetrics) {
/* 241 */     connMetrics.incrementRequestCount();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateOutputMetrics(HttpResponse response, BasicHttpConnectionMetrics connMetrics) {
/* 246 */     if (response.getCode() >= 200) {
/* 247 */       connMetrics.incrementResponseCount();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean handleIncomingMessage(HttpRequest request) throws HttpException {
/* 253 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContentDecoder createContentDecoder(long len, ReadableByteChannel channel, SessionInputBuffer buffer, BasicHttpTransportMetrics metrics) throws HttpException {
/* 262 */     if (len >= 0L)
/* 263 */       return new LengthDelimitedDecoder(channel, buffer, metrics, len); 
/* 264 */     if (len == -1L) {
/* 265 */       return new ChunkDecoder(channel, buffer, this.http1Config, metrics);
/*     */     }
/* 267 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleOutgoingMessage(HttpResponse response) throws HttpException {
/* 273 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContentEncoder createContentEncoder(long len, WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics) throws HttpException {
/* 282 */     int chunkSizeHint = (this.http1Config.getChunkSizeHint() >= 0) ? this.http1Config.getChunkSizeHint() : 2048;
/* 283 */     if (len >= 0L)
/* 284 */       return new LengthDelimitedEncoder(channel, buffer, metrics, len, chunkSizeHint); 
/* 285 */     if (len == -1L) {
/* 286 */       return new ChunkEncoder(channel, buffer, metrics, chunkSizeHint);
/*     */     }
/* 288 */     return new IdentityEncoder(channel, buffer, metrics, chunkSizeHint);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean inputIdle() {
/* 294 */     return (this.incoming == null);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean outputIdle() {
/* 299 */     return (this.outgoing == null && this.pipeline.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   HttpRequest parseMessageHead(boolean endOfStream) throws IOException, HttpException {
/*     */     try {
/* 305 */       return super.parseMessageHead(endOfStream);
/* 306 */     } catch (HttpException ex) {
/* 307 */       terminateExchange(ex);
/* 308 */       return null;
/*     */     } 
/*     */   }
/*     */   void terminateExchange(HttpException ex) throws HttpException, IOException {
/*     */     ServerHttp1StreamHandler streamHandler;
/* 313 */     suspendSessionInput();
/*     */     
/* 315 */     HttpCoreContext context = HttpCoreContext.create();
/* 316 */     context.setAttribute("http.ssl-session", getSSLSession());
/* 317 */     context.setAttribute("http.connection-endpoint", getEndpointDetails());
/* 318 */     if (this.outgoing == null) {
/* 319 */       streamHandler = new ServerHttp1StreamHandler(this.outputChannel, this.httpProcessor, this.connectionReuseStrategy, this.exchangeHandlerFactory, context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 325 */       this.outgoing = streamHandler;
/*     */     } else {
/* 327 */       streamHandler = new ServerHttp1StreamHandler(new DelayedOutputChannel(this.outputChannel), this.httpProcessor, this.connectionReuseStrategy, this.exchangeHandlerFactory, context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 333 */       this.pipeline.add(streamHandler);
/*     */     } 
/* 335 */     streamHandler.terminateExchange(ex);
/* 336 */     this.incoming = null;
/*     */   }
/*     */   
/*     */   void consumeHeader(HttpRequest request, EntityDetails entityDetails) throws HttpException, IOException {
/*     */     ServerHttp1StreamHandler streamHandler;
/* 341 */     if (this.streamListener != null) {
/* 342 */       this.streamListener.onRequestHead(this, request);
/*     */     }
/*     */     
/* 345 */     HttpCoreContext context = HttpCoreContext.create();
/* 346 */     context.setAttribute("http.ssl-session", getSSLSession());
/* 347 */     context.setAttribute("http.connection-endpoint", getEndpointDetails());
/* 348 */     if (this.outgoing == null) {
/* 349 */       streamHandler = new ServerHttp1StreamHandler(this.outputChannel, this.httpProcessor, this.connectionReuseStrategy, this.exchangeHandlerFactory, context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 355 */       this.outgoing = streamHandler;
/*     */     } else {
/* 357 */       streamHandler = new ServerHttp1StreamHandler(new DelayedOutputChannel(this.outputChannel), this.httpProcessor, this.connectionReuseStrategy, this.exchangeHandlerFactory, context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 363 */       this.pipeline.add(streamHandler);
/*     */     } 
/* 365 */     request.setScheme(this.scheme);
/* 366 */     streamHandler.consumeHeader(request, entityDetails);
/* 367 */     this.incoming = streamHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   void consumeData(ByteBuffer src) throws HttpException, IOException {
/* 372 */     Asserts.notNull(this.incoming, "Request stream handler");
/* 373 */     this.incoming.consumeData(src);
/*     */   }
/*     */ 
/*     */   
/*     */   void updateCapacity(CapacityChannel capacityChannel) throws HttpException, IOException {
/* 378 */     Asserts.notNull(this.incoming, "Request stream handler");
/* 379 */     this.incoming.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   void dataEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 384 */     Asserts.notNull(this.incoming, "Request stream handler");
/* 385 */     this.incoming.dataEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   void inputEnd() throws HttpException, IOException {
/* 390 */     if (this.incoming != null) {
/* 391 */       if (this.incoming.isCompleted()) {
/* 392 */         this.incoming.releaseResources();
/*     */       }
/* 394 */       this.incoming = null;
/*     */     } 
/* 396 */     if (isShuttingDown() && outputIdle() && inputIdle()) {
/* 397 */       shutdownSession(CloseMode.IMMEDIATE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void execute(RequestExecutionCommand executionCommand) throws HttpException {
/* 403 */     throw new HttpException("Illegal command: " + executionCommand.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isOutputReady() {
/* 408 */     return (this.outgoing != null && this.outgoing.isOutputReady());
/*     */   }
/*     */ 
/*     */   
/*     */   void produceOutput() throws HttpException, IOException {
/* 413 */     if (this.outgoing != null) {
/* 414 */       this.outgoing.produceOutput();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void outputEnd() throws HttpException, IOException {
/* 420 */     if (this.outgoing != null && this.outgoing.isResponseFinal()) {
/* 421 */       if (this.streamListener != null) {
/* 422 */         this.streamListener.onExchangeComplete(this, this.outgoing.keepAlive());
/*     */       }
/* 424 */       if (this.outgoing.isCompleted()) {
/* 425 */         this.outgoing.releaseResources();
/*     */       }
/* 427 */       this.outgoing = null;
/*     */     } 
/* 429 */     if (this.outgoing == null && isActive()) {
/* 430 */       ServerHttp1StreamHandler handler = this.pipeline.poll();
/* 431 */       if (handler != null) {
/* 432 */         this.outgoing = handler;
/* 433 */         handler.activateChannel();
/* 434 */         if (handler.isOutputReady()) {
/* 435 */           handler.produceOutput();
/*     */         }
/*     */       } 
/*     */     } 
/* 439 */     if (isShuttingDown() && outputIdle() && inputIdle()) {
/* 440 */       shutdownSession(CloseMode.IMMEDIATE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean handleTimeout() {
/* 446 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   void appendState(StringBuilder buf) {
/* 451 */     super.appendState(buf);
/* 452 */     buf.append(", incoming=[");
/* 453 */     if (this.incoming != null) {
/* 454 */       this.incoming.appendState(buf);
/*     */     }
/* 456 */     buf.append("], outgoing=[");
/* 457 */     if (this.outgoing != null) {
/* 458 */       this.outgoing.appendState(buf);
/*     */     }
/* 460 */     buf.append("], pipeline=");
/* 461 */     buf.append(this.pipeline.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 466 */     StringBuilder buf = new StringBuilder();
/* 467 */     buf.append("[");
/* 468 */     appendState(buf);
/* 469 */     buf.append("]");
/* 470 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static class DelayedOutputChannel
/*     */     implements Http1StreamChannel<HttpResponse>
/*     */   {
/*     */     private final Http1StreamChannel<HttpResponse> channel;
/*     */     private volatile boolean direct;
/*     */     private volatile HttpResponse delayedResponse;
/*     */     private volatile boolean completed;
/*     */     
/*     */     private DelayedOutputChannel(Http1StreamChannel<HttpResponse> channel) {
/* 482 */       this.channel = channel;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 487 */       this.channel.close();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void submit(HttpResponse response, boolean endStream, FlushMode flushMode) throws HttpException, IOException {
/* 495 */       synchronized (this) {
/* 496 */         if (this.direct) {
/* 497 */           this.channel.submit(response, endStream, flushMode);
/*     */         } else {
/* 499 */           this.delayedResponse = response;
/* 500 */           this.completed = endStream;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void suspendOutput() throws IOException {
/* 507 */       this.channel.suspendOutput();
/*     */     }
/*     */ 
/*     */     
/*     */     public void requestOutput() {
/* 512 */       this.channel.requestOutput();
/*     */     }
/*     */ 
/*     */     
/*     */     public Timeout getSocketTimeout() {
/* 517 */       return this.channel.getSocketTimeout();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSocketTimeout(Timeout timeout) {
/* 522 */       this.channel.setSocketTimeout(timeout);
/*     */     }
/*     */ 
/*     */     
/*     */     public int write(ByteBuffer src) throws IOException {
/* 527 */       synchronized (this) {
/* 528 */         return this.direct ? this.channel.write(src) : 0;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void complete(List<? extends Header> trailers) throws IOException {
/* 534 */       synchronized (this) {
/* 535 */         if (this.direct) {
/* 536 */           this.channel.complete(trailers);
/*     */         } else {
/* 538 */           this.completed = true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean abortGracefully() throws IOException {
/* 545 */       synchronized (this) {
/* 546 */         if (this.direct) {
/* 547 */           return this.channel.abortGracefully();
/*     */         }
/* 549 */         this.completed = true;
/* 550 */         return true;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCompleted() {
/* 556 */       synchronized (this) {
/* 557 */         return this.direct ? this.channel.isCompleted() : this.completed;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void activate() throws IOException, HttpException {
/* 563 */       synchronized (this) {
/* 564 */         this.direct = true;
/* 565 */         if (this.delayedResponse != null) {
/* 566 */           this.channel.submit(this.delayedResponse, this.completed, this.completed ? FlushMode.IMMEDIATE : FlushMode.BUFFER);
/* 567 */           this.delayedResponse = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ServerHttp1StreamDuplexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */