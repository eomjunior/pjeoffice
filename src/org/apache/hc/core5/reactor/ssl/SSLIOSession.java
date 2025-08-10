/*     */ package org.apache.hc.core5.reactor.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.SocketTimeoutExceptionFactory;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOSession;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ @Internal
/*     */ public class SSLIOSession
/*     */   implements IOSession
/*     */ {
/*     */   enum TLSHandShakeState
/*     */   {
/*  76 */     READY, INITIALIZED, HANDSHAKING, COMPLETE;
/*     */   }
/*  78 */   private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);
/*     */   
/*     */   private final NamedEndpoint targetEndpoint;
/*     */   
/*     */   private final IOSession session;
/*     */   
/*     */   private final SSLEngine sslEngine;
/*     */   private final SSLManagedBuffer inEncrypted;
/*     */   private final SSLManagedBuffer outEncrypted;
/*     */   private final SSLManagedBuffer inPlain;
/*     */   private final SSLSessionInitializer initializer;
/*     */   private final SSLSessionVerifier verifier;
/*     */   private final Callback<SSLIOSession> sessionStartCallback;
/*     */   private final Callback<SSLIOSession> sessionEndCallback;
/*     */   private final AtomicReference<FutureCallback<SSLSession>> handshakeCallbackRef;
/*     */   private final Timeout handshakeTimeout;
/*     */   private final SSLMode sslMode;
/*     */   private final AtomicInteger outboundClosedCount;
/*     */   private final AtomicReference<TLSHandShakeState> handshakeStateRef;
/*     */   private final IOEventHandler internalEventHandler;
/*     */   private int appEventMask;
/*     */   private volatile boolean endOfStream;
/* 100 */   private volatile IOSession.Status status = IOSession.Status.ACTIVE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Timeout socketTimeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile TlsDetails tlsDetails;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLIOSession(NamedEndpoint targetEndpoint, IOSession session, SSLMode sslMode, SSLContext sslContext, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier, Callback<SSLIOSession> sessionStartCallback, Callback<SSLIOSession> sessionEndCallback, Timeout connectTimeout) {
/* 129 */     this(targetEndpoint, session, sslMode, sslContext, sslBufferMode, initializer, verifier, connectTimeout, sessionStartCallback, sessionEndCallback, null);
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
/*     */   public SSLIOSession(NamedEndpoint targetEndpoint, final IOSession session, SSLMode sslMode, SSLContext sslContext, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier, final Timeout handshakeTimeout, Callback<SSLIOSession> sessionStartCallback, Callback<SSLIOSession> sessionEndCallback, FutureCallback<SSLSession> resultCallback) {
/* 161 */     Args.notNull(session, "IO session");
/* 162 */     Args.notNull(sslContext, "SSL context");
/* 163 */     this.targetEndpoint = targetEndpoint;
/* 164 */     this.session = session;
/* 165 */     this.sslMode = sslMode;
/* 166 */     this.initializer = initializer;
/* 167 */     this.verifier = verifier;
/* 168 */     this.sessionStartCallback = sessionStartCallback;
/* 169 */     this.sessionEndCallback = sessionEndCallback;
/* 170 */     this.handshakeCallbackRef = new AtomicReference<>(resultCallback);
/*     */     
/* 172 */     this.appEventMask = session.getEventMask();
/* 173 */     if (this.sslMode == SSLMode.CLIENT && targetEndpoint != null) {
/* 174 */       this.sslEngine = sslContext.createSSLEngine(targetEndpoint.getHostName(), targetEndpoint.getPort());
/*     */     } else {
/* 176 */       this.sslEngine = sslContext.createSSLEngine();
/*     */     } 
/*     */     
/* 179 */     SSLSession sslSession = this.sslEngine.getSession();
/*     */     
/* 181 */     int netBufferSize = sslSession.getPacketBufferSize();
/* 182 */     this.inEncrypted = SSLManagedBuffer.create(sslBufferMode, netBufferSize);
/* 183 */     this.outEncrypted = SSLManagedBuffer.create(sslBufferMode, netBufferSize);
/*     */ 
/*     */     
/* 186 */     int appBufferSize = sslSession.getApplicationBufferSize();
/* 187 */     this.inPlain = SSLManagedBuffer.create(sslBufferMode, appBufferSize);
/* 188 */     this.outboundClosedCount = new AtomicInteger(0);
/* 189 */     this.handshakeStateRef = new AtomicReference<>(TLSHandShakeState.READY);
/* 190 */     this.handshakeTimeout = handshakeTimeout;
/* 191 */     this.internalEventHandler = new IOEventHandler()
/*     */       {
/*     */         public void connected(IOSession protocolSession) throws IOException
/*     */         {
/* 195 */           SSLIOSession.this.beginHandshake(protocolSession);
/*     */         }
/*     */ 
/*     */         
/*     */         public void inputReady(IOSession protocolSession, ByteBuffer src) throws IOException {
/* 200 */           SSLIOSession.this.receiveEncryptedData();
/* 201 */           SSLIOSession.this.doHandshake(protocolSession);
/* 202 */           SSLIOSession.this.decryptData(protocolSession);
/* 203 */           SSLIOSession.this.updateEventMask();
/*     */         }
/*     */ 
/*     */         
/*     */         public void outputReady(IOSession protocolSession) throws IOException {
/* 208 */           SSLIOSession.this.encryptData(protocolSession);
/* 209 */           SSLIOSession.this.sendEncryptedData();
/* 210 */           SSLIOSession.this.doHandshake(protocolSession);
/* 211 */           SSLIOSession.this.updateEventMask();
/*     */         }
/*     */ 
/*     */         
/*     */         public void timeout(IOSession protocolSession, Timeout timeout) throws IOException {
/* 216 */           if (SSLIOSession.this.sslEngine.isInboundDone() && !SSLIOSession.this.sslEngine.isInboundDone())
/*     */           {
/* 218 */             SSLIOSession.this.close(CloseMode.IMMEDIATE);
/*     */           }
/* 220 */           if (SSLIOSession.this.handshakeStateRef.get() != SSLIOSession.TLSHandShakeState.COMPLETE) {
/* 221 */             exception(protocolSession, SocketTimeoutExceptionFactory.create(handshakeTimeout));
/*     */           } else {
/* 223 */             SSLIOSession.this.ensureHandler().timeout(protocolSession, timeout);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void exception(IOSession protocolSession, Exception cause) {
/* 229 */           FutureCallback<SSLSession> resultCallback = SSLIOSession.this.handshakeCallbackRef.getAndSet(null);
/* 230 */           if (resultCallback != null) {
/* 231 */             resultCallback.failed(cause);
/*     */           }
/* 233 */           IOEventHandler handler = session.getHandler();
/* 234 */           if (SSLIOSession.this.handshakeStateRef.get() != SSLIOSession.TLSHandShakeState.COMPLETE) {
/* 235 */             session.close(CloseMode.GRACEFUL);
/* 236 */             SSLIOSession.this.close(CloseMode.IMMEDIATE);
/*     */           } 
/* 238 */           if (handler != null) {
/* 239 */             handler.exception(protocolSession, cause);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void disconnected(IOSession protocolSession) {
/* 245 */           IOEventHandler handler = session.getHandler();
/* 246 */           if (handler != null) {
/* 247 */             handler.disconnected(protocolSession);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private IOEventHandler ensureHandler() {
/* 256 */     IOEventHandler handler = this.session.getHandler();
/* 257 */     Asserts.notNull(handler, "IO event handler");
/* 258 */     return handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public IOEventHandler getHandler() {
/* 263 */     return this.internalEventHandler;
/*     */   }
/*     */   
/*     */   public void beginHandshake(IOSession protocolSession) throws IOException {
/* 267 */     if (this.handshakeStateRef.compareAndSet(TLSHandShakeState.READY, TLSHandShakeState.INITIALIZED)) {
/* 268 */       initialize(protocolSession);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void initialize(IOSession protocolSession) throws IOException {
/* 274 */     this.socketTimeout = this.session.getSocketTimeout();
/* 275 */     if (this.handshakeTimeout != null) {
/* 276 */       this.session.setSocketTimeout(this.handshakeTimeout);
/*     */     }
/*     */     
/* 279 */     this.session.getLock().lock();
/*     */     try {
/* 281 */       if (this.status.compareTo((Enum)IOSession.Status.CLOSING) >= 0) {
/*     */         return;
/*     */       }
/* 284 */       switch (this.sslMode) {
/*     */         case NEED_WRAP:
/* 286 */           this.sslEngine.setUseClientMode(true);
/*     */           break;
/*     */         case NEED_UNWRAP:
/* 289 */           this.sslEngine.setUseClientMode(false);
/*     */           break;
/*     */       } 
/* 292 */       if (this.initializer != null) {
/* 293 */         this.initializer.initialize(this.targetEndpoint, this.sslEngine);
/*     */       }
/* 295 */       this.handshakeStateRef.set(TLSHandShakeState.HANDSHAKING);
/* 296 */       this.sslEngine.beginHandshake();
/*     */       
/* 298 */       this.inEncrypted.release();
/* 299 */       this.outEncrypted.release();
/* 300 */       doHandshake(protocolSession);
/* 301 */       updateEventMask();
/*     */     } finally {
/* 303 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SSLException convert(RuntimeException ex) {
/* 313 */     Throwable cause = ex.getCause();
/* 314 */     if (cause == null) {
/* 315 */       cause = ex;
/*     */     }
/* 317 */     return new SSLException(cause);
/*     */   }
/*     */   
/*     */   private SSLEngineResult doWrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/*     */     try {
/* 322 */       return this.sslEngine.wrap(src, dst);
/* 323 */     } catch (RuntimeException ex) {
/* 324 */       throw convert(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SSLEngineResult doUnwrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/*     */     try {
/* 330 */       return this.sslEngine.unwrap(src, dst);
/* 331 */     } catch (RuntimeException ex) {
/* 332 */       throw convert(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doRunTask() {
/* 337 */     Runnable r = this.sslEngine.getDelegatedTask();
/* 338 */     if (r != null) {
/* 339 */       r.run();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doHandshake(IOSession protocolSession) throws IOException {
/* 344 */     boolean handshaking = true;
/*     */     
/* 346 */     SSLEngineResult result = null;
/* 347 */     while (handshaking) {
/* 348 */       ByteBuffer inEncryptedBuf, inPlainBuf; SSLEngineResult.HandshakeStatus handshakeStatus = this.sslEngine.getHandshakeStatus();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 353 */       if (handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING && this.outboundClosedCount.get() > 0) {
/* 354 */         handshakeStatus = SSLEngineResult.HandshakeStatus.NEED_WRAP;
/*     */       }
/*     */       
/* 357 */       switch (handshakeStatus) {
/*     */ 
/*     */         
/*     */         case NEED_WRAP:
/* 361 */           this.session.getLock().lock();
/*     */           
/*     */           try {
/* 364 */             ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();
/*     */ 
/*     */             
/* 367 */             result = doWrap(EMPTY_BUFFER, outEncryptedBuf);
/*     */             
/* 369 */             if (result.getStatus() != SSLEngineResult.Status.OK || result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
/* 370 */               handshaking = false;
/*     */             }
/*     */           } finally {
/*     */             
/* 374 */             this.session.getLock().unlock();
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/*     */         case NEED_UNWRAP:
/* 380 */           inEncryptedBuf = this.inEncrypted.acquire();
/* 381 */           inPlainBuf = this.inPlain.acquire();
/*     */ 
/*     */           
/* 384 */           inEncryptedBuf.flip();
/*     */           try {
/* 386 */             result = doUnwrap(inEncryptedBuf, inPlainBuf);
/*     */           } finally {
/* 388 */             inEncryptedBuf.compact();
/*     */           } 
/*     */           
/*     */           try {
/* 392 */             if (!inEncryptedBuf.hasRemaining() && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
/* 393 */               throw new SSLException("Input buffer is full");
/*     */             }
/*     */           } finally {
/*     */             
/* 397 */             if (inEncryptedBuf.position() == 0) {
/* 398 */               this.inEncrypted.release();
/*     */             }
/*     */           } 
/*     */           
/* 402 */           if (this.status.compareTo((Enum)IOSession.Status.CLOSING) >= 0) {
/* 403 */             this.inPlain.release();
/*     */           }
/* 405 */           if (result.getStatus() != SSLEngineResult.Status.OK) {
/* 406 */             handshaking = false;
/*     */           }
/*     */         
/*     */         case NEED_TASK:
/* 410 */           doRunTask();
/*     */         
/*     */         case NOT_HANDSHAKING:
/* 413 */           handshaking = false;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 421 */     if (result != null && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED) {
/* 422 */       this.handshakeStateRef.set(TLSHandShakeState.COMPLETE);
/* 423 */       this.session.setSocketTimeout(this.socketTimeout);
/* 424 */       if (this.verifier != null) {
/* 425 */         this.tlsDetails = this.verifier.verify(this.targetEndpoint, this.sslEngine);
/*     */       }
/* 427 */       if (this.tlsDetails == null) {
/* 428 */         SSLSession sslSession = this.sslEngine.getSession();
/* 429 */         String applicationProtocol = this.sslEngine.getApplicationProtocol();
/* 430 */         this.tlsDetails = new TlsDetails(sslSession, applicationProtocol);
/*     */       } 
/*     */       
/* 433 */       ensureHandler().connected(protocolSession);
/*     */       
/* 435 */       if (this.sessionStartCallback != null) {
/* 436 */         this.sessionStartCallback.execute(this);
/*     */       }
/* 438 */       FutureCallback<SSLSession> resultCallback = this.handshakeCallbackRef.getAndSet(null);
/* 439 */       if (resultCallback != null) {
/* 440 */         resultCallback.completed(this.sslEngine.getSession());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateEventMask() {
/* 446 */     this.session.getLock().lock();
/*     */     
/*     */     try {
/* 449 */       if (this.status == IOSession.Status.ACTIVE && (this.endOfStream || this.sslEngine
/* 450 */         .isInboundDone())) {
/* 451 */         this.status = IOSession.Status.CLOSING;
/* 452 */         FutureCallback<SSLSession> resultCallback = this.handshakeCallbackRef.getAndSet(null);
/* 453 */         if (resultCallback != null) {
/* 454 */           resultCallback.failed(new SSLHandshakeException("TLS handshake failed"));
/*     */         }
/*     */       } 
/* 457 */       if (this.status == IOSession.Status.CLOSING && !this.outEncrypted.hasData()) {
/* 458 */         this.sslEngine.closeOutbound();
/* 459 */         this.outboundClosedCount.incrementAndGet();
/*     */       } 
/* 461 */       if (this.status == IOSession.Status.CLOSING && this.sslEngine.isOutboundDone() && (this.endOfStream || this.sslEngine
/* 462 */         .isInboundDone())) {
/* 463 */         this.status = IOSession.Status.CLOSED;
/*     */       }
/*     */       
/* 466 */       if (this.status.compareTo((Enum)IOSession.Status.CLOSING) <= 0 && this.endOfStream && this.sslEngine
/* 467 */         .getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
/* 468 */         this.status = IOSession.Status.CLOSED;
/*     */       }
/* 470 */       if (this.status == IOSession.Status.CLOSED) {
/* 471 */         this.session.close();
/* 472 */         if (this.sessionEndCallback != null) {
/* 473 */           this.sessionEndCallback.execute(this);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/* 478 */       if (this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/* 479 */         doRunTask();
/*     */       }
/*     */       
/* 482 */       int oldMask = this.session.getEventMask();
/* 483 */       int newMask = oldMask;
/* 484 */       switch (this.sslEngine.getHandshakeStatus()) {
/*     */         case NEED_WRAP:
/* 486 */           newMask = 5;
/*     */           break;
/*     */         case NEED_UNWRAP:
/* 489 */           newMask = 1;
/*     */           break;
/*     */         case NOT_HANDSHAKING:
/* 492 */           newMask = this.appEventMask;
/*     */           break;
/*     */       } 
/*     */       
/* 496 */       if (this.endOfStream && !this.inPlain.hasData()) {
/* 497 */         newMask &= 0xFFFFFFFE;
/* 498 */       } else if (this.status == IOSession.Status.CLOSING) {
/* 499 */         newMask |= 0x1;
/*     */       } 
/*     */ 
/*     */       
/* 503 */       if (this.outEncrypted.hasData()) {
/* 504 */         newMask |= 0x4;
/* 505 */       } else if (this.sslEngine.isOutboundDone()) {
/* 506 */         newMask &= 0xFFFFFFFB;
/*     */       } 
/*     */ 
/*     */       
/* 510 */       if (oldMask != newMask) {
/* 511 */         this.session.setEventMask(newMask);
/*     */       }
/*     */     } finally {
/* 514 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int sendEncryptedData() throws IOException {
/* 519 */     this.session.getLock().lock();
/*     */     try {
/* 521 */       if (!this.outEncrypted.hasData())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 526 */         return this.session.write(EMPTY_BUFFER);
/*     */       }
/*     */ 
/*     */       
/* 530 */       ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();
/*     */ 
/*     */ 
/*     */       
/* 534 */       if (this.status == IOSession.Status.CLOSED) {
/* 535 */         outEncryptedBuf.clear();
/*     */       }
/*     */ 
/*     */       
/* 539 */       int bytesWritten = 0;
/* 540 */       if (outEncryptedBuf.position() > 0) {
/* 541 */         outEncryptedBuf.flip();
/*     */         try {
/* 543 */           bytesWritten = this.session.write(outEncryptedBuf);
/*     */         } finally {
/* 545 */           outEncryptedBuf.compact();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 550 */       if (outEncryptedBuf.position() == 0) {
/* 551 */         this.outEncrypted.release();
/*     */       }
/* 553 */       return bytesWritten;
/*     */     } finally {
/* 555 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int receiveEncryptedData() throws IOException {
/* 560 */     if (this.endOfStream) {
/* 561 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 565 */     ByteBuffer inEncryptedBuf = this.inEncrypted.acquire();
/*     */ 
/*     */     
/* 568 */     int bytesRead = this.session.read(inEncryptedBuf);
/*     */ 
/*     */     
/* 571 */     if (inEncryptedBuf.position() == 0) {
/* 572 */       this.inEncrypted.release();
/*     */     }
/* 574 */     if (bytesRead == -1) {
/* 575 */       this.endOfStream = true;
/*     */     }
/* 577 */     return bytesRead;
/*     */   }
/*     */   
/*     */   private void decryptData(IOSession protocolSession) throws IOException {
/* 581 */     SSLEngineResult.HandshakeStatus handshakeStatus = this.sslEngine.getHandshakeStatus();
/* 582 */     if ((handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING || handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED) && this.inEncrypted
/* 583 */       .hasData()) {
/* 584 */       ByteBuffer inEncryptedBuf = this.inEncrypted.acquire();
/* 585 */       inEncryptedBuf.flip();
/*     */       try {
/* 587 */         while (inEncryptedBuf.hasRemaining()) {
/* 588 */           ByteBuffer inPlainBuf = this.inPlain.acquire();
/*     */           
/* 590 */           try { SSLEngineResult result = doUnwrap(inEncryptedBuf, inPlainBuf);
/* 591 */             if (!inEncryptedBuf.hasRemaining() && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
/* 592 */               throw new SSLException("Unable to complete SSL handshake");
/*     */             }
/* 594 */             if (this.sslEngine.isInboundDone()) {
/* 595 */               this.endOfStream = true;
/*     */             }
/* 597 */             if (inPlainBuf.position() > 0) {
/* 598 */               inPlainBuf.flip();
/*     */               try {
/* 600 */                 ensureHandler().inputReady(protocolSession, inPlainBuf.hasRemaining() ? inPlainBuf : null);
/*     */               } finally {
/* 602 */                 inPlainBuf.clear();
/*     */               } 
/*     */             } 
/* 605 */             if (result.getStatus() != SSLEngineResult.Status.OK)
/* 606 */             { if (result.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW && this.endOfStream) {
/* 607 */                 throw new SSLException("Unable to decrypt incoming data due to unexpected end of stream");
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 612 */               this.inPlain.release(); break; }  } finally { this.inPlain.release(); }
/*     */         
/*     */         } 
/*     */       } finally {
/* 616 */         inEncryptedBuf.compact();
/*     */         
/* 618 */         if (inEncryptedBuf.position() == 0) {
/* 619 */           this.inEncrypted.release();
/*     */         }
/*     */       } 
/*     */     } 
/* 623 */     if (this.endOfStream && !this.inEncrypted.hasData()) {
/* 624 */       ensureHandler().inputReady(protocolSession, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void encryptData(IOSession protocolSession) throws IOException {
/*     */     boolean appReady;
/* 630 */     this.session.getLock().lock();
/*     */ 
/*     */     
/*     */     try {
/* 634 */       appReady = ((this.appEventMask & 0x4) > 0 && this.status == IOSession.Status.ACTIVE && this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING);
/*     */     } finally {
/* 636 */       this.session.getLock().unlock();
/*     */     } 
/* 638 */     if (appReady) {
/* 639 */       ensureHandler().outputReady(protocolSession);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/* 645 */     Args.notNull(src, "Byte buffer");
/* 646 */     this.session.getLock().lock();
/*     */     try {
/* 648 */       if (this.status != IOSession.Status.ACTIVE) {
/* 649 */         throw new ClosedChannelException();
/*     */       }
/* 651 */       if (this.handshakeStateRef.get() == TLSHandShakeState.READY) {
/* 652 */         return 0;
/*     */       }
/* 654 */       ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();
/* 655 */       SSLEngineResult result = doWrap(src, outEncryptedBuf);
/* 656 */       return result.bytesConsumed();
/*     */     } finally {
/* 658 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) {
/* 664 */     return this.endOfStream ? -1 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/* 669 */     return this.session.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public Lock getLock() {
/* 674 */     return this.session.getLock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgrade(IOEventHandler handler) {
/* 679 */     this.session.upgrade(handler);
/*     */   }
/*     */   
/*     */   public TlsDetails getTlsDetails() {
/* 683 */     return this.tlsDetails;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 688 */     return (this.status == IOSession.Status.ACTIVE && this.session.isOpen());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 693 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 698 */     this.session.getLock().lock();
/*     */     try {
/* 700 */       if (closeMode == CloseMode.GRACEFUL) {
/* 701 */         if (this.status.compareTo((Enum)IOSession.Status.CLOSING) >= 0) {
/*     */           return;
/*     */         }
/* 704 */         this.status = IOSession.Status.CLOSING;
/* 705 */         if (this.session.getSocketTimeout().isDisabled()) {
/* 706 */           this.session.setSocketTimeout(Timeout.ofMilliseconds(1000L));
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 713 */           updateEventMask();
/* 714 */         } catch (CancelledKeyException ex) {
/* 715 */           this.session.close(CloseMode.GRACEFUL);
/* 716 */         } catch (Exception ex) {
/* 717 */           this.session.close(CloseMode.IMMEDIATE);
/*     */         } 
/*     */       } else {
/* 720 */         if (this.status == IOSession.Status.CLOSED) {
/*     */           return;
/*     */         }
/* 723 */         this.inEncrypted.release();
/* 724 */         this.outEncrypted.release();
/* 725 */         this.inPlain.release();
/*     */         
/* 727 */         this.status = IOSession.Status.CLOSED;
/* 728 */         this.session.close(closeMode);
/*     */       } 
/*     */     } finally {
/* 731 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IOSession.Status getStatus() {
/* 737 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public void enqueue(Command command, Command.Priority priority) {
/* 742 */     this.session.getLock().lock();
/*     */     try {
/* 744 */       this.session.enqueue(command, priority);
/* 745 */       setEvent(4);
/*     */     } finally {
/* 747 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCommands() {
/* 753 */     return this.session.hasCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public Command poll() {
/* 758 */     return this.session.poll();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteChannel channel() {
/* 763 */     return this.session.channel();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 768 */     return this.session.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 773 */     return this.session.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEventMask() {
/* 778 */     this.session.getLock().lock();
/*     */     try {
/* 780 */       return this.appEventMask;
/*     */     } finally {
/* 782 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEventMask(int ops) {
/* 788 */     this.session.getLock().lock();
/*     */     try {
/* 790 */       this.appEventMask = ops;
/* 791 */       updateEventMask();
/*     */     } finally {
/* 793 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEvent(int op) {
/* 799 */     this.session.getLock().lock();
/*     */     try {
/* 801 */       this.appEventMask |= op;
/* 802 */       updateEventMask();
/*     */     } finally {
/* 804 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearEvent(int op) {
/* 810 */     this.session.getLock().lock();
/*     */     try {
/* 812 */       this.appEventMask &= op ^ 0xFFFFFFFF;
/* 813 */       updateEventMask();
/*     */     } finally {
/* 815 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/* 821 */     return this.session.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(Timeout timeout) {
/* 826 */     this.socketTimeout = timeout;
/* 827 */     if (this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED) {
/* 828 */       this.session.setSocketTimeout(timeout);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateReadTime() {
/* 834 */     this.session.updateReadTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWriteTime() {
/* 839 */     this.session.updateWriteTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastReadTime() {
/* 844 */     return this.session.getLastReadTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastWriteTime() {
/* 849 */     return this.session.getLastWriteTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastEventTime() {
/* 854 */     return this.session.getLastEventTime();
/*     */   }
/*     */   
/*     */   private static void formatOps(StringBuilder buffer, int ops) {
/* 858 */     if ((ops & 0x1) > 0) {
/* 859 */       buffer.append('r');
/*     */     }
/* 861 */     if ((ops & 0x4) > 0) {
/* 862 */       buffer.append('w');
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 868 */     this.session.getLock().lock();
/*     */     try {
/* 870 */       StringBuilder buffer = new StringBuilder();
/* 871 */       buffer.append(this.session);
/* 872 */       buffer.append("[");
/* 873 */       buffer.append(this.status);
/* 874 */       buffer.append("][");
/* 875 */       formatOps(buffer, this.appEventMask);
/* 876 */       buffer.append("][");
/* 877 */       buffer.append(this.sslEngine.getHandshakeStatus());
/* 878 */       if (this.sslEngine.isInboundDone()) {
/* 879 */         buffer.append("][inbound done][");
/*     */       }
/* 881 */       if (this.sslEngine.isOutboundDone()) {
/* 882 */         buffer.append("][outbound done][");
/*     */       }
/* 884 */       if (this.endOfStream) {
/* 885 */         buffer.append("][EOF][");
/*     */       }
/* 887 */       buffer.append("][");
/* 888 */       buffer.append(!this.inEncrypted.hasData() ? 0 : this.inEncrypted.acquire().position());
/* 889 */       buffer.append("][");
/* 890 */       buffer.append(!this.inPlain.hasData() ? 0 : this.inPlain.acquire().position());
/* 891 */       buffer.append("][");
/* 892 */       buffer.append(!this.outEncrypted.hasData() ? 0 : this.outEncrypted.acquire().position());
/* 893 */       buffer.append("]");
/* 894 */       return buffer.toString();
/*     */     } finally {
/* 896 */       this.session.getLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ssl/SSLIOSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */