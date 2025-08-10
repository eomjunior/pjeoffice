/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.SocketAddress;
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
/*    */ public class SocksProxyProtocolHandlerFactory
/*    */   implements IOEventHandlerFactory
/*    */ {
/*    */   private final InetSocketAddress targetAddress;
/*    */   private final String username;
/*    */   private final String password;
/*    */   private final IOEventHandlerFactory eventHandlerFactory;
/*    */   
/*    */   public SocksProxyProtocolHandlerFactory(SocketAddress targetAddress, String username, String password, IOEventHandlerFactory eventHandlerFactory) throws IOException {
/* 42 */     this.eventHandlerFactory = eventHandlerFactory;
/* 43 */     this.username = username;
/* 44 */     this.password = password;
/* 45 */     if (targetAddress instanceof InetSocketAddress) {
/* 46 */       this.targetAddress = (InetSocketAddress)targetAddress;
/*    */     } else {
/* 48 */       throw new IOException("Unsupported target address type for SOCKS proxy connection: " + targetAddress.getClass());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public IOEventHandler createHandler(ProtocolIOSession ioSession, Object attachment) {
/* 54 */     return new SocksProxyProtocolHandler(ioSession, attachment, this.targetAddress, this.username, this.password, this.eventHandlerFactory);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/SocksProxyProtocolHandlerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */