/*     */ package org.apache.hc.client5.http.nio;
/*     */ 
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.util.TimeValue;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public interface AsyncClientConnectionManager
/*     */   extends ModalCloseable
/*     */ {
/*     */   Future<AsyncConnectionEndpoint> lease(String paramString, HttpRoute paramHttpRoute, Object paramObject, Timeout paramTimeout, FutureCallback<AsyncConnectionEndpoint> paramFutureCallback);
/*     */   
/*     */   void release(AsyncConnectionEndpoint paramAsyncConnectionEndpoint, Object paramObject, TimeValue paramTimeValue);
/*     */   
/*     */   Future<AsyncConnectionEndpoint> connect(AsyncConnectionEndpoint paramAsyncConnectionEndpoint, ConnectionInitiator paramConnectionInitiator, Timeout paramTimeout, Object paramObject, HttpContext paramHttpContext, FutureCallback<AsyncConnectionEndpoint> paramFutureCallback);
/*     */   
/*     */   void upgrade(AsyncConnectionEndpoint paramAsyncConnectionEndpoint, Object paramObject, HttpContext paramHttpContext);
/*     */   
/*     */   default void upgrade(AsyncConnectionEndpoint endpoint, Object attachment, HttpContext context, FutureCallback<AsyncConnectionEndpoint> callback) {
/* 149 */     upgrade(endpoint, attachment, context);
/* 150 */     if (callback != null)
/* 151 */       callback.completed(endpoint); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/nio/AsyncClientConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */