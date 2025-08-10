/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.Socket;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ 
/*    */ 
/*    */ public class SocketHolder
/*    */ {
/*    */   private final Socket socket;
/*    */   private final AtomicReference<InputStream> inputStreamRef;
/*    */   private final AtomicReference<OutputStream> outputStreamRef;
/*    */   
/*    */   public SocketHolder(Socket socket) {
/* 51 */     this.socket = (Socket)Args.notNull(socket, "Socket");
/* 52 */     this.inputStreamRef = new AtomicReference<>();
/* 53 */     this.outputStreamRef = new AtomicReference<>();
/*    */   }
/*    */   
/*    */   public final Socket getSocket() {
/* 57 */     return this.socket;
/*    */   }
/*    */   
/*    */   public final InputStream getInputStream() throws IOException {
/* 61 */     InputStream local = this.inputStreamRef.get();
/* 62 */     if (local != null) {
/* 63 */       return local;
/*    */     }
/* 65 */     local = getInputStream(this.socket);
/* 66 */     if (this.inputStreamRef.compareAndSet(null, local)) {
/* 67 */       return local;
/*    */     }
/* 69 */     return this.inputStreamRef.get();
/*    */   }
/*    */   
/*    */   protected InputStream getInputStream(Socket socket) throws IOException {
/* 73 */     return socket.getInputStream();
/*    */   }
/*    */   
/*    */   protected OutputStream getOutputStream(Socket socket) throws IOException {
/* 77 */     return socket.getOutputStream();
/*    */   }
/*    */   
/*    */   public final OutputStream getOutputStream() throws IOException {
/* 81 */     OutputStream local = this.outputStreamRef.get();
/* 82 */     if (local != null) {
/* 83 */       return local;
/*    */     }
/* 85 */     local = getOutputStream(this.socket);
/* 86 */     if (this.outputStreamRef.compareAndSet(null, local)) {
/* 87 */       return local;
/*    */     }
/* 89 */     return this.outputStreamRef.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return this.socket.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/SocketHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */