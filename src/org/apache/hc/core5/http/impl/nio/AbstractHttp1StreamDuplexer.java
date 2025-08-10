/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.EndpointDetails;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.HttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.Message;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.BasicEndpointDetails;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.impl.CharCodingSupport;
/*     */ import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.impl.IncomingEntityDetails;
/*     */ import org.apache.hc.core5.http.io.HttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.ContentDecoder;
/*     */ import org.apache.hc.core5.http.nio.ContentEncoder;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageParser;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageWriter;
/*     */ import org.apache.hc.core5.http.nio.SessionInputBuffer;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.http.nio.command.CommandSupport;
/*     */ import org.apache.hc.core5.http.nio.command.RequestExecutionCommand;
/*     */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.SocketTimeoutExceptionFactory;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Identifiable;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractHttp1StreamDuplexer<IncomingMessage extends HttpMessage, OutgoingMessage extends HttpMessage>
/*     */   implements Identifiable, HttpConnection
/*     */ {
/*     */   private final ProtocolIOSession ioSession;
/*     */   private final Http1Config http1Config;
/*     */   private final SessionInputBufferImpl inbuf;
/*     */   private final SessionOutputBufferImpl outbuf;
/*     */   private final BasicHttpTransportMetrics inTransportMetrics;
/*     */   private final BasicHttpTransportMetrics outTransportMetrics;
/*     */   private final BasicHttpConnectionMetrics connMetrics;
/*     */   private final NHttpMessageParser<IncomingMessage> incomingMessageParser;
/*     */   private final NHttpMessageWriter<OutgoingMessage> outgoingMessageWriter;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final ByteBuffer contentBuffer;
/*     */   private final AtomicInteger outputRequests;
/*     */   private volatile Message<IncomingMessage, ContentDecoder> incomingMessage;
/*     */   private volatile Message<OutgoingMessage, ContentEncoder> outgoingMessage;
/*     */   private volatile ConnectionState connState;
/*     */   private volatile CapacityWindow capacityWindow;
/*     */   private volatile ProtocolVersion version;
/*     */   private volatile EndpointDetails endpointDetails;
/*     */   
/*     */   private enum ConnectionState
/*     */   {
/*  84 */     READY, ACTIVE, GRACEFUL_SHUTDOWN, SHUTDOWN;
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
/*     */   AbstractHttp1StreamDuplexer(ProtocolIOSession ioSession, Http1Config http1Config, CharCodingConfig charCodingConfig, NHttpMessageParser<IncomingMessage> incomingMessageParser, NHttpMessageWriter<OutgoingMessage> outgoingMessageWriter, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
/* 116 */     this.ioSession = (ProtocolIOSession)Args.notNull(ioSession, "I/O session");
/* 117 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/* 118 */     int bufferSize = this.http1Config.getBufferSize();
/* 119 */     this
/*     */       
/* 121 */       .inbuf = new SessionInputBufferImpl(bufferSize, Math.min(bufferSize, 512), this.http1Config.getMaxLineLength(), CharCodingSupport.createDecoder(charCodingConfig));
/* 122 */     this
/* 123 */       .outbuf = new SessionOutputBufferImpl(bufferSize, Math.min(bufferSize, 512), CharCodingSupport.createEncoder(charCodingConfig));
/* 124 */     this.inTransportMetrics = new BasicHttpTransportMetrics();
/* 125 */     this.outTransportMetrics = new BasicHttpTransportMetrics();
/* 126 */     this.connMetrics = new BasicHttpConnectionMetrics((HttpTransportMetrics)this.inTransportMetrics, (HttpTransportMetrics)this.outTransportMetrics);
/* 127 */     this.incomingMessageParser = incomingMessageParser;
/* 128 */     this.outgoingMessageWriter = outgoingMessageWriter;
/* 129 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/* 131 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE;
/*     */     
/* 133 */     this.contentBuffer = ByteBuffer.allocate(this.http1Config.getBufferSize());
/* 134 */     this.outputRequests = new AtomicInteger(0);
/* 135 */     this.connState = ConnectionState.READY;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 140 */     return this.ioSession.getId();
/*     */   }
/*     */   
/*     */   boolean isActive() {
/* 144 */     return (this.connState == ConnectionState.ACTIVE);
/*     */   }
/*     */   
/*     */   boolean isShuttingDown() {
/* 148 */     return (this.connState.compareTo(ConnectionState.GRACEFUL_SHUTDOWN) >= 0);
/*     */   }
/*     */   
/*     */   void shutdownSession(CloseMode closeMode) {
/* 152 */     if (closeMode == CloseMode.GRACEFUL) {
/* 153 */       this.connState = ConnectionState.GRACEFUL_SHUTDOWN;
/* 154 */       this.ioSession.enqueue((Command)ShutdownCommand.GRACEFUL, Command.Priority.NORMAL);
/*     */     } else {
/* 156 */       this.connState = ConnectionState.SHUTDOWN;
/* 157 */       this.ioSession.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   void shutdownSession(Exception cause) {
/* 162 */     this.connState = ConnectionState.SHUTDOWN;
/*     */     try {
/* 164 */       terminate(cause);
/*     */     } finally {
/*     */       CloseMode closeMode;
/* 167 */       if (cause instanceof ConnectionClosedException) {
/* 168 */         closeMode = CloseMode.GRACEFUL;
/* 169 */       } else if (cause instanceof IOException) {
/* 170 */         closeMode = CloseMode.IMMEDIATE;
/*     */       } else {
/* 172 */         closeMode = CloseMode.GRACEFUL;
/*     */       } 
/* 174 */       this.ioSession.close(closeMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void disconnected();
/*     */ 
/*     */   
/*     */   abstract void terminate(Exception paramException);
/*     */ 
/*     */   
/*     */   abstract void updateInputMetrics(IncomingMessage paramIncomingMessage, BasicHttpConnectionMetrics paramBasicHttpConnectionMetrics);
/*     */ 
/*     */   
/*     */   abstract void updateOutputMetrics(OutgoingMessage paramOutgoingMessage, BasicHttpConnectionMetrics paramBasicHttpConnectionMetrics);
/*     */ 
/*     */   
/*     */   abstract void consumeHeader(IncomingMessage paramIncomingMessage, EntityDetails paramEntityDetails) throws HttpException, IOException;
/*     */ 
/*     */   
/*     */   abstract boolean handleIncomingMessage(IncomingMessage paramIncomingMessage) throws HttpException;
/*     */ 
/*     */   
/*     */   abstract boolean handleOutgoingMessage(OutgoingMessage paramOutgoingMessage) throws HttpException;
/*     */   
/*     */   abstract ContentDecoder createContentDecoder(long paramLong, ReadableByteChannel paramReadableByteChannel, SessionInputBuffer paramSessionInputBuffer, BasicHttpTransportMetrics paramBasicHttpTransportMetrics) throws HttpException;
/*     */   
/*     */   abstract ContentEncoder createContentEncoder(long paramLong, WritableByteChannel paramWritableByteChannel, SessionOutputBuffer paramSessionOutputBuffer, BasicHttpTransportMetrics paramBasicHttpTransportMetrics) throws HttpException;
/*     */   
/*     */   abstract void consumeData(ByteBuffer paramByteBuffer) throws HttpException, IOException;
/*     */   
/*     */   abstract void updateCapacity(CapacityChannel paramCapacityChannel) throws HttpException, IOException;
/*     */   
/*     */   abstract void dataEnd(List<? extends Header> paramList) throws HttpException, IOException;
/*     */   
/*     */   abstract boolean isOutputReady();
/*     */   
/*     */   abstract void produceOutput() throws HttpException, IOException;
/*     */   
/*     */   abstract void execute(RequestExecutionCommand paramRequestExecutionCommand) throws HttpException, IOException;
/*     */   
/*     */   abstract void inputEnd() throws HttpException, IOException;
/*     */   
/*     */   abstract void outputEnd() throws HttpException, IOException;
/*     */   
/*     */   abstract boolean inputIdle();
/*     */   
/*     */   abstract boolean outputIdle();
/*     */   
/*     */   abstract boolean handleTimeout();
/*     */   
/*     */   private void processCommands() throws HttpException, IOException {
/*     */     Command command;
/*     */     while (true) {
/* 228 */       command = this.ioSession.poll();
/* 229 */       if (command == null) {
/*     */         return;
/*     */       }
/* 232 */       if (command instanceof ShutdownCommand) {
/* 233 */         ShutdownCommand shutdownCommand = (ShutdownCommand)command;
/* 234 */         requestShutdown(shutdownCommand.getType()); continue;
/* 235 */       }  if (command instanceof RequestExecutionCommand) {
/* 236 */         if (this.connState.compareTo(ConnectionState.GRACEFUL_SHUTDOWN) >= 0) {
/* 237 */           command.cancel(); continue;
/*     */         } 
/* 239 */         execute((RequestExecutionCommand)command); return;
/*     */       } 
/*     */       break;
/*     */     } 
/* 243 */     throw new HttpException("Unexpected command: " + command.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onConnect() throws HttpException, IOException {
/* 249 */     if (this.connState == ConnectionState.READY) {
/* 250 */       this.connState = ConnectionState.ACTIVE;
/* 251 */       processCommands();
/*     */     } 
/*     */   }
/*     */   
/*     */   IncomingMessage parseMessageHead(boolean endOfStream) throws IOException, HttpException {
/* 256 */     HttpMessage httpMessage = (HttpMessage)this.incomingMessageParser.parse(this.inbuf, endOfStream);
/* 257 */     if (httpMessage != null) {
/* 258 */       this.incomingMessageParser.reset();
/*     */     }
/* 260 */     return (IncomingMessage)httpMessage;
/*     */   }
/*     */   
/*     */   public final void onInput(ByteBuffer src) throws HttpException, IOException {
/* 264 */     if (src != null) {
/* 265 */       int n = src.remaining();
/* 266 */       this.inbuf.put(src);
/* 267 */       this.inTransportMetrics.incrementBytesTransferred(n);
/*     */     } 
/*     */     
/* 270 */     if (this.connState.compareTo(ConnectionState.GRACEFUL_SHUTDOWN) >= 0 && this.inbuf.hasData() && inputIdle()) {
/* 271 */       this.ioSession.clearEvent(1);
/*     */       
/*     */       return;
/*     */     } 
/* 275 */     boolean endOfStream = false;
/* 276 */     if (this.incomingMessage == null) {
/* 277 */       int bytesRead = this.inbuf.fill((ReadableByteChannel)this.ioSession);
/* 278 */       if (bytesRead > 0) {
/* 279 */         this.inTransportMetrics.incrementBytesTransferred(bytesRead);
/*     */       }
/* 281 */       endOfStream = (bytesRead == -1);
/*     */     } 
/*     */     
/*     */     do {
/* 285 */       if (this.incomingMessage == null) {
/*     */         
/* 287 */         IncomingMessage messageHead = parseMessageHead(endOfStream);
/* 288 */         if (messageHead != null) {
/* 289 */           ContentDecoder contentDecoder1; this.version = messageHead.getVersion();
/*     */           
/* 291 */           updateInputMetrics(messageHead, this.connMetrics);
/*     */           
/* 293 */           if (handleIncomingMessage(messageHead)) {
/* 294 */             long len = this.incomingContentStrategy.determineLength((HttpMessage)messageHead);
/* 295 */             contentDecoder1 = createContentDecoder(len, (ReadableByteChannel)this.ioSession, this.inbuf, this.inTransportMetrics);
/* 296 */             consumeHeader(messageHead, (contentDecoder1 != null) ? (EntityDetails)new IncomingEntityDetails((MessageHeaders)messageHead, len) : null);
/*     */           } else {
/* 298 */             consumeHeader(messageHead, null);
/* 299 */             contentDecoder1 = null;
/*     */           } 
/* 301 */           this.capacityWindow = new CapacityWindow(this.http1Config.getInitialWindowSize(), (IOSession)this.ioSession);
/* 302 */           if (contentDecoder1 != null) {
/* 303 */             this.incomingMessage = new Message((MessageHeaders)messageHead, contentDecoder1);
/*     */           } else {
/* 305 */             inputEnd();
/* 306 */             if (this.connState.compareTo(ConnectionState.ACTIVE) == 0) {
/* 307 */               this.ioSession.setEvent(1);
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 315 */       if (this.incomingMessage == null)
/* 316 */         continue;  ContentDecoder contentDecoder = (ContentDecoder)this.incomingMessage.getBody();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 322 */       int bytesRead = contentDecoder.read(this.contentBuffer);
/* 323 */       if (bytesRead > 0) {
/* 324 */         this.contentBuffer.flip();
/* 325 */         consumeData(this.contentBuffer);
/* 326 */         this.contentBuffer.clear();
/* 327 */         int capacity = this.capacityWindow.removeCapacity(bytesRead);
/* 328 */         if (capacity <= 0 && 
/* 329 */           !contentDecoder.isCompleted()) {
/* 330 */           updateCapacity(this.capacityWindow);
/*     */         }
/*     */       } 
/*     */       
/* 334 */       if (contentDecoder.isCompleted()) {
/* 335 */         dataEnd(contentDecoder.getTrailers());
/* 336 */         this.capacityWindow.close();
/* 337 */         this.incomingMessage = null;
/* 338 */         this.ioSession.setEvent(1);
/* 339 */         inputEnd();
/* 340 */       } else if (bytesRead == 0) {
/*     */         
/*     */         break;
/*     */       } 
/* 344 */     } while (this.inbuf.hasData());
/*     */     
/* 346 */     if (endOfStream && !this.inbuf.hasData()) {
/* 347 */       if (outputIdle() && inputIdle()) {
/* 348 */         requestShutdown(CloseMode.GRACEFUL);
/*     */       } else {
/* 350 */         shutdownSession((Exception)new ConnectionClosedException("Connection closed by peer"));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public final void onOutput() throws IOException, HttpException {
/* 356 */     this.ioSession.getLock().lock();
/*     */     try {
/* 358 */       if (this.outbuf.hasData()) {
/* 359 */         int bytesWritten = this.outbuf.flush((WritableByteChannel)this.ioSession);
/* 360 */         if (bytesWritten > 0) {
/* 361 */           this.outTransportMetrics.incrementBytesTransferred(bytesWritten);
/*     */         }
/*     */       } 
/*     */     } finally {
/* 365 */       this.ioSession.getLock().unlock();
/*     */     } 
/* 367 */     if (this.connState.compareTo(ConnectionState.SHUTDOWN) < 0) {
/* 368 */       boolean outputEnd; int pendingOutputRequests = this.outputRequests.get();
/* 369 */       produceOutput();
/* 370 */       boolean outputPending = isOutputReady();
/*     */       
/* 372 */       this.ioSession.getLock().lock();
/*     */       try {
/* 374 */         if (!outputPending && !this.outbuf.hasData() && this.outputRequests.compareAndSet(pendingOutputRequests, 0)) {
/* 375 */           this.ioSession.clearEvent(4);
/*     */         } else {
/* 377 */           this.outputRequests.addAndGet(-pendingOutputRequests);
/*     */         } 
/* 379 */         outputEnd = (this.outgoingMessage == null && !this.outbuf.hasData());
/*     */       } finally {
/* 381 */         this.ioSession.getLock().unlock();
/*     */       } 
/* 383 */       if (outputEnd) {
/* 384 */         outputEnd();
/* 385 */         if (this.connState.compareTo(ConnectionState.ACTIVE) == 0) {
/* 386 */           processCommands();
/* 387 */         } else if (this.connState.compareTo(ConnectionState.GRACEFUL_SHUTDOWN) >= 0 && inputIdle() && outputIdle()) {
/* 388 */           this.connState = ConnectionState.SHUTDOWN;
/*     */         } 
/*     */       } 
/*     */     } 
/* 392 */     if (this.connState.compareTo(ConnectionState.SHUTDOWN) >= 0) {
/* 393 */       this.ioSession.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public final void onTimeout(Timeout timeout) throws IOException, HttpException {
/* 398 */     if (!handleTimeout()) {
/* 399 */       onException(SocketTimeoutExceptionFactory.create(timeout));
/*     */     }
/*     */   }
/*     */   
/*     */   public final void onException(Exception ex) {
/* 404 */     shutdownSession(ex);
/* 405 */     CommandSupport.failCommands((IOSession)this.ioSession, ex);
/*     */   }
/*     */   
/*     */   public final void onDisconnect() {
/* 409 */     disconnected();
/* 410 */     CommandSupport.cancelCommands((IOSession)this.ioSession);
/*     */   }
/*     */   
/*     */   void requestShutdown(CloseMode closeMode) {
/* 414 */     switch (closeMode) {
/*     */       case GRACEFUL:
/* 416 */         if (this.connState == ConnectionState.ACTIVE) {
/* 417 */           this.connState = ConnectionState.GRACEFUL_SHUTDOWN;
/*     */         }
/*     */         break;
/*     */       case IMMEDIATE:
/* 421 */         this.connState = ConnectionState.SHUTDOWN;
/*     */         break;
/*     */     } 
/* 424 */     this.ioSession.setEvent(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void commitMessageHead(OutgoingMessage messageHead, boolean endStream, FlushMode flushMode) throws HttpException, IOException {
/* 431 */     this.ioSession.getLock().lock();
/*     */     try {
/* 433 */       this.outgoingMessageWriter.write((MessageHeaders)messageHead, this.outbuf);
/* 434 */       updateOutputMetrics(messageHead, this.connMetrics);
/* 435 */       if (!endStream) {
/*     */         ContentEncoder contentEncoder;
/* 437 */         if (handleOutgoingMessage(messageHead)) {
/* 438 */           long len = this.outgoingContentStrategy.determineLength((HttpMessage)messageHead);
/* 439 */           contentEncoder = createContentEncoder(len, (WritableByteChannel)this.ioSession, this.outbuf, this.outTransportMetrics);
/*     */         } else {
/* 441 */           contentEncoder = null;
/*     */         } 
/* 443 */         if (contentEncoder != null) {
/* 444 */           this.outgoingMessage = new Message((MessageHeaders)messageHead, contentEncoder);
/*     */         }
/*     */       } 
/* 447 */       this.outgoingMessageWriter.reset();
/* 448 */       if (flushMode == FlushMode.IMMEDIATE) {
/* 449 */         int bytesWritten = this.outbuf.flush((WritableByteChannel)this.ioSession);
/* 450 */         if (bytesWritten > 0) {
/* 451 */           this.outTransportMetrics.incrementBytesTransferred(bytesWritten);
/*     */         }
/*     */       } 
/* 454 */       this.ioSession.setEvent(4);
/*     */     } finally {
/* 456 */       this.ioSession.getLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   void requestSessionInput() {
/* 461 */     this.ioSession.setEvent(1);
/*     */   }
/*     */   
/*     */   void requestSessionOutput() {
/* 465 */     this.outputRequests.incrementAndGet();
/* 466 */     this.ioSession.setEvent(4);
/*     */   }
/*     */   
/*     */   Timeout getSessionTimeout() {
/* 470 */     return this.ioSession.getSocketTimeout();
/*     */   }
/*     */   
/*     */   void setSessionTimeout(Timeout timeout) {
/* 474 */     this.ioSession.setSocketTimeout(timeout);
/*     */   }
/*     */   
/*     */   void suspendSessionInput() {
/* 478 */     this.ioSession.clearEvent(1);
/*     */   }
/*     */   
/*     */   void suspendSessionOutput() throws IOException {
/* 482 */     this.ioSession.getLock().lock();
/*     */     try {
/* 484 */       if (this.outbuf.hasData()) {
/* 485 */         int bytesWritten = this.outbuf.flush((WritableByteChannel)this.ioSession);
/* 486 */         if (bytesWritten > 0) {
/* 487 */           this.outTransportMetrics.incrementBytesTransferred(bytesWritten);
/*     */         }
/*     */       } else {
/* 490 */         this.ioSession.clearEvent(4);
/*     */       } 
/*     */     } finally {
/* 493 */       this.ioSession.getLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   int streamOutput(ByteBuffer src) throws IOException {
/* 498 */     this.ioSession.getLock().lock();
/*     */     try {
/* 500 */       if (this.outgoingMessage == null) {
/* 501 */         throw new ClosedChannelException();
/*     */       }
/* 503 */       ContentEncoder contentEncoder = (ContentEncoder)this.outgoingMessage.getBody();
/* 504 */       int bytesWritten = contentEncoder.write(src);
/* 505 */       if (bytesWritten > 0) {
/* 506 */         this.ioSession.setEvent(4);
/*     */       }
/* 508 */       return bytesWritten;
/*     */     } finally {
/* 510 */       this.ioSession.getLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/* 514 */   enum MessageDelineation { NONE, CHUNK_CODED, MESSAGE_HEAD; }
/*     */   
/*     */   MessageDelineation endOutputStream(List<? extends Header> trailers) throws IOException {
/* 517 */     this.ioSession.getLock().lock();
/*     */     try {
/* 519 */       if (this.outgoingMessage == null) {
/* 520 */         return MessageDelineation.NONE;
/*     */       }
/* 522 */       ContentEncoder contentEncoder = (ContentEncoder)this.outgoingMessage.getBody();
/* 523 */       contentEncoder.complete(trailers);
/* 524 */       this.ioSession.setEvent(4);
/* 525 */       this.outgoingMessage = null;
/* 526 */       return (contentEncoder instanceof ChunkEncoder) ? MessageDelineation.CHUNK_CODED : MessageDelineation.MESSAGE_HEAD;
/*     */     }
/*     */     finally {
/*     */       
/* 530 */       this.ioSession.getLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean isOutputCompleted() {
/* 535 */     this.ioSession.getLock().lock();
/*     */     try {
/* 537 */       if (this.outgoingMessage == null) {
/* 538 */         return true;
/*     */       }
/* 540 */       ContentEncoder contentEncoder = (ContentEncoder)this.outgoingMessage.getBody();
/* 541 */       return contentEncoder.isCompleted();
/*     */     } finally {
/* 543 */       this.ioSession.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 549 */     this.ioSession.enqueue((Command)ShutdownCommand.GRACEFUL, Command.Priority.NORMAL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 554 */     this.ioSession.enqueue((Command)new ShutdownCommand(closeMode), Command.Priority.IMMEDIATE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 559 */     return (this.connState == ConnectionState.ACTIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 564 */     return this.ioSession.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 569 */     this.ioSession.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointDetails getEndpointDetails() {
/* 574 */     if (this.endpointDetails == null) {
/* 575 */       this
/*     */ 
/*     */ 
/*     */         
/* 579 */         .endpointDetails = (EndpointDetails)new BasicEndpointDetails(this.ioSession.getRemoteAddress(), this.ioSession.getLocalAddress(), (HttpConnectionMetrics)this.connMetrics, this.ioSession.getSocketTimeout());
/*     */     }
/* 581 */     return this.endpointDetails;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 586 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 591 */     return this.ioSession.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 596 */     return this.ioSession.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSSLSession() {
/* 601 */     TlsDetails tlsDetails = this.ioSession.getTlsDetails();
/* 602 */     return (tlsDetails != null) ? tlsDetails.getSSLSession() : null;
/*     */   }
/*     */   
/*     */   void appendState(StringBuilder buf) {
/* 606 */     buf.append("connState=").append(this.connState)
/* 607 */       .append(", inbuf=").append(this.inbuf)
/* 608 */       .append(", outbuf=").append(this.outbuf)
/* 609 */       .append(", inputWindow=").append((this.capacityWindow != null) ? this.capacityWindow.getWindow() : 0);
/*     */   }
/*     */   
/*     */   static class CapacityWindow implements CapacityChannel {
/*     */     private final IOSession ioSession;
/*     */     private final Object lock;
/*     */     private int window;
/*     */     private boolean closed;
/*     */     
/*     */     CapacityWindow(int window, IOSession ioSession) {
/* 619 */       this.window = window;
/* 620 */       this.ioSession = ioSession;
/* 621 */       this.lock = new Object();
/*     */     }
/*     */ 
/*     */     
/*     */     public void update(int increment) throws IOException {
/* 626 */       synchronized (this.lock) {
/* 627 */         if (this.closed) {
/*     */           return;
/*     */         }
/* 630 */         if (increment > 0) {
/* 631 */           updateWindow(increment);
/* 632 */           this.ioSession.setEvent(1);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int removeCapacity(int delta) {
/* 642 */       synchronized (this.lock) {
/* 643 */         updateWindow(-delta);
/* 644 */         if (this.window <= 0) {
/* 645 */           this.ioSession.clearEvent(1);
/*     */         }
/* 647 */         return this.window;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void updateWindow(int delta) {
/* 652 */       int newValue = this.window + delta;
/*     */       
/* 654 */       if (((this.window ^ newValue) & (delta ^ newValue)) < 0) {
/* 655 */         newValue = (delta < 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
/*     */       }
/* 657 */       this.window = newValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void close() {
/* 665 */       synchronized (this.lock) {
/* 666 */         this.closed = true;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int getWindow() {
/* 672 */       return this.window;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/AbstractHttp1StreamDuplexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */