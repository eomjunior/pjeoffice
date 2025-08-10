/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLServerSocket;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.http.ExceptionListener;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultBHttpServerConnection;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultBHttpServerConnectionFactory;
/*     */ import org.apache.hc.core5.http.impl.io.HttpService;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.SocketConfig;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
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
/*     */ public class HttpServer
/*     */   implements ModalCloseable
/*     */ {
/*     */   private final int port;
/*     */   private final InetAddress ifAddress;
/*     */   private final SocketConfig socketConfig;
/*     */   private final ServerSocketFactory serverSocketFactory;
/*     */   private final HttpService httpService;
/*     */   private final HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
/*     */   private final Callback<SSLParameters> sslSetupHandler;
/*     */   private final ExceptionListener exceptionListener;
/*     */   private final ThreadPoolExecutor listenerExecutorService;
/*     */   private final ThreadGroup workerThreads;
/*     */   private final WorkerPoolExecutor workerExecutorService;
/*     */   private final AtomicReference<Status> status;
/*     */   private volatile ServerSocket serverSocket;
/*     */   private volatile RequestListener requestListener;
/*     */   
/*     */   enum Status
/*     */   {
/*  69 */     READY, ACTIVE, STOPPING;
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
/*     */   @Internal
/*     */   public HttpServer(int port, HttpService httpService, InetAddress ifAddress, SocketConfig socketConfig, ServerSocketFactory serverSocketFactory, HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory, Callback<SSLParameters> sslSetupHandler, ExceptionListener exceptionListener) {
/*  97 */     this.port = Args.notNegative(port, "Port value is negative");
/*  98 */     this.httpService = (HttpService)Args.notNull(httpService, "HTTP service");
/*  99 */     this.ifAddress = ifAddress;
/* 100 */     this.socketConfig = (socketConfig != null) ? socketConfig : SocketConfig.DEFAULT;
/* 101 */     this.serverSocketFactory = (serverSocketFactory != null) ? serverSocketFactory : ServerSocketFactory.getDefault();
/* 102 */     this.connectionFactory = (connectionFactory != null) ? connectionFactory : (HttpConnectionFactory<? extends DefaultBHttpServerConnection>)new DefaultBHttpServerConnectionFactory((this.serverSocketFactory instanceof javax.net.ssl.SSLServerSocketFactory) ? URIScheme.HTTPS.id : URIScheme.HTTP.id, Http1Config.DEFAULT, CharCodingConfig.DEFAULT);
/*     */ 
/*     */ 
/*     */     
/* 106 */     this.sslSetupHandler = sslSetupHandler;
/* 107 */     this.exceptionListener = (exceptionListener != null) ? exceptionListener : ExceptionListener.NO_OP;
/* 108 */     this.listenerExecutorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), (ThreadFactory)new DefaultThreadFactory("HTTP-listener-" + this.port));
/*     */ 
/*     */ 
/*     */     
/* 112 */     this.workerThreads = new ThreadGroup("HTTP-workers");
/* 113 */     this.workerExecutorService = new WorkerPoolExecutor(0, 2147483647, 1L, TimeUnit.SECONDS, new SynchronousQueue<>(), (ThreadFactory)new DefaultThreadFactory("HTTP-worker", this.workerThreads, true));
/*     */ 
/*     */ 
/*     */     
/* 117 */     this.status = new AtomicReference<>(Status.READY);
/*     */   }
/*     */   
/*     */   public InetAddress getInetAddress() {
/* 121 */     ServerSocket localSocket = this.serverSocket;
/* 122 */     if (localSocket != null) {
/* 123 */       return localSocket.getInetAddress();
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public int getLocalPort() {
/* 129 */     ServerSocket localSocket = this.serverSocket;
/* 130 */     if (localSocket != null) {
/* 131 */       return localSocket.getLocalPort();
/*     */     }
/* 133 */     return -1;
/*     */   }
/*     */   
/*     */   public void start() throws IOException {
/* 137 */     if (this.status.compareAndSet(Status.READY, Status.ACTIVE)) {
/* 138 */       this.serverSocket = this.serverSocketFactory.createServerSocket(this.port, this.socketConfig
/* 139 */           .getBacklogSize(), this.ifAddress);
/* 140 */       this.serverSocket.setReuseAddress(this.socketConfig.isSoReuseAddress());
/* 141 */       if (this.socketConfig.getRcvBufSize() > 0) {
/* 142 */         this.serverSocket.setReceiveBufferSize(this.socketConfig.getRcvBufSize());
/*     */       }
/* 144 */       if (this.sslSetupHandler != null && this.serverSocket instanceof SSLServerSocket) {
/* 145 */         SSLServerSocket sslServerSocket = (SSLServerSocket)this.serverSocket;
/* 146 */         SSLParameters sslParameters = sslServerSocket.getSSLParameters();
/* 147 */         this.sslSetupHandler.execute(sslParameters);
/* 148 */         sslServerSocket.setSSLParameters(sslParameters);
/*     */       } 
/* 150 */       this.requestListener = new RequestListener(this.socketConfig, this.serverSocket, this.httpService, (HttpConnectionFactory)this.connectionFactory, this.exceptionListener, this.workerExecutorService);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 157 */       this.listenerExecutorService.execute(this.requestListener);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 162 */     if (this.status.compareAndSet(Status.ACTIVE, Status.STOPPING)) {
/* 163 */       this.listenerExecutorService.shutdownNow();
/* 164 */       this.workerExecutorService.shutdown();
/* 165 */       RequestListener local = this.requestListener;
/* 166 */       if (local != null) {
/*     */         try {
/* 168 */           local.terminate();
/* 169 */         } catch (IOException ex) {
/* 170 */           this.exceptionListener.onError(ex);
/*     */         } 
/*     */       }
/* 173 */       this.workerThreads.interrupt();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initiateShutdown() {
/* 178 */     stop();
/*     */   }
/*     */   
/*     */   public void awaitTermination(TimeValue waitTime) throws InterruptedException {
/* 182 */     Args.notNull(waitTime, "Wait time");
/* 183 */     this.workerExecutorService.awaitTermination(waitTime.getDuration(), waitTime.getTimeUnit());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 188 */     close(closeMode, Timeout.ofSeconds(5L));
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
/*     */   public void close(CloseMode closeMode, Timeout timeout) {
/* 201 */     initiateShutdown();
/* 202 */     if (closeMode == CloseMode.GRACEFUL) {
/*     */       try {
/* 204 */         awaitTermination((TimeValue)timeout);
/* 205 */       } catch (InterruptedException ex) {
/* 206 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
/* 209 */     Set<Worker> workers = this.workerExecutorService.getWorkers();
/* 210 */     for (Worker worker : workers) {
/* 211 */       Closer.close((ModalCloseable)worker.getConnection(), CloseMode.GRACEFUL);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 217 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/HttpServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */