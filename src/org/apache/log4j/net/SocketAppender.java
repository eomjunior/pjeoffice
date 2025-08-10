/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import org.apache.log4j.AppenderSkeleton;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   public static final int DEFAULT_PORT = 4560;
/*     */   static final int DEFAULT_RECONNECTION_DELAY = 30000;
/*     */   String remoteHost;
/*     */   public static final String ZONE = "_log4j_obj_tcpconnect_appender.local.";
/*     */   InetAddress address;
/* 134 */   int port = 4560;
/*     */   ObjectOutputStream oos;
/* 136 */   int reconnectionDelay = 30000;
/*     */   
/*     */   boolean locationInfo = false;
/*     */   
/*     */   private String application;
/*     */   private Connector connector;
/* 142 */   int counter = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int RESET_FREQUENCY = 1;
/*     */ 
/*     */   
/*     */   private boolean advertiseViaMulticastDNS;
/*     */ 
/*     */   
/*     */   private ZeroConfSupport zeroConf;
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAppender(InetAddress address, int port) {
/* 157 */     this.address = address;
/* 158 */     this.remoteHost = address.getHostName();
/* 159 */     this.port = port;
/* 160 */     connect(address, port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAppender(String host, int port) {
/* 167 */     this.port = port;
/* 168 */     this.address = getAddressByName(host);
/* 169 */     this.remoteHost = host;
/* 170 */     connect(this.address, port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 177 */     if (this.advertiseViaMulticastDNS) {
/* 178 */       this.zeroConf = new ZeroConfSupport("_log4j_obj_tcpconnect_appender.local.", this.port, getName());
/* 179 */       this.zeroConf.advertise();
/*     */     } 
/* 181 */     connect(this.address, this.port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 191 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 194 */     this.closed = true;
/* 195 */     if (this.advertiseViaMulticastDNS) {
/* 196 */       this.zeroConf.unadvertise();
/*     */     }
/*     */     
/* 199 */     cleanUp();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanUp() {
/* 207 */     if (this.oos != null) {
/*     */       try {
/* 209 */         this.oos.close();
/* 210 */       } catch (IOException e) {
/* 211 */         if (e instanceof java.io.InterruptedIOException) {
/* 212 */           Thread.currentThread().interrupt();
/*     */         }
/* 214 */         LogLog.error("Could not close oos.", e);
/*     */       } 
/* 216 */       this.oos = null;
/*     */     } 
/* 218 */     if (this.connector != null) {
/*     */       
/* 220 */       this.connector.interrupted = true;
/* 221 */       this.connector = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   void connect(InetAddress address, int port) {
/* 226 */     if (this.address == null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 230 */       cleanUp();
/* 231 */       this.oos = new ObjectOutputStream((new Socket(address, port)).getOutputStream());
/* 232 */     } catch (IOException e) {
/* 233 */       if (e instanceof java.io.InterruptedIOException) {
/* 234 */         Thread.currentThread().interrupt();
/*     */       }
/* 236 */       String msg = "Could not connect to remote log4j server at [" + address.getHostName() + "].";
/* 237 */       if (this.reconnectionDelay > 0) {
/* 238 */         msg = msg + " We will try again later.";
/* 239 */         fireConnector();
/*     */       } else {
/* 241 */         msg = msg + " We are not retrying.";
/* 242 */         this.errorHandler.error(msg, e, 0);
/*     */       } 
/* 244 */       LogLog.error(msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 249 */     if (event == null) {
/*     */       return;
/*     */     }
/* 252 */     if (this.address == null) {
/* 253 */       this.errorHandler.error("No remote host is set for SocketAppender named \"" + this.name + "\".");
/*     */       
/*     */       return;
/*     */     } 
/* 257 */     if (this.oos != null) {
/*     */       
/*     */       try {
/* 260 */         if (this.locationInfo) {
/* 261 */           event.getLocationInformation();
/*     */         }
/* 263 */         if (this.application != null) {
/* 264 */           event.setProperty("application", this.application);
/*     */         }
/* 266 */         event.getNDC();
/* 267 */         event.getThreadName();
/* 268 */         event.getMDCCopy();
/* 269 */         event.getRenderedMessage();
/* 270 */         event.getThrowableStrRep();
/*     */         
/* 272 */         this.oos.writeObject(event);
/*     */         
/* 274 */         this.oos.flush();
/* 275 */         if (++this.counter >= 1) {
/* 276 */           this.counter = 0;
/*     */ 
/*     */ 
/*     */           
/* 280 */           this.oos.reset();
/*     */         } 
/* 282 */       } catch (IOException e) {
/* 283 */         if (e instanceof java.io.InterruptedIOException) {
/* 284 */           Thread.currentThread().interrupt();
/*     */         }
/* 286 */         this.oos = null;
/* 287 */         LogLog.warn("Detected problem with connection: " + e);
/* 288 */         if (this.reconnectionDelay > 0) {
/* 289 */           fireConnector();
/*     */         } else {
/* 291 */           this.errorHandler.error("Detected problem with connection, not reconnecting.", e, 0);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAdvertiseViaMulticastDNS(boolean advertiseViaMulticastDNS) {
/* 299 */     this.advertiseViaMulticastDNS = advertiseViaMulticastDNS;
/*     */   }
/*     */   
/*     */   public boolean isAdvertiseViaMulticastDNS() {
/* 303 */     return this.advertiseViaMulticastDNS;
/*     */   }
/*     */   
/*     */   void fireConnector() {
/* 307 */     if (this.connector == null) {
/* 308 */       LogLog.debug("Starting a new connector thread.");
/* 309 */       this.connector = new Connector();
/* 310 */       this.connector.setDaemon(true);
/* 311 */       this.connector.setPriority(1);
/* 312 */       this.connector.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   static InetAddress getAddressByName(String host) {
/*     */     try {
/* 318 */       return InetAddress.getByName(host);
/* 319 */     } catch (Exception e) {
/* 320 */       if (e instanceof java.io.InterruptedIOException || e instanceof InterruptedException) {
/* 321 */         Thread.currentThread().interrupt();
/*     */       }
/* 323 */       LogLog.error("Could not find address of [" + host + "].", e);
/* 324 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 333 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteHost(String host) {
/* 341 */     this.address = getAddressByName(host);
/* 342 */     this.remoteHost = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRemoteHost() {
/* 349 */     return this.remoteHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 357 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 364 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationInfo(boolean locationInfo) {
/* 373 */     this.locationInfo = locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/* 380 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplication(String lapp) {
/* 391 */     this.application = lapp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getApplication() {
/* 400 */     return this.application;
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
/*     */   public void setReconnectionDelay(int delay) {
/* 413 */     this.reconnectionDelay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReconnectionDelay() {
/* 420 */     return this.reconnectionDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAppender() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class Connector
/*     */     extends Thread
/*     */   {
/*     */     boolean interrupted = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 441 */       while (!this.interrupted) {
/*     */         try {
/* 443 */           sleep(SocketAppender.this.reconnectionDelay);
/* 444 */           LogLog.debug("Attempting connection to " + SocketAppender.this.address.getHostName());
/* 445 */           Socket socket = new Socket(SocketAppender.this.address, SocketAppender.this.port);
/* 446 */           synchronized (this) {
/* 447 */             SocketAppender.this.oos = new ObjectOutputStream(socket.getOutputStream());
/* 448 */             SocketAppender.this.connector = null;
/* 449 */             LogLog.debug("Connection established. Exiting connector thread.");
/*     */           } 
/*     */           break;
/* 452 */         } catch (InterruptedException e) {
/* 453 */           LogLog.debug("Connector interrupted. Leaving loop.");
/*     */           return;
/* 455 */         } catch (ConnectException e) {
/* 456 */           LogLog.debug("Remote host " + SocketAppender.this.address.getHostName() + " refused connection.");
/* 457 */         } catch (IOException e) {
/* 458 */           if (e instanceof java.io.InterruptedIOException) {
/* 459 */             Thread.currentThread().interrupt();
/*     */           }
/* 461 */           LogLog.debug("Could not connect to " + SocketAppender.this.address.getHostName() + ". Exception is " + e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/SocketAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */