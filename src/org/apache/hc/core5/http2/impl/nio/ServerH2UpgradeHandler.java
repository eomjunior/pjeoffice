/*    */ package org.apache.hc.core5.http2.impl.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.http.impl.nio.HttpConnectionEventHandler;
/*    */ import org.apache.hc.core5.reactor.IOEventHandler;
/*    */ import org.apache.hc.core5.reactor.IOSession;
/*    */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*    */ import org.apache.hc.core5.reactor.ProtocolUpgradeHandler;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ @Internal
/*    */ public class ServerH2UpgradeHandler
/*    */   implements ProtocolUpgradeHandler
/*    */ {
/*    */   private final ServerH2StreamMultiplexerFactory http2StreamHandlerFactory;
/*    */   
/*    */   public ServerH2UpgradeHandler(ServerH2StreamMultiplexerFactory http2StreamHandlerFactory) {
/* 54 */     this.http2StreamHandlerFactory = (ServerH2StreamMultiplexerFactory)Args.notNull(http2StreamHandlerFactory, "HTTP/2 stream handler factory");
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade(ProtocolIOSession ioSession, FutureCallback<ProtocolIOSession> callback) {
/* 59 */     HttpConnectionEventHandler protocolNegotiator = new ServerH2PrefaceHandler(ioSession, this.http2StreamHandlerFactory, callback);
/*    */     
/* 61 */     ioSession.upgrade((IOEventHandler)protocolNegotiator);
/*    */     try {
/* 63 */       protocolNegotiator.connected((IOSession)ioSession);
/* 64 */     } catch (IOException ex) {
/* 65 */       protocolNegotiator.exception((IOSession)ioSession, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerH2UpgradeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */