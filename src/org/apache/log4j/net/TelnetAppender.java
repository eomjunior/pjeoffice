/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
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
/*     */ public class TelnetAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   private SocketHandler sh;
/*  66 */   private int port = 23;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/*     */     try {
/*  81 */       this.sh = new SocketHandler(this.port);
/*  82 */       this.sh.start();
/*  83 */     } catch (InterruptedIOException e) {
/*  84 */       Thread.currentThread().interrupt();
/*  85 */       e.printStackTrace();
/*  86 */     } catch (IOException e) {
/*  87 */       e.printStackTrace();
/*  88 */     } catch (RuntimeException e) {
/*  89 */       e.printStackTrace();
/*     */     } 
/*  91 */     super.activateOptions();
/*     */   }
/*     */   
/*     */   public int getPort() {
/*  95 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/*  99 */     this.port = port;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 104 */     if (this.sh != null) {
/* 105 */       this.sh.close();
/*     */       try {
/* 107 */         this.sh.join();
/* 108 */       } catch (InterruptedException ex) {
/* 109 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void append(LoggingEvent event) {
/* 119 */     if (this.sh != null) {
/* 120 */       this.sh.send(this.layout.format(event));
/* 121 */       if (this.layout.ignoresThrowable()) {
/* 122 */         String[] s = event.getThrowableStrRep();
/* 123 */         if (s != null) {
/* 124 */           StringBuilder buf = new StringBuilder();
/* 125 */           for (int i = 0; i < s.length; i++) {
/* 126 */             buf.append(s[i]);
/* 127 */             buf.append("\r\n");
/*     */           } 
/* 129 */           this.sh.send(buf.toString());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class SocketHandler
/*     */     extends Thread
/*     */   {
/* 143 */     private Vector writers = new Vector();
/* 144 */     private Vector connections = new Vector();
/*     */     private ServerSocket serverSocket;
/* 146 */     private int MAX_CONNECTIONS = 20;
/*     */     
/*     */     public void finalize() {
/* 149 */       close();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 158 */       synchronized (this) {
/* 159 */         for (Enumeration<Socket> e = this.connections.elements(); e.hasMoreElements();) {
/*     */           
/* 161 */           try { ((Socket)e.nextElement()).close(); }
/* 162 */           catch (InterruptedIOException ex)
/* 163 */           { Thread.currentThread().interrupt(); }
/* 164 */           catch (IOException iOException) {  }
/* 165 */           catch (RuntimeException runtimeException) {}
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 171 */       try { this.serverSocket.close(); }
/* 172 */       catch (InterruptedIOException ex)
/* 173 */       { Thread.currentThread().interrupt(); }
/* 174 */       catch (IOException iOException) {  }
/* 175 */       catch (RuntimeException runtimeException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void send(String message) {
/* 181 */       Iterator ce = this.connections.iterator();
/* 182 */       for (Iterator<PrintWriter> e = this.writers.iterator(); e.hasNext(); ) {
/* 183 */         ce.next();
/* 184 */         PrintWriter writer = e.next();
/* 185 */         writer.print(message);
/* 186 */         if (writer.checkError()) {
/* 187 */           ce.remove();
/* 188 */           e.remove();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       while (true)
/* 198 */       { if (!this.serverSocket.isClosed()) {
/*     */           try {
/* 200 */             Socket newClient = this.serverSocket.accept();
/* 201 */             PrintWriter pw = new PrintWriter(newClient.getOutputStream());
/* 202 */             if (this.connections.size() < this.MAX_CONNECTIONS) {
/* 203 */               synchronized (this) {
/* 204 */                 this.connections.addElement(newClient);
/* 205 */                 this.writers.addElement(pw);
/* 206 */                 pw.print("TelnetAppender v1.0 (" + this.connections.size() + " active connections)\r\n\r\n");
/* 207 */                 pw.flush();
/*     */               }  continue;
/*     */             } 
/* 210 */             pw.print("Too many connections.\r\n");
/* 211 */             pw.flush();
/* 212 */             newClient.close();
/*     */             continue;
/* 214 */           } catch (Exception e) {
/* 215 */             if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
/* 216 */               Thread.currentThread().interrupt();
/*     */             }
/* 218 */             if (!this.serverSocket.isClosed()) {
/* 219 */               LogLog.error("Encountered error while in SocketHandler loop.", e);
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */         try {
/* 226 */           this.serverSocket.close();
/* 227 */         } catch (InterruptedIOException ex) {
/* 228 */           Thread.currentThread().interrupt();
/* 229 */         } catch (IOException iOException) {} return; }  try { this.serverSocket.close(); } catch (InterruptedIOException interruptedIOException) { Thread.currentThread().interrupt(); } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/*     */     public SocketHandler(int port) throws IOException {
/* 234 */       this.serverSocket = new ServerSocket(port);
/* 235 */       setName("TelnetAppender-" + getName() + "-" + port);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/TelnetAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */