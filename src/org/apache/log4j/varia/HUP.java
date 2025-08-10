/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import org.apache.log4j.helpers.LogLog;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HUP
/*     */   extends Thread
/*     */ {
/*     */   int port;
/*     */   ExternallyRolledFileAppender er;
/*     */   
/*     */   HUP(ExternallyRolledFileAppender er, int port) {
/* 113 */     this.er = er;
/* 114 */     this.port = port;
/*     */   }
/*     */   
/*     */   public void run() {
/* 118 */     while (!isInterrupted()) {
/*     */       try {
/* 120 */         ServerSocket serverSocket = new ServerSocket(this.port);
/*     */         while (true) {
/* 122 */           Socket socket = serverSocket.accept();
/* 123 */           LogLog.debug("Connected to client at " + socket.getInetAddress());
/* 124 */           (new Thread(new HUPNode(socket, this.er), "ExternallyRolledFileAppender-HUP")).start();
/*     */         } 
/* 126 */       } catch (InterruptedIOException e) {
/* 127 */         Thread.currentThread().interrupt();
/* 128 */         e.printStackTrace();
/* 129 */       } catch (IOException e) {
/* 130 */         e.printStackTrace();
/* 131 */       } catch (RuntimeException e) {
/* 132 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/HUP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */