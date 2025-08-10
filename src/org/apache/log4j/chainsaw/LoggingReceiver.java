/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.net.HardenedLoggingEventInputStream;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LoggingReceiver
/*     */   extends Thread
/*     */ {
/*  36 */   private static final Logger LOG = Logger.getLogger(LoggingReceiver.class);
/*     */ 
/*     */   
/*     */   private MyTableModel mModel;
/*     */ 
/*     */   
/*     */   private ServerSocket mSvrSock;
/*     */ 
/*     */ 
/*     */   
/*     */   private class Slurper
/*     */     implements Runnable
/*     */   {
/*     */     private final Socket mClient;
/*     */ 
/*     */ 
/*     */     
/*     */     Slurper(Socket aClient) {
/*  54 */       this.mClient = aClient;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  59 */       LoggingReceiver.LOG.debug("Starting to get data");
/*     */       
/*     */       try {
/*  62 */         HardenedLoggingEventInputStream hleis = new HardenedLoggingEventInputStream(this.mClient.getInputStream());
/*     */         while (true) {
/*  64 */           LoggingEvent event = (LoggingEvent)hleis.readObject();
/*  65 */           LoggingReceiver.this.mModel.addEvent(new EventDetails(event));
/*     */         } 
/*  67 */       } catch (EOFException e) {
/*  68 */         LoggingReceiver.LOG.info("Reached EOF, closing connection");
/*  69 */       } catch (SocketException e) {
/*  70 */         LoggingReceiver.LOG.info("Caught SocketException, closing connection");
/*  71 */       } catch (IOException e) {
/*  72 */         LoggingReceiver.LOG.warn("Got IOException, closing connection", e);
/*  73 */       } catch (ClassNotFoundException e) {
/*  74 */         LoggingReceiver.LOG.warn("Got ClassNotFoundException, closing connection", e);
/*     */       } 
/*     */       
/*     */       try {
/*  78 */         this.mClient.close();
/*  79 */       } catch (IOException e) {
/*  80 */         LoggingReceiver.LOG.warn("Error closing connection", e);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LoggingReceiver(MyTableModel aModel, int aPort) throws IOException {
/*  99 */     setDaemon(true);
/* 100 */     this.mModel = aModel;
/* 101 */     this.mSvrSock = new ServerSocket(aPort);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 106 */     LOG.info("Thread started");
/*     */     try {
/*     */       while (true) {
/* 109 */         LOG.debug("Waiting for a connection");
/* 110 */         Socket client = this.mSvrSock.accept();
/* 111 */         LOG.debug("Got a connection from " + client.getInetAddress().getHostName());
/* 112 */         Thread t = new Thread(new Slurper(client));
/* 113 */         t.setDaemon(true);
/* 114 */         t.start();
/*     */       } 
/* 116 */     } catch (IOException e) {
/* 117 */       LOG.error("Error in accepting connections, stopping.", e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/LoggingReceiver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */