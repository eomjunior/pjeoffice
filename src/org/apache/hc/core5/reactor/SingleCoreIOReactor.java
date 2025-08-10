/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
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
/*     */ class SingleCoreIOReactor
/*     */   extends AbstractSingleCoreIOReactor
/*     */   implements ConnectionInitiator
/*     */ {
/*     */   private static final int MAX_CHANNEL_REQUESTS = 10000;
/*     */   private final IOEventHandlerFactory eventHandlerFactory;
/*     */   private final IOReactorConfig reactorConfig;
/*     */   private final Decorator<IOSession> ioSessionDecorator;
/*     */   private final IOSessionListener sessionListener;
/*     */   private final Callback<IOSession> sessionShutdownCallback;
/*     */   private final Queue<InternalDataChannel> closedSessions;
/*     */   private final Queue<ChannelEntry> channelQueue;
/*     */   private final Queue<IOSessionRequest> requestQueue;
/*     */   private final AtomicBoolean shutdownInitiated;
/*     */   private final long selectTimeoutMillis;
/*     */   private volatile long lastTimeoutCheckMillis;
/*     */   
/*     */   SingleCoreIOReactor(Callback<Exception> exceptionCallback, IOEventHandlerFactory eventHandlerFactory, IOReactorConfig reactorConfig, Decorator<IOSession> ioSessionDecorator, IOSessionListener sessionListener, Callback<IOSession> sessionShutdownCallback) {
/*  81 */     super(exceptionCallback);
/*  82 */     this.eventHandlerFactory = (IOEventHandlerFactory)Args.notNull(eventHandlerFactory, "Event handler factory");
/*  83 */     this.reactorConfig = (IOReactorConfig)Args.notNull(reactorConfig, "I/O reactor config");
/*  84 */     this.ioSessionDecorator = ioSessionDecorator;
/*  85 */     this.sessionListener = sessionListener;
/*  86 */     this.sessionShutdownCallback = sessionShutdownCallback;
/*  87 */     this.shutdownInitiated = new AtomicBoolean(false);
/*  88 */     this.closedSessions = new ConcurrentLinkedQueue<>();
/*  89 */     this.channelQueue = new ConcurrentLinkedQueue<>();
/*  90 */     this.requestQueue = new ConcurrentLinkedQueue<>();
/*  91 */     this.selectTimeoutMillis = this.reactorConfig.getSelectInterval().toMilliseconds();
/*     */   }
/*     */   
/*     */   void enqueueChannel(ChannelEntry entry) throws IOReactorShutdownException {
/*  95 */     if (getStatus().compareTo(IOReactorStatus.ACTIVE) > 0) {
/*  96 */       throw new IOReactorShutdownException("I/O reactor has been shut down");
/*     */     }
/*  98 */     this.channelQueue.add(entry);
/*  99 */     this.selector.wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   void doTerminate() {
/* 104 */     closePendingChannels();
/* 105 */     closePendingConnectionRequests();
/* 106 */     processClosedSessions();
/*     */   }
/*     */ 
/*     */   
/*     */   void doExecute() throws IOException {
/* 111 */     while (!Thread.currentThread().isInterrupted()) {
/*     */       
/* 113 */       int readyCount = this.selector.select(this.selectTimeoutMillis);
/*     */       
/* 115 */       if (getStatus().compareTo(IOReactorStatus.SHUTTING_DOWN) >= 0) {
/* 116 */         if (this.shutdownInitiated.compareAndSet(false, true)) {
/* 117 */           initiateSessionShutdown();
/*     */         }
/* 119 */         closePendingChannels();
/*     */       } 
/* 121 */       if (getStatus() == IOReactorStatus.SHUT_DOWN) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 126 */       if (readyCount > 0) {
/* 127 */         processEvents(this.selector.selectedKeys());
/*     */       }
/*     */       
/* 130 */       validateActiveChannels();
/*     */ 
/*     */       
/* 133 */       processClosedSessions();
/*     */ 
/*     */       
/* 136 */       if (getStatus() == IOReactorStatus.ACTIVE) {
/* 137 */         processPendingChannels();
/* 138 */         processPendingConnectionRequests();
/*     */       } 
/*     */ 
/*     */       
/* 142 */       if (getStatus() == IOReactorStatus.SHUTTING_DOWN && this.selector.keys().isEmpty()) {
/*     */         break;
/*     */       }
/* 145 */       if (getStatus() == IOReactorStatus.SHUT_DOWN) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initiateSessionShutdown() {
/* 152 */     if (this.sessionShutdownCallback != null) {
/* 153 */       Set<SelectionKey> keys = this.selector.keys();
/* 154 */       for (SelectionKey key : keys) {
/* 155 */         InternalChannel channel = (InternalChannel)key.attachment();
/* 156 */         if (channel instanceof InternalDataChannel) {
/* 157 */           this.sessionShutdownCallback.execute(channel);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateActiveChannels() {
/* 164 */     long currentTimeMillis = System.currentTimeMillis();
/* 165 */     if (currentTimeMillis - this.lastTimeoutCheckMillis >= this.selectTimeoutMillis) {
/* 166 */       this.lastTimeoutCheckMillis = currentTimeMillis;
/* 167 */       for (SelectionKey key : this.selector.keys()) {
/* 168 */         checkTimeout(key, currentTimeMillis);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processEvents(Set<SelectionKey> selectedKeys) {
/* 174 */     for (SelectionKey key : selectedKeys) {
/* 175 */       InternalChannel channel = (InternalChannel)key.attachment();
/* 176 */       if (channel != null) {
/*     */         try {
/* 178 */           channel.handleIOEvent(key.readyOps());
/* 179 */         } catch (CancelledKeyException ex) {
/* 180 */           channel.close(CloseMode.GRACEFUL);
/*     */         } 
/*     */       }
/*     */     } 
/* 184 */     selectedKeys.clear();
/*     */   }
/*     */   
/*     */   private void processPendingChannels() throws IOException {
/*     */     ChannelEntry entry;
/* 189 */     for (int i = 0; i < 10000 && (entry = this.channelQueue.poll()) != null; i++) {
/* 190 */       SelectionKey key; SocketChannel socketChannel = entry.channel;
/* 191 */       Object attachment = entry.attachment;
/*     */       try {
/* 193 */         prepareSocket(socketChannel.socket());
/* 194 */         socketChannel.configureBlocking(false);
/* 195 */       } catch (IOException ex) {
/* 196 */         logException(ex);
/*     */         try {
/* 198 */           socketChannel.close();
/* 199 */         } catch (IOException ex2) {
/* 200 */           logException(ex2);
/*     */         } 
/* 202 */         throw ex;
/*     */       } 
/*     */       
/*     */       try {
/* 206 */         key = socketChannel.register(this.selector, 1);
/* 207 */       } catch (ClosedChannelException ex) {
/*     */         return;
/*     */       } 
/* 210 */       IOSession ioSession = new IOSessionImpl("a", key, socketChannel);
/* 211 */       InternalDataChannel dataChannel = new InternalDataChannel(ioSession, null, this.ioSessionDecorator, this.sessionListener, this.closedSessions);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       dataChannel.upgrade(this.eventHandlerFactory.createHandler(dataChannel, attachment));
/* 218 */       dataChannel.setSocketTimeout(this.reactorConfig.getSoTimeout());
/* 219 */       key.attach(dataChannel);
/* 220 */       dataChannel.handleIOEvent(8);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processClosedSessions() {
/*     */     while (true) {
/* 226 */       InternalDataChannel dataChannel = this.closedSessions.poll();
/* 227 */       if (dataChannel == null) {
/*     */         break;
/*     */       }
/*     */       try {
/* 231 */         dataChannel.disconnected();
/* 232 */       } catch (CancelledKeyException cancelledKeyException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkTimeout(SelectionKey key, long nowMillis) {
/* 239 */     InternalChannel channel = (InternalChannel)key.attachment();
/* 240 */     if (channel != null) {
/* 241 */       channel.checkTimeout(nowMillis);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<IOSession> connect(NamedEndpoint remoteEndpoint, SocketAddress remoteAddress, SocketAddress localAddress, Timeout timeout, Object attachment, FutureCallback<IOSession> callback) throws IOReactorShutdownException {
/* 253 */     Args.notNull(remoteEndpoint, "Remote endpoint");
/*     */ 
/*     */     
/* 256 */     IOSessionRequest sessionRequest = new IOSessionRequest(remoteEndpoint, (remoteAddress != null) ? remoteAddress : new InetSocketAddress(remoteEndpoint.getHostName(), remoteEndpoint.getPort()), localAddress, timeout, attachment, callback);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     this.requestQueue.add(sessionRequest);
/* 263 */     this.selector.wakeup();
/*     */     
/* 265 */     return sessionRequest;
/*     */   }
/*     */   
/*     */   private void prepareSocket(Socket socket) throws IOException {
/* 269 */     socket.setTcpNoDelay(this.reactorConfig.isTcpNoDelay());
/* 270 */     socket.setKeepAlive(this.reactorConfig.isSoKeepAlive());
/* 271 */     if (this.reactorConfig.getSndBufSize() > 0) {
/* 272 */       socket.setSendBufferSize(this.reactorConfig.getSndBufSize());
/*     */     }
/* 274 */     if (this.reactorConfig.getRcvBufSize() > 0) {
/* 275 */       socket.setReceiveBufferSize(this.reactorConfig.getRcvBufSize());
/*     */     }
/* 277 */     if (this.reactorConfig.getTrafficClass() > 0) {
/* 278 */       socket.setTrafficClass(this.reactorConfig.getTrafficClass());
/*     */     }
/* 280 */     int linger = this.reactorConfig.getSoLinger().toSecondsIntBound();
/* 281 */     if (linger >= 0) {
/* 282 */       socket.setSoLinger(true, linger);
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateAddress(SocketAddress address) throws UnknownHostException {
/* 287 */     if (address instanceof InetSocketAddress) {
/* 288 */       InetSocketAddress endpoint = (InetSocketAddress)address;
/* 289 */       if (endpoint.isUnresolved()) {
/* 290 */         throw new UnknownHostException(endpoint.getHostName());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processPendingConnectionRequests() {
/*     */     IOSessionRequest sessionRequest;
/* 297 */     for (int i = 0; i < 10000 && (sessionRequest = this.requestQueue.poll()) != null; i++) {
/* 298 */       if (!sessionRequest.isCancelled()) {
/*     */         SocketChannel socketChannel;
/*     */         try {
/* 301 */           socketChannel = SocketChannel.open();
/* 302 */         } catch (IOException ex) {
/* 303 */           sessionRequest.failed(ex);
/*     */           return;
/*     */         } 
/*     */         try {
/* 307 */           processConnectionRequest(socketChannel, sessionRequest);
/* 308 */         } catch (IOException|SecurityException ex) {
/* 309 */           Closer.closeQuietly(socketChannel);
/* 310 */           sessionRequest.failed(ex);
/*     */         } 
/*     */       } 
/*     */     }  } private void processConnectionRequest(SocketChannel socketChannel, IOSessionRequest sessionRequest) throws IOException {
/*     */     SocketAddress targetAddress;
/*     */     IOEventHandlerFactory eventHandlerFactory;
/*     */     boolean connected;
/* 317 */     validateAddress(sessionRequest.localAddress);
/* 318 */     validateAddress(sessionRequest.remoteAddress);
/*     */     
/* 320 */     socketChannel.configureBlocking(false);
/* 321 */     prepareSocket(socketChannel.socket());
/*     */     
/* 323 */     if (sessionRequest.localAddress != null) {
/* 324 */       Socket sock = socketChannel.socket();
/* 325 */       sock.setReuseAddress(this.reactorConfig.isSoReuseAddress());
/* 326 */       sock.bind(sessionRequest.localAddress);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 331 */     if (this.reactorConfig.getSocksProxyAddress() != null) {
/* 332 */       targetAddress = this.reactorConfig.getSocksProxyAddress();
/*     */ 
/*     */ 
/*     */       
/* 336 */       eventHandlerFactory = new SocksProxyProtocolHandlerFactory(sessionRequest.remoteAddress, this.reactorConfig.getSocksProxyUsername(), this.reactorConfig.getSocksProxyPassword(), this.eventHandlerFactory);
/*     */     } else {
/*     */       
/* 339 */       targetAddress = sessionRequest.remoteAddress;
/* 340 */       eventHandlerFactory = this.eventHandlerFactory;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 347 */       connected = ((Boolean)AccessController.<Boolean>doPrivileged(() -> Boolean.valueOf(socketChannel.connect(targetAddress)))).booleanValue();
/*     */     }
/* 349 */     catch (PrivilegedActionException e) {
/* 350 */       Asserts.check(e.getCause() instanceof IOException, "method contract violation only checked exceptions are wrapped: " + e
/* 351 */           .getCause());
/*     */       
/* 353 */       throw (IOException)e.getCause();
/*     */     } 
/*     */ 
/*     */     
/* 357 */     SelectionKey key = socketChannel.register(this.selector, 9);
/* 358 */     InternalChannel channel = new InternalConnectChannel(key, socketChannel, sessionRequest, (k, sc, namedEndpoint, attachment) -> {
/*     */           IOSession ioSession = new IOSessionImpl("c", k, sc);
/*     */           
/*     */           InternalDataChannel dataChannel = new InternalDataChannel(ioSession, namedEndpoint, this.ioSessionDecorator, this.sessionListener, this.closedSessions);
/*     */           
/*     */           dataChannel.upgrade(eventHandlerFactory.createHandler(dataChannel, attachment));
/*     */           
/*     */           dataChannel.setSocketTimeout(this.reactorConfig.getSoTimeout());
/*     */           
/*     */           return dataChannel;
/*     */         });
/*     */     
/* 370 */     if (connected) {
/* 371 */       channel.handleIOEvent(8);
/*     */     } else {
/* 373 */       key.attach(channel);
/* 374 */       sessionRequest.assign(channel);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closePendingChannels() {
/*     */     ChannelEntry entry;
/* 380 */     while ((entry = this.channelQueue.poll()) != null) {
/* 381 */       SocketChannel socketChannel = entry.channel;
/*     */       try {
/* 383 */         socketChannel.close();
/* 384 */       } catch (IOException ex) {
/* 385 */         logException(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closePendingConnectionRequests() {
/*     */     IOSessionRequest sessionRequest;
/* 392 */     while ((sessionRequest = this.requestQueue.poll()) != null)
/* 393 */       sessionRequest.cancel(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/SingleCoreIOReactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */