/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.helpers.CyclicBuffer;
/*     */ import org.apache.log4j.helpers.LogLog;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketHubAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   static final int DEFAULT_PORT = 4560;
/* 123 */   private int port = 4560;
/* 124 */   private Vector oosList = new Vector();
/* 125 */   private ServerMonitor serverMonitor = null;
/*     */   private boolean locationInfo = false;
/* 127 */   private CyclicBuffer buffer = null;
/*     */ 
/*     */   
/*     */   private String application;
/*     */ 
/*     */   
/*     */   private boolean advertiseViaMulticastDNS;
/*     */ 
/*     */   
/*     */   private ZeroConfSupport zeroConf;
/*     */ 
/*     */   
/*     */   public static final String ZONE = "_log4j_obj_tcpaccept_appender.local.";
/*     */   
/*     */   private ServerSocket serverSocket;
/*     */ 
/*     */   
/*     */   public SocketHubAppender(int _port) {
/* 145 */     this.port = _port;
/* 146 */     startServer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 153 */     if (this.advertiseViaMulticastDNS) {
/* 154 */       this.zeroConf = new ZeroConfSupport("_log4j_obj_tcpaccept_appender.local.", this.port, getName());
/* 155 */       this.zeroConf.advertise();
/*     */     } 
/* 157 */     startServer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 166 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 169 */     LogLog.debug("closing SocketHubAppender " + getName());
/* 170 */     this.closed = true;
/* 171 */     if (this.advertiseViaMulticastDNS) {
/* 172 */       this.zeroConf.unadvertise();
/*     */     }
/* 174 */     cleanUp();
/*     */     
/* 176 */     LogLog.debug("SocketHubAppender " + getName() + " closed");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanUp() {
/* 185 */     LogLog.debug("stopping ServerSocket");
/* 186 */     this.serverMonitor.stopMonitor();
/* 187 */     this.serverMonitor = null;
/*     */ 
/*     */     
/* 190 */     LogLog.debug("closing client connections");
/* 191 */     while (this.oosList.size() != 0) {
/* 192 */       ObjectOutputStream oos = this.oosList.elementAt(0);
/* 193 */       if (oos != null) {
/*     */         try {
/* 195 */           oos.close();
/* 196 */         } catch (InterruptedIOException e) {
/* 197 */           Thread.currentThread().interrupt();
/* 198 */           LogLog.error("could not close oos.", e);
/* 199 */         } catch (IOException e) {
/* 200 */           LogLog.error("could not close oos.", e);
/*     */         } 
/*     */         
/* 203 */         this.oosList.removeElementAt(0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 212 */     if (event != null) {
/*     */       
/* 214 */       if (this.locationInfo) {
/* 215 */         event.getLocationInformation();
/*     */       }
/* 217 */       if (this.application != null) {
/* 218 */         event.setProperty("application", this.application);
/*     */       }
/* 220 */       event.getNDC();
/* 221 */       event.getThreadName();
/* 222 */       event.getMDCCopy();
/* 223 */       event.getRenderedMessage();
/* 224 */       event.getThrowableStrRep();
/*     */       
/* 226 */       if (this.buffer != null) {
/* 227 */         this.buffer.add(event);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 232 */     if (event == null || this.oosList.size() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 237 */     for (int streamCount = 0; streamCount < this.oosList.size(); streamCount++) {
/*     */       
/* 239 */       ObjectOutputStream oos = null;
/*     */       try {
/* 241 */         oos = this.oosList.elementAt(streamCount);
/* 242 */       } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 249 */       if (oos == null) {
/*     */         break;
/*     */       }
/*     */       try {
/* 253 */         oos.writeObject(event);
/* 254 */         oos.flush();
/*     */ 
/*     */ 
/*     */         
/* 258 */         oos.reset();
/* 259 */       } catch (IOException e) {
/* 260 */         if (e instanceof InterruptedIOException) {
/* 261 */           Thread.currentThread().interrupt();
/*     */         }
/*     */         
/* 264 */         this.oosList.removeElementAt(streamCount);
/* 265 */         LogLog.debug("dropped connection");
/*     */ 
/*     */         
/* 268 */         streamCount--;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 278 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int _port) {
/* 286 */     this.port = _port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplication(String lapp) {
/* 295 */     this.application = lapp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getApplication() {
/* 302 */     return this.application;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 309 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferSize(int _bufferSize) {
/* 317 */     this.buffer = new CyclicBuffer(_bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 324 */     if (this.buffer == null) {
/* 325 */       return 0;
/*     */     }
/* 327 */     return this.buffer.getMaxSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationInfo(boolean _locationInfo) {
/* 337 */     this.locationInfo = _locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/* 344 */     return this.locationInfo;
/*     */   }
/*     */   
/*     */   public void setAdvertiseViaMulticastDNS(boolean advertiseViaMulticastDNS) {
/* 348 */     this.advertiseViaMulticastDNS = advertiseViaMulticastDNS;
/*     */   }
/*     */   
/*     */   public boolean isAdvertiseViaMulticastDNS() {
/* 352 */     return this.advertiseViaMulticastDNS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startServer() {
/* 359 */     this.serverMonitor = new ServerMonitor(this.port, this.oosList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServerSocket createServerSocket(int socketPort) throws IOException {
/* 370 */     return new ServerSocket(socketPort);
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketHubAppender() {}
/*     */ 
/*     */   
/*     */   private class ServerMonitor
/*     */     implements Runnable
/*     */   {
/*     */     private int port;
/*     */     
/*     */     private Vector oosList;
/*     */     private boolean keepRunning;
/*     */     private Thread monitorThread;
/*     */     
/*     */     public ServerMonitor(int _port, Vector _oosList) {
/* 387 */       this.port = _port;
/* 388 */       this.oosList = _oosList;
/* 389 */       this.keepRunning = true;
/* 390 */       this.monitorThread = new Thread(this);
/* 391 */       this.monitorThread.setDaemon(true);
/* 392 */       this.monitorThread.setName("SocketHubAppender-Monitor-" + this.port);
/* 393 */       this.monitorThread.start();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void stopMonitor() {
/* 401 */       if (this.keepRunning) {
/* 402 */         LogLog.debug("server monitor thread shutting down");
/* 403 */         this.keepRunning = false;
/*     */         try {
/* 405 */           if (SocketHubAppender.this.serverSocket != null) {
/* 406 */             SocketHubAppender.this.serverSocket.close();
/* 407 */             SocketHubAppender.this.serverSocket = null;
/*     */           } 
/* 409 */         } catch (IOException iOException) {}
/*     */ 
/*     */         
/*     */         try {
/* 413 */           this.monitorThread.join();
/* 414 */         } catch (InterruptedException e) {
/* 415 */           Thread.currentThread().interrupt();
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 420 */         this.monitorThread = null;
/* 421 */         LogLog.debug("server monitor thread shut down");
/*     */       } 
/*     */     }
/*     */     
/*     */     private void sendCachedEvents(ObjectOutputStream stream) throws IOException {
/* 426 */       if (SocketHubAppender.this.buffer != null) {
/* 427 */         for (int i = 0; i < SocketHubAppender.this.buffer.length(); i++) {
/* 428 */           stream.writeObject(SocketHubAppender.this.buffer.get(i));
/*     */         }
/* 430 */         stream.flush();
/* 431 */         stream.reset();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 440 */       SocketHubAppender.this.serverSocket = null;
/*     */       try {
/* 442 */         SocketHubAppender.this.serverSocket = SocketHubAppender.this.createServerSocket(this.port);
/* 443 */         SocketHubAppender.this.serverSocket.setSoTimeout(1000);
/* 444 */       } catch (Exception e) {
/* 445 */         if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
/* 446 */           Thread.currentThread().interrupt();
/*     */         }
/* 448 */         LogLog.error("exception setting timeout, shutting down server socket.", e);
/* 449 */         this.keepRunning = false;
/*     */         
/*     */         return;
/*     */       } 
/*     */       try {
/*     */         try {
/* 455 */           SocketHubAppender.this.serverSocket.setSoTimeout(1000);
/* 456 */         } catch (SocketException e) {
/* 457 */           LogLog.error("exception setting timeout, shutting down server socket.", e);
/*     */           
/*     */           return;
/*     */         } 
/* 461 */         while (this.keepRunning) {
/* 462 */           Socket socket = null;
/*     */           try {
/* 464 */             socket = SocketHubAppender.this.serverSocket.accept();
/* 465 */           } catch (InterruptedIOException interruptedIOException) {
/*     */           
/* 467 */           } catch (SocketException e) {
/* 468 */             LogLog.error("exception accepting socket, shutting down server socket.", e);
/* 469 */             this.keepRunning = false;
/* 470 */           } catch (IOException e) {
/* 471 */             LogLog.error("exception accepting socket.", e);
/*     */           } 
/*     */ 
/*     */           
/* 475 */           if (socket != null) {
/*     */             try {
/* 477 */               InetAddress remoteAddress = socket.getInetAddress();
/* 478 */               LogLog.debug("accepting connection from " + remoteAddress.getHostName() + " (" + remoteAddress
/* 479 */                   .getHostAddress() + ")");
/*     */ 
/*     */               
/* 482 */               ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
/* 483 */               if (SocketHubAppender.this.buffer != null && SocketHubAppender.this.buffer.length() > 0) {
/* 484 */                 sendCachedEvents(oos);
/*     */               }
/*     */ 
/*     */               
/* 488 */               this.oosList.addElement(oos);
/* 489 */             } catch (IOException e) {
/* 490 */               if (e instanceof InterruptedIOException) {
/* 491 */                 Thread.currentThread().interrupt();
/*     */               }
/* 493 */               LogLog.error("exception creating output stream on socket.", e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } finally {
/*     */         
/*     */         try {
/* 500 */           SocketHubAppender.this.serverSocket.close();
/* 501 */         } catch (InterruptedIOException e) {
/* 502 */           Thread.currentThread().interrupt();
/* 503 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/SocketHubAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */