/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLMode;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLSessionInitializer;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLSessionVerifier;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ import org.apache.hc.core5.util.TextUtils;
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
/*     */ final class InternalDataChannel
/*     */   extends InternalChannel
/*     */   implements ProtocolIOSession
/*     */ {
/*     */   private final IOSession ioSession;
/*     */   private final NamedEndpoint initialEndpoint;
/*     */   private final Decorator<IOSession> ioSessionDecorator;
/*     */   private final IOSessionListener sessionListener;
/*     */   private final Queue<InternalDataChannel> closedSessions;
/*     */   private final AtomicReference<SSLIOSession> tlsSessionRef;
/*     */   private final AtomicReference<IOSession> currentSessionRef;
/*     */   private final AtomicReference<IOEventHandler> eventHandlerRef;
/*     */   private final ConcurrentMap<String, ProtocolUpgradeHandler> protocolUpgradeHandlerMap;
/*     */   private final AtomicBoolean closed;
/*     */   
/*     */   InternalDataChannel(IOSession ioSession, NamedEndpoint initialEndpoint, Decorator<IOSession> ioSessionDecorator, IOSessionListener sessionListener, Queue<InternalDataChannel> closedSessions) {
/*  81 */     this.ioSession = ioSession;
/*  82 */     this.initialEndpoint = initialEndpoint;
/*  83 */     this.closedSessions = closedSessions;
/*  84 */     this.ioSessionDecorator = ioSessionDecorator;
/*  85 */     this.sessionListener = sessionListener;
/*  86 */     this.tlsSessionRef = new AtomicReference<>();
/*  87 */     this
/*  88 */       .currentSessionRef = new AtomicReference<>((ioSessionDecorator != null) ? (IOSession)ioSessionDecorator.decorate(ioSession) : ioSession);
/*  89 */     this.eventHandlerRef = new AtomicReference<>();
/*  90 */     this.protocolUpgradeHandlerMap = new ConcurrentHashMap<>();
/*  91 */     this.closed = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  96 */     return this.ioSession.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public NamedEndpoint getInitialEndpoint() {
/* 101 */     return this.initialEndpoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public IOEventHandler getHandler() {
/* 106 */     return this.eventHandlerRef.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgrade(IOEventHandler handler) {
/* 111 */     IOSession currentSession = this.currentSessionRef.get();
/* 112 */     currentSession.upgrade(handler);
/* 113 */     this.eventHandlerRef.set(handler);
/*     */   }
/*     */   
/*     */   private IOEventHandler ensureHandler(IOSession session) {
/* 117 */     IOEventHandler handler = session.getHandler();
/* 118 */     Asserts.notNull(handler, "IO event handler");
/* 119 */     return handler;
/*     */   }
/*     */ 
/*     */   
/*     */   void onIOEvent(int readyOps) throws IOException {
/* 124 */     if ((readyOps & 0x8) != 0) {
/* 125 */       IOSession currentSession = this.currentSessionRef.get();
/* 126 */       currentSession.clearEvent(8);
/* 127 */       if (this.tlsSessionRef.get() == null) {
/* 128 */         if (this.sessionListener != null) {
/* 129 */           this.sessionListener.connected(currentSession);
/*     */         }
/* 131 */         IOEventHandler handler = ensureHandler(currentSession);
/* 132 */         handler.connected(currentSession);
/*     */       } 
/*     */     } 
/* 135 */     if ((readyOps & 0x1) != 0) {
/* 136 */       IOSession currentSession = this.currentSessionRef.get();
/* 137 */       currentSession.updateReadTime();
/* 138 */       if (this.sessionListener != null) {
/* 139 */         this.sessionListener.inputReady(currentSession);
/*     */       }
/* 141 */       IOEventHandler handler = ensureHandler(currentSession);
/* 142 */       handler.inputReady(currentSession, null);
/*     */     } 
/* 144 */     if ((readyOps & 0x4) != 0 || (this.ioSession
/* 145 */       .getEventMask() & 0x4) != 0) {
/* 146 */       IOSession currentSession = this.currentSessionRef.get();
/* 147 */       currentSession.updateWriteTime();
/* 148 */       if (this.sessionListener != null) {
/* 149 */         this.sessionListener.outputReady(currentSession);
/*     */       }
/* 151 */       IOEventHandler handler = ensureHandler(currentSession);
/* 152 */       handler.outputReady(currentSession);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Timeout getTimeout() {
/* 158 */     IOSession currentSession = this.currentSessionRef.get();
/* 159 */     return currentSession.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   void onTimeout(Timeout timeout) throws IOException {
/* 164 */     IOSession currentSession = this.currentSessionRef.get();
/* 165 */     if (this.sessionListener != null) {
/* 166 */       this.sessionListener.timeout(currentSession);
/*     */     }
/* 168 */     IOEventHandler handler = ensureHandler(currentSession);
/* 169 */     handler.timeout(currentSession, timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   void onException(Exception cause) {
/* 174 */     IOSession currentSession = this.currentSessionRef.get();
/* 175 */     if (this.sessionListener != null) {
/* 176 */       this.sessionListener.exception(currentSession, cause);
/*     */     }
/* 178 */     IOEventHandler handler = currentSession.getHandler();
/* 179 */     if (handler != null) {
/* 180 */       handler.exception(currentSession, cause);
/*     */     }
/*     */   }
/*     */   
/*     */   void onTLSSessionStart(SSLIOSession sslSession) {
/* 185 */     IOSession currentSession = this.currentSessionRef.get();
/* 186 */     if (this.sessionListener != null) {
/* 187 */       this.sessionListener.connected(currentSession);
/*     */     }
/*     */   }
/*     */   
/*     */   void onTLSSessionEnd(SSLIOSession sslSession) {
/* 192 */     if (this.closed.compareAndSet(false, true)) {
/* 193 */       this.closedSessions.add(this);
/*     */     }
/*     */   }
/*     */   
/*     */   void disconnected() {
/* 198 */     IOSession currentSession = this.currentSessionRef.get();
/* 199 */     if (this.sessionListener != null) {
/* 200 */       this.sessionListener.disconnected(currentSession);
/*     */     }
/* 202 */     IOEventHandler handler = currentSession.getHandler();
/* 203 */     if (handler != null) {
/* 204 */       handler.disconnected(currentSession);
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
/*     */   public void startTls(SSLContext sslContext, NamedEndpoint endpoint, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier, Timeout handshakeTimeout) {
/* 216 */     startTls(sslContext, endpoint, sslBufferMode, initializer, verifier, handshakeTimeout, null);
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
/*     */   public void startTls(SSLContext sslContext, NamedEndpoint endpoint, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier, Timeout handshakeTimeout, final FutureCallback<TransportSecurityLayer> callback) {
/* 228 */     SSLIOSession sslioSession = new SSLIOSession((endpoint != null) ? endpoint : this.initialEndpoint, this.ioSession, (this.initialEndpoint != null) ? SSLMode.CLIENT : SSLMode.SERVER, sslContext, sslBufferMode, initializer, verifier, handshakeTimeout, this::onTLSSessionStart, this::onTLSSessionEnd, (FutureCallback)new CallbackContribution<SSLSession>(callback)
/*     */         {
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
/*     */           public void completed(SSLSession sslSession)
/*     */           {
/* 243 */             if (callback != null) {
/* 244 */               callback.completed(InternalDataChannel.this);
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 249 */     if (this.tlsSessionRef.compareAndSet(null, sslioSession)) {
/* 250 */       this.currentSessionRef.set((this.ioSessionDecorator != null) ? (IOSession)this.ioSessionDecorator.decorate(sslioSession) : (IOSession)sslioSession);
/*     */     } else {
/* 252 */       throw new IllegalStateException("TLS already activated");
/*     */     } 
/*     */     try {
/* 255 */       if (this.sessionListener != null) {
/* 256 */         this.sessionListener.startTls((IOSession)sslioSession);
/*     */       }
/* 258 */       sslioSession.beginHandshake(this);
/* 259 */     } catch (Exception ex) {
/* 260 */       onException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TlsDetails getTlsDetails() {
/* 267 */     SSLIOSession sslIoSession = this.tlsSessionRef.get();
/* 268 */     return (sslIoSession != null) ? sslIoSession.getTlsDetails() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Lock getLock() {
/* 273 */     return this.ioSession.getLock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 278 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 283 */     IOSession currentSession = this.currentSessionRef.get();
/* 284 */     if (closeMode == CloseMode.IMMEDIATE) {
/* 285 */       this.closed.set(true);
/* 286 */       currentSession.close(closeMode);
/*     */     }
/* 288 */     else if (this.closed.compareAndSet(false, true)) {
/*     */       try {
/* 290 */         currentSession.close(closeMode);
/*     */       } finally {
/* 292 */         this.closedSessions.add(this);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IOSession.Status getStatus() {
/* 300 */     IOSession currentSession = this.currentSessionRef.get();
/* 301 */     return currentSession.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 306 */     IOSession currentSession = this.currentSessionRef.get();
/* 307 */     return currentSession.isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void enqueue(Command command, Command.Priority priority) {
/* 312 */     IOSession currentSession = this.currentSessionRef.get();
/* 313 */     currentSession.enqueue(command, priority);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCommands() {
/* 318 */     IOSession currentSession = this.currentSessionRef.get();
/* 319 */     return currentSession.hasCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public Command poll() {
/* 324 */     IOSession currentSession = this.currentSessionRef.get();
/* 325 */     return currentSession.poll();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteChannel channel() {
/* 330 */     IOSession currentSession = this.currentSessionRef.get();
/* 331 */     return currentSession.channel();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 336 */     return this.ioSession.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 341 */     return this.ioSession.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEventMask() {
/* 346 */     IOSession currentSession = this.currentSessionRef.get();
/* 347 */     return currentSession.getEventMask();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEventMask(int ops) {
/* 352 */     IOSession currentSession = this.currentSessionRef.get();
/* 353 */     currentSession.setEventMask(ops);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEvent(int op) {
/* 358 */     IOSession currentSession = this.currentSessionRef.get();
/* 359 */     currentSession.setEvent(op);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearEvent(int op) {
/* 364 */     IOSession currentSession = this.currentSessionRef.get();
/* 365 */     currentSession.clearEvent(op);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 370 */     return this.ioSession.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 375 */     this.ioSession.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/* 380 */     IOSession currentSession = this.currentSessionRef.get();
/* 381 */     return currentSession.read(dst);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/* 386 */     IOSession currentSession = this.currentSessionRef.get();
/* 387 */     return currentSession.write(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateReadTime() {
/* 392 */     this.ioSession.updateReadTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWriteTime() {
/* 397 */     this.ioSession.updateWriteTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastReadTime() {
/* 402 */     return this.ioSession.getLastReadTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastWriteTime() {
/* 407 */     return this.ioSession.getLastWriteTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastEventTime() {
/* 412 */     return this.ioSession.getLastEventTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public void switchProtocol(String protocolId, FutureCallback<ProtocolIOSession> callback) {
/* 417 */     Args.notEmpty(protocolId, "Application protocol ID");
/* 418 */     ProtocolUpgradeHandler upgradeHandler = this.protocolUpgradeHandlerMap.get(TextUtils.toLowerCase(protocolId));
/* 419 */     if (upgradeHandler != null) {
/* 420 */       upgradeHandler.upgrade(this, callback);
/*     */     } else {
/* 422 */       throw new IllegalStateException("Unsupported protocol: " + protocolId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerProtocol(String protocolId, ProtocolUpgradeHandler upgradeHandler) {
/* 428 */     Args.notEmpty(protocolId, "Application protocol ID");
/* 429 */     Args.notNull(upgradeHandler, "Protocol upgrade handler");
/* 430 */     this.protocolUpgradeHandlerMap.put(TextUtils.toLowerCase(protocolId), upgradeHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 435 */     IOSession currentSession = this.currentSessionRef.get();
/* 436 */     if (currentSession != null) {
/* 437 */       return currentSession.toString();
/*     */     }
/* 439 */     return this.ioSession.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/InternalDataChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */