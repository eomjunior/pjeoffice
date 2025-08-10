/*    */ package org.apache.hc.client5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.Socket;
/*    */ import org.apache.hc.client5.http.impl.Wire;
/*    */ import org.apache.hc.core5.http.impl.io.SocketHolder;
/*    */ import org.slf4j.Logger;
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
/*    */ class LoggingSocketHolder
/*    */   extends SocketHolder
/*    */ {
/*    */   private final Wire wire;
/*    */   
/*    */   public LoggingSocketHolder(Socket socket, String id, Logger log) {
/* 44 */     super(socket);
/* 45 */     this.wire = new Wire(log, id);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InputStream getInputStream(Socket socket) throws IOException {
/* 50 */     return new LoggingInputStream(super.getInputStream(socket), this.wire);
/*    */   }
/*    */ 
/*    */   
/*    */   protected OutputStream getOutputStream(Socket socket) throws IOException {
/* 55 */     return new LoggingOutputStream(super.getOutputStream(socket), this.wire);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/LoggingSocketHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */