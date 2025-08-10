/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.config.imp.Global;
/*     */ import br.jus.cnj.pje.office.core.IPjeWebCodec;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.WebCodec;
/*     */ import java.io.IOException;
/*     */ import java.net.ProxySelector;
/*     */ import java.util.Optional;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*     */ import org.apache.hc.client5.http.impl.classic.HttpClients;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
/*     */ import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
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
/*     */ class PjeWebCodec
/*     */   extends WebCodec<PjeTaskResponse>
/*     */   implements IPjeWebCodec
/*     */ {
/*     */   private boolean closed = false;
/*     */   
/*     */   protected PjeWebCodec(CloseableHttpClient client) {
/*  61 */     super(client);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSuccess(int code) {
/*  66 */     return (super.isSuccess(code) || code == 302);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClosed() {
/*  71 */     return this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Exception launch(String message, Exception cause) {
/*  76 */     return new PjeClientException(message, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final PjeTaskResponse success() {
/*  81 */     return PjeWebTaskResponse.success();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void handleFail(IResultChecker checkResults, String responseText, int httpCode) throws Exception {
/*  86 */     checkResults.handle(responseText, httpCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() throws IOException {
/*  91 */     if (!this.closed) {
/*  92 */       Throwables.quietly(this::doClose);
/*  93 */       Throwables.quietly(this.client::close);
/*  94 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws IOException {}
/*     */   
/*     */   protected static class Builder<T extends PjeWebCodec>
/*     */   {
/* 103 */     private String userAgent = Global.CONFIG.http_client_user_agent();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     private int maxTotal = Global.CONFIG.http_client_connections_maxTotal();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     private int defaultMaxPerRoute = Global.CONFIG.http_client_connections_maxPerRoute();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     private Timeout connectTimeout = Global.CONFIG.http_client_connectTimeout();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     private Timeout responseTimeout = Global.CONFIG.http_client_responseTimeout();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     private Timeout connectionRequestTimeout = Global.CONFIG.http_client_connectionRequestTimeout();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     private Timeout connectionKeepAliveTimeout = Global.CONFIG.http_client_connectionKeepAliveTimeout();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     private Timeout evictIdleConnectionTimeout = Global.CONFIG.http_client_evictIdleConnectionTimeout();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     private Timeout validateAfterInactivityTimeout = Global.CONFIG.http_client_validateAfterInactivityTimeout();
/*     */ 
/*     */     
/* 277 */     private Optional<PoolingHttpClientConnectionManager> manager = Optional.empty();
/*     */     
/* 279 */     private HttpRequestInterceptor loggerInterceptor = PjeInterceptor.NOTHING;
/*     */     
/*     */     public Builder<T> setUserAgent(String userAgent) {
/* 282 */       this.userAgent = Args.requireText(userAgent, "userAgent is empty/null");
/* 283 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> setConnectTimeout(Timeout timeout) {
/* 287 */       this.connectTimeout = (Timeout)Args.requireNonNull(timeout, "timeout is null");
/* 288 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> setResponseTimeout(Timeout timeout) {
/* 292 */       this.responseTimeout = (Timeout)Args.requireNonNull(timeout, "timeout is null");
/* 293 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> setLoggerInterceptor(HttpRequestInterceptor interceptor) {
/* 297 */       this.loggerInterceptor = (HttpRequestInterceptor)Args.requireNonNull(interceptor, "interceptor is null");
/* 298 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> setConnectionKeepAlive(Timeout timeout) {
/* 302 */       this.connectionKeepAliveTimeout = (Timeout)Args.requireNonNull(timeout, "timeout is null");
/* 303 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> setConnectionRequestTimeout(Timeout timeout) {
/* 307 */       this.connectionRequestTimeout = (Timeout)Args.requireNonNull(timeout, "timeout is null");
/* 308 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> setEvictIdleConnectionTimeout(Timeout timeout) {
/* 312 */       this.evictIdleConnectionTimeout = (Timeout)Args.requireNonNull(timeout, "timeout is null");
/* 313 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> setConnectionManager(PoolingHttpClientConnectionManager manager) {
/* 317 */       this.manager = Optional.of(Args.requireNonNull(manager, "manager is null"));
/* 318 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected PjeWebCodec newInstance(CloseableHttpClient client, final HttpClientConnectionManager manager) {
/* 323 */       return new PjeWebCodec(client)
/*     */         {
/*     */           public void doClose() throws IOException {
/* 326 */             manager.close();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final PjeWebCodec build() {
/* 338 */       ConnectionConfig connectionConfigPool = ConnectionConfig.custom().setConnectTimeout(this.connectTimeout).setTimeToLive((TimeValue)this.connectionKeepAliveTimeout).setValidateAfterInactivity((TimeValue)this.validateAfterInactivityTimeout).build();
/*     */       
/* 340 */       PoolingHttpClientConnectionManager connManager = this.manager.orElseGet(() -> PoolingHttpClientConnectionManagerBuilder.create().build());
/* 341 */       connManager.setMaxTotal(this.maxTotal);
/* 342 */       connManager.setDefaultMaxPerRoute(this.defaultMaxPerRoute);
/* 343 */       connManager.setDefaultConnectionConfig(connectionConfigPool);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 352 */       RequestConfig requestConfigClient = RequestConfig.custom().setResponseTimeout(this.responseTimeout).setConnectionKeepAlive((TimeValue)this.connectionKeepAliveTimeout).setConnectionRequestTimeout(this.connectionRequestTimeout).setCookieSpec("ignore").build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 363 */       CloseableHttpClient client = HttpClients.custom().setUserAgent(this.userAgent).addRequestInterceptorLast(this.loggerInterceptor).setConnectionManager((HttpClientConnectionManager)connManager).setRoutePlanner((HttpRoutePlanner)new SystemDefaultRoutePlanner(ProxySelector.getDefault())).setConnectionManagerShared(false).evictExpiredConnections().disableRedirectHandling().evictIdleConnections((TimeValue)this.evictIdleConnectionTimeout).setDefaultRequestConfig(requestConfigClient).build();
/*     */       
/* 365 */       return newInstance(client, (HttpClientConnectionManager)connManager);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeWebCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */