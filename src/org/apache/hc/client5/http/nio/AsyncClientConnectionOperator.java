/*     */ package org.apache.hc.client5.http.nio;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public interface AsyncClientConnectionOperator
/*     */ {
/*     */   Future<ManagedAsyncClientConnection> connect(ConnectionInitiator paramConnectionInitiator, HttpHost paramHttpHost, SocketAddress paramSocketAddress, Timeout paramTimeout, Object paramObject, FutureCallback<ManagedAsyncClientConnection> paramFutureCallback);
/*     */   
/*     */   default Future<ManagedAsyncClientConnection> connect(ConnectionInitiator connectionInitiator, HttpHost host, SocketAddress localAddress, Timeout connectTimeout, Object attachment, HttpContext context, FutureCallback<ManagedAsyncClientConnection> callback) {
/*  93 */     return connect(connectionInitiator, host, localAddress, connectTimeout, attachment, callback);
/*     */   }
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
/*     */   void upgrade(ManagedAsyncClientConnection paramManagedAsyncClientConnection, HttpHost paramHttpHost, Object paramObject);
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
/*     */   default void upgrade(ManagedAsyncClientConnection conn, HttpHost host, Object attachment, HttpContext context, FutureCallback<ManagedAsyncClientConnection> callback) {
/* 126 */     upgrade(conn, host, attachment, context);
/* 127 */     if (callback != null) {
/* 128 */       callback.completed(conn);
/*     */     }
/*     */   }
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
/*     */   default void upgrade(ManagedAsyncClientConnection conn, HttpHost host, Object attachment, HttpContext context) {
/* 144 */     upgrade(conn, host, attachment);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/nio/AsyncClientConnectionOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */