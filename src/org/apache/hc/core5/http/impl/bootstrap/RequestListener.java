/*    */ package org.apache.hc.core5.http.impl.bootstrap;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import org.apache.hc.core5.http.ExceptionListener;
/*    */ import org.apache.hc.core5.http.impl.io.HttpService;
/*    */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*    */ import org.apache.hc.core5.http.io.HttpServerConnection;
/*    */ import org.apache.hc.core5.http.io.SocketConfig;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class RequestListener
/*    */   implements Runnable
/*    */ {
/*    */   private final SocketConfig socketConfig;
/*    */   private final ServerSocket serverSocket;
/*    */   private final HttpService httpService;
/*    */   private final HttpConnectionFactory<? extends HttpServerConnection> connectionFactory;
/*    */   private final ExceptionListener exceptionListener;
/*    */   private final ExecutorService executorService;
/*    */   private final AtomicBoolean terminated;
/*    */   
/*    */   public RequestListener(SocketConfig socketConfig, ServerSocket serversocket, HttpService httpService, HttpConnectionFactory<? extends HttpServerConnection> connectionFactory, ExceptionListener exceptionListener, ExecutorService executorService) {
/* 58 */     this.socketConfig = socketConfig;
/* 59 */     this.serverSocket = serversocket;
/* 60 */     this.connectionFactory = connectionFactory;
/* 61 */     this.httpService = httpService;
/* 62 */     this.exceptionListener = exceptionListener;
/* 63 */     this.executorService = executorService;
/* 64 */     this.terminated = new AtomicBoolean(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 70 */       while (!isTerminated() && !Thread.interrupted()) {
/* 71 */         Socket socket = this.serverSocket.accept();
/* 72 */         socket.setSoTimeout(this.socketConfig.getSoTimeout().toMillisecondsIntBound());
/* 73 */         socket.setKeepAlive(this.socketConfig.isSoKeepAlive());
/* 74 */         socket.setTcpNoDelay(this.socketConfig.isTcpNoDelay());
/* 75 */         if (this.socketConfig.getRcvBufSize() > 0) {
/* 76 */           socket.setReceiveBufferSize(this.socketConfig.getRcvBufSize());
/*    */         }
/* 78 */         if (this.socketConfig.getSndBufSize() > 0) {
/* 79 */           socket.setSendBufferSize(this.socketConfig.getSndBufSize());
/*    */         }
/* 81 */         if (this.socketConfig.getSoLinger().toSeconds() >= 0L) {
/* 82 */           socket.setSoLinger(true, this.socketConfig.getSoLinger().toSecondsIntBound());
/*    */         }
/* 84 */         HttpServerConnection conn = (HttpServerConnection)this.connectionFactory.createConnection(socket);
/* 85 */         Worker worker = new Worker(this.httpService, conn, this.exceptionListener);
/* 86 */         this.executorService.execute(worker);
/*    */       } 
/* 88 */     } catch (Exception ex) {
/* 89 */       this.exceptionListener.onError(ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isTerminated() {
/* 94 */     return this.terminated.get();
/*    */   }
/*    */   
/*    */   public void terminate() throws IOException {
/* 98 */     if (this.terminated.compareAndSet(false, true))
/* 99 */       this.serverSocket.close(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/RequestListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */