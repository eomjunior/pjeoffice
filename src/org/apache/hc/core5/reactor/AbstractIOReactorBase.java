/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import java.util.concurrent.Future;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ import org.apache.hc.core5.util.Timeout;
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
/*    */ abstract class AbstractIOReactorBase
/*    */   implements ConnectionInitiator, IOReactorService
/*    */ {
/*    */   public final Future<IOSession> connect(NamedEndpoint remoteEndpoint, SocketAddress remoteAddress, SocketAddress localAddress, Timeout timeout, Object attachment, FutureCallback<IOSession> callback) throws IOReactorShutdownException {
/* 48 */     Args.notNull(remoteEndpoint, "Remote endpoint");
/* 49 */     if (getStatus().compareTo(IOReactorStatus.ACTIVE) > 0) {
/* 50 */       throw new IOReactorShutdownException("I/O reactor has been shut down");
/*    */     }
/*    */     try {
/* 53 */       return getWorkerSelector().next().connect(remoteEndpoint, remoteAddress, localAddress, timeout, attachment, callback);
/* 54 */     } catch (IOReactorShutdownException ex) {
/* 55 */       initiateShutdown();
/* 56 */       throw ex;
/*    */     } 
/*    */   }
/*    */   
/*    */   abstract IOWorkers.Selector getWorkerSelector();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/AbstractIOReactorBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */