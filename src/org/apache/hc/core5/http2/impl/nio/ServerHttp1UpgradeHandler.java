/*    */ package org.apache.hc.core5.http2.impl.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.http.URIScheme;
/*    */ import org.apache.hc.core5.http.impl.nio.ServerHttp1IOEventHandler;
/*    */ import org.apache.hc.core5.http.impl.nio.ServerHttp1StreamDuplexerFactory;
/*    */ import org.apache.hc.core5.reactor.IOEventHandler;
/*    */ import org.apache.hc.core5.reactor.IOSession;
/*    */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*    */ import org.apache.hc.core5.reactor.ProtocolUpgradeHandler;
/*    */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
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
/*    */ public class ServerHttp1UpgradeHandler
/*    */   implements ProtocolUpgradeHandler
/*    */ {
/*    */   private final ServerHttp1StreamDuplexerFactory http1StreamHandlerFactory;
/*    */   
/*    */   public ServerHttp1UpgradeHandler(ServerHttp1StreamDuplexerFactory http1StreamHandlerFactory) {
/* 57 */     this.http1StreamHandlerFactory = (ServerHttp1StreamDuplexerFactory)Args.notNull(http1StreamHandlerFactory, "HTTP/1.1 stream handler factory");
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade(ProtocolIOSession ioSession, FutureCallback<ProtocolIOSession> callback) {
/* 62 */     TlsDetails tlsDetails = ioSession.getTlsDetails();
/* 63 */     ServerHttp1IOEventHandler eventHandler = new ServerHttp1IOEventHandler(this.http1StreamHandlerFactory.create((tlsDetails != null) ? URIScheme.HTTPS.id : URIScheme.HTTP.id, ioSession));
/*    */ 
/*    */     
/* 66 */     ioSession.upgrade((IOEventHandler)eventHandler);
/* 67 */     ioSession.upgrade((IOEventHandler)eventHandler);
/*    */     try {
/* 69 */       eventHandler.connected((IOSession)ioSession);
/* 70 */       if (callback != null) {
/* 71 */         callback.completed(ioSession);
/*    */       }
/* 73 */     } catch (IOException ex) {
/* 74 */       eventHandler.exception((IOSession)ioSession, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerHttp1UpgradeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */