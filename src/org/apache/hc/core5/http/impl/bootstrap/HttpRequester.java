/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.ConnectionRequestTimeoutException;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.DefaultAddressResolver;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultBHttpClientConnectionFactory;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.io.EofSensorInputStream;
/*     */ import org.apache.hc.core5.http.io.EofSensorWatcher;
/*     */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*     */ import org.apache.hc.core5.http.io.HttpClientResponseHandler;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.HttpResponseInformationCallback;
/*     */ import org.apache.hc.core5.http.io.SocketConfig;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.io.entity.HttpEntityWrapper;
/*     */ import org.apache.hc.core5.http.io.ssl.SSLSessionVerifier;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.pool.ConnPoolControl;
/*     */ import org.apache.hc.core5.pool.ManagedConnPool;
/*     */ import org.apache.hc.core5.pool.PoolEntry;
/*     */ import org.apache.hc.core5.pool.PoolStats;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ public class HttpRequester
/*     */   implements ConnPoolControl<HttpHost>, ModalCloseable
/*     */ {
/*     */   private final HttpRequestExecutor requestExecutor;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final ManagedConnPool<HttpHost, HttpClientConnection> connPool;
/*     */   private final SocketConfig socketConfig;
/*     */   private final HttpConnectionFactory<? extends HttpClientConnection> connectFactory;
/*     */   private final SSLSocketFactory sslSocketFactory;
/*     */   private final Callback<SSLParameters> sslSetupHandler;
/*     */   private final SSLSessionVerifier sslSessionVerifier;
/*     */   private final Resolver<HttpHost, InetSocketAddress> addressResolver;
/*     */   
/*     */   @Internal
/*     */   public HttpRequester(HttpRequestExecutor requestExecutor, HttpProcessor httpProcessor, ManagedConnPool<HttpHost, HttpClientConnection> connPool, SocketConfig socketConfig, HttpConnectionFactory<? extends HttpClientConnection> connectFactory, SSLSocketFactory sslSocketFactory, Callback<SSLParameters> sslSetupHandler, SSLSessionVerifier sslSessionVerifier, Resolver<HttpHost, InetSocketAddress> addressResolver) {
/* 123 */     this.requestExecutor = (HttpRequestExecutor)Args.notNull(requestExecutor, "Request executor");
/* 124 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/* 125 */     this.connPool = (ManagedConnPool<HttpHost, HttpClientConnection>)Args.notNull(connPool, "Connection pool");
/* 126 */     this.socketConfig = (socketConfig != null) ? socketConfig : SocketConfig.DEFAULT;
/* 127 */     this.connectFactory = (connectFactory != null) ? connectFactory : (HttpConnectionFactory<? extends HttpClientConnection>)new DefaultBHttpClientConnectionFactory(Http1Config.DEFAULT, CharCodingConfig.DEFAULT);
/*     */     
/* 129 */     this.sslSocketFactory = (sslSocketFactory != null) ? sslSocketFactory : (SSLSocketFactory)SSLSocketFactory.getDefault();
/* 130 */     this.sslSetupHandler = sslSetupHandler;
/* 131 */     this.sslSessionVerifier = sslSessionVerifier;
/* 132 */     this.addressResolver = (addressResolver != null) ? addressResolver : (Resolver<HttpHost, InetSocketAddress>)DefaultAddressResolver.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 137 */     return this.connPool.getTotalStats();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(HttpHost route) {
/* 142 */     return this.connPool.getStats(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 147 */     this.connPool.setMaxTotal(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 152 */     return this.connPool.getMaxTotal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 157 */     this.connPool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 162 */     return this.connPool.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(HttpHost route, int max) {
/* 167 */     this.connPool.setMaxPerRoute(route, max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(HttpHost route) {
/* 172 */     return this.connPool.getMaxPerRoute(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdle(TimeValue idleTime) {
/* 177 */     this.connPool.closeIdle(idleTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 182 */     this.connPool.closeExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<HttpHost> getRoutes() {
/* 187 */     return this.connPool.getRoutes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(HttpClientConnection connection, ClassicHttpRequest request, HttpResponseInformationCallback informationCallback, HttpContext context) throws HttpException, IOException {
/* 195 */     Args.notNull(connection, "HTTP connection");
/* 196 */     Args.notNull(request, "HTTP request");
/* 197 */     Args.notNull(context, "HTTP context");
/* 198 */     if (!connection.isOpen()) {
/* 199 */       throw new ConnectionClosedException();
/*     */     }
/* 201 */     this.requestExecutor.preProcess(request, this.httpProcessor, context);
/* 202 */     ClassicHttpResponse response = this.requestExecutor.execute(request, connection, informationCallback, context);
/* 203 */     this.requestExecutor.postProcess(response, this.httpProcessor, context);
/* 204 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(HttpClientConnection connection, ClassicHttpRequest request, HttpContext context) throws HttpException, IOException {
/* 211 */     return execute(connection, request, (HttpResponseInformationCallback)null, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keepAlive(HttpClientConnection connection, ClassicHttpRequest request, ClassicHttpResponse response, HttpContext context) throws IOException {
/* 219 */     boolean keepAlive = this.requestExecutor.keepAlive(request, response, connection, context);
/* 220 */     if (!keepAlive) {
/* 221 */       connection.close();
/*     */     }
/* 223 */     return keepAlive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpClientConnection connection, ClassicHttpRequest request, HttpContext context, HttpClientResponseHandler<T> responseHandler) throws HttpException, IOException {
/* 231 */     try (ClassicHttpResponse response = execute(connection, request, context)) {
/* 232 */       T result = (T)responseHandler.handleResponse(response);
/* 233 */       EntityUtils.consume(response.getEntity());
/* 234 */       boolean keepAlive = this.requestExecutor.keepAlive(request, response, connection, context);
/* 235 */       if (!keepAlive) {
/* 236 */         connection.close();
/*     */       }
/* 238 */       return result;
/* 239 */     } catch (HttpException|IOException|RuntimeException ex) {
/* 240 */       connection.close(CloseMode.IMMEDIATE);
/* 241 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Socket createSocket(HttpHost targetHost) throws IOException {
/*     */     Socket sock;
/* 247 */     if (this.socketConfig.getSocksProxyAddress() != null) {
/* 248 */       sock = new Socket(new Proxy(Proxy.Type.SOCKS, this.socketConfig.getSocksProxyAddress()));
/*     */     } else {
/* 250 */       sock = new Socket();
/*     */     } 
/* 252 */     sock.setSoTimeout(this.socketConfig.getSoTimeout().toMillisecondsIntBound());
/* 253 */     sock.setReuseAddress(this.socketConfig.isSoReuseAddress());
/* 254 */     sock.setTcpNoDelay(this.socketConfig.isTcpNoDelay());
/* 255 */     sock.setKeepAlive(this.socketConfig.isSoKeepAlive());
/* 256 */     if (this.socketConfig.getRcvBufSize() > 0) {
/* 257 */       sock.setReceiveBufferSize(this.socketConfig.getRcvBufSize());
/*     */     }
/* 259 */     if (this.socketConfig.getSndBufSize() > 0) {
/* 260 */       sock.setSendBufferSize(this.socketConfig.getSndBufSize());
/*     */     }
/* 262 */     int linger = this.socketConfig.getSoLinger().toMillisecondsIntBound();
/* 263 */     if (linger >= 0) {
/* 264 */       sock.setSoLinger(true, linger);
/*     */     }
/*     */     
/* 267 */     InetSocketAddress targetAddress = (InetSocketAddress)this.addressResolver.resolve(targetHost);
/*     */ 
/*     */     
/*     */     try {
/* 271 */       AccessController.doPrivileged(() -> {
/*     */             sock.connect(targetAddress, this.socketConfig.getSoTimeout().toMillisecondsIntBound());
/*     */             return null;
/*     */           });
/* 275 */     } catch (PrivilegedActionException e) {
/* 276 */       Asserts.check(e.getCause() instanceof IOException, "method contract violation only checked exceptions are wrapped: " + e
/* 277 */           .getCause());
/*     */       
/* 279 */       throw (IOException)e.getCause();
/*     */     } 
/* 281 */     if (URIScheme.HTTPS.same(targetHost.getSchemeName())) {
/* 282 */       SSLSocket sslSocket = (SSLSocket)this.sslSocketFactory.createSocket(sock, targetHost
/* 283 */           .getHostName(), targetAddress.getPort(), true);
/* 284 */       if (this.sslSetupHandler != null) {
/* 285 */         SSLParameters sslParameters = sslSocket.getSSLParameters();
/* 286 */         this.sslSetupHandler.execute(sslParameters);
/* 287 */         sslSocket.setSSLParameters(sslParameters);
/*     */       } 
/*     */       try {
/* 290 */         sslSocket.startHandshake();
/* 291 */         SSLSession session = sslSocket.getSession();
/* 292 */         if (session == null) {
/* 293 */           throw new SSLHandshakeException("SSL session not available");
/*     */         }
/* 295 */         if (this.sslSessionVerifier != null) {
/* 296 */           this.sslSessionVerifier.verify(targetHost, session);
/*     */         }
/* 298 */       } catch (IOException ex) {
/* 299 */         Closer.closeQuietly(sslSocket);
/* 300 */         throw ex;
/*     */       } 
/* 302 */       return sslSocket;
/*     */     } 
/* 304 */     return sock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(HttpHost targetHost, final ClassicHttpRequest request, HttpResponseInformationCallback informationCallback, Timeout connectTimeout, final HttpContext context) throws HttpException, IOException {
/*     */     PoolEntry<HttpHost, HttpClientConnection> poolEntry;
/* 313 */     Args.notNull(targetHost, "HTTP host");
/* 314 */     Args.notNull(request, "HTTP request");
/* 315 */     Future<PoolEntry<HttpHost, HttpClientConnection>> leaseFuture = this.connPool.lease(targetHost, null, connectTimeout, null);
/*     */     
/* 317 */     Timeout timeout = Timeout.defaultsToDisabled(connectTimeout);
/*     */     try {
/* 319 */       poolEntry = leaseFuture.get(timeout.getDuration(), timeout.getTimeUnit());
/* 320 */     } catch (InterruptedException ex) {
/* 321 */       Thread.currentThread().interrupt();
/* 322 */       throw new InterruptedIOException(ex.getMessage());
/* 323 */     } catch (ExecutionException ex) {
/* 324 */       throw new HttpException("Unexpected failure leasing connection", ex);
/* 325 */     } catch (TimeoutException ex) {
/* 326 */       throw new ConnectionRequestTimeoutException("Connection request timeout");
/*     */     } 
/* 328 */     final PoolEntryHolder connectionHolder = new PoolEntryHolder(poolEntry);
/*     */     try {
/* 330 */       HttpClientConnection connection = (HttpClientConnection)poolEntry.getConnection();
/* 331 */       if (connection == null) {
/* 332 */         Socket socket = createSocket(targetHost);
/* 333 */         connection = (HttpClientConnection)this.connectFactory.createConnection(socket);
/* 334 */         poolEntry.assignConnection((ModalCloseable)connection);
/*     */       } 
/* 336 */       if (request.getAuthority() == null) {
/* 337 */         request.setAuthority(new URIAuthority(targetHost.getHostName(), targetHost.getPort()));
/*     */       }
/* 339 */       final ClassicHttpResponse response = execute(connection, request, informationCallback, context);
/* 340 */       HttpEntity entity = response.getEntity();
/* 341 */       if (entity != null) {
/* 342 */         response.setEntity((HttpEntity)new HttpEntityWrapper(entity)
/*     */             {
/*     */               private void releaseConnection() throws IOException {
/*     */                 try {
/* 346 */                   HttpClientConnection localConn = connectionHolder.getConnection();
/* 347 */                   if (localConn != null && 
/* 348 */                     HttpRequester.this.requestExecutor.keepAlive(request, response, localConn, context)) {
/* 349 */                     if (super.isStreaming()) {
/* 350 */                       Closer.close(super.getContent());
/*     */                     }
/* 352 */                     connectionHolder.releaseConnection();
/*     */                   } 
/*     */                 } finally {
/*     */                   
/* 356 */                   connectionHolder.discardConnection();
/*     */                 } 
/*     */               }
/*     */               
/*     */               private void abortConnection() {
/* 361 */                 connectionHolder.discardConnection();
/*     */               }
/*     */ 
/*     */               
/*     */               public boolean isStreaming() {
/* 366 */                 return true;
/*     */               }
/*     */ 
/*     */               
/*     */               public InputStream getContent() throws IOException {
/* 371 */                 return (InputStream)new EofSensorInputStream(super.getContent(), new EofSensorWatcher()
/*     */                     {
/*     */                       public boolean eofDetected(InputStream wrapped) throws IOException
/*     */                       {
/* 375 */                         HttpRequester.null.this.releaseConnection();
/* 376 */                         return false;
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public boolean streamClosed(InputStream wrapped) throws IOException {
/* 381 */                         HttpRequester.null.this.releaseConnection();
/* 382 */                         return false;
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public boolean streamAbort(InputStream wrapped) throws IOException {
/* 387 */                         HttpRequester.null.this.abortConnection();
/* 388 */                         return false;
/*     */                       }
/*     */                     });
/*     */               }
/*     */ 
/*     */ 
/*     */               
/*     */               public void writeTo(OutputStream outStream) throws IOException {
/*     */                 try {
/* 397 */                   if (outStream != null) {
/* 398 */                     super.writeTo(outStream);
/*     */                   }
/* 400 */                   close();
/* 401 */                 } catch (IOException|RuntimeException ex) {
/* 402 */                   abortConnection();
/*     */                 } 
/*     */               }
/*     */ 
/*     */               
/*     */               public void close() throws IOException {
/* 408 */                 releaseConnection();
/*     */               }
/*     */             });
/*     */       } else {
/*     */         
/* 413 */         HttpClientConnection localConn = connectionHolder.getConnection();
/* 414 */         if (!this.requestExecutor.keepAlive(request, response, localConn, context)) {
/* 415 */           localConn.close();
/*     */         }
/* 417 */         connectionHolder.releaseConnection();
/*     */       } 
/* 419 */       return response;
/* 420 */     } catch (HttpException|IOException|RuntimeException ex) {
/* 421 */       connectionHolder.discardConnection();
/* 422 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(HttpHost targetHost, ClassicHttpRequest request, Timeout connectTimeout, HttpContext context) throws HttpException, IOException {
/* 431 */     return execute(targetHost, request, (HttpResponseInformationCallback)null, connectTimeout, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost targetHost, ClassicHttpRequest request, Timeout connectTimeout, HttpContext context, HttpClientResponseHandler<T> responseHandler) throws HttpException, IOException {
/* 440 */     try (ClassicHttpResponse response = execute(targetHost, request, (HttpResponseInformationCallback)null, connectTimeout, context)) {
/* 441 */       T result = (T)responseHandler.handleResponse(response);
/* 442 */       EntityUtils.consume(response.getEntity());
/* 443 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ConnPoolControl<HttpHost> getConnPoolControl() {
/* 448 */     return (ConnPoolControl)this.connPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 453 */     this.connPool.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 458 */     this.connPool.close();
/*     */   }
/*     */   
/*     */   private class PoolEntryHolder
/*     */   {
/*     */     private final AtomicReference<PoolEntry<HttpHost, HttpClientConnection>> poolEntryRef;
/*     */     
/*     */     PoolEntryHolder(PoolEntry<HttpHost, HttpClientConnection> poolEntry) {
/* 466 */       this.poolEntryRef = new AtomicReference<>(poolEntry);
/*     */     }
/*     */     
/*     */     HttpClientConnection getConnection() {
/* 470 */       PoolEntry<HttpHost, HttpClientConnection> poolEntry = this.poolEntryRef.get();
/* 471 */       return (poolEntry != null) ? (HttpClientConnection)poolEntry.getConnection() : null;
/*     */     }
/*     */     
/*     */     void releaseConnection() {
/* 475 */       PoolEntry<HttpHost, HttpClientConnection> poolEntry = this.poolEntryRef.getAndSet(null);
/* 476 */       if (poolEntry != null) {
/* 477 */         HttpClientConnection connection = (HttpClientConnection)poolEntry.getConnection();
/* 478 */         HttpRequester.this.connPool.release(poolEntry, (connection != null && connection.isOpen()));
/*     */       } 
/*     */     }
/*     */     
/*     */     void discardConnection() {
/* 483 */       PoolEntry<HttpHost, HttpClientConnection> poolEntry = this.poolEntryRef.getAndSet(null);
/* 484 */       if (poolEntry != null) {
/* 485 */         poolEntry.discardConnection(CloseMode.GRACEFUL);
/* 486 */         HttpRequester.this.connPool.release(poolEntry, false);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/HttpRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */