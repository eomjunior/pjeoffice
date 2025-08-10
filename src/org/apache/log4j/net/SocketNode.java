/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.spi.LoggerRepository;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketNode
/*     */   implements Runnable
/*     */ {
/*     */   Socket socket;
/*     */   LoggerRepository hierarchy;
/*     */   HardenedLoggingEventInputStream ois;
/*  49 */   static Logger logger = Logger.getLogger(SocketNode.class);
/*     */   
/*     */   public SocketNode(Socket socket, LoggerRepository hierarchy) {
/*  52 */     this.socket = socket;
/*  53 */     this.hierarchy = hierarchy;
/*     */     try {
/*  55 */       this.ois = new HardenedLoggingEventInputStream(socket.getInputStream());
/*  56 */     } catch (InterruptedIOException e) {
/*  57 */       Thread.currentThread().interrupt();
/*  58 */       logger.error("Could not open ObjectInputStream to " + socket, e);
/*  59 */     } catch (IOException e) {
/*  60 */       logger.error("Could not open ObjectInputStream to " + socket, e);
/*  61 */     } catch (RuntimeException e) {
/*  62 */       logger.error("Could not open ObjectInputStream to " + socket, e);
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
/*     */   public void run() {
/*     */     try {
/*  77 */       if (this.ois != null) {
/*     */         while (true) {
/*     */           
/*  80 */           LoggingEvent event = (LoggingEvent)this.ois.readObject();
/*     */ 
/*     */           
/*  83 */           Logger remoteLogger = this.hierarchy.getLogger(event.getLoggerName());
/*     */ 
/*     */           
/*  86 */           if (event.getLevel().isGreaterOrEqual((Priority)remoteLogger.getEffectiveLevel()))
/*     */           {
/*  88 */             remoteLogger.callAppenders(event);
/*     */           }
/*     */         } 
/*     */       }
/*  92 */     } catch (EOFException e) {
/*  93 */       logger.info("Caught java.io.EOFException closing conneciton.");
/*  94 */     } catch (SocketException e) {
/*  95 */       logger.info("Caught java.net.SocketException closing conneciton.");
/*  96 */     } catch (InterruptedIOException e) {
/*  97 */       Thread.currentThread().interrupt();
/*  98 */       logger.info("Caught java.io.InterruptedIOException: " + e);
/*  99 */       logger.info("Closing connection.");
/* 100 */     } catch (IOException e) {
/* 101 */       logger.info("Caught java.io.IOException: " + e);
/* 102 */       logger.info("Closing connection.");
/* 103 */     } catch (Exception e) {
/* 104 */       logger.error("Unexpected exception. Closing conneciton.", e);
/*     */     } finally {
/* 106 */       if (this.ois != null) {
/*     */         try {
/* 108 */           this.ois.close();
/* 109 */         } catch (Exception e) {
/* 110 */           logger.info("Could not close connection.", e);
/*     */         } 
/*     */       }
/* 113 */       if (this.socket != null)
/*     */         try {
/* 115 */           this.socket.close();
/* 116 */         } catch (InterruptedIOException e) {
/* 117 */           Thread.currentThread().interrupt();
/* 118 */         } catch (IOException iOException) {} 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/SocketNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */