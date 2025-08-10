/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ProtocolIOSession
/*    */   extends IOSession, TransportSecurityLayer
/*    */ {
/*    */   default void switchProtocol(String protocolId, FutureCallback<ProtocolIOSession> callback) throws UnsupportedOperationException {
/* 49 */     throw new UnsupportedOperationException("Protocol switch not supported");
/*    */   }
/*    */   
/*    */   default void registerProtocol(String protocolId, ProtocolUpgradeHandler upgradeHandler) {}
/*    */   
/*    */   NamedEndpoint getInitialEndpoint();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ProtocolIOSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */