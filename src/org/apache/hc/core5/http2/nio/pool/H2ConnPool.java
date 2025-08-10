/*     */ package org.apache.hc.core5.http2.nio.pool;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.impl.DefaultAddressResolver;
/*     */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http2.nio.AsyncPingHandler;
/*     */ import org.apache.hc.core5.http2.nio.command.PingCommand;
/*     */ import org.apache.hc.core5.http2.nio.support.BasicPingHandler;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.AbstractIOSessionPool;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public final class H2ConnPool
/*     */   extends AbstractIOSessionPool<HttpHost>
/*     */ {
/*     */   private final ConnectionInitiator connectionInitiator;
/*     */   private final Resolver<HttpHost, InetSocketAddress> addressResolver;
/*     */   private final TlsStrategy tlsStrategy;
/*  67 */   private volatile TimeValue validateAfterInactivity = TimeValue.NEG_ONE_MILLISECOND;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public H2ConnPool(ConnectionInitiator connectionInitiator, Resolver<HttpHost, InetSocketAddress> addressResolver, TlsStrategy tlsStrategy) {
/*  74 */     this.connectionInitiator = (ConnectionInitiator)Args.notNull(connectionInitiator, "Connection initiator");
/*  75 */     this.addressResolver = (addressResolver != null) ? addressResolver : (Resolver<HttpHost, InetSocketAddress>)DefaultAddressResolver.INSTANCE;
/*  76 */     this.tlsStrategy = tlsStrategy;
/*     */   }
/*     */   
/*     */   public TimeValue getValidateAfterInactivity() {
/*  80 */     return this.validateAfterInactivity;
/*     */   }
/*     */   
/*     */   public void setValidateAfterInactivity(TimeValue timeValue) {
/*  84 */     this.validateAfterInactivity = timeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeSession(IOSession ioSession, CloseMode closeMode) {
/*  91 */     if (closeMode == CloseMode.GRACEFUL) {
/*  92 */       ioSession.enqueue((Command)ShutdownCommand.GRACEFUL, Command.Priority.NORMAL);
/*     */     } else {
/*  94 */       ioSession.close(closeMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<IOSession> connectSession(final HttpHost namedEndpoint, final Timeout connectTimeout, final FutureCallback<IOSession> callback) {
/* 103 */     InetSocketAddress remoteAddress = (InetSocketAddress)this.addressResolver.resolve(namedEndpoint);
/* 104 */     return this.connectionInitiator.connect((NamedEndpoint)namedEndpoint, remoteAddress, null, connectTimeout, null, (FutureCallback)new CallbackContribution<IOSession>(callback)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void completed(final IOSession ioSession)
/*     */           {
/* 114 */             if (H2ConnPool.this.tlsStrategy != null && URIScheme.HTTPS
/* 115 */               .same(namedEndpoint.getSchemeName()) && ioSession instanceof TransportSecurityLayer) {
/*     */               
/* 117 */               H2ConnPool.this.tlsStrategy.upgrade((TransportSecurityLayer)ioSession, (NamedEndpoint)namedEndpoint, null, connectTimeout, (FutureCallback)new CallbackContribution<TransportSecurityLayer>(callback)
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/*     */                     public void completed(TransportSecurityLayer transportSecurityLayer)
/*     */                     {
/* 126 */                       callback.completed(ioSession);
/*     */                     }
/*     */                   });
/*     */               
/* 130 */               ioSession.setSocketTimeout(connectTimeout);
/*     */             } else {
/* 132 */               callback.completed(ioSession);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateSession(IOSession ioSession, Callback<Boolean> callback) {
/* 143 */     if (ioSession.isOpen()) {
/* 144 */       TimeValue timeValue = this.validateAfterInactivity;
/* 145 */       if (TimeValue.isNonNegative(timeValue)) {
/* 146 */         long lastAccessTime = Math.min(ioSession.getLastReadTime(), ioSession.getLastWriteTime());
/* 147 */         long deadline = lastAccessTime + timeValue.toMilliseconds();
/* 148 */         if (deadline <= System.currentTimeMillis()) {
/* 149 */           Timeout socketTimeoutMillis = ioSession.getSocketTimeout();
/* 150 */           ioSession.enqueue((Command)new PingCommand((AsyncPingHandler)new BasicPingHandler(result -> { ioSession.setSocketTimeout(socketTimeoutMillis); callback.execute(result); })), Command.Priority.NORMAL);
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 157 */       callback.execute(Boolean.valueOf(true));
/*     */     } else {
/* 159 */       callback.execute(Boolean.valueOf(false));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/nio/pool/H2ConnPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */