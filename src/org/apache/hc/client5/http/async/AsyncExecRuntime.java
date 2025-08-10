/*     */ package org.apache.hc.client5.http.async;
/*     */ 
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.util.TimeValue;
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
/*     */ @Internal
/*     */ public interface AsyncExecRuntime
/*     */ {
/*     */   boolean isEndpointAcquired();
/*     */   
/*     */   Cancellable acquireEndpoint(String paramString, HttpRoute paramHttpRoute, Object paramObject, HttpClientContext paramHttpClientContext, FutureCallback<AsyncExecRuntime> paramFutureCallback);
/*     */   
/*     */   void releaseEndpoint();
/*     */   
/*     */   void discardEndpoint();
/*     */   
/*     */   boolean isEndpointConnected();
/*     */   
/*     */   Cancellable connectEndpoint(HttpClientContext paramHttpClientContext, FutureCallback<AsyncExecRuntime> paramFutureCallback);
/*     */   
/*     */   void upgradeTls(HttpClientContext paramHttpClientContext);
/*     */   
/*     */   default void upgradeTls(HttpClientContext context, FutureCallback<AsyncExecRuntime> callback) {
/* 125 */     upgradeTls(context);
/* 126 */     if (callback != null)
/* 127 */       callback.completed(this); 
/*     */   }
/*     */   
/*     */   boolean validateConnection();
/*     */   
/*     */   Cancellable execute(String paramString, AsyncClientExchangeHandler paramAsyncClientExchangeHandler, HttpClientContext paramHttpClientContext);
/*     */   
/*     */   void markConnectionReusable(Object paramObject, TimeValue paramTimeValue);
/*     */   
/*     */   void markConnectionNonReusable();
/*     */   
/*     */   AsyncExecRuntime fork();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/AsyncExecRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */