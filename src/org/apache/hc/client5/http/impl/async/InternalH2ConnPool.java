/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http2.nio.pool.H2ConnPool;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.IOSession;
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
/*     */ class InternalH2ConnPool
/*     */   implements ModalCloseable
/*     */ {
/*     */   private final H2ConnPool connPool;
/*     */   private volatile Resolver<HttpHost, ConnectionConfig> connectionConfigResolver;
/*     */   
/*     */   InternalH2ConnPool(ConnectionInitiator connectionInitiator, Resolver<HttpHost, InetSocketAddress> addressResolver, TlsStrategy tlsStrategy) {
/*  55 */     this.connPool = new H2ConnPool(connectionInitiator, addressResolver, tlsStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/*  60 */     this.connPool.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  65 */     this.connPool.close();
/*     */   }
/*     */   
/*     */   private ConnectionConfig resolveConnectionConfig(HttpHost httpHost) {
/*  69 */     Resolver<HttpHost, ConnectionConfig> resolver = this.connectionConfigResolver;
/*  70 */     ConnectionConfig connectionConfig = (resolver != null) ? (ConnectionConfig)resolver.resolve(httpHost) : null;
/*  71 */     return (connectionConfig != null) ? connectionConfig : ConnectionConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<IOSession> getSession(HttpHost endpoint, Timeout connectTimeout, final FutureCallback<IOSession> callback) {
/*  78 */     final ConnectionConfig connectionConfig = resolveConnectionConfig(endpoint);
/*  79 */     return this.connPool.getSession(endpoint, (connectTimeout != null) ? connectTimeout : connectionConfig
/*     */         
/*  81 */         .getConnectTimeout(), (FutureCallback)new CallbackContribution<IOSession>(callback)
/*     */         {
/*     */           
/*     */           public void completed(IOSession ioSession)
/*     */           {
/*  86 */             Timeout socketTimeout = connectionConfig.getSocketTimeout();
/*  87 */             if (socketTimeout != null) {
/*  88 */               ioSession.setSocketTimeout(socketTimeout);
/*     */             }
/*  90 */             callback.completed(ioSession);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdle(TimeValue idleTime) {
/*  97 */     this.connPool.closeIdle(idleTime);
/*     */   }
/*     */   
/*     */   public void setConnectionConfigResolver(Resolver<HttpHost, ConnectionConfig> connectionConfigResolver) {
/* 101 */     this.connectionConfigResolver = connectionConfigResolver;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/InternalH2ConnPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */