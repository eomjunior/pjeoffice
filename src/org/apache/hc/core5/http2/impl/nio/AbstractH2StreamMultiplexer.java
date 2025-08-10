/*      */ package org.apache.hc.core5.http2.impl.nio;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.nio.channels.WritableByteChannel;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedDeque;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*      */ import org.apache.hc.core5.http.ConnectionClosedException;
/*      */ import org.apache.hc.core5.http.EndpointDetails;
/*      */ import org.apache.hc.core5.http.Header;
/*      */ import org.apache.hc.core5.http.HttpConnection;
/*      */ import org.apache.hc.core5.http.HttpConnectionMetrics;
/*      */ import org.apache.hc.core5.http.HttpException;
/*      */ import org.apache.hc.core5.http.HttpStreamResetException;
/*      */ import org.apache.hc.core5.http.HttpVersion;
/*      */ import org.apache.hc.core5.http.ProtocolException;
/*      */ import org.apache.hc.core5.http.ProtocolVersion;
/*      */ import org.apache.hc.core5.http.RequestNotExecutedException;
/*      */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*      */ import org.apache.hc.core5.http.impl.BasicEndpointDetails;
/*      */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*      */ import org.apache.hc.core5.http.impl.CharCodingSupport;
/*      */ import org.apache.hc.core5.http.io.HttpTransportMetrics;
/*      */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*      */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*      */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*      */ import org.apache.hc.core5.http.nio.command.ExecutableCommand;
/*      */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*      */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*      */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*      */ import org.apache.hc.core5.http2.H2ConnectionException;
/*      */ import org.apache.hc.core5.http2.H2Error;
/*      */ import org.apache.hc.core5.http2.H2StreamResetException;
/*      */ import org.apache.hc.core5.http2.config.H2Config;
/*      */ import org.apache.hc.core5.http2.config.H2Param;
/*      */ import org.apache.hc.core5.http2.config.H2Setting;
/*      */ import org.apache.hc.core5.http2.frame.FrameFactory;
/*      */ import org.apache.hc.core5.http2.frame.FrameFlag;
/*      */ import org.apache.hc.core5.http2.frame.FrameType;
/*      */ import org.apache.hc.core5.http2.frame.RawFrame;
/*      */ import org.apache.hc.core5.http2.frame.StreamIdGenerator;
/*      */ import org.apache.hc.core5.http2.hpack.HPackDecoder;
/*      */ import org.apache.hc.core5.http2.hpack.HPackEncoder;
/*      */ import org.apache.hc.core5.http2.impl.BasicH2TransportMetrics;
/*      */ import org.apache.hc.core5.http2.nio.AsyncPingHandler;
/*      */ import org.apache.hc.core5.http2.nio.command.PingCommand;
/*      */ import org.apache.hc.core5.io.CloseMode;
/*      */ import org.apache.hc.core5.reactor.Command;
/*      */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*      */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*      */ import org.apache.hc.core5.util.Args;
/*      */ import org.apache.hc.core5.util.ByteArrayBuffer;
/*      */ import org.apache.hc.core5.util.Identifiable;
/*      */ import org.apache.hc.core5.util.Timeout;
/*      */ 
/*      */ 
/*      */ abstract class AbstractH2StreamMultiplexer
/*      */   implements Identifiable, HttpConnection
/*      */ {
/*      */   private static final long LINGER_TIME = 1000L;
/*      */   private static final long CONNECTION_WINDOW_LOW_MARK = 10485760L;
/*      */   private final ProtocolIOSession ioSession;
/*      */   private final FrameFactory frameFactory;
/*      */   private final StreamIdGenerator idGenerator;
/*      */   private final HttpProcessor httpProcessor;
/*      */   private final H2Config localConfig;
/*      */   private final BasicH2TransportMetrics inputMetrics;
/*      */   private final BasicH2TransportMetrics outputMetrics;
/*      */   private final BasicHttpConnectionMetrics connMetrics;
/*      */   private final FrameInputBuffer inputBuffer;
/*      */   private final FrameOutputBuffer outputBuffer;
/*      */   private final Deque<RawFrame> outputQueue;
/*      */   private final HPackEncoder hPackEncoder;
/*      */   private final HPackDecoder hPackDecoder;
/*      */   private final Map<Integer, H2Stream> streamMap;
/*      */   private final Queue<AsyncPingHandler> pingHandlers;
/*      */   private final AtomicInteger connInputWindow;
/*      */   private final AtomicInteger connOutputWindow;
/*      */   private final AtomicInteger outputRequests;
/*      */   private final AtomicInteger lastStreamId;
/*      */   private final H2StreamListener streamListener;
/*      */   
/*      */   enum ConnectionHandshake
/*      */   {
/*   98 */     READY, ACTIVE, GRACEFUL_SHUTDOWN, SHUTDOWN; }
/*   99 */   enum SettingsHandshake { READY, TRANSMITTED, ACKED; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  122 */   private ConnectionHandshake connState = ConnectionHandshake.READY;
/*  123 */   private SettingsHandshake localSettingState = SettingsHandshake.READY;
/*  124 */   private SettingsHandshake remoteSettingState = SettingsHandshake.READY;
/*      */ 
/*      */   
/*      */   private int initInputWinSize;
/*      */ 
/*      */   
/*      */   private int initOutputWinSize;
/*      */   
/*      */   private int lowMark;
/*      */   
/*      */   private volatile H2Config remoteConfig;
/*      */   
/*      */   private Continuation continuation;
/*      */   
/*      */   private int processedRemoteStreamId;
/*      */   
/*      */   private EndpointDetails endpointDetails;
/*      */   
/*      */   private boolean goAwayReceived;
/*      */ 
/*      */   
/*      */   AbstractH2StreamMultiplexer(ProtocolIOSession ioSession, FrameFactory frameFactory, StreamIdGenerator idGenerator, HttpProcessor httpProcessor, CharCodingConfig charCodingConfig, H2Config h2Config, H2StreamListener streamListener) {
/*  146 */     this.ioSession = (ProtocolIOSession)Args.notNull(ioSession, "IO session");
/*  147 */     this.frameFactory = (FrameFactory)Args.notNull(frameFactory, "Frame factory");
/*  148 */     this.idGenerator = (StreamIdGenerator)Args.notNull(idGenerator, "Stream id generator");
/*  149 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/*  150 */     this.localConfig = (h2Config != null) ? h2Config : H2Config.DEFAULT;
/*  151 */     this.inputMetrics = new BasicH2TransportMetrics();
/*  152 */     this.outputMetrics = new BasicH2TransportMetrics();
/*  153 */     this.connMetrics = new BasicHttpConnectionMetrics((HttpTransportMetrics)this.inputMetrics, (HttpTransportMetrics)this.outputMetrics);
/*  154 */     this.inputBuffer = new FrameInputBuffer(this.inputMetrics, this.localConfig.getMaxFrameSize());
/*  155 */     this.outputBuffer = new FrameOutputBuffer(this.outputMetrics, this.localConfig.getMaxFrameSize());
/*  156 */     this.outputQueue = new ConcurrentLinkedDeque<>();
/*  157 */     this.pingHandlers = new ConcurrentLinkedQueue<>();
/*  158 */     this.outputRequests = new AtomicInteger(0);
/*  159 */     this.lastStreamId = new AtomicInteger(0);
/*  160 */     this.hPackEncoder = new HPackEncoder(CharCodingSupport.createEncoder(charCodingConfig));
/*  161 */     this.hPackDecoder = new HPackDecoder(CharCodingSupport.createDecoder(charCodingConfig));
/*  162 */     this.streamMap = new ConcurrentHashMap<>();
/*  163 */     this.remoteConfig = H2Config.INIT;
/*  164 */     this.connInputWindow = new AtomicInteger(H2Config.INIT.getInitialWindowSize());
/*  165 */     this.connOutputWindow = new AtomicInteger(H2Config.INIT.getInitialWindowSize());
/*      */     
/*  167 */     this.initInputWinSize = H2Config.INIT.getInitialWindowSize();
/*  168 */     this.initOutputWinSize = H2Config.INIT.getInitialWindowSize();
/*      */     
/*  170 */     this.hPackDecoder.setMaxTableSize(H2Config.INIT.getHeaderTableSize());
/*  171 */     this.hPackEncoder.setMaxTableSize(H2Config.INIT.getHeaderTableSize());
/*  172 */     this.hPackDecoder.setMaxListSize(H2Config.INIT.getMaxHeaderListSize());
/*      */     
/*  174 */     this.lowMark = H2Config.INIT.getInitialWindowSize() / 2;
/*  175 */     this.streamListener = streamListener;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getId() {
/*  180 */     return this.ioSession.getId();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int updateWindow(AtomicInteger window, int delta) throws ArithmeticException {
/*      */     while (true) {
/*  203 */       int current = window.get();
/*  204 */       long newValue = current + delta;
/*      */ 
/*      */ 
/*      */       
/*  208 */       if (newValue == 2147483648L) {
/*  209 */         newValue = 2147483647L;
/*      */       }
/*      */ 
/*      */       
/*  213 */       if (Math.abs(newValue) > 2147483647L) {
/*  214 */         throw new ArithmeticException("Update causes flow control window to exceed 2147483647");
/*      */       }
/*  216 */       if (window.compareAndSet(current, (int)newValue)) {
/*  217 */         return (int)newValue;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int updateInputWindow(int streamId, AtomicInteger window, int delta) throws ArithmeticException {
/*  224 */     int newSize = updateWindow(window, delta);
/*  225 */     if (this.streamListener != null) {
/*  226 */       this.streamListener.onInputFlowControl(this, streamId, delta, newSize);
/*      */     }
/*  228 */     return newSize;
/*      */   }
/*      */ 
/*      */   
/*      */   private int updateOutputWindow(int streamId, AtomicInteger window, int delta) throws ArithmeticException {
/*  233 */     int newSize = updateWindow(window, delta);
/*  234 */     if (this.streamListener != null) {
/*  235 */       this.streamListener.onOutputFlowControl(this, streamId, delta, newSize);
/*      */     }
/*  237 */     return newSize;
/*      */   }
/*      */   
/*      */   private void commitFrameInternal(RawFrame frame) throws IOException {
/*  241 */     if (this.outputBuffer.isEmpty() && this.outputQueue.isEmpty()) {
/*  242 */       if (this.streamListener != null) {
/*  243 */         this.streamListener.onFrameOutput(this, frame.getStreamId(), frame);
/*      */       }
/*  245 */       this.outputBuffer.write(frame, (WritableByteChannel)this.ioSession);
/*      */     } else {
/*  247 */       this.outputQueue.addLast(frame);
/*      */     } 
/*  249 */     this.ioSession.setEvent(4);
/*      */   }
/*      */   
/*      */   private void commitFrame(RawFrame frame) throws IOException {
/*  253 */     Args.notNull(frame, "Frame");
/*  254 */     this.ioSession.getLock().lock();
/*      */     try {
/*  256 */       commitFrameInternal(frame);
/*      */     } finally {
/*  258 */       this.ioSession.getLock().unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void commitHeaders(int streamId, List<? extends Header> headers, boolean endStream) throws IOException {
/*  264 */     if (this.streamListener != null) {
/*  265 */       this.streamListener.onHeaderOutput(this, streamId, headers);
/*      */     }
/*  267 */     ByteArrayBuffer buf = new ByteArrayBuffer(512);
/*  268 */     this.hPackEncoder.encodeHeaders(buf, headers, this.localConfig.isCompressionEnabled());
/*      */     
/*  270 */     int off = 0;
/*  271 */     int remaining = buf.length();
/*  272 */     boolean continuation = false;
/*      */     
/*  274 */     while (remaining > 0) {
/*  275 */       RawFrame frame; int chunk = Math.min(this.remoteConfig.getMaxFrameSize(), remaining);
/*  276 */       ByteBuffer payload = ByteBuffer.wrap(buf.array(), off, chunk);
/*      */       
/*  278 */       remaining -= chunk;
/*  279 */       off += chunk;
/*      */       
/*  281 */       boolean endHeaders = (remaining == 0);
/*      */       
/*  283 */       if (!continuation) {
/*  284 */         frame = this.frameFactory.createHeaders(streamId, payload, endHeaders, endStream);
/*  285 */         continuation = true;
/*      */       } else {
/*  287 */         frame = this.frameFactory.createContinuation(streamId, payload, endHeaders);
/*      */       } 
/*  289 */       commitFrameInternal(frame);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void commitPushPromise(int streamId, int promisedStreamId, List<Header> headers) throws IOException {
/*  295 */     if (headers == null || headers.isEmpty()) {
/*  296 */       throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Message headers are missing");
/*      */     }
/*  298 */     if (this.streamListener != null) {
/*  299 */       this.streamListener.onHeaderOutput(this, streamId, headers);
/*      */     }
/*  301 */     ByteArrayBuffer buf = new ByteArrayBuffer(512);
/*  302 */     buf.append((byte)(promisedStreamId >> 24));
/*  303 */     buf.append((byte)(promisedStreamId >> 16));
/*  304 */     buf.append((byte)(promisedStreamId >> 8));
/*  305 */     buf.append((byte)promisedStreamId);
/*      */     
/*  307 */     this.hPackEncoder.encodeHeaders(buf, headers, this.localConfig.isCompressionEnabled());
/*      */     
/*  309 */     int off = 0;
/*  310 */     int remaining = buf.length();
/*  311 */     boolean continuation = false;
/*      */     
/*  313 */     while (remaining > 0) {
/*  314 */       RawFrame frame; int chunk = Math.min(this.remoteConfig.getMaxFrameSize(), remaining);
/*  315 */       ByteBuffer payload = ByteBuffer.wrap(buf.array(), off, chunk);
/*      */       
/*  317 */       remaining -= chunk;
/*  318 */       off += chunk;
/*      */       
/*  320 */       boolean endHeaders = (remaining == 0);
/*      */       
/*  322 */       if (!continuation) {
/*  323 */         frame = this.frameFactory.createPushPromise(streamId, payload, endHeaders);
/*  324 */         continuation = true;
/*      */       } else {
/*  326 */         frame = this.frameFactory.createContinuation(streamId, payload, endHeaders);
/*      */       } 
/*  328 */       commitFrameInternal(frame);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void streamDataFrame(int streamId, AtomicInteger streamOutputWindow, ByteBuffer payload, int chunk) throws IOException {
/*  337 */     RawFrame dataFrame = this.frameFactory.createData(streamId, payload, false);
/*  338 */     if (this.streamListener != null) {
/*  339 */       this.streamListener.onFrameOutput(this, streamId, dataFrame);
/*      */     }
/*  341 */     updateOutputWindow(0, this.connOutputWindow, -chunk);
/*  342 */     updateOutputWindow(streamId, streamOutputWindow, -chunk);
/*  343 */     this.outputBuffer.write(dataFrame, (WritableByteChannel)this.ioSession);
/*      */   }
/*      */ 
/*      */   
/*      */   private int streamData(int streamId, AtomicInteger streamOutputWindow, ByteBuffer payload) throws IOException {
/*  348 */     if (this.outputBuffer.isEmpty() && this.outputQueue.isEmpty()) {
/*  349 */       int chunk, capacity = Math.min(this.connOutputWindow.get(), streamOutputWindow.get());
/*  350 */       if (capacity <= 0) {
/*  351 */         return 0;
/*      */       }
/*  353 */       int maxPayloadSize = Math.min(capacity, this.remoteConfig.getMaxFrameSize());
/*      */       
/*  355 */       if (payload.remaining() <= maxPayloadSize) {
/*  356 */         chunk = payload.remaining();
/*  357 */         streamDataFrame(streamId, streamOutputWindow, payload, chunk);
/*      */       } else {
/*  359 */         chunk = maxPayloadSize;
/*  360 */         int originalLimit = payload.limit();
/*      */         try {
/*  362 */           payload.limit(payload.position() + chunk);
/*  363 */           streamDataFrame(streamId, streamOutputWindow, payload, chunk);
/*      */         } finally {
/*  365 */           payload.limit(originalLimit);
/*      */         } 
/*      */       } 
/*  368 */       payload.position(payload.position() + chunk);
/*  369 */       this.ioSession.setEvent(4);
/*  370 */       return chunk;
/*      */     } 
/*  372 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private void incrementInputCapacity(int streamId, AtomicInteger inputWindow, int inputCapacity) throws IOException {
/*  377 */     if (inputCapacity > 0) {
/*  378 */       int streamWinSize = inputWindow.get();
/*  379 */       int remainingCapacity = Integer.MAX_VALUE - streamWinSize;
/*  380 */       int chunk = Math.min(inputCapacity, remainingCapacity);
/*  381 */       if (chunk != 0) {
/*  382 */         RawFrame windowUpdateFrame = this.frameFactory.createWindowUpdate(streamId, chunk);
/*  383 */         commitFrame(windowUpdateFrame);
/*  384 */         updateInputWindow(streamId, inputWindow, chunk);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void requestSessionOutput() {
/*  390 */     this.outputRequests.incrementAndGet();
/*  391 */     this.ioSession.setEvent(4);
/*      */   }
/*      */   
/*      */   private void updateLastStreamId(int streamId) {
/*  395 */     int currentId = this.lastStreamId.get();
/*  396 */     if (streamId > currentId) {
/*  397 */       this.lastStreamId.compareAndSet(currentId, streamId);
/*      */     }
/*      */   }
/*      */   
/*      */   private int generateStreamId() {
/*      */     while (true) {
/*  403 */       int currentId = this.lastStreamId.get();
/*  404 */       int newStreamId = this.idGenerator.generate(currentId);
/*  405 */       if (this.lastStreamId.compareAndSet(currentId, newStreamId)) {
/*  406 */         return newStreamId;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void onConnect() throws HttpException, IOException {
/*  412 */     this.connState = ConnectionHandshake.ACTIVE;
/*  413 */     RawFrame settingsFrame = this.frameFactory.createSettings(new H2Setting[] { new H2Setting(H2Param.HEADER_TABLE_SIZE, this.localConfig
/*  414 */             .getHeaderTableSize()), new H2Setting(H2Param.ENABLE_PUSH, 
/*  415 */             this.localConfig.isPushEnabled() ? 1 : 0), new H2Setting(H2Param.MAX_CONCURRENT_STREAMS, this.localConfig
/*  416 */             .getMaxConcurrentStreams()), new H2Setting(H2Param.INITIAL_WINDOW_SIZE, this.localConfig
/*  417 */             .getInitialWindowSize()), new H2Setting(H2Param.MAX_FRAME_SIZE, this.localConfig
/*  418 */             .getMaxFrameSize()), new H2Setting(H2Param.MAX_HEADER_LIST_SIZE, this.localConfig
/*  419 */             .getMaxHeaderListSize()) });
/*      */     
/*  421 */     commitFrame(settingsFrame);
/*  422 */     this.localSettingState = SettingsHandshake.TRANSMITTED;
/*  423 */     maximizeConnWindow(this.connInputWindow.get());
/*      */     
/*  425 */     if (this.streamListener != null) {
/*  426 */       int initInputWindow = this.connInputWindow.get();
/*  427 */       this.streamListener.onInputFlowControl(this, 0, initInputWindow, initInputWindow);
/*  428 */       int initOutputWindow = this.connOutputWindow.get();
/*  429 */       this.streamListener.onOutputFlowControl(this, 0, initOutputWindow, initOutputWindow);
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void onInput(ByteBuffer src) throws HttpException, IOException {
/*  434 */     if (this.connState == ConnectionHandshake.SHUTDOWN) {
/*  435 */       this.ioSession.clearEvent(1);
/*      */     } else {
/*      */       while (true) {
/*  438 */         RawFrame frame = this.inputBuffer.read(src, (ReadableByteChannel)this.ioSession);
/*  439 */         if (frame == null) {
/*      */           break;
/*      */         }
/*  442 */         if (this.streamListener != null) {
/*  443 */           this.streamListener.onFrameInput(this, frame.getStreamId(), frame);
/*      */         }
/*  445 */         consumeFrame(frame);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void onOutput() throws HttpException, IOException {
/*  451 */     this.ioSession.getLock().lock();
/*      */     try {
/*  453 */       if (!this.outputBuffer.isEmpty()) {
/*  454 */         this.outputBuffer.flush((WritableByteChannel)this.ioSession);
/*      */       }
/*  456 */       while (this.outputBuffer.isEmpty()) {
/*  457 */         RawFrame frame = this.outputQueue.poll();
/*  458 */         if (frame != null) {
/*  459 */           if (this.streamListener != null) {
/*  460 */             this.streamListener.onFrameOutput(this, frame.getStreamId(), frame);
/*      */           }
/*  462 */           this.outputBuffer.write(frame, (WritableByteChannel)this.ioSession);
/*      */         }
/*      */       
/*      */       } 
/*      */     } finally {
/*      */       
/*  468 */       this.ioSession.getLock().unlock();
/*      */     } 
/*      */     
/*  471 */     if (this.connState.compareTo(ConnectionHandshake.SHUTDOWN) < 0) {
/*      */       
/*  473 */       if (this.connOutputWindow.get() > 0 && this.remoteSettingState == SettingsHandshake.ACKED) {
/*  474 */         produceOutput();
/*      */       }
/*  476 */       int pendingOutputRequests = this.outputRequests.get();
/*  477 */       boolean outputPending = false;
/*  478 */       if (!this.streamMap.isEmpty() && this.connOutputWindow.get() > 0) {
/*  479 */         for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/*  480 */           Map.Entry<Integer, H2Stream> entry = it.next();
/*  481 */           H2Stream stream = entry.getValue();
/*  482 */           if (!stream.isLocalClosed() && stream
/*  483 */             .getOutputWindow().get() > 0 && stream
/*  484 */             .isOutputReady()) {
/*  485 */             outputPending = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*  490 */       this.ioSession.getLock().lock();
/*      */       try {
/*  492 */         if (!outputPending && this.outputBuffer.isEmpty() && this.outputQueue.isEmpty() && this.outputRequests
/*  493 */           .compareAndSet(pendingOutputRequests, 0)) {
/*  494 */           this.ioSession.clearEvent(4);
/*      */         } else {
/*  496 */           this.outputRequests.addAndGet(-pendingOutputRequests);
/*      */         } 
/*      */       } finally {
/*  499 */         this.ioSession.getLock().unlock();
/*      */       } 
/*      */     } 
/*      */     
/*  503 */     if (this.connState.compareTo(ConnectionHandshake.ACTIVE) <= 0 && this.remoteSettingState == SettingsHandshake.ACKED) {
/*  504 */       processPendingCommands();
/*      */     }
/*  506 */     if (this.connState.compareTo(ConnectionHandshake.GRACEFUL_SHUTDOWN) == 0) {
/*  507 */       int liveStreams = 0;
/*  508 */       for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/*  509 */         Map.Entry<Integer, H2Stream> entry = it.next();
/*  510 */         H2Stream stream = entry.getValue();
/*  511 */         if (stream.isLocalClosed() && stream.isRemoteClosed()) {
/*  512 */           stream.releaseResources();
/*  513 */           it.remove(); continue;
/*      */         } 
/*  515 */         if (this.idGenerator.isSameSide(stream.getId()) || stream.getId() <= this.processedRemoteStreamId) {
/*  516 */           liveStreams++;
/*      */         }
/*      */       } 
/*      */       
/*  520 */       if (liveStreams == 0) {
/*  521 */         this.connState = ConnectionHandshake.SHUTDOWN;
/*      */       }
/*      */     } 
/*  524 */     if (this.connState.compareTo(ConnectionHandshake.SHUTDOWN) >= 0) {
/*  525 */       if (!this.streamMap.isEmpty()) {
/*  526 */         for (H2Stream stream : this.streamMap.values()) {
/*  527 */           stream.releaseResources();
/*      */         }
/*  529 */         this.streamMap.clear();
/*      */       } 
/*  531 */       this.ioSession.getLock().lock();
/*      */       try {
/*  533 */         if (this.outputBuffer.isEmpty() && this.outputQueue.isEmpty()) {
/*  534 */           this.ioSession.close();
/*      */         }
/*      */       } finally {
/*  537 */         this.ioSession.getLock().unlock();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   public final void onTimeout(Timeout timeout) throws HttpException, IOException {
/*      */     RawFrame goAway;
/*  543 */     this.connState = ConnectionHandshake.SHUTDOWN;
/*      */ 
/*      */     
/*  546 */     if (this.localSettingState != SettingsHandshake.ACKED) {
/*  547 */       goAway = this.frameFactory.createGoAway(this.processedRemoteStreamId, H2Error.SETTINGS_TIMEOUT, "Setting timeout (" + timeout + ")");
/*      */     } else {
/*      */       
/*  550 */       goAway = this.frameFactory.createGoAway(this.processedRemoteStreamId, H2Error.NO_ERROR, "Timeout due to inactivity (" + timeout + ")");
/*      */     } 
/*      */     
/*  553 */     commitFrame(goAway);
/*  554 */     for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/*  555 */       Map.Entry<Integer, H2Stream> entry = it.next();
/*  556 */       H2Stream stream = entry.getValue();
/*  557 */       stream.reset((Exception)new H2StreamResetException(H2Error.NO_ERROR, "Timeout due to inactivity (" + timeout + ")"));
/*      */     } 
/*  559 */     this.streamMap.clear();
/*      */   }
/*      */   
/*      */   public final void onDisconnect() {
/*      */     while (true) {
/*  564 */       AsyncPingHandler pingHandler = this.pingHandlers.poll();
/*  565 */       if (pingHandler != null) {
/*  566 */         pingHandler.cancel();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  571 */     for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/*  572 */       Map.Entry<Integer, H2Stream> entry = it.next();
/*  573 */       H2Stream stream = entry.getValue();
/*  574 */       stream.cancel();
/*      */     } 
/*      */     while (true) {
/*  577 */       Command command = this.ioSession.poll();
/*  578 */       if (command != null) {
/*  579 */         if (command instanceof ExecutableCommand) {
/*  580 */           ((ExecutableCommand)command).failed((Exception)new ConnectionClosedException()); continue;
/*      */         } 
/*  582 */         command.cancel();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void processPendingCommands() throws IOException, HttpException {
/*  591 */     while (this.streamMap.size() < this.remoteConfig.getMaxConcurrentStreams()) {
/*  592 */       Command command = this.ioSession.poll();
/*  593 */       if (command == null) {
/*      */         break;
/*      */       }
/*  596 */       if (command instanceof ShutdownCommand) {
/*  597 */         ShutdownCommand shutdownCommand = (ShutdownCommand)command;
/*  598 */         if (shutdownCommand.getType() == CloseMode.IMMEDIATE) {
/*  599 */           for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/*  600 */             Map.Entry<Integer, H2Stream> entry = it.next();
/*  601 */             H2Stream stream = entry.getValue();
/*  602 */             stream.cancel();
/*      */           } 
/*  604 */           this.streamMap.clear();
/*  605 */           this.connState = ConnectionHandshake.SHUTDOWN; break;
/*      */         } 
/*  607 */         if (this.connState.compareTo(ConnectionHandshake.ACTIVE) <= 0) {
/*  608 */           RawFrame goAway = this.frameFactory.createGoAway(this.processedRemoteStreamId, H2Error.NO_ERROR, "Graceful shutdown");
/*  609 */           commitFrame(goAway);
/*  610 */           this.connState = this.streamMap.isEmpty() ? ConnectionHandshake.SHUTDOWN : ConnectionHandshake.GRACEFUL_SHUTDOWN;
/*      */         } 
/*      */         break;
/*      */       } 
/*  614 */       if (command instanceof PingCommand) {
/*  615 */         PingCommand pingCommand = (PingCommand)command;
/*  616 */         AsyncPingHandler handler = pingCommand.getHandler();
/*  617 */         this.pingHandlers.add(handler);
/*  618 */         RawFrame ping = this.frameFactory.createPing(handler.getData());
/*  619 */         commitFrame(ping); continue;
/*  620 */       }  if (command instanceof ExecutableCommand) {
/*  621 */         int streamId = generateStreamId();
/*  622 */         H2StreamChannelImpl channel = new H2StreamChannelImpl(streamId, true, this.initInputWinSize, this.initOutputWinSize);
/*      */         
/*  624 */         ExecutableCommand executableCommand = (ExecutableCommand)command;
/*  625 */         H2StreamHandler streamHandler = createLocallyInitiatedStream(executableCommand, channel, this.httpProcessor, this.connMetrics);
/*      */ 
/*      */         
/*  628 */         H2Stream stream = new H2Stream(channel, streamHandler, false);
/*  629 */         this.streamMap.put(Integer.valueOf(streamId), stream);
/*      */         
/*  631 */         if (this.streamListener != null) {
/*  632 */           int initInputWindow = stream.getInputWindow().get();
/*  633 */           this.streamListener.onInputFlowControl(this, streamId, initInputWindow, initInputWindow);
/*  634 */           int initOutputWindow = stream.getOutputWindow().get();
/*  635 */           this.streamListener.onOutputFlowControl(this, streamId, initOutputWindow, initOutputWindow);
/*      */         } 
/*      */         
/*  638 */         if (stream.isOutputReady()) {
/*  639 */           stream.produceOutput();
/*      */         }
/*  641 */         CancellableDependency cancellableDependency = executableCommand.getCancellableDependency();
/*  642 */         if (cancellableDependency != null) {
/*  643 */           cancellableDependency.setDependency(stream::abort);
/*      */         }
/*  645 */         if (!this.outputQueue.isEmpty()) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public final void onException(Exception cause) {
/*      */     
/*      */     try { while (true) {
/*  655 */         AsyncPingHandler pingHandler = this.pingHandlers.poll();
/*  656 */         if (pingHandler != null) {
/*  657 */           pingHandler.failed(cause);
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */       while (true) {
/*  663 */         Command command = this.ioSession.poll();
/*  664 */         if (command != null) {
/*  665 */           if (command instanceof ExecutableCommand) {
/*  666 */             ((ExecutableCommand)command).failed((Exception)new ConnectionClosedException()); continue;
/*      */           } 
/*  668 */           command.cancel();
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*  674 */       for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/*  675 */         Map.Entry<Integer, H2Stream> entry = it.next();
/*  676 */         H2Stream stream = entry.getValue();
/*  677 */         stream.reset(cause);
/*      */       } 
/*  679 */       this.streamMap.clear();
/*  680 */       if (!(cause instanceof ConnectionClosedException) && 
/*  681 */         this.connState.compareTo(ConnectionHandshake.GRACEFUL_SHUTDOWN) <= 0) {
/*      */         H2Error errorCode;
/*  683 */         if (cause instanceof H2ConnectionException) {
/*  684 */           errorCode = H2Error.getByCode(((H2ConnectionException)cause).getCode());
/*  685 */         } else if (cause instanceof ProtocolException) {
/*  686 */           errorCode = H2Error.PROTOCOL_ERROR;
/*      */         } else {
/*  688 */           errorCode = H2Error.INTERNAL_ERROR;
/*      */         } 
/*  690 */         RawFrame goAway = this.frameFactory.createGoAway(this.processedRemoteStreamId, errorCode, cause.getMessage());
/*  691 */         commitFrame(goAway);
/*      */       }
/*      */        }
/*  694 */     catch (IOException iOException) {  }
/*      */     finally
/*  696 */     { CloseMode closeMode; this.connState = ConnectionHandshake.SHUTDOWN;
/*      */       
/*  698 */       if (cause instanceof ConnectionClosedException) {
/*  699 */         closeMode = CloseMode.GRACEFUL;
/*  700 */       } else if (cause instanceof IOException) {
/*  701 */         closeMode = CloseMode.IMMEDIATE;
/*      */       } else {
/*  703 */         closeMode = CloseMode.GRACEFUL;
/*      */       } 
/*  705 */       this.ioSession.close(closeMode); }
/*      */   
/*      */   }
/*      */   
/*      */   private H2Stream getValidStream(int streamId) throws H2ConnectionException {
/*  710 */     if (streamId == 0) {
/*  711 */       throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal stream id: " + streamId);
/*      */     }
/*  713 */     H2Stream stream = this.streamMap.get(Integer.valueOf(streamId));
/*  714 */     if (stream == null) {
/*  715 */       if (streamId <= this.lastStreamId.get()) {
/*  716 */         throw new H2ConnectionException(H2Error.STREAM_CLOSED, "Stream closed");
/*      */       }
/*  718 */       throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Unexpected stream id: " + streamId);
/*      */     } 
/*      */     
/*  721 */     return stream; } private void consumeFrame(RawFrame frame) throws HttpException, IOException { H2Stream h2Stream2; ByteBuffer byteBuffer2; H2Stream h2Stream1; ByteBuffer ping, byteBuffer1; H2Stream stream; ByteBuffer payload; int delta; ByteBuffer byteBuffer4, pong; RawFrame response; ByteBuffer byteBuffer3; int processedLocalStreamId, i; RawFrame rawFrame1; int promisedStreamId, errorCode;
/*      */     H2StreamChannelImpl channel;
/*      */     H2StreamHandler streamHandler;
/*      */     H2Stream promisedStream;
/*  725 */     FrameType frameType = FrameType.valueOf(frame.getType());
/*  726 */     int streamId = frame.getStreamId();
/*  727 */     if (this.continuation != null && frameType != FrameType.CONTINUATION) {
/*  728 */       throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "CONTINUATION frame expected");
/*      */     }
/*  730 */     switch (frameType) {
/*      */       case HEADER_TABLE_SIZE:
/*  732 */         h2Stream2 = getValidStream(streamId);
/*      */         try {
/*  734 */           consumeDataFrame(frame, h2Stream2);
/*  735 */         } catch (H2StreamResetException ex) {
/*  736 */           h2Stream2.localReset(ex);
/*  737 */         } catch (HttpStreamResetException ex) {
/*  738 */           h2Stream2.localReset((Exception)ex, (ex.getCause() != null) ? H2Error.INTERNAL_ERROR : H2Error.CANCEL);
/*      */         } 
/*      */         
/*  741 */         if (h2Stream2.isTerminated()) {
/*  742 */           this.streamMap.remove(Integer.valueOf(streamId));
/*  743 */           h2Stream2.releaseResources();
/*  744 */           requestSessionOutput();
/*      */         } 
/*      */         break;
/*      */       
/*      */       case MAX_CONCURRENT_STREAMS:
/*  749 */         if (streamId == 0) {
/*  750 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal stream id: " + streamId);
/*      */         }
/*  752 */         h2Stream2 = this.streamMap.get(Integer.valueOf(streamId));
/*  753 */         if (h2Stream2 == null) {
/*  754 */           H2StreamHandler h2StreamHandler; acceptHeaderFrame();
/*      */           
/*  756 */           if (this.idGenerator.isSameSide(streamId)) {
/*  757 */             throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal stream id: " + streamId);
/*      */           }
/*  759 */           if (this.goAwayReceived) {
/*  760 */             throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "GOAWAY received");
/*      */           }
/*      */           
/*  763 */           updateLastStreamId(streamId);
/*      */           
/*  765 */           H2StreamChannelImpl h2StreamChannelImpl = new H2StreamChannelImpl(streamId, false, this.initInputWinSize, this.initOutputWinSize);
/*      */ 
/*      */           
/*  768 */           if (this.connState.compareTo(ConnectionHandshake.ACTIVE) <= 0) {
/*  769 */             h2StreamHandler = createRemotelyInitiatedStream(h2StreamChannelImpl, this.httpProcessor, this.connMetrics, null);
/*      */           } else {
/*  771 */             h2StreamHandler = NoopH2StreamHandler.INSTANCE;
/*  772 */             h2StreamChannelImpl.setLocalEndStream();
/*      */           } 
/*      */           
/*  775 */           h2Stream2 = new H2Stream(h2StreamChannelImpl, h2StreamHandler, true);
/*  776 */           if (h2Stream2.isOutputReady()) {
/*  777 */             h2Stream2.produceOutput();
/*      */           }
/*  779 */           this.streamMap.put(Integer.valueOf(streamId), h2Stream2);
/*      */         } 
/*      */         
/*      */         try {
/*  783 */           consumeHeaderFrame(frame, h2Stream2);
/*      */           
/*  785 */           if (h2Stream2.isOutputReady()) {
/*  786 */             h2Stream2.produceOutput();
/*      */           }
/*  788 */         } catch (H2StreamResetException ex) {
/*  789 */           h2Stream2.localReset(ex);
/*  790 */         } catch (HttpStreamResetException ex) {
/*  791 */           h2Stream2.localReset((Exception)ex, (ex.getCause() != null) ? H2Error.INTERNAL_ERROR : H2Error.CANCEL);
/*  792 */         } catch (HttpException ex) {
/*  793 */           h2Stream2.handle(ex);
/*      */         } 
/*      */         
/*  796 */         if (h2Stream2.isTerminated()) {
/*  797 */           this.streamMap.remove(Integer.valueOf(streamId));
/*  798 */           h2Stream2.releaseResources();
/*  799 */           requestSessionOutput();
/*      */         } 
/*      */         break;
/*      */       
/*      */       case ENABLE_PUSH:
/*  804 */         if (this.continuation == null) {
/*  805 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Unexpected CONTINUATION frame");
/*      */         }
/*  807 */         if (streamId != this.continuation.streamId) {
/*  808 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Unexpected CONTINUATION stream id: " + streamId);
/*      */         }
/*      */         
/*  811 */         h2Stream2 = getValidStream(streamId);
/*      */         
/*      */         try {
/*  814 */           consumeContinuationFrame(frame, h2Stream2);
/*  815 */         } catch (H2StreamResetException ex) {
/*  816 */           h2Stream2.localReset(ex);
/*  817 */         } catch (HttpStreamResetException ex) {
/*  818 */           h2Stream2.localReset((Exception)ex, (ex.getCause() != null) ? H2Error.INTERNAL_ERROR : H2Error.CANCEL);
/*      */         } 
/*      */         
/*  821 */         if (h2Stream2.isTerminated()) {
/*  822 */           this.streamMap.remove(Integer.valueOf(streamId));
/*  823 */           h2Stream2.releaseResources();
/*  824 */           requestSessionOutput();
/*      */         } 
/*      */         break;
/*      */       
/*      */       case INITIAL_WINDOW_SIZE:
/*  829 */         byteBuffer2 = frame.getPayload();
/*  830 */         if (byteBuffer2 == null || byteBuffer2.remaining() != 4) {
/*  831 */           throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Invalid WINDOW_UPDATE frame payload");
/*      */         }
/*  833 */         delta = byteBuffer2.getInt();
/*  834 */         if (delta <= 0) {
/*  835 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Invalid WINDOW_UPDATE delta");
/*      */         }
/*  837 */         if (streamId == 0) {
/*      */           try {
/*  839 */             updateOutputWindow(0, this.connOutputWindow, delta);
/*  840 */           } catch (ArithmeticException ex) {
/*  841 */             throw new H2ConnectionException(H2Error.FLOW_CONTROL_ERROR, ex.getMessage());
/*      */           } 
/*      */         } else {
/*  844 */           H2Stream h2Stream = this.streamMap.get(Integer.valueOf(streamId));
/*  845 */           if (h2Stream != null) {
/*      */             try {
/*  847 */               updateOutputWindow(streamId, h2Stream.getOutputWindow(), delta);
/*  848 */             } catch (ArithmeticException ex) {
/*  849 */               throw new H2ConnectionException(H2Error.FLOW_CONTROL_ERROR, ex.getMessage());
/*      */             } 
/*      */           }
/*      */         } 
/*  853 */         this.ioSession.setEvent(4);
/*      */         break;
/*      */       
/*      */       case MAX_FRAME_SIZE:
/*  857 */         if (streamId == 0) {
/*  858 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal stream id: " + streamId);
/*      */         }
/*  860 */         h2Stream1 = this.streamMap.get(Integer.valueOf(streamId));
/*  861 */         if (h2Stream1 == null) {
/*  862 */           if (streamId > this.lastStreamId.get())
/*  863 */             throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Unexpected stream id: " + streamId); 
/*      */           break;
/*      */         } 
/*  866 */         byteBuffer4 = frame.getPayload();
/*  867 */         if (byteBuffer4 == null || byteBuffer4.remaining() != 4) {
/*  868 */           throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Invalid RST_STREAM frame payload");
/*      */         }
/*  870 */         i = byteBuffer4.getInt();
/*  871 */         h2Stream1.reset((Exception)new H2StreamResetException(i, "Stream reset (" + i + ")"));
/*  872 */         this.streamMap.remove(Integer.valueOf(streamId));
/*  873 */         h2Stream1.releaseResources();
/*  874 */         requestSessionOutput();
/*      */         break;
/*      */ 
/*      */       
/*      */       case MAX_HEADER_LIST_SIZE:
/*  879 */         if (streamId != 0) {
/*  880 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal stream id");
/*      */         }
/*  882 */         ping = frame.getPayloadContent();
/*  883 */         if (ping == null || ping.remaining() != 8) {
/*  884 */           throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Invalid PING frame payload");
/*      */         }
/*  886 */         if (frame.isFlagSet(FrameFlag.ACK)) {
/*  887 */           AsyncPingHandler pingHandler = this.pingHandlers.poll();
/*  888 */           if (pingHandler != null)
/*  889 */             pingHandler.consumeResponse(ping); 
/*      */           break;
/*      */         } 
/*  892 */         pong = ByteBuffer.allocate(ping.remaining());
/*  893 */         pong.put(ping);
/*  894 */         pong.flip();
/*  895 */         rawFrame1 = this.frameFactory.createPingAck(pong);
/*  896 */         commitFrame(rawFrame1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case null:
/*  901 */         if (streamId != 0) {
/*  902 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal stream id");
/*      */         }
/*  904 */         if (frame.isFlagSet(FrameFlag.ACK)) {
/*  905 */           if (this.localSettingState == SettingsHandshake.TRANSMITTED) {
/*  906 */             this.localSettingState = SettingsHandshake.ACKED;
/*  907 */             this.ioSession.setEvent(4);
/*  908 */             applyLocalSettings();
/*      */           }  break;
/*      */         } 
/*  911 */         byteBuffer1 = frame.getPayload();
/*  912 */         if (byteBuffer1 != null) {
/*  913 */           if (byteBuffer1.remaining() % 6 != 0) {
/*  914 */             throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Invalid SETTINGS payload");
/*      */           }
/*  916 */           consumeSettingsFrame(byteBuffer1);
/*  917 */           this.remoteSettingState = SettingsHandshake.TRANSMITTED;
/*      */         } 
/*      */         
/*  920 */         response = this.frameFactory.createSettingsAck();
/*  921 */         commitFrame(response);
/*  922 */         this.remoteSettingState = SettingsHandshake.ACKED;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case null:
/*  930 */         acceptPushFrame();
/*      */         
/*  932 */         if (this.goAwayReceived) {
/*  933 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "GOAWAY received");
/*      */         }
/*      */         
/*  936 */         if (!this.localConfig.isPushEnabled()) {
/*  937 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Push is disabled");
/*      */         }
/*      */         
/*  940 */         stream = getValidStream(streamId);
/*  941 */         if (stream.isRemoteClosed()) {
/*  942 */           stream.localReset(new H2StreamResetException(H2Error.STREAM_CLOSED, "Stream closed"));
/*      */           
/*      */           break;
/*      */         } 
/*  946 */         byteBuffer3 = frame.getPayloadContent();
/*  947 */         if (byteBuffer3 == null || byteBuffer3.remaining() < 4) {
/*  948 */           throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Invalid PUSH_PROMISE payload");
/*      */         }
/*  950 */         promisedStreamId = byteBuffer3.getInt();
/*  951 */         if (promisedStreamId == 0 || this.idGenerator.isSameSide(promisedStreamId)) {
/*  952 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal promised stream id: " + promisedStreamId);
/*      */         }
/*  954 */         if (this.streamMap.get(Integer.valueOf(promisedStreamId)) != null) {
/*  955 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Unexpected promised stream id: " + promisedStreamId);
/*      */         }
/*      */         
/*  958 */         updateLastStreamId(promisedStreamId);
/*      */         
/*  960 */         channel = new H2StreamChannelImpl(promisedStreamId, false, this.initInputWinSize, this.initOutputWinSize);
/*      */ 
/*      */         
/*  963 */         if (this.connState.compareTo(ConnectionHandshake.ACTIVE) <= 0) {
/*  964 */           streamHandler = createRemotelyInitiatedStream(channel, this.httpProcessor, this.connMetrics, stream
/*  965 */               .getPushHandlerFactory());
/*      */         } else {
/*  967 */           streamHandler = NoopH2StreamHandler.INSTANCE;
/*  968 */           channel.setLocalEndStream();
/*      */         } 
/*      */         
/*  971 */         promisedStream = new H2Stream(channel, streamHandler, true);
/*  972 */         this.streamMap.put(Integer.valueOf(promisedStreamId), promisedStream);
/*      */         
/*      */         try {
/*  975 */           consumePushPromiseFrame(frame, byteBuffer3, promisedStream);
/*  976 */         } catch (H2StreamResetException ex) {
/*  977 */           promisedStream.localReset(ex);
/*  978 */         } catch (HttpStreamResetException ex) {
/*  979 */           promisedStream.localReset((Exception)ex, (ex.getCause() != null) ? H2Error.INTERNAL_ERROR : H2Error.NO_ERROR);
/*      */         } 
/*      */         break;
/*      */       
/*      */       case null:
/*  984 */         if (streamId != 0) {
/*  985 */           throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Illegal stream id");
/*      */         }
/*  987 */         payload = frame.getPayload();
/*  988 */         if (payload == null || payload.remaining() < 8) {
/*  989 */           throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Invalid GOAWAY payload");
/*      */         }
/*  991 */         processedLocalStreamId = payload.getInt();
/*  992 */         errorCode = payload.getInt();
/*  993 */         this.goAwayReceived = true;
/*  994 */         if (errorCode == H2Error.NO_ERROR.getCode()) {
/*  995 */           if (this.connState.compareTo(ConnectionHandshake.ACTIVE) <= 0) {
/*  996 */             for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/*  997 */               Map.Entry<Integer, H2Stream> entry = it.next();
/*  998 */               int activeStreamId = ((Integer)entry.getKey()).intValue();
/*  999 */               if (!this.idGenerator.isSameSide(activeStreamId) && activeStreamId > processedLocalStreamId) {
/* 1000 */                 H2Stream h2Stream = entry.getValue();
/* 1001 */                 h2Stream.cancel();
/* 1002 */                 it.remove();
/*      */               } 
/*      */             } 
/*      */           }
/* 1006 */           this.connState = this.streamMap.isEmpty() ? ConnectionHandshake.SHUTDOWN : ConnectionHandshake.GRACEFUL_SHUTDOWN;
/*      */         } else {
/* 1008 */           for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/* 1009 */             Map.Entry<Integer, H2Stream> entry = it.next();
/* 1010 */             H2Stream h2Stream = entry.getValue();
/* 1011 */             h2Stream.reset((Exception)new H2StreamResetException(errorCode, "Connection terminated by the peer (" + errorCode + ")"));
/*      */           } 
/* 1013 */           this.streamMap.clear();
/* 1014 */           this.connState = ConnectionHandshake.SHUTDOWN;
/*      */         } 
/*      */         
/* 1017 */         this.ioSession.setEvent(4);
/*      */         break;
/*      */     }  }
/*      */ 
/*      */   
/*      */   private void consumeDataFrame(RawFrame frame, H2Stream stream) throws HttpException, IOException {
/* 1023 */     int streamId = stream.getId();
/* 1024 */     ByteBuffer payload = frame.getPayloadContent();
/* 1025 */     if (payload != null) {
/* 1026 */       int frameLength = frame.getLength();
/* 1027 */       int streamWinSize = updateInputWindow(streamId, stream.getInputWindow(), -frameLength);
/* 1028 */       if (streamWinSize < this.lowMark && !stream.isRemoteClosed()) {
/* 1029 */         stream.produceInputCapacityUpdate();
/*      */       }
/* 1031 */       int connWinSize = updateInputWindow(0, this.connInputWindow, -frameLength);
/* 1032 */       if (connWinSize < 10485760L) {
/* 1033 */         maximizeConnWindow(connWinSize);
/*      */       }
/*      */     } 
/* 1036 */     if (stream.isRemoteClosed()) {
/* 1037 */       throw new H2StreamResetException(H2Error.STREAM_CLOSED, "Stream already closed");
/*      */     }
/* 1039 */     if (frame.isFlagSet(FrameFlag.END_STREAM)) {
/* 1040 */       stream.setRemoteEndStream();
/*      */     }
/* 1042 */     if (stream.isLocalReset()) {
/*      */       return;
/*      */     }
/* 1045 */     stream.consumeData(payload);
/*      */   }
/*      */   
/*      */   private void maximizeConnWindow(int connWinSize) throws IOException {
/* 1049 */     int delta = Integer.MAX_VALUE - connWinSize;
/* 1050 */     if (delta > 0) {
/* 1051 */       RawFrame windowUpdateFrame = this.frameFactory.createWindowUpdate(0, delta);
/* 1052 */       commitFrame(windowUpdateFrame);
/* 1053 */       updateInputWindow(0, this.connInputWindow, delta);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void consumePushPromiseFrame(RawFrame frame, ByteBuffer payload, H2Stream promisedStream) throws HttpException, IOException {
/* 1058 */     int promisedStreamId = promisedStream.getId();
/* 1059 */     if (!frame.isFlagSet(FrameFlag.END_HEADERS)) {
/* 1060 */       this.continuation = new Continuation(promisedStreamId, frame.getType(), true);
/*      */     }
/* 1062 */     if (this.continuation == null) {
/* 1063 */       List<Header> headers = this.hPackDecoder.decodeHeaders(payload);
/* 1064 */       if (promisedStreamId > this.processedRemoteStreamId) {
/* 1065 */         this.processedRemoteStreamId = promisedStreamId;
/*      */       }
/* 1067 */       if (this.streamListener != null) {
/* 1068 */         this.streamListener.onHeaderInput(this, promisedStreamId, headers);
/*      */       }
/* 1070 */       promisedStream.consumePromise(headers);
/*      */     } else {
/* 1072 */       this.continuation.copyPayload(payload);
/*      */     } 
/*      */   }
/*      */   
/*      */   List<Header> decodeHeaders(ByteBuffer payload) throws HttpException {
/* 1077 */     return this.hPackDecoder.decodeHeaders(payload);
/*      */   }
/*      */   
/*      */   private void consumeHeaderFrame(RawFrame frame, H2Stream stream) throws HttpException, IOException {
/* 1081 */     int streamId = stream.getId();
/* 1082 */     if (!frame.isFlagSet(FrameFlag.END_HEADERS)) {
/* 1083 */       this.continuation = new Continuation(streamId, frame.getType(), frame.isFlagSet(FrameFlag.END_STREAM));
/*      */     }
/* 1085 */     ByteBuffer payload = frame.getPayloadContent();
/* 1086 */     if (frame.isFlagSet(FrameFlag.PRIORITY)) {
/*      */       
/* 1088 */       payload.getInt();
/* 1089 */       payload.get();
/*      */     } 
/* 1091 */     if (this.continuation == null) {
/* 1092 */       List<Header> headers = decodeHeaders(payload);
/* 1093 */       if (stream.isRemoteInitiated() && streamId > this.processedRemoteStreamId) {
/* 1094 */         this.processedRemoteStreamId = streamId;
/*      */       }
/* 1096 */       if (this.streamListener != null) {
/* 1097 */         this.streamListener.onHeaderInput(this, streamId, headers);
/*      */       }
/* 1099 */       if (stream.isRemoteClosed()) {
/* 1100 */         throw new H2StreamResetException(H2Error.STREAM_CLOSED, "Stream already closed");
/*      */       }
/* 1102 */       if (stream.isLocalReset()) {
/*      */         return;
/*      */       }
/* 1105 */       if (frame.isFlagSet(FrameFlag.END_STREAM)) {
/* 1106 */         stream.setRemoteEndStream();
/*      */       }
/* 1108 */       stream.consumeHeader(headers);
/*      */     } else {
/* 1110 */       this.continuation.copyPayload(payload);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void consumeContinuationFrame(RawFrame frame, H2Stream stream) throws HttpException, IOException {
/* 1115 */     int streamId = frame.getStreamId();
/* 1116 */     ByteBuffer payload = frame.getPayload();
/* 1117 */     this.continuation.copyPayload(payload);
/* 1118 */     if (frame.isFlagSet(FrameFlag.END_HEADERS)) {
/* 1119 */       List<Header> headers = decodeHeaders(this.continuation.getContent());
/* 1120 */       if (stream.isRemoteInitiated() && streamId > this.processedRemoteStreamId) {
/* 1121 */         this.processedRemoteStreamId = streamId;
/*      */       }
/* 1123 */       if (this.streamListener != null) {
/* 1124 */         this.streamListener.onHeaderInput(this, streamId, headers);
/*      */       }
/* 1126 */       if (stream.isRemoteClosed()) {
/* 1127 */         throw new H2StreamResetException(H2Error.STREAM_CLOSED, "Stream already closed");
/*      */       }
/* 1129 */       if (stream.isLocalReset()) {
/*      */         return;
/*      */       }
/* 1132 */       if (this.continuation.endStream) {
/* 1133 */         stream.setRemoteEndStream();
/*      */       }
/* 1135 */       if (this.continuation.type == FrameType.PUSH_PROMISE.getValue()) {
/* 1136 */         stream.consumePromise(headers);
/*      */       } else {
/* 1138 */         stream.consumeHeader(headers);
/*      */       } 
/* 1140 */       this.continuation = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void consumeSettingsFrame(ByteBuffer payload) throws HttpException, IOException {
/* 1145 */     H2Config.Builder configBuilder = H2Config.initial();
/* 1146 */     while (payload.hasRemaining()) {
/* 1147 */       int code = payload.getShort();
/* 1148 */       int value = payload.getInt();
/* 1149 */       H2Param param = H2Param.valueOf(code);
/* 1150 */       if (param != null) {
/* 1151 */         switch (param) {
/*      */           case HEADER_TABLE_SIZE:
/*      */             try {
/* 1154 */               configBuilder.setHeaderTableSize(value);
/* 1155 */             } catch (IllegalArgumentException ex) {
/* 1156 */               throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, ex.getMessage());
/*      */             } 
/*      */           
/*      */           case MAX_CONCURRENT_STREAMS:
/*      */             try {
/* 1161 */               configBuilder.setMaxConcurrentStreams(value);
/* 1162 */             } catch (IllegalArgumentException ex) {
/* 1163 */               throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, ex.getMessage());
/*      */             } 
/*      */           
/*      */           case ENABLE_PUSH:
/* 1167 */             configBuilder.setPushEnabled((value == 1));
/*      */           
/*      */           case INITIAL_WINDOW_SIZE:
/*      */             try {
/* 1171 */               configBuilder.setInitialWindowSize(value);
/* 1172 */             } catch (IllegalArgumentException ex) {
/* 1173 */               throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, ex.getMessage());
/*      */             } 
/*      */           
/*      */           case MAX_FRAME_SIZE:
/*      */             try {
/* 1178 */               configBuilder.setMaxFrameSize(value);
/* 1179 */             } catch (IllegalArgumentException ex) {
/* 1180 */               throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, ex.getMessage());
/*      */             } 
/*      */           
/*      */           case MAX_HEADER_LIST_SIZE:
/*      */             try {
/* 1185 */               configBuilder.setMaxHeaderListSize(value);
/* 1186 */             } catch (IllegalArgumentException ex) {
/* 1187 */               throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, ex.getMessage());
/*      */             } 
/*      */         } 
/*      */       
/*      */       }
/*      */     } 
/* 1193 */     applyRemoteSettings(configBuilder.build());
/*      */   }
/*      */   
/*      */   private void produceOutput() throws HttpException, IOException {
/* 1197 */     for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/* 1198 */       Map.Entry<Integer, H2Stream> entry = it.next();
/* 1199 */       H2Stream stream = entry.getValue();
/* 1200 */       if (!stream.isLocalClosed() && stream.getOutputWindow().get() > 0) {
/* 1201 */         stream.produceOutput();
/*      */       }
/* 1203 */       if (stream.isTerminated()) {
/* 1204 */         it.remove();
/* 1205 */         stream.releaseResources();
/* 1206 */         requestSessionOutput();
/*      */       } 
/* 1208 */       if (!this.outputQueue.isEmpty()) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void applyRemoteSettings(H2Config config) throws H2ConnectionException {
/* 1215 */     this.remoteConfig = config;
/*      */     
/* 1217 */     this.hPackEncoder.setMaxTableSize(this.remoteConfig.getHeaderTableSize());
/* 1218 */     int delta = this.remoteConfig.getInitialWindowSize() - this.initOutputWinSize;
/* 1219 */     this.initOutputWinSize = this.remoteConfig.getInitialWindowSize();
/*      */     
/* 1221 */     int maxFrameSize = this.remoteConfig.getMaxFrameSize();
/* 1222 */     if (maxFrameSize > this.localConfig.getMaxFrameSize()) {
/* 1223 */       this.outputBuffer.expand(maxFrameSize);
/*      */     }
/*      */     
/* 1226 */     if (delta != 0 && 
/* 1227 */       !this.streamMap.isEmpty()) {
/* 1228 */       for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/* 1229 */         Map.Entry<Integer, H2Stream> entry = it.next();
/* 1230 */         H2Stream stream = entry.getValue();
/*      */         try {
/* 1232 */           updateOutputWindow(stream.getId(), stream.getOutputWindow(), delta);
/* 1233 */         } catch (ArithmeticException ex) {
/* 1234 */           throw new H2ConnectionException(H2Error.FLOW_CONTROL_ERROR, ex.getMessage());
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void applyLocalSettings() throws H2ConnectionException {
/* 1242 */     this.hPackDecoder.setMaxTableSize(this.localConfig.getHeaderTableSize());
/* 1243 */     this.hPackDecoder.setMaxListSize(this.localConfig.getMaxHeaderListSize());
/*      */     
/* 1245 */     int delta = this.localConfig.getInitialWindowSize() - this.initInputWinSize;
/* 1246 */     this.initInputWinSize = this.localConfig.getInitialWindowSize();
/*      */     
/* 1248 */     if (delta != 0 && !this.streamMap.isEmpty()) {
/* 1249 */       for (Iterator<Map.Entry<Integer, H2Stream>> it = this.streamMap.entrySet().iterator(); it.hasNext(); ) {
/* 1250 */         Map.Entry<Integer, H2Stream> entry = it.next();
/* 1251 */         H2Stream stream = entry.getValue();
/*      */         try {
/* 1253 */           updateInputWindow(stream.getId(), stream.getInputWindow(), delta);
/* 1254 */         } catch (ArithmeticException ex) {
/* 1255 */           throw new H2ConnectionException(H2Error.FLOW_CONTROL_ERROR, ex.getMessage());
/*      */         } 
/*      */       } 
/*      */     }
/* 1259 */     this.lowMark = this.initInputWinSize / 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/* 1264 */     this.ioSession.enqueue((Command)ShutdownCommand.GRACEFUL, Command.Priority.IMMEDIATE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void close(CloseMode closeMode) {
/* 1269 */     this.ioSession.close(closeMode);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOpen() {
/* 1274 */     return (this.connState == ConnectionHandshake.ACTIVE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSocketTimeout(Timeout timeout) {
/* 1279 */     this.ioSession.setSocketTimeout(timeout);
/*      */   }
/*      */ 
/*      */   
/*      */   public SSLSession getSSLSession() {
/* 1284 */     TlsDetails tlsDetails = this.ioSession.getTlsDetails();
/* 1285 */     return (tlsDetails != null) ? tlsDetails.getSSLSession() : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public EndpointDetails getEndpointDetails() {
/* 1290 */     if (this.endpointDetails == null) {
/* 1291 */       this
/*      */ 
/*      */ 
/*      */         
/* 1295 */         .endpointDetails = (EndpointDetails)new BasicEndpointDetails(this.ioSession.getRemoteAddress(), this.ioSession.getLocalAddress(), (HttpConnectionMetrics)this.connMetrics, this.ioSession.getSocketTimeout());
/*      */     }
/* 1297 */     return this.endpointDetails;
/*      */   }
/*      */ 
/*      */   
/*      */   public Timeout getSocketTimeout() {
/* 1302 */     return this.ioSession.getSocketTimeout();
/*      */   }
/*      */ 
/*      */   
/*      */   public ProtocolVersion getProtocolVersion() {
/* 1307 */     return (ProtocolVersion)HttpVersion.HTTP_2;
/*      */   }
/*      */ 
/*      */   
/*      */   public SocketAddress getRemoteAddress() {
/* 1312 */     return this.ioSession.getRemoteAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public SocketAddress getLocalAddress() {
/* 1317 */     return this.ioSession.getLocalAddress();
/*      */   }
/*      */   
/*      */   void appendState(StringBuilder buf) {
/* 1321 */     buf.append("connState=").append(this.connState)
/* 1322 */       .append(", connInputWindow=").append(this.connInputWindow)
/* 1323 */       .append(", connOutputWindow=").append(this.connOutputWindow)
/* 1324 */       .append(", outputQueue=").append(this.outputQueue.size())
/* 1325 */       .append(", streamMap=").append(this.streamMap.size())
/* 1326 */       .append(", processedRemoteStreamId=").append(this.processedRemoteStreamId);
/*      */   }
/*      */   abstract void acceptHeaderFrame() throws H2ConnectionException;
/*      */   abstract void acceptPushRequest() throws H2ConnectionException;
/*      */   abstract void acceptPushFrame() throws H2ConnectionException;
/*      */   abstract H2StreamHandler createRemotelyInitiatedStream(H2StreamChannel paramH2StreamChannel, HttpProcessor paramHttpProcessor, BasicHttpConnectionMetrics paramBasicHttpConnectionMetrics, HandlerFactory<AsyncPushConsumer> paramHandlerFactory) throws IOException;
/*      */   abstract H2StreamHandler createLocallyInitiatedStream(ExecutableCommand paramExecutableCommand, H2StreamChannel paramH2StreamChannel, HttpProcessor paramHttpProcessor, BasicHttpConnectionMetrics paramBasicHttpConnectionMetrics) throws IOException;
/*      */   private static class Continuation { final int streamId; final int type; final boolean endStream;
/*      */     final ByteArrayBuffer headerBuffer;
/*      */     
/*      */     private Continuation(int streamId, int type, boolean endStream) {
/* 1337 */       this.streamId = streamId;
/* 1338 */       this.type = type;
/* 1339 */       this.endStream = endStream;
/* 1340 */       this.headerBuffer = new ByteArrayBuffer(1024);
/*      */     }
/*      */     
/*      */     void copyPayload(ByteBuffer payload) {
/* 1344 */       if (payload == null) {
/*      */         return;
/*      */       }
/* 1347 */       this.headerBuffer.ensureCapacity(payload.remaining());
/* 1348 */       payload.get(this.headerBuffer.array(), this.headerBuffer.length(), payload.remaining());
/*      */     }
/*      */     
/*      */     ByteBuffer getContent() {
/* 1352 */       return ByteBuffer.wrap(this.headerBuffer.array(), 0, this.headerBuffer.length());
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   private class H2StreamChannelImpl
/*      */     implements H2StreamChannel
/*      */   {
/*      */     private final int id;
/*      */     
/*      */     private final AtomicInteger inputWindow;
/*      */     private final AtomicInteger outputWindow;
/*      */     private volatile boolean idle;
/*      */     private volatile boolean remoteEndStream;
/*      */     private volatile boolean localEndStream;
/*      */     private volatile long deadline;
/*      */     
/*      */     H2StreamChannelImpl(int id, boolean idle, int initialInputWindowSize, int initialOutputWindowSize) {
/* 1370 */       this.id = id;
/* 1371 */       this.idle = idle;
/* 1372 */       this.inputWindow = new AtomicInteger(initialInputWindowSize);
/* 1373 */       this.outputWindow = new AtomicInteger(initialOutputWindowSize);
/*      */     }
/*      */     
/*      */     int getId() {
/* 1377 */       return this.id;
/*      */     }
/*      */     
/*      */     AtomicInteger getOutputWindow() {
/* 1381 */       return this.outputWindow;
/*      */     }
/*      */     
/*      */     AtomicInteger getInputWindow() {
/* 1385 */       return this.inputWindow;
/*      */     }
/*      */ 
/*      */     
/*      */     public void submit(List<Header> headers, boolean endStream) throws IOException {
/* 1390 */       AbstractH2StreamMultiplexer.this.ioSession.getLock().lock();
/*      */       try {
/* 1392 */         if (headers == null || headers.isEmpty()) {
/* 1393 */           throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Message headers are missing");
/*      */         }
/* 1395 */         if (this.localEndStream) {
/*      */           return;
/*      */         }
/* 1398 */         this.idle = false;
/* 1399 */         AbstractH2StreamMultiplexer.this.commitHeaders(this.id, headers, endStream);
/* 1400 */         if (endStream) {
/* 1401 */           this.localEndStream = true;
/*      */         }
/*      */       } finally {
/* 1404 */         AbstractH2StreamMultiplexer.this.ioSession.getLock().unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void push(List<Header> headers, AsyncPushProducer pushProducer) throws HttpException, IOException {
/* 1410 */       AbstractH2StreamMultiplexer.this.acceptPushRequest();
/* 1411 */       int promisedStreamId = AbstractH2StreamMultiplexer.this.generateStreamId();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1416 */       H2StreamChannelImpl channel = new H2StreamChannelImpl(promisedStreamId, true, AbstractH2StreamMultiplexer.this.localConfig.getInitialWindowSize(), AbstractH2StreamMultiplexer.this.remoteConfig.getInitialWindowSize());
/* 1417 */       HttpCoreContext context = HttpCoreContext.create();
/* 1418 */       context.setAttribute("http.ssl-session", AbstractH2StreamMultiplexer.this.getSSLSession());
/* 1419 */       context.setAttribute("http.connection-endpoint", AbstractH2StreamMultiplexer.this.getEndpointDetails());
/*      */       
/* 1421 */       H2StreamHandler streamHandler = new ServerPushH2StreamHandler(channel, AbstractH2StreamMultiplexer.this.httpProcessor, AbstractH2StreamMultiplexer.this.connMetrics, pushProducer, context);
/* 1422 */       AbstractH2StreamMultiplexer.H2Stream stream = new AbstractH2StreamMultiplexer.H2Stream(channel, streamHandler, false);
/* 1423 */       AbstractH2StreamMultiplexer.this.streamMap.put(Integer.valueOf(promisedStreamId), stream);
/*      */       
/* 1425 */       AbstractH2StreamMultiplexer.this.ioSession.getLock().lock();
/*      */       try {
/* 1427 */         if (this.localEndStream) {
/* 1428 */           stream.releaseResources();
/*      */           return;
/*      */         } 
/* 1431 */         AbstractH2StreamMultiplexer.this.commitPushPromise(this.id, promisedStreamId, headers);
/* 1432 */         this.idle = false;
/*      */       } finally {
/* 1434 */         AbstractH2StreamMultiplexer.this.ioSession.getLock().unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void update(int increment) throws IOException {
/* 1440 */       if (this.remoteEndStream) {
/*      */         return;
/*      */       }
/* 1443 */       AbstractH2StreamMultiplexer.this.incrementInputCapacity(0, AbstractH2StreamMultiplexer.this.connInputWindow, increment);
/* 1444 */       AbstractH2StreamMultiplexer.this.incrementInputCapacity(this.id, this.inputWindow, increment);
/*      */     }
/*      */ 
/*      */     
/*      */     public int write(ByteBuffer payload) throws IOException {
/* 1449 */       AbstractH2StreamMultiplexer.this.ioSession.getLock().lock();
/*      */       try {
/* 1451 */         if (this.localEndStream) {
/* 1452 */           return 0;
/*      */         }
/* 1454 */         return AbstractH2StreamMultiplexer.this.streamData(this.id, this.outputWindow, payload);
/*      */       } finally {
/* 1456 */         AbstractH2StreamMultiplexer.this.ioSession.getLock().unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void endStream(List<? extends Header> trailers) throws IOException {
/* 1462 */       AbstractH2StreamMultiplexer.this.ioSession.getLock().lock();
/*      */       try {
/* 1464 */         if (this.localEndStream) {
/*      */           return;
/*      */         }
/* 1467 */         this.localEndStream = true;
/* 1468 */         if (trailers != null && !trailers.isEmpty()) {
/* 1469 */           AbstractH2StreamMultiplexer.this.commitHeaders(this.id, trailers, true);
/*      */         } else {
/* 1471 */           RawFrame frame = AbstractH2StreamMultiplexer.this.frameFactory.createData(this.id, null, true);
/* 1472 */           AbstractH2StreamMultiplexer.this.commitFrameInternal(frame);
/*      */         } 
/*      */       } finally {
/* 1475 */         AbstractH2StreamMultiplexer.this.ioSession.getLock().unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void endStream() throws IOException {
/* 1481 */       endStream(null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void requestOutput() {
/* 1486 */       AbstractH2StreamMultiplexer.this.requestSessionOutput();
/*      */     }
/*      */     
/*      */     boolean isRemoteClosed() {
/* 1490 */       return this.remoteEndStream;
/*      */     }
/*      */     
/*      */     void setRemoteEndStream() {
/* 1494 */       this.remoteEndStream = true;
/*      */     }
/*      */     
/*      */     boolean isLocalClosed() {
/* 1498 */       return this.localEndStream;
/*      */     }
/*      */     
/*      */     void setLocalEndStream() {
/* 1502 */       this.localEndStream = true;
/*      */     }
/*      */     
/*      */     boolean isLocalReset() {
/* 1506 */       return (this.deadline > 0L);
/*      */     }
/*      */     
/*      */     boolean isResetDeadline() {
/* 1510 */       long l = this.deadline;
/* 1511 */       return (l > 0L && l < System.currentTimeMillis());
/*      */     }
/*      */     
/*      */     boolean localReset(int code) throws IOException {
/* 1515 */       AbstractH2StreamMultiplexer.this.ioSession.getLock().lock();
/*      */       try {
/* 1517 */         if (this.localEndStream) {
/* 1518 */           return false;
/*      */         }
/* 1520 */         this.localEndStream = true;
/* 1521 */         this.deadline = System.currentTimeMillis() + 1000L;
/* 1522 */         if (!this.idle) {
/* 1523 */           RawFrame resetStream = AbstractH2StreamMultiplexer.this.frameFactory.createResetStream(this.id, code);
/* 1524 */           AbstractH2StreamMultiplexer.this.commitFrameInternal(resetStream);
/* 1525 */           return true;
/*      */         } 
/* 1527 */         return false;
/*      */       } finally {
/* 1529 */         AbstractH2StreamMultiplexer.this.ioSession.getLock().unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean localReset(H2Error error) throws IOException {
/* 1534 */       return localReset((error != null) ? error.getCode() : H2Error.INTERNAL_ERROR.getCode());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean cancel() {
/*      */       try {
/* 1540 */         return localReset(H2Error.CANCEL);
/* 1541 */       } catch (IOException ignore) {
/* 1542 */         return false;
/*      */       } 
/*      */     }
/*      */     
/*      */     void appendState(StringBuilder buf) {
/* 1547 */       buf.append("id=").append(this.id)
/* 1548 */         .append(", connState=").append(AbstractH2StreamMultiplexer.this.connState)
/* 1549 */         .append(", inputWindow=").append(this.inputWindow)
/* 1550 */         .append(", outputWindow=").append(this.outputWindow)
/* 1551 */         .append(", localEndStream=").append(this.localEndStream)
/* 1552 */         .append(", idle=").append(this.idle);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1557 */       StringBuilder buf = new StringBuilder();
/* 1558 */       buf.append("[");
/* 1559 */       appendState(buf);
/* 1560 */       buf.append("]");
/* 1561 */       return buf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class H2Stream
/*      */   {
/*      */     private final AbstractH2StreamMultiplexer.H2StreamChannelImpl channel;
/*      */     
/*      */     private final H2StreamHandler handler;
/*      */     
/*      */     private final boolean remoteInitiated;
/*      */ 
/*      */     
/*      */     private H2Stream(AbstractH2StreamMultiplexer.H2StreamChannelImpl channel, H2StreamHandler handler, boolean remoteInitiated) {
/* 1576 */       this.channel = channel;
/* 1577 */       this.handler = handler;
/* 1578 */       this.remoteInitiated = remoteInitiated;
/*      */     }
/*      */     
/*      */     int getId() {
/* 1582 */       return this.channel.getId();
/*      */     }
/*      */     
/*      */     boolean isRemoteInitiated() {
/* 1586 */       return this.remoteInitiated;
/*      */     }
/*      */     
/*      */     AtomicInteger getOutputWindow() {
/* 1590 */       return this.channel.getOutputWindow();
/*      */     }
/*      */     
/*      */     AtomicInteger getInputWindow() {
/* 1594 */       return this.channel.getInputWindow();
/*      */     }
/*      */     
/*      */     boolean isTerminated() {
/* 1598 */       return (this.channel.isLocalClosed() && (this.channel.isRemoteClosed() || this.channel.isResetDeadline()));
/*      */     }
/*      */     
/*      */     boolean isRemoteClosed() {
/* 1602 */       return this.channel.isRemoteClosed();
/*      */     }
/*      */     
/*      */     boolean isLocalClosed() {
/* 1606 */       return this.channel.isLocalClosed();
/*      */     }
/*      */     
/*      */     boolean isLocalReset() {
/* 1610 */       return this.channel.isLocalReset();
/*      */     }
/*      */     
/*      */     void setRemoteEndStream() {
/* 1614 */       this.channel.setRemoteEndStream();
/*      */     }
/*      */     
/*      */     void consumePromise(List<Header> headers) throws HttpException, IOException {
/*      */       try {
/* 1619 */         this.handler.consumePromise(headers);
/* 1620 */         this.channel.setLocalEndStream();
/* 1621 */       } catch (ProtocolException ex) {
/* 1622 */         localReset((Exception)ex, H2Error.PROTOCOL_ERROR);
/*      */       } 
/*      */     }
/*      */     
/*      */     void consumeHeader(List<Header> headers) throws HttpException, IOException {
/*      */       try {
/* 1628 */         this.handler.consumeHeader(headers, this.channel.isRemoteClosed());
/* 1629 */       } catch (ProtocolException ex) {
/* 1630 */         localReset((Exception)ex, H2Error.PROTOCOL_ERROR);
/*      */       } 
/*      */     }
/*      */     
/*      */     void consumeData(ByteBuffer src) throws HttpException, IOException {
/*      */       try {
/* 1636 */         this.handler.consumeData(src, this.channel.isRemoteClosed());
/* 1637 */       } catch (CharacterCodingException ex) {
/* 1638 */         localReset(ex, H2Error.INTERNAL_ERROR);
/* 1639 */       } catch (ProtocolException ex) {
/* 1640 */         localReset((Exception)ex, H2Error.PROTOCOL_ERROR);
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean isOutputReady() {
/* 1645 */       return this.handler.isOutputReady();
/*      */     }
/*      */     
/*      */     void produceOutput() throws HttpException, IOException {
/*      */       try {
/* 1650 */         this.handler.produceOutput();
/* 1651 */       } catch (ProtocolException ex) {
/* 1652 */         localReset((Exception)ex, H2Error.PROTOCOL_ERROR);
/*      */       } 
/*      */     }
/*      */     
/*      */     void produceInputCapacityUpdate() throws IOException {
/* 1657 */       this.handler.updateInputCapacity();
/*      */     }
/*      */     
/*      */     void reset(Exception cause) {
/* 1661 */       this.channel.setRemoteEndStream();
/* 1662 */       this.channel.setLocalEndStream();
/* 1663 */       this.handler.failed(cause);
/*      */     }
/*      */     
/*      */     void localReset(Exception cause, int code) throws IOException {
/* 1667 */       this.channel.localReset(code);
/* 1668 */       this.handler.failed(cause);
/*      */     }
/*      */     
/*      */     void localReset(Exception cause, H2Error error) throws IOException {
/* 1672 */       localReset(cause, (error != null) ? error.getCode() : H2Error.INTERNAL_ERROR.getCode());
/*      */     }
/*      */     
/*      */     void localReset(H2StreamResetException ex) throws IOException {
/* 1676 */       localReset((Exception)ex, ex.getCode());
/*      */     }
/*      */     
/*      */     void handle(HttpException ex) throws IOException, HttpException {
/* 1680 */       this.handler.handle(ex, this.channel.isRemoteClosed());
/*      */     }
/*      */     
/*      */     HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() {
/* 1684 */       return this.handler.getPushHandlerFactory();
/*      */     }
/*      */     
/*      */     void cancel() {
/* 1688 */       reset((Exception)new RequestNotExecutedException());
/*      */     }
/*      */     
/*      */     boolean abort() {
/* 1692 */       boolean cancelled = this.channel.cancel();
/* 1693 */       this.handler.failed((Exception)new RequestNotExecutedException());
/* 1694 */       return cancelled;
/*      */     }
/*      */     
/*      */     void releaseResources() {
/* 1698 */       this.handler.releaseResources();
/*      */     }
/*      */     
/*      */     void appendState(StringBuilder buf) {
/* 1702 */       buf.append("channel=[");
/* 1703 */       this.channel.appendState(buf);
/* 1704 */       buf.append("]");
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1709 */       StringBuilder buf = new StringBuilder();
/* 1710 */       buf.append("[");
/* 1711 */       appendState(buf);
/* 1712 */       buf.append("]");
/* 1713 */       return buf.toString();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/AbstractH2StreamMultiplexer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */