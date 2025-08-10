/*    */ package org.apache.hc.client5.http.socket;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedActionException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.io.Closer;
/*    */ import org.apache.hc.core5.util.Asserts;
/*    */ import org.apache.hc.core5.util.TimeValue;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class PlainConnectionSocketFactory
/*    */   implements ConnectionSocketFactory
/*    */ {
/* 53 */   public static final PlainConnectionSocketFactory INSTANCE = new PlainConnectionSocketFactory();
/*    */   
/*    */   public static PlainConnectionSocketFactory getSocketFactory() {
/* 56 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket createSocket(HttpContext context) throws IOException {
/* 65 */     return new Socket();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket connectSocket(TimeValue connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
/* 76 */     Socket sock = (socket != null) ? socket : createSocket(context);
/* 77 */     if (localAddress != null) {
/* 78 */       sock.bind(localAddress);
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/*    */       try {
/* 84 */         AccessController.doPrivileged(() -> {
/*    */               sock.connect(remoteAddress, TimeValue.isPositive(connectTimeout) ? connectTimeout.toMillisecondsIntBound() : 0);
/*    */               return null;
/*    */             });
/* 88 */       } catch (PrivilegedActionException e) {
/* 89 */         Asserts.check(e.getCause() instanceof IOException, "method contract violation only checked exceptions are wrapped: " + e
/* 90 */             .getCause());
/*    */         
/* 92 */         throw (IOException)e.getCause();
/*    */       } 
/* 94 */     } catch (IOException ex) {
/* 95 */       Closer.closeQuietly(sock);
/* 96 */       throw ex;
/*    */     } 
/* 98 */     return sock;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/socket/PlainConnectionSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */