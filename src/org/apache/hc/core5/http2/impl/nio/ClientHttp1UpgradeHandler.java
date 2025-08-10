/*    */ package org.apache.hc.core5.http2.impl.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.http.impl.nio.ClientHttp1IOEventHandler;
/*    */ import org.apache.hc.core5.http.impl.nio.ClientHttp1StreamDuplexerFactory;
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
/*    */ public class ClientHttp1UpgradeHandler
/*    */   implements ProtocolUpgradeHandler
/*    */ {
/*    */   private final ClientHttp1StreamDuplexerFactory http1StreamHandlerFactory;
/*    */   
/*    */   public ClientHttp1UpgradeHandler(ClientHttp1StreamDuplexerFactory http1StreamHandlerFactory) {
/* 55 */     this.http1StreamHandlerFactory = (ClientHttp1StreamDuplexerFactory)Args.notNull(http1StreamHandlerFactory, "HTTP/1.1 stream handler factory");
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade(ProtocolIOSession ioSession, FutureCallback<ProtocolIOSession> callback) {
/* 60 */     ClientHttp1IOEventHandler eventHandler = new ClientHttp1IOEventHandler(this.http1StreamHandlerFactory.create(ioSession));
/* 61 */     ioSession.upgrade((IOEventHandler)eventHandler);
/*    */     try {
/* 63 */       eventHandler.connected((IOSession)ioSession);
/* 64 */       if (callback != null) {
/* 65 */         callback.completed(ioSession);
/*    */       }
/* 67 */     } catch (IOException ex) {
/* 68 */       eventHandler.exception((IOSession)ioSession, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ClientHttp1UpgradeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */