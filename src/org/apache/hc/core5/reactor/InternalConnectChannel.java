/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.io.SocketTimeoutExceptionFactory;
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
/*     */ final class InternalConnectChannel
/*     */   extends InternalChannel
/*     */ {
/*     */   private final SelectionKey key;
/*     */   private final SocketChannel socketChannel;
/*     */   private final IOSessionRequest sessionRequest;
/*     */   private final long creationTimeMillis;
/*     */   private final InternalDataChannelFactory dataChannelFactory;
/*     */   
/*     */   InternalConnectChannel(SelectionKey key, SocketChannel socketChannel, IOSessionRequest sessionRequest, InternalDataChannelFactory dataChannelFactory) {
/*  53 */     this.key = key;
/*  54 */     this.socketChannel = socketChannel;
/*  55 */     this.sessionRequest = sessionRequest;
/*  56 */     this.creationTimeMillis = System.currentTimeMillis();
/*  57 */     this.dataChannelFactory = dataChannelFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   void onIOEvent(int readyOps) throws IOException {
/*  62 */     if ((readyOps & 0x8) != 0) {
/*  63 */       if (this.socketChannel.isConnectionPending()) {
/*  64 */         this.socketChannel.finishConnect();
/*     */       }
/*     */       
/*  67 */       long now = System.currentTimeMillis();
/*  68 */       if (checkTimeout(now)) {
/*  69 */         InternalDataChannel dataChannel = this.dataChannelFactory.create(this.key, this.socketChannel, this.sessionRequest.remoteEndpoint, this.sessionRequest.attachment);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  74 */         this.key.attach(dataChannel);
/*  75 */         this.sessionRequest.completed(dataChannel);
/*  76 */         dataChannel.handleIOEvent(8);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Timeout getTimeout() {
/*  83 */     return this.sessionRequest.timeout;
/*     */   }
/*     */ 
/*     */   
/*     */   long getLastEventTime() {
/*  88 */     return this.creationTimeMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   void onTimeout(Timeout timeout) throws IOException {
/*  93 */     this.sessionRequest.failed(SocketTimeoutExceptionFactory.create(timeout));
/*  94 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   void onException(Exception cause) {
/*  99 */     this.sessionRequest.failed(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 104 */     this.key.cancel();
/* 105 */     this.socketChannel.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 110 */     this.key.cancel();
/* 111 */     Closer.closeQuietly(this.socketChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return this.sessionRequest.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/InternalConnectChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */