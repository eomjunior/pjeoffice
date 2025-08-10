/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.BindException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.io.Closer;
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
/*     */ class SingleCoreListeningIOReactor
/*     */   extends AbstractSingleCoreIOReactor
/*     */   implements ConnectionAcceptor
/*     */ {
/*     */   private final IOReactorConfig reactorConfig;
/*     */   private final Callback<ChannelEntry> callback;
/*     */   private final Queue<ListenerEndpointRequest> requestQueue;
/*     */   private final ConcurrentMap<ListenerEndpointImpl, Boolean> endpoints;
/*     */   private final AtomicBoolean paused;
/*     */   private final long selectTimeoutMillis;
/*     */   
/*     */   SingleCoreListeningIOReactor(Callback<Exception> exceptionCallback, IOReactorConfig ioReactorConfig, Callback<ChannelEntry> callback) {
/*  66 */     super(exceptionCallback);
/*  67 */     this.reactorConfig = (ioReactorConfig != null) ? ioReactorConfig : IOReactorConfig.DEFAULT;
/*  68 */     this.callback = callback;
/*  69 */     this.requestQueue = new ConcurrentLinkedQueue<>();
/*  70 */     this.endpoints = new ConcurrentHashMap<>();
/*  71 */     this.paused = new AtomicBoolean(false);
/*  72 */     this.selectTimeoutMillis = this.reactorConfig.getSelectInterval().toMilliseconds();
/*     */   }
/*     */ 
/*     */   
/*     */   void doTerminate() {
/*     */     ListenerEndpointRequest request;
/*  78 */     while ((request = this.requestQueue.poll()) != null) {
/*  79 */       request.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void doExecute() throws IOException {
/*  85 */     while (!Thread.currentThread().isInterrupted() && 
/*  86 */       getStatus() == IOReactorStatus.ACTIVE) {
/*     */ 
/*     */ 
/*     */       
/*  90 */       int readyCount = this.selector.select(this.selectTimeoutMillis);
/*     */       
/*  92 */       if (getStatus() != IOReactorStatus.ACTIVE) {
/*     */         break;
/*     */       }
/*     */       
/*  96 */       processEvents(readyCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processEvents(int readyCount) throws IOException {
/* 101 */     if (!this.paused.get()) {
/* 102 */       processSessionRequests();
/*     */     }
/*     */     
/* 105 */     if (readyCount > 0) {
/* 106 */       Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
/* 107 */       for (SelectionKey key : selectedKeys)
/*     */       {
/* 109 */         processEvent(key);
/*     */       }
/*     */       
/* 112 */       selectedKeys.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processEvent(SelectionKey key) throws IOException {
/*     */     try {
/* 119 */       if (key.isAcceptable()) {
/*     */         
/* 121 */         ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
/*     */         while (true) {
/* 123 */           SocketChannel socketChannel = serverChannel.accept();
/* 124 */           if (socketChannel == null) {
/*     */             break;
/*     */           }
/* 127 */           ListenerEndpointRequest endpointRequest = (ListenerEndpointRequest)key.attachment();
/* 128 */           this.callback.execute(new ChannelEntry(socketChannel, endpointRequest.attachment));
/*     */         }
/*     */       
/*     */       } 
/* 132 */     } catch (CancelledKeyException ex) {
/* 133 */       ListenerEndpointImpl endpoint = (ListenerEndpointImpl)key.attachment();
/* 134 */       this.endpoints.remove(endpoint);
/* 135 */       key.attach(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address, Object attachment, FutureCallback<ListenerEndpoint> callback) {
/* 142 */     if (getStatus().compareTo(IOReactorStatus.SHUTTING_DOWN) >= 0) {
/* 143 */       throw new IOReactorShutdownException("I/O reactor has been shut down");
/*     */     }
/* 145 */     BasicFuture<ListenerEndpoint> future = new BasicFuture(callback);
/* 146 */     this.requestQueue.add(new ListenerEndpointRequest(address, attachment, future));
/* 147 */     this.selector.wakeup();
/* 148 */     return (Future<ListenerEndpoint>)future;
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address, FutureCallback<ListenerEndpoint> callback) {
/* 153 */     return listen(address, (Object)null, callback);
/*     */   }
/*     */   
/*     */   private void processSessionRequests() throws IOException {
/*     */     ListenerEndpointRequest request;
/* 158 */     while ((request = this.requestQueue.poll()) != null) {
/* 159 */       if (request.isCancelled()) {
/*     */         continue;
/*     */       }
/* 162 */       SocketAddress address = request.address;
/* 163 */       ServerSocketChannel serverChannel = ServerSocketChannel.open();
/*     */       try {
/* 165 */         ServerSocket socket = serverChannel.socket();
/* 166 */         socket.setReuseAddress(this.reactorConfig.isSoReuseAddress());
/* 167 */         if (this.reactorConfig.getRcvBufSize() > 0) {
/* 168 */           socket.setReceiveBufferSize(this.reactorConfig.getRcvBufSize());
/*     */         }
/* 170 */         serverChannel.configureBlocking(false);
/*     */         
/*     */         try {
/* 173 */           socket.bind(address, this.reactorConfig.getBacklogSize());
/* 174 */         } catch (BindException ex) {
/*     */           
/* 176 */           BindException detailedEx = new BindException(String.format("Socket bind failure for socket %s, address=%s, BacklogSize=%d: %s", new Object[] {
/* 177 */                   socket, address, Integer.valueOf(this.reactorConfig.getBacklogSize()), ex }));
/* 178 */           detailedEx.setStackTrace(ex.getStackTrace());
/* 179 */           throw detailedEx;
/*     */         } 
/*     */         
/* 182 */         SelectionKey key = serverChannel.register(this.selector, 16);
/* 183 */         key.attach(request);
/* 184 */         ListenerEndpointImpl endpoint = new ListenerEndpointImpl(key, request.attachment, socket.getLocalSocketAddress());
/* 185 */         this.endpoints.put(endpoint, Boolean.TRUE);
/* 186 */         request.completed(endpoint);
/* 187 */       } catch (IOException ex) {
/* 188 */         Closer.closeQuietly(serverChannel);
/* 189 */         request.failed(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ListenerEndpoint> getEndpoints() {
/* 196 */     Set<ListenerEndpoint> set = new HashSet<>();
/* 197 */     Iterator<ListenerEndpointImpl> it = this.endpoints.keySet().iterator();
/* 198 */     while (it.hasNext()) {
/* 199 */       ListenerEndpoint endpoint = it.next();
/* 200 */       if (!endpoint.isClosed()) {
/* 201 */         set.add(endpoint); continue;
/*     */       } 
/* 203 */       it.remove();
/*     */     } 
/*     */     
/* 206 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pause() throws IOException {
/* 211 */     if (this.paused.compareAndSet(false, true)) {
/* 212 */       Iterator<ListenerEndpointImpl> it = this.endpoints.keySet().iterator();
/* 213 */       while (it.hasNext()) {
/* 214 */         ListenerEndpointImpl endpoint = it.next();
/* 215 */         if (!endpoint.isClosed()) {
/* 216 */           endpoint.close();
/* 217 */           this.requestQueue.add(new ListenerEndpointRequest(endpoint.address, endpoint.attachment, null));
/*     */         } 
/* 219 */         it.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void resume() throws IOException {
/* 226 */     if (this.paused.compareAndSet(true, false))
/* 227 */       this.selector.wakeup(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/SingleCoreListeningIOReactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */